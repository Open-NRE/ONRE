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
	
	public String sentence;
	public Integer	patternNumber;
	
	public OnreExtractionPart	argument;
	public OnreExtractionPart	relation;
	public OnreExtractionPart	quantity;
	public OnreExtractionPart	additional_info;
	
	public OnreExtractionPart	quantity_unit_plus; 
	
	public OnreExtractionPart	quantity_percent;
	
	public OnreExtractionPart	argument_headWord;
	public OnreExtractionPart	relation_headWord;
	public String q_unit;


	public OnreExtraction() {
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.argument);
		sb.append(OnreConstants.DELIMETER_EXTR);
		sb.append(this.relation);
		sb.append(OnreConstants.DELIMETER_EXTR);
		sb.append(this.quantity);
		if(this.quantity_unit_plus!=null) sb.append(" ").append(this.quantity_unit_plus);
		if(this.additional_info!=null && this.additional_info.text!=null && !this.additional_info.text.isEmpty()) {
			sb.append(OnreConstants.DELIMETER_EXTR);
			sb.append(this.additional_info);
		}
		sb.append(")");
		
		return sb.toString();
	}
}
