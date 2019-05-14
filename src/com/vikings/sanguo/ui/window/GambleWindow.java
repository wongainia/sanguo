package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.LoadGambleImgInvoker;
import com.vikings.sanguo.message.MachinePlayResp;
import com.vikings.sanguo.model.GambleData;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.protos.MachinePlayType;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.GambleRewardTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.ui.wheel.WheelView;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;
import com.vikings.sanguo.widget.SlowOnClick;

public class GambleWindow extends CustomPopupWindow {

	private MachinePlayType machinePlayType;
	private List<GambleData> gambleData;
	private ImageView rateFrame;
	private Button go;
	private WheelView wheel1;
	private WheelView wheel2;
	private WheelView wheel3;
	private int gambleRate; // 中奖倍率
	private boolean isNetOk; // 网络是否正常
	private ArrayList<MachinePlayResp> respList;
	private boolean isExit = true;

	// 每次赌博的起始时间
	private long gamble_start = 0;
	private long gamble_end = 0;

	// 上榜标志
	private boolean isUpdateList = false;
	private CallBack updateLuckyList; // 更新幸运榜回调

	private CallBack rmb;// 更新元宝回调

	private long allStopTime = 0;
	private String failMsg = "";

	// 消息内容
	private final static int RESET = 0;
	private final static int ALL_WHEEL_STOP = 1;
	private final static int WHEEL1_STOP = 2;
	private final static int WHEEL2_STOP = 3;
	private final static int WHEEL3_STOP = 4;

	private View bambleType;

	public void open(MachinePlayType machinePlayType, CallBack list,
			CallBack rmb) {
		this.rmb = rmb;
		this.machinePlayType = machinePlayType;
		this.updateLuckyList = list;
		this.gambleData = CacheMgr.gambleCache
				.getGambleData(this.machinePlayType);

		new LoadGambleImgInvoker(gambleData, new CallBack() {
			@Override
			public void onCall() {
				doOpen();
				freePlayPrompt();
			}
		}).start();
	}

