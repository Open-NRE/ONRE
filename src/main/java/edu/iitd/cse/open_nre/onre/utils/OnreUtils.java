/**
 * 
 */
package edu.iitd.cse.open_nre.onre.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import edu.iitd.cse.open_nre.onre.comparators.OnreComparator_PatternNode;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import scala.collection.JavaConversions;

/**
 * @author harinder
 *
 */
public class OnreUtils {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static Set scalaSet2JavaSet(scala.collection.immutable.Set set_scala){
		if(set_scala==null) return null;
		return JavaConversions.asJavaSet(set_scala);
	}
	
	public static void sortPatternTree(OnrePatternNode onrePatternNode) {
		Queue<OnrePatternNode> patternQ = new LinkedList<>();
		patternQ.add(onrePatternNode);
		
		while(!patternQ.isEmpty()) {
			OnrePatternNode currNode = patternQ.remove();
			Collections.sort(currNode.children, new OnreComparator_PatternNode());
			for (OnrePatternNode child : currNode.children) {
	            patternQ.add(child);
            }
		}
	}
	
}
