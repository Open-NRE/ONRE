/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iitd.cse.open_nre.onre.OnreGlobals;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils;
import edu.illinois.cs.cogcomp.quant.driver.QuantSpan;
import edu.illinois.cs.cogcomp.quant.driver.Quantifier;
import edu.illinois.cs.cogcomp.quant.standardize.Quantity;

/**
 * @author harinder
 *
 */
public class OnreHelper_DanrothQuantifier {
	
	private static Double getQuantityValue(QuantSpan quantSpan) {
		/*String phraseSplit[] = getQuantityPhrase(quantSpan).split(" ");
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
		
		return value;*/
		
		return ((Quantity)(quantSpan.object)).value;
	}
	
	private static String getQuantityPhrase(QuantSpan quantSpan) {
		return ((Quantity)(quantSpan.object)).phrase;
	}
	
	private static String getQuantityUnit(QuantSpan quantSpan) {
		return ((Quantity)(quantSpan.object)).units;
	}
	
	private static String getValueFromPhrase(QuantSpan quantSpan) {
		String phrase = getQuantityPhrase(quantSpan);
		
		String[] phraseSplit = phrase.split(" "); //TODO: need tokenization?
		for (String word : phraseSplit) {
			if(OnreUtils.isNumber(word)) return word;
		}
		
		return null;
	}
	
	private static String getUnitFromPhrase(QuantSpan quantSpan, String unit) {
		if(unit.split(" ").length>1) return null; //TODO: ignoring multiwords unit as of now
		
		String phrase = getQuantityPhrase(quantSpan);

		String[] phraseSplit = phrase.split(" "); //TODO: need tokenization?
		for (String word : phraseSplit) {
			if(unit.equalsIgnoreCase("us$")) {
				if(word.equalsIgnoreCase("dollar")) return "dollar";
				if(word.contains("$")) return "$";
				if(word.equalsIgnoreCase("cents")) return "cents";
			}
			
			if(unit.equalsIgnoreCase("percent")) {
				if(word.contains("%")) return "%";
			}
			
			if(word.equalsIgnoreCase(unit)) return unit;
		}
		
		return null;
	}
	
	public static Map<Double, String> getValueMap(String text) {
		Map<Double, String> map_quantifiers_value = new HashMap<Double, String>();
		
		List<QuantSpan> quantSpans = getQuantitiesDanroth(text);
		
		for (QuantSpan quantSpan : quantSpans) {
			if(!(quantSpan.object instanceof Quantity)) continue;
			map_quantifiers_value.put(getQuantityValue(quantSpan), getValueFromPhrase(quantSpan));
		}
		
		return map_quantifiers_value;
	}
	
	public static Map<String, String> getUnitMap(String text) {
		Map<String, String> map_quantifiers_unit = new HashMap<String, String>();
		
		List<QuantSpan> quantSpans = getQuantitiesDanroth(text);
		
		for (QuantSpan quantSpan : quantSpans) {
			if(!(quantSpan.object instanceof Quantity)) continue;
			String unit = getQuantityUnit(quantSpan);
			map_quantifiers_unit.put(OnreUtils.lowerTrim(unit), OnreUtils.lowerTrim(getUnitFromPhrase(quantSpan, unit)));
		}
		
		return map_quantifiers_unit;
	}
	
	private static List<QuantSpan> getQuantitiesDanroth(String text) {
		List<QuantSpan> quantSpans = null;
		//try{
			Quantifier quantifier = new Quantifier();
			quantSpans = quantifier.getSpans(text, true);
		//} catch(Exception e) {
			//System.out.println("Exception in Danroth - continuing");
		//}
		return quantSpans;
	}

	public static Object getQuantity(OnrePatternNode subTreeNode) {
		List<QuantSpan> quantSpans = getQuantitiesDanroth(OnreGlobals.sentence);
		for (QuantSpan quantSpan : quantSpans) {
			
			Object quantObject = quantSpan.object;
			if(!(quantObject instanceof Quantity)) continue;
			
			//String phrase = ((Quantity)quantObject).phrase;
			//String phrase = ((Quantity)quantObject).bound + " " + 
			//((Quantity)quantObject).value + " " + ((Quantity)quantObject).units;
			
			//if(phrase.toLowerCase().contains(subTreeNode.word.toLowerCase())) return phrase;
			if(subTreeNode.offset >= quantSpan.start && subTreeNode.offset <= quantSpan.end) return quantObject;
		}
		
		for (QuantSpan quantSpan : quantSpans) {
			
			Object quantObject = quantSpan.object;
			if(!(quantObject instanceof Quantity)) continue;
			
			String phrase = ((Quantity)quantObject).phrase;
			//String phrase = ((Quantity)quantObject).bound + " " + 
			//((Quantity)quantObject).value + " " + ((Quantity)quantObject).units;
			
			if(phrase.toLowerCase().contains(subTreeNode.word.toLowerCase())) return quantObject;
			//if(subTreeNode.offset >= quantSpan.start && subTreeNode.offset <= quantSpan.end) return phrase;
		}
		
		return null;
	}
}
