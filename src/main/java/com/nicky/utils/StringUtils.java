package com.nicky.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 更改所生成类型注释的模板为 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class StringUtils {
	// time format
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_1 = "yyyy/MM/dd";
	public static final String DATE_FORMAT_2 = "dd/MM/yyyy";
	public static final String TIME_FORMAT = "HH:mm";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String LONG_DATE_TIME_FORMAT = "yyMMddhhmmssSSS";

	public static final String GBK_CHARSET = "GBK";
	public static final String ISO_8859_1 = "ISO-8859-1";

	// boolean format
	public static final int BOOLEAN_01 = 0;
	public static final int BOOLEAN_YESNO = 1;
	public static final int BOOLEAN_TRUEFLASE = 2;

	// pad format
	public static final int STR_PAD_LEFT = 1;
	public static final int STR_PAD_RIGHT = 2;
	public static final int STR_PAD_BOTH = 3;

	// email address pattern
	public static final String EMAIL_ADDRESS_PATTERN = "^(\\w+([-+.]\\w+)*@\\w+([-]\\w+)*.\\w+)$|^(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+).\\w+$";

	private static Hashtable htmlEntityTable = null;
	private static final String[] HTML_ENTITIES = { ">", "&gt;", "<", "&lt;", "&", "&amp;", "\"", "&quot;", "'",
			"&#039;", "//", "&#092;", "\u00a9", "&copy;", "\u00ae", "&reg;" };

	// BSC00015P
	private static final String beginFormula = "{#";
	private static final String endFormula = "}";
	// system root path
	public static String sysRootPath = "";

	public static final String FOLDER_SEPARATOR = "/";

	public static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	public static final String TOP_PATH = "..";

	public static final String CURRENT_PATH = ".";

	public static final char EXTENSION_SEPARATOR = '.';

	public static final char LF = '\n';

	public static final char CR = '\r';

	public static final String EMPTY_STRING = "";
	public static final char DOT = '.';
	public static final char UNDERSCORE = '_';
	public static final String COMMA_SPACE = ", ";
	public static final String COMMA = ",";
	public static final String OPEN_PAREN = "(";
	public static final String CLOSE_PAREN = ")";
	public static final char SINGLE_QUOTE = '\'';

	/**
	 * return null input string as emptry string
	 * 
	 * @param s
	 *            input String
	 * @return String
	 */
	public static String null2Str(String s) {
		return null2Str(s, "");
	}

	/**
	 * return null input string as emptry string
	 * 
	 * @param s
	 *            input String
	 * @param def
	 *            default String if input s is null
	 * @return String
	 */
	public static String null2Str(String s, String def) {
		if (isNull(s))
			return def;
		else
			return s.trim();
	}

	/**
	 * check if the input string is null
	 * 
	 * @param s
	 *            String
	 * @return boolean
	 */
	public static boolean isNull(String s) {
		if (s == null || s.trim().equals(""))
			return true;
		else
			return false;
	}

	/**
	 * return 0 if input string is null or integer of input string
	 * 
	 * @param s
	 *            input String
	 * @return int
	 */
	public static int str2Int(String s) {
		int i = 0;
		try {
			// if the number format is "2,000" , clean the ","
			i = Integer.parseInt(strReplace(",", "", null2Str(s, "0")));
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	/**
	 * return 0 if input string is null or integer of input string
	 * 
	 * @param s
	 *            input String
	 * @return long
	 */
	public static long str2Long(String s) {
		long i = 0;
		try {
			// if the number format is "2,000" , clean the ","
			i = Long.parseLong(strReplace(",", "", null2Str(s, "0")));
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	/**
	 * return String of input integer
	 * 
	 * @param i
	 *            integer
	 * @return String
	 */
	public static String int2Str(int i) {
		return int2Str(i, false);
	}

	/**
	 * return String of input integer
	 * 
	 * @param i
	 *            integer
	 * @param bShowZero
	 *            boolean
	 * @return String
	 */
	public static String int2Str(int i, boolean bShowZero) {
		return int2Str(i, bShowZero, false);
	}

	/**
	 * return String of input integer
	 * 
	 * @param i
	 *            integer
	 * @param bShowZero
	 *            boolean
	 * @param bShowGroup
	 *            boolean
	 * @return String
	 */
	public static String int2Str(int i, boolean bShowZero, boolean bShowGroup) {
		if (!bShowZero && i == 0) {
			return "";
		} else {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(bShowGroup);
			nf.setMinimumIntegerDigits(1);
			nf.setMaximumFractionDigits(0);
			nf.setMinimumFractionDigits(0);
			return nf.format(i);
		}

	}

	/**
	 * return String of input double
	 * 
	 * @param d
	 * @return
	 */
	public static String double2Str(double d) {
		return double2Str(d, 2);
	}

	/**
	 * return String of input double with decimal points
	 * 
	 * @param d
	 * @param decimalPoints
	 * @return
	 */
	public static String double2Str(double d, int decimalPoints) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(true);
		nf.setMinimumIntegerDigits(1);
		nf.setMaximumFractionDigits(decimalPoints);
		nf.setMinimumFractionDigits(decimalPoints);
		return nf.format(d);
	}

	/**
	 * 按小数位四舍五入
	 * 
	 * @param d
	 * @return
	 */
	public static Double doubleRound(double d, int decimalPoints) {
		double p = Math.pow(10, decimalPoints);
		return Math.round(d * p) / p;
	}

	/**
	 * return double of input string value (can input format is "2,000" )
	 * 直接截取固定小数位,未四舍五入
	 * 
	 * @param s
	 * @return
	 */
	public static double str2Double(String s) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(true);
		try {
			// if the number format is "2,000" , clean the ","
			return nf.parse(strReplace(",", "", null2Str(s, "0"))).doubleValue();
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * return Date of input string
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static Date str2Date(String s) {
		return str2Date(s, DATE_FORMAT);
	}

	/**
	 * return Date of input string
	 *
	 * @param s
	 *            String
	 * @param format
	 *            String
	 * @return String
	 */
	public static Date str2Date(String s, String format) {
		Date dRet = null;
		SimpleDateFormat sdf = null;
		try {
			sdf = new SimpleDateFormat(format);
			dRet = sdf.parse(s);
			return dRet;
		} catch (ParseException pe) {
		}
		try {
			sdf = new SimpleDateFormat(DATE_FORMAT);
			dRet = sdf.parse(s);
			return dRet;
		} catch (ParseException pe) {
		}
		try {
			sdf = new SimpleDateFormat(DATE_FORMAT_1);
			dRet = sdf.parse(s);
			return dRet;
		} catch (ParseException pe) {
		}
		try {
			sdf = new SimpleDateFormat(DATE_FORMAT_2);
			dRet = sdf.parse(s);
			return dRet;
		} catch (ParseException pe) {
		}

		return dRet;
	}

	/**
	 * return String of input date
	 *
	 * @param d
	 *            Date
	 * @return String
	 */
	public static String date2Str(Date d) {
		return date2Str(d, DATE_FORMAT);
	}

	/**
	 * return String of input date
	 *
	 * @param d
	 *            Date
	 * @param format
	 *            String
	 * @return String
	 */
	public static String date2Str(Date d, String format) {
		if (d == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}

	/**
	 * return String of input date
	 *
	 * @param d
	 *            Date
	 * @return String
	 */
	public static String time2Str(Date d) {
		return date2Str(d, TIME_FORMAT);
	}

	/**
	 * return String of input date
	 *
	 * @param d
	 *            Date
	 *            String
	 * @return String
	 */
	public static String datetime2Str(Date d) {
		if (d == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		return sdf.format(d);
	}

	/**
	 * 将String类型日期转化成java.sql.Date类型"2003-01-01"格式
	 *
	 * @param str
	 *            String
	 * @param format
	 *            String
	 * @return Date
	 */
	public static java.sql.Date str2SqlDate(String str, String format) {
		if (str == null || format == null) {
			return null;
		}
		return new java.sql.Date(str2Date(str, format).getTime());
	}

	/**
	 * 将String类型日期转化成java.sql.Date类型"2003-01-01"格式
	 *
	 * @param str
	 *            String
	 * @return Date
	 */
	public static java.sql.Date str2SqlDate(String str) {

		if (str == null) {
			return null;
		}
		return new java.sql.Date(str2Date(str).getTime());
	}

	/**
	 * 将String 类型转化为boolean类型,接受"true" ,"false" , "0" ,
	 * "1","Y","N","YES","NO"等类型,其他形式为false返回
	 *
	 */
	public static boolean str2boolean(String str) {
		if (str == null) {
			return false;
		} else if (str.equalsIgnoreCase("true") || str.equals("1") || str.equalsIgnoreCase("Y")
				|| str.equalsIgnoreCase("YES")) {
			return true;
		} else if (str.equalsIgnoreCase("false") || str.equals("0") || str.equalsIgnoreCase("N")
				|| str.equalsIgnoreCase("NO")) {
			return false;
		} else {
			return false;
		}
	}

	/**
	 * 将boolean类型转化为String 类型输出,格式有"0","1","Y","N","T","F"等类型,默认使用"0","1"
	 * @return
	 */
	public static String boolean2Str(boolean bl, int format) {
		switch (format) {
		case BOOLEAN_01: {
			return bl ? "1" : "0";
		}
		case BOOLEAN_YESNO: {
			return bl ? "Y" : "N";
		}
		case BOOLEAN_TRUEFLASE: {
			return bl ? "T" : "F";
		}
		default:
			return "0";
		}
	}

	/**
	 * 将boolean类型转化为String 类型输出,默认使用"0","1"
	 */
	public static String boolean2Str(boolean bl) {
		return boolean2Str(bl, BOOLEAN_01);
	}

	/**
	 * 将java.util.Date日期转化成java.sql.Date类型
	 */
	public static java.sql.Date toSqlDate(Date date) {

		if (date == null) {
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 将java.util.Date日期转化成java.sql.Timestamp类型
	 *
	 * @return 格式化后的java.sql.Timestamp
	 */
	public static java.sql.Timestamp toSqlTimestamp(Date date) {

		if (date == null) {
			return null;
		}
		return new java.sql.Timestamp(date.getTime());
	}

	/**
	 * 将日历转化为日期
	 *
	 * @param calendar
	 *            Calendar
	 * @return Date
	 */
	public static Date converToDate(Calendar calendar) {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 将日期转化为日历
	 *
	 * @param date
	 *            Date
	 * @return Calendar
	 */
	public static Calendar converToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 求得从某天开始，过了几年几月几日几时几分几秒后，日期是多少 几年几月几日几时几分几秒可以为负数
	 *
	 * @param date
	 *            Date
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @param day
	 *            int
	 * @param hour
	 *            int
	 * @param min
	 *            int
	 * @param sec
	 *            int
	 * @return Date
	 */
	public static Date modifyDate(Date date, int year, int month, int day, int hour, int min,
			int sec) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DATE, day);
		cal.add(Calendar.HOUR, hour);
		cal.add(Calendar.MINUTE, min);
		cal.add(Calendar.SECOND, sec);

		return cal.getTime();

	}

	/**
	 * 取得当前日期时间 1:year 2:month 3:date 4:day
	 */
	public static int getCurTime(int i) {
		if (i == 1) {
			return Calendar.getInstance().get(Calendar.YEAR);
		} else if (i == 2) {
			return Calendar.getInstance().get(Calendar.MONTH) + 1;
		} else if (i == 3) {
			return Calendar.getInstance().get(Calendar.DATE);
		} else if (i == 4) {
			int temp = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			if (temp == 1) {
				temp = 7;
			} else {
				temp--;
			}
			return temp;
		}

		return 0;

	}

	/**
	 * Repeat string with multiplier times.
	 *
	 * @param input
	 * @param multiplier
	 * @return
	 */
	public static String strRepeat(String input, int multiplier) {
		StringBuffer sb = new StringBuffer("");
		if (isNull(input)) {
			input = " ";
		}

		for (int i = 0; i < multiplier; i++) {
			sb.append(input);
		}
		return sb.toString();
	}

	/**
	 * return string with padString and length . defualt padstring in left .
	 *
	 * @param input
	 * @param length
	 * @param padString
	 * @return
	 */
	public static String strPad(String input, int length, String padString) {

		return strPad(input, length, padString, STR_PAD_LEFT);
	}

	/**
	 * return string with padString using length .
	 *
	 * @param input
	 * @param length
	 * @param padString
	 * @param padType
	 * @return
	 */
	public static String strPad(String input, int length, String padString, int padType) {
		int multiplier = 0;
		String tmpStr = "";
		String outStr = "";
		int pos = 0;

		input = null2Str(input, "");
		padString = null2Str(padString, " ");

		if (input.length() >= length)
			return input;

		multiplier = (int) Math.ceil((double) length / (double) padString.length());
		tmpStr = strRepeat(padString, multiplier);

		if (padType == STR_PAD_RIGHT) {
			pos = length - input.length();
		} else if (padType == STR_PAD_BOTH) {
			pos = ((length - input.length()) / 2);
		} else {
			pos = 0;
		}

		if (pos > 0) {
			outStr = tmpStr.substring(0, pos);
		}
		outStr += input;
		outStr += tmpStr.substring(pos + input.length(), length);

		return outStr;
	}

	/**
	 * If Java 1.4 is unavailable, the following technique may be used.
	 *
	 * @param aInput
	 *            is the original String which may contain substring aOldPattern
	 * @param aOldPattern
	 *            is the non-empty substring which is to be replaced
	 * @param aNewPattern
	 *            is the replacement for aOldPattern
	 */
	public static String strReplace(final String aOldPattern, final String aNewPattern, final String aInput) {
		if (aOldPattern == null || aOldPattern.equals("")) {
			throw new IllegalArgumentException("Old pattern must have content.");
		}

		if (aInput == null || aInput.equals("")) {
			return aInput;
		}

		if (aNewPattern == null) {
			throw new IllegalArgumentException("New pattern must not null.");
		}

		if (aNewPattern.equals(aOldPattern))
			return aInput;

		final StringBuffer result = new StringBuffer();
		// startIdx and idxOld delimit various chunks of aInput; these
		// chunks always end where aOldPattern begins
		int startIdx = 0;
		int idxOld = 0;
		while ((idxOld = aInput.indexOf(aOldPattern, startIdx)) >= 0) {
			// grab a part of aInput which does not include aOldPattern
			result.append(aInput.substring(startIdx, idxOld));
			// add aNewPattern to take place of aOldPattern
			result.append(aNewPattern);
			// reset the startIdx to just after the current match, to see
			// if there are any further matches
			startIdx = idxOld + aOldPattern.length();
		}
		// the final chunk will go to the end of aInput
		result.append(aInput.substring(startIdx));
		return result.toString();
	}

	/**
	 * return a string arraylist separator by Separator of input.
	 *
	 * @param aSeparator
	 * @param aInput
	 * @return
	 */
	public static ArrayList strSplit(final String aSeparator, final String aInput) {
		ArrayList token = new ArrayList();

		if (aSeparator == null || aSeparator.equals("")) {
			token.add(aInput);
			return token;
		}
		if (aInput == null || aInput.equals("")) {
			return token;
		}

		// final StringBuffer result = new StringBuffer();
		// startIdx and idxOld delimit various chunks of aInput; these
		// chunks always end where aOldPattern begins
		int startIdx = 0;
		int idxOld = 0;
		while ((idxOld = aInput.indexOf(aSeparator, startIdx)) >= 0) {
			// grab a part of aInput which does not include aOldPattern

			token.add(aInput.substring(startIdx, idxOld));
			// reset the startIdx to just after the current match, to see
			// if there are any further matches
			startIdx = idxOld + aSeparator.length();
		}
		// the final chunk will go to the end of aInput
		token.add(aInput.substring(startIdx));

		return token;
	}

	/**
	 * return string show file size .
	 *
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		if (size > 0 && size < 1024) {
			return Long.toString(size) + " B";
		} else if (size >= 1024 && size < 1024 * 1024) {
			return StringUtils.double2Str((double) size / 1024) + " KB";
		} else if (size >= 1024 * 1024) {
			return StringUtils.double2Str((double) size / 1024 / 1024) + " MB";
		} else {
			return "0 B";
		}
	}

	/***
	 * Converts entity array to hashtable
	 */
	private static synchronized void buildEntityTable() {
		htmlEntityTable = new Hashtable(HTML_ENTITIES.length);

		for (int i = 0; i < HTML_ENTITIES.length; i += 2) {
			if (!htmlEntityTable.containsKey(HTML_ENTITIES[i])) {
				htmlEntityTable.put(HTML_ENTITIES[i], HTML_ENTITIES[i + 1]);
			}
		}
	}

	/***
	 * Converts a single character to HTML
	 */
	private static String encodeSingleChar(String ch) {
		return (String) htmlEntityTable.get(ch);
	}

	/***
	 * Converts a String to HTML by converting all special characters to
	 * HTML-entities.
	 */
	public final static String encodeHTML(String s) {
		final String CR = "<BR>";

		if (htmlEntityTable == null) {
			buildEntityTable();
		}
		if (s == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(s.length() * 2);

		char ch;
		for (int i = 0; i < s.length(); ++i) {
			ch = s.charAt(i);
			if ((ch >= 63 && ch <= 90) || (ch >= 97 && ch <= 122) || (ch == ' ')) {
				sb.append(ch);
			} else if (ch == '\n') {
				sb.append(CR);
			} else {
				String chEnc = encodeSingleChar(String.valueOf(ch));
				if (chEnc != null) {
					sb.append(chEnc);
				} else {
					sb.append(ch);
					/*
					 * // Not 7 Bit use the unicode system sb.append("&#");
					 * sb.append(new Integer(ch).toString()); sb.append(';');
					 */
				}
			}
		}
		return sb.toString();
	}

	/**
	 * source like: command1=value1;command2=value2 ... get the value n from
	 * command n string.
	 *
	 * @param source
	 * @param command
	 * @param delimi
	 * @return valuen
	 */
	public static String getCommand(String source, String command, String delimi) {
		String result = "";
		StringTokenizer st = new StringTokenizer(source, delimi, false);
		while (st.hasMoreTokens()) {
			StringTokenizer stcomm = new StringTokenizer(null2Str(st.nextToken()), "=", false);
			while (stcomm.hasMoreTokens()) {
				if (null2Str(stcomm.nextToken()).equalsIgnoreCase(command)) {
					result = null2Str(stcomm.nextToken());
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * change the path string use the os native string.
	 *
	 * @param path
	 * @return right path string of system.
	 */
	public static String pathName(String path) {
		String pathName = null;
		String OSName = System.getProperty("os.name");
		if (OSName.toLowerCase().indexOf("window") > 0) {
			pathName = strReplace("\\", "/", path);
		} else {
			pathName = strReplace("/", "\\", path);
		}
		return pathName;
	}

	/**
	 * change the sql string in statement using prepared SQL .
	 *
	 * @param fieldName
	 * @param values
	 * @return the string is fit for prepared sqlstatement.
	 */
	public static String concatForPreparedSQL(String fieldName, ArrayList values) {
		String result = "";
		if ((values == null) || (values.size() == 0)) {
			return " (1=1) ";
		}

		/*
		 * can't use the statment to optimize if (values.size() == 1){ return
		 * " " + fieldName + " = '" + null2Str((String)values.get(0)) + "'"; }
		 */
		for (int i = 0; i < values.size(); i++) {
			result += "?,";
		}
		result = result.substring(0, result.length() - 1);
		result = " " + fieldName + " in ( " + result + " ) ";
		return result;
	}

	/**
	 * return string of year , using 'YY' format
	 *
	 * @param year
	 * @return
	 */
	public static String strYear(int year) {
		String str = String.valueOf(year);
		if (str.length() == 4)
			str = str.substring(2);
		return str;
	}

	/**
	 * return String of month , using 'MM' format .
	 *
	 * @param month
	 * @return
	 */
	public static String strMon(int month) {
		return strPad(Integer.toString(month), 2, "0", STR_PAD_RIGHT);
	}

	/**
	 * return String Serial NO using left padding with '0'.
	 *
	 * @param serialno
	 * @param length
	 * @return
	 */
	public static String strSerial(int serialno, int length) {
		return strPad(Integer.toString(serialno), length, "0", STR_PAD_RIGHT);
	}

	/**
	 *
	 * @param str
	 * @return
	 */
	public static String strRight(String str) {
		str = "000" + str;
		int len = str.length();
		return str.substring(len - 3);
	}

	/**
	 * Validate the input string which is digit or char.
	 *
	 * @param s,
	 *            input string
	 * @return true for digit, false for char
	 * @version 1.0 shine new
	 */
	public static boolean isDigit(String s) {
		if (s == null)
			return false;
		return Pattern.matches("^\\d+$", s);
	}

	/**
	 * Count the date2 from date1 with second depart.
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getQuotSecond(Date date1, Date date2) {
		long quot = 0;
		quot = date1.getTime() - date2.getTime();
		quot = quot / 1000;
		return quot;
	}

	/**
	 * Count the date2 from date1 with minute depart.
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getQuotMinute(Date date1, Date date2) {
		long quot = 0;
		quot = date1.getTime() - date2.getTime();
		quot = quot / 1000 / 60;
		return quot;
	}

	/**
	 * Count the date2 from date1 with hour depart.
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getQuotHour(Date date1, Date date2) {
		long quot = 0;
		quot = date1.getTime() - date2.getTime();
		quot = quot / 1000 / 60 / 60;
		return quot;
	}

	/**
	 * Count the date2 from date1 with days depart.
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getQuot(Date date1, Date date2) {
		long quot = 0;
		quot = date1.getTime() - date2.getTime();
		quot = quot / 1000 / 60 / 60 / 24;
		return quot;
	}

	/**
	 * Count the datetime2 from datetime1 with days depart.
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	/**
	 * Count the datetime1 from now with days depart.
	 *
	 * @param time1
	 * @return
	 */
	public static long getQuot(String time1) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = ft.parse(time1);
			quot = new Date().getTime() - date.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	/**
	 * return the SQLCode from the SQL error messag. When input null, it will
	 * return "". e.g: input "[SQL0104] Token SELEC was not valid.", returns
	 * "SQL0104".
	 *
	 * @param errMsg,
	 *            e.g: SQLException.getMessage().
	 * @return String, e.g: "SQL0104"
	 */
	public static String getSQLErrCode(String errMsg) {
		if (errMsg == null)
			return "";

		int startIndex = errMsg.indexOf("[");
		if (startIndex == -1)
			return "";

		int endIndex = errMsg.indexOf("]");
		if (endIndex == -1)
			return "";

		if (++startIndex > endIndex)
			return "";
		return errMsg.substring(startIndex, endIndex).trim();
	}

	/**
	 * return the SQL Message from the SQL error messag. When input null, it
	 * will return "". e.g: input "[SQL0104] Token SELEC was not valid.",
	 * returns "Token SELEC was not valid.".
	 *
	 * @param errMsg,
	 *            e.g: SQLException.getMessage().
	 * @return String, e.g: "Token SELEC was not valid."
	 */
	public static String getSQLErrMessage(String errMsg) {
		if (errMsg == null)
			return "";

		Pattern pattern = Pattern.compile("\\[.*\\]");
		Matcher matcher = pattern.matcher(errMsg);
		return matcher.replaceFirst("").trim();
	}

	/**
	 * SQL Injection Charator checking .
	 *
	 * @param str
	 * @return
	 */
	public static boolean sql_inj(String str) {
		String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
		ArrayList inj_stra = strSplit(inj_str, "|");
		for (int i = 0; i < inj_stra.size(); i++) {
			if (str.indexOf((String) inj_stra.get(i)) >= 0) {
				return true;
			}
		}
		return false;
	}

	public static boolean getValidIdentityNo(String xidentityno) {
		if (xidentityno.length() != 18) {
			throw new IllegalArgumentException("身份证长度不符");
		}
		char A[] = ((xidentityno.toUpperCase()).trim()).toCharArray();
		int W[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
		int s = 0;
		int m = 0;
		int b = 0;
		for (int i = 1; i <= 17; i++) {
			b = new Integer(A[i - 1]).intValue();
			if (b < 48 || b > 57) {
				throw new IllegalArgumentException("身份证号码必需为数字");
			}
			m = str2Int(String.valueOf(A[i - 1]));
			s = s + m * W[i - 1];
		}
		int y = s % 11;
		String v = "";
		if (y == 0)
			v = "1";
		if (y == 1)
			v = "0";
		if (y == 2)
			v = "X";
		if (y >= 3 && y <= 10)
			v = new Integer(12 - y).toString();
		String idno = xidentityno.substring(0, 17) + v;
		if (!idno.equals(xidentityno)) {
			throw new IllegalArgumentException("身份证号码不对!");
		}
		return true;
	}

	/**
	 * 验证身份证（抄getValidIdentityNo 方法），区别：根据错误类型返回不同的值，而不是直接抛出错误
	 *
	 * @param xidentityno
	 * @return
	 * @version 1.1 habe
	 */
	public static int getValidIdentityNoReturnType(String xidentityno) {
		if (xidentityno.length() != 18) {
			return 1;// ("身份证长度不符");
		}
		char A[] = ((xidentityno.toUpperCase()).trim()).toCharArray();
		int W[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
		int s = 0;
		int m = 0;
		int b = 0;
		for (int i = 1; i <= 17; i++) {
			b = new Integer(A[i - 1]).intValue();
			if (b < 48 || b > 57) {
				return 2;// ("身份证号码必需为数字");
			}
			m = str2Int(String.valueOf(A[i - 1]));
			s = s + m * W[i - 1];
		}
		int y = s % 11;
		String v = "";
		if (y == 0)
			v = "1";
		if (y == 1)
			v = "0";
		if (y == 2)
			v = "X";
		if (y >= 3 && y <= 10)
			v = Integer.toString(12 - y);
		String idno = xidentityno.substring(0, 17) + v;
		if (!idno.equals(xidentityno)) {
			return 3;// ("身份证号码不对!");
		}
		return 0;
	}

	/**
	 * 验证email格式
	 *
	 * @param email
	 * @return 是否符合格式
	 */
	public static boolean verifyEmail(String email) {
		if (email == null || "".equals(email)) {
			return false;
		}
		if (email.indexOf('@') < 1) {
			return false;
		}
		return Pattern.matches(EMAIL_ADDRESS_PATTERN, email);
	}

	/**
	 * 将java.sql.Date日期转化成java.util.Date类型
	 * @return 格式化后的java.sql.Date
	 */
	public static Date toUtilDate(java.sql.Date date) {

		if (date == null) {
			return null;
		}
		return new Date(date.getTime());
	}

	/**
	 * 转换字符编码
	 *
	 * @param str
	 * @param beginCharset
	 * @param endCharset
	 * @return
	 */
	public static String changeCharset(String str, String beginCharset, String endCharset) {
		try {
			return new String(str.getBytes(beginCharset), endCharset);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * 判断sourceStr 是否符合 patern规则. 例如: 判断是否输入的是否只有数字和字母 pattern = /[A-Za-z0-9]/
	 *
	 * @param pattern
	 * @param sourceStr
	 * @return
	 */
	public static boolean testStr(String pattern, String sourceStr) {
		return Pattern.matches(pattern, sourceStr);
	}

	/**
	 * 多选的参数，转换为SP输入的parameter
	 *
	 * @param para
	 * @return
	 */
	public static String strMulti2Parameter(String para) {

		ArrayList array = StringUtils.strSplit(";", para);
		StringBuffer retString = new StringBuffer();
		int size = array.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (i != 0)
					retString.append(",");
				retString.append("'''" + (String) array.get(i) + "'''");
			}
		}
		return retString.toString();
	}

	/**
	 * 多选的参数，转换为SP输入的parameter
	 *
	 * @param para
	 * @return
	 */
	public static String strMulti2ParameterForSQL(String para) {

		ArrayList array = StringUtils.strSplit(";", para);
		StringBuffer retString = new StringBuffer();
		int size = array.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (i != 0)
					retString.append(",");
				retString.append("'" + (String) array.get(i) + "'");
			}
		}
		return retString.toString();
	}

	/**
	 * 字符串替换
	 *
	 * @param regex
	 * @param replacement
	 */
	public static String replaceAll(String regex, String replacement, String inputString) {
		if (inputString == null)
			throw new RuntimeException(" 'inputString' can not be null! ");
		return inputString.replaceAll(regex, replacement);
	}

	/**
	 * 获取特定的日期, 如无定义中的内容，则返回今天 LAST_MONTHN_FIRST_DATE = 上一个月首日
	 * LAST_MONTHN_LAST_DATE = 上一个月最后一日
	 *
	 * @return
	 */
	public static Date getDateByDefine(String define) {
		// defined : LAST_MONTHN_FIRST_DATE
		// defined : LAST_MONTHN_LAST_DATE
		Date date = new Date();
		Calendar c = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
		c.setTime(date);

		if (define.equalsIgnoreCase("LAST_MONTHN_FIRST_DATE")) {
			c.set(Calendar.DATE, 1); // 设置这个月的第一天.
			date = modifyDate(c.getTime(), 0, -1, 0, 0, 0, 0); // 上一个月首日
		} else if (define.equalsIgnoreCase("LAST_MONTHN_LAST_DATE")) {
			c.set(Calendar.DATE, 1); // 设置这个月的第一天.
			date = modifyDate(c.getTime(), 0, 0, -1, 0, 0, 0); // 上一个月最后一日
		} else if (define.equalsIgnoreCase("LAST_LAST_WEEK_SATURDAY_DATE")) {
			int dayofweek = c.get(Calendar.DAY_OF_WEEK);
			date = modifyDate(c.getTime(), 0, 0, -(dayofweek + 7), 0, 0, 0);// 上上个星期六
		} else if (define.equalsIgnoreCase("LAST_WEEK_FRIDAY_DATE")) {
			int dayofweek = c.get(Calendar.DAY_OF_WEEK);
			date = modifyDate(c.getTime(), 0, 0, -(dayofweek + 1), 0, 0, 0);// 上个星期五
		} else if (define.equalsIgnoreCase("LAST_DATE")) {
			date = modifyDate(c.getTime(), 0, 0, -1, 0, 0, 0); // 昨天
		}

		if (date == null) {
			return null;
		}
		return date;
	};

	public static java.sql.Date getSqlDate(Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * change String value to any type of sql input parameter suport :
	 * date,timestamp ,string ,bigdecimal,int,double ...
	 *
	 * @param type
	 * @param value
	 * @return
	 */
	public static Object str2Object(String type, String value) {
		Object rtn = null;

		String nullvalue = "null";
		type = null2Str(type);

		// date,timestamp,string,bigdecimal,int,double
		if (type.equals(""))
			return null;
		if (value == null)
			return null;
		value = null2Str(value);

		if (type.equalsIgnoreCase("string")) {
			return value;
		} else if (type.equalsIgnoreCase("date")) {
			return str2SqlDate(value);
		}
		return rtn;
	}

	/**
	 * 判断文件存不存在 文件要指定好相对路径
	 *
	 * @version 1.1
	 */
	public static boolean isFileExists(String fileName) {
		if ("".equals(fileName))
			return false;
		File currFile = new File(fileName);
		return currFile.exists();
	}

	/**
	 * 获取当前项目的根目录
	 *
	 * @return 系统目录名
	 * @version 1.1
	 */
	public static String getPath() {
		if ("".equals(sysRootPath)) {
			StringUtils o = new StringUtils();
			String projectPath = o.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String path = projectPath.substring(0, projectPath.lastIndexOf("/") - 1);
			sysRootPath = path.substring(0, path.lastIndexOf("/"));
		}
		return sysRootPath;

	}

	/**
	 * 判断是否表达式参数(拷贝SystemSettingDAO)
	 *
	 * @param value
	 * @return
	 * @version 1.1
	 */
	public static boolean isFormulaString(String value) {
		if (value == null)
			return false;
		int firstIndex = value.indexOf(beginFormula);
		int lastIndex = value.lastIndexOf(endFormula);
		if (lastIndex > firstIndex) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否表达式参数(拷贝SystemSettingDAO)
	 *
	 * @return
	 */
	public static String getFormulaString(String value) {
		if (value == null)
			return "";
		int firstIndex = value.indexOf(beginFormula);
		int lastIndex = value.lastIndexOf(endFormula);
		value = value.substring(firstIndex + 2, lastIndex);
		return value;
	}

	/**
	 * 获取文件名的后缀
	 *
	 * @param fileName
	 * @return
	 */
	public static String getFileExtension(String fileName) {
		return getFileExtension(fileName, "xls");// default xls
	}

	/**
	 * 获取文件名的后缀
	 *
	 * @param fileName
	 * @param defaultExtension
	 * @return
	 */
	public static String getFileExtension(String fileName, String defaultExtension) {
		if (fileName == null || fileName.equals("")) {
			return "";
		}
		int extensionIndex = fileName.lastIndexOf(".");
		if (extensionIndex < 0) {
			return "";
		} else if (extensionIndex == fileName.length() - 1) {
			return defaultExtension;
		} else {
			return fileName.substring(extensionIndex + 1);
		}
	}

	/**
	 * 根据文件路径获取文件名
	 *
	 * @param filePath
	 * @return
	 */
	public static String getFileNameByPath(String filePath) {
		return getFileNameByPath(filePath, true, File.separator);
	}

	/**
	 * 根据文件路径获取文件名
	 *
	 * @param filePath
	 * @param getExtension
	 *            是否获取扩展名
	 * @return
	 */
	public static String getFileNameByPath(String filePath, boolean getExtension) {
		return getFileNameByPath(filePath, getExtension, File.separator);
	}

	/**
	 * 根据文件路径获取文件名
	 *
	 * @param filePath
	 * @param getExtension
	 *            是否获取扩展名
	 * @param separator
	 * @return
	 */
	public static String getFileNameByPath(String filePath, boolean getExtension, String separator) {
		if (filePath == null || filePath.equals("")) {
			return "";
		} else {
			int lastSeparatorIndex = filePath.lastIndexOf(separator);
			if (lastSeparatorIndex < 0) {
				if (getExtension) {
					return filePath;
				} else {
					int extensionIndex = filePath.lastIndexOf(".");
					return filePath.substring(0, extensionIndex);
				}

			} else if (lastSeparatorIndex == filePath.length() - 1) {
				return "";
			} else {
				if (getExtension) {
					return filePath.substring(lastSeparatorIndex + 1);
				} else {
					int extensionIndex = filePath.lastIndexOf(".");
					return filePath.substring(lastSeparatorIndex + 1, extensionIndex);
				}
			}
		}
	}

	/**
	 * 获取文件夹路径
	 *
	 * @param path
	 * @return
	 */
	public static String getFilePath(String path) {
		return getFilePath(path, File.separator);
	}

	/**
	 * 获取文件夹路径
	 *
	 * @param path
	 * @param separator
	 * @return
	 */
	public static String getFilePath(String path, String separator) {
		if (path == null || path.equals("")) {
			return "";
		} else {
			int lastSeparatorIndex = path.lastIndexOf(separator);
			if (lastSeparatorIndex < 0) {
				return "";
			} else {
				return path.substring(0, lastSeparatorIndex + 1);
			}
		}
	}

	/**
	 * 判断最后的是否分隔符
	 *
	 * @param path
	 * @return
	 */
	public static boolean isLastSeparator(String path) {
		return isLastSeparator(path, File.separator);
	}

	/**
	 * 判断最后的是否分隔符
	 *
	 * @param path
	 * @param separator
	 * @return
	 */
	public static boolean isLastSeparator(String path, String separator) {
		if (path == null || path.length() == 0) {
			return false;
		}
		return path.substring(path.length() - 1).equals(separator);
	}

	/**
	 * 在文件路径中增加文件名
	 *
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String pathAddFileName(String path, String fileName) {
		if (null2Str(path).equals("")) {
			return "";
		}

		if (null2Str(fileName).equals("")) {
			return path;
		}

		if (isLastSeparator(path)) {
			return path + fileName;
		} else {
			return path + File.separator + fileName;
		}
	}

	/**
	 * 返回两个字符串中间的内容
	 *
	 * @param all
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getMiddleString(String all, String start, String end) {
		int beginIdx = all.indexOf(start);
		int endIdx = all.indexOf(end);
		if (beginIdx >= 0) {
			beginIdx += start.length();
		}
		if (beginIdx > 0 && endIdx > 0 && endIdx > beginIdx) {
			return all.substring(beginIdx, endIdx);
		} else {
			return null;
		}
	}

	// ---------------------------------------------------------------------
	// General convenience methods for working with Strings
	// ---------------------------------------------------------------------

	/**
	 * Check that the given CharSequence is neither <code>null</code> nor of
	 * length 0. Note: Will return <code>true</code> for a CharSequence that
	 * purely consists of whitespace.
	 * <p>
	 *
	 * <pre>
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 *
	 * @param str
	 *            the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check that the given String is neither <code>null</code> nor of length 0.
	 * Note: Will return <code>true</code> for a String that purely consists of
	 * whitespace.
	 *
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not null and has length
	 * @see #hasLength(CharSequence)
	 */
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	/**
	 * Check whether the given CharSequence has actual text. More specifically,
	 * returns <code>true</code> if the string not <code>null</code>, its length
	 * is greater than 0, and it contains at least one non-whitespace character.
	 * <p>
	 *
	 * <pre>
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 *
	 * @param str
	 *            the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not <code>null</code>,
	 *         its length is greater than 0, and it does not contain whitespace
	 *         only
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given String has actual text. More specifically,
	 * returns <code>true</code> if the string not <code>null</code>, its length
	 * is greater than 0, and it contains at least one non-whitespace character.
	 *
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not <code>null</code>, its
	 *         length is greater than 0, and it does not contain whitespace only
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}

	/**
	 * Check whether the given CharSequence contains any whitespace characters.
	 *
	 * @param str
	 *            the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not empty and contains
	 *         at least 1 whitespace character
	 * @see Character#isWhitespace
	 */
	public static boolean containsWhitespace(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given String contains any whitespace characters.
	 *
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not empty and contains at
	 *         least 1 whitespace character
	 * @see #containsWhitespace(CharSequence)
	 */
	public static boolean containsWhitespace(String str) {
		return containsWhitespace((CharSequence) str);
	}

	/**
	 * Trim leading and trailing whitespace from the given String.
	 *
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim <i>all</i> whitespace from the given String: leading, trailing, and
	 * inbetween characters.
	 *
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		int index = 0;
		while (buf.length() > index) {
			if (Character.isWhitespace(buf.charAt(index))) {
				buf.deleteCharAt(index);
			} else {
				index++;
			}
		}
		return buf.toString();
	}

	/**
	 * Trim leading whitespace from the given String.
	 *
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * Trim trailing whitespace from the given String.
	 *
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim all occurences of the supplied leading character from the given
	 * String.
	 *
	 * @param str
	 *            the String to check
	 * @param leadingCharacter
	 *            the leading character to be trimmed
	 * @return the trimmed String
	 */
	public static String trimLeadingCharacter(String str, char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * Trim all occurences of the supplied trailing character from the given
	 * String.
	 *
	 * @param str
	 *            the String to check
	 * @param trailingCharacter
	 *            the trailing character to be trimmed
	 * @return the trimmed String
	 */
	public static String trimTrailingCharacter(String str, char trailingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && buf.charAt(buf.length() - 1) == trailingCharacter) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Test if the given String starts with the specified prefix, ignoring
	 * upper/lower case.
	 *
	 * @param str
	 *            the String to check
	 * @param prefix
	 *            the prefix to look for
	 * @see String#startsWith
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * Test if the given String ends with the specified suffix, ignoring
	 * upper/lower case.
	 *
	 * @param str
	 *            the String to check
	 * @param suffix
	 *            the suffix to look for
	 * @see String#endsWith
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		if (str.length() < suffix.length()) {
			return false;
		}

		String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	/**
	 * Test whether the given string matches the given substring at the given
	 * index.
	 *
	 * @param str
	 *            the original string (or StringBuffer)
	 * @param index
	 *            the index in the original string to start matching against
	 * @param substring
	 *            the substring to match at the given index
	 */
	public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
		for (int j = 0; j < substring.length(); j++) {
			int i = index + j;
			if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Count the occurrences of the substring in string s.
	 *
	 * @param str
	 *            string to search in. Return 0 if this is null.
	 * @param sub
	 *            string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * Replace all occurences of a substring within a string with another
	 * string.
	 *
	 * @param inString
	 *            String to examine
	 * @param oldPattern
	 *            String to replace
	 * @param newPattern
	 *            String to insert
	 * @return a String with the replacements
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (inString == null) {
			return null;
		}
		if (oldPattern == null || newPattern == null) {
			return inString;
		}

		StringBuffer sbuf = new StringBuffer();
		// output StringBuffer we'll build up
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));

		// remember to append any characters to the right of a match
		return sbuf.toString();
	}

	/**
	 * Delete all occurrences of the given substring.
	 *
	 * @param inString
	 *            the original String
	 * @param pattern
	 *            the pattern to delete all occurrences of
	 * @return the resulting String
	 */
	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	/**
	 * Delete any character in a given String.
	 *
	 * @param inString
	 *            the original String
	 * @param charsToDelete
	 *            a set of characters to delete. E.g. "az\n" will delete 'a's,
	 *            'z's and new lines.
	 * @return the resulting String
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with formatted Strings
	// ---------------------------------------------------------------------

	/**
	 * Quote the given String with single quotes.
	 *
	 * @param str
	 *            the input String (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or
	 *         <code>null<code> if the input was <code>null</code>
	 */
	public static String quote(String str) {
		return (str != null ? "'" + str + "'" : null);
	}

	/**
	 * Turn the given Object into a String with single quotes if it is a String;
	 * keeping the Object as-is else.
	 *
	 * @param obj
	 *            the input Object (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or the input object as-is
	 *         if not a String
	 */
	public static Object quoteIfString(Object obj) {
		return (obj instanceof String ? quote((String) obj) : obj);
	}

	/**
	 * Unqualify a string qualified by a '.' dot character. For example,
	 * "this.name.is.qualified", returns "qualified".
	 *
	 * @param qualifiedName
	 *            the qualified name
	 */
	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	/**
	 * Unqualify a string qualified by a separator character. For example,
	 * "this:name:is:qualified" returns "qualified" if using a ':' separator.
	 *
	 * @param qualifiedName
	 *            the qualified name
	 * @param separator
	 *            the separator
	 */
	public static String unqualify(String qualifiedName, char separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	/**
	 * Capitalize a <code>String</code>, changing the first letter to upper case
	 * as per {@link Character#toUpperCase(char)}. No other letters are changed.
	 *
	 * @param str
	 *            the String to capitalize, may be <code>null</code>
	 * @return the capitalized String, <code>null</code> if null
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * Uncapitalize a <code>String</code>, changing the first letter to lower
	 * case as per {@link Character#toLowerCase(char)}. No other letters are
	 * changed.
	 *
	 * @param str
	 *            the String to uncapitalize, may be <code>null</code>
	 * @return the uncapitalized String, <code>null</code> if null
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		} else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}

	/**
	 * Extract the filename from the given path, e.g. "mypath/myfile.txt" ->
	 * "myfile.txt".
	 *
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the extracted filename, or <code>null</code> if none
	 */
	public static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	/**
	 * Extract the filename extension from the given path, e.g.
	 * "mypath/myfile.txt" -> "txt".
	 *
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the extracted filename extension, or <code>null</code> if none
	 */
	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
	}

	/**
	 * Strip the filename extension from the given path, e.g.
	 * "mypath/myfile.txt" -> "mypath/myfile".
	 *
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the path with stripped filename extension, or <code>null</code>
	 *         if none
	 */
	public static String stripFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
	}

	/**
	 * Apply the given relative path to the given path, assuming standard Java
	 * folder separation (i.e. "/" separators);
	 *
	 * @param path
	 *            the path to start from (usually a full file path)
	 * @param relativePath
	 *            the relative path to apply (relative to the full file path
	 *            above)
	 * @return the full file path that results from applying the relative path
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	/**
	 * Normalize the path by suppressing sequences like "path/.." and inner
	 * simple dots.
	 * <p>
	 * The result is convenient for path comparison. For other uses, notice that
	 * Windows separators ("\") are replaced by simple slashes.
	 *
	 * @param path
	 *            the original path
	 * @return the normalized path
	 */
	public static String cleanPath(String path) {
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List pathElements = new LinkedList();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			if (CURRENT_PATH.equals(pathArray[i])) {
				// Points to current directory - drop it.
			} else if (TOP_PATH.equals(pathArray[i])) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with corresponding to top path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, pathArray[i]);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * Compare two paths after normalization of them.
	 *
	 * @param path1
	 *            first path for comparison
	 * @param path2
	 *            second path for comparison
	 * @return whether the two paths are equivalent after normalization
	 */
	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	/**
	 * Parse the given <code>localeString</code> into a {@link Locale}.
	 * <p>
	 * This is the inverse operation of {@link Locale#toString Locale's
	 * toString}.
	 *
	 * @param localeString
	 *            the locale string, following <code>Locale's</code>
	 *            <code>toString()</code> format ("en", "en_UK", etc); also
	 *            accepts spaces as separators, as an alternative to underscores
	 * @return a corresponding <code>Locale</code> instance
	 */
	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = (parts.length > 0 ? parts[0] : "");
		String country = (parts.length > 1 ? parts[1] : "");
		String variant = "";
		if (parts.length >= 2) {
			// There is definitely a variant, and it is everything after the
			// country
			// code sans the separator between the country code and the variant.
			int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
			// Strip off any leading '_' and whitespace, what's left is the
			// variant.
			variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
			if (variant.startsWith("_")) {
				variant = trimLeadingCharacter(variant, '_');
			}
		}
		return (language.length() > 0 ? new Locale(language, country, variant) : null);
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with String arrays
	// ---------------------------------------------------------------------

	/**
	 * Append the given String to the given String array, returning a new array
	 * consisting of the input array contents plus the given String.
	 *
	 * @param array
	 *            the array to append to (can be <code>null</code>)
	 * @param str
	 *            the String to append
	 * @return the new array (never <code>null</code>)
	 */
	public static String[] addStringToArray(String[] array, String str) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[] { str };
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	/**
	 * Concatenate the given String arrays into one, with overlapping array
	 * elements included twice.
	 * <p>
	 * The order of elements in the original arrays is preserved.
	 *
	 * @param array1
	 *            the first array (can be <code>null</code>)
	 * @param array2
	 *            the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were
	 *         <code>null</code>)
	 */
	public static String[] concatenateStringArrays(String[] array1, String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	/**
	 * Merge the given String arrays into one, with overlapping array elements
	 * only included once.
	 * <p>
	 * The order of elements in the original arrays is preserved (with the
	 * exception of overlapping elements, which are only included on their first
	 * occurence).
	 *
	 * @param array1
	 *            the first array (can be <code>null</code>)
	 * @param array2
	 *            the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were
	 *         <code>null</code>)
	 */
	public static String[] mergeStringArrays(String[] array1, String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		List result = new ArrayList();
		result.addAll(Arrays.asList(array1));
		for (int i = 0; i < array2.length; i++) {
			String str = array2[i];
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}

	/**
	 * Turn given source String array into sorted array.
	 *
	 * @param array
	 *            the source array
	 * @return the sorted array (never <code>null</code>)
	 */
	public static String[] sortStringArray(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		Arrays.sort(array);
		return array;
	}

	/**
	 * Copy the given Collection into a String array. The Collection must
	 * contain String elements only.
	 *
	 * @param collection
	 *            the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in Collection
	 *         was <code>null</code>)
	 */
	public static String[] toStringArray(Collection collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	/**
	 * Copy the given Enumeration into a String array. The Enumeration must
	 * contain String elements only.
	 *
	 * @param enumeration
	 *            the Enumeration to copy
	 * @return the String array (<code>null</code> if the passed-in Enumeration
	 *         was <code>null</code>)
	 */
	public static String[] toStringArray(Enumeration enumeration) {
		if (enumeration == null) {
			return null;
		}
		List list = Collections.list(enumeration);
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * Trim the elements of the given String array, calling
	 * <code>String.trim()</code> on each of them.
	 *
	 * @param array
	 *            the original String array
	 * @return the resulting array (of the same size) with trimmed elements
	 */
	public static String[] trimArrayElements(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			result[i] = (element != null ? element.trim() : null);
		}
		return result;
	}

	/**
	 * Remove duplicate Strings from the given array. Also sorts the array, as
	 * it uses a TreeSet.
	 *
	 * @param array
	 *            the String array
	 * @return an array without duplicates, in natural sort order
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		}
		Set set = new TreeSet();
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return toStringArray(set);
	}

	/**
	 * Split a String at the first occurrence of the delimiter. Does not include
	 * the delimiter in the result.
	 *
	 * @param toSplit
	 *            the string to split
	 * @param delimiter
	 *            to split the string up with
	 * @return a two element array with index 0 being before the delimiter, and
	 *         index 1 being after the delimiter (neither element includes the
	 *         delimiter); or <code>null</code> if the delimiter wasn't found in
	 *         the given input String
	 */
	public static String[] split(String toSplit, String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	/**
	 * Take an array Strings and split each element based on the given
	 * delimiter. A <code>Properties</code> instance is then generated, with the
	 * left of the delimiter providing the key, and the right of the delimiter
	 * providing the value.
	 * <p>
	 * Will trim both the key and value before adding them to the
	 * <code>Properties</code> instance.
	 *
	 * @param array
	 *            the array to process
	 * @param delimiter
	 *            to split each element using (typically the equals symbol)
	 * @return a <code>Properties</code> instance representing the array
	 *         contents, or <code>null</code> if the array to process was null
	 *         or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	/**
	 * Take an array Strings and split each element based on the given
	 * delimiter. A <code>Properties</code> instance is then generated, with the
	 * left of the delimiter providing the key, and the right of the delimiter
	 * providing the value.
	 * <p>
	 * Will trim both the key and value before adding them to the
	 * <code>Properties</code> instance.
	 *
	 * @param array
	 *            the array to process
	 * @param delimiter
	 *            to split each element using (typically the equals symbol)
	 * @param charsToDelete
	 *            one or more characters to remove from each element prior to
	 *            attempting the split operation (typically the quotation mark
	 *            symbol), or <code>null</code> if no removal should occur
	 * @return a <code>Properties</code> instance representing the array
	 *         contents, or <code>null</code> if the array to process was
	 *         <code>null</code> or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {

		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		Properties result = new Properties();
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			if (charsToDelete != null) {
				element = deleteAny(array[i], charsToDelete);
			}
			String[] splittedElement = split(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
		}
		return result;
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * Trims tokens and omits empty tokens.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 *
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those
	 *            characters is individually considered as delimiter).
	 * @return an array of the tokens
	 * @see StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 *
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those
	 *            characters is individually considered as delimiter)
	 * @param trimTokens
	 *            trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens
	 *            omit empty tokens from the result array (only applies to
	 *            tokens that are empty after trimming; StringTokenizer will not
	 *            consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens (<code>null</code> if the input String was
	 *         <code>null</code>)
	 * @see StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
			boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of
	 * potential delimiter characters - in contrast to
	 * <code>tokenizeToStringArray</code>.
	 * 
	 * @param str
	 *            the input String
	 * @param delimiter
	 *            the delimiter between elements (this is a single delimiter,
	 *            rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of
	 * potential delimiter characters - in contrast to
	 * <code>tokenizeToStringArray</code>.
	 * 
	 * @param str
	 *            the input String
	 * @param delimiter
	 *            the delimiter between elements (this is a single delimiter,
	 *            rather than a bunch individual delimiter characters)
	 * @param charsToDelete
	 *            a set of characters to delete. Useful for deleting unwanted
	 *            line breaks: e.g. "\r\n\f" will delete all new lines and line
	 *            feeds in a String.
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List result = new ArrayList();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		} else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Convert a CSV list into an array of Strings.
	 * 
	 * @param str
	 *            the input String
	 * @return an array of Strings, or the empty array in case of empty input
	 */
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * Convenience method to convert a CSV string list to a set. Note that this
	 * will suppress duplicates.
	 * 
	 * @param str
	 *            the input String
	 * @return a Set of String entries in the list
	 */
	public static Set commaDelimitedListToSet(String str) {
		Set set = new TreeSet();
		String[] tokens = commaDelimitedListToStringArray(str);
		set.addAll(Arrays.asList(tokens));
		return set;
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * 
	 * @param coll
	 *            the Collection to display
	 * @param delim
	 *            the delimiter to use (probably a ",")
	 * @param prefix
	 *            the String to start each element with
	 * @param suffix
	 *            the String to end each element with
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * 
	 * @param coll
	 *            the Collection to display
	 * @param delim
	 *            the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convenience method to return a Collection as a CSV String. E.g. useful
	 * for <code>toString()</code> implementations.
	 * 
	 * @param coll
	 *            the Collection to display
	 * @return the delimited String
	 */
	public static String collectionToCommaDelimitedString(Collection coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * Convenience method to return a String array as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * 
	 * @param arr
	 *            the array to display
	 * @param delim
	 *            the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * Convenience method to return a String array as a CSV String. E.g. useful
	 * for <code>toString()</code> implementations.
	 * 
	 * @param arr
	 *            the array to display
	 * @return the delimited String
	 */
	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	/**
	 * remove any leading whitespace from a string
	 * 
	 * @param s
	 * @return String
	 */
	public static String ltrim(String s) {
		int i = 0;

		for (; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n')
				; // do nothing.
			else
				break;
		}

		return s.substring(i);
	}

	/**
	 * remove the trailing whitespace from a string
	 * 
	 * @param s
	 * @return String
	 */
	public static String rtrim(String s) {
		int i = s.length() - 1;

		for (; i > 0; i--) {
			char ch = s.charAt(i);
			if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n')
				; // do nothing.
			else
				break;
		}

		return s.substring(0, i + 1);
	}

	/**
	 * converts a block of text into an array of strings by tokenizing using
	 * \r\n as the delimiter If no \r\n, it will look for \n or \r and use them
	 * instead.
	 * 
	 * @param block
	 * @return String[]
	 */
	public static String[] splitLines(String block) {
		String sepReg = "[\r\n]+";
		Pattern pattern = Pattern.compile(sepReg);
		return pattern.split(block);
	}

	/**
	 * converts a block of text into an array of strings by tokenizing using
	 * \s\t as the delimiter If no \s\t, it will look for \s or \t and use them
	 * instead.
	 * 
	 * @param block
	 * @return String[]
	 */
	public static String[] splitByWhiteSpace(String block) {
		String sepReg = "[\\s\t]+";
		Pattern pattern = Pattern.compile(sepReg);
		return pattern.split(block);
	}

	/**
	 * 剔除数字字符串中的非法字符. eg. "15%", "10,000.00"
	 * 
	 * @param s
	 * @return
	 */
	public static String formatToNumber(String s) {
		String str = s.replaceAll("[$,%\r\n\\s\t()]", "");
		return str.replaceAll("[a-zA-Z]*", "");
	}

	/**
	 * 剔除xml字串中开始的回车/换行符 eg. \r\n<?xml version="1.0" encoding="GB2312"?>
	 */
	public static String trimHeadLF(String xml) {
		int index = xml.indexOf("<?xml ");
		if (index >= 0)
			return xml.substring(index);
		return xml;
	}

	/**
	 * 剔除xml字串中结束的回车/换行符 eg. </ultragent>\r\n
	 */
	public static String trimTailLF(String xml) {
		int index = xml.indexOf("</ultragent>");
		if (index >= 0)
			return xml.substring(0, index + 12);
		return null;
	}

	/**
	 * 保留小数点后一位
	 */
	public static String trimDotNum(String s) {
		int index = s.indexOf(".");
		if (index > 0)
			return s.substring(0, index + 2);
		return s;
	}

	/**
	 * 判断是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (s == null || s.trim().equals("")) {
			return true;
		}

		return false;
	}

	/**
	 * <p>
	 * Removes one newline from end of a String if it's there, otherwise leave
	 * it alone. A newline is &quot;{@code \n}&quot;, &quot;{@code \r}&quot;, or
	 * &quot;{@code \r\n}&quot;.
	 * </p>
	 *
	 * <p>
	 * NOTE: This method changed in 2.0. It now more closely matches Perl chomp.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.chomp(null)          = null
	 * StringUtils.chomp("")            = ""
	 * StringUtils.chomp("abc \r")      = "abc "
	 * StringUtils.chomp("abc\n")       = "abc"
	 * StringUtils.chomp("abc\r\n")     = "abc"
	 * StringUtils.chomp("abc\r\n\r\n") = "abc\r\n"
	 * StringUtils.chomp("abc\n\r")     = "abc\n"
	 * StringUtils.chomp("abc\n\rabc")  = "abc\n\rabc"
	 * StringUtils.chomp("\r")          = ""
	 * StringUtils.chomp("\n")          = ""
	 * StringUtils.chomp("\r\n")        = ""
	 * </pre>
	 *
	 * @param str
	 *            the String to chomp a newline from, may be null
	 * @return String without newline, {@code null} if null String input
	 */
	public static String chomp(String str) {
		if (isEmpty(str)) {
			return str;
		}

		if (str.length() == 1) {
			char ch = str.charAt(0);
			if (ch == CR || ch == LF) {
				return "";
			}
			return str;
		}

		int lastIdx = str.length() - 1;
		char last = str.charAt(lastIdx);

		if (last == LF) {
			if (str.charAt(lastIdx - 1) == CR) {
				lastIdx--;
			}
		} else if (last != CR) {
			lastIdx++;
		}
		return str.substring(0, lastIdx);
	}

	/**
	 * 将一个以逗号相隔的字符串转换成一个字符串list
	 * 
	 * @param string
	 *            以逗号相隔的字符串
	 * @return 字符串List
	 */
	public static List<String> setStringToList(String string) {
		List<String> returnList = new ArrayList<String>();
		if (string != null && !string.equals("")) {
			String[] operString = string.split(",");
			for (int i = 0; i < operString.length; i++) {
				String str = operString[i];
				if (!isEmpty(str)) {
					returnList.add(str);
				}
			}
		}
		return returnList;
	}

	public static String join(String seperator, String[] strings) {
		int length = strings.length;
		if (length == 0)
			return EMPTY_STRING;
		StringBuffer buf = new StringBuffer(length * strings[0].length()).append(strings[0]);
		for (int i = 1; i < length; i++) {
			buf.append(seperator).append(strings[i]);
		}
		return buf.toString();
	}

	public static String join(String seperator, Iterator objects) {
		StringBuffer buf = new StringBuffer();
		if (objects.hasNext())
			buf.append(objects.next());
		while (objects.hasNext()) {
			buf.append(seperator).append(objects.next());
		}
		return buf.toString();
	}

	public static String[] add(String[] x, String sep, String[] y) {
		String[] result = new String[x.length];
		for (int i = 0; i < x.length; i++) {
			result[i] = x[i] + sep + y[i];
		}
		return result;
	}

	public static String repeat(String string, int times) {
		StringBuffer buf = new StringBuffer(string.length() * times);
		for (int i = 0; i < times; i++)
			buf.append(string);
		return buf.toString();
	}

	public static String replaceOnce(String template, String placeholder, String replacement) {
		int loc = template.indexOf(placeholder);
		if (loc < 0) {
			return template;
		} else {
			return new StringBuffer(template.substring(0, loc)).append(replacement)
					.append(template.substring(loc + placeholder.length())).toString();
		}
	}

	public static String[] split(String seperators, String list, boolean include) {
		StringTokenizer tokens = new StringTokenizer(list, seperators, include);
		String[] result = new String[tokens.countTokens()];
		int i = 0;
		while (tokens.hasMoreTokens()) {
			result[i++] = tokens.nextToken();
		}
		return result;
	}

	public static String unqualify(String qualifiedName, String seperator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(seperator) + 1);
	}

	public static String qualifier(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		if (loc < 0) {
			return EMPTY_STRING;
		} else {
			return qualifiedName.substring(0, loc);
		}
	}

	public static String[] suffix(String[] columns, String suffix) {
		if (suffix == null)
			return columns;
		String[] qualified = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			qualified[i] = suffix(columns[i], suffix);
		}
		return qualified;
	}

	public static String suffix(String name, String suffix) {
		return (suffix == null) ? name : name + suffix;
	}

	public static String[] prefix(String[] columns, String prefix) {
		if (prefix == null)
			return columns;
		String[] qualified = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			qualified[i] = prefix + columns[i];
		}
		return qualified;
	}

	public static String root(String qualifiedName) {
		int loc = qualifiedName.indexOf(".");
		return (loc < 0) ? qualifiedName : qualifiedName.substring(0, loc);
	}

	public static boolean booleanValue(String tfString) {
		String trimmed = tfString.trim().toLowerCase();
		return trimmed.equals("true") || trimmed.equals("t");
	}

	public static String toString(Object[] array) {
		int len = array.length;
		if (len == 0)
			return EMPTY_STRING;
		StringBuffer buf = new StringBuffer(len * 12);
		for (int i = 0; i < len - 1; i++) {
			buf.append(array[i]).append(COMMA_SPACE);
		}
		return buf.append(array[len - 1]).toString();
	}

	public static String[] multiply(String string, Iterator placeholders, Iterator replacements) {
		String[] result = new String[] { string };
		while (placeholders.hasNext()) {
			result = multiply(result, (String) placeholders.next(), (String[]) replacements.next());
		}
		return result;
	}

	private static String[] multiply(String[] strings, String placeholder, String[] replacements) {
		String[] results = new String[replacements.length * strings.length];
		int n = 0;
		for (int i = 0; i < replacements.length; i++) {
			for (int j = 0; j < strings.length; j++) {
				results[n++] = replaceOnce(strings[j], placeholder, replacements[i]);
			}
		}
		return results;
	}

	/*
	 * public static String unQuote(String name) { return (
	 * Dialect.QUOTE.indexOf( name.charAt(0) ) > -1 ) ? name.substring(1,
	 * name.length()-1) : name; }
	 * 
	 * public static void unQuoteInPlace(String[] names) { for ( int i=0;
	 * i<names.length; i++ ) names[i] = unQuote( names[i] ); }
	 * 
	 * public static String[] unQuote(String[] names) { String[] unquoted = new
	 * String[ names.length ]; for ( int i=0; i<names.length; i++ ) unquoted[i]
	 * = unQuote( names[i] ); return unquoted; }
	 */

	public static int count(String string, char character) {
		int n = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == character)
				n++;
		}
		return n;
	}

	public static int countUnquoted(String string, char character) {
		if (SINGLE_QUOTE == character) {
			throw new IllegalArgumentException("Unquoted count of quotes is invalid");
		}
		// Impl note: takes advantage of the fact that an escpaed single quote
		// embedded within a quote-block can really be handled as two seperate
		// quote-blocks for the purposes of this method...
		int count = 0;
		int stringLength = string == null ? 0 : string.length();
		boolean inQuote = false;
		for (int indx = 0; indx < stringLength; indx++) {
			if (inQuote) {
				if (SINGLE_QUOTE == string.charAt(indx)) {
					inQuote = false;
				}
			} else if (SINGLE_QUOTE == string.charAt(indx)) {
				inQuote = true;
			} else if (string.charAt(indx) == character) {
				count++;
			}
		}
		return count;
	}

	public static boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}

	public static String qualify(String prefix, String name) {
		char first = name.charAt(0);
		if (first == SINGLE_QUOTE || // a SQLstring literal
				Character.isDigit(first) // a SQL numeric literal

		) {
			return name;
		} else {
			return new StringBuffer(prefix.length() + name.length() + 1).append(prefix).append(DOT).append(name)
					.toString();
		}
	}

	public static String[] qualify(String prefix, String[] names) {
		if (prefix == null)
			return names;
		int len = names.length;
		String[] qualified = new String[len];
		for (int i = 0; i < len; i++) {
			qualified[i] = qualify(prefix, names[i]);
		}
		return qualified;
	}

	public static int firstIndexOfChar(String sqlString, String string, int startindex) {
		int matchAt = -1;
		for (int i = 0; i < string.length(); i++) {
			int curMatch = sqlString.indexOf(string.charAt(i), startindex);
			if (curMatch >= 0) {
				if (matchAt == -1) { // first time we find match!
					matchAt = curMatch;
				} else {
					matchAt = Math.min(matchAt, curMatch);
				}
			}
		}
		return matchAt;
	}

	public static String truncate(String string, int length) {
		if (string.length() <= length) {
			return string;
		} else {
			return string.substring(0, length);
		}
	}

	public static String toUpperCase(String str) {
		return str == null ? null : str.toUpperCase();
	}

	public static String toLowerCase(String str) {
		return str == null ? null : str.toLowerCase();
	}

	/**
	 * 通过正则表达式来抽取指定的字符串
	 * 
	 * @param source
	 *            源串
	 * @param regex
	 *            正则表达式
	 * @return 源串中满足条件的字符串数组
	 */
	public static String[] extractStr(String source, String regex) {
		List list = new ArrayList();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(source);
		while (m.find()) {
			String value = m.group(0);
			if (!isValueExistInList(list, value)) {
				list.add(value);
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * 检查list中是否存在值为value的字符串,这是在添加值的过程中进行判断，
	 * 其实如果为了更好的效率，应该是在添加值完了之后进行去掉重复值，那样或许更好
	 * 
	 * @param list
	 *            值列表
	 * @param value
	 *            要检查的值
	 * @return 如果存在则返回true,否则，返回false
	 */
	public static boolean isValueExistInList(List list, String value) {
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			String currentValue = (String) list.get(i);
			if (value.equals(currentValue)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 字符串替换
	 * 
	 * @param sString
	 *            字符串值
	 * @param sOld
	 *            替换的旧值
	 * @param sNew
	 *            替换的新值
	 * @return sString经过sOld和sNew替换后得到的新串
	 */
	public static String replaceAllWord(String sString, String sOld, String sNew) {
		int index = sString.indexOf(sOld);
		String newString = "";
		while (index != -1) { // 如果字符串中还存在sOld串，则替换该串
			sString = sString.substring(0, index) + sNew + sString.substring(index + sOld.length(), sString.length());
			newString = sString;
			index = sString.indexOf(sOld);
		}
		return newString;
	}

	/**
	 * 字符串替换，不区分大小写
	 * 
	 * @param source
	 *            字符串值
	 * @param sOld
	 *            替换的旧值
	 * @param sNew
	 *            替换的新值
	 * @return sString经过sOld和sNew替换后得到的新串
	 */
	public static String ignoreCaseReplace(String source, String sOld, String sNew) {
		try {
			Pattern p = Pattern.compile(sOld, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(source);
			String ret = m.replaceAll(sNew);
			return ret;
		} catch (Exception e) {
			System.out.println(
					"replace string error,source=" + source + ",old=" + sOld + ",new=" + sNew + " " + e.getMessage());
		}
		return "";
	}

	/**
	 * 查找字符串，不区分大小写
	 * 
	 * @param source
	 *            字符串值
	 * @param str
	 *            查找的字符串
	 * @return true如果找到，false如果没有找到
	 */
	public static boolean findStringIgnoreCase(String source, String str) {
		Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(source);
		return m.find();
	}

	public static String escapeSQL(String sql) {
		if (sql == null) {
			return "";
		}

		String newSQL = sql.replaceAll("'", "''");

		return newSQL;
	}

	/**
	 * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HelloWorld->HELLO_WORLD
	 * 
	 * @param name
	 *            转换前的驼峰式命名的字符串
	 * @return 转换后下划线大写方式命名的字符串
	 */
	public static String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			// 将第一个字符处理成大写
			result.append(name.substring(0, 1).toUpperCase());
			// 循环处理其余字符
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				// 在大写字母前添加下划线
				if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
					result.append("_");
				}
				// 其他字符直接转成大写
				result.append(s.toUpperCase());
			}
		}
		return result.toString();
	}

	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HELLO_WORLD->HelloWorld
	 * 
	 * @param name
	 *            转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String camelName(String name) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (name == null || name.isEmpty()) {
			// 没必要转换
			return "";
		} else if (!name.contains("_")) {
			// 不含下划线，仅将首字母小写
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		// 用下划线将原始字符串分割
		String camels[] = name.split("_");
		for (String camel : camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0) {
				// 第一个驼峰片段，全部字母都小写
				result.append(camel.toLowerCase());
			} else {
				// 其他的驼峰片段，首字母大写
				result.append(camel.substring(0, 1).toUpperCase());
				result.append(camel.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}
	
	
	/**
	 * 按照大小分将list细分
	 * @param targetList
	 * @param splitSize
	 * @return
	 */
	public static List<List<String>> splitList(List<String> targetList, int splitSize) {
		if (targetList == null)
			return null;
		int size = targetList.size();
		List<List<String>> resultList = new ArrayList<List<String>>();
		if (size <= splitSize) {
			resultList.add(targetList);
		} else {
			for (int i = 0; i < size; i += splitSize) {
				// 用于限制最后一部分size小于splitSize的list
				int limit = i + splitSize;
				if (limit > size) {
					limit = size;
				}
				resultList.add(targetList.subList(i, limit));
			}
		}
		return resultList;
	}

	public static void main(String[] args) {
//		String str1 = underscoreName("serverCode");
//		System.out.println(str1);
		System.out.println(camelName("ACCOUNT_ID"));
//		System.out.println(camelName("STATS_UNIT_SUBM_NUM"));
//		System.out.println(camelName("STATS_UNIT_SUBM_TIMELY"));
//		System.out.println(camelName("STATS_SERVER_SUBM_STATE"));
//		System.out.println(camelName("STATS_SERVER_SUBM_SPEC"));
//		System.out.println(camelName("STATS_SERVER_SAMPLE_RESULT"));
//		System.out.println(camelName("STATS_SERVER_DRAFTE"));
//		System.out.println(camelName("STATS_SERVER_POSTPONE"));
//		System.out.println(camelName("STATS_SERVER_APPRAISAL_STATE"));
//		System.out.println(camelName("STATS_SERVER_PRE_ACCEPT_EFF"));
//		System.out.println(camelName("STATS_SERVER_APPRAISAL_EFF"));
//		System.out.println(camelName("STATS_SERVER_DRAFTE_EFF"));
	}
}
