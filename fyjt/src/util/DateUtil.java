package util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期工具类
 * 
 * @author hxw
 * 
 */
public class DateUtil {
	/**
	 * 短日期格式 (yyyy-MM-dd)
	 */
	public static final String SHORT_DATE_PATTERN = "yyyy-MM-dd";
	/**
	 * 短日期格式 (yyyy-MM-dd)
	 */
	public static final String SHORT_DATE_PATTERN_ZW = "yyyy年MM月dd日";
	

	/**
	 * 短日期格式(无分隔符) (yyyyMMdd)
	 */
	public static final String SHORT_DATE_PATTERN_NO_DELIMITER = "yyyyMMdd";

	/**
	 * 长日期格式 (yyyy-MM-dd HH:mm:ss)
	 */
	public static final String LONG_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 长日期格式 (yyyy-MM-dd HH:mm:ss)
	 */
	public static final String LONG_DATE_PATTERN_FEN = "yyyy-MM-dd HH:mm";
	
	/**
	 * 长日期格式(yyyyMMDDHHmmss)
	 */
	public static final String LONG_DATE_NO_SIGN_PATTERN = "yyyyMMddHHmmss";
	/**
	 * 时间：一分钟
	 */
	private static final long MS_ONE_MINUTE = 60 * 1000;
	/**
	 * 时间：一小时
	 */
	private static final long MS_ONE_HOUR = 60 * MS_ONE_MINUTE;
	/**
	 * 时间：一天
	 */
	private static final long MS_ONE_DAY = 24 * 60 * MS_ONE_MINUTE;
	/**
	 * 时间：一周
	 */
	private static final long MS_ONE_WEEK = 7 * MS_ONE_DAY;
	/**
	 * 时间：一个月
	 */
	private static final long MS_ONE_MONTH = 30 * MS_ONE_DAY;
	/**
	 * 时间：一年
	 */
	private static final long MS_ONE_YEAR = 365 * MS_ONE_DAY;

	private DateUtil() {
	}

	/**
	 * 获取当前日期，短日期格式yyyy-MM-dd
	 * 
	 * @return 当前日期
	 */
	public static String getCurrentDate() {
		return formatShort(new Date());
	}

	/**
	 * 获取当前时间，长日期格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 当前时间
	 */
	public static String getCurrentTime() {
		return formatLong(new Date());
	}

