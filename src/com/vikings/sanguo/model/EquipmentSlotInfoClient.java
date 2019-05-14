package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.EquipmentSlotInfo;

//将领装备槽
public class EquipmentSlotInfoClient {
	private int type;
	private EquipmentInfoClient eic;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public EquipmentInfoClient getEic() {
		return eic;
	}

	public void setEic(EquipmentInfoClient eic) {
		this.eic = eic;
	}

	public boolean hasEquipment() {
		return null != eic;
	}

	public int getAttack() {
		int attack = 0;
		if (hasEquipment()) {
			// TODO 装备属性
		}
		return attack;
	}

	public static EquipmentSlotInfoClient convert(EquipmentSlotInfo info,
			long heroId) throws GameException {
		EquipmentSlotInfoClient esic = new EquipmentSlotInfoClient();
		esic.setType(info.getType());
		if (info.hasInfo()) {
			EquipmentInfoClient eic = EquipmentInfoClient.convert(
					info.getInfo(), heroId);
			esic.setEic(eic);
		}
		return esic;
	}

	public static List<EquipmentSlotInfoClient> convert2List(
			List<EquipmentSlotInfo> infos, long heroId) throws GameException {
		List<EquipmentSlotInfoClient> esics = new ArrayList<EquipmentSlotInfoClient>();
		if (null != infos && !infos.isEmpty()) {
			for (EquipmentSlotInfo info : infos) {
				esics.add(convert(info, heroId));
			}
		}
		return esics;
	}
}
