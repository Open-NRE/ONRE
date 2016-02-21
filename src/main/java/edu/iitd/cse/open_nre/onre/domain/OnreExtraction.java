/**
 * 
 */
package edu.iitd.cse.open_nre.onre.domain;

import edu.iitd.cse.open_nre.onre.constants.OnreChangeType;
import edu.iitd.cse.open_nre.onre.constants.OnreConst;

/**
 * @author harinder
 *
 */
public class OnreExtraction {
	private String	       argument;
	private String	       relation;
	private double	       quantity;
	private String	       unit;
	private OnreChangeType	changeType;
	private String	       temporal;

	public String toString() {
		return "(" + this.argument + OnreConst.EXTR_DELIMETER + this.relation + OnreConst.EXTR_DELIMETER
		        + this.quantity + OnreConst.EXTR_DELIMETER + this.unit + OnreConst.EXTR_DELIMETER + this.changeType
		        + OnreConst.EXTR_DELIMETER + this.temporal + ")";
	}
}
