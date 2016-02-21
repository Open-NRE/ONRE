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
		return "(" + this.argument + OnreConst.DELIMETER_EXTR + this.relation + OnreConst.DELIMETER_EXTR
		        + this.quantity + OnreConst.DELIMETER_EXTR + this.unit + OnreConst.DELIMETER_EXTR + this.changeType
		        + OnreConst.DELIMETER_EXTR + this.temporal + ")";
	}
}
