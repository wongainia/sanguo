package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.BuildingBuyResp;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.ui.alert.CommonAlert;

public class BuildingBuyInvoker extends BaseInvoker {

	private int type;
	protected BuildingProp prop;
	protected BuildingBuyResp resp;

	public BuildingBuyInvoker(int type, BuildingProp prop) {
		this.type = type;
		this.prop = prop;
	}

	@Override
	protected String loadingMsg() {
		return "请稍后...";
	}

	@Override
	protected String failMsg() {
		return "失败";
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().buildingBuy(type, prop.getId());
		Account.manorInfoClient.update(resp.getMic());
	}

	@Override
	protected void onOK() {
		ctr.updateUI(resp.getRi(), true, true, true);
		if (prop.getLevel() == 1) {
			new CommonAlert("购买成功", "购买了" + prop.getBuildingName(), "效果:"
					+ prop.getDesc(), "点击任意位置关闭", true).show();
		} else {
			BuildingProp preProp = prop.getPreLevel();
			String desc = "";
			if (null != preProp) {
				if (preProp.getMainType() == 9) {
					desc += preProp.getBuildingName();
				} else {
					desc += preProp.getBuildingName() + " lv"
							+ preProp.getLevel();
				}
			}
			desc += "升级到了";
			if (prop.getMainType() == 9) {
				desc += prop.getBuildingName();
			} else {
				desc += prop.getBuildingName() + " lv" + prop.getLevel();
			}

			new CommonAlert("升级成功", desc, "效果:" + prop.getDesc(), "点击任意位置关闭",
					true).show();
		}
	}
}
