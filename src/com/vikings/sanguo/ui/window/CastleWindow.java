package com.vikings.sanguo.ui.window;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.battle.anim.AnimPool;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BackgroundInvoker;
import com.vikings.sanguo.message.ManorLayDownResp;
import com.vikings.sanguo.message.ManorRecommendEmptySpaceResp;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.BuildingRequirement;
import com.vikings.sanguo.model.EventRewards;
import com.vikings.sanguo.model.ManorLocation;
import com.vikings.sanguo.model.Quest;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.model.WorldLevelInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.CastleUI;
import com.vikings.sanguo.ui.DownloadTip;
import com.vikings.sanguo.ui.DragLayout;
import com.vikings.sanguo.ui.EventRewardsUI;
import com.vikings.sanguo.ui.NotifyWorldChatMsg;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.ProtectedTip;
import com.vikings.sanguo.ui.alert.ToMapTip;
import com.vikings.sanguo.ui.alert.WeakStateTip;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.ui.guide.UIChecker;
import com.vikings.sanguo.ui.listener.BuildingSelfOnClick;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;

/**
 * 主城界面
 */
public class CastleWindow extends PopupWindow implements OnClickListener {

	private ViewGroup window, ownBtnLayout, otherBtnLayout, weakLayout,
			vipLayout, infoPopupBt, castleInfoLayout, castleInfo,
			worldLevelInfo, rightInfoLayout;

	private ImageView icon, vipIcon, num0Icon;
	private ImageView eventBtn, rewardsBtn;
	private ImageView event_tip, rward_tip; // 有完成的任务或者vip包月
	private ImageView protectedIcon;
	private TextView name, level, armCnt, money, currency, food, vipLevel,
			guildName, weakTime, progressDesc;
	private View storeBt, firendBt, chatBt, guildBt, rankBt, moreBt, worldBt,
			campaignBt, troopBt, rechargeBt, systemBt, shopBt;

	private ProgressBar progressBar;

	private CastleUI castleUI;// 绘制界面

	private EventRewardsUI eventRewardUI;

	private Animation topAnim, downAnim, leftAnim, rightAnim;// 动画

	private DownloadTip download = null;

	// private CheckInvoker checkInvoker;

	@Override
	protected View getPopupView() {
		return window;
	}

	public CastleUI getCastleUI() {
		return castleUI;
	}

	@Override
	public boolean goBack() {
		controller.quit();
		return true;
	}

	// 初始化
	@Override
	protected void init() {
		window = (ViewGroup) Config.getController().inflate(R.layout.castle);
		((ViewGroup) Config.getController().findViewById(R.id.castleWindow))
				.addView(window);

		// 位移动画效果
		topAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1,
				Animation.RELATIVE_TO_SELF, 0);
		topAnim.setDuration(500);
		topAnim.setInterpolator(new AccelerateInterpolator(3f));

