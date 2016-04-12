/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import scala.collection.JavaConversions;
import scala.collection.Seq;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.knowitall.tool.parse.graph.DependencyGraph;

/**
 * @author harinder
 *
 */
public class MayIHelpYou {

    public static Seq<OnreExtraction> runMe(DependencyGraph depGraph) throws IOException {
		
		DependencyGraph simplifiedGraph = OnreHelper_graph.simplifyGraph(depGraph);
		//System.out.println("---simplified graph");
		OnrePatternNode onrePatternNode = OnreHelper_graph.convertGraph2PatternTree(simplifiedGraph);
		//System.out.println("---convertGraph2PatternTree");
		List<OnrePatternNode> list_configuredPattern = OnreHelper_pattern.getConfiguredPatterns();
		//System.out.println("---getConfiguredPatterns");
		List<OnreExtraction> extrs = getExtractions(onrePatternNode, list_configuredPattern);
		//System.out.println("---getExtractions");
		System.out.println(depGraph.text());
		for (OnreExtraction onreExtraction : extrs) {
			System.out.println(onreExtraction);
		}
		System.out.println();
		
		//addDummyExtractions(extrs);
		return javaList2ScalaSeq(extrs);

		// System.out.println("You are running me :)");
	}
    
    private static List<OnreExtraction> getExtractions(OnrePatternNode onrePatternNode, List<OnrePatternNode> list_configuredPattern) {
    	List<OnreExtraction> extrs = new ArrayList<>();
    	
    	for (int i=0; i<list_configuredPattern.size(); i++) {
	        OnreExtraction onreExtraction = getExtraction(onrePatternNode, list_configuredPattern.get(i));
	        if(onreExtraction != null) {System.out.println(i+1); extrs.add(onreExtraction);}
        }
    	
    	return extrs;
    }
    
    private static OnreExtraction getExtraction(OnrePatternNode patternNode_sentence, OnrePatternNode patternNode_configured) {
    	OnreExtraction onreExtraction = new OnreExtraction();
    	OnrePatternNode subTree = OnreHelper.findPatternSubTree(patternNode_sentence, patternNode_configured, onreExtraction);
    	
    	if(subTree == null) return null;
    	
    	OnreHelper.expandExtraction(onreExtraction, patternNode_sentence);
    	OnreHelper.onreExtraction_dummyForNull(onreExtraction);
    	
    	return onreExtraction;
    }

	private static Seq<OnreExtraction> javaList2ScalaSeq(List<OnreExtraction> list_java) {
	    return JavaConversions.asScalaBuffer(list_java).toList();
    }

	/*private static void addDummyExtractions(List<OnreExtraction> extrs) {
	    OnreExtraction extr1 = newExtr("Deadpool", "budget", "53", "$ million");
		OnreExtraction extr2 = newExtr("India", "population", "1", "billion");
		extr2.temporal = newPart("2011");
		extr2.changeType = newPart(OnreChangeType.CHANGE_INCREASE.toString());
		
		extrs.add(extr1);
		extrs.add(extr2);
    }

	private static OnreExtraction newExtr(String arg, String rel, String q, String unit) {
		return new OnreExtraction(newPart(arg), newPart(rel), newPart(q), newPart(unit));
	}

	private static OnreExtractionPart newPart(String text) {
		return new OnreExtractionPart(text);
	}*/
}