	@Override
	protected void init() {
		super.init("铜雀台");
		setContentBelowTitle(R.layout.gamble_machine);
		setLeftBtn("规则说明", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new RuleSpecTip("铜雀台规则说明", CacheMgr.uiTextCache
						.getTxt(UITextProp.TONGQUETAI_SPEC)).show();
			}
		});
		setRightTxt("#rmb#" + Account.user.getCurrency());
		bambleType = window.findViewById(R.id.bamble_type);
		imageHolder.saveRef(bambleType);
		setGambleType();
		rateFrame = (ImageView) findViewById(R.id.rate);

		go = (Button) findViewById(R.id.go);
		go.setOnClickListener(goClick);

		respList = new ArrayList<MachinePlayResp>();

		ViewUtil.setRichText(window.findViewById(R.id.price), "#rmb#"
				+ CacheMgr.gambleCache.getPrice(this.machinePlayType), true,
				10, 10);

		wheel1 = (WheelView) findViewById(R.id.line1);
		wheel2 = (WheelView) findViewById(R.id.line2);
		wheel3 = (WheelView) findViewById(R.id.line3);

		wheel1.initWheel(gambleData);
		wheel2.initWheel(gambleData);
		wheel3.initWheel(gambleData);

		loadSound();
	}

	private void setGambleType() {
		if (machinePlayType.equals(MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR)) {
			ViewUtil.setImage(bambleType, "gamble_bg1.jpg");
		} else if (machinePlayType
				.equals(MachinePlayType.MACHINE_PLAY_TYPE_MIDDLE)) {
			ViewUtil.setImage(bambleType, "gamble_bg2.jpg");
		}
	}

	private void loadSound() {
		// 预先加载声音
		SoundMgr.loadSound("box_start.ogg");
		SoundMgr.loadSound("box_stop.ogg");
		SoundMgr.loadSound("result_6.ogg");
		SoundMgr.loadSound("result_36.ogg");
		SoundMgr.loadSound("sfx_tips.ogg");
	}

	@Override
	protected void destory() {
		if (isUpdateList)
			updateLuckyList.onCall();
		rmb.onCall();
		canExitThisWindow(true);
		super.destory();
	}

	private void startWheel() {
		SoundMgr.play(R.raw.box_start);
		gamble_start = System.currentTimeMillis();

		canExitThisWindow(false);
		ViewUtil.disableButton(go);
		wheel1.setWheel(true, wheel2);
		wheel2.setWheel(false, wheel3);
		wheel3.setWheel(false, null);

		ViewUtil.setGone(rateFrame);

		isNetOk = true;

		new GambleInvoker().start();
		new Thread(new IsWheelOver()).start();
	}

	class GambleInvoker extends BaseInvoker {
		private MachinePlayResp resp;

		@Override
		protected String failMsg() {
			return "网络错误!您的元宝并未扣除,再试一次吧!";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().machinePlay(machinePlayType);
		}

		@Override
		protected String loadingMsg() {
			return null;
		}

		@Override
		protected void beforeFire() {
		}

		@Override
		protected void onOK() {
			setRightTxt("#rmb#" + Account.user.getCurrency());
			gamble_end = System.currentTimeMillis();
			if (!isTimeOut()) {
				List<Integer> list = resp.getList();
				if (3 != list.size()) {
					controller.alert("网络错误，请稍候重试");
					return;
				}

				// 有上榜就更新
				if (1 == resp.getIsOnList())
					isUpdateList = true;

				Message msg1 = Message.obtain();
				msg1.what = WHEEL1_STOP;
				msg1.arg1 = list.get(0) - 1;
				handler.sendMessage(msg1);

				Message msg2 = Message.obtain();
				msg2.what = WHEEL2_STOP;
				msg2.arg1 = list.get(1) - 1;
				handler.sendMessage(msg2);

				Message msg3 = Message.obtain();
				msg3.what = WHEEL3_STOP;
				msg3.arg1 = list.get(2) - 1;
				handler.sendMessage(msg3);

				gambleRate = getRate(list);

				respList.add(resp);
			}
		}

		@Override
		protected void onFail(GameException exception) {
			gamble_end = System.currentTimeMillis();
			isNetOk = false;
			if (!isTimeOut()) {
				Message msg = new Message();
				msg.what = ALL_WHEEL_STOP;
				handler.sendMessage(msg);
			}
			failMsg = exception.getErrorMsg();
		}

		private boolean isTimeOut() {
			return (gamble_end - gamble_start) > WheelView.MAX_WHEEL_DURATION;
		}

		private int getRate(List<Integer> list) {
			if (null == list || list.size() != 3)
				return 0;
			if (list.get(0) == list.get(1) && list.get(0) == list.get(2))
				return 36;
			if (list.get(0) != list.get(1) && list.get(0) != list.get(2)
					&& list.get(1) != list.get(2))
				return 1;

			return 6;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RESET:
				canExitThisWindow(true);
				ViewUtil.enableButton(go);

				// 如果获取结果出错，给出提示
				if (!isNetOk) {
					ViewUtil.setGone(rateFrame);
					if (!isNetOk) {
						if (failMsg.equals(""))
							controller.alert("网络错误,您的元宝并未扣除，请稍候重试!");
						else
							controller.alert(failMsg);
					}

					return;
				}

				setAward();

				if (0 == gambleRate) {
					ViewUtil.setGone(rateFrame);
				} else {
					ViewUtil.setVisible(rateFrame);

					if (1 == gambleRate) {
					} else if (6 == gambleRate) {
						rateFrame.setBackgroundResource(R.drawable.gamble6);
						SoundMgr.play("result_6.ogg");
					} else if (36 == gambleRate) {
						rateFrame.setBackgroundResource(R.drawable.gamble36);
						SoundMgr.play("result_36.ogg");
						startRewardAnim();
					}
					if (1 == gambleRate || 6 == gambleRate)
						showRewardItem();
				}
				break;
			case ALL_WHEEL_STOP:
				setAllWheelDefStop();
				break;
			case WHEEL1_STOP:
				wheel1.setStopItem(msg.arg1);
				break;
			case WHEEL2_STOP:
				wheel2.setStopItem(msg.arg1);
				break;
			case WHEEL3_STOP:
				wheel3.setStopItem(msg.arg1);
				break;
			}

		}

		private void startRewardAnim() {
			ViewUtil.setVisible(window, R.id.animLayout);
			Animation rotate = new RotateAnimation(0, 1 * 360,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setDuration(2000);
			rotate.setInterpolator(new DecelerateInterpolator());
			rotate.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					ViewUtil.disableButton(go);
					canExitThisWindow(false);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					ViewUtil.enableButton(go);
					canExitThisWindow(true);
					ViewUtil.setHide(window, R.id.animLayout);
					showRewardItem();
				}
			});

			findViewById(R.id.anim).startAnimation(rotate);
		}
	};

	private void showRewardItem() {
		MachinePlayResp resp = respList.get(respList.size() - 1);
		List<ShowItem> rewards = resp.getRi().showReturn(true);
		new GambleRewardTip((gambleRate == 1 ? "获得" : (gambleRate + "倍"))
				+ "奖励", rewards, resp.getLeftCnt()).show();
	}

	private void setAllWheelDefStop() {
		wheel1.setStopItem(5);
		wheel1.setForceStop(true);
		wheel2.setStopItem(6);
		wheel2.setForceStop(true);
		wheel3.setStopItem(7);
		wheel3.setForceStop(true);
	}

	private void setAward() {
		ViewGroup award = (ViewGroup) window.findViewById(R.id.award);
		if (null == award || null == respList || 0 == respList.size())
			return;

		MachinePlayResp resp = respList.get(respList.size() - 1);

		ViewGroup vg = (ViewGroup) controller.inflate(R.layout.gamble_award);
		if (award.getChildCount() >= 1) {
			ViewUtil.setVisible(vg, R.id.seperator_item);
		}
		ReturnInfoClient ri = resp.getRi();
		ArrayList<ShowItem> ls = ri.showReturn(true);
		if (!ListUtil.isNull(ls)) {
			ViewGroup content = (ViewGroup) vg.findViewById(R.id.content);
			ViewGroup content1 = (ViewGroup) vg.findViewById(R.id.content1);

			for (int j = 0; j < ls.size(); j++) {
				if (ls.get(j).getCount() > 0) {
					if ((j + 1) % 2 == 0) {
						content1.addView(ViewUtil.getShowItemView(
								ls.get(j),
								Config.getController().getResources()
										.getColor(R.color.color2), 10));
					} else {
						content.addView(ViewUtil.getShowItemView(
								ls.get(j),
								Config.getController().getResources()
										.getColor(R.color.color2), 10));
					}

				}

			}
		}
		award.addView(vg, 0);
	}

	private boolean isAllWheelStop() {
		return !wheel1.isScrollingPerformed() && !wheel2.isScrollingPerformed()
				&& !wheel3.isScrollingPerformed();
	}

	class IsWheelOver implements Runnable {
		@Override
		public void run() {
			while (true) {
				allStopTime = System.currentTimeMillis();
				if (allStopTime - gamble_start > WheelView.MAX_WHEEL_DURATION
						&& isNetOk) {
					isNetOk = false;
					setAllWheelDefStop();
				}

				// 不管请求成功或失败，所有wheel结束后再发RESET
				if (isAllWheelStop()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Message msg = new Message();
					msg.what = RESET;
					handler.sendMessage(msg);
					return;
				}
			}
		}
	}

	@Override
	public boolean goBack() {
		if (isExit)
			return false;
		else
			return true;
	}

	// 控制 转盘转动时不能退出当前界面
	private void canExitThisWindow(boolean isExit) {
		this.isExit = isExit;
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	private void freePlayPrompt() {
		if (0 != Account.readLog.FREE_TIMES
				&& MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR == machinePlayType) {
			String title = "今天有免费体验次数，不会扣除元宝";
			controller.alert(title, null);
		}
	}

	private SlowOnClick goClick = new SlowOnClick() {

		@Override
		public void doOnClick(View v) {
			// 初级的只能免费玩一次
			if (0 != Account.readLog.FREE_TIMES
					&& MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR == machinePlayType) {
				String title = "本次为免费体验，不会扣除元宝";
				controller.alert(title, new CallBack() {
					@Override
					public void onCall() {
						startWheel();
					}
				});

				return;
			}

			// 非免费情况
			if (Account.user.getCurrency() < CacheMgr.gambleCache
					.getPrice(GambleWindow.this.machinePlayType)) {
				// controller.confirm("余额不足", "是否现在充值?", new CallBack() {
				// @Override
				// public void onCall() {
				// doClose();
				// controller.openRechargeCenterWindow();
				// }
				// }, null);
				new ToActionTip(
						CacheMgr.gambleCache
								.getPrice(GambleWindow.this.machinePlayType)
								- Account.user.getCurrency()).show();
			} else {
				startWheel();
			}
		}

		@Override
		protected int getTime() {
			return 500;
		}
	};

}
