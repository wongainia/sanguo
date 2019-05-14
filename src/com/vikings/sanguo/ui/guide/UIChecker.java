package com.vikings.sanguo.ui.guide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.utils.DateUtil;

public class UIChecker {
	// 七圣
	public static final int FUNC_BATTLE = 10;
	// 水果
	// 3 菜单开启【商店】按钮
	public static final byte FUNC_SHOP = 3;
	// 5 菜单开启【好友】按钮
	public static final byte FUNC_FRIEND = 5;
	// 6 农田主界面开启【探索】按钮
	public static final byte FUNC_EXPLORE = 6;
	// 7 菜单开启【偷摘】按钮
	public static final byte FUNC_STEAL = 7;
	// 8级开启关闭地图提示
	public static final byte FUNC_MAP_CTR_NOTICE = 8;
	// 10 "个人总览界面开启【技能】按钮
	public static final byte FUNC_SKILL = 10;
	// 好友列表开启【黑屋】的分页, 好友总览界面开启【黑名单】按钮
	public static final byte FUNC_BLACK_LIST = 10;
	// 好友列表进入操作栏后，开启【动TA】按钮, 好友总览界面开启【动TA】按钮
	public static final byte FUNC_POKE = 10;
	// 11 农田主界面开启【左轮枪】
	public static final byte FUNC_MANOR = 11;
	// 12 玩家主界面开启【许愿】按钮，12级开启好友/附近的许愿推送信息；
	public static final byte FUNC_WISH = 12;
	// 12级提示玩家绑定，如果已经绑定就不提示
	public static final byte FUNC_BINDING_TIPS = 12;
	// 13 玩家主界面开启【擦肩】按钮，13级开启界面右上角擦肩推送信息；
	public static final byte FUNC_MEET = 13;
	// 20 领地主界面开启【左轮枪】
	public static final byte FUNC_FIEF = 18;

	public static final byte FUNC_MANOR_EVENT = 20;
	// 15级开放水果机
	public static final byte FUNC_GAMBLE_MACHINE = 15;
	// 12级开放小助手
	public static final byte FUNC_ROBOT = 12;

	/**
	 * 开放功能
	 */
	public static void check() {
		int level = Account.user.getLevel();
		switch (level) {
		case FUNC_SHOP:
		case FUNC_FRIEND:
			// Config.getController().getPopupMenu().checkUI();
			break;
		// case FUNC_EXPLORE:
		// Config.getController().getPopupMenu().checkUI();
		// if (Config.getController().isGardenMapOveraly()) {
		// Config.getController().getGmap().getGardenMapOverlay()
		// .setButton();
		// }
		// break;
		case FUNC_STEAL:
		case FUNC_WISH:
		case FUNC_MEET:
		case FUNC_MANOR:
			break;
		case FUNC_FIEF:
			break;
		}
	}

	public static String getRechargeDate() {
		String str = CacheMgr.dictCache.getDict(Dict.TYPE_TIP, 3);
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		Date d;
		try {
			d = df.parse(str);
		} catch (ParseException e) {
			return "充值功能稍后开放!";
		}
		return "充值功能将于" + DateUtil.getDateDesc(d) + "开放!";
	}
}
