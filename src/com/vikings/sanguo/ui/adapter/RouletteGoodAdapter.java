package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.RouletteGoodData;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.EditNumTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class RouletteGoodAdapter extends ObjectAdapter {
	private CallBack callBack;

	public RouletteGoodAdapter(CallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.iconLayout = convertView.findViewById(R.id.iconLayout);
			holder.goodAddDesc = (TextView) convertView
					.findViewById(R.id.goodAddDesc);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.selectCount = (EditText) convertView
					.findViewById(R.id.selectCount);
			holder.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RouletteGoodData data = (RouletteGoodData) getItem(position);
		ItemBag itemBag = data.getItemBag();
		Item item = itemBag.getItem();
		new ViewImgScaleCallBack(item.getImage(),
				holder.iconLayout.findViewById(R.id.icon),
				Constants.ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
		ViewUtil.setText(holder.goodAddDesc,
				CalcUtil.format(data.getProp().getGoodPercent()) + "%");
		ViewUtil.setText(holder.name, item.getName() + "x" + itemBag.getCount());
		ViewUtil.setText(holder.selectCount, data.getSelCount());
		holder.selectCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new EditNumTip(data, callBack).show();

			}
		});
		holder.seekBar.setOnSeekBarChangeListener(null);
		holder.seekBar.setMax(data.getMax());
		holder.seekBar.setProgress(data.getSelCount());
		holder.seekBar.setOnSeekBarChangeListener(new SeekBarListener(data,
				holder));
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.roulette_good_item;
	}

	private class ViewHolder {
		View iconLayout;
		TextView goodAddDesc, name;
		EditText selectCount;
		SeekBar seekBar;
	}

	private class SeekBarListener implements OnSeekBarChangeListener {

		private RouletteGoodData data;
		private ViewHolder holder;

		public SeekBarListener(RouletteGoodData data, ViewHolder holder) {
			this.data = data;
			this.holder = holder;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			int cnt = seekBar.getProgress();

			// 设置剩余数量和选中数量
			ViewUtil.setText(holder.selectCount, String.valueOf(cnt));

			data.setSelCount(cnt);

			if (null != callBack)
				callBack.onCall();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (null != callBack)
				callBack.onCall();
		}
	}

	/**
	 * 第一位为选中的宝石数量 ;第二位为当前的好感度加成值
	 * 
	 * @return
	 */
	public float[] getCurValue() {
		float[] value = new float[2];
		List list = getContent();
		for (Object obj : list) {
			RouletteGoodData data = (RouletteGoodData) obj;
			value[0] += data.getSelCount();
			value[1] += data.getSelCount() * data.getProp().getGoodPercent();
		}
		return value;
	}
}
