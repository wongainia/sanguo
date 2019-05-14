package com.vikings.sanguo.ui.alert;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShopData;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class BuyTip extends CustomConfirmDialog implements TextWatcher {
	private static final int layout = R.layout.alert_buy;
	private EditText amount;
	private Item item;
	private int discount = Constants.NO_DISCOUNT;
	private ShopData shopData;
	private int price;

	public BuyTip(ShopData shopData) {
		super(shopData.getItem().getName());
		this.shopData = shopData;
		this.item = shopData.getItem();

		amount = (EditText) content.findViewById(R.id.amount);
		amount.addTextChangedListener(this);

		setButton(FIRST_BTN, "确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				buy();
			}
		});

		setButton(SECOND_BTN, "关闭", closeL);

		setDiscount();
		setRefreshInteval();
		updateDynView();

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
		if (shopData.isLimitBuy()) {
			ViewUtil.setVisible(tip, R.id.limitProp);
			ViewUtil.setRichText(
					tip,
					R.id.limitProp,
					StringUtil.color("限量购买" + shopData.getLimitBuyAmount()
							+ "个", R.color.color19));
		}
		if (Constants.NO_DISCOUNT != discount) {
			if (ViewUtil.isGone(tip.findViewById(R.id.countDown)))
				ViewUtil.setVisible(tip, R.id.countDown);
			ViewUtil.setRichText(tip, R.id.countDown,
					shopData.getDiscountCountDown(ShopData.TYPE_TOOL) + "后结束打折");
		}
		if (shopData.isLimitBuy() && Constants.NO_DISCOUNT != discount) {
			ViewUtil.setVisible(tip, R.id.limitProp);
			ViewUtil.setVisible(tip, R.id.countDown);
			ViewUtil.setRichText(tip, R.id.limitProp,
					"限量购买" + shopData.getLimitBuyAmount() + "个,  ");

			ViewUtil.setRichText(tip, R.id.countDown,
					shopData.getDiscountCountDown(ShopData.TYPE_TOOL) + "后结束打折");
		}
	}

	public void show() {
		super.show();
		setValue();
	}

	private void setValue() {
		new ViewImgScaleCallBack(item.getImage(),
				content.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
						* Constants.ICON_WIDTH, Config.SCALE_FROM_HIGH
						* Constants.ICON_HEIGHT);
		if (Constants.NO_DISCOUNT != discount) {
			ViewUtil.setVisible(content, R.id.oldPrice);
			ViewUtil.setVisible(content, R.id.discountName);

			ViewUtil.setRichText(content, R.id.oldPrice, item.getPreIcon()
					+ item.getPrice(), true);
			ViewUtil.setMiddleLine((TextView) findViewById(R.id.oldPrice));
			price = item.getDiscountPrice(discount);
			ViewUtil.setRichText(content, R.id.price, " " + item.getPreIcon()
					+ price, true);
		} else {
			ViewUtil.setGone(content, R.id.oldPrice);
			price = item.getPrice();
			ViewUtil.setRichText(content, R.id.price, "单价:" + item.getPreIcon()
					+ item.getPrice(), true);
		}

		ViewUtil.setText(content, R.id.type, "类型:" + item.getTypeName());
		ViewUtil.setRichText(content, R.id.desc, item.getDesc());
		ViewUtil.setEditText(amount, "1");
		setCost(1);
	}

	private int getCost(int amount) {
		return price * amount;
	}

	private void setCost(int amount) {
		ViewUtil.setRichText(content, R.id.cost, item.getPreIcon()
				+ getCost(amount));
	}

	private int getAmount() {
		return StringUtil.parseInt(amount.getText().toString());
	}

	private void buy() {
		int count = getAmount();
		if (count <= 0) {
			controller.alert("请输入购买数量");
			return;
		}
		if (item.isEnoughToBuy(discount, count)) {
			new BuyInvoker().start();
		} else {
			if (item.isCurrencyItem()) {
				new ToActionTip(getCost(count)).show();// 跳转到充值购买资源界面
				dismiss();
			} else {
				controller.alert("你的金钱不足");
			}
		}
	}

	private int getMaxAmount(Item item) {
		int total = 0;
		if (item.isCurrencyItem())
			total = Account.user.getCurrency(); // / item.getFunds();
		else
			total = Account.user.getMoney(); // / item.getMoney();
		if (price > 0) {
			int amount = total / price;
			return (0 == amount) ? 1 : amount;
		} else {
			return 1;
		}
	}

	private class BuyInvoker extends BaseInvoker {

		private ReturnInfoClient rs;

		@Override
		protected String failMsg() {
			return "购买物品" + item.getName() + "失败";
		}

		@Override
		protected void fire() throws GameException {
			int amount = getAmount();
			if (item.isCurrencyItem())
				rs = GameBiz.getInstance().itemBuy(item.getId(), amount,
						getCost(amount));// 传给协议最后一个参数是元宝，不消化元宝则传0
			else
				rs = GameBiz.getInstance().itemBuy(item.getId(), amount, 0);
		}

		@Override
		protected String loadingMsg() {
			return "购买物品-" + item.getName();
		}

		@Override
		protected void onOK() {
			if (rs.getItemPack() == null || rs.getItemPack().size() == 0) {
				controller.alert("购买物品错误，请联系客服");
				return;
			}
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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String str = amount.getText().toString().trim();

		if (str.length() == 0) {
			setCost(0);
		} else {
			int cnt = Integer.valueOf(str);
			if (!item.isEnoughToBuy(discount, cnt) && cnt != 1) {
				ViewUtil.setEditText(amount, String.valueOf(getMaxAmount(item)));
				setCost(getMaxAmount(item));
			} else {
				setCost(cnt);
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}
