package com.vikings.sanguo.model;

import java.io.Serializable;

import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ReturnEffectInfo;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 玩家回报信息
 * 
 * @author Brad.Chen
 * 
 */
public class ReturnEffectInfoClient implements Serializable {

	private static final long serialVersionUID = -4900400134579804745L;

	// eSelfMoney = 1, 自己金钱修改
	// eSelfExp, 自己经验修改
	// eSelfRegard, 自己关注修改
	// eOtherMoney, 玩家金钱修改
	// eOtherExp, 玩家经验修改
	// eOtherRegard,玩家关注修改
	// eSelfItem, 自己物品修改
	// eOtherItem, 玩家物品修改
	// eRobMoney, 打劫
	// pit ePitDel = 10, 矿井销毁
	// ePitDuration, 矿井挖矿时间
	// ePitYield,矿井产量
	// ePitRemoveBuff = 15, 解除buff状态
	// eSelfFund = 16, 自己元宝修改
	// eOtherFund, 玩家元宝修改
	// eStealItem, 盗窃物品
	// eTargetProtectTimes = 21, 玩家保护次数修改
	// eTargetProtectDuration,玩家保护次数修改
	// eTargetStealSeed, 抢玩家一个种子
	// eTargetStealFruit, 抢玩家一组果实
	// eTargetSellFruit, 强卖玩家一组果实
	// eTargetFarmRecover 复活玩家一块农田
	// eTargetFarmActionExp, 更改玩家一块农场上可捡取的金钱
	// eSelfRandomMoney, 自己随机金钱更改
	// eTargetRandomMoney, 目标玩家随机金钱更改
	// eSelfBuilding, 自己建筑修改(针对建筑)
	// eOtherBuilding, 玩家建筑修改(针对建筑)
	// eSelfFarmStealProtect, 自己的农田防备偷窃功能
	// eSelfQuestAdd, 给自己增加对应任务
	// eSelfCredit, 自己崇拜值修改
	// eTargetCredit, 目标崇拜值修改
	// eSelfManorTroopAdd, 自己庄园兵力增加
	// eSelfHero, 自己增加将领

	private byte effectId;

	/**
	 * 改变值
	 */
	private long value;

	public byte getEffectId() {
		return effectId;
	}

