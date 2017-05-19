package net.decosa.sii.util;

import java.text.Normalizer;


public class StringUtils {
	
	public static String removeSpecialChars(String s) {
		if (s == null) return null;
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("[^\\p{ASCII}]", "");
		return s;
	}
	
	
	public static String limit(String s, int size) {
		if (s == null) return null;
		if (s.length() < size) return s;
		return s.substring(0, size);
	}
	
	
	public static boolean isBlank(String s) {
		return ((s == null)||(s.length() == 0));
	}
}