		// 位移动画效果
		downAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
				Animation.RELATIVE_TO_SELF, 0);
		downAnim.setDuration(500);
		downAnim.setInterpolator(new AccelerateInterpolator(4f));

		leftAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.5f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0);
		leftAnim.setDuration(500);
		leftAnim.setInterpolator(new OvershootInterpolator(3f));
		leftAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				ViewUtil.setGone(infoPopupBt);
				ViewUtil.setVisible(castleInfoLayout);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});

		rightAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1.5f, Animation.RELATIVE_TO_SELF,
				0, Animation.RELATIVE_TO_SELF, 0);
		rightAnim.setDuration(500);
		rightAnim.setInterpolator(new AccelerateInterpolator(4f));
		rightAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ViewUtil.setGone(castleInfoLayout);
				ViewUtil.setVisible(infoPopupBt);
			}
		});

		ownBtnLayout = (ViewGroup) window.findViewById(R.id.ownBtnLayout);
		ViewUtil.setVisible(ownBtnLayout);
		otherBtnLayout = (ViewGroup) window.findViewById(R.id.otherBtnLayout);
		ViewUtil.setGone(otherBtnLayout);
		weakLayout = (ViewGroup) window.findViewById(R.id.weakLayout);
		weakLayout.setOnClickListener(this);
		infoPopupBt = (ViewGroup) window.findViewById(R.id.infoPopupBt);
		infoPopupBt.setOnClickListener(this);
		ViewUtil.setVisible(infoPopupBt);
		castleInfoLayout = (ViewGroup) window
				.findViewById(R.id.castleInfoLayout);
		castleInfoLayout.setOnClickListener(this);
		ViewUtil.setGone(castleInfoLayout);
		castleInfo = (ViewGroup) castleInfoLayout.findViewById(R.id.castleInfo);
		worldLevelInfo = (ViewGroup) castleInfoLayout
				.findViewById(R.id.worldLevelInfo);

		rightInfoLayout = (ViewGroup) window.findViewById(R.id.rightInfo);

		icon = (ImageView) window.findViewById(R.id.icon);
		icon.setOnClickListener(this);
		ViewUtil.setImage(castleInfo.findViewById(R.id.pattenMirror),
				ImageUtil.getMirrorBitmapDrawable("pattern_line.png"));
		ViewUtil.setImage(worldLevelInfo.findViewById(R.id.pattenMirror),
				ImageUtil.getMirrorBitmapDrawable("pattern_line.png"));

		name = (TextView) window.findViewById(R.id.name);// 名字
		level = (TextView) window.findViewById(R.id.level);// 等级
		armCnt = (TextView) window.findViewById(R.id.armCnt);// 兵力
		money = (TextView) window.findViewById(R.id.money);// 金币
		currency = (TextView) window.findViewById(R.id.currency);// 元宝
		food = (TextView) window.findViewById(R.id.food);// 粮草
		weakTime = (TextView) window.findViewById(R.id.weakTime);
		progressDesc = (TextView) window.findViewById(R.id.progressDesc);

		progressBar = (ProgressBar) window.findViewById(R.id.progressBar);// 经验进度条

		// vip字体点击事件
		vipLayout = (ViewGroup) window.findViewById(R.id.vipLayout);
		vipLayout.setOnClickListener(this);
		vipIcon = (ImageView) window.findViewById(R.id.vipIcon);
		num0Icon = (ImageView) window.findViewById(R.id.num0Icon);
		vipLevel = (TextView) window.findViewById(R.id.vipLevel);

		guildName = (TextView) window.findViewById(R.id.guildName);

		// 背包
		storeBt = window.findViewById(R.id.storeBt);
		storeBt.setOnClickListener(this);

		// 商城
		shopBt = window.findViewById(R.id.shopBt);
		shopBt.setOnClickListener(this);

		// 好友
		firendBt = window.findViewById(R.id.firendBt);
		firendBt.setOnClickListener(this);
		// 聊天
		chatBt = window.findViewById(R.id.chatBt);
		chatBt.setOnClickListener(this);
		// 家族
		guildBt = window.findViewById(R.id.guildBt);
		guildBt.setOnClickListener(this);

		// 排行
		rankBt = window.findViewById(R.id.rankBt);
		rankBt.setOnClickListener(this);

		// 更多
		moreBt = window.findViewById(R.id.moreBt);
		moreBt.setOnClickListener(this);
		// 世界
		worldBt = window.findViewById(R.id.worldBt);
		worldBt.setOnClickListener(this);
		// 副本
		campaignBt = window.findViewById(R.id.campaignBt);
		campaignBt.setOnClickListener(this);
		// 充值
		rechargeBt = window.findViewById(R.id.rechargeBt);
		rechargeBt.setOnClickListener(this);
		// 系统
		systemBt = window.findViewById(R.id.systemBt);
		systemBt.setOnClickListener(this);
		// 军队
		troopBt = window.findViewById(R.id.troopBt);
		troopBt.setOnClickListener(this);

		// 创建建筑效果
		castleUI = new CastleUI(
				(DragLayout) window.findViewById(R.id.castleUI),
				new BuildingSelfOnClick(), new CallBack() {

					@Override
					public void onCall() {
						clearUI();
					}
				}, true);

		// 新手保护
		protectedIcon = (ImageView) window.findViewById(R.id.protectedIcon);
		protectedIcon.setOnClickListener(this);

		eventBtn = (ImageView) window.findViewById(R.id.eventBtn);
		eventBtn.setOnClickListener(this);
		rewardsBtn = (ImageView) window.findViewById(R.id.rewardsBtn);
		event_tip = (ImageView) window.findViewById(R.id.event_tip);
		rward_tip = (ImageView) window.findViewById(R.id.rward_tip);
		rewardsBtn.setOnClickListener(this);
		eventRewardUI = new EventRewardsUI(
				window.findViewById(R.id.extendLayout),
				(ImageView) window.findViewById(R.id.extendArrow));

	}

	public EventRewardsUI getEventRewardUI() {
		return eventRewardUI;
	}

	public DownloadTip showDownload() {
		if (download == null) {
			download = new DownloadTip(window);
		}
		return download;
	}

	// 救济包
	// private NoviceHelp getNoviceHelpTip() {
	// if (noviceHelpTip == null) {
	// noviceHelpTip = new NoviceHelp(window);
	// }
	// return noviceHelpTip;
	// }

	@Override
	protected void destory() {
		((ViewGroup) Config.getController().findViewById(R.id.castleWindow))
				.removeView(window);
	}

	public void setValue() {
		setUserInfo();
		setCastleInfo();
		setWorldLevelInfo();

		// 是否有未完成的任务 或者双倍优惠
		if (isAward()) {
			if (!eventRewardUI.isShowRewards()) {
				rward_tip.setVisibility(View.VISIBLE);
				rward_tip.startAnimation(AnimPool.getAwardTip());
			} else {
				rward_tip.clearAnimation();
				ViewUtil.setGone(rward_tip);
			}
		}

		if (isActive()) {
			if (!eventRewardUI.isShowEvent()) {
				event_tip.setVisibility(View.VISIBLE);
				event_tip.startAnimation(AnimPool.getAwardTip());
			} else {
				event_tip.clearAnimation();
				ViewUtil.setGone(event_tip);
			}
		}
	}

	private boolean isActive() {
		// vip 特权
		List<QuestInfoClient> quests = Account
				.getQuestInfoBySpecialType(Quest.SPECIAL_TYPE_VIP);
		boolean hasBonus = false;
		if (null != quests && !quests.isEmpty()) {
			QuestInfoClient quest = quests.get(0);
			List<UserVip> list = CacheMgr.userVipCache.getAll();
			for (UserVip vip : list) {
				if (quest.getQuestId() == vip.getQuestId()
						&& Account.getCurVip().getLevel() >= vip.getLevel()) {
					hasBonus = true;
				}
			}

		}
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(1)) {
			if (hasBonus) {
				return true;
			}
		}
		// 包月 不确定
		int leftDays = Account.user.getMonthChargeRewardLeftDays();
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(7)) {
			if (leftDays > 0 && Account.user.rewardToday() == false) {
				return true;
			}
		}
		// 外敌入侵
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(3)) {
			if (Account.user.invasionTimes() > 0) {
				return true;
			}
		}
		// 铜雀台
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(4)) {
			if (Account.user.dongjakTimes() > 0) {
				return true;
			}
		}
		// 凤仪亭
		if (Account.user.realRouletteGoodIsFill() && Account.user.getLevel() >= Account.readLog.getOpenLevel(5)) {
			return true;
		}
		// 双倍奖励
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(6)) {
			if (Account.user.hasDoubleRechrgePrivilege()) {
				return true;
			}
		}
		if (Account.readLog != null) {
			if (ListUtil.isNull(Account.readLog.rewards) == false) {
				for (EventRewards eventRewards : Account.readLog.rewards) {
					if (eventRewards.getType() == EventRewardsUI.TYPE_EVENT) {
						if (eventRewards.getFirstFlash() == false
								&& Account.user.getLevel() >= eventRewards
										.getMinLevel()
								&& eventRewards.getMinLevel() > 1) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	// 有奖励 是否
	private boolean isAward() {
		List<QuestInfoClient> ls = null;
		boolean hasBonus = false;
		// 任务
		ls = Account.getNormalQuest();
		hasBonus = Account.hasBonus(ls);
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(8))
			if (hasBonus) {
				return true;
			}

		// 荣耀榜
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(9))
			if (Account.hasHonorRankReward()) {
				return true;
			}

		// 每日签到
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(12))
			if (Account.user.dailyattendanceTimes() > 0) {
				return true;
			}

		// 我要变强
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(14))
			if (Account.user.stiffenTimes() > 0) {
				return true;
			}

		// 天降横财
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(16)) {
			if (Account.user.godWealthLeftTimes() > 0) {
				return true;
			}
		}
		// 版本更新
		if (Account.readLog != null
				&& Account.user.getLevel() >= Account.readLog.getOpenLevel(15)) {
			if (Account.getUpdateQuest() != null) {
				return true;
			}
		}
		if (Account.readLog != null) {
			if (ListUtil.isNull(Account.readLog.rewards) == false) {
				for (EventRewards eventRewards : Account.readLog.rewards) {
					if (eventRewards.getType() == EventRewardsUI.TYPE_REWARDS
							&& Account.user.getLevel() >= eventRewards
									.getMinLevel()
							&& eventRewards.getMinLevel() > 1) {
						if (eventRewards.getFirstFlash() == false) {
							if (eventRewards.getId() == 15) // 更新
							{
								if (Account.getUpdateQuest() != null) {
									return true;
								}
							} else {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private void setWorldLevelInfo() {
		if (WorldLevelInfoClient.worldLevel < 0)
			return;
		ViewUtil.setText(worldLevelInfo, R.id.worldLevel, "世界等级："
				+ WorldLevelInfoClient.worldLevel + "级");

		ProgressBar bar = (ProgressBar) worldLevelInfo
				.findViewById(R.id.progressBar);
		bar.set(WorldLevelInfoClient.worldLevelProcess,
				WorldLevelInfoClient.worldLevelProcessTotal);
		if (WorldLevelInfoClient.worldLevel == CacheMgr.worldLevelPropCache.maxLevel
				&& WorldLevelInfoClient.worldLevelProcess == WorldLevelInfoClient.worldLevelProcessTotal) {
			ViewUtil.setRichText(worldLevelInfo, R.id.progressDesc,
					StringUtil.color("已达满级", R.color.color19));
		} else {
			ViewUtil.setText(worldLevelInfo, R.id.progressDesc,
					WorldLevelInfoClient.worldLevelProcess + "/"
							+ WorldLevelInfoClient.worldLevelProcessTotal);
		}
		if (null != WorldLevelInfoClient.getWorldLevelProp())
			ViewUtil.setRichText(worldLevelInfo.findViewById(R.id.desc),
					WorldLevelInfoClient.getWorldLevelProp().getDesc());
		else
			ViewUtil.setRichText(worldLevelInfo.findViewById(R.id.desc), "");
	}

	// 界面窗口左列显示的主城信息详情
	public void setCastleInfo() {
		if (null == Account.manorInfoClient)
			return;
		ViewUtil.setText(
				castleInfo,
				R.id.position,
				"坐标："
						+ TileUtil.uniqueMarking(Account.manorInfoClient
								.getPos()));

		if (null != Account.manorInfoClient.getFiefScale())
			ViewUtil.setText(castleInfo, R.id.scaleName, "规模："
					+ Account.manorInfoClient.getFiefScale().getName());
		else
			ViewUtil.setText(castleInfo, R.id.scaleName, "规模：未知");

		ViewUtil.setText(castleInfo, R.id.curArmCount, "驻兵："
				+ Account.manorInfoClient.getCurArmCount());

		ViewUtil.setVisible(castleInfo, R.id.costFood);
		int cost = TroopUtil.costFood(Account.myLordInfo.getTroopInfo());
		ViewUtil.setText(castleInfo, R.id.costFood, "耗粮："
				+ (cost > 0 ? cost : "0") + "/小时"); // "-" +

		ViewUtil.setText(castleInfo, R.id.curPop, "人口："
				+ Account.manorInfoClient.getRealCurPop());

		ViewUtil.setText(castleInfo, R.id.popSpeed, "增长："
				+ Account.manorInfoClient.getPopAddSpeed() + "人/小时");
		ViewUtil.setText(castleInfo, R.id.scale,
				Account.manorInfoClient.getDefenceSkillName());

		// richfiefcache 领地数据没取完 就显示加载
		if (Account.richFiefCache.isFetchOver()) {
			ViewUtil.setText(castleInfo, R.id.fiefCount, "资源点："
					+ Account.richFiefCache.getResourceFiefCount() + "/"
					+ Account.manorInfoClient.getMaxResource());
		} else {
			ViewUtil.setText(castleInfo, R.id.fiefCount, "资源点：加载中");
		}

	}

	// 用户信息详情
	private void setUserInfo() {

		// 用户头像信息
		new UserIconCallBack(Account.user.bref(), icon, Constants.ICON_WIDTH
				* Config.SCALE_FROM_HIGH, Constants.ICON_HEIGHT
				* Config.SCALE_FROM_HIGH);
		ViewUtil.setText(name, Account.user.getNick());

		// 用户所处的国家
		if (StringUtil.isNull(Account.user.bref().getCountryName()))
			ViewUtil.setText(window, R.id.country, "");
		else
			ViewUtil.setText(window, R.id.country, Account.user.bref()
					.getCountryName());

		// 家族信息
		setGuildInfo();

		// 用户等级与资源信息
		ViewUtil.setRichText(level,
				"#lv#" + StringUtil.numImgStr("v", Account.user.getLevel()));
		ViewUtil.setText(
				armCnt,
				CalcUtil.turnToHundredThousand(Account.myLordInfo.getArmCount())
						+ "/"
						+ CalcUtil
								.turnToHundredThousand(Account.manorInfoClient
										.getToplimitArmCount()));
		ViewUtil.setText(money, Account.user.getMoney());
		ViewUtil.setText(currency, Account.user.getCurrency());
		ViewUtil.setText(food, Account.user.getFood());
		UserVip userVip = Account.getCurVip();
		if (null != userVip && userVip.getLevel() >= 1) {
			ImageUtil.setBgNormal(vipIcon);
			ViewUtil.setVisible(vipLevel);
			ViewUtil.setRichText(vipLevel,
					StringUtil.numImgStr("v", userVip.getLevel()));
			ViewUtil.setGone(num0Icon);
		} else {
			ImageUtil.setBgGray(vipIcon);
			ViewUtil.setVisible(num0Icon);
			ImageUtil.setBgGray(num0Icon);
			ViewUtil.setGone(vipLevel);
		}

		progressBar.set(Account.user.getExp(), Account.user.getExpTotal());
		ViewUtil.setText(progressDesc, "经验：" + Account.user.getExp() + "/"
				+ Account.user.getExpTotal());
	}

	// 家族信息
	public void setGuildInfo() {
		RichGuildInfoClient rgic = Account.guildCache.getRichInfoInCache();
		if (null != rgic) {
			ViewUtil.setRichText(guildName, "<U>(" + rgic.getGic().getName() // 家族：
					+ ")</U>");
			guildName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 家族详情
					new GuildInfoWindow().open(Account.guildCache.getGuildid());
				}
			});
		} else {
			ViewUtil.setRichText(guildName, "");// 家族：无
			guildName.setOnClickListener(null);
		}
	}

	@Override
	public void showUI() {
		refreshUI();
		// 遍历及购买建筑
		// if (null == checkInvoker)
		// checkInvoker = new CheckInvoker();
		// if (!checkInvoker.isRunning())
		// checkInvoker.start();
		StepMgr.checkCastleStep();

	}

	private void setProtected() {
		if (Account.user.getLevel() < CacheMgr.userCommonCache
				.getNewerMinLevel()) { // 新手保护
			ViewUtil.setGone(protectedIcon);
		} else {
			ViewUtil.setVisible(protectedIcon);
		}
	}

	@Override
	public void hideUI() {
		setRightTopInfoVisible();
		super.hideUI();
	}

	public void setRightTopInfoVisible() {
		ViewUtil.setVisible(findViewById(R.id.rightInfo));
		ViewUtil.setGone(findViewById(R.id.popupMenu));
	}

	@Override
	protected int refreshInterval() {
		if (Account.user.isWeak())
			return 1000;
		else
			return 30 * 1000;
	}

	private void clearUI() {
		if (ViewUtil.isVisible(castleInfoLayout)) {
			ViewUtil.setGone(castleInfoLayout);
			ViewUtil.setVisible(infoPopupBt);
		}
	}

	@Override
	public void onClick(View v) {
		// 弹出菜单
		if (v == systemBt) {
			ViewUtil.toggleVisible(window.findViewById(R.id.popupMenu),
					rightInfoLayout);
		} else {
			ViewUtil.setGone(window.findViewById(R.id.popupMenu));
		}
		// 主城信息
		if (v == infoPopupBt) {
			ViewUtil.setVisible(castleInfoLayout);
			castleInfoLayout.startAnimation(leftAnim);
		} else if (v == castleInfoLayout) {
			castleInfoLayout.startAnimation(rightAnim);
		} else {
			if (ViewUtil.isVisible(castleInfoLayout)) {
				ViewUtil.setGone(castleInfoLayout);
				ViewUtil.setVisible(infoPopupBt);
			}
		}

		if (v == eventBtn && !eventRewardUI.isShowEvent()) {
			event_tip.clearAnimation();
			event_tip.setVisibility(View.INVISIBLE);
			event_tip.setTag(1);

			if (isAward()) {
				rward_tip.setTag(0);
				rward_tip.setVisibility(View.VISIBLE);
				rward_tip.startAnimation(AnimPool.getAwardTip());
			}

			eventRewardUI.open(EventRewardsUI.TYPE_EVENT);
		} else if (v == rewardsBtn && !eventRewardUI.isShowRewards()) {
			rward_tip.clearAnimation();
			rward_tip.setVisibility(View.INVISIBLE);
			rward_tip.setTag(1);

			if (isActive()) {
				event_tip.setTag(0);
				event_tip.setVisibility(View.VISIBLE);
				event_tip.startAnimation(AnimPool.getAwardTip());
			}

			eventRewardUI.open(EventRewardsUI.TYPE_REWARDS);
		} else {
			eventRewardUI.close();
			if (v == rewardsBtn) {
				rward_tip.setTag(0);
				if (isAward()) {
					ViewUtil.setVisible(rward_tip);
					rward_tip.startAnimation(AnimPool.getAwardTip());
				}
				if (isActive()) {
					ViewUtil.setVisible(event_tip);
					event_tip.startAnimation(AnimPool.getAwardTip());
				}
			} else if (v == eventBtn) {
				event_tip.setTag(0);
				if (isActive()) {
					ViewUtil.setVisible(event_tip);
					event_tip.startAnimation(AnimPool.getAwardTip());
				}
				if (isAward()) {
					ViewUtil.setVisible(rward_tip);
					rward_tip.startAnimation(AnimPool.getAwardTip());
				}

			}
		}

		if (v == icon) {
			controller.openUserInfo();
		} else if (v == vipLayout) {
			controller.openVipListWindow();
		} else if (v == rechargeBt) {
			new RechargeCenterWindow().open();
		} else if (v == campaignBt) {
			controller.openActTypeWindow();
		} else if (v == worldBt) {
			if (Account.user.getLevel() < UIChecker.FUNC_BATTLE) {
				if (Account.getCurVip() == null
						|| Account.getCurVip().getLevel() <= 0) {
					new ToMapTip().show();
				} else {
					controller.alert("出征功能" + UIChecker.FUNC_BATTLE + "级开启");
				}

				return;
			}
			controller.getFiefMap().open();
		} else if (v == storeBt) {
			controller.openStore(0);
		} else if (v == shopBt) {
			controller.openShop();
		} else if (v == firendBt) {
			controller.openFriendsWindow();
		} else if (v == chatBt) {
			controller.openChatUserListWindow();
		} else if (v == guildBt) {
			if (Account.user.hasGuild()) {
				// 进入家族信息界面
				new GuildInfoWindow().open(Account.guildCache.getGuildid());
			} else {
				// 没有家族
				new GuildBuildWindow().open();
			}
		} else if (v == rankBt) {
			new RankEntryWindow().doOpen();
		} else if (v == moreBt) {
			new MoreFuncWindow().doOpen();
		} else if (v == troopBt) {
			new ReviewArmInManorListWindow().open();
		} else if (v == weakLayout) {
			// 虚弱弹出框
			new WeakStateTip(Account.user, Account.manorInfoClient,
					Account.user.getButcherId()).show();
		} else if (v == protectedIcon) {
			new ProtectedTip().show();
		}
	}

	private class LayDownInvoker extends BackgroundInvoker {
		private long pos = TileUtil.toTileId(ctr.getCurLocation());
		private ManorLayDownResp layDownResp;

		@Override
		protected void fire() throws GameException {
			int count = 3;
			while (!Account.manorInfoClient.isLaydown() && count > 0) {
				count--;
				try {
					ManorRecommendEmptySpaceResp resp = GameBiz.getInstance()
							.manorRecommendEmptySpace(pos);
					layDownResp = GameBiz.getInstance().manorLayDown(
							resp.getPos());
				} catch (GameException e) {
					Log.e("LayDownInvoker", e.getMessage());
					if (count <= 0)
						throw e;
				}
			}

		}

		@Override
		protected void onOK() {
			ctr.updateUI(layDownResp.getRi(), true);
		}

	}

	public void moveToBuilding(int buildingType) {
		if (null != castleUI) {
			castleUI.moveToBuilding(buildingType);
		}
	}

	@Override
	protected void refreshUI() {

		// 出现动画效果
		boolean anim = false;
		if (!isShow()) {
			anim = true;
		}
		super.showUI();
		castleUI.update(Account.manorInfoClient);// 更新主城界面

		// 世界聊天
		NotifyWorldChatMsg notifyWorldChatMsg = controller
				.getNotifyWorldChatMsg();
		if (null != notifyWorldChatMsg)
			notifyWorldChatMsg.setMarginTop(175);

		setValue();
		controller.setBackBt(false);
		if (anim) {
			window.findViewById(R.id.topInfo).startAnimation(topAnim);
			window.findViewById(R.id.ownBtnLayout).startAnimation(downAnim);
		}
		// 落地
		if (!Account.manorInfoClient.isLaydown()
				&& Account.user.getLevel() >= UIChecker.FUNC_BATTLE)
			new LayDownInvoker().start();

		setProtected();
		// 虚弱时间
		int weakLeftTime = Account.user.getWeakLeftTime();
		if (weakLeftTime > 0) {
			castleUI.setWeakState(true);
			ViewUtil.setVisible(weakLayout);
			ViewUtil.setText(weakTime,
					"虚弱状态还剩" + DateUtil.formatHour(weakLeftTime));
		} else {
			castleUI.setWeakState(false);
			ViewUtil.setGone(weakLayout);
		}
	}

	private class CheckInvoker extends BackgroundInvoker {
		private boolean running;

		public boolean isRunning() {
			return running;
		}

		@Override
		protected void beforeFire() {
			running = true;
		}

		@Override
		protected void afterFire() {
			running = false;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void fire() throws GameException {

			// 遍历，如果够等级购买建筑
			List<ManorLocation> list = CacheMgr.manorLocationCache.getAll();
			for (ManorLocation ml : list) {
				// 等级不够，未解锁
				if (ml.getLevel() > Account.user.getLevel())
					continue;
				// 已经拥有该建筑
				if (null != Account.manorInfoClient.getBuilding(ml.getType()))
					continue;
				// 购买
				BuildingProp prop = CacheMgr.buildingPropCache
						.getInitialBuildingByType(ml.getType());

				// 当购买建筑是医馆的时候还需要判断VIP等级是否符合条件
				if (BuildingProp.BUILDING_TYPE_REVIVE == prop.getType()) {
					List<BuildingRequirement> requirements = CacheMgr.buildingRequireMentCache
							.search(prop.getId());
					for (BuildingRequirement requirement : requirements) {
						if (requirement.getType() == BuildingRequirement.TYPE_VIPS) {
							if (requirement.getValue() <= Account.getCurVip()
									.getLevel()) {
								GameBiz.getInstance().buildingBuy(
										Constants.TYPE_NOAMRL, prop.getId());
							}
						}
					}
				} else if (null != prop) {// 林洋会提供新的批量购买的接口
					GameBiz.getInstance().buildingBuy(Constants.TYPE_NOAMRL,
							prop.getId());
				}
			}
		}

		@Override
		protected void onOK() {
			if (null != castleUI)
				castleUI.update(Account.manorInfoClient);
		}

	}
}
