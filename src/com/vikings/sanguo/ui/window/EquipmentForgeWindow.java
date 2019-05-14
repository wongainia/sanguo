package com.vikings.sanguo.ui.window;

import java.util.List;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.vikings.sanguo.R;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.EquipmentForgeResp;
import com.vikings.sanguo.model.EquipEffectClient;
import com.vikings.sanguo.model.EquipmentForge;
import com.vikings.sanguo.model.EquipmentForgeEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.protos.RES_DATA_TYPE;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.EquipmentForgetSuccessTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

/**
 * 锻造装备
 * 
 * @author susong
 * 
 */
public class EquipmentForgeWindow extends CustomPopupWindow {
	private ViewGroup forgeContent, progressBarLayout, equipmentLayout,
			newEquipmentLayout, checkLayout, stateIconLayout,
			autoCheckedLayout, anima_layout;
	private ProgressBar progressBar;
	private ImageView stateIcon, stateIconSmall, checked, progressRight;
	private TextView progressDesc, stateDesc, curEffect, nextEffect, cost,
			effectTitle;
	private Button forgeBtn;

	private HeroInfoClient hic;
	private EquipmentInfoClient eic;
	private byte type;
	private boolean currencyForge = true;

	private int needCount;
	private int selfCount;

	public void open(EquipmentInfoClient eic, HeroInfoClient hic) {
		this.eic = eic;
		this.hic = hic;
		this.type = eic.getProp().getType();
		doOpen();
	}

