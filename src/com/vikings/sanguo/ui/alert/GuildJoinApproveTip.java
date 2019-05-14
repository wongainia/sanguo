package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.GuildInviteApproveInvoker;
import com.vikings.sanguo.invoker.GuildJoinApproveInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public abstract class GuildJoinApproveTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_guild_invite_log;

	// protected TextView desc;

	protected Button acceptBtn, infoBtn, refuseBtn, closeBtn;

	private ViewGroup icon;
	private ImageView vipIcon;
	private TextView vipLevel;

	public GuildJoinApproveTip() {
		super("申请选择", DEFAULT);
		initView();
	}

	protected void initView() {
		// desc = (TextView) content.findViewById(R.id.desc);
		acceptBtn = (Button) content.findViewById(R.id.acceptBtn);
		acceptBtn.setOnClickListener(this);
		acceptBtn.setText("接受申请");
		infoBtn = (Button) content.findViewById(R.id.infoBtn);
		infoBtn.setOnClickListener(this);
		infoBtn.setText("查看玩家");
		refuseBtn = (Button) content.findViewById(R.id.refuseBtn);
		refuseBtn.setOnClickListener(this);
		refuseBtn.setText("拒绝申请");
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);
		closeBtn.setText("关闭");

		vipIcon = (ImageView) content.findViewById(R.id.vipIcon);
		vipLevel = (TextView) content.findViewById(R.id.vipLevel);
		icon = (ViewGroup) content.findViewById(R.id.iconLayout);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		// ViewUtil.setRichText(desc, getDesc());
		new UserIconCallBack(getBriefUser(), icon, Constants.USER_ICON_WIDTH
				* Config.SCALE_FROM_HIGH, Constants.USER_ICON_HEIGHT_BIG
				* Config.SCALE_FROM_HIGH);

		ViewUtil.setText(content, R.id.name, getBriefUser().getNickName());
		ViewUtil.setText(content, R.id.level, "等级："
				+ getBriefUser().getLevel().intValue() + "级");
		ViewUtil.setText(content, R.id.userId, "ID："
				+ getBriefUser().getId().intValue());

		if (getBriefUser().getCurVip().getLevel() >= 1) {
			ImageUtil.setBgNormal(vipIcon);
			ViewUtil.setVisible(vipLevel);
			ViewUtil.setRichText(vipLevel, StringUtil
					.vipNumImgStr(getBriefUser().getCurVip().getLevel()));
		} else {
			ImageUtil.setBgGray(vipIcon);
			ViewUtil.setGone(vipLevel);
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (getBriefUser() == null)
			return;
		if (v == acceptBtn) {
			if (getBriefUser().hasGuild()) {
				controller.alert("对方已有家族");
				return;
			}
			new GuildJoinApproveInvoker(getGuildId(), getBriefUser(),
					GuildInviteApproveInvoker.ACCEPT).start();
		} else if (v == refuseBtn) {
			new GuildJoinApproveInvoker(getGuildId(), getBriefUser(),
					GuildInviteApproveInvoker.REFUSE).start();
		} else if (v == infoBtn) {
			Config.getController().showCastle(getBriefUser());
		}

	}

	protected abstract BriefUserInfoClient getBriefUser();

	protected abstract int getGuildId();

	protected abstract String getDesc();
}