	public void setEffectId(byte effectId) {
		this.effectId = effectId;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public int[] getIntValue() {
		byte[] buf = new byte[8];
		BytesUtil.putLong(buf, value, 0);
		int[] rs = new int[2];
		rs[0] = BytesUtil.getInt(buf, 0);
		rs[1] = BytesUtil.getInt(buf, 4);
		return rs;
	}

	public String getChgItemName() {
		int[] v = getIntValue();
		int itemId = v[0];
		int count = v[1];
		if (count == 0)
			return "";
		Item item;
		try {
			item = (Item) CacheMgr.itemCache.get(itemId);
		} catch (GameException e) {
			return "";
		}
		return item.getName();
	}

	private String itemDesc(String name) {
		if (name == null)
			name = "";
		int[] v = getIntValue();
		int itemId = v[0];
		int count = v[1];
		if (count == 0)
			return "";
		Item item;
		try {
			item = (Item) CacheMgr.itemCache.get(itemId);
		} catch (GameException e) {
			return "";
		}
		if (count > 0)
			return name + Config.getController().getString(R.string.gain) + "#"
					+ item.getImage() + "#X" + count;
		else
			return name + Config.getController().getString(R.string.expend)
					+ "#" + item.getImage() + "#X" + count;
	}

	private String setItemDesc(String desc) {
		if (StringUtil.isNull(desc))
			return desc;
		int[] v = getIntValue();
		int count = Math.abs(v[0]);
		int itemId = v[1];
		Item item;
		try {
			item = (Item) CacheMgr.itemCache.get(itemId);
		} catch (GameException e) {
			return desc;
		}
		desc = desc.replaceAll("<number>", String.valueOf(count));
		desc = desc.replaceAll("<item>",
				StringUtil.color(item.getName(), "red"));
		return desc;
	}

	/**
	 * 果园被破坏或复活，果园通过种子id取得
	 * 
	 * @param desc
	 * @return
	 */
	private String setFarmDesc(String desc) {
		if (StringUtil.isNull(desc))
			return desc;
		int[] v = getIntValue();
		int itemId = v[0];
		Item item;
		try {
			item = (Item) CacheMgr.itemCache.get(itemId);
		} catch (GameException e) {
			return desc;
		}
		desc = desc.replaceAll("<item>",
				StringUtil.color(item.getName().replace("种子", "园"), "red"));
		return desc;
	}

	private String setHouseDesc(String desc) {
		if (StringUtil.isNull(desc))
			return desc;
		try {
			BuildingProp b = (BuildingProp) CacheMgr.buildingPropCache
					.get(value);
			desc = desc.replaceAll("<house>", b.getBuildingName());
		} catch (GameException e) {
			Log.e("ReturnInfo", e.getMessage(), e);
			return desc;
		}
		return desc;
	}

	/**
	 * 效果数值描述
	 * 
	 * @param fromUser
	 * @return
	 */
	public String toDesc(String fromUser) {
		switch (effectId) {
		// case 1:
		// return StringUtil.color(fromUser, "red") + "金钱#money#" +
		// StringUtil.value(value);
		// case 2:
		// return StringUtil.color(fromUser, "red") + "经验" +
		// StringUtil.value(value);
		// case 3:
		// return StringUtil.color(fromUser, "red") + "关注#regards#" +
		// StringUtil.value(value);
		case 4:
			return Config.getController().getString(R.string.money) + "#money#"
					+ StringUtil.value(value);
		case 5:
			return Config.getController().getString(R.string.exp)
					+ StringUtil.value(value);
		case 6:
			return Config.getController().getString(R.string.regard)
					+ "#regards#" + StringUtil.value(value);
		case 7:
		case 8:
			return itemDesc(fromUser);
		case 17:
			return Config.getController().getString(R.string.funds) + "#rmb#"
					+ StringUtil.value(value);
			// case 23:
			// return "关注#regards#" + StringUtil.value(value);
			// case 24:
			// return "关注#regards#" + StringUtil.value(value);
			// case 25:
			// return "关注#regards#" + StringUtil.value(value);
			// case 26:
			// return "关注#regards#" + StringUtil.value(value);
		default:
			return "";
		}
	}

	/**
	 * 替换技能成功描述
	 * 
	 * @param desc
	 */
	public String setDesc(String desc) {
		if (StringUtil.isNull(desc))
			return desc;
		switch (effectId) {
		case 1:
			return desc.replaceAll("<money>", StringUtil.abs(value));
		case 2:
			return desc.replaceAll("<exp>", StringUtil.abs(value));
		case 3:
			return desc.replaceAll("<regards>", StringUtil.abs(value));
		case 4:
			return desc.replaceAll("<money>", StringUtil.abs(value));
		case 5:
			return desc.replaceAll("<exp>", StringUtil.abs(value));
		case 6:
			return desc.replaceAll("<regards>", StringUtil.abs(value));
		case 7:
		case 8:
			return setItemDesc(desc);
		case 10:
		case 26:
			return setFarmDesc(desc);
		case 20:
			return setHouseDesc(desc);
		default:
			return desc;
		}
	}

	public Item getItem() {
		int itemId = getIntValue()[1];
		Item item = null;
		try {
			item = (Item) CacheMgr.itemCache.get(itemId);
		} catch (GameException e) {
		}
		return item;
	}

	public BuildingProp getBuilding() {
		int buildingId = getIntValue()[1];
		BuildingProp bui = null;
		try {
			bui = (BuildingProp) CacheMgr.buildingPropCache.get(buildingId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bui;
	}

	// 将probuf对象转成客户端对象
	public static ReturnEffectInfoClient convert(ReturnEffectInfo rei) {
		if (rei.getField() == 0)
			return null;
		ReturnEffectInfoClient ri = new ReturnEffectInfoClient();
		ri.setEffectId((byte) rei.getField().intValue());
		ri.setValue(rei.getValue());
		return ri;
	}
}
