package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BlackListInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.LordInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.SyncCtrl;
import com.vikings.sanguo.model.SyncData;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.protos.BagItemInfos;
import com.vikings.sanguo.protos.BaseBagItemInfo;
import com.vikings.sanguo.protos.BattleIdInfos;
import com.vikings.sanguo.protos.BlacklistInfos;
import com.vikings.sanguo.protos.BriefUserInfo;
import com.vikings.sanguo.protos.DataCtrl;
import com.vikings.sanguo.protos.EquipmentInfos;
import com.vikings.sanguo.protos.ExBagItemInfo;
import com.vikings.sanguo.protos.ExBattleIdInfo;
import com.vikings.sanguo.protos.ExBlacklistInfo;
import com.vikings.sanguo.protos.ExEquipmentInfo;
import com.vikings.sanguo.protos.ExFriendInfo;
import com.vikings.sanguo.protos.ExHeroInfo;
import com.vikings.sanguo.protos.ExLordFiefInfo;
import com.vikings.sanguo.protos.ExLordInfo;
import com.vikings.sanguo.protos.ExManorInfo;
import com.vikings.sanguo.protos.ExQuestInfo;
import com.vikings.sanguo.protos.ExUserGuildInfo;
import com.vikings.sanguo.protos.FriendInfos;
import com.vikings.sanguo.protos.HeroInfos;
import com.vikings.sanguo.protos.LordFiefInfos;
import com.vikings.sanguo.protos.LordInfo;
import com.vikings.sanguo.protos.QuestInfos;
import com.vikings.sanguo.protos.RichUserInfo;

public class Decoder {

	// 数据同步解析
	public static SyncDataSet decodeRichUserInfo(RichUserInfo info,
			SyncDataSet dataSet) throws GameException {
		dataSet.version = info.getCtrl().getVer();

		dataSet.userInfo = new SyncData<UserAccountClient>();
		dataSet.userInfo.setData(UserAccountClient.covert(info));

		if (info.hasFriendInfos())
			decodeFriendInfos(info.getFriendInfos(), dataSet);

		if (info.hasBlacklistInfos())
			decodeBlackListInfos(info.getBlacklistInfos(), dataSet);

		if (info.hasManorInfo()) {
			decodeManorInfo(info.getManorInfo(), dataSet);
		}

		if (info.hasBagItemInfos())
			decodeBagItemInfos(info.getBagItemInfos(), dataSet);

		if (info.hasEquipmentInfos())
			decodeEquipmentInfos(info.getEquipmentInfos(), dataSet);

		if (info.hasQuestInfos())
			decodeQuestInfos(info.getQuestInfos(), dataSet);

		if (info.hasLordInfo())
			decodLordInfo(info.getLordInfo(), dataSet);

		if (info.hasLordFiefInfos())
			decodeLordFiefInfos(info.getLordFiefInfos(), dataSet);

		if (info.hasBattleidInfos()) {
			if (info.getBattleidInfos().hasCtrl())
				dataSet.battleVer = info.getBattleidInfos().getCtrl().getVer();
			if (info.getBattleidInfos().hasInfos())
				decodeBattleIdInfos(info.getBattleidInfos(), dataSet);
		}

		if (info.hasGuildInfo()) {
			decodeGuildInfo(info.getGuildInfo(), dataSet);
		}

		if (info.hasHeroInfos()) {
			decodeHeroInfos(info.getHeroInfos(), dataSet);
		}

		if (info.hasActInfos())
			dataSet.actInfos = info.getActInfos();

		if (info.hasDynamicActInfos())
			dataSet.dynamicActInfos = info.getDynamicActInfos();

		if (info.hasArmPropInfos())
			dataSet.armPropInfos = info.getArmPropInfos();

		return dataSet;
	}

	private static void decodeManorInfo(ExManorInfo exManorInfo,
			SyncDataSet dataSet) throws GameException {

		SyncData<ManorInfoClient> syncData = new SyncData<ManorInfoClient>();
		syncData.setCtrl(decodeCtrl(exManorInfo.getCtrl()));
		ManorInfoClient mic = ManorInfoClient.convert(exManorInfo.getInfo());
		syncData.setData(mic);

		dataSet.manorInfoClient = syncData;
	}

