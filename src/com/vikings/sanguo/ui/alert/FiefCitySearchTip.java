/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-9 下午4:09:00
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.CityGeoInfo;
import com.vikings.sanguo.model.Country;
import com.vikings.sanguo.model.Province;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.map.core.GeoPoint;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class FiefCitySearchTip extends CustomConfirmDialog {
	private ListView listView;
	private View header;
	private ProvinceAdapter pAdapter;
	private CityAdapter cAdapter = new CityAdapter();

	public FiefCitySearchTip() {
		super("按城市查找", CustomConfirmDialog.DEFAULT);

		CacheMgr.cityGeoInfoCache.checkLoad();
		setCloseBtn();

		// 设置省份列表
		listView = (ListView) tip.findViewById(R.id.listView);
		ArrayList<Province> provinces = getProvinces();
		pAdapter = new ProvinceAdapter();
		pAdapter.addItems(provinces);
		listView.setAdapter(pAdapter);
		pAdapter.notifyDataSetChanged();

		initHeader();
	}

	private void initHeader() {
		header = controller.inflate(R.layout.city_item);
		ViewUtil.setVisible(header, R.id.arrow);
		ViewUtil.setBtnMirrorBt(header.findViewById(R.id.arrow), "arrow");
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewUtil.setText(tip, R.id.mode, "--请选择省份--");
				listView.removeHeaderView(header);
				listView.setAdapter(pAdapter);
				pAdapter.notifyDataSetChanged();
			}
		});
	}

	private ArrayList<Province> getProvinces() {
		ArrayList<Province> provinceLs = new ArrayList<Province>();

		ArrayList<Country> countryLs = CacheMgr.countryCache.getAll();

		for (int i = 0; i < countryLs.size(); i++) {
			Country c = countryLs.get(i);
			int pIdx = c.getProvinceId() - 1;

			// 有效范围0～30
			if (pIdx < 0) // || pIdx > 31
				continue;

			Province p = new Province();
			p.setIdx(pIdx + 1);
			p.setName(Config.province[pIdx] + " (" + c.getName() + ")");
			ArrayList<CityGeoInfo> citys = (ArrayList<CityGeoInfo>) CacheMgr.cityGeoInfoCache
					.search(c.getProvinceId());
			p.setCityGeoInfos(citys);

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

			ViewUtil.setText(tip, R.id.mode, "--请选择城市--");
			ViewUtil.setText(header, R.id.province, province.getName());
			ViewUtil.setBoldText(header.findViewById(R.id.province),
					province.getName());
			cAdapter.clear();
			cAdapter.addItems(province.getCityGeoInfos());
			listView.setAdapter(null);
			listView.addHeaderView(header);
			listView.setAdapter(cAdapter);
			cAdapter.notifyDataSetChanged();
		}
	}


	// 城市Adpater
	public class CityAdapter extends ObjectAdapter implements OnClickListener {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = Config.getController().inflate(getLayoutId());
				ViewHolder holder = new ViewHolder();
				holder.province = (TextView) convertView
						.findViewById(R.id.province);
				convertView.setTag(holder);
				convertView.setOnClickListener(this);
			}
			this.setViewDisplay(convertView, getItem(position), position);
			return convertView;
		}

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			CityGeoInfo city = (CityGeoInfo) getItem(index);

			String name = Config.getCitys(city.getProvince())[city.getCity() - 1];
			ViewHolder holder = (ViewHolder) v.getTag();
			holder.city = (CityGeoInfo) getItem(index);
			ViewUtil.setRichText(holder.province, StringUtil.color(name, Config
					.getController().getResourceColorText(R.color.color6)));

			ViewUtil.setBoldText(holder.province, name);

		}

		class ViewHolder {
			public TextView province;
			public CityGeoInfo city;
		}

		@Override
		public int getLayoutId() {
			return R.layout.city_item;
		}

		@Override
		public void onClick(View v) {
			dismiss();
			CityGeoInfo info = ((ViewHolder) v.getTag()).city;
			GeoPoint point = new GeoPoint(info.getLatitude(),
					info.getLongtitude());
			Config.getController().getBattleMap()
					.moveToFief(TileUtil.geoPoint2FiefId(point), false, true);
		}
	}
}
