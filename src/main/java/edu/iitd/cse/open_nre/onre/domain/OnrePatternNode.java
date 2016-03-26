/**
 * 
 */
package edu.iitd.cse.open_nre.onre.domain;

import java.util.ArrayList;
import java.util.List;

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
		String split[] = nodeString.split("\\|");
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
	
	public boolean isEqualTo(OnrePatternNode onrePatternNode) {
		//System.out.println(this.word);
		//System.out.println(onrePatternNode.word);
		//System.out.println();
		if(!isMatchPosTag(onrePatternNode)) return false;
		if(!isMatchDepLabel(onrePatternNode)) return false;
		if(!isMatchWord(onrePatternNode)) return false; 
		return true;
	}

	private boolean isMatchWord(OnrePatternNode onrePatternNode) {
		//TODO: null/empty checks for both this & that
		if(this.word==null || this.word.equals("")) return true;
		if(onrePatternNode.word==null || onrePatternNode.word.equals("")) return true;
		
		//TODO: curly check for both this & that
		if(onrePatternNode.word.startsWith("{") && onrePatternNode.word.endsWith("}")) return true;
		if(this.word.startsWith("{") && this.word.endsWith("}")) return true;
		
		if(this.word.equalsIgnoreCase(onrePatternNode.word)) return true;
		
		return false;
	}

	private boolean isMatchDepLabel(OnrePatternNode onrePatternNode) {
		//TODO: null/empty checks for both this & that
		if(this.dependencyLabel==null || this.dependencyLabel.equals("")) return true;
		if(onrePatternNode.dependencyLabel==null || onrePatternNode.dependencyLabel.equals("")) return true;
		
		if(this.dependencyLabel.equalsIgnoreCase(onrePatternNode.dependencyLabel)) return true;
		
		return false;
	}

	private boolean isMatchPosTag(OnrePatternNode onrePatternNode) {
		//TODO: null/empty checks for both this & that
		if(this.posTag==null || this.posTag.equals("")) return true;
		if(onrePatternNode.posTag==null || onrePatternNode.posTag.equals("")) return true;
		
		if(this.posTag.equalsIgnoreCase(onrePatternNode.posTag)) return true;
		return false;
	}
	
	public String toString() {
		return "(" + dependencyLabel + "_" + word + "_" + posTag + ")" + children;
	}
}
