package com.vikings.sanguo.ui.map;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.cache.LRUCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.ui.alert.FamousFiefTip;
import com.vikings.sanguo.ui.alert.ManorFiefTip;
import com.vikings.sanguo.ui.alert.OtherGuildAltarTip;
import com.vikings.sanguo.ui.alert.OwnGuildAltarTip;
import com.vikings.sanguo.ui.alert.ResourceFiefTip;
import com.vikings.sanguo.ui.map.core.GeoPoint;
import com.vikings.sanguo.ui.map.core.MapView;
import com.vikings.sanguo.ui.map.core.Overlay;
import com.vikings.sanguo.ui.map.core.Projection;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;

/**
 * 绘制地图层 无缝衔接领地边缘 数据缓存
 * 
 * @author chenqing
 * 
 */
public class BattleMarker implements Overlay {

	private Rect screenRect = new Rect();

	private long fiefId = 0;

	private Point lt = new Point();

	private Point rb = new Point();

	private long selId = 0;

	public void sel(long id) {
		this.selId = id;
	}

	private LRUCache<Long, BriefFiefInfoClient> datas = new LRUCache<Long, BriefFiefInfoClient>(
			500);

	public void update(List<BriefFiefInfoClient> fiefs) {
		for (BriefFiefInfoClient f : fiefs) {
			update(f);
		}
	}

	public void update(BriefFiefInfoClient fief) {
		// 更新数据如果是主城， 并且没有manor 尝试在缓冲中找到之前的manor 设置进去
		if (fief.isCastle() && fief.getManor() == null) {
			BriefFiefInfoClient f = getCachedFief(fief.getId());
			if (f != null)
				fief.setManor(f.getManor());
		}
		datas.put(fief.getId(), fief);
	}

	public boolean contains(Long fiefId) {
		return datas.get(fiefId) != null;
	}

	public BriefFiefInfoClient getCachedFief(Long fiefId) {
		return datas.get(fiefId);
	}

	/**
	 * 根据屏幕rect 绘制所有在屏幕内的fief
	 */
	@Override
	public void draw(Canvas canvas, MapView mapView) {
		// 获取屏幕范围的fiefid 进行绘制
		Projection projection = mapView.getProjection();
		GeoPoint leftBtm = projection.fromPixels(0, Config.screenHeight);
		GeoPoint rightTop = projection.fromPixels(Config.screenWidth, 0);
		int x1 = TileUtil.latitude2Index(TileUtil.transServer(leftBtm
				.getLongitudeE6())) / Config.FIEF_SIZE;
		int x2 = TileUtil.latitude2Index(TileUtil.transServer(rightTop
				.getLongitudeE6())) / Config.FIEF_SIZE;
		int y1 = TileUtil.latitude2Index(TileUtil.transServer(leftBtm
				.getLatitudeE6())) / Config.FIEF_SIZE;
		int y2 = TileUtil.latitude2Index(TileUtil.transServer(rightTop
				.getLatitudeE6())) / Config.FIEF_SIZE;
		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				fiefId = TileUtil.index2TileId(i, j);
				BriefFiefInfoClient b = datas.get(fiefId);
				// if (b == null)
				// continue;
				// 计算绘制rect大小
				long leftTop = TileUtil.index2TileId(TileUtil.tileId2x(fiefId)
						* Config.FIEF_SIZE, TileUtil.tileId2y(fiefId)
						* Config.FIEF_SIZE + Config.FIEF_SIZE);
				long rightBottom = TileUtil.index2TileId(
						TileUtil.tileId2x(fiefId) * Config.FIEF_SIZE
								+ Config.FIEF_SIZE, TileUtil.tileId2y(fiefId)
								* Config.FIEF_SIZE);
				lt = projection.toPixels(
						new GeoPoint(TileUtil.getTileLat(leftTop), TileUtil
								.getTileLon(leftTop)), lt);
				rb = projection.toPixels(
						new GeoPoint(TileUtil.getTileLat(rightBottom), TileUtil
								.getTileLon(rightBottom)), rb);
				screenRect.set(lt.x, lt.y, rb.x, rb.y);
				FiefDrawable.draw(canvas, fiefId, b, screenRect, selId);
			}
		}

	}

	//
	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		long fiefId = TileUtil.geoPoint2FiefId(arg0);
		BriefFiefInfoClient bfic = datas.get(fiefId);
		if (bfic != null) {
			if (bfic.getId() == selId) {
				selId = 0;
			}
			int state = TroopUtil.getCurBattleState(bfic.getBattleState(),
					bfic.getBattleTime());
			if (state == BattleStatus.BATTLE_STATE_FINISH) {
				Config.getController().alert(
						"这里刚发生过战争，正在清理战场<br/><br/>请稍后待领地状态恢复后再进行操作");
			} else {
				if (bfic.isHoly()) {
					new FamousFiefTip().show(bfic);
				} else if (bfic.isCastle()) {
					new ManorFiefTip().show(bfic);
				} else if (bfic.isResource()) {
					new ResourceFiefTip().show(bfic);
				} else if (bfic.isOwnAltar()) {
					new OwnGuildAltarTip().show();
				} else if (bfic.isOtherAltar()) {
					new OtherGuildAltarTip(bfic).show();
				}

			}
		}
		return true;
	}

}
