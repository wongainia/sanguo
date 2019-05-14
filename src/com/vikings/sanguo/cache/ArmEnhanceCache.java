package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.model.ArmEffectClient;
import com.vikings.sanguo.model.ArmPropInfoClient;
import com.vikings.sanguo.model.SyncCtrl;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.protos.ArmPropInfo;
import com.vikings.sanguo.protos.ExArmPropInfo;
import com.vikings.sanguo.protos.TroopEffectInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;

public class ArmEnhanceCache {

	private List<ArmPropInfoClient> propList = new ArrayList<ArmPropInfoClient>();

	public void updateDate(SyncDataSet syncDataSet) {
		if (syncDataSet.armPropInfos == null)
			return;
		for (ExArmPropInfo ea : syncDataSet.armPropInfos.getInfosList()) {
			switch (ea.getCtrl().getOp().byteValue()) {
			case SyncCtrl.DATA_CTRL_OP_NONE:
			case SyncCtrl.DATA_CTRL_OP_ADD:
			case SyncCtrl.DATA_CTRL_OP_REP:
				update(ea.getInfo());
				break;
			case SyncCtrl.DATA_CTRL_OP_DEL:
				delete(ea.getInfo().getArmid());
				break;
			default:
				break;
			}
		}

	}

	private synchronized void delete(int armId) {
		int index = -1;
		for (int i = 0; i < propList.size(); i++) {
			if (propList.get(i).getArmid() == armId) {
				index = i;
				break;
			}
		}
		if (index != -1)
			propList.remove(index);
	}

	public synchronized void update(ArmPropInfo ap) {
		ArmPropInfoClient a = new ArmPropInfoClient(ap);
		int index = propList.indexOf(a);
		if (index == -1)
			propList.add(a);
		else
			propList.set(index, a);
	}

	/**
	 * 获取可强化的兵种id
	 * 
	 * @return
	 */
	public List<Integer> getEnhanceArmId() {
		return CacheMgr.armEnhancePropCache.armId();
	}

	/**
	 * 获取兵种的强化信息 所有属性都有初始值
	 * 
	 * @param armId
	 * @return
	 */
	public synchronized ArmPropInfoClient getPropByArmId(int armId) {
		for (ArmPropInfoClient ac : propList) {
			if (ac.getArmid() == armId)
				return ac;
		}
		ArmPropInfoClient ac = new ArmPropInfoClient(armId);
		propList.add(ac);
		return ac;
	}

	public UserTroopEffectInfo getUserTroopEffectInfo(int armId) {
		UserTroopEffectInfo info = new UserTroopEffectInfo();
		info.setUserid(Account.user.getId());
		List<TroopEffectInfo> effectInfos = new ArrayList<TroopEffectInfo>();
		for (ArmPropInfoClient apic : propList) {
			if (apic.getArmid() == armId) {
				for (ArmEffectClient aec : apic.getEnhanceList()) {
					TroopEffectInfo tei = new TroopEffectInfo();
					tei.setArmid(armId);
					tei.setAttr(aec.getId());
					tei.setValue(aec.getEnhanceValue());
					effectInfos.add(tei);
				}
				break;
			}
		}
		info.setInfosList(effectInfos);
		return info;
	}

	public UserTroopEffectInfo getUserTroopEffectInfo() {
		UserTroopEffectInfo info = new UserTroopEffectInfo();
		info.setUserid(Account.user.getId());
		List<TroopEffectInfo> effectInfos = new ArrayList<TroopEffectInfo>();
		for (ArmPropInfoClient apic : propList) {
			for (ArmEffectClient aec : apic.getEnhanceList()) {
				TroopEffectInfo tei = new TroopEffectInfo();
				tei.setArmid(aec.getArmId());
				tei.setAttr(aec.getId());
				tei.setValue(aec.getEnhanceValue());
				effectInfos.add(tei);
			}
		}
		info.setInfosList(effectInfos);
		return info;
	}
}
