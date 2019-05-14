package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.EventEntryInvoker;
import com.vikings.sanguo.ui.alert.ToMapTip;
import com.vikings.sanguo.ui.alert.UpdateBonusTip;
import com.vikings.sanguo.ui.guide.UIChecker;
import com.vikings.sanguo.ui.window.ArmEnhanceListWindow;
import com.vikings.sanguo.ui.window.BloodRewardWindow;
import com.vikings.sanguo.ui.window.BonusWindow;
import com.vikings.sanguo.ui.window.GuildBuildWindow;
import com.vikings.sanguo.ui.window.GuildInfoWindow;
import com.vikings.sanguo.ui.window.HeroEvolveListWindow;
import com.vikings.sanguo.ui.window.RechargeWindow;
import com.vikings.sanguo.ui.window.ShopWindow;

public class JumpTargetPanel {
	/*
	 * 10 副本章节选择界面 20 血战首页界面 30 巅峰战场首页界面 40 领地界面 50 充值界面 - 首页 51 充值界面 - 短信充值界面
	 * 52 充值界面 - 有奖充值界面 53 充值界面 - 双倍优惠 54 充值界面 - 包月充值 60 商店 - 热卖页卡 61 商店 - 道具页卡
	 * 62 商店 - 装备页卡 70 背包 - 礼包页卡 71 背包 - 道具页卡 72 背包 - 装备页卡 73 背包 - 宝石页卡 80 酒馆 -
	 * 主界面 81 酒馆 - 将魂兑换 90 英雄殿 - 主界面 100 铁匠铺 - 主界面 110 招募所 - 主界面 120 任务 -主界面 130
	 * 家族 - 主界面 140 奖励 - 开宝箱 150 活动 - 外敌入侵 160 活动 - 铜雀台 170 活动 - 凤仪亭 180 活动 -
	 * 激活码 190 VIP列表200 英雄殿 - 进化界面 210 主城 - 校场士兵列表220 公告 - 弹出框 230 签到 - 弹出框 240
	 * 活动 - 拜财神 250 荣耀榜 260 变强 - 弹出框 270 升级奖励 280 游戏更新
	 */
	public static void doJump(int jumpId) {
		switch (jumpId) {
		case 10:
			// 副本章节选择界面 与付斌确认 只进 2：战役副本
			Config.getController().openActWindow(
					(PropActType) CacheMgr.actTypeCache.getActTypeByType(2));
			break;

		case 20:
			// 血战首页界面
			if (!Account.myLordInfo.hasReward()) {
				Config.getController().openBloodWindow();
			} else {
				new BloodRewardWindow().open();
			}

			break;
		case 30:
			// 巅峰战场首页界面
			Config.getController().openArenaWindow();

			break;
		case 40:
			// 领地界面
			if (Account.user.getLevel() < UIChecker.FUNC_BATTLE) {
				if (Account.getCurVip() == null
						|| Account.getCurVip().getLevel() <= 0) {
					new ToMapTip().show();
				} else {
					Config.getController().alert(
							"出征功能" + UIChecker.FUNC_BATTLE + "级开启");
				}

				return;
			}
			Config.getController().getFiefMap().open();
			break;

		case 50:
			// 50 充值界面 - 首页
			Config.getController().openRechargeWindow(Account.user.bref());
			break;

		case 51:
			Config.getController().openRechargeWindow(
					RechargeWindow.TYPE_SMS_RECHARGE, Account.user.bref());
			break;
		case 52:
			Config.getController().openRechargeWindow(
					RechargeWindow.TYPE_REWARD_RECHARGE, Account.user.bref());
			break;
		case 53:
			Config.getController().openDoubleRechargeWindow();
			break;
		case 54:
			Config.getController().openMonthRechargeWindow();
			break;
		case 60:
			Config.getController().openShop(ShopWindow.TAB1);
			break;
		case 61:
			Config.getController().openShop(ShopWindow.TAB2);
			break;
		case 62:
			Config.getController().openShop(ShopWindow.TAB3);
			break;

		case 70:
			Config.getController().openStore(0);
			break;
		case 71:
			Config.getController().openStore(1);
			break;
		case 72:
			Config.getController().openStore(2);
			break;
		case 73:
			Config.getController().openStore(3);
			break;

		case 80:
			Config.getController().openBarWindow();
			break;
		case 81:
			Config.getController().openHeroExchangeListWindow();
			break;

		case 90:
			Config.getController().openHeroCenterWindow();
			break;
		case 100:
			Config.getController().openSmithyWindow();
			break;
		case 110:
			Config.getController().openArmTrainingListWindow();
			break;
		case 120:
			Config.getController().openQuestListWindow();
			break;
		case 130:
			if (Account.user.hasGuild()) {
				// 进入家族信息界面
				new GuildInfoWindow().open(Account.guildCache.getGuildid());
			} else {
				// 没有家族
				new GuildBuildWindow().open();
			}
			break;
		case 140:
			Config.getController().openWarLordBox();
			break;
		case 150:
			Config.getController().openForeignInvasionFiefTip();
			break;

		case 160:
			Config.getController().openBronzeTerraceEnterWindow();
			break;
		case 170:
			Config.getController().openRouletteWindow();
			break;
		case 180: // TODO 激活码
			break;

		case 190:
			Config.getController().openVipListWindow();
			break;
		case 200:
			new HeroEvolveListWindow().open();
			break;
		case 210:
			new ArmEnhanceListWindow().open();
			break;
		case 220:
			new EventEntryInvoker().start();
			break;
		case 230:
			// 每日签到
			Config.getController().openCheckIn();
			break;
		case 240:
			// 天降横财
			Config.getController().openGodWealth();
			break;
		case 250:
			new BonusWindow().doOpen();
			break;
		case 260:
			// 我要变强
			Config.getController().openStronger();
			break;
		case 270: // TODO 升级奖励
			break;
		case 280:
			new UpdateBonusTip().show();
			break;
		default:
			Config.getController().alert(
					"你的版本过低，不能直接进入活动相关页面，请手动进入相关的活动页面<br><br>建议更新客户端，体验更多新功能");
			break;
		}
	}
}
