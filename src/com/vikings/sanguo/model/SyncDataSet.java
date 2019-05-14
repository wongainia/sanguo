package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.ActInfos;
import com.vikings.sanguo.protos.ArmPropInfos;

/**
 * 
 * @author Brad.Chen
 * 
 */

public class SyncDataSet {

	public int version;

	public int notifyVer;

	public int battleVer;

	public SyncData<UserAccountClient> userInfo;

	public SyncData<Integer>[] friendInfos;

	public SyncData<ItemBag>[] bagInfos;

	public SyncData<EquipmentInfoClient>[] equipmentInfos;

	public SyncData<Short>[] skillIds;

	public SyncData<QuestInfoClient>[] questInfos;

	public SyncData<ManorInfoClient> manorInfoClient;

	public SyncData<BlackListInfoClient>[] blackListInfoClients;

	public SyncData<LordInfoClient> lordInfoClient;

	public SyncData<LordFiefInfoClient>[] lordFiefInfos;

	public SyncData<Long>[] battleIds;

	public SyncData<Integer> guildId;

	public SyncData<HeroInfoClient>[] heroInfos;

	public ActInfos actInfos;
	public ActInfos dynamicActInfos;

	public ArmPropInfos armPropInfos;

	public List<Integer> getFriendList() {
		List<Integer> ls = new ArrayList<Integer>();
		if (friendInfos != null) {
			for (int i = 0; i < friendInfos.length; i++) {
				ls.add(friendInfos[i].getData());
			}
		}
		return ls;
	}

}
