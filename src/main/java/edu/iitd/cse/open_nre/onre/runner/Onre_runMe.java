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
		Onre_runMe.setArguments(args);

		String sentence = "Awarded Bids 100% Completion Rate Hi there, I am an expert in Excel Database solution also good in typing skill around 45 to 60 wpm.";
		
		DependencyGraph depGraph = getDepGraph(sentence);

		Onre_runMe.setArguments(args);
		
		if(depGraph != null) MayIHelpYou.runMe(depGraph);
	}
	
	public static void setArguments(String[] args) {
		if(args.length > 0) OnreGlobals.arg_isSeedFact = (args[0].equals("true")); //TODO: "shall have named arguments"
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
