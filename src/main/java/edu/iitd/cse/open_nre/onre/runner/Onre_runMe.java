/**
 * 
 */
package edu.iitd.cse.open_nre.onre.runner;

import java.io.IOException;

import edu.iitd.cse.open_nre.onre.OnreGlobals;
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
public class Onre_runMe {
	
	static DependencyParser parser;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//System.out.println("I am here");
		String sentence = "In january, the polulation of india was 1.2 billion.";
		
		//String sentence = "The height of Tower is 1063 feet.";
		DependencyGraph depGraph = getDepGraph(sentence);
		//System.out.println("---Got depGraph");
		Onre_runMe.setArguments(args);
		
		if(depGraph != null) MayIHelpYou.runMe(depGraph);
	}
	
	public static void setArguments(String[] args) {
		if(args.length > 0) OnreGlobals.arg_isSeedFact = (args[0].equals("--seedFact")); //TODO: "--seedFact" shall be in a constants file
	}

    public static DependencyGraph getDepGraph(String sentence) {
    	//sentence = preprocessing(sentence);
    	
	    //String cleaned = clean(sentence);
    	if(parser == null) parser = getParser();
		
		DependencyGraph depGraph = null;
		
		try {
		depGraph = parser.apply(sentence);
		} catch(AssertionError error) {
			System.err.println("----->" + error.toString());
			return null;
		}
		
	    return depGraph;
    }

	private static DependencyParser getParser() {
		ClearTokenizer tokenizer = new ClearTokenizer();
		ClearPostagger postagger = new ClearPostagger(tokenizer);
		DependencyParser parser = new ClearParser(postagger);
		return parser;
	}
    
    /*private static String preprocessing(String sentence) {
    	sentence = sentence.replace("per cent", "percent");
    	return sentence;
    }*/

	/*private String clean(String line) {
		String cleaned = line;

		cleaned = replaceChars.replacenow(cleaned);

		cleaned = CharMatcher.WHITESPACE.replaceFrom(cleaned, ' ');
		cleaned = CharMatcher.JAVA_ISO_CONTROL.removeFrom(cleaned);

		return cleaned;
	}*/
}
