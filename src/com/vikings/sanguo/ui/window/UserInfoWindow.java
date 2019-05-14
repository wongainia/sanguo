package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class UserInfoWindow extends CustomPopupWindow implements
		OnClickListener {
	private ViewGroup securityLayout;
	private Button editBtn, manageBtn;
	private OtherUserClient ouc;
	private boolean isAccount;
	private TextView guildName;

	@Override
	protected void init() {
		super.init("查看资料");
		setContent(R.layout.user_info_view);
		securityLayout = (ViewGroup) window.findViewById(R.id.securityLayout);
		editBtn = (Button) window.findViewById(R.id.editBtn);
		editBtn.setOnClickListener(this);
		manageBtn = (Button) window.findViewById(R.id.manageBtn);
		manageBtn.setOnClickListener(this);
		guildName = (TextView) window.findViewById(R.id.guild);
	}

	public void open() {
		this.isAccount = true;
		doOpen();
	}

	public void open(OtherUserClient ouc) {
		this.ouc = ouc;
		this.isAccount = false;
		doOpen();
	}

	@Override
	public void showUI() {
		if (isAccount) {
			ViewUtil.setUserDetail(Account.user, window);
			ViewUtil.setVisible(securityLayout);
			ViewUtil.setVisible(editBtn);
			ViewUtil.setVisible(manageBtn);
			if (Account.user.hasGuild()
					&& null != Account.guildCache.getRichInfoInCache()) {
				ViewUtil.setRichText(guildName, "<U>"
						+ Account.guildCache.getRichInfoInCache().getGic()
								.getName() + "</U>");
				guildName.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new GuildInfoWindow().open(Account.guildCache
								.getGuildid());

					}
				});
			} else {
				ViewUtil.setText(window, R.id.guild, "无");
			}
		} else {
			ViewUtil.setBriefUserDetail(ouc.bref(), window);
			ViewUtil.setGone(securityLayout);
			ViewUtil.setGone(editBtn);
			ViewUtil.setGone(manageBtn);
			ViewUtil.setText(window, R.id.sign, ouc.getDesc());
			if (null != ouc.getBgic()) {
				ViewUtil.setRichText(guildName, "<U>" + ouc.getBgic().getName()
						+ "</U>");
				guildName.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new GuildInfoWindow().open(ouc.getGuildid().intValue());

					}
				});
			} else {
				ViewUtil.setText(guildName, "无");
			}

		}
		super.showUI();
	}

	@Override
	public void onClick(View v) {
		if (v == editBtn) {
			controller.openUserInfoEditWindow();
		} else if (v == manageBtn) {
			controller.openAccountMangt();
		}
	}
}
