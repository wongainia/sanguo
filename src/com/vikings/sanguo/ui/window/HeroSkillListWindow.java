package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.HeroSkillUpgrade;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.protos.RES_DATA_TYPE;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.HeroSkillAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class HeroSkillListWindow extends CustomListViewWindow {

	private HeroInfoClient hic;
	private HeroSkillSlotInfoClient hssic;
	private CallBack mCallBack;

	@Override
	protected void init() {
		super.init("学习技能");
	}

	@Override
	public void showUI() {
		adapter.clear();
		adapter.addItems(getSkills());
		adapter.notifyDataSetChanged();
		super.showUI();
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	/**
	 * @param hic
	 * @param hssic
	 * @param learnedType
	 *            已经学习过的技能type
	 */
	public void open(HeroInfoClient hic, HeroSkillSlotInfoClient hssic) {
		this.hic = hic;
		this.hssic = hssic;
		doOpen();
		dealwithEmptyAdpter();
	}

	/**
	 * @param hic
	 * @param hssic
	 * @param learnedType
	 *            已经学习过的技能type
	 */
	public void open(HeroInfoClient hic, HeroSkillSlotInfoClient hssic,
			CallBack mCallBack) {
		this.hic = hic;
		this.hssic = hssic;
		this.mCallBack = mCallBack;
		doOpen();
		dealwithEmptyAdpter();
	}

	private List<BattleSkill> getSkills() {
		List<BattleSkill> initSkills = CacheMgr.battleSkillCache
				.getHeroSkillLv1();
		List<BattleSkill> skills = new ArrayList<BattleSkill>();
		for (BattleSkill battleSkill : initSkills) { // 未学过，且可以学，且够材料的技能,
			if (!isStudySameType(battleSkill) && isHasMaterial(battleSkill))
				skills.add(battleSkill);
		}

		Collections.sort(skills, new Comparator<BattleSkill>() {
			@Override
			public int compare(BattleSkill skill1, BattleSkill skill2) {
				return skill1.getId() - skill2.getId();
			}
		});
		return skills;
	}

	private boolean isStudySameType(BattleSkill skill) {
		for (HeroSkillSlotInfoClient hssic : hic.getSkillSlotInfos()) {
			if (hssic.getSkillId() > 0) {
				BattleSkill battleSkill = hssic.getBattleSkill();
				if (null != battleSkill
						&& battleSkill.getType() == skill.getType()) {
					return true;
				}
			}

		}
		return false;
	}

	@Override
	protected void destory() {
		controller.removeContentFullScreen(this.window);
	}

	@Override
	protected void bindField() {

	}

	@SuppressWarnings("unchecked")
	public boolean isHasMaterial(BattleSkill skill) {
		if (skill == null)
			return false;
		List<HeroSkillUpgrade> list = CacheMgr.heroSkillUpgradeCache
				.search(skill.getId());
		ReturnInfoClient ric = new ReturnInfoClient();
		for (HeroSkillUpgrade upgrade : list) {
			ric.addCfg(RES_DATA_TYPE.RES_DATA_TYPE_ITEM.getNumber(),
					upgrade.getItemID(), upgrade.getCount());
		}

		List<ShowItem> showItems = ric.checkRequire();
		return showItems.isEmpty();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new HeroSkillAdapter(hic, hssic, mCallBack);
	}

	@Override
	protected String getEmptyShowText() {
		return "目前没有技能书";
	}

	@Override
	protected void dealwithEmptyAdpter() {
		super.dealwithEmptyAdpter();
		if (null != emptyShow) {
			ViewUtil.setVisible(emptyShow, R.id.emptyDesc2);
			ViewUtil.setText(emptyShow, R.id.emptyDesc2, "分解将领可快速获得技能书");
			ViewUtil.setVisible(emptyShow, R.id.btn);
			ViewUtil.setRichText(emptyShow.findViewById(R.id.btn), "去分解将领");
			emptyShow.findViewById(R.id.btn).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							new HeroAbandonListWindow().open();
						}
					});
		}
	}
}
