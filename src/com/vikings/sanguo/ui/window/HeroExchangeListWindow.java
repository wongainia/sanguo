package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.HeroRecruitExchange;
import com.vikings.sanguo.model.HeroRecruitExchangeData;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.HeroExchangeAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class HeroExchangeListWindow extends CustomListViewWindow implements
		OnClickListener {

	static public final int TAB1 = 0;
	static public final int TAB2 = 1;
	static public final int TAB3 = 2;
	static public final int TAB4 = 3;

	private ImageButton[] tabs;

	private int[] txts = { R.drawable.txt_exchange_dj,
			R.drawable.txt_exchange_bj, R.drawable.txt_exchange_yj,
			R.drawable.txt_exchange_jj };

	private int[] txtPresses = { R.drawable.txt_exchange_dj_press,
			R.drawable.txt_exchange_bj_press, R.drawable.txt_exchange_yj_press,
			R.drawable.txt_exchange_jj_press };

	private int[] bgIds = { R.drawable.tab_btn1, R.drawable.tab_btn1_press };

	// 当前选中tab
	private int index;

	private List<HeroRecruitExchangeData> weiExchanges, shuExchanges,
			wuExchanges, qunExchanges;

	@Override
	protected void init() {
		super.init("将领兑换");
		setContentBelowTitle(R.layout.list_4_tabs);
		tabs = new ImageButton[4];
		tabs[0] = (ImageButton) findViewById(R.id.tab1);
		tabs[0].setOnClickListener(this);

		tabs[1] = (ImageButton) findViewById(R.id.tab2);
		tabs[1].setOnClickListener(this);

		tabs[2] = (ImageButton) findViewById(R.id.tab3);
		tabs[2].setOnClickListener(this);

		tabs[3] = (ImageButton) findViewById(R.id.tab4);
		tabs[3].setOnClickListener(this);
	}

	public void open(int index) {
		this.doOpen();
		selectTab(index);
	}

	public void open() {
		open(TAB1);
	}

	private void selectTab(int index) {
		this.index = index;
		ViewUtil.selectTab(tabs, txts, txtPresses, bgIds, index);
		showUI();
	}

	@Override
	public void showUI() {
		adapter.clear();
		if (index == TAB1) {
			if (null == weiExchanges) {
				weiExchanges = CacheMgr.heroRecruitExchangeCache
						.getByCountry(HeroRecruitExchange.EXCHANGE_DING);
			}
			adapter.addItems(weiExchanges);
		} else if (index == TAB2) {
			if (null == shuExchanges) {
				shuExchanges = CacheMgr.heroRecruitExchangeCache
						.getByCountry(HeroRecruitExchange.EXCHANGE_BING);
			}
			adapter.addItems(shuExchanges);
		} else if (index == TAB3) {
			if (null == wuExchanges) {
				wuExchanges = CacheMgr.heroRecruitExchangeCache
						.getByCountry(HeroRecruitExchange.EXCHANGE_YI);
			}
			adapter.addItems(wuExchanges);
		} else {
			if (null == qunExchanges) {
				qunExchanges = CacheMgr.heroRecruitExchangeCache
						.getByCountry(HeroRecruitExchange.EXCHANGE_JIA);
			}
			adapter.addItems(qunExchanges);
		}
		adapter.notifyDataSetChanged();
		super.showUI();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new HeroExchangeAdapter();
	}

	@Override
	public void onClick(View v) {
		if (v == tabs[0])
			selectTab(TAB1);
		else if (v == tabs[1])
			selectTab(TAB2);
		else if (v == tabs[2])
			selectTab(TAB3);
		else if (v == tabs[3])
			selectTab(TAB4);

	}

}
