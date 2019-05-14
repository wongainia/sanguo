/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-6 下午2:44:23
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.ReinforceBuyUnitInvoker;
import com.vikings.sanguo.model.BaseGodSoldierInfoClient;
import com.vikings.sanguo.model.PokerReinforce;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.KeyValue;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class AssistGodSoldierWindow extends CustomPopupWindow {
	private RichBattleInfoClient rbic;
	private PokerReinforce juniorRf;
	private PokerReinforce middleRf;
	private PokerReinforce seniorRf;
	private ListView log;
	private boolean isFirstTime = true;
	private AsssitGodSoldierLogAdapter adapter;
	private int pokerType;
	private String title;

	public void open(String title, RichBattleInfoClient rbic) {
		open(title, rbic, PokerReinforce.ASSIST_POKER);
	}

	public void open(String title, RichBattleInfoClient rbic, int battleType) {
		this.title = title;
		this.rbic = rbic;
		if (4 == battleType) // //单挑
			pokerType = PokerReinforce.SINFLED_POKER;
		else
			pokerType = PokerReinforce.ASSIST_POKER;
		doOpen();
	}

	@Override
	protected void init() {
		super.init(title);
		setContent(R.layout.assist_god_soldier);
		log = ((ListView) findViewById(R.id.log));
		getPokerReinforce();
		initTimes();
		setDesc();
		setCard(R.id.layout1, seniorRf);
		setCard(R.id.layout2, middleRf);
		setCard(R.id.layout3, juniorRf);
		initLog();
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	private void getPokerReinforce() {
		try {
			if (PokerReinforce.SINFLED_POKER == pokerType) {
				juniorRf = (PokerReinforce) CacheMgr.pokerReinforceCache
						.get(PokerReinforce.SINGLED_JUNIOR);
				middleRf = (PokerReinforce) CacheMgr.pokerReinforceCache
						.get(PokerReinforce.SINGLED_MIDDLE);
				seniorRf = (PokerReinforce) CacheMgr.pokerReinforceCache
						.get(PokerReinforce.SINGLED_SENIOR);
			} else {
				juniorRf = (PokerReinforce) CacheMgr.pokerReinforceCache
						.get(PokerReinforce.ASSIST_JUNIOR);
				middleRf = (PokerReinforce) CacheMgr.pokerReinforceCache
						.get(PokerReinforce.ASSIST_MIDDLE);
				seniorRf = (PokerReinforce) CacheMgr.pokerReinforceCache
						.get(PokerReinforce.ASSIST_SENIOR);
			}
		} catch (GameException e) {
			e.printStackTrace();
		}
	}

	private void setDesc() {
		if (juniorRf != null) {
			new ViewImgScaleCallBack(juniorRf.getIcon(),
					window.findViewById(R.id.armIcon),
					120 * Config.SCALE_FROM_HIGH, 120 * Config.SCALE_FROM_HIGH);
			try {
				TroopProp prop = (TroopProp) CacheMgr.troopPropCache
						.get(juniorRf.getTroopId());
				ViewUtil.setText(window.findViewById(R.id.armName),
						prop.getName());
				ViewUtil.setRichText(window.findViewById(R.id.armDesc),
						prop.getDesc());
			} catch (GameException e) {
				e.printStackTrace();
			}

		}
	}

	private void setCard(int resId, final PokerReinforce pr) {
		ViewGroup card = (ViewGroup) window.findViewById(resId);
		ViewUtil.setRichText(card, R.id.price, "#rmb# x" + pr.getCost(), true);
		ViewUtil.setText(card, R.id.desc, pr.getDesc());
		View view = card.findViewById(R.id.btn);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rbic.getAutoBattleTime() > 0) {
					if (pr.getCost() > Account.user.getCurrency()) {
						new ToActionTip(pr.getCost()).show();
						return;
					}
					new ReinforceBuyUnitInvoker(rbic, pr.getCost(), pr
							.getType(), new CallBack() {
						@Override
						public void onCall() {
							refresh();
						}
					}).start();
				} else {
					controller.alert("战争已自动开启，无法支援神兵！");
				}
			}
		});
	}

	private void refresh() {
		setRightTxt("#rmb#" + Account.user.getCurrency());
		setTotal();
		setDesc();
		adapter.clear();
		adapter.addItems(Account.assistCache.getAssistGodSoldier(rbic.getId(),
				rbic.getUniqueId()));
		adapter.notifyDataSetChanged();
	}

	private void setTotal() {
		ViewUtil.setRichText(
				window,
				R.id.total,
				"共花费 "
						+ Account.assistCache.getTotalCost(rbic.getId(),
								rbic.getUniqueId())
						+ "元宝，招募"
						+ CalcUtil.turnToHundredThousand(Account.assistCache
								.getTotalTroopAmount(rbic.getId(),
										rbic.getUniqueId())) + " 名神兵", true);
	}

	private void initLog() {
		if (0 == Account.assistCache.getTotalCost(rbic.getId(),
				rbic.getUniqueId()))
			ViewUtil.setRichText(window, R.id.total, "暂未支援神兵", true);
		else
			setTotal();

		adapter = new AsssitGodSoldierLogAdapter();
		adapter.addItems(Account.assistCache.getAssistGodSoldier(rbic.getId(),
				rbic.getUniqueId()));
		log.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void initTimes() {
		KeyValue val = rbic.getBattleInfo().getBbic()
				.getUsersBuyUnit(Account.user.getId());
		if (null != val) {
			Account.assistCache.setTimes(rbic.getId(), rbic.getUniqueId(), val
					.getValue().intValue());
		}
	}

	@Override
	public void showUI() {
		super.showUI();
		setValue();
	}

	private void setValue() {
		setRightTxt("#rmb#" + Account.user.getCurrency());
		setLeftTime();
		setWarStart();
	}

	private void setWarStart() {
		if (rbic.getAutoBattleTime() < 0 && isFirstTime) {
			isFirstTime = false;
			controller.alert("战争已自动开启，无法支援神兵！", new CallBack() {
				@Override
				public void onCall() {
					controller.goBack();
				}
			});
		}
	}

	private void setLeftTime() {
		if (null == rbic)
			return;
		ViewUtil.setRichText(window, R.id.gradientMsg,
				rbic.getRedCountDownDesc(), true);
	}

	@Override
	protected int refreshInterval() {
		return 1000;
	}

	private class AsssitGodSoldierLogAdapter extends ObjectAdapter {

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			BaseGodSoldierInfoClient baseInfo = (BaseGodSoldierInfoClient) getItem(index);
			ViewUtil.setRichText(v, baseInfo.toString());
		}

		@Override
		public int getLayoutId() {
			return R.layout.battle_log_txt;
		}
	}
}
