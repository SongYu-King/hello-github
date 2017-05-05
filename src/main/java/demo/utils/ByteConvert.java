package demo.utils;

import javax.crypto.KeyGenerator;
import java.security.Key;

public class ByteConvert {
	static public char[] byte2char(byte[] b, int length) {
		char[] c = new char[length];
		for (int i = 0; i < length; i++) {
			c[i] = (char) (0xff & b[i]);
		}
		return c;
	}

	public static byte[] hex2byte(String hex) {
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bts;
	}

	public static final byte[] short2byte(short s) {
		byte dest[] = new byte[2];
		dest[1] = (byte) (s & 0xff);
		dest[0] = (byte) (s >>> 8 & 0xff);
		return dest;
	}

	public static final byte[] int2byte(int i) {
		byte dest[] = new byte[4];
		dest[3] = (byte) (i & 0xff);
		dest[2] = (byte) (i >>> 8 & 0xff);
		dest[1] = (byte) (i >>> 16 & 0xff);
		dest[0] = (byte) (i >>> 24 & 0xff);
		return dest;
	}

	public static final byte[] long2byte(long l) {
		byte dest[] = new byte[8];
		dest[7] = (byte) (int) (l & 255L);
		dest[6] = (byte) (int) (l >>> 8 & 255L);
		dest[5] = (byte) (int) (l >>> 16 & 255L);
		dest[4] = (byte) (int) (l >>> 24 & 255L);
		dest[3] = (byte) (int) (l >>> 32 & 255L);
		dest[2] = (byte) (int) (l >>> 40 & 255L);
		dest[1] = (byte) (int) (l >>> 48 & 255L);
		dest[0] = (byte) (int) (l >>> 56 & 255L);
		return dest;
	}

	public static final byte[] float2byte(float f) {
		byte dest[] = new byte[4];
		return setfloat(dest, 0, f);
	}

	public static final byte[] double2byte(double d) {
		byte dest[] = new byte[8];
		return setdouble(dest, 0, d);
	}

	public static final byte getbyte(byte src[], int offset) {
		return src[offset];
	}

	public static final byte[] getbytes(byte src[], int offset, int length) {
		byte dest[] = new byte[length];
		System.arraycopy(src, offset, dest, 0, length);
		return dest;
	}

	public static final short getshort(byte src[], int offset) {
		return (short) ((src[offset] & 0xff) << 8 | src[offset + 1] & 0xff);
	}

	public static final int getint(byte src[], int offset) {
		return (src[offset] & 0xff) << 24 | (src[offset + 1] & 0xff) << 16 | (src[offset + 2] & 0xff) << 8
				| src[offset + 3] & 0xff;
	}

	public static final long getlong(byte src[], int offset) {
		return (long) getint(src, offset) << 32 | (long) getint(src, offset + 4) & 0xffffffffL;
	}

	public static final float getfloat(byte src[], int offset) {
		return Float.intBitsToFloat(getint(src, offset));
	}

	public static final double getdouble(byte src[], int offset) {
		return Double.longBitsToDouble(getlong(src, offset));
	}

	public static final byte[] setbyte(byte dest[], int offset, byte b) {
		dest[offset] = b;
		return dest;
	}

	public static final byte[] setbytes(byte dest[], int offset, byte src[]) {
		System.arraycopy(src, 0, dest, offset, src.length);
		return dest;
	}

	public static final byte[] setbytes(byte dest[], int offset, byte src[], int len) {
		System.arraycopy(src, 0, dest, offset, len);
		return dest;
	}

	public static final byte[] setshort(byte dest[], int offset, short s) {
		dest[offset] = (byte) (s >>> 8 & 0xff);
		dest[offset + 1] = (byte) (s & 0xff);
		return dest;
	}

	public static final byte[] setint(byte dest[], int offset, int i) {
		dest[offset] = (byte) (i >>> 24 & 0xff);
		dest[offset + 1] = (byte) (i >>> 16 & 0xff);
		dest[offset + 2] = (byte) (i >>> 8 & 0xff);
		dest[offset + 3] = (byte) (i & 0xff);
		return dest;
	}

	public static final byte[] setlong(byte dest[], int offset, long l) {
		setint(dest, offset, (int) (l >>> 32));
		setint(dest, offset + 4, (int) (l & 0xffffffffL));
		return dest;
	}

	public static final byte[] setfloat(byte dest[], int offset, float f) {
		return setint(dest, offset, Float.floatToIntBits(f));
	}

	public static final byte[] setdouble(byte dest[], int offset, double d) {
		return setlong(dest, offset, Double.doubleToLongBits(d));
	}

