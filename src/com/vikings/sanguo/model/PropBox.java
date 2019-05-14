/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-4-12 下午3:42:24
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropBox {
	private int id;// 宝箱id
	private int needItemId;// 开箱需要物品id
	private int needItemCount;// 开箱需要物品数量
	private int needMoney;// 开箱需要金币数量
	private int needCurrency;// 开箱子需要元宝数量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNeedItemId() {
		return needItemId;
	}

	public void setNeedItemId(int needItemId) {
		this.needItemId = needItemId;
	}

	public int getNeedItemCount() {
		return needItemCount;
	}

	public void setNeedItemCount(int needItemCount) {
		this.needItemCount = needItemCount;
	}

	public int getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}

	public int getNeedCurrency() {
		return needCurrency;
	}

	public void setNeedCurrency(int needCurrency) {
		this.needCurrency = needCurrency;
	}

	public static PropBox fromString(String csv) {
		PropBox pb = new PropBox();
		StringBuilder sb = new StringBuilder(csv);
		pb.setId(StringUtil.removeCsvInt(sb));
		StringUtil.removeCsv(sb);//跳过方案号
		pb.setNeedItemId(StringUtil.removeCsvInt(sb));
		pb.setNeedItemCount(StringUtil.removeCsvInt(sb));
		StringUtil.removeCsv(sb);//跳过最多产生物品数
		StringUtil.removeCsv(sb);//跳过是否自动开启
		pb.setNeedMoney(StringUtil.removeCsvInt(sb));
		pb.setNeedCurrency(StringUtil.removeCsvInt(sb));
		return pb;
	}

}
