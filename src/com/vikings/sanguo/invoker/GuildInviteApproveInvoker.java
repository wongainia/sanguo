package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.ui.alert.CommonAlert;
import com.vikings.sanguo.ui.window.GuildInfoWindow;
import com.vikings.sanguo.utils.StringUtil;

public class GuildInviteApproveInvoker extends BaseInvoker {
	public static final int ACCEPT = 1;
	public static final int REFUSE = 2;

	private BriefGuildInfoClient bgic;
	private int answer;
	private String desc;

	/**
	 * 
	 * @param _guildid
	 *            家族ID
	 * @param _answer
	 *            1:为同意 2:为拒绝
	 */
	public GuildInviteApproveInvoker(BriefGuildInfoClient bgic, int _answer) {
		this.bgic = bgic;
		this.answer = _answer;
		if (_answer == ACCEPT) {
			desc = "加入家族";
		} else {
			desc = "拒绝邀请";
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
		GameBiz.getInstance().guildInviteApprove(bgic.getId(), answer);
		if (answer == ACCEPT) {
			Account.guildCache.setGuildid(bgic.getId());
			Account.guildCache.updata(true);
		}
	}

	@Override
	protected void onOK() {
		if (answer == ACCEPT) {
			if (null != ctr.getHeartBeat())
				ctr.getHeartBeat().updataChatIds();
			ctr.closeAllPopup();
			new GuildInfoWindow().open(bgic.getId());
			new CommonAlert("加入家族成功", "恭喜您成为"
					+ StringUtil.color(bgic.getName(), R.color.k7_color9)
					+ "家族的一员，快和您所属家族的成员打个招呼吧！", "", "点击任意位置关闭", true).show();
		} else
			ctr.alert("你拒绝了"
					+ StringUtil.color(bgic.getName(),
							ctr.getResourceColorText(R.color.k7_color9))
					+ "的邀请");
	}

}
