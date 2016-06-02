package edu.iitd.cse.open_nre.onre.runner;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class temp {

	public static void main(String[] args) throws ParseException {
/*		String regex = "4$";
		
		
		String s = "$";
		System.out.println(regex.equals("$"));
*/
		
	/*	String pattern = "<(#is|are|was|were#VERB)<(attr#{quantity}#NNP|NN)(nsubj#{rel}#NNP|NN)<(poss#{arg}#PRP$)>>>";
		
		pattern = pattern.replaceFirst("#\\{quantity\\}#.+\\)", "#{quantity}#.+)");
		
		System.out.println();*/
		
		NumberFormat format = NumberFormat.getInstance(Locale.US);

        Number number = format.parse("1,038");
        System.out.println(number); // or use number.doubleValue()
		
		//String s2 = "(pobj#{quantity}#.+)";"(attr#{quantity}#CD)"
		
		//System.out.println(s1.replaceFirst("(poss#\\{arg}#PRP$)", "(poss#{arg}#PRP\\$)"));
		/*String s = "nsubj#{rel}#NN";
		String[] kk = s.split("#");
		System.out.println();*/
	}

}
