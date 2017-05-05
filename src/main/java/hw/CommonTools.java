package hw;
/**
 *
 */


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangshangyu
 * @description
 * @date 2014-6-13 上午10:37:51
 */
public class CommonTools {

    /**
     * 判断输入的字符串是否为空
     *
     * @param s
     * @return 空则返回true, 非空则flase
     */
    public static boolean isEmpty(String s) {
        return null == s || 0 == s.length()
                || 0 == s.replaceAll("\\s", "").length();
    }

    /**
     * 判断是否是数字
     *
     * @param inputStr
     * @return
     */
    public static boolean isNumerical(String inputStr) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(inputStr);
        return m.matches();
    }

    /**
     * 判断是否符合yyyy-MM-dd格式
     *
     * @param inputStr
     * @return
     */
    public static boolean isYMD(String inputStr) {
        Pattern p = Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
        Matcher m = p.matcher(inputStr);
        return m.matches();
    }

    /**
     * 判断是否符合HH:mm:ss格式,24小时制
     *
     * @param inputStr
     * @return
     */
    public static boolean isHMS(String inputStr) {
        Pattern p = Pattern.compile("(([0-1][0-9])|2[0-3]):[0-5][0-9]:[0-5][0-9]");
        Matcher m = p.matcher(inputStr);
        return m.matches();
    }
    /**
     * 格式：yyyy-MM-dd HH:mm:ss
     * @param inputStr
     * @return
     */
    public static boolean isYMDHMS(String inputStr) {
        Pattern p = Pattern.compile("((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)) (([0-1][0-9])|2[0-3]):[0-5][0-9]:[0-5][0-9]");
        Matcher m = p.matcher(inputStr);
        return m.matches();
    }

    /**
     * @param inputStr
     * @return
     */
    public static String formatDate(String inputStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(sdf.parse(inputStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    // test
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()));
        Date d = new Date();
        System.out.println(d.getTime());
        System.out.println(sdf.format(new Date(1425873600)));
        // 1425830401
        // 1429752594
        // 1429752594000
        // 2015-04-23 09:29:54
        String time = "2015-04-23 09:29:54";
        Date date = sdf.parse(time);
        System.out.println("Format To times:" + date.getTime());
        Integer playStartDate = (int) (date.getTime() / 1000);
        System.out.println(playStartDate);
        System.out.println(sdf.format(new Date(1429752594L * 1000)));

        System.out.println(formatDate("2015-4-3 29:29:54"));
        System.out.println(isYMDHMS(formatDate("2015-4-3 29:29:54")));
        System.out.println("http://www.baidu.com/".matches("^http(s)?://.*"));

    }
}
