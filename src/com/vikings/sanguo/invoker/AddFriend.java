package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.utils.StringUtil;

public class AddFriend extends BaseInvoker {

	BriefUserInfoClient user;

	public AddFriend(BriefUserInfoClient user) {
		this.user = user;
	}

	@Override
	protected String failMsg() {
		return StringUtil.repParams(ctr.getString(R.string.AddFriend_failMsg),
				user.getNickName());
	}

	@Override
	protected void fire() throws GameException {
		GameBiz.getInstance().addFriend(user.getId());
	}

	@Override
	protected String loadingMsg() {
		return StringUtil.repParams(
				ctr.getString(R.string.AddFriend_loadingMsg),
				user.getNickName());
	}

	@Override
	protected void onOK() {
		Account.addFriend(user);
		if (Account.isBlackList(user)) {
			Account.deleteBlackList(user);
		}
		Config.getController().alert(
				StringUtil.repParams(ctr.getString(R.string.AddFriend_onOK),
						user.getNickName()));
	}
}