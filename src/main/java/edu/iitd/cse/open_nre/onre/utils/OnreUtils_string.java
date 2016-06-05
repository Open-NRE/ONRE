package edu.iitd.cse.open_nre.onre.utils;

public class OnreUtils_string {
	
	public static boolean isIgnoreCaseMatch(String s1, String s2) {
		return s1.toLowerCase().matches(s2.toLowerCase());
	}
	
	public static boolean isIgnoreCaseIgnoreCommaIgnoreSpaceContains(String s1, String s2) {
		return lowerTrimCommaSpace(s1).contains(lowerTrimCommaSpace(s2));
	}

	private static String lowerTrimCommaSpace(String s1) {
		return s1.toLowerCase().trim().replace(",", "").replace(" ", "");
	}
	
	public static String lowerTrim(String s1) {
		if(s1!=null) return s1.toLowerCase().trim();
		return null;
	}
	
	public static String replacePer_centToPerCent(String str) {
		return str.replace("per cent", "percent");
	}
}
