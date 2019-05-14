package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.GambleData;
import com.vikings.sanguo.model.ItemWeight;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.wheel.AbstractWheelAdapter;
import com.vikings.sanguo.utils.ViewUtil;

public class GambleSlotAdapter extends AbstractWheelAdapter {
	private List<GambleData> gambleData;

	public GambleSlotAdapter(List<GambleData> gambleData) {
		this.gambleData = gambleData;
	}

	@Override
	public int getItemsCount() {
		if (null != gambleData)
			return gambleData.size();
		return 0;
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		if (cachedView == null) {
			cachedView = Config.getController()
					.inflate(R.layout.gamble_content);
			ViewHolder holder = new ViewHolder();
			holder.img = (ImageView) cachedView.findViewById(R.id.img);
			holder.cnt = (TextView) cachedView.findViewById(R.id.cnt);
			cachedView.setTag(holder);
		}

		ViewHolder holder = (ViewHolder) cachedView.getTag();

		GambleData gd = gambleData.get(index);

		// 如果取不到水果或材料图片，用stub代替
		if (GambleData.ITEM == gd.getType() && null != gd.getItem())
			new ViewImgScaleCallBack(gd.getItem().getImage(), holder.img,
					Constants.GAMBLE_ICON_WIDTH, Constants.GAMBLE_ICON_HEIGHT);
		else if (GambleData.RES == gd.getType()) {
			try {
				ItemWeight weight = (ItemWeight) CacheMgr.weightCache.get(gd
						.getItemId());
				new ViewImgScaleCallBack(weight.getIcon(), holder.img,
						Constants.GAMBLE_ICON_WIDTH,
						Constants.GAMBLE_ICON_HEIGHT);
			} catch (GameException e) {
				e.printStackTrace();
			}
		}

		ViewUtil.setText(holder.cnt, "×" + gd.getAmount());
		return cachedView;
	}

	class ViewHolder {
		ImageView img;
		TextView cnt;
	}
}