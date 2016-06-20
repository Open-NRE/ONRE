/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.iitd.cse.open_nre.onre.constants.OnreFilePaths;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.utils.OnreIO;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils_tree;

/**
 * @author harinder
 *
 */
public class OnreHelper_pattern {
	
	public static List<OnrePatternNode> getConfiguredPatterns() throws IOException {
		List<OnrePatternNode> list_configuredPattern = new ArrayList<OnrePatternNode>();
		List<String> configuredPatterns = OnreIO.readFile_classPath(OnreFilePaths.filePath_depPatterns);

		for (String configuredPattern : configuredPatterns) {
			if(configuredPattern.trim().length()==0) {list_configuredPattern.add(null); continue;}

			//if(configuredPattern.contains("(nn#")) {list_configuredPattern.add(null); continue;} //TODO: IMPORTANT-CHANGE:ignoring patterns with depLabel as nn (nn#)
			//if(configuredPattern.contains("{arg}#dt")) {list_configuredPattern.add(null); continue;} //TODO: IMPORTANT-CHANGE:ignoring patterns with {arg} postag as dt
			//if(configuredPattern.contains("rel}#in)")) {list_configuredPattern.add(null); continue;} //TODO: IMPORTANT-CHANGE:ignoring patterns with {rel} postag as IN
			
			list_configuredPattern.add(convertPattern2PatternTree(configuredPattern));
        }
		
		return list_configuredPattern;
	}
	
	private static OnrePatternNode convertPattern2PatternTree(String pattern) {
		OnrePatternNode onrePatternNode = convertPattern2PatternTree_helper(pattern);
		OnreUtils_tree.sortPatternTree(onrePatternNode);
		return onrePatternNode;
	}
	
	private static OnrePatternNode convertPattern2PatternTree_helper(String pattern) {
		Stack<Character> myStack = new Stack<>();
		myStack.push(pattern.charAt(0));
		int index=1;
		
		OnrePatternNode onrePatternNode = null;
		while(!myStack.isEmpty()) {
			while(pattern.charAt(index)=='(' || pattern.charAt(index)=='<') {
				StringBuilder sb = new StringBuilder("");
				index = setNodeString(pattern, index, sb);
				OnrePatternNode onrePatternNode_child = new OnrePatternNode(sb.toString(), onrePatternNode);
				if(onrePatternNode==null) onrePatternNode=onrePatternNode_child; //at the start
				else onrePatternNode.children.add(onrePatternNode_child);
				
				if(pattern.charAt(index)=='<') {
					myStack.push('<');
					onrePatternNode = onrePatternNode_child;
				}
			}
			
			while(pattern.charAt(index)=='>') { 
				myStack.pop(); 
				if(onrePatternNode.parent==null) return onrePatternNode; 
				onrePatternNode = onrePatternNode.parent; 
				index++; 
			}
		}
		
		return onrePatternNode;
	}
	
    public static int setNodeString(String pattern, int index, StringBuilder sb) {
	    if(pattern.charAt(index)=='<') index++;
		index++; //current index: '('...moving to next index
		
		while(pattern.charAt(index) != ')') {
			sb.append(pattern.charAt(index));
			index++;
		}
		
		index++; //current index: ')'...moving to next index
	    return index;
    }
}
