package edu.iitd.cse.open_nre.onre.runner;

import java.io.IOException;
import java.util.List;

import edu.iitd.cse.open_nre.onre.constants.OnreConstants;
import edu.iitd.cse.open_nre.onre.domain.Onre_dsDanrothSpan;
import edu.iitd.cse.open_nre.onre.domain.Onre_dsDanrothSpans;
import edu.iitd.cse.open_nre.onre.helper.OnreHelper_json;
import edu.iitd.cse.open_nre.onre.utils.OnreIO;


public class temp {

	public static void main(String[] args) throws IOException {
		List<String> jsonDanrothSpans = OnreIO.readFile("/home/harinder/Documents/IITD_MTP/Open_nre/ONRE_DS/data/temp_filtered"+OnreConstants.SUFFIX_DANROTH_SPANS);
		for (String string : jsonDanrothSpans) {
			Onre_dsDanrothSpans danrothSpans = (Onre_dsDanrothSpans)OnreHelper_json.getObjectFromJsonString(string, Onre_dsDanrothSpans.class);
			
			for (Onre_dsDanrothSpan danrothSpan : danrothSpans.quantSpans) {
				System.out.println(danrothSpan.phrase);
				System.out.println(danrothSpan.value);
				System.out.println(danrothSpan.unit);
				System.out.println(danrothSpan.bound);
			}
		}
	}

}
