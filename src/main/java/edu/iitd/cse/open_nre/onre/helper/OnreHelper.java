/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.iitd.cse.open_nre.onre.OnreGlobals;
import edu.iitd.cse.open_nre.onre.comparators.OnreComparator_PatternNode_Index;
import edu.iitd.cse.open_nre.onre.constants.OnreExtractionPartType;
import edu.iitd.cse.open_nre.onre.constants.OnreFilePaths;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnreExtractionPart;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternTree;
import edu.iitd.cse.open_nre.onre.domain.Onre_dsDanrothSpan;
import edu.iitd.cse.open_nre.onre.domain.Onre_dsDanrothSpans;
import edu.iitd.cse.open_nre.onre.utils.OnreIO;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils_string;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils_tree;

/**
 * @author harinder
 *
 */
public class OnreHelper {
	
    private static void setExtractionPart(OnrePatternTree onrePatternTree, OnrePatternNode subTreeNode, OnrePatternNode patternNode_configured, OnreExtraction onreExtraction, Onre_dsDanrothSpans danrothSpans) {

    	switch(OnreExtractionPartType.getType(patternNode_configured.word)) {
	    	case ARGUMENT: 
	    		onreExtraction.argument_headWord = new OnreExtractionPart(subTreeNode.word, subTreeNode.index);
	    		onreExtraction.argument = new OnreExtractionPart(subTreeNode.word, subTreeNode.index);
	    		break;	    	
	    	case RELATION: 
	    		onreExtraction.relation_headWord = new OnreExtractionPart(subTreeNode.word, subTreeNode.index);
	    		onreExtraction.relation = new OnreExtractionPart(subTreeNode.word, subTreeNode.index);
	    		break;
	    	case QUANTITY: 
	    		setQuantityExtractionPart(onrePatternTree, subTreeNode, onreExtraction, subTreeNode.index, danrothSpans); 
	    		break;
	    	case UNKNOWN: 
	    		break;
	    }
	}
    
    /*private static String addNegationIfPossibleToQuantity(OnreExtraction onreExtraction, OnrePatternTree onrePatternTree, String quantityPhrase) {
    	OnrePatternNode quantity_parent_node = OnreUtils_tree.searchParentOfNodeInTreeByIndex(onreExtraction.quantity, onrePatternTree.root);
    	if(quantity_parent_node==null) return quantityPhrase;
		
    	String negationWord = "";
		for(OnrePatternNode child : quantity_parent_node.children) {
			if(child.dependencyLabel.equals("neg") ) {
				negationWord = child.word;
				break;
			}
		}
		
		if(!negationWord.isEmpty())
			return negationWord + " " + quantityPhrase;
		else return quantityPhrase;
    }*/
    
    private static void setQuantityExtractionPart(OnrePatternTree onrePatternTree, OnrePatternNode subTreeNode, OnreExtraction onreExtraction, int index, Onre_dsDanrothSpans danrothSpans) {
    	Onre_dsDanrothSpan danrothSpan = OnreHelper_DanrothQuantifier.getQuantity(subTreeNode, danrothSpans);
    	if(danrothSpan == null) return;
    	
    	String quantityPhrase = danrothSpan.phrase;
    	if(quantityPhrase == null) return;
    	
    	if(OnreGlobals.arg_onre_isSeedFact) { //saving value and unit separately in case we want to generate a seed fact
    		onreExtraction.quantity = new OnreExtractionPart(danrothSpan.value.toString());
    		onreExtraction.additional_info = new OnreExtractionPart(danrothSpan.unit);
    		return;
    	}
    	
    	onreExtraction.q_unit =danrothSpan.unit;
    	
    	onreExtraction.quantity = new OnreExtractionPart(quantityPhrase);
    	onreExtraction.quantity.index = index;
    	
    	//TODO: IMPORTANT-CHANGE: Add sibling of quantity if that sibling is connected by "neg" depLabel
    	/*String newQuantityPhrase = addNegationIfPossibleToQuantity(onreExtraction, onrePatternTree, quantityPhrase);
    	
    	if(!newQuantityPhrase.equals(quantityPhrase)) {
    		onreExtraction.quantity.text = newQuantityPhrase;	
    	}*/
    	
    	
    	if(!quantityPhrase.contains("per cent") && !quantityPhrase.contains("percent") && !quantityPhrase.contains("%")) return;
    	
    	//finding percent node
    	OnrePatternNode node_percent = null;
    	node_percent = OnreUtils_tree.searchNodeInTreeByText("percent", subTreeNode);
    	if(node_percent == null) node_percent = OnreUtils_tree.searchNodeInTreeByText("cent", subTreeNode);
    	if(node_percent == null) node_percent = OnreUtils_tree.searchNodeInTreeByText("%", subTreeNode);
    	if(node_percent == null) return;
    	
    	onreExtraction.quantity_percent = new OnreExtractionPart(node_percent.word, node_percent.index);
    }
	
