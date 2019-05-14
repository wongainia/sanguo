package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.GuildJoinApproveResp;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.utils.StringUtil;

public class GuildJoinApproveInvoker extends BaseInvoker {
	public static final int ACCEPT = 1;
	public static final int REFUSE = 2;

	private BriefUserInfoClient briefUser;
	protected int answer, guildId;
	private String desc;
	protected GuildJoinApproveResp resp;

	/**
	 * 
	 * @param _guildid
	 *            家族ID
	 * @param _answer
	 *            1:为同意 2:为拒绝
	 */
	public GuildJoinApproveInvoker(int guildId, BriefUserInfoClient briefUser,
			int _answer) {
		this.guildId = guildId;
		this.briefUser = briefUser;
		this.answer = _answer;
		if (_answer == ACCEPT) {
			desc = "接受申请";
		} else {
			desc = "拒绝申请";
		}
	}

	@Override
	protected String loadingMsg() {
		return desc;
	}

	@Override
	protected String failMsg() {
		return desc + "失败";
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().guildJoinApprove(guildId,
				briefUser.getId(), answer);
		Account.guildCache.updata(true);
	}

	@Override
	protected void onOK() {
		ctr.updateUI(resp.getRi(), true);
		if (answer == ACCEPT) {
			ctr.alert(StringUtil.nickNameColor(briefUser) + "已成功加入你的家族");
		} else
			ctr.alert("你拒绝了" + StringUtil.nickNameColor(briefUser) + "的申请");
	}
}
