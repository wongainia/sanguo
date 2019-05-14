package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BitmapCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleLogHeroInfoClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleSideInfo;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.AbnormalFrameLayout;
import com.vikings.sanguo.ui.AbnormalFrameLayout.RecyleResource;
import com.vikings.sanguo.ui.alert.UserLevelUpTip;
import com.vikings.sanguo.ui.alert.VipFailTip;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.BattleResultAwardTab;
import com.vikings.sanguo.widget.BattleResultLogTab;
import com.vikings.sanguo.widget.BattleResultLossTab;

public class BattleResultWindow extends PopupWindow implements OnClickListener {
	protected AbnormalFrameLayout window;
	protected BattleLogInfoClient blic;
	protected Button close;
	protected ReturnInfoClient ric;
	protected boolean isLog;
	protected String bgName;

	// 当前的tab页
	protected int index = 0;

	// 奖励 战报 详情 tab title
	protected Button[] tabs;
	// 奖励 战报 详情 layout
	protected ViewGroup[] tabsLayoutGroups;

	protected int[] tabBgIds = { R.drawable.settlement_tab_normal,
			R.drawable.settlement_tab_pressed };
	protected int[] tabColorId = { R.color.color9, R.color.color7 };
	protected boolean[] tabIsInit = { false, false, false };
	private boolean isGuide;

	// private FrameLayout battle_panel;
	private Drawable d_battle_panel;

	private ImageView battle_panel;

	// 防止重复弹出vip框框标识
	public static boolean alertVip = true;

	public void open(BattleLogInfoClient blic, ReturnInfoClient ric,
			boolean isLog, String bgName) {
		this.blic = blic;
		this.ric = ric;
		this.isLog = isLog;
		this.bgName = bgName;
		doOpen();

	}

	public void open(BattleLogInfoClient blic, ReturnInfoClient ric,
			boolean isLog, String bgName, boolean isGuide) {
		this.blic = blic;
		this.ric = ric;
		this.isLog = isLog;
		this.bgName = bgName;
		this.isGuide = isGuide;
		doOpen();

	}

	public void setIndex(int index) {
		if (index < 0 || index >= tabs.length) {
			return;
		}
		this.index = index;
	}

	@Override
	protected void init() {

		window = (AbnormalFrameLayout) controller
				.inflate(R.layout.battle_result);

		window.setRecyleResourceListener(mRecyleResource);

		setAtkSideBaseInfo();
		setDefSideBaseInfo();
		// setHeroLevelHint();

		setButtons();

		// ViewUtil.setImage(window, R.id.bg, bgName);
		controller.addContentFullScreen(window);

		battle_panel = (ImageView) window.findViewById(R.id.battle_panel);
		// battle_panel = (FrameLayout) window.findViewById(R.id.battle_panel);
		d_battle_panel = Config.getController().getDrawableHdInBattle("jianbian_bg",ImageUtil.ARGB4444);
		if (d_battle_panel != null) {
			battle_panel.setBackgroundDrawable(d_battle_panel);
		}

		tabs = new Button[3];
		tabs[0] = (Button) window.findViewById(R.id.tab_award);
		tabs[0].setOnClickListener(this);
		tabs[1] = (Button) window.findViewById(R.id.tab_battle_result);
		tabs[1].setOnClickListener(this);
		tabs[2] = (Button) window.findViewById(R.id.tab_battle_log);
		tabs[2].setOnClickListener(this);
		initTabsLayoutGroups();
		select(getIndex());
		// select(this.index);
	}

	public int getIndex() {
		return index;
	}

	protected void initTabsLayoutGroups() {
		tabsLayoutGroups = new ViewGroup[3];
		tabsLayoutGroups[0] = (ViewGroup) window.findViewById(R.id.awardLayout);
		tabsLayoutGroups[1] = (ViewGroup) window.findViewById(R.id.lossLayout);
		tabsLayoutGroups[2] = (ViewGroup) window.findViewById(R.id.logLayout);
		ViewUtil.setGone(window, R.id.arenaLayout);
	}

