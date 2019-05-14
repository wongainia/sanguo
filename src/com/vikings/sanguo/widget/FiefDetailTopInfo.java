/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-9 下午2:07:11
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.widget;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.FiefProp;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.MoveTroopInfoClient;
import com.vikings.sanguo.model.OtherFiefInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.Plunder;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.model.SiteSpecial;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.protos.BUILDING_STATUS;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.window.GuildInfoWindow;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class FiefDetailTopInfo {

	public static void update(ViewGroup widget, final BriefFiefInfoClient bfic,
			final BriefGuildInfoClient bgic) { // String familyName
		// 图标
		IconUtil.setFiefIcon(widget, bfic);

		// 坐标
		ViewUtil.setText(widget, R.id.idx, StringUtil.getCoordinateDesc(bfic));

		// 领主
		TextView lord = (TextView) widget.findViewById(R.id.lord);
		final BriefUserInfoClient user = bfic.getLord();
		ViewUtil.setText(lord, user.getNickName()); // "领主:" +

		if (!CacheMgr.npcCache.contains(user.getId())) {
			lord.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			lord.setTextColor(Config.getController().getResources()
					.getColor(R.color.color16));
			ViewUtil.setBold(lord);
		}

		lord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!CacheMgr.npcCache.contains(user.getId()))
					Config.getController().showCastle(user);
			}
		});

		// 家族
		TextView guild = (TextView) widget.findViewById(R.id.guild);
		String familyName = null == bgic ? "无" : bgic.getName();
		ViewUtil.setText(guild, familyName); // "家族:" +

		if (null != bgic) {
			guild.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			guild.setTextColor(Config.getController().getResources()
					.getColor(R.color.color19));
			ViewUtil.setBold(guild);
		} else {
		}

		guild.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != bgic)
					new GuildInfoWindow().open(bgic.getId());
			}
		});

		// 城防
		// ViewUtil.setText(widget, R.id.defence,
		// "城防:" + bfic.getDefenceSkillName());

		ViewUtil.setText(widget, R.id.defence, bfic.getDefenceSkillName());

		// 兵力
		ViewUtil.setText(widget, R.id.troop, StringUtil.getTroopDesc(bfic));

		if (bfic.isHoly()) { // 圣都
			ViewUtil.setVisible(widget, R.id.detailLayout);
			ViewUtil.setGone(widget, R.id.productLayout);
		} else if (bfic.isResource()) { // 资源点
			ViewUtil.setGone(widget, R.id.detailLayout);
			ViewUtil.setVisible(widget, R.id.productLayout);
			setProduct(bfic,
					(ViewGroup) widget.findViewById(R.id.productLayout),
					R.drawable.setoff_cnt_sg, bfic.getSiteSpecial(), null);
		} else {
			ViewUtil.setGone(widget, R.id.detailLayout);
			ViewUtil.setGone(widget, R.id.productLayout);
		}

		ViewUtil.setGone(widget, R.id.topDesc);

		if (bfic.isHoly()) {
			try {
				HolyProp hpHolyProp = (HolyProp) CacheMgr.holyPropCache
						.get(bfic.getId());
				setState(bfic,
						(ViewGroup) widget.findViewById(R.id.detailLayout),
						hpHolyProp);
			} catch (GameException e) {
				e.printStackTrace();
			}

		}
	}

	public static void setTopDesc(ViewGroup widget, String info) {
		if (null == info)
			return;

		ViewUtil.setVisible(widget, R.id.topDesc);
		ViewUtil.setText(widget, R.id.topDesc, info);
	}

	public static void setProduct(BriefFiefInfoClient bfic, View view,
			int resId, SiteSpecial siteSpecial, OtherUserClient ouc) {
		ViewUtil.setImage(view, resId);
		FiefProp fiefProp = bfic.getProp();
		String nameIcon = "";
		if (fiefProp.getProductType() == 0) {
			// 为荒地 无产出的概念
		} else {
			nameIcon = "#"
					+ ReturnInfoClient.getAttrTypeIconName(fiefProp
							.getProductType()) + "#";
		}

		String nameStr = nameIcon + fiefProp.getName();
		ViewUtil.setRichText(view, R.id.name, nameStr, true);

		BuildingInfoClient bic = bfic.getBuilding();
		int productSpeed = 0;
		int product = 0;
		if (null != bic) {
			int statusId = bic.getResourceStatus();
			if (statusId > 0) {
				productSpeed = bic.producePerHour(BUILDING_STATUS
						.valueOf(statusId));
				product = bic.produce(BUILDING_STATUS.valueOf(statusId), bfic
						.getLord().getLastLoginTime());
			}
		}

		ViewUtil.setText(view, R.id.product, "每小时产能" + productSpeed + " (最大"
				+ fiefProp.getProduceSpeed() + ")");

		ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressBar);
		TextView progressDesc = (TextView) view.findViewById(R.id.progressDesc);

		if (null != bic) {
			ViewUtil.setVisible(view, R.id.progressLayout);
			if (bfic.isMyFief()) {
				ViewUtil.setVisible(view, R.id.progressBar);
				progressBar.set(product, bic.maxStore());
				String progressDescStr = "已产出" + product + "/" + bic.maxStore();
				if (product == bic.maxStore() && null != siteSpecial) {
					Item item = CacheMgr.getItemByID(siteSpecial.getItemId());
					if (null != item) {
						progressDescStr = "已产出"
								+ (product - (siteSpecial.getValue() * siteSpecial
										.getCount()))
								+ ReturnInfoClient.getAttrTypeName(fiefProp
										.getProductType()) + "+"
								+ siteSpecial.getCount() + "[" + item.getName()
								+ "]";
					}
				}
				ViewUtil.setRichText(progressDesc, progressDescStr);

			} else {

				int type = 0;

				if (bfic.getCountryId() == Account.user.getCountry().intValue()) {
					type = Plunder.RESOURCE_SAME_COUNTRY;
				} else {
					type = Plunder.RESOURCE_DIF_COUNTRY;
				}
				Plunder plunder = null;
				try {
					plunder = (Plunder) CacheMgr.plunderCache.get(type);
					product = (int) (product * plunder.getRate() / 100f);
					if (ouc != null && ouc.isVipBlessState()) {
						product = product * 2;
					}
				} catch (GameException e) {
					Log.e("TroopMoveTip", e.getMessage());
				}

				// 付斌需求，只有当掠夺比列为100%的时候，才会出金砖
				String progressDescStr = StringUtil.color("目前可掠夺：",
						R.color.k7_color15)
						+ nameIcon
						+ StringUtil.color("" + product, R.color.k7_color15);
				if (product == bic.maxStore() && null != siteSpecial) {
					Item item = CacheMgr.getItemByID(siteSpecial.getItemId());
					if (null != item) {
						progressDescStr = StringUtil.color("目前可掠夺：",
								R.color.k7_color15)
								+ nameIcon
								+ StringUtil
										.color((product - (siteSpecial
												.getValue() * siteSpecial
												.getCount()))
												+ "+"
												+ siteSpecial.getCount()
												+ "[" + item.getName() + "]",
												R.color.k7_color15);
					}
				}
				ViewUtil.setGone(view, R.id.progressBar);
				progressDesc.setShadowLayer(0, 0, 0, 0);
				ViewUtil.setRichText(progressDesc, progressDescStr, true);
			}

		} else {
			if (bfic.isMyFief()) {
				ViewUtil.setVisible(view, R.id.progressLayout);
				ViewUtil.setRichText(progressDesc,
						StringUtil.color("需要建造（资源建筑）", R.color.k7_color15));
			} else {
				ViewUtil.setGone(view, R.id.progressLayout);
			}
		}
	}

	public static void setState(BriefFiefInfoClient bfic, ViewGroup viewGroup,
			HolyProp hp) {
		ViewUtil.setRichText(viewGroup, R.id.reinforceDesc, hp.getAlertTitle());
	}

	public static int getBattleTime(int nextBattleTime) {
		int left = 0;
		left = nextBattleTime - (int) (Config.serverTime() / 1000);
		return left;
	}

	public static void setBaseFiefInfo(View view, BriefFiefInfoClient bfic,
			boolean hasHero) {
		ViewUtil.setText(
				view,
				R.id.position,
				"坐标：" + bfic.getNatureCountryName() + " ("
						+ TileUtil.uniqueMarking(bfic.getId()) + ")");

		BriefUserInfoClient briefUser = bfic.getLord();
		if (null == briefUser) {
			ViewUtil.setText(view, R.id.lordName, "领主：无");
		} else {
			StringBuilder buf = new StringBuilder("领主：").append(briefUser
					.getNickName());
			if (!briefUser.isNPC()
					&& !StringUtil.isNull(briefUser.getCountryName()))
				buf.append(" (").append(briefUser.getCountryName()).append(")");
			ViewUtil.setText(view, R.id.lordName, buf.toString());
		}

		// if (hasHero) {
		ViewUtil.setVisible(view, R.id.guardHero);
		HeroIdInfoClient hiics = bfic.getMainHero();
		ViewUtil.setRichText(
				view,
				R.id.guardHero,
				(null == hiics ? "无" : StringUtil.getHeroName(
						hiics.getHeroProp(), hiics.getHeroQuality())));
		// } else
		// ViewUtil.setGone(view, R.id.guardHero);

		ViewUtil.setText(view, R.id.armCount, bfic.getUnitCount());
	}

	// 判断消耗的特殊物品是否充足
	public static boolean SpecialConsumableIsEnough(HolyProp hp, int Type) {
		int expeditionItemId = hp.getItemId();
		if (expeditionItemId != 0) {
			final ItemBag bag = Account.store.getItemBag(expeditionItemId);
			if (Type == Constants.OCCUPY) {
				if (hp.getItemCost() <= 0) {
					{// 0 不需要消耗物品
						return true;
					}
				}
				return (null != bag && bag.getCount() >= hp.getItemCost());
			} else if (Type == Constants.ASSISTATTACK) {
				if (hp.getItemReinforceCost() <= 0) {// 0 不需要消耗物品
					return true;
				}
				return (null != bag && bag.getCount() >= hp
						.getItemReinforceCost());
			}
		}
		return false;
	}

	public static Item getItem(HolyProp hp, int Type) {
		Item item = null;
		int expeditionItemId = hp.getItemId();
		if (expeditionItemId != 0) {
			try {
				item = (Item) CacheMgr.itemCache.get(expeditionItemId);
			} catch (GameException e) {
				e.printStackTrace();
			}
			if (Type == Constants.OCCUPY) {
				if (hp.getItemCost() > 0 && item != null) {
					return item;
				}
			} else if (Type == Constants.ASSISTATTACK) {
				if (hp.getItemReinforceCost() > 0 && item != null) {
					return item;
				}
			}
		}

		return null;
	}

	// 通过军队 获得参战人数
	public static int getBattleTotal(RichBattleInfoClient rbic) {
		List<Integer> ids = new ArrayList<Integer>();
		if (null != rbic.getAttackTroopInfos()) {
			for (MoveTroopInfoClient info : rbic.getAttackTroopInfos()) {
				if (!BriefUserInfoClient.isNPC(info.getUserid())
						&& !ids.contains(info.getUserid()))
					ids.add(info.getUserid());
			}
		}
		return ids.size();
	}

	public static void setFamousBaseFiefInfo(View view,
			BriefFiefInfoClient bfic, RichBattleInfoClient rbic,
			OtherFiefInfoClient ofic, HolyProp holyProp) {
		ViewUtil.setText(
				view,
				R.id.position,
				"坐标：" + bfic.getNatureCountryName() + " ("
						+ TileUtil.uniqueMarking(bfic.getId()) + ")");
		ViewUtil.setText(view, R.id.armCount, bfic.getUnitCount());
		if (rbic != null && null != holyProp) {
			ViewUtil.setText(
					view,
					R.id.battleNumber,
					"参战人数：" + getBattleTotal(rbic) + "人( 最多"
							+ holyProp.getMaxReinforceUser() + "人)");
		}
		if (ofic != null) {
			int time = getBattleTime(ofic.getNextExtraBattleTime());
			if (holyProp != null && holyProp.canOccupied() && bfic != null
					&& !bfic.isMyFief() && bfic.isHoly()) // 可被占领
			{
				ViewUtil.setRichText(view, R.id.status,
						StringUtil.color("目前可征讨！", R.color.color19));
			} else {
				if (time > 0) {
					ViewUtil.setRichText(view, R.id.status,
							StringUtil.color(
									DateUtil.formatTime(time) + "后可征讨",
									R.color.color24));
				} else {
					ViewUtil.setRichText(view, R.id.status,
							StringUtil.color("目前可征讨！", R.color.color19));
				}
			}
		}

		BriefUserInfoClient briefUser = bfic.getLord();
		if (null == briefUser) {
			ViewUtil.setText(view, R.id.lordName, "领主：无");
		} else {
			StringBuilder buf = new StringBuilder("领主：").append(briefUser
					.getNickName());
			if (!briefUser.isNPC()
					&& !StringUtil.isNull(briefUser.getCountryName())) {
				buf.append(" (").append(briefUser.getCountryName()).append(")");
			}

			ViewUtil.setText(view, R.id.lordName, buf.toString());
		}

	}

	public static void setGuildInfo(View view, final BriefGuildInfoClient gic,
			final CallBack cb) {
		TextView guildName = (TextView) view.findViewById(R.id.guildName);
		ViewUtil.setText(guildName, null == gic ? "无" : gic.getName());
		if (null != gic) {
			ViewUtil.setUnderLine(guildName);
			guildName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != cb)
						cb.onCall();
					new GuildInfoWindow().open(gic.getId());
				}
			});
		}
	}

	public static void setResources(View view, AttrData attrData,
			BriefFiefInfoClient bfic) {
		ViewUtil.setRichText(view, R.id.money,
				bfic.getAttrDesc(attrData, AttrType.ATTR_TYPE_MONEY), true);

		ViewUtil.setRichText(view, R.id.food,
				bfic.getAttrDesc(attrData, AttrType.ATTR_TYPE_FOOD), true);

		ViewUtil.setRichText(view, R.id.material0,
				bfic.getAttrDesc(attrData, AttrType.ATTR_TYPE_MATERIAL_0), true);

		ViewUtil.setRichText(view, R.id.material1,
				bfic.getAttrDesc(attrData, AttrType.ATTR_TYPE_MATERIAL_1), true);

		ViewUtil.setRichText(view, R.id.material2,
				bfic.getAttrDesc(attrData, AttrType.ATTR_TYPE_WOOD), true);
	}
}
