/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-10 上午10:44:10
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.RankProp;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class RankEntryWindow extends CustomListViewWindow {

	private int[] words = { R.drawable.wealth_mast_word,R.drawable.hero_history_word,
			R.drawable.arm_battlefield_word,R.drawable.hero_selfless_word, };
	
	@Override
	protected void init() {
		super.init("排行榜");
	}

	@Override
	protected ObjectAdapter getAdapter() {
		RankPropAdapter rankPropAdapter = new RankPropAdapter();
		List<RankProp> ls = CacheMgr.rankPropCache.getAllData();
		rankPropAdapter.addItems(ls);
		return rankPropAdapter;
	}

	class RankPropAdapter extends ObjectAdapter implements OnClickListener {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = Config.getController().inflate(getLayoutId());
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.name = (ImageView) convertView.findViewById(R.id.name);
				holder.desc = (TextView) convertView.findViewById(R.id.desc);
				convertView.setTag(holder);
				convertView.setOnClickListener(this);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.rankProp = (RankProp) getItem(position);
			this.setViewDisplay(convertView, getItem(position), position);
			return convertView;
		}

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			RankProp rp = (RankProp) getItem(index);
			ViewHolder holder = (ViewHolder) v.getTag();

			new ViewImgCallBack(rp.getIcon(), holder.img);
			holder.name.setBackgroundResource(words[index]);
//			ViewUtil.setBoldText(holder.name, rp.getTitle());
			
			ViewUtil.setRichText(holder.desc, ViewUtil.half2Full(rp.getDesc()));
		}

		@Override
		public int getLayoutId() {
			return R.layout.common_type_item;
		}

		@Override
		public void onClick(View v) {
			if (v instanceof ViewGroup) {
				RankProp rp = ((ViewHolder) v.getTag()).rankProp;
				RankWindow rankWindow = RankWindow.create(rp);
				if (null != rankWindow)
					rankWindow.open(rp);
			}
		}

		class ViewHolder {
			ImageView img,name;
			TextView desc;
			RankProp rankProp;
		}
	}
}
