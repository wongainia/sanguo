package com.vikings.sanguo.invoker;

import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.ui.alert.FiefSucTip;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;

public class TroopReinforceInvoker extends BaseInvoker {

	private ReturnInfoClient ri;

	private int type;
	private BriefFiefInfoClient myBfc;
	private BriefFiefInfoClient lordBfc;
	private List<ArmInfoClient> armInfoList;
	private List<HeroIdInfoClient> heros;
	private int holyCost;

	public TroopReinforceInvoker(int type, BriefFiefInfoClient myBfc,
			BriefFiefInfoClient lordBfc, List<ArmInfoClient> armInfoList,
			List<HeroIdInfoClient> heros, int holyCost) {
		this.type = type;
		this.myBfc = myBfc;
		this.lordBfc = lordBfc;
		this.armInfoList = armInfoList;
		this.heros = heros;
		this.holyCost = holyCost;
	}

	@Override
	protected String loadingMsg() {
		return "正在出征";
	}

	@Override
	protected String failMsg() {
		return "出征失败";
	}

	@Override
	protected void fire() throws GameException {
		ri = GameBiz
				.getInstance()
				.battleReinfor(lordBfc.getReinforceTargetId(), lordBfc.getId(),
						holyCost, type, lordBfc.getReinforceType(),
						armInfoList, heros).getRi();
	}

	@Override
	protected void onOK() {
		int stamina = 0;
		// if (hero != null && hero.getId() > 0)
		// stamina = CacheMgr.heroCommonConfigCache.getCostStamina();
		new FiefSucTip().show(Constants.TROOP_REINFORCE, ri.getItemPack(),
				TroopUtil.countArm(armInfoList), ri.getFood(), 0,
				ri.getCurrency(), stamina,
				TileUtil.fiefId2TileId(lordBfc.getId()));

		ctr.updateUI(ri, true);
		Config.getController().goBack();
		ctr.openWarInfoWindow(lordBfc); // , null
	}
}
