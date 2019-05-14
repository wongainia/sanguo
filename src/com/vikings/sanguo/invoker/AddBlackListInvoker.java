package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.utils.StringUtil;

public class AddBlackListInvoker extends BaseInvoker {
	private BriefUserInfoClient briefUser;

	public AddBlackListInvoker(BriefUserInfoClient briefUser) {
		this.briefUser = briefUser;
	}

	@Override
	protected String failMsg() {
		return "拉黑" + briefUser.getNickName() + "失败";
	}

	@Override
	protected void fire() throws GameException {
		GameBiz.getInstance().blackListAdd(briefUser.getId().intValue());
	}

	@Override
	protected String loadingMsg() {
		return "拉黑" + briefUser.getNickName();
	}

	@Override
	protected void onOK() {
		Account.addBlackList(briefUser);
		ctr.alert(
				"操作成功",
				"你已将 "
						+ StringUtil.color(briefUser.getHtmlNickName(), R.color.k7_color5)
						+ " 拉入仇人录", null, true);
	}
}
