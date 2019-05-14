package com.vikings.sanguo.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.vikings.pay.VKPayMgr;
import com.vikings.pay.impl.VKChargeListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BitmapCache;
import com.vikings.sanguo.cache.ServerFileCache;
import com.vikings.sanguo.cache.ViewCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.DipUtil;
import com.vikings.sanguo.controller.GameController;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.CheckServerConfigBeforeEnterInvoker;
import com.vikings.sanguo.invoker.CheckServerConfigInvoker;
import com.vikings.sanguo.invoker.CheckVersion;
import com.vikings.sanguo.invoker.EventEntryAutoInvoker;
import com.vikings.sanguo.invoker.LoadConfig;
import com.vikings.sanguo.invoker.LoginInvoker;
import com.vikings.sanguo.invoker.SaveFilesInvoker;
import com.vikings.sanguo.invoker.WebNoticeInvoker;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.BuildingType;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.HolyCategory;
import com.vikings.sanguo.model.OtherRichGuildInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.PropActType;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.RechargeCardInfo;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.model.UserQuery;
import com.vikings.sanguo.service.Notification;
import com.vikings.sanguo.sound.MediaPlayerMgr;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.CrashHandler;
import com.vikings.sanguo.thread.HeartBeat;
import com.vikings.sanguo.thread.ImageLoader;
import com.vikings.sanguo.thread.SetCountry;
import com.vikings.sanguo.ui.AccountBar;
import com.vikings.sanguo.ui.Announce;
import com.vikings.sanguo.ui.FiefMap;
import com.vikings.sanguo.ui.Home;
import com.vikings.sanguo.ui.NotifyMsg;
import com.vikings.sanguo.ui.NotifyUI;
import com.vikings.sanguo.ui.NotifyWorldChatMsg;
import com.vikings.sanguo.ui.PhotoTaker;
import com.vikings.sanguo.ui.Prologue;
import com.vikings.sanguo.ui.ScrollText;
import com.vikings.sanguo.ui.alert.DailyRewardTip;
import com.vikings.sanguo.ui.alert.ForeignInvasionFiefTip;
import com.vikings.sanguo.ui.alert.HolyFiefSearchTip;
import com.vikings.sanguo.ui.alert.LoadingTip;
import com.vikings.sanguo.ui.alert.MsgAlert;
import com.vikings.sanguo.ui.alert.MsgConfirm;
import com.vikings.sanguo.ui.alert.QuitConfirm;
import com.vikings.sanguo.ui.alert.StrongerWindow;
import com.vikings.sanguo.ui.guide.Step301;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.ui.map.BattleMap;
import com.vikings.sanguo.ui.window.AccountManagement;
import com.vikings.sanguo.ui.window.ActTypeWindow;
import com.vikings.sanguo.ui.window.ActWindow;
import com.vikings.sanguo.ui.window.ArenaWindow;
import com.vikings.sanguo.ui.window.ArmTrainingListWindow;
import com.vikings.sanguo.ui.window.ArmTrainingWindow;
import com.vikings.sanguo.ui.window.AssistGodSoldierWindow;
import com.vikings.sanguo.ui.window.BarWindow;
import com.vikings.sanguo.ui.window.BloodRewardWindow;
import com.vikings.sanguo.ui.window.BloodWindow;
import com.vikings.sanguo.ui.window.BronzeTerraceEnterWindow;
import com.vikings.sanguo.ui.window.BuildingListWindow;
import com.vikings.sanguo.ui.window.BuildingTypeListWindow;
import com.vikings.sanguo.ui.window.CampaignTroopSetWindow;
import com.vikings.sanguo.ui.window.CampaignWindow;
import com.vikings.sanguo.ui.window.CastleWindow;
import com.vikings.sanguo.ui.window.ChatUserListWindow;
import com.vikings.sanguo.ui.window.ChatWindow;
import com.vikings.sanguo.ui.window.DoubleRechargeWindow;
import com.vikings.sanguo.ui.window.FiefDetailWindow;
import com.vikings.sanguo.ui.window.FiefTroopWindow;
import com.vikings.sanguo.ui.window.FillUserBaseInfoWindow;
import com.vikings.sanguo.ui.window.FriendListWindow;
import com.vikings.sanguo.ui.window.GameSettingWindow;
import com.vikings.sanguo.ui.window.GodWealthWindow;
import com.vikings.sanguo.ui.window.GuildChatWindow;
import com.vikings.sanguo.ui.window.GuildUserListWindow;
import com.vikings.sanguo.ui.window.HeroCenterWindow;
import com.vikings.sanguo.ui.window.HeroDetailHDWindow;
import com.vikings.sanguo.ui.window.HeroExchangeListWindow;
import com.vikings.sanguo.ui.window.HeroSkillListWindow;
import com.vikings.sanguo.ui.window.HeroStrengthenWindow;
import com.vikings.sanguo.ui.window.HistoryWarInfoWindow;
import com.vikings.sanguo.ui.window.MonthRechargeWindow;
import com.vikings.sanguo.ui.window.OptionsSearchWindow;
import com.vikings.sanguo.ui.window.OtherGuildUserListWindow;
import com.vikings.sanguo.ui.window.OthersCastleWindow;
import com.vikings.sanguo.ui.window.PopupUI;
import com.vikings.sanguo.ui.window.QuestDetailWindow;
import com.vikings.sanguo.ui.window.QuestListWindow;
import com.vikings.sanguo.ui.window.RechargeCenterWindow;
import com.vikings.sanguo.ui.window.RechargeInputConfirmWindow;
import com.vikings.sanguo.ui.window.RechargeLogWindow;
import com.vikings.sanguo.ui.window.RechargeWindow;
import com.vikings.sanguo.ui.window.RetrievePwdWindow;
import com.vikings.sanguo.ui.window.ReviewArmInManorListWindow;
import com.vikings.sanguo.ui.window.RouletteGoodListWindow;
import com.vikings.sanguo.ui.window.RouletteWindow;
import com.vikings.sanguo.ui.window.SearchConditionsUserResultWindow;
import com.vikings.sanguo.ui.window.SearchWindow;
import com.vikings.sanguo.ui.window.ShopWindow;
import com.vikings.sanguo.ui.window.SmithyWindow;
import com.vikings.sanguo.ui.window.StoreWindow;
import com.vikings.sanguo.ui.window.UserEditWindow;
import com.vikings.sanguo.ui.window.UserInfoWindow;
import com.vikings.sanguo.ui.window.VipListWindow;
import com.vikings.sanguo.ui.window.WarInfoWindow;
import com.vikings.sanguo.ui.window.WarLordBox;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class MainActivity extends Activity implements GameController {

	// 用户浮动条
	private AccountBar accountBar;

	// 用户选择头像
	// private IconPickWindow iconPickWindow;

	// 聊天玩家列表
	private ChatUserListWindow chatUserListWindow;

	// 消息通知组件
	private NotifyUI notify;
	private NotifyWorldChatMsg notifyWorldChatMsg;

	// 滚动文字
	private Announce announce;

	// 弹出窗口栈
	private Stack<PopupUI> windowStack = new Stack<PopupUI>();
	// loading 提示
	private LoadingTip loadingTip;

	private FileAccess fileAccess;

	private HeartBeat heartBeat;

	private View backBt;

	private View guideWindow;

	private BitmapCache bitmapCache = new BitmapCache();

	private ViewCache viewCache = new ViewCache();

	protected Home home;

	private PhotoTaker photoTaker;

	private CastleWindow castleWindow;

	private FiefMap fiefMap;

	private NotifyMsg notifyMsg;

	// 记录键盘返回键是否能用
	private boolean backKeyValid = true;

	private Handler handler = new Handler();

	private VKPayMgr vkPayMgr;

	private ServerFileCache serverFileCache;

	private boolean restart = false, autoEnter = true;

	private int sdkLevel = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DipUtil.adjustDisplayMetrics(getResources().getDisplayMetrics());
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.logo);
		Config.setController(this);
		this.handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				init();
			}
		}, 200);
	}

	private void init() {
		// Debug.startMethodTracing("trace");

		Intent intent = getIntent();
		if (null != intent) {
			autoEnter = intent.getBooleanExtra("autoEnter", true);
		}

		SoundMgr.initSounds();
		fileAccess = new FileAccess(this);

		ViewGroup mainView = (ViewGroup) inflate(R.layout.main);

		guideWindow = mainView.findViewById(R.id.guideWindow);

		// 这里要设置默认分区
		serverFileCache = new ServerFileCache();
		serverFileCache.init();

		vkPayMgr = new VKPayMgr(this, Integer.parseInt(Config.gameId));
		home = new Home((ViewGroup) mainView.findViewById(R.id.mainLayout));
		home.doOpen();

		setContentView(mainView);

		postRunnable(new Runnable() {
			@Override
			public void run() {

				Notification.startService(MainActivity.this);
				new CrashHandler(999999);

				if (isNewer() && autoEnter) {
					home.autoEnter();
				} else {
					new CheckServerConfigBeforeEnterInvoker(home).start();
					new WebNoticeInvoker().start();
				}

				if (restart)
					home.enter();
				else {
					if (!PrefAccess.hasShortCutIcon()) {
						addShortCut();
						PrefAccess.addShortCutIcon();
					}
				}
			}
		});

	}

	/**
	 * 从Assets中读取图片
	 */
	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}

	private boolean isNewer() {
		if (Config.serverId == 0) {
			return false;
		} else {
			if (!fileAccess.hasUser())
				return true;
			else
				return false;
		}
	}

	private boolean isInit() {
		return heartBeat != null;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		restart = true;
		// if (!isInit()) {
		// if (home == null) {
		// home = new Home();
		// home.doOpen();
		// }
		// home.enter();
		// }
	}

	private void stop() {
		if (this.heartBeat != null)
			this.heartBeat.stop();
	}

	private void start() {
		if (this.heartBeat != null)
			this.heartBeat.start();
	}

	@Override
	protected void onPause() {
		stop();
		new SaveFilesInvoker(false).start();
		super.onPause();
		MediaPlayerMgr.getInstance().pauseSound();
	}

	@Override
	protected void onResume() {
		super.onResume();
		start();
		DipUtil.adjustDisplayMetrics(getResources().getDisplayMetrics());
		// MediaPlayerMgr.getInstance().restart();
	}

	public void quit() {
		new QuitConfirm(new QuitCallBack()).show();
	}

	protected void logout() {
		stop();
		MediaPlayerMgr.getInstance().realseSound();
		// 写文件，发登出
		new SaveFilesInvoker(true).start();
	}

	private class QuitCallBack implements CallBack {
		@Override
		public void onCall() {
			stop();
			MediaPlayerMgr.getInstance().realseSound();
			// 写文件，发登出
			new SaveFilesInvoker(true).start();
		}
	}

	public void killProcess() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/**
	 * 按后退
	 */
	public void goBack() {
		// 动画播放时设置返回键无效
		if (!backKeyValid) {
			return;
		}

		// 向导窗口时候按返回 提示退出
		if (ViewUtil.isVisible(guideWindow) || windowStack.empty()) {
			quit();
			return;
		}

		setAccountBarUser(Account.user);

		PopupUI popup = windowStack.peek();

		// 如果子类中返回true，则不继续下面逻辑
		if (popup.goBack())
			return;

		// 取当前弹出窗
		popup = windowStack.pop();

		// 回收当前弹出窗
		popup.doClose();
		// 取上一个弹出窗口
		while (true) {
			if (windowStack.empty()) {
				// 遍历任务，如果有引导任务，开始引导
				// checkQuest();
				break;
			}
			popup = windowStack.peek();
			// 已被回收的窗口
			if (!popup.isInit()) {
				windowStack.pop();
				continue;
			}
			popup.showUI();
			return;

		}
		ViewUtil.setGone(backBt);
	}

	@Override
	public PopupUI getCurPopupUI() {
		PopupUI popup = null;
		if (!windowStack.isEmpty()) {
			popup = windowStack.peek();
		}
		return popup;
	}

	public void refreshCurPopupUI() {
		PopupUI pop = getCurPopupUI();
		if (pop != null)
			pop.showUI();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
			return true;
		}

		AudioManager maudio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			maudio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			maudio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			return true;
		}
		return false;
	}

	@Override
	public Context getUIContext() {
		return this;
	}

	@Override
	public Bitmap getBitmap(int id) {
		Drawable d = getResources().getDrawable(id);
		if (d instanceof BitmapDrawable) {
			BitmapDrawable b = (BitmapDrawable) d;
			return b.getBitmap();
		} else
			return null;
	}

	@Override
	public Drawable getDrawable(int id) {
		return getResources().getDrawable(id);
	}

	private int findResId(String name) {
		return findResId(name, "drawable");
	}

	/**
	 * 只用于取颜色
	 */
	public String getResourceColorText(int resId) {
		CharSequence chars = getResources().getText(resId);
		if (null != chars) {
			String color = chars.toString();
			if (color.length() >= 3)
				color = "#" + color.substring(3, color.length());
			return color;
		} else
			return "";
	}

	@Override
	public int findResId(String name, String defType) {
		Resources res = getResources();
		String resName = name;
		int i = name.indexOf(".");
		if (i != -1) {
			resName = name.substring(0, i);
		}
		if (StringUtil.isNumeric(resName))
			return 0;
		return res.getIdentifier(resName, defType, this.getPackageName());
	}

	@Override
	public Drawable getDrawable(String name) {
		Bitmap bmp = getBitmap(name);
		if (bmp != null)
			return new BitmapDrawable(getResources(), bmp);
		else
			return null;
	}

	// 采用16位、RGB bitmap 在战斗中的图
	@Override
	public Drawable getDrawableHdInBattle(String name, int type) {
		Bitmap bmp = getBitmapHdInBattle(name, type);
		if (bmp != null)
			return new BitmapDrawable(getResources(), bmp);
		else
			return null;
	}

	@Override
	public Drawable getDrawable(String name, boolean reload) {
		if (reload)
			bitmapCache.remove(name);
		return getDrawable(name);
	}

	@Override
	public Drawable getDrawable(String name, int scale) {
		return getDrawable(name, scale, scale);
	}

	@Override
	public Drawable getDrawable(String name, int xScale, int yScale) {
		Bitmap b = getScaleBitmap(name, xScale, yScale);
		if (b != null)
			return new BitmapDrawable(getResources(), b);
		else
			return null;
	}

	@Override
	public Drawable getDrawable(String name, int xScale, int yScale, String post) {
		Bitmap b = getScaleBitmap(name, xScale, yScale, post);
		if (b != null)
			return new BitmapDrawable(getResources(), b);
		else
			return null;
	}

	@Override
	public Bitmap getScaleDrawable(Drawable d, String name, float xScale,
			float yScale) {
		if ((int) xScale == 100 && (int) yScale == 100)
			return ((BitmapDrawable) d).getBitmap();

		String newName = ImageUtil.imageScaleName(name, (int) xScale,
				(int) yScale);

		// 先找缓存
		Bitmap bitmap = bitmapCache.get(newName);
		if (bitmap != null)
			return bitmap;
		// 再找文件
		bitmap = ImageUtil.getGenerateImage(newName);
		if (bitmap != null) {
			bitmapCache.save(newName, bitmap);
			return bitmap;
		}
		// 还没有生成
		Bitmap b = ((BitmapDrawable) d).getBitmap();
		bitmap = ImageUtil.imageScale(b, newName, xScale, yScale);
		return bitmap;
	}

	@Override
	public boolean hasPic(String name) {
		int resId = findResId(name);
		if (resId > 0) {
			return true;
		}
		File f = fileAccess.readImage(name);
		return f.exists();
	}

	@Override
	public Bitmap getMirrorBitmap(String name) {
		String newName = ImageUtil.imageMirrorName(name);
		// 先找缓存
		Bitmap bitmap = bitmapCache.get(newName);
		if (bitmap != null)
			return bitmap;
		// 再找文件
		bitmap = ImageUtil.getGenerateImage(newName);
		if (bitmap != null) {
			bitmapCache.save(newName, bitmap);
			return bitmap;
		}
		// 还没有生成
		Bitmap b = getBitmap(name);
		bitmap = ImageUtil.imageMirror(b, name);
		if (bitmap == null)
			return null;
		bitmapCache.save(newName, bitmap);
		return bitmap;
	}

	@Override
	public Bitmap getRotateBitmap(String name, float degrees) {
		String newName = ImageUtil.imageRotateName(name, degrees);
		// 先找缓存
		Bitmap bitmap = bitmapCache.get(newName);
		if (bitmap != null)
			return bitmap;
		// 再找文件
		bitmap = ImageUtil.getGenerateImage(newName);
		if (bitmap != null) {
			bitmapCache.save(newName, bitmap);
			return bitmap;
		}
		// 还没有生成
		Bitmap b = getBitmap(name);
		bitmap = ImageUtil.imageRotate(b, name, degrees);
		if (bitmap == null)
			return null;
		bitmapCache.save(newName, bitmap);
		return bitmap;
	}

	@Override
	public Bitmap getScaleBitmap(String name, float xScale, float yScale) {
		if ((int) xScale == 100 && (int) yScale == 100)
			return getBitmap(name);
		String newName = ImageUtil.imageScaleName(name, (int) xScale,
				(int) yScale);
		// 先找缓存
		Bitmap bitmap = bitmapCache.get(newName);
		if (bitmap != null)
			return bitmap;
		// 再找文件
		bitmap = ImageUtil.getGenerateImage(newName);
		if (bitmap != null) {
			bitmapCache.save(newName, bitmap);
			return bitmap;
		}
		// 还没有生成
		Bitmap b = getBitmap(name);
		bitmap = ImageUtil.imageScale(b, name, xScale, yScale);
		if (bitmap == null)
			return null;
		bitmapCache.save(newName, bitmap);
		return bitmap;
	}

	@Override
	public Bitmap getScaleBitmap(String name, float xScale, float yScale,
			String post) {
		if ((int) xScale == 100 && (int) yScale == 100)
			return getBitmap(name);

		String newName = ImageUtil.imageScaleName(name, post);
		// 先找缓存
		Bitmap bitmap = bitmapCache.get(newName);
		if (bitmap != null)
			return bitmap;
		// 再找文件
		bitmap = ImageUtil.getGenerateImage(newName);
		if (bitmap != null) {
			bitmapCache.save(newName, bitmap);
			return bitmap;
		}
		// 还没有生成
		Bitmap b = getBitmap(name);
		bitmap = ImageUtil.imageScale(b, name, xScale, yScale, post);
		if (bitmap == null)
			return null;
		bitmapCache.save(newName, bitmap);
		return bitmap;
	}

	@Override
	public Bitmap getBitmap(String name) {
		if (StringUtil.isNull(name))
			return null;

		Bitmap bmp = null;
		// 先在缓存找
		bmp = bitmapCache.get(name);
		if (bmp != null)
			return bmp;

		int resId = findResId(name);
		// 在打包drawable内
		if (resId > 0) {
			InputStream is = getResources().openRawResource(resId);
			bmp = BitmapFactory.decodeStream(is, null,
					ImageUtil.getOptimizedOptions(name, false));
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		// 从sdCard读取
		else {
			bmp = ImageUtil.getResImage(name);
		}
		if (bmp == null)
			return null;
		bitmapCache.save(name, bmp);
		return bmp;
	}

	@Override
	public Bitmap getBitmapHdInBattle(String name, int type) {
		if (StringUtil.isNull(name))
			return null;

		Bitmap bmp = null;
		// 先在缓存找
		bmp = bitmapCache.get(name);
		if (bmp != null)
			return bmp;

		int resId = findResId(name);
		Options options = ImageUtil.getBitmapOption(type);
		// 在打包drawable内
		if (resId > 0) {
			InputStream is = getResources().openRawResource(resId);
			bmp = BitmapFactory.decodeStream(is, null, options);
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		// 从sdCard读取
		else {
			bmp = ImageUtil.getGenerateImage(name, options);
		}
		if (bmp == null)
			return null;
		bitmapCache.save(name, bmp);
		return bmp;
	}

	// 镜像 并且旋转
	@Override
	public Bitmap getMirrorRotateBitmap(String name, int horiMirror,
			int veriMirror, int degree, float xScale, float yScale,
			boolean isSave) {
		String newName = ImageUtil.imageMirrorName(name) + "_" + degree;
		// 先找缓存
		Bitmap bitmap = bitmapCache.get(newName);
		if (bitmap != null)
			return bitmap;
		// 再找文件
		bitmap = ImageUtil.getGenerateImage(newName);
		if (bitmap != null) {
			bitmapCache.save(newName, bitmap);
			return bitmap;
		}
		// 还没有生成
		Bitmap b = getBitmap(name);

		if (b != null) {
			bitmap = ImageUtil.imageMirrorRotate(b, name, horiMirror,
					veriMirror, degree, xScale, yScale, isSave);
		}
		if (bitmap == null) {
			return null;
		} else {
			if (b != null && b.isRecycled() == false) {
				b.recycle();
				b = null;
			}
		}
		bitmapCache.save(newName, bitmap);
		return bitmap;
	}

	public Bitmap getThumbnailBitmap(String name, int width) {
		Bitmap bmp = null;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// 在资源文件夹中找
		int resId = findResId(name);
		if (resId > 0) {
			InputStream is = getResources().openRawResource(resId);
			bmp = BitmapFactory.decodeStream(is, null, options);

			int w = options.outWidth;
			int scale = w / width;
			if (scale <= 0)
				scale = 1;
			else if (scale > 0 && scale <= 2)
				scale = 2;
			else
				scale = 4;

			options.inSampleSize = scale;
			options.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeStream(is, null, options);
			try {
				is.close();
			} catch (IOException e) {
			}
			return bmp;
		}

		// 在下载文件目录中找
		File f = fileAccess.readImage(name);
		if (f.exists()) {
			bmp = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
			int w = options.outWidth;
			int scale = w / width;
			if (scale <= 0)
				scale = 1;
			options.inSampleSize = scale;
			options.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
			return bmp;
		}
		// 没有找到 返回null
		return bmp;
	}

	public View getCachedView(int resId) {
		View v = null;
		v = viewCache.get(resId);
		if (v == null) {
			v = inflate(resId);
			viewCache.save(resId, v);
		}
		return v;
	}

	@Override
	public LayoutInflater getLayoutInflater() {
		LayoutInflater l = super.getLayoutInflater();
		DipUtil.adjustDisplayMetrics(getResources().getDisplayMetrics());
		return l;
	}

	@Override
	public View inflate(int resId) {
		return this.getLayoutInflater().inflate(resId, null);
	}

	@Override
	public View inflate(int resId, ViewGroup root) {
		return this.getLayoutInflater().inflate(resId, root, true);
	}

	@Override
	public View inflate(int resId, ViewGroup root, boolean attach) {
		return this.getLayoutInflater().inflate(resId, root, attach);
	}

	/**
	 * 为满足后台的tile要求，调整为tile中心
	 * 
	 * @param loc
	 */
	private void adjustLoc(Location loc) {
		long tileId = TileUtil.toTileId(loc);
		loc.setLatitude(((double) TileUtil.getTileCenterLat(tileId)) / 1E6D);
		loc.setLongitude(((double) TileUtil.getTileCenterLon(tileId)) / 1E6D);
	}

	@Override
	public void setCurLocation(Location location) {
		if (location == null)
			return;
		adjustLoc(location);
		this.fiefMap.getBattleMap().setCurLocation(location);
	}

	public void registerWindow(PopupUI popup) {
		// 窗口栈中第一位是popup 不需要再次添加
		if (!windowStack.empty() && windowStack.peek() == popup)
			return;
		if (!windowStack.empty()) {
			windowStack.peek().hideUI();
		}
		windowStack.push(popup);
		ViewUtil.setVisible(backBt);
	}

	@Override
	public void closeAllPopup() {
		while (!windowStack.empty()) {
			PopupUI popup = windowStack.peek();
			// 如果子类中返回true，则不继续下面逻辑
			if (popup instanceof CastleWindow) {
				popup.showUI();
				break;
			}
			windowStack.pop().doCloseMute();
		}
		ViewUtil.setGone(backBt);
		// 遍历任务，如果有引导任务，开始引导
		// checkQuest();
	}

	@Override
	public CastleWindow getCastleWindow() {
		return castleWindow;
	}

	@Override
	public void addContent(View v) {
		ViewGroup content = ((ViewGroup) this.findViewById(R.id.contentWindow));
		if (content.indexOfChild(v) == -1)
			content.addView(v, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));
	}

	@Override
	public void removeContent(View v) {
		if (v != null)
			((ViewGroup) this.findViewById(R.id.contentWindow)).removeView(v);
	}

	@Override
	public void addContentFullScreen(View v) {
		addContent(v);
	}

	@Override
	public void removeContentFullScreen(View v) {
		removeContent(v);
	}

	@Override
	public void updateUI(ReturnInfoClient rsinfo, boolean isGain) {
		updateUI(rsinfo, isGain, true, true);
	}

	@Override
	public void updateUI(ReturnInfoClient rsinfo, boolean isGain,
			boolean playDefaultSound, boolean showLevelUpTip) {
		accountBar.updateUI(rsinfo, isGain, playDefaultSound, showLevelUpTip);
	}

	public void alert(String titleStr, String msgStr, int gravity,
			String msgExtendStr, CallBack callBack, boolean playDefaultSound) {
		new MsgAlert().show(titleStr, msgStr, gravity, msgExtendStr, callBack,
				playDefaultSound);
	}

	public void alert(String titleStr, String msgStr, String msgExtendStr,
			CallBack callBack, boolean playDefaultSound) {
		alert(titleStr, msgStr, Gravity.CENTER, msgExtendStr, callBack,
				playDefaultSound);
	}

	@Override
	public void alert(String titleStr, String msgStr, CallBack callBack,
			boolean playDefaultSound) {
		alert(titleStr, msgStr, "", callBack, playDefaultSound);
	}

	@Override
	public void alert(String msgStr, CallBack callBack) {
		alert("", msgStr, callBack, true);
	}

	@Override
	public void alert(String msgStr) {
		alert(msgStr, null);
	}

	public void confirm(String msg, CallBack okHandler) {
		this.confirm(msg, null, okHandler, null);
	}

	@Override
	public void confirm(String msg, String msgTip, CallBack okHandler,
			final CallBack dismissHandler) {
		new MsgConfirm().show(msg, msgTip, okHandler, dismissHandler);
	}

	@Override
	public void confirm(String title, int scale, String msg, String msgTip,
			CallBack okHandler, CallBack dismissHandler) {
		new MsgConfirm(title, scale).show(msg, msgTip, okHandler,
				dismissHandler);
	}

	@Override
	public void confirm(String title, int scale, String msg, String msgTip,
			String okBtnStr, CallBack okHandler, String cancelBtnStr,
			CallBack dismissHandler) {
		MsgConfirm msgConfirm = new MsgConfirm(title, scale);
		if (!StringUtil.isNull(okBtnStr))
			msgConfirm.setOKText(okBtnStr);
		if (!StringUtil.isNull(cancelBtnStr))
			msgConfirm.setCancelText(cancelBtnStr);
		msgConfirm.show(msg, msgTip, okHandler, dismissHandler);

	}

	@Override
	public void showLoading(String msg) {
		if (loadingTip != null)
			loadingTip.show(msg);
	}

	@Override
	public void dismissLoading() {
		if (loadingTip != null)
			loadingTip.dismiss();
	}

	@Override
	public void showOptionsWindow() {
		new OptionsSearchWindow().open();
	}

	@Override
	public Location getCurLocation() {
		return this.fiefMap.getBattleMap().getCurLocation();
	}

	@Override
	public void openUserInfoEditWindow() {
		new UserEditWindow().show();
	}

	@Override
	public void openUserInfo() {
		new UserInfoWindow().open();
	}

	@Override
	public void openUserInfo(OtherUserClient ouc) {
		new UserInfoWindow().open(ouc);
	}

	public void openAccountMangt() {
		new AccountManagement().show();
	}

	@Override
	public void openFriendsWindow() {
		new FriendListWindow().open();
	}

	@Override
	public void openChatWindow(BriefUserInfoClient u) {
		if (null != notify && null != notify.getUser()
				&& notify.getUser().getId().intValue() == u.getId().intValue()) {
			notify.stopNotify();
		}
		new ChatWindow().open(u);
	}

	@Override
	public void openGroupChatWindow(int type) {
		new GuildChatWindow().open(type);
	}

	@Override
	public void openChatUserListWindow() {
		getNotify().stopNotify();
		chatUserListWindow.open();
	}

	@Override
	public void openSearchWindow() {
		new SearchWindow().show();
	}

	@Override
	public void openSearchResult(UserQuery query) {
		new SearchConditionsUserResultWindow(query).open();
	}

	@Override
	public void openStore(int index) {
		new StoreWindow().open(index);
	}

	@Override
	public void openShop() {
		new ShopWindow().open();
	}

	@Override
	public void openShop(int index) {
		new ShopWindow().open(index);
	}

	@Override
	public void openShop(int index, byte type, int id) {
		new ShopWindow().open(index, type, id);
	}

	@Override
	public void addChatUser(final List<BriefUserInfoClient> briefUsers) {
		chatUserListWindow.addChatUsers(briefUsers);
	}

	@Override
	public void refreshCurMap() {
		if (this.fiefMap != null)
			this.fiefMap.getBattleMap().refreshMap();
	}

	@Override
	public NotifyUI getNotify() {
		return notify;
	}

	@Override
	public FileAccess getFileAccess() {
		return fileAccess;
	}

	public void initUI1() {
		loadingTip = new LoadingTip();
	}

	public void initUI2() {
		chatUserListWindow = new ChatUserListWindow();
	}

	public void initUI3() {
		notifyMsg = new NotifyMsg();
		notifyWorldChatMsg = new NotifyWorldChatMsg();
		notify = new NotifyUI();
		announce = new Announce();
		backBt = findViewById(R.id.backBt);
		ViewUtil.setGone(backBt);
		backBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goBack();
			}
		});
	}

	public void initMap() {
		fiefMap = new FiefMap();
		if (Account.manorInfoClient.getPos() < 1)
			fiefMap.getBattleMap().startLocaltionListener();
		accountBar = new AccountBar();
		home = null;
	}

	public void initUserInfo() {
		// iconPickWindow = new IconPickWindow();
		photoTaker = PhotoTaker.getInstance();
		ImageLoader.getInstance();
		castleWindow = new CastleWindow();
	}

	public void initHeartBeat(SyncDataSet syncData) {
		if (null == heartBeat) {
			heartBeat = new HeartBeat(syncData);
		}
	}

	@Override
	public NotifyMsg getNotifyMsg() {
		return notifyMsg;
	}

	@Override
	public NotifyWorldChatMsg getNotifyWorldChatMsg() {
		return notifyWorldChatMsg;
	}

	@Override
	public void openFirst() {
		if (Account.user.getId() <= 0) {
			this.openFillUserBaseInfoWindow();
		} else if (!Account.readLog.PROLOGUE && Account.user.isNewUser()) {
			new Prologue().doOpen();
		} else {
			heartBeat.start();
			logNotify();
			castleWindow.doOpen();
			// 当天没有领奖 就主动弹出领奖界面 *付斌 在玩家完成天降横财的引导前，不主动弹出*
			if (Step301.isFinish() && !StepMgr.isRunning()) {
				if (!Account.isTodayChecked())
					Config.getController().openCheckIn();
				else
					showEventEntry();
			}

			handler.postDelayed(new SetCountry(), 1000 * 60 * 1);
			MediaPlayerMgr.getInstance().startSound(R.raw.game_default);
		}
	}

	@Override
	public void showEventEntry() {
		long lastShowTime = PrefAccess.getLastShowNoticeTime();
		if (!DateUtil.isSameDay(new Date(lastShowTime), new Date())) {
			new EventEntryAutoInvoker().start();
		}
	}

	private void logNotify() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					announce.getScrollText().fetchMsg();
					heartBeat.updateUI();
				} catch (GameException e) {
				}
			}
		}).start();
	}

	public void checkServer() {
		if (StringUtil.isNull(Config.checkNetwork())) {
			alert("无可用的网络连接，请确认开启网络重试");
			home.showMenu();
			return;
		}
		new CheckServerConfigInvoker(home).start();
	}

	public void checkVer() {
		if (StringUtil.isNull(Config.checkNetwork())) {
			alert("无可用的网络连接，请确认开启网络重试");
			home.showMenu();
			return;
		}
		new CheckVersion(home).start();
	}

	public void login() {
		new LoginInvoker().start();
	}

	public void loadConfig() {
		new LoadConfig(home).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1000 || requestCode == 1001) {
			// 1000和1001为相册或相机
			new Thread(new OnReturnHandler(requestCode, resultCode, data))
					.start();
			return;
		}
	}

	@Override
	public PhotoTaker getPhotoTaker() {
		return photoTaker;
	}

	private class OnReturnHandler implements Runnable {

		int requestCode;
		int resultCode;
		Intent data;

		public OnReturnHandler(int requestCode, int resultCode, Intent data) {
			this.requestCode = requestCode;
			this.resultCode = resultCode;
			this.data = data;
		}

		@Override
		public void run() {
			while (!isInit()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
			if (null != photoTaker)
				photoTaker.onReturn(requestCode, resultCode, data);
		}

	}

	@Override
	public Activity getMainActivity() {
		return this;
	}

	@Override
	public void openGameSetting(boolean fullScreen) {
		new GameSettingWindow(fullScreen).doOpen();
	}

	@Override
	public void openRertievePwd(boolean reboot, boolean fullScreen) {
		new RetrievePwdWindow(reboot, fullScreen).doOpen();
	}

	@Override
	public AccountBar getAccountBar() {
		return accountBar;
	}

	@Override
	public void openFillUserBaseInfoWindow() {
		new FillUserBaseInfoWindow().open();
	}

	@Override
	public BitmapCache getBitmapCache() {
		return this.bitmapCache;
	}

	@Override
	public HeartBeat getHeartBeat() {
		return heartBeat;
	}

	@Override
	public ScrollText getScrollText() {
		return announce.getScrollText();
	}

	@Override
	public void setBackBt(boolean visibale) {
		if (visibale) {
			ViewUtil.setVisible(backBt);
		} else {
			ViewUtil.setGone(backBt);
		}
	}

	@Override
	public boolean isBackBtVisibale() {
		if (null != backBt)
			return ViewUtil.isVisible(backBt);
		else
			return false;
	}

	@Override
	public void openSite(String strUri) {
		Uri uri = Uri.parse(strUri);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	@Override
	public void openQuestListWindow() {
		new QuestListWindow().open();
	}

	@Override
	public void openQuestListWindow(QuestInfoClient qi) {
		new QuestListWindow().open(qi);
	}

	@Override
	public boolean getBackKeyValid() {
		return this.backKeyValid;
	}

	@Override
	public void setBackKeyValid(boolean valid) {
		this.backKeyValid = valid;
	}

	@Override
	public void openQuestDetailWindow(QuestInfoClient qic) {
		new QuestDetailWindow().open(qic);
	}

	@Override
	public void reboot() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("autoEnter", false);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		killProcess();
	}

	@Override
	public void setAccountBarUser(UserAccountClient user) {

	}

	@Override
	public Handler getHandler() {
		return handler;
	}

	@Override
	public void openRechargeCenterWindow() {
		new RechargeCenterWindow().open();
	}

	@Override
	public void openRechargeWindow(BriefUserInfoClient user) {
		openRechargeWindow(RechargeWindow.TYPE_REWARD_RECHARGE, user);
	}

	@Override
	public void openRechargeWindow(byte type, BriefUserInfoClient user) {
		new RechargeWindow().open(type, user);
	}

	@Override
	public void openRechageLogWindow() {
		new RechargeLogWindow().open();
	}

	@Override
	public boolean checkCurLocation() {
		if (null == getCurLocation()) {
			alert("请等待定位");
			return false;
		}
		return true;
	}

	@Override
	public BattleMap getBattleMap() {
		if (this.fiefMap == null)
			return null;
		else
			return this.fiefMap.getBattleMap();
	}

	@Override
	public void openArmTrainingWindow(TroopProp prop) {
		new ArmTrainingWindow().open(prop);
	}

	@Override
	public void openReviewArmInManorListWindow() {
		new ReviewArmInManorListWindow().open();
	}

	public void openWarInfoWindow(BriefFiefInfoClient fief) {
		if (getCurPopupUI() != null && getCurPopupUI() instanceof WarInfoWindow) {
			goBack();
		}
		new WarInfoWindow().open(fief);
	}
	
	@Override
	public void openHistoryWarInfoWindow(BriefFiefInfoClient briefFiefInfoClient) {
		new HistoryWarInfoWindow().open(briefFiefInfoClient);
	}

	@Override
	public void openFiefTroopWindow(BriefFiefInfoClient bfic) {
		new FiefTroopWindow().open(bfic);

	}

	@Override
	public void openGuildUserListWindow(RichGuildInfoClient rgic) {
		new GuildUserListWindow().open(rgic);
	}

	@Override
	public void openOtherGuildUserListWindow(OtherRichGuildInfoClient orgic) {
		new OtherGuildUserListWindow().open(orgic);
	}

	@Override
	public void openGuildChatWindow(RichGuildInfoClient rgic) {
		if (null != notify && null != notify.getChatData()) {
			notify.stopNotify();
		}

		if (Account.guildCache.getGuildid() != rgic.getGuildid()) {
			alert("你已经不在该家族中,不能执行该操作");
			return;
		}

		new GuildChatWindow().open(rgic);

	}

	@Override
	public void openStrengthenWindow(HeroInfoClient heroInfoClient) {
		new HeroStrengthenWindow().open(heroInfoClient);
	}

	@Override
	public void openHeroSkillListWindow(HeroInfoClient heroInfoClient,
			HeroSkillSlotInfoClient heroSkillSlotInfoClient) {
		new HeroSkillListWindow().open(heroInfoClient, heroSkillSlotInfoClient);

	}

	@Override
	public String getStringById(int resId) {
		return this.getResources().getString(resId);
	}

	@Override
	public Home getHome() {
		return home;
	}

	@Override
	public void openActTypeWindow() {
		new ActTypeWindow().doOpen();
	}

	@Override
	public void openActWindow(PropActType propActType) {
		new ActWindow().open(propActType);
	}

	@Override
	public void openCampaignWindow(ActInfoClient actClient) {
		new CampaignWindow().open(actClient);
	}

	public void postRunnable(Runnable r) {
		this.handler.post(r);
	}

	@Override
	public void openFiefDetailWindow(BriefFiefInfoClient bfic) {
		new FiefDetailWindow().open(bfic);
	}

	@Override
	public void openSetOffTroopWindow(CampaignInfoClient campaignClient) {
		new CampaignTroopSetWindow().open(campaignClient);
	}

	@Override
	public FiefMap getFiefMap() {
		return fiefMap;
	}

	@Override
	public void hideIconForFullScreen() {
		ViewUtil.setGone(findViewById(R.id.systemAnnonce));
		ViewUtil.setGone(findViewById(R.id.systemGoback));
	}

	@Override
	public void showIconForFullScreen() {
		ViewUtil.setVisible(findViewById(R.id.systemAnnonce));
		ViewUtil.setVisible(findViewById(R.id.systemGoback));
	}

	@Override
	public void hideSystemAnnonce() {
		ViewUtil.setGone(findViewById(R.id.systemAnnonce));
	}

	@Override
	public void showSystemAnnonce() {
		ViewUtil.setVisible(findViewById(R.id.systemAnnonce));
	}

	@Override
	public void showCastle(BriefUserInfoClient user) {
		if (Account.user.getId() == user.getId()) {
			closeAllPopup();
		} else {
			new OthersCastleWindow().open(user.getId().intValue());
		}
	}

	@Override
	public void showCastle(int userId) {
		if (Account.user.getId() == userId) {
			closeAllPopup();
		} else {
			new OthersCastleWindow().open(userId);
		}
	}

	@Override
	public void pay(int channel, int userId, int amount, String exParam) {
		if (null != vkPayMgr) {
			showLoading("订单提交中...");
			vkPayMgr.pay(channel, userId, amount, exParam,
					new VKChargeListener());
		}
	}

	@Override
	public VKPayMgr getVKPayMgr() {
		return vkPayMgr;
	}

	public ServerFileCache getServerFileCache() {
		return serverFileCache;
	}

	@Override
	public void moveToFief(long tileId) {
		closeAllPopup();
		getFiefMap().backToMap();
		getBattleMap().moveToFief(tileId, true, true);
	}

	@Override
	public int getSdkLevel() {
		if (sdkLevel == -1)
			sdkLevel = android.os.Build.VERSION.SDK_INT;
		return sdkLevel;
	}

	public void addShortCut() {

		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		// 是否允许重复创建
		shortcut.putExtra("duplicate", false);

		// 设置属性
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getResources().getString(R.string.app_name));

		// 设置桌面快捷方式的图标
		Parcelable icon = Intent.ShortcutIconResource.fromContext(this,
				R.drawable.icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// 点击快捷方式的操作
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(this, MainActivity.class);
		// 设置启动程序
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 广播通知桌面去创建
		sendBroadcast(shortcut);
	}

	@Override
	public void openArenaWindow() {
		new ArenaWindow().open();
	}

	@Override
	public void openBloodWindow() {
		if (!Account.myLordInfo.hasReward()) {
			new BloodWindow().open();
		} else {
			new BloodRewardWindow().open();
		}
	}

	@Override
	public void openRechargeInputConfirmWindow(RechargeCardInfo info) {
		new RechargeInputConfirmWindow().open(info);
	}

	@Override
	public void openCheckIn() {
		new DailyRewardTip().show();
	}

	@Override
	public void openHolyFiefDetail(HolyCategory hc) {
		new HolyFiefSearchTip(hc).show();
	}

	// 外敌入侵
	@Override
	public void openForeignInvasionFiefTip() {
		new ForeignInvasionFiefTip().show();
	}

	@Override
	public void openRouletteWindow() {
		new RouletteWindow().open();
	}

	// 铜雀台
	@Override
	public void openBronzeTerraceEnterWindow() {
		new BronzeTerraceEnterWindow().open();
	}

	// 战神宝箱
	@Override
	public void openWarLordBox() {
		new WarLordBox().open();
	}

	// 天降横财
	@Override
	public void openGodWealth() {
		new GodWealthWindow().open();
	}

	@Override
	public void openRouletteGoodListWindow() {
		new RouletteGoodListWindow().open();
	}

	// 我要变强
	@Override
	public void openStronger() {
		new StrongerWindow().show();
	}

	@Override
	public void openVipListWindow() {
		new VipListWindow().open();
	}

	@Override
	public void openHeroDetailHDWindow(HeroProp hp) {
		new HeroDetailHDWindow().open(hp);
	}

	@Override
	public void openDoubleRechargeWindow() {
		new DoubleRechargeWindow().open();
	}

	@Override
	public void openMonthRechargeWindow() {
		new MonthRechargeWindow().open();
	}

	@Override
	public void openAssistGodSoldierWindow(String title,
			RichBattleInfoClient rbic) {
		new AssistGodSoldierWindow().open(title, rbic);
	}

	@Override
	public void openAssistGodSoldierWindow(String title,
			RichBattleInfoClient rbic, int battleType) {
		new AssistGodSoldierWindow().open(title, rbic, battleType);
	}

	@Override
	public void openBarWindow() {
		new BarWindow().open();
	}

	@Override
	public void openHeroExchangeListWindow() {
		new HeroExchangeListWindow().open();
	}

	@Override
	public void openHeroCenterWindow() {
		new HeroCenterWindow().open();
	}

	@Override
	public void openSmithyWindow() {
		new SmithyWindow().open();
	}

	@Override
	public void openArmTrainingListWindow() {
		new ArmTrainingListWindow().open();
	}

	@Override
	public void openBuildingTypeListWindow() {
		new BuildingTypeListWindow().open();
	}

	@Override
	public void openBuildingListWindow(BuildingType type) {
		new BuildingListWindow().open(type);
	}

	@Override
	public View getBackKey() {
		return findViewById(R.id.systemGoback);
	}

	@Override
	public void addContentFullScreenGuide(View v) {
		ViewGroup content = ((ViewGroup) this.findViewById(R.id.contentWindow));
		if (content.indexOfChild(v) == -1) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT);
			layoutParams.setMargins(10, 45, 10, 60);
			content.addView(v, layoutParams);
		}
	}
}
