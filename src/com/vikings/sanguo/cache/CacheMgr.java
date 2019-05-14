package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Mapping;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArenaUserRankInfoClient;
import com.vikings.sanguo.model.BaseBattleInfoClient;
import com.vikings.sanguo.model.BattleHeroInfoClient;
import com.vikings.sanguo.model.BattleHotInfoClient;
import com.vikings.sanguo.model.BattleLogClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.EquipmentForgeEffect;
import com.vikings.sanguo.model.GuildSearchInfoClient;
import com.vikings.sanguo.model.HeroFavourWordCache;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.model.MoveTroopInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.OtherUserBattleIdInfoClient;
import com.vikings.sanguo.model.PlayerWantedInfoClient;
import com.vikings.sanguo.model.PropBoxCache;
import com.vikings.sanguo.model.PropHeroFavourCache;
import com.vikings.sanguo.model.Quest;
import com.vikings.sanguo.model.QuestEffect;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.model.ShopCache;
import com.vikings.sanguo.model.StatInfo;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.protos.PlayerWantedInfo;
import com.vikings.sanguo.thread.NullImgCallBack;
import com.vikings.sanguo.thread.SoundLoader;
import com.vikings.sanguo.utils.ListUtil;

/**
 * 
 * 缓存
 * 
 * user 缓存内存
 * 
 * 
 * item/oreprop/event 内存--保存本地
 * 
 * @author Brad.Chen
 * 
 */
@SuppressWarnings("unchecked")
public class CacheMgr {

	public static DictCache dictCache;
	public static TipsCache tipsCache;
	public static ErrorCodeCache errorCodeCache;
	public static AddrCache addrCache;
	public static LevelupDescCache levelUpDescCache;
	public static GambleCache gambleCache;
	public static WorldLevelPropCache worldLevelPropCache;

	// 物品

	public static ItemCache itemCache;
	public static ShopCache shop;
	public static PropBoxCache propBoxCache;// 箱子一键开启

	// 玩家缓存
	public static UserCache userCache;
	public static PropUserCache propUserCache;
	public static NpcClientPropCache npcCache;
	public static UserNameRandomCache userNameRandomCache;
	public static UserCommonCache userCommonCache;

	// 家族brief缓存
	public static GuildPropCache guildPropCache;
	public static GuildLevelUpMaterialCache guildLevelUpMaterialCache;
	public static GuildCommonConfigCache guildCommonConfigCache;
	public static BriefGuildInfoClientCache bgicCache;

	// 任务
	public static QuestConditionCache questConditonCache;
	public static QuestEffectCache questEffectCache;
	public static QuestCache questCache;

	// 建筑
	public static BuildingPropCache buildingPropCache;
	public static BuildingTypeCache buildingTypeCache;
	public static BuildingBuyCostCache buildingBuyCostCache;
	public static BuildingRequirementCache buildingRequireMentCache;
	public static BuildingEffectCache buildingEffectCache;
	public static BuildingStatusCache buildingStatusCache;
	public static BuildingStoreCache buildingStoreCache;

	// 新手教程
	public static TrainingRewardsCache trainingRewardsCache;

	// 领地及战斗
	public static FiefPropCache fiefPropCache;

	// 士兵
	public static TroopPropCache troopPropCache;
	public static PokerPriceCache pokerPriceCache;
	public static PokerConfigCache pokerConfigCache;
	public static ManorDraftCache manorDraftCache;
	public static ManorDraftResourceCache manorDraftResourceCache;
	public static FiefScaleCache fiefScaleCache;
	public static CityGeoInfoCache cityGeoInfoCache;
	public static HolyPropCfgCache holyPropCfgCache;
	public static HolyPropCache holyPropCache;
	public static HolyTroopCache holyTroopCache;
	public static PropTroopNameCache propTroopNameCache;
	public static PropTroopDescCache propTroopDescCache;
	public static PlunderCache plunderCache;
	public static SiteSpecialCache siteSpecialCache;
	public static PropFiefBlankCache propFiefBlankCache;

	// 将领相关
	public static HeroLevelUpCache heroLevelUpCache;
	public static HeroQualityCache heroQualityCache;
	public static HeroPropCache heroPropCache;
	public static HeroEnhanceCache heroEnhanceCache;
	public static HeroTroopNameCache heroTroopNameCache;
	public static BattleSkillCache battleSkillCache;
	public static BattleEffectCache battleEffectCache;
	public static BattleSkillEffectCache battleSkillEffectCache;
	public static EquipmentForgeEffectCache equipmentForgeEffectCache;
	public static BattleBuffEffectCache battleBuffEffectCache;
	public static HeroSkillUpgradeCache heroSkillUpgradeCache;
	public static HeroCommonConfigCache heroCommonConfigCache;
	public static HeroTypeCache heroTypeCache;
	public static HeroDevourExpCache heroDevourExpCache;
	public static HeroEvolveCache heroEvolveCache;
	public static BattleFiefCache battleFiefCache;
	public static ArenaRewardCache arenaRewardCache;
	public static ArenaTroopCache arenaTroopCache;
	public static HeroInitCache heroInitCache;
	public static HeroAbandonExpToItemCache heroAbandonExpToItemCache;
	public static PropHeroFavourCache propHeroFavourCache;// 宠幸使用道具
	public static HeroFavourWordCache heroFavourWordCache;// 宠幸英雄挑逗语句

	// 装备相关
	public static EquipmentDescCache equipmentDescCache;
	public static PropEquipmentCache propEquipmentCache;
	public static EquipmentTypeCache equipmentTypeCache;
	public static EquipmentInitCache equipmentInitCache;
	public static EquipmentEffectCache equipmentEffectCache;
	public static EquipmentLevelUpCache equipmentLevelUpCache;
	public static EquipmentForgeCache equipmentForgeCache;
	public static EquipmentCommonConfigCache equipmentCommonConfigCache;
	public static EquipmentInsertItemCache equipmentInsertItemCache;
	public static EquipmentInsertItemEffectCache equipmentInsertItemEffectCache;
	public static BattleSkillEquipmentCache battleSkillEquipmentCache;

