package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.battle.anim.Anim;
import com.vikings.sanguo.battle.anim.AnimPool;
import com.vikings.sanguo.battle.anim.ArcherLongAtkAnim;
import com.vikings.sanguo.battle.anim.ArmSkillNameAnim;
import com.vikings.sanguo.battle.anim.BaseAnim;
import com.vikings.sanguo.battle.anim.BaseDrawableAnim;
import com.vikings.sanguo.battle.anim.BattleAnimList;
import com.vikings.sanguo.battle.anim.BattleDriver;
import com.vikings.sanguo.battle.anim.BuffDisappearAnim;
import com.vikings.sanguo.battle.anim.ClearArmSkill;
import com.vikings.sanguo.battle.anim.ClearSkill;
import com.vikings.sanguo.battle.anim.DrawableAnimationBasis;
import com.vikings.sanguo.battle.anim.EffectAttackAnim;
import com.vikings.sanguo.battle.anim.EscapeAnim;
import com.vikings.sanguo.battle.anim.FallDownAnim;
import com.vikings.sanguo.battle.anim.ForceAttack;
import com.vikings.sanguo.battle.anim.GetBeatenDrawableAnimation;
import com.vikings.sanguo.battle.anim.GroupHurtAnim;
import com.vikings.sanguo.battle.anim.HeroIconAnim;
import com.vikings.sanguo.battle.anim.HeroSkillAnim;
import com.vikings.sanguo.battle.anim.HigherSkillAnim;
import com.vikings.sanguo.battle.anim.MagicLongAtkAnim;
import com.vikings.sanguo.battle.anim.MissionAnim;
import com.vikings.sanguo.battle.anim.ModifyBattleWindow;
import com.vikings.sanguo.battle.anim.ReachAtkPos;
import com.vikings.sanguo.battle.anim.SetAmyIcon;
import com.vikings.sanguo.battle.anim.SetSkillBg;
import com.vikings.sanguo.battle.anim.SetSkillIcon;
import com.vikings.sanguo.battle.anim.ShortRangeAtk;
import com.vikings.sanguo.battle.anim.ShortRangeAtkBounce;
import com.vikings.sanguo.battle.anim.ShowHpAnimation;
import com.vikings.sanguo.battle.anim.ShowRoundAnim;
import com.vikings.sanguo.battle.anim.SkillBg;
import com.vikings.sanguo.battle.anim.SkillNameAnim;
import com.vikings.sanguo.battle.anim.TopEffectAnim;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleAnimEffects;
import com.vikings.sanguo.model.BattleArrayInfoClient;
import com.vikings.sanguo.model.BattleLogHeroInfoClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Buff;
import com.vikings.sanguo.model.HeroArmPropClient;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.protos.BattleEventArmInfo;
import com.vikings.sanguo.protos.BattleEventInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.sound.MediaPlayerMgr;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.AbnormalFrameLayout;
import com.vikings.sanguo.ui.AbnormalFrameLayout.RecyleResource;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.TroopDetailTip;
import com.vikings.sanguo.ui.alert.VipFailTip;
import com.vikings.sanguo.ui.listener.BattleHeroClickListener;
import com.vikings.sanguo.utils.BattleCoordUtil;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.utils.VipUtil;
import com.vikings.sanguo.widget.SlowOnClick;

//自己永远在下方  把原来的变量都改成上方 下方  把isLeft改成  isDown
public class BattleWindow extends PopupWindow implements ModifyBattleWindow {
	private AbnormalFrameLayout window;
	private BattleDriver battleDriver;
	private BattleLogInfoClient battleLogInfo;
	private BattleAnimList battleAnim;
	private View skip;
	private static final int AMY_END = -1; // -1表示这支部队不存在，全灭或逃走
	private CallBack callBackAfterAnim;

	private ViewGroup downAmy; // 自己永远在下方 把原来的变量都改成上方 下方
	private ViewGroup upAmy; // 对方永远在上方
	private ViewGroup downAmyFrame; // 自己 below
	private ViewGroup upAmyFrame;
	private TextView downAmyCnt;
	private TextView upAmyCnt;

	private TextView downAmyName;
	private TextView upAmyName;

	private TextView Eff;

	private boolean isSkip = true;
	private boolean isResultAnimOver = false;
	private boolean isLog = false; // 是否日志回放动画

	private ViewGroup downScroll_layout;
	private ViewGroup upScroll_layout;
	private ViewGroup down_officers_layout;
	private ViewGroup up_officers_layout;

	private HorizontalScrollView downScroll;

	private FrameLayout armSkillLayout;

	private LinearLayout downAmyHPInfo;
	private LinearLayout upAmyHPInfo;

	private TextView downName;
	private TextView upName;

	private ImageView specialEffects;

	private int oldRank = -1;
	private int newRank = -1;

	private boolean isBlood; // 是否是血战

	private int upHeroFrameX;
	private int downHeroFrameX;

	private boolean isGuide;

	// private final String TAG = "BattleWindow";
	private Drawable downWallDrawable = null;
	private Drawable upWallDrawable = null;
	private Drawable flyDrawable = null;
	private Drawable skillBgDrawable = null;

	private List<ImageView> downAmyViewsPool = new ArrayList<ImageView>();
	private List<ImageView> upAmyViewsPool = new ArrayList<ImageView>();

	@Override
	protected void bindField() {
	}

	public void open(BattleDriver battleDriver, CallBack callBackAfterAnim,
			boolean isLog) {
		this.isLog = isLog;
		this.open(battleDriver, callBackAfterAnim);
	}

	public void open(BattleDriver battleDriver, CallBack callBackAfterAnim) {
		this.battleDriver = battleDriver;
		this.battleDriver.setModifyBattleWindow(this);
		battleLogInfo = battleDriver.getBlic();
		this.callBackAfterAnim = callBackAfterAnim;
		doOpen();
	}

	public void open(boolean isGuide, BattleDriver battleDriver,
			CallBack callBackAfterAnim) {
		this.battleDriver = battleDriver;
		this.battleDriver.setModifyBattleWindow(this);
		battleLogInfo = battleDriver.getBlic();
		this.callBackAfterAnim = callBackAfterAnim;
		this.isGuide = isGuide;
		doOpen();
	}

	public void open(BattleDriver battleDriver, CallBack callBackAfterAnim,
			boolean isLog, int oldRank, int newRank) {
		this.oldRank = oldRank;
		this.newRank = newRank;
		open(battleDriver, callBackAfterAnim, isLog);
	}

	public void open(BattleDriver battleDriver, CallBack callBackAfterAnim,
			boolean isLog, boolean isBlood) {
		this.isBlood = isBlood;
		open(battleDriver, callBackAfterAnim, isLog);
	}

	@Override
	public void showUI() {
		super.showUI();
		controller.hideIconForFullScreen();
		if (isResultAnimOver || (!isSkip))
			controller.goBack();
	}

	@Override
	public void hideUI() {
		super.hideUI();
		controller.showIconForFullScreen();
	}

