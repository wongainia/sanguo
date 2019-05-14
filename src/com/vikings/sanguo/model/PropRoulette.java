package com.vikings.sanguo.model;

import java.util.Iterator;
import java.util.List;

import com.vikings.sanguo.protos.KeyValue;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

//轮盘配置
public class PropRoulette {
	private byte layer; // 轮盘ID（1外层；2中层；3内层）
	private byte id;// 轮盘上格子id
	private int itemId;
	private int count;
	private String icon;// 格子上显示图片名
	private int x;
	private int y;

	// 得到7 的个数
	public static int get7Count(List<KeyValue> keyValues) {
		int count = 0;
		if (ListUtil.isNull(keyValues))
			return count;
		for (KeyValue keyValue : keyValues) {
			if (keyValue.getValue() == 1/* 选中的是7 */) {
				++count;
			}
		}
		return count;
	}

	public byte getLayer() {
		return layer;
	}

	public void setLayer(byte layer) {
		this.layer = layer;
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static PropRoulette fromString(String line) {
		PropRoulette pr = new PropRoulette();
		StringBuilder buf = new StringBuilder(line);
		pr.setId(StringUtil.removeCsvByte(buf));
		pr.setLayer(StringUtil.removeCsvByte(buf));
		pr.setItemId(StringUtil.removeCsvInt(buf));
		pr.setCount(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		pr.setIcon(StringUtil.removeCsv(buf));
		pr.setX(StringUtil.removeCsvInt(buf));
		pr.setY(StringUtil.removeCsvInt(buf));
		return pr;
	}
}
