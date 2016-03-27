/**
 * 
 */
package edu.iitd.cse.open_nre.onre.runner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import edu.iitd.cse.open_nre.onre.helper.MayIHelpYou;
import edu.knowitall.tool.parse.graph.DependencyGraph;


/**
 * @author harinder
 *
 */
public class TestMain_file {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("I am here");

		String inputFile = "data/in.txt";
		BufferedReader br = new BufferedReader(new FileReader(inputFile));

		String line = br.readLine();
		
		int i=0;
		while(line != null) {
			System.out.println("::" + (++i));
			DependencyGraph depGraph = TestMain.getDepGraph(line);
			MayIHelpYou.runMe(depGraph);
			
			line = br.readLine();
		}
		br.close();
	}

}
