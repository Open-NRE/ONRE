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
	public static boolean arg_onre_isSeedFact = false;
	public static boolean isSubjectSingular = false;
	public static boolean isSentenceInPastTense = false;
	public static String negatedWord = null;
	public static String auxVerb = null;
	
	//arguments-onreDS
	public static Onre_dsRunType arg_onreds_runType;
	public static String arg_onreds_path_inputFolder;
	public static String arg_onreds_path_facts;
	public static double arg_onreds_partialMatchingThresholdPercent;
	
	public static void resetGlobals() {
		isSubjectSingular = false;
		isSentenceInPastTense = false;
		negatedWord = null;
		auxVerb = null;
	}
}
