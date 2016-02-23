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
	public OnreExtractionPart	quantity;
	public OnreExtractionPart	unit;
	public OnreExtractionPart	changeType;
	public OnreExtractionPart	temporal;
	
	public OnreExtraction(OnreExtractionPart argument, OnreExtractionPart relation, OnreExtractionPart quantity,
	        OnreExtractionPart unit) {
		this.argument = argument;
		this.relation = relation;
		this.quantity = quantity;
		this.unit = unit;
	}

	public String toString() {
		return "(" + this.argument + OnreConst.DELIMETER_EXTR + this.relation + OnreConst.DELIMETER_EXTR
		        + this.quantity + OnreConst.DELIMETER_EXTR + this.unit + OnreConst.DELIMETER_EXTR + this.changeType
		        + OnreConst.DELIMETER_EXTR + this.temporal + ")";
	}
}
