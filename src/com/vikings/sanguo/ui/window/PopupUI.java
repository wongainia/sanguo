package com.vikings.sanguo.ui.window;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.sound.MediaPlayerMgr;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.BaseUI;
import com.vikings.sanguo.ui.ImageHolder;

abstract public class PopupUI extends BaseUI {

	protected boolean isPopup = false;

	private boolean isInit = false;

	protected abstract View getPopupView();

	protected ImageHolder imageHolder = new ImageHolder();

	private Runnable refresh = null;

	/**
	 * 窗口打开前的初始化工作
	 */
	protected abstract void init();

	/**
	 * 窗口关闭后的回收内存工作
	 */
	protected abstract void destory();

	/**
	 * 打开弹出ui
	 */
	public void doOpen() {
		init();
		isInit = true;
		showUI();
		doOpenPlay();
	}

	/**
	 * 刷新时间间隔 子类若需要自动刷新界面 在这里返回大于0 的毫秒数 ， 界面会自动轮询调用showUI
	 * 
	 * @return
	 */
	protected int refreshInterval() {
		return 0;
	}

	/**
	 * 关闭弹出ui
	 */
	public void doClose() {
		doCloseMute();
		doClosePlay();
	}

	public void doCloseMute() {
		hideUI();
		imageHolder.recycle();
		destory();
		isInit = false;
		System.gc();
	}

	/**
	 * 如果子类需要改变声音，可重写
	 */
	public void doOpenPlay() {
		SoundMgr.play(R.raw.sfx_window_open);
	}

	public void doClosePlay() {
		SoundMgr.play(R.raw.sfx_button_default);
	}

	public void toggle() {
		if (this.isPopup) {
			doClosePlay();
			doClose();
		} else {
			doOpenPlay();
			doOpen();
		}
	}

	public void hideUI() {
		Config.lastUpdateTime = System.currentTimeMillis();
		this.isPopup = false;
		// popupView.startAnimation(Anim.hide);
		getPopupView().setVisibility(View.GONE);
	}

	public void showUI() {
		Config.lastUpdateTime = System.currentTimeMillis();
		// 需要自动刷新
		if (refreshInterval() > 0) {
			// 若未初始化过work 初始化 挂第一次
			if (refresh == null) {
				refresh = new RefreshWorker();
				getPopupView().postDelayed(refresh, refreshInterval());
			}
		}

		if (this.isPopup)
			return;
		this.isPopup = true;
		controller.registerWindow(this);
		// popupView.startAnimation(Anim.transShow);
		getPopupView().setVisibility(View.VISIBLE);
		MediaPlayerMgr mm = MediaPlayerMgr.getInstance();
		if (mm.isPause())
			mm.restartSound();
	}

	public boolean isShow() {
		return isPopup;
	}

	public boolean isInit() {
		return isInit;
	}

	public View findViewById(int id) {
		return getPopupView().findViewById(id);
	}

	// 点返回按钮调用，子类有特殊业务逻辑，重写该方法;
	// 如果返回true，处理完子类goBack的逻辑后就结束，否则，继续MainActivity中goBack的逻辑
	public boolean goBack() {
		return false;
	}

	private class RefreshWorker implements Runnable {
		@Override
		public void run() {
			// 界面已不再顶层或者关闭掉
			if (!isShow() || 0 == refreshInterval()) {
				refresh = null;
				return;
			}
			refreshUI();
			getPopupView().postDelayed(refresh, refreshInterval());
		}

	}

	protected void refreshUI() {
		showUI();
	}

}
