/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午3:01:05
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 章节奖励信息
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropActSpoils {
	private int actId;          //章节ID（ID分段，战役副本为1-1000，活动副本为1001-2000，其他副本为2001-10000；）
    private int type;           //type（1资源，2道具）
    private int spoilId;        //（1对应effectid，2对应物品id）
    private int spoilAmt;       //数量
    private boolean isShow;     //是否显示在章节栏（0：不显示，1：显示）

    public void setActId(int actId) {
		this.actId = actId;
	}
    
    public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
    
    public void setSpoilAmt(int spoilAmt) {
		this.spoilAmt = spoilAmt;
	}
    
    public void setSpoilId(int spoilId) {
		this.spoilId = spoilId;
	}
    
    public void setType(int type) {
		this.type = type;
	}
    
    public int getActId() {
		return actId;
	}
    
    public int getSpoilAmt() {
		return spoilAmt;
	}
    
    public int getSpoilId() {
		return spoilId;
	}
    
    public int getType() {
		return type;
	}
    
    public boolean isShow() {
		return isShow;
	}
    
    static public PropActSpoils fromString (String line) {
    	StringBuilder buf = new StringBuilder(line);
    	PropActSpoils as = new PropActSpoils();
    	
    	as.setActId(StringUtil.removeCsvInt(buf));
    	as.setType(StringUtil.removeCsvInt(buf));
    	as.setSpoilId(StringUtil.removeCsvInt(buf));
    	as.setSpoilAmt(StringUtil.removeCsvInt(buf));
    	as.setShow((1 == StringUtil.removeCsvInt(buf)) ? true : false);
    	
    	return as;
    }
}
