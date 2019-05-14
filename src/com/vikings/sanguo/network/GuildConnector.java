package com.vikings.sanguo.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.GuildChatData;

public class GuildConnector {

	/**
	 * 聊天
	 * 
	 * @param url
	 * @param guildid
	 * @param userId
	 * @param msg
	 * @throws GameException
	 */
	public static void addChatMsg(String url, int guildid, int userId,
			String msg) throws GameException {
		try {
			JSONObject params = new JSONObject();
			params.put("guildId", guildid);
			params.put("userId", userId);
			params.put("msg", msg);

			String json = HttpConnector.getInstance().httpPost(url, params);
			JSONObject rs = new JSONObject(json);
			if (rs.getInt("rs") != 0)
				throw new GameException(rs.getString("error"));
		} catch (JSONException e) {
			Log.e("GuildConnector", e.getMessage(), e);
			throw new GameException("数据解析失败!");
		} catch (IOException e) {
			Log.e("GuildConnector", e.getMessage(), e);
			throw new GameException("网络异常,请重试");
		}
	}

	/**
	 * 取聊天消息 userId 为自己的id， time是int32格式 time不填 是取最新10条，
	 * time有值是取从time开始之后10条，如果有，返回消息列表 查看消息后翻【size可能为0，提示用户没有最新消息。
	 * 返回结果已经按时间升序排列】 查看消息前翻【size可能为0，提示用户最大保存1000条消息，返回结果已经按时间升序排列】
	 * 
	 * @param url
	 * @param guildid
	 * @param userId
	 * @param time
	 * @return
	 * @throws GameException
	 */
	public static List<GuildChatData> getChatMsg(String url, int guildid,
			int userId, int time) throws GameException {
		try {
			JSONObject params = new JSONObject();
			params.put("guildId", guildid);
			params.put("userId", userId);
			params.put("time", time);
			params.put("sid", Config.serverId);

			String json = HttpConnector.getInstance().httpPost(url, params);

			JSONObject rs = new JSONObject(json);
			if (rs.getInt("rs") != 0)
				throw new GameException(rs.getString("error"));

			JSONArray ja = rs.getJSONArray("content");
			List<GuildChatData> datas = new ArrayList<GuildChatData>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				GuildChatData data = new GuildChatData();
				data.setGuildid(jo.getInt("guildId"));
				data.setUserId(jo.getInt("userId"));
				data.setTime(jo.getInt("time"));
				data.setMsg(jo.getString("msg"));
				datas.add(data);
			}
			return datas;
		} catch (JSONException e) {
			Log.e("GuildConnector", e.getMessage(), e);
			throw new GameException("数据解析失败!");
		} catch (IOException e) {
			Log.e("GuildConnector", e.getMessage(), e);
			throw new GameException("网络异常,请重试");
		}
	}

	/**
	 * 查最新的聊天消息
	 * 
	 * @param url
	 * @param guildid
	 *            （查世界传0， 查家族传家族id,查国家传国家Id）
	 * @param userId
	 * @return
	 * @throws GameException
	 */
	public static GuildChatData getLatestChatData(String url, int guildid,
			int userId) throws GameException {
		try {
			JSONObject params = new JSONObject();
			params.put("guildId", guildid);
			params.put("userId", userId);
			params.put("sid", Config.serverId);

			String json = HttpConnector.getInstance().httpPost(url, params);

			JSONObject rs = new JSONObject(json);
			if (rs.getInt("rs") != 0)
				throw new GameException(rs.getString("error"));

			JSONArray ja = rs.getJSONArray("content");
			List<GuildChatData> datas = new ArrayList<GuildChatData>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				GuildChatData data = new GuildChatData();
				data.setGuildid(jo.getInt("guildId"));
				data.setUserId(jo.getInt("userId"));
				data.setTime(jo.getInt("time"));
				data.setMsg(jo.getString("msg"));
				datas.add(data);
			}
			if (datas.isEmpty())
				return null;
			else
				return datas.get(0);
		} catch (JSONException e) {
			Log.e("GuildConnector", e.getMessage(), e);
			throw new GameException("数据解析失败!");
		} catch (IOException e) {
			Log.e("GuildConnector", e.getMessage(), e);
			throw new GameException("网络异常,请重试");
		}
	}
}
