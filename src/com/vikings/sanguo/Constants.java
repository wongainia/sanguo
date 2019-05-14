package com.vikings.sanguo;

import com.vikings.sanguo.config.Config;

public class Constants {

	// 三国 begin

	// 图像框大小
	// 正常将领图片框

	public static final int HERO_TALENT_WIDTH = 152;
	public static final int HERO_TALENT_HEIGHT = 152;

	public static final int HERO_ICON_WIDTH = 130;
	public static final int HERO_ICON_HEIGHT = 130;

	public static final int HERO_STAR_WIDTH = 21;
	public static final int HERO_STAR_HEIGHT = 21;

	// 技能评定框 大小
	public static final int SKILL_RATINGS_WIDTH = 5;
	public static final int SKILL_RATINGS_HEIGHT = 5;
	// 星星距离边框上部偏移
	public static final int HERO_STAR_TOP_MARGIN = 11;

	// // 缩小后将领头像框
	// public static final int HERO_TALENT_WIDTH_SCALE = 90;
	// public static final int HERO_TALENT_HEIGHT_SCALE = 90;
	//
	// public static final int HERO_ICON_WIDTH_SCALE = 77;
	// public static final int HERO_ICON_HEIGHT_SCALE = 77;
	//
	// public static final int HERO_STAR_WIDTH_SCALE = 15;
	// public static final int HERO_STAR_HEIGHT_SCALE = 15;
	//
	// public static final int HERO_STAR_TOP_MARGIN_SCALE = 7;

	public static final int DEVOUR_ITEM_ICON_WIDTH = 75;
	public static final int DEVOUR_ITEM_ICON_HEIGHT = 75;

	public static final int COMMON_SKILL_ICON_WIDTH = 89;
	public static final int COMMON_SKILL_ICON_HEIGHT = 89;

	public static final int SKILL_ICON_WIDTH = 40;
	public static final int SKILL_ICON_HEIGHT = 40;

	public static final float GAMBLE_ICON_WIDTH = 50 * Config.SCALE_FROM_HIGH;
	public static final float GAMBLE_ICON_HEIGHT = 50 * Config.SCALE_FROM_HIGH;

	public static final int EQUIPMENT_ICON_WIDTH = 103;
	public static final int EQUIPMENT_ICON_HEIGHT = 102;

	public static final int EQUIPMENT_WEAR_ICON_WIDTH = 84;
	public static final int EQUIPMENT_WEAR_ICON_HEIGHT = 84;

	public static final int EQUIPMENT_STONE_SMALL_ICON_WIDTH = 25;
	public static final int EQUIPMENT_STONE_SMALL_ICON_HEIGHT = 25;

	public static final int ACT_ICON_WIDTH = 100;
	public static final int ACT_ICON_HEIGHT = 100;
	// 物品图标
	public static final int ITEM_ICON_WIDTH = 80;
	public static final int ITEM_ICON_HEIGHT = 80;
	// 物品列表小图标
	public static final int LIST_ITEM_ICON_WIDTH = 33;
	public static final int LIST_ITEM_ICON_HEIGHT = 33;
	// 商城物品图标
	public static final int SHOP_ITEM_ICON_WIDTH = 110;
	public static final int SHOP_ITEM_ICON_HEIGHT = 110;

	// 领地图标
	public static final int FIEF_ICON_WIDTH = 80;
	public static final int FIEF_ICON_HEIGHT = 80;

	public static final int FIEF_ICON_SCROLL_WIDTH = 75;
	public static final int FIEF_ICON_SCROLL_HEIGHT = 70;

	// 玩家图片框
	public static final int USER_ICON_WIDTH = 85;
	public static final int USER_ICON_HEIGHT = 85;

	// 物品图标
	public static final int CAMPAIGN_ICON_WIDTH = 80;
	public static final int CAMPAIGN_ICON_HEIGHT = 80;

	// 三国 end

	public static final int TYPE_FRUIT = 10000;
	public static final int TYPE_MSG = 10001;
	public static final int TYPE_LOGIN = 10002;

	// 获得物品标识 0:普通物品 1：系统赠送物品
	public static final int COMMON_ITEM = 0;
	public static final int GIFT_ITEM = 1;

