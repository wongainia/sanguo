package com.vikings.sanguo.invoker;

import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.FiefMoveTroopResp;
import com.vikings.sanguo.message.ManorMoveTroopResp;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.ui.alert.FiefSucTip;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;

public class TroopMoveInvoker extends BaseInvoker {

	private ReturnInfoClient ri;

	private int type;
	private BriefFiefInfoClient myBfc;
	private BriefFiefInfoClient lordBfc;
	private List<ArmInfoClient> armInfoList;
	private List<HeroIdInfoClient> heros;

	public TroopMoveInvoker(int type, BriefFiefInfoClient myBfc,
			BriefFiefInfoClient lordBfc, List<ArmInfoClient> armInfoList,
			List<HeroIdInfoClient> heros) {
		this.type = type;
		this.myBfc = myBfc;
		this.lordBfc = lordBfc;
		this.armInfoList = armInfoList;
		this.heros = heros;
	}

	@Override
	protected String loadingMsg() {
		return "正在调动军队";
	}

	@Override
	protected String failMsg() {
		return "调动军队失败";
	}

	@Override
	protected void fire() throws GameException {
		// 主城往外调兵
		if (myBfc.getId() == Account.manorInfoClient.getPos()) {
			ManorMoveTroopResp rsp = GameBiz.getInstance().manorMoveTroop(
					lordBfc.getId(), type, armInfoList, heros);
			ri = rsp.getRi();
		} else {
			FiefMoveTroopResp rsp = GameBiz.getInstance().fiefMoveTroop(
					myBfc.getId(), type, armInfoList, heros);
			ri = rsp.getRi();
		}
	}

	@Override
	protected void onOK() {
		new FiefSucTip().show(Constants.TROOP_DISPATCH, ri.getItemPack(),
				TroopUtil.countArm(armInfoList), ri.getFood(), 0,
				ri.getCurrency(), 0, TileUtil.fiefId2TileId(lordBfc.getId()));
		Config.getController().updateUI(ri, true);
		Config.getController().goBack();
	}
}
