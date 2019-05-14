package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.HeroTroopName;
import com.vikings.sanguo.model.OtherHeroArmPropInfoClient;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HeroSkillTip extends CustomConfirmDialog {
	private int defSkillId = -1; // 默认防守技能
	private List<OtherHeroArmPropInfoClient> heroArmlist;
	private ViewGroup skillItem;

	public HeroSkillTip() {
		super("技能效果", DEFAULT);
		skillItem = (ViewGroup) content.findViewById(R.id.skill_item);
		findViewById(R.id.closeBtn1).setOnClickListener(closeL);
	}

	public void show(List<HeroSkillSlotInfoClient> skillSlotInfos,
			List<OtherHeroArmPropInfoClient> heroArmlist, int defSkillId) {
		setValue(skillSlotInfos, heroArmlist, defSkillId);
		super.show();
	}

	public HeroSkillTip(List<HeroSkillSlotInfoClient> skillSlotInfos,
			List<OtherHeroArmPropInfoClient> heroArmlist, int defSkillId) {
		this();
		setValue(skillSlotInfos, heroArmlist, defSkillId);
	}

	private void setValue(List<HeroSkillSlotInfoClient> skillSlotInfos,
			List<OtherHeroArmPropInfoClient> heroArmlist, int defSkillId) {
		// tip复用时，清除之前的内容
		skillItem.removeAllViews();

		this.heroArmlist = heroArmlist;
		this.defSkillId = defSkillId;

		List<BattleSkill> list = new ArrayList<BattleSkill>();

		if (!ListUtil.isNull(skillSlotInfos)) {
			for (HeroSkillSlotInfoClient hssic : skillSlotInfos) {
				if (hssic.getSkillId() > 0)
					list.add(hssic.getBattleSkill());
			}
		}

		if (defSkillId > 0) {
			try {
				BattleSkill bs = (BattleSkill) CacheMgr.battleSkillCache
						.get(defSkillId);
				list.add(bs);
			} catch (GameException e) {
				Log.e("HeroSkillTip",
						"BattleSkill " + defSkillId + " not find", e);
			}
		}

		for (BattleSkill it : list)
			setSkillDesc(it);
	}

	public void setSkillDesc(BattleSkill bs) {
		View v = controller.inflate(R.layout.skill_effect_item);
		ImageView skillIcon = (ImageView) v.findViewById(R.id.skillIcon);
		TextView skillName = (TextView) v.findViewById(R.id.skillName);
		TextView skillDesc = (TextView) v.findViewById(R.id.skillDesc);

		if (null != bs) {
			Drawable d = CacheMgr.battleSkillCache.getSkillDrawable(bs.getId(),
					true);
			skillIcon.setBackgroundDrawable(d);
			ViewUtil.setText(skillName, bs.getName());
			String attrStr = initAttrStr(bs);
			if (null != attrStr)
				ViewUtil.setRichText(skillDesc, initAttrStr(bs));
		}

		skillItem.addView(v);
	}

	private String initAttrStr(BattleSkill skill) {
		if (ListUtil.isNull(heroArmlist)) {
			// 3.25 与韦洲誉讨论：没有守城将领时，城防技能直接显示说明
			if (-1 != defSkillId) {
				try {
					BattleSkill bs = (BattleSkill) CacheMgr.battleSkillCache
							.get(defSkillId);
					return bs.getEffectDesc();
				} catch (GameException e) {
					Log.e("HeroSkillTip", "BattleSkill " + defSkillId
							+ " not find", e);
				}
			} else
				return null;
		}

		StringBuffer sBuffer = new StringBuffer();

		for (OtherHeroArmPropInfoClient ohapic : heroArmlist) {
			HeroTroopName htn = ohapic.getHeroTroopName();
			String string = "";
			if (null != htn) {
				string = StringUtil.getSkillEffectDesc(ohapic, skill); // , htn,
				// skill.getEffectDesc()

				sBuffer.append(string + "<br>");
				break;
			}

		}
		return sBuffer.toString();

	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_hero_skill_detail, tip, false);
	}

}
