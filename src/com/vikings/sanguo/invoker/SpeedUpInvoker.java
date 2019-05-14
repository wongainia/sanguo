package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;

public class SpeedUpInvoker extends BaseInvoker {

	private BuildingInfoClient bic;
	private BriefFiefInfoClient bfic;

	private ReturnInfoClient ric;

	public SpeedUpInvoker(BuildingInfoClient bic, BriefFiefInfoClient bfic) {
		this.bic = bic;
		this.bfic = bfic;
	}

	@Override
	protected String loadingMsg() {
		return "加速";
	}

	@Override
	protected String failMsg() {
		return "加速失败";
	}

	@Override
	protected void fire() throws GameException {
		ric = GameBiz.getInstance().buildingReset(bic, bfic);
	}

	@Override
	protected void onOK() {
		ctr.updateUI(ric, true);
	}

}