	@SuppressWarnings("unchecked")
	private static void decodeHeroInfos(HeroInfos heroInfos, SyncDataSet dataSet)
			throws GameException {
		List<ExHeroInfo> list = heroInfos.getInfosList();
		SyncData<HeroInfoClient>[] datas = new SyncData[list.size()];
		for (int i = 0; i < list.size(); i++) {
			SyncData<HeroInfoClient> data = new SyncData<HeroInfoClient>();
			ExHeroInfo exInfo = list.get(i);
			data.setCtrl(decodeCtrl(exInfo.getCtrl()));
			data.setData(HeroInfoClient.convert(exInfo.getInfo()));
			datas[i] = data;
		}
		dataSet.heroInfos = datas;
	}

	private static void decodeGuildInfo(ExUserGuildInfo exUserGuildInfo,
			SyncDataSet dataSet) {
		SyncData<Integer> syncData = new SyncData<Integer>();
		syncData.setCtrl(decodeCtrl(exUserGuildInfo.getCtrl()));
		syncData.setData(exUserGuildInfo.getInfo().getBi().getGuildid());
		dataSet.guildId = syncData;
	}

	@SuppressWarnings("unchecked")
	private static void decodeBattleIdInfos(BattleIdInfos battleidInfos,
			SyncDataSet dataSet) {
		List<ExBattleIdInfo> list = battleidInfos.getInfosList();
		SyncData<Long>[] data = new SyncData[list.size()];
		for (int i = 0; i < list.size(); i++) {
			SyncData<Long> syncData = new SyncData<Long>();
			ExBattleIdInfo exBattleIdInfo = list.get(i);
			syncData.setCtrl(decodeCtrl(exBattleIdInfo.getCtrl()));
			syncData.setData(exBattleIdInfo.getInfo().getBi().getBattleid());
			data[i] = syncData;
		}
		dataSet.battleIds = data;
	}

	private static void decodLordInfo(ExLordInfo exLordInfo, SyncDataSet dataSet)
			throws GameException {
		LordInfo lordInfo = exLordInfo.getInfo();
		LordInfoClient lic = LordInfoClient.convert(lordInfo);
		SyncData<LordInfoClient> syncData = new SyncData<LordInfoClient>();
		syncData.setCtrl(decodeCtrl(exLordInfo.getCtrl()));
		syncData.setData(lic);
		dataSet.lordInfoClient = syncData;
	}

	@SuppressWarnings("unchecked")
	public static void decodeLordFiefInfos(LordFiefInfos lordFiefInfos,
			SyncDataSet dataSet) throws GameException {
		if (lordFiefInfos == null)
			return;
		List<ExLordFiefInfo> list = lordFiefInfos.getInfosList();
		SyncData<LordFiefInfoClient>[] data = new SyncData[list.size()];
		for (int i = 0; i < list.size(); i++) {
			SyncData<LordFiefInfoClient> syncData = new SyncData<LordFiefInfoClient>();
			ExLordFiefInfo exInfo = list.get(i);
			syncData.setCtrl(decodeCtrl(exInfo.getCtrl()));
			syncData.setData(LordFiefInfoClient.convert(exInfo.getInfo()));
			data[i] = syncData;
		}
		dataSet.lordFiefInfos = data;
	}

	@SuppressWarnings("unchecked")
	private static void decodeQuestInfos(QuestInfos questInfos,
			SyncDataSet dataSet) throws GameException {
		List<ExQuestInfo> list = questInfos.getInfosList();
		SyncData<QuestInfoClient>[] data = new SyncData[list.size()];
		for (int i = 0; i < list.size(); i++) {
			SyncData<QuestInfoClient> syncData = new SyncData<QuestInfoClient>();
			ExQuestInfo exQuestInfo = list.get(i);
			syncData.setCtrl(decodeCtrl(exQuestInfo.getCtrl()));
			QuestInfoClient qi = QuestInfoClient.convert(exQuestInfo.getInfo());
			syncData.setData(qi);
			data[i] = syncData;
		}
		dataSet.questInfos = data;
	}

