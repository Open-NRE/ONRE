/**
 * 
 */
package edu.iitd.cse.open_nre.onre.utils;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import scala.collection.JavaConversions;
import edu.iitd.cse.open_nre.onre.comparators.OnreComparator_PatternNode_depLabel;
import edu.iitd.cse.open_nre.onre.domain.OnreExtractionPart;
import edu.iitd.cse.open_nre.onre.domain.OnrePatternNode;

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
		Queue<OnrePatternNode> q_patternNode = new LinkedList<>();
		q_patternNode.add(onrePatternNode);
		
		while(!q_patternNode.isEmpty()) {
			OnrePatternNode currNode = q_patternNode.remove();
			Collections.sort(currNode.children, new OnreComparator_PatternNode_depLabel());
			q_patternNode.addAll(currNode.children);
		}
	}
	
	public static OnrePatternNode searchNodeInTreeByText(String text, OnrePatternNode tree) {
		Queue<OnrePatternNode> q_patternNode = new LinkedList<>();
		q_patternNode.add(tree);
		
		while(!q_patternNode.isEmpty()) {
			OnrePatternNode currNode = q_patternNode.remove();
			if(currNode.word.equalsIgnoreCase(text)) return currNode;
			q_patternNode.addAll(currNode.children);
		}
		
		return null;
	}
	
	public static OnrePatternNode searchNodeInTreeByIndex(OnreExtractionPart onreExtractionPart, OnrePatternNode tree) {
		Queue<OnrePatternNode> q_patternNode = new LinkedList<>();
		q_patternNode.add(tree);
		
		while(!q_patternNode.isEmpty()) {
			OnrePatternNode currNode = q_patternNode.remove();
			if(currNode.index==onreExtractionPart.index) return currNode;
			q_patternNode.addAll(currNode.children);
		}
		
		return null;
	}
	
	public static boolean isNumber(String str) {
		Pattern numberPat = Pattern.compile("^[\\+-]?\\d+([,\\.]\\d+)*([eE]-?\\d+)?$");
		return numberPat.matcher(str.toString()).find();
	}
	
	public static void listFilesForFolder(final File folder, Set<String> files) {
		if (!folder.isDirectory()) {
			files.add(folder.getPath());
			return;
		}
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry, files);
	        } else {
	        	String fileName = fileEntry.getPath();
	        	if(fileName.charAt(fileName.length()-1)!='~') files.add(fileEntry.getPath());
	        }
	    }
	}
	
}
