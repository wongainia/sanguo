package com.vikings.sanguo.message;

public class Constants {

	public static final int SHORT_LEN = 2;
	public static final int INT_LEN = 4;
	public static final int LONG_LEN = 8;

	public static final int MAX_LEN_NICK = 9 * 2 + 2;
	public static final int MAX_LEN_PASSWORD = 12 * 2 + 2;
	public static final int MAX_LEN_MOBILE = 12 * 2 + 2;
	public static final int MAX_LEN_SIGN = 24 * 2 + 2;
	public static final int MAX_LEN_CHAT = 128 * 2 + 2;
	public static final int MAX_LEN_PIT_TITLE = 16 * 2 + 2;

	public static final int MAX_LEN_ORESITE_ADDR = 20 * 2 + 2;

	public static final int MAX_LEN_FIELD_VALUE = MAX_LEN_MOBILE;

	public static final int MAX_LEN_SIM = (32 * 2 + 2); // sim卡号最大长度

	public static final int MAX_LEN_EXTEND = 800; // 注册扩展字段长度

	public static final int MAX_LEN_MESSAGE = MAX_LEN_CHAT;

	public static final int MAX_LEN_EMAIL = (32 * 2 + 2); // 邮箱长度

	public static final int MAX_LEN_ID_CARD_NUMBER = (18 * 2 + 2);

	public static final int MAX_LEN_MOBILE_EMAIL = MAX_LEN_EMAIL;

	public static final int MAX_LEN_IP_ADDR = 64; // 最大域名长度

	public static final int MAX_LEN_KEN_LEN = 16; // 最大域名长度

	// 数据同步 数据类型

	static public final int DATA_TYPE_ACCOUNT_INFO = 0x00000004;
	static public final int DATA_TYPE_ROLE_INFO = 0x00000008;
	static public final int DATA_TYPE_FRIEND = 0x00000010;
	static public final int DATA_TYPE_BAG = 0x00000020;
	// static public final int DATA_TYPE_MEDAL = 0x00000040;
	static public final int DATA_TYPE_QUEST = 0x00000080;
	static public final int DATA_TYPE_MANOR = 0x00000100;
	static public final int DATA_TYPE_BLACKLIST = 0x00000200;
	static public final int DATA_TYPE_LORD = 0x00000400;
	static public final int DATA_TYPE_LORD_FIEF = 0x00000800;
	static public final int DATA_TYPE_BATTLE_ID = 0x00001000;
	static public final int DATA_TYPE_GUILD = 0x00002000;
	static public final int DATA_TYPE_HERO = 0x00004000;
	static public final int DATA_TYPE_ACT = 0x00008000;
	static public final int DATA_TYPE_DYNAMIC_ACT = 0x00010000;
	static public final int DATA_TYPE_ARM_PROP = 0x00020000;
	static public final int DATA_TYPE_EQUIPMENT = 0x00040000;

	static public final int DATA_TYPE_ALL = 0x7fffffff;

	// 重要的数据
	static public final int DATA_TYPE_IMPORTANT = DATA_TYPE_BATTLE_ID;

	// 取其他用富用户信息
	static public final int DATA_TYPE_OTHER_RICHINFO = DATA_TYPE_ACCOUNT_INFO
			| DATA_TYPE_MANOR | DATA_TYPE_LORD;

	// 定义登录的时候取的数据
	static public final int DATA_TYPE_LOGIN = DATA_TYPE_ACCOUNT_INFO
			| DATA_TYPE_ROLE_INFO | DATA_TYPE_FRIEND | DATA_TYPE_BAG
			| DATA_TYPE_MANOR | DATA_TYPE_LORD | DATA_TYPE_LORD_FIEF
			| DATA_TYPE_BATTLE_ID | DATA_TYPE_GUILD;

	// 定义进入游戏后取的数据 延迟加载不影响登录
	static public final int DATA_TYPE_LAZY = DATA_TYPE_QUEST
			| DATA_TYPE_BLACKLIST | DATA_TYPE_HERO | DATA_TYPE_ACT
			| DATA_TYPE_DYNAMIC_ACT | DATA_TYPE_EQUIPMENT;

	static public final byte SYNC_TYPE_ALL = 0;
	static public final byte SYNC_TYPE_DIFF = 1;

	// FiefDataType
	public static final int FIEF_DATA_TYPE_FIEFINFO = 0x00000001;
	public static final int FIEF_DATA_TYPE_LEAVED_TROOP = 0x00000002;
	public static final int FIEF_DATA_TYPE_REACHING_TROOP = 0x00000004;
	public static final int FIEF_DATA_TYPE_ATTACK_BATTLEID = 0x00000008;
	public static final int FIEF_DATA_TYPE_ASSIST_BATTLEID = 0x00000010;
	public static final int FIEF_DATA_TYPE_DEFEND_BATTLEID = 0x00000020;
	public static final int FIEF_DATA_TYPE_ALL = 0x7fffffff;

	// BattleDataType
	public static final int BATTLE_DATA_TYPE_BATTLEINFO = 0x00000001;
	public static final int BATTLE_DATA_TYPE_ATTACK_TROOP = 0x00000002;
	public static final int BATTLE_DATA_TYPE_DEFEND_TROOP = 0x00000004;
	public static final int BATTLE_DATA_TYPE_ALL = 0x7fffffff;

	// GuildDataType
	public static final int GUILD_DATA_TYPE_GUILDINFO = 0x00000001;
	public static final int GUILD_DATA_TYPE_GUILD_MEMBER = 0x00000002;
	public static final int GUILD_DATA_TYPE_GUILD_JOIN = 0x00000004;
	public static final int GUILD_DATA_TYPE_GUILD_INVITE = 0x00000008;
	public static final int GUILD_DATA_TYPE_ALL = 0x7fffffff;

	// BattleFlag
	public static final int E_BATTLE_FLAG_GUILD_ASSIST = 0x00000001; // 战斗可以家族增援

	public static final String VALUEOPEN = "OPEN";
	// public static final String TIMING = "TIMING";

}
