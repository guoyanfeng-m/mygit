package util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;


public class StringUtil {

	public static final String PERCENTAGE_DEFAULT_VALUE = "0%";

	private StringUtil() {
	}
	/**
	 * 
	 *	UserType  ==> userTpye<br/>
	 *  把一个字符串的第一个字母小写、效率是最高的<br/>
	 * @param fildeName
	 * @return
	 */
	public static  String getMinName(String fildeName){  
        byte[] items = fildeName.getBytes();  
        items[0] = (byte) ((char) items[0] - 'A' + 'a'); 
        return new String(items);  
    }
	/**
	 * 校验指定字符串是否匹配指定正则表达式
	 * 
	 * @param s
	 *            字符串
	 * @param pattern
	 *            正则表达式
	 * @return true if 指定字符串匹配指定正则表达式, otherwise false
	 */
	public static boolean regexMatch(String s, String pattern) {
		return Pattern.matches(pattern, s);
	}

	/**
	 * 校验指定字符串是否匹配指定通配符表达式。通配符表达式是指含有*和?的字符串，其中*代表匹配任意个字符，?代表匹配一个字符
	 * 
	 * @param s
	 *            字符串
	 * @param pattern
	 *            通配符表达式
	 * @return true if 指定字符串匹配指定通配符表达式, otherwise false
	 */
	public static boolean wildcardMatch(String s, String pattern) {
		// 先将通配符表达式转换为正则表达式
		pattern = pattern.replace('.', '#');
		pattern = pattern.replaceAll("#", "\\\\.");
		pattern = pattern.replace('*', '#');
		pattern = pattern.replaceAll("#", ".*");
		pattern = pattern.replace('?', '#');
		pattern = pattern.replaceAll("#", ".?");
		pattern = "^" + pattern + "$";
		// 按正则表达式校验匹配
		return regexMatch(s, pattern);
	}

