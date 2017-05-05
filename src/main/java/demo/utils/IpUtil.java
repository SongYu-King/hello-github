package demo.utils;


import java.util.StringTokenizer;

public class IpUtil {
	
	public static long ipToLongValue(String ipAddress) {
		String[] addressParts = new String[4];
		StringTokenizer ipTokens = new StringTokenizer(ipAddress, ".");
		for (int i = 0; i < 4; i++) {
			addressParts[i] = ipTokens.nextToken();
		}

		String hexVal = "";
		for (int i = 0; i < 4; i++) {
			int intVal = Integer.parseInt(addressParts[i]);
			String hexTemp = Integer.toHexString(intVal);
			/*
			 * Must be a two-digit hex value to work properly; if the
			 * value 'c' is inserted between two other values, 'ffcff', the
			 * end value is different than what is expected, 'ff0cff'.
			 */
			if (hexTemp.length() < 2) {
				hexTemp = "0" + hexTemp;
			}
			//Builds a String of the hex values for each byte
			hexVal = hexVal + hexTemp;
		}
		return Long.parseLong(hexVal, 16);
	}
	
}
