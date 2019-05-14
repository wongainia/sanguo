package com.vikings.sanguo.ui.listener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.message.ManorReviveTip;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorLocation;
import com.vikings.sanguo.ui.CastleUI;
import com.vikings.sanguo.ui.alert.BuildingStatTip;
import com.vikings.sanguo.ui.alert.BuildingTip;
import com.vikings.sanguo.ui.window.ArmEnhanceListWindow;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 当前用户主城建筑的点击事件
 */
public class BuildingSelfOnClick implements CastleUI.BuildingClickListener {

	@Override
	public void click(BuildingInfoClient b) {
		// 只要是战役第4关 没有胜利 进入红楼提示 先打战役
//		if (BuildingProp.BUILDING_TYPE_BAR == b.getProp().getType()
//				&& StepMgr.checkStep201Bar()) {
//			return;
//		}

		// 建筑没有解散时候 提示解锁
		ManorLocation ml = CacheMgr.manorLocationCache.getManorLocationByType(b
				.getProp().getType());
		if (b.isLocked(ml)) {
			StringBuilder sBuilder = new StringBuilder();
			if (ml.getUnlockLevel() > 0) {
				sBuilder.append("角色达到" + ml.getUnlockLevel() + "级，");
			}
			if (ml.getVip() > 0) {
				sBuilder.append("VIP达到" + ml.getVip() + "级，");
			}
			sBuilder.append("可解锁该建筑");
			Config.getController().alert(
					StringUtil.color(sBuilder.toString(), R.color.color13));
			return;
		}

		switch (b.getProp().getType()) {
		case BuildingProp.BUILDING_TYPE_MAIN:
			Config.getController().openBuildingTypeListWindow();
			break;
		case BuildingProp.BUILDING_TYPE_ARM_ENROLL:
			Config.getController().openArmTrainingListWindow();// 士兵招募所
			break;
		case BuildingProp.BUILDING_TYPE_BAR:
//			if (StepMgr.checkStep201Bar()) {
//				return;
//			}
			Config.getController().openBarWindow();// 英雄招募酒吧
			break;
		case BuildingProp.BUILDING_TYPE_HERO_CENTER:
			Config.getController().openHeroCenterWindow();// 英雄殿--->对英雄的强化等操作
			break;
		case BuildingProp.BUILDING_TYPE_SMITHY:
			Config.getController().openSmithyWindow();// 铁匠铺
			break;
		case BuildingProp.BUILDING_TYPE_ARM_ENHANCE:// 校场
			new ArmEnhanceListWindow().open();// 士兵部队的强化
			break;
		case BuildingProp.BUILDING_TYPE_REVIVE:
			// 医馆建筑属VIP玩家特权建筑，同时满足玩家等级跟vip等级解锁
			new ManorReviveTip(b, true).show();// 医馆
			break;
		default:
			// 普通建筑类型
			if (b.isResourceBuilding())
				new BuildingStatTip(b, b.getResourceStatus(), true).show();// 资源产出建筑收获
			else
				new BuildingTip(b, true).show();// 普通
			break;
		}
	}
}