	// 副本相关
	public static PropActCache actCache;
	public static PropActSpoilsCache actSpoilsCache;
	public static PropActTypeCache actTypeCache;
	public static PropCampaignCache campaignCache;
	public static PropCampaignBossCache campaignBossCache;
	public static PropCampaignModeCache campaignModeCache;
	public static PropCampaignSpoilsCache campaignSpoilsCache;
	public static CampaignTroopCache campaignTroopCache;
	public static TimeConditionCache timeConditionCache;

	// 血战相关
	public static BloodRewardCache bloodRewardCache;
	public static BloodRankRewardCache bloodRankRewardCache;
	public static BloodCommonCache bloodCommonCache;
	public static BloodTroopCache bloodTroopCache;

	// 地理相关
	public static ZoneCache zoneCache;
	public static CountryCache countryCache;
	public static BattleBgPropCache battleBgPropCache;

	// 运营相关
	// 轮盘
	public static PropRouletteCache propRouletteCache;
	public static PropRouletteCommonCache propRouletteCommonCache;
	public static PropRouletteItemCache propRouletteItemCache;

	// 士兵强化

	public static ArmEnhancePropCache armEnhancePropCache;
	public static ArmEnhanceCostCache armEnhanceCostCache;

	public static CheckInRewardsCache checkInRewardsCache;
	public static WeightCache weightCache;

	public static UserVipCache userVipCache;
	public static VipRewardsCache vipRewardsCache;

	public static HeroRecruitExchangeCache heroRecruitExchangeCache;

	public static BonusCache bonusCache;

	public static FirstRechargeRewardCache firstRechargeRewardCache;

	public static ManorLocationCache manorLocationCache;

	public static CampaignSpoilsAppendCache campaignSpoilsAppendCache;

	public static BattleBuffCache battleBuffCache;

	public static ShopDailyDiscountCache shopDailyDiscountCache;

	public static ShopTimeDiscountCache shopTimeDiscountCache;

	public static TimeTypeConditionCache timeTypeConditionCache;

	public static EventEntryCache eventEntryCache;

	public static UITextPropCache uiTextCache;

	public static HonorRankCache honorRankCache;

	public static RankPropCache rankPropCache;

	public static RechargeStateCache rechargeStateCache;
	public static ChargeCommonConfigCache chargeCommonConfigCache;
	public static DoubleChargeCache doubleChargeCache;

	public static PokerReinforceCache pokerReinforceCache;

	public static HolyCategoryCache holyCategoryCache;

	public static CurrencyBoxCache currencyBoxCache;

	public static ArenaNPCCache arenaNPCCache;

	public static ArenaResetTimeCache arenaResetTimeCache;

	public static ManorReviveCache manorReviveCache;

	public static BattleAnimEffectCache battleAnimEffect;

	public static BattleSkillComboCache battleSkillCombo;

	public static EventRewardsCache eventRewardsCache;

	public static PropStrongerCache propStrongerCache;

	public static HeroFavourCache heroFavourCache;

	public static HeroFavourConvertCache heroFavourConvertCache;

	public static void preInit() {
		try {
			dictCache = new DictCache();
			dictCache.init();

			tipsCache = new TipsCache();
			tipsCache.init();

			errorCodeCache = new ErrorCodeCache();
			errorCodeCache.checkLoad(false);

			countryCache = new CountryCache();
			countryCache.init();

		} catch (GameException e) {
			Log.e("CacheMgr", e.getMessage(), e);
		}
	}

	private static int cacheInitCount = 0;

	private static void initFileCache(FileCache f) throws GameException {
		f.init();
		cacheInitCount++;
		Config.getController()
				.getHome()
				.setPercentInThread(
						70 + (cacheInitCount * 20 / Mapping.cfgMapping.size()));
	}

