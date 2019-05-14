package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.ShopData;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ShopHintTip extends CustomConfirmDialog {

	public ShopHintTip(ShopData shopData) {
		this("很抱歉,该物品需要  "
				+ StringUtil.color("VIP" + shopData.getBuyVipLv(),
						R.color.color11) + "  才可购买,请先提升您的VIP等级", "");
	}

	public ShopHintTip(String str) {
		this(str, "");
	}

	public ShopHintTip(String str, String str2) {
		this("VIP等级不足", str, str2, "立刻提升VIP等级", false);
	}

	public ShopHintTip(String title, String str, String str2, String btn1Str,
			boolean rightCloseBtn) {
		super(title, DEFAULT);

		if (!StringUtil.isNull(str)) {
			ViewUtil.setVisible(tip, R.id.msg1);
			ViewUtil.setRichText(tip, R.id.msg1, str);
		}

		if (!StringUtil.isNull(str2)) {
			ViewUtil.setVisible(tip, R.id.msg2);
			ViewUtil.setRichText(tip, R.id.msg2, str2);
		}

		setButton(FIRST_BTN, btn1Str, new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				controller.openVipListWindow();
			}
		});

		if (rightCloseBtn)
			setRightTopCloseBtn();
		else
			setButton(SECOND_BTN, "关闭", closeL);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_toplant, contentLayout, false);
	}

}
