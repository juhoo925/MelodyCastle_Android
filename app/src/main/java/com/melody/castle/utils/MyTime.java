package com.melody.castle.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MyTime {
	public static String getCorrectTime( String time )
	{
		if( time == null )
			return getCurrentTime();
		
		if( time.length() > 19 )
			time = time.substring(0, 19);
		
		String yearPattern = "((19|20)\\d\\d)-";
		String monthPattern = "(0?[1-9]|1[012])-";
		String dayPattern = "(0?[1-9]|[12][0-9]|3[01]) ";
		String hourPattern = "(0?[0-9]|[1][0-9]|2[0-3]):";
		String minutePattern = "(0?[0-9]|[1-5][0-9]):";
		String secondePattern = "(0?[0-9]|[1-5][0-9])";
		
		String timePattern = yearPattern + monthPattern + dayPattern + hourPattern + minutePattern + secondePattern;
		
		if( AlgorithmUtils.isEmpty(time) || time.matches(timePattern) == false )
		{
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = format.format(date);			
		}
		
		return time;
	}
	
	public static String getCurrentTime()
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		
		return time;
	}
	
	public static String getCurrentDate()
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		
		return time;
	}
	
	public static String getCurrentDateTimeForEnglish()
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy HH:mm",  Locale.ENGLISH);
		String time = format.format(date);
		
		return time;
	}

	public static String getCurrentDateForEnglish()
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy",  Locale.ENGLISH);
		String time = format.format(date);
		
		return time;
	}
	
	
	public static String getCurrentDateForEnglish(long timeMilli)
	{
		Date date = new Date(timeMilli);
		SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy HH:mm",  Locale.ENGLISH);
		String time = format.format(date);
		
		return time;
	}
}
