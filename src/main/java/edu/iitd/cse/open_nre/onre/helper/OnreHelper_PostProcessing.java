/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import edu.iitd.cse.open_nre.onre.OnreGlobals;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils_string;

/**
 * @author swarna
 *
 */
public class OnreHelper_PostProcessing {
	
	public static OnreExtraction onreExtraction_postProcessing(OnrePatternNode patternNode_sentence, OnreExtraction onreExtraction, OnrePatternNode patternNode_configured) throws IOException {
		//if(!OnreGlobals.arg_onre_isSeedFact) OnreHelper_expansions.expandExtraction(onreExtraction, patternNode_sentence);
		OnreHelper_expansions.expandExtraction(onreExtraction, patternNode_sentence);

		/*===DIDN'T WORK===if(onreExtraction.quantity!=null && OnreUtils_string.ignoreCaseContainsPhrase(onreExtraction.argument.text, OnreHelper_DanrothQuantifier.getPhraseExceptValue(onreExtraction.quantity.text))) {
				onreExtraction.quantity.text = OnreHelper_DanrothQuantifier.getValueFromPhrase(onreExtraction.quantity.text);
				onreExtraction.q_unit = "";
		}*/
		
		if(OnreGlobals.arg_onre_isSeedFact) return onreExtraction;
		
        if(postProcessingHelper_isValueInArgOrRel(onreExtraction)) return null; 				//TODO: IMPORTANT-CHANGE #4:Don't extract if quantity value is present in the argument or relation
        if(postProcessingHelper_isUnitInArg(onreExtraction)) return null;   	   				//TODO: IMPORTANT-CHANGE #5:Don't extract if quantity unit is present in the argument
        if(postProcessingHelper_isArgWordInQuantity(onreExtraction)) return null;
    	
        if(postProcessingHelper_isAdditionalInfoAlreadyPresent(onreExtraction)) return null;		
    	
    	postProcessingHelper_numberOf(onreExtraction); 											//TODO: IMPORTANT-CHANGE #7:use [number of] if the relation phrase is exactly same as unit - have only value in the quantity part
    	
    	postProcessingHelper_improveRelations(onreExtraction, patternNode_configured);
    	
    	postProcessingHelper_removePossession(onreExtraction);
    	
    	//replaceDoubleSpaces(onreExtraction);
    	return onreExtraction;
	}
	
	public static void postProcessingHelper_improveRelations(OnreExtraction onreExtraction, OnrePatternNode patternNode_configured) throws IOException {
		boolean isNounRelation = false, isAdjectiveOrAdverbRelation = false;
		
		Queue<OnrePatternNode> q_yetToExpand = new LinkedList<OnrePatternNode>();
		q_yetToExpand.add(patternNode_configured);
		while(!q_yetToExpand.isEmpty()) {
			OnrePatternNode currNode = q_yetToExpand.remove();
			if(currNode.word.equals("{rel}") && currNode.posTag.equalsIgnoreCase("nnp|nn")) { isNounRelation = true;}
			if(currNode.word.equals("{rel}") && currNode.posTag.equalsIgnoreCase("JJ|RB")) { isAdjectiveOrAdverbRelation = true; }
			for(OnrePatternNode child : currNode.children) {
				 q_yetToExpand.add(child);
			}
		}
		
		appendHasHaveAndPrep(onreExtraction, isNounRelation, isAdjectiveOrAdverbRelation);
		
		if(isAdjectiveOrAdverbRelation) {
			replaceRelationWithNounForm(onreExtraction);
		}
	}
	
	private static void appendHasHaveAndPrep(OnreExtraction onreExtraction, boolean isNounRelation, boolean isAdjectiveOrAdverbRelation) {
		if(isNounRelation || isAdjectiveOrAdverbRelation) {
			if(OnreGlobals.isSentenceInPastTense) {
				onreExtraction.relation.text = "had " + onreExtraction.relation.text;
			}
			else {
				if(isPluralArgument(onreExtraction)) {
					onreExtraction.relation.text = "have " + onreExtraction.relation.text;
				}
				else {
					onreExtraction.relation.text = "has " + onreExtraction.relation.text;
				}
			}
			
			if(OnreGlobals.expandedOnPrep == null && !onreExtraction.relation.text.endsWith("of") && !onreExtraction.relation.text.contains("[number of]")) {
				onreExtraction.relation.text = onreExtraction.relation.text + " of";
			}
			
			if(OnreGlobals.negatedWord != null) {
				onreExtraction.relation.text = OnreGlobals.negatedWord + " " + onreExtraction.relation.text;
			}
			
			if(OnreGlobals.auxVerb != null) {
				onreExtraction.relation.text = OnreGlobals.auxVerb + " " + onreExtraction.relation.text;
			}
		}
		
		if(OnreGlobals.expandedOnPrep != null) {
			onreExtraction.relation.text = onreExtraction.relation.text.trim() + " " + OnreGlobals.expandedOnPrep;
		}
	}
	
