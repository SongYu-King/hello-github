package demo.http;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author wangshangyu
 * @description 发送https请求
 * @date 2014-3-19 下午02:21:54
 */
public class SendHttpsRequest {

    private static class SecurityTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * 使用https协议发送get请求
     *
     * @param url
     * @return
     */
    public static String sendHttpsByGet(String url) {
        String result = "";
        try {
            SSLContext sslContext = SSLContext.getInstance("SSLv3");
            TrustManager[] trustManager = {new SecurityTrustManager()};
            sslContext.init(null, trustManager, new java.security.SecureRandom());
            HttpsURLConnection httpsConn = (HttpsURLConnection) new URL(url).openConnection();
            httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
            httpsConn.setHostnameVerifier(new TrustyHostnameVerifier());
            httpsConn.setRequestMethod("GET");
            httpsConn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            in.close();
            httpsConn.disconnect();
        } catch (Exception e) {
            System.out.println("Exception at SendHttpsRequest.sendHttpsByGet() : " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 使用https协议发送post请求
     *
     * @param url
     * @return
     */
    public static String sendHttpsByPost(String url, String param) throws Exception {
        String result = "";
        try {
            SSLContext sslContext = SSLContext.getInstance("SSLv3");
            TrustManager[] trustManager = {new SecurityTrustManager()};
            sslContext.init(null, trustManager, new java.security.SecureRandom());
            HttpsURLConnection httpsConn = (HttpsURLConnection) new URL(url).openConnection();
            httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
            httpsConn.setHostnameVerifier(new TrustyHostnameVerifier());
            httpsConn.setDoOutput(true);
            httpsConn.setDoInput(true);
            httpsConn.setUseCaches(false);
            httpsConn.setRequestMethod("POST");

            httpsConn.setRequestProperty("Accept", "*/*");
//			httpsConn.setRequestProperty("client_id", "2198288703");
//			httpsConn.setRequestProperty("client_secret", "cf302175d9bb46f87f92178d69e79217");
//			httpsConn.setRequestProperty("connection", "Keep-Alive");
//			httpsConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//			httpsConn.setRequestProperty("Content-type", "application/x-java-serialized-object");
            httpsConn.connect();

            OutputStreamWriter out = new OutputStreamWriter(httpsConn.getOutputStream());
            out.write(param);
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            in.close();
            httpsConn.disconnect();
        } catch (Exception e) {
            System.out.println("Exception at SendHttpsRequest.sendHttpsByPost() : " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
