package com.jan.learning.web.rest.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MyDateUtil {

    //"CST" give me "CDT" date on console
    //"IST" gives me "IST"date on console
    //"CDT" gives me "GMT"date on console

    public static TimeZone timeZone = TimeZone.getTimeZone("IST");
    public static DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    public static DateFormat datetime_formatter = new SimpleDateFormat("dd-MM-yyyy hh.mm aa");

    static {
        TimeZone.setDefault(timeZone);
    }

    public static void main(String[] args) {
        System.out.println("Print Today " + getToday());
        System.out.println("Print Yesterday " + getYesterday());
        System.out.println("Print Firstday of the month " + getFirstDateOfCurrentMonth());
    }

    public static Date getYesterday() {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.add(Calendar.DATE, -1);
        return getDateWithoutTime(cal.getTime());
    }

    public static Date getToday() {
        Calendar cal = Calendar.getInstance(timeZone);

        return getDateWithoutTime(cal.getTime());
    }

    public static Date getDateWithoutTime(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    /*public static Date  getDateWithoutTime(Date dt) {

		Date dayWithZeroTime = null;
		try {
			dayWithZeroTime = formatter.parse(formatter.format(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dayWithZeroTime;
	}*/

    /*private Date getToday()   {
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance(timeZone);//Today

		//formatter.setTimeZone(timeZone);

		Date today =null;
		try {
			today = formatter.parse(formatter.format(calendar.getTime()));
			System.out.println("TODAY "+ today);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return today;
	}*/

    public static Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return getDateWithoutTime(cal.getTime());
    }

    public static Date getAddedMinutesTime(int waitMinuntes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, waitMinuntes);
        Date later = now.getTime();

        try {
            later = datetime_formatter.parse(datetime_formatter.format(later));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return later;
    }
}
