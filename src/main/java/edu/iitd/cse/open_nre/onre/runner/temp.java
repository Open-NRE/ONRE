package edu.iitd.cse.open_nre.onre.runner;

import java.io.IOException;
import java.util.List;

import edu.iitd.cse.open_nre.onre.constants.OnreConstants;
import edu.iitd.cse.open_nre.onre.domain.Onre_dsDanrothSpan;
import edu.iitd.cse.open_nre.onre.domain.Onre_dsDanrothSpans;
import edu.iitd.cse.open_nre.onre.helper.OnreHelper_DanrothQuantifier;
import edu.iitd.cse.open_nre.onre.helper.OnreHelper_json;
import edu.iitd.cse.open_nre.onre.utils.OnreIO;
import edu.iitd.cse.open_nre.onre.utils.OnreUtils_string;


public class temp {

	public static void main(String[] args) throws IOException {
		/*List<String> jsonDanrothSpans = OnreIO.readFile("/home/harinder/Documents/IITD_MTP/Open_nre/ONRE_DS/data/temp_filtered"+OnreConstants.SUFFIX_DANROTH_SPANS);
		for (String string : jsonDanrothSpans) {
			Onre_dsDanrothSpans danrothSpans = (Onre_dsDanrothSpans)OnreHelper_json.getObjectFromJsonString(string, Onre_dsDanrothSpans.class);
			
			for (Onre_dsDanrothSpan danrothSpan : danrothSpans.quantSpans) {
				System.out.println(danrothSpan.phrase);
				System.out.println(danrothSpan.value);
				System.out.println(danrothSpan.unit);
				System.out.println(danrothSpan.bound);
			}
		}*/
		//String pattern = "<(#is|are|was|were#verb)<(attr#{quantity}#.+)(nsubj#{rel}#nnp|nn)<(nn#{arg}#dt)>>>";
		//pattern = pattern.replace("poss#{arg}#PRP$)", "poss#{arg}#PRP\\$)");
		//System.out.println(pattern);
		
		//System.out.println(pattern.contains("{arg}#dt"));
		//System.out.println(OnreHelper_DanrothQuantifier.getValueFromPhrase(".361"));
		
		//System.out.println("abc".contains(""));
		//pattern = pattern.replaceFirst("poss#\\{arg\\}#PRP\\$\\)", "poss#{arg}#PRP\\\\\\$)");
		//System.out.println(pattern);
		
		//System.out.println("2 terrapins".replace("terrapins", ""));
		
		//System.out.println("1.2".replaceAll("\\.$", ""));
		
		//System.out.println("black plastic earth machine things".matches("\\w|^" + "black plastic earth machine things" + "\\w|$")); 
		//System.out.println("black plastic earth machinE things".matches("(?i).*\\b" + "black plastic earth machine things" + "\\b.*"));
		//System.out.println(" things ".matches(".*(\\w|^)" + "things" + "(\\w|$).*"));
		String s1="the US$";
		System.out.println(s1.matches("(?i).*\\b" + "us$" + "\\b.*"));
		
		//System.out.println(OnreUtils_string.isIgnoreCaseIgnoreCommaIgnoreSpaceContainsWord("of the average household 's contribution", "the average household 's contribution"));
		
	}

}
