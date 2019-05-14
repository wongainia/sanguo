package com.vikings.sanguo.network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.GzipUtil;

public class HttpConnector {

	public static final int timeout = Config.getIntConfig("serverTimeout");

	public static final int HTTP_CMD = 0;

	public static final String encoding = "UTF-8";

	private static final HttpConnector instance = new HttpConnector();

	private HttpConnector() {

	}

	public static HttpConnector getInstance() {
		return instance;
	}

	public HttpURLConnection getConn(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept-Language", "zh-CN");
		conn.setRequestProperty("Cache-Control", "no-cache");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (compatible; MSIE 6.0; Windows NT)");
		conn.setDoInput(true);
		conn.setConnectTimeout(timeout);
		conn.setReadTimeout(timeout);
		System.setProperty("http.keepAlive", "false");
		conn.setUseCaches(false);
		conn.setDefaultUseCaches(false);
		conn.connect();
		if (conn.getResponseCode() != 200)
			throw new IOException("get http error,response code:"
					+ conn.getResponseCode() + "url:" + url);
		return conn;
	}

	public String httpGet(String url) throws IOException {
		HttpURLConnection conn = getConn(url);
		InputStream in = conn.getInputStream();
		String rs = readResp(in, encoding);
		conn.disconnect();
		return rs;
	}

