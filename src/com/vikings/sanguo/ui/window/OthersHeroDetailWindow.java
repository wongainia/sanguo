package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.OtherHeroArmPropInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.EquipmentTip;
import com.vikings.sanguo.ui.alert.HeroSkillTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

// 别人的将领
public class OthersHeroDetailWindow extends HeroDetailWindow {
	private OtherHeroInfoClient ohic;

	public void open(boolean isNoSaveRef) {
		super.open(isNoSaveRef);
	}

	public void open(OtherHeroInfoClient ohic) {
		if (null == ohic)
			return;
		this.ohic = ohic;
		doOpen();
	}

	@Override
	protected boolean isMineHero() {
		return false;
	}

	@Override
	protected BaseHeroInfoClient getBaseHeroInfoClient() {
		return ohic;
	}

	@Override
	protected void setArmPropInfos() {
		List<OtherHeroArmPropInfoClient> ohapics = getArmPropInfoClients();
		if (null == armPropContent)
			return;
		int childCnt = armPropContent.getChildCount();
		for (int i = 0; i < ohapics.size() - childCnt; i++) {
			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.hero_armprop_detail);
			armPropContent.addView(viewGroup);
		}

		for (int i = 0; i < ohapics.size(); i++) {
			OtherHeroArmPropInfoClient ohapic = ohapics.get(i);
			View v = armPropContent.getChildAt(i);
			setArmPropValue(ohapic, v);
		}
		ViewUtil.setGone(window, R.id.armPropsDesc);
	}

	protected List<OtherHeroArmPropInfoClient> getArmPropInfoClients() {
		List<OtherHeroArmPropInfoClient> ohapics = ohic.getArmPropInfos();
		return ohapics;
	}

	private void setArmPropValue(OtherHeroArmPropInfoClient ohapic, View v) {
		ViewUtil.setImage(v, R.id.smallIcon, ohapic.getHeroTroopName()
				.getSmallIcon());
		ViewUtil.setText(v, R.id.name, ohapic.getArmPropSlug(false));
		ProgressBar bar = (ProgressBar) v.findViewById(R.id.progressBar);
		bar.set(ohapic.getValue(), ohapic.getMaxValue());
		ViewUtil.setRichText(v, R.id.progressDesc, ohapic.getValue() + "/"
				+ ohapic.getMaxValue());
		List<TroopProp> troopProps = CacheMgr.troopPropCache
				.getTroopPropByType((byte) ohapic.getType());
		int[] equipValues = getBaseHeroInfoClient().getEquipmentValue();
		if (troopProps.size() > 0) {
			TroopProp troopProp = troopProps.get(0);
			double attackValue = ohapic.attackAddValue(getAttack()
					+ equipValues[0], troopProp) * 100;
			double defendValue = ohapic.defendAddValue(getDefend()
					+ equipValues[1], troopProp) * 100;
			ViewUtil.setRichText(
					v.findViewById(R.id.attack),
					"攻"
							+ StringUtil.color("+" + (int) attackValue + "%",
									R.color.color19));
			ViewUtil.setRichText(
					v.findViewById(R.id.defend),
					"防"
							+ StringUtil.color("+" + (int) defendValue + "%",
									R.color.color19));
		}

	}

	protected int getDefend() {
		return ohic.getRealDefend();
	}

	protected int getAttack() {
		return ohic.getRealAttack();
	}

	@Override
	protected void setSkillInfo(View view, HeroSkillSlotInfoClient hssic) {
		ViewUtil.setGone(view, R.id.addDesc);
		if (hssic.hasSkill() && null != hssic.getBattleSkill()) {
			ViewUtil.setVisible(view, R.id.name);
			StringBuilder skillName = new StringBuilder(hssic.getBattleSkill()
					.getName());
			int index = skillName.indexOf("L");
			if (index >= 0)
				skillName.insert(index, "<br/>");
			ViewUtil.setRichText(view, R.id.name, skillName.toString());
			ViewUtil.setGone(view, R.id.level);
			ViewUtil.setVisible(view, R.id.icon);
			new ViewImgScaleCallBack(hssic.getBattleSkill().getIcon(),
					view.findViewById(R.id.icon), Constants.SKILL_ICON_WIDTH
							* Config.SCALE_FROM_HIGH,
					Constants.SKILL_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

			List<HeroSkillSlotInfoClient> heroSkillSlotInfoClients = new ArrayList<HeroSkillSlotInfoClient>();
			heroSkillSlotInfoClients.add(hssic);
			view.setTag(heroSkillSlotInfoClients);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new HeroSkillTip()
							.show(/* getSkillSlotInfoClients() */(List<HeroSkillSlotInfoClient>) v
									.getTag(), getArmPropInfoClients(), 0);

				}
			});
		} else {
			ViewUtil.setGone(view, R.id.name);
			ViewUtil.setGone(view, R.id.level);
			ViewUtil.setGone(view, R.id.icon);
			view.setOnClickListener(null);
		}

	}

	protected List<HeroSkillSlotInfoClient> getSkillSlotInfoClients() {
		return ohic.getSkillSlotInfos();
	}

	@Override
	protected void setHasEquipment(View view, EquipmentSlotInfoClient esic) {
		IconUtil.setEquipmentIcon(view, (byte) esic.getType(), esic.getEic(),
				false, isSuit());
		final EquipmentInfoClient eic = esic.getEic();
		if (null != eic)
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 装备详情弹框
					new EquipmentTip().show(eic, getBaseHeroInfoClient());
				}
			});
		else
			view.setOnClickListener(null);

	}

	@Override
	protected void setNoEquipment(View view, byte type) {
		IconUtil.setEquipmentIcon(view, type, null, false);
		view.setOnClickListener(null);
	}

	@Override
	protected boolean isSuit() {
		return ohic.isHeroSuit();
	}

	@Override
	protected int getAbility() {
		return ohic.getHeroAbility();
	}

}