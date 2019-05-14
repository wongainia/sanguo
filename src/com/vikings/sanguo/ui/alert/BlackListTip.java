package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Country;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class BlackListTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_add_black_list_confirm;

	private GuildUserData data;
	private BriefUserInfoClient briefUser;
	private ImageView vipIcon;
	private TextView vipLevel;
	private ViewGroup icon;

	public BlackListTip(GuildUserData data) {
		super("选择操作", CustomConfirmDialog.MEDIUM);
		this.data = data;
		briefUser = data.getUser();
		// icon = (ImageView) content.findViewById(R.id.icon);
		icon = (ViewGroup) content.findViewById(R.id.iconLayout);
		vipIcon = (ImageView) content.findViewById(R.id.vipIcon);
		vipLevel = (TextView) content.findViewById(R.id.vipLevel);
		setButton(FIRST_BTN, "查看主城", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				controller.showCastle(BlackListTip.this.briefUser);
			}
		});
		setButton(SECOND_BTN, "移出仇人录", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				new DeleteBlackListConfirmTip(BlackListTip.this.briefUser)
						.show();
			}
		});
		setButton(THIRD_BTN, "关闭", closeL);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		new UserIconCallBack(briefUser, icon, Constants.USER_ICON_WIDTH
				* Config.SCALE_FROM_HIGH, Constants.USER_ICON_HEIGHT_BIG
				* Config.SCALE_FROM_HIGH);

		ViewUtil.setText(content, R.id.name, briefUser.getNickName());
		ViewUtil.setText(content, R.id.level, "等级: " + briefUser.getLevel()
				+ "级");
		if (null != data.getBgic())
			ViewUtil.setText(content, R.id.guild, "家族: "
					+ data.getBgic().getName());
		else
			ViewUtil.setText(content, R.id.guild, "家族: 无");
		Country country = CacheMgr.countryCache.getCountry(briefUser
				.getCountry());
		if (null != country)
			ViewUtil.setText(content, R.id.country, "国家: " + country.getName());
		else
			ViewUtil.setText(content, R.id.country, "国家: 无");

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
}
