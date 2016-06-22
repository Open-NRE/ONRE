package edu.iitd.cse.open_nre.onre.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.iitd.cse.open_nre.onre.comparators.OnreComparator_PatternNode_Index;
import edu.iitd.cse.open_nre.onre.constants.OnreFilePaths;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnreExtractionPart;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.utils.OnreIO;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils_string;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils_tree;

public class OnreHelper_expansions {
	
	public static void expandExtraction(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) throws IOException {
		if(onreExtraction.relation != null) expandRelation(onreExtraction, patternNode_sentence);
		else onreExtraction.relation = new OnreExtractionPart();
		
		expandArgument(onreExtraction, patternNode_sentence);
		if(OnreUtils.quantityExists(onreExtraction)) expandQuantity_settingAdditionalInfo(onreExtraction,patternNode_sentence);
		if(onreExtraction.quantity_percent != null) expandQuantity_percent(onreExtraction, patternNode_sentence);
	}
	
	private static void expandArgument(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		OnrePatternNode node_argument = OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.argument, patternNode_sentence);
		
	    List<OnrePatternNode> expansions = new ArrayList<>();
		expansions.add(node_argument);
		
		expandArgumentHelper_basicExpansions(node_argument, expansions);
		expansions = expandArgumentHelper_expandOnPrepSubTree(onreExtraction, expansions); //TODO: IMPORTANT-CHANGE #13: expand on prep if subtree does not have relation/quantity

