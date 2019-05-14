/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-4 下午7:00:20
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.DelFavorateFief;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class SearchFiefAdapter extends ObjectAdapter {
	private int type;

	private CallBack deleteCallBack;

	public void setType(int type) {
		this.type = type;
	}

	public void setCallBack(CallBack callBack) {
		this.deleteCallBack = callBack;
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
			holder.address = (TextView) convertView.findViewById(R.id.name);
			holder.country = (TextView) convertView.findViewById(R.id.country);
			holder.lord = (TextView) convertView.findViewById(R.id.lord);
			holder.troop = (TextView) convertView.findViewById(R.id.troop);
			holder.fiefState = (ImageView) convertView
					.findViewById(R.id.fiefState);
			holder.deleteBtn = (ImageView) convertView
					.findViewById(R.id.deleteBtn);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		BriefFiefInfoClient bfic = (BriefFiefInfoClient) getItem(position);
		// 领地图标
		String fiefIconName = bfic.getIcon();
		if (StringUtil.isNull(fiefIconName))
			fiefIconName = "fief_wasteland.png";
		new ViewImgScaleCallBack(fiefIconName, holder.icon,
				Constants.ARM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ARM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

		// 领地规模名称和领地名称
		String name = bfic.getName();
		ViewUtil.setText(holder.scaleName, name);
		ViewUtil.setText(holder.address, name);

		if (type == Constants.FAVOR) {
			ViewUtil.setVisible(holder.deleteBtn);
			holder.deleteBtn
					.setOnClickListener(new DeleteFavorateListener(bfic));
		} else {
			ViewUtil.setGone(holder.deleteBtn);
		}

		if (type == Constants.HOLY || type == Constants.FAMOUS) {
			if (null != bfic.getNatureCountry()
					|| null != bfic.getNatureCountry().getName()) {
				ViewUtil.setVisible(holder.country);
				ViewUtil.setText(holder.country, "("
						+ bfic.getNatureCountry().getName() + ")");
			} else {
				ViewUtil.setGone(holder.country);
			}
		} else {
			ViewUtil.setGone(holder.country);
		}

		// 领主
		ViewUtil.setText(holder.lord, "领主: " + bfic.getLord().getNickName());

		// 兵力
		StringBuilder buf = new StringBuilder("兵力:")
				.append(bfic.getUnitCount())
				.append("(")
				.append(bfic.getMainHero() == null ? "无" : bfic.getMainHero()
						.getHeroFullName()).append(")");

		ViewUtil.setRichText(holder.troop, buf.toString());

		if (type == Constants.TOWN) {
			// 领主 -- 国家
			StringBuilder sBuilder = new StringBuilder(bfic.getLord()
					.getNickName()).append("(").append(bfic.getCountry())
					.append(")");
			ViewUtil.setText(holder.address, sBuilder.toString());
			// 守将 -- 主将
			ViewUtil.setRichText(holder.lord, "守将: "
					+ (bfic.getMainHero() == null ? "无" : bfic.getMainHero()
							.getHeroFullName()));

			ViewUtil.setRichText(holder.troop, "兵力：" + bfic.getUnitCount());

		} else if (type == Constants.FAVOR) {

			// 领主
			ViewUtil.setText(holder.lord, "领主: " + bfic.getLord().getNickName());

			// 兵力
			StringBuilder sb = new StringBuilder("兵力:")
					.append(bfic.getUnitCount())
					.append("(")
					.append(bfic.getMainHero() == null ? "无" : bfic
							.getMainHero().getHeroFullName()).append(")");

			ViewUtil.setRichText(holder.troop, sb.toString());
		}

		holder.bfic = bfic;
		// 领地状态
		String stateIcon = bfic.getStateIcon();
		if (null != stateIcon) {
			ViewUtil.setVisible(holder.fiefState);
			ViewUtil.setImage(holder.fiefState, stateIcon);
		} else
			ViewUtil.setGone(holder.fiefState);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.search_fief_item;
	}

	static class ViewHolder {
		ImageView icon;
		TextView scaleName, address, lord, troop, country;
		ImageView fiefState;
		BriefFiefInfoClient bfic;
		ImageView deleteBtn;
	}

	private class DeleteFavorateListener implements OnClickListener {
		private BriefFiefInfoClient bfic;

		public DeleteFavorateListener(BriefFiefInfoClient bfic) {
			super();
			this.bfic = bfic;
		}

		@Override
		public void onClick(View v) {
			new DelFavorateFief(bfic.getId(), deleteCallBack).start();
		}

	}
}
