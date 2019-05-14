package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.FatSheepData;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class FatSheepFiefAdapter extends ObjectAdapter {
	private CallBack cb;

	public FatSheepFiefAdapter(CallBack cb) {
		this.cb = cb;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.scaleName = (TextView) convertView
					.findViewById(R.id.scaleName);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.lord = (TextView) convertView.findViewById(R.id.lord);
			holder.troop = (TextView) convertView.findViewById(R.id.troop);
			holder.fiefState = (ImageView) convertView
					.findViewById(R.id.fiefState);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FatSheepData data = (FatSheepData) getItem(position);

		// 领地图标
		new ViewImgScaleCallBack(data.getIcon(), holder.icon,
				Constants.ARM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ARM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
		// 领地规模名称和领地名称
		ViewUtil.setText(holder.scaleName, data.getScaleName());

		// 领主
		ViewUtil.setText(holder.name,
				data.getLordName() + " (" + data.getLordCountry() + ")");

		// 兵力
		StringBuilder buf = new StringBuilder("兵力:" + data.getUnitCount()
				+ "  ").append("(将:" + data.getTotalHeroInFief() + ")");
		ViewUtil.setText(holder.lord, buf.toString());

		// 领地状态
		String stateIcon = data.getStateIcon();
		if (null != stateIcon) {
			ViewUtil.setVisible(holder.fiefState);
			ViewUtil.setImage(holder.fiefState, stateIcon);
		} else {
			ViewUtil.setGone(holder.fiefState);
		}

		ViewUtil.setRichText(holder.troop, data.getResource(), true);
		convertView.setOnClickListener(new ClickListener(data.getBfic()));
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.search_fief_item;
	}

	private static class ViewHolder {
		ImageView icon;
		TextView scaleName, name, lord, troop;
		ImageView fiefState;
	}

	private class ClickListener implements OnClickListener {
		private BriefFiefInfoClient bfic;

		public ClickListener(BriefFiefInfoClient bfic) {
			this.bfic = bfic;
		}

		@Override
		public void onClick(View v) {
			if (null != cb)
				cb.onCall();
			Config.getController().getBattleMap()
					.moveToFief(bfic.getId(), true, true);
		}
	}
}
