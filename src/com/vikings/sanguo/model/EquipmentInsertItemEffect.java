package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class EquipmentInsertItemEffect {
	private int id; // 宝石ID（itemID）

	private short propId; // 属性id 1=生命 3=攻击 4=防御 5=射程 6=拦截 7=灵巧 9=速度
							// 10=暴击率（1=0.01%） 11=暴击伤害倍数（1=1%）
							// 12=韧性（免暴率）（1=0.01%）

	private int value;// 加成值

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public short getPropId() {
		return propId;
	}

	public void setPropId(short propId) {
		this.propId = propId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static EquipmentInsertItemEffect fromString(String line) {
		EquipmentInsertItemEffect eiie = new EquipmentInsertItemEffect();
		StringBuilder buf = new StringBuilder(line);
		eiie.setId(StringUtil.removeCsvInt(buf));
		eiie.setPropId(StringUtil.removeCsvShort(buf));
		eiie.setValue(StringUtil.removeCsvInt(buf));
		return eiie;
	}

	public String getDesc() {
		StringBuilder buf = new StringBuilder("增加"
				+ BattlePropDefine.getPropDesc(propId));
		switch (propId) {
		case BattlePropDefine.PROP_CRIT:
		case BattlePropDefine.PROP_ANTICRIT:
			buf.append(value * 0.01).append("%");
			break;
		case BattlePropDefine.PROP_CRIT_MULTIPLE:
			buf.append(value).append("%");
			break;
		default:
			buf.append(value);
			break;
		}
		return buf.toString();
	}
}
