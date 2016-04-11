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

		String sentence = "Portugal had 10,475 million inhabitants.";
		//String sentence = "The height of Tower is 1063 feet.";
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
