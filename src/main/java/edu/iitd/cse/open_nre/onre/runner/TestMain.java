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

		String sentence = "The population of India is 1.2 billion.";

		//String cleaned = clean(sentence);
		ClearTokenizer tokenizer = new ClearTokenizer();
		ClearPostagger postagger = new ClearPostagger(tokenizer);
		DependencyParser parser = new ClearParser(postagger);
		
		DependencyGraph depGraph = parser.apply(sentence);
		
		//MorphaStemmer morphaStemmer = new MorphaStemmer();
		//DependencyPattern pattern = DependencyPattern.deserialize("be {rel} {prep}	{arg1} <nsubjpass< {rel:postag=VBN} >{prep:regex=prep_(.*)}> {arg2}	1.0000", morphaStemmer);
		
		MayIHelpYou.runMe(depGraph);
	}

	/*private String clean(String line) {
		String cleaned = line;

		cleaned = replaceChars.replacenow(cleaned);

		cleaned = CharMatcher.WHITESPACE.replaceFrom(cleaned, ' ');
		cleaned = CharMatcher.JAVA_ISO_CONTROL.removeFrom(cleaned);

		return cleaned;
	}*/
}
