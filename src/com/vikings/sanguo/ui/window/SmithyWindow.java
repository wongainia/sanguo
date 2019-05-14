package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

//铁匠铺
public class SmithyWindow extends CustomListViewWindow {
	private int[] opTxtResIds = { R.drawable.txt_bq, R.drawable.txt_fj,
			R.drawable.txt_zj, R.drawable.txt_sp };

	private String[] icon = { "title_blacksmith_01.png",
			"title_blacksmith_02.png", "title_blacksmith_03.png",
			"title_blacksmith_04.png" };
	private Integer[] descs = { UITextProp.SMITHY_DESC1,
			UITextProp.SMITHY_DESC2, UITextProp.SMITHY_DESC3,
			UITextProp.SMITHY_DESC4 };
	private OnClickListener[] listeners = { new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 兵器
			new EquipmentListWindow().open(PropEquipment.TYPE_WEAPON);
		}
	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 防具
			new EquipmentListWindow().open(PropEquipment.TYPE_ARMOR);
		}

	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 战甲
			new EquipmentListWindow().open(PropEquipment.TYPE_CLOTHES);
		}

	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 饰品
			new EquipmentListWindow().open(PropEquipment.TYPE_ACCESSORIES);
		}

	} };

	@Override
	protected void init() {
		adapter = new HeroCenterAdapter();
		adapter.addItems(descs);
		super.init("铁匠铺");
		setContentBelowTitle(R.layout.gradient_msg);
		TextView textView = (TextView) window.findViewById(R.id.gradientMsg);
		textView.setTextSize(14);
		ViewUtil.setText(textView, "选择类别进行装备管理");
	}

	public void open() {
		this.doOpen();
	}

	@Override
	public void showUI() {
		super.showUI();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	private class HeroCenterAdapter extends ObjectAdapter {

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			new ViewImgCallBack(icon[index], v.findViewById(R.id.icon));
			ViewUtil.setImage(v, R.id.nameIcon, opTxtResIds[index]);
			ViewUtil.setText(v, R.id.desc,
					CacheMgr.uiTextCache.getTxt(descs[index].intValue()));
			v.setOnClickListener(listeners[index]);
		}

		@Override
		public int getLayoutId() {
			return R.layout.common_type_item2;
		}

	}
}
