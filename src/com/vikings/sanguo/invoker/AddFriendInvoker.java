package com.vikings.sanguo.invoker;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;

public class AddFriendInvoker extends BaseInvoker {

	private BriefUserInfoClient user;
	private CallBack callBack;

	public AddFriendInvoker(BriefUserInfoClient user, CallBack callBack) {
		this.user = user;
		this.callBack = callBack;
	}

	@Override
	protected String failMsg() {
		return "";
	}

	@Override
	protected void fire() throws GameException {
		if (Account.friends.contains(user.getId()))
			throw new GameException(CacheMgr.errorCodeCache.getMsg((short) 47));

		if (user.getId().intValue() == Constants.GM_USER_ID)
			throw new GameException(
					CacheMgr.errorCodeCache.getMsg((short) 1510));

		if (Account.user.getCountry().intValue() != user.getCountry()
				.intValue())
			throw new GameException(
					CacheMgr.errorCodeCache.getMsg((short) 1093));
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
		Account.updateFriendRank(user);
		if (Account.isBlackList(user)) {
			Account.deleteBlackList(user);
		}
		Config.getController().alert(
				"添加成功",
				StringUtil.repParams(ctr.getString(R.string.AddFriend_onOK),
						StringUtil.color(user.getHtmlNickName(),
								R.color.k7_color5)), null, true);
		if (null != callBack)
			callBack.onCall();
	}
}