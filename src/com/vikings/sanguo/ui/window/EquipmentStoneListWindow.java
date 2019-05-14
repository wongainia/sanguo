package com.vikings.sanguo.ui.window;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentInsertItem;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.ui.adapter.EquipmentStoneAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class EquipmentStoneListWindow extends CustomListViewWindow {
	protected byte type;
	protected EquipmentInfoClient eic;
	protected HeroInfoClient hic;

	public void open(EquipmentInfoClient eic, HeroInfoClient hic) {
		this.type = eic.getProp().getType();
		this.eic = eic;
		this.hic = hic;
		this.doOpen();
	}

	@Override
	protected void init() {
		super.init("镶嵌宝石");
		setContentBelowTitle(R.layout.gradient_msg);
		TextView textView = (TextView) window.findViewById(R.id.gradientMsg);
		ViewUtil.setRichText(textView, "选择你想要镶嵌的宝石");
	}

	@Override
	public void showUI() {
		adapter.clear();
		List<ItemBag> stones = Account.store.getStone();
		Collections.sort(stones, new Comparator<ItemBag>() {

			@Override
			public int compare(ItemBag bag1, ItemBag bag2) {
				if (canInsert(bag1) && !canInsert(bag2))
					return -1;
				else if (!canInsert(bag1) && canInsert(bag2))
					return 1;
				else
					return bag1.getItemId() - bag2.getItemId();
			}
		});
		adapter.addItems(stones);
		adapter.notifyDataSetChanged();
		dealwithEmptyAdpter();
		super.showUI();
	}

	private boolean canInsert(ItemBag bag) {
		EquipmentInsertItem eii = null;
		try {
			eii = (EquipmentInsertItem) CacheMgr.equipmentInsertItemCache
					.get(bag.getItemId());
		} catch (GameException e) {

		}
		if (null != eic.getEquipmentType() && null != eii
				&& eic.getEquipmentType().getMaxGemLevel() < eii.getLevel())
			return false;
		else
			return true;
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new EquipmentStoneAdapter(eic);
	}

	@Override
	protected String getEmptyShowText() {
		return "您的背包中没有可以镶嵌的宝石";
	}

	@Override
	protected void dealwithEmptyAdpter() {
		super.dealwithEmptyAdpter();
		if (null != emptyShow) {
			ViewUtil.setVisible(emptyShow, R.id.emptyDesc2);
			ViewUtil.setText(emptyShow, R.id.emptyDesc2, "点击 “凤仪亭” 可获得宝石");
			ViewUtil.setVisible(emptyShow, R.id.btn);
			ViewUtil.setRichText(emptyShow.findViewById(R.id.btn),
					"#icon_event_005#凤仪亭", false, 30, 30);
			emptyShow.findViewById(R.id.btn).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							controller.openRouletteWindow();

						}
					});
		}
	}
}
