package demo.utils;

/**

 * 类说明
 */

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


public class Rijndael
{
	public static void main(String[] args) throws Exception
	{
        String fet= "test12";
        byte[] coded = encrypt(fet.getBytes());
        String fetion = ThreeDES.byte2hex(coded).toLowerCase();
		System.out.println("加密后的字符串:" + fetion);	
		byte[] code = decrypt(ThreeDES.tempHexStringToByteArray(fetion));
		System.out.println("解密后的字符串:" + new String(code)); 
		
	}
	private static byte[] iv =  new byte[]{(byte)0x59, (byte)0x63,(byte) 0x5e, (byte)0xaf, (byte)0xab, (byte)0x7b, (byte)0xd9, (byte)0xef, (byte)0x56, (byte)0x8c, (byte)0x4a, (byte)0x01, (byte)0xad, (byte)0x90, (byte)0xc4, (byte)0x14 };	
	
	private static 	byte[] aes_key = new byte[]{(byte)0x6c, (byte)0xbc, (byte)0xfc, (byte)0xc7, (byte)0x92, (byte)0xc5, (byte)0xfd, (byte)0x6b, (byte)0xdf, (byte)0xf9, (byte)0x75, (byte)0x65,
		(byte)0x56, (byte)0xc4, (byte)0x09, (byte)0xfb,(byte)0x2c, (byte)0x4b, (byte)0xfd, (byte)0x3d, (byte)0x74, (byte)0xb6, (byte)0x60, (byte)0xbe, (byte)0x4a, (byte)0x5e,
		(byte)0xa1, (byte)0x96, (byte)0xc2, (byte)0x02, (byte)0x3e, (byte)0xec};
	public static byte[] decrypt(byte[] sSrc) {
		try {
			Key rijndaelKey =new SecretKeySpec(aes_key, "Rijndael");
			IvParameterSpec spec=new IvParameterSpec(iv);
			Cipher out=Cipher.getInstance("Rijndael/CBC/PKCS5Padding");
			out.init(Cipher.DECRYPT_MODE, rijndaelKey, spec);
			
			return out.doFinal(sSrc);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static byte[] encrypt(byte[] sSrc) {   
		try {
		Key rijndaelKey =new SecretKeySpec(aes_key, "Rijndael");	    
	    IvParameterSpec spec=new IvParameterSpec(iv);
	    Cipher in=Cipher.getInstance("Rijndael/CBC/PKCS5Padding");
	    in.init(Cipher.ENCRYPT_MODE,rijndaelKey,spec);
	    byte[] enc =in.doFinal(sSrc);
		return enc;
		} catch (Exception ex) {
            ex.printStackTrace();
			return null;
		}
	}
}
