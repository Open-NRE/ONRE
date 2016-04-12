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

		String sentence = "The National Highways constitute only 1.67 per cent of the total road length.";
		
		//String sentence = "The height of Tower is 1063 feet.";
		DependencyGraph depGraph = getDepGraph(sentence);
		//System.out.println("---Got depGraph");
		if(depGraph != null) MayIHelpYou.runMe(depGraph);
	}

    public static DependencyGraph getDepGraph(String sentence) {
    	sentence = preprocessing(sentence);
    	
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
    
    private static String preprocessing(String sentence) {
    	sentence = sentence.replace("per cent", "percent");
    	return sentence;
    }

	/*private String clean(String line) {
		String cleaned = line;

		cleaned = replaceChars.replacenow(cleaned);

		cleaned = CharMatcher.WHITESPACE.replaceFrom(cleaned, ' ');
		cleaned = CharMatcher.JAVA_ISO_CONTROL.removeFrom(cleaned);

		return cleaned;
	}*/
}
