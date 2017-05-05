package demo.utils;

/**
 *
 * 类说明
 */
public class Sequence {
	private static long contentId = System.currentTimeMillis();

	public synchronized static long getTimeId() {
		return contentId++;
	}

	public static String getUUID() {
		/*org.doomdark.uuid.UUIDGenerator uu = org.doomdark.uuid.UUIDGenerator
				.getInstance();
		return uu.generateTimeBasedUUID(
				org.doomdark.uuid.EthernetAddress
						.valueOf("00:01:02:03:04:05:06")).toString();*/
		return "";
	}

	public static String getRadomId() {
		/*return org.doomdark.uuid.UUIDGenerator.getInstance()
				.generateRandomBasedUUID().toString();*/
		return "";
	}
}

