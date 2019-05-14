package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.ui.alert.CommonAlert;

public class BuildingFiefBuyInvoker extends BaseInvoker {

	private int type;
	protected BuildingProp prop;
	protected long fiefId;

	private ReturnInfoClient ri;

	public BuildingFiefBuyInvoker(int type, BuildingProp prop, long fiefId) {
		super();
		this.type = type;
		this.prop = prop;
		this.fiefId = fiefId;
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
		ri = GameBiz.getInstance().buildingFiefBuy(type, prop.getId(), fiefId);
	}

	@Override
	protected void onOK() {
		ctr.updateUI(ri, true, true, true);
		if (prop.getLevel() == 1) {
			new CommonAlert("购买成功", "购买了" + prop.getBuildingName(), "效果:"
					+ prop.getDesc(), "点击任意位置关闭", true).show();
		} else {
			BuildingProp preProp = prop.getPreLevel();
			String desc = "";
			if (null != preProp) {
				desc += preProp.getBuildingName();
			}
			desc += "升级到了" + prop.getBuildingName();
			new CommonAlert("升级成功", desc, "效果:" + prop.getDesc(), "点击任意位置关闭",
					true).show();
		}
	}

}
