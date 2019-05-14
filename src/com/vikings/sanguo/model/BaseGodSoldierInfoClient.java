package com.vikings.sanguo.model;

import java.io.Serializable;
import java.util.Date;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class BaseGodSoldierInfoClient implements Serializable {

	private static final long serialVersionUID = -9109216139085016189L;

	private long time;
	private int cost;
	private MoveTroopInfoClient mtic;

	public void setMtic(MoveTroopInfoClient mtic) {
		this.mtic = mtic;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public MoveTroopInfoClient getMtic() {
		return mtic;
	}

	public long getTime() {
		return time;
	}

	public int getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return StringUtil.color(
				DateUtil.db2MinuteFormat4.format(new Date(time)) + "  您花费"
						+ cost + "元宝，增援了" + mtic.getTotalTroopAmount(),
				R.color.k7_color2);
	}
}
