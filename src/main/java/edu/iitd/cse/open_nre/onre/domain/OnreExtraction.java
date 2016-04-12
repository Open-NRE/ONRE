/**
 * 
 */
package edu.iitd.cse.open_nre.onre.domain;

import edu.iitd.cse.open_nre.onre.constants.OnreConst;

/**
 * @author harinder
 *
 */
public class OnreExtraction {
	
	public OnreExtractionPart	argument;
	public OnreExtractionPart	relation;
	//public OnreExtractionPart	relation_joint; //TODO: removed for now - remove completely if not required
	public OnreExtractionPart	quantity_value;
	public OnreExtractionPart	quantity_modifier;
	public OnreExtractionPart	quantity_unit;
	//public OnreExtractionPart	quantity_unit_objType; //TODO: removed for now - remove completely if not required
	public OnreExtractionPart	quantity_unit_plus;
	public OnreExtractionPart	changeType;
	public OnreExtractionPart	temporal;

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
		
		return "(" 
				+ this.argument 
				+ OnreConst.DELIMETER_EXTR 
				//+ this.relation_joint + " " 
				+ this.relation.text
				+ OnreConst.DELIMETER_EXTR
		        + this.quantity_value + " " + this.quantity_modifier
		        + OnreConst.DELIMETER_EXTR
		        + this.quantity_unit + " " + this.quantity_unit_plus
		        + OnreConst.DELIMETER_EXTR
		        + this.changeType
		        + OnreConst.DELIMETER_EXTR
		        + this.temporal
		        + ")";
	}
}