	public static OnrePatternNode findPatternSubTree(OnrePatternTree onrePatternTree, OnrePatternNode patternNode_sentence, 
			OnrePatternNode patternNode_configured, OnreExtraction onreExtraction, Onre_dsDanrothSpans danrothSpans) {

		if (patternNode_sentence.matches(patternNode_configured) 
				&& matchChildren(onrePatternTree, patternNode_sentence, patternNode_configured, onreExtraction, danrothSpans)) {
			
			setExtractionPart(onrePatternTree, patternNode_sentence, patternNode_configured, onreExtraction, danrothSpans);
			return patternNode_sentence;
		}

    	OnrePatternNode result = null;
    	
    	for (OnrePatternNode child : patternNode_sentence.children) {
    		result = findPatternSubTree(onrePatternTree, child, patternNode_configured, onreExtraction, danrothSpans);
    		if (result != null && matchChildren(onrePatternTree, patternNode_sentence, result, onreExtraction, danrothSpans)) return result;
    	}

    	return result;
    }
	
	private static boolean matchChildren(OnrePatternTree onrePatternTree, OnrePatternNode patternNode_sentence, 
			OnrePatternNode patternNode_configured, OnreExtraction onreExtraction, Onre_dsDanrothSpans danrothSpans) {
    	
		if (patternNode_sentence.matches(patternNode_configured)) 
			setExtractionPart(onrePatternTree, patternNode_sentence, patternNode_configured, onreExtraction, danrothSpans);
		else return false;
		
    	if (patternNode_sentence.children.size() < patternNode_configured.children.size()) return false;

    	boolean result = true;
    	int index_sentence = 0;

    	for (int index_config = 0; index_config < patternNode_configured.children.size(); index_config++) {

    		// Skip non-matching children in the tree.
    		while (index_sentence < patternNode_sentence.children.size()
    		      && !(result = matchChildren(onrePatternTree, patternNode_sentence.children.get(index_sentence), 
    		    		  patternNode_configured.children.get(index_config), onreExtraction, danrothSpans))) {
    			index_sentence++;
    		}

    		if (!result) return result;
    	}

    	return result;
    }
	
	private static void expandExtraction(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) throws IOException {
		if(onreExtraction.relation != null) expandRelation(onreExtraction, patternNode_sentence);
		else onreExtraction.relation = new OnreExtractionPart();
		
		expandArgument(onreExtraction, patternNode_sentence);
		if(OnreUtils.quantityExists(onreExtraction)) expandQuantity_settingAdditionalInfo(onreExtraction,patternNode_sentence);
		if(onreExtraction.quantity_percent != null) expandQuantity_percent(onreExtraction, patternNode_sentence);
	}
	
	private static void expandQuantity_percent(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {

		OnrePatternNode quantity_percent = OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.quantity_percent, patternNode_sentence);
		
		OnrePatternNode node_prepOf = null;
		for(OnrePatternNode child : quantity_percent.children) {
			if(child.dependencyLabel.equals("prep") && child.word.equals("of")) node_prepOf = child;
		}
		
		if(node_prepOf == null) return;
		
	    List<OnrePatternNode> expansions = new ArrayList<>();
	    expansions.add(node_prepOf);
		
		Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
		q_yetToExpand.add(node_prepOf);
		while(!q_yetToExpand.isEmpty()) {
			OnrePatternNode currNode = q_yetToExpand.remove();
			
			for(OnrePatternNode child : currNode.children) {
				expansions.add(child); q_yetToExpand.add(child);
			}
		}

		//sorting the expansions & setting in the argument
		Collections.sort(expansions, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions) {
			sb.append(expansion.word + " ");
        }
		
		String quantity_unit_plus = sb.toString().trim();
		
		// If upon expansion, we include already included text - ignore
		if(isAlreadyPresent(onreExtraction, quantity_unit_plus, 2)) return;
		
		onreExtraction.quantity_unit_plus = new OnreExtractionPart(quantity_unit_plus, node_prepOf.index); 
    }
	
