package com.vikings.sanguo.ui.window;

import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.PropAct;
import com.vikings.sanguo.model.PropActType;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class ActTypeWindow extends CustomListViewWindow {
	@Override
	protected void init() {
		super.init("战役列表");
		listView.setDividerHeight(1);
		// 血战引导
		StepMgr.checkStep801();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		ActTypeAdapter actTypeAdapter = new ActTypeAdapter();
		// List<PropActType> ls = CacheMgr.actTypeCache.getAllData();
		List<PropActType> ls = CacheMgr.actTypeCache.getShowPropAct();

		actTypeAdapter.addItems(ls);
		return actTypeAdapter;
	}

	public static int actTypeBloodPosition() {
		List<PropActType> ls = CacheMgr.actTypeCache.getShowPropAct();
		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i).getType() == 99/* 无尽血战 */) {
				return i;
			}
		}
		return -1;
	}

	private class ActTypeAdapter extends ObjectAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = Config.getController().inflate(getLayoutId());
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.nameIcon = (ImageView) convertView
						.findViewById(R.id.nameIcon);
				holder.desc = (TextView) convertView.findViewById(R.id.desc);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			final PropActType prop = (PropActType) getItem(position);
			new ViewImgCallBack(prop.getIcon(), holder.icon);
			new ViewImgCallBack(prop.getNameIcon(), holder.nameIcon);
			ViewUtil.setRichText(holder.desc, prop.getDesc());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!prop.isOpen()) {
						controller.alert("暂未开放!");
					} else {
						if (prop.isBlood()) {
							controller.openBloodWindow();
						} else if (prop.isArena()) {
							controller.openArenaWindow();
						} else {
							List<ActInfoClient> ls = Account.actInfoCache
									.getActsByType(prop.getType(),
											PropAct.DIFFICULT_NORMAL);
							if (ListUtil.isNull(ls))
								controller.alert("暂时没有章节!");
							else
								controller.openActWindow(prop);
						}
					}

				}
			});
			return convertView;
		}

		@Override
		public void setViewDisplay(View v, Object o, int index) {
		}

		@Override
		public int getLayoutId() {
			return R.layout.common_type_item2;
		}

		class ViewHolder {
			ImageView icon, nameIcon;
			TextView desc;
		}
	}
}
