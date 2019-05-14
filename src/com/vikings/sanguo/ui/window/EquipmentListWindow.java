package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.ui.adapter.EquipmentAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class EquipmentListWindow extends CustomListViewWindow {
	protected byte type;
	protected List<EquipmentInfoClient> list = new ArrayList<EquipmentInfoClient>();

	/**
	 * @param hic
	 *            要装备的将领
	 * @param type
	 *            装备类型
	 */
	public void open(byte type) {
		this.type = type;
		this.doOpen();
	}

	protected void setData() {
		list.clear();
		List<HeroInfoClient> hics = Account.heroInfoCache.get();
		for (HeroInfoClient heroInfoClient : hics) {
			for (EquipmentSlotInfoClient esic : heroInfoClient
					.getEquipmentSlotInfos()) {
				if (type == esic.getType() && esic.hasEquipment()) {
					list.add(esic.getEic());
				}
			}
		}

		List<EquipmentInfoClient> eics = Account.equipmentCache.get(type);
		if (null != eics && !eics.isEmpty()) {
			Collections.sort(eics, new Comparator<EquipmentInfoClient>() {

				@Override
				public int compare(EquipmentInfoClient equip1,
						EquipmentInfoClient equip2) {
					if (equip1.getQuality() == equip2.getQuality()) {
						return equip2.getLevel() - equip1.getLevel();
					} else {
						return equip2.getQuality() - equip1.getQuality();
					}
				}
			});
			list.addAll(eics);
		}

	}

	@Override
	protected void init() {
		super.init("装备列表");
		setContentBelowTitle(R.layout.gradient_msg);
		TextView textView = (TextView) window.findViewById(R.id.gradientMsg);
		ViewUtil.setRichText(textView, "选择一件" + PropEquipment.getTypeName(type)
				+ "进行管理");
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	public void showUI() {
		setLeftTxt("#rmb#" + Account.user.getCurrency());
		setData();
		adapter.clear();
		adapter.addItems(list);
		super.showUI();
		dealwithEmptyAdpter();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new EquipmentAdapter();
	}

	@Override
	protected String getEmptyShowText() {
		return "您没有任何该类型装备<br/>请先在商店中购买，<br/>或通过游戏其他途径获得装备";
	}

}
