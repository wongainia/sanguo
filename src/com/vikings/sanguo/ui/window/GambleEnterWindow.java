/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-22
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui.window;

import java.util.Date;
import java.util.List;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.MachinePlayType;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.MachinePlayStatInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.sound.MediaPlayerMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.LuckyAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.alert.GambleLuckyTip;
import com.vikings.sanguo.ui.alert.SystemNotifyTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class GambleEnterWindow extends CustomBaseListWindow implements
		OnClickListener {
	private View juniorMachine; // 初级水果机
	private View middleMachine; // 中级水果机
	private View advancedMachine; // 高级水果机

	private TextView juniorPrice; // 初级水果机价格
	private TextView middlePrice; // 中级水果机价格
	private TextView advancedPrice; // 高级水果机价格

	// 幸运大榜对于三种水果机里中奖价值较高的最新前10名用户进行公布
	private final static int LUCK_LIST_CNT = 10;

	@Override
	protected void init() {
		super.init("幸运转转转");
		setLeftBtn("说      明", new OnClickListener() {
			@Override
			public void onClick(View v) {
//				new GambleDirectWindow().doOpen();
			}
		});
		setContentBelowTitle(R.layout.gamble_enter);

//		juniorMachine = bindButton(window, R.id.junior, this);
//		middleMachine = bindButton(window, R.id.middle, this);
//		advancedMachine = bindButton(window, R.id.advanced, this);
//
//		juniorPrice = (TextView) findViewById(R.id.jPrice);
//		middlePrice = (TextView) findViewById(R.id.mPrice);
//		advancedPrice = (TextView) findViewById(R.id.aPrice);
//
//		ViewUtil.setText(window, R.id.jName,
//				CacheMgr.gambleCache.getJuniorName());
//		ViewUtil.setText(window, R.id.mName,
//				CacheMgr.gambleCache.getMiddleName());
//		ViewUtil.setText(window, R.id.sName,
//				CacheMgr.gambleCache.getAdvancedName());

		setMachineEnable(CacheMgr.gambleCache.getJuniorPrice(), juniorMachine,
				juniorPrice);
		setMachineEnable(CacheMgr.gambleCache.getMiddlePrice(), middleMachine,
				middlePrice);
		setMachineEnable(CacheMgr.gambleCache.getAdvancedPrice(),
				advancedMachine, advancedPrice);
	}

	private void setMachineEnable(short price, View machine, TextView priceV) {
		if (0 == price) {
			machine.setEnabled(false);
			// machine.setBackgroundResource(R.drawable.gamble_gray);
			priceV.setText(Html.fromHtml("<font color='#F8362C'>" + "暂未开放"
					+ "</font>"));
		} else {
			machine.setEnabled(true);
			machine.setOnClickListener(this);
			ViewUtil.setRichText(priceV,
					"#rmb#" + Html.fromHtml(getPriceString(price)), true);
		}
	}

//	private void setFreeTimesDesc() {
//		if (0 != Account.readLog.FREE_TIMES)
//			ViewUtil.setRichText(
//					window,
//					R.id.times_desc,
//					"您可免费体验"
//							+ StringUtil.color(
//									String.valueOf(Account.readLog.FREE_TIMES),
//									"#F8362C") + "次"
//							+ CacheMgr.gambleCache.getJuniorName());// 初级幸运轮盘
//		else {
//			String free = "免费次数已用完，"
//					+ "<font color='#F8362C'>"
//					+ DateUtil.getMonthDay(new Date(
//							Account.readLog.FREE_RESET_TIME * (long) 1000))
//					+ "</font>" + "可再次免费体验";
//			ViewUtil.setRichText(window, R.id.times_desc, free);
//		}
//	}

	private String getPriceString(short price) {
		return "<font color='#F8362C'>" + price + "</font>"
				+ "<font color='#5D2B16'>" + "/次" + "</font>";
	}

	public void open() {
		doOpen();

		// 如果是第一次进入水果机界面，弹出奖励规则
		if (!Account.readLog.FIRST_ENTER_GAMBLE) {
			Account.readLog.FIRST_ENTER_GAMBLE = true;
			new SystemNotifyTip(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
					(byte) 9), "幸运转转转说明").show();
		}

		this.firstPage();
	}

	public void guide() {
		firstPage();
	}

	@Override
	public void showUI() {
//		setFreeTimesDesc();
		super.showUI();
	}

	@Override
	public void onClick(View v) {
		MediaPlayerMgr.getInstance().pauseSound();
		if (v == juniorMachine) {
			new GambleDialog().open(MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR,
					new UpdateLuckyList()); // new UpdateDesc(),
		} else if (v == middleMachine) {
			new GambleDialog().open(MachinePlayType.MACHINE_PLAY_TYPE_MIDDLE,
					new UpdateLuckyList()); // new UpdateDesc(),
		} else if (v == advancedMachine) {
			new GambleDialog().open(MachinePlayType.MACHINE_PLAY_TYPE_SENIOR,
					new UpdateLuckyList()); // new UpdateDesc(),
		}
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new LuckyAdapter();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<MachinePlayStatInfoClient> list = GameBiz.getInstance()
				.machinePlayStatData();
		resultPage.setTotal(LUCK_LIST_CNT);
		resultPage.setResult(list); // infoClient
	}

	@Override
	public void handleItem(Object o) {
		MachinePlayStatInfoClient mpsic = (MachinePlayStatInfoClient) o;
		new GambleLuckyTip(mpsic).show();
	}

	class UpdateLuckyList implements CallBack {

		@Override
		public void onCall() {
			GambleEnterWindow.this.firstPage();
		}
	}
}
