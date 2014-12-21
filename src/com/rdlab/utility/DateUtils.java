package com.rdlab.utility;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateUtils {
	public static final String yyyy = "yyyy";
	public static final String MM = "MM";
	public static final String dd = "dd";
	public static final String HH = "HH";
	public static final String mm = "mm";
	public static final String ss = "ss";
	public static final String SSS = "SSS";
	public static final String yyyyMM = yyyy + MM;
	public static final String yyyyMMdd = yyyyMM + dd;
	public static final String yyyyMMddHH = yyyyMMdd + HH;
	public static final String yyyyMMddHHmm = yyyyMMddHH + mm;
	public static final String yyyyMMddHHmmss = yyyyMMddHHmm + ss;
	public static final String yyyyMMddHHmmssSSS = yyyyMMddHHmmss + SSS;
	

	public static final String formatted = "yyyy/MM/dd HH:mm:ss";
	
	public static final Random random = new Random();
	
	
	public static void main(String[] args) {
		System.out.println(nowLong());
	}
	
	public static final Long nowLong() {
		return Long.valueOf(format(yyyyMMddHHmmss, nowDate()));
	}
	
	public static final Long forwardRandomFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, Math.abs(random.nextInt(120)));
		return Long.valueOf(format(yyyyMMddHHmmssSSS, calendar.getTime()));
	}
	
	public static final Date parse(String format, Long date) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
		return sdf.parse("" + date);
	}
	
	public static final Long formatForThrougput() {
		return Long.valueOf(format(yyyyMMddHHmmss, nowDate()));
	}
	
	public static final Long formatForDropThrougput(int diff) {
		return Long.valueOf(format(yyyyMMddHHmmss, nowDate(0-diff)));
	}
	
	public static final String ts() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		sdf1.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
		return sdf1.format(nowDate());
	}
	
	public static final String FormatLongStringDateToString(String date) {
		try {
			Date dt=parse(yyyyMMddHHmmss, Long.parseLong(date));
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
			sdf1.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
			return sdf1.format(dt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static final String ConvertDateToString(Date date) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		sdf1.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
		return sdf1.format(date);
	}
	
	public static final String reverseFormatForThrougput(Long date) {
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat(formatted);
			SimpleDateFormat sdf2 = new SimpleDateFormat(yyyyMMddHHmmss);
		
			sdf1.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
			sdf2.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
			
			return sdf1.format(sdf2.parse("" + date));
		} catch (Exception e) {
			return "EXP-" + date;
		}
	}
	
	public static final Long formatDefault(Date date) {
		return Long.valueOf(format(yyyyMMddHHmmssSSS, date));
	}
	
	public static final String format(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
		return sdf.format(date);
	}
	
	public static final String formatNowSave(Long now) {
		if (now == null || now == 0L) {
			return "N/A";
		}
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat(formatted);
			SimpleDateFormat sdf2 = new SimpleDateFormat(yyyyMMddHHmmssSSS);
		
			sdf1.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
			sdf2.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
			
			return sdf1.format(sdf2.parse("" + now));
		} catch (Exception e) {
			return "EXP-" + now;
		}
	}
	
	public static final Date nowDate() {
		Calendar cal =  Calendar.getInstance();
		return cal.getTime();
	}
	
	public static final Date nowDate(int diffMinute) {
		Calendar cal =  Calendar.getInstance();
		cal.add(Calendar.SECOND, diffMinute);
		return cal.getTime();
	}
}