	private static void replaceRelationWithNounForm(OnreExtraction onreExtraction) throws IOException {
		String nounForm = OnreHelper_WordNet.getWhoseAttributeIsWord(onreExtraction.relation_headWord.text);
		
		if(onreExtraction.relation_headWord.posTag.equalsIgnoreCase("JJ")) {
			if(nounForm == null) {
				nounForm = OnreHelper_WordNet.getDerivationallyRelatedNounWord(onreExtraction.relation_headWord.text, 1);
			}
		}
		else if(onreExtraction.relation_headWord.posTag.equalsIgnoreCase("RB")) {
			if(nounForm == null) {
				nounForm = OnreHelper_WordNet.getDerivationallyRelatedNounWord(onreExtraction.relation_headWord.text, 2);
			}
		}
		
		if(nounForm != null) onreExtraction.relation.text = onreExtraction.relation.text.replaceAll(onreExtraction.relation_headWord.text, nounForm);
	}
	
	private static boolean isPluralArgument(OnreExtraction onreExtraction) {
		if(onreExtraction.argument_headWord.text.endsWith("s") 
				&& !onreExtraction.argument_headWord.posTag.equals("PRP$") 
				&& !OnreGlobals.hasPronounsList.contains(onreExtraction.argument_headWord.text.toLowerCase()) 
				&& !OnreGlobals.havePronounsList.contains(onreExtraction.argument_headWord.text.toLowerCase())) return true;
		
		if(OnreGlobals.havePronounsList.contains(onreExtraction.argument_headWord.text.toLowerCase())) return true;
		
		if(OnreGlobals.isMatchingWithBe) return true;
		
		if(OnreGlobals.plural_possessive_pronounsTOpronouns.containsKey(onreExtraction.argument.text.toLowerCase())) {
			return true;
		}
		
		if(OnreGlobals.auxVerbsList.contains(OnreGlobals.auxVerb)) return true;
		
		return false;
	}
	
	private static void postProcessingHelper_removePossession(OnreExtraction onreExtraction) {
		if(onreExtraction.argument.text.endsWith("'s")) {
			onreExtraction.argument.text = onreExtraction.argument.text.substring(0, onreExtraction.argument.text.length() - 3);
		}
		if(onreExtraction.argument.text.endsWith("'")) {
			onreExtraction.argument.text = onreExtraction.argument.text.substring(0, onreExtraction.argument.text.length() - 2);
		}
		
		if(OnreGlobals.singular_possessive_pronounsTOpronouns.containsKey(onreExtraction.argument.text.toLowerCase())) {
			onreExtraction.argument.text = OnreGlobals.singular_possessive_pronounsTOpronouns.get(onreExtraction.argument.text.toLowerCase());
		}
		
		if(OnreGlobals.plural_possessive_pronounsTOpronouns.containsKey(onreExtraction.argument.text.toLowerCase())) {
			onreExtraction.argument.text = OnreGlobals.plural_possessive_pronounsTOpronouns.get(onreExtraction.argument.text.toLowerCase());
		}
	}

	/*private static void replaceDoubleSpaces(OnreExtraction onreExtraction) {
		onreExtraction.argument.text = onreExtraction.argument.text.replaceAll("  ", " ");
    	onreExtraction.relation.text = onreExtraction.relation.text.replaceAll("  ", " ");
    	if(onreExtraction.quantity_unit_plus!=null) onreExtraction.quantity_unit_plus.text = onreExtraction.quantity_unit_plus.text.replaceAll("  ", " ");
    	if(onreExtraction.quantity!=null) onreExtraction.quantity.text = onreExtraction.quantity.text.replaceAll("  ", " ");
	}*/

