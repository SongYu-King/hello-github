package demo.utils;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @desc 常用工具类
 * @version v1.0
 */
public class Util
{
	/**
	 * 序列化一个对象
	 * @param object 要序列化的对象
	 * @return 序列化对象后的字节数组
	 */
	public static byte[] serializeObject(Object object)
	{
		ObjectOutputStream objectOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		
		try
		{
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			
			objectOutputStream.writeObject(object);
			
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e)
		{
			return null;
		} finally
		{
			try
			{
				if (null != byteArrayOutputStream)
				{
					byteArrayOutputStream.close();
				}
				
				if (null != objectOutputStream)
				{
					objectOutputStream.close();
				}
			} catch (Exception e)
			{
				
			}
		}
	}
	
	/**
	 * 反序列化对象
	 * @param objectBytes 对象的字节数组 
	 * @return 反序列化后的对象
	 */
	public static Object unserializeObject(byte[] objectBytes)
	{
		ObjectInputStream objectInputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		
		try
		{
			byteArrayInputStream = new ByteArrayInputStream(objectBytes);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			
			return objectInputStream.readObject();
		} catch (Exception e)
		{
			return null;
		} finally
		{
			try
			{
				if (null != byteArrayInputStream)
				{
					byteArrayInputStream.close();
				}
				
				if (null != objectInputStream)
				{
					objectInputStream.close();
				}
			} catch (Exception e)
			{
				
			}
			
		}
	}
	
	
	/**
	 * 获取源目录下的配置文件
	 * @param propertiesName 配置文件的名称
	 * @return 配置文件
	 */
	public static Properties getProperties(String propertiesName)
	{
		InputStream inputStream = null;
		try
		{
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesName);
			
			Properties properties = new Properties();
			properties.load(inputStream);
			
			return properties;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (null != inputStream)
				{
					inputStream.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 * 获取时间戳
	 * @return
	 */
	public static int getTimestamp()
	{
		return (int)(System.currentTimeMillis() / 1000);
	}
	
	
	/**
	 * @param stringObj 字符串Object
	 * @return
	 */
	public static String isNullDefault(Object stringObj)
	{
		if (null == stringObj)
		{
			return "";
		}
		
		return stringObj.toString();
	}
	
	/**
	 * 判断一个字符串是不是有值，非 NULL||空格||空串
	 * @param str
	 * @return 
	 */
	public static boolean isNotEmpty(String str)
	{
		return null != str && !str.trim().equals("");
	}
	
	/**
	 * 集合求交集
	 * @return
	 */
	public static Set<String> interSet(List<Set<String>> setList)
	{
		
		Set<String> set = setList.get(0);
		
		for (int index = 1; index < setList.size(); index++)
		{
			set.retainAll(setList.get(index));
		}
		
		return set;
	}
	
	/**
	 * 根据指定格式获取当前时间的字符
	 * @param format 指定的格式
	 * @return 
	 */
	public static String getDateFormat(String format)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		
		return dateFormat.format(new Date());
	}
	
	
	/**
	 * 根据指定格式获取当前时间的字符
	 * @param format 指定的格式
	 * @return 
	 */
	public static String getDateFormat(String format, Date date)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		
		return dateFormat.format(date);
	}
	
	
	
	/**
	 * 格式化小数
	 * @param maximumFractionDigits 小数部分保留的最大位数
	 * @return 格式化后的小数
	 */
	public static String formatDouble(double d, int maximumFractionDigits)
	{
		NumberFormat format = NumberFormat.getNumberInstance();
		
		format.setMaximumFractionDigits(maximumFractionDigits);
		
		return format.format(d);
	}
	
	/**
	 *  获取本机local area network ip
	 * @return
	 */
	public static String getLocalIP() {
		StringBuilder IFCONFIG = new StringBuilder();
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						IFCONFIG.append(inetAddress.getHostAddress().toString());
					}

				}
			}
		} catch (SocketException ex) {
		}

		return IFCONFIG.toString();
	}

    /**
     * 将字符串转换为数字，如果字符串为空或非法，返回<code>defaultValue</code>
     * @param value 字符串值
     * @param defaultValue 默认值
     * @return 数字或<code>defaultValue</code>
     */
    public static int formatInt(String value,int defaultValue){
        try {
            if (isNotEmpty(value)) {
                return Integer.valueOf(value).intValue();
            }else {
                return defaultValue;
            }
        }catch (Exception e) {
            return defaultValue;
        }
    }

}
