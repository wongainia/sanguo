package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.AddFavorateFief;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.DelFavorateFief;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.OtherFiefInfoClient;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.thread.ViewRichTextCallBack;
import com.vikings.sanguo.ui.window.FiefDetailWindow;
import com.vikings.sanguo.ui.window.TroopMoveDetailWindow;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.utils.VipUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.FiefDetailTopInfo;

public class FamousFiefTip extends CustomConfirmDialog implements
		OnClickListener {
	private Button occupyBtn, detailBtn, favoriteBtn, closeBtn,
			assistAttackBtn, battleBtn, transferBtn, assistDefendBtn;
	private BriefFiefInfoClient bfic;
	private RichBattleInfoClient rbic;
	private OtherFiefInfoClient ofic;
	private HolyProp hpHolyProp;

	public FamousFiefTip() {
		super(CustomConfirmDialog.DEFAULT);
		// 查看战场
		battleBtn = (Button) content.findViewById(R.id.battleBtn);
		battleBtn.setOnClickListener(this);

		// 征讨外敌
		occupyBtn = (Button) content.findViewById(R.id.occupyBtn);
		occupyBtn.setOnClickListener(this);

		// 协助进攻
		assistAttackBtn = (Button) content.findViewById(R.id.assistAttackBtn);
		assistAttackBtn.setOnClickListener(this);

		// 调动部队
		transferBtn = (Button) content.findViewById(R.id.transferBtn);
		transferBtn.setOnClickListener(this);

		// 查看详情
		detailBtn = (Button) content.findViewById(R.id.detailBtn);
		detailBtn.setOnClickListener(this);

		// 协助防守
		assistDefendBtn = (Button) content.findViewById(R.id.assistDefendBtn);
		assistDefendBtn.setOnClickListener(this);

		// 添加收藏
		favoriteBtn = (Button) content.findViewById(R.id.favoriteBtn);
		favoriteBtn.setOnClickListener(this);
		// 关闭
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);

	}

	public void show(BriefFiefInfoClient bfic) {
		this.bfic = bfic;
		refreshInterval = 1000;
		new QueryDataInvoker().start();
	}

	private void showTip() {
		setValue();
		super.show();
	}

	private void setValue() {
		new AddrLoader(getTitle(), TileUtil.fiefId2TileId(bfic.getId()),
				AddrLoader.TYPE_SUB);

		setButton();
		ViewGroup infoLayout = (ViewGroup) content
				.findViewById(R.id.infoLayout);
		IconUtil.setFiefIconWithBattleState(infoLayout, bfic);
		updateDynView();
	}

	protected void updateDynView() {
		FiefDetailTopInfo
				.setState(bfic,
						(ViewGroup) content.findViewById(R.id.detailLayout),
						hpHolyProp);
		ViewGroup infoLayout = (ViewGroup) content
				.findViewById(R.id.infoLayout);
		FiefDetailTopInfo.setFamousBaseFiefInfo(infoLayout, bfic, rbic, ofic,
				hpHolyProp);

		if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
				bfic.getBattleState(), bfic.getBattleTime()))) {
			if (FiefDetailTopInfo.getBattleTotal(rbic) >= hpHolyProp
					.getMaxReinforceUser()) {
				ViewUtil.disableButton(assistAttackBtn);
			} else {
				ViewUtil.enableButton(assistAttackBtn);
			}

		} else {			
					if(bfic != null && bfic.canOccupied() && bfic.isHoly() && !bfic.isMyFief())
					{
						ViewUtil.enableButton(occupyBtn);
					}
					else 
					{
						if (FiefDetailTopInfo.getBattleTime(ofic.getNextExtraBattleTime()) > 0 ? false
								: true) {
							ViewUtil.enableButton(occupyBtn);
						} else {
							ViewUtil.disableButton(occupyBtn);
				}
			}
		}
	}

	public void initButtonVal() {
		Item item = FiefDetailTopInfo.getItem(hpHolyProp,
				Constants.ASSISTATTACK);

		if (!Account.isMy(bfic.getAttacker())) {
			if (item != null) {
				new ViewRichTextCallBack(item.getImage(), "duel_icon",
						"协助进攻   ", hpHolyProp.getItemReinforceCost() + "",
						assistAttackBtn, 30, 30);
			}
		}

		item = FiefDetailTopInfo.getItem(hpHolyProp, Constants.OCCUPY);

		String txtString = "征讨外敌";
		if (bfic.isMyFief()) {
			txtString = "开启战场";
		}
		if (item == null) {
			ViewUtil.setText(occupyBtn, txtString);
		} else {
			new ViewRichTextCallBack(item.getImage(), "duel_icon", txtString
					+ "  ", hpHolyProp.getItemCost() + "", occupyBtn, 30, 30);
		}
	}

	private void setButton() {
		initButtonVal();
		initReinforceButton();
		if (bfic.isMyFief()) { // 自己的资源点
			if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
					bfic.getBattleState(), bfic.getBattleTime()))) { // 战争中
				ViewUtil.setVisible(battleBtn);				
			} else {
				// 开启战场 需要钥匙
				ViewUtil.setVisible(occupyBtn);
				ViewUtil.setVisible(transferBtn);
			}
		} else {
			// 战斗中 :显示协助进攻，非战斗:显示征讨外敌
			
			if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
					bfic.getBattleState(), bfic.getBattleTime()))) {
				ViewUtil.setVisible(battleBtn);								
//				if (bfic.isHoly() && bfic.canOccupied()) {
//					ViewUtil.setRichText(assistAttackBtn, "协助进攻");
//				}
			}
			else {
				if (bfic.isHoly() && bfic.canOccupied())
					ViewUtil.setRichText(occupyBtn, "出兵占领");
				ViewUtil.setVisible(occupyBtn);
			}
			ViewUtil.setVisible(favoriteBtn);
			setFavoriteBtn();
		}
		ViewUtil.setVisible(closeBtn);
		ViewUtil.setVisible(detailBtn);

	}
		
	private void initReinforceButton()
	{
		if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
				bfic.getBattleState(), bfic.getBattleTime())))
		{
			reinforceData();
		}
	}
	
	private boolean reinforceData()
	{
		boolean isReinAttack = true;
		if(!bfic.isMyFief() && bfic.getAttackerId() == Account.user.getId())
		{
			ViewUtil.setVisible(assistAttackBtn);
			ViewUtil.setRichText(assistAttackBtn, "协助进攻");
			isReinAttack = true;
		}
		else if(bfic.isMyFief() && bfic.getAttackerId() != Account.user.getId())  //自己的领地   协助防守自己
		{
			ViewUtil.setVisible(assistAttackBtn);
			ViewUtil.setRichText(assistAttackBtn, "协助防守");
			isReinAttack = false;
		}
		else
		{
			if(rbic.isAttackerById(bfic.getAttackerId()))
			{
				if((bfic.isHoly() && rbic.canReinforceAttackInShenJi()) || (!bfic.isHoly()&&rbic.canReinforceAttack()))
				{
					ViewUtil.setVisible(assistAttackBtn);
					ViewUtil.setRichText(assistAttackBtn, "协助进攻");
					isReinAttack = true;
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
					isReinAttack = false;
				}
				else
				{
					ViewUtil.setGone(assistAttackBtn);
				}
			}
		}
		return isReinAttack;
	}

	private void setFavoriteBtn() {
		if (!Account.isFavoriteFief(bfic.getId())) {
			ViewUtil.setText(favoriteBtn, "添加收藏");
		} else {
			ViewUtil.setText(favoriteBtn, "移除收藏");
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_famous_fief, tip, false);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == occupyBtn) {
			if (Account.user.getCurVip().getLevel() < VipUtil.attackHoly()
					&& hpHolyProp.getType() == HolyProp.TYPE_CAPITAL) {
				new ShopHintTip("友情提示", "VIP等级不足！", "只有达到VIP"
						+ VipUtil.attackHoly() + "或更高VIP等级的玩家才能主动出兵征讨！",
						"立刻充值成为VIP" + VipUtil.attackHoly(), true).show();
				return;
			}
			
			//占领圣都不用钥匙   开启才用
			if(!bfic.isMyFief() && bfic.isHoly() && bfic.canOccupied())
			{
				//占领时  不用钥匙
			}
			else
			{
				if (!FiefDetailTopInfo.SpecialConsumableIsEnough(hpHolyProp,
					Constants.OCCUPY)) {
				new ToShopTip(hpHolyProp.getItemId()).show();
				return;
				}
			}
			
			 if(bfic.isMyFief() && bfic.canOccupied())
			  {
//				   if (!FiefDetailTopInfo.SpecialConsumableIsEnough(hpHolyProp,
//							Constants.OCCUPY)) {
//						new ToShopTip(hpHolyProp.getItemId()).show();
//						return;
//					}
				   if (null != hpHolyProp
							&& bfic.getUnitCount() < hpHolyProp
									.getMinArmCountOpenDoor()) {
						controller.alert(
								"开启恶魔之门失败",
								"你的士兵太少了，开启恶魔之门至少需要"
										+ StringUtil.color(
												""
														+ CalcUtil
																.turnToTenThousand(hpHolyProp
																		.getMinArmCountOpenDoor()),
												R.color.k7_color5)
										+ "名士兵", "", null, true);
						return;
					}

					new OpenEvilDoorConfirm(bfic).show();
			   }
			   else
			   {				
				new TroopMoveDetailWindow().open(Account.richFiefCache
						.getManorFief().brief(), bfic, null,
						com.vikings.sanguo.Constants.TROOP_OCCUPY);
			   }
//			}

		} else if (v == detailBtn) {
			new FiefDetailWindow().open(bfic);
		} else if (v == favoriteBtn) {
			if (Account.isFavoriteFief(bfic.getId())) {
				new DelFavorateFief(bfic.getId(), null).start();
			} else {
				new AddFavorateFief(bfic.getId()).start();
			}
		} else if (v == assistAttackBtn) {
			//占领圣都不用钥匙   开启才用
			boolean isGoShop = false;
			if(bfic.isHoly() && bfic.canOccupied())
			{
				if(BriefUserInfoClient.isNPC(bfic.getAttackerId()))
				{
					isGoShop = true;
				}
				else
				{
					isGoShop = false;
				}
			}
			else
			{
				isGoShop = true;
			}
			
			boolean isReinfoceAttack = reinforceData();
			if(rbic != null)
			{
				if(rbic.isReinforce(isReinfoceAttack))  //是否增援过
				{
					isGoShop = false;
				}
			}
			{
				//如果自己援助自己不需要消耗物品 
				if(isGoShop)
				{
						if(!bfic.isMyFief() && rbic!= null && !rbic.isAttacker() && !rbic.isDefender())
						{
							if (!FiefDetailTopInfo.SpecialConsumableIsEnough(hpHolyProp,
								Constants.ASSISTATTACK)) {
								new ToShopTip(hpHolyProp.getItemId()).show();
								return;
						}
					}
				}
			}
			// 需要加检测条件
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, null,
					com.vikings.sanguo.Constants.TROOP_REINFORCE);
		} else if (v == battleBtn) {
			controller.openWarInfoWindow(bfic);
		} else if (v == transferBtn) {
			new ResourceFiefTransfer().show(bfic);
		} else if (v == assistDefendBtn) {
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, null,
					com.vikings.sanguo.Constants.TROOP_REINFORCE);
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
				bfic.setDefenderHero();
			}

		}

		@Override
		protected void onOK() {
			showTip();
		}
	}	
}
