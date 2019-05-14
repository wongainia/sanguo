package com.vikings.sanguo.cache;

import java.util.ArrayList;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;

/**
 * 文件缓存 用于保存用资源服获取的资源，在客户端保存在本地
 * 
 * @author Brad.Chen
 * 
 */
public class ZoneCache extends LazyLoadCache {

	protected ArrayList<Zone> list = new ArrayList<Zone>();

	@Override
	protected void addContent(Object obj) {
		list.add((Zone) obj);
	}

	@Override
	public String getName() {
		return "prop_zone_client.csv";
	}

	@Override
	public Object getKey(Object obj) {
		return null;
	}

	@Override
	public Object fromString(String line) {
		Zone z = new Zone();
		StringBuilder buf = new StringBuilder(line);
		z.left = StringUtil.removeCsvInt(buf) / Config.FIEF_SIZE
				/ TileUtil.TILE_SPACE;
		z.right = StringUtil.removeCsvInt(buf) / Config.FIEF_SIZE
				/ TileUtil.TILE_SPACE;
		z.bottom = StringUtil.removeCsvInt(buf) / Config.FIEF_SIZE
				/ TileUtil.TILE_SPACE;
		z.top = StringUtil.removeCsvInt(buf) / Config.FIEF_SIZE
				/ TileUtil.TILE_SPACE;
		z.provice = StringUtil.removeCsvInt(buf);
		return z;
	}

	public int getProvince(long fiefId) {
		if (!isInit())
			return 0;
		int x = TileUtil.tileId2x(fiefId);
		int y = TileUtil.tileId2y(fiefId);
		for (Zone z : list) {
			if (z.left <= x && z.right > x && z.bottom <= y && z.top > y)
				return z.provice;
		}
		return 0;
	}

	private class Zone implements Comparable<Zone> {

		int left;
		int right;
		int bottom;
		int top;
		int provice;

		@Override
		public int compareTo(Zone another) {
			if (bottom == another.bottom)
				return left - another.left;
			else
				return bottom - another.bottom;
		}
	}

}
