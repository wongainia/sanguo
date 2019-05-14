package com.vikings.sanguo.ui.map;

import java.util.List;

import android.graphics.Point;
import android.location.Location;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.location.LocationMgr;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.FiefProp;
import com.vikings.sanguo.model.NpcClientProp;
import com.vikings.sanguo.model.RichFiefInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ImageLoader;
import com.vikings.sanguo.ui.map.core.GeoPoint;
import com.vikings.sanguo.ui.map.core.MapView;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;

public class BattleMap {

	private BattleMarker marker = new BattleMarker();

	private MapView mapView;

	private LocationMgr lctMgr;

	private Location curLocation;

	private boolean isGuilde;// 引导标识位
	private long guildeNpcFiefID;// 引导用的npc领地
	private CallBack step504CallBack;

	public void setStep504CallBack(CallBack step504CallBack) {
		this.step504CallBack = step504CallBack;
	}

	public long getGuildeNpcFiefID() {
		return guildeNpcFiefID;
	}

	public BattleMap() {
		mapView = (MapView) Config.getController().findViewById(R.id.mapView);
		mapView.addOverlay(marker);
		lctMgr = new LocationMgr();
		if (Setting.isMapEnable()) {
			mapView.setShowMap(true);
		} else {
			mapView.setShowMap(false);
		}
		mapView.postDelayed(moveGuide, 5000);
	}

	public void startLocaltionListener() {
		lctMgr.startListen();
	}

	public void stopLocaltionListener() {
		lctMgr.stopListen();
	}

	public void setCurLocation(Location curLocation) {
		this.curLocation = curLocation;
	}

	public Location getCurLocation() {
		return curLocation;
	}

	public MapView getMapView() {
		return mapView;
	}

	public void refreshMap() {
		mapView.invalidate();
	}

	private int countCacheFief(List<Long> fiefIds) {
		int count = 0;
		for (Long id : fiefIds) {
			if (marker.contains(id))
				count++;
		}
		return count;
	}

	public void moveToFief(long fiefId, boolean sel, boolean forceRefresh) {
		if (sel)
			marker.sel(fiefId);
		GeoPoint p = TileUtil.getFiefCenterGeo(fiefId);
		// 判断是否要去取数据
		Point pixP = mapView.getProjection().toPixels(p, null);
		GeoPoint leftBot = mapView.getProjection().fromPixels(
				pixP.x - ((Config.screenWidth) >> 1),
				pixP.y + ((Config.screenHeight) >> 1));
		GeoPoint rightTop = mapView.getProjection().fromPixels(
				pixP.x + ((Config.screenWidth) >> 1),
				pixP.y - ((Config.screenHeight) >> 1));
		List<Long> fiefIds = TileUtil.regionFiefs(leftBot.getLongitudeE6(),
				leftBot.getLatitudeE6(), rightTop.getLongitudeE6(),
				rightTop.getLatitudeE6());
		if (forceRefresh || !marker.contains(fiefId)
				|| countCacheFief(fiefIds) < fiefIds.size() / 2) {
			new RefreshFiefInvoker(leftBot, rightTop, false).start();
		}
		// mapView.setMapCenter(p);
		mapView.animateTo(p);
	}

	public void moveToFief(long fiefId, boolean sel) {
		moveToFief(fiefId, sel, false);
	}

	// 引导用
	public void moveToFief(boolean isGuilde) {
		if (!isGuilde)
			return;
		this.isGuilde = isGuilde;
		GeoPoint p = TileUtil
				.getFiefCenterGeo(Account.manorInfoClient.getPos());
		// 判断是否要去取数据
		Point pixP = mapView.getProjection().toPixels(p, null);
		GeoPoint leftBot = mapView.getProjection().fromPixels(
				pixP.x - ((Config.screenWidth) >> 1),
				pixP.y + ((Config.screenHeight) >> 1));
		GeoPoint rightTop = mapView.getProjection().fromPixels(
				pixP.x + ((Config.screenWidth) >> 1),
				pixP.y - ((Config.screenHeight) >> 1));
		new RefreshFiefInvoker(leftBot, rightTop, true).start();
		mapView.animateTo(p);

	}

