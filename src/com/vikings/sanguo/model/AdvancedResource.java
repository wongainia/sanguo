/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-28 下午2:53:46
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.protos.AttrType;

public class AdvancedResource {
	private AttrType type;
	private String name;
	private List<Integer> propIds;

	final static private AttrType[] attrTypes = { AttrType.ATTR_TYPE_MONEY,
			AttrType.ATTR_TYPE_FOOD, AttrType.ATTR_TYPE_WOOD,
			AttrType.ATTR_TYPE_MATERIAL_0, AttrType.ATTR_TYPE_MATERIAL_1 };

	public void setName(String name) {
		this.name = name;
	}

	public void setPropIds(List<Integer> propIds) {
		this.propIds = propIds;
	}

	public void setType(AttrType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public List<Integer> getPropIds() {
		return propIds;
	}

	public AttrType getType() {
		return type;
	}

	public static List<AdvancedResource> getAdvancedResouceInfos() {
		List<AdvancedResource> ls = new ArrayList<AdvancedResource>();

		for (int i = 0; i < attrTypes.length; i++) {
			AdvancedResource ar = new AdvancedResource();
			ar.setType(attrTypes[i]);
			ar.setName(ar.getResName(attrTypes[i]));
			ar.setPropIds(CacheMgr.fiefPropCache.getAdvancedResPropIds(
					attrTypes[i].number, CacheMgr.dictCache.getDictInt(
							Dict.TYPE_ADVANCED_RESOURCE, 2)));
			ls.add(ar);
		}
		return ls;
	}

	private String getResName(AttrType type) {
		String name = "";
		switch (type) {
		case ATTR_TYPE_MONEY:
			name = "金矿";
			break;
		case ATTR_TYPE_FOOD:
			name = "农田";
			break;
		case ATTR_TYPE_WOOD:
			name = "林场";
			break;
		case ATTR_TYPE_MATERIAL_0:
			name = "铁矿";
			break;
		case ATTR_TYPE_MATERIAL_1:
			name = "猎园";
			break;
		default:
			break;
		}

		return "高级" + name;
	}
}
