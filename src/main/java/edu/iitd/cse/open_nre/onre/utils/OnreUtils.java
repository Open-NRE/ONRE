/**
 * 
 */
package edu.iitd.cse.open_nre.onre.utils;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scala.collection.JavaConversions;
import edu.iitd.cse.open_nre.onre.domain.OnreExtraction;

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
	
	
	
	public static boolean quantityExists(OnreExtraction onreExtraction) {
	    	if(onreExtraction.quantity == null) return false;
	    	if(onreExtraction.quantity.text == null) return false;
	    	
	    	if(onreExtraction.quantity.text.matches(".*\\d.*")) return true;
	    	
	    	return false;
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
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) result.put(entry.getKey(), entry.getValue());
		
		return result;
	}

}
