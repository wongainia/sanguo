package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.FiefFightNpcResp;
import com.vikings.sanguo.model.BriefFiefInfoClient;

public class OpenEvilDoorInvoker extends BaseInvoker {

	private FiefFightNpcResp resp;
	private BriefFiefInfoClient bfic;
	private int itemCount;

	public OpenEvilDoorInvoker(BriefFiefInfoClient bfic, int itemCount) {
		this.bfic = bfic;
		this.itemCount = itemCount;
	}

	@Override
	protected String loadingMsg() {
		return "请稍候";
	}

	@Override
	protected String failMsg() {
		return "获取数据失败...";
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().fiefFightNpc(bfic.getId(), itemCount);
		Account.briefBattleInfoCache.syncBattleIdFromSer();
//		Account.heroInfoCache.update(resp.getHeroInfoClient());
	}

	@Override
	protected void onOK() {
		ctr.updateUI(resp.getRi(), true);
		Config.getController().goBack();
		ctr.openWarInfoWindow(bfic);
	}
}
