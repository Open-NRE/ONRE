/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.iitd.cse.open_nre.onre.comparators.OnreComparator_PatternNode_Index;
import edu.iitd.cse.open_nre.onre.constants.OnreExtractionPartType;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnreExtractionPart;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils;

/**
 * @author harinder
 *
 */
public class OnreHelper {
	
    private static void setExtractionPart(OnrePatternNode subTreeNode, OnrePatternNode patternNode_configured,
            OnreExtraction onreExtraction) {
	    switch(OnreExtractionPartType.getType(patternNode_configured.word)) {
	    	
	    	case ARGUMENT: onreExtraction.argument = new OnreExtractionPart(subTreeNode.word, subTreeNode.index);  break;
	    	//case RELATION_JOINT: onreExtraction.relation_joint = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case RELATION: onreExtraction.relation = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case QUANTITY: setQuantityExtractionPart(subTreeNode, onreExtraction); break; //TODO: trying for Danroth's quantifier
	    	//case QUANTITY_UNIT: onreExtraction.quantity_unit = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	//case QUANTITY_UNIT_OBJTYPE: onreExtraction.quantity_unit_objType = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break; 
	    	//case QUANTITY_MODIFIER: onreExtraction.quantity_modifier = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	//case QUANTITY_VALUE: onreExtraction.quantity_value = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break; //TODO: shall commit this & remove q_value from everywhere
	    	//case QUANTITY_UNIT_PLUS: onreExtraction.quantity_unit_plus = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
			case UNKNOWN: break;
	    		
	    	/*case "{rel}": onreExtraction.relation = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{q_value}": onreExtraction.quantity_value = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{q_modifier}": onreExtraction.quantity_modifier = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{q_unit}": onreExtraction.quantity_unit = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{arg}": onreExtraction.argument = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;*/
	    }
	}
    
    private static void setQuantityExtractionPart(OnrePatternNode subTreeNode, OnreExtraction onreExtraction) {
    	String quantity = OnreHelper_DanrothQuantifier.getQuantity(subTreeNode);
    	if(quantity == null) return;
    	
    	onreExtraction.quantity = new OnreExtractionPart(quantity);
    	if(!quantity.contains("per cent") && !quantity.contains("percent")) return;
    	
    	//finding percent node
    	OnrePatternNode node_percent = null;
    	node_percent = OnreUtils.searchNodeInTreeByText("percent", subTreeNode);
    	if(node_percent == null) node_percent = OnreUtils.searchNodeInTreeByText("cent", subTreeNode);
    	if(node_percent == null) return;
    	
    	onreExtraction.quantity_percent = new OnreExtractionPart(node_percent.word, node_percent.index);
    }
	
	public static OnrePatternNode findPatternSubTree(OnrePatternNode patternNode_sentence, 
			OnrePatternNode patternNode_configured, OnreExtraction onreExtraction) {

		if (patternNode_sentence.matches(patternNode_configured) 
				&& matchChildren(patternNode_sentence, patternNode_configured, onreExtraction)) {
			
			setExtractionPart(patternNode_sentence, patternNode_configured, onreExtraction);
			return patternNode_sentence;
		}

    	OnrePatternNode result = null;
    	
    	for (OnrePatternNode child : patternNode_sentence.children) {
    		result = findPatternSubTree(child, patternNode_configured, onreExtraction);
    		if (result != null && matchChildren(patternNode_sentence, result, onreExtraction)) return result;
    	}

    	return result;
    }
	
	private static boolean matchChildren(OnrePatternNode patternNode_sentence, 
			OnrePatternNode patternNode_configured, OnreExtraction onreExtraction) {
    	
		if (patternNode_sentence.matches(patternNode_configured)) 
			setExtractionPart(patternNode_sentence, patternNode_configured, onreExtraction);
		else return false;
		
    	if (patternNode_sentence.children.size() < patternNode_configured.children.size()) return false;

    	boolean result = true;
    	int index_sentence = 0;

    	for (int index_config = 0; index_config < patternNode_configured.children.size(); index_config++) {

    		// Skip non-matching children in the tree.
    		while (index_sentence < patternNode_sentence.children.size()
    		      && !(result = matchChildren(patternNode_sentence.children.get(index_sentence), 
    		    		  patternNode_configured.children.get(index_config), onreExtraction))) {
    			index_sentence++;
    		}

    		if (!result) return result;
    	}

    	return result;
    }
	