	public synchronized String httpGet(String url, JSONObject json)
			throws IOException {
		HttpURLConnection conn = getConn(url);
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

	public String httpPost(String url) throws IOException {
		HttpURLConnection conn = getPostConn(url);
		InputStream in = conn.getInputStream();
		String rs = readGzipResp(in, encoding);
		conn.disconnect();
		return rs;
	}

	public String httpPost(String url, String param) throws IOException {
		HttpURLConnection conn = getPostConn(url);
		OutputStream os = conn.getOutputStream();
		os.write(param.getBytes(encoding));
		os.flush();
		InputStream in = conn.getInputStream();
		String rs = readResp(in, encoding);
		int code = conn.getResponseCode();
		conn.disconnect();
		if (code != 200)
			throw new IOException("get http error,response code:" + code);
		return rs;
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

	// public String getLocation(String Province, String city) {
	// String query = Config.getConfig("geoUrl") + URLEncoder.encode(Province)
	// + "+" + URLEncoder.encode(city);
	// String rs = null;
	// for (int i = 0; i < 3; i++) {
	// try {
	// rs = httpGet(query);
	// if (!StringUtil.isNull(rs) && rs.startsWith("200"))
	// break;
	// rs = null;
	// } catch (IOException e) {
	// }
	// }
	// if (rs == null)
	// return null;
	// StringBuilder buf = new StringBuilder(rs);
	// StringUtil.removeCsv(buf);
	// StringUtil.removeCsv(buf);
	// return buf.toString();
	// }

	/**
	 * 根据tile获取地址所在城市，转换为province/city 代码
	 * 
	 * @param tileId
	 * @return
	 */
	public byte[] getAddrCity(long tileId) {

		Address addr = CacheMgr.addrCache.getAddr(tileId);
		try {
			String p = addr.getAdminArea();
			String c = addr.getLocality();
			if (p.endsWith("省"))
				p = p.replace("省", "");
			if (p.endsWith("特别行政区"))
				p = p.replace("特别行政区", "");
			if (c.endsWith("市"))
				c = c.replace("市", "");
			byte[] city = new byte[2];
			city[0] = (byte) Config.indexOf(p, Config.province);
			city[1] = (byte) Config.indexOf(c, Config.getCitys(city[0]));
			return city;
		} catch (Exception e) {
			return null;
		}
	}

	public InputStream httpGetStream(String url) throws IOException {
		HttpURLConnection conn = getConn(url);
		return conn.getInputStream();
	}

	public List<String> httpGetLines(String url) throws IOException {
		HttpURLConnection conn = getConn(url);
		InputStream in = conn.getInputStream();
		List<String> rs = readRespLines(in);
		conn.disconnect();
		return rs;
	}

	public byte[] httpGetBytes(String url) throws IOException {
		HttpURLConnection conn = getConn(url);
		InputStream in = conn.getInputStream();
		byte[] rs = readRespBytes(in);
		conn.disconnect();
		return rs;
	}

	private byte[] readRespBytes(InputStream input) throws IOException {
		byte[] buf = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int count = -1;
		while ((count = input.read(buf)) != -1) {
			out.write(buf, 0, count);
		}
		byte[] rs = out.toByteArray();
		// NetStat.getInstance().log(HTTP_CMD, false, rs.length);
		return rs;
	}

	public String readGzipResp(InputStream input, String encoding)
			throws IOException {
		return new String(GzipUtil.decompress(readRespBytes(input)), encoding);
	}

	public String readResp(InputStream input, String encoding)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input,
				encoding));
		StringBuilder buf = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) {
			buf.append(line);
		}
		in.close();
		String rs = buf.toString();
		NetStat.getInstance()
				.log(HTTP_CMD, false, rs.getBytes(encoding).length);
		return rs;
	}

	private List<String> readRespLines(InputStream input) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input,
				encoding));
		List<String> rs = new ArrayList<String>();
		String line = null;
		while ((line = in.readLine()) != null) {
			NetStat.getInstance().log(HTTP_CMD, false,
					line.getBytes(encoding).length);
			rs.add(line);
		}
		in.close();
		return rs;
	}

	/**
	 * 上传资料头像
	 * 
	 * @param name
	 * @param file
	 * @throws Exception
	 */
	public void uploadIcon(long name, File file) throws Exception {
		HttpURLConnection conn = getPostConn(Config.uploadUrl);
		OutputStream out = conn.getOutputStream();
		BytesUtil.putLong(out, name);
		BytesUtil.putInt(out, (int) file.length());
		FileInputStream in = new FileInputStream(file);
		byte[] buf = new byte[4096];
		int count = -1;
		while ((count = in.read(buf)) != -1) {
			out.write(buf, 0, count);
		}
		in.close();
		out.flush();
		out.close();
		int code = conn.getResponseCode();
		if (code != 200)
			throw new IOException("get http error,response code:" + code);
		conn.disconnect();
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
		conn.setReadTimeout(timeout);
		System.setProperty("http.keepAlive", "false");
		conn.setUseCaches(false);
		conn.setDefaultUseCaches(false);
		conn.connect();
		// if (conn.getResponseCode() != 200)
		// throw new IOException("get http error,response code:"
		// + conn.getResponseCode() + "url:" + url);
		return conn;
	}

	public void uploadSpeedStat(List<String> ls) {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(Config.statUrl)
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(timeout);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			for (String line : ls) {
				out.write(line.getBytes(encoding));
			}
			out.flush();
			out.close();
			int code = conn.getResponseCode();
			conn.disconnect();
			if (code != 200)
				throw new IOException("http error,response code:" + code);
			// HttpPost post = new HttpPost(Config.statUrl);
			// StringBuilder buf = new StringBuilder();
			// for (String line : ls) {
			// buf.append(line);
			// }
			// post.setEntity(new StringEntity(buf.toString()));
			// HttpClient client = new DefaultHttpClient();
			// client.execute(post);
		} catch (Exception e) {
		}
	}

	public void uploadCrash(String log) {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(
					Config.crashUrl).openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(timeout);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			out.write(log.getBytes(encoding));
			out.flush();
			out.close();
			int code = conn.getResponseCode();
			if (code != 200)
				throw new IOException("http error,response code:" + code);
			conn.disconnect();
		} catch (Exception e) {
		}
	}

	public JSONObject getVersion() throws IOException, JSONException {
		// 添加请求参数，避免http 资源被缓存
		return new JSONObject(new String(httpGetBytes(Config.versionUrl + "?"
				+ System.currentTimeMillis()), "UTF-8"));
	}

	public JSONObject getVersion(String url) throws IOException, JSONException {
		// 添加请求参数，避免http 资源被缓存
		return new JSONObject(new String(httpGetBytes(url + "?"
				+ System.currentTimeMillis()), "UTF-8"));
	}

	public int getNotice(String url) throws IOException {
		return getInteger(url + "?" + System.currentTimeMillis());
	}

	public int getInteger(String url) throws IOException {
		return Integer.parseInt(new String(httpGetBytes(url), "UTF-8"));
	}

	public void uploadString(String log, String url) {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(timeout);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			out.write(log.getBytes(encoding));
			out.flush();
			out.close();
			int code = conn.getResponseCode();
			if (code != 200)
				throw new IOException("http error,response code:" + code);
			conn.disconnect();
		} catch (Exception e) {
		}
	}

}