	public static final byte[] short2Byte(short[] inData) {
		int j = 0;
		int length = inData.length;
		byte[] outData = new byte[length * 2];
		for (int i = 0; i < length; i++) {
			outData[j++] = (byte) (inData[i] >>> 8);
			outData[j++] = (byte) (inData[i] >>> 0);
		}
		return outData;
	}
	public static final short[] byte2Short(byte[] inData, boolean byteSwap) {
		//int j=0;
		int length = inData.length / 2;
		short[] outData = new short[length];
		if (!byteSwap)
			for (int i = 0, j = 0; i < length; i++, j += 2) {
				//j=i*2;
				//outData[i]=(short)( ((inData[j] & 0xff) << 8) + ((inData[j+1] & 0xff) << 0 ) );
				outData[i] = (short) ((inData[j] << 8) + (inData[j + 1] & 0xff));
			}
		else
			for (int i = 0; i < length; i++) {
				int j = i * 2;
				outData[i] = (short) (((inData[j + 1] & 0xff) << 8) + ((inData[j] & 0xff) << 0));
			}
 
		return outData;
	}
	/**
	 * 
	 * 输入字符形式，返回字节数组形式。
	 * 
	 * 如输入字符串：AD67EA2F3BE6E5AD
	 * 
	 * 返回字节数组：{173,103,234,47,59,230,229,173}
	 * 
	 */

	public static final byte[] getKeyByStr(String str) {

		byte[] bRet = new byte[str.length() / 2];

		for (int i = 0; i < str.length() / 2; i++) {

			Integer itg = new Integer(16 * getChrInt(str.charAt(2 * i)) + getChrInt(str.charAt(2 * i + 1)));

			bRet[i] = itg.byteValue();

		}

		return bRet;

	}
	/**
	 * 
	 * 计算一个16进制字符的10进制值
	 * 
	 * 输入：0-F
	 * 
	 */

	private static int getChrInt(char chr) {

		int iRet = 0;

		if (chr == "0".charAt(0))
			iRet = 0;

		if (chr == "1".charAt(0))
			iRet = 1;

		if (chr == "2".charAt(0))
			iRet = 2;

		if (chr == "3".charAt(0))
			iRet = 3;

		if (chr == "4".charAt(0))
			iRet = 4;

		if (chr == "5".charAt(0))
			iRet = 5;

		if (chr == "6".charAt(0))
			iRet = 6;

		if (chr == "7".charAt(0))
			iRet = 7;

		if (chr == "8".charAt(0))
			iRet = 8;

		if (chr == "9".charAt(0))
			iRet = 9;

		if (chr == "A".charAt(0))
			iRet = 10;

		if (chr == "B".charAt(0))
			iRet = 11;

		if (chr == "C".charAt(0))
			iRet = 12;

		if (chr == "D".charAt(0))
			iRet = 13;

		if (chr == "E".charAt(0))
			iRet = 14;

		if (chr == "F".charAt(0))
			iRet = 15;

		return iRet;

	}	
	public static String getByteStr(byte[] byt) {

		String strRet = "";

		for (int i = 0; i < byt.length; i++) {

			// System.out.println(byt[i]);

			strRet += getHexValue((byt[i] & 240) / 16);

			strRet += getHexValue(byt[i] & 15);

		}

		return strRet;

	}	
	private static String getHexValue(int s) {

		String sRet = null;

		switch (s) {

		case 0:
			sRet = "0";
			break;

		case 1:
			sRet = "1";
			break;

		case 2:
			sRet = "2";
			break;

		case 3:
			sRet = "3";
			break;

		case 4:
			sRet = "4";
			break;

		case 5:
			sRet = "5";
			break;

		case 6:
			sRet = "6";
			break;

		case 7:
			sRet = "7";
			break;

		case 8:
			sRet = "8";
			break;

		case 9:
			sRet = "9";
			break;

		case 10:
			sRet = "A";
			break;

		case 11:
			sRet = "B";
			break;

		case 12:
			sRet = "C";
			break;

		case 13:
			sRet = "D";
			break;

		case 14:
			sRet = "E";
			break;

		case 15:
			sRet = "F";

		}

		return sRet;

	}

	public static String generatorKey() {
		byte[] bytK1 = null;
		try {

			KeyGenerator kg = KeyGenerator.getInstance("DES");

			kg.init(56);

			Key ke = kg.generateKey();

			bytK1 = ke.getEncoded();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return getByteStr(bytK1);
	}	
	public  static byte[] addBuff(byte buff[], byte add[]) {
		if (buff == null || add == null) {
			return null;
		}
		int bufflength = buff.length;
		int addlength = add.length;
		int templenth = bufflength + addlength;
		byte temp[] = new byte[templenth];

		for (int i = 0; i < templenth; i++) {
			if (i < bufflength) {
				temp[i] = buff[i];
			} else {
				temp[i] = add[i - bufflength];
			}

		}
		return temp;
	}
	public  static byte[] subBuff(byte buff[], int from, int length) {
		if (buff == null) {
			return null;
		}
		if (buff.length < from + length) {
			return null;
		}
		byte temp[] = new byte[length];
		for (int i = 0; i < length; i++) {
			temp[i] = buff[from + i];
		}
		return temp;
	}	
}
