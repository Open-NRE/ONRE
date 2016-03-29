/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.iitd.cse.open_nre.onre.comparators.OnreComparator_PatternNode_Index;
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
	    switch(patternNode_configured.word){
	    	case "{rel}": onreExtraction.relation = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{q_value}": onreExtraction.quantity_value = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{q_modifier}": onreExtraction.quantity_modifier = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{q_unit}": onreExtraction.quantity_unit = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    	case "{arg}": onreExtraction.argument = new OnreExtractionPart(subTreeNode.word, subTreeNode.index); break;
	    }
	}
	
	public static OnrePatternNode findPatternSubTree(OnrePatternNode patternNode_sentence, 
			OnrePatternNode patternNode_configured, OnreExtraction onreExtraction) {

		if (patternNode_sentence.isEqualTo(patternNode_configured) 
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
    	
		if (patternNode_sentence.isEqualTo(patternNode_configured)) 
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
		
		expandArgument(onreExtraction, patternNode_sentence);
	}

	public static void expandArgument(OnreExtraction onreExtraction, OnrePatternNode patternNode_sentence) {
		OnrePatternNode argument = OnreUtils.searchNodeInTree(onreExtraction.argument, patternNode_sentence);
		
	    List<OnrePatternNode> expansions_argument = new ArrayList<>();
		expansions_argument.add(argument);
		for(OnrePatternNode child : argument.children) {
			if(child.dependencyLabel.equals("amod")) expansions_argument.add(child);
			if(child.dependencyLabel.equals("nn")) expansions_argument.add(child);
		}
		
		Collections.sort(expansions_argument, new OnreComparator_PatternNode_Index());
		StringBuilder sb = new StringBuilder("");
		for (OnrePatternNode expansion : expansions_argument) {
			sb.append(expansion.word + " ");
        }
		
		onreExtraction.argument.text = sb.toString().trim();
    }
	
	public static void onreExtraction_dummyForNull(OnreExtraction onreExtraction) {
		if(onreExtraction.quantity_modifier == null) onreExtraction.quantity_modifier = new OnreExtractionPart();
		if(onreExtraction.quantity_unit == null) onreExtraction.quantity_unit = new OnreExtractionPart();
	}
	
	
}