	@Override
	protected void init() {
		System.gc();
		window = (AbnormalFrameLayout) controller
				.inflate(R.layout.combat_field);
		window.setRecyleResourceListener(mRecyleResource);

		FileAccess fa = Config.getController().getFileAccess();
		if (fa.checkImageExist(battleLogInfo.getBgName()) == false) {
			Drawable bg = Config.getController().getDrawableHdInBattle(
					"battle_map.jpg", ImageUtil.RGB565);
			if (bg == null) {
				System.gc();
			} else {
				window.findViewById(R.id.bg).setBackgroundDrawable(bg);
			}
		} else {
			Drawable bg = Config.getController().getDrawableHdInBattle(
					battleLogInfo.getBgName(), ImageUtil.RGB565);
			if (bg == null) {
				System.gc();
			} else {
				window.findViewById(R.id.bg).setBackgroundDrawable(bg);
			}
		}
		if (StringUtil.isNull(battleLogInfo.getWall()) == false) {
			if (battleLogInfo.isDownAtk()) {
				window.findViewById(R.id.upAmyWall).setVisibility(View.VISIBLE);
				downWallDrawable = Config.getController().getDrawable(
						battleLogInfo.getWall());
				if (downWallDrawable != null) {
					ViewUtil.setImage(window, R.id.upAmyWall, downWallDrawable);
				}
			} else {
				window.findViewById(R.id.downAmyWall).setVisibility(
						View.VISIBLE);
				upWallDrawable = Config.getController().getDrawable(
						battleLogInfo.getWall());
				if (upWallDrawable != null) {
					ViewUtil.setImage(window, R.id.downAmyWall, upWallDrawable);
				}
			}
		}
		battleAnim = new BattleAnimList(battleDriver, callBackAfterAnim,
				new CallBack() {
					@Override
					public void onCall() {
						clearAnimation();
						isResultAnimOver = true;

						// // 战斗结束主动清理内存
						System.gc();
					}
				});
		upName = (TextView) findViewById(R.id.upName);
		downName = (TextView) findViewById(R.id.downName);

		// 设置我方昵称
		BriefUserInfoClient main = battleLogInfo.getDownSide().getMainFighter();
		if (null != main) {
			StringBuilder buf = new StringBuilder(main.getNickName() + "("
					+ battleLogInfo.getDownSide().getTotalTroopAmount() + ")");

			downName.setText(buf);

			int resId = battleLogInfo.isDownAtk() ? R.drawable.btl_atk
					: R.drawable.btl_def;
			ViewUtil.setImage(findViewById(R.id.defAttr), resId);
		}

		// 设置敌方头像,昵称
		BriefUserInfoClient sub = battleLogInfo.getUpSide().getMainFighter();
		if (null != sub) {
			StringBuilder buf = new StringBuilder(sub.getNickName() + "("
					+ battleLogInfo.getUpSide().getTotalTroopAmount() + ")");

			upName.setText(buf);

			int resId = battleLogInfo.isDownAtk() ? R.drawable.btl_def
					: R.drawable.btl_atk;
			ViewUtil.setImage(findViewById(R.id.atkAttr), resId);
		}

		skip = findViewById(R.id.skip);
		skip.setOnClickListener(new SlowOnClick() {
			@Override
			public void doOnClick(View v) {
				System.gc();
				int animVip = VipUtil.skipAnimLevel();
				if (Account.getCurVip().getLevel() >= animVip) {
					if (isSkip) {
						isSkip = false;
						exit();
					}
				} else {
					// UserVip curCfg = Account.getCurVip();
					UserVip vip = CacheMgr.userVipCache.getVipByLvl(animVip);
					new VipFailTip(vip.getLevel(), true).show();
					// if (curCfg == null || curCfg.getLevel() <= 0) {
					//
					// new RechargeTip("成为VIP会员立刻跳过动画",
					// "充值<param0>元 ，立刻成为VIP会员，轻松跳过战斗动画，从此享受飞一般的极致快感！")
					// .show();
					// } else {
					//
					// int diff = vip.getCharge() - Account.user.getCharge();
					// int amt = diff / 100;
					// amt += (0 != diff % 100) ? 1 : 0;
					//
					// StringBuilder buf = new StringBuilder("只有VIP");
					// if (animVip > 1)
					// buf.append(animVip);
					//
					// buf.append("会员才可以跳过动画").append("只要再充").append(amt)
					// .append("元立刻可以成为VIP");
					//
					// if (animVip > 1)
					// buf.append(animVip);
					// controller.alert(buf.toString());
					// }
				}
			}
		});

		upAmy = (ViewGroup) findViewById(R.id.upAmy);
		downAmy = (ViewGroup) findViewById(R.id.downAmy);

		downAmyFrame = (ViewGroup) findViewById(R.id.downAmyFrame);
		upAmyFrame = (ViewGroup) findViewById(R.id.upAmyFrame);

		downAmyCnt = (TextView) findViewById(R.id.downAmyCnt);
		upAmyCnt = (TextView) findViewById(R.id.upAmyCnt);

		downAmyName = (TextView) findViewById(R.id.downAmyName);
		upAmyName = (TextView) findViewById(R.id.upAmyName);

		Eff = (TextView) findViewById(R.id.Eff);

		specialEffects = (ImageView) findViewById(R.id.special_effects);

		downScroll_layout = (ViewGroup) findViewById(R.id.downScroll_layout);
		upScroll_layout = (ViewGroup) findViewById(R.id.upScroll_layout);
		up_officers_layout = (ViewGroup) findViewById(R.id.up_officers_layout);
		down_officers_layout = (ViewGroup) findViewById(R.id.down_officers_layout);

		Drawable troopBgDrawable = Config.getController().getDrawable(
				"troop_info_bg");
		if (troopBgDrawable != null) {
			upScroll_layout.setBackgroundDrawable(troopBgDrawable);
		}

		Drawable officerBgDrawable = Config.getController().getDrawable(
				"officers_bg");
		if (officerBgDrawable != null) {
			up_officers_layout.setBackgroundDrawable(officerBgDrawable);
		}

		Drawable downScrollDrawable = ImageUtil.getRotateBitmapDrawable(
				"troop_info_bg.png", 180);
		if (downScrollDrawable != null) {
			downScroll_layout.setBackgroundDrawable(downScrollDrawable);
		}
		Drawable down_officersDrawable = ImageUtil.getRotateBitmapDrawable(
				"officers_bg.png", 180);
		if (down_officersDrawable != null) {
			down_officers_layout.setBackgroundDrawable(down_officersDrawable);
		}

		downScroll = (HorizontalScrollView) findViewById(R.id.downScroll);

		upAmyHPInfo = (LinearLayout) findViewById(R.id.upAmyHPInfo);
		downAmyHPInfo = (LinearLayout) findViewById(R.id.downAmyHPInfo);

		armSkillLayout = (FrameLayout) findViewById(R.id.armSkillLayout);

		MediaPlayerMgr.getInstance().startSound(R.raw.battle_default);

		controller.addContentFullScreen(window);

		// 189是英雄框宽度
		upHeroFrameX = (int) (189 * Config.SCALE_FROM_HIGH);// location[0];
		downHeroFrameX = (int) (Config.screenWidth - 189 * Config.SCALE_FROM_HIGH);// location[0];

		BattleCoordUtil.initBattleCoorDate();

		initAmyPos();
		battleAnim.play();
		setSkillNameGradient();
	}

	private void initAmyPos() {
		for (int i = 0; i < 12; i++) {
			ImageView arm = new ImageView(Config.getController().getUIContext());
			arm.setScaleType(ScaleType.CENTER_INSIDE);
			downAmyViewsPool.add(arm);
		}
		for (int i = 0; i < 12; i++) {
			ImageView arm = new ImageView(Config.getController().getUIContext());
			arm.setScaleType(ScaleType.CENTER_INSIDE);
			upAmyViewsPool.add(arm);
		}
	}

	private void setSkillNameGradient() {

	}

