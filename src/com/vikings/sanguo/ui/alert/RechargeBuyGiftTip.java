package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.pay.VKConstants;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.BuyAndOpenGiftInvoker;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.utils.PayUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class RechargeBuyGiftTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_recharge;

	public static final int GIFT_TYPE_MONEY = 1;
	public static final int GIFT_TYPE_RES = 2;

	private TextView desc, ext;
	private Button rechargeBtn;

	private int type;
	private int amount;
	private Item item;
	private String descStr, extStr;

	public RechargeBuyGiftTip(int resType) {
		super("友情提示", DEFAULT);
		setRightTopCloseBtn();
		this.type = getType(resType);
		desc = (TextView) content.findViewById(R.id.desc);
		ext = (TextView) content.findViewById(R.id.ext);
		rechargeBtn = (Button) content.findViewById(R.id.rechargeBtn);
		if (type == GIFT_TYPE_MONEY)
			item = CacheMgr.getItemByID(CacheMgr.dictCache.getDictInt(
					Dict.TYPE_ITME_ID, 3));
		else if (type == GIFT_TYPE_RES)
			item = CacheMgr.getItemByID(CacheMgr.dictCache.getDictInt(
					Dict.TYPE_ITME_ID, 4));
		if (null != item)
			this.amount = item.getFunds();
		initString();
	}

	private void initString() {
		this.descStr = controller
				.getStringById(R.string.RechargeBuyGiftTip_desc);
		if (type == GIFT_TYPE_MONEY) {
			String str = CacheMgr.uiTextCache.getTxt(UITextProp.GIFT_TIP_MONEY);
			if (Account.user.getCurrency() >= amount) {
				this.extStr = str.replace("<param0>", amount + "元宝");
			} else {
				this.extStr = str.replace("<param0>", "充值"
						+ (this.amount / Constants.CENT) + "元");
			}

		} else if (type == GIFT_TYPE_RES) {
			String str = CacheMgr.uiTextCache.getTxt(UITextProp.GIFT_TIP_RES);
			if (Account.user.getCurrency() >= amount) {
				this.extStr = str.replace("<param0>", amount + "元宝");
			} else {
				this.extStr = str.replace("<param0>", "充值"
						+ (this.amount / Constants.CENT) + "元");
			}
		}
	}

	public static int getType(int resType) {
		if (resType == AttrType.ATTR_TYPE_MONEY.getNumber()
				|| resType == AttrType.ATTR_TYPE_FOOD.getNumber())
			return GIFT_TYPE_MONEY;
		else
			return GIFT_TYPE_RES;
	}

	public void show() {
		if (item == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		ViewUtil.setRichText(desc, descStr);
		ViewUtil.setRichText(ext, extStr);
		if (Account.user.getCurrency() >= amount) {
			ViewUtil.setText(rechargeBtn, "立刻购买礼包");
			rechargeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					new BuyAndOpenGiftInvoker(item).start();
				}
			});
			ViewUtil.setGone(content, R.id.smNotice);
		} else {
			ViewUtil.setText(rechargeBtn, (amount / Constants.CENT) + "元立刻购买礼包");
			if (Config.isCMCC() || Config.isCUCC()) {
				rechargeBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();

						PayUtil.pay(VKConstants.CHANNEL_CMCC_MM,
								Account.user.getId(), amount, "", item.getId());
					}
				});
				ViewUtil.setVisible(content, R.id.smNotice);
			} else if (Config.isTelecom()) {
				rechargeBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();

						controller.pay(VKConstants.CHANNEL_TELCOM,
								Account.user.getId(), amount, "");
					}
				});
				ViewUtil.setGone(content, R.id.smNotice);
			} else {
				rechargeBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
						controller.openRechargeWindow(Account.user.bref());
					}
				});

				ViewUtil.setGone(content, R.id.smNotice);
			}
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
