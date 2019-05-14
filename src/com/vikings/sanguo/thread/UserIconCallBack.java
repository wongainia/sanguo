package com.vikings.sanguo.thread;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class UserIconCallBack extends ImageCallBack {

	private static final int key = R.id.icon;

	private View v;

	private BriefUserInfoClient u;

	private long time;

	private float width;
	private float height;

	public UserIconCallBack(BriefUserInfoClient user, View v) {
		this(user, v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public UserIconCallBack(BriefUserInfoClient user, ViewGroup v) {
		this(user, (View) v, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	public UserIconCallBack(BriefUserInfoClient user, ViewGroup v, float width,
			float height) {
		this(user, v.findViewById(R.id.icon), width, height);
	}

	public UserIconCallBack(BriefUserInfoClient user, View v, float width,
			float height) {
		this.u = user;
		this.v = v;
		this.width = width;
		this.height = height;
		time = System.currentTimeMillis();
		this.v.setTag(key, time);
		if (u.getId() == 10000) { // 如果是GM显示GM头像
			v.setBackgroundResource(R.drawable.gm_icon);
			ViewUtil.adjustLayout(v, (int) width, (int) height);
		} else if (!u.isCustomIcon() || !"OPEN".equals(Setting.icon)) { // 非自定义头像
			if (u.getSex() == Constants.MALE) {
				v.setBackgroundResource(Config.iconId[0]);
			} else {
				v.setBackgroundResource(Config.iconId[1]);
			}
			ViewUtil.adjustLayout(v, (int) width, (int) height);
			return;
		} else { // 自定义头像
			set(BytesUtil.getLong(u.getId(), u.getImage()) + ".png");
		}
	}

	@Override
	public Drawable getStub() {
		// 修改默认
		if (u.getSex() == Constants.MALE) {
			return Config.getController().getDrawable(R.drawable.user_icon_1);
		} else {
			return Config.getController().getDrawable(R.drawable.user_icon_2);
		}
	}

	@Override
	public void setImage(Drawable d) {
		long t = (Long) v.getTag(key);
		if (t != time)
			return;
		if (v.getBackground() != null)
			v.getBackground().setCallback(null);
		v.setBackgroundDrawable(d);
		if (width > 0 && height > 0)
			ViewUtil.adjustLayout(v, (int) width, (int) height);
	}

	@Override
	public String getUrl() {
		return Config.iconUrl;
	}

}
