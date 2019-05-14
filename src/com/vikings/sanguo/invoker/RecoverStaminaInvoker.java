package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.HeroStaminaRecoveryResp;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;

public class RecoverStaminaInvoker extends BaseInvoker {
	private HeroInfoClient heroInfoClient;
	private int needRmb;
	private CallBack callBack;
	private HeroStaminaRecoveryResp resp;

	public RecoverStaminaInvoker(HeroInfoClient heroInfoClient, int needRmb,
			CallBack callBack) {
		this.heroInfoClient = heroInfoClient;
		this.needRmb = needRmb;
		this.callBack = callBack;
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.RecoverStaminaInvoker_loadingMsg);
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.RecoverStaminaInvoker_failMsg);
	}

	@Override
	protected void fire() throws GameException {
		try {
			resp = GameBiz.getInstance().heroStaminaRecovery(
					heroInfoClient.getId(), needRmb);
		} catch (GameException e) {
			if (e.getResult() == 1082) {
				heroInfoClient.setStamina(CacheMgr.heroCommonConfigCache
						.getMaxStamina());
			} else
				throw e;
		}

	}

	@Override
	protected void onFail(GameException exception) {
		if (null != callBack)
			callBack.onCall();
		super.onFail(exception);
	}

	@Override
	protected void onOK() {
		// 恢复成功
		if (null != resp) {
			if (resp.getInfo() != null) {
				heroInfoClient.update(resp.getInfo());
				// Account.heroInfoCache.update(resp.getInfo());
			}
			if (null != callBack)
				callBack.onCall();
			Config.getController().alert(
					StringUtil.repParams(
							ctr.getString(R.string.RecoverStaminaInvoker_onOK),
							"#rmb#" + resp.getRi().getCurrency()));
		} else
			Config.getController().alert("将领体力已满,无需恢复体力");
	}
}
