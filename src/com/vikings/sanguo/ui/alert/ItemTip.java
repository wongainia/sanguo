package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ItemTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_store_item;

	private ImageView icon;
	private TextView type, count, price, desc;
	private Item item;

	public ItemTip(Item item) {
		super(item.getName(), DEFAULT);
		this.item = item;
		icon = (ImageView) content.findViewById(R.id.icon);
		type = (TextView) content.findViewById(R.id.type);
		count = (TextView) content.findViewById(R.id.count);
		price = (TextView) content.findViewById(R.id.price);
		desc = (TextView) content.findViewById(R.id.desc);
	}
	
	//去掉中间的按钮
	public void setHideButton()
	{
		ViewUtil.setGone(content.findViewById(R.id.sellBtn));
		ViewUtil.setGone(content.findViewById(R.id.overBtn));
		ViewUtil.setGone(content.findViewById(R.id.openBtn));
		ViewUtil.setGone(content.findViewById(R.id.closeBtn));
	}
	
	public void show() {
		if (item == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		new ViewImgScaleCallBack(item.getImage(), icon, Config.SCALE_FROM_HIGH
				* Constants.ICON_WIDTH, Config.SCALE_FROM_HIGH
				* Constants.ICON_HEIGHT);
		ViewUtil.setText(type, "类型：" + item.getTypeName());
		ViewUtil.setGone(count);
		ViewUtil.setGone(price);
		ViewUtil.setRichText(desc, item.getDesc());
		setButton(THIRD_BTN, "关闭", closeL);
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
