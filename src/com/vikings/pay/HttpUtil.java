package com.vikings.pay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.json.JSONObject;

public class HttpUtil {

	private static final int timeout = 10000;

	private static final String encoding = "UTF-8";

	public static HttpURLConnection getPostConn(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept-Language", "zh-CN");
		conn.setRequestProperty("Cache-Control", "no-cache");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (compatible; MSIE 6.0; Windows NT)");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(timeout);
		conn.setReadTimeout(timeout);
		System.setProperty("http.keepAlive", "false");
		conn.setUseCaches(false);
		conn.setDefaultUseCaches(false);
		conn.connect();
		return conn;
	}

	private static byte[] readRespBytes(InputStream input) throws IOException {
		byte[] buf = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int count = -1;
		while ((count = input.read(buf)) != -1) {
			out.write(buf, 0, count);
		}
		byte[] rs = out.toByteArray();
		return rs;
	}

	private static byte[] decompress(byte[] src) throws IOException {
		return decompress(src, 0);
	}

	private static byte[] decompress(byte[] src, int start) throws IOException {
		GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(src,
				start, src.length - start));
		byte[] buf = new byte[1024];
		int num = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((num = in.read(buf, 0, buf.length)) != -1) {
			baos.write(buf, 0, num);
		}
		byte[] dest = baos.toByteArray();
		baos.close();
		in.close();
		return dest;
	}

	public static String readGzipResp(InputStream input, String encoding)
			throws IOException {
		return new String(decompress(readRespBytes(input)), encoding);
	}

	public static String httpPost(String url, JSONObject json)
			throws IOException {
		HttpURLConnection conn = getPostConn(url);
		OutputStream os = conn.getOutputStream();
		os.write(json.toString().getBytes(encoding));
		os.flush();

		InputStream in = conn.getInputStream();
		String rs = readGzipResp(in, encoding);
		int code = conn.getResponseCode();
		conn.disconnect();
		if (code != 200)
			throw new IOException("get http error,response code:" + code);
		return rs;
	}

}
