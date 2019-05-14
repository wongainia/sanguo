package com.vikings.sanguo.ui.alert;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.GuildInviteAskResp;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.GuildInviteInfoClient;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class InviteUserConfirmTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_invite_user_confirm;

	private RichGuildInfoClient rgic;
	private BriefUserInfoClient briefUser;
	private GuildInviteInfoClient temp;
	private GuildProp guildProp;

	public InviteUserConfirmTip(BriefUserInfoClient briefUser,
			RichGuildInfoClient rgic, GuildInviteInfoClient temp) {
		super("确认邀请", CustomConfirmDialog.DEFAULT);
		this.rgic = rgic;
		this.briefUser = briefUser;
		this.temp = temp;
		setButton(FIRST_BTN, "确定", this);
		setButton(SECOND_BTN, "取消", closeL);

		try {
			guildProp = (GuildProp) CacheMgr.guildPropCache.get(rgic.getGic()
					.getLevel());
		} catch (GameException e) {
			e.printStackTrace();
		}
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		if (briefUser.isVip()) {
			IconUtil.setUserIcon(
					(ViewGroup) content.findViewById(R.id.iconLayout),
					briefUser, "VIP" + briefUser.getCurVip().getLevel());
		} else {
			IconUtil.setUserIcon(
					(ViewGroup) content.findViewById(R.id.iconLayout),
					briefUser);
		}

		ViewUtil.setText(content, R.id.name, briefUser.getNickName() + "(ID:"
				+ briefUser.getId().intValue() + ")");
		ViewUtil.setText(content, R.id.level, "等级:"
				+ briefUser.getLevel().intValue() + "级");
		ViewUtil.setText((TextView) content.findViewById(R.id.count), "族员:"
				+ rgic.getMembers().size() + "/" + guildProp.getMaxMemberCnt());

		int cost = CacheMgr.dictCache.getDictInt(Dict.TYPE_GUILD, 1);
		ViewUtil.setRichText(content, R.id.cost, "邀请需要花费#rmb#" + cost + "元宝",
				true);
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	@Override
	public void onClick(View v) {
		new InviteInvoker("").start();
	}

	private class InviteInvoker extends BaseInvoker {
		private String msgStr;
		private GuildInviteAskResp resp;
		private int maxCnt;

		public InviteInvoker(String msg) {
			this.msgStr = msg;
		}

		@Override
		protected String failMsg() {
			return null;
		}

		@Override
		protected void onFail(GameException exception) {
			Log.e("InviteUserConfirmTip", exception.getMessage());
			if (exception.getResult() == (short) 281) {
				String error = exception.getErrorMsg();
				error = error.replace("<number>", maxCnt + "");
				String msg = failMsg();
				if (!StringUtil.isNull(msg))
					msg = msg + error;
				else
					msg = error;
				Config.getController().alert(msg);

			} else {
				String msg = failMsg();
				if (!StringUtil.isNull(msg))
					msg = msg + exception.getErrorMsg();
				else
					msg = exception.getErrorMsg();
				Config.getController().alert(msg);
			}

		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRi(), true);
			dismiss();
			// 邀请成功提示
			// new GuildInviteSucessTip(briefUser).show();
			ctr.alert(
					"你已向"
							+ StringUtil.color(briefUser.getHtmlNickName(),
									R.color.k7_color9)
							+ "发送了邀请，<br/>对方同意后将加入家族。",
					"如果对方长时间不响应，你可以在邀请列表中取消邀请", null, true);
		}

		@Override
		protected String loadingMsg() {
			return "发送邀请中";
		}

		@Override
		protected void fire() throws GameException {
			try {
				// GuildProp guildProp =
				// CacheMgr.guildPropCache.getGuildProp(rgic
				// .getMembers().size());
				GuildProp guildProp = CacheMgr.guildPropCache.search(rgic.getGic()
						.getLevel());
				if (guildProp != null) {
					maxCnt = guildProp.getMaxInviteCnt();
				}

				resp = GameBiz.getInstance().guildInviteAsk(rgic.getGuildid(),
						briefUser.getId().intValue(), msgStr);

				temp.setApprove(false);
				temp.setBriefUser(briefUser);
				temp.setUserId(briefUser.getId());
				temp.setTime(Config.serverTimeSS());
			} catch (GameException e) {
				if (e.getResult() == ResultCode.RESULT_FAILED_GUILD_INVITE_MEMBER) {
					CacheMgr.userCache.getNew(briefUser.getId().intValue());
				}
				throw e;
			}

			// 此处只刷新数据，加try—catch是保证不会因为数据更新失败，导致提示操作失败；心跳也会更新数据
			try {
				Account.guildCache.updata(true);
			} catch (Exception e) {
				Log.e("InviteUserConfirmTip", e.getMessage());
			}

		}
	}
}
