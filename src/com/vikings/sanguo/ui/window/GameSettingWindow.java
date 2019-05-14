package com.vikings.sanguo.ui.window;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.invoker.DeleteCfgInvoker;
import com.vikings.sanguo.invoker.DeleteSDCardImgInvoker;
import com.vikings.sanguo.sound.MediaPlayerMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UpdateVersion;
import com.vikings.sanguo.ui.alert.BigMsgAlert;
import com.vikings.sanguo.ui.alert.MsgConfirm;
import com.vikings.sanguo.ui.pick.TimePick;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class GameSettingWindow extends CustomPopupWindow implements
		OnClickListener, CallBack {

	public static final String tag = "com.vikings.sanguo.sharedPreferences";
	// 背景音乐
	private static final String MUSICSETTING = "MUSICSETTING";
	// 音效
	private static final String SOUNDSETTING = "SOUNDSETTING";
	public static final String MAPSETTING = "MAPSETTING";
	public static final String NOTIFYSETTING = "NOTIFYSETTING";
	// 自定义头像
	private static final String ICONSETTING = "ICONSETTING";

	public static final String VALUEOPEN = "OPEN";
	public static final String VALUECLOSE = "CLOSE";
	public static final String VALUETIME = "TIMING";

	public static final String BEGIN = "BEGINTIME";
	public static final String END = "ENDTIME";

	private String musicSettingValue = VALUEOPEN;
	private String soundSettingValue = VALUEOPEN;
	private String mapSettingValue = VALUEOPEN;
	private String iconSettingValue = VALUEOPEN;
	private String notifySettingValue = VALUEOPEN;

	private String beginTime = "8:00";

	private String endTime = "20:00";

	private View musicOpen, soundOpen, mapOpen, notifyOpen, iconOpen, clearImg,
			clearCfg, reload;

	private boolean fullScreen;

	public GameSettingWindow(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	@Override
	protected void init() {
		super.init("游戏设置", fullScreen);
		setContent(R.layout.game_setting);
		setBottomButton("保存并返回", new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences share = Config.getController().getUIContext()
						.getSharedPreferences(tag, Context.MODE_PRIVATE);
				share.edit().putString(MUSICSETTING, musicSettingValue)
						.putString(SOUNDSETTING, soundSettingValue)
						.putString(MAPSETTING, mapSettingValue)
						.putString(NOTIFYSETTING, notifySettingValue)
						.putString(ICONSETTING, iconSettingValue)
						.putString(BEGIN, beginTime).putString(END, endTime)
						.commit();
				Setting.music = musicSettingValue;
				Setting.sound = soundSettingValue;
				Setting.map = mapSettingValue;
				Setting.notify = notifySettingValue;
				Setting.icon = iconSettingValue;
				Setting.noticeBegin = beginTime;
				Setting.noticeEnd = endTime;
				if (controller.getBattleMap() != null) {
					if (VALUECLOSE.equals(Setting.map)) {
						controller.getBattleMap().getMapView()
								.setShowMap(false);
					} else {
						controller.getBattleMap().getMapView().setShowMap(true);
					}
				}
				controller.refreshCurMap();
				// controller.alert("设置成功");
				new BigMsgAlert().show("设置成功", true);
				Config.getController().goBack();
				MediaPlayerMgr.getInstance().musicStateChange();
			}
		});

		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		if (share != null) {
			musicSettingValue = share.getString(MUSICSETTING, VALUEOPEN);
			soundSettingValue = share.getString(SOUNDSETTING, VALUEOPEN);
			mapSettingValue = share.getString(MAPSETTING, VALUEOPEN);
			notifySettingValue = share.getString(NOTIFYSETTING, VALUEOPEN);
			iconSettingValue = share.getString(ICONSETTING, VALUEOPEN);
			beginTime = share.getString(BEGIN, "8:00");
			endTime = share.getString(END, "20:00");
		}

		Setting.music = musicSettingValue;
		Setting.sound = soundSettingValue;
		Setting.map = mapSettingValue;
		Setting.notify = notifySettingValue;
		Setting.icon = iconSettingValue;

		musicOpen = window.findViewById(R.id.musicOpen);
		musicOpen.setOnClickListener(this);

		soundOpen = window.findViewById(R.id.soundOpen);
		soundOpen.setOnClickListener(this);

		mapOpen = window.findViewById(R.id.mapOpen);
		mapOpen.setOnClickListener(this);

		notifyOpen = window.findViewById(R.id.notifyOpen);
		notifyOpen.setOnClickListener(this);

		iconOpen = window.findViewById(R.id.iconOpen);
		iconOpen.setOnClickListener(this);

		clearImg = window.findViewById(R.id.clearImg);
		clearImg.setOnClickListener(this);

		clearCfg = window.findViewById(R.id.clearCfg);
		clearCfg.setOnClickListener(this);

		reload = window.findViewById(R.id.reload);
		reload.setOnClickListener(this);

		switchState(musicSettingValue, VALUEOPEN, musicOpen);
		switchState(soundSettingValue, VALUEOPEN, soundOpen);
		switchState(iconSettingValue, VALUEOPEN, iconOpen);
		switchState(mapSettingValue, VALUEOPEN, mapOpen);
		switchState(notifySettingValue, VALUEOPEN, notifyOpen);

		new TimePick((TextView) window.findViewById(R.id.begin), "开始时间", this);
		new TimePick((TextView) window.findViewById(R.id.end), "结束时间", this);
		ViewUtil.setText(window, R.id.begin, beginTime);
		ViewUtil.setText(window, R.id.end, endTime);
	}

	private void switchState(String name, String value, View v) {
		if (name.equals(value))
			ViewUtil.setVisible(v, R.id.set);
		else
			ViewUtil.setGone(v, R.id.set);
	}

	@Override
	public void onClick(View v) {
		if (v == musicOpen) {
			if (musicSettingValue.equals(VALUEOPEN))
				musicSettingValue = VALUECLOSE;
			else
				musicSettingValue = VALUEOPEN;

			switchState(musicSettingValue, VALUEOPEN, musicOpen);
		} else if (v == soundOpen) {
			if (soundSettingValue.equals(VALUEOPEN))
				soundSettingValue = VALUECLOSE;
			else
				soundSettingValue = VALUEOPEN;

			switchState(soundSettingValue, VALUEOPEN, soundOpen);
		} else if (v == iconOpen) {
			if (iconSettingValue.equals(VALUEOPEN))
				iconSettingValue = VALUECLOSE;
			else
				iconSettingValue = VALUEOPEN;

			switchState(iconSettingValue, VALUEOPEN, iconOpen);
		} else if (v == mapOpen) {
			if (mapSettingValue.equals(VALUEOPEN))
				mapSettingValue = VALUECLOSE;
			else
				mapSettingValue = VALUEOPEN;

			switchState(mapSettingValue, VALUEOPEN, mapOpen);
		} else if (v == notifyOpen) {
			if (notifySettingValue.equals(VALUEOPEN))
				notifySettingValue = VALUECLOSE;
			else
				notifySettingValue = VALUEOPEN;

			switchState(notifySettingValue, VALUEOPEN, notifyOpen);
		} else if (v == clearImg) {
			new MsgConfirm("清除缓存图片", CustomConfirmDialog.DEFAULT).show(
					"确定清除SD卡中缓存图片吗？", new CallBack() {
						@Override
						public void onCall() {
							new DeleteSDCardImgInvoker().start();
						}

					}, null);
		} else if (v == clearCfg) {
			new MsgConfirm("清除缓存图片", CustomConfirmDialog.DEFAULT).show(
					"确定要修复数据文件吗？", new CallBack() {
						@Override
						public void onCall() {
							new DeleteCfgInvoker().start();
						}

					}, null);
		} else if (v == reload) {
			// 新的方案
			MsgConfirm msgConfirm = new MsgConfirm("下载安装最新客户端",
					CustomConfirmDialog.DEFAULT);
			((TextView) msgConfirm.getOkButton()).setText("立即下载");
			msgConfirm.show("下载需要消耗流量,建议在出现严重问题时使用,并在wifi环境下载", new CallBack() {
				@Override
				public void onCall() {
					if (Config.getController().getFiefMap() == null) {
						Config.getController().goBack();
						new UpdateVersion(controller.getHome()).start();
					} else {
						new UpdateVersion(controller.getCastleWindow()
								.showDownload()).start();
						controller.closeAllPopup();
					}

				}
			}, null);
		}
	}

	// 内存/SD卡小于10M时，打开设置窗口
	public void open() {
		doOpen();
	}

	@Override
	public void onCall() {
		notifySettingValue = VALUETIME;
		beginTime = ((TextView) window.findViewById(R.id.begin)).getText()
				.toString();
		endTime = ((TextView) window.findViewById(R.id.end)).getText()
				.toString();
	}
}
