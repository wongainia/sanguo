package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.SkillCombo;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HeroComboSkillTip extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_hero_skill_combo_detail;

	private ViewGroup curEffectLayout, iconLayout, hero_combo_item;
	private SkillCombo skillCombo;
	private BattleSkill mBattleSkill;

	public HeroComboSkillTip(SkillCombo skillCombo) {
		super();
		this.skillCombo = skillCombo;
		try {
			this.mBattleSkill = (BattleSkill) CacheMgr.battleSkillCache
					.get(skillCombo.getId());
		} catch (GameException e) {
			e.printStackTrace();
		}
	}

	public HeroComboSkillTip(BattleSkill mBattleSkill) {
		super();
		this.mBattleSkill = mBattleSkill;
	}

	public void show() {
		super.show();
		hero_combo_item = (ViewGroup) content
				.findViewById(R.id.hero_combo_item);
		curEffectLayout = (ViewGroup) content
				.findViewById(R.id.curEffectLayout);
		iconLayout = (ViewGroup) content.findViewById(R.id.iconLayout);
		setValue();
	}

	private void setValue() {
		setTitle(mBattleSkill.getName());
		findViewById(R.id.closeBt).setOnClickListener(closeL);
		new ViewImgScaleCallBack(mBattleSkill.getIcon(),
				iconLayout.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
						* Constants.HERO_ICON_WIDTH
						* IconUtil.HERO_ICON_SCALE_MED, Config.SCALE_FROM_HIGH
						* Constants.HERO_ICON_WIDTH
						* IconUtil.HERO_ICON_SCALE_MED);
		ViewUtil.setText(curEffectLayout, R.id.desc,
				mBattleSkill.getEffectDesc());
		if (skillCombo != null)
			setHeroDetail();
	}

	private void setHeroDetail() {
		ViewUtil.setVisible(content.findViewById(R.id.vip_seprate_bg1));
		ViewUtil.setVisible(content.findViewById(R.id.hero_combo_desc));
		hero_combo_item.removeAllViews();
		setHeroItem(skillCombo.getHero1Id());
		setHeroItem(skillCombo.getHero2Id());
		setHeroItem(skillCombo.getHero3Id());

	}

	private void setHeroItem(int heroId) {
		if (heroId == 0)
			return;
		try {
			HeroProp hpHeroProp;
			hpHeroProp = (HeroProp) CacheMgr.heroPropCache.get(heroId);
			ViewGroup viewGroup = (ViewGroup) controller.inflate(
					R.layout.hero_skill_combo_item_detail, hero_combo_item,
					false);
			ViewUtil.setRichText(viewGroup.findViewById(R.id.heroName),
					hpHeroProp.getName());

			hero_combo_item.addView(viewGroup);
			new ViewImgScaleCallBack(hpHeroProp.getIcon(),
					viewGroup.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
							* Constants.HERO_ICON_WIDTH
							* IconUtil.HERO_ICON_SCALE_MED,
					Config.SCALE_FROM_HIGH * Constants.HERO_ICON_HEIGHT
							* IconUtil.HERO_ICON_SCALE_MED);

		} catch (GameException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
