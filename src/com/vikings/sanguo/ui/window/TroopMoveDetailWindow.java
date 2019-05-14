package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.RecoverStaminaInvoker;
import com.vikings.sanguo.invoker.TroopRiseInvoker;
import com.vikings.sanguo.message.RichBattleInfoQueryResp;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.ArmInfoSelectData;
import com.vikings.sanguo.model.BattleHeroInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.FiefScale;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.OtherFiefInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.model.RichFiefInfoClient;
import com.vikings.sanguo.protos.BattleAttackType;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.TroopAdapter;
import com.vikings.sanguo.ui.alert.MsgConfirm;
import com.vikings.sanguo.ui.alert.ResetChargeTip;
import com.vikings.sanguo.ui.alert.TroopMoveTip;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class TroopMoveDetailWindow extends TroopSetWindow implements CallBack {

	private BriefFiefInfoClient myBfic, targetBfic;
	private int type;
	private OtherFiefInfoClient otherFief;
	private OtherUserClient ouc;
	private boolean initTroop = false;
	private TroopAdapter troopAdapter = new TroopAdapter(this, true);
	// step506引导标识位
	public static boolean isGuide = false;

	private RichBattleInfoClient rbic;
	private long[] battleHeroIds = new long[3]; // 已经在战场上的将领id

	public void open(BriefFiefInfoClient myBfic, BriefFiefInfoClient otherBfic,
			OtherUserClient ouc, int type) {
		if (null == myBfic)
			return;
		this.myBfic = myBfic;
		this.targetBfic = otherBfic;
		this.ouc = ouc;
		this.type = type;
		if (null != targetBfic && type == Constants.TROOP_REINFORCE
				&& targetBfic.isInBattle()) {
			new FetchInvoker(targetBfic.getId()).start();
		} else {
			doOpen();
			firstPage();
		}
	}

	public void open(BriefFiefInfoClient myBfic, BriefFiefInfoClient otherBfic,
			OtherUserClient ouc, int type, RichBattleInfoClient rbic) {
		if (null == myBfic)
			return;
		this.myBfic = myBfic;
		this.targetBfic = otherBfic;
		this.ouc = ouc;
		this.type = type;
		this.rbic = rbic;
		if (null == rbic && null != targetBfic
				&& type == Constants.TROOP_REINFORCE && targetBfic.isInBattle()) {
			new FetchInvoker(targetBfic.getId()).start();
		} else {
			doOpen();
			firstPage();
		}
	}

	@Override
	protected String getTitle() {
		if (myBfic.getId() == Account.manorInfoClient.getPos()) {
			return "派遣主城部队";
		} else {
			return "派遣领地部队";
		}
	}

	// 点击占领后的引导处理事件
	public void guideOccupy() {
		try {
			new TroopRiseInvoker(Constants.TYPE_NOAMRL,
					BattleAttackType.E_BATTLE_COMMON_ATTACK.getNumber(),
					myBfic, targetBfic, getTroop(), tidyHeroData(), 0, true)
					.start();
		} catch (GameException e) {
			e.printStackTrace();
		}
	}

	// 引导用的 副本用 自动上将
	public void setMainInfo() {
		List<HeroInfoClient> heros = new ArrayList<HeroInfoClient>();
		// 取全部将领 上将要求（只有一个将领且是孔融则上孔融；否则上其它将领）
		heros.addAll(Account.heroInfoCache.get());
		if (!ListUtil.isNull(heros)) {
			HeroInfoClient temp = null;
			if (heros.size() > 1) {
				for (HeroInfoClient hic : heros) {
					if (hic.getHeroId() == 10000/* 孔融 */) {
					} else {
						temp = hic;
						break;
					}

				}
			} else {
				temp = heros.get(0);
			}

			hics[0] = temp;
			setMainHero(hero1, hics[0]);
		}
	}

	@Override
	protected void setinitHeroInfos() {
		if (null != rbic) {
			if (Account.user.getId() == targetBfic.getAttackerId()) {
				ViewUtil.setVisible(heroLayout);
				List<BattleHeroInfoClient> bhics = rbic.getBbic()
						.getAttackHeroInfos();
				if (bhics.isEmpty())
					super.setinitHeroInfos();
				else
					setHeroInBattle(bhics);

			} else if (Account.user.getId() == targetBfic.getUserId()) {
				ViewUtil.setVisible(heroLayout);
				List<BattleHeroInfoClient> bhics = rbic.getBbic()
						.getDefendHeroInfos();
				if (bhics.isEmpty())
					super.setinitHeroInfos();
				else
					setHeroInBattle(bhics);
			} else {
				super.setinitHeroInfos();
				ViewUtil.setGone(heroLayout);
			}
		} else {
			super.setinitHeroInfos();
		}

	}

	private void setHeroInBattle(List<BattleHeroInfoClient> bhics) {
		for (BattleHeroInfoClient bhic : bhics) {
			if (bhic.isMain()) {
				battleHeroIds[0] = bhic.getId();
			} else {
				if (battleHeroIds[1] <= 0)
					battleHeroIds[1] = bhic.getId();
				else
					battleHeroIds[2] = bhic.getId();
			}

		}

		if (battleHeroIds[0] > 0
				&& Account.heroInfoCache.hasHero(battleHeroIds[0])) {
			hero1.setOnClickListener(null);
			hics[0] = Account.heroInfoCache.get(battleHeroIds[0]);
		} else {
			hics[0] = HeroInfoClient.newInstance();
		}

		if (battleHeroIds[1] > 0
				&& Account.heroInfoCache.hasHero(battleHeroIds[1])) {
			hero2.setOnClickListener(null);
			hics[1] = Account.heroInfoCache.get(battleHeroIds[1]);
		} else {
			hics[1] = HeroInfoClient.newInstance();
		}

		if (battleHeroIds[2] > 0
				&& Account.heroInfoCache.hasHero(battleHeroIds[2])) {
			hero3.setOnClickListener(null);
			hics[2] = Account.heroInfoCache.get(battleHeroIds[2]);
		} else {
			hics[2] = HeroInfoClient.newInstance();
		}
	}

	protected List<HeroIdInfoClient> tidyHeroData() throws GameException {
		List<HeroIdInfoClient> list = new ArrayList<HeroIdInfoClient>();
		if (null != hics) {
			for (int i = 0; i < hics.length; i++) {
				HeroInfoClient hic = hics[i];
				if (null != hic && hic.isValid() && battleHeroIds[i] == 0) {
					HeroIdInfoClient hiic = new HeroIdInfoClient(hic.getId(),
							hic.getHeroId(), hic.getStar(), hic.getTalent());
					if (null != rbic && rbic.canReinforceAttack()) {
						if (i == 0)
							hiic.setRole(HeroIdInfoClient.HERO_ROLE_ATTACK_MAIN);
						else
							hiic.setRole(HeroIdInfoClient.HERO_ROLE_ATTACK_ASSIST);
					} else {
						if (i == 0)
							hiic.setRole(HeroIdInfoClient.HERO_ROLE_DEFEND_MAIN);
						else
							hiic.setRole(HeroIdInfoClient.HERO_ROLE_DEFEND_ASSIST);
					}
					list.add(hiic);
				}
			}
		}
		return list;
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	protected void init() {
		super.init();
		setContentAboveTitle(R.layout.common_top_info);
		setBottomButton();
		setTopViewInfo();
		setLeftTxt("#arm#" + myBfic.getUnitCount());
		setRightTxt("#hero_limit#" + myBfic.getHeroCount());
	}

	protected void setTopViewInfo() {
		new ViewImgScaleCallBack(targetBfic.getIcon(),
				window.findViewById(R.id.icon), Constants.FIEF_ICON_WIDTH
						* Config.SCALE_FROM_HIGH, Constants.FIEF_ICON_HEIGHT
						* Config.SCALE_FROM_HIGH);
		new AddrLoader(window.findViewById(R.id.fiefName),
				TileUtil.fiefId2TileId(targetBfic.getId()), "", "  ("
						+ targetBfic.getCountry() + ")", AddrLoader.TYPE_SUB);
		ViewUtil.setText(window, R.id.scaleName, targetBfic.getName());
		ViewUtil.setText(window, R.id.lord, "领主:"
				+ targetBfic.getLord().getNickName());
		ViewUtil.setRichText(window, R.id.cityDefense,
				"城防:" + targetBfic.getDefenceSkillName());
		HeroIdInfoClient mainHero = targetBfic.getMainHero();
		if (null == mainHero)
			ViewUtil.setText(window, R.id.mainHero, "武将:" + "无");
		else
			ViewUtil.setRichText(
					window,
					R.id.mainHero,
					StringUtil.color("武将:",
							controller.getResourceColorText(R.color.k7_color3))
							+ mainHero.getHeroProp().getName());
		ViewUtil.setText(window, R.id.armCount,
				"兵力:" + targetBfic.getUnitCount());
	}

	private void setMaxTooopCnt(int curHp) {
		int heroCount = getSelectHeroCount();
		String hp = String.valueOf(curHp);
		StringBuilder buf = new StringBuilder();
		if (Constants.TROOP_DISPATCH != type) {
			int maxHp = targetBfic.getFiefScale().getMaxReinforceCount();
			if (curHp > maxHp)
				hp = StringUtil.color(hp, "red");
		}
		buf.append("已选择").append(hp).append("兵力，" + heroCount + "名将领");

		ViewUtil.setRichText(contentBelowTitle, R.id.gradientMsg,
				buf.toString());
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void destory() {
		super.destory();
		controller.removeContentFullScreen(this.window);
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		super.getServerData(resultPage);
		if (type == Constants.TROOP_PLUNDER || type == Constants.TROOP_OCCUPY) {
			// 进攻要校验血量
			otherFief = GameBiz.getInstance().otherFiefInfoQuery(
					targetBfic.getId());
		}
		initTroop = true;
	}

	@Override
	protected void updateUI() {
		super.updateUI();
		dealwithEmptyAdpter();
		setMaxTooopCnt(((TroopAdapter) adapter).getTroopCnt());
	}

	protected String getEmptyShowText() {
		return "主城士兵为0，从"
				+ StringUtil.color("其它资源点-调动部队-撤回主城", R.color.color11) + "再征战。";
	}

	@Override
	public void onCall() {
		setMaxTooopCnt(((TroopAdapter) adapter).getTroopCnt());
	}

	private void setBottomButton() {
		switch (type) {
		case Constants.TROOP_DISPATCH:
			setBottomButton("调    遣", dispatchListener);
			break;
		case Constants.TROOP_REINFORCE:
			setBottomButton("增    援", dispatchListener);
			break;
		case Constants.TROOP_PLUNDER:
			setBottomButton("掠    夺", dispatchListener);
			break;
		case Constants.TROOP_OCCUPY:
			setBottomButton("占    领", dispatchListener);
			if (targetBfic.isHoly()
					&& BriefUserInfoClient.isNPC(targetBfic.getUserId())
					&& !targetBfic.canOccupied()) {
				setBottomButton("征 讨", dispatchListener);
			}
			break;
		case Constants.TROOP_DUEL:
			setBottomButton("单    挑", dispatchListener);
			break;
		case Constants.TROOP_MASSACRE:
			setBottomButton("屠    城", dispatchListener);
			break;
		default:
			break;
		}
	}

	private List<ArmInfoClient> getTroop() {
		return ((TroopAdapter) adapter).getTroops();
	}

	private OnClickListener dispatchListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!initTroop) {
				controller.alert("请稍等，数据加载中");
				return;
			}

			if (check()) {
				try {
					new TroopMoveTip(type, myBfic, targetBfic, ouc, getTroop(),
							tidyHeroData()).show();
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}

		// 这里统一做可否出征的校验
		private boolean check() {
			int armCount = getSelectTroopCount();

			// 调遣 增援 不能为兵将=0
			if ((type == Constants.TROOP_DISPATCH || type == Constants.TROOP_REINFORCE)
					&& armCount == 0 && getSelectHeroCount() == 0) {
				controller.alert("请选择将领或士兵");
				return false;
			}

			for (int i = 0; i < hics.length; i++) {
				final HeroInfoClient heroInfoClient = hics[i];
				if (!heroInfoClient.isValid())
					continue;
				// 检查武将体力
				if (type != Constants.TROOP_DISPATCH
						&& heroInfoClient.getStamina() < CacheMgr.heroCommonConfigCache
								.getCostStamina()) {
					final int needRmb = hics[i].getRecoverStaminaCost();

					String msg = "将领体力不足,是否花费"
							+ StringUtil.color(String.valueOf(needRmb), "red")
							+ "元宝直接恢复满体力?";

					new MsgConfirm("体力不足", CustomConfirmDialog.DEFAULT)
							.setOKText("恢复体力").show(msg, new CallBack() {
								@Override
								public void onCall() {
									if (Account.user.getCurrency() < needRmb)
										new ResetChargeTip().show();
									else
										new RecoverStaminaInvoker(
												heroInfoClient,
												heroInfoClient
														.getRecoverStaminaCost(),
												new CallBack() {
													@Override
													public void onCall() {
														setHeros();
													}
												}).start();
								}
							}, null);

					return false;
				}

			}

			// 进攻检查血量
			if (type == Constants.TROOP_PLUNDER
					|| type == Constants.TROOP_OCCUPY) {
				// 兵力为0
				if (armCount == 0) {
					controller.alert("出征士兵数目为0,请先部署出征士兵");
					return false;
				}
				// 2014年5月31日16:31:34付斌确认去掉血量比较，只比较数量
				FiefScale fs = targetBfic.getFiefScale();
				if (fs.getMinCount() > armCount) {
					String msg = CacheMgr.errorCodeCache
							.getMsg(ResultCode.RESULT_FAILED_MIN_BLOOD);
					msg = msg.replace("<fiefscale>", fs.getName());
					msg = msg.replace("<number>", fs.getMinCount() + "");
					controller.alert(msg);
					return false;
				}

				// 资源存储上限 检查仓库是否已经满 提醒是否出征

				if (type == Constants.TROOP_OCCUPY) {
					if (targetBfic.isResource()) {
						int count = Account.richFiefCache.countResSite();
						if (count >= Account.manorInfoClient.getMaxResource()) {
							controller.alert(CacheMgr.errorCodeCache
									.getMsg((short) 1097));
							return false;
						}
					} else {
						int count = Account.richFiefCache
								.getFiefCountExceptManor();
						if (count >= Account.user.getMaxFief()) {
							controller
									.alert("您可以占领的领地数量已经达到上限,请提升主城里科技建筑等级或者放弃一些领地");
							return false;
						}
					}
				}

				// PVE
				if (BriefUserInfoClient.isNPC(targetBfic.getUserId())) {
					long cnt = TroopUtil.minArmCntPVE(TroopUtil
							.troopBlood(otherFief.getInfo()));
					if (armCount < cnt) {
						controller.confirm("确认出征吗？",
								CustomConfirmDialog.MEDIUM, "敌人太强大，建议最少派" + cnt
										+ "名士兵征讨!"
										+ "<br/><br>你也可以发起进攻后，请求家族的同伴前来增援", "",
								new ToNext(), null);
						return false;
					}
				} else {
					// PCV
					// long cnt = TroopUtil.minArmCntPVP(TroopUtil
					// .troopBlood(otherFief.getInfo())); //
					long cnt = TroopUtil.minArmCntPVP(TroopUtil
							.countArm(otherFief.getInfo()));
					if (armCount < cnt) {
						String msg = "你的士兵太少了，注定会失败的<br><br>本次出征至少需要" + cnt
								+ "名士兵";
						controller.alert(msg);
						return false;
					}
				}

			}
			return true;
		}

	};

	private class ToNext implements CallBack {

		@Override
		public void onCall() {
			try {
				new TroopMoveTip(type, myBfic, targetBfic, ouc, getTroop(),
						tidyHeroData()).show();
			} catch (GameException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected List<ArmInfoSelectData> getArmInfoSelectDatas() {
		List<ArmInfoSelectData> datas = new ArrayList<ArmInfoSelectData>();
		if (null != myBfic) {
			// 获取领地驻军
			RichFiefInfoClient rfic = Account.richFiefCache.getInfo(myBfic
					.getId());
			List<ArmInfoClient> aics = rfic.getArriveTroop();

			// 引导要求只派遣8k兵力
			if (isGuide) {
				boolean found8kArm = false;
				for (ArmInfoClient aic : aics) {
					ArmInfoSelectData selectData = new ArmInfoSelectData(aic,
							true, true);
					selectData.setSelCount(aic.getCount());
					if (aic.getCount() >= 8000 && !found8kArm) {
						found8kArm = true;
						selectData.setSelCount(8000);
					} else {
						selectData.setSelCount(0);
					}
					datas.add(selectData);
				}
			}
			if (isGuide) {
				isGuide = false;
				return datas;
			}

			for (ArmInfoClient aic : aics) {
				ArmInfoSelectData selectData = new ArmInfoSelectData(aic, true,
						true);
				selectData.setSelCount(aic.getCount());
				datas.add(selectData);
			}
		}
		return datas;
	}

	@Override
	public void handleItem(Object o) {

	}

	@Override
	protected ObjectAdapter getAdapter() {
		troopAdapter.setTroopEffectInfo(Account.getUserTroopEffectInfo());
		return troopAdapter;
	}

	@Override
	protected BriefFiefInfoClient getHeroChooseBriefFief() {
		return myBfic;
	}

	private class FetchInvoker extends BaseInvoker {
		private long battleId;

		public FetchInvoker(long battleId) {
			this.battleId = battleId;
		}

		@Override
		protected String failMsg() {
			return "获取数据失败";
		}

		@Override
		protected String loadingMsg() {
			return "正在加载";
		}

		@Override
		protected void fire() throws GameException {
			RichBattleInfoQueryResp resp = GameBiz.getInstance()
					.richBattleInfoQuery(battleId, true);
			rbic = resp.getInfo();
		}

		@Override
		protected void onOK() {
			doOpen();
			firstPage();
		}
	}

	@Override
	protected CallBack getCallBackAfterChooseHero() {
		return this;
	}

}