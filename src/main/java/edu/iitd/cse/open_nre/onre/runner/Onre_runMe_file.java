/**
 * 
 */
package edu.iitd.cse.open_nre.onre.runner;

import java.io.IOException;
import java.util.List;

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
		//System.out.println("I am here");

		String filePath_inputSentences = "data/in_wiki.txt";
		List<String> inputLines = OnreIO.readFile(filePath_inputSentences);
		//BufferedReader br = new BufferedReader(new FileReader(inputFile));

		//String line = br.readLine();
		
		//int i=0;
		//while(line != null) {
		for(int i=0;i<inputLines.size();i++){
			System.out.println("::" + (i+1));
			DependencyGraph depGraph = Onre_runMe.getDepGraph(inputLines.get(i));
			if(depGraph != null) MayIHelpYou.runMe(depGraph);
			//line = br.readLine();
		}
		//br.close();
	}

}
