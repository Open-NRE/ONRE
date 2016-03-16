/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.utils.OnreIO;

/**
 * @author harinder
 *
 */
public class OnrePatternHelper {
	
	public static List<OnrePatternNode> getConfiguredPatterns() throws IOException {
		List<OnrePatternNode> list_configuredPattern = new ArrayList<OnrePatternNode>();
		List<String> configuredPatterns = OnreIO.readDepPatterns();

		for (String configuredPattern : configuredPatterns) {
			list_configuredPattern.add(convertPattern2PatternTree(configuredPattern));
        }
		
		return list_configuredPattern;
	}
	
	private static OnrePatternNode convertPattern2PatternTree(String pattern) {
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