	private static void expandQuantity_settingAdditionalInfo(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		
		OnrePatternNode quantity_parent_node = OnreUtils_tree.searchParentOfNodeInTreeByIndex(onreExtraction.quantity, patternNode_sentence);
		if(quantity_parent_node==null) return;
		
		OnrePatternNode node_prep = null;
		for(OnrePatternNode child : quantity_parent_node.children) {
			if(child.dependencyLabel.equals("prep") || child.word.equalsIgnoreCase("prior")) node_prep = child;
			if(node_prep == null) continue;
			
			List<OnrePatternNode> expansions = new ArrayList<>();
			expansions.add(node_prep);
			
			Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
			q_yetToExpand.add(node_prep);
			while(!q_yetToExpand.isEmpty()) {
				OnrePatternNode currNode = q_yetToExpand.remove();
				
				for(OnrePatternNode child1 : currNode.children) {
					//if(currNode.word.equals(",")) {q_yetToExpand.clear(); break;}
					expansions.add(child1); q_yetToExpand.add(child1);
				}
			}
			
			//sorting the expansions & setting in the argument
			Collections.sort(expansions, new OnreComparator_PatternNode_Index());
			StringBuilder sb_additional_info = new StringBuilder("");
			for (OnrePatternNode expansion : expansions) {
				sb_additional_info.append(expansion.word + " ");
	        }
			
			String additional_info = sb_additional_info.toString().trim();
			
			// If upon expansion, we include already included text - ignore
			if(isAlreadyPresent(onreExtraction, additional_info, 2)) return;
			
			/*int posOfComma = quantity_unit_plus.indexOf(',');
			if(posOfComma != -1) {
				quantity_unit_plus = quantity_unit_plus.substring(0,posOfComma-1);
			}*/
			
			onreExtraction.additional_info = new OnreExtractionPart(additional_info, node_prep.index);
			break;
		}
		
	}
	
	private static void expandRelation(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) throws IOException {
		OnrePatternNode node_relation = OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.relation, patternNode_sentence);
		
	    List<OnrePatternNode> expansions = new ArrayList<>();
	    expansions.add(node_relation);
		/*for(OnrePatternNode child : node_relation.children) {
			if(child.dependencyLabel.equals("amod")) expansions.add(child); 
			if(child.dependencyLabel.equals("nn")) expansions.add(child); 
			if(child.dependencyLabel.equals("advmod")) expansions.add(child); 
			if(child.dependencyLabel.equals("hmod")) expansions.add(child); 
		}*/
	    Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
	    q_yetToExpand.add(node_relation);
	    while(!q_yetToExpand.isEmpty())
	    {
	    	OnrePatternNode currNode = q_yetToExpand.remove();
	    	for(OnrePatternNode child : currNode.children) {
				//if(child.dependencyLabel.equals("amod")) {expansions.add(child); q_yetToExpand.add(child); } 
				if(child.dependencyLabel.equals("nn")) {expansions.add(child); q_yetToExpand.add(child); } 
				if(child.dependencyLabel.equals("neg")) {expansions.add(child); q_yetToExpand.add(child); }//TODO: IMPORTANT-CHANGE: negation handling
				//if(child.dependencyLabel.equals("advmod")) {expansions.add(child); q_yetToExpand.add(child); } 
				//if(child.dependencyLabel.equals("hmod")) {expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.matches(".*mod")) { expansions.add(child); q_yetToExpand.add(child); }
			}
	    }
	    
