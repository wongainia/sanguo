package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.OtherHeroAdapter;
import com.vikings.sanguo.ui.adapter.ReviewEnemyAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class ReviewOtherArmInManorListWindow extends CustomListViewWindow
		implements OnClickListener {
	private ReviewEnemyAdapter armAdapter = new ReviewEnemyAdapter();
	private OtherHeroAdapter heroAdapter = new OtherHeroAdapter();

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

	private OtherUserClient ouc;

	public void open(OtherUserClient ouc) {
		this.ouc = ouc;
		if (ouc.getArmInfos() == null || ouc.getOhics() == null) {
			new QueryInfoInvoker().start();
		} else {
			doOpen();
			select(this.index);
		}
	}

	@Override
	public void showUI() {
		if (index < 0 || index > 1)
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
		armAdapter.setUserTroopEffectInfo(ouc.getTroopEffectInfo());
		heroAdapter.setOtherUser(ouc);
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

		setRightTxt("#arm#" + getTotalArmCount());
	}

	private void updateHeroInfo() {
		heroAdapter.clear();
		heroAdapter.addItems(ouc.getOhics());
		adapter = heroAdapter;
		setRightTxt("#hero_limit#" + getHeroCount() + "/" + ouc.getHeroLimit());
	}

	private void updateArmInfo() {
		armAdapter.clear();
		armAdapter.addItems(ouc.getArmInfos());
		adapter = armAdapter;
		setRightTxt("#arm#" + getTotalArmCount());
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
			updateHeroInfo();
			adapter = heroAdapter;
		} else {
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

	private int getTotalArmCount() {
		int count = 0;
		if (null != ouc && null != ouc.getArmInfos())
			for (ArmInfoClient info : ouc.getArmInfos()) {
				count += info.getCount();
			}
		return count;
	}

	private int getHeroCount() {
		int count = 0;
		if (null != ouc && null != ouc.getOhics())
			count = ouc.getOhics().size();
		return count;
	}

	@Override
	protected ObjectAdapter getAdapter() {
		armAdapter.addItems(ouc.getArmInfos());
		return armAdapter;
	}

	@Override
	protected String getEmptyShowText() {
		if (adapter instanceof ReviewEnemyAdapter)
			return "没有士兵";
		else
			return "没有将领";
	}

	private class QueryInfoInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "加载数据";
		}

		@Override
		protected String failMsg() {
			return "加载数据失败";
		}

		@Override
		protected void fire() throws GameException {
			List<ArmInfoClient> arms = GameBiz.getInstance()
					.otherLordTroopInfoQuery(ouc.getId().intValue());
			ouc.setArmInfos(arms);
			List<OtherHeroInfoClient> heros = GameBiz.getInstance()
					.otherUserHeroInfoQuery(ouc.getId(), null);
			ouc.setOhics(heros);
		}

		@Override
		protected void onOK() {
			doOpen();
			select(index);
		}

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
