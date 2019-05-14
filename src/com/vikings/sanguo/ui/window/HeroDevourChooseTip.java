package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroAbandonExpToItem;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HeroDevourChooseTip extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_devour_choose;

	private int[] itemIds;
	private HeroInfoClient hic;
	private int index;// 选择的格子
	private ViewGroup itemsLayout;

	public HeroDevourChooseTip(HeroInfoClient hic, int[] itemIds, int index) {
		super("点击添加升级道具");
		this.hic = hic;
		this.itemIds = itemIds;
		this.index = index;
	}

	public void show() {
		super.show();
		setValue();
	}

	private void setValue() {
		itemsLayout = (ViewGroup) content.findViewById(R.id.itemsLayout);
		List<HeroAbandonExpToItem> list = CacheMgr.heroAbandonExpToItemCache
				.getAll();
		for (int i = 0; i < list.size();) {
			View view = controller.inflate(R.layout.alert_devour_choose_line,
					itemsLayout, false);
			itemsLayout.addView(view);
			setItem(view.findViewById(R.id.itemLayout1), list.get(i));
			i++;
			if (i < list.size()) {
				setItem(view.findViewById(R.id.itemLayout2), list.get(i));
				i++;
			} else {
				ViewUtil.setHide(view.findViewById(R.id.itemLayout2));
			}
			if (i < list.size()) {
				setItem(view.findViewById(R.id.itemLayout3), list.get(i));
				i++;
			} else {
				ViewUtil.setHide(view.findViewById(R.id.itemLayout3));
			}
		}
	}

	private void setItem(View view, HeroAbandonExpToItem haeti) {
		final int itemId = haeti.getItemId();
		Item item = CacheMgr.getItemByID(itemId);
		ItemBag itemBag = Account.store.getItemBag(itemId);
		if (null != item) {
			new ViewImgScaleCallBack(item.getImage(),
					view.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
							* Constants.DEVOUR_ITEM_ICON_WIDTH,
					Config.SCALE_FROM_HIGH * Constants.DEVOUR_ITEM_ICON_HEIGHT);
			if (null != itemBag) {
				int count = 0;// 已经添加的数量
				for (int i = 0; i < itemIds.length; i++) {
					if (itemIds[i] == item.getId())
						count++;
				}
				int left = itemBag.getCount() - count;
				if (left > 0) {
					ViewUtil.setText(view.findViewById(R.id.count), "x" + left);

					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							itemIds[index] = itemId;
							dismiss();
						}
					});
				} else {
					ViewUtil.setText(view.findViewById(R.id.count), "x0");
				}
			} else {
				ViewUtil.setText(view.findViewById(R.id.count), "x0");
			}
			ViewUtil.setText(view.findViewById(R.id.name), item.getName());
			ViewUtil.setText(view.findViewById(R.id.expAdd),
					CalcUtil.turnToTenThousand(haeti.getExp()) + "EXP");
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
