package com.vikings.sanguo.ui.window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.HeroEnhanceResp;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroEnhance;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewRichTextCallBack;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class HeroStrengthenWindow extends CustomPopupWindow {

	private Button exploitBtn, toolBtn, currencyBtn, oneKeyBtn;
	private HeroInfoClient hic;
	private Map<Integer, Integer> armPropValues = new HashMap<Integer, Integer>();
	private int oldAttack, oldDefend;

	public void open(HeroInfoClient hic) {
		this.hic = hic;
		doOpen();
	}

	private void updateArmPropValues(HeroInfoClient hic) {
		armPropValues.clear();
		for (HeroArmPropInfoClient hapic : hic.getArmPropInfos()) {
			armPropValues.put(hapic.getType(), hapic.getValue());
		}
	}

	@Override
	protected void init() {
		super.init("将领强化");
		setContentBelowTitle(R.layout.strengthen);
		exploitBtn = (Button) window.findViewById(R.id.exploitBtn);
		toolBtn = (Button) window.findViewById(R.id.toolBtn);
		currencyBtn = (Button) window.findViewById(R.id.currencyBtn);
		oneKeyBtn = (Button) window.findViewById(R.id.oneKeyBtn);
		setBaseHeroInfo(hic);
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	private void setBaseHeroInfo(HeroInfoClient hic) {
		IconUtil.setHeroIconScale(findViewById(R.id.iconLayout), hic);
		ViewUtil.setRichText(window, R.id.talentName, hic.getColorTypeName());
		ViewUtil.setRichText(window, R.id.name, hic.getColorHeroName());
		ViewUtil.setText(window, R.id.level, "LV" + hic.getLevel());
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	private void setValue() {
		setAttr();
		setArmProp();
		setEffect();
		setBtns();
	}

	private void setEffect() {
		List<HeroArmPropInfoClient> hapics = hic.getArmPropInfos();
		ViewGroup heroArmprops = (ViewGroup) findViewById(R.id.effectLayout);
		int childCnt = heroArmprops.getChildCount();
		for (int i = 0; i < hapics.size() - childCnt; i++) {
			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.hero_armprop_effect);
			heroArmprops.addView(viewGroup);
		}

		for (int i = 0; i < hapics.size(); i++) {
			HeroArmPropInfoClient hapic = hapics.get(i);
			View v = heroArmprops.getChildAt(i);
			setEffectValue(hapic, v);
		}
	}

	private void setEffectValue(HeroArmPropInfoClient hapic, View v) {
		ViewUtil.setImage(v, R.id.smallIcon, hapic.getHeroTroopName()
				.getSmallIcon());
		ViewUtil.setText(v, R.id.name, hapic.getArmPropSlug(false) + "加成:");
		List<TroopProp> troopProps = CacheMgr.troopPropCache
				.getTroopPropByType((byte) hapic.getType());
		if (troopProps.size() > 0) {
			TroopProp troopProp = troopProps.get(0);
			int oldAttackValue = -1;
			int oldDefendValue = -1;
			if (!armPropValues.isEmpty()
					&& armPropValues.containsKey(hapic.getType())) {
				int value = armPropValues.get(hapic.getType());
				oldAttackValue = (int) (HeroArmPropInfoClient.attackAddValue(
						oldAttack, troopProp, value) * 100);
				oldDefendValue = (int) (HeroArmPropInfoClient.defendAddValue(
						oldDefend, troopProp, value) * 100);
			}

			int attackValue = (int) (hapic.attackAddValue(hic.getRealAttack(),
					troopProp) * 100);
			int defendValue = (int) (hapic.defendAddValue(hic.getRealDefend(),
					troopProp) * 100);
			if (oldAttackValue >= 0 && oldAttackValue != attackValue) {
				int add = attackValue - oldAttackValue;
				if (add >= 0) {
					ViewUtil.setRichText(
							v.findViewById(R.id.attack),
							"攻+"

									+ attackValue
									+ "%"
									+ StringUtil.color("(+" + add + "%)",
											R.color.color19));
				} else {
					ViewUtil.setRichText(
							v.findViewById(R.id.attack),
							"攻+"

									+ attackValue
									+ "%"
									+ StringUtil.color("(" + add + "%)",
											R.color.color11));
				}

			} else {
				ViewUtil.setRichText(v.findViewById(R.id.attack), "攻+"
						+ attackValue + "%");
			}
			if (oldDefendValue >= 0 && oldDefendValue != defendValue) {
				int add = defendValue - oldDefendValue;
				if (add >= 0) {
					ViewUtil.setRichText(
							v.findViewById(R.id.defend),
							"防+"

									+ defendValue
									+ "%"
									+ StringUtil.color("(+" + add + "%)",
											R.color.color19));
				} else {
					ViewUtil.setRichText(
							v.findViewById(R.id.defend),
							"防+"
									+ defendValue
									+ "%"
									+ StringUtil.color("(" + add + "%)",
											R.color.color11));
				}

			} else {
				ViewUtil.setRichText(v.findViewById(R.id.defend), "防+"
						+ defendValue + "%");
			}

		}
	}

	private void setBtns() {
		if (hic.canStrength()) {
			ViewUtil.enableButton(toolBtn);
			ViewUtil.enableButton(currencyBtn);
			ViewUtil.enableButton(oneKeyBtn);

			// 常规强化

			exploitBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					new EnhanceInvoker(HeroEnhance.TYPE_BY_NORMAL).start();
				}
			});

			int[] values = hic.getAverageArmProp();
			HeroEnhance heroEnhance = CacheMgr.heroEnhanceCache
					.getNormal(values[0]);
			setLeftTxt("#money#"
					+ CalcUtil.turnToHundredThousand(Account.user.getMoney()));
			if (null == heroEnhance) {
				ViewUtil.setRichText(exploitBtn, "常规强化");
				ViewUtil.disableButton(exploitBtn);
			} else {
				ViewUtil.enableButton(exploitBtn);
				int exploitCost = heroEnhance.getCost() * values[1];
				if (heroEnhance.getCostType() == AttrType.ATTR_TYPE_EXPLOIT
						.getNumber()) {
					setLeftTxt("#exploit#"
							+ CalcUtil.turnToHundredThousand(Account.user
									.getExploit()));
				}
				ViewUtil.setRichText(
						exploitBtn,
						ReturnInfoClient.getAttrTypeName(heroEnhance
								.getCostType())
								+ "强化 #"
								+ ReturnInfoClient
										.getAttrTypeIconName(heroEnhance
												.getCostType())
								+ "#-"
								+ CalcUtil.turnToHundredThousand(exploitCost),
						true);
			}

			// 道具强化
			int itemId = CacheMgr.dictCache.getDictInt(Dict.TYPE_HERO_ENHANCE,
					1);
			Item item = CacheMgr.getItemByID(itemId);
			new ViewRichTextCallBack(item.getImage(), "stub.png", "道具强化 ",
					" -1", toolBtn, 32, 32);
			toolBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new EnhanceInvoker(HeroEnhance.TYPE_BY_ITEM).start();
				}
			});

			// 元宝强化
			final int currencyCost = CacheMgr.heroEnhanceCache
					.getCurrencyCost(hic.getArmPropInfos());
			ViewUtil.setRichText(
					currencyBtn,
					"元宝强化 #rmb# -"
							+ CalcUtil.turnToHundredThousand(currencyCost),
					true);
			currencyBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Account.user.getCurrency() < currencyCost) {
						new ToActionTip(currencyCost).show();
						return;
					}
					new EnhanceInvoker(HeroEnhance.TYPE_BY_CURRENCY).start();
				}
			});

			// 道具强化
			final int value2Full = hic.getArmPropValue2Full();
			final int oneKeyCost = CacheMgr.heroEnhanceCache.getOneKeyCost(hic
					.getArmPropInfos());
			ViewUtil.setRichText(
					oneKeyBtn,
					"至尊强化 #rmb# -" + CalcUtil.turnToHundredThousand(oneKeyCost),
					true);
			oneKeyBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					controller.confirm(
							"至尊强化",
							CustomConfirmDialog.DEFAULT,
							"将领离强化满还差"
									+ StringUtil.color("" + value2Full,
											R.color.color11) + "的统率值<br/>花费 "
									+ oneKeyCost + " 元宝让将领统率值强化到满", "",
							new CallBack() {

								@Override
								public void onCall() {
									if (Account.user.getCurrency() < oneKeyCost) {
										new ToActionTip(oneKeyCost).show();
										return;
									}
									new EnhanceInvoker(
											HeroEnhance.TYPE_BY_ONE_KEY)
											.start();
								}
							}, null);

				}
			});
		} else {
			ViewUtil.setText(exploitBtn, "常规强化");
			ViewUtil.disableButton(exploitBtn);
			ViewUtil.setText(toolBtn, "道具强化");
			ViewUtil.disableButton(toolBtn);
			ViewUtil.setText(currencyBtn, "元宝强化");
			ViewUtil.disableButton(currencyBtn);
			ViewUtil.setText(oneKeyBtn, "至尊强化");
			ViewUtil.disableButton(oneKeyBtn);
		}
	}

	private void setAttr() {
		setRightTxt("#rmb#" + Account.user.getCurrency());
	}

	private void setArmProp() {
		List<HeroArmPropInfoClient> hapics = hic.getArmPropInfos();
		ViewGroup heroArmprops = (ViewGroup) findViewById(R.id.armPropLayout);
		int childCnt = heroArmprops.getChildCount();
		for (int i = 0; i < hapics.size() - childCnt; i++) {
			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.hero_armprop);
			heroArmprops.addView(viewGroup);
		}

		for (int i = 0; i < hapics.size(); i++) {
			HeroArmPropInfoClient hapic = hapics.get(i);
			View v = heroArmprops.getChildAt(i);
			setArmPropValue(hapic, v);
		}
	}

	private void setArmPropValue(HeroArmPropInfoClient hapic, View v) {
		ViewUtil.setImage(v, R.id.smallIcon, hapic.getHeroTroopName()
				.getSmallIcon());
		ViewUtil.setText(v, R.id.name, hapic.getArmPropSlug(false) + "统率:");
		ProgressBar bar = (ProgressBar) v.findViewById(R.id.progressBar);
		bar.set(hapic.getValue(), hapic.getMaxValue());

		ViewUtil.setRichText(v, R.id.progressDesc, hapic.getValue() + "/"
				+ hapic.getMaxValue());
		if (!armPropValues.isEmpty()
				&& armPropValues.containsKey(hapic.getType())) {
			ViewUtil.setVisible(v, R.id.addDesc);
			int value = armPropValues.get(hapic.getType());
			int add = hapic.getValue() - value;
			if (add > 0)
				ViewUtil.setRichText(v, R.id.addDesc,
						StringUtil.color("+" + add, R.color.color19));
			else if (add < 0)
				ViewUtil.setRichText(v, R.id.addDesc,
						StringUtil.color("" + add, R.color.color11));
			else
				ViewUtil.setText(v, R.id.addDesc, "");

		} else {
			ViewUtil.setGone(v, R.id.addDesc);
		}
		ViewUtil.setText(v, R.id.extendValue, "");
	}

	private class EnhanceInvoker extends BaseInvoker {

		private byte type;
		private HeroEnhanceResp resp;

		public EnhanceInvoker(byte type) {
			this.type = type;
		}

		@Override
		protected String loadingMsg() {
			return "将领强化";
		}

		@Override
		protected String failMsg() {
			return "强化失败";
		}

		@Override
		protected void onFail(GameException exception) {
			String msg = null;
			if (exception.getResult() == 49/* 道具不足 */) {
				msg = "强化失败<br/><br/>没有足够的[强化丸]<br/><br/>"
						+ StringUtil.color("分解单熟练在2w以上的将领，可获得[强化丸]", "#E1AD22");

			}
			Config.getController().alert("", msg, null, false);
		}

		@Override
		protected void fire() throws GameException {
			HeroInfoClient copy = hic.copy();
			resp = GameBiz.getInstance().heroEnhance(hic.getId(), type);
			updateArmPropValues(copy);
			oldAttack = copy.getRealAttack();
			oldDefend = copy.getRealDefend();
			hic.update(resp.getInfo());
		}

		@Override
		protected void onOK() {
			setValue();
			ctr.updateUI(resp.getRi(), true);
		}
	}
}
