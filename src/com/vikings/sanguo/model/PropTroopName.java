/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-2 上午10:17:23
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropTroopName {
	private int type;
	private String name;

	public static int[] armType = { 1, 2, 3, 4, 5, 6, 7 };// 1、步兵 2、骑兵 3、枪兵 4、弓兵
															// 5、法师 6、车械 7、神兵）

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public static PropTroopName fromString(String csv) {
		PropTroopName ptn = new PropTroopName();
		StringBuilder buf = new StringBuilder(csv);
		ptn.setType(StringUtil.removeCsvInt(buf));
		ptn.setName(StringUtil.removeCsv(buf));
		return ptn;
	}
}
