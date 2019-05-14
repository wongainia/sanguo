package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArenaUserRankInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.guide.Step901;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.ArenaWindowTab;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class ArenaWindow extends CustomBaseListWindow implements
		OnClickListener {
	static public final int TAB1 = 0;
	static public final int TAB2 = 1;
	static public final int TAB3 = 2;

	private ImageButton[] tabs;
	private ArenaFightTab fightTab;
	private ArenaAwardTab awardTab;
	private ArenaLogTab logTab;

	private int[] txts = { R.drawable.txt_arena_dj, R.drawable.txt_arena_jl,
			R.drawable.txt_arena_rz };

	private int[] txtPresses = { R.drawable.txt_arena_dj_press,
			R.drawable.txt_arena_jl_press, R.drawable.txt_arena_rz_press };

	private int[] bgIds = { R.drawable.tab_btn1, R.drawable.tab_btn1_press };

	// 当前选中tab
	private int index;

	@Override
	protected void init() {
		initTabs();
		super.init("巅峰战场");
		setLeftBtn("规则说明", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new RuleSpecTip("巅峰战场规则说明", CacheMgr.uiTextCache
						.getTxt(UITextProp.HERO_ARENA_DESC)).show();
			}
		});
		setContentBelowTitle(R.layout.pinnacle_battle_field);
		tabs = new ImageButton[3];
		tabs[0] = (ImageButton) findViewById(R.id.tab1);
		tabs[0].setOnClickListener(this);

		tabs[1] = (ImageButton) findViewById(R.id.tab2);
		tabs[1].setOnClickListener(this);

		tabs[2] = (ImageButton) findViewById(R.id.tab3);
		tabs[2].setOnClickListener(this);

		setWindow();

		if (!Account.myLordInfo.isArenaGraded())
			controller.alert("你需要一场定级赛来获取排名");

		// 巅峰战场 引导
		StepMgr.checkStep901();

	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	private void initTabs() {
		fightTab = new ArenaFightTab();
		awardTab = new ArenaAwardTab(new CallBack() {

			@Override
			public void onCall() {
				setExploit();

			}
		});
		logTab = new ArenaLogTab();
	}

	private void setWindow() {
		fightTab.setWindow(this);
		awardTab.setWindow(window);
		logTab.setWindow(window);
	}

	private ArenaWindowTab getSelTab() {
		if (TAB1 == index)
			return fightTab;
		else if (TAB2 == index)
			return awardTab;
		else
			return logTab;
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		getSelTab().getServerData(resultPage);
	}

	@Override
	public void handleItem(Object o) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (getSelTab().needScroll()) {
			super.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	@Override
	public void firstPage() {
		resultPage = new ResultPage();
		getAdapter().clear();
		getAdapter().notifyDataSetChanged();
		fetchData();
	}

	@Override
	public void showUI() {
		getSelTab().showUI();
		super.showUI();
	}

	@Override
	protected void updateUI() {
		List ls = resultPage.getResult();
		if (ls != null && ls.size() != 0) {
			ListUtil.deleteRepeat(ls, getAdapter().getContent());
			getAdapter().addItems(ls);
		}
		resultPage.addIndex(Math.max(ls.size(), resultPage.getPageSize()));
		resultPage.clearResult();
		listView.setAdapter(getAdapter());
		getAdapter().notifyDataSetChanged();
		dealwithEmptyAdpter();
		if (index == TAB1) {
			List<ArenaUserRankInfoClient> list = getAdapter().getContent();
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getUserId() == Account.user.getId()) {
						listView.setSelection(i);
						break;
					}
				}
			}
		}
		showUI();
	}

	@Override
	protected void dealwithEmptyAdpter() {
		if (emptyShow == null)
			return;
		if (getAdapter().isEmpty()) {
			ViewUtil.setGone(listView);
			ViewUtil.setVisible(emptyShow);
			ViewUtil.setRichText(window, R.id.emptyDesc, getSelTab()
					.getEmptyShowText());
		} else {
			showListView();
		}
	}

	private void showListView() {
		ViewUtil.setVisible(listView);
		ViewUtil.setGone(emptyShow);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return getSelTab().getAdapter();
	}

	@Override
	protected int refreshInterval() {
		return getSelTab().refreshInterval();
	}

	public void open() {
		int arenaLvl = CacheMgr.dictCache.getDictInt(Dict.TYPE_ARENA, 1);
		if (Account.user.getLevel() < arenaLvl) {
			controller.alert("你的等级不够，需要达到" + arenaLvl + "级以上才能进入巅峰战场！");
			return;
		}
		open(TAB1);
	}

	private void open(int idx) {
		index = idx;
		doOpen();
		selectTab(index);
	}

	@Override
	public void onClick(View v) {
		int index = ViewUtil.indexOf(tabs, v);
		if (index != -1) {
			SoundMgr.play(R.raw.sfx_window_open);
			if (this.index != index) {
				listView.setAdapter(null);
				showListView();
				ViewUtil.setGone(findViewById(R.id.listTitle));
				ViewUtil.setGone(findViewById(R.id.bonusLayout));
				selectTab(index);
				firstPage();
			}
		}
	}

	private void selectTab(int index) {
		this.index = index;
		ViewUtil.selectTab(tabs, txts, txtPresses, bgIds, index);
		if (index == TAB1) {
			changeBtn(rightBtnLayout, "", WINDOW_BTN_BG_TYPE_NULL);
		} else if (index == TAB2) {
			setExploit();
		} else if (index == TAB3) {
			changeBtn(rightBtnLayout, "", WINDOW_BTN_BG_TYPE_NULL);
		}
	}

	protected void setExploit() {
		changeBtn(rightBtnLayout, "#exploit#" + Account.user.getExploit(),
				WINDOW_BTN_BG_TYPE_DESC);
		//更新适配器
		getAdapter().notifyDataSetChanged();
	}
}