	// 刷新当前屏幕的领地
	public void refreshCurFief() {
		SoundMgr.play(R.raw.sfx_refresh);
		GeoPoint leftBot = mapView.getProjection().fromPixels(0,
				Config.screenHeight);
		GeoPoint rightTop = mapView.getProjection().fromPixels(
				Config.screenWidth, 0);
		new RefreshFiefInvoker(leftBot, rightTop, true).start();
	}

	// 刷新当前屏幕的领地
	public void refreshCurFief(boolean sync) {
		SoundMgr.play(R.raw.sfx_refresh);
		GeoPoint leftBot = mapView.getProjection().fromPixels(0,
				Config.screenHeight);
		GeoPoint rightTop = mapView.getProjection().fromPixels(
				Config.screenWidth, 0);
		new RefreshFiefInvoker(leftBot, rightTop, sync).start();
	}

	// 世界引导 选择资源点规则
	private void guideChooseResRule(List<BriefFiefInfoClient> fiefList) {
		// 引导 找野地 当前屏幕下的野地
		if (isGuilde) {
			isGuilde = false;
			long[] fiefID = { 0, 0, 0, 0, 0 };
			for (BriefFiefInfoClient bf : fiefList) {
				if (CacheMgr.npcCache.containKey(bf.getUserId())
						&& !bf.isHoly() && bf.getProp().getLevel() <= 3) {
					switch (bf.getProp().getResType()) {
					case FiefProp.TYPE_GOLD:
					case FiefProp.TYPE_FARMLAND:
					case FiefProp.TYPE_FORESTRY:
					case FiefProp.TYPE_IRON:
					case FiefProp.TYPE_PARK:
						fiefID[bf.getProp().getResType() - 2] = bf.getId();
						break;
					default:
						break;
					}
				}
			}

			for (int i = 0; i < fiefID.length; i++) {
				if (fiefID[i] != 0) {
					guildeNpcFiefID = fiefID[i];
					return;
				}
			}
		}
	}

	private class RefreshFiefInvoker extends BaseInvoker {
		private GeoPoint leftBot;
		private GeoPoint rightTop;

		private boolean sync = false;

		@Override
		protected void beforeFire() {
			if (sync)
				super.beforeFire();
		}

		@Override
		protected void afterFire() {
			if (sync)
				super.afterFire();
		}

		public RefreshFiefInvoker(GeoPoint leftBot, GeoPoint rightTop,
				boolean sync) {
			this.leftBot = leftBot;
			this.rightTop = rightTop;
			this.sync = sync;
		}

		@Override
		protected String failMsg() {
			return "获取领地信息失败";
		}

		@Override
		protected void fire() throws GameException {
			List<BriefFiefInfoClient> ls = getFiefList(leftBot, rightTop);
			getFiefIcon(ls);
			Account.updateFavorateFief();
			update(ls);
		}

		@Override
		protected String loadingMsg() {
			return "刷新领地信息";
		}

		@Override
		protected void onOK() {
			// mapView.invalidate();

			if (step504CallBack != null) {
				step504CallBack.onCall();
				step504CallBack = null;
			}
		}
	}

