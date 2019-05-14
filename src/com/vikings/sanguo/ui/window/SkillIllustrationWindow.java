/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-8-30 上午11:13:07
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.ui.adapter.SkillIllustrationAdpater;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class SkillIllustrationWindow extends CustomPopupWindow {
	private GridView grid;
	private List<Item> skillIllustrations;

	@Override
	protected void init() {
		super.init("技能图鉴");
		setLeftBtn("效果说明", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SkillDirectWindow().doOpen();
			}
		});
		setRightTxt("共计:" + skillIllustrations.size());
		setContent(R.layout.hero_statistics);
		grid = (GridView) content.findViewById(R.id.gridView);

		SkillIllustrationAdpater adapter = new SkillIllustrationAdpater();
		adapter.addItems(skillIllustrations);
		grid.setAdapter(adapter);
	}

	public void open() {
		skillIllustrations = CacheMgr.itemCache.getIllustrations();
		doOpen();
	}

}
