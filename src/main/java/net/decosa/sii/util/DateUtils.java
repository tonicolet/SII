package net.decosa.sii.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.LocalDate;

import net.decosa.sii.SiiConfig;


public class DateUtils {
	
	public final static String DEFAULT_TIME_FORMAT		= "HH:mm";
	public final static String DEFAULT_TIME_SEC_FORMAT	= DEFAULT_TIME_FORMAT + ":ss";
	public final static String DEFAULT_DATE_TIME_FORMAT	= SiiConfig.DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_SEC_FORMAT;

	
	public static Date hoy() {
		return new Date();
	}
	
	
	public static String format() {
		return format(new Date(), SiiConfig.DEFAULT_DATE_FORMAT);
	}
	
	
	public static String format(Date d) {
		return format(d, SiiConfig.DEFAULT_DATE_FORMAT);
	}

	
	public static String format(Date d, String format) {
		if (d == null) return "";
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	
	
	public static String format(long l) {
		SimpleDateFormat sdf = new SimpleDateFormat(SiiConfig.DEFAULT_DATE_FORMAT);
		return sdf.format(l);
	}
	
	
	public static Date parse(String s) {
		return parse(s, SiiConfig.DEFAULT_DATE_FORMAT);
	}
	
	
	public static Date parse(String s, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static int getMes(Date d) {
		LocalDate ld = getLocalDate(d);
		return ld.getMonthOfYear();
	}
	
	
	public static String getMMes(Date d) {
		LocalDate ld = getLocalDate(d);
		int m = ld.getMonthOfYear();
		return NumberUtils.leadingZeros(m, 2);
	}
	
	
	public static int getAnyo() {
		return getAnyo(new Date());
	}
	
	
	public static int getAnyo(Date d) {
		LocalDate ld = getLocalDate(d);
		return ld.getYear();
	}
	
	
	public static String getAAnyo(Date d) {
		return String.valueOf(getAnyo(d)).substring(2);
	}
	
	
	private static LocalDate getLocalDate(Date d) {
		return new LocalDate(d);
	}
}