	/**
	 * 校验指定字符串是否匹配指定多个通配符表达式中的一个。通配符表达式是指含有*和?的字符串，其中*代表匹配任意个字符，?代表匹配一个字符
	 * 
	 * @param s
	 *            字符串
	 * @param patterns
	 *            通配符表达式清单
	 * @return true if 指定字符串匹配指定多个通配符表达式中的一个, otherwise false
	 */
	public static boolean wildcardMatchOneOf(String s, String[] patterns) {
		for (String pattern : patterns) {
			if (wildcardMatch(s, pattern)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Description:当str为空时则将它的值设为str1
	 * @param str
	 * @param str1
	 * @return
	 * @return String
	 */
	public static String emptyConvert(String str, String str1) {
		return isNotEmpty(str) ? str : str1;
	}

	public static boolean  isNotEmpty(String str) {
		if(str==null || "".equals(str)){
			return false;
		}
		return true;
	}
	
	public static boolean  isEmpty(String str) {
		return !isNotEmpty(str);
	}
	
	
	
	
	/**
	 * 截取指定字符串，限制其最大长度为指定长度。若最大长度小于0，则返回null
	 * 
	 * @param s
	 *            字符串
	 * @param maxLength
	 *            允许最大长度
	 * @return 截取之后的字符串
	 */
	public static String cut(String s, int maxLength) {
		if (s == null || maxLength < 0) {
			return null;
		}
		if (s.length() <= maxLength) {
			return s;
		} else {
			s = s.substring(0, maxLength);
			char[] chars = s.toCharArray();
			if (chars[chars.length - 1] > 255) {
				s = s.substring(0, s.length() - 1);
			} else if (s.length() >= 2) {
				s = s.substring(0, s.length() - 2);
			}
			return s + "...";
		}
	}

	/**
	 * 获取指定字符串中以begin开始以end结束的所有子字符串
	 * 
	 * @param s
	 *            字符串
	 * @param begin
	 *            开始字符串
	 * @param end
	 *            结束字符串
	 * @return 子字符串集合
	 */
	public static String[] substringsBetweens(String s, String begin, String end) {
		List<String> list = new ArrayList<String>();
		if (begin.equals(end) && s.indexOf(begin) >= 0) {
			list.add(begin);
		}
		for (int index = s.indexOf(begin); index >= 0; index = s.indexOf(begin,
				index + 1)) {
			if (end.length() == 0) {
				list.add(s.substring(index));
			} else {
				int endIndex = s.indexOf(end, index + 1);
				if (endIndex > index) {
					list.add(s.substring(index, endIndex + end.length()));
				} else if (endIndex < 0) {
					break;
				}
			}
		}
		return list.toArray(new String[0]);
	}

	/**
	 * 
	 * @Description:计算百分数
	 * @param dividend
	 *            被除数
	 * @param divisor
	 *            除数
	 * @return String percentage 字符串百分数
	 */
	public static String calculatePercentage(double dividend, double divisor) {

		double percentage = 0;

		if (divisor < 1 || dividend < 1) {
			return PERCENTAGE_DEFAULT_VALUE;
		}

		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMaximumIntegerDigits(3);

		percentage = dividend / divisor;
		return nf.format(percentage);
	}

	/**
	 * 格式化（去掉匹配格式结尾的字符串，并将其强制转换为双精度型）
	 * 
	 * @author zhb
	 * @param str
	 *            字符串
	 * @param regex
	 *            匹配符
	 * @return 格式化后的双精度型参数
	 */
	public static double formatDouble(String str, String regex) {

		if (isEmpty(str)) {
			return 0;
		}
		if (str.endsWith(regex)) {
			str = str.substring(0, str.length() - 1);
			return (Double.parseDouble(str));
		}
		return 0;
	}

	/**
	 * 字节码转换成16进制字符串
	 * 
	 * @param data
	 *            字节码
	 * @return 16进制字符串
	 */
	public static String byte2hex(byte[] data) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < data.length; n++) {
			stmp = (Integer.toHexString(data[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < data.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	/**
	 * 格式化邮箱手机
	 * 
	 * @param value
	 * @return 116****@qq.com 135****410
	 */
	public static String formatEmailOrPhone(String value) {

		if (isNotEmpty(value)) {
			value = value.trim();
			if (value.length() <= 7) {
				return value;
			}
			String str = "";
			String str1 = "";
			if (value.contains("@")) {
				int num = value.indexOf("@");
				if (num <= 2) {
					return value;
				}

				str = value.substring(0, 3);
				str1 = value.substring(num, value.length());
				return str + "****" + str1;
			} else {

				str = value.substring(0, 3);
				str1 = value.substring(value.length() - 4, value.length());
				return str + "****" + str1;
			}
		}

		return "";
	}

 

	public static String[] formatAddress(String value) {
		String[] address = null;
		if (isNotEmpty(value)) {
			if (value.contains(" ")) {
				value = value.replaceAll(" ", " ");
			}

			int index = value.indexOf(" ");
			if (index > 0) {
				address = value.split(" ");
			} else {
				address = new String[] { value };
			}

		}
		return address;

	}

	/**
	 * 将字符串按指定的分割标记（单个字符或者字符串标记），拆分成新的字符串数组
	 * 
	 * @param resource
	 *            源字符串
	 * @param regex
	 *            分隔符
	 * @return 字符串数组
	 */
	public static String[] string2ArraysByRegex(String resource, String regex) {
		if (isEmpty(resource)) {
			return null;
		}
		return resource.split(regex);
	}
	
	/**
	 * 生成流水号
	 * 
	 */
	public static String generateSerialNumber() {
		return DateUtil.fromatShortNoSign(new Date())
				+ RandomStringUtils.randomNumeric(5);
	}
	/**
	 * 去掉字符串中的空格、回车、换行符、制表符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		if (str != null) {
			Pattern ptn = Pattern.compile("\\s*|\t|\r|\n");
			Matcher matcher = ptn.matcher(str);
			return matcher.replaceAll("");
		}
		return str;
	}

	public static String html2text(String source) {
		String result;
		// remove all styles
		result = source.replaceAll("<( )*style([^>])*>|(<style>).*(</style>)",
				"");
		// remove all \r\n|\r\n\t
		result = result.replaceAll("\r\n(\t)?", "");
		// replaced all <br> <p> tags
		result = result.replaceAll("<( )*br( )*>|<( )*p([^>])*>", "");
		// remove all <> tags
		result = result.replaceAll("<.*?>", "");
		// replace special characters
		result = result.replaceAll("&amp;", "&");
		result = result.replaceAll("&nbsp;", "");
		result = result.replaceAll("&lt;", "<");
		result = result.replaceAll("&gt;", ">");
		// remove extra whitespace
		result = result.replaceAll("\\s{2,}", " ");
		return result;
	}

	/**
	 * 通过网站域名URL获取该网站的源码
	 * 
	 * @param url
	 * @return String
	 * @throws Exception
	 */
	public static String getURLSource(String pathUrl) throws Exception {
		URL url = new URL(pathUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream inStream = conn.getInputStream(); // 通过输入流获取html二进制数据
		byte[] data = readInputStream(inStream); // 把二进制数据转化为byte字节数据
		String htmlSource = new String(data, "UTF-8");
		return htmlSource;
	}

	/**
	 * 把二进制流转化为byte字节数组
	 * 
	 * @param instream
	 * @return byte[]
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream instream)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = instream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		instream.close();
		return outStream.toByteArray();
	}


	public static String filterIllegalString(String str) {
		String[] strArray = { "print", "md5", "etc", "AND", "aNd", "and",
				"AnD", "And", "aND", "die", "this_is_a_test_string" };
		final String repStr = "[IllegalString]";
		for (String s : strArray) {
			if (str.indexOf(s) != -1) {
				str = str.replaceAll(s, repStr);
			}
		}
		return str;
	}

	public static final String[] maxRMB = { "零", "壹", "贰", "叁", "肆", "伍", "陆",
			"柒", "捌", "玖" };
	public static final String[] numRMB = { "仟", "", "拾", "佰" };
	public static final String[] noRMB = { "", "万", "亿" };

	/**
	 *  把一个字符串的第一个字母小写、效率是最高的
	 * @param fildeName
	 * @return
	 * @throws Exception
	 */
	public static  String getMethodName(String fildeName){  
        byte[] items = fildeName.getBytes();  
        items[0] = (byte) ((char) items[0] - 'A' + 'a');  
        return new String(items);  
    }
 
	 
	/**
	 * 返回 整型 数组
	 * 
	 * @param param
	 * @return
	 */
	public static List<Integer> getIdArray(String param) {
		String[] nums = null;
		List<Integer> listIdArray = null;

		if (isNotEmpty(param)) {

			int index = param.indexOf(",");
			if (index > 0) {
				nums = param.split(",");
			} else {
				nums = new String[] { param };
			}
			listIdArray = new ArrayList<Integer>();
			for (int i = 0; i < nums.length; i++) {
				listIdArray.add(Integer.parseInt(nums[i]));
			}
		}
		return listIdArray;
	}
	/**
	 * 返回 整型 数组
	 * 
	 * @param param
	 * @return
	 */
	public static   String getIdRepeatArray(String param) {
		String[] nums = null;
		Set<String> listIdArray = null;
		String repeat ="";
		String snum ="";
		if (isNotEmpty(param)) {
			int index = param.indexOf(",");
			if (index > 0) {
				nums = param.split(",");
			} else {
				return param;
			}
			listIdArray = new HashSet<String>();
			for (int i = 0; i < nums.length; i++) {
				 snum = nums[i];
				if(!"".equals(snum)){
					listIdArray.add(snum);
				}
			}
			  
			for (String str : listIdArray) {
				repeat+=str+",";
			}
		}
		repeat = repeat.substring(0,repeat.length()-1);
		return repeat;
	}

	public static String getIdQueryArray(String param) {
		String[] nums = null;
		Set<String> listIdArray = null;
		String repeat ="";
		String snum ="";
		if (isNotEmpty(param)) {
			int index = param.indexOf(",");
			if (index > 0) {
				nums = param.split(",");
			} else {
				return param;
			}
			listIdArray = new HashSet<String>();
			for (int i = 0; i < nums.length; i++) {
				 snum = nums[i];
				if(!"".equals(snum)){
					listIdArray.add("'"+snum+"'");
				}
			}
			  
			for (String str : listIdArray) {
				repeat+=str+",";
			}
		}
		repeat = repeat.substring(0,repeat.length()-1);
		return repeat;
	}
	public static String getProductLsitParam(String aids,int id) {
		String url  = id+"";
		String[] nums;
		if(isNotEmpty(aids)){
			if(aids.indexOf("-")>0){
				nums = aids.split("-");
				if(nums!=null){
					for(int i = 0; i < nums.length; i++) {
						if(url.equals(nums[i])){
							return aids;
						}
					}
					url = url+"-"+aids;
				}
				
			}else if(!url.equals(aids)){
				url = url+"-"+aids;
			}
		}
		return url;
		 
	}
	public static String getProductLsitParamV(String outa,int rec,String tvids,int attId){
		int th=0;
		String out = tvids;
		String[] nums;
		String srec= rec+"";
		String sattId = attId+"";
		String[] stvids;
		if(outa.indexOf("-")>0){
			nums = outa.split("-");
			if(nums!=null){
				for(int i = 0; i < nums.length; i++) {
					if(srec.equals(nums[i])){
						th = i;
						break;
					}
				}
			}
			stvids = tvids.split("-");
			stvids[th]=sattId;
			out=splitReverse(stvids,"-");
		}else{
			out = sattId;
		}
		return out;
	}
	
	public static String splitReverse(String[] splits,String type){
		String out="";
		if(splits!=null){
			for (int j = 0; j < splits.length; j++) {
				out = out+splits[j]+type;
			} 
			if(out.indexOf(type)>0){
				out = out.substring(0, out.length()-1);
			}
		}
		return out;
	}
	
	public static String getProductLsitParamAll(String taids,String tvids,int rec){
		int th=0;
		String outs="";
		String srec = rec+"";
		String[] stvids;
		String[] staids;
		if(taids.indexOf("-")>0){
			staids = taids.split("-");
			if(staids!=null){
				for(int i = 0; i < staids.length; i++) {
					if(srec.equals(staids[i])){
						th = i;
						break;
					}
				}
			}
			stvids = tvids.split("-");
			staids[th]="XXOOXX";
			stvids[th]="XXOOXX";
			taids = splitReverse(staids,"-").replaceAll("XXOOXX", "").replaceAll("--", "-");
			tvids = splitReverse(stvids,"-").replaceAll("XXOOXX", "").replaceAll("--", "-");
			outs = "_adis-"+taids+"_vids-"+tvids;
			outs = outs.replaceAll("-_", "_").replaceAll("--", "-");
		} 
		
		return outs;
	 
	}
	public static void main(String[] args) {
//		StringBuilder str = new StringBuilder(800);
//		str.append("<script type=\"text/javascript\"> \n");
//		str.append("  function goPaging(form,page){ \n");
//		str.append("    $(\"#\"+form+\" :input[name='page']\").val(page); \n");
//		str.append("    $(\"#\"+form).submit(); \n");
//		str.append("  }\n</script>");
//		System.err.println(str.toString());
//		System.err.println(generateSerialNumber());
		
	}

}
