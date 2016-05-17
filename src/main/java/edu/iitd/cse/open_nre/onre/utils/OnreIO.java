/**
 * 
 */
package edu.iitd.cse.open_nre.onre.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author harinder
 *
 */
public class OnreIO {
	
	/*public static List<String> readDepPatterns() throws IOException {
		InputStream inputStream = OnreIO.class.getResourceAsStream(OnreConst.FILE_DEP_PATTERNS);
		
		if(inputStream == null) {
			System.err.println("ERROR :: ---Not able to read DepPath patterns...exiting---");
			System.exit(1);
		} //TODO: check why not working
			
		List<String> list_patterns = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(depPath));
		String line = br.readLine();
		while(line != null) {
			list_patterns.add(line);
			line = br.readLine();
		}
		
		br.close();
		return list_patterns;
	}*/
	
	public static List<String> readFile(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		List<String> lines = new ArrayList<>();
		
		String line = br.readLine();
		while(line != null) {
			if(!line.trim().isEmpty()) lines.add(line); 
			line = br.readLine();
		}
		
		br.close();
		return lines;
	}
	
	public static void writeFile(String filePath, List<String> lines) throws IOException {
		PrintWriter pw = new PrintWriter(filePath);
		for (String string : lines) pw.println(string);
		pw.close();
	}
	
}
