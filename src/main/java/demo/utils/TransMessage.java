package demo.utils;


/**
 * 收取请求流信息,发送流信息
 * @author gaoq
 *
 */
public class TransMessage {
//	
//	/**
//	 * 通过HTTP的POST或GET向指定的url发送内容. 并将服务端返回的内容按照UTF8编码返回.
//	 * 
//	 * @param request
//	 *            HttpServletRequest
//	 * 
//	 * @param strURL
//	 *            HTTP地址
//	 * @param method
//	 *            (GET OR POST)
//	 * @param Filenamein
//	 *            文本内容
//	 * @return 服务端返回的内容 按照UTF8编码
//	 */
//	public static String sendBodyHttpURL(HttpServletRequest request, String mobile,
//			String strURL, String method, String filenamein) {
//		String ret = "";
//		HttpURLConnection httpConn;
//
//		try {
//			URL url = new URL(strURL);
//			httpConn = (HttpURLConnection) url.openConnection();
//			httpConn.setConnectTimeout(180000);
//			httpConn.setReadTimeout(180000); 
//			httpConn.setDoOutput(true);
//			httpConn.setDoInput(true);
//
//			httpConn.setRequestMethod(method);
//			if (request != null) {
//				httpConn.setRequestProperty("Host", request.getHeader("Host"));
//				httpConn.setRequestProperty("UA", request.getHeader("UA"));
//				httpConn.setRequestProperty("Version", request.getHeader("Version"));
//				httpConn.setRequestProperty("Ptype", request.getHeader("Ptype"));
//				if (mobile != null && !"".equals(mobile)) {
//					httpConn.setRequestProperty("X-Up-Calling-Line-ID", mobile);				
//				} else {
//					httpConn.setRequestProperty("X-Up-Calling-Line-ID", request
//							.getHeader("X-Up-Calling-Line-ID"));
//				}
//				httpConn.setRequestProperty("Cache-Control", request
//						.getHeader("Cache-Control"));
//				httpConn.setRequestProperty("Content-Type", request
//						.getHeader("Content-Type"));
//			} else if (mobile != null && !"".equals(mobile)) {
//				httpConn.setRequestProperty("X-Up-Calling-Line-ID", mobile);				
//			}
//
//			
//			if (method.equalsIgnoreCase("post")) {
//				DataOutputStream out = new DataOutputStream(httpConn
//						.getOutputStream());
//				out.write(filenamein.getBytes("UTF-8"));
//				out.flush();
//				out.close();
//			}
//			InputStream stream = httpConn.getInputStream();
//			DataInputStream in = new DataInputStream(stream);
//			byte[] bin = null;
//			byte[] inc = new byte[1024];
//			int datelength = 0;
//			int insize = 0;
//			while ((insize = in.read(inc)) != -1) {
//				int oldlength = datelength;
//				datelength += insize;
//				byte[] oldbin = new byte[datelength];
//				for (int i = 0; i < oldlength; i++) {
//					oldbin[i] = bin[i];
//				}	
//				for (int i = oldlength; i < datelength; i++) {
//					oldbin[i] = inc[i - oldlength];
//				}	
//				bin = oldbin;
//			}
//			ret = new String(bin, "UTF8");
//			in.close();
//		} catch (Exception ex) {
//			LogUtil.logWrite(Constant.LOG_LEVEL_ERROR, "TransMessage", "sendBodyHttpURL", 
//					"", ex.getMessage(), "", Constant.LOG_SORT_EXCEPTION);
//			return "1";
//		}
//		return ret;
//	}
//	
//	public static String sendBodyHttpURL(HttpServletRequest request, String strURL, 
//				String method, String filenamein) {
//		String ret = "";
//		HttpURLConnection httpConn;
//		try {
//			URL url = new URL(strURL);
//			httpConn = (HttpURLConnection) url.openConnection();
//			httpConn.setConnectTimeout(180000);
//			httpConn.setReadTimeout(180000); 
//			httpConn.setDoOutput(true);
//			httpConn.setDoInput(true);
//
//			if (request != null) {
//				httpConn.setRequestProperty("Host", request.getHeader("Host"));
//				httpConn.setRequestProperty("UA", request.getHeader("UA"));
//				if (request.getHeader("Version") != null) {
//					httpConn.setRequestProperty("Version", request.getHeader("Version"));
//				} else {
//					httpConn.setRequestProperty("Version", request.getParameter("Version"));
//				}
//				httpConn.setRequestProperty("Ptype", request.getHeader("Ptype"));
//				if (request.getHeader("X-Up-Calling-Line-ID") != null) {
//					httpConn.setRequestProperty("X-Up-Calling-Line-ID", request.getHeader("X-Up-Calling-Line-ID"));
//				}
//			}
//			
//			httpConn.setRequestMethod(method);
//			
//			if (method.equalsIgnoreCase("post")) {
//				DataOutputStream out = new DataOutputStream(httpConn
//						.getOutputStream());
//				out.write(filenamein.getBytes("UTF-8"));
//				out.flush();
//				out.close();
//			}
//			InputStream stream = httpConn.getInputStream();
//			DataInputStream in = new DataInputStream(stream);
//			byte[] bin = null;
//			byte[] inc = new byte[1024];
//			int datelength = 0;
//			int insize = 0;
//			while ((insize = in.read(inc)) != -1) {
//				int oldlength = datelength;
//				datelength += insize;
//				byte[] oldbin = new byte[datelength];
//				for (int i = 0; i < oldlength; i++) {
//					oldbin[i] = bin[i];
//				}	
//				for (int i = oldlength; i < datelength; i++) {
//					oldbin[i] = inc[i - oldlength];
//				}	
//				bin = oldbin;
//			}
//			ret = new String(bin, "UTF8");
//			in.close();
//		} catch (Exception ex) {
//			LogUtil.logWrite(Constant.LOG_LEVEL_ERROR, "TransMessage", "sendBodyHttpURL", 
//					"", ex.getMessage(), "", Constant.LOG_SORT_EXCEPTION);
//			return "1";
//		}
//		return ret;
//		
//	}
//	
//	/**
//	 * 通过HTTP的POST或GET向指定的url发送内容.<br/>
//	 * 并将服务端返回的内容按照UTF8编码返回.
//	 * @param strURL HTTP地址
//	 * @param method (GET OR POST)
//	 * @param filenamein 文本内容
//	 * @return 服务端返回的内容 按照UTF8编码
//	 */
//	public static String sendBodyHttpURL(String strURL, String method, String filenamein) {
//		String ret = "";
//		HttpURLConnection httpConn;
//
//		try {
//			URL url = new URL(strURL);
//			httpConn = (HttpURLConnection) url.openConnection();
//			httpConn.setConnectTimeout(180000);
//			httpConn.setReadTimeout(180000); 
//			httpConn.setDoOutput(true);
//			httpConn.setDoInput(true);
//
//			httpConn.setRequestMethod(method);
//			
//			if (method.equalsIgnoreCase("post")) {
//				DataOutputStream out = new DataOutputStream(httpConn
//						.getOutputStream());
//				out.write(filenamein.getBytes("UTF-8"));
//				out.flush();
//				out.close();
//			}
//			
//			InputStream stream = httpConn.getInputStream();
//			DataInputStream in = new DataInputStream(stream);
//			byte[] bin = null;
//			byte[] inc = new byte[1024];
//			int datelength = 0;
//			int insize = 0;
//			while ((insize = in.read(inc)) != -1) {
//				int oldlength = datelength;
//				datelength += insize;
//				byte[] oldbin = new byte[datelength];
//				for (int i = 0; i < oldlength; i++) {
//					oldbin[i] = bin[i];
//				}	
//				for (int i = oldlength; i < datelength; i++) {
//					oldbin[i] = inc[i - oldlength];
//				}	
//				bin = oldbin;
//			}
//			ret = new String(bin, "UTF8");
//			in.close();
//		} catch (Exception ex) {
//			LogUtil.logWrite(Constant.LOG_LEVEL_ERROR, "TransMessage", "sendBodyHttpURL", 
//					"", ex.getMessage(), "", Constant.LOG_SORT_EXCEPTION);			
//			return "1";
//		}
//		return ret;
//	}
//	
//	/**
//	 * 返回请求流信息 
//	 * @param request
//	 * @return String
//	 */
//	public static String getReqStream(ServletRequest request) {
//		String retcode = "";
//		try {
//			ServletInputStream is = request.getInputStream();
//			
//			byte[] in_b;
//			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
//			/* buff用于存放循环读取的临时数据 */
//			byte[] buff = new byte[100];			
//			int rc = 0;
//			while ((rc = is.read(buff, 0, 100)) > 0) {
//				swapStream.write(buff, 0, rc);
//			}	
//			in_b = swapStream.toByteArray();
//			if (in_b.length > 0) {
//				retcode = new String(in_b, "UTF-8");
//			}
//			swapStream.close();
//		} catch (Exception ex) {
//			LogUtil.logWrite(Constant.LOG_LEVEL_ERROR, "TransMessage", "getReqStream", 
//					"", "read input stream in byte []", request.getRemoteAddr(),
//					Constant.LOG_SORT_EXCEPTION);			
//		}
//		return retcode;
//	}
//	
//	/**
//	 * 返回请求流信息 
//	 * @param request
//	 * @return String
//	 */
//	public static Document getReqStreamByDoc(ServletRequest request) {
//		Document document = null;
//		try {
//			ServletInputStream is = request.getInputStream();
//			SAXReader reader = new SAXReader(); 
//			document = reader.read(is);   
//		} catch (Exception ex) {
//			LogUtil.logWrite(Constant.LOG_LEVEL_ERROR, "TransMessage", "getReqStream", 
//					"", "read input stream in byte []", request.getRemoteAddr(),
//					Constant.LOG_SORT_EXCEPTION);			
//		}
//		return document;
//	}	
//	
//	/**
//	 * 发送
//	 * @param strURLs
//	 * @param method
//	 * @param filenamein
//	 * @return "1" 异常失败
//	 */
//	public static String sendBodyHttpURL(String[] strURLs, String method, String filenamein) {
//		String result = "1";
//		if (strURLs == null || strURLs.length == 0) {
//			return result;
//		}
//		String [] urls = strURLs.clone();
//		if (urls.length == 1) {
//			result = TransMessage.sendBodyHttpURL(urls[0], method, filenamein);
//		} else {
//			Random random = new Random();
//			for (int i = 0; i < strURLs.length; i++) {
//				if ("1".equals(result)) {
//					int rand = random.nextInt(urls.length);
//					result = TransMessage.sendBodyHttpURL(urls[rand], method, filenamein);
//					urls[rand] = "";
//					if (urls.length != 1) {
//						String[] tmp = new String[urls.length -1];
//						for (int j = 0; j < tmp.length; j++) {
//							if (j < rand) {
//								tmp[j] = urls[j];
//							} else {
//								tmp[j] = urls[j + 1];
//							}
//						}
//						urls = tmp;
//					}
//				} else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
//	
//	public static void main(String[] args) {
//		System.out.println(System.currentTimeMillis());
//		Random random = new Random();
//		String[] strURLs = new String[]{"http://0","http://1","http://2","http://3","http://4","http://5","http://6","http://7","http://8","http://9","http://10","http://11"};
//		String[] urls = strURLs.clone();
//		String result = "1";
//		for (int i = 0; i < strURLs.length; i++) {
//			if ("1".equals(result)) {
//				int rand = random.nextInt(urls.length);
//				System.out.println("*****************************************");
//				System.out.println("rand:" + rand);
//				urls[rand] = "";
//				if (urls.length != 1) {
//					String[] tmp = new String[urls.length -1];
//					for (int j = 0; j < tmp.length; j++) {
//						if (j < rand) {
//							tmp[j] = urls[j];
//						} else {
//							tmp[j] = urls[j + 1];
//						}
//					}
//					urls = tmp;
//					for (int j = 0; j < urls.length; j++) {
//						System.out.println(urls[j]);
//					}
//				}
//				System.out.println("*****************************************");
//			} else {
//				break;
//			}
//			
//		}
//		
//		System.out.println(System.currentTimeMillis());
//	}
//
}
