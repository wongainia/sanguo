package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildInviteInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ManageInvitedUserTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_manage_invited_user;

	private ViewGroup icon;
	private Button sendMsgBtn, castleBtn, deleteInvitedBtn, closeBtn;
	private ImageView vipIcon;
	private TextView vipLevel;
	private RichGuildInfoClient rgic;
	private GuildInviteInfoClient info;
	private BriefUserInfoClient briefUser;

	public ManageInvitedUserTip(RichGuildInfoClient rgic,
			GuildInviteInfoClient info) {
		super("选择操作", DEFAULT);
		this.rgic = rgic;
		this.info = info;
		this.briefUser = info.getBriefUser();
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		// if (briefUser.getId().intValue() == Account.user.getId())
		// setTipBg(DEFAULT);

		vipIcon = (ImageView) content.findViewById(R.id.vipIcon);
		vipLevel = (TextView) content.findViewById(R.id.vipLevel);
		icon = (ViewGroup) content.findViewById(R.id.iconLayout);
		sendMsgBtn = (Button) content.findViewById(R.id.sendMsgBtn);
		sendMsgBtn.setOnClickListener(this);
		castleBtn = (Button) content.findViewById(R.id.castleBtn);
		castleBtn.setOnClickListener(this);
		deleteInvitedBtn = (Button) content.findViewById(R.id.deleteInvitedBtn);
		deleteInvitedBtn.setOnClickListener(this);
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(this);

		new UserIconCallBack(briefUser, icon, Constants.USER_ICON_WIDTH
				* Config.SCALE_FROM_HIGH, Constants.USER_ICON_HEIGHT_BIG
				* Config.SCALE_FROM_HIGH);

		ViewUtil.setText(content, R.id.name, briefUser.getNickName());
		ViewUtil.setText(content, R.id.level, "等级："
				+ briefUser.getLevel().intValue() + "级");
		ViewUtil.setText(content, R.id.userId, "ID："
				+ briefUser.getId().intValue());
		if (briefUser.getCurVip().getLevel() >= 1) {
			ImageUtil.setBgNormal(vipIcon);
			ViewUtil.setVisible(vipLevel);
			ViewUtil.setRichText(vipLevel,
					StringUtil.vipNumImgStr(briefUser.getCurVip().getLevel()));
		} else {
			ImageUtil.setBgGray(vipIcon);
			ViewUtil.setGone(vipLevel);
		}

		if (null != rgic) {
			if (rgic.isLeader(briefUser.getId())) {
				ViewUtil.setText(content, R.id.position, "职务:族长");
			} else if (rgic.isElder(briefUser.getId())) {
				ViewUtil.setText(content, R.id.position, "职务:长老");
			} else {
				ViewUtil.setText(content, R.id.position, "职务:成员");
			}
		}

	}

	@Override
	public void onClick(View v) {
		if (v == sendMsgBtn) {
			dismiss();
			controller.openChatWindow(briefUser);
		} else if (v == castleBtn) {
			// 打开主城
			dismiss();
			controller.showCastle(briefUser);
		} else if (v == closeBtn) {
			dismiss();
		} else if (v == deleteInvitedBtn) {
			dismiss();
			new GuildInviteInfoDeleteConfirm(rgic, info).show();
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
