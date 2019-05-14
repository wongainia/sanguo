package com.vikings.sanguo.thread;

import android.location.Address;
import android.view.View;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.utils.ViewUtil;

public class AddrMutiLoader {

	public static final String TILE_TAG = "<tile>";

	private long[] tiles;

	private View v;

	private String text;

	private long time;
	
	/**
	 * 
	 * @param tiles
	 * @param v
	 * @param text
	 */
	public AddrMutiLoader(long[] tiles, View v, String text) {
		if (tiles == null)
			return;
		this.tiles = tiles;
		this.v = v;
		this.text = text;
		this.time = System.currentTimeMillis();
		this.v.setTag(time);
		if (setStub("载入.."))
			new AddrInvoker().start();
	}

	private void setText(StringBuilder buf, String text) {
		int i = buf.indexOf(TILE_TAG);
		if (i != -1) {
			buf.delete(i, i + TILE_TAG.length());
			buf.insert(i, text);
		}
	}

	private boolean setStub(String stub) {
		boolean needFetch = false;
		StringBuilder buf = new StringBuilder(text);
		for (int i = 0; i < tiles.length; i++) {
			Address a = CacheMgr.addrCache.getAddrInCache(tiles[i]);
			if (a == null) {
				setText(buf, stub);
				needFetch = true;
				break;
			} else {
				setText(buf, AddrLoader.toDesc(a, AddrLoader.TYPE_SUB));
			}
		}
		ViewUtil.setRichText(v, buf.toString());
		return needFetch;
	}

	private class AddrInvoker extends BaseInvoker {

		String[] addrs;

		public AddrInvoker() {
			addrs = new String[tiles.length];
		}

		@Override
		protected String failMsg() {
			return null;
		}

		@Override
		protected void onFail(GameException exception) {
		}

		@Override
		protected void beforeFire() {
		}

		@Override
		protected void afterFire() {
		}

		@Override
		protected void fire() throws GameException {
			for (int k = 0; k < tiles.length; k++) {
				Address addr = CacheMgr.addrCache.getAddr(tiles[k]);
				// // 领地做特殊处理 ,如果是空地址，或者不精确的地址，就重新取
				// if (AddrLoader.isNull(addr) || AddrLoader.isRough(addr)) {
				// int x = TileUtil.tileId2x(tiles[k]) - 10;
				// int y = TileUtil.tileId2y(tiles[k]) - 10;
				//
				// for (int i = x; i < x + 20; i = i + 5) {
				// for (int j = y; j < y + 20; j = j + 5) {
				// long t = TileUtil.index2TileId(i, j);
				// if (t == tiles[k])
				// continue;
				// Address tmp = CacheMgr.addrCache.getAddr(t);
				// if (!AddrLoader.isNull(tmp))
				// addr = tmp;
				// if (!AddrLoader.isRough(tmp))
				// break;
				// }
				// }
				// }
				addrs[k] = AddrLoader.toDesc(addr, AddrLoader.TYPE_SUB);
			}

		}

		@Override
		protected String loadingMsg() {
			return null;
		}

		@Override
		protected void onOK() {
			if ((Long) v.getTag() == time) {
				StringBuilder buf = new StringBuilder(text);
				for (int i = 0; i < tiles.length; i++) {
					setText(buf, addrs[i]);
				}
				ViewUtil.setRichText(v, buf.toString());
			}
		}

	}

}
