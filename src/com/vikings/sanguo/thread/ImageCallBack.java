package com.vikings.sanguo.thread;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.vikings.sanguo.config.Config;

abstract public class ImageCallBack {

	protected String imgName;

	private CallBack failCallBack;

	abstract public void setImage(Drawable d);

	protected String stubName = "stub.png";

	public Drawable getStub() {
		return Config.getController().getDrawable(stubName);
	}

	public CallBack getFailCallBack() {
		return failCallBack;
	}

	public void setFailCallBack(CallBack failCallBack) {
		this.failCallBack = failCallBack;
	}

	protected void set(String imgName) {
		this.imgName = imgName;
		ImageLoader.getInstance().setImage(this);
	}

	public String getImgName() {
		return imgName;
	}

	public String getUrl() {
		return Config.imgUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ImageCallBack))
			return false;
		if (o == null || imgName == null || ((ImageCallBack) o).imgName == null) {
			return false;
		}
		return ((ImageCallBack) o).imgName.equals(imgName);
	}

	@Override
	public int hashCode() {
		return imgName.hashCode();
	}
	
	//加一个方法     加载的时候用16位bitmap
	protected void setLowQuality(String imgName) {
		this.imgName = imgName;
		ImageLoader.getInstance().setImageLowQuality(this);
	}
}
