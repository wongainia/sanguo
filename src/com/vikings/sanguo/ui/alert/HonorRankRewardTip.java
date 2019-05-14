package com.vikings.sanguo.ui.alert;

import android.view.View;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HonorRankRewardTip extends CustomConfirmDialog {
	private Item item;

	public HonorRankRewardTip(Item item) {
		super(item.getName(), DEFAULT, true);
		this.item = item;
	}

	public void show() {
		if (item == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		new ViewImgScaleCallBack(item.getImage(), findViewById(R.id.icon),
				Config.SCALE_FROM_HIGH * Constants.ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.ICON_HEIGHT);
		ViewUtil.setText(findViewById(R.id.type), "类型：" + item.getTypeName());
		ViewUtil.setText(findViewById(R.id.count), "数量：1");

		if (item.getSell() > 0)
			ViewUtil.setRichText(findViewById(R.id.price),
					"售价：#money#" + item.getSell(), true);
		else
			ViewUtil.setRichText(findViewById(R.id.price),
					"售价：" + StringUtil.color("不可出售", R.color.color24), true);

		ViewUtil.setRichText(findViewById(R.id.desc), item.getDesc());
		ViewUtil.setGone(tip, R.id.openBtn);
		ViewUtil.setGone(tip, R.id.overBtn);
		ViewUtil.setGone(tip, R.id.sellBtn);
		ViewUtil.setGone(tip, R.id.closeBtn);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_store_item, tip, false);
	}
}
