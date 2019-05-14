package com.vikings.sanguo.ui.alert;

import android.view.View;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.GuildInviteApproveInvoker;
import com.vikings.sanguo.invoker.GuildJoinApproveInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildJoinInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class GuildJoinInfoApproveTip extends GuildJoinApproveTip {

	private GuildJoinInfoClient info;
	private RichGuildInfoClient rgic;

	public GuildJoinInfoApproveTip(RichGuildInfoClient rgic,
			GuildJoinInfoClient info) {
		super();
		this.info = info;
		this.rgic = rgic;
	}

	@Override
	protected String getDesc() {
		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(info.getTime())).append("前，")
				.append(StringUtil.nickNameColor(info.getBriefUser()))
				.append("申请加入家族");
		return buf.toString();
	}

	@Override
	protected BriefUserInfoClient getBriefUser() {
		return info.getBriefUser();
	}

	@Override
	protected int getGuildId() {
		return rgic.getGuildid();
	}

	@Override
	public void onClick(View v) {
		if (v == acceptBtn) {
			if (getBriefUser().hasGuild()) {
				dismiss();
				controller.alert("对方已有家族");
				return;
			}
			new JoinInvoker(getGuildId(), getBriefUser(),
					GuildInviteApproveInvoker.ACCEPT).start();
		} else if (v == refuseBtn) {
			new JoinInvoker(getGuildId(), getBriefUser(),
					GuildInviteApproveInvoker.REFUSE).start();
		} else if (v == infoBtn) {
			dismiss();
			Config.getController().showCastle(getBriefUser());
		}

	}

	private class JoinInvoker extends GuildJoinApproveInvoker {

		public JoinInvoker(int guildId, BriefUserInfoClient briefUser,
				int _answer) {
			super(guildId, briefUser, _answer);
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRi(), true);
			info.setApprove(true);
			dismiss();
			if (answer == ACCEPT) {
				ctr.alert(StringUtil.nickNameColor(getBriefUser())
						+ "已成功加入你的家族");
			} else
				ctr.alert("你拒绝了" + StringUtil.nickNameColor(getBriefUser())
						+ "的申请");
		}

		@Override
		protected void onFail(GameException exception) {
			dismiss();
			super.onFail(exception);
		}

	}

}
