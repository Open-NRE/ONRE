/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnreExtractionPart;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;

/**
 * @author harinder
 *
 */
public class OnreHelper {
	
	public static OnreExtraction createExtractionFromSubTree(OnrePatternNode subTree, OnrePatternNode patternNode_configured) {
		OnreExtraction onreExtraction = new OnreExtraction();
		createExtractionFromSubTree_helper(subTree, patternNode_configured, onreExtraction);
		return onreExtraction;
	}
	
	private static void createExtractionFromSubTree_helper(OnrePatternNode subTreeNode, OnrePatternNode patternNode_configured, OnreExtraction onreExtraction) {
		if(subTreeNode.isEqualTo(patternNode_configured)) {
			switch_extraction(subTreeNode, patternNode_configured, onreExtraction);
		}
		
		createExtractionFromSubTree_helper(subTreeNode, patternNode_configured, onreExtraction);
		
		int index_sentence=0, index_configured=0;
		while(index_configured<patternNode_configured.children.size()) {
			OnrePatternNode child_sentence = subTreeNode.children.get(index_sentence);
			OnrePatternNode child_configured = patternNode_configured.children.get(index_configured);
			
			if(!child_sentence.isEqualTo(child_configured)) { index_sentence++; continue; }
			switch_extraction(child_configured, child_sentence, onreExtraction);
			
		}
	}

	/**
	 * @param subTreeNode
	 * @param patternNode_configured
	 * @param onreExtraction
	 */
    public static void switch_extraction(OnrePatternNode subTreeNode, OnrePatternNode patternNode_configured,
            OnreExtraction onreExtraction) {
	    switch(patternNode_configured.word){
	    	case "{rel}": onreExtraction.relation = new OnreExtractionPart(subTreeNode.word); break;
	    	case "{q_value}": onreExtraction.quantity_value = new OnreExtractionPart(subTreeNode.word); break;
	    	case "{q_modifier}": onreExtraction.quantity_modifier = new OnreExtractionPart(subTreeNode.word); break;
	    	case "{q_unit}": onreExtraction.quantity_unit = new OnreExtractionPart(subTreeNode.word); break;
	    	case "{arg}": onreExtraction.argument = new OnreExtractionPart(subTreeNode.word); break;
	    }
	}
	
	public static OnrePatternNode findPatternSubTree(OnrePatternNode patternNode_sentence, OnrePatternNode patternNode_configured) {

		if (patternNode_sentence.isEqualTo(patternNode_configured) && matchChildren(patternNode_sentence, patternNode_configured)) return patternNode_sentence;

    	OnrePatternNode result = null;
    	
    	for (OnrePatternNode child : patternNode_sentence.children) {
    		result = findPatternSubTree(child, patternNode_configured);
    		if (result != null && matchChildren(patternNode_sentence, result)) return result;
    	}

    	return result;
    }
	
	private static boolean matchChildren(OnrePatternNode patternNode_sentence, OnrePatternNode patternNode_configured) {
    	
		//System.out.println(patternNode_sentence);
		//System.out.println(patternNode_configured);
		//System.out.println();
		if (!patternNode_sentence.isEqualTo(patternNode_configured)) return false;
    	if (patternNode_sentence.children.size() < patternNode_configured.children.size()) return false;

    	boolean result = true;
    	int index_sentence = 0;

    	for (int index_config = 0; index_config < patternNode_configured.children.size(); index_config++) {

    		// Skip non-matching children in the tree.
    		while (index_sentence < patternNode_sentence.children.size()
    		      && !(result = matchChildren(patternNode_sentence.children.get(index_sentence), patternNode_configured.children.get(index_config)))) {
    			index_sentence++;
    		}

    		if (!result) return result;
    	}

    	//System.out.println("returning "+result);
    	return result;
    }
}
