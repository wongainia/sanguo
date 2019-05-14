package com.vikings.sanguo;

public interface BuildingStateId {
	int BUILDING_STATUS_PRODUCE_ITEM = 1; // 生产中物品id
	int BUILDING_STATUS_PRODUCE_TIME = 2; // 生产结束时间
	int BUILDING_STATUS_MARKET_ITEM = 3; // 市场贩卖中物品id
	int BUILDING_STATUS_MARKET_TIME = 4; // 市场贩卖预期结束时间
	int BUILDING_STATUS_MARKET_SPEED = 5; // 市场贩卖速度
	int BUILDING_STATUS_MARKET_AMOUNT = 6; // 市场贩卖物品数量
	int BUILDING_STATUS_HELPER = 7; // 帮忙者
	int BUILDING_STATUS_NEXT_BROKEN_TIME = 8; // 建筑损坏时间
	int BUILDING_STATUS_NEXT_DIRTY_TIME = 9; // 建筑变脏时间
	int BUILDING_STATUS_NEXT_DIRTY_RATE = 10; // 建筑变脏速度（客户端不需使用）
	int BUILDING_STATUS_BANK_MONEY = 11; // 银行存款数目
	int BUILDING_STATUS_PRESENT_USERID = 12; // 赠送者userID
	int BUILDING_STATUS_LEVELUP_SKILL_AMOUNT = 13; // 升级技能积累被使用次数
	int BUILDING_STATUS_NEXT_DRAFT = 14; // 下次可征兵时间
	int BUILDING_STATUS_PRODUCE_COUNT = 15; // 生产中物品数量
	int BUILDING_STATUS_PRODUCE_SCHEME = 16; // 生产方案号
	int BUILDING_STATUS_PRODUCE_ACCELERATER = 17; // 生产加速者

}
