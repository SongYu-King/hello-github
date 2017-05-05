package demo.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtil {
	
	private int year;
	
	private int month;
	
	private int day;
	
	private Date _date;
	
	@SuppressWarnings("unused")
	private int _year;
	
	private int _month;
	
	private int _day;
	
	private boolean leap;
	
	final static String chineseNumber[] = { "正", "二", "三", "四", "五", "六", "七",
		"八", "九", "十", "十一", "腊" };

	final static String chineseNumber1[] = { "一", "二", "三", "四", "五", "六", "七",
		"八", "九", "十", "十一", "十二" };
	
	static Locale locale = Locale.US;
	
	static SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
	
	final static long[] lunarInfo = new long[]
	                                         {0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
	                                          0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
	                                          0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
	                                          0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
	                                          0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
	                                          0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
	                                          0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
	                                          0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
	                                          0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
	                                          0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
	                                          0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
	                                          0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
	                                          0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
	                                          0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
	                                          0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0};
	
	/* 传回农历 y年的总天数 */
    final private static int yearDays(int y) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((lunarInfo[y - 1900] & i) != 0) sum += 1;
        }
        return (sum + leapDays(y));
    }
    
    /* 传回农历 y年闰月的天数 */ 
    final private static int leapDays(int y) {
        if (leapMonth(y) != 0) {
            if ((lunarInfo[y - 1900] & 0x10000) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }
    
    /* 传回农历 y年闰哪个月 1-12 , 没闰传回  */
    final private static int leapMonth(int y) {
        return (int) (lunarInfo[y - 1900] & 0xf);
    }
    
    /* 传回农历 y年m月的总天数 */
    final private static int monthDays(int y, int m) {
        if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
            return 29;
        else
            return 30;
    }
	
    /* 传回农历 y年的生肖 */
    final public String animalsYear() {
        final String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        return Animals[(year - 4) % 12];
    }    
    
    /* 传入 月日的offset 传回干支, 0=甲子 */
    final private static String cyclicalm(int num) {
        final String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
        final String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
        return (Gan[num % 10] + Zhi[num % 12]);
    }    
    
    /* 传入 offset 传回干支, 0=甲子 */
    final public String cyclical() {
        int num = year - 1900 + 36;
        return (cyclicalm(num));
    }

    /**
     * 传出y年m月d日对应的农历.
     * yearCyl3:农历年与1864的相差数 ?
     * monCyl4:从1900年1月31日以来,闰月数
     * dayCyl5:与1900年1月31日相差的天数,再加40 ?
     *
     * @param cal
     * @return
     */
    @SuppressWarnings({ "unused", "deprecation" })
    public DateUtil(Calendar cal) {
		int yearCyl, monCyl, dayCyl;
        int leapMonth = 0;
        Date baseDate = null;
        try {
            baseDate = chineseDateFormat.parse("1900-01-31");
        } catch (ParseException e) {
            e.printStackTrace(); //To change body of catch statement use Options | File Templates.
        }
        
        this._date = new Date(cal.getTime().getTime());
        String yldate = chineseDateFormat.format(_date);
        
        this._year = Integer.parseInt(yldate.split("-")[0]);
        
        this._month = Integer.parseInt(yldate.split("-")[1]);
        
        this._day = Integer.parseInt(yldate.split("-")[2]);

        /* 求出和1900年1月31日相差的天数 */
        int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
        dayCyl = offset + 40;
        monCyl = 14;

        /* 用offset减去每农历年的天数
         * 计算当天是农历第几天
         * i最终结果是农历的年份
         * offset是当年的第几天
         * */
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
            monCyl += 12;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
            monCyl -= 12;
        }

        /* 农历年份 */
        year = iYear;

        yearCyl = iYear - 1864;
        /* 闰哪个月,1-12 */
        leapMonth = leapMonth(iYear); 
        leap = false;

        /* 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天 */
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
        	/* 闰月 */
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapDays(year);
            } else
                daysOfMonth = monthDays(year, iMonth);

            offset -= daysOfMonth;
            /* 解除闰月 */
            if (leap && iMonth == (leapMonth + 1)) leap = false;
            if (!leap) monCyl++;
        }
        /* offset为0时，并且刚才计算的月份是闰月，要校正 */
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
                --monCyl;
            }
        }
        /* offset小于0时，也要校正 */
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            --monCyl;
        }
        month = iMonth;
        day = offset + 1;
    }
    public static String getChinaDayString(int day) {
        String chineseTen[] = {"初", "十", "廿", "卅"};
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30)
            return "";
        if (day == 10)
            return "初十";
        else
            return chineseTen[day / 10] + chineseNumber1[n];
    }

    public String toString() {
        return (leap ? "闰" : "") + chineseNumber[month - 1] + "月" + getChinaDayString(day);
    }
    
    public Integer toNumLunarAnimal() {
        final Integer[] Animals = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        return Animals[(year - 4) % 12];
    }
    