	private static boolean postProcessingHelper_isAdditionalInfoAlreadyPresent(OnreExtraction onreExtraction) {
		removeAdditionalInfoIfAlreadyPresent(onreExtraction);									
        //if(postProcessingHelper_isAdditionalInfoStillPresent(onreExtraction)) return true;
        return false;
	}

	private static void removeAdditionalInfoIfAlreadyPresent(OnreExtraction onreExtraction) {
		if(onreExtraction.additional_info == null) return;
		if(OnreUtils_string.isIgnoreCaseContainsPhrase(onreExtraction.quantity.text, onreExtraction.additional_info.text)) {onreExtraction.additional_info = null; return;}
		if(OnreUtils_string.isIgnoreCaseContainsPhrase(onreExtraction.relation.text, onreExtraction.additional_info.text)) {onreExtraction.additional_info = null; return;}
		if(OnreUtils_string.isIgnoreCaseContainsPhrase(onreExtraction.argument.text, onreExtraction.additional_info.text)) {onreExtraction.additional_info = null; return;}
	}

	private static boolean postProcessingHelper_isAdditionalInfoStillPresent(OnreExtraction onreExtraction) {
		if(onreExtraction.additional_info == null) return false;
		if(OnreUtils_string.isIgnoreCaseContainsPhrase(onreExtraction.additional_info.text, onreExtraction.argument_headWord.text)) return true;
		if(OnreUtils_string.isIgnoreCaseContainsPhrase(onreExtraction.additional_info.text, onreExtraction.relation_headWord.text)) return true;

		return false;
	}

	private static void postProcessingHelper_numberOf(OnreExtraction onreExtraction) {
		if(onreExtraction.q_unit==null || onreExtraction.q_unit.isEmpty()) return;
		
        //if(onreExtraction.relation.text.equalsIgnoreCase(onreExtraction.q_unit)) {
		if(OnreUtils_string.isIgnoreCaseContainsPhrase(onreExtraction.relation.text, onreExtraction.q_unit)) {
        	//onreExtraction.quantity.text = onreExtraction.quantity.text.replaceAll("(?i)"+onreExtraction.relation.text, "").trim();
			onreExtraction.quantity.text = onreExtraction.quantity.text.replaceAll("(?i)"+onreExtraction.q_unit, "").trim();
        	
			if(onreExtraction.relation_headWord.posTag.matches("(?i)NNP?S")
					|| onreExtraction.quantity.posTag.matches("(?i)NNP?S")
					|| onreExtraction.relation_headWord.text.endsWith("s")
					|| onreExtraction.q_unit.endsWith("s")
					) //adding [number of] only in case of plural noun - rel headword or q_unit
        		onreExtraction.relation.text = "[number of] " + onreExtraction.relation.text;
        }
	}

	private static boolean postProcessingHelper_isUnitInArg(OnreExtraction onreExtraction) {
		if(onreExtraction.q_unit == null || onreExtraction.q_unit.isEmpty()) return false;

		if(OnreUtils_string.isIgnoreCaseContainsPhrase(onreExtraction.argument.text, onreExtraction.q_unit)) return true;
		if(OnreUtils_string.isIgnoreCaseContainsPhrase(onreExtraction.q_unit, onreExtraction.argument_headWord.text)) return true;
   		
		return false;
	}

	private static boolean postProcessingHelper_isValueInArgOrRel(OnreExtraction onreExtraction) {
		if(!OnreUtils.quantityExists(onreExtraction)) return false;
		
		String q_value = OnreHelper_DanrothQuantifier.getValueFromPhrase(onreExtraction.quantity.text);
		if(q_value == null) return false;
      
		//if(onreExtraction.argument.text.contains(q_value)) return true;
       	//if(onreExtraction.relation.text.contains(q_value)) return true;
		
		if(OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContains(onreExtraction.argument.text, q_value)) return true;
		if(OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContains(onreExtraction.relation.text, q_value)) return true;
		
		return false;
	}
	
	private static boolean postProcessingHelper_isArgWordInQuantity(OnreExtraction onreExtraction) {
		if(!OnreUtils.quantityExists(onreExtraction)) return false;
		String []argWords = onreExtraction.argument.text.split(" ");
		String []quantWords = onreExtraction.quantity.text.split(" ");
		
		if(argWords.length == 1) {
			String argWord = argWords[0];
			for(String quantWord : quantWords) {
				if(quantWord.equalsIgnoreCase(argWord)) {
					return true;
				}
			}
		}
		return false;
	}
}
