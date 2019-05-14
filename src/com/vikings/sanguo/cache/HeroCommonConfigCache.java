package com.vikings.sanguo.cache;

import com.vikings.sanguo.utils.StringUtil;

public class HeroCommonConfigCache extends FileCache {
	private int costRecover100Stamina;// 恢复100体力花费
	private int costStamina;// 出征消耗的体力数量
	private int maxLevelPVE; // PVE经验获得最大等级
	private int singleRefreshHeroPrice; // 盲刷功勋数量
	private int groupRefreshHeroPrice;// 将领翻牌元宝数量
	private int increasePriceRate; // 将领恢复体力涨幅
	private int maxIncreaseCount; // 将领恢复体力价格上限
	private int heroStrengthenAddByItem;// 强化道具增加统率值
	private int upgradeByToolPrice;// 道具升级等级单价(*将领当前等级*道具数量——金币）
	private int expPrice;// 1元宝=?经验(一键升级)
	private int maxStamina;// 将领体力值上限
	private int favourCost;//宠幸消耗元宝
	private int favourTime;//宠幸时效
	private int heroEvolveDiscount;//将领进化补齐将魂元宝折扣

	private static final String FILE_NAME = "hero_common_config.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		costRecover100Stamina = StringUtil.removeCsvInt(buf);
		costStamina = StringUtil.removeCsvInt(buf);
		maxLevelPVE = StringUtil.removeCsvInt(buf);
		singleRefreshHeroPrice = StringUtil.removeCsvInt(buf);
		groupRefreshHeroPrice = StringUtil.removeCsvInt(buf);
		StringUtil.removeCsv(buf);
		increasePriceRate = StringUtil.removeCsvInt(buf);
		maxIncreaseCount = StringUtil.removeCsvInt(buf);
		StringUtil.removeCsv(buf);
		heroStrengthenAddByItem = StringUtil.removeCsvInt(buf);
		upgradeByToolPrice = StringUtil.removeCsvInt(buf);
		expPrice = StringUtil.removeCsvInt(buf);
		maxStamina = StringUtil.removeCsvInt(buf);
		favourCost = StringUtil.removeCsvInt(buf);
		favourTime = StringUtil.removeCsvInt(buf);
		heroEvolveDiscount = StringUtil.removeCsvInt(buf);
		return line;
	}

	public int getCostRecover100Stamina() {
		return costRecover100Stamina;
	}

	public int getCostStamina() {
		return costStamina;
	}

	public int getMaxLevelPVE() {
		return maxLevelPVE;
	}

	public int getIncreasePriceRate() {
		return increasePriceRate;
	}

	public int getMaxIncreaseCount() {
		return maxIncreaseCount;
	}

	public int getSingleRefreshHeroPrice() {
		return singleRefreshHeroPrice;
	}

	public int getGroupRefreshHeroPrice() {
		return groupRefreshHeroPrice;
	}

	public int getHeroStrengthenAddByItem() {
		return heroStrengthenAddByItem;
	}

	public int getUpgradeByToolPrice() {
		return upgradeByToolPrice;
	}

	public int getExpPrice() {
		return expPrice;
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public int getPatronizeCost() {
		return favourCost;
	}

	public int getPatronizeTime() {
		return favourTime;
	}

	public int getHeroEvolveDiscount() {
		return heroEvolveDiscount;
	}
	
	
}