	/**
	 * 获取当前时间戳
	 * 
	 * @return 当前时间戳
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 按照指定格式解析字符串型日期值为日期对象
	 * 
	 * @param date
	 *            字符串型日期
	 * @param pattern
	 *            日期格式
	 * @return 日期对象
	 */
	public static Date parse(String date, String pattern) {
		if (date==null) {
			return null;
		}
		DateFormat formater = new SimpleDateFormat(pattern);
		try {
			return formater.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 按照指定格式转换日期对象为字符串型日期
	 * 
	 * @param date
	 *            日期对象
	 * @param pattern
	 *            日期格式
	 * @return 字符串型日期
	 */
	public static String format(Date date, String pattern) {
		if(date==null){
			return "";
		}
		DateFormat formater = new SimpleDateFormat(pattern);
		return formater.format(date);
	}

	/**
	 * 按照短日期格式(yyyy-MM-dd)解析字符串型日期值为日期对象
	 * 
	 * @param date
	 *            字符串型日期
	 * @return 日期对象
	 */
	public static Date parseShort(String date) {
		return parse(date, SHORT_DATE_PATTERN);
	}

	/**
	 * 按照短日期格式(yyyyMMdd)转换日期对象为字符串型日期
	 * 
	 * @param date
	 *            日期对象
	 * @return 字符串型日期
	 */
	public static String formatShortNoDelimiter(Date date) {
		return format(date, SHORT_DATE_PATTERN_NO_DELIMITER);
	}

	/**
	 * 按照短日期格式(yyyy-MM-dd)转换日期对象为字符串型日期
	 * 
	 * @param date
	 *            日期对象
	 * @return 字符串型日期
	 */
	public static String formatShort(Date date) {
		return format(date, SHORT_DATE_PATTERN);
	}
	/**
	 * 按照短日期格式(yyyy年MM月dd日)转换日期对象为字符串型日期
	 * 
	 * @param date
	 *            日期对象
	 * @return 字符串型日期
	 */
	public static String formatShortZw(Date date) {
		return format(date, SHORT_DATE_PATTERN_ZW);
	}
	/**
	 * 按照日期格式(yyyyMMddHHmmss)转换日期对象为字符串日期
	 * 
	 * @param date
	 *            日期对象
	 * @return 字符串日期
	 */
	public static String fromatShortNoSign(Date date) {
		return format(date, LONG_DATE_NO_SIGN_PATTERN);
	}

	/**
	 * 按照长日期格式(yyyy-MM-dd HH:mm:ss)解析字符串型日期值为日期对象
	 * 
	 * @param date
	 *            字符串型日期
	 * @return 日期对象
	 */
	public static Date parseLong(String date) {
		return parse(date, LONG_DATE_PATTERN);
	}

	/**
	 * 分钟
	 * 按照长日期格式(yyyy-MM-dd HH:mm)解析字符串型日期值为日期对象
	 * 
	 * @param date
	 *            字符串型日期
	 * @return 日期对象
	 */
	public static Date parseLongFen(String date) {
		return parse(date, LONG_DATE_PATTERN_FEN);
	}
	/**
	 * 按照长日期格式(yyyy-MM-dd HH:mm:ss)解析为日期类型
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseLong(Date date) {
		return parseLong((format(date, LONG_DATE_PATTERN)));
	}

	/**
	 * 按照长日期格式(yyyy-MM-dd HH:mm:ss)转换日期对象为字符串型日期
	 * 
	 * @param date
	 *            日期对象
	 * @return 字符串型日期
	 */
	public static String formatLong(Date date) {
		return format(date, LONG_DATE_PATTERN);
	}
	/**
	 * 按照长日期格式(yyyy-MM-dd HH:mm)转换日期对象为字符串型日期
	 * 
	 * @param date
	 *            日期对象
	 * @return 字符串型日期
	 */
	public static String formatLongFen(Date date) {
		return format(date, LONG_DATE_PATTERN_FEN);
	}
	/**
	 * 获取指定时间的日历对象
	 * 
	 * @param date
	 *            时间
	 * @return 日历对象
	 */
	public static Calendar getCalendar(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * 计算指定两个时间之间的相差天数。如果earlierDate晚于laterDate，则返回负值
	 * 
	 * @param earlierDate
	 *            较早时间
	 * @param laterDate
	 *            较晚时间
	 * @return 天数差
	 */
	public static int daysBetween(Date earlierDate, Date laterDate) {
		long dms = laterDate.getTime() - earlierDate.getTime();
		return (int) (dms / MS_ONE_DAY);
	}

	/**
	 * 计算指定两个时间之间的相差小时之差。如果earlierDate晚于laterDate，则返回负值
	 * 
	 * @param earlierDate
	 *            较早时间
	 * @param laterDate
	 *            较晚时间
	 * @return 小时之差
	 */
	public static int hoursBetween(Date earlierDate, Date laterDate) {
		long dms = laterDate.getTime() - earlierDate.getTime();
		return (int) (dms / MS_ONE_HOUR);
	}

	public static int timeDifference(Date earlierDate, Date laterDate) {

		long dms = laterDate.getTime() - earlierDate.getTime();
		long fiveMinute = MS_ONE_MINUTE * 5;
		long tenMinute = MS_ONE_MINUTE * 10;
		long thirtyMinute = MS_ONE_MINUTE * 30;
		long fiveHour = MS_ONE_HOUR * 5;
		long tenHour = MS_ONE_HOUR * 10;
		long trenty_threeHour = MS_ONE_HOUR * 23;
		long twoDay = MS_ONE_DAY * 2;
		long twoWeek = MS_ONE_WEEK * 2;
		long twoMonth = MS_ONE_MONTH * 2;
		long twoYear = MS_ONE_YEAR * 2;

		// 小于5分钟：刚刚
		if (dms < fiveMinute) {
			return 1;
		}
		// 大于5分钟而小于10分钟：10分钟之前
		if ((dms > fiveMinute) && dms < tenMinute) {
			return 2;
		}
		// 大于10分钟而小于30分钟：30分钟之前
		if (dms > tenMinute && dms < thirtyMinute) {
			return 3;
		}
		// 大于30分钟而小于1小时：1小时之前
		if (dms > thirtyMinute && dms < MS_ONE_HOUR) {
			return 4;
		}
		// 大于1小时而小于5小时：5小时之前
		if (dms > MS_ONE_HOUR && dms < fiveHour) {
			return 5;
		}
		// 大于5小时而小于10小时：10小时之前
		if (dms > fiveHour && dms < tenHour) {
			return 6;
		}
		// 大于10小时而小于23小时：23小时之前
		if (dms > tenHour && dms < trenty_threeHour) {
			return 7;
		}
		// 大于23小时而小于1天：1天之前
		if (dms > trenty_threeHour && dms < MS_ONE_DAY) {
			return 8;
		}
		// 大于1天而小于2天：2天之前
		if (dms > MS_ONE_DAY && dms < twoDay) {
			return 9;
		}
		// 大于2天而小于1周：1周之前
		if (dms > twoDay && dms < MS_ONE_WEEK) {
			return 10;
		}
		// 大于1周而小于2周：2周之前
		if (dms > MS_ONE_WEEK && dms < twoWeek) {
			return 11;
		}
		// 大于2周而小于1个月：1个月之前
		if (dms > twoWeek && dms < MS_ONE_MONTH) {
			return 12;
		}
		// 大于1个月而小于2个月：2个月之前
		if (dms > MS_ONE_MONTH && dms < twoMonth) {
			return 13;
		}
		// 大于2个月而小于1年：1年之前
		if (dms > twoMonth && dms < MS_ONE_YEAR) {
			return 14;
		}
		// 大于1年而小于2年：2年之前
		if (dms > MS_ONE_YEAR && dms < twoYear) {
			return 15;
		}

		return 16;

	}

	/**
	 * 计算指定两个时间之间的相差分钟数。如果earlierDate晚于laterDate，则返回负值
	 * 
	 * @param earlierDate
	 *            较早时间
	 * @param laterDate
	 *            较晚时间
	 * @return 分钟差
	 */
	public static int minutesBetween(Date earlierDate, Date laterDate) {
		long dms = laterDate.getTime() - earlierDate.getTime();
		return (int) (dms / MS_ONE_MINUTE);
	}

	/**
	 * 获取指定日期加上指定年份值。
	 * 
	 * @param date
	 *            原日期
	 * @param year
	 *            年份
	 * @return 计算后新日期
	 */
	public static Date addYear(Date date, int year) {
		Calendar cal = getCalendar(date);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}

	/**
	 * 获取指定日期加上指定月份值，负数表示时间向后推移。
	 * 
	 * @param date
	 *            原日期
	 * @param months
	 *            月份
	 * @return 计算后新日期
	 */
	public static Date addMonths(Date date, int months) {
		Calendar cal = getCalendar(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	/**
	 * 获取指定日期加上指定天数后的日期值。若天数为负，则实际进行减操作。
	 * 
	 * @param date
	 *            原日期
	 * @param days
	 *            天数
	 * @return 计算后的新日期
	 */
	public static Date addDays(Date date, int days) {
		Calendar c = getCalendar(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	/**
	 * 获取指定日期加上指定小时数后的日期值。若小时数为负，则实际进行减操作。
	 * 
	 * @param date
	 *            原日期
	 * @param hours
	 *            小时数
	 * @return 计算后的新日期
	 */
	public static Date addHours(Date date, int hours) {
		Calendar c = getCalendar(date);
		c.add(Calendar.HOUR_OF_DAY, hours);
		return c.getTime();
	}

	/**
	 * 获取指定日期加上指定分钟数后的日期值。若分钟数为负，则实际进行减操作。
	 * 
	 * @param date
	 *            原日期
	 * @param hours
	 *            分钟数
	 * @return 计算后的新日期
	 */
	public static Date addMinutes(Date date, int minutes) {
		Calendar c = getCalendar(date);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}

	/**
	 * 为指定日期设置年月日，返回新日期
	 * 
	 * @param date
	 *            原日期
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 新日期
	 */
	public static Date setDate(Date date, int year, int month, int day) {
		Calendar c = getCalendar(date);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DATE, day);
		return c.getTime();
	}

	/**
	 * 为指定日期设置时分秒毫秒，返回新日期
	 * 
	 * @param date
	 *            原日期
	 * @param hour
	 *            时
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 * @param millisecond
	 *            毫秒
	 * @return 新日期
	 */
	public static Date setTime(Date date, int hour, int minute, int second,
			int millisecond) {
		Calendar c = getCalendar(date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		c.set(Calendar.MILLISECOND, millisecond);
		return c.getTime();
	}

	/**
	 * @Description:获取当前日期的年份
	 * @author zhb
	 * @return 当前的年份
	 */
	public static String getCurrentDateYear() {
		return getCurrentDate("year");
	}

	/**
	 * @Description: 获取当前日期的月份
	 * @author zhb
	 * @return 当前月份
	 */
	public static String getCurrentDateMonth() {
		return getCurrentDate("month");
	}

	/**
	 * 获取上个月的年和月 ,比如当前为2011-1 那么取出来的就是2010-12月
	 * 
	 * @author zhb
	 * @return 上个月的年和月
	 */
	public static String getPreviousMonthYAndM() {

		return fromatYearAndMonth(getPreviousMonthFirst());
	}

	/**
	 * 获取当前时间的年和月 比如(2010-12)
	 * 
	 * @author zhb
	 * @return 当前时间的年和月 比如(2010-12)
	 */
	public static String getCurrentYearAndMonth() {

		return fromatYearAndMonth(getCurrentMonthFirst());
	}

	/**
	 * 将指定日期格式化为：年－月 比如2011-10
	 * 
	 * @param date
	 *            日期
	 * @return 年－月
	 */
	public static String fromatYearAndMonth(Date date) {
		String str[] = formatShort(date).split("-");
		int i = 0;
		return str[i] + "-" + str[++i];
	}

	/**
	 * @Description:获取当前日期的天数
	 * @author zhb
	 * @return 当前天数
	 */
	public static String getCurrentDateDay() {
		return getCurrentDate("day");
	}

	/**
	 * @Description:获取所需的当前日期相应的内容（如年，月，天）
	 * @author zhb
	 * @param type
	 *            内容类型
	 * @return 日期所包含的相应内容
	 */
	private static String getCurrentDate(String type) {
		Calendar calendar = Calendar.getInstance();
		int day = 0;
		int month = 0;
		int year = 0;
		if (type.equals("day")) {
			day = calendar.get(Calendar.DAY_OF_MONTH);
			return adjustDate(day);
		} else if (type.equals("month")) {
			month = calendar.get(Calendar.MONTH) + 1;
			return adjustDate(month);
		} else if (type.equals("year")) {
			year = calendar.get(Calendar.YEAR);
			return adjustDate(year);
		}
		return getCurrentDate();
	}

	/**
	 * 
	 * @Description:调整适当的日期格式
	 * @author zhb
	 * @param date
	 *            日期
	 * @return 合适的日期格式
	 */
	private static String adjustDate(int date) {
		if (date < 10) {
			return "0" + date;
		}
		return String.valueOf(date);
	}

	/**
	 * 获取当月第一天的日期
	 * 
	 * @author zhb
	 * @return 当月第一天的日期
	 */
	public static Date getCurrentMonthFirst() {
		Calendar current = Calendar.getInstance();
		return getMonthFirst(current);
	}

	/**
	 * 获取给定日期的月份的开始一天
	 * 
	 * @param calendar
	 * @return Date
	 */
	public static Date getMonthFirst(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 获取当月最后一天的日期
	 * 
	 * @author zhb
	 * @return 当月最后一天的日期
	 */
	public static Date getCurrentMonthEnd() {
		Calendar current = Calendar.getInstance();

		/*
		 * current.set(Calendar.DATE, 1); current.roll(Calendar.DATE, -1); //
		 * 日期回滚一天，也就是本月最后一天
		 */
		return getMonthEnd(current);
	}

	/**
	 * 获取给定日期的月份的最后一天
	 * 
	 * @param calendar
	 * @return
	 */
	public static Date getMonthEnd(Calendar calendar) {
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1); // 日期回滚一天，也就是月最后一天

		return calendar.getTime();
	}

	/**
	 * 获取上月第一天的日期
	 * 
	 * @author zhb
	 * @return 上月第一天的日期
	 */
	public static Date getPreviousMonthFirst() {

		Calendar current = Calendar.getInstance();

		current.set(Calendar.DATE, 1); // 设为当前月的1号
		current.add(Calendar.MONTH, -1);
		return current.getTime();
	}

	public static Date getCurrentDateTime() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 获取上月最后一天的日期
	 * 
	 * @author zhb
	 * @return 上月最后一天的日期
	 */
	public static Date getPreviousMonthEnd() {

		Calendar current = Calendar.getInstance();
		current.add(Calendar.MONTH, -1); // 减一个月
		current.set(Calendar.DATE, 1); // 把日期设置为当月第一天
		current.roll(Calendar.DATE, -1); // 日期回滚一天，也就是本月最后一天

		return current.getTime();
	}

	/**
	 * 获取下月第一天的日期
	 * 
	 * @author zhb
	 * @return 下月第一天的日期
	 */
	public static String getNextMonthFirst() {

		Calendar current = Calendar.getInstance();
		current.add(Calendar.MONTH, 1); // 减一个月
		current.set(Calendar.DATE, 1); // 把日期设置为当月第一天

		return formatShort(current.getTime());
	}

	/**
	 * 获取下月最后一天的日期
	 * 
	 * @author zhb
	 * @return 下月最后一天的日期
	 */
	public static String getNextMonthEnd() {

		Calendar current = Calendar.getInstance();
		current.add(Calendar.MONTH, 1); // 加一个月
		current.set(Calendar.DATE, 1); // 把日期设置为当月第一天
		current.roll(Calendar.DATE, -1); // 日期回滚一天，也就是本月最后一天

		return formatShort(current.getTime()) + "";
	}

	/**
	 * 根据一个String类型的日期返回一个Calendar
	 * 
	 * @param date
	 *            date中包含年月，如2011-02,注意分隔符请一定用"-"
	 * @return Calendar
	 */
	public static Calendar getCalendarByStringDate(String date) {
		Calendar calendar = Calendar.getInstance();
		if (date.split("-").length >= 2) {
			calendar.set(Calendar.YEAR, Integer.parseInt(date.split("-")[0]));
			calendar.set(Calendar.MONTH,
					Integer.parseInt(date.split("-")[1]) - 1);
		}
		return calendar;
	}

	public static void main(String[] args) {
		// System.out.println(parse("2008-12-12", "yyyy-MM-dd"));
		// System.out.println(format(new Date(), "yyyy-MM-dd"));

		// System.out.println("当期日期+1天后为："
		// + formatShort(addDays(parseShort(getCurrentDate()), -27)));
		// int day = getCurrentDate("day", addDays(parseShort(getCurrentDate()),
		// -27));
		// int month = getCurrentDate("month", addDays(
		// parseShort(getCurrentDate()), -27));
		// int year = getCurrentDate("year",
		// addDays(parseShort(getCurrentDate()),
		// -27));
		// System.out.println(day);
		// System.out.println(month);
		// System.out.println(year);
		// System.out.println(getNextMonthFirst());
		// System.out.println(getNextMonthEnd());
		// System.out.println(getPreviousMonthFirst());
		// System.out.println(getPreviousMonthEnd());
//		Date t1 = parseLong("2011-5-12 16:18:20");
//		Date t2 = parseLong("2011-5-13 17:18:20");
		System.out.println(new Date().toString());
		// System.out.println(timeDifference(t1, t2));

	}

}