//    /**
//     * 返回星座
//     * @return
//     */
//    public Integer Horoscope() {
//    	Integer result = 0;
//		if ((_month == 1 && _day >= 20) 
//				|| (_month == 2 && _day <= 18)) {
//			result = Horoscope.Aquarius;
//		} else if ((_month == 2 && _day >= 19) 
//				|| (_month == 3 && _day <= 20)) {
//			result = Horoscope.Pisces;
//		} else if ((_month == 3 && _day >= 21) 
//				|| (_month == 4 && _day <= 19)) {
//			result = Horoscope.Aries;
//		} else if ((_month == 4 && _day >= 20) 
//				|| (_month == 5 && _day <= 20)) {
//			result = Horoscope.Taurus;
//		} else if ((_month == 5 && _day >= 21) 
//				|| (_month == 6 && _day <= 20)) {
//			result = Horoscope.Gemini;
//		} else if ((_month == 6 && _day >= 21) 
//				|| (_month == 7 && _day <= 22)) {
//			result = Horoscope.Cancer;
//		} else if ((_month == 7 && _day >= 23) 
//				|| (_month == 8 && _day <= 22)) {
//			result = Horoscope.Leo;
//		} else if ((_month == 8 && _day >= 23) 
//				|| (_month == 9 && _day <= 22)) {
//			result = Horoscope.Virgo;
//		} else if ((_month == 9 && _day >= 23) 
//				|| (_month == 10 && _day <= 22)) {
//			result = Horoscope.Libra;
//		} else if ((_month == 10 && _day >= 23) 
//				|| (_month == 11 && _day <= 21)) {
//			result = Horoscope.Scorpio;
//		} else if ((_month == 11 && _day >= 22) 
//				|| (_month == 12 && _day <= 21)) {
//			result = Horoscope.Sagittarius;
//		} else if ((_month == 12 && _day >= 22) 
//				|| (_month == 1 && _day <= 19)) {
//			result = Horoscope.Capricorn;
//		}
//
//    	return result;
//    }
    
    public String ChineseHoroscope() {
    	final String[] horoscope = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座"};
    	String result = null;
    	
		if ((_month == 1 && _day >= 20) 
				|| (_month == 2 && _day <= 18)) {
			result = horoscope[1];
		} else if ((_month == 2 && _day >= 19) 
				|| (_month == 3 && _day <= 20)) {
			result = horoscope[2];
		} else if ((_month == 3 && _day >= 21) 
				|| (_month == 4 && _day <= 19)) {
			result = horoscope[3];
		} else if ((_month == 4 && _day >= 20) 
				|| (_month == 5 && _day <= 20)) {
			result = horoscope[4];
		} else if ((_month == 5 && _day >= 21) 
				|| (_month == 6 && _day <= 20)) {
			result = horoscope[5];
		} else if ((_month == 6 && _day >= 21) 
				|| (_month == 7 && _day <= 22)) {
			result = horoscope[6];
		} else if ((_month == 7 && _day >= 23) 
				|| (_month == 8 && _day <= 22)) {
			result = horoscope[7];
		} else if ((_month == 8 && _day >= 23) 
				|| (_month == 9 && _day <= 22)) {
			result = horoscope[8];
		} else if ((_month == 9 && _day >= 23) 
				|| (_month == 10 && _day <= 22)) {
			result = horoscope[9];
		} else if ((_month == 10 && _day >= 23) 
				|| (_month == 11 && _day <= 21)) {
			result = horoscope[10];
		} else if ((_month == 11 && _day >= 22) 
				|| (_month == 12 && _day <= 21)) {
			result = horoscope[11];
		} else if ((_month == 12 && _day >= 22) 
				|| (_month == 1 && _day <= 19)) {
			result = horoscope[0];
		}
    	return result;
    }
    
    
    public String getChinaWeekdayString(String weekday){
     if(weekday.equals("Mon"))
      return "一";
     if(weekday.equals("Tue"))
      return "二";
     if(weekday.equals("Wed"))
      return "三";
     if(weekday.equals("Thu"))
      return "四";
     if(weekday.equals("Fri"))
      return "五";
     if(weekday.equals("Sat"))
      return "六";
     if(weekday.equals("Sun"))
      return "日";
     else
      return "";
     
    }
    
    /**
     * 转化为c#的日期
     * @param epochStr
     * @return
     */
    public static long GetTicks(long epochStr) {
		String date = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss").format(new Date (epochStr));
		String[] ds=date.split("/");
		//start of the ticks time
		Calendar calStart=Calendar.getInstance();
		calStart.set(1, 1, 3, 0, 0, 0);
		//the target time
		Calendar calEnd=Calendar.getInstance();
		calEnd.set(Integer.parseInt(ds[0]) ,Integer.parseInt(ds[1]),Integer.parseInt(ds[2]),Integer.parseInt(ds[3]),Integer.parseInt(ds[4]),Integer.parseInt(ds[5]) );
		//epoch time of the ticks-start time
		long epochStart=calStart.getTime().getTime();
		//epoch time of the target time
		long epochEnd=calEnd.getTime().getTime();
		//get the sum of epoch time, from the target time to the ticks-start time
		long all=epochEnd-epochStart;    
		long ticks=( (all/1000) * 1000000) * 10;
		return ticks;
    }   
    
    
    
    public static void main(String[] args) throws ParseException{
     Calendar today  = Calendar.getInstance();
     
     today.setTime(chineseDateFormat.parse("1977-01-30"));//加载自定义日期
     DateUtil lunar = new DateUtil(today);
     System.out.print(lunar.cyclical()+"年");//计算输出阴历年份
     System.out.println(lunar.toString());//计算输出阴历日期
     System.out.println(lunar.animalsYear());//计算输出属相
     System.out.println(lunar.toNumLunarAnimal());//计算输出属相
     System.out.println(lunar.ChineseHoroscope());//计算星座
   //  System.out.println(lunar.Horoscope());//计算星座
     System.out.println(new java.sql.Date(today.getTime().getTime()));//输出阳历日期
     System.out.println("星期"+lunar.getChinaWeekdayString(today.getTime().toString().substring(0,3)));//计算输出星期几
    }
        
    

}
