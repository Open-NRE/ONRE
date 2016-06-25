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
		String s = "<(#{rel}#verb)<(dobj#{quantity}#.+)<(prep#of|for#in)<(pobj#{arg}#nnp|nn|prp)>>>>";
		System.out.println(s.equalsIgnoreCase("<(#{rel}#verb)<(dobj#{quantity}#.+)<(prep#of|for#in)<(pobj#{arg}#nnp|nn|prp)>>>>"));
	}

}
