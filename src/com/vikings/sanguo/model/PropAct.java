/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午2:17:09
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 副本数据
 */
package com.vikings.sanguo.model;

import java.util.Date;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.StringUtil;

public class PropAct {
	public static final byte DIFFICULT_NORMAL = 1;
	public static final byte DIFFICULT_HARD = 2;

	private int id; // 章节ID(ID分段,战役副本为1-1000,活动副本为1001-2000,其他副本为2001-10000;)
	private int type; // 副本类型（1：新手副本，2：战役副本，3：活动副本，4：时空副本，5：名将副本，6：秘籍副本）
	private int serverType; // 服务器副本类型（1：普通副本，2：活动副本）
	private String name; // 章节名称
	private String icon; // 章节图标(填0为不使用图标，使用则填写图片名称)
	private String desc; // 章节描述
	private byte difficult; // 难度(1普通、2困难)
	private int preActId; // 前置开启章节ID（前置章节的所有战役首个难度全通之后则开启后续章节，无前置章节则填0，有则填写章节ID）

	private Date openTime; // 副本开启日期（永久开则填0，有则按此规则2013/4/3）
	private Date closeTime; // 副本消失时间（（永久开则填0，有则按此规则2013/4/3）

	private String spoilDesc; // 通关奖励描述
	private byte preActDiff; // 前置开启战役的难度要求（0：无难度，1：普通，2：噩梦，3：普通+噩梦，4：地狱,5：普通+地狱，6：噩梦+地狱，7：普通+噩梦+地狱；）
	private byte clear;// 是否可以扫荡（0:不可以，1:可以）
	private String nameIcon;

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPreActId(int preActId) {
		this.preActId = preActId;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public String getIcon() {
		return icon;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSubName() {
		int index = name.indexOf(":");
		if (index < 0)
			index = name.indexOf("：");
		if (index >= 0)
			return name.substring(0, index);
		return name;
	}

	public int getPreActId() {
		return preActId;
	}

	public int getType() {
		return type;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public void setSpoilDesc(String spoilDesc) {
		this.spoilDesc = spoilDesc;
	}

	public String getSpoilDesc() {
		return spoilDesc;
	}

	public void setPreActDiff(byte preActDiff) {
		this.preActDiff = preActDiff;
	}

	public byte getPreActDiff() {
		return preActDiff;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	public int getServerType() {
		return serverType;
	}

	public byte getDifficult() {
		return difficult;
	}

	public void setDifficult(byte difficult) {
		this.difficult = difficult;
	}

	public boolean hasNoPreAct() {
		return preActId <= 0;
	}

	public byte getClear() {
		return clear;
	}

	public void setClear(byte clear) {
		this.clear = clear;
	}

	// 是否可以扫荡
	public boolean canClear() {
		return this.clear == 1;
	}

	public String getNameIcon() {
		return nameIcon;
	}

	public void setNameIcon(String nameIcon) {
		this.nameIcon = nameIcon;
	}

	public int getDifficultyImgResId() {
		switch (difficult) {
		case DIFFICULT_NORMAL:
			return R.drawable.diff_normal;
		case DIFFICULT_HARD:
			return R.drawable.diff_hard;
		default:
			return 0;
		}
	}

	public static String getDifficultyDesc(int difficult) {
		switch (difficult) {
		case DIFFICULT_NORMAL:
			return "普通";
		case DIFFICULT_HARD:
			return "困难";
		default:
			return "";
		}
	}

	public static PropAct fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		PropAct act = new PropAct();
		act.setId(StringUtil.removeCsvInt(buf));
		act.setType(StringUtil.removeCsvInt(buf));
		act.setServerType(StringUtil.removeCsvInt(buf));
		act.setName(StringUtil.removeCsv(buf));
		act.setIcon(StringUtil.removeCsv(buf));
		act.setDesc(StringUtil.removeCsv(buf));
		act.setDifficult(StringUtil.removeCsvByte(buf));
		act.setPreActId(StringUtil.removeCsvInt(buf));
		act.setOpenTime(StringUtil.toDate(StringUtil.removeCsv(buf)));
		act.setCloseTime(StringUtil.toDate(StringUtil.removeCsv(buf)));
		act.setSpoilDesc(StringUtil.removeCsv(buf));
		act.setPreActDiff(StringUtil.removeCsvByte(buf));
		act.setClear(StringUtil.removeCsvByte(buf));
		act.setNameIcon(StringUtil.removeCsv(buf));
		return act;
	}
}
