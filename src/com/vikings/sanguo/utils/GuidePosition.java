package com.vikings.sanguo.utils;

public class GuidePosition {
	int vleft;
	int vTop;
	int width;
	int hight;

	public GuidePosition(int vleft, int vTop, int width, int hight) {
		this.vleft = vleft;
		this.vTop = vTop;
		this.width = width;
		this.hight = hight;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHight() {
		return hight;
	}

	public void setHight(int hight) {
		this.hight = hight;
	}

	public int getVleft() {
		return vleft;
	}

	public void setVleft(int vleft) {
		this.vleft = vleft;
	}

	public int getVTop() {
		return vTop;
	}

	public void setVTop(int vTop) {
		this.vTop = vTop;
	}

}
