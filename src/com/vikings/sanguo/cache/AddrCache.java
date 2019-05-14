package com.vikings.sanguo.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

import android.location.Address;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.ui.map.core.Geocoder;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;

/**
 * @author Brad.Chen
 */
public class AddrCache {

	private static final String fileName = "_tiles_";

	private static final int MAX_SAVE_COUNT = 300;

	private HashMap<Long, Address> saved = new HashMap<Long, Address>();

	private HashMap<Long, Address> cache = new HashMap<Long, Address>();

	private static Geocoder geocoder = new Geocoder();

	private AddrCache() {
	}

	public boolean needFetch(long tileId) {
		int x = TileUtil.tileId2x(tileId);
		int y = TileUtil.tileId2y(tileId);
		for (int i = x; i <= x; i++) {
			for (int j = y; j <= y; j++) {
				long id = TileUtil.index2TileId(i, j);
				if (saved.containsKey(id))
					return false;
				if (cache.containsKey(id))
					return false;
			}
		}
		return true;
	}

	public Address getAddrInCache(long tileId) {

		// 插入圣地的逻辑
		long fid = TileUtil.tileId2FiefId(tileId);
		if (CacheMgr.holyPropCache.isHoly(fid)) {
			try {
				HolyProp hp = (HolyProp) CacheMgr.holyPropCache.get(fid);
				Address a = new Address(Locale.CHINESE);
				a.setAdminArea("");
				a.setLocality("");
				a.setSubLocality(hp.getName());
				a.setFeatureName(hp.getScaleDesc());
				return a;
			} catch (GameException e) {
			}

		}

		int x = TileUtil.tileId2x(tileId);
		int y = TileUtil.tileId2y(tileId);
		for (int i = x; i <= x; i++) {
			for (int j = y; j <= y; j++) {
				long id = TileUtil.index2TileId(i, j);
				if (saved.containsKey(id))
					return saved.get(id);
				if (cache.containsKey(id))
					return cache.get(id);
			}
		}
		return null;
	}

	public Address getAddr(long tileId) {
		Address addr = getAddrInCache(tileId);
		if (addr != null)
			return addr;
		// 没缓存
		addr = geocoder.getFromLocation(
				((double) TileUtil.getTileCenterLat(tileId)) / 1E6,
				((double) TileUtil.getTileCenterLon(tileId)) / 1E6);
		// if (addr != null)
		cache.put(tileId, addr);
		return addr;
	}

	public void save() {
		if (cache.size() == 0)
			return;
		List<String> ls = new ArrayList<String>(cache.size());
		for (Long id : cache.keySet()) {
			Address a = cache.get(id);
			ls.add(id + "," + StringUtil.noNull(a.getAdminArea()) + ","
					+ StringUtil.noNull(a.getLocality()) + ","
					+ StringUtil.noNull(a.getSubLocality()) + ","
					+ StringUtil.noNull(a.getFeatureName()));
		}
		List<String> ls2 = new ArrayList<String>(cache.size());
		ls2.addAll(ls);
		int index = ls2.size();
		for (Long id : saved.keySet()) {
			Address a = saved.get(id);
			ls2.add(id + "," + StringUtil.noNull(a.getAdminArea()) + ","
					+ StringUtil.noNull(a.getLocality()) + ","
					+ StringUtil.noNull(a.getSubLocality()) + ","
					+ StringUtil.noNull(a.getFeatureName()));
			index++;
			if (index > MAX_SAVE_COUNT)
				break;
		}
		Config.getController().getFileAccess().saveSdcard(fileName, ls2, false);
		if (Setting.tileStat)
			new SaveTile(ls).run();
		// new Thread(new SaveTile(ls)).start();
	}

	public static AddrCache getInstance() {
		AddrCache ac = new AddrCache();
		try {
			List<String> lines = Config.getController().getFileAccess()
					.readSdcard(fileName);
			for (String line : lines) {
				StringBuilder buf = new StringBuilder(line);
				Address a = new Address(Locale.CHINESE);
				long tile = StringUtil.removeCsvLong(buf);
				a.setAdminArea(StringUtil.removeCsv(buf));
				a.setLocality(StringUtil.removeCsv(buf));
				a.setSubLocality(StringUtil.removeCsv(buf));
				a.setFeatureName(StringUtil.removeCsv(buf));
				ac.saved.put(tile, a);
			}
		} catch (Exception e) {
		}
		return ac;
	}

	private class SaveTile implements Runnable {

		List<String> tiles;

		SaveTile(List<String> tiles) {
			this.tiles = tiles;
		}

		@Override
		public void run() {
			try {
				HttpURLConnection conn = HttpConnector.getInstance()
						.getPostConn(Config.resURl + "tiles");
				ByteArrayOutputStream tmp = new ByteArrayOutputStream();
				GZIPOutputStream gout = new GZIPOutputStream(tmp);
				for (String s : tiles) {
					gout.write(s.getBytes("UTF-8"));
					gout.write("\r\n".getBytes("UTF-8"));
				}
				gout.finish();
				gout.flush();
				gout.close();
				OutputStream out = conn.getOutputStream();
				out.write(tmp.toByteArray());
				out.flush();
				out.close();
				int code = conn.getResponseCode();
				if (code != 200)
					throw new IOException("http error,response code:" + code);
				conn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
