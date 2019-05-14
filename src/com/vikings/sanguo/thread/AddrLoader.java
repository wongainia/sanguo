package com.vikings.sanguo.thread;

import android.location.Address;
import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class AddrLoader {

	/**
	 * 领地地址
	 */
	public static final byte ADDR_FIEF = 0;

	/**
	 * tile地址
	 */
	public static final byte ADDR_TILE = 1;

	/**
	 * 取全部地址
	 */
	public static final byte TYPE_ALL = 0;

	/**
	 * 取头地址 如 深圳市 南山区
	 */
	public static final byte TYPE_MAIN = 1;

	/**
	 * 取子地址 如 软件园一期
	 */
	public static final byte TYPE_SUB = 2;

	private View v;

	private long tileId;

	private String before = "";

	private String after = "";

	private byte type = TYPE_ALL;

	private byte addrType = ADDR_TILE;

	private long time;

	public AddrLoader(View v, Long tileId) {
		init(v, tileId);
	}

	public AddrLoader(View v, Long tileId, byte type) {
		this.type = type;
		this.addrType = ADDR_FIEF;
		init(v, tileId);
	}

	/**
	 * 
	 * @param v
	 * @param tileId
	 * @param before
	 *            地址前描述
	 * @param after
	 *            地址后描述
	 */
	public AddrLoader(View v, Long tileId, String before, String after) {
		this.before = before;
		this.after = after;
		init(v, tileId);
	}

	public AddrLoader(View v, Long tileId, String before, String after,
			byte type) {
		this.type = type;
		this.before = before;
		this.after = after;
		this.addrType = ADDR_FIEF;
		init(v, tileId);
	}

	private void init(View v, Long tileId) {
		this.v = v;
		this.tileId = tileId;
		this.time = System.currentTimeMillis();
		this.v.setTag(time);
		set();
	}

	private void set() {
		if (tileId == 0) {
			ViewUtil.setText(v,
					Config.getController().getString(R.string.AddrLoader_set));
			return;
		}
		Address addr = queryAddr(true);
		if (addr != null) {
			setAddr(addr, v);
			return;
		}
		ViewUtil.setText(v, Config.getController().getString(R.string.loading));
		new AddrInvoker().start();
	}

	private Address queryAddr(boolean inCache) {
		Address addr = inCache ? CacheMgr.addrCache.getAddrInCache(tileId)
				: CacheMgr.addrCache.getAddr(tileId);
		// 领地做特殊处理 ,如果是空地址，或者不精确的地址，就重新取
		if (addrType == ADDR_FIEF && (isNull(addr) || isRough(addr))) {
			int x = TileUtil.tileId2x(tileId) - Config.FIEF_SIZE / 2;
			int y = TileUtil.tileId2y(tileId) - Config.FIEF_SIZE / 2;
			for (int i = x; i < x + Config.FIEF_SIZE; i = i
					+ Config.FIEF_SIZE / 2) {
				for (int j = y; j < y + Config.FIEF_SIZE; j = j
						+ Config.FIEF_SIZE / 2) {
					long t = TileUtil.index2TileId(i, j);
					if (t == tileId)
						continue;
					Address tmp = inCache ? CacheMgr.addrCache
							.getAddrInCache(t) : CacheMgr.addrCache.getAddr(t);
					if (!isNull(tmp))
						addr = tmp;
					if (!isRough(tmp))
						break;
				}
			}
		}
		return addr;
	}

	private static String trim(String str, int length) {
		if (str.length() > length)
			return str.substring(0, length) + "..";
		else
			return str;
	}

	private void setAddr(Address addr, View v) {
		if ((Long) v.getTag() != time) {
			return;
		}
		String desc = toDesc(addr, type);
		setAddr(desc);
	}

	private void setAddr(String addr) {
		if (StringUtil.isNull(before) && StringUtil.isNull(after))
			ViewUtil.setText(v, addr);
		else
			ViewUtil.setRichText(v, before + addr + after);
	}

	public static String toDesc(Address addr) {
		return toDesc(addr, TYPE_ALL);
	}

	public static String toDesc(Address addr, byte type) {
		String desc;
		if (addr == null) {
			if (type == TYPE_MAIN)
				desc = "";
			else
				desc = Config.getController()
						.getString(R.string.AddrLoader_set);
			return desc;
		}
		switch (type) {
		case TYPE_ALL:
			desc = getAdminArea(addr) + getLocality(addr)
					+ addr.getFeatureName();
			if (StringUtil.isNull("desc"))
				desc = Config.getController()
						.getString(R.string.AddrLoader_set);
			break;
		case TYPE_MAIN:
			desc = getAdminArea(addr) + getLocality(addr);
			desc = trim(desc, 6);
			break;
		case TYPE_SUB:
			if (StringUtil.isNull(addr.getFeatureName()))
				desc = Config.getController()
						.getString(R.string.AddrLoader_set);
			else if (isRough(addr))
				desc = StringUtil.repParams(
						Config.getController().getString(
								R.string.AddrLoader_toDesc),
						addr.getFeatureName());
			else
				desc = StringUtil.trimAddr(addr.getFeatureName());
			desc = trim(desc, 7);
			break;
		default:
			desc = addr.toString();
			break;
		}
		return desc;
	}

	private static String getAdminArea(Address addr) {
		String s = StringUtil.noNull(addr.getAdminArea());
		if (s.endsWith("特别行政区"))
			s = s.replace("特别行政区", "");
		if (s.equals(getLocality(addr)))
			return "";
		return s;
	}

	public static String getLocality(Address addr) {
		String s = StringUtil.noNull(addr.getLocality());
		if (s.endsWith("特别行政区"))
			s = s.replace("特别行政区", "");
		return s;
	}

	public static boolean isNull(Address a) {
		if (a == null)
			return true;
		if (StringUtil.isNull(a.getAdminArea())
				&& StringUtil.isNull(a.getLocality()))
			return true;
		else
			return false;
	}

	public static boolean isRough(Address a) {
		if (a == null)
			return true;
		if (StringUtil.noNull(a.getSubLocality()).equals(
				StringUtil.noNull(a.getFeatureName())))
			return true;
		else
			return false;
	}

	private class AddrInvoker extends BaseInvoker {

		Address addr = null;

		@Override
		protected String failMsg() {
			return null;
		}

		@Override
		protected void onFail(GameException exception) {
		}

		@Override
		protected void afterFire() {
		}

		@Override
		protected void beforeFire() {
		}
		
		@Override
		protected void fire() throws GameException {
			addr = queryAddr(false);
		}

		@Override
		protected String loadingMsg() {
			return null;
		}

		@Override
		protected void onOK() {
			setAddr(addr, v);
		}

	}

}
