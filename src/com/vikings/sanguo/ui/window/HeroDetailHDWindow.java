package com.vikings.sanguo.ui.window;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroTroopName;
import com.vikings.sanguo.model.SkillCombo;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgHdCallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.HeroComboSkillTip;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class HeroDetailHDWindow extends CustomPopupWindow {

	private HeroProp heroProp;
	private ViewGroup armPropContent, heroLayout;
	private ViewGroup skillCombinationContent, skillContent;
	private View hdImg;
	private FrameLayout general_bg;
	private boolean isNoSaveRef; // 是否退出时候回收高清大图

	@Override
	protected void init() {
		super.init(heroProp.getName());
		setContent(R.layout.hero_hd_detail);
		armPropContent = (ViewGroup) window.findViewById(R.id.armPropContent);
		skillCombinationContent = (ViewGroup) window
				.findViewById(R.id.skillCombinationContent);
		skillContent = (ViewGroup) window.findViewById(R.id.skillContent);
		heroLayout = (ViewGroup) window.findViewById(R.id.heroLayout);
		hdImg = window.findViewById(R.id.hdImg);
		general_bg = (FrameLayout) window.findViewById(R.id.general_bg);
		ViewUtil.setImage(general_bg, R.drawable.general_btn);
		if (!isNoSaveRef) {
			imageHolder.saveRef(hdImg);
		}
	}

	public void open(HeroProp hp) {
		this.heroProp = hp;
		doOpen();
	}

	public void open(HeroProp hp, boolean isNoSaveRef) {
		this.heroProp = hp;
		this.isNoSaveRef = isNoSaveRef;
		doOpen();
	}

	@Override
	public void showUI() {
		super.showUI();
		setValue();
	}

	public void open(int heroId, boolean isNoSaveRef) {
		try {
			this.heroProp = (HeroProp) CacheMgr.heroPropCache.get(heroId);
			this.isNoSaveRef = isNoSaveRef;
			doOpen();
		} catch (GameException e) {
			Log.e("HeroDetailWindow", "heroId:" + heroId + "not found!  " + e);
			e.printStackTrace();
		}

	}

	private void setValue() {
		heroProp.initValue();
		heroDesc();
		heroSourceSpec();
		heroAttackDefend();
		// 最强标识
		if (heroProp.getStrongestTag() == 1) {
			ViewUtil.setVisible(findViewById(R.id.most_stronger));
		} else {
			ViewUtil.setGone(findViewById(R.id.most_stronger));
		}

		// 将领评定
		ViewUtil.setImage(findViewById(R.id.hero_evaluate),
				heroProp.getRatingPic());

		// 统率
		initArmPropPanel();

		// 组合技能
		setCombSkillInfo();
		// 固有技能
		setSkillInfos();
		ViewUtil.setVisible(findViewById(R.id.progress_layout));

		// if(isNoSaveRef) //16 bitmap
		// {
		// new ViewImgHdCallBack(heroProp.getImg(), heroLayout, hdImg,
		// new SetHDImageCallBack(), new SetHDImageFailCallBack(),true);
		// }
		// else
		{
			new ViewImgHdCallBack(heroProp.getImg(), heroLayout, hdImg,
					new SetHDImageCallBack(), new SetHDImageFailCallBack());
		}
	}

	// 英雄来源
	private void heroSourceSpec() {
		ViewUtil.setRichText(window, R.id.hero_source_spec,
				heroProp.getSourceSpec());
	}

	// 英雄描述
	private void heroDesc() {
		ViewUtil.setRichText(window, R.id.hero_spec, heroProp.getDesc());
	}

	// 初始化英雄攻防指标
	private void heroAttackDefend() {
		// 武力
		ViewUtil.setRichText(
				window,
				R.id.attack,
				"武力:"
						+ StringUtil.color("" + heroProp.getAttack(),
								R.color.color14));
		// 防护
		ViewUtil.setRichText(
				window,
				R.id.defend,
				"防护:"
						+ StringUtil.color("" + heroProp.getDefend(),
								R.color.color14));
	}

	// 初始化英雄兵种统率
	private void initArmPropPanel() {
		if (ListUtil.isNull(heroProp.skillVal)) {
			ViewUtil.setGone(window, R.id.armPropLayout);
			return;
		}
		armPropContent.removeAllViews();
		for (int i = 0; i < heroProp.skillVal.size(); i++) {
			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.hero_armprop_item);

			ViewUtil.setRichText(viewGroup, R.id.name,
					getArmPropIcon(heroProp.heroTroopNames.get(i)) + "统率:");
			ProgressBar bar = (ProgressBar) viewGroup
					.findViewById(R.id.progressBar);
			bar.set(heroProp.skillVal.get(i), heroProp.skillVal.get(i));
			ViewUtil.setRichText(viewGroup, R.id.progressDesc,
					heroProp.skillVal.get(i) + "");
			armPropContent.addView(viewGroup);
		}
	}

	// 组合技能 应军要求 改成下面的展现方式
	protected void setCombSkillInfo() {
		// 此为该英雄 的组合技能
		List<SkillCombo> skillCombos = heroProp.getSkillCombos();
		List<BattleSkill> mBattleSkills = BaseHeroInfoClient
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
			StringBuilder skillName = new StringBuilder(mBattleSkills.get(i)
					.getName());
			int index = skillName.indexOf("L");
			if (index >= 0)
				skillName.insert(index, "<br/>");
			ViewUtil.setRichText(viewGroup, R.id.name, skillName.toString());
			ViewUtil.setGone(viewGroup, R.id.level);
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

	private void setSkillInfos() {
		BattleSkill staticSkill = null;
		try {
			staticSkill = (BattleSkill) CacheMgr.battleSkillCache.get(heroProp
					.getStaticSkillId());
		} catch (GameException e) {
			e.printStackTrace();
			Log.e("battleskill", heroProp.getStaticSkillId() + "not found!");
		}
		if (staticSkill == null) {
			ViewUtil.setGone(findViewById(R.id.skillLayout));
			return;
		}
		skillContent.removeAllViews();
		ViewGroup viewGroup = (ViewGroup) controller.inflate(
				R.layout.hero_skill_unit, skillContent, false);
		ViewUtil.setVisible(viewGroup, R.id.name);
		StringBuilder skillName = new StringBuilder(staticSkill.getName());
		int index = skillName.indexOf("L");
		if (index >= 0)
			skillName.insert(index, "<br/>");
		ViewUtil.setRichText(viewGroup, R.id.name, skillName.toString());
		ViewUtil.setGone(viewGroup, R.id.level);
		ViewUtil.setGone(viewGroup, R.id.addDesc);
		new ViewImgScaleCallBack(staticSkill.getIcon(),
				viewGroup.findViewById(R.id.icon), Constants.SKILL_ICON_WIDTH
						* Config.SCALE_FROM_HIGH, Constants.SKILL_ICON_HEIGHT
						* Config.SCALE_FROM_HIGH);
		viewGroup.setTag(staticSkill);
		viewGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new HeroComboSkillTip((BattleSkill) v.getTag()).show();
			}
		});
		skillContent.addView(viewGroup);
	}

	// 旧的组合技能展现
	// private void initSkillCommboPanel() {
	// if (ListUtil.isNull(heroProp.skillCombos)) {
	// ViewUtil.setGone(window, R.id.fit_skill);
	// return;
	// }
	// heroSkillComonContent.removeAllViews();
	// // 初始化 组合技能
	// for (SkillCombo sk : heroProp.skillCombos) {
	// ViewGroup viewGroup = (ViewGroup) controller
	// .inflate(R.layout.hero_skillcomon_item);
	//
	// ViewUtil.setRichText(viewGroup.findViewById(R.id.heros_name),
	// heroProp.getComboHeroNamesBySkillCombo(sk));
	//
	// ViewUtil.setRichText(viewGroup.findViewById(R.id.heros_desc),
	// heroProp.getComboSkillDesc(sk));
	// heroSkillComonContent.addView(viewGroup);
	// }
	// }

	private String getArmPropIcon(HeroTroopName hTroopName) {
		StringBuilder buf = new StringBuilder();
		if (hTroopName.getType() == 0) {
			buf.append(hTroopName.getSlug()).append("兵");
		} else {
			buf.append("#").append(hTroopName.getSmallIcon()).append("#");
			buf.append(hTroopName.getSlug()).append("兵");
		}

		return buf.toString();
	}

	public class SetHDImageFailCallBack implements CallBack {
		@Override
		public void onCall() {
			// vip0 不可以看高清大图
			if (Account.user.getCurVip().getLevel() == 0) {
				findViewById(R.id.open_vip).setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Config.getController().openVipListWindow();
							}
						});

				ViewUtil.setVisible(window, R.id.open_vip_layout);
			} else {
				ViewUtil.setGone(window, R.id.open_vip_layout);
			}
			// 隐藏高清图片加载框
			// ViewUtil.setGone(findViewById(R.id.progress_layout));
			ViewUtil.setRichText(findViewById(R.id.loding_hd), "加载失败！");

		}
	}

	public class SetHDImageCallBack implements CallBack {
		@Override
		public void onCall() {
			// vip0 不可以看高清大图
			if (Account.user.getCurVip().getLevel() == 0) {
				findViewById(R.id.open_vip).setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Config.getController().openVipListWindow();
							}
						});
				// 黑色轮廓
				ImageUtil.getMaskBitmap(heroProp.getImg());
				ViewUtil.setImage(findViewById(R.id.hdImg),
						ImageUtil.getMaskName(heroProp.getImg()));

				ViewUtil.setVisible(window, R.id.open_vip_layout);
			} else {

				ViewUtil.setGone(window, R.id.open_vip_layout);
			}

			// 隐藏高清图片加载框
			ViewUtil.setGone(findViewById(R.id.progress_layout));
		}
	}
}
