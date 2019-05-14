package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.ArmEffectInfo;
import com.vikings.sanguo.protos.ArmPropInfo;

public class ArmPropInfoClient {
	private int armId;

	private ArrayList<ArmEffectClient> effects = new ArrayList<ArmEffectClient>();

	public ArmPropInfoClient(int armId) {
		this.armId = armId;
		for (int i = BattlePropDefine.PROP_LIFE; i <= BattlePropDefine.PROP_ANTICRIT; i++) {
			ArmEffectInfo e = new ArmEffectInfo().setId(i).setExp(0)
					.setLevel(0);
			ArmEffectClient ec = new ArmEffectClient(e, armId);
			if (ec.isValid())
				effects.add(ec);
		}
	}

	// PROP_CRIT暴击伤害跟韧性都可以强化
	public ArmPropInfoClient(ArmPropInfo prop) {
		this.armId = prop.getArmid();
		for (int i = BattlePropDefine.PROP_LIFE; i <= BattlePropDefine.PROP_ANTICRIT; i++) {
			boolean found = false;
			for (ArmEffectInfo e : prop.getInfosList()) {
				if (e.getId() == i) {
					found = true;
					ArmEffectClient ec = new ArmEffectClient(e, armId);
					if (ec.isValid())
						effects.add(ec);
					break;
				}
			}
			if (!found) {
				ArmEffectInfo e = new ArmEffectInfo().setId(i).setExp(0)
						.setLevel(0);
				ArmEffectClient ec = new ArmEffectClient(e, armId);
				if (ec.isValid())
					effects.add(ec);
			}
		}
	}

	public int getArmid() {
		return armId;
	}

	/**
	 * 取强化属性列表
	 * 
	 * @return
	 */
	public List<ArmEffectClient> getEnhanceList() {
		return effects;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + armId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArmPropInfoClient other = (ArmPropInfoClient) obj;
		return other.getArmid() == getArmid();
	}

}