	public static void init() throws GameException {
		cacheInitCount = 0;

		dictCache = new DictCache();
		initFileCache(dictCache);

		propUserCache = new PropUserCache();
		initFileCache(propUserCache);

		addrCache = AddrCache.getInstance();
		levelUpDescCache = new LevelupDescCache();
		initFileCache(levelUpDescCache);

		itemCache = new ItemCache();
		initFileCache(itemCache);

		gambleCache = new GambleCache();
		initFileCache(gambleCache);

		propRouletteCache = new PropRouletteCache();
		initFileCache(propRouletteCache);
		propRouletteCommonCache = new PropRouletteCommonCache();
		initFileCache(propRouletteCommonCache);
		propRouletteItemCache = new PropRouletteItemCache();
		initFileCache(propRouletteItemCache);

		worldLevelPropCache = new WorldLevelPropCache();
		initFileCache(worldLevelPropCache);

		propBoxCache = new PropBoxCache();// 箱子一键开启
		initFileCache(propBoxCache);

		userCache = new UserCache();
		npcCache = new NpcClientPropCache();
		initFileCache(npcCache);
		userCommonCache = new UserCommonCache();
		initFileCache(userCommonCache);

		guildPropCache = new GuildPropCache();
		initFileCache(guildPropCache);
		guildLevelUpMaterialCache = new GuildLevelUpMaterialCache();
		initFileCache(guildLevelUpMaterialCache);
		guildCommonConfigCache = new GuildCommonConfigCache();
		initFileCache(guildCommonConfigCache);
		bgicCache = new BriefGuildInfoClientCache();

		buildingPropCache = new BuildingPropCache();
		initFileCache(buildingPropCache);
		buildingTypeCache = new BuildingTypeCache();
		initFileCache(buildingTypeCache);
		buildingBuyCostCache = new BuildingBuyCostCache();
		initFileCache(buildingBuyCostCache);
		buildingRequireMentCache = new BuildingRequirementCache();
		initFileCache(buildingRequireMentCache);
		buildingEffectCache = new BuildingEffectCache();
		initFileCache(buildingEffectCache);
		buildingStatusCache = new BuildingStatusCache();
		initFileCache(buildingStatusCache);
		siteSpecialCache = new SiteSpecialCache();
		initFileCache(siteSpecialCache);
		propFiefBlankCache = new PropFiefBlankCache();
		initFileCache(propFiefBlankCache);

		fiefPropCache = new FiefPropCache();
		initFileCache(fiefPropCache);
		troopPropCache = new TroopPropCache();
		initFileCache(troopPropCache);
		pokerPriceCache = new PokerPriceCache();
		initFileCache(pokerPriceCache);
		pokerConfigCache = new PokerConfigCache();
		initFileCache(pokerConfigCache);
		manorDraftCache = new ManorDraftCache();
		initFileCache(manorDraftCache);
		manorDraftResourceCache = new ManorDraftResourceCache();
		initFileCache(manorDraftResourceCache);

		// fiefDraftCache = new FiefDraftCache();
		// fiefDraftCache);
		fiefScaleCache = new FiefScaleCache();
		initFileCache(fiefScaleCache);
		cityGeoInfoCache = new CityGeoInfoCache();
		initFileCache(cityGeoInfoCache);
		holyPropCfgCache = new HolyPropCfgCache();
		initFileCache(holyPropCfgCache);
		holyPropCache = new HolyPropCache();
		initFileCache(holyPropCache);
		holyTroopCache = new HolyTroopCache();
		initFileCache(holyTroopCache);
		propTroopNameCache = new PropTroopNameCache();
		initFileCache(propTroopNameCache);
		propTroopDescCache = new PropTroopDescCache();
		initFileCache(propTroopDescCache);
		plunderCache = new PlunderCache();
		initFileCache(plunderCache);

		heroLevelUpCache = new HeroLevelUpCache();
		initFileCache(heroLevelUpCache);
		heroQualityCache = new HeroQualityCache();
		initFileCache(heroQualityCache);
		heroPropCache = new HeroPropCache();
		initFileCache(heroPropCache);
		// provinceHeroCache = new ProvinceHeroCache();
		// initFileCache(provinceHeroCache);
		heroEnhanceCache = new HeroEnhanceCache();
		initFileCache(heroEnhanceCache);
		heroTroopNameCache = new HeroTroopNameCache();
		initFileCache(heroTroopNameCache);
		battleSkillCache = new BattleSkillCache();
		initFileCache(battleSkillCache);
		battleEffectCache = new BattleEffectCache();
		initFileCache(battleEffectCache);
		battleSkillEffectCache = new BattleSkillEffectCache();
		initFileCache(battleSkillEffectCache);
		equipmentForgeEffectCache = new EquipmentForgeEffectCache();
		initFileCache(equipmentForgeEffectCache);
		battleBuffEffectCache = new BattleBuffEffectCache();
		initFileCache(battleBuffEffectCache);
		heroSkillUpgradeCache = new HeroSkillUpgradeCache();
		initFileCache(heroSkillUpgradeCache);
		heroCommonConfigCache = new HeroCommonConfigCache();
		initFileCache(heroCommonConfigCache);
		heroTypeCache = new HeroTypeCache();
		initFileCache(heroTypeCache);
		heroDevourExpCache = new HeroDevourExpCache();
		initFileCache(heroDevourExpCache);
		heroEvolveCache = new HeroEvolveCache();
		initFileCache(heroEvolveCache);
		battleFiefCache = new BattleFiefCache();
		initFileCache(battleFiefCache);
		arenaRewardCache = new ArenaRewardCache();
		initFileCache(arenaRewardCache);
		arenaTroopCache = new ArenaTroopCache();
		initFileCache(arenaTroopCache);
		heroInitCache = new HeroInitCache();
		initFileCache(heroInitCache);
		heroAbandonExpToItemCache = new HeroAbandonExpToItemCache();
		initFileCache(heroAbandonExpToItemCache);
		propHeroFavourCache = new PropHeroFavourCache();// 宠幸使用道具
		initFileCache(propHeroFavourCache);
		heroFavourWordCache = new HeroFavourWordCache();// 宠幸挑逗语句
		initFileCache(heroFavourWordCache);

		equipmentDescCache = new EquipmentDescCache();
		initFileCache(equipmentDescCache);
		propEquipmentCache = new PropEquipmentCache();
		initFileCache(propEquipmentCache);
		equipmentTypeCache = new EquipmentTypeCache();
		initFileCache(equipmentTypeCache);
		equipmentInitCache = new EquipmentInitCache();
		initFileCache(equipmentInitCache);
		equipmentEffectCache = new EquipmentEffectCache();
		initFileCache(equipmentEffectCache);
		equipmentLevelUpCache = new EquipmentLevelUpCache();
		initFileCache(equipmentLevelUpCache);
		equipmentForgeCache = new EquipmentForgeCache();
		initFileCache(equipmentForgeCache);
		equipmentCommonConfigCache = new EquipmentCommonConfigCache();
		initFileCache(equipmentCommonConfigCache);
		equipmentInsertItemCache = new EquipmentInsertItemCache();
		initFileCache(equipmentInsertItemCache);
		equipmentInsertItemEffectCache = new EquipmentInsertItemEffectCache();
		initFileCache(equipmentInsertItemEffectCache);
		battleSkillEquipmentCache = new BattleSkillEquipmentCache();
		initFileCache(battleSkillEquipmentCache);

		bloodRewardCache = new BloodRewardCache();
		initFileCache(bloodRewardCache);
		bloodRankRewardCache = new BloodRankRewardCache();
		initFileCache(bloodRankRewardCache);
		bloodCommonCache = new BloodCommonCache();
		initFileCache(bloodCommonCache);
		bloodTroopCache = new BloodTroopCache();
		initFileCache(bloodTroopCache);

		actCache = new PropActCache();
		initFileCache(actCache);
		actSpoilsCache = new PropActSpoilsCache();
		initFileCache(actSpoilsCache);
		actTypeCache = new PropActTypeCache();
		initFileCache(actTypeCache);
		campaignCache = new PropCampaignCache();
		initFileCache(campaignCache);
		campaignBossCache = new PropCampaignBossCache();
		initFileCache(campaignBossCache);
		// campaignHeroCache = new CampaignHeroCache();
		// initFileCache(campaignHeroCache);
		campaignModeCache = new PropCampaignModeCache();
		initFileCache(campaignModeCache);
		campaignSpoilsCache = new PropCampaignSpoilsCache();
		initFileCache(campaignSpoilsCache);
		campaignTroopCache = new CampaignTroopCache();
		initFileCache(campaignTroopCache);
		timeConditionCache = new TimeConditionCache();
		initFileCache(timeConditionCache);

		Account.actInfoCache = new ActInfoCache();
		countryCache = new CountryCache();
		initFileCache(countryCache);

		zoneCache = new ZoneCache();
		initFileCache(zoneCache);

		battleBgPropCache = new BattleBgPropCache();
		initFileCache(battleBgPropCache);

		questCache = new QuestCache();
		initFileCache(questCache);
		questEffectCache = new QuestEffectCache();
		initFileCache(questEffectCache);
		questConditonCache = new QuestConditionCache();
		initFileCache(questConditonCache);

		buildingStoreCache = new BuildingStoreCache();
		initFileCache(buildingStoreCache);
		armEnhancePropCache = new ArmEnhancePropCache();
		initFileCache(armEnhancePropCache);
		armEnhanceCostCache = new ArmEnhanceCostCache();
		initFileCache(armEnhanceCostCache);
		Account.armEnhanceCache = new ArmEnhanceCache();

		checkInRewardsCache = new CheckInRewardsCache();
		initFileCache(checkInRewardsCache);
		weightCache = new WeightCache();
		initFileCache(weightCache);
		userVipCache = new UserVipCache();
		initFileCache(userVipCache);
		vipRewardsCache = new VipRewardsCache();
		initFileCache(vipRewardsCache);

		heroRecruitExchangeCache = new HeroRecruitExchangeCache();
		initFileCache(heroRecruitExchangeCache);

		bonusCache = new BonusCache();
		initFileCache(bonusCache);

		firstRechargeRewardCache = new FirstRechargeRewardCache();
		initFileCache(firstRechargeRewardCache);

		manorLocationCache = new ManorLocationCache();
		initFileCache(manorLocationCache);

		campaignSpoilsAppendCache = new CampaignSpoilsAppendCache();
		initFileCache(campaignSpoilsAppendCache);

		battleBuffCache = new BattleBuffCache();
		initFileCache(battleBuffCache);

		shopDailyDiscountCache = new ShopDailyDiscountCache();
		initFileCache(shopDailyDiscountCache);

		shopTimeDiscountCache = new ShopTimeDiscountCache();
		initFileCache(shopTimeDiscountCache);

		timeTypeConditionCache = new TimeTypeConditionCache();
		initFileCache(timeTypeConditionCache);

		eventEntryCache = new EventEntryCache();
		initFileCache(eventEntryCache);

		uiTextCache = new UITextPropCache();
		initFileCache(uiTextCache);

		honorRankCache = new HonorRankCache();
		initFileCache(honorRankCache);

		rankPropCache = new RankPropCache();
		initFileCache(rankPropCache);

		rechargeStateCache = new RechargeStateCache();
		initFileCache(rechargeStateCache);
		chargeCommonConfigCache = new ChargeCommonConfigCache();
		initFileCache(chargeCommonConfigCache);
		doubleChargeCache = new DoubleChargeCache();
		initFileCache(doubleChargeCache);

		pokerReinforceCache = new PokerReinforceCache();
		initFileCache(pokerReinforceCache);

		holyCategoryCache = new HolyCategoryCache();
		initFileCache(holyCategoryCache);

		arenaNPCCache = new ArenaNPCCache();
		initFileCache(arenaNPCCache);

		arenaResetTimeCache = new ArenaResetTimeCache();
		initFileCache(arenaResetTimeCache);

		manorReviveCache = new ManorReviveCache();
		initFileCache(manorReviveCache);

		// 由于商店配置要依赖与item和equipment，所以商店配置数据加载放最后
		shop = new ShopCache();
		initFileCache(shop);

		battleAnimEffect = new BattleAnimEffectCache();
		initFileCache(battleAnimEffect);

		// 组合技能
		battleSkillCombo = new BattleSkillComboCache();
		initFileCache(battleSkillCombo);

		eventRewardsCache = new EventRewardsCache();
		initFileCache(eventRewardsCache);
		
		currencyBoxCache = new CurrencyBoxCache();
		initFileCache(currencyBoxCache);

		propStrongerCache = new PropStrongerCache();
		initFileCache(propStrongerCache);

		trainingRewardsCache = new TrainingRewardsCache();
		initFileCache(trainingRewardsCache);

		heroFavourCache = new HeroFavourCache();
		initFileCache(heroFavourCache);

		heroFavourConvertCache = new HeroFavourConvertCache();
		initFileCache(heroFavourConvertCache);
		
		postInit();
	}

