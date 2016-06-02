/**
 * 
 */
package edu.iitd.cse.open_nre.onre.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.iitd.cse.open_nre.onre.OnreGlobals;
import edu.iitd.cse.open_nre.onre.constants.OnreConstants;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternTree;
import edu.iitd.cse.open_nre.onre.domain.Onre_dsDanrothSpans;
import edu.iitd.cse.open_nre.onre.helper.MayIHelpYou;
import edu.iitd.cse.open_nre.onre.helper.OnreHelper_DanrothQuantifier;
import edu.iitd.cse.open_nre.onre.helper.OnreHelper_json;
import edu.iitd.cse.open_nre.onre.utils.OnreIO;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils;


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
		Onre_runMe.setArguments(args);
		
		File folder = new File(args[1]);
		
		Set<String> files = new TreeSet<>();
		OnreUtils.listFilesForFolder(folder, files);
		
		List<OnreExtraction> extrs_all = new ArrayList<OnreExtraction>();
		for (String file : files) {
			if(!file.endsWith(OnreConstants.SUFFIX_JSON_STRINGS)) continue; //only jsonSuffix files are required
			System.out.println("----------------------------------running file: " + file);
			
			List<String> inputJsonStrings_patternTree = OnreIO.readFile(file);
			List<Onre_dsDanrothSpans> listOfDanrothSpans = OnreHelper_DanrothQuantifier.getListOfDanrothSpans(file.replaceAll("_jsonStrings", ""));
			
			for(int i=0;i<inputJsonStrings_patternTree.size();i++) {
				//if(!OnreGlobals.arg_isSeedFact) System.out.println("::" + (i+1));
				OnrePatternTree onrePatternTree = OnreHelper_json.getOnrePatternTree(inputJsonStrings_patternTree.get(i));
				//DependencyGraph depGraph = Onre_runMe.getDepGraph();
				
				List<OnreExtraction> extrs = MayIHelpYou.runMe(onrePatternTree, listOfDanrothSpans.get(i));
				
				if(!OnreGlobals.arg_isSeedFact) {
					for (OnreExtraction onreExtraction : extrs) {
						System.out.println(onreExtraction.sentence);
						System.out.println(onreExtraction.patternNumber);
						System.out.println(onreExtraction);
					}
				}
				
				extrs_all.addAll(extrs);
			}
		}
		
		if(OnreGlobals.arg_isSeedFact) OnreIO.writeFile("data/out_facts_new", extrs_all);
			
		
		
		//String filePath_input = "data/0000tw";

		
		System.out.println("==============Done===========");
	}

}
