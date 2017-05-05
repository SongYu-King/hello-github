/**
 * @FileName: HttpClient.java
 * @author wangshangyu
 * @date 2014-9-18 下午05:39:53
 * @version 1.0
 */
package demo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @description 
 * @author wangshangyu
 * @date 2014-9-18 下午05:39:53
 * @version 1.0
 */
public class HttpClient {

	public HttpClient() {
	}

	/**
	 * 普通的HTTP请求
	 * @param url
	 * @param rule
	 * @return
	 */
//	public static String getHttpResult(String url) {
//		String result = "";
//		HttpGet request = new HttpGet(url);
//
//		HttpResponse response;
//		try {
//			response = HttpClients.createDefault().execute(request);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				result = EntityUtils.toString(response.getEntity());
//			}
//		} catch (ClientProtocolException e1) {
//			e1.printStackTrace();
//			return "";
//		} catch (IOException e1) {
//			e1.printStackTrace();
//			return "";
//		}
////		System.out.println("返回结果：" + result);
//		return result;
//	}

	/**
	 * @param url 请求地址
	 * @return
	 */
	public static String httpGet(String url) {
		String result = "";
		try {
			HttpURLConnection hcon = (HttpURLConnection) new URL(url).openConnection();
			hcon.setDoOutput(true);
			hcon.setRequestMethod("GET");
			hcon.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			hcon.setRequestProperty("user-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			hcon.connect();

			InputStream is = hcon.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				result += line;
			}
			is.close();
			// 断开连接
			hcon.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param url
	 * @param param
	 * @return
	 */
	public static String httpPost(String url, String param) {
		String result = "";
		try {
			HttpURLConnection hcon = (HttpURLConnection) new URL(url).openConnection();
			hcon.setDoInput(true);
			hcon.setDoOutput(true);
			hcon.setUseCaches(false);
			hcon.setRequestMethod("POST");
			hcon.setConnectTimeout(3000);
			hcon.setRequestProperty("Accept", "*/*");
			hcon.setRequestProperty("Connection", "Keep-Alive");
			hcon.setRequestProperty("Content-type", "application/x-java-serialized-object");
			hcon.setRequestProperty("Content-Type", "application/html;charset=UTF-8");
			hcon.setRequestProperty("user-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			hcon.connect();

			java.io.PrintWriter out = new java.io.PrintWriter(new java.io.OutputStreamWriter(hcon.getOutputStream(), "UTF-8"));
			out.write(param);
			out.flush();
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(hcon.getInputStream(), "UTF-8"));
			// 读取服务器对于此次http请求的返回信息
			String line = null;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			in.close();
			// 断开连接
			hcon.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		String url = "http://182.92.67.78/nlpdata/dataiotest.do";
//		String param = "{\"ret_count\":1,\"parse_result\":[{\"app_id\":400401,\"app_name\":\"TRAVEL.TRAIN.STATIONTOSTATION\",\"match_score\":0,\"negation\":0,\"text\":\"\",\"semantic\":{\"depart_date\":\"2014-10-17\",\"depart_time\":\"全天\",\"end_city\":\"高碑店\",\"start_city\":\"北京\",\"train_type\":\"全部列车\",\"result_type\":1}}],\"user_info\":{\"userid\":\"0013398181\"},\"location_info\":{\"province\":\"\",\"city\":\"\",\"address\":\"\",\"nearest_poi_name\":\"\",\"user_latitude\":\"\",\"user_longitude\":\"\"}}";
		String param = "{\"ret_count\":1,\"parse_result\":[{\"app_id\":400401,\"app_name\":\"TRAVEL.TRAIN.STATIONTOSTATION\",\"match_score\":0,\"negation\":0,\"text\":\"\",\"semantic\":{\"depart_date\":\"2014-10-17\",\"depart_time\":\"下午\",\"end_city\":\"天津\",\"start_city\":\"北京\",\"train_type\":\"城际高速\",\"result_type\":1}}],\"user_info\":{\"userid\":\"0013398181\"},\"location_info\":{\"province\":\"\",\"city\":\"\",\"address\":\"\",\"nearest_poi_name\":\"\",\"user_latitude\":\"\",\"user_longitude\":\"\"}}";
		String res = httpPost(url, param);
		System.out.println(res);
	}
}
