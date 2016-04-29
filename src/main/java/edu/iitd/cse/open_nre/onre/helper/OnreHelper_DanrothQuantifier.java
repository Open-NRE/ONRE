/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.util.List;

import edu.iitd.cse.open_nre.onre.OnreGlobals;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.illinois.cs.cogcomp.quant.driver.QuantSpan;
import edu.illinois.cs.cogcomp.quant.driver.Quantifier;
import edu.illinois.cs.cogcomp.quant.standardize.Quantity;

/**
 * @author harinder
 *
 */
public class OnreHelper_DanrothQuantifier {
	
	/*private static Double getQuantityValue(QuantSpan quantSpan) {
		String phraseSplit[] = getQuantityPhrase(quantSpan).split(" ");
		Double value = 0.0;;
		
		for(int i=0; i<phraseSplit.length; i++) {
			String valueStr = phraseSplit[i];
			
			try {
				value = Double.valueOf(valueStr);
			} catch(Exception e){
				value = 0.0;
			}
			
			if(value!=0.0) break;
		}
		
		return value;
	}*/
	
	/*private static String getQuantityPhrase(QuantSpan quantSpan) {
		return ((Quantity)(quantSpan.object)).phrase;
	}*/
	
	/*private static Map<Double, String> getPhraseMap(String text) {
		Map<Double, String> map_quantifiers_phrase = new HashMap<Double, String>();
		
		List<QuantSpan> quantSpans = getQuantitiesDanroth(text);
		
		for (QuantSpan quantSpan : quantSpans) {
			if(!(quantSpan.object instanceof Quantity)) continue;
			map_quantifiers_phrase.put(getQuantityValue(quantSpan), getQuantityPhrase(quantSpan));
		}
		
		return map_quantifiers_phrase;
	}*/
	
	private static List<QuantSpan> getQuantitiesDanroth(String text) {
		Quantifier quantifier = new Quantifier();
		return quantifier.getSpans(text, true);
	}

	public static String getQuantity(OnrePatternNode subTreeNode) {
		List<QuantSpan> quantSpans = getQuantitiesDanroth(OnreGlobals.sentence);
		for (QuantSpan quantSpan : quantSpans) {
			
			Object quantObject = quantSpan.object;
			if(!(quantObject instanceof Quantity)) continue;
			
			String phrase = ((Quantity)quantObject).phrase;
			
			if(subTreeNode.offset >= quantSpan.start && subTreeNode.offset <= quantSpan.end) return phrase;
			if(phrase.toLowerCase().contains(subTreeNode.word.toLowerCase())) return phrase;
		}
		
		return null;
	}
}
