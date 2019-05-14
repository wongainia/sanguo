/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-28 上午11:30:18
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.AdvancedResource;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class AdvancedRessourceNameTip extends CustomConfirmDialog {
	private ListView listView;
	private ResourceNameAdapter pAdapter;

	public AdvancedRessourceNameTip() {
		super("高级资源点", CustomConfirmDialog.DEFAULT);

		ViewUtil.setRichText(
				tip,
				R.id.mode,
				"展示全世界  "
						+ StringUtil.color(
								""
										+ CacheMgr.dictCache.getDictInt(
												Dict.TYPE_ADVANCED_RESOURCE, 2),
								R.color.color19) + "  级以上的资源点");
		setCloseBtn();

		// 设置省份列表
		listView = (ListView) tip.findViewById(R.id.listView);
		pAdapter = new ResourceNameAdapter();
		pAdapter.addItems(AdvancedResource.getAdvancedResouceInfos());
		listView.setAdapter(pAdapter);
		pAdapter.notifyDataSetChanged();
	}

	@Override
	protected View getContent() {
		return Config.getController().inflate(R.layout.alert_search_city, tip,
				false);
	}

	class ResourceNameAdapter extends ObjectAdapter implements OnClickListener {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = Config.getController().inflate(getLayoutId());
				ViewHolder holder = new ViewHolder();
				holder.name = (TextView) convertView
						.findViewById(R.id.province);
				convertView.setTag(holder);
				convertView.setOnClickListener(this);
			}
			this.setViewDisplay(convertView, getItem(position), position);
			return convertView;
		}

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			AdvancedResource ar = (AdvancedResource) getItem(index);
			ViewHolder holder = (ViewHolder) v.getTag();
			holder.ar = ar;
			ViewUtil.setBoldText(holder.name, ar.getName());
		}

		@Override
		public int getLayoutId() {
			return R.layout.province_item;
		}

		class ViewHolder {
			public TextView name;
			public AdvancedResource ar;
		}

		@Override
		public void onClick(View v) {
			dismiss();
			new AdvancedResourceSearchTip(((ViewHolder) v.getTag()).ar).show();
		}
	}
}