	private void setAtkSideBaseInfo() {
		BattleSideInfo leftSide = null;
		if (Account.user.getId() == blic.getDownSide().getMainFighter().getId()) {
			leftSide = blic.getDownSide();
		} else {
			leftSide = blic.getUpSide();
		}
		if (leftSide == null)
			return;

		BriefUserInfoClient leftUser = leftSide.getMainFighter();
		ViewGroup atkIv = (ViewGroup) findViewById(R.id.atkIcon).findViewById(
				R.id.iconLayout);
		if (null != leftUser) {
			new UserIconCallBack(leftUser, atkIv, Constants.ICON_WIDTH
					* Config.SCALE_FROM_HIGH, Constants.ICON_HEIGHT
					* Config.SCALE_FROM_HIGH);
			ViewUtil.setText(window, R.id.atkName, leftUser.getNickName());
		}

		ViewUtil.setImage(window, R.id.atkResult, getLeftResultIcon());

		ViewUtil.setText(window, R.id.atkTotal,
				String.valueOf(leftSide.getTotalTroopAmount())); // blic.getLeftSide()

		ViewUtil.setText(window, R.id.atkTotalLoss, leftSide.getTotalLoss());
		ViewUtil.setText(window, R.id.atkHurt, leftSide.getWound());
		ViewUtil.setText(window, R.id.atkDeath, leftSide.getDeath());
	}

	private void setDefSideBaseInfo() {
		BattleSideInfo rightSide = null;
		if (Account.user.getId() != blic.getDownSide().getMainFighter().getId()) {
			rightSide = blic.getDownSide();
		} else {
			rightSide = blic.getUpSide();
		}
		if (rightSide == null)
			return;

		BriefUserInfoClient rightUser = rightSide.getMainFighter();

		ViewGroup defIv = (ViewGroup) findViewById(R.id.defIcon).findViewById(
				R.id.iconLayout);
		if (null != rightUser) {
			new UserIconCallBack(rightUser, defIv, Constants.ICON_WIDTH
					* Config.SCALE_FROM_HIGH, Constants.ICON_HEIGHT
					* Config.SCALE_FROM_HIGH);
			ViewUtil.setText(window, R.id.defName, rightUser.getNickName());
		}

		ViewUtil.setImage(window, R.id.defResult, getRightResultIcon());

		ViewUtil.setText(window, R.id.defTotal,
				String.valueOf(rightSide.getTotalTroopAmount()));

		ViewUtil.setText(window, R.id.defTotalLoss, rightSide.getTotalLoss());
		ViewUtil.setText(window, R.id.defHurt, rightSide.getWound());
		ViewUtil.setText(window, R.id.defDeath, rightSide.getDeath());
	}

	private int getRightResultIcon() {
		if (Account.user.getId() != blic.getDownSide().getMainFighter().getId()) {
			return blic.isDownWin() ? R.drawable.btl_tri_icon_sg
					: R.drawable.btl_fail_icon_sg;
		} else {
			return blic.isDownWin() ? R.drawable.btl_fail_icon_sg
					: R.drawable.btl_tri_icon_sg;
		}
	}

	private int getLeftResultIcon() {
		if (Account.user.getId() == blic.getDownSide().getMainFighter().getId()) {
			return blic.isDownWin() ? R.drawable.btl_tri_icon_sg
					: R.drawable.btl_fail_icon_sg;
		} else {
			return blic.isDownWin() ? R.drawable.btl_fail_icon_sg
					: R.drawable.btl_tri_icon_sg;
		}
	}

	private void setHeroLevelHint() {
		if (blic.needShowHeroLevelHint()) {
			int pveLvl = Integer.valueOf(CacheMgr.dictCache.getDict(
					Dict.TYPE_BATTLE_COST, 21));
			List<BattleLogHeroInfoClient> heros = blic
					.getHeroInfos(Account.user.getId());
			if (null != heros && !heros.isEmpty()) {
				for (BattleLogHeroInfoClient hero : heros) {
					if (hero.getLevel() >= pveLvl) {
						ViewUtil.setVisible(window, R.id.levelLimit);
						ViewUtil.setText(window, R.id.levelLimit, pveLvl
								+ "级以上的将领只能在PVP中获得经验");
						break;
					}
				}
			}
		}
	}

