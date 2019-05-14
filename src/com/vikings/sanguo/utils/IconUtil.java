package com.vikings.sanguo.utils;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.EquipmentDesc;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroIdBaseInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroQuality;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.TroopDetailTip;

public class IconUtil {
	// 将领头像，正常缩放比例
	public static final float HERO_ICON_SCALE_NORMAL = 1f;

	// 将领头像，中等缩放比例
	public static final float HERO_ICON_SCALE_MED = 0.6f;

	// 将领头像，最小缩放比例
	public static final float HERO_ICON_SCALE_SMALL = 0.4f;
	public static byte RANK_TYPE = 1;

	private static int[] starViewIds = { R.id.star1, R.id.star2, R.id.star3,
			R.id.star4, R.id.star5, R.id.star6 };

	public static void setHeroIcon(View view, HeroIdBaseInfoClient hiic) {
		if (null == hiic || null == hiic.getHeroProp()
				|| null == hiic.getHeroQuality()) {
			ViewUtil.setGone(view, R.id.icon);
			ViewUtil.setGone(view, R.id.talent);
		} else {
			setHeroIcon(view, hiic.getHeroProp(), hiic.getHeroQuality(),
					hiic.getStar(), false);
		}
	}

	public static void setHeroIconScale(View view, HeroIdBaseInfoClient hiic) {
		if (null == hiic || null == hiic.getHeroProp()
				|| null == hiic.getHeroQuality()) {
			ViewUtil.setGone(view, R.id.icon);
			ViewUtil.setGone(view, R.id.talent);
		} else {
			setHeroIcon(view, hiic.getHeroProp(), hiic.getHeroQuality(),
					hiic.getStar(), true);
		}
	}

	public static void setEmptyHeroIcon(View view) {
		ViewUtil.setGone(view, R.id.starLayout);
		ViewUtil.setGone(view, R.id.talent);
		ViewUtil.setGone(view, R.id.rating_flop);
		view.findViewById(R.id.hero_item).setBackgroundDrawable(
				Config.getController().getDrawable("flop_un_bg"));

	}

	public static void setHeroIcon(View view, HeroProp heroProp,
			HeroQuality heroQuality, int starCnt, boolean scale) {
		if (scale) {
			setHeroIcon(view, heroProp, heroQuality, starCnt,
					HERO_ICON_SCALE_MED);
		} else {
			setHeroIcon(view, heroProp, heroQuality, starCnt,
					HERO_ICON_SCALE_NORMAL);
		}
	}

	//
	public static void setRecruitHeroIcon(View view, HeroProp heroProp,
			HeroQuality heroQuality, int starCnt, boolean scale, boolean isReal) {
		ViewUtil.setGone(view, R.id.starLayout);
		// ViewUtil.setGone(view, R.id.talent);
		ViewUtil.setVisible(view, R.id.talent);
		ViewUtil.setVisible(view, R.id.icon);
		//ViewUtil.setVisible(view, R.id.rating_flop);
		if (isReal) {
			if (heroProp.isNoClothHero()) // tuo yi
			{
				view.findViewById(R.id.hero_item).setBackgroundDrawable(
						Config.getController().getDrawable("strip_board"));
				new ViewImgScaleCallBack("strip_frame",
						view.findViewById(R.id.talent),
						Config.SCALE_FROM_HIGH * 92,
						Config.SCALE_FROM_HIGH * 92);
			} else {
				view.findViewById(R.id.hero_item).setBackgroundDrawable(
						Config.getController().getDrawable("hero_baseboard"));
				new ViewImgScaleCallBack("hero_frame",
						view.findViewById(R.id.talent),
						Config.SCALE_FROM_HIGH * 92,
						Config.SCALE_FROM_HIGH * 92);
			}

			ViewUtil.setText(view.findViewById(R.id.name), heroProp.getName());
			new ViewImgScaleCallBack(heroProp.getIcon(),
					view.findViewById(R.id.icon), Config.SCALE_FROM_HIGH * 82,
					Config.SCALE_FROM_HIGH * 82);

//			ViewUtil.setFlopRatingIcon(view.findViewById(R.id.rating_flop),
//					heroProp.getRating());

		} else {
			ViewUtil.setGone(view, R.id.talent);

			ViewUtil.setGone(view, R.id.icon);
			ViewUtil.setGone(view, R.id.rating_flop);
			// hero_item
			view.findViewById(R.id.hero_item).setBackgroundDrawable(
					Config.getController().getDrawable("flop_un_bg"));

			ViewUtil.setRichText(view.findViewById(R.id.name), ""/*
																 * heroProp.getName
																 * ()
																 */);
		}
	}

