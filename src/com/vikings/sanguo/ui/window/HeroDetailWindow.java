package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.HeroLevelUp;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.SkillCombo;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.HeroComboSkillTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public abstract class HeroDetailWindow extends CustomPopupWindow {
	protected ViewGroup equipmentLayout1, equipmentLayout2, equipmentLayout3,
			equipmentLayout4, heroIconLayout, armPropLayout, skillLayout,
			armPropContent, skillContent, skillCombinationContent, btnLayout;
	protected TextView talent, level, name, stamina, exp, attack, defend,
			ability, seeLargePic;
	protected Button strengthBtn, upgradeBtn, evolveBtn, favourBtn;
	private boolean isNoSaveRef;   //是否退出后回收高清大图
	
	public void open(boolean isNoSaveRef)
	{		
		doOpen();
		this.isNoSaveRef = isNoSaveRef;
	}

	@Override
	protected void init() {
		super.init("将领详情");
		setContent(R.layout.hero_detail);
		// 加保护
		if (window == null) {
			return;
		}
		equipmentLayout1 = (ViewGroup) window
				.findViewById(R.id.equipmentLayout1);
		equipmentLayout2 = (ViewGroup) window
				.findViewById(R.id.equipmentLayout2);
		equipmentLayout3 = (ViewGroup) window
				.findViewById(R.id.equipmentLayout3);
		equipmentLayout4 = (ViewGroup) window
				.findViewById(R.id.equipmentLayout4);
		heroIconLayout = (ViewGroup) window.findViewById(R.id.heroIconLayout);
		armPropLayout = (ViewGroup) window.findViewById(R.id.armPropLayout);
		skillLayout = (ViewGroup) window.findViewById(R.id.skillLayout);
		talent = (TextView) window.findViewById(R.id.talent);
		level = (TextView) window.findViewById(R.id.level);
		name = (TextView) window.findViewById(R.id.name);
		stamina = (TextView) window.findViewById(R.id.stamina);
		exp = (TextView) window.findViewById(R.id.exp);
		attack = (TextView) window.findViewById(R.id.attack);
		defend = (TextView) window.findViewById(R.id.defend);
		ability = (TextView) window.findViewById(R.id.ability);
		armPropContent = (ViewGroup) window.findViewById(R.id.armPropContent);
		skillContent = (ViewGroup) window.findViewById(R.id.skillContent);
		skillCombinationContent = (ViewGroup) window
				.findViewById(R.id.skillCombinationContent);
		btnLayout = (ViewGroup) window.findViewById(R.id.btnLayout);
		strengthBtn = (Button) window.findViewById(R.id.strengthBtn);
		upgradeBtn = (Button) window.findViewById(R.id.upgradeBtn);
		evolveBtn = (Button) window.findViewById(R.id.evolveBtn);
		favourBtn = (Button) window.findViewById(R.id.favourBtn);
		seeLargePic = (TextView) window.findViewById(R.id.seeLargePic);
	}

	@Override
	public void showUI() {
		super.showUI();
		setValue();
	}

	private void setValue() {
		// 加保护
		if (mainContent == null) {
			return;
		}
		setBtn();
		setHeroInfo(getBaseHeroInfoClient());
		setArmPropInfos();
		setSkillInfos(getBaseHeroInfoClient());
		setCombSkillInfo(getBaseHeroInfoClient());
		setEquipmentInfo();
	}

	protected void setBtn() {
		ViewUtil.setGone(btnLayout);
	}

	protected void setEquipmentInfo() {
		setEquipment(equipmentLayout1, PropEquipment.TYPE_WEAPON);
		setEquipment(equipmentLayout2, PropEquipment.TYPE_ARMOR);
		setEquipment(equipmentLayout3, PropEquipment.TYPE_CLOTHES);
		setEquipment(equipmentLayout4, PropEquipment.TYPE_ACCESSORIES);
	}

	protected void setEquipment(View view, byte type) {
		List<EquipmentSlotInfoClient> esics = getBaseHeroInfoClient()
				.getEquipmentSlotInfos();
		EquipmentSlotInfoClient info = null;
		for (EquipmentSlotInfoClient esic : esics) {
			if (esic.getType() == type) {
				info = esic;
				break;
			}
		}
		if (null != info && info.hasEquipment()) {
			setHasEquipment(view, info);
		} else {
			setNoEquipment(view, type);
		}
	}

	protected void setCombSkillInfo(BaseHeroInfoClient bhic) {
		// 此为该英雄 的组合技能
		List<SkillCombo> skillCombos = bhic.getSkillCombos();
		List<BattleSkill> mBattleSkills = bhic
				.getBattleSkillsBySkillCombos(skillCombos);
		if (ListUtil.isNull(mBattleSkills)
				|| skillCombos.size() != mBattleSkills.size()) {
			ViewUtil.setGone(findViewById(R.id.skillCombinationLayout));
			return;
		}

		int childCnt = skillCombinationContent.getChildCount();
		for (int i = 0; i < mBattleSkills.size() - childCnt; i++) {
			ViewGroup viewGroup = (ViewGroup) controller.inflate(
					R.layout.hero_skill_unit, skillCombinationContent, false);
			ViewUtil.setVisible(viewGroup, R.id.name);
			ViewUtil.setText(viewGroup, R.id.name, mBattleSkills.get(i)
					.getName());
			ViewUtil.setVisible(viewGroup, R.id.level);
			ViewUtil.setText(viewGroup, R.id.level, "Lv:"
					+ mBattleSkills.get(i).getLevel());
			ViewUtil.setGone(viewGroup, R.id.addDesc);
			new ViewImgScaleCallBack(mBattleSkills.get(i).getIcon(),
					viewGroup.findViewById(R.id.icon),
					Constants.SKILL_ICON_WIDTH * Config.SCALE_FROM_HIGH,
					Constants.SKILL_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
			viewGroup.setTag(skillCombos.get(i));
			viewGroup.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new HeroComboSkillTip((SkillCombo) v.getTag()).show();
				}
			});
			skillCombinationContent.addView(viewGroup);
		}
	}

	private void setSkillInfos(BaseHeroInfoClient bhic) {
		List<HeroSkillSlotInfoClient> list = bhic.getSkillSlotInfos();
		int childCnt = skillContent.getChildCount();
		for (int i = 0; i < list.size() - childCnt; i++) {
			ViewGroup viewGroup = (ViewGroup) controller.inflate(
					R.layout.hero_skill_unit, skillContent, false);
			skillContent.addView(viewGroup);
		}

		for (int i = 0; i < list.size(); i++) {
			setSkillInfo(skillContent.getChildAt(i), list.get(i));
		}
		if (isMineHero()) {
			ViewUtil.setVisible(window, R.id.skillsDesc);
		} else {
			ViewUtil.setGone(window, R.id.skillsDesc);
		}

	}

	private void setHeroInfo(BaseHeroInfoClient bhic) {
		setIcon(bhic);
		ViewUtil.setText(talent, bhic.getHeroQuality().getName());
		ViewUtil.setText(name, bhic.getHeroProp().getName());
		ViewUtil.setText(level, "LV" + bhic.getLevel());
		if (isMineHero()) {
			ViewUtil.setVisible(stamina);
			ViewUtil.setText(stamina, "体力:" + bhic.getStamina() + "/"
					+ CacheMgr.heroCommonConfigCache.getMaxStamina());
			ViewUtil.setVisible(exp);
			if (null != bhic.getHeroType()) {
				HeroLevelUp heroLevelUp = (HeroLevelUp) CacheMgr.heroLevelUpCache
						.getExp(bhic.getHeroType().getType(), bhic.getStar(),
								bhic.getLevel());

				if (null != heroLevelUp)
					ViewUtil.setText(exp, "经验:" + bhic.getExp() + "/"
							+ heroLevelUp.getNeedExp());
			}
		} else {
			ViewUtil.setGone(stamina);
			ViewUtil.setGone(exp);
		}

		ViewUtil.setRichText(ability, StringUtil.numImgStr("v", getAbility()));

		int[] values = getBaseHeroInfoClient().getEquipmentValue();
		ViewUtil.setRichText(
				attack,
				EquipmentEffect
						.getEffectTypeName(EquipmentEffect.EFFECT_TYPE_ATTACK)
						+ ":"
						+ bhic.getRealAttack()
						+ StringUtil.color((values[0] > 0 ? "+" + values[0]
								: ""), R.color.color19));
		ViewUtil.setRichText(
				defend,
				EquipmentEffect
						.getEffectTypeName(EquipmentEffect.EFFECT_TYPE_DEFEND)
						+ ":"
						+ bhic.getRealDefend()
						+ StringUtil.color((values[1] > 0 ? "+" + values[1]
								: ""), R.color.color19));

	}

	private void setIcon(BaseHeroInfoClient bhic) {
		// 加保护
		if (heroIconLayout == null) {
			return;
		}
		heroIconLayout.setTag(bhic);
		heroIconLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new HeroDetailHDWindow().open(((BaseHeroInfoClient) v.getTag())
						.getHeroId(),isNoSaveRef);

			}
		});
		IconUtil.setHeroIcon(heroIconLayout, bhic);
	}

	protected abstract boolean isMineHero();

	protected abstract BaseHeroInfoClient getBaseHeroInfoClient();

	protected abstract void setArmPropInfos();

	protected abstract void setSkillInfo(View view,
			HeroSkillSlotInfoClient hssic);

	protected abstract void setHasEquipment(View view,
			EquipmentSlotInfoClient esic);

	protected abstract void setNoEquipment(View view, byte type);

	protected abstract boolean isSuit();

	protected abstract int getAbility();
}
