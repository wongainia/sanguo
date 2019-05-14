package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class Dict {

	public static final int TYPE_EXCHANGE_NOTICE = 101;
	public static final int TYPE_EXCHANGE_OK = 102;
	public static final int TYPE_SITE_ADDR = 1001;
	public static final int TYPE_SMS = 1101;
	public static final int TYPE_RECHARGE_DIRECT = 1102;
	public static final int TYPE_FAKE_LOCATION_DESC = 1105;
	public static final int TYPE_ARM_PROP = 1106;
	public static final int TYPE_HERO_REFRESH_DESC = 1107;
	public static final int TYPE_BATTLE_PROTECTED = 1108;
	public static final int TYPE_TIP = 1201;
	public static final int TYPE_VERIFICODE = 1301;
	public static final int TYPE_CUSTOMER_SERVICE_PHONE = 1302; // 客服电话
	public static final int TYPE_CHANGE_NAME_COST = 1404;
	public static final int TYPE_REFRESH_QUEST_MAX_LEVEL = 1407; // 回到地图界面自动刷新任务的等级
	public static final int TYPE_PRODUCT_FOOD_MAX_COUNT = 1408;// 军粮原料生产最大数量
	public static final int TYPE_BATTLE_COST = 1409;// 战争消耗相关数据
	public static final int TYPE_ITME_ID = 1410;// 物品id
	public static final int TYPE_FORCE = 1411;// 强攻强途系数
	public static final int TYPE_ARM_PROP_EFFECT = 1412;// 士兵属性强化
	public static final int TYPE_HERO_EVOLUTE = 1414;// 将领进化属性
	public static final int TYPE_OCCUPY_BATTLE = 1415;// 占领玩家资源点消耗元宝中的参数
	public static final int TYPE_RESOURCE_PRODUCE = 1416;// 资源涨停时间，单位秒
	public static final int TYPE_BATTLE_MASSACRE = 1417;// 屠城相关数据，单位秒
	public static final int TYPE_BATTLE_DUEL = 1418;// 单挑相关数据，单位秒
	public static final int TYPE_ADVANCED_RESOURCE = 1419;// 资源点信息
	public static final int TYPE_NOVICE = 1420;// 新手救济
	public static final int TYPE_GUESTBOOK = 1501; // 快速留言
	public static final int TYPE_WISH_NAME = 1502; // 取愿望名称
	public static final int TYPE_HERO_DISCOUNT = 1504; // 取愿望名称
	public static final int TYPE_CLEAR_ACT = 1505; // 扫荡副本
	public static final int TYPE_WANTED = 1506; // 追杀令
	public static final int TYPE_GUILD = 1510;// 家族
	public static final int TYPE_USER_EXP_BONUS = 1511;// 玩家副本经验加倍
	public static final int TYPE_HERO_EXP_BONUS = 1512;// 将领副本经验加倍
	public static final int TYPE_ALTARS_COST = 1513;// 创建家族总坛
	public static final int TYPE_ARENA = 1514;// 巅峰战场
	public static final int TYPE_ATTACK_HOLY = 1519;// 主动出征圣都vip等级限制

	public static final int TYPE_ALBUM_MIN_REGARDS = 1401; // 相册中使用赞或雷的最小关注度
	public static final int TYPE_FAT_SHEEP = 1413; // 相册中使用赞或雷的最小关注度
	public static final int TYPE_MAX_ASSIST_BUY = 1421; // 增援神兵的最大次数
	public static final int TYPE_HERO_INHERIT = 1422; // 将领传承
	public static final int TYPE_HERO_IDENTIFY = 1423; // 将领鉴定 -
														// 可鉴定的将领天赋下限（5、天选）
	public static final int TYPE_USER_STAMINA = 1424;// 玩家行动力相关
	public static final int TYPE_ALIPAY_ERR_CODE = 2001; // 支付宝错误码
	public static final int TYPE_YEEPAY_ERR_CODE = 2002; // 易宝错误码
	public static final int TYPE_MANOR_ARM_DESC = 3003; // 士兵数据描述
	public static final int TYPE_MANOR_DETAIL_POP_DESC = 3011; // 庄园详情 居民描述
	public static final int TYPE_MANOR_DETAIL_SHOP_DESC = 3012; // 商店描述
	public static final int TYPE_MANOR_DETAIL_ENV_DESC = 3013; // 庄园详情 环境描述
	public static final int TYPE_CHARGE_HOT_NEW = 4001; // 菜单充值按钮

	public static final int TYPE_CLIENT_VERSION = 9999;// 客户端版本

	public static final int TYPE_FAKE_LOCATION = 99998; // 屏蔽的虚拟定位进程
	public static final int TYPE_RECHARGE_CHANNEL = 2003; // 充值渠道

	public static final int TYPE_ANNOUNCE_COLOR = 1501; // 公告颜色

	public static final int TYPE_ANNOUNCE_NAME = 1502; // 公告名字

	public static final int TYPE_SPECIAL_ITEM = 1401; // 特殊物品分类

	public static final int TYPE_UNLOCK_HERO = 1518;// 解锁将领

	public static final int ITEM_MOVE_CASTLE = 10999; // 搬迁令
	public static final int ITEM_BROADCAST = 10011; // 喇叭

	public static final int TYPE_HERO_ENHANCE = 5051;// 将领强化

	public static final int GOD_WEALTH_TIMES_CURRENCY = 1515;// 天降横财每次的消耗

	public static final int WAR_GOD_BOX_OPEN_TOTAL_TIMES = 1516; // 战神宝箱总开启次数

	private int type;
	private int value;
	private String desc;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static Dict fromString(String str) {
		Dict dict = new Dict();
		StringBuilder buf = new StringBuilder(str);
		dict.setType(Integer.parseInt(StringUtil.removeCsv(buf)));
		dict.setValue(Integer.parseInt(StringUtil.removeCsv(buf)));
		dict.setDesc(StringUtil.removeCsv(buf));
		return dict;
	}

}
