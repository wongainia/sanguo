package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.battle.anim.BattleDriver;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.EndGuider;
import com.vikings.sanguo.message.DungeonAttackResp;
import com.vikings.sanguo.message.UserStaminaResetResp;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.ArmInfoSelectData;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.adapter.CampaignAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.TroopAdapter;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.ui.guide.Step10001;
import com.vikings.sanguo.ui.guide.Step12001;
import com.vikings.sanguo.ui.guide.Step13001;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class CampaignTroopSetWindow extends TroopSetWindow {
	protected CampaignInfoClient campaignClient;
	private OtherHeroInfoClient[] ohics;// 敌方将领
	protected byte minTalent;// 最低将领品质
	public static boolean isGuild = false;
	public static boolean step208Guide = false;
	private TroopAdapter troopAdapter = new TroopAdapter(null, false);

	private boolean systemHero = false;

	// 防止点击出征时 把假将领刷掉了
	private boolean alreadyEnterGuide = false;

	public void open(CampaignInfoClient campaignClient) {
		if (null == campaignClient)
			return;
		this.campaignClient = campaignClient;
		this.minTalent = campaignClient.getPropCampaignMode()
				.getMinHeroTalent();
		this.systemHero = !campaignClient.getPropCampaignMode().canSetOwnHero();
		doOpen();
		this.firstPage();
	}

	@Override
	protected void init() {
		super.init();
		setContentAboveTitle(R.layout.campaign_attack_top);
		ViewUtil.setGone(gradientBelowLayout);
		setCampaignInfo();
		setStaticDesc();
		setBottomButton("出   战", getBottomBtnListener());
		setBottomPadding();
		setUserStamina();
	}

	@Override
	protected void setinitHeroInfos() {
		if (systemHero) {
			hics[0] = getSystemHero(1, campaignClient.getPropCampaignMode()
					.getOwnHeroId1());
			hics[1] = getSystemHero(2, campaignClient.getPropCampaignMode()
					.getOwnHeroId2());
			hics[2] = getSystemHero(3, campaignClient.getPropCampaignMode()
					.getOwnHeroId3());
		} else {
			List<HeroInfoClient> list = Account.getCampaignHeros();
			hics[0] = list.get(0);
			hics[1] = list.get(1);
			hics[2] = list.get(2);
		}

	}

	@Override
	public void setMainHero(ViewGroup viewGroup, BaseHeroInfoClient hic) {
		super.setMainHero(viewGroup, hic);
		if (systemHero) {
			setSystemHeroInfo(viewGroup, hic);
		}
	}

	@Override
	public void setExtHero(ViewGroup viewGroup, BaseHeroInfoClient hic,
			boolean unlock) {
		if (systemHero) {
			super.setExtHero(viewGroup, hic, true);
			setSystemHeroInfo(viewGroup, hic);
		} else {
			super.setExtHero(viewGroup, hic, unlock);
		}
	}

	protected void setSystemHeroInfo(ViewGroup viewGroup, BaseHeroInfoClient hic) {
		if (null == hic || !hic.isValid()) {
			ViewUtil.setImage(viewGroup.findViewById(R.id.state),
					R.drawable.hero_lock);
			ViewUtil.setText(viewGroup.findViewById(R.id.desc), "");
			ViewUtil.setGone(viewGroup.findViewById(R.id.heroIcon));
			ViewUtil.setGone(viewGroup, R.id.progressDesc);
		}
		viewGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				controller.alert("本关为系统将领助战，无需自己派遣将领");
			}
		});
	}

	private HeroInfoClient getSystemHero(long id, int initId) {
		if (initId > 0) {
			try {
				return CacheMgr.heroInitCache.getHeroInfoClient(id,
						Account.user.getId(), initId);
			} catch (GameException e) {
				return HeroInfoClient.newInstance();
			}
		} else {
			return HeroInfoClient.newInstance();
		}
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	private void setUserStamina() {
		setRightTxt("行动：" + Account.user.getUserStamina() + "/"
				+ Account.user.getMaxStamina());
	}

	// 引导用的 副本用 自动上将
	public void setMainInfo() {
		List<HeroInfoClient> heros = new ArrayList<HeroInfoClient>();
		// 取全部将领
		heros.addAll(Account.heroInfoCache.get());
		if (!ListUtil.isNull(heros)) {
			hics[0] = heros.get(0);
			setMainHero(hero1, hics[0]);
		}
	}

	// 引导用的 副本用 自动上将
	public void setMainSpecificInfo() {
		if (HeroChooseListWindow.guideChooseHeroInfoClient != null) {
			hics[0] = HeroChooseListWindow.guideChooseHeroInfoClient;
			setMainHero(hero1, hics[0]);
		}

	}

	// 引导用的 副本用 自动上将
	public void setVoidMainInfo() {
		hics[0] = HeroInfoClient.newInstance();
		setMainHero(hero1, hics[0]);

	}

	protected OnClickListener getBottomBtnListener() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!systemHero)
					if (hics[0] == null || !hics[0].isValid()) {
						controller.alert("请选择主将");
						return;
					}

				if (!checkLeftCount())
					return;

				if (!checkUserStamina())
					return;

				startInvoker();
			}
		};
	}

	protected boolean checkLeftCount() {
		int[] times = campaignClient.getTimeLimit();
		if (times[1] != 0 && times[0] >= times[1]) {
			controller.alert("副本次数已经用尽");
			return false;
		}
		return true;
	}

	private boolean checkUserStamina() {
		if (Account.user.getUserStamina() < getStaminaCost()) {
			controller.confirm("行动力不足", CustomConfirmDialog.DEFAULT,
					"是否花费#rmb#" + Account.user.getStaminaRecoverPrice()
							+ "恢复全部行动力?", "", new CallBack() {

						@Override
						public void onCall() {
							if (Account.user.getCurrency() < Account.user
									.getStaminaRecoverPrice()) {
								new ToActionTip(Account.user
										.getStaminaRecoverPrice()).show();
								return;
							}
							new UserStaminaRecoverInvoker().start();

						}
					}, null);
			return false;
		}
		return true;
	}

	protected int getStaminaCost() {
		return campaignClient.getPropCampaignMode().getUserStaminaCost();
	}

	protected void startInvoker() {
		new BattleDungeonAttackInvoker(false) //
				.start();
	}

	protected void setStaticDesc() {
		// if (minTalent > 0) {
		// try {
		// HeroQuality quality = (HeroQuality) CacheMgr.heroQualityCache
		// .get(minTalent);
		// ViewUtil.setVisible(gradientLayout);
		// ViewUtil.setRichText(
		// gradientLayout,
		// R.id.gradientMsg,
		// "将领最低品质要求"
		// + StringUtil.color(quality.getName(),
		// quality.getColor()));
		// } catch (GameException e) {
		// ViewUtil.setGone(gradientLayout);
		// Log.e("", e.getMessage());
		// }
		// } else {
		// ViewUtil.setGone(gradientLayout);
		// }

	}

	// 设置战役信息
	protected void setCampaignInfo() {
		View view = window.findViewById(R.id.campaignIcon);
		new ViewImgScaleCallBack(campaignClient.getImg(),
				view.findViewById(R.id.icon), Constants.COMMON_ICON_WIDTH,
				Constants.COMMON_ICON_HEIGHT);

		ViewUtil.setText(window, R.id.campaignName, campaignClient
				.getPropCampaign().getName());

		ViewUtil.setImage(window, R.id.difficultIcon,
				campaignClient.getDifficultyImgResId());
		if (null == ohics)
			ohics = campaignClient.getEnemyHeros();
		if (null != ohics[0]) {
			ViewUtil.setRichText(window, R.id.heroName,
					"敌将:" + ohics[0].getColorHeroName());
		} else {
			ViewUtil.setText(window, R.id.heroName, "敌将:无");
		}

		ViewUtil.setText(window, R.id.armCount,
				"敌军:" + campaignClient.getEnemyArmsCount());
	}

	@Override
	public void showUI() {
		if (alreadyEnterGuide)
			return;
		super.showUI();
		setHeros();
		setUserStamina();
		if (isGuild) {
			setMainSpecificInfo();
			isGuild = false;
		}

		if (step208Guide) {
			setVoidMainInfo();
			step208Guide = false;
		}
		// 处理副本 1-4、1-5、1-7、1-8的引导
		actGuide();

		herosAbilityTotal();
	}

	// 处理副本1-4、 1-5、1-7、1-8的说话引导
	private void actGuide() {
		if (!StepMgr.isRunning()) {
			switch (campaignClient.getId()) {
			case BaseStep.ACT_CAMPAINGN_1_4:
				if (Step13001.checkCondition(BaseStep.ACT_CAMPAINGN_1_4)) {
					new Step13001().run();
					setMainHero(hero1,
							BaseStep.getFakeGuideHero(BaseStep.heroInit1));
					alreadyEnterGuide = true;
				}
				break;
			case BaseStep.ACT_CAMPAINGN_1_5:
				if (Step10001.checkCondition(BaseStep.ACT_CAMPAINGN_1_5)) {
					new Step10001().run();
					setMainHero(hero1,
							BaseStep.getFakeGuideHero(BaseStep.heroInit1));
					setExtHero(hero2,
							BaseStep.getFakeGuideHero(BaseStep.heroInit2), true);
					alreadyEnterGuide = true;
				}
				break;
			// case BaseStep.ACT_CAMPAINGN_1_7:
			// if (Step11001.checkCondition(BaseStep.ACT_CAMPAINGN_1_7)) {
			// new Step11001().run();
			// setMainHero(hero1,
			// BaseStep.getFakeGuideHero(BaseStep.heroInit1));
			// setExtHero(hero2,
			// BaseStep.getFakeGuideHero(BaseStep.heroInit2), true);
			// alreadyEnterGuide = true;
			// }
			// break;
			case BaseStep.ACT_CAMPAINGN_1_8:
				if (Step12001.checkCondition(BaseStep.ACT_CAMPAINGN_1_8)) {
					new Step12001().run();
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected ObjectAdapter getAdapter() {
		// 设置士兵信息

		troopAdapter.setTroopEffectInfo(Account.getUserTroopEffectInfo());
		return troopAdapter;
	}

	@Override
	protected List<ArmInfoSelectData> getArmInfoSelectDatas()
			throws GameException {
		List<ArmInfoSelectData> datas = new ArrayList<ArmInfoSelectData>();

		if (null != campaignClient
				&& null != campaignClient.getPropCampaignMode()
				&& campaignClient.getPropCampaignMode().canSetOwnTroop()) {
			List<ArmInfoClient> selfArmInfos = Account.myLordInfo
					.getTroopInfo();
			if (null != selfArmInfos && !selfArmInfos.isEmpty())
				for (ArmInfoClient armInfo : selfArmInfos) {
					ArmInfoSelectData data = new ArmInfoSelectData(
							armInfo.getId(), armInfo.getCount(),
							armInfo.getCount(), true, false);
					datas.add(data);
				}
		}
		List<ArmInfoClient> armInfos = campaignClient.getSelfArms();
		if (null != armInfos && !armInfos.isEmpty())
			for (ArmInfoClient armInfo : armInfos) {
				ArmInfoSelectData data = new ArmInfoSelectData(armInfo.getId(),
						armInfo.getCount(), armInfo.getCount(), false, false);
				datas.add(data);
			}
		return datas;
	}

	@Override
	protected List<HeroIdInfoClient> tidyHeroData() throws GameException {
		if (systemHero)
			return new ArrayList<HeroIdInfoClient>();
		else
			return super.tidyHeroData();
	}

	@Override
	protected BriefFiefInfoClient getHeroChooseBriefFief() {
		return null;
	}

	@Override
	public void handleItem(Object o) {
	}

	private class UserStaminaRecoverInvoker extends BaseInvoker {
		private UserStaminaResetResp resp;

		@Override
		protected String loadingMsg() {
			return "恢复行动力";
		}

		@Override
		protected String failMsg() {
			return "恢复行动力失败";
		}

		@Override
		protected void fire() throws GameException {
			int count = Account.user.getUserStaminaResetCount();
			resp = GameBiz.getInstance().userStaminaReset(
					Account.user.getStaminaRecoverPrice());
			Account.user.setStaminaRecoverTimes(count++);
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRic(), true);
			ctr.alert("行动力已恢复");
			setUserStamina();
		}

	}

	private class BattleDungeonAttackInvoker extends BaseInvoker {
		private BattleLogInfoClient blic;
		private BattleDriver battleDriver;
		private boolean help;

		/**
		 * @param campaignClient
		 * @param hics
		 * @param help
		 *            是否使用系统将领
		 */
		public BattleDungeonAttackInvoker(boolean help) {
			this.help = help;
		}

		@Override
		protected String loadingMsg() {
			return "数据加载中...";
		}

		@Override
		protected String failMsg() {
			return "获取数据失败";
		}

		@Override
		protected void onFail(GameException exception) {
			super.onFail(exception);
			// 失败后刷新界面 避免兵力不对
			Config.getController().getCurPopupUI().showUI();
		}

		@Override
		protected void fire() throws GameException {
			// 是否打过第四关副本战役
			if (campaignClient.getId() == CampaignInfoClient.CAMPAIGN_1_4
					&& !BaseStep.isStep501Enter()) {
				new EndGuider(BaseStep.INDEX_STEP501_ENTER).start();
			}

			boolean first = (campaignClient.getTimes() == 0); // 是否未胜利过
			DungeonAttackResp resp = GameBiz.getInstance().dungeonAttack(
					campaignClient.getPropCampaign().getActId(),
					campaignClient.getId(), tidyHeroData(), help);
			blic = new BattleLogInfoClient();
			blic.init(resp.getLog(), campaignClient.getBgName(),
					campaignClient.getWall());
			battleDriver = new BattleDriver(blic, resp.getRi());
			// TODO 下载战斗图片
			CacheMgr.downloadBattleImgAndSound(blic);
			if (blic.isMeWin()) {
				if (campaignClient.getId() == CampaignInfoClient.CAMPAIGN_1_4
						&& Config.firstComplement1_4_win == 0)
					Config.firstComplement1_4_win = 3;
				Config.latestDungeonResult = 1;
				CampaignAdapter.isFromBattleResult = true;
				if (first)
					CampaignAdapter.isFirstWin = true;
			} else if (blic.isMeLose()) {
				Config.latestDungeonResult = -1;
				CampaignAdapter.isFromBattleResult = false;
				if (campaignClient.getId() == CampaignInfoClient.CAMPAIGN_1_4) {
					Config.firstComplement1_4 = 1;
				}
			}

			// 记录章节ID 和 成败
			Config.CAMPAIGN_ID = campaignClient.getId();
			Config.CAMPAIGN_IS_FAIL = blic.isMeLose() ? true : false;
		}

		@Override
		protected void onOK() {
			setUserStamina();
			Config.getController().goBack();
			// ctr.goBack();
			new BattleWindow().open(battleDriver, null);
		}
	}

	private void herosAbilityTotal() {
		int ability = 0;
		for (HeroInfoClient heroInfoClient : hics) {
			if (heroInfoClient != null && heroInfoClient.isValid()) {
				ability += heroInfoClient.getHeroAbility();
			}
		}
		ViewUtil.setRichText(gradientLayout, R.id.gradientMsg, "上阵将领总战力:"
				+ ability);
	}

	@Override
	protected boolean needSaveCampaignHero() {
		return !systemHero;
	}

	@Override
	protected int refreshInterval() {
		return 1000;
	}

	@Override
	protected void refreshUI() {
		setUserStamina();
	}

	@Override
	protected CallBack getCallBackAfterChooseHero() {
		return new CallBack() {
			@Override
			public void onCall() {
				herosAbilityTotal();
			}
		};
	}
}