	public static final short STEAL_ALL_SKILL_ID = 2015;
	public static final short RECV_ALL_SKILL_ID = 2016;
	public static final short REFRESH_GARDEN_ALL_SKILL_ID = 2018;
	public static final short PUT_SMALL_GHOST_SKILLID = 2024;
	public static final short DRIVE_SMALL_GHOST_SKILLID = 2025;
	public static final short PUT_BIG_GHOST_SKILLID = 2020;
	public static final short DRIVE_BIG_GHOST_SKILLID = 2022;
	public static final short GUILD_INVITE = 1; // 家族邀请技能
	public static final short GUILD_MAKE_OVER = 2;// 禅让
	public static final short GUILD_KICK_OUT = 3;// 踢出家族
	public static final short GUILD_JOIN = 4; // 申请加入
	public static final short GUILD_JOIN_AGREE = 5;// 同意申请
	public static final short GUILD_JOIN_REFUSE = 6;// 拒绝申请
	public static final short GUILD_SET_ELDER = 7;// 7：任命为长老
	public static final short GUILD_REMOVE_ELDER = 8;// 8：贬为平民

	// 按钮文字图片偏移
	public static final int[] CHAR_OFFESET = { 0, 0, 0,
			(int) (2 * Config.SCALE_FROM_HIGH) };

	// 按钮按下时文字图片偏移
	public static final int[] CHAR_OFFESET_PRESS = { 0,
			(int) (6 * Config.SCALE_FROM_HIGH), 0,
			(int) (2 * Config.SCALE_FROM_HIGH) };

	// 性别默认0
	public static final byte DEFAULT = (byte) 0;
	/** 女 */
	public static final int FEMALE = 1;
	/** 男 */
	public static final int MALE = 2;

	// 最小响应手势移动像素点
	public static final int MIN_MOVE_PX = (int) (10 * Config.SCALE_FROM_HIGH);

	public static final int GM_USER_ID = 10000;

	// 服务器取user信息，一次最多取到的个数
	public static final int USER_COUNT = 10;

	// 服务器 取数据集合，一次最多取到的条数
	public static final int MAX_COUNT = 10;

	// 保护状态（不会被打劫等操作）
	public static final int USER_FLAG_PROTECTED = 0;

	// 账号找回
	public static final int RESTORE_OP_PHONE_INPUT = 1;
	public static final int RESTORE_OP_MAIL_INPUT = 2;
	public static final int RESTORE_OP_VERIFY_CODE_INPUT = 0;

	public static final String RESTORE_TAG = "com.vikings.sanguo.restore";
	public static final String RESTORE_VALUE = "VALUE";
	public static final String RESTORE_FLAG = "FLAG";

	// 圆角分
	public static final byte YUAN = 1;
	public static final byte TEN_CENT = 10;
	public static final byte CENT = 100;

	public static final String IN_OTHERS_BLACK_LIST = "你在TA的仇人录里，不能进行此项操作";
	public static final String IN_SELF_BLACK_LIST = "TA在你的仇人录里，不能进行此项操作";

	public static final long CHECK_QUEST_CD = 5 * 1000;

	// 庄园征兵类型
	public static final int MANOR_DRAFT_BY_MONEY = 1;
	public static final int MANOR_DRAFT_BY_RMB = 2;

	public static final boolean IS_TALK_FIEF_INFO = false;

	public static float PER_HUNDRED = 100;
	// 征兵价格的数量单位 n元每10000个兵
	public static float PER_TEN_THOUSAND = 10000f;

	// 动画持续时间
	public static int ANIM_TIME = 100;

	// 内存下限10M
	public static int MEM_LOW_THRESHOLD = 10 * 1024 * 1024;

	// 昵称提示文字
	public static final int NAME_MAX_LENGTH = 6;
	public static final String NAME_EDIT_HINT = "昵称最多六个字";

	public static final int PREFIX_OFFICIAL_LENGTH = 3;
	public static final String PREFIX_OFFICIAL_EDIT_HINT = "官衔最多三个字";

	public static final int MANOR_NAME_MAX_LENGTH = 9;
	public static final String MANOR_NAME_EDIT_HINT = "庄园名称最多九个字";

