/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-16 下午5:39:52
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.util.FloatMath;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.ManorReviveInvoker;
import com.vikings.sanguo.message.ManorReviveReq;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.UserInfoHeadData;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.RevivedItemAdapter;
import com.vikings.sanguo.ui.alert.RechargeBuyGiftTip;
import com.vikings.sanguo.ui.alert.RevivedShowAlert;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class ManorReviveWindow extends CustomPopupWindow implements
		OnClickListener {
	private BuildingProp bp;

	private ImageButton armBtn, bossBtn;
	private ImageView armIv, bossIv;
	private List<Integer> ids = new ArrayList<Integer>();// 选中的士兵数量
	private int[] armBg = { R.drawable.armbtn_word_bg, R.drawable.armbtn_word };// 士兵图片文字样式
	private int[] bossBg = { R.drawable.boss_word_bg, R.drawable.boss_word }; // boss图片文字样式
	private int[] bgIds = { R.drawable.tab_btn1, R.drawable.tab_btn1_press };// 按钮图片
	private GridView grid;
	private View revivedShow;
	private TextView textView;
	private long cdTime;

	// 当前选中tab
	private int index = 0;
	private RevivedItemAdapter revivedItemAdapter;

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.tab1) {
			armBtn.setBackgroundResource(bgIds[1]);
			armIv.setBackgroundResource(armBg[1]);

			bossBtn.setBackgroundResource(bgIds[0]);
			bossIv.setBackgroundResource(bossBg[0]);

			index = 0;
		} else if (v.getId() == R.id.tab2) {
			bossBtn.setBackgroundResource(bgIds[1]);
			bossIv.setBackgroundResource(bossBg[1]);

			armBtn.setBackgroundResource(bgIds[0]);
			armIv.setBackgroundResource(armBg[0]);

			index = 1;
		}
		dealwithEmptyAdpter(revivedItemAdapter, index);
		updateByCD(index);
	}

	public void open(BuildingProp bp) {
		this.bp = bp;
		revivedItemAdapter = new RevivedItemAdapter(bp, ids,
				new ButtonCallback(), index);
		doOpen();
		selevt();
	}

	@Override
	protected void init() {
		super.init("医    馆");
		setContent(R.layout.manor_revived_top);

		// 士兵按钮
		armBtn = (ImageButton) window.findViewById(R.id.tab1);
		armBtn.setOnClickListener(this);
		armIv = (ImageView) window.findViewById(R.id.armImage);

		// boss按钮
		bossBtn = (ImageButton) window.findViewById(R.id.tab2);
		bossBtn.setOnClickListener(this);
		bossIv = (ImageView) window.findViewById(R.id.bossImage);

		grid = (GridView) content.findViewById(R.id.gridView_revive);

		ViewGroup revivedLayout = (ViewGroup) window
				.findViewById(R.id.manor_revivedLayout);
		revivedShow = controller.inflate(R.layout.empty_show, revivedLayout,
				false);
		revivedLayout.addView(revivedShow);

		TextView emptyText = (TextView) revivedShow
				.findViewById(R.id.emptyDesc);
		emptyText.setTextSize(18);

		setContentAboveTitle(R.layout.user_info_head);
		setContentBelowTitle(R.layout.gradient_msg);

		textView = (TextView) window.findViewById(R.id.gradientMsg);
		textView.setTextSize(14);

		ViewUtil.setUserInfoHeadAttrs(window,
				UserInfoHeadData.getSpecialDatas1(), true, Account.user);
		setBottomPadding();
	}

	// 框体左按钮背景
	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	// 放弃治疗按钮
	protected void setRightBtn(String str, OnClickListener onclick, int index) {
		View rightBtn = findViewById(R.id.rightBtn);
		if (0 == index) {
			Bitmap rt = controller.getMirrorBitmap("window_lt1");
			ViewUtil.setImage(window, R.id.rightBtn, rt);
			ViewUtil.setVisible((TextView) findViewById(R.id.rightText));
			ViewUtil.setBold((TextView) findViewById(R.id.rightText));
			ViewUtil.setRichText(window, R.id.rightText, str, true);
			rightBtn.setOnClickListener(onclick);
		}
		if (1 == index) {
			Bitmap rt = controller.getMirrorBitmap("window_lt3");
			ViewUtil.setImage(window, R.id.rightBtn, rt);
			ViewUtil.setGone((TextView) findViewById(R.id.rightText));
			rightBtn.setOnClickListener(null);
		}

	}

	@Override
	public void showUI() {
		super.showUI();

		setLeftBtn("医馆说明", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new RevivedShowAlert().show();
			}
		});

		if (index < 0 || index > 1) {
			return;
		} else {
			updateByCD(index);
		}

	}

	@SuppressWarnings("unchecked")
	private void getAdapterView(int index) {
		revivedItemAdapter.getContent().clear();
		revivedItemAdapter.setIndex(index);
		revivedItemAdapter.getContent().addAll(
				Account.myLordInfo.getReviveInfo(index));
		revivedItemAdapter.fillEmpty();
		revivedItemAdapter.notifyDataSetChanged();
		dealwithEmptyAdpter(revivedItemAdapter, index);
	}

	// 选择的士兵数量
	private int getArmCount() {
		int armCount = 0;
		List<ArmInfoClient> arms = Account.myLordInfo.getReviveInfo();

		if (!ListUtil.isNull(ids) && !ListUtil.isNull(arms)) {
			for (int i = 0; i < ids.size(); i++) {
				for (ArmInfoClient armInfo : arms) {
					if (armInfo.getId() == ids.get(i)) {
						armCount += armInfo.getCount();
					}
				}
			}

		}
		return armCount;

	}

	// 士兵救治花费
	private int getCost() {
		List<ArmInfoClient> arms = Account.myLordInfo.getReviveInfo();
		int cost = 0;
		if (!ListUtil.isNull(ids) && !ListUtil.isNull(arms)) {
			for (int i = 0; i < ids.size(); i++) {
				for (ArmInfoClient armInfo : arms) {
					if (armInfo.getId() == ids.get(i)) {

						cost += (int) FloatMath.ceil(CacheMgr.manorReviveCache
								.getArmReviveCose(bp.getLevel(), armInfo,
										Account.myLordInfo.getCount(),
										armInfo.getCount()));
					}
				}
			}

		}
		return cost;
	}

	private String getadsaadCost() {
		String noCost = StringUtil.color(getCost() + "", R.color.color11);
		if (!checkCondition()) {
			return noCost;
		} else {
			return getCost() + "";
		}
	}

	// 士兵消费是否足够金币
	protected boolean checkCondition() {
		if (getCost() > Account.user.getMoney()) {
			return false;
		}
		return true;

	}

	private void getCostNoEnough() {
		if (AttrData.isResource(AttrType.ATTR_TYPE_MONEY.number))
			new RechargeBuyGiftTip(AttrType.ATTR_TYPE_MONEY.number).show();
		else
			controller.alert("你的金币不足，不能救治这些士兵");
	}

	private void updateByCD(int index) {

		getAdapterView(index);
		int cd = CacheMgr.manorReviveCache.getReviveCD(bp);
		if (index == 0) {
			setRightBtn("放弃治疗", new OnClickListener() {
				@Override
				public void onClick(View v) {
					controller.confirm("放弃治疗", CustomConfirmDialog.DEFAULT,
							StringUtil.color("确定要放弃医馆中的伤兵吗？", R.color.color8),
							StringUtil.color("（放弃后将清空医馆）", R.color.color24),
							new CallBack() {

								@Override
								public void onCall() {
									// 放弃治疗
									new ManorReviveDeleteInvoker().start();
								}
							}, null);
				}
			}, index);

			disableReviveBtn();
			int[] cnt = CacheMgr.buildingEffectCache.getReviveCount(bp, index);
			if (cd > 0) {
				cdTime = cd * 1000l;

			} else if (ListUtil.isNull(Account.myLordInfo.getReviveInfo())) {
				ViewUtil.setText(textView, "目前没有受伤的士兵");
			} else {
				ViewUtil.setVisible(belowBtnFrame);

				if (ListUtil.isNull(ids)) {
					ViewUtil.setText(textView, "请选择要救治的士兵");
				} else {
					ViewUtil.setText(textView, "已选择 " + getArmCount()
							+ " 人（最多选择 " + cnt[0] + "人）");
					enableReviveBtn();

				}
			}
			if (Account.user.getWeakLeftTime() > 0) {
				ViewUtil.setRichText(textView,
						StringUtil.color("你被屠城了， 不能救治士兵", R.color.color24));
			}

			Object obj = belowBtn.getTag();
			TimerCount timerCount = null;
			if (null != obj) {
				timerCount = (TimerCount) obj;
				timerCount.cancel();
			}
			if (cdTime > 0) {
				// 刷新控件内容的线程
				timerCount = new TimerCount(textView, cdTime, 1000);
				timerCount.start();
				belowBtn.setTag(timerCount);
			}

		} else if (index == 1) {
			Object obj = belowBtn.getTag();
			TimerCount timerCount = null;
			if (null != obj) {
				timerCount = (TimerCount) obj;
				timerCount.cancel();
			}
			setRightBtn(null, null, index);
			ViewUtil.setText(textView, "点击BOSS头像，单独复活");
			ViewUtil.setGone(belowBtnFrame);
		}
	}

	private void disableReviveBtn() {
		ViewUtil.setVisible(belowBtnFrame);
		belowBtn.setEnabled(false);
		setBottomButton(StringUtil.color("救  治", R.color.color17), null);
	}

	private void enableReviveBtn() {
		ViewUtil.setVisible(belowBtnFrame);
		belowBtn.setEnabled(true);

		setBottomButton("救  治#money#" + getadsaadCost(), new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkCondition()) {
					getCostNoEnough();
				} else {
					new ManorTimeInvoker(ManorReviveReq.TARGET_ARM, bp.getId(),
							ids, 0, 0).start();
				}
			}
		});

	}

	private void selevt() {
		grid.setAdapter(revivedItemAdapter);
		grid.setNumColumns(3);
		showUI();
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	protected void dealwithEmptyAdpter(ObjectAdapter adapter, int index) {
		if (adapter.isEmpty()) {// 内容展示列表为空
			ViewUtil.setGone(grid);
			ViewUtil.setVisible(revivedShow);
			ViewUtil.setRichText(revivedShow, R.id.emptyDesc,
					getEmptyShowText(index));
		} else {
			ViewUtil.setVisible(grid);
			ViewUtil.setGone(revivedShow);
		}
	}

	// 无展示列表显示
	private String getEmptyShowText(int index) {
		if (index == 0) {
			return "目前没有受伤的士兵";
		} else if (index == 1) {
			return "目前没有受伤的BOSS";
		} else {
			return "";
		}

	}

	public GridView getGridView() {
		return grid;
	}

	@Override
	protected int refreshInterval() {
		if (Account.user.getWeakLeftTime() > 0) {
			return 1000;
		} else {
			return 0;
		}
	}

	@Override
	protected void refreshUI() {
		if (Account.user.getWeakLeftTime() > 0 && index == 0/* 士兵选项卡 */) {
			ViewUtil.disableButton(belowBtn);
		} else {
			ViewUtil.enableButton(belowBtn);
		}
	}

	public class ManorReviveDeleteInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "放弃医疗中...";
		}

		@Override
		protected String failMsg() {
			return "放弃医疗失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().manorReviveClean();
		}

		@Override
		protected void onOK() {
			ctr.alert("成功放弃治疗！");
		}

	}

	// 救治Button显示消耗的操作
	public class ButtonCallback implements CallBack {

		@Override
		public void onCall() {
			int cd = CacheMgr.manorReviveCache.getReviveCD(bp);
			disableReviveBtn();
			int[] cnt = CacheMgr.buildingEffectCache.getReviveCount(bp, index);

			if (cd <= 0) {
				if (ListUtil.isNull(Account.myLordInfo.getReviveInfo())) {
					ViewUtil.setText(textView, "目前没有受伤的士兵");
				} else {
					ViewUtil.setVisible(belowBtnFrame);

					if (ListUtil.isNull(ids)) {
						ViewUtil.setText(textView, "请选择要救治的士兵");
					} else {
						ViewUtil.setText(textView, "已选择 " + getArmCount()
								+ " 人（最多选择 " + cnt[0] + "人）");
						enableReviveBtn();

					}
				}
			}

		}
	}

	// 倒计时的刷新view线程（医馆冷却时间倒计时）
	public class TimerCount extends CountDownTimer {
		private View timeView;

		// 参数依次为millisInFuture总时长,和countDownInterval计时的时间间隔
		public TimerCount(View timeView, long millisInFuture,
				long countDownInterval) {
			super(millisInFuture, countDownInterval);
			this.timeView = timeView;
		}

		// 计时过程显示
		@Override
		public void onTick(long millisUntilFinished) {
			belowBtn.setClickable(false);
			if (timeView != null)
				ViewUtil.setRichText(
						timeView,
						"医馆休息："
								+ StringUtil.color(
										DateUtil._formatTime((int) (millisUntilFinished / 1000)),
										R.color.color24) + "后可再次救治");
		}

		// 计时完毕时触发
		@Override
		public void onFinish() {
			disableReviveBtn();
			if (ListUtil.isNull(Account.myLordInfo.getReviveInfo())) {
				ViewUtil.setText(timeView, "目前没有受伤的士兵");
			} else {
				if (getArmCount() <= 0) {
					ViewUtil.setText(timeView, "请选择要救治的士兵");
				} else {
					int[] cnt = CacheMgr.buildingEffectCache.getReviveCount(bp,
							index);
					ViewUtil.setText(timeView, "已选择 " + getArmCount()
							+ " 人（最多选择 " + cnt[0] + "人）");
					enableReviveBtn();
					belowBtn.setClickable(true);
				}
			}
			cdTime = 0;
		}

	}

	private class ManorTimeInvoker extends ManorReviveInvoker {

		@Override
		protected void onOK() {
			super.onOK();
			ViewUtil.setUserInfoHeadAttrs(window,
					UserInfoHeadData.getSpecialDatas1(), true, Account.user);
		}

		public ManorTimeInvoker(int target, int buildingId,
				List<Integer> armIds, int bossArmId, int bossCount) {
			super(target, buildingId, armIds, bossArmId, bossCount);

			if (cdTime > 0) {
				// 刷新控件内容的线程
				TimerCount timerCount = new TimerCount(textView, cdTime, 1000);
				timerCount.start();
				belowBtn.setTag(timerCount);
			}
		}

	}
}
