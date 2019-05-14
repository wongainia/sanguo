package com.vikings.sanguo.model;

import android.graphics.Rect;

import com.vikings.sanguo.utils.StringUtil;

public class ManorLocation {

	private int type;

	private int x;

	private int y;

	private Rect clickRect = new Rect();

	private int z;

	private int tagX;// 标签底图X
	private int tagY;// 标签底图Y
	private String unlockImg;// 解锁后的挂牌
	private int unlockLevel;// 解锁等级

	private int guideX;// 引导图X
	private int guideY;// 引导图Y
	private int level;// 解锁等级
	private int vip;// 解锁Vip

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getType() {
		return type;
	}

	public int getUnlockLevel() {
		return unlockLevel;
	}

	public void setUnlockLevel(int unlockLevel) {
		this.unlockLevel = unlockLevel;
	}

	public String getUnlockImg() {
		return unlockImg;
	}

	public void setUnlockImg(String unlockImg) {
		this.unlockImg = unlockImg;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Rect getClickRect() {
		return clickRect;
	}

	public void setClickRect(Rect clickRect) {
		this.clickRect = clickRect;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getTagX() {
		return tagX;
	}

	public void setTagX(int tagX) {
		this.tagX = tagX;
	}

	public int getTagY() {
		return tagY;
	}

	public void setTagY(int tagY) {
		this.tagY = tagY;
	}

	public int getGuideX() {
		return guideX;
	}

	public void setGuideX(int guideX) {
		this.guideX = guideX;
	}

	public int getGuideY() {
		return guideY;
	}

	public void setGuideY(int guideY) {
		this.guideY = guideY;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public static ManorLocation fromString(String csv) {
		ManorLocation m = new ManorLocation();
		StringBuilder buf = new StringBuilder(csv);
		m.type = (StringUtil.removeCsvInt(buf));
		m.x = StringUtil.removeCsvInt(buf);
		m.y = StringUtil.removeCsvInt(buf);
		m.clickRect.left = StringUtil.removeCsvInt(buf);
		m.clickRect.top = StringUtil.removeCsvInt(buf);
		m.clickRect.right = StringUtil.removeCsvInt(buf);
		m.clickRect.bottom = StringUtil.removeCsvInt(buf);
		m.z = StringUtil.removeCsvInt(buf);
		m.setTagX(StringUtil.removeCsvInt(buf));
		m.setTagY(StringUtil.removeCsvInt(buf));
		m.setUnlockImg(StringUtil.removeCsv(buf));
		m.setUnlockLevel(StringUtil.removeCsvInt(buf));
		m.setVip(StringUtil.removeCsvInt(buf));
		return m;
	}

	// 主城建筑的宽度
	public int buildWidth() {
		return Math.abs(clickRect.right - clickRect.left);
	}

	// 主城建筑的高度
	public int buildHeight() {
		return Math.abs(clickRect.bottom - clickRect.top);
	}

	// 主城建筑左坐标
	public int buildLeft() {
		return x;
	}

	// 主城建筑左坐标
	public int buildTop() {
		return y;
	}

	public boolean isShow() {
		return x != -1 || y != -1;
	}

	// 是否显示挂牌
	public boolean isShowWood() {
		return tagX != -1 && tagY != -1;
	}

}