	private void setButtons() {
		close = (Button) window.findViewById(R.id.belowBtn);
		close.setOnClickListener(this);
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void destory() {
		// ViewUtil.releaseDrawable(battle_panel, d_battle_panel);
		controller.removeContentFullScreen(window);
	}

	@Override
	public void showUI() {
		super.showUI();
		controller.hideIconForFullScreen();
		// 回收掉bitmap
		// releseBitmap();
		// 检测第四关以及之后关口战败 引导
		// StepMgr.checkStep201();

		// 战败充值指引
		StepMgr.checkStep14001_15001();

		// 首次打赢 第1章第4关 触发
		StepMgr.checkStep701();
		// 世界第二部分引导领奖
		if (isGuide) {
			StepMgr.startStep507();
			isGuide = false;
		}
		StepMgr.startStep601();
	}

	@Override
	public void hideUI() {
		super.hideUI();
		controller.showIconForFullScreen();
	}

	@Override
	public void onClick(View v) {
		// 处理tabs
		int index = ViewUtil.indexOf(tabs, v);// 遍历控件，对应则返回该控件的数组位置
		if (index != -1) {
			SoundMgr.play(R.raw.sfx_window_open);
			if (this.index != index)
				select(index);
			return;
		}

		// 处理关闭
		if (v == close) {
			controller.goBack();

			// 2014年6月10日0:37:13付斌确认只在副本失败、PVP主动进攻失败弹出
			if (!isLog
					&& blic.isMeLose()
					&& ((blic.isActType() && Account.isCampain1_4Passed()) || (blic
							.isPvp() && blic.isMeAttacker()))) {
				if (!alertVip) {
					alertVip = true;
					return;
				}
				int vipLevel = getVipLevel();
				if (vipLevel > 0) {
					new VipFailTip(vipLevel).show();
					return;
				} else {
					controller.openStronger();
					return;
				}
			}
			if (null != ric && ric.getLevel() > 0)
				new UserLevelUpTip(ric).show();
		}
	}

	private int getVipLevel() {
		int level = Account.getCurVip().getLevel();
		if (level == 0)
			return 1;
		else if (level == 1)
			return 2;
		else if (level == 2)
			return 3;
		else if (level >= 3 && level <= 5)
			return 6;
		else
			return -1;
	}

	// 传入顶部控件对应的序列值
	private void select(int index) {
		selectTab(index);
		if (index == 0) {
			// 处理奖励相关的显示项
			setBonus();
		} else if (index == 1) {
			// 处理战报相关的显示项
			if (!tabIsInit[1]) {
				new BattleResultLossTab(blic, tabsLayoutGroups[1]);
				tabIsInit[1] = true;
			}

		} else if (index == 2) {
			// 处理战斗日志相关的显示项
			if (!tabIsInit[2]) {
				new BattleResultLogTab(blic, tabsLayoutGroups[2]);
				tabIsInit[2] = true;
			}

		}
	}

	protected void setBonus() {
		if (!tabIsInit[0]) {
			new BattleResultAwardTab(blic, tabsLayoutGroups[0]);
			tabIsInit[0] = true;
		}
	}

	// tabs 选中效果
	protected void selectTab(int index) {
		this.index = index;
		for (int i = 0; i < tabs.length; i++) {
			if (i == index) {
				tabs[i].setBackgroundResource(tabBgIds[1]);
				tabs[i].setTextColor(Config.getController().getResources()
						.getColor(tabColorId[1]));// tabColorId[1]);
				tabsLayoutGroups[i].setVisibility(View.VISIBLE);

			} else {
				tabs[i].setBackgroundResource(tabBgIds[0]);
				tabs[i].setTextColor(Config.getController().getResources()
						.getColor(tabColorId[0]));
				tabsLayoutGroups[i].setVisibility(View.GONE);
			}
		}
	}

	private RecyleResource mRecyleResource = new RecyleResource() {
		@Override
		public void doRecyle() {
			List<String> imgs = new ArrayList<String>();
			imgs.add("jianbian_bg");
			BitmapCache bitmapCache = Config.getController().getBitmapCache();
			for (String img : imgs) {
				Bitmap bmp = bitmapCache.get(img);
				if (bmp != null && bmp.isRecycled() == false) {
					bitmapCache.remove(img);
				}
			}

			System.gc();
		}
	};
}
