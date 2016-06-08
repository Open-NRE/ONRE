/**
 * 
 */
package edu.iitd.cse.open_nre.onre.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.iitd.cse.open_nre.onre.OnreGlobals;
import edu.iitd.cse.open_nre.onre.constants.OnreConstants;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternTree;
import edu.iitd.cse.open_nre.onre.domain.Onre_dsDanrothSpans;
import edu.iitd.cse.open_nre.onre.helper.MayIHelpYou;
import edu.iitd.cse.open_nre.onre.helper.OnreHelper_DanrothQuantifier;
import edu.iitd.cse.open_nre.onre.helper.OnreHelper_json;
import edu.iitd.cse.open_nre.onre.helper.OnreHelper_pattern;
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

			List<OnrePatternNode> list_configuredPattern = OnreHelper_pattern.getConfiguredPatterns();
			
			for(int i=0;i<inputJsonStrings_patternTree.size();i++) {
				//if(!OnreGlobals.arg_isSeedFact) System.out.println("::" + (i+1));
				OnrePatternTree onrePatternTree = OnreHelper_json.getOnrePatternTree(inputJsonStrings_patternTree.get(i));
				//DependencyGraph depGraph = Onre_runMe.getDepGraph();
				Map<OnreExtraction, Integer> extrs = MayIHelpYou.runMe(onrePatternTree, listOfDanrothSpans.get(i), list_configuredPattern);
				
				Map<String, Integer> uniq_extrs = new HashMap<String, Integer>();
				for(Map.Entry<OnreExtraction, Integer> entry : extrs.entrySet()) {
					uniq_extrs.put(entry.getKey().toString(), entry.getValue());
				}
				
				if(!OnreGlobals.arg_onre_isSeedFact) {
					System.out.println("::" + (i+1));
					System.out.println(onrePatternTree.sentence);
					for (Map.Entry<String, Integer> entry : uniq_extrs.entrySet()) {
						System.out.println(entry.getValue());
						System.out.println(entry.getKey());
					}
					System.out.println();
				}
				
				if(extrs!=null) extrs_all.addAll(extrs.keySet());
			}
		}
		
		if(OnreGlobals.arg_onre_isSeedFact) OnreIO.writeFile(args[1]+"_out_facts", extrs_all);
			
		
		
		//String filePath_input = "data/0000tw";

		
		System.out.println("==============Done===========");
	}

}
