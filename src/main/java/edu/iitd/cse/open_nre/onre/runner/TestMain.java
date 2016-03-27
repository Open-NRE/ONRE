/**
 * 
 */
package edu.iitd.cse.open_nre.onre.runner;

import java.io.IOException;

import edu.iitd.cse.open_nre.onre.helper.MayIHelpYou;
import edu.knowitall.tool.parse.ClearParser;
import edu.knowitall.tool.parse.DependencyParser;
import edu.knowitall.tool.parse.graph.DependencyGraph;
import edu.knowitall.tool.postag.ClearPostagger;
import edu.knowitall.tool.tokenize.ClearTokenizer;


/**
 * @author harinder
 *
 */
public class TestMain {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("I am here");

		//String sentence = "The population of urban India is 1.2 billion.";
		String sentence = "Rural Roads National Highways Development Projects NHAI has taken up some "
				+ "major projects in the country under different phases : Golden Quadrilateral : "
				+ "It comprises construction of 5,846 km long 4/6 lane, high density traffic corridor, "
				+ "to connect India's four big metro cities of Delhi-Mumbai-Chennai- Kolkata.";
		DependencyGraph depGraph = getDepGraph(sentence);
		//System.out.println("---Got depGraph");
		if(depGraph != null) MayIHelpYou.runMe(depGraph);
	}

    public static DependencyGraph getDepGraph(String sentence) {
	    //String cleaned = clean(sentence);
		ClearTokenizer tokenizer = new ClearTokenizer();
		ClearPostagger postagger = new ClearPostagger(tokenizer);
		DependencyParser parser = new ClearParser(postagger);
		
		DependencyGraph depGraph = null;
		
		try {
		depGraph = parser.apply(sentence);
		} catch(AssertionError error) {
			System.err.println("----->" + error.toString());
			return null;
		}
		
	    return depGraph;
    }

	/*private String clean(String line) {
		String cleaned = line;

		cleaned = replaceChars.replacenow(cleaned);

		cleaned = CharMatcher.WHITESPACE.replaceFrom(cleaned, ' ');
		cleaned = CharMatcher.JAVA_ISO_CONTROL.removeFrom(cleaned);

		return cleaned;
	}*/
}
