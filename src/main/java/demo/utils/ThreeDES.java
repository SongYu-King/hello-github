package demo.utils;


public class ThreeDES {

	private static final String Algorithm =  "DESede/CBC/PKCS7Padding";

	private static byte[] iv = new byte[] { 31, 78, -127, 12, -88, 32, -2, 66 };

	/**
	 * 加密
	 * 
	 * @param keybyte
	 * @param src
	 * @return
	 */
//	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
//		try {
//			// 生成密钥
//	           // 添加新安全算法:PKCS7
//	           Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
//			IvParameterSpec ivps = new IvParameterSpec(iv);
//			Cipher c1 = Cipher.getInstance(Algorithm);
//			c1.init(Cipher.ENCRYPT_MODE, deskey, ivps);
//			return c1.doFinal(src);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}


	
	/**
	 * 解密
	 * 
	 * @param keybyte
	 * @param src
	 * @return
	 */
//	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
//		try {
//	           // 添加新安全算法:PKCS7
//	           Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
//			IvParameterSpec ivps = new IvParameterSpec(iv);
//			Cipher c1 = Cipher.getInstance(Algorithm);
//			c1.init(Cipher.DECRYPT_MODE, deskey, ivps);
//			return c1.doFinal((src));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	/**
	 * 转换成十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + "";
		}
		return hs.toUpperCase();
	}
	
	public static byte[] tempHexStringToByteArray(String hexString) {
		byte[] bytes = new byte[hexString.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;

	}
	public static byte[] checkKeys(String  keys){
		  byte[] bt = tempHexStringToByteArray(keys);
          byte[] kbt = new byte[bt.length + 8];
          System.arraycopy(bt, 0, kbt, 0, bt.length);
          System.arraycopy(bt, 0, kbt, 16, 8);	
		return kbt;
	}
	
	public static void main(String[] args) throws Exception {

		String fetion ="48E58DCE6ED07B80F5D6344B67E5CD51B98896D22A2814DDD276E6EB77807C8DDABF83251EA2266A";
        String fetionkey ="650EB218CC6FCE5835FB7482266D020C";




        String fet= "650EB218CC6FCE5835FB7482266D020C:111111";


//  		byte[] coded = encryptMode(checkKeys(fetionkey), fet.getBytes());
//		System.out.println("加密后的字符串:" + ThreeDES.byte2hex(coded));	
//		
//		byte[] code = decryptMode(checkKeys(fetionkey),tempHexStringToByteArray(fetion));
//		System.out.println("解密后的字符串:" + new String(code));  

	}

	
	




}
