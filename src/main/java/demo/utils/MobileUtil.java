package demo.utils;


public class MobileUtil {
	/**
	 * 获取手机的pool值
	 * 
	 * @param mobile
	 * @return
	 */
	public static Integer getMobilePool(long mp) {
		long tmp;
	    if (mp >= 13400000000L && mp <= 13489999999L) {
	    	tmp = mp - 13400000000L;
	    	return (int)tmp / 100000 + 1;
	    } else if (mp >= 13500000000L && mp <= 13999999999L) {
	    	tmp = mp - 13500000000L;
	    	return (int)tmp / 100000 + 901;
	    } else if (mp >= 15900000000L && mp <= 15999999999L) {
	    	tmp = mp - 15900000000L;
	    	return (int)tmp / 100000 + 5901;
	    } else if (mp >= 15800000000L && mp <= 15899999999L) {
	    	tmp = mp - 15800000000L;
	    	return (int)tmp / 100000 + 6901;
	    } else if (mp >= 15700000000L && mp <= 15799999999L) {
	    	tmp = mp - 15700000000L;
	    	return (int)tmp / 100000 + 7901;
	    } else if (mp >= 15000000000L && mp <= 15099999999L) {
	    	tmp = mp - 15000000000L;
	    	return (int)tmp / 100000 + 8901;
	    } else if (mp >= 15100000000L && mp <= 15199999999L) {
	    	tmp = mp - 15100000000L;
	    	return (int)tmp / 100000 + 9901;
	    }
	    
	    else if (mp >= 15200000000L&& mp <= 15299999999L){
	    	tmp = mp - 15200000000L;
	    	return (int)tmp / 100000 + 12101;
	    }else if (mp >= 18800000000L&& mp <= 18899999999L){
	    	tmp = mp - 18800000000L;
	    	return (int)tmp / 100000 + 13101;
	    }else if (mp >= 44000000000L&& mp <= 44009999999L){
	    	tmp = mp - 44000000000L;
	    	return (int)tmp / 100000 + 11901;
	    }else if (mp >= 44100000000L&& mp <= 44109999999L){
	    	tmp = mp - 44100000000L;
	    	return (int)tmp / 100000 + 12001;
	    }

	    return -1;
	}
	
	/**
	 * 检查手机号是否是中移动号码
	 * @param mp
	 * @return
	 */
	public static boolean checkChinaMobile(long mp){
		String smp = String.valueOf(mp);
		if(smp.startsWith("86")){
			smp =smp.substring(2);
		}
		if(smp.length()!=11){
			return false;
		}
		smp =smp.substring(0,3);
		if(smp.equals("134")||smp.equals("135")||smp.equals("136")||smp.equals("137")||smp.equals("138")||smp.equals("139")
				||smp.equals("158")||smp.equals("159")||smp.equals("157")||smp.equals("150")||smp.equals("151")||smp.equals("152")
				||smp.equals("188")||smp.equals("440")||smp.equals("441")){
			return true;
		}
		return false;
	}

	
}
