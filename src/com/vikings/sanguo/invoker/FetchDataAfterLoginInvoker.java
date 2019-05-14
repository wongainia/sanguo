package com.vikings.sanguo.invoker;

import android.util.Log;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.ui.window.CastleWindow;

public class FetchDataAfterLoginInvoker extends BaseInvoker {
	@Override
	protected void beforeFire() {
	}

	@Override
	protected void afterFire() {
	}

	@Override
	protected void onFail(GameException exception) {
		Log.e("FetchDataAfterLoginInvoker", exception.getMessage());
	}

	@Override
	protected String loadingMsg() {
		return "";
	}

	@Override
	protected String failMsg() {
		return "";
	}

	@Override
	protected void fire() throws GameException {
		if (Account.user.hasGuild())
			Account.guildCache.updata(true);
		// 取荣耀榜 判断是否有奖励
		Account.obtainHonorReward();

	}

	@Override
	protected void onOK() {
		CastleWindow castleWindow = ctr.getCastleWindow();
		if (Account.user.hasGuild() && null != castleWindow) {
			castleWindow.setValue();
		}
	}

}
