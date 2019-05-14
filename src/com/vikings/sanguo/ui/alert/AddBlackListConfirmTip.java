package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.AddBlackListInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class AddBlackListConfirmTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_add_black_list_confirm;

	private GuildUserData data;
	private BriefUserInfoClient briefUser;
	private ImageView vipIcon;
	private TextView vipLevel;
	private ViewGroup icon;

	public AddBlackListConfirmTip(GuildUserData data) {
		super("仇人确认");
		this.data = data;
		this.briefUser = data.getUser();
		icon = (ViewGroup) content.findViewById(R.id.iconLayout);
		vipIcon = (ImageView) content.findViewById(R.id.vipIcon);
		vipLevel = (TextView) content.findViewById(R.id.vipLevel);
		setButton(FIRST_BTN, "确定", this);
		setButton(SECOND_BTN, "取消", closeL);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		if (briefUser.isVip()) {
			IconUtil.setUserIcon(icon, briefUser, "VIP"
					+ briefUser.getCurVip().getLevel());
		} else {
			IconUtil.setUserIcon(icon, briefUser);
		}
		ViewUtil.setText(content, R.id.name, briefUser.getNickName());
		ViewUtil.setText(content, R.id.level, "等级: " + briefUser.getLevel()
				+ "级");
		if (null == data.getBgic()) {
			ViewUtil.setText(content, R.id.guild, "家族: 无");
		} else {
			ViewUtil.setText(content, R.id.guild, "家族: "
					+ data.getBgic().getName());
		}

		if (null == data.getCountry())
			ViewUtil.setText(content, R.id.country, "国家: 无");
		else
			ViewUtil.setText(content, R.id.country, "国家: "
					+ data.getCountry().getName());

		ViewUtil.setText(content, R.id.userid, "ID: " + briefUser.getId());

		if (briefUser.getCurVip().getLevel() >= 1) {
			ImageUtil.setBgNormal(vipIcon);
			ViewUtil.setVisible(vipLevel);
			ViewUtil.setRichText(vipLevel,
					StringUtil.vipNumImgStr(briefUser.getCurVip().getLevel()));
		} else {
			ImageUtil.setBgGray(vipIcon);
			ViewUtil.setGone(vipLevel);
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

	private class AddBLInvoker extends AddBlackListInvoker {

		public AddBLInvoker(BriefUserInfoClient briefUser) {
			super(briefUser);
		}

		@Override
		protected void onOK() {
			dismiss();
			super.onOK();
		}
	}

	@Override
	public void onClick(View v) {
		new AddBLInvoker(briefUser).start();

	}
}