	@Override
	protected void destory() {
		controller.removeContentFullScreen(window);
		controller.showIconForFullScreen();
		battleAnim.setSkip();
		releaseWidgetBitmap();
		// release();
		// System.gc();
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	public void setTroopInfo(List<BattleArrayInfoClient> list,
			final List<BattleLogHeroInfoClient> blhic, boolean isDown) {
		LinearLayout amy = isDown ? (LinearLayout) window
				.findViewById(R.id.downAmy) : (LinearLayout) window
				.findViewById(R.id.upAmy);
		if (!ListUtil.isNull(blhic)) {
			boolean isSecond = false; // 主将
			for (int i = 0; i < blhic.size(); i++) {

				BattleLogHeroInfoClient battleLogHeroInfoClient = blhic.get(i);
				if (battleLogHeroInfoClient.getRole() == 1
						|| battleLogHeroInfoClient.getRole() == 3) {
					ViewGroup officer_icon = isDown ? (ViewGroup) down_officers_layout
							.findViewById(R.id.major_icon)
							: (ViewGroup) up_officers_layout
									.findViewById(R.id.major_icon);
					setOfficersGrid(battleLogHeroInfoClient, officer_icon,
							false);
				} else {
					// 第一个副
					if (!isSecond) {
						ViewGroup second_officers_purple = (isDown ? (ViewGroup) down_officers_layout
								.findViewById(R.id.second_officer_layout)
								: (ViewGroup) up_officers_layout
										.findViewById(R.id.second_officer_layout));
						setOfficersGrid(battleLogHeroInfoClient,
								second_officers_purple, true);
						isSecond = true;
					} else {
						ViewGroup second_officers_two = (isDown ? (ViewGroup) down_officers_layout
								.findViewById(R.id.second_officers_two)
								: (ViewGroup) up_officers_layout
										.findViewById(R.id.second_officers_two));
						setOfficersGrid(battleLogHeroInfoClient,
								second_officers_two, true);
					}
				}
			}
		}
		if (ListUtil.isNull(list))
			return;
	}

	public void setAmyOrder(List<BattleArrayInfoClient> list,
			final List<BattleLogHeroInfoClient> blhic, boolean isDown,
			final BattleSkill fiefBattleSkill,
			final UserTroopEffectInfo userTroopEffectInfo) {

		LinearLayout amy = isDown ? (LinearLayout) window
				.findViewById(R.id.downAmy) : (LinearLayout) window
				.findViewById(R.id.upAmy);
		if (ListUtil.isNull(list)) {
			return;
		}
		amy.removeAllViews();
		if (isDown == true) {
			addLeftTroopInfoEx(list, blhic, amy, isDown, fiefBattleSkill,
					userTroopEffectInfo);
		} else {
			addRightTroopInfoEx(list, blhic, amy, isDown, fiefBattleSkill,
					userTroopEffectInfo);
		}
	}

	// 设置将领头像 副将的图标为58 *58
	private void setOfficersGrid(BattleLogHeroInfoClient blhic,
			ViewGroup iconLayout, boolean isLegate) {
		if (null == blhic)
			return;
		IconUtil.setBattleHeroIcon(iconLayout, blhic, isLegate);

		iconLayout.setTag(blhic.getHeroId());
		iconLayout.setOnClickListener(new BattleHeroClickListener(blhic));
		return;
	}

	// 设置将领技能 现在不用
	@Override
	public void setHeroSkill(List<BattleLogHeroInfoClient> blhic,
			boolean isDown, int type) {
	}

	@Override
	public void setSelTroop(boolean isDown, int id, boolean isTarget) { // BattleEventInfo
																		// info
		ViewGroup vg = isDown ? downAmy : upAmy;

		for (int i = 0; i < vg.getChildCount(); i++) {
			View child = vg.getChildAt(i);
			if (!(child instanceof ImageView)) {
				Object tag = vg.getChildAt(i).getTag();
				if (null != tag && id == (Integer) tag) {
					ViewUtil.setVisible(child.findViewById(R.id.selBg));
					ViewUtil.setVisible(child.findViewById(R.id.state));
					// 设置行动者或目标
					View role = child.findViewById(R.id.role);
					if (isTarget)
						role.setBackgroundResource(R.drawable.btl_target);
					else
						role.setBackgroundResource(R.drawable.btl_active);
				} else {
					if (AMY_END != (Integer) tag) {
						ViewUtil.setHide(child.findViewById(R.id.selBg));
						ViewUtil.setHide(child.findViewById(R.id.state));
					}
				}
			}
		}
	}

	@Override
	public void modifyTroopAmount(boolean isDown, long value, String name) {
		if (isDown) {
			downAmyName.setText(name);
			downAmyCnt.setText(/* name + */"(" + value + ")");
			downName.setText(battleLogInfo.getNickName(isDown) + "("
					+ battleLogInfo.getCurrTroopAmount(isDown) + ")");
		} else {
			upAmyName.setText(name);
			upAmyCnt.setText(/* name + */"(" + value + ")");
			upName.setText(battleLogInfo.getNickName(isDown) + "("
					+ battleLogInfo.getCurrTroopAmount(isDown) + ")");
		}
	}

	@Override
	public void clearTroopAmount(boolean isDown) {
		View v = isDown ? findViewById(R.id.downAmyCnt)
				: findViewById(R.id.upAmyCnt);
		View name = isDown ? findViewById(R.id.downAmyName)
				: findViewById(R.id.upAmyName);
		ViewUtil.setRichText(v, "");
		ViewUtil.setRichText(name, "");
	}

	@Override
	public BaseAnim handleBuf(boolean isDown, Animation anim, int troopId,
			Drawable d) {
		ViewGroup vg = getSelTroopGrid(isDown, troopId);
		if (null == vg)
			return null;

		ImageView iv = (ImageView) vg.findViewById(R.id.skill);

		int[] location = new int[2];
		vg.getLocationOnScreen(location);
		if (isDown) {
			if (location[0] > downHeroFrameX) {
				return null;
			}
		} else {
			if (location[0] + vg.getWidth() < upHeroFrameX) {
				return null;
			}
		}
		return new BuffDisappearAnim(isDown, window, iv, vg, anim, d);
	}

	//
	@Override
	public ArrayList<BaseAnim> groupHurt(boolean isDown,
			List<BattleEventArmInfo> ls,
			List<BattleArrayInfoClient> infoClients, boolean isOwner) {
		ViewGroup vg = isDown ? downAmy : upAmy;
		ArrayList<BaseAnim> hurtAnimation = new ArrayList<BaseAnim>();
		for (int i = 0; i < vg.getChildCount(); i++) {
			View child = vg.getChildAt(i);
			if (!(child instanceof ImageView)) {
				Object tag = vg.getChildAt(i).getTag();
				for (int j = 0; j < ls.size(); j++)
				// for (BattleEventArmInfo it : ls)
				{
					BattleEventArmInfo it = ls.get(j);
					if (null != tag && it.getArmid().equals((Integer) tag)) {
						final TextView tv = (TextView) child
								.findViewById(R.id.loss);
						ArrayList<Integer> valLs = new ArrayList<Integer>();
						StringBuilder builder = new StringBuilder();
						if (isOwner) {
							if (it.getEx() == 0) {
								continue;
							}
							if (it.getEx() < 100000) // 小于1W
							{
								valLs = CalcUtil
										.parseLong(Math.abs(it.getEx()));
							} else {
								valLs = CalcUtil.parseMoreMill(Math.abs(it
										.getEx()));
							}
							if (it.getEx() > 0) {
								builder.append("#btl_b_add#");
							} else {
								builder.append("#btl_b_minus#");
							}
						} else {
							if (it.getValue() == 0) {
								continue;
							}
							if (it.getValue() < 100000) // 小于1W
							{

								valLs = CalcUtil.parseLong(Math.abs(it
										.getValue()));
							} else {
								valLs = CalcUtil.parseMoreMill(Math.abs(it
										.getValue()));
							}

							builder.append("#btl_b_minus#");
						}
						builder.append(ImageUtil.getNumStr(valLs, "btl_b_"));
						BattleArrayInfoClient baic = infoClients.get(j);
						int num = 0;
						if (infoClients != null) {
							num = (int) baic.getNum();
						}
						final TextView cnt = (TextView) child
								.findViewById(R.id.cnt);

						ViewGroup viewGroup = getSelTroopGrid(isDown,
								baic.getId());
						int[] location = new int[2];
						viewGroup.getLocationOnScreen(location);
						if (!isDown) {
							if (location[0] > downHeroFrameX) {
								continue;
							}
							if (location[0] + viewGroup.getWidth() < 0) {
								continue;
							}
						} else {
							if (location[0] + viewGroup.getWidth() < upHeroFrameX
							/* + left_officers_layout.getWidth() */) {
								continue;
							}
							if (location[0] > Config.screenWidth) {
								continue;
							}
						}

						GroupHurtAnim hurtAnim = new GroupHurtAnim(isDown,
								window, tv, AnimPool.groupHurt(/* isLeft */),
								builder, cnt, num);

						TextView armName = isDown ? downName : upName;
						hurtAnim.setHpInfo(armName, viewGroup, battleLogInfo);
						hurtAnimation.add(hurtAnim);

					}
				}
			}

		}
		return hurtAnimation;
	}

	@Override
	public void clearTroop(BattleEventInfo info) {
		ViewUtil.setHide(downAmyFrame);
		ViewUtil.setHide(upAmyFrame);

		clearTroopImg(downAmyFrame);
		clearTroopImg(upAmyFrame);

		downAmyFrame.clearAnimation();
		upAmyFrame.clearAnimation();
	}

	// 清除士兵图像
	private void clearTroopImg(View v) {
		if (v.getId() == R.id.downAmyFrame) {
			ViewGroup arm = (ViewGroup) v;
			for (int i = 0; i < arm.getChildCount(); i++) {
				View child = arm.getChildAt(i);
				if (child instanceof ImageView) {
					child.setBackgroundDrawable(null);
				}
			}
		} else if (v.getId() == R.id.upAmyFrame) {
			ViewGroup arm = (ViewGroup) v;
			for (int i = 0; i < arm.getChildCount(); i++) {
				View child = arm.getChildAt(i);
				if (child instanceof ImageView) {
					child.setBackgroundDrawable(null);
				}
			}
		}
	}

	public static int leftSkillIdx = 0;
	public static int rightSkillIdx = 0;

	@Override
	public void clearSkillColumn() {
		// 回合开始清空左右技能动 leftSkillIdx = 0;
		leftSkillIdx = 0;
		rightSkillIdx = 0;
		clearSkill((ViewGroup) findViewById(R.id.upPerSkill));
		clearSkill((ViewGroup) findViewById(R.id.downPerSkill));
	}

	private void clearSkill(ViewGroup vg) {
		if (null == vg)
			return;
		for (int i = 0; i < vg.getChildCount(); i++) {
			View v = vg.getChildAt(i);
			v.setTag(null);
			v.clearAnimation();
			ViewUtil.setGone(v);
		}
	}

	@Override
	public BaseAnim showRound(Animation anim, int round) {
		ViewUtil.setRichText(findViewById(R.id.roundNum),
				ImageUtil.getNumStr(CalcUtil.parseInteger(round), "num"));
		FrameLayout midRound = (FrameLayout) window.findViewById(R.id.midRound);
		return new ShowRoundAnim(midRound, anim);
	}

	public ArrayList<Anim> getHitContinus(boolean isDown, Drawable d,
			boolean isArchor, int startCount, int armscount, int type,
			int currentHP, int totalHp) {
		ViewGroup v = null;
		if (isDown) {
			v = downAmyFrame/* .findViewById(R.id.ltroop) */;
		} else {
			v = upAmyFrame/* .findViewById(R.id.rtroop) */;
		}
		ArrayList<Anim> hitAnimation = new ArrayList<Anim>();
		int count = v.getChildCount();
		if (count == 0) {
			return null;
		}
		int dis = 0;
		if (startCount == 0) {
			return null;
		}
		int flyCount = startCount - armscount; // 打飞的个数
		int beatCount = startCount - flyCount;// 受击的个数
		int width = v.getChildAt(0).getWidth();
		for (int i = 0; i < startCount; i++) {
			View view = v.getChildAt(i);
			if (i < beatCount) {
				{
					BaseAnim anim = new BaseAnim(view,
							AnimPool.beat(isDown, 0), false);
					anim.setBeat(true);
					hitAnimation.add(anim);
				}
			} else {

				if (isDown) {
					dis = v.getChildAt(i).getLeft() + width;
				} else {
					dis = Config.screenWidth - v.getChildAt(i).getLeft();
				}
				// if(type == 5 || isArchor)
				{
					BaseAnim anim = new FallDownAnim(
							v,
							view,
							AnimPool.soldierFly(isDown, dis, 0, 800,
									view.getWidth(), window, currentHP, totalHp),
							true);
					anim.setBeat(true);
					hitAnimation.add(anim);
				}
			}
		}
		return hitAnimation;
	}

	public ArrayList<Anim> getHit(boolean isDown, Drawable d, boolean isArchor,
			int armscount, int type, int currentHp, int totalHp,
			BattleEventArmInfo beai, BattleArrayInfoClient baic) {
		ViewGroup v = null;
		if (isDown) {
			v = downAmyFrame;
		} else {
			v = upAmyFrame;
		}
		ArrayList<Anim> hitAnimation = new ArrayList<Anim>();
		int count = v.getChildCount();
		if (count == 0) {
			return null;
		}
		int dis = 0;
		int width = v.getChildAt(0).getWidth();

		for (int i = count - 1; i >= 0; i--) {
			View view = v.getChildAt(i);
			if (ViewUtil.isGone(view)) {
				continue;
			}
			if (i < armscount) {
				{
					BaseAnim anim = new BaseAnim(view,
							AnimPool.beat(isDown, 0), false);
					anim.setBeat(true);
					// anim.setSoundName("dead_mmm.ogg");
					hitAnimation.add(anim);
				}
			} else {
				if (isDown) {
					dis = v.getChildAt(i).getLeft() + width;
				} else {
					dis = Config.screenWidth - v.getChildAt(i).getLeft();
				}
				BaseAnim anim = new FallDownAnim(v, view, AnimPool.soldierFly(
						isDown, dis, 0, 800, view.getWidth(), window,
						currentHp, totalHp, beai, baic, this), true);
				anim.setBeat(true);
				// anim.setSoundName("dead_m.ogg");
				hitAnimation.add(anim);
			}
		}
		return hitAnimation;
	}

	// 显示buff
	@Override
	public void setSkillAppear(boolean isDown, Animation anim,
			Animation nullAnim, Buff buff, Drawable d) { // , CallBack cb

		ViewGroup vg = (ViewGroup) (isDown ? findViewById(R.id.downPerSkill)
				: findViewById(R.id.upPerSkill));

		View bufView = null;
		int childCount = vg.getChildCount();
		SoundMgr.play("buff.ogg");
		if (isDown) {
			for (int i = childCount - 1; i >= 0; i--) {
				Integer bufTag = (Integer) vg.getChildAt(i).getTag();
				if (bufTag == null || bufTag.equals(buff.buffId)
						|| bufTag == -1) {
					bufView = vg.getChildAt(i);
					ViewUtil.setVisible(bufView);
					if (d != null) {
						bufView.setBackgroundDrawable(d);
					}
					bufView.setTag(buff.buffId);
					break;
				}
			}
		} else {
			for (int i = 0; i < childCount; i++) {
				Integer bufTag = (Integer) vg.getChildAt(i).getTag();
				if (bufTag == null || bufTag.equals(buff.buffId)
						|| bufTag == -1) {
					bufView = vg.getChildAt(i);
					ViewUtil.setVisible(bufView);

					if (d != null) {
						bufView.setBackgroundDrawable(d);
					}
					bufView.setTag(buff.buffId);
					break;
				}
			}
		}
	}

	private boolean isMeAtk; // 进攻者处于屏幕中间，true：自己在中间，false：敌方在中间

	@Override
	public void setMeAtk(boolean isMeAtk) {
		this.isMeAtk = isMeAtk;
	}

	// 攻方进入战斗位置
	@Override
	public BaseAnim reachAtkPos(boolean isDown, Animation anim, Drawable d,
			int dis) {
		if (isDown) {
			isMeAtk = true;
			return new ReachAtkPos(downAmyFrame, anim, d, dis);
		} else {
			isMeAtk = false;
			return new ReachAtkPos(upAmyFrame, anim, d, -dis);
		}
	}

	// 攻方进入战斗位置
	@Override
	public List<BaseAnim> reachAtkTarget(boolean isDown, Animation anim,
			Drawable d, int dis) {
		ArrayList<BaseAnim> reach = new ArrayList<BaseAnim>();
		ViewGroup v = isDown ? downAmyFrame : upAmyFrame;
		int childNum = v.getChildCount();

		for (int i = 0; i < childNum; i++) {
			View v_sub = v.getChildAt(i);
			if (v_sub.getVisibility() != View.VISIBLE) {
				continue;
			}
			BaseAnim baseAnim = null;
			if (isDown)
				baseAnim = new ReachAtkPos(v_sub, anim, d, dis);
			else
				baseAnim = new ReachAtkPos(v_sub, anim, d, -dis);
			reach.add(baseAnim);
		}
		if (isDown) {
			BattleCoordUtil.downMatrixLB.x += dis;
			BattleCoordUtil.downMatrixLB.y -= dis;
		} else {
			BattleCoordUtil.upMatrixLB.x += -dis;
			BattleCoordUtil.upMatrixLB.y -= -dis;
		}
		return reach;
	}

	// 延时动画，仅用于显示日志
	@Override
	public BaseAnim getDelay(Animation anim) {
		return new BaseAnim(window.findViewById(R.id.skip), anim, false);
	}

	// 近程攻击
	@Override
	public BaseAnim shortRangeAtk(boolean isDown, Animation anim, int dis,
			int atkNum, boolean isBounce) {
		View v = isDown ? downAmyFrame : upAmyFrame;
		if (isDown) {
			return new ShortRangeAtk(isDown, v, anim, false, dis, atkNum,
					isBounce);
		} else {
			return new ShortRangeAtk(isDown, v, anim, false, -dis, atkNum,
					isBounce);
		}
	}

	// 近程攻击
	@Override
	public BaseAnim shortRangeAtkBounce(boolean isDown, Animation anim, int dis) {
		View v = isDown ? downAmyFrame : upAmyFrame;
		if (isDown) {
			return new ShortRangeAtkBounce(isDown, v, anim, false, dis);
		} else {
			return new ShortRangeAtkBounce(isDown, v, anim, false, -dis);
		}
	}

	// 清除buff的小图标 好像反了 vg的值 //0705
	@Override
	public void setSkillDisappear(boolean isDown, Animation anim, int skillId) { // ,
		View v = null;
		ViewGroup vg = (ViewGroup) (isDown ? findViewById(R.id.downPerSkill)
				: findViewById(R.id.upPerSkill));

		int i = 0;
		for (; i < vg.getChildCount(); i++) {
			v = vg.getChildAt(i);
			if (null != v.getTag()
					&& ((Integer) v.getTag()).intValue() == skillId) {
				v.setVisibility(View.INVISIBLE);
				v.setTag(-1);
				break;
			}
		}
	}

	// 逃跑
	@Override
	public BaseAnim getEscape(boolean isDown, int troopId, Animation anim) {
		ViewGroup vg = getSelTroopGrid(isDown, troopId);
		ViewUtil.setHide(vg, R.id.selBg);
		ViewUtil.setVisible(vg, R.id.state);
		View role = vg.findViewById(R.id.role);
		ViewUtil.setImage(role, R.drawable.btl_escape);
		vg.setTag(AMY_END);

		if (isDown) {
			View v = downAmyFrame; // lAmy;
			return new BaseAnim(v, anim, true);
		} else {
			View v = upAmyFrame; // rAmy;
			return new BaseAnim(v, anim, true);
		}
	}

	private int getChildCount(boolean isDown) {
		ViewGroup viewGroup = (ViewGroup) (isDown ? downAmyFrame : upAmyFrame);
		if (viewGroup == null) {
			return 0;
		}
		int amys = viewGroup.getChildCount();
		return amys;
	}

	// 头顶信息
	@Override
	public BaseAnim getTopEff(boolean isDown, Animation anim, String str,
			TroopProp tp, BattleArrayInfoClient baic) {

		Point arm_center = getArmsCenter(isDown);
		int x = arm_center.x;
		int y = arm_center.y;
		TopEffectAnim topEffectAnim = new TopEffectAnim(Eff, anim, str, x, y); // ,

		TextView name = isDown ? downAmyName : upAmyName;
		TextView cnt = isDown ? downAmyCnt : upAmyCnt;
		LinearLayout hpBar = isDown ? downAmyHPInfo : upAmyHPInfo;
		TextView armName = isDown ? downName : upName;

		ViewGroup vg = getSelTroopGrid(isDown, baic.getId());
		topEffectAnim.setHpInfo(isDown, cnt, name, armName, hpBar, vg, baic,
				tp, battleLogInfo);
		return topEffectAnim;
	}

	// 头顶信息
	@Override
	public BaseAnim getForceAtk(boolean isDown, Animation anim, String str,
			TroopProp tp, BattleArrayInfoClient baic) {
		int childNum = getChildCount(isDown);
		Point arm_center = getArmsCenter(isDown);

		ForceAttack forceAttack = new ForceAttack(Eff, anim, str, arm_center.x,
				arm_center.y, childNum); // , width
		TextView name = isDown ? downAmyName : upAmyName;
		TextView cnt = isDown ? downAmyCnt : upAmyCnt;
		LinearLayout hpBar = isDown ? downAmyHPInfo : upAmyHPInfo;
		TextView armName = isDown ? downName : upName;

		ViewGroup vg = getSelTroopGrid(isDown, baic.getId());
		forceAttack.setHpInfo(isDown, cnt, name, armName, hpBar, vg, baic, tp,
				battleLogInfo);
		return forceAttack;
	}

	// 帮助
	@Override
	public BaseAnim getHelp(boolean isDown, Animation anim, Drawable d) {
		int x = 0;
		if (isDown) {
			View amy = downAmyFrame;
			return new EscapeAnim(Eff, anim, amy, d, x);
		} else {
			View amy = upAmyFrame;
			return new EscapeAnim(Eff, anim, amy, d, x);
		}
	}

	// 受击动画
	@Override
	public BaseDrawableAnim getBeatAnimationDrawable(boolean isDown) {
		int x = 0;
		ImageView v = specialEffects;
		DrawableAnimationBasis beatAnimation = null;

		Point armCenter;
		if (isDown) {
			armCenter = BattleCoordUtil.downMatrixLB;
		} else {
			armCenter = BattleCoordUtil.upMatrixLB;
		}
		return new GetBeatenDrawableAnimation(v, beatAnimation, x, isDown,
				"dead_m.ogg", armCenter); // knight.ogg
	}

	// 特效动画处理
	@Override
	public BaseDrawableAnim effectDrawableAnim(boolean isDown,
			BattleAnimEffects animEffects) {
		ImageView v = specialEffects;

		Point armCenter;
		if (isDown) {
			armCenter = BattleCoordUtil.downMatrixLB;
		} else {
			armCenter = BattleCoordUtil.upMatrixLB;
		}
		String soundName = "knight.ogg";
		if (animEffects.getId() / 10 != 1) {
			soundName = "g_arrow.ogg";
		}
		return new EffectAttackAnim(v, isDown, /* soundName */"dead_m.ogg",
				armCenter, animEffects); // knight.ogg
	}

	// 特效动画处理
	@Override
	public BaseDrawableAnim heroEffectAnim(boolean isAttack, boolean isDefend,
			boolean isGain) {
		return new HeroSkillAnim(isAttack, isDefend, window, false, isGain); // knight.ogg
	}

	@Override
	public void clearAnimation() {
		specialEffects.clearAnimation();
		Eff.clearAnimation();
	}

	// @Override
	public BaseDrawableAnim longRangeAtkDrawable(boolean isDown, int atkRange) {
		if (atkRange == 2) {
			return new ArcherLongAtkAnim(specialEffects, null, isDown,
					"dead_m.ogg");
		} else
		/* if (atkRange == 3) */
		{
			return new MagicLongAtkAnim(specialEffects, null, isDown,
					"dead_m.ogg");
		}
	}

	// 受击抖动
	@Override
	public BaseAnim getShake(boolean isDown, Animation anim, boolean isArchor) {
		if (isDown) {
			View v = downAmyFrame; // lAmy;
			return new BaseAnim(v, anim, false);
		} else {
			View v = upAmyFrame; // rAmy;
			return new BaseAnim(v, anim, false);
		}
	}

	private ViewGroup getSelTroopGrid(boolean isDown, int troopId) {
		ViewGroup vg = isDown ? downAmy : upAmy;

		for (int i = 0; i < vg.getChildCount(); i++) {
			View child = vg.getChildAt(i);
			if (!(child instanceof ImageView)) {
				Object tag = child.getTag();
				if (null != tag && troopId == (Integer) tag)
					return (ViewGroup) child;
			}
		}
		return null;
	}

	@Override
	public void modifyTroopGridAmount(boolean isDown, BattleArrayInfoClient baic) {
		ViewGroup vg = getSelTroopGrid(isDown, baic.getId());
		// 加保护是为了避免鞭尸的情
		if (null != vg) {
			ViewUtil.setRichText(vg, R.id.cnt,
					CalcUtil.turnToTenThousandEx(baic.getNum()));
			if (0 == baic.getNum()) { // 全灭
				ViewUtil.setHide(vg, R.id.selBg);

				View state = vg.findViewById(R.id.state);
				if (ViewUtil.isHidden(state))
					;
				ViewUtil.setVisible(state);
				ViewUtil.setImage(vg, R.id.role, R.drawable.btl_die);
				vg.setTag(AMY_END);
				// SoundMgr.play("dead.ogg");
			}
		}
	}

	@Override
	public void doClose() {
		super.doClose();
		MediaPlayerMgr.getInstance().stopSound();
		// 释放资源
		// release();
		if (oldRank != -1 || newRank != -1) { // 巅峰结算界面
			new BattleResultArenaWindow().open(battleLogInfo,
					battleDriver.getRic(), isLog, battleLogInfo.getBgName(),
					oldRank, newRank);
		} else if (isBlood) {
			new BattleResultBloodWindow().open(battleLogInfo,
					battleDriver.getRic(), isLog, battleLogInfo.getBgName());
		} else { // 通用结算界面
			new BattleResultWindow().open(battleLogInfo, battleDriver.getRic(),
					isLog, battleLogInfo.getBgName(), isGuide);
		}
	}

	private void exit() {
		battleAnim.setSkip();
		battleAnim.showResult();
		clear();
		// releaseWidgetBitmap();
	}

	private void clear() {
		ViewUtil.setGone(window.findViewById(R.id.screenSkillFrame));
		ViewUtil.setGone(window.findViewById(R.id.screenSkillImg));
		ViewUtil.setGone(window.findViewById(R.id.screenSkillName));
		// ViewUtil.setHide(window.findViewById(R.id.screenSkillName1));
		ViewUtil.setGone(window.findViewById(R.id.screenHeroIcon));
		ViewUtil.setGone(window.findViewById(R.id.screenHeroIcon1));
		ViewUtil.setGone(window.findViewById(R.id.screenHeroIcon2));
		ViewUtil.setGone(window.findViewById(R.id.screenSkillBg));
		// ViewUtil.setHide(window.findViewById(R.id.screenSkillLight));
	}

	@Override
	public void modifyTroopHP(boolean isDown, long value, long total) {
		if (isDown) {
			if (downAmyHPInfo.getVisibility() != View.VISIBLE) {
				downAmyHPInfo.setVisibility(View.VISIBLE);
			}
		} else {
			if (upAmyHPInfo.getVisibility() != View.VISIBLE) {
				upAmyHPInfo.setVisibility(View.VISIBLE);
			}
		}
		if (total == 0) {
			return;
		}
		ProgressBar v = (ProgressBar) (isDown ? findViewById(R.id.downAmyHP)
				: findViewById(R.id.upAmyHP));
		if (value < 0)
			value = 0;

		int cur = (int) (100 * value / total);
		v.set(cur);
	}

	@Override
	public void clearTroopHP(boolean isDown) {
		ProgressBar v = (ProgressBar) (isDown ? findViewById(R.id.downAmyHP)
				: findViewById(R.id.upAmyHP));
		v.set(0, 100);

		if (isDown) {
			downAmyHPInfo.setVisibility(View.INVISIBLE);
		} else {
			upAmyHPInfo.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean goBack() {
		if (isResultAnimOver || (!isSkip))
			return false;
		return true;
	}

	class HeroSkillHolder {
		public List<HeroSkillSlotInfoClient> skillSlotInfos;
		public List<HeroArmPropInfoClient> heroArmlist;
		public int defSkillId = -1;

		public boolean isInvalid() {
			return ListUtil.isNull(skillSlotInfos)
					&& ListUtil.isNull(heroArmlist) && -1 == defSkillId;
		}
	}

	@Override
	public BaseAnim moveHeroIcon(Animation anim, boolean isGone,
			boolean isDown, Drawable d, int index, String heroName) {
		ImageView view = null;
		if (index == 0) {
			view = (ImageView) findViewById(R.id.screenHeroIcon);
		} else if (index == 1) {
			view = (ImageView) findViewById(R.id.screenHeroIcon1);
		} else if (index == 2) // 第三个英雄
		{
			view = (ImageView) findViewById(R.id.screenHeroIcon2);
		}
		return new HeroIconAnim(isDown, view, null, anim, isGone, d, index,
				heroName);
	}

	@Override
	public BaseAnim moveSkillName(String name, Animation anim, boolean isGone,
			boolean isDown, Drawable d) {
		return new SkillNameAnim(isDown, findViewById(R.id.screenSkillName),
				null, anim, name, isGone, d);
	}

	@Override
	public BaseAnim moveSkillNameSlow(String name, Animation anim,
			boolean isGone) {
		return null;
	}

	@Override
	public BaseAnim moveSkillNameOut(String name, Animation anim, boolean isGone) {
		return null;
	}

	@Override
	public BaseAnim moveSkillBg(Animation anim, boolean isGone, int resId,
			boolean isDown) {
		skillBgDrawable = Config.getController().getDrawable("skill_bg");
		return new SkillBg(isDown, findViewById(R.id.screenSkillBg), anim,
				isGone, skillBgDrawable);
	}

	@Override
	public BaseAnim showSkill(Animation anim, Drawable d, boolean isGone) {
		return new HigherSkillAnim(findViewById(R.id.screenSkillImg), anim, d,
				isGone);
	}

	@Override
	public BaseAnim showBg(Animation anim) {
		return new BaseAnim(findViewById(R.id.screenSkillFrame), anim, false);
	}

	@Override
	public BaseAnim skillFling(Animation anim, boolean isGone, boolean isDown) {
		View view = findViewById(R.id.screenSkillImg);
		if (isDown) {
			flyDrawable = ImageUtil.getMirrorBitmapDrawable("flyings");
			view.setBackgroundDrawable(flyDrawable);
			ViewUtil.setMargin(view, 0, 0, 0,
					(int) (120 * Config.SCALE_FROM_HIGH), Gravity.LEFT
							| Gravity.BOTTOM);
		} else {
			flyDrawable = Config.getController().getDrawableHdInBattle(
					"flyings", ImageUtil.RGB565);
			view.setBackgroundDrawable(flyDrawable);
			ViewUtil.setMargin(view, 0, (int) (120 * Config.SCALE_FROM_HIGH),
					0, 0, Gravity.RIGHT | Gravity.TOP);
		}
		return new BaseAnim(view, anim, isGone);
	}

	@Override
	public BaseAnim skillLight(Animation anim, boolean isGone, boolean isDown,
			int index) {
		View short_light0 = findViewById(R.id.short_light0);
		View long_light0 = findViewById(R.id.long_light0);
		View short_light1 = findViewById(R.id.short_light1);
		View long_light1 = findViewById(R.id.long_light1);

		View short_light0_below = findViewById(R.id.short_light0_below);
		View short_light1_below = findViewById(R.id.short_light1_below);

		// 0是第一组
		if (index == -1) {
			if (isDown) {
				ViewUtil.setMargin(short_light0_below, 0, 0, 0,
						(int) (240 * Config.SCALE_FROM_HIGH), Gravity.LEFT
								| Gravity.BOTTOM);
			} else {
				ViewUtil.setMargin(short_light0_below, 0,
						(int) (240 * Config.SCALE_FROM_HIGH), 0, 0,
						Gravity.RIGHT | Gravity.TOP);
			}
			short_light0_below.setBackgroundDrawable(Config.getController()
					.getDrawable("short_light"));
			return new BaseAnim(short_light0_below, anim, isGone);
		} else if (index == 0) {
			if (isDown) {
				ViewUtil.setMargin(short_light0, 0, 0, 0,
						(int) (300 * Config.SCALE_FROM_HIGH), Gravity.LEFT
								| Gravity.BOTTOM);
			} else {
				ViewUtil.setMargin(short_light0, 0,
						(int) (300 * Config.SCALE_FROM_HIGH), 0, 0,
						Gravity.RIGHT | Gravity.TOP);
			}
			short_light0.setBackgroundDrawable(ImageUtil
					.getMirrorBitmapDrawable("short_light"));
			return new BaseAnim(short_light0, anim, isGone);
		} else if (index == 1) {
			if (isDown) {
				ViewUtil.setMargin(long_light0, 0, 0, 0,
						(int) (270 * Config.SCALE_FROM_HIGH), Gravity.LEFT
								| Gravity.BOTTOM);
			} else {
				ViewUtil.setMargin(long_light0, 0,
						(int) (270 * Config.SCALE_FROM_HIGH), 0, 0,
						Gravity.RIGHT | Gravity.TOP);
			}
			long_light0.setBackgroundDrawable(Config.getController()
					.getDrawable("long_light"));
			return new BaseAnim(long_light0, anim, isGone);
		} else if (index == 2) {
			if (isDown) {
				ViewUtil.setMargin(short_light1, 0, 0, 0,
						(int) (300 * Config.SCALE_FROM_HIGH), Gravity.LEFT
								| Gravity.BOTTOM);
			} else {
				ViewUtil.setMargin(short_light1, 0,
						(int) (300 * Config.SCALE_FROM_HIGH), 0, 0,
						Gravity.RIGHT | Gravity.TOP);
			}
			short_light1.setBackgroundDrawable(Config.getController()
					.getDrawable("short_light"));
			return new BaseAnim(short_light1, anim, isGone);
		} else if (index == 3) {
			if (isDown) {
				ViewUtil.setMargin(long_light1, 0, 0, 0,
						(int) (270 * Config.SCALE_FROM_HIGH), Gravity.LEFT
								| Gravity.BOTTOM);
			} else {
				ViewUtil.setMargin(long_light1, 0,
						(int) (270 * Config.SCALE_FROM_HIGH), 0, 0,
						Gravity.RIGHT | Gravity.TOP);
			}
			long_light1.setBackgroundDrawable(Config.getController()
					.getDrawable("long_light"));
			return new BaseAnim(long_light1, anim, isGone);
		} else if (index == 4) {
			if (isDown) {
				ViewUtil.setMargin(short_light1_below, 0, 0, 0,
						(int) (240 * Config.SCALE_FROM_HIGH), Gravity.LEFT
								| Gravity.BOTTOM);
			} else {
				ViewUtil.setMargin(short_light1_below, 0,
						(int) (240 * Config.SCALE_FROM_HIGH), 0, 0,
						Gravity.RIGHT | Gravity.TOP);
			}
			short_light1_below.setBackgroundDrawable(Config.getController()
					.getDrawable("short_light"));
			return new BaseAnim(short_light1_below, anim, isGone);
		}
		return null;
	}

	@Override
	public BaseAnim clearSkill(Animation anim) {
		return new ClearSkill(skip, window, anim);
	}

	@Override
	public ArrayList<Anim> startReadyEffects(boolean isDown, Animation anim,
			boolean isArchor, boolean isGone, int atkNum) {
		ArrayList<Anim> ready = new ArrayList<Anim>();
		ViewGroup v = isDown ? downAmyFrame : upAmyFrame;
		int childNum = v.getChildCount();

		if (isArchor == true) {
			BaseAnim baseAnim = new BaseAnim(v, anim, false);
			ready.add(baseAnim);
			return ready;
		} else {
			for (int i = 0; i < childNum; i++) {
				View v_sub = v.getChildAt(i);
				if (v_sub.getVisibility() != View.VISIBLE) {
					continue;
				}
				BaseAnim baseAnim = new BaseAnim(v_sub, anim, false);
				ready.add(baseAnim);
			}
		}
		return ready;
	}

	@Override
	public ArrayList<Anim> magicStartEffects(boolean isDown, boolean isGone,
			int atkNum) {
		ArrayList<Anim> ready = new ArrayList<Anim>();
		ViewGroup v = isDown ? downAmyFrame : upAmyFrame;

		for (int i = 0; i < atkNum; i++) {
			View v_sub = v.getChildAt(i);
			BaseAnim baseAnim = null;
			if (i == 0) {
				baseAnim = new BaseAnim(v_sub,
						AnimPool.magicRotate(isDown, 220), false);
			} else {
				baseAnim = new BaseAnim(v_sub,
						AnimPool.magicRotate(isDown, 200), false);
			}
			ready.add(baseAnim);
		}
		return ready;
	}

	// 进攻可移动的距离
	public int getAtkDis(boolean isDown, Drawable d) {
		if (downAmyFrame == null || upAmyFrame == null) {
			return 0;
		}
		// 做一個估
		if (downAmyFrame.getChildCount() == 0 && upAmyFrame.getChildCount() > 0) {
			return (upAmyFrame.getChildAt(0).getLeft() - (BattleCoordUtil.downArmPosition[0].x + d
					.getIntrinsicWidth() / 2)) * 2;
		} else if (downAmyFrame.getChildCount() > 0
				&& upAmyFrame.getChildCount() == 0) {
			return (BattleCoordUtil.upArmPosition[0].x - d.getIntrinsicWidth()
					/ 2 - (downAmyFrame.getChildAt(0).getRight())) * 2;
		}
		return upAmyFrame.getChildAt(0).getLeft()
				- downAmyFrame.getChildAt(0).getRight();
	}

	@Override
	public Anim enterBattleField(boolean isDown, Animation anim, Drawable d,
			int amrCount, byte type, boolean isBoss) {
		ViewGroup v = null;
		if (isDown) {
			v = downAmyFrame;
			return new SetAmyIcon(v, isDown, anim, d, amrCount, type, isBoss,
					downAmyViewsPool);
		} else {
			v = upAmyFrame;
			return new SetAmyIcon(v, isDown, anim, d, amrCount, type, isBoss,
					upAmyViewsPool);
		}

	}

	// 士兵技能显
	@Override
	public BaseAnim armSkillAppear(boolean isDown, Animation anim) {
		int childNum = getChildCount(isDown);
		Point point = getArmsCenter(isDown);
		return new SetSkillBg(armSkillLayout, anim, point.x, point.y, childNum);
	}

	// 技能icon 显示
	@Override
	public BaseAnim armSkillImageAppear(boolean isDown, Animation anim,
			Drawable d, String icon) {
		View v = armSkillLayout.findViewById(R.id.skill_small);
		return new SetSkillIcon(v, anim, d, icon);
	}

	// 显示技能名
	@Override
	public BaseAnim armSkillNameAppear(boolean isDown, Animation anim,
			String name) {
		TextView v = (TextView) (armSkillLayout
				.findViewById(R.id.arm_skill_bottom));
		BaseAnim baseAnim = new ArmSkillNameAnim(v, anim, name, false);
		return baseAnim;
	}

	// 技能旋转
	@Override
	public BaseAnim armSkillRotate(boolean isDown, Animation anim) {
		View v = armSkillLayout.findViewById(R.id.arm_skill);
		BaseAnim baseAnim = new BaseAnim(v, anim, false);
		return baseAnim;
	}

	// 技能消
	@Override
	public BaseAnim armSkilldisppear(boolean isDown, Animation anim) {
		BaseAnim baseAnim = new BaseAnim(armSkillLayout, anim, false);
		return baseAnim;
	}

	@Override
	public BaseAnim armSkillClear(boolean isDown, Animation anim) {
		return new ClearArmSkill(skip, window, anim);
	}

	private Point getArmsCenter(boolean isDown) {
		int x_center = 0;
		int y_center = 0;
		int arm_width = 0;
		int arm_height = 0;

		ViewGroup viewGroup = (ViewGroup) (isDown ? downAmyFrame : upAmyFrame);
		int amys = viewGroup.getChildCount();

		if (amys > 0) {
			arm_width = viewGroup.getChildAt(0).getWidth();
			arm_height = viewGroup.getChildAt(0).getHeight();
		}

		if (amys >= 4) {
			Point point0 = getPosition(viewGroup.getChildAt(0));
			Point point1 = getPosition(viewGroup.getChildAt(1));
			x_center = (point0.x + point1.x + arm_width) / 2;
			y_center = (point0.y + point1.y + arm_height) / 2;
			// x_center = (viewGroup.getChildAt(0).getLeft() + viewGroup
			// .getChildAt(1).getRight()) / 2;
			// y_center = (viewGroup.getChildAt(0).getTop() + viewGroup
			// .getChildAt(1).getBottom()) / 2;
		} else if (amys == 3) {
			// View amr_center = viewGroup.getChildAt(0);
			// x_center = amr_center.getLeft() + arm_width / 2;
			// y_center = amr_center.getTop() + arm_height / 2;

			Point point0 = getPosition(viewGroup.getChildAt(0));
			x_center = point0.x + arm_width / 2;
			y_center = point0.y + arm_height / 2;
		} else if (amys == 2) {
			Point point0 = getPosition(viewGroup.getChildAt(0));
			Point point1 = getPosition(viewGroup.getChildAt(1));
			x_center = (point0.x + point1.x + arm_width) / 2;
			y_center = (point0.y + point1.y + arm_height) / 2;

			// x_center = (viewGroup.getChildAt(0).getLeft() + viewGroup
			// .getChildAt(1).getRight()) / 2;
			// y_center = (viewGroup.getChildAt(0).getTop() + viewGroup
			// .getChildAt(1).getBottom()) / 2;
		} else if (amys == 1) {
			// View amr_center = viewGroup.getChildAt(0);
			// x_center = amr_center.getLeft() + arm_width / 2;
			// y_center = amr_center.getTop();

			Point point0 = getPosition(viewGroup.getChildAt(0));
			x_center = point0.x + arm_width / 2;
			y_center = point0.y;
		}
		Point point = new Point();
		point = new Point();
		point.x = x_center;
		point.y = y_center;
		if (amys >= 4) // 防止2边部队靠的太近 buff分不清是哪一边的
		{
			if (isDown) {
				point.x = (int) (point.x - 20 * Config.SCALE_FROM_HIGH);
			}
		}
		return point;
	}

	private void addLeftTroopInfoEx(final List<BattleArrayInfoClient> list,
			final List<BattleLogHeroInfoClient> blhic, LinearLayout amy,
			boolean isDown, final BattleSkill fiefBattleSkill,
			final UserTroopEffectInfo userTroopEffectInfo) {

		// 不满八支部队需要补
		for (int i = 0; i < 8 - list.size(); i++) {
			View v = controller.inflate(R.layout.null_amy);
			ViewUtil.setImage(v.findViewById(R.id.icon_frame),
					R.drawable.common_icon_bg_right);
			v.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			v.setTag(AMY_END);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.gravity = Gravity.RIGHT;
			amy.addView(v, layoutParams);
		}

		for (int i = list.size() - 1; i >= 0; i--) {
			BattleArrayInfoClient it = list.get(i);
			ViewGroup vg = (ViewGroup) controller
					.inflate(R.layout.combat_amy_grid);
			try {
				final TroopProp tp = (TroopProp) CacheMgr.troopPropCache.get(it
						.getId());
				new ViewImgScaleCallBack(tp.getIcon(), "arm_default",
						vg.findViewById(R.id.icon), Constants.ARMY_ICON_WIDTH,
						Constants.ARMY_ICON_WIDTH);

				ViewUtil.setImage(vg.findViewById(R.id.icon_frame),
						R.drawable.common_icon_bg_right);
				vg.findViewById(R.id.seq_right).setVisibility(View.VISIBLE);
				ViewUtil.setRichText(vg, R.id.seq_right, "" + (i + 1));
				// 设置兵力
				new ViewImgScaleCallBack(tp.getSmallIcon(),
						vg.findViewById(R.id.armType),
						17 * Config.SCALE_FROM_HIGH,
						17 * Config.SCALE_FROM_HIGH);
				if (blhic != null && blhic.size() > 0) {
					for (BattleLogHeroInfoClient client : blhic) {
						if (HeroArmPropClient.isStrength(
								client.getHeroArmPropInfos(), tp)) {
							ViewUtil.setVisible(vg, R.id.up);
							break;
						}
					}
				}
				ViewUtil.setRichText(vg, R.id.cnt,
						CalcUtil.turnToTenThousandEx(it.getNum())); // String.valueOf(it.getNum()));
				final List<Buff> buffs = it.getBuffIds();
				vg.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						List<OtherHeroInfoClient> otherHeroInfoClients = null;
						if (!ListUtil.isNull(blhic)) {
							otherHeroInfoClients = new ArrayList<OtherHeroInfoClient>();
							for (BattleLogHeroInfoClient battleLogHeroInfoClient : blhic) {
								OtherHeroInfoClient otherHeroInfoClient = BattleLogHeroInfoClient
										.convertFrom(battleLogHeroInfoClient);
								if (otherHeroInfoClient != null) {
									otherHeroInfoClients
											.add(otherHeroInfoClient);
								}
							}
						}

						new TroopDetailTip(false).show(
								tp,
								userTroopEffectInfo,
								otherHeroInfoClients,
								fiefBattleSkill == null ? 0 : fiefBattleSkill
										.getId(), null, buffs);
					}
				});

			} catch (GameException e) {
				e.printStackTrace();
			}
			vg.setTag(it.getId());
			if (0 == it.getNum()) { // 全灭
				ViewUtil.setHide(vg, R.id.selBg);

				View state = vg.findViewById(R.id.state);
				ViewUtil.setVisible(state);
				ViewUtil.setImage(vg, R.id.role, R.drawable.btl_die);
				vg.setTag(AMY_END);
				// SoundMgr.play("dead.ogg");
			}
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// layoutParams.gravity = Gravity.RIGHT;
			amy.addView(vg, layoutParams);
		}
		downScroll.post(new Runnable() {
			@Override
			public void run() {
				// 189是英雄框的宽度
				int count = 8;
				if (list.size() > 8) {
					count = list.size();
				}
				int dis = (int) (75 * Config.SCALE_FROM_HIGH * count
						- (Config.screenWidth - 189 * Config.SCALE_FROM_HIGH) + 15 * Config.SCALE_FROM_HIGH);
				downScroll.scrollTo(dis, 0); //
			}
		});
	}