	// 耗时的配置 进游戏后子线程加载
	private static void postInit() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// trainingRewardsCache.checkLoad();
				battleSkillCache.checkLoad();
				campaignSpoilsCache.checkLoad();
				campaignTroopCache.checkLoad();
				buildingEffectCache.checkLoad();
				heroLevelUpCache.checkLoad();
				zoneCache.checkLoad();
				battleSkillEffectCache.checkLoad();
				battleEffectCache.checkLoad();
				battleBuffEffectCache.checkLoad();
				// 技能特效
				battleAnimEffect.checkLoad();
				battleSkillCombo.checkLoad();
			}
		}).start();
	}

	public static List<BriefUserInfoClient> getUser(List<Integer> ids)
			throws GameException {
		return userCache.get(ids);
	}

	public static List<BriefGuildInfoClient> getBriefGuildInfoClient(
			List<Integer> ids) throws GameException {
		return bgicCache.get(ids);
	}

	/**
	 * 根据Id获取物品
	 * 
	 * @param skillID
	 * @return
	 */
	public static Item getItemByID(int itemId) {
		try {
			return (Item) itemCache.get(itemId);
		} catch (GameException e) {
			return null;
		}
	}

	public static BuildingProp getBuildingByID(int id) {
		try {
			return (BuildingProp) buildingPropCache.get(id);
		} catch (GameException e) {
			return null;
		}
	}

	public static BattleSkill getBattleSkillById(int id) {
		try {
			return (BattleSkill) battleSkillCache.get(id);
		} catch (GameException e) {
			return null;
		}
	}
	
	public static EquipmentForgeEffect getEffectById(int id) {
		try {
			return (EquipmentForgeEffect) equipmentForgeEffectCache.get(id);
		} catch (GameException e) {
			return null;
		}
	}

	public static List<Item> getItemsByIds(List<Short> ids)
			throws GameException {
		List<Item> items = new ArrayList<Item>();
		for (Short id : ids) {
			if (id != 0) {
				Item item = ((Item) itemCache.get(id));
				if (item != null)
					items.add(item);
			}
		}
		return items;
	}

	private static void fillActionLog(LogInfoClient lic) throws GameException {
		int type = lic.getLogInfo().getType();
		if (type == LogInfoClient.LT_FARM_ITEM
				|| type == LogInfoClient.LT_POKE_ITEM) {
			lic.setItem((Item) CacheMgr.itemCache.get(lic.getLogInfo()
					.getStid().intValue()));
		} else if (type == LogInfoClient.LT_FARM_SKILL
				|| type == LogInfoClient.LT_POKE_SKILL
				|| type == LogInfoClient.LT_PRESENT_REQUEST
				|| type == LogInfoClient.LT_PRESENT_RESPONSE) {

		} else if (type == LogInfoClient.LT_BUILDING_ACTION) {
			if (lic.getLogInfo().getStid() == 1
					|| lic.getLogInfo().getStid() == 3) {
				lic.setTimes((int) lic.getLogInfo().getParams(0).intValue());
			} else {
				lic.setBuilding((BuildingProp) CacheMgr.buildingPropCache
						.get((int) lic.getLogInfo().getParams(0).intValue()));
				lic.setItem((Item) CacheMgr.itemCache.get(lic.getLogInfo()
						.getParams(1).intValue()));
			}
		} else if (type == LogInfoClient.LT_PRESENT_REQUEST
				|| type == LogInfoClient.LT_PRESENT_RESPONSE) {

		}
	}

	public static List<LogInfoClient> fillActionLog(List<LogInfoClient> lics)
			throws GameException {
		for (LogInfoClient lic : lics) {
			fillActionLog(lic);
		}
		return lics;
	}

	/**
	 * 获取action log里的发起玩家 信息
	 * 
	 * @param logLs
	 * @return
	 * @throws GameException
	 */
	public static List<LogInfoClient> fillLogUser(List<LogInfoClient> lics)
			throws GameException {
		List<Integer> ids = new ArrayList<Integer>();
		for (LogInfoClient lic : lics) {
			int id = lic.getLogInfo().getActive();
			if (!ids.contains(id))
				ids.add(id);
		}
		List<BriefUserInfoClient> users = getUser(ids);
		for (Iterator<LogInfoClient> it = lics.iterator(); it.hasNext();) {
			LogInfoClient lic = (LogInfoClient) it.next();
			for (BriefUserInfoClient u : users) {
				if (u.getId().intValue() == lic.getLogInfo().getActive()
						.intValue()) {
					lic.setFromUser(u);
					break;
				}
			}
			if (lic.getFromUser() == null)
				it.remove();
		}
		return lics;
	}

	public static void fillLogUser(LogInfoClient lic) throws GameException {
		List<Integer> ids = new ArrayList<Integer>(1);
		ids.add(lic.getLogInfo().getActive());
		List<BriefUserInfoClient> users = getUser(ids);
		if (users.size() == 0)
			throw new GameException("无法查询用户:" + lic.getLogInfo().getActive());
		lic.setFromUser(users.get(0));
	}

	public static List<StatInfo> fillStatInfo(List<StatInfo> statInfos)
			throws GameException {
		for (StatInfo si : statInfos) {
			si.setItem((Item) CacheMgr.itemCache.get(si.getItemId()));
		}
		return statInfos;
	}

	public static BriefUserInfoClient getUserById(int userId,
			List<BriefUserInfoClient> list) throws GameException {
		for (BriefUserInfoClient user : list) {
			if (user.getId() == userId)
				return user;
		}
		throw new GameException("无法查询用户:" + userId);
	}

	private static void fillQuestInfo(QuestInfoClient qi) throws GameException {
		Quest quest = (Quest) questCache.get(qi.getQuestId());
		List<QuestEffect> list = questEffectCache.search(quest.getId());
		// for (QuestEffect qe : list) {
		// // 先填充奖励的Item
		// if (qe.getEffectId() == 7 || qe.getEffectId() == 8)
		// qe.setItem((Item) itemCache.get((short) qe.getItemId()));
		// if (qe.getEffectId() == 30 || qe.getEffectId() == 31) {
		// qe.setBuildingProp((BuildingProp) (buildingPropCache.get(qe
		// .getItemId())));
		// }
		// }
		// 填充奖励
		// quest.setQuestEffect(list);
		// quest.setConditions(questConditonCache.search(quest.getId()));
		qi.setQuest(quest);
		qi.setQuestEffect(list);
		qi.setConditions(questConditonCache.search(quest.getId()));
	}

	public static SyncDataSet fillSyncDataSet(SyncDataSet data)
			throws GameException {
		// if (data.medalInfos != null) {
		// for (int i = 0; i < data.medalInfos.length; i++) {
		// MedalInfo mi = data.medalInfos[i].getData();
		// mi.setMedal((Medal) medalCache.get(mi.getMedalId()));
		// }
		// }
		if (data.bagInfos != null) {
			for (int i = 0; i < data.bagInfos.length; i++) {
				ItemBag bag = data.bagInfos[i].getData();
				bag.setItem((Item) itemCache.get(bag.getItemId()));
			}
		}

		if (data.questInfos != null) {
			for (int i = 0; i < data.questInfos.length; i++) {
				fillQuestInfo(data.questInfos[i].getData());
			}
		}

		return data;
	}

	// 土地相关
	public static void fillBriefFiefInfoClients(List<BriefFiefInfoClient> list)
			throws GameException {
		if (null == list || list.isEmpty())
			return;
		List<Integer> ids = new ArrayList<Integer>();
		for (BriefFiefInfoClient bic : list) {
			if (!ids.contains(bic.getUserId()))
				ids.add(bic.getUserId());
			if (!ids.contains(bic.getAttackerId()))
				ids.add(bic.getAttackerId());
		}
		List<BriefUserInfoClient> users = getUser(ids);
		for (BriefFiefInfoClient bic : list) {
			bic.setLord(getUserById(bic.getUserId(), users));
			if (bic.getAttackerId() > 0)
				bic.setAttacker(getUserById(bic.getAttackerId(), users));
			bic.setCountry();
			bic.setNatureCountry();
		}
	}

	/**
	 * 下载战斗相关的声音和圖片
	 * 
	 * @throws GameException
	 */
	public static void downloadBattleImgAndSound(BattleLogInfoClient blic)
			throws GameException {
		HashSet<String> icons = blic.getDownloadIcons();
		for (String it : icons) {
			new NullImgCallBack(it);
		}
		// ImageLoader.getInstance().downloadInCase(it, Config.imgUrl);
		// 预先加载声音
		SoundLoader.getInstance().downloadInCase("battle_lose.ogg");
		SoundLoader.getInstance().downloadInCase("battle_win.ogg");
		SoundLoader.getInstance().downloadInCase("buff.ogg");
		SoundLoader.getInstance().downloadInCase("dead.ogg");
		SoundLoader.getInstance().downloadInCase("g_arrow.ogg");
		SoundLoader.getInstance().downloadInCase("knight.ogg");
		SoundLoader.getInstance().downloadInCase("battle_default.ogg");

	}

	public static void fillLogGuild(List<LogInfoClient> logInfos)
			throws GameException {
		if (null == logInfos || logInfos.isEmpty())
			return;
		List<Integer> ids = new ArrayList<Integer>();
		for (LogInfoClient logInfo : logInfos) {
			if (LogInfoClient.LT_GUILD == logInfo.getLogInfo().getType()) {
				int guildid = logInfo.getLogInfo().getGuildid();
				if (guildid > 0 && !ids.contains(guildid))
					ids.add(guildid);
			}
		}
		if (!ids.isEmpty()) {
			List<BriefGuildInfoClient> list = getBriefGuildInfoClient(ids);
			for (LogInfoClient logInfo : logInfos) {
				if (LogInfoClient.LT_GUILD == logInfo.getLogInfo().getType()) {
					int guildid = logInfo.getLogInfo().getGuildid();
					for (BriefGuildInfoClient bgic : list) {
						if (bgic.getId() == guildid) {
							logInfo.setGuildInfo(bgic);
							break;
						}

					}
				}
			}
		}
	}

	public static void fillOtherUserBattleIdInfoClents(
			List<OtherUserBattleIdInfoClient> battleIdInfos)
			throws GameException {
		if (null == battleIdInfos || battleIdInfos.isEmpty())
			return;
		List<Integer> ids = new ArrayList<Integer>();
		for (OtherUserBattleIdInfoClient battleIdInfoClient : battleIdInfos) {
			if (!BriefUserInfoClient.isNPC(battleIdInfoClient.getUserId())
					&& !ids.contains(battleIdInfoClient.getUserId())) {
				ids.add(battleIdInfoClient.getUserId());
			}
		}
		if (!ids.isEmpty()) {
			List<BriefUserInfoClient> users = getUser(ids);
			for (OtherUserBattleIdInfoClient battleIdInfoClient : battleIdInfos) {
				battleIdInfoClient.setUser(getUserById(
						battleIdInfoClient.getUserId(), users));
			}
		}
	}

	public static void fillRichBattleInfoClient(RichBattleInfoClient rbic,
			List<OtherHeroInfoClient> heroInfos) throws GameException {
		if (null == rbic)
			return;
		List<Integer> ids = new ArrayList<Integer>();
		if (null != rbic.getAttackTroopInfos()) {
			for (MoveTroopInfoClient info : rbic.getAttackTroopInfos()) {
				if (!BriefUserInfoClient.isNPC(info.getUserid())
						&& !ids.contains(info.getUserid()))
					ids.add(info.getUserid());
			}
		}

		if (null != rbic.getDefendTroopInfos()) {
			for (MoveTroopInfoClient info : rbic.getDefendTroopInfos()) {
				if (!BriefUserInfoClient.isNPC(info.getUserid())
						&& !ids.contains(info.getUserid()))
					ids.add(info.getUserid());
			}
		}
		List<BriefUserInfoClient> users = new ArrayList<BriefUserInfoClient>();
		if (!ids.isEmpty())
			users = getUser(ids);

		if (null != rbic.getAttackTroopInfos()) {
			for (MoveTroopInfoClient info : rbic.getAttackTroopInfos()) {
				if (!BriefUserInfoClient.isNPC(info.getUserid())) {
					info.setUser(getUserById(info.getUserid(), users));
				} else {
					info.setUser(CacheMgr.npcCache.getNpcUser(info.getUserid()));
				}

			}
		}

		if (null != rbic.getDefendTroopInfos()) {
			for (MoveTroopInfoClient info : rbic.getDefendTroopInfos()) {
				if (!BriefUserInfoClient.isNPC(info.getUserid())) {
					info.setUser(getUserById(info.getUserid(), users));
				} else {
					info.setUser(CacheMgr.npcCache.getNpcUser(info.getUserid()));
				}
			}
		}

		fillBaseBattleInfoClient(rbic.getBattleInfo().getBbic(), heroInfos);
	}

	private static void fillBaseBattleInfoClient(BaseBattleInfoClient bbic,
			List<OtherHeroInfoClient> heroInfos) throws GameException {
		if (null == bbic)
			return;

		fillBattleHeroInfoClient(bbic.getAttackHeroInfos(), heroInfos);
		fillBattleHeroInfoClient(bbic.getDefendHeroInfos(), heroInfos);

		bbic.setFiefScale(CacheMgr.fiefScaleCache.getFiefScale(bbic.getScale(),
				bbic.getDefendFiefid()));

		List<Integer> ids = new ArrayList<Integer>();
		if (!BriefUserInfoClient.isNPC(bbic.getAttacker())) {
			ids.add(bbic.getAttacker());
		}

		if (!BriefUserInfoClient.isNPC(bbic.getDefender())) {
			ids.add(bbic.getDefender());
		}

		List<BriefUserInfoClient> users = new ArrayList<BriefUserInfoClient>();
		if (!ids.isEmpty()) {
			users = getUser(ids);
		}

		if (!BriefUserInfoClient.isNPC(bbic.getAttacker())) {
			bbic.setAttackerUser(getUserById(bbic.getAttacker(), users));
		} else {
			bbic.setAttackerUser(CacheMgr.npcCache.getNpcUser(bbic
					.getAttacker()));
		}

		if (!BriefUserInfoClient.isNPC(bbic.getDefender())) {
			bbic.setDefenderUser(getUserById(bbic.getDefender(), users));
		} else {
			bbic.setDefenderUser(CacheMgr.npcCache.getNpcUser(bbic
					.getDefender()));
		}
	}

	private static void fillBattleHeroInfoClient(
			List<BattleHeroInfoClient> bhics, List<OtherHeroInfoClient> ohics) {
		if (null == bhics || null == ohics || bhics.isEmpty()
				|| ohics.isEmpty())
			return;
		for (BattleHeroInfoClient bhic : bhics) {
			for (OtherHeroInfoClient ohic : ohics) {
				if (ohic.getId() == bhic.getId())
					bhic.setHeroInfo(ohic);
			}
		}
	}

	public static void fillBattleHotInfoClients(List<BattleHotInfoClient> bhics)
			throws GameException {
		if (null == bhics || bhics.isEmpty())
			return;
		ArrayList<Integer> userIds = new ArrayList<Integer>();
		for (BattleHotInfoClient it : bhics) {
			if (!BriefUserInfoClient.isNPC(it.getAttackerId())
					&& !userIds.contains(it.getAttackerId()))
				userIds.add(it.getAttackerId());

			if (!BriefUserInfoClient.isNPC(it.getDefenderId())
					&& !userIds.contains(it.getDefenderId()))
				userIds.add(it.getDefenderId());
		}

		ArrayList<BriefUserInfoClient> users = new ArrayList<BriefUserInfoClient>(
				1);
		for (int i = 0; i < userIds.size(); i += 10) {
			List<Integer> ls = userIds.subList(i,
					((i + 10 > userIds.size()) ? userIds.size() : (i + 10)));
			users.addAll(CacheMgr.userCache.get(ls));
		}

		for (BattleHotInfoClient it : bhics) {
			if (BriefUserInfoClient.isNPC(it.getAttackerId())) {
				it.setAttacker(CacheMgr.npcCache.getNpcUser(it.getAttackerId()));
			} else {
				for (BriefUserInfoClient user : users) {
					if (user.getId() == it.getAttackerId()) {
						it.setAttacker(user);
						break;
					}
				}
			}

			if (BriefUserInfoClient.isNPC(it.getDefenderId())) {
				it.setDefender(CacheMgr.npcCache.getNpcUser(it.getDefenderId()));
			} else {
				for (BriefUserInfoClient user : users) {
					if (user.getId() == it.getDefenderId()) {
						it.setDefender(user);
						break;
					}
				}
			}

		}
	}

	public static void fillBriefFief(BriefFiefInfoClient info)
			throws GameException {
		ArrayList<Integer> ls = new ArrayList<Integer>();
		ls.add(info.getUserId());
		if (!ls.contains(info.getAttackerId()))
			ls.add(info.getAttackerId());
		List<BriefUserInfoClient> rs = CacheMgr.userCache.get(ls);
		for (BriefUserInfoClient u : rs) {
			if (u.getId() == info.getUserId())
				info.setLord(u);
			if (u.getId() == info.getAttackerId())
				info.setAttacker(u);
		}
		info.setCountry();
		info.setNatureCountry();
	}

	/**
	 * 起兵 增援后返回的数据 进行填充
	 * 
	 * @param fiefInfo
	 * @param battle
	 * @throws GameException
	 */
	public static void fillBattleResult(BriefFiefInfoClient fiefInfo,
			BriefBattleInfoClient battle) throws GameException {
		fillBriefFief(fiefInfo);
		battle.setBfic(fiefInfo);
		battle.setDefendUser(fiefInfo.getLord());
		battle.setAttackerUser(fiefInfo.getAttacker());
	}

	public static void fillHolyBattleState(List<HolyProp> hps)
			throws GameException {
		if (ListUtil.isNull(hps))
			return;
		List<Long> fiefids = new ArrayList<Long>();
		for (HolyProp holyProp : hps) {
			if (!fiefids.contains(holyProp.getFiefId())) {
				fiefids.add(holyProp.getFiefId());
			}
		}

		List<BriefFiefInfoClient> bfics = GameBiz.getInstance()
				.briefFiefInfoQuery(fiefids);
		for (HolyProp holyProp : hps) {
			for (BriefFiefInfoClient briefFiefInfoClient : bfics) {
				if (holyProp.getFiefId() == briefFiefInfoClient.getId()) {
					holyProp.setbFiefInfoClient(briefFiefInfoClient);
					break;
				}
			}
		}

	}

	public static void fillBriefBattleInfoClient(
			List<BriefBattleInfoClient> infos) throws GameException {
		List<Long> fiefids = new ArrayList<Long>();
		for (BriefBattleInfoClient info : infos) {
			if (!fiefids.contains(info.getDefendFiefid())) {
				fiefids.add(info.getDefendFiefid());
			}
		}
		List<BriefFiefInfoClient> bfics = GameBiz.getInstance()
				.briefFiefInfoQuery(fiefids);
		for (BriefBattleInfoClient info : infos) {
			for (BriefFiefInfoClient bfic : bfics) {
				if (info.getDefendFiefid() == bfic.getId()) {
					info.setBfic(bfic);
					break;
				}
			}
		}

		// user
		List<Integer> userids = new ArrayList<Integer>();
		for (BriefBattleInfoClient info : infos) {
			if (!userids.contains(info.getAttacker())) {
				userids.add(info.getAttacker());
			}
			if (!userids.contains(info.getDefender())) {
				userids.add(info.getDefender());
			}
		}
		List<BriefUserInfoClient> users = CacheMgr.userCache.get(userids);
		for (BriefBattleInfoClient info : infos) {
			info.setDefendUser(CacheMgr.getUserById(info.getDefender(), users));
			info.setAttackerUser(CacheMgr.getUserById(info.getAttacker(), users));
		}

	}

	// 填user
	public static void fillBattleLogClient(List<BattleLogClient> infos)
			throws GameException {
		List<Integer> userids = new ArrayList<Integer>();
		for (BattleLogClient info : infos) {
			if (!BriefUserInfoClient.isNPC(info.getAttackUserId())
					&& !userids.contains(info.getAttackUserId())) {
				userids.add(info.getAttackUserId());
			}
			if (!BriefUserInfoClient.isNPC(info.getDefendUserId())
					&& !userids.contains(info.getDefendUserId())) {
				userids.add(info.getDefendUserId());
			}
		}
		List<BriefUserInfoClient> users = CacheMgr.userCache.get(userids);
		for (BattleLogClient info : infos) {
			if (BriefUserInfoClient.isNPC(info.getDefendUserId())) {
				info.setDefendUser(CacheMgr.npcCache.getNpcUser(info
						.getDefendUserId()));
			} else {
				info.setDefendUser(CacheMgr.getUserById(info.getDefendUserId(),
						users));
			}
			if (BriefUserInfoClient.isNPC(info.getAttackUserId())) {
				info.setAttackUser(CacheMgr.npcCache.getNpcUser(info
						.getAttackUserId()));
			} else {
				info.setAttackUser(CacheMgr.getUserById(info.getAttackUserId(),
						users));
			}
		}

	}

	public static void fillPlayerWantedInfoClients(
			List<PlayerWantedInfoClient> infos) throws GameException {
		if (null == infos || infos.isEmpty())
			return;
		List<Integer> userids = new ArrayList<Integer>();

		for (PlayerWantedInfoClient pwic : infos) {
			PlayerWantedInfo info = pwic.getInfo();
			if (!BriefUserInfoClient.isNPC(info.getUserid())
					&& !userids.contains(info.getUserid())) {
				userids.add(info.getUserid());
			}
			if (!BriefUserInfoClient.isNPC(info.getTarget())
					&& !userids.contains(info.getTarget())) {
				userids.add(info.getTarget());
			}
		}

		List<BriefUserInfoClient> users = CacheMgr.userCache.get(userids);
		for (PlayerWantedInfoClient pwic : infos) {
			PlayerWantedInfo info = pwic.getInfo();
			if (BriefUserInfoClient.isNPC(info.getUserid())) {
				pwic.setBriefUser(CacheMgr.npcCache.getNpcUser(info.getUserid()));
			} else {
				pwic.setBriefUser(CacheMgr.getUserById(info.getUserid(), users));
			}
			if (BriefUserInfoClient.isNPC(info.getTarget())) {
				pwic.setTargetUser(CacheMgr.npcCache.getNpcUser(info
						.getTarget()));
			} else {
				pwic.setTargetUser(CacheMgr.getUserById(info.getTarget(), users));
			}
		}
	}

	public static void fillGuildSearchInfoClient(
			List<GuildSearchInfoClient> infos) throws GameException {
		List<Integer> userIds = new ArrayList<Integer>();
		for (GuildSearchInfoClient guildSearchInfoClient : infos) {
			if (!userIds.contains(guildSearchInfoClient.getInfo().getLeader())) {
				userIds.add(guildSearchInfoClient.getInfo().getLeader());
			}
		}

		List<BriefUserInfoClient> users = CacheMgr.userCache.get(userIds);
		for (GuildSearchInfoClient guildSearchInfoClient : infos) {
			guildSearchInfoClient.setBriefUser(CacheMgr.getUserById(
					guildSearchInfoClient.getInfo().getLeader(), users));
		}

	}

	public static void fillArenaUserRankInfoClient(
			List<ArenaUserRankInfoClient> infos) throws GameException {
		List<Integer> userIds = new ArrayList<Integer>();
		for (ArenaUserRankInfoClient it : infos) {
			if (!userIds.contains(it.getUserId()))
				userIds.add(it.getUserId());
		}

		List<BriefUserInfoClient> users = CacheMgr.userCache.get(userIds);
		for (BriefUserInfoClient user : users) {
			for (ArenaUserRankInfoClient it : infos) {
				if (user.getId() == it.getUserId())
					it.setUser(user);
			}
		}
	}
}
