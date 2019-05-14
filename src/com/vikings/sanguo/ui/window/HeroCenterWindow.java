package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

//英雄殿
public class HeroCenterWindow extends CustomListViewWindow {
	private int[] opTxtResIds = { R.drawable.txt_sj, R.drawable.txt_jh,
			R.drawable.txt_qh, R.drawable.txt_cx,R.drawable.txt_fenjie };

	private String[] icon = { "title_hero_01.png", "title_hero_02.png",
			"title_hero_03.png", "title_hero_04.png","title_hero_05.png" };

	private Integer[] descs = { UITextProp.HERO_CENTER_DESC1,
			UITextProp.HERO_CENTER_DESC2, UITextProp.HERO_CENTER_DESC3,
			UITextProp.HERO_CENTER_DESC4,UITextProp.HERO_CENTER_DESC5 };
	private OnClickListener[] listeners = { new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 升级
			new HeroDevourListWindow().open();
		}
	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 进化
			new HeroEvolveListWindow().open();
		}

	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 强化
			new HeroStrengthenListWindow().open();
		}

	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 宠幸
			new HeroFavourListWindow().open();
		}
		},
		new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 分解
				new HeroAbandonListWindow().open();
			}
		}
	};
	@Override
	protected void init() {
		super.init("英雄殿");
		adapter.addItems(descs);
		setContentBelowTitle(R.layout.gradient_msg);
		ViewUtil.setText(window, R.id.gradientMsg, "选择方式调教你的武将");
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
		adapter = new HeroCenterAdapter();
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
