package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

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

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.Constants;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.model.WorldLevelInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.CastleUI;
import com.vikings.sanguo.ui.DragLayout;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.ExpeditionTip;
import com.vikings.sanguo.ui.alert.FavorFiefSearchTip;
import com.vikings.sanguo.ui.alert.RechargeTip;
import com.vikings.sanguo.ui.alert.SocialTip;
import com.vikings.sanguo.ui.alert.WeakStateTip;
import com.vikings.sanguo.ui.guide.UIChecker;
import com.vikings.sanguo.ui.listener.BuildingOtherOnClick;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class OthersCastleWindow extends PopupWindow implements OnClickListener {

	private ViewGroup window, ownBtnLayout, otherBtnLayout, weakLayout,
			infoPopupBt, castleInfoLayout, castleInfo, worldLevelInfo,
			rightInfoLayout;

	private ImageView icon, vipIcon, num0Icon;
	private TextView name, level, armCnt, money, food, vipLevel, guildName,
			weakTime, progressDesc;
	private View socialBt, attackBt, historyBt, fiefBt, troopBt, leaveBt;

	private ProgressBar progressBar;

	private CastleUI castleUI;

	private Animation leftAnim, rightAnim;// 动画

	private int userId;
	private OtherUserClient ouc;
	private BriefFiefInfoClient bfic;

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void init() {
		window = (ViewGroup) Config.getController().inflate(R.layout.castle);
		controller.addContentFullScreen(window);

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
		ViewUtil.setGone(ownBtnLayout);
		otherBtnLayout = (ViewGroup) window.findViewById(R.id.otherBtnLayout);
		ViewUtil.setVisible(otherBtnLayout);
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
		ViewUtil.setGone(rightInfoLayout);

		weakLayout = (ViewGroup) window.findViewById(R.id.weakLayout);
		weakLayout.setOnClickListener(this);

		icon = (ImageView) window.findViewById(R.id.icon);
		icon.setOnClickListener(this);
		ViewUtil.setImage(castleInfo.findViewById(R.id.pattenMirror),
				ImageUtil.getMirrorBitmapDrawable("pattern_line.png"));
		ViewUtil.setImage(worldLevelInfo.findViewById(R.id.pattenMirror),
				ImageUtil.getMirrorBitmapDrawable("pattern_line.png"));

		name = (TextView) window.findViewById(R.id.name);
		level = (TextView) window.findViewById(R.id.level);
		armCnt = (TextView) window.findViewById(R.id.armCnt);
		money = (TextView) window.findViewById(R.id.money);
		ViewUtil.setHide(window, R.id.currencyLayout);
		food = (TextView) window.findViewById(R.id.food);

		vipIcon = (ImageView) window.findViewById(R.id.vipIcon);
		num0Icon = (ImageView) window.findViewById(R.id.num0Icon);
		vipLevel = (TextView) window.findViewById(R.id.vipLevel);
		guildName = (TextView) window.findViewById(R.id.guildName);
		weakTime = (TextView) window.findViewById(R.id.weakTime);
		progressDesc = (TextView) window.findViewById(R.id.progressDesc);

		progressBar = (ProgressBar) window.findViewById(R.id.progressBar);// 经验进度条

		socialBt = otherBtnLayout.findViewById(R.id.socialBt);
		socialBt.setOnClickListener(this);

		attackBt = otherBtnLayout.findViewById(R.id.plunderBt);
		attackBt.setOnClickListener(this);

		historyBt = otherBtnLayout.findViewById(R.id.historyBt);
		historyBt.setOnClickListener(this);

		fiefBt = otherBtnLayout.findViewById(R.id.fiefBt);
		fiefBt.setOnClickListener(this);

		troopBt = otherBtnLayout.findViewById(R.id.troopBt);
		troopBt.setOnClickListener(this);

		leaveBt = otherBtnLayout.findViewById(R.id.leaveBt);
		leaveBt.setOnClickListener(this);

		castleUI = new CastleUI(
				(DragLayout) window.findViewById(R.id.castleUI),
				new BuildingOtherOnClick(), null, false);
	}

	@Override
	protected void destory() {
		Config.getController().removeContentFullScreen(window);
	}

	public void open(int userId) {
		this.userId = userId;
		new QueryInvoker().start();
	}

	public void open(OtherUserClient ouc) {
		this.userId = ouc.getId().intValue();
		this.ouc = ouc;
		doOpen();
	}

	public void setValue() {
		setUserInfo();
		setCastleInfo();
		setWorldLevelInfo();
	}

	private void setWorldLevelInfo() {
		if (WorldLevelInfoClient.worldLevel <= 0)
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

	private void setCastleInfo() {
		if (null == ouc.getManor())
			return;
		ManorInfoClient mic = ouc.getManor();
		ViewUtil.setText(window, R.id.position,
				"坐标：" + TileUtil.uniqueMarking(mic.getPos()));

		ViewUtil.setText(window, R.id.scaleName, "规模："
				+ mic.getFiefScale().getName());

		ViewUtil.setText(window, R.id.curArmCount, "驻兵：" + mic.getCurArmCount());

		ViewUtil.setGone(window, R.id.costFood);

		ViewUtil.setText(window, R.id.curPop, "人口：" + mic.getRealCurPop(ouc));

		ViewUtil.setText(window, R.id.popSpeed, "增长：" + mic.getPopAddSpeed()
				+ "人/小时");

		ViewUtil.setText(window, R.id.scale, mic.getDefenceSkillName());

		// 2013年6月20日17:47:03于林洋确认，不管落地与否，fiefcount都不包含主城
		ViewUtil.setText(window, R.id.fiefCount,
				"资源点：" + ouc.getLord().getInfo().getSiteCount().intValue()
						+ "/" + mic.getMaxResource());
	}

	private void setUserInfo() {
		new UserIconCallBack(ouc.bref(), icon,
				com.vikings.sanguo.Constants.ICON_WIDTH
						* Config.SCALE_FROM_HIGH,
				com.vikings.sanguo.Constants.ICON_HEIGHT
						* Config.SCALE_FROM_HIGH);
		ViewUtil.setText(name, ouc.getNick());
		ViewUtil.setText(window, R.id.country, ouc.bref().getCountryName());
		ViewUtil.setRichText(level,
				"#lv#" + StringUtil.numImgStr("v", ouc.getLevel()));
		ViewUtil.setText(armCnt, ouc.getLord().getArmCount());
		ViewUtil.setText(money, ouc.getMoney());
		ViewUtil.setText(food, ouc.getFood());

		if (ouc.hasGuild() && null != ouc.getBgic()) {
			ViewUtil.setRichText(guildName, "<U>" + ouc.getBgic().getName()
					+ "</U>");
			guildName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new GuildInfoWindow().open(ouc.getGuildid().intValue());
				}
			});
		} else {
			ViewUtil.setText(guildName, ""); //
		}

		UserVip vipCfg = ouc.getCurVip();
		if (null != vipCfg && vipCfg.getLevel() >= 1) {
			ImageUtil.setBgNormal(vipIcon);
			ViewUtil.setVisible(vipLevel);
			ViewUtil.setRichText(vipLevel,
					StringUtil.numImgStr("v", vipCfg.getLevel()));
			ViewUtil.setGone(num0Icon);
		} else {
			ImageUtil.setBgGray(vipIcon);
			ViewUtil.setVisible(num0Icon);
			ImageUtil.setBgGray(num0Icon);
			ViewUtil.setGone(vipLevel);
		}

		progressBar.set(ouc.getExp(), ouc.getExpTotal());
		ViewUtil.setText(progressDesc,
				"经验：" + ouc.getExp() + "/" + ouc.getExpTotal());
	}

	@Override
	public void showUI() {
		super.showUI();
		castleUI.update(ouc.getManor());
		setValue();
		// 虚弱时间
		int weakLeftTime = ouc.getWeakLeftTime();
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

	@Override
	protected int refreshInterval() {
		if (ouc.isWeak())
			return 1000;
		else
			return 60 * 1000;
	}

	@Override
	public void onClick(View v) {
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

		if (v == attackBt) {

			if (ouc.getLevel() < UIChecker.FUNC_BATTLE)
				controller.alert("不能进攻" + UIChecker.FUNC_BATTLE + "级以下的玩家");
			else {
				if (Account.manorInfoClient.getPos() == 0) {
					controller.alert("当你的等级达到" + UIChecker.FUNC_BATTLE
							+ "级，成功出征世界之后再来吧！");
					return;
				}
				if (ouc.getManor().getPos() == 0) {
					controller.alert("抱歉，无法出征！<br/>对方还没成功出征世界，等他进入世界征战之后再来吧！");
					return;
				}

				if (null != bfic) {
					if (Account.briefBattleInfoCache.battleIds.contains(bfic
							.getId())) {
						controller.openWarInfoWindow(bfic); // , null
						return;
					}

					if (ouc.isDuelProtected()) {
						controller.alert(CacheMgr.errorCodeCache
								.getMsg((short) 1102));
						return;
					}

					new ExpeditionTip(bfic, ouc).show();
				}
			}
		} else if (v == fiefBt) {
			new FavorFiefSearchTip(com.vikings.sanguo.Constants.USER,
					ouc.bref(), new CallBack() {

						@Override
						public void onCall() {
							if (Account.user.getLevel() < UIChecker.FUNC_BATTLE) {
								if (Account.getCurVip() == null
										|| Account.getCurVip().getLevel() <= 0) {
									new RechargeTip("您需要达到"
											+ UIChecker.FUNC_BATTLE
											+ "级才能查看TA的领地位置",
											"充值<param0>元立刻成为VIP会员，打副本享受双倍经验，帮你快速升至10级，早日进入世界征战！")
											.show();
								} else {
									controller.alert("您需要达到"
											+ UIChecker.FUNC_BATTLE
											+ "级才能查看TA的领地位置");
								}
								return;
							}
							controller.closeAllPopup();
							controller.getFiefMap().backToMap();
						}
					}).show();
		} else if (v == icon) {
			controller.openUserInfo(ouc);
		} else if (v == historyBt) {
			if (ouc.getLevel() < UIChecker.FUNC_BATTLE) {
				controller.alert("不能查看" + UIChecker.FUNC_BATTLE + "级以下玩家的战况");
			} else {
				if (null != bfic)
					controller.openHistoryWarInfoWindow(bfic);
			}
		} else if (v == troopBt) {
			new ReviewOtherArmInManorListWindow().open(ouc);
		} else if (v == socialBt) {
			new SocialTip(ouc).show();
		} else if (v == weakLayout) {
			new WeakStateTip(ouc, ouc.getManor(), ouc.getButcherId()).show();
		} else if (v == leaveBt) {
			controller.goBack();
		}
	}

	private class QueryInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "加载中";
		}

		@Override
		protected String failMsg() {
			return "加载数据失败";
		}

		@Override
		protected void fire() throws GameException {
			ouc = GameBiz.getInstance().queryRichOtherUserInfo(
					userId,
					Constants.DATA_TYPE_OTHER_RICHINFO
							| Constants.DATA_TYPE_ARM_PROP);
			if (null != ouc.getManor()) {
				List<Long> ids = new ArrayList<Long>();
				ids.add(ouc.getManor().getPos());
				List<BriefFiefInfoClient> bfics = GameBiz.getInstance()
						.briefFiefInfoQuery(ids);
				if (!bfics.isEmpty()) {
					bfic = bfics.get(0);
				}
			}
			if (ouc.hasGuild())
				ouc.setBgic(CacheMgr.bgicCache.get(ouc.getOtherUserInfo()
						.getGuildid().intValue()));
			else
				ouc.setBgic(null);
		}

		@Override
		protected void onOK() {
			doOpen();
		}

	}
}
