package com.vikings.sanguo.invoker;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.HeroAbandonResp;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;

public class HeroAbandonInvoker extends BaseInvoker {
	protected List<HeroInfoClient> hics;
	protected HeroAbandonResp resp;
	private CallBack callBack;

	public HeroAbandonInvoker(List<HeroInfoClient> hics, CallBack callBack) {
		this.hics = hics;
		this.callBack = callBack;
	}

	@Override
	protected String loadingMsg() {
		return "将领分解…";
	}

	@Override
	protected String failMsg() {
		return "将领分解失败";
	}

	@Override
	protected void fire() throws GameException {
		List<Long> ids = new ArrayList<Long>();
		if (null != hics && !hics.isEmpty())
			for (HeroInfoClient hic : hics) {
				if (hic.getId() > 0)
					ids.add(hic.getId());
			}
		resp = GameBiz.getInstance().heroAbandon(ids);
		Account.heroInfoCache.delete(ids);
		for (HeroInfoClient hic : hics) {
			if (ids.contains(hic.getId()))
				hic.setId(0);
		}
	}

	@Override
	protected void onOK() {
		SoundMgr.play(R.raw.sfx_break);
		if (null != callBack)
			callBack.onCall();
		ReturnInfoClient ri = resp.getRi();
		if (ri.isReturnNothing()) {
			ctr.alert("分解将领", "分解将领成功，可惜你什么也没得到。再接再厉，下次会好运", null, false);
		} else {
			ri.setMsg("将领分解成功");
		}
		ctr.updateUI(ri, true);
		// 返回到将领列表
	}
}
