/**
 * 
 */
package edu.iitd.cse.open_nre.onre.domain;

import edu.iitd.cse.open_nre.onre.constants.OnreConstants;

/**
 * @author harinder
 *
 */
public class OnreExtraction {
	
	public OnreExtractionPart	argument;
	public OnreExtractionPart	relation;
	//public OnreExtractionPart	relation_joint;
	//public OnreExtractionPart	quantity_value;
	//public OnreExtractionPart	quantity_modifier;
	//public OnreExtractionPart	quantity_unit;
	//public OnreExtractionPart	quantity_unit_objType;
	public OnreExtractionPart	quantity_unit_plus; 
	//public OnreExtractionPart	changeType;
	//public OnreExtractionPart	temporal;
	
	public OnreExtractionPart	quantity; //for using Danroth's quantifier
	
	public OnreExtractionPart	quantity_percent;
	
	public OnreExtractionPart	extra_quantity_info; /*giving extra info relating to the quantity*/
	
	public Integer	patternNumber;
	public String sentence;
	
	//public String q_value; //TO-DO: IMPORTANT-CHANGE:Don't extract if quantity value is present in the argument or relation
	public String q_unit; //TODO: IMPORTANT-CHANGE:Don't extract if quantity unit is present in the argument

	public OnreExtraction() {
	}

	/*public OnreExtraction(OnreExtractionPart argument, OnreExtractionPart relation, OnreExtractionPart relation_joint, OnreExtractionPart quantity_value,
	        OnreExtractionPart quantity_modifier, OnreExtractionPart unit) {
		this.argument = argument;
		this.relation = relation;
		//this.relation_joint = relation_joint;
		this.quantity_value = quantity_value;
		this.quantity_modifier = quantity_modifier;
		this.quantity_unit = unit;
	}*/

	public String toString() {
		//String relText = relation.text;
		//if(quantity_unit_objType != null) {
		//	relText = "[number of]" + " " + quantity_unit_objType.text + " " + relation.text;
		//}
		
		/*return "(" 
				+ this.argument 
				+ OnreConst.DELIMETER_EXTR 
				//+ this.relation_joint + " " 
				+ this.relation.text
				+ OnreConst.DELIMETER_EXTR
		        + this.quantity_value + " " + this.quantity_modifier
		        + OnreConst.DELIMETER_EXTR
		        + this.quantity_unit 
		        //+ " " + this.quantity_unit_plus
		        + OnreConst.DELIMETER_EXTR
		        + this.changeType
		        + OnreConst.DELIMETER_EXTR
		        + this.temporal
		        + ")";*/
		
		return "(" 
		+ this.argument 
		+ OnreConstants.DELIMETER_EXTR 
		+ this.relation.text
		+ OnreConstants.DELIMETER_EXTR
        + this.quantity
        + this.quantity_unit_plus
        + OnreConstants.DELIMETER_EXTR
        + this.extra_quantity_info
        /*+ OnreConstants.DELIMETER_EXTR
        + this.changeType
        + OnreConstants.DELIMETER_EXTR
        + this.temporal*/
        + ")";
	}
}
