package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildInviteInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class InviteUserTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_invite_user;

	private RichGuildInfoClient rgic;
	private GuildInviteInfoClient temp;

	public InviteUserTip(RichGuildInfoClient rgic, GuildInviteInfoClient temp) {
		super("邀请加入家族", CustomConfirmDialog.DEFAULT);
		this.rgic = rgic;
		this.temp = temp;
		setButton(FIRST_BTN, "确定", this);
		setButton(SECOND_BTN, "取消", closeL);
	}

	public void show() {
		super.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

	@Override
	public void onClick(View v) {
		String text = ViewUtil.getText(content, R.id.userID);
		if (StringUtil.isNormalUserId(text)) {
			int id = Integer.valueOf(text);
			if (BriefUserInfoClient.isNPC(id)) {
				controller.alert("请输入有效的玩家ID!");
				return;
			}
			new UserInvoker(id).start();
		} else
			Config.getController().alert("请输入有效的玩家ID!");
	}

	private class UserInvoker extends BaseInvoker {
		private int id;
		private BriefUserInfoClient briefUser;

		public UserInvoker(int id) {
			this.id = id;
		}

		@Override
		protected String loadingMsg() {
			return "查询玩家";
		}

		@Override
		protected String failMsg() {
			return "查询玩家失败";
		}

		@Override
		protected void onFail(GameException exception) {
			controller.alert(StringUtil.color("没有找到ID对应玩家，请重新输入",
					ctr.getResourceColorText(R.color.k7_color8)));
		}

		@Override
		protected void fire() throws GameException {
			briefUser = CacheMgr.userCache.getNew(id);
		}

		@Override
		protected void onOK() {
			dismiss();
			if (briefUser.hasGuild())
				ctr.alert("该玩家已有家族,不能邀请");
			else
				new InviteUserConfirmTip(briefUser, rgic, temp).show();
		}

	}
}
