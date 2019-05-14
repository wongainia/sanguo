package com.vikings.sanguo.ui.window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.EquipmentLevelUpResp;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentLevelUp;
import com.vikings.sanguo.model.EquipmentLevelUpData;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.WorldLevelInfoClient;
import com.vikings.sanguo.model.WorldLevelProp;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.EquipmentUpgradeSuccessTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomPopupWindow;

/**
 * 升级装备
 * 
 * @author susong
 * 
 */
public class EquipmentLevelUpWindow extends CustomPopupWindow implements
		OnClickListener {
	private ViewGroup upgradeContent, equipmentLayout, newEquipmentLayout,
			anima_layout;
	private TextView upgradeDesc, curEffect, nextEffect, cost, worldLevelDesc;
	private Button upgradeBtn, oneKeyBtn, qualityBtn;

	private HeroInfoClient hic;
	private EquipmentInfoClient eic;
	private byte type;
	private int costOneLevel;
	private Map<Long, EquipmentLevelUpData> oneLevelmap = new HashMap<Long, EquipmentLevelUpData>();
	private int cost2Top;
	private Map<Long, EquipmentLevelUpData> oneKeymap = new HashMap<Long, EquipmentLevelUpData>();

	public void open(EquipmentInfoClient eic, HeroInfoClient hic) {
		this.eic = eic;
		this.hic = hic;
		this.type = eic.getProp().getType();
		doOpen();
	}

	@Override
	protected void init() {
		super.init("装备升级");
		setContent(R.layout.equipment_level_up);
		anima_layout = (ViewGroup) window.findViewById(R.id.anima_layout);
		upgradeContent = (ViewGroup) window.findViewById(R.id.upgradeContent);
		equipmentLayout = (ViewGroup) window.findViewById(R.id.equipmentLayout);
		newEquipmentLayout = (ViewGroup) window
				.findViewById(R.id.newEquipmentLayout);
		upgradeDesc = (TextView) window.findViewById(R.id.upgradeDesc);
		curEffect = (TextView) window.findViewById(R.id.curEffect);
		nextEffect = (TextView) window.findViewById(R.id.nextEffect);
		cost = (TextView) window.findViewById(R.id.cost);
		worldLevelDesc = (TextView) window.findViewById(R.id.worldLevelDesc);
		upgradeBtn = (Button) window.findViewById(R.id.upgradeBtn);
		upgradeBtn.setOnClickListener(this);
		oneKeyBtn = (Button) window.findViewById(R.id.oneKeyBtn);
		oneKeyBtn.setOnClickListener(this);
		qualityBtn = (Button) window.findViewById(R.id.qualityBtn);
		qualityBtn.setOnClickListener(this);
		upgradeContent.setBackgroundDrawable(ImageUtil.getRotateBitmapDrawable(
				"jianbian_bg", 180));
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	private void setValue() {
		costOneLevel = 0;
		cost2Top = 0;
		oneKeymap.clear();
		oneLevelmap.clear();
		if (null != hic) {
			EquipmentSlotInfoClient esic = hic.getEquipmentSlotInfoClient(type);
			eic = esic.getEic();
		}
		setLeftTxt("#rmb#" + Account.user.getCurrency());
		ViewUtil.setRichText(equipmentLayout.findViewById(R.id.equipmentName),
				eic.getColorName());
		ViewUtil.setRichText(
				equipmentLayout.findViewById(R.id.equipmentQuality),
				eic.getColorTypeName());
		IconUtil.setEquipmentIcon(equipmentLayout, type, eic, false);

		List<EquipmentLevelUp> levelUps = CacheMgr.equipmentLevelUpCache
				.getLevelUp(eic.getProp().getLevelUpScheme(),
						(byte) eic.getQuality(), (byte) eic.getLevel());
		if (levelUps.isEmpty()) {// 不能再升级了
			ViewUtil.setHide(newEquipmentLayout);
			ViewUtil.setRichText(upgradeDesc,
					StringUtil.color("已达满级", R.color.color19));
			ViewUtil.setGone(nextEffect);
			ViewUtil.setGone(cost);
			ViewUtil.setGone(upgradeBtn);
			ViewUtil.setGone(oneKeyBtn);
			ViewUtil.setGone(qualityBtn);
			ViewUtil.setGone(worldLevelDesc);
		} else { // 可以升级
			EquipmentInfoClient next = null;
			if (eic.getLevel() == EquipmentLevelUp.EQUIPMENT_MAX_LEVEL) {
				ViewUtil.setGone(oneKeyBtn);
				ViewUtil.setGone(upgradeBtn);
				ViewUtil.setVisible(qualityBtn);
				next = eic.copy(eic.getQuality() + 1, 1);
				if (null != next) {
					ViewUtil.setText(upgradeDesc, eic.getEquipmentDesc()
							.getName()
							+ "升级为"
							+ next.getEquipmentDesc().getName());
				}
				WorldLevelProp prop = WorldLevelInfoClient.getWorldLevelProp();
				if (eic.getQuality() >= prop.getMaxEquipmentQuality()) {
					ViewUtil.setVisible(worldLevelDesc);
					ViewUtil.setRichText(worldLevelDesc, "世界等级达到"
							+ (WorldLevelInfoClient.worldLevel + 1)
							+ "级，才能升级到更高品质");
					ViewUtil.disableButton(qualityBtn);
				} else {
					ViewUtil.setGone(worldLevelDesc);
					ViewUtil.enableButton(qualityBtn);
				}
			} else {
				ViewUtil.setVisible(oneKeyBtn);
				ViewUtil.setVisible(upgradeBtn);
				ViewUtil.setGone(qualityBtn);
				ViewUtil.setGone(worldLevelDesc);
				next = eic.copy(eic.getQuality(), eic.getLevel() + 1);
				ViewUtil.setText(upgradeDesc, "lv" + eic.getLevel() + "升级为lv"
						+ next.getLevel());
				ViewUtil.enableButton(oneKeyBtn);
				List<EquipmentLevelUp> list = CacheMgr.equipmentLevelUpCache
						.getLevelUp2Top(eic.getProp().getLevelUpScheme(),
								(byte) eic.getQuality(), (byte) eic.getLevel());
				cost2Top = getCurrency(list, oneKeymap);
				if (cost2Top > 0) {
					ViewUtil.setRichText(oneKeyBtn, "一键升满#rmb#" + cost2Top,
							true);
				} else {
					ViewUtil.setRichText(oneKeyBtn, "一键升满", true);
				}
			}

			if (null != next) {
				ViewUtil.setRichText(
						newEquipmentLayout.findViewById(R.id.equipmentName),
						next.getColorName());
				ViewUtil.setRichText(
						newEquipmentLayout.findViewById(R.id.equipmentQuality),
						next.getColorTypeName());
				IconUtil.setEquipmentIcon(newEquipmentLayout, type, next, false);

				EquipmentEffect nextEe = CacheMgr.equipmentEffectCache
						.getEquipmentEffect(next.getEquipmentId(),
								(byte) next.getQuality(), next.getLevel());
				if (null != nextEe)
					ViewUtil.setText(
							nextEffect,
							"升级效果："
									+ nextEe.getEffectDesc(nextEe
											.getEffectValue(next.getLevel())));
			}

			EquipmentEffect ee = CacheMgr.equipmentEffectCache
					.getEquipmentEffect(eic.getEquipmentId(),
							(byte) eic.getQuality(), eic.getLevel());
			if (null != ee)
				ViewUtil.setText(
						curEffect,
						"当前效果："
								+ ee.getEffectDesc(ee.getEffectValue(eic
										.getLevel())));

			costOneLevel = getCurrency(levelUps, oneLevelmap);
			List<ShowItem> showItems = getShowItems(levelUps);
			if (!showItems.isEmpty()) {
				ViewUtil.setVisible(cost);
				StringBuilder buf = new StringBuilder("升级材料：");
				for (ShowItem showItem : showItems) {
					buf.append("#" + showItem.getImg() + "#")
							.append(showItem.getName())
							.append("x")
							.append(showItem.getCount())
							.append(StringUtil.color(
									"(" + showItem.getSelfCount() + ")",
									showItem.getCount() > showItem
											.getSelfCount() ? R.color.color11
											: R.color.color19)).append("<br/>");
				}
				int index = buf.lastIndexOf("<br/>");
				if (index >= 0)
					buf.delete(index, index + "<br/>".length());
				ViewUtil.setRichText(cost, buf.toString());
			} else {
				ViewUtil.setGone(cost);
			}
		}

	}

	private List<ShowItem> getShowItems(List<EquipmentLevelUp> levelUps) {
		ReturnInfoClient ric = new ReturnInfoClient();
		for (EquipmentLevelUp levelUp : levelUps) {
			ric.addCfg(levelUp.getType(), levelUp.getEffect(),
					levelUp.getValue());
		}
		return ric.showRequire();

	}

	// 取一键所需要的元宝，同时整理出缺少的物品
	private int getCurrency(List<EquipmentLevelUp> list,
			Map<Long, EquipmentLevelUpData> map) {
		int currency = 0;
		for (EquipmentLevelUp levelUp : list) {
			long key = CalcUtil
					.buildKey(levelUp.getType(), levelUp.getEffect());
			if (map.containsKey(key)) {
				EquipmentLevelUpData data = map.get(key);
				if (data.getCount() <= 0) {
					currency += CalcUtil.upNum(levelUp.getValue()
							* (1f / EquipmentLevelUp.CURRENCY_UNIT * levelUp
									.getCurrency()));
				} else if (data.getCount() < levelUp.getValue()) {
					currency += CalcUtil.upNum((levelUp.getValue() - data
							.getCount())
							* (1f / EquipmentLevelUp.CURRENCY_UNIT * levelUp
									.getCurrency()));
				}
				data.setCount(data.getCount() - levelUp.getValue());
			} else {
				if (levelUp.getType() == EquipmentLevelUp.TYPE_ITEM) {
					ItemBag itemBag = Account.store.getItemBag(levelUp
							.getEffect());
					if (null == itemBag) {
						Item item = CacheMgr.getItemByID(levelUp.getEffect());
						if (null != item) {
							EquipmentLevelUpData data = new EquipmentLevelUpData();
							data.setType(levelUp.getType());
							data.setId(levelUp.getEffect());
							data.setCount(0 - levelUp.getValue());
							data.setName(item.getName());
							data.setIcon(item.getImage());
							map.put(key, data);
						}
						currency += CalcUtil
								.upNum(levelUp.getValue()
										* (1f / EquipmentLevelUp.CURRENCY_UNIT * levelUp
												.getCurrency()));
					} else {
						EquipmentLevelUpData data = new EquipmentLevelUpData();
						data.setType(levelUp.getType());
						data.setId(levelUp.getEffect());
						data.setCount(itemBag.getCount() - levelUp.getValue());
						data.setName(itemBag.getItem().getName());
						data.setIcon(itemBag.getItem().getImage());
						map.put(key, data);
						if (itemBag.getCount() < levelUp.getValue()) {
							currency += CalcUtil
									.upNum((levelUp.getValue() - itemBag
											.getCount())
											* (1f / EquipmentLevelUp.CURRENCY_UNIT * levelUp
													.getCurrency()));
						}
					}
				} else if (levelUp.getType() == EquipmentLevelUp.TYPE_ATTR) {
					String name = ReturnInfoClient.getAttrTypeName(levelUp
							.getEffect());
					String icon = ReturnInfoClient.getAttrTypeIconName(levelUp
							.getEffect());
					int count = Account.user.getAttr(AttrType.valueOf(levelUp
							.getEffect()));
					EquipmentLevelUpData data = new EquipmentLevelUpData();
					data.setType(levelUp.getType());
					data.setId(levelUp.getEffect());
					data.setCount(count - levelUp.getValue());
					data.setName(name);
					data.setIcon(icon);
					map.put(key, data);
					if (count < levelUp.getValue()) {
						currency += CalcUtil
								.upNum((levelUp.getValue() - count)
										* (1f / EquipmentLevelUp.CURRENCY_UNIT * levelUp
												.getCurrency()));
					}

				}
			}
		}
		return currency;
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	public void onClick(View v) {
		if (v == upgradeBtn || v == qualityBtn) {
			if (costOneLevel > 0) {
				final View view = v;
				controller.confirm(
						"材料不足",
						CustomConfirmDialog.DEFAULT,
						getDesc(oneLevelmap),
						"是否花费#rmb#"
								+ StringUtil.color("" + costOneLevel,
										R.color.color11) + "补足材料立刻升级?",
						new CallBack() {

							@Override
							public void onCall() {
								if (Account.user.getCurrency() < costOneLevel) {
									new ToActionTip(costOneLevel).show();
									return;
								}
								new EquipmentLevelUpInvoker(2,
										view == upgradeBtn ? 2 : 1).start();
							}
						}, null);
			} else {
				new EquipmentLevelUpInvoker(1, v == upgradeBtn ? 2 : 1).start();
			}
		} else if (v == oneKeyBtn) {
			if (cost2Top > 0) {
				controller.confirm(
						"材料不足",
						CustomConfirmDialog.DEFAULT,
						getDesc(oneKeymap),
						"是否花费#rmb#"
								+ StringUtil.color("" + cost2Top,
										R.color.color11) + "补足材料立刻升级?",
						new CallBack() {

							@Override
							public void onCall() {
								if (Account.user.getCurrency() < cost2Top) {
									new ToActionTip(cost2Top).show();
									return;
								}
								new EquipmentLevelUpInvoker(3).start();

							}
						}, null);
			} else {
				new EquipmentLevelUpInvoker(3).start();
			}
		}
	}

	private String getDesc(Map<Long, EquipmentLevelUpData> map) {
		StringBuilder buf = new StringBuilder();
		for (EquipmentLevelUpData data : map.values()) {
			if (data.getCount() < 0)
				buf.append("&lt;").append(data.getName()).append("x")
						.append(StringUtil.abs(data.getCount()))
						.append("&gt;, ");
		}

		int lastIndex = buf.lastIndexOf(",");
		if (lastIndex != -1)
			buf.delete(lastIndex, buf.length());
		return "目前缺少材料：" + StringUtil.color(buf.toString(), R.color.color11);
	}

	private class EquipmentLevelUpInvoker extends BaseInvoker {
		private int type;
		private int levelUpType = 2;// 1品质升级，2普通升级
		private EquipmentLevelUpResp resp;
		private int valueBefore;
		private EquipmentEffect effectBefore;
		private int valueAfter;
		private EquipmentEffect effectAfter;

		public EquipmentLevelUpInvoker(int type) {
			this.type = type;
		}

		public EquipmentLevelUpInvoker(int type, int levelUpType) {
			this.type = type;
			this.levelUpType = levelUpType;
		}

		@Override
		protected String loadingMsg() {
			return "升级装备";
		}

		@Override
		protected String failMsg() {
			return "升级装备失败";
		}

		@Override
		protected void fire() throws GameException {
			effectBefore = CacheMgr.equipmentEffectCache.getEquipmentEffect(
					eic.getEquipmentId(), (byte) eic.getQuality(),
					eic.getLevel());
			if (null != effectBefore)
				valueBefore = effectBefore.getEffectValue(eic.getLevel());
			resp = GameBiz.getInstance().equipmentLevelUp(eic.getId(),
					hic == null ? 0 : hic.getId(), type);
			if (null != resp.getEic()) {
				eic.update(resp.getEic());
			} else if (null != resp.getHic()) {
				eic.update(resp.getHic().getEquipmentInfoClient(eic.getId()));
			}
			effectAfter = CacheMgr.equipmentEffectCache.getEquipmentEffect(
					eic.getEquipmentId(), (byte) eic.getQuality(),
					eic.getLevel());
			if (null != effectAfter)
				valueAfter = effectAfter.getEffectValue(eic.getLevel());
		}

		@Override
		protected void onOK() {
			controller.updateUI(resp.getRic(), true);
			String msg = "升级成功";
			if (null != effectAfter)
				msg = msg + "<br/><br/>" + effectAfter.getEffectTypeName()
						+ " " + valueBefore + "→" + valueAfter;
			// controller.alert(msg);
			if (levelUpType == 1/* 品质升级 */) {
				new EquipmentUpgradeSuccessTip(eic, Math.abs(valueAfter))
						.show();
			} else if (levelUpType == 2/* 普通升级 */) {
				startAnimation(Math.abs(valueAfter - valueBefore));
			}
			setValue();
		}
	}

	private void startAnimation(int value) {
		ViewGroup viewGroup = (ViewGroup) controller
				.inflate(R.layout.equipment_upgrade_anim);
		StringUtil.descImg(viewGroup, value, R.drawable.upgrade_success,
				R.drawable.force, R.drawable.add);
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
