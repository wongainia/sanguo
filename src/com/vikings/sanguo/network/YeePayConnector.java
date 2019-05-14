/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-8-23
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.GzipUtil;

public class YeePayConnector {
	public static final int timeout = Config.getIntConfig("serverTimeout");
	public static final String encoding = "UTF-8";
	public static final int READ_TIMEOUT = 50000;
	public static final int HTTP_CMD = 0;

	private static final YeePayConnector instance = new YeePayConnector();

	private YeePayConnector() {}

	public static YeePayConnector getInstance() {
		return instance;
	}
	
	public synchronized String httpPost(String url, JSONObject json)
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

	public String readGzipResp(InputStream input, String encoding)
			throws IOException {
		return new String(GzipUtil.decompress(readRespBytes(input)), encoding);
	}

	private byte[] readRespBytes(InputStream input) throws IOException {
		byte[] buf = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int count = -1;
		while ((count = input.read(buf)) != -1) {
			out.write(buf, 0, count);
		}
		byte[] rs = out.toByteArray();
		NetStat.getInstance().log(HTTP_CMD, false, rs.length);
		return rs;
	}
	
	public HttpURLConnection getPostConn(String url) throws IOException {
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
		conn.setReadTimeout(READ_TIMEOUT);
		System.setProperty("http.keepAlive", "false");
		conn.setUseCaches(false);
		conn.setDefaultUseCaches(false);
		conn.connect();
		return conn;
	}
}