	public static final int GUILD_INVITE_MSG_MAX_LENGTH = 70;
	public static final String GUILD_INVITE_MSG_HINT = "最多输入70个字";

	public static final int CHAT_MSG_MAX_LENGTH = 128;
	public static final String CHAT_MSG_HINT = "最多输入128个字";

	// 战争方式 1：攻城 2：突围 3：强攻 4:突围
	public static final int BATTLE_TYPE_ATTACK = 1;
	public static final int BATTLE_TYPE_BREAK_THROUGH = 2;
	public static final int BATTLE_TYPE_STORM = 3;
	public static final int BATTLE_TYPE_FORCE_BREAK = 4;

	// 0为修改用户头像 1为修改家族徽章
	public static final int SET_USER_ICON = 0;
	public static final int SET_BADGE_ICON = 1;

	// 战争
	public static final int TYPE_ATTACK = 1;
	public static final int TYPE_DEFEND = 2;

	public static final int SMALL_USER_ICON_WIDTH = (int) (30 * Config.scaleRate);
	public static final int SMALL_USER_ICON_HEIGHT = (int) (30 * Config.scaleRate);

	public static final int ICON_WIDTH = 81;
	public static final int ICON_HEIGHT = 81;

	public static final int USER_ICON_WIDTH_BIG = (int) (100 * Config.scaleRate);
	public static final int USER_ICON_HEIGHT_BIG = (int) (100 * Config.scaleRate);

	// 建筑图片框
	public static final int BUILDING_ICON_WIDTH = 132 - 9;
	public static final int BUILDING_ICON_HEIGHT = 100 - 9;
	// 士兵图片框
	public static final int ARM_ICON_WIDTH = ICON_WIDTH - 1;
	public static final int ARM_ICON_HEIGHT = ICON_HEIGHT - 1;

	// 家族头像框
	public static final int GUILD_ICON_WIDTH = ICON_WIDTH;
	public static final int GUILD_ICON_HEIGHT = ICON_HEIGHT;
	// 任务图标
	public static final int QUEST_ICON_WIDTH = ICON_WIDTH - 1;
	public static final int QUEST_ICON_HEIGHT = ICON_HEIGHT - 1;

	public static final float SMALL_ICON_WIDTH = 25 * Config.SCALE_FROM_HIGH;
	public static final float SMALL_ICON_HEIGHT = 25 * Config.SCALE_FROM_HIGH;

	public static final float COMMON_ICON_WIDTH = 80 * Config.SCALE_FROM_HIGH;
	public static final float COMMON_ICON_HEIGHT = 80 * Config.SCALE_FROM_HIGH;

	public static final float STORE_ICON_WIDTH = 70 * Config.SCALE_FROM_HIGH;
	public static final float STORE_ICON_HEIGHT = 70 * Config.SCALE_FROM_HIGH;

	public static final float CHECKIN_ICON_WIDTH = 70 * Config.SCALE_FROM_HIGH;
	public static final float CHECKIN_ICON_HEIGHT = 70 * Config.SCALE_FROM_HIGH;

	public static final float ARMY_ICON_WIDTH = 70 * Config.SCALE_FROM_HIGH; //
	public static final float ARMY_ICON_HEIGHT = 70 * Config.SCALE_FROM_HIGH;

	public static final float SMALL_PACK_ICON_WIDTH = 40 * Config.SCALE_FROM_HIGH;
	public static final float SMALL_PACK_ICON_HEIGHT = 40 * Config.SCALE_FROM_HIGH;

	public static final int RUNE_ICON_BG_WIDTH = 100;
	public static final int RUNE_ICON_BG_HEIGHT = 105;
	public static final float RUNE_SCALE = 0.45f;

	public final static int TROOP_MASSACRE = 6;// 屠城
	public final static int TROOP_DUEL = 5;// 单挑
	public final static int TROOP_DISPATCH = 4; // 调遣
	public final static int TROOP_REINFORCE = 3; // 增援
	public final static int TROOP_PLUNDER = 2; // 掠夺
	public final static int TROOP_OCCUPY = 1; // 占领

	public static final int TYPE_NOAMRL = 1;
	public static final int TYPE_RMB = 2;

