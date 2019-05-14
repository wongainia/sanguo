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
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.window.GuildInfoWindow;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class GuildInviteLogTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_guild_invite_log;

	// private TextView desc;

	private Button acceptBtn, guildInfoBtn, refuseBtn, closeBtn;

	private LogInfoClient log;

	private ViewGroup icon;
	private ImageView vipIcon;
	private TextView vipLevel;

	public GuildInviteLogTip(LogInfoClient log) {
		super("邀请选择", DEFAULT);
		this.log = log;
		// desc = (TextView) content.findViewById(R.id.desc);

		acceptBtn = (Button) content.findViewById(R.id.acceptBtn);
		acceptBtn.setOnClickListener(this);
		acceptBtn.setText("同意加入");
		guildInfoBtn = (Button) content.findViewById(R.id.infoBtn);
		guildInfoBtn.setOnClickListener(this);
		guildInfoBtn.setText("查看家族");
		refuseBtn = (Button) content.findViewById(R.id.refuseBtn);
		refuseBtn.setOnClickListener(this);
		refuseBtn.setText("拒绝邀请");
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
		// ViewUtil.setRichText(desc, log.getInvitedLogDesc());
		new UserIconCallBack(log.getFromUser(), icon, Constants.USER_ICON_WIDTH
				* Config.SCALE_FROM_HIGH, Constants.USER_ICON_HEIGHT_BIG
				* Config.SCALE_FROM_HIGH);

		ViewUtil.setText(content, R.id.name, log.getFromUser().getNickName());
		ViewUtil.setText(content, R.id.level, "等级："
				+ log.getFromUser().getLevel().intValue() + "级");
		ViewUtil.setText(content, R.id.userId, "ID："
				+ log.getFromUser().getId().intValue());

		if (log.getFromUser().getCurVip().getLevel() >= 1) {
			ImageUtil.setBgNormal(vipIcon);
			ViewUtil.setVisible(vipLevel);
			ViewUtil.setRichText(
					vipLevel,
					StringUtil.vipNumImgStr(log.getFromUser().getCurVip()
							.getLevel()));
		} else {
			ImageUtil.setBgGray(vipIcon);
			ViewUtil.setGone(vipLevel);
		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == acceptBtn) {
			new GuildInviteApproveInvoker(log.getGuildInfo(),
					GuildInviteApproveInvoker.ACCEPT).start();
		} else if (v == refuseBtn) {
			new GuildInviteApproveInvoker(log.getGuildInfo(),
					GuildInviteApproveInvoker.REFUSE).start();
		} else if (v == guildInfoBtn) {
			new GuildInfoWindow().open(log.getGuildInfo().getId());
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