	@SuppressWarnings("unchecked")
	private static void decodeBagItemInfos(BagItemInfos bagItemInfos,
			SyncDataSet dataSet) {
		List<ExBagItemInfo> list = bagItemInfos.getInfosList();
		SyncData<ItemBag>[] data = new SyncData[list.size()];
		for (int i = 0; i < list.size(); i++) {
			SyncData<ItemBag> syncData = new SyncData<ItemBag>();
			ExBagItemInfo exBagItemInfo = list.get(i);
			syncData.setCtrl(decodeCtrl(exBagItemInfo.getCtrl()));
			BaseBagItemInfo bi = exBagItemInfo.getInfo().getBi();
			ItemBag itemBag = new ItemBag();
			itemBag.setId(bi.getId());
			itemBag.setNew(true);
			itemBag.setLaidEesOn(false);
			itemBag.setItemId(bi.getItemid().intValue());
			itemBag.setCount(bi.getAmount());
			syncData.setData(itemBag);
			data[i] = syncData;
		}
		dataSet.bagInfos = data;
	}

	@SuppressWarnings("unchecked")
	private static void decodeEquipmentInfos(EquipmentInfos equipmentInfos,
			SyncDataSet dataSet) throws GameException {
		List<ExEquipmentInfo> list = equipmentInfos.getInfosList();
		SyncData<EquipmentInfoClient>[] data = new SyncData[list.size()];
		for (int i = 0; i < list.size(); i++) {
			SyncData<EquipmentInfoClient> syncData = new SyncData<EquipmentInfoClient>();
			ExEquipmentInfo exEquipmentInfo = list.get(i);
			syncData.setCtrl(decodeCtrl(exEquipmentInfo.getCtrl()));
			EquipmentInfoClient eic = EquipmentInfoClient
					.convert(exEquipmentInfo.getInfo());
			syncData.setData(eic);
			data[i] = syncData;
		}
		dataSet.equipmentInfos = data;
	}

	@SuppressWarnings("unchecked")
	private static void decodeBlackListInfos(BlacklistInfos blacklistInfos,
			SyncDataSet dataSet) {
		List<ExBlacklistInfo> list = blacklistInfos.getInfosList();
		SyncData<BlackListInfoClient>[] data = new SyncData[list.size()];
		for (int i = 0; i < list.size(); i++) {
			SyncData<BlackListInfoClient> syncData = new SyncData<BlackListInfoClient>();
			ExBlacklistInfo exBlacklistInfo = list.get(i);
			syncData.setCtrl(decodeCtrl(exBlacklistInfo.getCtrl()));
			BlackListInfoClient blic = new BlackListInfoClient();
			blic.setBli(exBlacklistInfo.getInfo());
			syncData.setData(blic);
			data[i] = syncData;
		}
		dataSet.blackListInfoClients = data;
	}

	@SuppressWarnings("unchecked")
	private static void decodeFriendInfos(FriendInfos friendInfos,
			SyncDataSet dataSet) {
		List<ExFriendInfo> list = friendInfos.getInfosList();
		SyncData<Integer>[] data = new SyncData[list.size()];
		for (int i = 0; i < list.size(); i++) {
			SyncData<Integer> syncData = new SyncData<Integer>();
			ExFriendInfo exFriendInfo = list.get(i);
			syncData.setCtrl(decodeCtrl(exFriendInfo.getCtrl()));
			syncData.setData(exFriendInfo.getInfo().getBi().getUserid());
			data[i] = syncData;
		}
		dataSet.friendInfos = data;
	}

	private static SyncCtrl decodeCtrl(DataCtrl dataCtrl) {
		SyncCtrl ctrl = new SyncCtrl();
		ctrl.setOp((byte) dataCtrl.getOp().intValue());
		ctrl.setVer(dataCtrl.getVer());
		return ctrl;
	}

	public static List<BriefUserInfoClient> decodeBriefUserInfos(
			List<BriefUserInfo> infosList) {
		List<BriefUserInfoClient> users = new ArrayList<BriefUserInfoClient>();
		for (BriefUserInfo info : infosList) {
			users.add(decodeBriefUserInfo(info));
		}
		return users;
	}

	private static BriefUserInfoClient decodeBriefUserInfo(BriefUserInfo info) {
		return new BriefUserInfoClient(info);
	}
}