	@Override
	protected void init() {
		super.init("强化特效");
		setContent(R.layout.equipment_forge);
		forgeContent = (ViewGroup) window.findViewById(R.id.forgeContent);
		progressBarLayout = (ViewGroup) window
				.findViewById(R.id.progressBarLayout);
		stateIconLayout = (ViewGroup) window.findViewById(R.id.stateIconLayout);
		equipmentLayout = (ViewGroup) window.findViewById(R.id.equipmentLayout);
		newEquipmentLayout = (ViewGroup) window
				.findViewById(R.id.newEquipmentLayout);
		autoCheckedLayout = (ViewGroup) window
				.findViewById(R.id.autoCheckedLayout);
		anima_layout = (ViewGroup) window.findViewById(R.id.anima_layout);
		checkLayout = (ViewGroup) window.findViewById(R.id.checkedLayout);
		checkLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				currencyForge = !currencyForge;
				PrefAccess.setEquipForgeCurrency(currencyForge);
				setChecked();
				setValue();
			}
		});

		progressBar = (ProgressBar) window.findViewById(R.id.progressBar);
		stateIcon = (ImageView) window.findViewById(R.id.stateIcon);
		stateIconSmall = (ImageView) window.findViewById(R.id.stateIconSmall);
		checked = (ImageView) window.findViewById(R.id.checked);
		progressRight = (ImageView) window.findViewById(R.id.progressRight);
		progressRight.setImageDrawable(ImageUtil
				.getMirrorBitmapDrawable("progress_left"));
		progressDesc = (TextView) window.findViewById(R.id.progressDesc);
		stateDesc = (TextView) window.findViewById(R.id.stateDesc);
		curEffect = (TextView) window.findViewById(R.id.curEffect);
		nextEffect = (TextView) window.findViewById(R.id.nextEffect);
		cost = (TextView) window.findViewById(R.id.cost);
		effectTitle = (TextView) window.findViewById(R.id.effectTitle);
		forgeBtn = (Button) window.findViewById(R.id.forgeBtn);

		forgeContent.setBackgroundDrawable(ImageUtil.getRotateBitmapDrawable(
				"jianbian_bg", 180));
	}

	@Override
	public void showUI() {
		needCount = 0;
		selfCount = 0;
		setValue();
		super.showUI();
	}

	private void setValue() {
		if (null != hic) {
			EquipmentSlotInfoClient esic = hic.getEquipmentSlotInfoClient(type);
			eic = esic.getEic();
		}

		setLeftTxt("#rmb#" + Account.user.getCurrency());
		PropEquipment prop = eic.getProp();

		EquipEffectClient eec = eic.getEffect();
		EquipmentForgeEffect forgeEffect = eec.getEffect();

		// 锻造前
		ViewUtil.setRichText(equipmentLayout.findViewById(R.id.equipmentName),
				StringUtil.color(forgeEffect.getEffectName(), eic
						.getEquipmentDesc().getColor()));
		IconUtil.setEquipmentIcon(equipmentLayout, type, eic, false);
		ViewUtil.setRichText(curEffect, "【" + forgeEffect.getEffectName() + "】"
				+ forgeEffect.getEffectDesc());

		final EquipmentForge ef = eic.getNextForgeLevel();
		final EquipmentForgeEffect nextLvEffect = CacheMgr.equipmentForgeEffectCache
				.getNextLevel(forgeEffect);
		if (null != ef && nextLvEffect != null) {
			// 设置进度
			ViewUtil.setVisible(progressBarLayout);
			ViewUtil.setVisible(stateIconLayout);
			ViewUtil.setVisible(stateIconSmall);
			ViewUtil.setVisible(autoCheckedLayout);

			int hotUpTime = eec.getHotUpTime();
			if (hotUpTime > 0) {// 还在趁热打铁中
				progressBar.setImageResource(R.drawable.progress_red_271);
				progressBar.set(eec.getExp(), ef.getMax());
				ViewUtil.setText(progressDesc, eec.getExp() + "/" + ef.getMax());
				ViewUtil.setVisible(stateIcon);
				ViewUtil.setImage(stateIcon, R.drawable.forge_state_hot);

				ViewUtil.setImage(stateIconSmall,
						R.drawable.forge_state_hot_small);
				ViewUtil.setVisible(stateDesc);
				ViewUtil.setText(stateDesc, "系统赠送"
						+ CacheMgr.equipmentCommonConfigCache.getRewardRate()
						+ "%强化值，" + DateUtil.formatSecond(hotUpTime)
						+ "内没有强化到下一级，赠送值将消失");
			} else {
				int exp = eec.getRealExp();

				progressBar.setImageResource(R.drawable.progress_blue_271);
				progressBar.set(exp, ef.getMax());
				ViewUtil.setText(progressDesc, exp + "/" + ef.getMax());
				if (exp > 0) { // 在缓慢冷却
					ViewUtil.setVisible(stateIcon);
					ViewUtil.setImage(stateIcon, R.drawable.forge_state_cool);
					ViewUtil.setVisible(stateIconSmall);
					ViewUtil.setImage(stateIconSmall,
							R.drawable.forge_state_cool_small);
					int coolDownTime = eec.getCoolDownTime();
					ViewUtil.setVisible(stateDesc);
					ViewUtil.setText(stateDesc,
							"当前的强化值会缓慢减少，" + DateUtil.formatHour(coolDownTime)
									+ "内没强化到下一级，则会减至零");
				} else {
					ViewUtil.setGone(stateIcon);
					ViewUtil.setGone(stateDesc);
				}
			}

			ViewUtil.setText(effectTitle, "强化成功后，装备获得以下效果");

			// final BattleSkill nextLvSkill = CacheMgr.battleSkillCache
			// .getNextLevel(battleSkill);
			ViewUtil.setVisible(newEquipmentLayout);
			// 强化后
			ViewUtil.setRichText(newEquipmentLayout
					.findViewById(R.id.equipmentName), StringUtil.color(
					nextLvEffect.getEffectName(), eic.getEquipmentDesc()
							.getColor()));
			IconUtil.setEquipmentIcon(newEquipmentLayout, type, eic, false);

			ViewUtil.setRichText(nextEffect, "【" + nextLvEffect.getEffectName()
					+ "】" + nextLvEffect.getEffectDesc());

			if (ef.getItemId() > 0 && ef.getCount() > 0) {
				ShowItem showItem = getShowItem(ef);
				if (null != showItem) {
					ViewUtil.setVisible(window, R.id.costLayout);
					StringBuilder buf = new StringBuilder("#"
							+ showItem.getImg() + "#" + showItem.getName()
							+ " x" + showItem.getCount());
					if (showItem.isEnough()) {
						buf.append(StringUtil.color(
								" (" + showItem.getSelfCount() + ")",
								R.color.color19));
					} else {
						buf.append(StringUtil.color(
								" (" + showItem.getSelfCount() + ")",
								R.color.color11));
					}
					needCount = showItem.getCount();
					selfCount = showItem.getSelfCount();
					ViewUtil.setRichText(cost, buf.toString());
				}
			} else {
				ViewUtil.setGone(window, R.id.costLayout);
			}

			currencyForge = PrefAccess.getEquipForgeCurrency();
			setChecked();

			forgeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if ((needCount > selfCount)
							&& Account.user.getCurrency() < ef.getPrice()
									* (needCount - selfCount)) {
						new ToActionTip(
								(ef.getPrice() * (needCount - selfCount)))
								.show();
						return;
					}
					new EquipmentForgeInvoker(nextLvEffect).start();
				}
			});

			ViewUtil.setText(forgeBtn, "开始强化");

			if (needCount > selfCount) {
				if (currencyForge) {
					ViewUtil.enableButton(forgeBtn);
					ViewUtil.setRichText(forgeBtn,
							"开始强化 #rmb#"
									+ (ef.getPrice() * (needCount - selfCount)));
				} else {
					ViewUtil.disableButton(forgeBtn);
				}
			} else {
				ViewUtil.enableButton(forgeBtn);
			}

		} else {
			ViewUtil.setHide(progressBarLayout);
			ViewUtil.setHide(stateIcon);
			ViewUtil.setGone(stateIconLayout);
			ViewUtil.setHide(stateIconSmall);
			ViewUtil.setHide(stateDesc);
			ViewUtil.setGone(newEquipmentLayout);
			ViewUtil.setText(effectTitle, "该装备特效已强化到最高等级");
			ViewUtil.setGone(window, R.id.costLayout);
			ViewUtil.setGone(checked);
			ViewUtil.setGone(forgeBtn);
			ViewUtil.setGone(autoCheckedLayout);
			ViewUtil.setGone(nextEffect);
		}

	}

	@Override
	protected int refreshInterval() {
		return 1000;
	}

	private ShowItem getShowItem(EquipmentForge ef) {
		ReturnInfoClient ric = new ReturnInfoClient();

		ric.addCfg(RES_DATA_TYPE.RES_DATA_TYPE_ITEM.getNumber(),
				ef.getItemId(), ef.getCount());

		List<ShowItem> showItems = ric.showRequire();
		if (null != showItems && !showItems.isEmpty())
			return showItems.get(0);
		else
			return null;
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	private void setChecked() {
		if (currencyForge) {
			ViewUtil.setVisible(checked);
		} else {
			ViewUtil.setGone(checked);
		}
	}

	private class EquipmentForgeInvoker extends BaseInvoker {
		private EquipmentForgeEffect effect;
		private EquipmentForgeResp resp;
		private int beforeValue;
		private int beforeLevel;
		private int afterValue;
		private int afterLevel;

		public EquipmentForgeInvoker(EquipmentForgeEffect effect) {
			this.effect = effect;
		}

		@Override
		protected String loadingMsg() {
			return "強化特效";
		}

		@Override
		protected String failMsg() {
			return "強化装备特效失败";
		}

		@Override
		protected void fire() throws GameException {
			beforeValue = eic.getEffect().getExp();
			beforeLevel = eic.getEffect().getEffect().getLevel();
			resp = GameBiz.getInstance().equipmentForge(eic.getId(),
					eic.getHeroId(), effect.getId(), currencyForge);
			if (null != resp.getEic()) {
				eic.update(resp.getEic());
			} else if (null != resp.getHic()) {
				eic.update(resp.getHic().getEquipmentInfoClient(eic.getId()));
			}

		}

		@Override
		protected void onOK() {
			controller.updateUI(resp.getRic(), true);
			// controller.alert("强化成功");
			setValue();
			afterValue = eic.getEffect().getExp();
			afterLevel = eic.getEffect().getEffect().getLevel();
			if (afterLevel != beforeLevel) {
				new EquipmentForgetSuccessTip(eic).show();
			} else {
				startAnimation(Math.abs(afterValue - beforeValue));
			}

		}
	}

	private void startAnimation(int value) {
		ViewGroup viewGroup = (ViewGroup) controller
				.inflate(R.layout.equipment_upgrade_anim);
		StringUtil.descImg(viewGroup, value, 0, R.drawable.txt_qh_val,
				R.drawable.add);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, (int) (50 * Config.SCALE_FROM_HIGH));
		params.gravity = Gravity.CENTER;
		anima_layout.addView(viewGroup, params);
		Animation animation = getAnimation(viewGroup);
		viewGroup.startAnimation(animation);
	}

	private Animation getAnimation(View view) {
		AnimationSet animationSet = new AnimationSet(false);
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,
				-75 * Config.SCALE_FROM_HIGH, -110 * Config.SCALE_FROM_HIGH);
		translateAnimation.setDuration(300);
		animationSet.addAnimation(translateAnimation);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
		alphaAnimation.setDuration(300);
		alphaAnimation.setStartOffset(600);
		animationSet.addAnimation(alphaAnimation);
		animationSet.setAnimationListener(new AnimaListener(view));
		return animationSet;
	}

	private class AnimaListener implements AnimationListener {

		View v;

		public AnimaListener(View v) {
			this.v = v;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			ViewUtil.setGone(v);
			controller.getHandler().postDelayed(new Runnable() {

				@Override
				public void run() {
					remove();
				}
			}, 1000);
		}

		private void remove() {
			if (anima_layout != null && anima_layout.indexOfChild(v) != -1) {
				v.clearAnimation();
				anima_layout.removeView(v);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}
}
