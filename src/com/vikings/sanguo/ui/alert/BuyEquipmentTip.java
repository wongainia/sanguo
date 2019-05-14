package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.EquipmentBuyResp;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.EquipmentDesc;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentInit;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShopData;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class BuyEquipmentTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_buy_equipment;
	private ViewGroup equipmentLayout, limitLayout, equipmentEffectLayout,
			stoneEffectLayout, suitEffectLayout;
	private TextView type, discountName, oldPrice, price, prop, desc;
	private ImageView rating;
	private EquipmentInit equipmentInit;
	private PropEquipment equipment;
	private EquipmentDesc equipmentDesc;
	private int discount = Constants.NO_DISCOUNT;
	private ShopData shopData;

	public BuyEquipmentTip(ShopData shopData) {
		super(shopData.getEquipment().getName());
		this.shopData = shopData;
		this.equipmentInit = shopData.getEquipmentInit();
		this.equipment = shopData.getEquipment();
		this.equipmentDesc = shopData.getEquipmentDesc();
		equipmentLayout = (ViewGroup) content
				.findViewById(R.id.equipmentLayout);
		limitLayout = (ViewGroup) content.findViewById(R.id.limitLayout);
		equipmentEffectLayout = (ViewGroup) content
				.findViewById(R.id.equipmentEffectLayout);
		stoneEffectLayout = (ViewGroup) content
				.findViewById(R.id.stoneEffectLayout);
		suitEffectLayout = (ViewGroup) content
				.findViewById(R.id.suitEffectLayout);

		type = (TextView) content.findViewById(R.id.type);
		discountName = (TextView) content.findViewById(R.id.discountName);
		oldPrice = (TextView) content.findViewById(R.id.oldPrice);
		price = (TextView) content.findViewById(R.id.price);
		prop = (TextView) content.findViewById(R.id.prop);
		desc = (TextView) content.findViewById(R.id.desc);

		rating = (ImageView) content.findViewById(R.id.rating);

		setDiscount();
		setRefreshInteval();
		updateDynView();

		setButton(FIRST_BTN, "购买", new OnClickListener() {
			@Override
			public void onClick(View v) {
				buy();
			}
		});

		setButton(SECOND_BTN, "关闭", closeL);
	}

	public void setDiscount() {
		if (null == shopData || null == shopData.getItem())
			discount = Constants.NO_DISCOUNT;
		this.discount = shopData.getDiscount();
	}

	protected void setRefreshInteval() {
		if (Constants.NO_DISCOUNT != discount)
			refreshInterval = 1000;
	}

	@Override
	protected void updateDynView() {
		if (null == shopData || null == shopData.getItem())
			return;

		ViewUtil.setGone(limitLayout);

		if (shopData.isLimitBuy()) {
			ViewUtil.setVisible(limitLayout);
			ViewUtil.setVisible(limitLayout, R.id.limitProp);
			ViewUtil.setRichText(
					limitLayout,
					R.id.limitProp,
					StringUtil.color("限量购买" + shopData.getLimitBuyAmount()
							+ "个", R.color.color19));
		}

		if (Constants.NO_DISCOUNT != discount) {
			ViewUtil.setVisible(limitLayout);
			ViewUtil.setVisible(limitLayout, R.id.countDown);
			ViewUtil.setRichText(tip, R.id.countDown,
					shopData.getDiscountCountDown(ShopData.TYPE_EQUIPMENT)
							+ "后结束打折");
		}

		if (shopData.isLimitBuy() && Constants.NO_DISCOUNT != discount) {
			ViewUtil.setVisible(limitLayout, R.id.limitProp);
			ViewUtil.setVisible(limitLayout, R.id.countDown);
			ViewUtil.setRichText(limitLayout, R.id.limitProp,
					"限量购买" + shopData.getLimitBuyAmount() + "个,");

			ViewUtil.setRichText(tip, R.id.countDown,
					shopData.getDiscountCountDown(ShopData.TYPE_EQUIPMENT)
							+ "后结束打折");
		}
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		IconUtil.setEquipmentIcon(equipmentLayout, equipment, equipmentDesc,
				equipmentInit.getInitLevel());

		if (Constants.NO_DISCOUNT != discount) {
			ViewUtil.setVisible(oldPrice);

			ViewUtil.setVisible(discountName);

			ViewUtil.setRichText(oldPrice, equipmentInit.getPreIcon()
					+ equipmentInit.getPrice(), true);

			ViewUtil.setMiddleLine(oldPrice);

			ViewUtil.setRichText(price, equipmentInit.getPreIcon()
					+ getDiscountPrice(1), true);
		} else {
			ViewUtil.setGone(oldPrice);
			ViewUtil.setRichText(price, "单价：" + equipmentInit.getPreIcon()
					+ equipmentInit.getPrice(), true);

		}
		ViewUtil.setImage(rating, equipment.getRatingPic());
		ViewUtil.setText(type, "类型：" + equipment.getTypeName());
		EquipmentEffect ee = CacheMgr.equipmentEffectCache.getEquipmentEffect(
				equipment.getId(), equipmentInit.getInitQuality(),
				equipmentInit.getInitLevel());
		if (null != ee) {
			ViewUtil.setVisible(prop);
			ViewUtil.setRichText(
					prop,
					"属性："
							+ StringUtil.color(
									ee.getEffectTypeName()
											+ "+"
											+ ee.getEffectValue(equipmentInit
													.getInitLevel()),
									R.color.color16));
		} else {
			ViewUtil.setGone(prop);
		}

		ViewUtil.setRichText(equipmentEffectLayout, R.id.effect,
				equipmentInit.getEquipmentEffectDesc());
		ViewUtil.setText(stoneEffectLayout, R.id.effect, "无");

		BattleSkill skill = null;
		if (equipment.isSuitEq()) {
			int skillId = CacheMgr.battleSkillEquipmentCache
					.getSuitBattleSkillId(equipment.getId());
			if (skillId > 0)
				skill = CacheMgr.getBattleSkillById(skillId);
		}
		if (null != skill) {
			ViewUtil.setVisible(suitEffectLayout);
			ViewUtil.setRichText(suitEffectLayout, R.id.effect,
					skill.getEffectDesc());
		} else {
			ViewUtil.setGone(suitEffectLayout);
		}

		ViewUtil.setRichText(desc, equipment.getDesc());
	}

	private int getDiscountPrice(int amount) {
		return equipmentInit.getDiscountPrice(discount) * amount;
	}

	private void buy() {
		int count = 1;
		if (equipmentInit.isEnoughToBuy(discount, count)) {
			new BuyInvoker().start();
		} else {
			if (equipmentInit.isCurrency()) {
				new ToActionTip(getDiscountPrice(count)).show();
				dismiss();
			} else {
				controller.alert("你的金钱不足");
			}
		}
	}

	private class BuyInvoker extends BaseInvoker {

		private EquipmentBuyResp resp;

		@Override
		protected String failMsg() {
			return "购买装备" + equipment.getName() + "失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance()
					.equipmentBuy(equipmentInit.getScheme());
		}

		@Override
		protected String loadingMsg() {
			return "购买装备";
		}

		@Override
		protected void onOK() {
			ReturnInfoClient rs = resp.getRic();
			rs.setMsg("购买成功");
			ctr.updateUI(rs, true);
			SoundMgr.play(R.raw.sfx_buy);
			dismiss();
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
