package com.vikings.sanguo.thread;

import android.graphics.drawable.Drawable;

public class NullImgCallBack extends ImageCallBack {

	public NullImgCallBack(String name) {
		set(name);
	}

	@Override
	public void setImage(Drawable d) {
	}

}
