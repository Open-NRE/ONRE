/**
 * 
 */
package edu.iitd.cse.open_nre.onre.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author harinder
 *
 */
public enum OnreExtractionPartType {

	ARGUMENT("{arg}"),
	RELATION("{rel}"),
	//RELATION_JOINT("{rel_joint}"),
	QUANTITY_UNIT("{q_unit}"),
	QUANTITY_UNIT_PLUS("{q_unit_plus}"),
	QUANTITY_UNIT_OBJTYPE("{q_unit_objType}"),
	QUANTITY_VALUE("{q_value}"),
	QUANTITY_MODIFIER("{q_modifier}"),
	UNKNOWN("{unknown}");
	
	public String text;
	public String value;
	private static Map<String, OnreExtractionPartType> typeMap;
	
	private OnreExtractionPartType(String text) {
	  this.text = text;
    }
	
	public static OnreExtractionPartType getType(String text) {
		if(typeMap==null) populateTypeMap();
		
		OnreExtractionPartType type = typeMap.get(text);
		if(type==null) return UNKNOWN;
		return type;
	}

	private static void populateTypeMap() {
	    typeMap = new HashMap<>();
	    for (OnreExtractionPartType extractionPartType : OnreExtractionPartType.values()) {
	        typeMap.put(extractionPartType.text, extractionPartType);
	    }
    }
}
