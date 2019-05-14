package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.PokerInfo;

public class PokerInfoclient {

	private int index; // 客户端传的index
	private int result; // 服务器给的结果
	private int count;// 获得兵力数量

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getResult() {
		return result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PokerInfoclient) {
			if (index == ((PokerInfoclient) obj).getIndex())
				return true;
		}
		return false;
	}

	public static PokerInfoclient convert(PokerInfo info) {
		if (null == info)
			return null;
		PokerInfoclient pic = new PokerInfoclient();
		pic.setIndex(info.getIndex());
		pic.setResult(info.getResult());
		pic.setCount(info.getCount());
		return pic;
	}

	public static List<PokerInfoclient> convert2List(List<PokerInfo> infos) {
		List<PokerInfoclient> list = new ArrayList<PokerInfoclient>();
		if (null == infos || infos.isEmpty())
			return list;
		for (PokerInfo info : infos) {
			list.add(convert(info));
		}
		return list;
	}
}
