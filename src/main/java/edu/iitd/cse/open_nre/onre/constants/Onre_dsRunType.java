/**
 * 
 */
package edu.iitd.cse.open_nre.onre.constants;

import java.util.HashMap;
import java.util.Map;

public enum Onre_dsRunType {
	TYPE1("type1"),//no value comparison, assuming single word unit
	DEFAULT("default"); //value comparison (partial matching), multiwords unit allowed
	
	public String text;
	private static Map<String, Onre_dsRunType> typeMap;
	
	private Onre_dsRunType(String text) {
	  this.text = text;
    }
	
	public static Onre_dsRunType getType(String text) {
		if(typeMap==null) populateTypeMap();
		
		Onre_dsRunType type = typeMap.get(text);
		if(type==null) return DEFAULT;
		return type;
	}

	private static void populateTypeMap() {
	    typeMap = new HashMap<>();
	    for (Onre_dsRunType runType : Onre_dsRunType.values()) {
	        typeMap.put(runType.text, runType);
	    }
    }
}
