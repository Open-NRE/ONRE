/**
 * 
 */
package edu.iitd.cse.open_nre.onre.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author harinder
 *
 */
public class OnreIO {
	
	public static List<String> readDepPatterns() throws IOException {
		String depPatternsFile = "/home/harinder/Documents/IITD_MTP/ONRE/src/main/resources/edu/iitd/cse/open_nre/onre/DependencyPathPatterns"; //TODO: use relative path
			
		List<String> list_patterns = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(depPatternsFile));
		String line = br.readLine();
		while(line != null) {
			list_patterns.add(line);
			line = br.readLine();
		}
		
		br.close();
		return list_patterns;
	}
}
