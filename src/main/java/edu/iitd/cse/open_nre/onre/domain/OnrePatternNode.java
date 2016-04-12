/**
 * 
 */
package edu.iitd.cse.open_nre.onre.domain;

import java.util.ArrayList;
import java.util.List;

import edu.iitd.cse.open_nre.onre.utils.OnreUtils;
import edu.knowitall.tool.parse.graph.DependencyNode;

/**
 * @author harinder
 *
 */
public class OnrePatternNode { 
	public String dependencyLabel;
	
	public String word;
	public String posTag;
	
	public int index;
	public int offset;
	
	public List<OnrePatternNode> children;
	public OnrePatternNode parent;
	
	//public ExtractionPartType extractionPartType;
	
	/*public OnrePatternNode() {
		this.children = new ArrayList<OnrePatternNode>();
	}*/
	
	public OnrePatternNode(String nodeString, OnrePatternNode parentNode) {
		String split[] = nodeString.split("#");
		this.dependencyLabel = split[0];
		this.word = split[1];
		this.posTag = split[2];
		this.parent = parentNode;

		this.children = new ArrayList<OnrePatternNode>();
	}
	
	public OnrePatternNode(DependencyNode depNode) {
		this.word = depNode.text();
		this.posTag = depNode.postag();
		this.index = depNode.index();
		this.offset = depNode.offset();
		
		this.children = new ArrayList<OnrePatternNode>();
	}
	
	public boolean matches(OnrePatternNode regexNode) {
		//System.out.println(this.word);
		//System.out.println(onrePatternNode.word);
		//System.out.println();
		if(!isValid(regexNode)) return false;
		
		if(!isMatchPosTag(regexNode)) return false;
		if(!isMatchDepLabel(regexNode)) return false;
		if(!isMatchWord(regexNode)) return false; 
		return true;
	}

	private boolean isValid(OnrePatternNode regexNode) {
		if(!(regexNode.word.startsWith("{") && regexNode.word.endsWith("}"))) return true;
		
		if(regexNode.word.equals("{q_value}") && !OnreUtils.isNumber(this.word)) return false;
		if(!regexNode.word.equals("{q_value}") && OnreUtils.isNumber(this.word)) return false;
		
		return true;
	}
	
	private boolean isMatchWord(OnrePatternNode regexNode) {
		//TODO: null/empty checks for both this & that
		if(this.word==null || this.word.equals("")) return true;
		if(regexNode.word==null || regexNode.word.equals("")) return true;
		
		//TODO: curly check for both this & that - edit: commented for 'this'
		if(regexNode.word.startsWith("{") && regexNode.word.endsWith("}")) return true;
		//if(this.word.startsWith("{") && this.word.endsWith("}")) return true;
		
		//if(this.word.equalsIgnoreCase(onrePatternNode.word)) return true;
		if(this.word.matches(regexNode.word)) return true;
		
		return false;
	}

	private boolean isMatchDepLabel(OnrePatternNode regexNode) {
		//TODO: null/empty checks for both this & that
		if(this.dependencyLabel==null || this.dependencyLabel.equals("")) return true;
		if(regexNode.dependencyLabel==null || regexNode.dependencyLabel.equals("")) return true;
		
		//if(this.dependencyLabel.equalsIgnoreCase(regexNode.dependencyLabel)) return true;
		if(this.dependencyLabel.matches(regexNode.dependencyLabel)) return true;
		
		return false;
	}

	private boolean isMatchPosTag(OnrePatternNode regexNode) {
		//TODO: null/empty checks for both this & that
		if(this.posTag==null || this.posTag.equals("")) return true;
		if(regexNode.posTag==null || regexNode.posTag.equals("")) return true;
		
		//if(this.posTag.equalsIgnoreCase(regexNode.posTag)) return true;
		if(this.posTag.matches(regexNode.posTag)) return true;
		
		return false;
	}
	
	public String toString() {
		return "(" + dependencyLabel + "_" + word + "_" + posTag + ")" + children;
	}
}
