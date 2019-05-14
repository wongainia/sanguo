package com.vikings.sanguo.model;

import android.util.Log;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class ShopData implements Comparable<ShopData> {
	public static final byte TYPE_TOOL = 1;// 道具
	public static final byte TYPE_EQUIPMENT = 2;// 装备

	// 与viewTab属性对应
	public static final byte TAB1 = 1;// 热卖
	public static final byte TAB2 = 2;// 道具
	public static final byte TAB3 = 3;// 装备

	private byte type;// 1、道具；2、装备
	private int id; // 类型为1时，该字段为ItemId； 类型为2时，该字段为EquipmentInit中的方案号
	private byte viewTab;// 1、热卖；2、道具；3、装备
	private int sequence;// 排序
	private int extraExp; // 购买加经验
	private int buyVipLv; // VIP等级购买限制
	private int showVipLv; // VIP等级显示限制
	private int limitBuyAmount;// 限购数量

	private Item item;
	private EquipmentInit equipmentInit;// 装备初始化定义
	private PropEquipment equipment;// 装备定义配置
	private EquipmentDesc equipmentDesc;

	public int getLimitBuyAmount() {
		return limitBuyAmount;
	}

	public void setLimitBuyAmount(int limitBuyAmount) {
		this.limitBuyAmount = limitBuyAmount;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public boolean isTool() {
		return this.type == TYPE_TOOL;
	}

	public boolean isEquipment() {
		return this.type == TYPE_EQUIPMENT;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public byte getViewTab() {
		return viewTab;
	}

	public void setViewTab(byte viewTab) {
		this.viewTab = viewTab;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getExtraExp() {
		return extraExp;
	}

	public void setExtraExp(int extraExp) {
		this.extraExp = extraExp;
	}

	public void setBuyVipLv(int buyVipLv) {
		this.buyVipLv = buyVipLv;
	}

	public int getBuyVipLv() {
		return buyVipLv;
	}

	public void setShowVipLv(int showVipLv) {
		this.showVipLv = showVipLv;
	}

	public int getShowVipLv() {
		return showVipLv;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	public PropEquipment getEquipment() {
		return equipment;
	}

	public void setEquipment(PropEquipment equipment) {
		this.equipment = equipment;
	}

	public EquipmentInit getEquipmentInit() {
		return equipmentInit;
	}

	public void setEquipmentInit(EquipmentInit equipmentInit) {
		this.equipmentInit = equipmentInit;
	}

	public EquipmentDesc getEquipmentDesc() {
		return equipmentDesc;
	}

	public void setEquipmentDesc(EquipmentDesc equipmentDesc) {
		this.equipmentDesc = equipmentDesc;
	}

	// 是不是限购产品
	public boolean isLimitBuy() {
		return limitBuyAmount != 0;
	}

	public static ShopData fromString(String csv) {
		ShopData d = new ShopData();
		StringBuilder buf = new StringBuilder(csv);
		d.setType(StringUtil.removeCsvByte(buf));
		d.setId(StringUtil.removeCsvInt(buf));
		d.setViewTab(StringUtil.removeCsvByte(buf));
		d.setSequence(StringUtil.removeCsvInt(buf));
		d.setExtraExp(StringUtil.removeCsvInt(buf));
		d.setBuyVipLv(StringUtil.removeCsvInt(buf));
		d.setShowVipLv(StringUtil.removeCsvInt(buf));
		d.setLimitBuyAmount(StringUtil.removeCsvInt(buf));

		try {
			if (d.isTool()) {
				d.setItem((Item) CacheMgr.itemCache.get(d.getId()));
			} else if (d.isEquipment()) {
				EquipmentInit equipmentInit = (EquipmentInit) CacheMgr.equipmentInitCache
						.get(d.getId());
				d.setEquipmentInit(equipmentInit);
				d.setEquipment((PropEquipment) CacheMgr.propEquipmentCache
						.get(equipmentInit.getId()));
				d.setEquipmentDesc((EquipmentDesc) CacheMgr.equipmentDescCache
						.get(equipmentInit.getInitQuality()));
			}
		} catch (GameException e) {
			Log.e("ShopData", e.getMessage());
		}
		return d;
	}

	@Override
	public int compareTo(ShopData another) {
		if (type == another.type)
			return sequence - another.sequence;
		else
			return type - another.type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShopData other = (ShopData) obj;
		if (type != other.getType() || id != other.getId())
			return false;
		return true;
	}

	// 折扣百分比
	public int getDiscount() {
		int discount = Constants.NO_DISCOUNT;
		if (isTool()) {
			// int itemcount=getItem().getDiscount();//手动折扣率
			// if(0==itemcount){
			// itemcount=discount;
			// }
			int itemId = getItem().getId();
			int dateCount = CacheMgr.shopTimeDiscountCache.getDiscount(
					TYPE_TOOL, itemId);// 指定日期折扣率(不在打折时间返回100)
			ShopDailyDiscount sdd = CacheMgr.shopDailyDiscountCache
					.getDailyDiscount(TYPE_TOOL, itemId);// 自动每日折扣率
			if (null != sdd) {// 三重折上折
				discount = (int) ((dateCount / 100f)
						* (sdd.getDiscount() / 100f) * 10);
			} else {// 双重折上折
				discount = (int) ((dateCount / 100f) * 10);
			}

		} else if (isEquipment()) {
			int planId = getEquipmentInit().getScheme();
			int equipmentCount = CacheMgr.shopTimeDiscountCache.getDiscount(
					TYPE_EQUIPMENT, planId);// 指定日期折扣率(不在打折时间返回100)
			ShopDailyDiscount sdd = CacheMgr.shopDailyDiscountCache
					.getDailyDiscount(TYPE_EQUIPMENT, planId);// 自动每日折扣率
			if (null != sdd) {// 三重折上折
				discount = (int) ((equipmentCount / 100f)
						* (sdd.getDiscount() / 100f) * 10);
			} else {// 双重折上折
				discount = (int) ((equipmentCount / 100f) * 10);
			}
		}
		discount = discount * 10;// 换算成百分比值
		return discount;
	}

	// 折扣时间
	public String getDiscountCountDown(byte type) {
		if (TYPE_TOOL == type) {// 道具
			int itemId = getItem().getId();
			int discount = CacheMgr.shopTimeDiscountCache.getDiscount(type,
					itemId);
			if (Constants.NO_DISCOUNT != discount)
				return DateUtil.formatTime(CacheMgr.shopTimeDiscountCache
						.getCountDownSecond(type, itemId));

			ShopDailyDiscount sdd = CacheMgr.shopDailyDiscountCache
					.getDailyDiscount(type, itemId);
			if (null != sdd && Constants.NO_DISCOUNT != sdd.getDiscount())
				return DateUtil.formatTime(Constants.DAY
						- DateUtil.getTodaySS());
		} else if (TYPE_EQUIPMENT == type) {// 装备
			int planId = getEquipmentInit().getScheme();
			int equipmentCount = CacheMgr.shopTimeDiscountCache.getDiscount(
					type, planId);// 指定日期折扣率(不在打折时间返回100)
			if (Constants.NO_DISCOUNT != equipmentCount)
				return DateUtil.formatTime(CacheMgr.shopTimeDiscountCache
						.getCountDownSecond(type, planId));

			ShopDailyDiscount sdd = CacheMgr.shopDailyDiscountCache
					.getDailyDiscount(type, planId);
			if (null != sdd && Constants.NO_DISCOUNT != sdd.getDiscount())
				return DateUtil.formatTime(Constants.DAY
						- DateUtil.getTodaySS());

		}

		return "";
	}
}