		String str = expandHelper_sortExpansions_createStr(expansions);
		if(expandHelper_isAlreadyPresent(onreExtraction, str, 0)) return;		// If upon expansion, we include already included text - ignore
		onreExtraction.argument.text = str;
    }

	private static void expandArgumentHelper_basicExpansions(OnrePatternNode node_argument, List<OnrePatternNode> expansions) {
		Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
		q_yetToExpand.add(node_argument);
		while(!q_yetToExpand.isEmpty()) {
			OnrePatternNode currNode = q_yetToExpand.remove();
			
			for(OnrePatternNode child : currNode.children) {
				if(child.dependencyLabel.equals("poss")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("possessive")) { expansions.add(child); q_yetToExpand.add(child); }
				
				if(child.dependencyLabel.equals("det")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("num")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("neg")) { expansions.add(child); q_yetToExpand.add(child); }
				
				if(child.dependencyLabel.matches(".*mod")) { expansions.add(child); q_yetToExpand.add(child); }
				
				if(child.dependencyLabel.equals("nn")) { expansions.add(child); q_yetToExpand.add(child); }
				
				//if(child.dependencyLabel.equals("prep")) { expansions.add(child); q_yetToExpand.add(child); } //TODO: IMPORTANT-CHANGE #13: expand on prep if subtree does not have relation/quantity
				if(child.dependencyLabel.equals("pobj")) { expansions.add(child); q_yetToExpand.add(child); }
			}
		}
	}
	
	private static List<OnrePatternNode> expandArgumentHelper_expandOnPrepSubTree(OnreExtraction onreExtraction, List<OnrePatternNode> expansions) {
		List<OnrePatternNode> expansions_all = new ArrayList<>();
	    expansions_all.addAll(expansions);
	    for (OnrePatternNode onrePatternNode : expansions) {
	    	//if(onrePatternNode.word.equals(onreExtraction.argument_headWord.text)) continue; //not expanding on headWord?
	    	for(OnrePatternNode child : onrePatternNode.children) {
	    		OnrePatternNode node_prep = null;
	    		if(child.dependencyLabel.equals("prep")) node_prep = child;
	    		
	    		if(node_prep == null) continue;
	    		if(OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.quantity, child) != null) continue;
	    		if(OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.relation_headWord, child) != null) continue;
	    		
	    		expansions_all.addAll(expandHelper_expandCompleteSubTree(node_prep));
	    	}
		}
	    expansions = expansions_all;
		return expansions;
	}
	
	private static void expandQuantity_percent(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {

		OnrePatternNode quantity_percent = OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.quantity_percent, patternNode_sentence);
		
		OnrePatternNode node_prepOf = null;
		for(OnrePatternNode child : quantity_percent.children) {
			if(child.dependencyLabel.equals("prep") && child.word.equals("of")) node_prepOf = child;
		}
		
		if(node_prepOf == null) return;
		
	    List<OnrePatternNode> expansions = expandHelper_expandCompleteSubTree(node_prepOf);
	    
		String quantity_unit_plus = expandHelper_sortExpansions_createStr(expansions);
		// If upon expansion, we include already included text - ignore
		if(expandHelper_isAlreadyPresent(onreExtraction, quantity_unit_plus, 2)) return;
		onreExtraction.quantity_unit_plus = new OnreExtractionPart(quantity_unit_plus, node_prepOf.index); 
    }

	private static void expandQuantity_settingAdditionalInfo(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		
		OnrePatternNode quantity_parent_node = OnreUtils_tree.searchParentOfNodeInTreeByIndex(onreExtraction.quantity, patternNode_sentence);
		if(quantity_parent_node==null) return;
		
		for(OnrePatternNode child : quantity_parent_node.children) {
			OnrePatternNode node_prep = null;
			if(child.dependencyLabel.equals("prep") || child.word.equalsIgnoreCase("prior")) node_prep = child;
			if(node_prep == null) continue;
			
			List<OnrePatternNode> expansions = expandHelper_expandCompleteSubTree(node_prep);
			
			String additional_info = expandHelper_sortExpansions_createStr(expansions);
			if(expandHelper_isAlreadyPresent(onreExtraction, additional_info, 2)) return;			// If upon expansion, we include already included text - ignore
			onreExtraction.additional_info = new OnreExtractionPart(additional_info, node_prep.index);
			break;
		}
	}
	
	private static void expandRelation(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) throws IOException {
		OnrePatternNode node_relation = OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.relation, patternNode_sentence);
		
	    List<OnrePatternNode> expansions = new ArrayList<>();
	    expansions.add(node_relation);

	    expandRelationHelper_basicExpansions(node_relation, expansions);
	    expandRelationHelper_expandOnPrepForConfiguredWords(onreExtraction, node_relation, expansions); //TODO: IMPORTANT-CHANGE #6: if the relation word is one of configured words(grew/increased/down), then expand it on prep
	    expansions = expandRelationHelper_expandOnPrepSubTree(onreExtraction, expansions); //TODO: IMPORTANT-CHANGE #12: expand on prep (except headWord) if subtree does not have argument/quantity
	    
		String str = expandHelper_sortExpansions_createStr(expansions);
		if(expandHelper_isAlreadyPresent(onreExtraction, str, 1)) return;		// If upon expansion, we include already included text - ignore
		onreExtraction.relation.text = str;
    }

	private static void expandRelationHelper_basicExpansions(OnrePatternNode node_relation, List<OnrePatternNode> expansions) {
		Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
	    q_yetToExpand.add(node_relation);
	    while(!q_yetToExpand.isEmpty())
	    {
	    	OnrePatternNode currNode = q_yetToExpand.remove();
	    	for(OnrePatternNode child : currNode.children) {
				if(child.dependencyLabel.equals("nn")) {expansions.add(child); q_yetToExpand.add(child); } 
				if(child.dependencyLabel.equals("neg")) {expansions.add(child); q_yetToExpand.add(child); }//TODO: IMPORTANT-CHANGE #10: negation handling
				//if(child.dependencyLabel.equals("advmod")) {expansions.add(child); q_yetToExpand.add(child); } 
				//if(child.dependencyLabel.equals("hmod")) {expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.matches(".*mod")) { expansions.add(child); q_yetToExpand.add(child); }
			}
	    }
	}

	private static void expandRelationHelper_expandOnPrepForConfiguredWords(OnreExtraction onreExtraction, OnrePatternNode node_relation, List<OnrePatternNode> expansions) throws IOException {
		
	    List<String> expandOnPrep = OnreIO.readFile_classPath(OnreFilePaths.filePath_expandOnPrep);
		if(expandOnPrep.contains(node_relation.word)) {
			for(OnrePatternNode child : node_relation.children) {
				if(!child.dependencyLabel.equals("prep")) continue;
				if(OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.quantity, child) == null) continue;
				expansions.add(child); 
			}
		}
	}
	
	private static List<OnrePatternNode> expandRelationHelper_expandOnPrepSubTree(OnreExtraction onreExtraction, List<OnrePatternNode> expansions) {
		List<OnrePatternNode> expansions_all = new ArrayList<>();
	    expansions_all.addAll(expansions);
	    for (OnrePatternNode onrePatternNode : expansions) {
	    	if(onrePatternNode.word.equals(onreExtraction.relation_headWord.text)) continue; //not expanding on headWord?
	    	for(OnrePatternNode child : onrePatternNode.children) {
	    		OnrePatternNode node_prep = null;
	    		if(child.dependencyLabel.equals("prep")) node_prep = child;
	    		
	    		if(node_prep == null) continue;
	    		if(OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.quantity, child) != null) continue;
	    		if(OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.argument_headWord, child) != null) continue;
	    		
	    		expansions_all.addAll(expandHelper_expandCompleteSubTree(node_prep));
	    	}
		}
	    expansions = expansions_all;
		return expansions;
	}

	private static boolean expandHelper_isAlreadyPresent(OnreExtraction onreExtraction,	String str, int type) {
		//here we are checking the complete extractionPart rather than just their headWord, let it be like this only
		switch(type) {
		case 0: // expanding argument
			if(onreExtraction.quantity!=null && OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContains(str, onreExtraction.quantity.text)) return true;
			if(onreExtraction.relation!=null && OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContains(str, onreExtraction.relation.text)) return true;
			break;
		case 1: // expanding relation
			if(onreExtraction.argument!=null && OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContains(str, onreExtraction.argument.text)) return true;
			if(onreExtraction.quantity!=null && OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContains(str, onreExtraction.quantity.text)) return true;
			break;
		case 2: // expanding quantity_percent or setting additional_info
			if(onreExtraction.argument!=null && OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContains(str, onreExtraction.argument.text)) return true;
			if(onreExtraction.relation!=null && OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContains(str, onreExtraction.relation.text)) return true;
			if(onreExtraction.quantity!=null && OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContains(str, onreExtraction.quantity.text)) return true;
			break;
		}
		
		return false;
	}
	
	private static List<OnrePatternNode> expandHelper_expandCompleteSubTree(OnrePatternNode node_prepOf) {
		List<OnrePatternNode> expansions = new ArrayList<>();
	    expansions.add(node_prepOf);
		
		expandHelper_expandCompleteSubTree(node_prepOf, expansions);
		return expansions;
	}

	private static void expandHelper_expandCompleteSubTree(OnrePatternNode nodeToExpand, List<OnrePatternNode> expansions) {
		Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
		q_yetToExpand.add(nodeToExpand);
		while(!q_yetToExpand.isEmpty()) {
			OnrePatternNode currNode = q_yetToExpand.remove();
			
			for(OnrePatternNode child : currNode.children) {
				expansions.add(child); q_yetToExpand.add(child);
			}
		}
	}

	private static String expandHelper_sortExpansions_createStr(List<OnrePatternNode> expansions) {
		Collections.sort(expansions, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions) {
			sb.append(expansion.word + " ");
        }
		return sb.toString().trim();
	}
}
