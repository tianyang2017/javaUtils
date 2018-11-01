package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author 张俊 2013-9-13
 */

public class DateUtil {

	/**
	 * 默认值：1900-01-01（时间）<br>
	 * 主要用于对非空日期的初始化
	 */
	public static final Date DEFAULT_NULL_DATE = new GregorianCalendar(1900, 0,
			1).getTime();
	public static final String DOB = "19000101";

	/**
	 * 获取N天前的日期
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, Integer day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 获取N天后的日期
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, Integer day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();

	}

	/**
	 * 获取当月的最后一天日期
	 */

	public static Date getLastDay(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);

		return calendar.getTime();
	}

	public static Date parseDate(String jsonString, String DataFormat) {
		SimpleDateFormat df = new SimpleDateFormat(DataFormat);
		Date date = null;
		try {
			date = df.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String formatDate(Date jsonString, String DataFormat) {
		SimpleDateFormat df = new SimpleDateFormat(DataFormat);
		return df.format(jsonString);
	}

	/**
	* yyyy-MM-dd字符串转换成日期
	* 
	* @param str
	* @return date
	*/
	public static Date StrToDate(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}



	/***
	 * @description: 得到传入时间当天零点的时间戳
	 * @param date 传入时间
	 * @return : long 零点时间戳
	*/
	public static long getZeroTime(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime().getTime();
	}
	
	
    /**
     * 获取当前日期时间
     *
     * @return 返回当前时间的字符串值
     */
    public static String currentDateTime() {
        return   new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
    }
}
