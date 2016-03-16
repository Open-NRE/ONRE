/**
 * 
 */
package edu.iitd.cse.open_nre.onre.utils;

import java.util.Set;

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
}