	// 副將、主将 battle
	public static final int BATTLE_HERO_ICON_WIDTH = 116;
	public static final int BATTLE_HERO_ICON_HEIGHT = 116;

	public static final int BATTLE_TALENT_WIDTH = 119;
	public static final int BATTLE_TALENT_HEIGHT = 119;

	public static final int BATTLE_HERO_STAR_WIDTH = 16;
	public static final int BATTLE_HERO_STAR_HEIGHT = 16;

	public static final int BATTLE_HERO_STAR_BOTTOM_MARGIN = 10;

	public static final int SECONDARY_ICON_WIDTH_SCALE = 48;
	public static final int SECONDARY_ICON_HEIGHT_SCALE = 48;

	public static final int SECONDARY_TALENT_WIDTH = 58;
	public static final int SECONDARY_TALENT_HEIGHT = 58;

	public static final int SECONDARY_STAR_WIDTH = 8;
	public static final int SECONDARY_STAR_HEIGHT = 8;

	public static final int SECONDARY_ICON_WIDTH = 48;
	public static final int SECONDARY_ICON_HEIGHT = 48;

	public static final int SECONDARY_STAR_BOTTOM_MARGIN = 5;

	/**
	 * 战场种类
	 */
	public final static int BATTLE_FIGHT_NPC = 0;

	// 一次吞噬将领的最大个数
	public final static int DEVOUR_MAX_COUNT = 6;

	/**
	 * 1: 私聊 2：家族 3：国家 4：世界
	 */
	public final static int MSG_PRIVATE = 1;
	public final static int MSG_GUILD = 2;
	public final static int MSG_COUNTRY = 3;
	public final static int MSG_WORLD = 4;

	// 聊天user列表排列用
	public static final int COUNTRY_ID = 9001;
	public static final int WORLD_ID = 9002;
	public static final int GUILD_ID = 9003;

	// 将领进化的最大星星
	public static final int HERO_MAX_STAR = 6;

	// 0:收藏,1:圣都,2.名城，3.用户ID, 4.肥羊 , 5重镇
	static final public byte FAVOR = 0;
	static final public byte HOLY = 1;
	static final public byte FAMOUS = 2;
	static final public byte USER = 3;
	static final public byte SHEEP = 4;
	static final public byte TOWN = 5;

	// briefFiefInfoQuery接口查询数量
	public static final int BRIEF_FIEF_INFO_QUERY_COUNT = 25;
	// briefBattleInfoQuery接口查询数量
	public static final int BRIEF_BATTLE_INFO_QUERY_COUNT = 10;

	// 大于等于vip7的用户聊天背景不一样
	public static final int CHAT_VIP_LVL = 12;

	// 坐标解析分割符
	public static final String FIEF_MARKING_SEPERATER = "[,，.]";

	public static final int HOUR = 60 * 60;
	public static final int DAY = 24 * 60 * 60;

	public static final int NO_DISCOUNT = 100;

	public static final float DIMMING = 0.7f;

	public static final int GOD_SOLDIER_BASE_COUNT = 1000;
	public static final int GOD_SOLDIER_BASE_HP = 20000;

	public static final String USER_EXP = "玩家经验";
	public static final String HERO_EXP = "将领经验";

	public static final int DEAD_HERO_ICON = (int) (50 * Config.SCALE_FROM_HIGH);
	public static final int MEDIUM_HERO_ICON = (int) (70 * Config.SCALE_FROM_HIGH);
	public static final int MEDIUM_HERO_ICON_RATE = 10;

	public static int TYPE_MATERIAL = 1;
	public static int TYPE_CURRENCY = 2;

	// 将领类型 0:主将 1：副将
	public static final int MAIN_GENERAL = 0;

	public static final int SECONDARY_GENERAL = 1;

	// 1:征讨外敌 2：增援
	public static final int OCCUPY = 1;
	public static final int ASSISTATTACK = 2;

	// 我要变强
	// 提升将领 类型
	public static final int GENERAL = 1;
	// 提升装备 类型
	public static final int EQUIPMENT = 2;
	// 提升士兵 类型
	public static final int SOLDIER = 3;

}
