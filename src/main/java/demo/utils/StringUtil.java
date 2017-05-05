package demo.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil extends ObjectUtil {
	/**
	 * 判断字符串是否为空
	 * @param str 要进行判断的字符串
	 * @return 如果为空返回True，否则返回False。
	 */
	public static boolean isEmpty(String str) {
		return (isNull(str) || str.trim().equals(""));
	}

	/**
	 * 判断字符串是否非空
	 * @param s 要进行判断的字符串。
	 * @return 如果非空返回True，否则返回False。
	 */
	public static boolean isNotEmpty(String s) {
		return (isNotNull(s) && !s.trim().equals(""));
	}

	/**
	 * 判断字符串是否为数字
	 * @param stringInput 要进行判断的字符串。
	 * @return 如果非空返回True，否则返回False。
	 */
	public static boolean isDigit(String stringInput) {
		if (stringInput == null)
			return false;
		if ("".endsWith(stringInput))
			return false;
		char[] times = stringInput.trim().toCharArray();
		for (int i = 0; i < times.length; i++) {
			char ch = times[i];
			if (false == Character.isDigit(ch)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否为数字
	 * @param stringInput 要进行判断的字符串。
	 * @return 如果非空返回True，否则返回False。
	 */
	public static boolean isDigit(int stringInput) {
		if (stringInput >= 0)
			return true;
		return false;
	}

	/**
	 * 将str 转换为 xml
	 * @param input
	 * @return
	 */
	public static String conStrToWml(String input) {
		StringBuffer ret = new StringBuffer();
		int len = input.length();
		for (int i = 0; i < len; i++) {
			char c = input.charAt(i);
			switch (c) {
			case '<':
				ret.append("&lt;");
				break;
			case '>':
				ret.append("&gt;");
				break;
			case '&':
				ret.append("&amp;");
				break;
			case '"':
				ret.append("&quot;");
				break;
			case '\'':
				ret.append("&apos;");
				break;
			case '$':
				ret.append("$$");
				break;
//			case '\n':
//				ret.append("<br/>");
//				break;				
			case 0x1f:
				break;
			default:
				ret.append(c);
				break;
			}
		}
		return ret.toString();
	}
	
	/**
	 * 将空值用空字符串替换
	 * @param obj
	 * @return String
	 */
	public static String getValue(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}	

	/**
	 * @return 注册产生6位随机数字密码
	 */
	public static String getRandomPass() {
		String retcode = "";
		// 生成随机类
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			String rand = String.valueOf(random.nextInt(10));
			retcode += rand;
		}
		String abc = "abcdefghijklmnopqrstuvwxyz";
		char cpwd = abc.charAt(random.nextInt(26));
		retcode = retcode.substring(0, 3) + cpwd + retcode.substring(4);
		
		return retcode;
	}	

	/**
	 * 对UTF8字符串bb截取bits位长度,并保证字符串完整
	 * 
	 * @param bb
	 *            需要截取的字节数组
	 * @param bits
	 *            需要截取的位数
	 * @return 截取后正确的字节数组
	 */
	private static byte[] uTF8Substring(byte[] bb, int bits) {
		int subbit = 0; // 保存位置
		int bblength = bb.length;
		if (bblength <= bits) {
			return bb;
		} else {
			int fontbit = 0; // 当前字的所占的位数
			while (subbit <= bits) {
				switch ((bb[subbit] & 0xf0)) {
				case 0xE0:
					fontbit = 3;
					break;
				case 0xc0:
				case 0xd0:
					fontbit = 2;
					break;
				case 0x00:
				case 0x10:
				case 0x20:
				case 0x30:
				case 0x40:
				case 0x50:
				case 0x60:
				case 0x70:
					fontbit = 1;
					break;
				case 0xf0:
					fontbit = 4;
					break;
				}

				if ((subbit + fontbit) > bits) {
					break;
				} else {
					subbit = subbit + fontbit;
				}
			}
			byte[] ret = new byte[subbit];
			for (int i = 0; i < subbit; i++) {
				ret[i] = bb[i];
			}
			return ret;
		}
	}

//	public static String uTF8Substring(String ssource, int length) {
//		String sReturn = ssource;
//		try {
//			if (ssource.getBytes("UTF-8").length > length) {
//				sReturn = new String(uTF8Substring(ssource.getBytes("UTF-8"),
//						length), "UTF-8");
//				LogUtil.logWrite(Constant.LOG_LEVEL_ERROR, "SysUtil", "uTF8Substring", 
//						"", ssource + "-->" + sReturn, "", Constant.LOG_SORT_EXCEPTION);				
//			}
//		} catch (Exception e) {
//			LogUtil.logWrite(Constant.LOG_LEVEL_ERROR, "SysUtil", "uTF8Substring", 
//					"", e.getMessage(), "", Constant.LOG_SORT_EXCEPTION);
//		}
//		return sReturn;
//	}		
	
	/**
	 * 取md5的加密密码
	 * @param pwd
	 * @return
	 */
//	public static String getMd5Pwd(String pwd) {
//		MD5 static_MD5 = new MD5();
//		return static_MD5.getMD5ofStr(pwd);
//	}	
	
	/**
	 * 取md5的加密密码
	 * @param pwd
	 * @return
	 */
	public static String getMd5Pwd(String fid, String pwd) {
		MD5 static_MD5 = new MD5();
		return static_MD5.getMD5ofStr(fid + ":fetion.com.cn:" + pwd);
	}
	

	/**
	 * 获取加密码
	 * @param pwd
	 * @return
	 */
	public static String getEncryptPwd(String pwd) {
		if(pwd==null||"".equals(pwd)){
			return null;
		}
		byte[] coded = Rijndael.encrypt(pwd.getBytes());		
		return ThreeDES.byte2hex(coded).toLowerCase();
	}
	
	
	
	
	/**
	 * 获取明码
	 * @param epwd
	 * @param md5pwd
	 * @return
	 */
	public static String getPwd( String epwd) {
		if(epwd==null||"".equals(epwd)){
			return null;
		}
		byte[] code = Rijndael.decrypt(ThreeDES.tempHexStringToByteArray(epwd));
		return  new String(code);
	}
	
	public static String getReqRegXML(HashMap<String, String> map) {
		String reqxml = "";
		reqxml += "<CmccImps>";
		if (map.get("OprType") != null) {
			reqxml += "<OprType>" + map.get("OprType") + "</OprType>";
		}
		reqxml += "<OprParas>";
		if (map.get("OprCode") != null) {
			reqxml += "<OprCode>" + map.get("OprCode") + "</OprCode>";
		}
		if (map.get("Mobile") != null) {
			reqxml += "<Mobile>" + map.get("Mobile") + "</Mobile>";
		}
		if (map.get("Name") != null) {
			reqxml += "<Name>" + map.get("Name") + "</Name>" ;
		}		
		if (map.get("Nick") != null) {
			reqxml += "<Nick>" + map.get("Nick") + "</Nick>" ;
		}
		if (map.get("Password") != null) {
			reqxml += "<Password>" + map.get("Password") + "</Password>";
		}
		if (map.get("Birthday") != null) {
			reqxml += "<Birthday>" + map.get("Birthday") + "</Birthday>";
		}		
		if (map.get("Check") != null) {
			reqxml += "<Check>" + map.get("Check") + "</Check>";
		}
		if (map.get("Age") != null) {
			reqxml += "<Age>" + map.get("Age") + "</Age>";
		}			
		if (map.get("Sex") != null) {
			reqxml += "<Sex>" + map.get("Sex") + "</Sex>";
		}				
		if (map.get("InDesc") != null) {
			reqxml += "<InDesc>" + map.get("InDesc") + "</InDesc>";
		}		
		if (map.get("RetUserInfo") != null) {
			reqxml += "<RetUserInfo>" + map.get("RetUserInfo") + "</RetUserInfo>";
		}			
		if (map.get("Source") != null) {
			reqxml += "<Source>" + map.get("Source") + "</Source>";
		}
		reqxml += "</OprParas>";
		reqxml += "</CmccImps>";
		
		return reqxml;
	}
	
//    /**
//     * 取手机号(移动MISC)
//     * @param strPoint
//     * @return
//     */
//
//    public static String getMobileByMISC(HttpServletRequest req){    	
//      String mobile = "";
//      String MISC_MSISDN = getValue(req.getParameter("MISC_MSISDN"));
//      String XCallingLineID = getValue(req.getHeader("X-Up-Calling-Line-ID"));
//      String HTTP_X_UP_SUBNO = getValue(req.getHeader("'HTTP_X_UP_SUBNO'"));
//      if(!"".equals(MISC_MSISDN)){
//    	  //如果取得MISC_MSISDN
//    	  if(MISC_MSISDN.indexOf("86")==0){
//    		  //带86
//    		  mobile = MISC_MSISDN.substring(2);
//    	  }else{
//    		  mobile = MISC_MSISDN;
//    	  }
//      }else{
//    	  if(!"".equals(XCallingLineID)){
//    		  if(XCallingLineID.indexOf("86")==0){
//    			  //带86
//    			  mobile = XCallingLineID.substring(2);
//    		  }else{
//    			  mobile = XCallingLineID;
//    		  }
//    	  }else{
//    		  if(!"".equals(HTTP_X_UP_SUBNO)){
//    			  if(HTTP_X_UP_SUBNO.indexOf("86")==0){
//    				  //带86
//    				  mobile = HTTP_X_UP_SUBNO.substring(2);
//    			  }
//    			  if(HTTP_X_UP_SUBNO.indexOf("_gateway")!=-1){
//    				  mobile = HTTP_X_UP_SUBNO.substring(0,HTTP_X_UP_SUBNO.indexOf("_gateway"));
//    			  }
//    		  }
//    	  }
//      }
//      return mobile;
//    }    
//	
	
	public static boolean checkMobile(String mp) {
        if ( !isDigit(mp)) {
            return false;
        } else if (mp.length() != 11) {
            return false;
        }
        return true;
	}

//	public static boolean checkPwd(String pwd, String mp) {
//		if (pwd == null) {
//			return false;
//		}
//		/* 1.长度：6～16个字符，ASCII码必须在0x21～0x7e之间，不允许使用中文、控制字符 */
//		if (pwd.length() < 6 || pwd.length() > 16) {
//			return false;
//		}
//		for (int i = 0; i < pwd.length(); i++) {
//			if (pwd.charAt(i) < 0x21 
//					|| pwd.charAt(i) > 0x7e) {
//				return false;
//			}
//		}
//		
//		/* 2.密码中必须至少含有一位英文字符 */
//		if (isDigit(pwd)) {
//			return false;
//		}
//		
//		/* 3.大小写敏感。 */
//		/* 4. 任意连续位置的4个字符，不允许出现如下情况： */
//		byte[] bpwd = pwd.getBytes();
//		
//		/* 4.1 如果是字母或者数字，不允许ASCII码连续(包括升序和降序)，例如：1234、abcd、4321等 */
//		for (int i = 0; i < bpwd.length - 3; i++) {
//			if (bpwd[i] == bpwd[i + 1] - 1 
//			                    && bpwd[i] == bpwd[i + 2] - 2
//			                    && bpwd[i] == bpwd[i + 3] - 3) {
//				return false;
//			}
//		}
//		for (int i = 0; i < bpwd.length - 3; i++) {
//			if (bpwd[i] == bpwd[i + 1] + 1 
//			                    && bpwd[i] == bpwd[i + 2] + 2
//			                    && bpwd[i] == bpwd[i + 3] + 3) {
//				return false;
//			}
//		}		
//		
//		/* 4.2 重复的字符，例如：aaaa、8888等 */
//		for (int i = 0; i < bpwd.length - 3; i++) {
//			if (bpwd[i] == bpwd[i + 1] 
//			                    && bpwd[i] == bpwd[i + 2]
//			                    && bpwd[i] == bpwd[i + 3]) {
//				return false;
//			}
//		}
//		
//		/* 4.3 与手机号码/飞信号的某一部分相同。例如：手机号为15864257826的用户把密码设置为ab4257是不允许的。*/
//		for (int i = 0; i < pwd.length() - 3; i++) {
//			if (mp.indexOf(pwd.substring(i, i + 4)) != -1) {
//				return false;
//			}
//		}
//        return true;		
//	}
	
	
	public static boolean checkPwd(String pwd){
		boolean flag = true;	
		if(pwd==null||"".equals(pwd)){
			flag = false;
			return flag;
		}		
//		int pwdLength = pwd.length();
//		if(pwdLength<6||pwdLength>16){
//			flag = false;
//			return flag;
//		}
//		boolean BhasLetter = true;
//		boolean BhasNumber = false;
//		boolean BhasChar = false;	
//		       
//		char []charpwd = pwd.toCharArray();
//		for(int i=0;i<charpwd.length;i++){
//			 char ch = charpwd[i];
//			 if(ch>0x7e || ch<0x21){
//				 BhasLetter = false;
//			 }
//			 else if (ch>='0'&&ch<='9'){
//				BhasNumber = true;	
//
//			 }else if((ch>='A'&& ch<='Z')||(ch>='a'|| ch<='z')){
//				BhasChar = true;	
//			 }	 
//		}
//	
//
//		if(BhasLetter && BhasNumber && BhasChar){		
//			flag = true;			
//		}else{
//			flag = false;
//		}		
		return flag;
	}	
	/**
	 * 去掉回车换行符号
	 * @param str
	 * @return
	 */
	public static String conCrLfToSpace(String str){
		String ret = str;
		Pattern crlf = Pattern.compile("(\r\n|\r|\n|\n\r)");
		Matcher m = crlf.matcher(str);
		if (m.find()) {
			ret = m.replaceAll("");
		}
		return ret;		
	}

	/**
	 * 添加回车换行符号
	 * @param str
	 * @return
	 */
	public static String addCrLf(String str) {
		StringBuffer sb;
		if (str != null) {
			sb = new StringBuffer(str);
		} else {
			sb = new StringBuffer("");
		}
		sb.append((char)13);
		return sb.toString();
	}
	
	public static String getEfftt(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);		
	}
	
	public static String getUrlCreden(String c) {
		return c.replaceAll("\\+", "%2b");
	}
	
	public static void main(String[] args) {
//		String pwd = "fdsf10915acdfg";
//		String mp = "13581750915";
//		if (StringUtil.checkPwd(pwd, mp)) {
//			System.out.println("OK");
//		}
//		System.out.println(StringUtil.getRandomPass());
//		String ypwd = "56890";
//		String epwd = StringUtil.getEncryptPwd(ypwd);
//		String mpwd = StringUtil.getMd5Pwd(ypwd);
//		System.out.println(epwd);
//		System.out.println(mpwd);
//		System.out.println(ypwd);
//		System.out.println(StringUtil.getPwd(epwd, mpwd));
//		System.out.println(StringUtil.getPwd("d7c8a3a7966967", "cc03e747a6afbbcbf8be7668acfebee5"));
		String fetion ="48E58DCE6ED07B80F5D6344B67E5CD51B98896D22A2814DDD276E6EB77807C8DDABF83251EA2266A";
        String fetionkey ="650EB218CC6FCE5835FB7482266D020C";
		System.out.println(getPwd("93374be4ffa6730c05cd120b1f7a731f"));
		System.out.println(getEncryptPwd("test12"));
	}
}
