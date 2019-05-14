package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.OtherHeroArmPropInfo;

public class OtherHeroArmPropInfoClient extends HeroArmPropClient {

	public OtherHeroArmPropInfoClient(int type) throws GameException {
		super(type);
	}

	public static OtherHeroArmPropInfoClient convert(OtherHeroArmPropInfo info)
			throws GameException {
		if (null == info)
			return null;
		OtherHeroArmPropInfoClient ohapic = new OtherHeroArmPropInfoClient(
				info.getType());
		ohapic.setValue(info.getValue());
		ohapic.setMaxValue(info.getMaxValue());
		return ohapic;
	}

	public static List<OtherHeroArmPropInfoClient> convert2List(
			List<OtherHeroArmPropInfo> infos) throws GameException {
		List<OtherHeroArmPropInfoClient> list = new ArrayList<OtherHeroArmPropInfoClient>();
		if (null == infos || infos.isEmpty())
			return list;
		for (OtherHeroArmPropInfo info : infos) {
			list.add(convert(info));
		}
		return list;
	}

	public OtherHeroArmPropInfoClient update(HeroArmPropClient hapc) {
		if (null != hapc) {
			setType(hapc.getType());
			setMaxValue(hapc.getMaxValue());
			setValue(hapc.getValue());
			setHeroTroopName(hapc.getHeroTroopName());
		}
		return this;
	}
	
	public String getArmPropSlug(boolean addIcon) {
		StringBuilder buf = new StringBuilder();
		if (addIcon)
			buf.append("#").append(getHeroTroopName().getSmallIcon())
					.append("#");
		buf.append(getHeroTroopName().getSlug()).append("兵");
		return buf.toString();
	}
}