	private void addRightTroopInfoEx(List<BattleArrayInfoClient> list,
			final List<BattleLogHeroInfoClient> blhic, LinearLayout amy,
			boolean isDown, final BattleSkill fiefBattleSkill,
			final UserTroopEffectInfo userTroopEffectInfo) {
		for (int i = 0; i < list.size(); i++) {
			BattleArrayInfoClient it = list.get(i);
			ViewGroup vg = (ViewGroup) controller
					.inflate(R.layout.combat_amy_grid);
			try {
				final TroopProp tp = (TroopProp) CacheMgr.troopPropCache.get(it
						.getId());
				new ViewImgScaleCallBack(tp.getIcon(), "arm_default",
						vg.findViewById(R.id.icon), Constants.ARMY_ICON_WIDTH,
						Constants.ARMY_ICON_WIDTH);

				ViewUtil.setImage(vg.findViewById(R.id.icon_frame),
						R.drawable.common_icon_bg_left);
				vg.findViewById(R.id.seq).setVisibility(View.VISIBLE);
				ViewUtil.setRichText(vg, R.id.seq, String.valueOf("" + (i + 1)));
				new ViewImgScaleCallBack(tp.getSmallIcon(),
						vg.findViewById(R.id.armType),
						17 * Config.SCALE_FROM_HIGH,
						17 * Config.SCALE_FROM_HIGH);
				if (null != blhic && blhic.size() > 0) {
					for (BattleLogHeroInfoClient client : blhic) {
						if (HeroArmPropClient.isStrength(
								client.getHeroArmPropInfos(), tp)) {
							ViewUtil.setVisible(vg, R.id.up);
						}
					}
				}
				ViewUtil.setRichText(vg, R.id.cnt,
						CalcUtil.turnToTenThousandEx(it.getNum())); // String.valueOf(it.getNum()));

				final List<Buff> buffs = it.getBuffIds();
				vg.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						List<OtherHeroInfoClient> otherHeroInfoClients = null;
						if (!ListUtil.isNull(blhic)) {
							otherHeroInfoClients = new ArrayList<OtherHeroInfoClient>();
							for (BattleLogHeroInfoClient battleLogHeroInfoClient : blhic) {
								OtherHeroInfoClient otherHeroInfoClient = BattleLogHeroInfoClient
										.convertFrom(battleLogHeroInfoClient);
								if (otherHeroInfoClient != null) {
									otherHeroInfoClients
											.add(otherHeroInfoClient);
								}
							}
						}

						new TroopDetailTip(false).show(
								tp,
								userTroopEffectInfo,
								otherHeroInfoClients,
								fiefBattleSkill != null ? fiefBattleSkill
										.getId() : 0, null, buffs);
					}
				});

			} catch (GameException e) {
				e.printStackTrace();
			}
			vg.setTag(it.getId());
			if (0 == it.getNum()) { // 全灭
				ViewUtil.setHide(vg, R.id.selBg);

				View state = vg.findViewById(R.id.state);
				ViewUtil.setVisible(state);
				ViewUtil.setImage(vg, R.id.role, R.drawable.btl_die);
				vg.setTag(AMY_END);
				// SoundMgr.play("dead.ogg");
			}
			amy.addView(vg);
		}

		// 不满八支部队需要补
		for (int i = 0; i < 8 - list.size(); i++) {
			View v = controller.inflate(R.layout.null_amy);
			if (isDown) {
				ViewUtil.setImage(v.findViewById(R.id.icon_frame),
						R.drawable.common_icon_bg_left);
			} else {
				ViewUtil.setImage(v.findViewById(R.id.icon_frame),
						R.drawable.common_icon_bg_right);
			}
			v.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			v.setTag(AMY_END);
			amy.addView(v);
		}
	}

	// 显示血 数量
	@Override
	public BaseAnim modifyHpNum(Animation anim, boolean isDown,
			BattleArrayInfoClient baic, String name, boolean isBoss) {
		TextView cnt = isDown ? downAmyCnt : upAmyCnt;
		LinearLayout hpBar = isDown ? downAmyHPInfo : upAmyHPInfo;
		TextView armName = isDown ? downName : upName;
		ViewGroup vg = getSelTroopGrid(isDown, baic.getId());
		return new ShowHpAnimation(isDown, cnt, armName, hpBar, vg, anim, baic,
				name, isBoss, battleLogInfo);

	}

	@Override
	public BaseAnim stay(boolean isDown, Animation anim) {
		View v = isDown ? downAmyFrame : upAmyFrame;
		return new BaseAnim(v, anim, true);
	}

	@Override
	public BaseAnim stayHp(boolean isDown, Animation anim) {
		View v = isDown ? downAmyHPInfo : upAmyHPInfo;
		return new BaseAnim(v, anim, false);
	}

	// 空动画 仅用于延时
	@Override
	public BaseAnim delay(boolean isDown, Animation anim) {
		return new BaseAnim(downAmy, anim, false);
	}

	public BaseAnim getAnimLight(boolean isDown, Animation anim, int index) {
		Drawable d = Config.getController().getDrawable(R.drawable.hero_light);

		int x = 0;// location[0];
		int y = 0;// location[1];
		// 英雄的宽度 根据英雄坐标 得到星星的坐标
		int d_width = (int) (231 * Config.SCALE_FROM_HIGH);
		int d_height = (int) (276 * Config.SCALE_FROM_HIGH);
		if (isDown) {
			x = Config.screenWidth - d_width;
			y = Config.screenHeight - (int) (135 * Config.SCALE_FROM_HIGH)
					- d_height;
		} else {
			x = 0;
			y = (int) (135 * Config.SCALE_FROM_HIGH);
		}
		int dx = 0;
		int dy = 0;
		if (d != null) {
			dx = d.getIntrinsicWidth();
			dy = d.getIntrinsicHeight();
		}
		Animation missionAnim = null;
		ImageView skill = new ImageView(Config.getController().getUIContext());
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		int dex = 0;

		if (index == 3) {
			dex = (int) (180 * Config.SCALE_FROM_HIGH);
		} else {
			dex = (int) (225 * Config.SCALE_FROM_HIGH);
		}

		if (isDown) {
			if (index == 1) {
				missionAnim = new MissionAnim(0, 300,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f, 0, 0, 0, -dex);
				missionAnim.setDuration(350);
				lp.leftMargin = x - dx / 2;
				lp.topMargin = y + d_height - dy;
				// lp.topMargin = y;
			} else if (index == 2) {
				missionAnim = new MissionAnim(0, 360,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f, 0, 0, 0, -dex);
				missionAnim.setStartOffset(200);
				missionAnim.setDuration(300);
				lp.leftMargin = (int) (20 * Config.SCALE_FROM_HIGH);
				lp.topMargin = (int) (y + d_height * 1 / 2 + 10 * Config.SCALE_FROM_HIGH);
				// lp.topMargin = (int) (y + 10*Config.SCALE_FROM_HIGH);
			} else if (index == 3) {
				missionAnim = new MissionAnim(0, 360,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f, 0, 0, 0, -dex);
				missionAnim.setStartOffset(300);
				missionAnim.setDuration(250);
				lp.leftMargin = x + d_width * 3 / 4 - dx;
				// lp.topMargin = (int) (y - 10*Config.SCALE_FROM_HIGH);
				lp.topMargin = (int) (y + d_height - dy - 10 * Config.SCALE_FROM_HIGH);
			}
		} else {
			if (index == 1) {
				lp.leftMargin = x + d_width * 3 / 4;
				lp.topMargin = y + d_height - dy;
				missionAnim = new MissionAnim(0, 360,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f, 0, 0, 0, -dex);
				missionAnim.setDuration(350);
			} else if (index == 2) {
				lp.leftMargin = (int) (Config.screenWidth - dx - 20 * Config.SCALE_FROM_HIGH);
				lp.topMargin = (int) (y + d_height * 1 / 2 + 10 * Config.SCALE_FROM_HIGH);
				missionAnim = new MissionAnim(0, 360,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f, 0, 0, 0, -dex);
				missionAnim.setStartOffset(200);
				missionAnim.setDuration(300);
			} else if (index == 3) {
				lp.leftMargin = x + d_width * 1 / 3;
				lp.topMargin = (int) (y + d_height - dy - 10 * Config.SCALE_FROM_HIGH);
				missionAnim = new MissionAnim(0, 360,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f, 0, 0, 0, -dex);
				missionAnim.setStartOffset(300);
				missionAnim.setDuration(250);
			}
		}
		lp.gravity = Gravity.TOP;
		skill.setBackgroundDrawable(d);
		window.addView(skill, lp);
		skill.setVisibility(View.INVISIBLE);
		skill.bringToFront();

		missionAnim.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input) * (1.0f - input)
						* (1.0f - input));
			}
		});
		return new BaseAnim(skill, missionAnim, true);
	}

	public BaseAnim getLightBlink(boolean isDown, Animation anim) {
		return null;
		// Drawable d =
		// Config.getController().getDrawable(R.drawable.anim_blink);
		// int x = 0;// location[0];
		// int y = 0;// location[1];
		// int d_width = (int) (231 * Config.SCALE_FROM_HIGH);
		// int d_height = (int) (276 * Config.SCALE_FROM_HIGH);
		// if (!isDown) {
		// x = 0;
		// y = (int) (135 * Config.SCALE_FROM_HIGH);
		// } else {
		// x = Config.screenWidth - d_width;
		// y = Config.screenHeight - (int) (135 * Config.SCALE_FROM_HIGH)
		// - d_height;
		// }
		// int dy = 0;
		// if (d != null) {
		// dy = d.getIntrinsicHeight();
		// }
		//
		// ImageView skill = new
		// ImageView(Config.getController().getUIContext());
		// skill.setBackgroundDrawable(d);
		// FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
		// FrameLayout.LayoutParams.WRAP_CONTENT,
		// FrameLayout.LayoutParams.WRAP_CONTENT);
		// Animation blink = null;
		// {
		// lp.leftMargin = Config.screenWidth / 2;
		// lp.topMargin = (int) (y + d_height - dy);
		// int dex = (int) (200 * Config.SCALE_FROM_HIGH);
		// blink = AnimPool.getLightBlink(isDown, dex);
		// }
		// lp.gravity = Gravity.TOP;
		// window.addView(skill, lp);
		// skill.setVisibility(View.INVISIBLE);
		// skill.bringToFront();
		// return new BaseAnim(skill, blink, true);
	}

	private void release() {
		releaseWidgetBitmap();
		window.removeAllViews();
		battleDriver.getBlic().releseBitmap();
		System.gc();
	}

	private void releaseWidgetBitmap() {

		window.findViewById(R.id.bg).setBackgroundDrawable(null);
		window.findViewById(R.id.downAmyWall).setBackgroundDrawable(null);
		window.findViewById(R.id.upAmyWall).setBackgroundDrawable(null);
		specialEffects.setBackgroundDrawable(null);

		window.findViewById(R.id.screenSkillImg).setBackgroundDrawable(null);
		window.findViewById(R.id.screenSkillBg).setBackgroundDrawable(null);

		window.findViewById(R.id.screenHeroIcon).setBackgroundDrawable(null);
		window.findViewById(R.id.screenHeroIcon1).setBackgroundDrawable(null);
		window.findViewById(R.id.screenHeroIcon2).setBackgroundDrawable(null);

	}

	private RecyleResource mRecyleResource = new RecyleResource() {
		@Override
		public void doRecyle() {
			battleDriver.getBlic().releseBitmap();
			System.gc();
		}
	};

	private Point getPosition(View v) {
		Point point = new Point();
		int pos[] = new int[2];
		v.getLocationOnScreen(pos);
		point.x = pos[0];
		point.y = pos[1];
		return point;
	}

}
