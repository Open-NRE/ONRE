/**
 * 
 */
package edu.iitd.cse.open_nre.onre.runner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.iitd.cse.open_nre.onre.constants.OnreConstants;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.helper.MayIHelpYou;
import edu.iitd.cse.open_nre.onre.utils.OnreIO;
import edu.knowitall.tool.parse.graph.DependencyGraph;


/**
 * @author harinder
 *
 */
public class Onre_runMe_file {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Onre_runMe.setArguments(args);

		String filePath_inputSentences = "data/input_files/in_numberRuleTestSet_lrb_rrb";
		List<String> inputLines = OnreIO.readFile(filePath_inputSentences);

		for(int i=0;i<inputLines.size();i++){
			System.out.println("\n::" + (i+1));
			DependencyGraph depGraph = Onre_runMe.getDepGraph(inputLines.get(i));
			if(depGraph != null) {
				Map<OnreExtraction, Integer> extrs = MayIHelpYou.runMe(depGraph);
				System.out.println(inputLines.get(i));
				for (OnreExtraction onreExtraction : extrs.keySet()) {
					System.out.println(onreExtraction.patternNumber-OnreConstants.NUMBER_OF_SEED_PATTERNS);
					System.out.println(onreExtraction);
				}
			}
		}

	}

}
