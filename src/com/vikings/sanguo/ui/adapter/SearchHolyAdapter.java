package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HolyCategory;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.thread.ViewRichTextCallBack;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.FiefDetailTopInfo;

public class SearchHolyAdapter extends ObjectAdapter {

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			holder.country = (TextView) convertView.findViewById(R.id.country);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.scaleName = (TextView) convertView
					.findViewById(R.id.scaleName);
			holder.scalebg = (ImageView) convertView.findViewById(R.id.scalebg);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		HolyCategory hc = (HolyCategory) getItem(position);

		// 领地图标
		String fiefIconName = hc.getIcon();
		if (StringUtil.isNull(fiefIconName))
			fiefIconName = "fief_wasteland.png";
		new ViewImgScaleCallBack(fiefIconName, holder.icon,
				Constants.ARM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ARM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

		// 领地规模名称和领地名称
		ViewUtil.setRichText(holder.desc, hc.getDesc());
		ViewUtil.setText(holder.country, hc.getName());

		if (hc.getState() == BattleStatus.BATTLE_STATE_SURROUND) {
			ViewUtil.setRichText(holder.state,
					StringUtil.color("战斗进行中，点击参与", R.color.color19));
		} else if (hc.getState() == BattleStatus.BATTLE_STATE_FINISH) {
			int time = FiefDetailTopInfo.getBattleTime(hc.getTime());
			if (time > 0) {
				ViewUtil.setRichText(holder.state, R.id.status, StringUtil
						.color("下次战斗：" + DateUtil.formatTime(time),
								R.color.color24));
			} else {
				ViewUtil.setRichText(holder.state,
						StringUtil.color("战斗进行中，点击参与", R.color.color19));
			}

		} else {
			ViewUtil.setRichText(holder.state,
					StringUtil.color("战斗进行中，点击参与", R.color.color19));
		}

		holder.scaleName.setBackgroundResource(hc.getHolyTypeName());
		holder.scalebg.setBackgroundResource(R.drawable.general_btn);
		try {
			Item item = (Item) CacheMgr.itemCache.get(hc.getItemId());
			if (item != null) {
				new ViewRichTextCallBack(item.getImage(), "duel_icon", "消耗:",
						"【" + item.getName() + "】 x" + hc.getItemCount() + "",
						holder.count,30,30);
			}
		} catch (GameException e) {
			e.printStackTrace();
		}

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.holy_item;
	}

	static class ViewHolder {
		ImageView icon, scalebg;
		TextView state, desc, country, count, scaleName;
	}

}
