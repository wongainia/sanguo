package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.EndGuider;
import com.vikings.sanguo.message.CurrencyMachinePlayResp;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.RewardTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.ui.wheel.WheelView;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class GodWealthWindow extends CustomPopupWindow implements
		OnClickListener {

	private WheelView wheel1, wheel2, wheel3, wheel4, wheel5, wheel6;
	private List<Integer> imgsIntegers = new ArrayList<Integer>();
	private int expendCurrency = 0;
	private boolean isExit = true;
	// 每次转动的起始时间
	private long gamble_start = 0;
	private long gamble_end = 0;
	private boolean isNetOk; // 网络是否正常
	private View goBtn;
	private long allStopTime = 0;
	private String failMsg = "";
	// 引导专用
	public static boolean isGuide = false;
	public static boolean enterGodWealthWindowGuide = false;

	private CurrencyMachinePlayResp cmpr;

	// 消息内容
	private final static int RESET = 0;
	private final static int ALL_WHEEL_STOP = 1;
	private final static int WHEEL1_STOP = 2;
	private final static int WHEEL2_STOP = 3;
	private final static int WHEEL3_STOP = 4;
	private final static int WHEEL4_STOP = 5;
	private final static int WHEEL5_STOP = 6;
	private final static int WHEEL6_STOP = 7;

	@Override
	protected void init() {
		super.init("天降横财");
		setContent(R.layout.god_wealth_layout);
		ViewUtil.setImage(findViewById(R.id.god_wealth_spc_bg),
				"god_wealth_spc_bg.jpg");
		setLeftBtn("规则说明", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new RuleSpecTip("天降横财规则说明", CacheMgr.uiTextCache
						.getTxt(UITextProp.GOD_WEALTH_SPEC)).show();
			}
		});
		setValue();
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	private void setValue() {
		updateDetail();

		goBtn = findViewById(R.id.go);
		goBtn.setOnClickListener(this);

		initWheelImages();

		initWheels();

		loadSound();
	}

	private void updateDetail() {
		setRightTxt("#rmb#" + Account.user.getCurrency());
		// 每次消耗的元宝不同
		expendCurrency = CacheMgr.dictCache.getDictInt(
				Dict.GOD_WEALTH_TIMES_CURRENCY,
				Account.user.godWealthTimes() + 1);
		if (isGuide) {
			ViewUtil.setRichText(window, R.id.price, "#rmb#" + "200/次");
		} else {
			ViewUtil.setRichText(window, R.id.price, "#rmb#" + expendCurrency
					+ "/次");
		}

		ViewUtil.setRichText(window, R.id.times,
				"剩余" + Account.user.godWealthLeftTimes() + "次机会");

	}

	// 初始化六个滚轮
	private void initWheels() {
		wheel1 = (WheelView) findViewById(R.id.line1);
		wheel2 = (WheelView) findViewById(R.id.line2);
		wheel3 = (WheelView) findViewById(R.id.line3);
		wheel4 = (WheelView) findViewById(R.id.line4);
		wheel5 = (WheelView) findViewById(R.id.line5);
		wheel6 = (WheelView) findViewById(R.id.line6);

		wheel1.initWheel(imgsIntegers, 9);
		wheel2.initWheel(imgsIntegers, 9);
		wheel3.initWheel(imgsIntegers, 9);
		wheel4.initWheel(imgsIntegers, 9);
		wheel5.initWheel(imgsIntegers, 9);
		wheel6.initWheel(imgsIntegers, 9);
	}

	// 初始化滚轮Item显示项
	private void initWheelImages() {
		imgsIntegers.clear();
		imgsIntegers.add(R.drawable.vip_0);
		imgsIntegers.add(R.drawable.vip_1);
		imgsIntegers.add(R.drawable.vip_2);
		imgsIntegers.add(R.drawable.vip_3);
		imgsIntegers.add(R.drawable.vip_4);
		imgsIntegers.add(R.drawable.vip_5);
		imgsIntegers.add(R.drawable.vip_6);
		imgsIntegers.add(R.drawable.vip_7);
		imgsIntegers.add(R.drawable.vip_8);
		imgsIntegers.add(R.drawable.vip_9);
	}

	private void loadSound() {
		// 预先加载声音
		SoundMgr.loadSound("box_start.ogg");
		SoundMgr.loadSound("box_stop.ogg");
		SoundMgr.loadSound("result_6.ogg");
		SoundMgr.loadSound("result_36.ogg");
		SoundMgr.loadSound("sfx_tips.ogg");
	}

	public void open() {
		doOpen();
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	public void onClick(View v) {
		if (isGuide) {
			startWheel();
			return;
		}
		// 判断当前元宝是否足够
		if (expendCurrency > Account.user.getCurrency()) {
			new ToActionTip(expendCurrency).show();
			return;
		}
		startWheel();

	}

	private void startWheel() {
		SoundMgr.play(R.raw.box_start);
		gamble_start = System.currentTimeMillis();

		canExitThisWindow(false);
		ViewUtil.disableButton(goBtn);
		wheel1.setWheel(true, wheel2);
		wheel2.setWheel(false, wheel3);
		wheel3.setWheel(false, wheel4);
		wheel4.setWheel(false, wheel5);
		wheel5.setWheel(false, wheel6);
		wheel6.setWheel(false, null);

		isNetOk = true;

		new OpenGodWealthInvoker().start();
		new Thread(new IsWheelOver()).start();

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

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RESET:
				canExitThisWindow(true);
				ViewUtil.enableButton(goBtn);

				// 如果获取结果出错，给出提示
				if (!isNetOk) {
					if (!isNetOk) {
						if (failMsg.equals(""))
							controller.alert("网络错误,您的元宝并未扣除，请稍候重试!");
						else
							controller.alert(failMsg);
					}
					return;
				}
				if (isGuide) {
					Account.readLog.STEP1001 = 1;
					Account.readLog.save();
					ReturnInfoClient riClient = new ReturnInfoClient();
					riClient.setCurrency(400);
					new RewardTip("拜财神奖励", riClient.showReturn(true), false,
							false, new CallBack() {
								@Override
								public void onCall() {
									// 引导结束
									new EndGuideStep301(BaseStep.INDEX_STEP301)
											.start();
									isGuide = false;
									enterGodWealthWindowGuide = true;
									updateDetail();
								}
							}).show();
				} else {
					controller.updateUI(cmpr.getRi(), true);
					cmpr.getRi().setCurrency(
							cmpr.getRi().getCurrency() + expendCurrency);
					new RewardTip("拜财神奖励", cmpr.getRi().showReturn(true),
							false, false).show();
					updateDetail();
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
			case WHEEL4_STOP:
				wheel4.setStopItem(msg.arg1);
				break;
			case WHEEL5_STOP:
				wheel5.setStopItem(msg.arg1);
				break;
			case WHEEL6_STOP:
				wheel6.setStopItem(msg.arg1);
				break;
			}

		}

	};

	private void setAllWheelDefStop() {
		wheel1.setStopItem(9);
		wheel1.setForceStop(true);
		wheel2.setStopItem(9);
		wheel2.setForceStop(true);
		wheel3.setStopItem(9);
		wheel3.setForceStop(true);

		wheel4.setStopItem(9);
		wheel4.setForceStop(true);
		wheel5.setStopItem(9);
		wheel5.setForceStop(true);
		wheel6.setStopItem(9);
		wheel6.setForceStop(true);
	}

	private boolean isAllWheelStop() {
		return !wheel1.isScrollingPerformed() && !wheel2.isScrollingPerformed()
				&& !wheel3.isScrollingPerformed()
				&& !wheel4.isScrollingPerformed()
				&& !wheel5.isScrollingPerformed()
				&& !wheel6.isScrollingPerformed();
	}

	// 控制 转盘转动时不能退出当前界面
	private void canExitThisWindow(boolean isExit) {
		this.isExit = isExit;
	}

	@Override
	public boolean goBack() {
		if (isExit)
			return false;
		else
			return true;
	}

	public class OpenGodWealthInvoker extends BaseInvoker {

		@Override
		protected String failMsg() {
			return "网络错误!您的元宝并未扣除,再试一次吧!";
		}

		@Override
		protected void fire() throws GameException {
			// 非引导情况下请求服务器
			if (isGuide == false)
				cmpr = GameBiz.getInstance().currencyMachinePlay();

		}

		@Override
		protected String loadingMsg() {
			return null;
		}

		@Override
		protected void onOK() {
			gamble_end = System.currentTimeMillis();
			if (!isTimeOut()) {
				List<Integer> list = null;
				if (isGuide) {
					list = StringUtil.dealWithCurrency(400);
				} else {
					list = StringUtil.dealWithCurrency(cmpr.getRi()
							.getCurrency() + expendCurrency);// 服务器返回的是挣了多少；显示加成本价格
				}

				Message msg1 = Message.obtain();
				msg1.what = WHEEL1_STOP;
				msg1.arg1 = list.get(0);
				handler.sendMessage(msg1);

				Message msg2 = Message.obtain();
				msg2.what = WHEEL2_STOP;
				msg2.arg1 = list.get(1);
				handler.sendMessage(msg2);

				Message msg3 = Message.obtain();
				msg3.what = WHEEL3_STOP;
				msg3.arg1 = list.get(2);
				handler.sendMessage(msg3);

				Message msg4 = Message.obtain();
				msg4.what = WHEEL4_STOP;
				msg4.arg1 = list.get(3);
				handler.sendMessage(msg4);

				Message msg5 = Message.obtain();
				msg5.what = WHEEL5_STOP;
				msg5.arg1 = list.get(4);
				handler.sendMessage(msg5);

				Message msg6 = Message.obtain();
				msg6.what = WHEEL6_STOP;
				msg6.arg1 = list.get(5);
				handler.sendMessage(msg6);
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
	}

	private boolean isTimeOut() {
		return (gamble_end - gamble_start) > WheelView.MAX_WHEEL_DURATION;
	}

	private class EndGuideStep301 extends EndGuider {

		@Override
		protected void refreshUI() {
			setRightTxt("#rmb#" + Account.user.getCurrency());
		}

		public EndGuideStep301(int trainingId) {
			super(trainingId);
		}

	}

}
