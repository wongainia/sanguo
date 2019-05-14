/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-27 下午1:52:25
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.Country;
import com.vikings.sanguo.model.Province;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.SearchFiefAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class FiefTownSearchTip extends CustomConfirmDialog implements
		OnItemClickListener {
	private ListView listView;
	private View header;
	private ProvinceAdapter pAdapter;
	private SearchFiefAdapter sAdapter;

	public FiefTownSearchTip() {
		super("重镇", CustomConfirmDialog.LARGE);
		ViewUtil.setText(tip, R.id.mode, "重镇信息");
		setCloseBtn();

		// 设置省份列表
		listView = (ListView) tip.findViewById(R.id.listView);
		ArrayList<Province> provinces = getProvinces();
		pAdapter = new ProvinceAdapter();
		pAdapter.addItems(provinces);
		listView.setAdapter(pAdapter);
		listView.setOnItemClickListener(this);
		pAdapter.notifyDataSetChanged();

		initHeader();

		sAdapter = new SearchFiefAdapter();
		sAdapter.setType(Constants.TOWN);
	}

	private void initHeader() {
		header = controller.inflate(R.layout.res_item);
		ViewUtil.setVisible(header, R.id.arrow);
		ViewUtil.setBtnMirrorBt(header.findViewById(R.id.arrow), "arrow");
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewUtil.setText(tip, R.id.mode, "重镇信息");
				listView.removeHeaderView(header);
				listView.setAdapter(pAdapter);
				pAdapter.notifyDataSetChanged();
			}
		});
	}


	private ArrayList<Province> getProvinces() {
		ArrayList<Province> provinceLs = new ArrayList<Province>();

		List<Integer> provinces = CacheMgr.holyPropCache.getProvinces();
		for (Integer it : provinces) {
			Country country = CacheMgr.countryCache.getCountryByProvice(it);

			int pIdx = it - 1;
			// 有效范围0～30
			if (pIdx < 0 || pIdx > 31)
				continue;

			Province p = new Province();
			p.setIdx(pIdx + 1);
			p.setName(Config.province[pIdx] + " (" + country.getName() + ")");
			provinceLs.add(p);
		}

		return provinceLs;
	}

	CallBack dismiss = new CallBack() {
		@Override
		public void onCall() {
			dialog.dismiss();
		}
	};

	@Override
	public View getContent() {
		return Config.getController().inflate(R.layout.alert_search_city);
	}

	// 省份Adpater
	class ProvinceAdapter extends ObjectAdapter implements OnClickListener {

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
			Province p = (Province) getItem(index);
			ViewHolder holder = (ViewHolder) v.getTag();

			holder.province = p;
			StringBuilder buf = new StringBuilder(p.getName());
			ViewUtil.setBoldText(holder.name, buf.toString());
		}

		@Override
		public int getLayoutId() {
			return R.layout.province_item;
		}

		class ViewHolder {
			public TextView name;
			public Province province;
		}

		@Override
		public void onClick(View v) {
			Province province = ((ViewHolder) v.getTag()).province;
			new QueryFiefInvoker(
					CacheMgr.holyPropCache.getTownIdsByProvince(province
							.getIdx()), province).start();
		}
	}

	class QueryFiefInvoker extends BaseInvoker {
		private List<Long> ids;
		private List<BriefFiefInfoClient> fiefs;
		private Province province;

		public QueryFiefInvoker(List<Long> ids, Province province) {
			this.ids = ids;
			this.province = province;
		}

		@Override
		protected String loadingMsg() {
			return "获取重镇信息中";
		}

		@Override
		protected String failMsg() {
			return "获取重镇信息失败";
		}

		@Override
		protected void fire() throws GameException {
			fiefs = GameBiz.getInstance().briefFiefInfoQuery(ids);
		}

		@Override
		protected void onOK() {
			ViewUtil.setText(tip, R.id.mode, "--请选择重镇--");
			ViewUtil.setText(header, R.id.province, province.getName());
			sAdapter.clear();
			sAdapter.addItems(fiefs);
			listView.setAdapter(null);
			listView.addHeaderView(header);
			listView.setAdapter(sAdapter);
			sAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (null != listView) {
			if (parent.getAdapter() instanceof HeaderViewListAdapter) {
				HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) parent
						.getAdapter();
				if (headerAdapter.getWrappedAdapter() instanceof SearchFiefAdapter) {
					SearchFiefAdapter adapter = (SearchFiefAdapter) headerAdapter
							.getWrappedAdapter();
					// 由于position是view的位置，该adapter有一个header，所以item的position应该减一
					doClick(adapter, position - 1);
				}
			} else if (parent.getAdapter() instanceof SearchFiefAdapter) {
				SearchFiefAdapter adapter = (SearchFiefAdapter) parent
						.getAdapter();
				doClick(adapter, position);
			}
		}
	}

	private void doClick(SearchFiefAdapter adapter, int position) {
		if (null == adapter)
			return;
		Object o = adapter.getItem(position);
		if (o != null) {
			dismiss();
			BriefFiefInfoClient bfic = (BriefFiefInfoClient) o;
			Config.getController().getBattleMap()
					.moveToFief(bfic.getId(), true, true);
		}
	}
}