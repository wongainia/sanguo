package com.vikings.sanguo.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.ui.alert.RevivedBossAlert;
import com.vikings.sanguo.ui.window.ManorReviveWindow.ButtonCallback;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class RevivedItemAdapter extends ObjectAdapter {
	List<Integer> armIds = new ArrayList<Integer>();
	private BuildingProp bp;
	private List<Integer> ids;
	private ButtonCallback btnCall;
	private int index = 0;

	public void setIndex(int index) {
		this.index = index;
	}

	public RevivedItemAdapter(BuildingProp bp, List<Integer> ids,
			ButtonCallback btnCall, int index) {
		this.index = index;
		this.bp = bp;
		this.ids = ids;
		this.btnCall = btnCall;
	}

	@Override
	public int getLayoutId() {
		return R.layout.revived_grid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.itemName = convertView.findViewById(R.id.itemName);
			holder.itemIcon = convertView.findViewById(R.id.itemIcon);
			holder.itemCount = convertView.findViewById(R.id.itemCount);
			holder.itemGet = convertView.findViewById(R.id.itemGet);
			holder.viewGroup = convertView.findViewById(R.id.revivedGroup);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	// @Override
	public void setViewDisplay(View v, Object o, int position) {
		final ViewHolder holder = (ViewHolder) v.getTag();
		final ArmInfoClient ai = (ArmInfoClient) getItem(position);
		final TroopProp prop = ai.getProp();
		final Integer obj = Integer.valueOf(prop.getId());
		IconUtil.setArmIcon(holder.itemIcon, ai);

		if (Account.user.getWeakLeftTime() > 0 && index == 0) {
			ViewUtil.disableButton(holder.viewGroup);
		} else {
			ViewUtil.enableButton(holder.viewGroup);
		}

		holder.viewGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!prop.isBoss()) {
					if (Account.user.getWeakLeftTime() > 0 && index == 0)
						return;// 虚弱状态不可救治士兵

					if (ids.contains(obj)) {
						ids.remove(obj);
						ViewUtil.setGone(holder.itemGet);

					} else {
						ViewUtil.setVisible(holder.itemGet);
						ids.add(obj);
					}
					if (null != btnCall) {
						btnCall.onCall();
					}
				} else {
					new RevivedBossAlert(bp, prop, ai.getCount()).show();
				}
			}
		});

		if (ids.contains(obj)) {
			ViewUtil.setVisible(holder.itemGet);
		} else {
			ViewUtil.setGone(holder.itemGet);
		}

		ImageUtil.setReviveBg(holder.itemIcon);
		ViewUtil.setRichText(holder.itemName, prop.getName());// CacheMgr.manorReviveCache.getArmReviveName(ai.getId())
		ViewUtil.setRichText(holder.itemCount, "X" + ai.getCount());
	}

	static class ViewHolder {
		View itemName, itemIcon, itemCount, itemGet, viewGroup;
	}

}
