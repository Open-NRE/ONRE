/**
 * 
 */
package edu.iitd.cse.open_nre.onre.runner;

import java.io.IOException;
import java.util.List;

import edu.iitd.cse.open_nre.onre.OnreGlobals;
import edu.iitd.cse.open_nre.onre.constants.OnreConstants;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternTree;
import edu.iitd.cse.open_nre.onre.helper.MayIHelpYou;
import edu.iitd.cse.open_nre.onre.helper.OnreHelper_json;
import edu.iitd.cse.open_nre.onre.utils.OnreIO;


/**
 * @author harinder
 *
 */
public class Onre_runMe_jsonStrings {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String filePath_input = "data/sentences.txt";

		List<String> inputJsonStrings = OnreIO.readFile(filePath_input+OnreConstants.SUFFIX_JSON_STRINGS);
		
		Onre_runMe.setArguments(args);

		for(int i=0;i<inputJsonStrings.size();i++){
			if(!OnreGlobals.arg_isSeedFact) System.out.println("::" + (i+1));
			OnrePatternTree onrePatternTree = OnreHelper_json.getOnrePatternTree(inputJsonStrings.get(i));
			//DependencyGraph depGraph = Onre_runMe.getDepGraph();
			MayIHelpYou.runMe(onrePatternTree);
		}

	}

}
