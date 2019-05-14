package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.TroopMoveInvoker;
import com.vikings.sanguo.invoker.TroopReinforceInvoker;
import com.vikings.sanguo.invoker.TroopRiseInvoker;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BattleFief;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.FiefProp;
import com.vikings.sanguo.model.FiefScale;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.Plunder;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShopData;
import com.vikings.sanguo.model.SiteSpecial;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.protos.BattleAttackType;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.ui.window.ShopWindow;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

/**
 * 军队移动统一提示框 移动 出征 增援
 * 
 * @author chenqing
 * 
 */
public class TroopMoveTip extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_troop_help;

	private int type;

	private BriefFiefInfoClient myBfc, lordBfc;

	private List<ArmInfoClient> armInfoList;

	private List<HeroIdInfoClient> heros;

	private OtherUserClient ouc;

	public TroopMoveTip(int type, BriefFiefInfoClient myBfc,
			BriefFiefInfoClient lordBfc, OtherUserClient ouc,
			List<ArmInfoClient> armInfoList, List<HeroIdInfoClient> heros) {
		super(CustomConfirmDialog.DEFAULT);
		this.type = type;
		this.myBfc = myBfc;
		this.lordBfc = lordBfc;
		this.ouc = ouc;
		this.armInfoList = armInfoList;
		this.heros = heros;
		setValue();
	}

	private void setValue() {

		ViewUtil.setVisible(content.findViewById(R.id.costDesc));

		FiefScale fs = lordBfc.getFiefScale();

		IconUtil.setFiefIcon(content, lordBfc);

		new AddrLoader(content.findViewById(R.id.addrDesc),
				TileUtil.fiefId2TileId(lordBfc.getId()), AddrLoader.TYPE_SUB);
		ViewUtil.setBold((TextView) content.findViewById(R.id.addrDesc));
		ViewUtil.setText(content.findViewById(R.id.defence),
				"城防:" + lordBfc.getDefenceSkillName());

		if (Constants.TROOP_DISPATCH == type)
			ViewUtil.setGone(content, R.id.troopInfo);
		else {
			ViewUtil.setBoldText(content.findViewById(R.id.myName),
					Account.user.getNick());
			ViewUtil.setBoldText(content.findViewById(R.id.otherName),
					0 == lordBfc.getLord().getId() ? "黑暗精灵" : lordBfc.getLord()
							.getNickName());

			HeroIdInfoClient heroSelf = getMainHeroInfo(heros);
			if (heroSelf != null && heroSelf.getId() > 0)
				ViewUtil.setBoldRichText(content.findViewById(R.id.myHero),
						"将领:" + heroSelf.getHeroFullName());
			else
				ViewUtil.setBoldText(content.findViewById(R.id.myHero), "将领:无");

			List<HeroIdInfoClient> otherHeros = lordBfc.getHeroIdInfos();
			HeroIdInfoClient otherHero = getMainHeroInfo(otherHeros);
			if (null != otherHero && otherHero.getId() > 0)
				// 设置将领
				ViewUtil.setBoldRichText(content.findViewById(R.id.otherHero),
						"将领:" + otherHero.getHeroFullName());
			else
				ViewUtil.setBoldText(content.findViewById(R.id.otherHero),
						"将领:无");

			ViewUtil.setBoldText(content.findViewById(R.id.myArmCnt), "兵力:"
					+ TroopUtil.countArm(armInfoList));
			ViewUtil.setBoldText(content.findViewById(R.id.otherArmCnt), "兵力:"
					+ lordBfc.getUnitCount());

		}

		long scrFiefid = myBfc.getId();

		String firstBtn = "";
		String secondBtn = "";
		String lastBtn = "";
		int cost = TroopUtil.costFoodMove(scrFiefid, lordBfc.getId(),
				armInfoList, heros);

		int costFoodTotal = TroopUtil.costFood(scrFiefid, lordBfc.getId(),
				armInfoList, heros, lordBfc.getFiefScale());
		int holyCost = 0;
		switch (type) {
		case Constants.TROOP_DISPATCH:
			setTitle("请确定是否调兵?");
			firstBtn = "普通调遣";
			secondBtn = "元宝调遣#rmb#" + TroopUtil.costRmbMove(cost);
			lastBtn = "取消";
			new AddrLoader(content.findViewById(R.id.costDesc),
					TileUtil.fiefId2TileId(lordBfc.getId()), "领主大人,调兵前往",
					",需要消耗#fief_food#"
							+ StringUtil.color(cost + "份", R.color.k7_color8)
							+ "粮草,元宝调遣无需消耗粮草", AddrLoader.TYPE_SUB);
			break;
		case Constants.TROOP_REINFORCE:
			setTitle("请确定是否援助?");
			firstBtn = "普通增援";
			secondBtn = "元宝增援#rmb#" + TroopUtil.costRmbMove(cost);
			lastBtn = "取消增援";
			int state = TroopUtil.getCurBattleState(lordBfc.getBattleState(),
					lordBfc.getBattleTime());

			StringBuilder buf = new StringBuilder();
			if (state == BattleStatus.BATTLE_STATE_SURROUND) {
				int time = TroopUtil.get2NextBattleStateTime(
						lordBfc.getBattleState(), lordBfc.getBattleTime(), fs);
				buf.append(DateUtil.formatMinute(time) + "后处于"
						+ BattleStatus.getStatusName(state + 1) + "状态");
			} else if (state == BattleStatus.BATTLE_STATE_SURROUND_END) {
				buf.append("当前处于" + BattleStatus.getStatusName(state) + "状态");
			} else {
				buf.append("当前已经是" + BattleStatus.getStatusName(state)
						+ ",不能援助");
			}

			new AddrLoader(content.findViewById(R.id.costDesc),
					TileUtil.fiefId2TileId(lordBfc.getId()), buf.toString()
							+ "领主大人,援助", ",需要消耗粮草#fief_food#" + cost
							+ "份,元宝增援无需消耗粮草", AddrLoader.TYPE_SUB);
			FiefScale fiefScale = lordBfc.getFiefScale();
			HolyProp holyProp = null;
			if (null != lordBfc) {
				try {
					holyProp = (HolyProp) CacheMgr.holyPropCache.get(lordBfc
							.getId());
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
			if (lordBfc.isHoly() && lordBfc.isInBattle()
					&& BriefUserInfoClient.isNPC(lordBfc.getAttackerId())
					&& lordBfc.getUserId() != Account.user.getId()
					&& null != holyProp && holyProp.canOccupied()) {
				if (null != fiefScale) {
					// HolyProp holyProp = (HolyProp) fiefScale;
					int itemId = holyProp.getItemId();
					int itemCount = holyProp.getItemReinforceCost();
					if (itemId > 0 && itemCount > 0) {
						Item reinforceItem = CacheMgr.getItemByID(itemId);
						ItemBag itembag = Account.store.getItemBag(itemId);
						if (reinforceItem != null) {
							holyCost = itemCount;
							ViewUtil.setVisible(content, R.id.extDesc);
							String title = "";
							if (holyProp.getProp() != null) {
								title = holyProp.getProp().getForeignTitle();
							}
							ViewUtil.setRichText(
									content.findViewById(R.id.extDesc),
									"援助此处"
											+ title/* 恶魔之门的 */
											+ "战争需要消耗"
											+ "<br/>#"
											+ reinforceItem.getImage()
											+ "#"
											+ reinforceItem.getName()
											+ "x"
											+ itemCount
											+ "("
											+ (itembag == null ? 0 : itembag
													.getCount())
											+ ")<br/>"

											+ StringUtil.color("(每场战斗只扣除一次)",
													R.color.k7_color8), true,
									30, 30);
						}
					}
				}
			}

			break;
		case Constants.TROOP_PLUNDER:
			cost = costFoodTotal;
			setTitle("请确定是否掠夺?");
			firstBtn = "普通掠夺";
			secondBtn = "元宝掠夺#rmb#" + TroopUtil.costRmbMove(cost);
			lastBtn = "取消掠夺";
			new AddrLoader(content.findViewById(R.id.costDesc),
					TileUtil.fiefId2TileId(lordBfc.getId()), "本次出征",
					"需消耗#fief_food#"
							+ StringUtil.color(cost + "份", R.color.k7_color8)
							+ "粮草,元宝征讨不消耗粮草", AddrLoader.TYPE_SUB);
			ViewUtil.setVisible(content, R.id.extDesc);
			ViewUtil.setVisible(content, R.id.midExtDesc);
			ViewUtil.setRichText(content, R.id.midExtDesc, getPlunderDesc());
			ViewUtil.setRichText(content, R.id.extDesc, "<br/><br/>"
					+ getReinforceUserCountDesc(Constants.TROOP_PLUNDER));
			break;
		case Constants.TROOP_OCCUPY:
			setTitle("请确定是否出征?");
			cost = costFoodTotal;
			lastBtn = "取消出征";
			if (lordBfc.isResource()
					&& !BriefUserInfoClient.isNPC(lordBfc.getUserId())) { // 非NPC的资源点
				int occupyCost = occupyCost();
				secondBtn = "出征占领#rmb#" + occupyCost;
				new AddrLoader(content.findViewById(R.id.costDesc),
						TileUtil.fiefId2TileId(lordBfc.getId()), "领主大人,占领",
						StringUtil.color(occupyFiefDesc(), R.color.k7_color7)
								+ "需花费#rmb#"
								+ StringUtil.color(occupyCost + "元宝",
										R.color.k7_color8)
								+ (StringUtil.isNull(getSurroundTime()) ? ""
										: ",围城时间"
												+ StringUtil.color(
														getSurroundTime(),
														R.color.k7_color8)),
						AddrLoader.TYPE_SUB);
			} else {
				if (lordBfc.isHoly()
						&& BriefUserInfoClient.isNPC(lordBfc.getUserId())
						&& !lordBfc.canOccupied()) {
					setTitle("请确定是否征讨?");
					lastBtn = "取消征讨";
					firstBtn = "普通征讨";
					secondBtn = "元宝征讨#rmb#" + TroopUtil.costRmbMove(cost);
					new AddrLoader(
							content.findViewById(R.id.costDesc),
							TileUtil.fiefId2TileId(lordBfc.getId()),
							"本次出征",
							(StringUtil.isNull(getSurroundTime()) ? "" : "围城"
									+ StringUtil.color(getSurroundTime(),
											R.color.k7_color8) + ",")
									+ "需消耗#fief_food#"
									+ StringUtil.color(cost + "份",
											R.color.k7_color8) + "粮草,元宝征讨不消耗粮草",
							AddrLoader.TYPE_SUB);
				} else {
					firstBtn = "普通出征";
					secondBtn = "元宝出征#rmb#" + TroopUtil.costRmbMove(cost);
					new AddrLoader(
							content.findViewById(R.id.costDesc),
							TileUtil.fiefId2TileId(lordBfc.getId()),
							"本次出征",

							(StringUtil.isNull(getSurroundTime()) ? "" : "围城"
									+ StringUtil.color(getSurroundTime(),
											R.color.k7_color8) + ",")
									+ "需消耗#fief_food#"
									+ StringUtil.color(cost + "份",
											R.color.k7_color8) + "粮草,元宝征讨不消耗粮草",
							AddrLoader.TYPE_SUB);
				}

			}

			ViewUtil.setVisible(content, R.id.extDesc);
			ViewUtil.setRichText(content, R.id.extDesc,
					getReinforceUserCountDesc(Constants.TROOP_OCCUPY));
			break;
		case Constants.TROOP_DUEL:
			setTitle("请确定是否单挑?");
			int duelItemId = CacheMgr.dictCache.getDictInt(
					Dict.TYPE_BATTLE_DUEL, 1);
			int duelItemCount = CacheMgr.dictCache.getDictInt(
					Dict.TYPE_BATTLE_DUEL, 2);
			int surroundTime = CacheMgr.dictCache.getDictInt(
					Dict.TYPE_BATTLE_DUEL, 3);
			Item duelItem = CacheMgr.getItemByID(duelItemId);
			String duelItemName = (duelItem != null ? duelItem.getName()
					: "单挑令");
			firstBtn = "单挑#duel_icon#" + duelItemCount;
			lastBtn = "取消";
			ViewUtil.setRichText(
					content.findViewById(R.id.costDesc),
					"领主大人,"
							+ (duelItemCount > 0 ? "消耗"
									+ StringUtil.color("" + duelItemCount,
											R.color.k7_color8)
									+ "个"
									+ StringUtil.color(
											"[" + duelItemName + "]",
											R.color.k7_color8) + "可在对方主城发动单挑！"
									: "")
							+ (surroundTime == 0 ? ""
									: "本次出战需围城"
											+ StringUtil.color(
													DateUtil.formatSecond(surroundTime),
													R.color.k7_color8)));
			ViewUtil.setVisible(content, R.id.midExtDesc);
			ViewUtil.setRichText(content, R.id.midExtDesc,
					CacheMgr.uiTextCache.getTxt(501));
			break;
		case Constants.TROOP_MASSACRE:
			setTitle("请确定是否屠城?");
			int massacreItemId = CacheMgr.dictCache.getDictInt(
					Dict.TYPE_BATTLE_MASSACRE, 1);
			int massacreItemCount = CacheMgr.dictCache.getDictInt(
					Dict.TYPE_BATTLE_MASSACRE, 2);
			Item massacreItem = CacheMgr.getItemByID(massacreItemId);
			String massacreItemName = (massacreItem != null ? massacreItem
					.getName() : "屠城令");
			firstBtn = "屠城#massacre_icon#" + massacreItemCount;
			lastBtn = "取消";
			ViewUtil.setRichText(
					content.findViewById(R.id.costDesc),
					"领主大人,"
							+ (massacreItemCount > 0 ? "消耗"
									+ StringUtil.color("" + massacreItemCount,
											R.color.k7_color8)
									+ "个"
									+ StringUtil.color("[" + massacreItemName
											+ "]", R.color.k7_color8)
									+ "可在对方主城发动屠城！" : "")
							+ (StringUtil.isNull(getSurroundTime()) ? ""
									: "本次出战需围城"
											+ StringUtil.color(
													getSurroundTime(),
													R.color.k7_color8)));

			ViewUtil.setVisible(content, R.id.extDesc);
			ViewUtil.setRichText(content, R.id.extDesc,
					CacheMgr.uiTextCache.getTxt(502));
			break;
		default:
			break;
		}

		final int holyCostItemCount = holyCost;
		if (!StringUtil.isNull(firstBtn)) {
			if (type == Constants.TROOP_DUEL
					|| type == Constants.TROOP_MASSACRE) {
				int dictId = 0;
				boolean buy = false;
				if (type == Constants.TROOP_DUEL) {
					dictId = Dict.TYPE_BATTLE_DUEL;
					buy = true;
				} else if (type == Constants.TROOP_MASSACRE) {
					dictId = Dict.TYPE_BATTLE_MASSACRE;
					buy = false;
				}

				final boolean canBuy = buy;

				int itemId = CacheMgr.dictCache.getDictInt(dictId, 1);
				final int itemCount = CacheMgr.dictCache.getDictInt(dictId, 2);
				final Item item = CacheMgr.getItemByID(itemId);
				final ItemBag bag = Account.store.getItemBag(itemId);

				setButton(CustomConfirmDialog.FIRST_BTN, firstBtn,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								boolean enough = true;
								if (null != item && itemCount > 0) {
									int selfCount = 0;
									if (null != bag && bag.getCount() > 0)
										selfCount = bag.getCount();
									enough = (selfCount >= itemCount);
									if (!enough) {
										dismiss();
										if (!canBuy) {
											controller.alert("没有足够的 "
													+ item.getName());
										} else {
											new AttackItemNotEnough("道具不足")
													.show(item, itemCount,
															selfCount);
										}
										return;
									}
								}
								new Call(Constants.TYPE_NOAMRL, type, enough,
										itemCount, 0).sendInvoker();
								dismiss();
							}
						});
			} else {

				final int realCostFood = cost;

				setButton(CustomConfirmDialog.SECOND_BTN, firstBtn,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (Account.user.getFood() < realCostFood) {
									Config.getController()
											.alert("你的<font color='#DC540A'> 粮草 </font>不足，请先积累资源");
									return;
								}
								boolean enough = Account.user.getFood() >= realCostFood;
								new Call(Constants.TYPE_NOAMRL, type, enough,
										realCostFood, holyCostItemCount)
										.sendInvoker();
								dismiss();
							}
						});
			}

		}

		if (!StringUtil.isNull(secondBtn)) {

			final int realCostRMB = ((type == Constants.TROOP_MASSACRE) ? cost
					: TroopUtil.costRmbMove(cost));

			setButton(CustomConfirmDialog.FIRST_BTN, secondBtn,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							boolean enough = Account.user.getCurrency() >= realCostRMB;
							new Call(Constants.TYPE_RMB, type, enough,
									realCostRMB, holyCostItemCount)
									.sendInvoker();
							dismiss();
						}
					});
		}

		if (!StringUtil.isNull(lastBtn))
			setButton(CustomConfirmDialog.THIRD_BTN, lastBtn, closeL);
	}

	private HeroIdInfoClient getMainHeroInfo(List<HeroIdInfoClient> heros) {
		if (null != heros && !heros.isEmpty()) {
			for (HeroIdInfoClient hiic : heros) {
				if (hiic.isMainHero())
					return hiic;
			}
		}
		return null;
	}

	private int occupyCost() {
		int cost = 0;
		if (null != lordBfc.getProp()) {
			cost += lordBfc.getProp().getOccupyCost();
		}
		if (null != lordBfc.getBuilding()
				&& null != lordBfc.getBuilding().getProp()) {
			cost += lordBfc.getBuilding().getProp().getOccupyCost();
		}
		return cost;
	}

	private String occupyFiefDesc() {
		String str = " (";
		if (null != lordBfc.getProp()) {
			str += lordBfc.getProp().getName();
		} else {
			str += "未知";
		}
		if (null != lordBfc.getBuilding()
				&& null != lordBfc.getBuilding().getProp()) {
			str += "," + lordBfc.getBuilding().getProp().getBuildingName();
		}
		str += ") ";
		return str;
	}

	private String getPlunderDesc() {
		StringBuffer buf = new StringBuffer();
		int troopWeight = 0;
		if (lordBfc.isCastle()) {
			int type = 0;

			if (lordBfc.getCountryId() == Account.user.getCountry().intValue()) {
				type = Plunder.MANOR_SAME_COUNTRY;
			} else {
				type = Plunder.MANOR_DIF_COUNTRY;
			}

			int plunderRate = 100;
			try {
				Plunder plunder = (Plunder) CacheMgr.plunderCache.get(type);
				if (null != plunder)
					plunderRate = plunder.getRate();
			} catch (GameException e) {
				Log.e("TroopMoveTip", e.getMessage());
			}

			// 保护的资源重量
			int protectedWeight = lordBfc.getManor()
					.getProtectedResourceWeight();

			troopWeight = getTroopWeight();
			int resourceWeight = getResourceWeight(ouc, protectedWeight,
					plunderRate);

			int rate = 1;
			if (resourceWeight <= 0)
				rate = 100;
			else
				rate = (int) (troopWeight * 100f / resourceWeight);

			rate = Math.min(rate, 100);

			rate = Math.max(rate, 1);

			StringBuffer resourceBuffer = new StringBuffer();
			for (int i = 0; i < ouc.attrTypeArray.length; i++) {
				AttrType attrType = ouc.attrTypeArray[i];
				int maxPlunderCount = (int) (getAttrCount(ouc, attrType,
						protectedWeight) * (plunderRate / 100f));
				if (maxPlunderCount > 0) {
					resourceBuffer.append("["
							+ ReturnInfoClient.getAttrTypeName(attrType
									.getNumber()) + "]x" + maxPlunderCount
							+ "、");
				}
			}

			int index = resourceBuffer.lastIndexOf("、");
			if (index >= 0)
				resourceBuffer.deleteCharAt(index);

			if (resourceBuffer.length() > 0) {
				buf.append("对方主城有如下资源:")
						.append(StringUtil.color(resourceBuffer.toString(),
								R.color.k7_color15))
						.append(";获胜后你最多能掠走")
						.append(StringUtil
								.color(rate + "%", R.color.k7_color15))
						.append(";派遣更多士兵或带[木牛]、[流马]，能抢更多资源！");
			} else {
				buf.append(StringUtil.color("对方主城已没有任何资源可掠夺!",
						R.color.k7_color15));
			}

		} else if (lordBfc.isResource()) {

			int attrType = lordBfc.getProp().getProductType();

			int weight = CacheMgr.weightCache.getWeight(attrType);
			if (weight == 0)
				weight = 1;

			int type = 0;
			if (lordBfc.getCountryId() == Account.user.getCountry().intValue()) {
				type = Plunder.RESOURCE_SAME_COUNTRY;
			} else {
				type = Plunder.RESOURCE_DIF_COUNTRY;
			}

			Plunder plunder = null;
			try {
				plunder = (Plunder) CacheMgr.plunderCache.get(type);
			} catch (GameException e) {
				Log.e("TroopMoveTip", e.getMessage());
			}

			int resourceWeight = 0;
			int produce = 0;
			if (null != lordBfc.getBuilding()) {
				produce = lordBfc.getBuilding().produce(
						lordBfc.getLord().getLastLoginTime());
				if (null != plunder) {
					resourceWeight = produce * weight * plunder.getRate() / 100;
				} else {
					resourceWeight = produce * weight;
				}

			}

			troopWeight = getTroopWeight();
			int rate = 1;

			if (resourceWeight <= 0)
				rate = 100;
			else
				rate = (int) (troopWeight * 100f / resourceWeight);

			rate = Math.min(rate, 100);

			rate = Math.max(rate, 1);

			SiteSpecial siteSpecial = lordBfc.getSiteSpecial();

			StringBuffer extBuf = new StringBuffer();
			buf.append("对方资源点有");
			if (lordBfc.isResourceFull()
					&& ((null != plunder && plunder.getRate() == 100) || null == plunder)
					&& null != siteSpecial) {
				Item item = CacheMgr.getItemByID(siteSpecial.getItemId());
				if (null != item) {
					buf.append(StringUtil.color(
							"["
									+ ReturnInfoClient
											.getAttrTypeName(attrType)
									+ "]x"
									+ (produce - siteSpecial.getCount()
											* siteSpecial.getValue()) + "和"
									+ siteSpecial.getCount() + "个["
									+ item.getName() + "]", R.color.k7_color15));
					if (rate == 100) {
						extBuf.append("和").append(
								StringUtil.color(siteSpecial.getCount() + "个["
										+ item.getName() + "]",
										R.color.k7_color15));
					}
				} else {
					buf.append(StringUtil.color(
							"["
									+ ReturnInfoClient
											.getAttrTypeName(attrType)
									+ "]x"
									+ (null != plunder ? (produce
											* plunder.getRate() / 100)
											: produce), R.color.k7_color15));
				}

			} else {
				buf.append(StringUtil.color(
						"["
								+ ReturnInfoClient.getAttrTypeName(attrType)
								+ "]x"
								+ (null != plunder ? (produce
										* plunder.getRate() / 100) : produce),
						R.color.k7_color15));
			}
			buf.append(";获胜后你最多能掠走")
					.append(StringUtil.color(rate + "%", R.color.k7_color15))
					.append("的")
					.append(ReturnInfoClient.getAttrTypeName(attrType))
					.append(";派遣更多的士兵或带[木牛]、[流马],能抢更多资源！");
		}
		return buf.toString();
	}

	private String getReinforceUserCountDesc(int type) {
		StringBuffer descBuf = new StringBuffer();
		if (null != lordBfc) {
			int propId = 0;
			if (lordBfc.isHoly()) {
				FiefScale prop = lordBfc.getFiefScale();
				if (prop instanceof HolyProp) {
					HolyProp holyProp = (HolyProp) prop;
					propId = holyProp.getPropId();
				}
			} else {
				FiefProp prop = lordBfc.getProp();
				if (null != prop)
					propId = prop.getId();
			}
			if (propId > 0) {
				try {
					BattleFief battleFief = (BattleFief) CacheMgr.battleFiefCache
							.get(propId);
					int count = 0;
					descBuf.append("本场战斗为");
					if (type == BattleAttackType.E_BATTLE_PLUNDER_ATTACK
							.getNumber()) {
						descBuf.append(StringUtil
								.color("掠夺", R.color.k7_color8));
						count = battleFief.getMaxPlunderUser();
					} else if (type == BattleAttackType.E_BATTLE_COMMON_ATTACK
							.getNumber()) {
						descBuf.append(StringUtil
								.color("占领", R.color.k7_color8));
						count = battleFief.getMaxOccupyUser();
					} else if (type == BattleAttackType.E_BATTLE_DUEL_ATTACK
							.getNumber()) {
						descBuf.append(StringUtil
								.color("单挑", R.color.k7_color8));
						count = battleFief.getMaxOccupyUser();
					} else if (type == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
							.getNumber()) {
						descBuf.append(StringUtil
								.color("屠城", R.color.k7_color8));
						count = battleFief.getMaxOccupyUser();
					} else {
						descBuf.append(StringUtil
								.color("攻打", R.color.k7_color8));
					}
					descBuf.append(StringUtil.color(lordBfc.getSimpleName(),
							R.color.k7_color8));
					if (count > 0) {
						descBuf.append("，双方各允许 "
								+ StringUtil.color("" + count,
										R.color.k7_color8) + "名玩家增援");
					}
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}
		return descBuf.toString();
	}

	private int getResourceWeight(OtherUserClient ouc, int protectedWeight,
			int countryRate) {
		int weight = 0;
		if (null != ouc) {
			for (int i = 0; i < ouc.attrTypeArray.length; i++) {
				weight += (int) (getAttrWeight(ouc, ouc.attrTypeArray[i],
						protectedWeight) * (countryRate / 100f));
			}
		}
		return weight;
	}

	private int getAttrWeight(AttrData attrData, AttrType attrType,
			int protectedWeight) {
		int count = attrData.getAttr(attrType);
		int weight = CacheMgr.weightCache.getWeight(attrType.getNumber());
		if (weight == 0)
			weight = 1;
		int resourceWeight = count * weight;
		resourceWeight = resourceWeight - protectedWeight;
		if (resourceWeight < 0)
			resourceWeight = 0;
		return resourceWeight;
	}

	private int getAttrCount(AttrData attrData, AttrType attrType,
			int protectedWeight) {
		int count = attrData.getAttr(attrType);
		int weight = CacheMgr.weightCache.getWeight(attrType.getNumber());
		if (weight == 0)
			weight = 1;
		int resourceWeight = count * weight;
		resourceWeight = resourceWeight - protectedWeight;
		if (resourceWeight < 0)
			resourceWeight = 0;
		return (int) (resourceWeight / weight);
	}

	private int getTroopWeight() {
		double totalWeight = 0;
		double temp = 1000;
		if (null != armInfoList) {
			for (ArmInfoClient info : armInfoList) {
				totalWeight += info.getProp().getCapacity() * info.getCount()
						/ temp;
			}
		}
		return (int) totalWeight;
	}

	private String getSurroundTime() {
		// PVE围城时间
		if (BriefUserInfoClient.isNPC(lordBfc.getUserId())
				&& CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 38) == 0) {
			return "";
		} else {
			FiefScale fs = lordBfc.getFiefScale();
			return DateUtil.formatSecond(fs.getLockTime());
		}

	}

	class Call {

		private int costType;

		private int opType;

		private boolean isEnough = false;

		private int amount;
		private int holyCost;

		public Call(int costType, int opType, boolean isEnough, int amount,
				int holyCost) {
			this.costType = costType;
			this.opType = opType;
			this.isEnough = isEnough;
			this.amount = amount;
			this.holyCost = holyCost;
		}

		public void sendInvoker() {
			if (!isEnough && costType == Constants.TYPE_RMB) {
				new ToActionTip(amount).show();
				dismiss();
				return;
			}
			SoundMgr.play(R.raw.sfx_assault);
			switch (opType) {
			case Constants.TROOP_DISPATCH:
				new TroopMoveInvoker(costType, myBfc, lordBfc, armInfoList,
						heros).start();
				break;
			case Constants.TROOP_REINFORCE:
				if (lordBfc.isMyFief()) {
					new TroopMoveInvoker(costType, myBfc, lordBfc, armInfoList,
							heros).start();
				} else {
					new TroopReinforceInvoker(costType, myBfc, lordBfc,
							armInfoList, heros, holyCost).start();
				}
				break;
			case Constants.TROOP_PLUNDER:
				new TroopRiseInvoker(costType,
						BattleAttackType.E_BATTLE_PLUNDER_ATTACK.getNumber(),
						myBfc, lordBfc, armInfoList, heros, 0).start();
				break;
			case Constants.TROOP_OCCUPY:
				if (lordBfc.isResource()
						&& !BriefUserInfoClient.isNPC(lordBfc.getUserId())) { // 非NPC的资源点
					new TroopRiseInvoker(
							costType,
							BattleAttackType.E_BATTLE_COMMON_ATTACK.getNumber(),
							myBfc, lordBfc, armInfoList, heros, occupyCost())
							.start();
				} else {
					new TroopRiseInvoker(
							costType,
							BattleAttackType.E_BATTLE_COMMON_ATTACK.getNumber(),
							myBfc, lordBfc, armInfoList, heros, 0).start();
				}
				break;
			case Constants.TROOP_MASSACRE: // 屠城
				new TroopRiseInvoker(costType,
						BattleAttackType.E_BATTLE_MASSACRE_ATTACK.getNumber(),
						myBfc, lordBfc, armInfoList, heros, amount).start();
				break;
			case Constants.TROOP_DUEL:// 单挑
				new TroopRiseInvoker(costType,
						BattleAttackType.E_BATTLE_DUEL_ATTACK.getNumber(),
						myBfc, lordBfc, armInfoList, heros, amount).start();
				break;
			default:
				break;
			}
		}

	}

	private class AttackItemNotEnough extends ItemNotEnoughTip {

		public AttackItemNotEnough(String title) {
			super(title);
		}

		@Override
		protected void setDesc() {
			ViewUtil.setText(desc1, item.getName() + "数量不足");
			ViewUtil.setGone(desc2);
		}

		@Override
		protected OnClickListener getClickListener() {
			return new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					controller.openShop(ShopWindow.TAB2, ShopData.TYPE_TOOL,
							item.getId());
				}
			};
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
