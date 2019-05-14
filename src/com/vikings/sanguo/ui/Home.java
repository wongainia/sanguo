package com.vikings.sanguo.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ListView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.activity.MainActivity;
import com.vikings.sanguo.battle.anim.ArcTranslateAnimation;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.invoker.DeleteSDCardImgInvoker;
import com.vikings.sanguo.invoker.LogInvoker;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.model.ServerUserData;
import com.vikings.sanguo.sound.MediaPlayerMgr;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UpdateVersion;
import com.vikings.sanguo.ui.adapter.ServerConfigAdapter;
import com.vikings.sanguo.ui.alert.SystemNotifyTip;
import com.vikings.sanguo.ui.window.PopupUI;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class Home extends PopupUI implements OnClickListener,
		UpdateVersion.DownloadListener {

	private ViewGroup content;

	private TextView tipsTxt; // 小贴士
	private View enter, enterGameText, retrievepswd, repair, loadingText,
			animHair1, animHair2, animHair3, animFlower1, animFlower2,
			animFlower3, animEye, logo;

	private ProgressBar loadingBar;

	private Handler handler = new Handler();

	private TipsWorker worker = new TipsWorker();

	private MainActivity main;

	private ServerConfigAdapter adapter;

	private ListView listView;
	private ViewGroup listLayout;
	private ViewGroup serverChooseLayout;

	private String[] tip = {
			Config.getController().getString(R.string.Home_tip_2),
			Config.getController().getString(R.string.Home_tip_2),
			Config.getController().getString(R.string.Home_tip_2),
			Config.getController().getString(R.string.Home_tip_3),
			Config.getController().getString(R.string.Home_tip_4) };

	private int[] percent = { 92, 94, 96, 98, 100 };

	private ViewGroup viewGroup;
	private boolean autoEnter = false;

	public Home(ViewGroup viewGroup) {
		this.viewGroup = viewGroup;
	}

	@Override
	protected void bindField() {

	}

	@Override
	public void doOpenPlay() {
	}

	@Override
	protected void destory() {
		handler.removeCallbacks(worker);
		viewGroup.removeView(content);
		viewGroup = null;
	}

	@Override
	protected View getPopupView() {
		return content;
	}

	private int getVerCode() {
		try {
			return Config
					.getController()
					.getMainActivity()
					.getPackageManager()
					.getPackageInfo(
							Config.getController().getMainActivity()
									.getPackageName(),

							0).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	@Override
	protected void init() {
		new LogInvoker("打开游戏").start();
		MediaPlayerMgr.getInstance().startSound(R.raw.game_start);
		// Verify.check();
		// new AddrInvoker().start();
		content = (ViewGroup) controller.inflate(R.layout.home);

		ViewUtil.setImage(content, "logobg.jpg");
		imageHolder.saveRef(content);
		enter = content.findViewById(R.id.enter);
		enter.setOnClickListener(this);
		enterGameText = content.findViewById(R.id.enterGameText);

		logo = content.findViewById(R.id.logo);
		ViewUtil.setImage(logo, "home_logo_bg");

		ViewUtil.setImage(enter, "enter_game");

		ViewUtil.setImage(enterGameText, "enter_game_text");

		retrievepswd = content.findViewById(R.id.retrievepassword);
		retrievepswd.setOnClickListener(this);

		repair = content.findViewById(R.id.repair);
		repair.setOnClickListener(this);

		tipsTxt = (TextView) content.findViewById(R.id.tipText);

		main = (MainActivity) controller.getMainActivity();

		adapter = new ServerConfigAdapter(new HomeCallBack());
		adapter.addItems(getServerUserDatas());
		listLayout = (ViewGroup) content.findViewById(R.id.listViewLayout);
		listLayout.setOnClickListener(this);
		listView = (ListView) content.findViewById(R.id.listView);
		listView.setAdapter(adapter);
		serverChooseLayout = (ViewGroup) content
				.findViewById(R.id.serverChooseLayout);
		serverChooseLayout.setOnClickListener(this);

		animHair1 = content.findViewById(R.id.animHair1);
		animHair2 = content.findViewById(R.id.animHair2);
		animHair3 = content.findViewById(R.id.animHair3);
		animFlower1 = content.findViewById(R.id.animFlower1);
		animFlower2 = content.findViewById(R.id.animFlower2);
		animFlower3 = content.findViewById(R.id.animFlower3);
		animEye = content.findViewById(R.id.animEye);
		ViewUtil.setImage(content.findViewById(R.id.bgMirror),
				ImageUtil.getMirrorBitmapDrawable("home_op_bg.png"));

		setVersion();

		loadingBar = (ProgressBar) content.findViewById(R.id.loadingBar);
		loadingText = content.findViewById(R.id.loadingText);

		showMenu();
		clearNotify();
		initAnim();
		viewGroup.addView(content);
	}

	public void setVersion() {
		String verStr = "版本";
		if (Config.useFor == 2) {
			verStr = Config.getController().getString(R.string.Home_init_2);
		} else if (Config.useFor == 3) {
			verStr = Config.getController().getString(R.string.Home_init_3);
		} else if (Config.useFor == 1) {
			verStr = "版本 v";
		}

		ViewUtil.setText(
				content.findViewById(R.id.version),
				verStr
						+ this.controller.getResources().getString(
								R.string.app_versionName) + "(" +

						getVerCode() + ")");
		ViewUtil.setText(content.findViewById(R.id.website), this.controller
				.getResources().getString(R.string.website));

	}

	private void clearNotify() {
		NotificationManager nm = (NotificationManager) controller
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(Constants.TYPE_FRUIT);
		nm.cancel(Constants.TYPE_LOGIN);
		nm.cancel(Constants.TYPE_MSG);
	}

	public void enter() {
		autoEnter = false;
		showProgress();
		checkVer();
	}

	public void autoEnter() {
		autoEnter = true;
		showProgress();
		checkServer();
	}

	public void showProgress() {
		handler.post(worker);
		ViewUtil.setGone(retrievepswd);
		ViewUtil.setGone(repair);
		ViewUtil.setGone(enter);
		ViewUtil.setGone(serverChooseLayout);
		ViewUtil.setGone(listLayout);
		ViewUtil.setVisible(loadingBar);
		if (!autoEnter)
			ViewUtil.setVisible(tipsTxt);
		else
			ViewUtil.setGone(tipsTxt);
	}

	public void setPercent(int p) {
		loadingBar.set(p);
	}

	public void setPercentInThread(final int p) {
		loadingBar.post(new Runnable() {

			@Override
			public void run() {
				loadingBar.set(p);
			}
		});
	}

	public void setLoadingText(String text) {
		ViewUtil.setText(loadingText, text);
	}

	public void showMenu() {
		setServerUI(Config.serverId, Config.userClient);
		setPercent(0);
		ViewUtil.setText(loadingText, "");
		ViewUtil.setGone(loadingBar);
		ViewUtil.setGone(tipsTxt);

		ViewUtil.setVisible(enter);
		ViewUtil.setVisible(retrievepswd);
		ViewUtil.setVisible(repair);

		handler.removeCallbacks(worker);
	}

	public void post(Runnable r) {
		this.content.post(r);
	}

	public void checkServer() {
		ViewUtil.setText(loadingText,
				Config.getController().getString(R.string.Home_checkServer));
		setPercent(5);
		main.checkServer();
	}

	public void checkVer() {
		ViewUtil.setText(loadingText,
				Config.getController().getString(R.string.Home_checkVer));
		setPercent(10);
		main.checkVer();
	}

	public void login() {
		ViewUtil.setText(loadingText,
				Config.getController().getString(R.string.Home_login));
		setPercent(90);
		main.login();
	}

	public void loadConfig() {
		ViewUtil.setText(loadingText,
				Config.getController().getString(R.string.Home_loadConfig));
		setPercent(70);
		main.loadConfig();
	}

	public void init(final int i) {
		if (i < tip.length) {
			ViewUtil.setText(loadingText, tip[i] + "...");
			setPercent(percent[i]);
			content.post(new Runnable() {
				@Override
				public void run() {
					switch (i) {
					case 0:
						main.initUI1();
						break;
					case 1:
						main.initUI2();
						break;
					case 2:
						main.initUI3();
						break;
					case 3:
						main.initMap();
						break;
					case 4:
						main.initUserInfo();
						break;
					default:
						break;
					}
					init(i + 1);
				}
			});
		} else {
			MediaPlayerMgr.getInstance().stopSound();
			handler.removeCallbacks(worker);
			controller.closeAllPopup();

			// 如有sd卡，检查sd卡剩余空间是否小于1M，如没有，检查数据内存剩余空间是否小于1M
			if (controller.getFileAccess().checkSDCard()) {
				if (!controller.getFileAccess().isSDCardEnough())
					showMemTip(Config.getController()
							.getString(R.string.sdCard));
			} else {
				if (!Setting.isDataMemEnough())
					showMemTip(Config.getController()
							.getString(R.string.memory));
			}
			controller.openFirst();
		}
	}

	private void showMemTip(String mem) {
		controller.confirm(
				Config.getController().getString(R.string.Home_showMemTip_2),
				StringUtil.repParams(
						Config.getController().getString(
								R.string.Home_showMemTip_1), mem),
				new CallBack() {
					@Override
					public void onCall() {
						new DeleteSDCardImgInvoker().start();
					}
				}, null);

	}

	@Override
	public void onClick(View v) {
		SoundMgr.play(R.raw.sfx_button_default);
		if (v == enter) {
			if (Config.serverId <= 0) {
				controller.alert("请先选择选择分区");
				return;
			}

			ServerData data = controller.getServerFileCache().getByServerId(
					Config.serverId);
			if (null != data) {
				if (data.isFull()
						&& !controller.getFileAccess().hasUser(Config.serverId)) {
					controller.alert("抱歉，您所选择的服务器已经爆满！<br>请选择其他推荐的服务器进行游戏！");
					return;
				}
			}

			if (data.isRepair()) {
				new SystemNotifyTip(CacheMgr.dictCache.getDict(
						Dict.TYPE_SITE_ADDR, (byte) 6),
						controller.getString(R.string.CheckVersion_onOK))
						.show();
				return;
			}

			enter();
		} else if (v == retrievepswd) {
			controller.openRertievePwd(false, true);
		} else if (v == serverChooseLayout) {
			ViewUtil.setVisible(listLayout);
			adapter.notifyDataSetChanged();
		} else if (v == repair) {
			controller.openGameSetting(true);
		} else if (v == listLayout) {
			if (ViewUtil.isVisible(listLayout)) {
				ViewUtil.setGone(listLayout);
			}
		}
	}

	private class TipsWorker implements Runnable {

		@Override
		public void run() {
			String tipsText = null;
			try {
				tipsText = CacheMgr.tipsCache.getTips(CacheMgr.tipsCache
						.getRandomNum());
			} catch (Exception e) {
			}
			if (StringUtil.isNull(tipsText)) {
				tipsText = Config.getController().getString(
						R.string.Home_TipsWorker_run_1);
			}

			ViewUtil.setRichText(tipsTxt, tipsText);
			handler.postDelayed(worker, 10000);

		}
	}

	@Override
	public boolean goBack() {
		if (isShow()) {
			if (ViewUtil.isVisible(listLayout)) {
				ViewUtil.setGone(listLayout);
			} else {
				controller.quit();
			}
			return true;
		} else {
			return false;
		}
	}

	private abstract class InnerRunner implements Runnable {

		@Override
		public void run() {
			if (Home.this.isShow())
				doRun();
		}

		public abstract void doRun();

	}

	private void initAnim() {
		startEnterTextAnim();
		startHairAnim();
		startEyesAnim();
		startFlowerAnim();
	}

	private void startEyesAnim() {
		int delayTime = (int) (Math.random() * 10 + 5) * 1000;
		ViewUtil.setVisible(animEye);
		content.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				ViewUtil.setHide(animEye);
			}
		}, 100);
		content.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				startEyesAnim();
			}
		}, delayTime);
	}

	private void startFlowerAnim() {
		flowerAnim(animFlower1, 100, 4000, 240, -1, 60, 350, 0.3f, 2000, 200);
		flowerAnim(animFlower2, 100, 5000, 315, -1, 270, 480, 0.4f, 2500, 300);
		flowerAnim(animFlower3, 100, 3500, 495, -1, 300, 550, 0.35f, 2100, 150);
		flowerAnim(animFlower2, 6500, 12800, 315, -1, 270, 480, 0.5f, 2500, 300);
		flowerAnim(animFlower1, 7500, 11000, 240, -1, 60, 350, 0.4f, 2000, 200);
		flowerAnim(animFlower1, 13000, 17000, 240, -1, 60, 350, 0.4f, 2000, 200);
		flowerAnim(animFlower3, 18000, 22000, 495, -1, 300, 550, 0.55f, 2100,
				150);
		flowerAnim(animFlower1, 20000, 24500, 240, -1, 60, 350, 0.35f, 2000,
				200);
		flowerAnim(animFlower2, 20500, 25000, 315, -1, 270, 480, 0.5f, 2500,
				300);
		flowerAnim(animFlower3, 28000, 31000, 495, -1, 300, 550, 0.45f, 2100,
				150);

		content.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				startFlowerAnim();
			}
		}, 31000);
	}

	private void startHairAnim() {
		hairAnim(animHair1, 193, 15, 243, 309, 1000, 2);
		hairAnim(animHair2, 318, 6, 338, 482, 1000, -2);
		hairAnim(animHair3, 218, 5, 222, 312, 1500, 3);
	}

	private void hairAnim(View view, int x, int y, float width, float height,
			int time, int degree) {
		RotateAnimation anim = new RotateAnimation(0, degree,
				Animation.RELATIVE_TO_SELF, x / width,
				Animation.RELATIVE_TO_SELF, y / height);
		anim.setDuration(time);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setInterpolator(new AccelerateInterpolator());
		view.startAnimation(anim);
	}

	private void startEnterTextAnim() {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.97f, 1, 0.97f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				1f);
		scaleAnimation.setDuration(300);
		scaleAnimation.setRepeatMode(Animation.REVERSE);
		scaleAnimation.setRepeatCount(Animation.INFINITE);
		scaleAnimation.setInterpolator(new AccelerateInterpolator());
		enter.startAnimation(scaleAnimation);
	}

	private void flowerAnim(View view, int startTime, int endTime,
			float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
			float k, long degreeTime, long alphaTime) {
		int time = endTime - startTime;

		ArcTranslateAnimation anim = new ArcTranslateAnimation(0,
				(toXDelta - fromXDelta) * Config.SCALE_FROM_HIGH, 0,
				(toYDelta - fromYDelta) * Config.SCALE_FROM_HIGH, k,
				degreeTime, alphaTime);

		anim.setDuration(time);
		anim.setFillAfter(true);

		final View v = view;
		final Animation animation = anim;

		content.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {

				v.startAnimation(animation);
			}
		}, startTime);

	}

	@Override
	public void setDownloadPercent(int p) {
		if (ViewUtil.isGone(loadingBar)) {
			showProgress();
			setLoadingText("下载新版本...");
		}
		setPercent(p);
	}

	@Override
	public void cancle() {
		showMenu();
	}

	public void setServerUI(int serverId, AccountPswInfoClient client) {
		ServerData data = controller.getServerFileCache().getByServerId(
				serverId);
		if (null == data)
			data = controller.getServerFileCache().getLatest();
		setServerUI(data, client);
	}

	private void setServerUI(ServerData data, AccountPswInfoClient client) {
		if (null == data)
			return;
		ViewUtil.setGone(listLayout);
		ViewUtil.setVisible(serverChooseLayout);
		String color = data.getColor();
		ViewUtil.setImage(serverChooseLayout, R.id.state, data.getStateImgId());
		ViewUtil.setRichText(serverChooseLayout, R.id.serverName,
				StringUtil.color(data.getName(), color));
		if (null != client)
			ViewUtil.setRichText(serverChooseLayout, R.id.userName,
					StringUtil.color("(" + client.getNick() + ")", color));
		else
			ViewUtil.setRichText(serverChooseLayout, R.id.userName, "");
	}

	private class HomeCallBack implements CallBack {

		@Override
		public void onCall() {
			setServerUI(Config.serverId, Config.userClient);
		}

	}

	public void refreshData() {
		if (null != adapter)
			adapter.clear();
		adapter.addItems(getServerUserDatas());
		adapter.notifyDataSetChanged();
	}

	private List<ServerUserData> getServerUserDatas() {
		List<ServerUserData> datas = new ArrayList<ServerUserData>();
		List<ServerData> serverDatas = main.getServerFileCache().getAll();
		List<AccountPswInfoClient> clients = controller.getFileAccess()
				.getUsers();
		for (ServerData serverData : serverDatas) {
			ServerUserData data = new ServerUserData();
			data.setServerData(serverData);
			for (AccountPswInfoClient client : clients) {
				if (serverData.getServerId() == client.getSid())
					data.add(client);
			}
			datas.add(data);
		}
		return datas;
	}

}