	public static void setHeroIcon(View view, HeroProp heroProp,
			HeroQuality heroQuality, int starCnt, float scale) {
		ViewUtil.setVisible(view, R.id.icon);
		ViewUtil.setVisible(view, R.id.talent);
		new ViewImgScaleCallBack(heroProp.getIcon(),
				view.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
						* Constants.HERO_ICON_WIDTH * scale,
				Config.SCALE_FROM_HIGH * Constants.HERO_ICON_HEIGHT * scale);
		new ViewImgScaleCallBack(heroQuality.getImage(), "hero_talent_default",
				view.findViewById(R.id.talent), Config.SCALE_FROM_HIGH
						* Constants.HERO_TALENT_WIDTH * scale,
				Config.SCALE_FROM_HIGH * Constants.HERO_TALENT_HEIGHT * scale);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.starLayout);
		LayoutParams params = (LayoutParams) layout.getLayoutParams();
		params.topMargin = (int) (Constants.HERO_STAR_TOP_MARGIN * scale * Config.SCALE_FROM_HIGH);
		layout.setLayoutParams(params);
		for (int i = 0; i < starViewIds.length; i++) {
			if (i < starCnt) {
				View star = layout.findViewById(starViewIds[i]);
				ViewUtil.setVisible(layout, starViewIds[i]);
				ViewUtil.adjustLayout(
						star,
						(int) (Constants.HERO_STAR_WIDTH * scale * Config.SCALE_FROM_HIGH),
						(int) (Constants.HERO_STAR_HEIGHT * scale * Config.SCALE_FROM_HIGH));
			} else {
				ViewUtil.setGone(layout, starViewIds[i]);
			}
		}
	}

	// 我要变强-->星级初始化
	public static void strongerStar(ViewGroup vg, int starCnt) {
		int[] starIds = { R.id.star1, R.id.star2, R.id.star3, R.id.star4,
				R.id.star5 };
		for (int i = 0; i < starIds.length; i++) {
			if (i < starCnt) {
				ImageUtil.setBgNormal(vg.findViewById(starIds[i]));
			} else {
				ImageUtil.setBgGray(vg.findViewById(starIds[i]));
			}
		}
	}

	public static void setSkillIcon(View view, BattleSkill battleSkill) {
		new ViewImgScaleCallBack(battleSkill.getIcon(),
				view.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
						* Constants.COMMON_SKILL_ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.COMMON_SKILL_ICON_HEIGHT);
	}

	/**
	 * 装备框
	 * 
	 * @param view
	 * @param type
	 *            与showDesc配合，当type>0 且showDesc==true时才会有效果
	 * @param equipmentInfoClient
	 * @param showDesc
	 */
	public static void setEquipmentIcon(View view, byte type,
			EquipmentInfoClient equipmentInfoClient, boolean showDesc,
			boolean isSuit) {
		if (null == view)
			return;
		if (null != equipmentInfoClient) {
			ViewUtil.setVisible(view, R.id.equipmentIcon);
			new ViewImgScaleCallBack(equipmentInfoClient.getProp().getIcon(),
					view.findViewById(R.id.equipmentIcon),
					Config.SCALE_FROM_HIGH
							* Constants.EQUIPMENT_WEAR_ICON_WIDTH,
					Config.SCALE_FROM_HIGH
							* Constants.EQUIPMENT_WEAR_ICON_HEIGHT);

			new ViewImgScaleCallBack(equipmentInfoClient.getEquipmentDesc()
					.getImage(), view.findViewById(R.id.equipmentTop),
					Config.SCALE_FROM_HIGH * Constants.EQUIPMENT_ICON_WIDTH,
					Config.SCALE_FROM_HIGH * Constants.EQUIPMENT_ICON_HEIGHT);

			ViewUtil.setVisible(view, R.id.equipmentLv);
			ViewUtil.setText(view.findViewById(R.id.equipmentLv), "LV "
					+ equipmentInfoClient.getLevel());
			View stoneLayout = view.findViewById(R.id.stoneLayout);
			if (equipmentInfoClient.hasStone()) {
				ViewUtil.setVisible(stoneLayout);
				setEquipmentStone(stoneLayout, equipmentInfoClient.getStone());
			} else {
				ViewUtil.setGone(stoneLayout);
			}
			ViewUtil.setGone(view, R.id.desc);
		} else {
			ViewUtil.setGone(view, R.id.equipmentIcon);
			ViewUtil.setImage(view, R.id.equipmentTop,
					R.drawable.equipment_null);
			ViewUtil.setGone(view, R.id.equipmentLv);
			if (showDesc && type > 0) {
				ViewUtil.setVisible(view, R.id.desc);
				ViewUtil.setText(view, R.id.desc,
						"添加" + PropEquipment.getTypeName(type));
			} else {
				ViewUtil.setGone(view, R.id.desc);
			}
			ViewUtil.setGone(view.findViewById(R.id.stoneLayout));
		}

		// 套装闪烁
		if (isSuit) {
			ViewUtil.setVisible(view.findViewById(R.id.equipmentLayoutItem));
		} else {
			ViewUtil.setHide(view.findViewById(R.id.equipmentLayoutItem));
		}

	}

	public static void setEquipmentIcon(View view, PropEquipment propEquipment,
			EquipmentDesc equipmentDesc, int level) {
		if (null == view || null == propEquipment)
			return;
		ViewUtil.setVisible(view, R.id.equipmentIcon);
		new ViewImgScaleCallBack(propEquipment.getIcon(),
				view.findViewById(R.id.equipmentIcon), Config.SCALE_FROM_HIGH
						* Constants.EQUIPMENT_WEAR_ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.EQUIPMENT_WEAR_ICON_HEIGHT);

		new ViewImgScaleCallBack(equipmentDesc.getImage(),
				view.findViewById(R.id.equipmentTop), Config.SCALE_FROM_HIGH
						* Constants.EQUIPMENT_ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.EQUIPMENT_ICON_HEIGHT);

		ViewUtil.setVisible(view, R.id.equipmentLv);
		ViewUtil.setText(view.findViewById(R.id.equipmentLv), "LV " + level);
		View stoneLayout = view.findViewById(R.id.stoneLayout);
		ViewUtil.setGone(stoneLayout);
		ViewUtil.setGone(view, R.id.desc);
		ViewUtil.setHide(view.findViewById(R.id.equipmentLayoutItem));
	}

	// 装备详情页 --套装效果
	public static void setSuitEffect(ViewGroup window, BaseHeroInfoClient hic,
			EquipmentInfoClient eic) {
		try {
			if (hic == null) {
				eic.getBattleSkillEqu();
				if (eic.itemHasSuit()) {
					ViewUtil.setVisible(window
							.findViewById(R.id.suit_desc_layout));
					ViewUtil.setRichText(window.findViewById(R.id.suit_title),
							StringUtil.color("【套装效果】", R.color.color17));

					ViewUtil.setRichText(
							window.findViewById(R.id.suit_effect),
							StringUtil.color(
									((BattleSkill) CacheMgr.battleSkillCache
											.get(eic.getBattleSkillEquipment()
													.getSkillId()))
											.getEffectDesc()
											+ "\n", R.color.color17)
									+ StringUtil.color("(穿齐4件"
											+ eic.getProp().getSuitName()
											+ "后开启)", R.color.color21));
				} else {
					ViewUtil.setGone(window.findViewById(R.id.suit_desc_layout));
				}
				return;
			}

			eic.getBattleSkillEqu();
			// 适配界面

			if (eic.itemHasSuit()) {
				ViewUtil.setVisible(window.findViewById(R.id.suit_desc_layout));
				BattleSkill battleSkill = hic.getSuitBattleSkill();
				if (battleSkill != null) {
					ViewUtil.setRichText(window.findViewById(R.id.suit_title),
							StringUtil.color("【套装效果】", R.color.color16));

					ViewUtil.setRichText(
							window.findViewById(R.id.suit_effect),
							StringUtil.color(
									battleSkill.getEffectDesc() + "\n",
									R.color.color16)
									+ StringUtil.color("(效果已开启)",
											R.color.color19));

				} else {
					ViewUtil.setRichText(window.findViewById(R.id.suit_title),
							StringUtil.color("【套装效果】", R.color.color17));

					ViewUtil.setRichText(
							window.findViewById(R.id.suit_effect),
							StringUtil.color(
									((BattleSkill) CacheMgr.battleSkillCache
											.get(eic.getBattleSkillEquipment()
													.getSkillId()))
											.getEffectDesc()
											+ "\n", R.color.color17)
									+ StringUtil.color("(穿齐4件"
											+ eic.getProp().getSuitName()
											+ "后开启)", R.color.color21));
				}
			} else {
				ViewUtil.setGone(window.findViewById(R.id.suit_desc_layout));
			}
		} catch (GameException e) {
			Log.e("EquipmentDetailWindow", "套装效果初始化失败！");
		}
	}

	public static void setEquipmentIcon(View view, byte type,
			EquipmentInfoClient equipmentInfoClient, boolean showDesc) {
		setEquipmentIcon(view, type, equipmentInfoClient, showDesc, false);
	}

	// 设装备上的宝石
	private static void setEquipmentStone(View view, Item item) {
		new ViewImgScaleCallBack(item.getImage(),
				view.findViewById(R.id.stoneIcon), Config.SCALE_FROM_HIGH
						* Constants.EQUIPMENT_STONE_SMALL_ICON_WIDTH,
				Config.SCALE_FROM_HIGH
						* Constants.EQUIPMENT_STONE_SMALL_ICON_HEIGHT);
	}

	public static void setHeroIcon(View widget, HeroIdBaseInfoClient info,
			int width, int height) {
		if (null == info || (0 == info.getId() && 0 == info.getHeroId())) {
			ViewUtil.setGone(widget, R.id.icon);
			ViewUtil.setImage(widget, R.id.rank, R.drawable.icon_bg2);
			ViewUtil.adjustLayout(widget.findViewById(R.id.rank), width, height);
			ViewUtil.setGone(widget, R.id.starLayout);
		} else {
			HeroProp hp = info.getHeroProp();
			HeroQuality heroQuality = info.getHeroQuality();
			setHeroIcon(widget, hp, heroQuality, info.getStar(), width, height);
		}
	}

	public static void setHeroIcon(View widget, HeroProp hp,
			HeroQuality heroQuality, int star) {
		if (null != hp) {
			ViewUtil.setVisible(widget, R.id.icon);
			new ViewImgScaleCallBack(hp.getIcon(),
					widget.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
							* Constants.HERO_ICON_WIDTH, Config.SCALE_FROM_HIGH
							* Constants.HERO_ICON_HEIGHT);
		}
		if (null != heroQuality) {
			ViewUtil.setVisible(widget, R.id.rank);
			ViewUtil.setText(widget, R.id.rank, "");
			// new HeroQualityBgImgCallBack(heroQuality.getImage(),
			// widget.findViewById(R.id.rank));
		}
		ViewUtil.setVisible(widget, R.id.starLayout);
		// ViewUtil.setHeroIconStar(
		// (ViewGroup) widget.findViewById(R.id.starLayout), star);
	}

	public static void setHeroIcon(View widget, HeroProp hp,
			HeroQuality heroQuality, int star, int width, int height) {
		ViewUtil.adjustLayout(widget.findViewById(R.id.bg), width - width
				/ Constants.MEDIUM_HERO_ICON_RATE, height - height
				/ Constants.MEDIUM_HERO_ICON_RATE);
		if (null != hp) {
			ViewUtil.setVisible(widget, R.id.icon);
			new ViewImgScaleCallBack(hp.getIcon(),
					widget.findViewById(R.id.icon), width - width
							/ Constants.MEDIUM_HERO_ICON_RATE, height - height
							/ Constants.MEDIUM_HERO_ICON_RATE);
		}
		if (null != heroQuality) {
			ViewUtil.setVisible(widget, R.id.rank);
			ViewUtil.setText(widget, R.id.rank, "");
			// new HeroQualityBgImgCallBack(heroQuality.getImage(),
			// widget.findViewById(R.id.rank), width, height);
		}
		ViewUtil.setVisible(widget, R.id.starLayout);
		// ViewUtil.setHeroIconStar(
		// (ViewGroup) widget.findViewById(R.id.starLayout), star, width,
		// height);
	}

	public static void setHeroIcon(View widget, String icon,
			HeroQuality heroQuality, int star) {
		if (!StringUtil.isNull(icon)) {
			ViewUtil.setVisible(widget, R.id.icon);
			new ViewImgScaleCallBack(icon, widget.findViewById(R.id.icon),
					Config.SCALE_FROM_HIGH * Constants.HERO_ICON_WIDTH,
					Config.SCALE_FROM_HIGH * Constants.HERO_ICON_HEIGHT);
		}
		if (null != heroQuality) {
			ViewUtil.setVisible(widget, R.id.rank);
			ViewUtil.setText(widget, R.id.rank, "");
			// new HeroQualityBgImgCallBack(heroQuality.getImage(),
			// widget.findViewById(R.id.rank));
		}
		ViewUtil.setVisible(widget, R.id.starLayout);
		// ViewUtil.setHeroIconStar(
		// (ViewGroup) widget.findViewById(R.id.starLayout), star);
	}

	// 领地图标与规模/资源类型
	public static void setFiefIcon(View widget, BriefFiefInfoClient bfic) {
		if (null == widget || null == bfic)
			return;

		new ViewImgScaleCallBack(bfic.getIcon(),
				widget.findViewById(R.id.icon), Constants.COMMON_ICON_WIDTH,
				Constants.COMMON_ICON_HEIGHT);
		ViewUtil.setText(widget, R.id.scaleName, bfic.getName());
	}

	// 领地图标与规模/资源类型
	public static void setFiefIconWithBattleState(View widget,
			BriefFiefInfoClient bfic) {
		setFiefIcon(widget, bfic);

		if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
				bfic.getBattleState(), bfic.getBattleTime()))) {
			ViewUtil.setVisible(widget, R.id.stateIcon);
			String stateIcon = bfic.getStateIcon();
			if (!StringUtil.isNull(stateIcon))
				ViewUtil.setImage(widget, R.id.stateIcon, stateIcon);
			else
				widget.findViewById(R.id.stateIcon).setBackgroundDrawable(null);
		}
	}

	public static void setArmIcon(View icon, final TroopProp prop,
			final UserTroopEffectInfo troopEffectInfo) {

		if (null == prop || null == icon)
			return;

		new ViewImgScaleCallBack(prop.getIcon(), icon,
				Constants.COMMON_ICON_WIDTH, Constants.COMMON_ICON_HEIGHT);

		icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new TroopDetailTip().show(prop, troopEffectInfo);
			}
		});
	}

	public static void setArmIcon(View icon, final ArmInfoClient armInfo,
			final UserTroopEffectInfo troopEffectInfo) {
		if (null == armInfo || null == armInfo.getProp() || null == icon)
			return;

		new ViewImgScaleCallBack(armInfo.getProp().getIcon(), icon,
				Constants.COMMON_ICON_WIDTH, Constants.COMMON_ICON_HEIGHT);
		icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new TroopDetailTip().show(armInfo.getProp(), troopEffectInfo);
			}
		});
	}

	public static void setArmIcon(View icon, final ArmInfoClient armInfo) {
		if (null == armInfo || null == armInfo.getProp() || null == icon)
			return;

		new ViewImgScaleCallBack(armInfo.getProp().getIcon(), icon,
				Constants.COMMON_ICON_WIDTH, Constants.COMMON_ICON_HEIGHT);
	}

	public static void setArmIcon(View icon, final ArmInfoClient armInfo,
			final UserTroopEffectInfo troopEffectInfo,
			final List<HeroInfoClient> hic) {
		if (null == armInfo || null == armInfo.getProp() || null == icon)
			return;

		new ViewImgScaleCallBack(armInfo.getProp().getIcon(), icon,
				Constants.COMMON_ICON_WIDTH, Constants.COMMON_ICON_HEIGHT);
		icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new TroopDetailTip().show(armInfo.getProp(), troopEffectInfo,
						hic, 0);
			}
		});
	}

	public static void setOtherArmIcon(View icon, final ArmInfoClient armInfo,
			final UserTroopEffectInfo troopEffectInfo,
			final List<OtherHeroInfoClient> ohic) {
		if (null == armInfo || null == armInfo.getProp() || null == icon)
			return;

		new ViewImgScaleCallBack(armInfo.getProp().getIcon(), icon,
				Constants.COMMON_ICON_WIDTH, Constants.COMMON_ICON_HEIGHT);
		icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new TroopDetailTip().show(armInfo.getProp(), troopEffectInfo,
						ohic, 0, null);
			}
		});
	}

	// 战斗 主将 副将icon
	public static void setBattleHeroIcon(View view, HeroIdBaseInfoClient hiic,
			boolean isLegate) {
		if (null == hiic || null == hiic.getHeroProp()
				|| null == hiic.getHeroQuality()) {
			ViewUtil.setGone(view, R.id.icon);
			ViewUtil.setGone(view, R.id.talent);
		} else {
			setBattleHeroIcon(view, hiic.getHeroProp(), hiic.getHeroQuality(),
					hiic.getStar(), false, isLegate);
		}
	}

	public static void setBattleHeroIcon(View view, HeroProp heroProp,
			HeroQuality heroQuality, int starCnt, boolean scale,
			boolean isLegate) {
		ViewUtil.setVisible(view, R.id.icon);
		ViewUtil.setVisible(view, R.id.talent);
		if (isLegate) {
			new ViewImgScaleCallBack(heroProp.getIcon(), "default_hero",
					view.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
							* Constants.SECONDARY_ICON_WIDTH,
					Config.SCALE_FROM_HIGH * Constants.SECONDARY_ICON_HEIGHT);
		} else {
			new ViewImgScaleCallBack(heroProp.getIcon(), "default_hero",
					view.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
							* Constants.BATTLE_HERO_ICON_WIDTH,
					Config.SCALE_FROM_HIGH * Constants.BATTLE_HERO_ICON_HEIGHT);
		}
		if (isLegate) {
			new ViewImgScaleCallBack(heroQuality.getImageSecond(),
					"frame_vice_white", view.findViewById(R.id.talent),
					Config.SCALE_FROM_HIGH * Constants.SECONDARY_TALENT_WIDTH,
					Config.SCALE_FROM_HIGH * Constants.SECONDARY_TALENT_WIDTH);
		} else {
			new ViewImgScaleCallBack(heroQuality.getImageMajor(),
					"frame_main_white", view.findViewById(R.id.talent),
					Config.SCALE_FROM_HIGH * Constants.BATTLE_TALENT_WIDTH,
					Config.SCALE_FROM_HIGH * Constants.BATTLE_TALENT_WIDTH);
		}
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.starLayout);
		LayoutParams params = (LayoutParams) layout.getLayoutParams();
		if (isLegate) {
			params.bottomMargin = (int) (Constants.SECONDARY_STAR_BOTTOM_MARGIN * Config.SCALE_FROM_HIGH);
		} else {
			params.bottomMargin = (int) (Constants.BATTLE_HERO_STAR_BOTTOM_MARGIN * Config.SCALE_FROM_HIGH);
		}
		if (starCnt > 0) {
			layout.setVisibility(View.VISIBLE);
		} else {
			layout.setVisibility(View.GONE);
		}
		for (int i = 0; i < starViewIds.length; i++) {
			if (i < starCnt) {
				View star = layout.findViewById(starViewIds[i]);
				ViewUtil.setVisible(layout, starViewIds[i]);
				int star_width = Constants.BATTLE_HERO_STAR_WIDTH;
				int star_height = Constants.BATTLE_HERO_STAR_HEIGHT;
				if (isLegate) {
					star_width = Constants.SECONDARY_STAR_WIDTH;
					star_height = Constants.SECONDARY_STAR_HEIGHT;
				}
				ViewUtil.adjustLayout(star,
						(int) (star_width * Config.SCALE_FROM_HIGH),
						(int) (star_height * Config.SCALE_FROM_HIGH));
			} else {
				ViewUtil.setGone(layout, starViewIds[i]);
			}
		}
	}

	// 设置玩家头像
	public static void setUserIcon(View view, BriefUserInfoClient briefUser) {
		new UserIconCallBack(briefUser, view.findViewById(R.id.icon),
				Constants.ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ICON_HEIGHT * Config.SCALE_FROM_HIGH);
		ViewUtil.setGone(view, R.id.numLayout);
		ViewUtil.setGone(view, R.id.rankTopLayout);
	}

	public static void setUserIcon(View view, BriefUserInfoClient briefUser,
			String desc) {
		new UserIconCallBack(briefUser, view.findViewById(R.id.icon),
				Constants.ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ICON_WIDTH * Config.SCALE_FROM_HIGH);
		ViewUtil.setVisible(view, R.id.numLayout);
		ViewUtil.setRichText(view, R.id.num, desc);
		ViewUtil.setGone(view, R.id.rankTopLayout);
	}

	public static void setRankNum(View view, BriefUserInfoClient briefUser,
			String desc) {
		new UserIconCallBack(briefUser, view.findViewById(R.id.icon),
				Constants.USER_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.USER_ICON_HEIGHT_BIG * Config.SCALE_FROM_HIGH);
		ViewUtil.setGone(view, R.id.numLayout);
		ViewUtil.setVisible(view, R.id.rankTopLayout);
		ViewUtil.setRichText(view, R.id.rankTopNum, desc);
	}

	// 设置Vip头像
	public static void setUserVipIcon(View view, BriefUserInfoClient briefUser,
			int vipLevel) {
		new UserIconCallBack(briefUser, view.findViewById(R.id.icon),
				Constants.USER_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.USER_ICON_HEIGHT_BIG * Config.SCALE_FROM_HIGH);
		ViewUtil.setVisible(view, R.id.numLayout);
		ViewUtil.setText(view, R.id.num, "VIP " + vipLevel);
		ViewUtil.setGone(view, R.id.rankTopLayout);
	}

	// 设置排行榜
	public static void setUserRankIcon(View view,
			BriefUserInfoClient briefUser, byte type, int rank) {
		new UserIconCallBack(briefUser, view.findViewById(R.id.icon),
				Constants.USER_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.USER_ICON_HEIGHT_BIG * Config.SCALE_FROM_HIGH);
		ViewUtil.setVisible(view, R.id.numLayout);
		ViewUtil.setText(view, R.id.num, "NO." + rank);
		ViewUtil.setGone(view, R.id.rankTopLayout);
	}

}
