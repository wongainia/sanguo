package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HeroArmPropInfo;
import com.vikings.sanguo.utils.StringUtil;

//将领统兵属性信息
public class HeroArmPropInfoClient extends OtherHeroArmPropInfoClient {
	private int curValueHide;// 隐藏数值
	private int maxValueHide;// 隐藏数值上限

	public HeroArmPropInfoClient(int type) throws GameException {
		super(type);
	}

	public int getCurValueHide() {
		return curValueHide;
	}

	public void setCurValueHide(int curValueHide) {
		this.curValueHide = curValueHide;
	}

	public int getMaxValueHide() {
		return maxValueHide;
	}

	public void setMaxValueHide(int maxValueHide) {
		this.maxValueHide = maxValueHide;
	}

	public static HeroArmPropInfoClient convert(HeroArmPropInfo info)
			throws GameException {
		if (null == info)
			return null;
		HeroArmPropInfoClient hapic = new HeroArmPropInfoClient(info.getType());
		hapic.setValue(info.getCurValue());
		hapic.setMaxValue(info.getMaxValue());
		hapic.setCurValueHide(info.getCurValueHide());
		hapic.setMaxValueHide(info.getMaxValueHide());
		return hapic;
	}

	public static List<HeroArmPropInfoClient> convert2List(
			List<HeroArmPropInfo> infos) throws GameException {
		List<HeroArmPropInfoClient> list = new ArrayList<HeroArmPropInfoClient>();
		if (null == infos || infos.isEmpty())
			return list;
		for (HeroArmPropInfo info : infos) {
			list.add(HeroArmPropInfoClient.convert(info));
		}
		return list;
	}

	public HeroArmPropInfoClient copy() {
		try {
			HeroArmPropInfoClient newHapic = new HeroArmPropInfoClient(type);
			newHapic.setValue(value);
			newHapic.setMaxValue(maxValue);
			newHapic.setCurValueHide(curValueHide);
			newHapic.setMaxValueHide(maxValueHide);
			newHapic.setHeroTroopName(heroTroopName);
			return newHapic;
		} catch (GameException e) {
			Log.e("HeroArmPropInfoClient", e.getMessage());
		}
		return null;
	}

	public String getHeroEvolveArmPropDesc(HeroIdBaseInfoClient hic) {
		StringBuilder buf = new StringBuilder();
		buf.append(getValue());
		HeroEvolve heroEvolve = null;
		try {
			heroEvolve = (HeroEvolve) CacheMgr.heroEvolveCache.get(hic
					.getHeroId());
		} catch (GameException e) {
			Log.e("HeroArmPropInfoClient", e.getMessage());
		}
		if (null != heroEvolve)
			buf.append(StringUtil.color(
					" (+"
							+ (int) (heroEvolve.getCurAdd() / 100f
									* hic.getHeroType().getPropRate() / 100f * heroEvolve
										.getTroopArmRate(getType())) + ")",
					R.color.color20));

		buf.append("/").append(getMaxValue());

		if (null != heroEvolve)
			buf.append(StringUtil.color(
					" (+"
							+ (int) (heroEvolve.getShowMaxAdd() / 100f * hic
									.getHeroType().getPropRate()) + ")",
					R.color.color20));
		return buf.toString();
	}
}
