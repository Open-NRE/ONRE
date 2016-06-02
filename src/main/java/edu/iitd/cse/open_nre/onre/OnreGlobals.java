/**
 * 
 */
package edu.iitd.cse.open_nre.onre;

import com.google.gson.Gson;

import edu.iitd.cse.open_nre.onre.constants.Onre_dsRunType;

/**
 * @author harinder
 *
 */
public class OnreGlobals {
	
	public static String sentence;
	
	public static Gson gson; //note: don't use it directly...call OnreHelper_json.getGson() instead
	
	//arguments-onre
	public static boolean arg_isSeedFact = false;
	
	//arguments-onreDS
	public static Onre_dsRunType arg_runType = Onre_dsRunType.DEFAULT;
}
