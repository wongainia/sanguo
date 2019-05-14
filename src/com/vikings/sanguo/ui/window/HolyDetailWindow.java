package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.HolyCategory;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.model.HolyTroop;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.OtherFiefInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.thread.ViewRichTextCallBack;
import com.vikings.sanguo.ui.alert.HeroSkillTip;
import com.vikings.sanguo.ui.alert.HolyDescTip;
import com.vikings.sanguo.ui.alert.OpenEvilDoorConfirm;
import com.vikings.sanguo.ui.alert.ToShopTip;
import com.vikings.sanguo.ui.alert.TroopDetailTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;
import com.vikings.sanguo.widget.FiefDetailTopInfo;

public class HolyDetailWindow extends CustomPopupWindow implements
		OnClickListener {
	private Button occupyBtn, assistAttackBtn;
	private ImageView icon;
	private TextView name, desc, dropDesc, belowBtn;
	private ViewGroup descFrame, enemyContent, belowBtnFrame;
	private BriefFiefInfoClient bfic;
	private RichBattleInfoClient rbic;
	private OtherFiefInfoClient ofic;
	private HolyProp hpHolyProp;
	private HolyProp holyProp;

	public void open(BriefFiefInfoClient bfic) {
		this.bfic = bfic;
		new QueryDataInvoker().start();
	}

	@Override
	protected void init() {
		super.init("外敌入侵");
		setContent(R.layout.holy_detail);
		icon = (ImageView) window.findViewById(R.id.icon);
		ViewUtil.setGone(window, R.id.scaleName);
		name = (TextView) window.findViewById(R.id.name);
		desc = (TextView) window.findViewById(R.id.desc);
		dropDesc = (TextView) window.findViewById(R.id.dropDesc);
		setBottomButton("", this);
		belowBtnFrame = (ViewGroup) window.findViewById(R.id.belowBtnFrame);
		belowBtn = (TextView) window.findViewById(R.id.belowBtn);
		descFrame = (ViewGroup) window.findViewById(R.id.descFrame);
		descFrame.setOnClickListener(this);
		enemyContent = (ViewGroup) window.findViewById(R.id.enemyContent);

		// 征讨外敌
		occupyBtn = (Button) content.findViewById(R.id.occupyBtn);
		occupyBtn.setOnClickListener(this);
		// 协助进攻
		assistAttackBtn = (Button) content.findViewById(R.id.assistAttackBtn);
		assistAttackBtn.setOnClickListener(this);
		setValue();
	}

	private void setValue() {
		try {
			holyProp = (HolyProp) CacheMgr.holyPropCache.get(bfic.getId());
			List<HolyTroop> ls = CacheMgr.holyTroopCache.getHolyTroop(holyProp
					.getPropId());

			for (HolyTroop it : ls) {
				ViewGroup vg = (ViewGroup) controller
						.inflate(R.layout.alert_holy_item);
				if (it.getType() == HolyTroop.TYPE_HERO) {
					ViewUtil.setVisible(vg, R.id.heroLayout);
					ViewUtil.setGone(vg, R.id.armLayout);
					final OtherHeroInfoClient ohic = CacheMgr.heroInitCache
							.getOtherHeroInfoClient(it.getTroopId(), 0,
									it.getTroopId());
					// final OtherHeroInfoClient ohic = CampaignInfoClient
					// .getHero();
					if (null != ohic) {
						IconUtil.setHeroIconScale(
								vg.findViewById(R.id.heroLayout), ohic);
						ViewUtil.setRichText(
								vg,
								R.id.heroInfo,
								ohic.getColorTypeName()
										+ "   "
										+ StringUtil.getHeroName(
												ohic.getHeroProp(),
												ohic.getHeroQuality())
										+ StringUtil.color(
												"   Lv:" + ohic.getLevel(),
												R.color.color7));

						// troopCnt; // 兵种数量 (type=TYPE_HERO(2)时 此字段为：0：主将 1：副将)
						ViewUtil.setText(vg, R.id.herotype,
								StringUtil.getHeroNameByType(it.getTroopCnt()));
						ViewUtil.setText(vg, R.id.armProps,
								ohic.getOtherHeroArmPropsName());
						ViewGroup skillLayout = (ViewGroup) vg
								.findViewById(R.id.skillLayout);
						for (HeroSkillSlotInfoClient ossic : ohic
								.getSkillSlotInfos()) {
							if (null != ossic.getBattleSkill()) {
								ViewUtil.addSkillImageView(skillLayout, ossic
										.getBattleSkill().getIcon());
							}
						}
						vg.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// new HeroSkillTip(ohic.getSkillSlotInfos(),
								// ohic
								// .getArmPropInfos(), -1).show();
								new OthersHeroDetailWindow().open(ohic);
							}
						});
						enemyContent.addView(vg);
					}
				} else if (it.getType() == HolyTroop.TYPE_ARM) {
					ViewUtil.setGone(vg, R.id.heroLayout);
					ViewUtil.setVisible(vg, R.id.armLayout);
					final TroopProp tp = (TroopProp) CacheMgr.troopPropCache
							.get(it.getTroopId());
					new ViewImgScaleCallBack(tp.getIcon(),
							vg.findViewById(R.id.icon),
							Constants.ARM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
							Constants.ARM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
					vg.findViewById(R.id.iconGroup).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									new TroopDetailTip().show(tp); // , null
								}
							});
					if (tp.isBoss()) {
						ViewUtil.setVisible(vg, R.id.sysTroop);
						ViewUtil.setText(vg, R.id.content, "BOSS");
					} else {
						ViewUtil.setGone(vg, R.id.sysTroop);
					}
					ViewUtil.setRichText(vg, R.id.count,
							"#arm#x" + it.getTroopCnt());
					ViewUtil.setRichText(vg, R.id.itemName, tp.getName());
					ViewUtil.setRichText(vg, R.id.itemDesc, tp.getDesc());
					enemyContent.addView(vg);
				}
			}

		} catch (GameException e) {
			e.printStackTrace();
		}

		ViewGroup iconLayout = (ViewGroup) content
				.findViewById(R.id.iconLayout);
		IconUtil.setFiefIconWithBattleState(iconLayout, bfic);
		ViewUtil.setRichText(name, holyProp.getEvilDoorName());
		ViewUtil.setRichText(desc, holyProp.getDesc());
		ViewUtil.setRichText(dropDesc, holyProp.getBonusDesc());
		setButton();
	}

	@Override
	public void showUI() {
		updateView();
		super.showUI();
	}

	private void updateView() {

		if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
				bfic.getBattleState(), bfic.getBattleTime()))) {

			if (FiefDetailTopInfo.getBattleTotal(rbic) >= hpHolyProp
					.getMaxReinforceUser()) {
				ViewUtil.disableButton(assistAttackBtn);
			} else {
				ViewUtil.enableButton(assistAttackBtn);
			}

		} else {
				{
					if (FiefDetailTopInfo.getBattleTime(ofic.getNextExtraBattleTime()) > 0 ? false
						: true) {
						ViewUtil.enableButton(occupyBtn);
						Item item = FiefDetailTopInfo.getItem(hpHolyProp,
								Constants.OCCUPY);

						String occupyName = "征讨外敌";
						if (bfic.isMyFief()) {
							occupyName = "开启战场";
						}

						if (item == null) {
							ViewUtil.setText(occupyBtn, occupyName);
						} else {
							new ViewRichTextCallBack(item.getImage(), "stub.png",
									occupyName + "  ", hpHolyProp.getItemCost() + "",
									occupyBtn, 30, 30);
						}
					} else {
						ViewUtil.disableButton(occupyBtn);
						ViewUtil.setRichText(occupyBtn,
							StringUtil.color(
									DateUtil.formatTime(FiefDetailTopInfo
											.getBattleTime(ofic
													.getNextExtraBattleTime()))
											+ ((bfic.isMyFief() == true) ? "后可开启"
													: "后可征讨"), R.color.color24));
				}
			}
		}		
	}

	private void setButton() {
		// 战斗中 :显示协助进攻，非战斗:显示征讨外敌
		if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
				bfic.getBattleState(), bfic.getBattleTime()))) {
			initReinforceButton();
			ViewUtil.setGone(occupyBtn);
		} else {
			ViewUtil.setVisible(occupyBtn);
			ViewUtil.setGone(assistAttackBtn);
		}

		Item item = FiefDetailTopInfo.getItem(hpHolyProp,
				Constants.ASSISTATTACK);
		
		if(bfic.isMyFief())
		{
			ViewUtil.setText(assistAttackBtn, "协助防守");
			if (item != null) {
				new ViewRichTextCallBack(item.getImage(), "stub.png",
						"协助防守   ", hpHolyProp.getItemReinforceCost() + "",
						assistAttackBtn, 30, 30);
			}
		}
		else if (!Account.isMy(bfic.getAttacker())) {
			if (item != null) {
				ViewUtil.setText(assistAttackBtn, "协助进攻");
				new ViewRichTextCallBack(item.getImage(), "stub.png",
						"协助进攻   ", hpHolyProp.getItemReinforceCost() + "",
						assistAttackBtn, 30, 30);
			}
		}

		ViewUtil.setGone(belowBtnFrame);
		ViewUtil.setGone(belowBtn);

		if (bfic != null && bfic.canOccupied() && bfic.isHoly()
				&& !bfic.isMyFief()) {
			ViewUtil.setGone(content, R.id.bottom_layout);
		}
	}

	@Override
	protected int refreshInterval() {
		return 1000;
	}

	@Override
	public void onClick(View v) {
		if (v == descFrame) {
			new HolyDescTip(holyProp).show();
		} else if (v == occupyBtn) {
			if (!FiefDetailTopInfo.SpecialConsumableIsEnough(hpHolyProp,
					Constants.OCCUPY)) {
				new ToShopTip(hpHolyProp.getItemId()).show();
				return;
			}
			// doClose();
			// Config.getController().getCurPopupUI().doClose();
			// new TroopMoveDetailWindow().open(Account.richFiefCache
			// .getManorFief().brief(), bfic, null,
			// com.vikings.sanguo.Constants.TROOP_OCCUPY);

			if (null != holyProp
					&& bfic.getUnitCount() < holyProp.getMinArmCountOpenDoor()) {
				controller
						.alert("开启恶魔之门失败",
								"你的士兵太少了，开启恶魔之门至少需要"
										+ StringUtil.color(
												""
														+ CalcUtil
																.turnToTenThousand(holyProp
																		.getMinArmCountOpenDoor()),
												R.color.k7_color5) + "名士兵", "",
								null, true);
				return;
			}

			new OpenEvilDoorConfirm(bfic).show();

		} else if (v == assistAttackBtn) {

			if (!Account.isMy(bfic.getAttacker())) {
				if (!FiefDetailTopInfo.SpecialConsumableIsEnough(hpHolyProp,
						Constants.ASSISTATTACK)) {
					new ToShopTip(hpHolyProp.getItemId()).show();
					return;
				}
			}

			// 需要加Vip 等级限制条件
			doClose();
			Config.getController().getCurPopupUI().doClose();
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, null,
					com.vikings.sanguo.Constants.TROOP_REINFORCE);
		}
	}

	private void initReinforceButton()
	{
		if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
				bfic.getBattleState(), bfic.getBattleTime())))
		{
			if(!bfic.isMyFief() && bfic.getAttackerId() == Account.user.getId())
			{
				ViewUtil.setVisible(assistAttackBtn);
				ViewUtil.setRichText(assistAttackBtn, "协助进攻");
			}
			else if(bfic.isMyFief() && bfic.getAttackerId() != Account.user.getId())  //自己的领地   协助防守自己
			{
				ViewUtil.setVisible(assistAttackBtn);
				ViewUtil.setRichText(assistAttackBtn, "协助防守");
			}
			else
			{
				if(rbic.isAttackerById(bfic.getAttackerId()))
				{
					if((bfic.isHoly() && rbic.canReinforceAttackInShenJi()) || (!bfic.isHoly()&&rbic.canReinforceAttack()))
					{
						ViewUtil.setVisible(assistAttackBtn);
						ViewUtil.setRichText(assistAttackBtn, "协助进攻");
					}
					else
					{
						ViewUtil.setGone(assistAttackBtn);
					}					
				}
				else
				{
					if((bfic.isHoly() &&rbic.canReinforceDefendInShenJi()) || (!bfic.isHoly()&&rbic.canReinforceDefend()))
					{
						ViewUtil.setVisible(assistAttackBtn);
						ViewUtil.setRichText(assistAttackBtn, "协助防守");
					}
					else
					{
						ViewUtil.setGone(assistAttackBtn);
					}
				}
			}
		}
	}
	
	private class QueryDataInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "加载数据";
		}

		@Override
		protected String failMsg() {
			return "加载数据失败";
		}

		@Override
		protected void fire() throws GameException {
			if (bfic != null) {
				// 可以获取重镇的参战人数
				rbic = GameBiz.getInstance()
						.richBattleInfoQuery(bfic.getId(), false).getInfo();
				// 可以获得下次参战时间
				ofic = GameBiz.getInstance().otherFiefInfoQuery(bfic.getId());
				// 可以获得征讨外敌 或者 协助进攻需要消耗的物品ID和数量
				hpHolyProp = (HolyProp) CacheMgr.holyPropCache
						.get(bfic.getId());
			}

		}

		@Override
		protected void onOK() {
			doOpen();
		}
	}
	
}
