package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.HeroAbandonConfirmTip;
import com.vikings.sanguo.ui.alert.HeroSkillStudyTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class OwnHeroDetailWindow extends HeroDetailWindow implements
		OnClickListener {
	private HeroInfoClient hic;
	private boolean showAbandonBtn;
	// 将领上阵回调
	private CallBack mCallBack;

	public void open(HeroInfoClient hic, boolean showAbadonBtn,
			CallBack mCallBack) {
		this.hic = hic;
		this.showAbandonBtn = showAbadonBtn;
		this.mCallBack = mCallBack;
		super.doOpen();
	}

	@Override
	protected void init() {
		super.init();
		if (showAbandonBtn) {
			setLeftBtn("分解将领", new OnClickListener() {
				@Override
				public void onClick(View v) {
					new HeroAbandonConfirmTip(hic, new CallBack() {

						@Override
						public void onCall() {
							controller.goBack();
						}
					}).show();
				}
			});
		}
		armPropLayout.setOnClickListener(this);
		strengthBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new HeroStrengthenWindow().open(hic);
			}
		});

		upgradeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new HeroDevourWindow(hic).open();
			}
		});

		evolveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new HeroEvolveWindow().open(hic);
			}
		});

		favourBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new HeroFavourWindow(hic).open();
			}
		});

		// 上阵将领
		if (mCallBack != null) {
			findViewById(R.id.hero_go_battle).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCallBack.onCall();
						}
					});
			ViewUtil.setVisible(findViewById(R.id.heroBelowBtnFrame));
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, (int) (60 * Config.SCALE_FROM_HIGH));
			findViewById(R.id.hero_detail_scrollview).setLayoutParams(params);
		} else {
			ViewUtil.setGone(findViewById(R.id.heroBelowBtnFrame));
		}
	}

	@Override
	protected byte getLeftBtnBgType() {
		if (showAbandonBtn)
			return WINDOW_BTN_BG_TYPE_CLICK;
		return super.getLeftBtnBgType();
	}

	private void setArmPropValue(HeroArmPropInfoClient hapic, View v) {
		ViewUtil.setImage(v, R.id.smallIcon, hapic.getHeroTroopName()
				.getSmallIcon());
		ViewUtil.setText(v, R.id.name, hapic.getArmPropSlug(false));
		ProgressBar bar = (ProgressBar) v.findViewById(R.id.progressBar);
		bar.set(hapic.getValue(), hapic.getMaxValue());
		ViewUtil.setRichText(v, R.id.progressDesc, hapic.getValue() + "/"
				+ hapic.getMaxValue());
		List<TroopProp> troopProps = CacheMgr.troopPropCache
				.getTroopPropByType((byte) hapic.getType());
		int[] equipValues = hic.getEquipmentValue();
		if (troopProps.size() > 0) {
			TroopProp troopProp = troopProps.get(0);
			double attackValue = hapic.attackAddValue(hic.getRealAttack()
					+ equipValues[0], troopProp) * 100;
			double defendValue = hapic.defendAddValue(hic.getRealDefend()
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

	@Override
	protected boolean isMineHero() {
		return true;
	}

	@Override
	protected BaseHeroInfoClient getBaseHeroInfoClient() {
		return hic;
	}

	@Override
	protected void setArmPropInfos() {
		List<HeroArmPropInfoClient> hapics = hic.getArmPropInfos();
		int childCnt = armPropContent.getChildCount();
		for (int i = 0; i < hapics.size() - childCnt; i++) {
			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.hero_armprop_detail);
			armPropContent.addView(viewGroup);
		}

		for (int i = 0; i < hapics.size(); i++) {
			HeroArmPropInfoClient hapic = hapics.get(i);
			View v = armPropContent.getChildAt(i);
			setArmPropValue(hapic, v);
		}
	}

	@Override
	protected void setBtn() {
		ViewUtil.setVisible(btnLayout);
		ViewUtil.setGone(upgradeBtn);
		ViewUtil.setGone(evolveBtn);
		ViewUtil.setGone(favourBtn);
		if (hic.canStrength()) {
			ViewUtil.enableButton(strengthBtn);
		} else {
			ViewUtil.disableButton(strengthBtn);
		}

		if (hic.canFavour()) {
			ViewUtil.setVisible(favourBtn);
		} else if (hic.canEvolve()) {
			ViewUtil.setVisible(evolveBtn);
		} else if (hic.canUpgrade()) {
			ViewUtil.setVisible(upgradeBtn);
			ViewUtil.enableButton(upgradeBtn);
		} else {
			ViewUtil.setVisible(upgradeBtn);
			ViewUtil.disableButton(upgradeBtn);
		}
	}

	@Override
	protected void setSkillInfo(View view, final HeroSkillSlotInfoClient hssic) {
		if (hssic.hasSkill() && null != hssic.getBattleSkill()) {
			ViewUtil.setVisible(view, R.id.name);
			StringBuilder skillName = new StringBuilder(hssic.getBattleSkill()
					.getName());
			int index = skillName.indexOf("L");
			if (index >= 0)
				skillName.insert(index, "<br/>");
			ViewUtil.setRichText(view, R.id.name, skillName.toString());
			ViewUtil.setGone(view, R.id.level);
			ViewUtil.setGone(view, R.id.addDesc);
			new ViewImgScaleCallBack(hssic.getBattleSkill().getIcon(),
					view.findViewById(R.id.icon), Constants.SKILL_ICON_WIDTH
							* Config.SCALE_FROM_HIGH,
					Constants.SKILL_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new HeroSkillStudyTip(hic, hssic).show();

				}
			});
		} else {
			ViewUtil.setGone(view, R.id.name);
			ViewUtil.setGone(view, R.id.level);
			ViewUtil.setVisible(view, R.id.addDesc);
			ViewUtil.setImage(view.findViewById(R.id.icon),
					R.drawable.hero_skill_unit_add);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new HeroSkillListWindow().open(hic, hssic, new CallBack() {

						@Override
						public void onCall() {
							showUI();
						}
					});
				}
			});
		}
	}

	@Override
	protected void setHasEquipment(View view, final EquipmentSlotInfoClient esic) {
		IconUtil.setEquipmentIcon(view, (byte) esic.getType(), esic.getEic(),
				true, isSuit());
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new EquipmentDetailWindow().open(esic.getEic(), hic, true);
			}
		});
	}

	@Override
	protected void setNoEquipment(View view, final byte type) {
		IconUtil.setEquipmentIcon(view, type, null, true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new EquipmentWearWindow().open(hic, type);

			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == armPropLayout) {
			controller.openStrengthenWindow(hic);
		}

	}

	@Override
	protected boolean isSuit() {
		return hic.isHeroSuit();
	}

	@Override
	protected int getAbility() {
		return hic.getHeroAbility();
	}

}