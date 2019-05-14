package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.adapter.HeroAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.ReviewEnemyAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class ReviewArmInManorListWindow extends CustomListViewWindow implements
		OnClickListener {
	private ReviewEnemyAdapter armAdapter = new ReviewEnemyAdapter();
	private HeroAdapter heroAdapter = new HeroAdapter();
	private boolean isHeroShow = false; // true:英雄界面，false:部队界面

	private ImageButton[] tabs;// 分类的图片按钮
	private ImageView[] tabWords;// 分类的图片文字

	// 将领、士兵背景图
	private int[] press = { R.drawable.hero_word_press,
			R.drawable.arm_word_press };// 选中的亮色文字
	private int[] nopress = { R.drawable.hero_word_bg, R.drawable.arm_word_bg };// 未选中的灰色文字

	// 点击按钮的效果
	private int[] bgIds = { R.drawable.tab_btn1, R.drawable.tab_btn1_press };// 图片依次为未点击效果、点击效果
	// 当前选中tab
	private int index = 0;

	public void open() {
		doOpen();
		select(this.index);
	}

	@Override
	public void showUI() {
		if (index < 0 || index > 3)
			return;

		if (index == 0) {
			updateHeroInfo();

		} else {
			updateArmInfo();
		}

		dealwithEmptyAdpter();
		super.showUI();
	}

	@Override
	protected void init() {
		super.init("检阅军队");
		setContentBelowTitle(R.layout.list_2_tabs);
		tabs = new ImageButton[2];
		tabWords = new ImageView[2];
		tabs[0] = (ImageButton) window.findViewById(R.id.tab1);// 将领
		tabs[0].setOnClickListener(this);
		tabWords[0] = (ImageView) window.findViewById(R.id.tabwords1);

		tabs[1] = (ImageButton) window.findViewById(R.id.tab2);// 士兵
		tabs[1].setOnClickListener(this);
		tabWords[1] = (ImageView) window.findViewById(R.id.tabwords2);

		setRightTxt("#arm#" + Account.getArmTotalCount());
		armAdapter.setUserTroopEffectInfo(Account.getUserTroopEffectInfo());
	}

	private void updateHeroInfo() {
		heroAdapter.clear();
		heroAdapter.addItems(Account.getHeroInfos());
		heroAdapter.notifyDataSetChanged();
		setRightTxt("#hero_limit#" + Account.getHeroInfos().size() + "/"
				+ Account.user.getHeroLimit());
	}

	private void updateArmInfo() {
		armAdapter.clear();
		armAdapter.addItems(Account.getArmInfos());
		armAdapter.notifyDataSetChanged();
		setRightTxt("#arm#" + Account.getArmTotalCount());
	}

	@Override
	protected ObjectAdapter getAdapter() {
		armAdapter.addItems(Account.getArmInfos());
		return armAdapter;
	}

	// 框架右部按钮背景
	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	// 传入顶部控件对应的序列值
	private void select(int index) {
		selectTab(index);
		if (index == 0) {
			isHeroShow = true;
			updateHeroInfo();
			adapter = heroAdapter;
		} else {
			isHeroShow = false;
			updateArmInfo();
			adapter = armAdapter;
		}
		listView.setAdapter(adapter);
		dealwithEmptyAdpter();
		showUI();
	}

	// 顶部按钮图片文字偏移
	private void selectTab(int index) {
		this.index = index;
		for (int i = 0; i < tabs.length; i++) {
			if (i == index) {
				tabs[i].setBackgroundResource(bgIds[1]);
				tabWords[i].setBackgroundResource(press[i]);
			} else {
				tabs[i].setBackgroundResource(bgIds[0]);
				tabWords[i].setBackgroundResource(nopress[i]);
			}
		}
	}

	@Override
	protected String getEmptyShowText() {
		if (!isHeroShow)
			return "您的军营中一名士兵都没有<br><br>快去[募兵所]中招募士兵吧";
		else
			return "您的幕府中一名将领都没有<br><br>快去[酒馆]中招募将领吧";
	}

	@Override
	public void onClick(View v) {
		int index = ViewUtil.indexOf(tabs, v);// 遍历控件，对应则返回该控件的数组位置
		if (index != -1) {
			SoundMgr.play(R.raw.sfx_window_open);
			if (this.index != index)
				select(index);
		}
	}
}
