package demo.utils;


public class ObjectUtil {
	/**
	 * 判断对象是否为空。
	 * @param object 要判断的对象。
	 * @return 是否为空，如果为空则返回True,否则返回False。
	 */
	public static boolean isNull(Object object) {
		return (object == null);
	}

	/**
	 * 判断对象是否非空
	 * @param object 要判断的对象
	 * @return 是否为空，如果为空则返回True，否则返回False。
	 */
	public static boolean isNotNull(Object object) {
		return !isNull(object);
	}
}