	public List<BriefFiefInfoClient> getFiefList(GeoPoint leftBot,
			GeoPoint rightTop) throws GameException {
		// 获取当前屏幕内所有领地
		List<Long> fiefIds = TileUtil.regionFiefs(leftBot.getLongitudeE6(),
				leftBot.getLatitudeE6(), rightTop.getLongitudeE6(),
				rightTop.getLatitudeE6());
		List<BriefFiefInfoClient> rs = GameBiz.getInstance()
				.briefFiefInfoQuery(fiefIds);
		// 引导的方法
		guideChooseResRule(rs);
		// 加入自己的领地
		for (RichFiefInfoClient ri : Account.richFiefCache.getAll()) {
			BriefFiefInfoClient bi = ri.brief();
			int index = rs.indexOf(bi);
			if (index == -1) {
				rs.add(ri.brief());
			} else
				rs.set(index, ri.brief());
		}
		// 查过的领地 如果无返回，用空地代替
		for (Long fiefId : fiefIds) {
			boolean exist = false;
			for (BriefFiefInfoClient f : rs) {
				if (f.getId() == fiefId.longValue()) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				BriefFiefInfoClient info = new BriefFiefInfoClient();
				info.setId(fiefId);
				info.setProp((FiefProp) CacheMgr.fiefPropCache
						.get(FiefProp.TYPE_WILDERNESS));
				rs.add(info);
			}
		}
		return rs;
	}

	//
	private void getFiefIcon(List<BriefFiefInfoClient> fiefList) { // throws
																	// GameException
																	// {
		for (BriefFiefInfoClient bf : fiefList) {
			// 检查用到的领地图片
			try {
				if (!StringUtil.isNull(bf.getIcon()))
					ImageLoader.getInstance().downloadInCase(bf.getIcon(),
							Config.imgUrl);
			} catch (GameException e) {
				e.printStackTrace();
			}

			if (CacheMgr.npcCache.containKey(bf.getUserId()) && bf.isHoly()) {
				try {
					NpcClientProp ncp = (NpcClientProp) CacheMgr.npcCache
							.get(bf.getUserId());
					if (!StringUtil.isNull(ncp.getMapImg()))
						ImageLoader.getInstance().downloadInCase(
								ncp.getMapImg(), Config.imgUrl);
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateFief(BriefFiefInfoClient bfic) {
		marker.update(bfic);
	}

	public void update(List<BriefFiefInfoClient> ls) {
		marker.update(ls);
		mapView.postInvalidate();
	}

	public void deleteFief(long id) {
		BriefFiefInfoClient b = marker.getCachedFief(id);
		if (b == null)
			return;
		b.setUserId(0);
		try {
			b.setLord(CacheMgr.npcCache.getNpcUser(0));
		} catch (GameException e) {
			e.printStackTrace();
		}
		updateFief(b);
	}

	public void updateFief(FiefInfoClient f) throws GameException {
		f.updateCacheFief(getCachedFief(f.getId()));
	}

	public BriefFiefInfoClient getCachedFief(long id) {
		return marker.getCachedFief(id);
	}

	private Runnable moveGuide = new Runnable() {

		@Override
		public void run() {
			if (null == Account.user)
				return;
			if (Account.user.getLevel() > 15)
				return;
			if (Account.readLog.MAP_GUIDE > 3)
				return;
			if (Config.getController().getCurPopupUI() == Config
					.getController().getFiefMap()) {
				GeoPoint p = mapView.getMapCenter();
				// 判断是否要去取数据
				Point pixP = mapView.getProjection().toPixels(p, null);
				GeoPoint leftBot = mapView.getProjection().fromPixels(
						pixP.x - ((Config.screenWidth) >> 1),
						pixP.y + ((Config.screenHeight) >> 1));
				GeoPoint rightTop = mapView.getProjection().fromPixels(
						pixP.x + ((Config.screenWidth) >> 1),
						pixP.y - ((Config.screenHeight) >> 1));
				List<Long> fiefIds = TileUtil.regionFiefs(
						leftBot.getLongitudeE6(), leftBot.getLatitudeE6(),
						rightTop.getLongitudeE6(), rightTop.getLatitudeE6());
				if (countCacheFief(fiefIds) < fiefIds.size() / 2
						&& Config.getController().getCurPopupUI() == Config
								.getController().getFiefMap()) {
					Account.readLog.MAP_GUIDE++;
				}
			}
			mapView.postDelayed(moveGuide, 10000);
		}
	};

}