	public static void expandExtraction(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		if(onreExtraction.relation != null) expandRelation(onreExtraction, patternNode_sentence);
		else onreExtraction.relation = new OnreExtractionPart();
		
		expandArgument(onreExtraction, patternNode_sentence);
		
		//if(onreExtraction.quantity_unit_objType != null) expandUnitObjType(onreExtraction, patternNode_sentence);
		if(onreExtraction.quantity_percent != null) expandQuantity_percent(onreExtraction, patternNode_sentence);
		//if(onreExtraction.quantity_unit != null) expandUnit(onreExtraction, patternNode_sentence); //TODO: I think it's not required after using Danroth's quantifier..might be required?
	}
	
	/*private static void expandUnit(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		OnrePatternNode node_relation = OnreUtils.searchNodeInTree(onreExtraction.quantity_unit, patternNode_sentence);
		
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
		
		onreExtraction.quantity_unit.text = sb.toString().trim();
    }*/
	
	private static void expandQuantity_percent(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {

		OnrePatternNode quantity_percent = OnreUtils.searchNodeInTreeByIndex(onreExtraction.quantity_percent, patternNode_sentence);
		
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
				//if(child.dependencyLabel.equals("amod")) { expansions.add(child); q_yetToExpand.add(child); }
				//if(child.dependencyLabel.equals("nn")) { expansions.add(child); q_yetToExpand.add(child); }
				
				//if(child.dependencyLabel.equals("prep") && child.word.equals("of")) { expansions.add(child); q_yetToExpand.add(child); } 
				//if(child.dependencyLabel.equals("pobj") && currNode.word.equals("in")) { expansions.add(child); q_yetToExpand.add(child); }
			}
		}

		//sorting the expansions & setting in the argument
		Collections.sort(expansions, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions) {
			sb.append(expansion.word + " ");
        }
		
		onreExtraction.quantity_unit_plus = new OnreExtractionPart(sb.toString().trim(), node_prepOf.index); 
    }
	
	private static void expandRelation(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		OnrePatternNode node_relation = OnreUtils.searchNodeInTreeByIndex(onreExtraction.relation, patternNode_sentence);
		
	    List<OnrePatternNode> expansions = new ArrayList<>();
	    expansions.add(node_relation);
		for(OnrePatternNode child : node_relation.children) {
			if(child.dependencyLabel.equals("amod")) expansions.add(child); 
			if(child.dependencyLabel.equals("nn")) expansions.add(child); 
			if(child.dependencyLabel.equals("advmod")) expansions.add(child); 
			if(child.dependencyLabel.equals("hmod")) expansions.add(child); 
		}
		
		Collections.sort(expansions, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions) {
			sb.append(expansion.word + " ");
        }
		
		onreExtraction.relation.text = sb.toString().trim();
		
		
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
		OnrePatternNode node_argument = OnreUtils.searchNodeInTreeByIndex(onreExtraction.argument, patternNode_sentence);
		
	    List<OnrePatternNode> expansions = new ArrayList<>();
		expansions.add(node_argument);
		
		Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
		q_yetToExpand.add(node_argument);
		while(!q_yetToExpand.isEmpty()) {
			OnrePatternNode currNode = q_yetToExpand.remove();
			
			for(OnrePatternNode child : currNode.children) {
				if(child.dependencyLabel.equals("det")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("num")) { expansions.add(child); q_yetToExpand.add(child); }
				
				if(child.dependencyLabel.equals("amod")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("hmod")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("nn")) { expansions.add(child); q_yetToExpand.add(child); }
				
				if(child.dependencyLabel.equals("prep") && child.word.equals("of")) { expansions.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("pobj") && currNode.word.equals("of")) { expansions.add(child); q_yetToExpand.add(child); }
				
				//if(child.dependencyLabel.equals("prep") && child.word.equals("in")) { expansions_argument.add(child); q_yetToExpand.add(child); } //TODO: not expanding on 'in'
				//if(child.dependencyLabel.equals("pobj") && currNode.word.equals("in")) { expansions_argument.add(child); q_yetToExpand.add(child); }
			}
		}

		//sorting the expansions & setting in the argument
		Collections.sort(expansions, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions) {
			sb.append(expansion.word + " ");
        }
		
		onreExtraction.argument.text = sb.toString().trim();
    }
	
	public static void onreExtraction_dummyForNull(OnreExtraction onreExtraction) {
		//TODO: 
		//if(onreExtraction.quantity_modifier == null) onreExtraction.quantity_modifier = new OnreExtractionPart();
		//if(onreExtraction.quantity_unit == null) onreExtraction.quantity_unit = new OnreExtractionPart();
		
		if(onreExtraction.quantity_unit_plus == null) onreExtraction.quantity_unit_plus = new OnreExtractionPart();
		
		//if(onreExtraction.relation_joint == null) onreExtraction.relation_joint = new OnreExtractionPart();
	}
	
}
