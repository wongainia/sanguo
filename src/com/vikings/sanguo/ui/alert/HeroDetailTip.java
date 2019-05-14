package com.vikings.sanguo.ui.alert;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.HeroIdBaseInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroQuality;
import com.vikings.sanguo.model.HeroType;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HeroDetailTip extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_hero_detail;
	private HeroProp heroProp;
	private HeroType heroType;
	private HeroQuality heroQuality;
	private String heroDec = null;

	public HeroDetailTip(HeroProp heroProp, HeroType heroType,
			HeroQuality heroQuality) {
		super(CustomConfirmDialog.DEFAULT);
		this.heroProp = heroProp;
		this.heroType = heroType;
		this.heroQuality = heroQuality;
	}

	public HeroDetailTip(HeroIdBaseInfoClient heroIdInfoClient, String heroDec) {
		this(heroIdInfoClient.getHeroProp(), heroIdInfoClient.getHeroType(),
				heroIdInfoClient.getHeroQuality());
		this.heroDec = heroDec;
	}

	public HeroDetailTip(HeroIdBaseInfoClient heroIdInfoClient) {
		this(heroIdInfoClient.getHeroProp(), heroIdInfoClient.getHeroType(),
				heroIdInfoClient.getHeroQuality());
	}

	public void show() {
		if (null == heroProp || null == heroQuality)
			return;
		SoundMgr.play(R.raw.sfx_tips);
		setValue();
		super.show();
	}

	private void setValue() {
		setTitle(heroProp.getName());
		findViewById(R.id.hero_evaluate).setBackgroundResource(
				heroProp.getRatingPic());

		if (heroDec != null) {
			ViewUtil.setRichText(content, R.id.soulDesc, heroDec);
		} else {
			ViewUtil.setGone(content, R.id.heroDesc);
		}

		content.findViewById(R.id.closeBtn1).setOnClickListener(closeL);
		if (null == heroType) {
			ViewUtil.setGone(content.findViewById(R.id.iconLayout), R.id.icon);
			ViewUtil.setGone(content.findViewById(R.id.iconLayout), R.id.talent);
		} else {
			IconUtil.setHeroIcon(content.findViewById(R.id.iconLayout),
					heroProp, heroQuality, heroType.getStar(), true);
		}
		ViewUtil.setText(content, R.id.typeName, "品质:" + heroQuality.getName());
		if (heroProp.getStaticSkillId() > 0) {
			try {
				BattleSkill skill = (BattleSkill) CacheMgr.battleSkillCache
						.get(heroProp.getStaticSkillId());
				ViewUtil.setText(content, R.id.staticSkill,
						"技能:" + skill.getName());
			} catch (GameException e) {

			}

		} else {

		}
		ViewUtil.setText(content, R.id.ownSkill,
				"统率:" + heroProp.getArmPropDesc());

		// String[] attrArr = heroQuality.getProfAndSkill();// 统率兵种/可学技能
		// ViewUtil.setText(content, R.id.prof, attrArr[0].trim());
		// ViewUtil.setText(content, R.id.skill, attrArr[1].trim());
		// ViewUtil.setText(content, R.id.troop,
		// "天生统率:" + heroProp.getArmPropDesc());
		// if (heroProp.getAbadonItemId() > 0) {
		// Item item = CacheMgr.getItemByID(heroProp.getAbadonItemId());
		// if (null != item)
		// ViewUtil.setText(content, R.id.decomposition,
		// "分解材料:" + item.getName());
		// } else {
		//
		// }

		ViewUtil.setRichText(content, R.id.desc, heroProp.getDesc());

		// Item item =
		// CacheMgr.heroRecruitExchangeCache.getItem(heroProp.getId());
		// if (null != item) {
		// ViewUtil.setRichText(content, R.id.soulDesc, item.getDesc());
		// } else {
		// ViewUtil.setRichText(content, R.id.soulDesc, "系统将领，玩家不可获得");
		// }

	}

	@Override
	public View getContent() {
		return controller.inflate(layout, tip, false);
	}
}
