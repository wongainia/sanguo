package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.HeroRefreshResp;
import com.vikings.sanguo.sound.SoundMgr;

public abstract class HeroRefreshInvoker extends BaseInvoker {
	public static final int TYPE_SINGLE = 1; // 盲选
	public static final int TYPE_GROUP = 2; // 商店

	protected int type;
	protected HeroRefreshResp resp;

	protected abstract void refreshUI();

	public HeroRefreshInvoker(int type) {
		this.type = type;
	}

	@Override
	protected String loadingMsg() {
		return "招募将领";
	}

	@Override
	protected String failMsg() {
		return "招募将领失败";
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().heroRefresh(type);
	}

	@Override
	protected void onOK() {
		SoundMgr.play(R.raw.sfx_recruit);
		ctr.updateUI(resp.getRi(), true);
		refreshUI();
	}

}
