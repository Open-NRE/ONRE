/**
 * 
 */
package edu.iitd.cse.open_nre.onre.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iitd.cse.open_nre.onre.OnreGlobals;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternTree;
import edu.iitd.cse.open_nre.onre.domain.Onre_dsDanrothSpans;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils;
import edu.knowitall.tool.parse.graph.DependencyGraph;

/**
 * @author harinder
 *
 */
public class MayIHelpYou {

    public static Map<OnreExtraction, Integer> runMe(DependencyGraph depGraph) throws IOException {
		
    	DependencyGraph simplifiedGraph = OnreHelper_graph.simplifyGraph(depGraph);
    	OnrePatternTree onrePatternTree = OnreHelper_graph.convertGraph2PatternTree(simplifiedGraph);
    	
    	OnreGlobals.sentence = onrePatternTree.sentence;
    	
    	Onre_dsDanrothSpans danrothSpans = OnreHelper_DanrothQuantifier.getQuantitiesDanroth(OnreGlobals.sentence);
    	List<OnrePatternNode> list_configuredPattern = OnreHelper_pattern.getConfiguredPatterns();
    	
    	return runMe(onrePatternTree, danrothSpans, list_configuredPattern);
	}

    public static Map<OnreExtraction, Integer> runMe(OnrePatternTree onrePatternTree, Onre_dsDanrothSpans danrothSpans, List<OnrePatternNode> list_configuredPattern) throws IOException {
    	if(onrePatternTree == null) return null;
    	OnreGlobals.sentence = onrePatternTree.sentence;
    	
    	Map<OnreExtraction, Integer> extrs = getExtractions(onrePatternTree, list_configuredPattern, danrothSpans);
		
		//if(!OnreGlobals.arg_isSeedFact) System.out.println(OnreGlobals.sentence);
		
		//for (OnreExtraction onreExtraction : extrs) {
			//if(OnreUtils.quantityExists(onreExtraction)) System.out.println(onreExtraction);
		//}
		
		//if(!OnreGlobals.arg_isSeedFact) System.out.println();
		
		//addDummyExtractions(extrs);
		//return javaList2ScalaSeq(extrs);
		
		return OnreUtils.sortMapByValue(extrs, false);

		// System.out.println("You are running me :)");
	}
    
    private static boolean checkIfExtractionPartSimilar(String s1, String s2) {
    	if(!s1.equals(null) && !s2.equals(null) && !s1.equals(s2)) return false;
    	return true;
    }
    
    private static boolean checkIfSimilarExtractionExists(OnreExtraction extr1, OnreExtraction extr2) {
    	if(!checkIfExtractionPartSimilar(extr1.argument.text, extr2.argument.text)) return false;
    	if(!checkIfExtractionPartSimilar(extr1.relation.text, extr2.relation.text)) return false;
    	if(!checkIfExtractionPartSimilar(extr1.quantity.text, extr2.quantity.text)) return false;
    	if(extr1.additional_info!=null && extr2.additional_info!=null 
    			&& !checkIfExtractionPartSimilar(extr1.additional_info.text, extr2.additional_info.text)) return false;
    	
    	return true;
    }
    
    private static int countNullFields(OnreExtraction onreExtraction) {
    	int count = 0;
    	if(onreExtraction.argument.text != null) count++;
    	if(onreExtraction.relation.text != null) count++;
    	if(onreExtraction.quantity.text != null) count++;
    	if(onreExtraction.additional_info != null && onreExtraction.additional_info.text != null) count++;
    	
    	return count;
    }
    
    private static void addExtractionToMap(Map<OnreExtraction, Integer> extrs, OnreExtraction onreExtraction) {
    	boolean isSimilarExtractionExists = false;
    	OnreExtraction currExtraction = null;
    	for(Map.Entry<OnreExtraction, Integer> entry : extrs.entrySet()) {
    		currExtraction = entry.getKey();
    		if(checkIfSimilarExtractionExists(currExtraction, onreExtraction)) {
    			isSimilarExtractionExists = true;
    			break;
    		}
    	}
    	
    	if(!isSimilarExtractionExists) {
    		extrs.put(onreExtraction, onreExtraction.patternNumber);
    	}
    	else {
    		if(countNullFields(currExtraction) < countNullFields(onreExtraction)) {
    			extrs.remove(currExtraction);
    			extrs.put(onreExtraction, onreExtraction.patternNumber);
    		}
    	}
    }
    
    private static Map<OnreExtraction, Integer> getExtractions(OnrePatternTree onrePatternTree, List<OnrePatternNode> list_configuredPattern, Onre_dsDanrothSpans danrothSpans) throws IOException {
    	Map<OnreExtraction, Integer> extrs = new HashMap<OnreExtraction, Integer>();
    	
    	for (int i=0; i<list_configuredPattern.size(); i++) {
    		//System.out.println("pattern: " + i);
    		OnrePatternNode configuredPattern = list_configuredPattern.get(i);
    		if(configuredPattern==null) continue;
    		
	        OnreExtraction onreExtraction = getExtraction(onrePatternTree, onrePatternTree.root, configuredPattern, danrothSpans);
	        if(onreExtraction == null) continue;
	        if(!OnreUtils.quantityExists(onreExtraction)) continue;
	        	
        	onreExtraction.patternNumber=i+1;
        	onreExtraction.sentence = onrePatternTree.sentence;
        	
        	//TODO: IMPORTANT-CHANGE #10:Removing extractions where they are similar except for the additional-info field
        	//extrs.put(onreExtraction, onreExtraction.patternNumber);
        	addExtractionToMap(extrs, onreExtraction);
        }
    	
    	return extrs;
    }
    
    private static OnreExtraction getExtraction(OnrePatternTree onrePatternTree, OnrePatternNode patternNode_sentence, OnrePatternNode patternNode_configured, Onre_dsDanrothSpans danrothSpans) throws IOException {
    	OnreExtraction onreExtraction = new OnreExtraction();
    	OnrePatternNode subTree = OnreHelper.findPatternSubTree(onrePatternTree, patternNode_sentence, patternNode_configured, onreExtraction, danrothSpans);
    	
    	if(subTree == null) return null;
    	
    	return OnreHelper.onreExtraction_postProcessing(patternNode_sentence, onreExtraction);
    }

	/*private static Seq<OnreExtraction> javaList2ScalaSeq(List<OnreExtraction> list_java) {
	    return JavaConversions.asScalaBuffer(list_java).toList();
    }*/

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
