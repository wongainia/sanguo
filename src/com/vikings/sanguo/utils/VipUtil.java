package com.vikings.sanguo.utils;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.Dict;

public class VipUtil {
	// 跳过战斗动画的vip等级
	public static int skipAnimLevel() {
		return CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 26);
	}

	// 允许玩家查看将领的大图
	public static int openHDImage() {
		return 0;
	}

	// 开启第二个出征将领位（或者玩家等级达到指定等级也开启）
	public static int openSecondHero() {
		return CacheMgr.dictCache.getDictInt(Dict.TYPE_UNLOCK_HERO, 1);
	}

	// 开启第三个出征将领位（或者玩家等级达到指定等级也开启）
	public static int openThirdHero() {
		return CacheMgr.dictCache.getDictInt(Dict.TYPE_UNLOCK_HERO, 2);
	}

	// 允许用【稀世将魂】道具兑换将领 （hero_recruit_exchange.csv）

	// 血战奖励时，可以翻卡牌数量增加（2~4个）(user_vip.csv中配置)

	// 允许使用扫荡副本功能
	public static int campaginClear() {
		return CacheMgr.dictCache.getDictInt(Dict.TYPE_CLEAR_ACT, 1);
	}

	// 允许建造【医馆】建筑(building_requirement.csv)

	// 解锁特色士兵（共5中特色士兵，分开解锁）(building_requirement.csv)

	// 允许主动出征圣都（特指三个国家的国都）
	public static int attackHoly() {
		return CacheMgr.dictCache.getDictInt(Dict.TYPE_ATTACK_HOLY, 1);
	}

	// 将领宠幸技能效果，持续时间延长(user_vip.csv)

	// 锻造值最少保留20%（缓慢冷却不会降到底）(user_vip.csv)
	// 酒馆可招募新的将领（开放招募的将领是多个）(客户端不需要（服务器在刷将表中）)
	// 建筑等级上限提升（指定建筑ID）用购买时的VIP等级限制做(building_requirement.csv)
	// 玩家在频道中发言，其边框发生改变
	public static int chatFrame() {
		return 0;
	}

	// 玩家在所有界面的头像框发生改变
	public static int iconFrame() {
		return 0;
	}

	// 可创建60人的家族（高级家族）创建高级家族需要收费元宝(guild_prop.csv)

	// 在国家频道聊天时，所有玩家都会在界面上推送
	public static int pushCountryMsg() {
		return 0;
	}

	// 主动出征圣都 Vip等级要求
	public static int getInitiativeExpeditionsFamous() {
		return 6;
	}
}
