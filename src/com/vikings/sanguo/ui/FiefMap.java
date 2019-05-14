package com.vikings.sanguo.ui;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.vikings.sanguo.R;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.invoker.RecommendInvoker;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.ui.alert.FiefAttackTip;
import com.vikings.sanguo.ui.alert.FiefSearchTip;
import com.vikings.sanguo.ui.map.BattleMap;
import com.vikings.sanguo.ui.window.LogWindow;
import com.vikings.sanguo.ui.window.PopupWindow;
import com.vikings.sanguo.utils.ViewUtil;

public class FiefMap extends PopupWindow implements OnClickListener {

	private ViewGroup window;

	// 地图主界面
	private BattleMap battleMap;

	private MyFiefUI myFiefUI;

	private Button plunderBt, refreshBt, attackBt, searchBt, logBt, homeBt;

	private boolean first = false;

	private boolean isBackBtnVisibale;

	public FiefMap() {
		window = (ViewGroup) controller.inflate(R.layout.my_fief,
				(ViewGroup) controller.findViewById(R.id.fiefMap), false);
		((ViewGroup) controller.findViewById(R.id.fiefMap)).addView(window,
				new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
		battleMap = new BattleMap();

		myFiefUI = new MyFiefUI();

		refreshBt = (Button) window.findViewById(R.id.refreshBt);
		refreshBt.setOnClickListener(this);

		attackBt = (Button) window.findViewById(R.id.attackBt);
		attackBt.setOnClickListener(this);
		searchBt = (Button) window.findViewById(R.id.searchBt);
		searchBt.setOnClickListener(this);
		logBt = (Button) window.findViewById(R.id.logBt);
		logBt.setOnClickListener(this);
		homeBt = (Button) window.findViewById(R.id.homeBt);
		homeBt.setOnClickListener(this);
		plunderBt = (Button) window.findViewById(R.id.plunderBt);
		plunderBt.setOnClickListener(this);
	}

	public Button getRefreshBt() {
		return refreshBt;
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	public void updateMyFief() {
		this.myFiefUI.updateFief();
		battleMap.refreshMap();
	}

	public BattleMap getBattleMap() {
		return battleMap;
	}

	public MyFiefUI getMyFiefUI() {
		return myFiefUI;
	}

	@Override
	public void showUI() {
		super.showUI();
		updateMyFief();
		ViewUtil.setText(
				window,
				R.id.fiefCount,
				"已占领" + Account.richFiefCache.getResourceFiefCount()
						+ "个资源点（最多占领"
						+ Account.manorInfoClient.getMaxResource() + "个）");
		isBackBtnVisibale = controller.getBackKeyValid();
		controller.setBackBt(false);
		NotifyWorldChatMsg notifyWorldChatMsg = controller
				.getNotifyWorldChatMsg();
		if (null != notifyWorldChatMsg)
			notifyWorldChatMsg.setMarginTop(205);
	}

	public void open() {
		this.doOpen();
		battleMap.moveToFief(Account.manorInfoClient.getPos(), false);
		checkFirst();
	}

	public void backToMap() {
		this.doOpen();
	}

	@Override
	protected void init() {

	}

	@Override
	protected void destory() {
		controller.setBackBt(isBackBtnVisibale);
	}

	@Override
	public void onClick(View v) {
		if (v == logBt) {
			new LogWindow().doOpen();
		} else if (v == homeBt) {
			controller.goBack();
		} else if (v == attackBt) {
			new FiefAttackTip().show();
		} else if (v == refreshBt) {
			controller.getBattleMap().refreshCurFief();
		} else if (v == searchBt) {
			new FiefSearchTip().show();
		} else if (v == plunderBt) {
			// new FatSheepTip().show();
			new RecommendInvoker().start();
		}
	}

	private void checkFirst() {
		if (first) {
			first = false;
			int lastFiefCount = PrefAccess.getLastFiefCount();
			List<Long> fiefids = Account.richFiefCache.getFiefids();

			int lostFiefCount = lastFiefCount - fiefids.size();
			if (lostFiefCount < 0)
				lostFiefCount = 0;

			int battleCount = 0;
			List<BriefBattleInfoClient> battles = Account.briefBattleInfoCache
					.getAll();
			if (battles.size() > 0 && fiefids.size() > 0) {
				for (BriefBattleInfoClient b : battles) {
					if (fiefids.contains(b.getBattleid()))
						battleCount++;
				}
			}
		}
	}

}
