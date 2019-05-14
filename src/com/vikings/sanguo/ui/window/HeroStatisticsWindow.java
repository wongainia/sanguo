package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.widget.GridView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.ui.adapter.HeroStatisticsAdapter;
import com.vikings.sanguo.widget.CustomPopupWindow;

/**
 * 将领图鉴
 * 
 * @author susong
 * 
 */
public class HeroStatisticsWindow extends CustomPopupWindow {

	private GridView grid;

	private HeroStatisticsAdapter adapter = new HeroStatisticsAdapter();
	private List<HeroProp> heros;

	@Override
	protected void init() {
		super.init("将领图鉴");
		setRightTxt("共计:" + heros.size());
		setContent(R.layout.hero_statistics);
		grid = (GridView) content.findViewById(R.id.gridView);
		adapter.addItems(heros);
		grid.setAdapter(adapter);
	}

	private List<HeroProp> getHeros() {
		List<HeroProp> list = CacheMgr.heroPropCache.getAll();
		List<HeroProp> filterHeroProps = new ArrayList<HeroProp>();
		for (HeroProp heroProp : list) {
			if (heroProp.isShowIllustrations()
					&& !filterHeroProps.contains(heroProp)) {
				filterHeroProps.add(heroProp);
			}
		}
		Collections.sort(filterHeroProps, new Comparator<HeroProp>() {
			@Override
			public int compare(HeroProp prop1, HeroProp prop2) {
				return prop1.getSequence() - prop2.getSequence();
			}
		});
		return filterHeroProps;
	}

	public void open() {
		heros = getHeros();
		doOpen();
	}

	// 框体右按钮背景
	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

}
