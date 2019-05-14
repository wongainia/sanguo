package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ShopData;
import com.vikings.sanguo.ui.window.ShopWindow;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ToShopTip extends CustomConfirmDialog {
	private final static int layout = R.layout.alert_toplant;
	private int itemId = 0;
	private ShopData shopData;

	public ToShopTip(int itemId) {
		super("", CustomConfirmDialog.DEFAULT);
		this.itemId = itemId;
		shopData = (ShopData) CacheMgr.shop.getShopDataById(itemId);
	}

	public void show() {
		if (shopData == null) {
			try {
				Item item = (Item) CacheMgr.itemCache.get(itemId);
				StringBuilder buf = new StringBuilder("道具不足");
				if (null != item)
					buf.append("<br/><br/>没有足够的[" + item.getName() + "]");
				if (itemId == 4102)
					buf.append("<br/><br/>挑战家族荣耀榜可获得[" + item.getName() + "]");
				controller.alert(buf.toString());
			} catch (GameException e) {
				e.printStackTrace();
			}
			return;
		}
		setValue();
		setTitle(shopData.getItem().getName() + "不足");
		super.show();
	}

	private void setValue() {
		ViewUtil.setVisible(content.findViewById(R.id.msg1));
		ViewUtil.setRichText(
				content,
				R.id.msg1,
				StringUtil.color("你的【" + shopData.getItem().getName()
						+ "】数量不足，请先去商店购买",
						controller.getResourceColorText(R.color.color11)));
		setButton(CustomConfirmDialog.FIRST_BTN, "去购买", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (shopData != null && shopData.getViewTab() > 0
						&& shopData.getViewTab() < 4)
					Config.getController().openShop(shopData.getViewTab() - 1);
			}
		});
		setButton(CustomConfirmDialog.SECOND_BTN, "关闭", closeL);

	}

	@Override
	public View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
