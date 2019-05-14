/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-11 下午5:14:41
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ArenaResetTime {
	private int time;  //巅峰战场奖励自动补满时间（单位秒，从周日的零点起算）
	private int rank;  //前N名可获得加速补满的资格
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setRank(int no) {
		this.rank = no;
	}
	
	public int getRank() {
		return rank;
	}
	
	public static ArenaResetTime fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		ArenaResetTime art = new ArenaResetTime();
		art.setTime(StringUtil.removeCsvInt(buf));
		art.setRank(StringUtil.removeCsvInt(buf));
		return art;
	}
}
