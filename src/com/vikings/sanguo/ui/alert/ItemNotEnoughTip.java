package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public abstract class ItemNotEnoughTip extends CustomConfirmDialog {
	protected CallBack callBack;
	protected TextView desc1, desc2;
	protected OnClickListener listener;
	protected Item item;

	public ItemNotEnoughTip(String title) {
		super(title, DEFAULT);
		desc1 = (TextView) content.findViewById(R.id.desc1);
		desc2 = (TextView) content.findViewById(R.id.desc2);
		listener = getClickListener();
		if (null != listener)
			setButton(SECOND_BTN, "确定", listener);
		setButton(THIRD_BTN, "关闭", closeL);
	}

	public void show(Item item, int needCount, int selfCount) {
		if (null == item || needCount <= selfCount)
			return;
		this.item = item;
		new ViewImgScaleCallBack(item.getImage(),
				content.findViewById(R.id.icon), Constants.ITEM_ICON_WIDTH
						* Config.SCALE_FROM_HIGH, Constants.ITEM_ICON_HEIGHT
						* Config.SCALE_FROM_HIGH);
		ViewUtil.setText(content.findViewById(R.id.itemName), item.getName());
		ViewUtil.setText(content.findViewById(R.id.count), "x"
				+ (needCount - selfCount));
		setDesc();
		super.show();
	}

	protected abstract void setDesc();

	protected abstract OnClickListener getClickListener();

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_rmb_hero_evolve, tip, false);
	}

}
