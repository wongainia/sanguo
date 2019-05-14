/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 上午10:17:44
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class UITextProp {
	private int id;
	private String txt;

	public static final int MARS_HONOR_RANK = 201;
	public static final int FAMILY_HONOR_RANK = 202;
	public static final int PERSON_EXPEND_HONOR_RANK = 203;
	public static final int GUILD_EXPEND_HONOR_RANK = 204;

	public static final int RANK_WEALTH = 301;
	public static final int RANK_HERO = 302;
	public static final int RANK_KILLS = 303;
	public static final int RANK_LORD = 304;
	public static final int NEWER_PROTECTED_DESC = 601;
	public static final int NEWER_PROTECTED_EXT_DESC = 602;
	public static final int HERO_INHERIT_RULE = 701;
	public static final int HERO_INHERIT_DESC = 702;
	public static final int HERO_REBORN_DESC = 703;
	public static final int HERO_ARENA_DESC = 801;
	public static final int HERO_ARENA_TITLE = 802;
	public static final int MANOR_REVIVE_TITLE = 901;
	public static final int MANOR_REVIVE_RULE = 902;
	public static final int BLOOD_RULER = 1101;

	// 铜雀台相关的提示
	public static final int DAQIAO_BRONZE_SPEC = 1201;
	public static final int DAQIAO_BRONZE_PROMPT = 1202;
	public static final int XIAOQIAO_BRONZE_SPEC = 1203;
	public static final int XIAOQIAO_BRONZE_PROMPT = 1204;

	// 铜雀台规则说明
	public static final int TONGQUETAI_SPEC = 1231;

	// 凤仪亭规则说明
	public static final int ROULETTE_SPEC = 2301;

	// 天降横财说明
	public static final int GOD_WEALTH_SPEC = 1301;

	// 神圣护佑描述
	public static final int PROTECTED_DESC = 1401;
	public static final int PROTECTED_VIP_DESC = 1401;

	public static final int ARM_PROP_DESC = 1501;

	// 包月优惠描述
	public static final int MONTH_CHARGE_REWARDS_DESC = 1601;
	// 双倍优惠描述
	public static final int DOUBLE_CHARGE_REWARDS_DESC = 1602;
	// 短信充值
	public static final int SM_CHARGE_REWARDS_DESC = 1603;
	// 有奖充值
	public static final int REWARD_CHARGE_REWARDS_DESC = 1604;
	// 给别人充值
	public static final int OTHER_CHARGE_REWARDS_DESC = 1605;

	// 英雄殿描述
	public static final int HERO_CENTER_DESC1 = 1701;
	public static final int HERO_CENTER_DESC2 = 1702;
	public static final int HERO_CENTER_DESC3 = 1703;
	public static final int HERO_CENTER_DESC4 = 1704;
	public static final int HERO_CENTER_DESC5 = 1705;

	// 铁匠铺
	public static final int SMITHY_DESC1 = 2401;
	public static final int SMITHY_DESC2 = 2402;
	public static final int SMITHY_DESC3 = 2403;
	public static final int SMITHY_DESC4 = 2404;

	// 战斗日志描述
	public static final int BATTLE_LOG_DESC1 = 1801;
	public static final int BATTLE_LOG_DESC2 = 1802;
	public static final int BATTLE_LOG_DESC3 = 1803;
	public static final int BATTLE_LOG_DESC4 = 1804;

	// 引导描述
	public static final int GUIDE_DESC_01 = 2001;
	public static final int GUIDE_DESC_11 = 2011;
	public static final int GUIDE_DESC_12 = 2012;
	public static final int GUIDE_DESC_13 = 2013;
	public static final int GUIDE_DESC_21 = 2021;
	public static final int GUIDE_DESC_31 = 2031;
	public static final int GUIDE_DESC_41 = 2041;
	public static final int GUIDE_DESC_51 = 2051;
	public static final int GUIDE_DESC_52 = 2052;
	public static final int GUIDE_DESC_61 = 2061;
	public static final int GUIDE_DESC_62 = 2062;
	public static final int GUIDE_DESC_81 = 2081;// 装备引导，第一句话
	public static final int GUIDE_DESC_86 = 2086;// 血战引导，第一句话
	public static final int GUIDE_DESC_87 = 2087;// 血战引导，第二句话
	public static final int GUIDE_DESC_91 = 2091;// 巅峰引导，第一句话

	public static final int GUIDE_DESC_2111 = 2101;// 副本1-5，第一句话
	public static final int GUIDE_DESC_2121 = 2111;// 副本1-7，第一句话
	public static final int GUIDE_DESC_2201 = 2121;// 副本1-8，第一句话
	public static final int GUIDE_DESC_2301 = 2321;// 副本1-4，第一句话
	public static final int GUIDE_DESC_2401 = 2421;// 副本战败指引 v0 1-8，第一句话
	public static final int GUIDE_DESC_2501 = 2521;// 副本战败指引v1，第一句话

	public static final int GUIDE_DESC_19 = 2019;

	public static final int GUIDE_TROOP_TOTAL_69 = 2069; // 募兵所引导 兵力触发条件

	// 钱粮礼包
	public static final int GIFT_TIP_MONEY = 2201;
	// 资源礼包
	public static final int GIFT_TIP_RES = 2202;

	// 解锁将领文字提示
	public static final int UNLOCK_HERO1 = 1901;
	public static final int UNLOCK_HERO2 = 1902;

	public void setId(int id) {
		this.id = id;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public int getId() {
		return id;
	}

	public String getTxt() {
		return txt;
	}

	public static UITextProp fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		UITextProp utp = new UITextProp();

		utp.setId(StringUtil.removeCsvInt(buf));
		utp.setTxt(StringUtil.removeCsv(buf));

		return utp;
	}
}
