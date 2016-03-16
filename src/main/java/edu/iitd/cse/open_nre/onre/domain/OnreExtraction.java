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
	public OnreExtractionPart	quantity_value;
	public OnreExtractionPart	quantity_modifier;
	public OnreExtractionPart	quantity_unit;
	public OnreExtractionPart	changeType;
	public OnreExtractionPart	temporal;

	public OnreExtraction() {
	}

	public OnreExtraction(OnreExtractionPart argument, OnreExtractionPart relation, OnreExtractionPart quantity_value,
	        OnreExtractionPart quantity_modifier, OnreExtractionPart unit) {
		this.argument = argument;
		this.relation = relation;
		this.quantity_value = quantity_value;
		this.quantity_value = quantity_modifier;
		this.quantity_unit = unit;
	}

	public String toString() {
		return "(" + this.argument + OnreConst.DELIMETER_EXTR + this.relation + OnreConst.DELIMETER_EXTR
		        + this.quantity_value + " " + this.quantity_modifier + OnreConst.DELIMETER_EXTR + this.quantity_unit + OnreConst.DELIMETER_EXTR
		        + this.changeType + OnreConst.DELIMETER_EXTR + this.temporal + ")";
	}
}
