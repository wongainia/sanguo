package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.SkillIllustrastionTip;
import com.vikings.sanguo.utils.ViewUtil;

public class SkillIllustrationAdpater extends ObjectAdapter implements
		OnClickListener {

	@Override
	public int getLayoutId() {
		return R.layout.skill_illus_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Item item = (Item) getItem(position);
		holder.item = item;

		new ViewImgScaleCallBack(item.getImage(), holder.icon,
				Config.SCALE_FROM_HIGH * Constants.HERO_ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.HERO_ICON_HEIGHT);
		ViewUtil.setText(holder.name, item.getName());

		convertView.setOnClickListener(this);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	private static class ViewHolder {
		ImageView icon;
		TextView name;
		Item item;
	}

	@Override
	public void onClick(View v) {
		Item item = ((ViewHolder) v.getTag()).item;
		new SkillIllustrastionTip(item).show();
	}

}
