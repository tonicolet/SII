package net.decosa.sii.util;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class NumberUtils {
	
	
	public static boolean isNullOrZero(Double d) {
		return (d == null || d.compareTo(0.0) == 0);
	}
	
	
	public static boolean isNullOrZero(Integer i) {
		return (i == null || i.compareTo(0) == 0);
	}
	
	
	public static Double round(Double value) {
		return round(value, 2);
	}
	
	
	public static Double round(Double value, int places) {
		if (value == null) return null;
		if (places < 0) return value;

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}
	
	
	public static String leadingZeros(int value, int places) {
	    return String.format("%0" + places + "d", value);
	}
}
