/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-29 下午4:24:10
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.GambleData;
import com.vikings.sanguo.model.ItemWeight;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.wheel.AbstractWheelAdapter;
import com.vikings.sanguo.utils.ViewUtil;

public class CurrencySlotAdapter extends AbstractWheelAdapter {
	private List<Integer> currencyData;

	public CurrencySlotAdapter(List<Integer> currencyData) {
		this.currencyData = currencyData;
	}

	@Override
	public int getItemsCount() {
		if (null != currencyData)
			return currencyData.size();
		return 0;
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		if (cachedView == null) {
			cachedView = Config.getController().inflate(
					R.layout.currency_content);
			ViewHolder holder = new ViewHolder();
			holder.img = (ImageView) cachedView.findViewById(R.id.img);
			cachedView.setTag(holder);
		}

		ViewHolder holder = (ViewHolder) cachedView.getTag();

		int gd = currencyData.get(index);
		holder.img.setBackgroundResource(gd);
		return cachedView;
	}

	class ViewHolder {
		ImageView img;
	}
}