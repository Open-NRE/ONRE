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
	    	case QUANTITY_UNIT: onreExtraction.quantity_unit = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case QUANTITY_UNIT_OBJTYPE: onreExtraction.quantity_unit_objType = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break; 
	    	case QUANTITY_MODIFIER: onreExtraction.quantity_modifier = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case QUANTITY_VALUE: onreExtraction.quantity_value = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
			case UNKNOWN: break;
	    		
	    	/*case "{rel}": onreExtraction.relation = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{q_value}": onreExtraction.quantity_value = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{q_modifier}": onreExtraction.quantity_modifier = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{q_unit}": onreExtraction.quantity_unit = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{arg}": onreExtraction.argument = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;*/
	    }
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
		//TODO: expandExtraction | to be implemented

		//TODO: imp-I feel we need to keep expanding(from children) unless not possible
		
		if(onreExtraction.relation != null) expandRelation(onreExtraction, patternNode_sentence);
		else onreExtraction.relation = new OnreExtractionPart();
		
		expandArgument(onreExtraction, patternNode_sentence);
		if(onreExtraction.quantity_unit_objType != null) expandUnitObjType(onreExtraction, patternNode_sentence);
	}
	
	private static void expandRelation(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		OnrePatternNode node_relation = OnreUtils.searchNodeInTree(onreExtraction.relation, patternNode_sentence);
		
	    List<OnrePatternNode> expansions_relation = new ArrayList<>();
		expansions_relation.add(node_relation);
		for(OnrePatternNode child : node_relation.children) {
			if(child.dependencyLabel.equals("amod")) expansions_relation.add(child);
			if(child.dependencyLabel.equals("nn")) expansions_relation.add(child);
		}
		
		Collections.sort(expansions_relation, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions_relation) {
			sb.append(expansion.word + " ");
        }
		
		onreExtraction.relation.text = sb.toString().trim();
    }
	
	private static void expandUnitObjType(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		OnrePatternNode node_relation = OnreUtils.searchNodeInTree(onreExtraction.quantity_unit_objType, patternNode_sentence);
		
	    List<OnrePatternNode> expansions_relation = new ArrayList<>();
		expansions_relation.add(node_relation);
		for(OnrePatternNode child : node_relation.children) {
			if(child.dependencyLabel.equals("amod")) expansions_relation.add(child);
		}
		
		Collections.sort(expansions_relation, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions_relation) {
			sb.append(expansion.word + " ");
        }
		
		onreExtraction.quantity_unit_objType.text = sb.toString().trim();
    }

	private static void expandArgument(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		OnrePatternNode node_argument = OnreUtils.searchNodeInTree(onreExtraction.argument, patternNode_sentence);
		
	    List<OnrePatternNode> expansions_argument = new ArrayList<>();
		expansions_argument.add(node_argument);
		
		Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
		q_yetToExpand.add(node_argument);
		while(!q_yetToExpand.isEmpty()) {
			OnrePatternNode currNode = q_yetToExpand.remove();
			
			for(OnrePatternNode child : currNode.children) {
				if(child.dependencyLabel.equals("amod")) { expansions_argument.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("nn")) { expansions_argument.add(child); q_yetToExpand.add(child); }
				
				if(child.dependencyLabel.equals("prep") && child.word.equals("of")) { expansions_argument.add(child); q_yetToExpand.add(child); }
				if(child.dependencyLabel.equals("pobj") && currNode.word.equals("of")) { expansions_argument.add(child); q_yetToExpand.add(child); }
				
				//if(child.dependencyLabel.equals("prep") && child.word.equals("in")) { expansions_argument.add(child); q_yetToExpand.add(child); } //TODO: not expanding on 'in'
				//if(child.dependencyLabel.equals("pobj") && currNode.word.equals("in")) { expansions_argument.add(child); q_yetToExpand.add(child); }
			}
		}

		//sorting the expansions & setting in the argument
		Collections.sort(expansions_argument, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions_argument) {
			sb.append(expansion.word + " ");
        }
		
		onreExtraction.argument.text = sb.toString().trim();
    }
	
	public static void onreExtraction_dummyForNull(OnreExtraction onreExtraction) {
		//TODO: 
		if(onreExtraction.quantity_modifier == null) onreExtraction.quantity_modifier = new OnreExtractionPart();
		if(onreExtraction.quantity_unit == null) onreExtraction.quantity_unit = new OnreExtractionPart();
		
		//if(onreExtraction.relation_joint == null) onreExtraction.relation_joint = new OnreExtractionPart();
	}
	
	
}
