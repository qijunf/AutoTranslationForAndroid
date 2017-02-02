package com.qijunf.googletrans.translation;

import java.net.*;
import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.*;

/**
 * Created by qijunf on 2017/1/22.
 */

public class WebRequest {

	private static void setRequestHeader(HttpURLConnection conn) {
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		conn.setRequestProperty("Accept",
				"Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		conn.setRequestProperty("Content-Type", "application/josn");
	}

	public static String post(String url, String data, String charset) {
		try {
			URL __url = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) __url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			setRequestHeader(conn);
			OutputStreamWriter bos = new OutputStreamWriter(conn.getOutputStream(), charset);
			bos.write(data);
			bos.flush();
			BufferedReader bis = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bis.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			bis.close();
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * https获取josn字符串
	 * 
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String get(String url, String charset) {
		try {
			URL __url = new URL(url);
			TrustManager[] tm = { new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}

			} };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			HttpsURLConnection conn = (HttpsURLConnection) __url.openConnection();
			setRequestHeader(conn);
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod("GET");

			conn.connect();
			// 将返回的输入流转换成字符串
			// InputStream inputStream = conn.getInputStream();
			BufferedReader bis = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bis.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			bis.close();
			return sb.toString();
		} catch (Exception e) {
			System.out.println("翻译失败，请用浏览器打开Google翻译网站,确认是否网络正常");
			// e.printStackTrace();
			return null;
		}
	}

	public static byte[] file(String url) {
		try {
			URL __url = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) __url.openConnection();
			setRequestHeader(conn);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			byte[] b = new byte[1024];
			int length = -1;
			while ((length = bis.read(b)) != -1) {
				bos.write(b, 0, length);
				bos.flush();
			}
			bis.close();
			bos.close();
			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String mid(String value, String left, String right) {
		try {
			int i = value.indexOf(left) + left.length();
			return value.substring(i, value.indexOf(right, i));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String sub(String value, String mark, int len) {
		try {
			int i = value.indexOf(mark) + mark.length();
			return value.substring(i, i + len);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String decode(String value, String charset) {
		try {
			return URLDecoder.decode(value, charset);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encode(String value, String charset) {
		try {
			return URLEncoder.encode(value, charset);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}