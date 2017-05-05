package demo.utils;

public class AesJni {
	static {
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary("cAes");
	}

	public native byte[] decode_aes(String key, byte[] iv, byte[] in, int len);
	public native byte[] encode_aes(String key, byte[] iv, byte[] in, int len);
}
