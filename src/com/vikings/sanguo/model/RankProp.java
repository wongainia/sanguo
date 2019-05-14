/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-10 下午5:55:06
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.protos.UserRankType;
import com.vikings.sanguo.utils.StringUtil;

public class RankProp {
	private int id;
	private String title;
	private String desc;
	private String icon;
	private String firstName;
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static RankProp fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		
		RankProp rp = new RankProp();
		rp.setId(StringUtil.removeCsvInt(buf));
		rp.setTitle(StringUtil.removeCsv(buf));
		rp.setDesc(StringUtil.removeCsv(buf));
		rp.setIcon(StringUtil.removeCsv(buf));
		rp.setFirstName(StringUtil.removeCsv(buf));
		
		return rp;
	}
	
	public UserRankType getUserRankType() {
		switch (getId()) {
		case 1:
			return UserRankType.USER_RANK_CHARGE;
		case 3:
			return UserRankType.USER_RANK_KILL;
		case 4:
			return UserRankType.USER_RANK_LEVEL;
		default:
			return null;
		}
	}
	
	public int getUITextId() {
		switch (getId()) {
		case 1:
			return UITextProp.RANK_WEALTH;
		case 2:
			return UITextProp.RANK_HERO;
		case 3:
			return UITextProp.RANK_KILLS;
		case 4:
			return UITextProp.RANK_LORD;
		default:
			return 0;
		}
	}
}
