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
	
	public String toString() {
		return word + "_" + posTag + "_" + index;
	}
}
