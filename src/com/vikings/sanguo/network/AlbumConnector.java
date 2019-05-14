package com.vikings.sanguo.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.BytesUtil;

public class AlbumConnector {

	/**
	 * 删除留言
	 * 
	 * @param userId
	 * @param targetId
	 * @param timestamp
	 */
	public static void deleteAlbumComment(String url, int userId, int targetId,
			int time) throws GameException {
		// String url = "http://szkmol.gicp.net:8080/userAlbum/comment";
		try {
			JSONObject params = new JSONObject();
			params.put("op", "delete");
			params.put("userId", userId);
			params.put("targetId", targetId);
			params.put("time", time);
			params.put("sid", Config.serverId);

			String json = HttpConnector.getInstance().httpPost(url, params);
			JSONObject rs = new JSONObject(json);
			if (rs.getInt("rs") != 0) { // 失败
				throw new GameException(rs.getString("error"));
			}
		} catch (JSONException e) {
			Log.e("AlbumConnector", e.getMessage(), e);
			throw new GameException("哎呀，出错了，稍后请重试!!");
		} catch (IOException e) {
			Log.e("AlbumConnector", e.getMessage(), e);
			throw new GameException("网络异常,请重试");
		}
	}

	/**
	 * 上传相册照片
	 * 
	 * @param name
	 * @param file
	 * @throws IOException
	 */
	public static synchronized void uploadAlbum(String url, long name, File file)
			throws GameException {
		try {
			HttpURLConnection conn = HttpConnector.getInstance().getPostConn(
					url);

			OutputStream out = conn.getOutputStream();

			BytesUtil.putLong(out, name);
			BytesUtil.putInt(out, Config.serverId);
			BytesUtil.putInt(out, (int) file.length());
			FileInputStream inf = new FileInputStream(file);
			byte[] buf = new byte[4096];
			int count = -1;
			while ((count = inf.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			inf.close();
			out.flush();
			out.close();

			InputStream in = conn.getInputStream();
			String json = HttpConnector.getInstance().readGzipResp(in,
					HttpConnector.encoding);

			int code = conn.getResponseCode();
			if (code != 200)
				throw new IOException("get http error,response code:" + code);
			conn.disconnect();

			JSONObject rs = new JSONObject(json);
			if (rs.getInt("rs") != 0) { // 失败
				throw new GameException(rs.getString("error"));
			}
		} catch (JSONException e) {
			Log.e("AlbumConnector", e.getMessage(), e);
			throw new GameException("哎呀，出错了，稍后请重试!!");
		} catch (IOException e) {
			Log.e("AlbumConnector", e.getMessage(), e);
			throw new GameException("网络异常,请重试");
		}
	}

	/**
	 * 删除相册中照片
	 * 
	 * @param id
	 * @return 第一位为结果码，0表示成功；第二位为失败描述，只有失败时有值，成功时为空
	 */
	public static void deleteAlbum(String url, long id) throws GameException {
		// String url = "http://szkmol.gicp.net:8080/userAlbum/delete";
		try {
			JSONObject params = new JSONObject();
			params.put("id", id);
			params.put("sid", Config.serverId);

			String json = HttpConnector.getInstance().httpPost(url, params);
			JSONObject rs = new JSONObject(json);
			if (rs.getInt("rs") != 0)
				throw new GameException(rs.getString("error"));
		} catch (JSONException e) {
			Log.e("AlbumConnector", e.getMessage(), e);
			throw new GameException("哎呀，出错了，稍后请重试!!");
		} catch (IOException e) {
			Log.e("AlbumConnector", e.getMessage(), e);
			throw new GameException("网络异常,请重试");
		}
	}
}
