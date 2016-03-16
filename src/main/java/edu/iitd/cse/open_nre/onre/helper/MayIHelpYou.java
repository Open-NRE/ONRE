/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import scala.collection.JavaConversions;
import scala.collection.Seq;
import edu.iitd.cse.open_nre.onre.constants.OnreChangeType;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnreExtractionPart;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.knowitall.tool.parse.graph.DependencyGraph;

/**
 * @author harinder
 *
 */
public class MayIHelpYou {

    public static Seq<OnreExtraction> runMe(DependencyGraph depGraph) throws IOException {
		
		DependencyGraph simplifiedGraph = OnreHelper_graph.simplifyGraph(depGraph);
		OnrePatternNode onrePatternNode = OnreHelper_graph.convertGraph2PatternTree(simplifiedGraph);

		List<OnrePatternNode> list_configuredPattern = OnreHelper_pattern.getConfiguredPatterns();
		
		List<OnreExtraction> extrs = getExtractions(onrePatternNode, list_configuredPattern);
		//addDummyExtractions(extrs);
		return javaList2ScalaSeq(extrs);

		// System.out.println("You are running me :)");
	}
    
    private static List<OnreExtraction> getExtractions(OnrePatternNode onrePatternNode, List<OnrePatternNode> list_configuredPattern) {
    	List<OnreExtraction> extrs = new ArrayList<>();
    	
    	for (OnrePatternNode configuredPattern : list_configuredPattern) {
	        OnreExtraction onreExtraction = getExtraction(onrePatternNode, configuredPattern);
	        if(onreExtraction!=null) extrs.add(onreExtraction);
        }
    	
    	return extrs;
    }
    
    private static OnreExtraction getExtraction(OnrePatternNode patternNode_sentence, OnrePatternNode patternNode_configured) {
    	OnrePatternNode subTree = OnreHelper.findPatternSubTree(patternNode_sentence, patternNode_configured);	
    	OnreExtraction onreExtraction = OnreHelper.createExtractionFromSubTree(subTree, patternNode_configured);
    	//TODO: expand the extraction/pattern - imp
    	return onreExtraction;
    }

	private static Seq<OnreExtraction> javaList2ScalaSeq(List<OnreExtraction> list_java) {
	    return JavaConversions.asScalaBuffer(list_java).toList();
    }

	private static void addDummyExtractions(List<OnreExtraction> extrs) {
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
	}
}
