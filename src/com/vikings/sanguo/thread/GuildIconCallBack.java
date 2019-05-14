package com.vikings.sanguo.thread;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GuildIconCallBack extends ImageCallBack {

	private static final int key = R.id.icon;

	private View v;
	private long time;
	private float width;
	private float height;

	public GuildIconCallBack(BriefGuildInfoClient info, View v) {
		this(info, v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public GuildIconCallBack(BriefGuildInfoClient info, View v, float width,
			float height) {
		stubName = "stub_guild_icon.jpg";
		this.v = v;
		this.width = width;
		this.height = height;
		this.time = System.currentTimeMillis();
		this.v.setTag(key, time);
		set(BytesUtil.getLong(info.getId(), info.getImage()) + ".png");
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
		return Config.guildIconUrl;
	}
}
