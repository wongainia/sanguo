package com.vikings.sanguo.ui.alert;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.StringUtil;

public class BoundChangeSuccess {
	public static final int BOUND_EMAIL = 1;
	public static final int CHANGE_EMAIL = 2;
	public static final int BOUND_PHONE = 3;
	public static final int CHANGE_PHONE = 4;
	public static final int BOUND_ID_CARD = 5;

	private int type;

	public void show(int type) {
		this.type = type;
		setValue();
	}

	private void setValue() {
		if (type == BOUND_EMAIL) {
			Config.getController().alert(
					StringUtil.color("绑定邮箱成功！", Config.getController()
							.getResourceColorText(R.color.k7_color4)),
					StringUtil.color("今后可以在登录界面找回账号了", Config.getController()
							.getResourceColorText(R.color.k7_color6)), null,
					true);
		} else if (type == CHANGE_EMAIL || type == CHANGE_PHONE) {
			Config.getController().alert(
					StringUtil.color("更改成功！", Config.getController()
							.getResourceColorText(R.color.k7_color4)),
					StringUtil.color("请妥善保存您的安全信息！", Config.getController()
							.getResourceColorText(R.color.k7_color6)), null,
					true);
		} else if (type == BOUND_PHONE) {
			Config.getController().alert(
					StringUtil.color("绑定手机成功！", Config.getController()
							.getResourceColorText(R.color.k7_color4)),
					StringUtil.color("今后可以重置你绑定的邮箱或手机了", Config.getController()
							.getResourceColorText(R.color.k7_color6)), null,
					true);
		} else if (type == BOUND_ID_CARD) {
			Config.getController().alert(
					StringUtil.color("实名认证成功！", Config.getController()
							.getResourceColorText(R.color.k7_color4)),
					StringUtil.color("当今后您的账号丢失或被盗时，可以去官网进行账号申诉了", Config.getController()
							.getResourceColorText(R.color.k7_color6)), null,
					true);
		}
	}
}