	    //TODO: IMPORTANT-CHANGE #6: ===START=== :if the relation word is one of configured words(grew/increased/down), then expand it on prep
	    List<String> expandOnPrep = OnreIO.readFile_classPath(OnreFilePaths.filePath_expandOnPrep);
		if(expandOnPrep.contains(node_relation.word)) {
			for(OnrePatternNode child : node_relation.children) {
				if(!child.dependencyLabel.equals("prep")) continue;
				if(OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.quantity, child) == null) continue;
				expansions.add(child); 
			}
		}
		//TODO: IMPORTANT-CHANGE #6: ===END=== :if the relation word is one of configured words(grew/increased/down), then expand it on prep
		
		Collections.sort(expansions, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions) {
			sb.append(expansion.word + " ");
        }
		
		String str = sb.toString().trim();
		// If upon expansion, we include already included text - ignore
		if(isAlreadyPresent(onreExtraction, str, 1)) return;
		
		onreExtraction.relation.text = str;
		
		
		/*OnrePatternNode node_relation = OnreUtils.searchNodeInTree(onreExtraction.relation, patternNode_sentence);
		
	    List<OnrePatternNode> expansions = new ArrayList<>();
	    expansions.add(node_relation);
		
		Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
		q_yetToExpand.add(node_relation);
		while(!q_yetToExpand.isEmpty()) {
			OnrePatternNode currNode = q_yetToExpand.remove();
			
			for(OnrePatternNode child : currNode.children) {
				if(child.dependencyLabel.equals("amod")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("nn")) { expansions.add(child); q_yetToExpand.add(child); }
				//if(child.dependencyLabel.equals("prep")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("advmod")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("hmod")) { expansions.add(child); q_yetToExpand.add(child); }
				
				//if(child.dependencyLabel.equals("prep") && child.word.equals("in")) { expansions.add(child); q_yetToExpand.add(child); } 
				//if(child.dependencyLabel.equals("pobj") && currNode.word.equals("in")) { expansions.add(child); q_yetToExpand.add(child); }
			}
		}

		//sorting the expansions & setting in the argument
		Collections.sort(expansions, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions) {
			sb.append(expansion.word + " ");
        }
		
		onreExtraction.relation.text = sb.toString().trim();*/
    }

	private static boolean isAlreadyPresent(OnreExtraction onreExtraction,	String str, int type) {
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
	
	/*private static void expandUnitObjType(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		OnrePatternNode node_relation = OnreUtils.searchNodeInTree(onreExtraction.quantity_unit_objType, patternNode_sentence);
		
	    List<OnrePatternNode> expansions = new ArrayList<>();
		expansions.add(node_relation);
		for(OnrePatternNode child : node_relation.children) {
			if(child.dependencyLabel.equals("amod")) expansions.add(child);
			if(child.dependencyLabel.equals("hmod")) expansions.add(child);
		}
		
		Collections.sort(expansions, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions) {
			sb.append(expansion.word + " ");
        }
		
		onreExtraction.quantity_unit_objType.text = sb.toString().trim();
    }*/

	private static void expandArgument(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		OnrePatternNode node_argument = OnreUtils_tree.searchNodeInTreeByIndex(onreExtraction.argument, patternNode_sentence);
		
	    List<OnrePatternNode> expansions = new ArrayList<>();
		expansions.add(node_argument);
		
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
				
				/*if(child.dependencyLabel.equals("amod")) { expansions.add(child); q_yetToExpand.add(child); } //TO-DO: changing to .*mod
				if(child.dependencyLabel.equals("hmod")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("npadvmod")) { expansions.add(child); q_yetToExpand.add(child); }*/
				
				if(child.dependencyLabel.matches(".*mod")) { expansions.add(child); q_yetToExpand.add(child); }
				
				if(child.dependencyLabel.equals("nn")) { expansions.add(child); q_yetToExpand.add(child); }
				
				if(child.dependencyLabel.equals("prep")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("pobj")) { expansions.add(child); q_yetToExpand.add(child); }
				
				//if(child.dependencyLabel.equals("prep") && child.word.equals("in")) { expansions_argument.add(child); q_yetToExpand.add(child); } //TO-DO: not expanding just on 'in'
				//if(child.dependencyLabel.equals("pobj") && currNode.word.equals("in")) { expansions_argument.add(child); q_yetToExpand.add(child); }
			}
		}

		//sorting the expansions & setting in the argument
		Collections.sort(expansions, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions) {
			sb.append(expansion.word + " ");
        }
		
		String str = sb.toString().trim();
		// If upon expansion, we include already included text - ignore
		if(isAlreadyPresent(onreExtraction, str, 0)) return;
		
		onreExtraction.argument.text = str;
    }
	
	private static void onreExtraction_dummyForNull(OnreExtraction onreExtraction) {
		//TO-DO: not required 
		//if(onreExtraction.quantity_modifier == null) onreExtraction.quantity_modifier = new OnreExtractionPart();
		//if(onreExtraction.quantity_unit == null) onreExtraction.quantity_unit = new OnreExtractionPart();
		
		//if(onreExtraction.quantity_unit_plus == null) onreExtraction.quantity_unit_plus = new OnreExtractionPart();
		//if(onreExtraction.additional_info == null) onreExtraction.additional_info = new OnreExtractionPart();//not required
		
		//if(onreExtraction.relation_joint == null) onreExtraction.relation_joint = new OnreExtractionPart();
	}
	
	public static OnreExtraction onreExtraction_postProcessing(OnrePatternNode patternNode_sentence, OnreExtraction onreExtraction) throws IOException {
		if(!OnreGlobals.arg_onre_isSeedFact) expandExtraction(onreExtraction, patternNode_sentence);
    	onreExtraction_dummyForNull(onreExtraction);
    	
		//TODO: IMPORTANT-CHANGE #8: ===START=== :additional_info not already present in argument or relation : checking headwords
    	if(onreExtraction.additional_info == null) return onreExtraction;
    	if(onreExtraction.additional_info.text.contains(onreExtraction.argument_headWord.text)) return null;
    	if(onreExtraction.additional_info.text.contains(onreExtraction.relation_headWord.text)) return null;
		//TODO: IMPORTANT-CHANGE #8: ===END=== :additional_info not already present in argument or relation : checking headwords
    	
    	return onreExtraction;
	}
	
}
