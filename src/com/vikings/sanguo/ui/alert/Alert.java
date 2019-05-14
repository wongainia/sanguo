package com.vikings.sanguo.ui.alert;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.BaseUI;
import com.vikings.sanguo.ui.window.PopupUI;

abstract public class Alert extends BaseUI {

	protected Button close;

	protected Dialog dialog;

	protected boolean isShow = false;

	private Runnable refresh = null;

	// 刷新时间间隔 子类若需要自动刷新界面 在这里设置大于0 的毫秒数 ， 界面会自动轮询调用updateDynView
	protected int refreshInterval;

	protected boolean isTouchClose = false;
	
	private float dimming = 0.75f;

	protected void playSound() {
		SoundMgr.play(R.raw.sfx_button_default);
	}

	public Alert() {
		this(false);
	}

	public Alert(boolean isTouchClose) {
		this.isTouchClose = isTouchClose;
		if (isTouchClose)
			dialog = new TouchCloseDialog(controller.getUIContext(),
					new CallBack() {
						@Override
						public void onCall() {
							handleTouchClose();
						}
					});
		else
			dialog = new Dialog(controller.getUIContext(), R.style.dialog);
	}

	protected void handleTouchClose() {
		dismiss();
	}

	protected void show(View v) {
		Config.lastUpdateTime = System.currentTimeMillis();
		if (dialog.isShowing())
			return;
		playSound();

		dialog.show();
		setDimming(dimming);
		isShow = true;
		dialog.getWindow().setContentView(v);
		setRefresh(v);
		setButtons(v);
	}

	protected void show(View v, float dimming) {
		Config.lastUpdateTime = System.currentTimeMillis();
		if (dialog.isShowing())
			return;
		playSound();

		dialog.show();
		setDimming(dimming);
		isShow = true;
		dialog.getWindow().setContentView(v);
		setRefresh(v);
		setButtons(v);
	}

	private void setRefresh(View v) {
		// 需要自动刷新
		if (refreshInterval > 0) {
			// 若未初始化过work 初始化 挂第一次
			if (refresh == null) {
				refresh = new RefreshWorker(v);
				v.postDelayed(refresh, refreshInterval);
			}
		}
	}

	private void setButtons(View v) {
		close = (Button) v.findViewById(R.id.closeBt);
		if (close != null)
			close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					playSound();
					dismiss();
				}
			});
		this.dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				isShow = false;
				PopupUI curWin = controller.getCurPopupUI();
				if (curWin != null && curWin.isShow())
					curWin.showUI();
				doOnDismiss();
			}
		});
	}

	protected void doOnDismiss() {
	}

	public void dismiss() {
		Config.lastUpdateTime = System.currentTimeMillis();
		this.dialog.cancel();
		isShow = false;
	}

	protected void okDismiss() {
		Config.lastUpdateTime = System.currentTimeMillis();
		this.dialog.dismiss();
		isShow = false;
	}

	public boolean isShow() {
		return isShow;
	}

	@Override
	protected void bindField() {
	}

	protected void updateDynView() {
	}

	private class RefreshWorker implements Runnable {
		private View v;

		public RefreshWorker(View v) {
			this.v = v;
		}

		@Override
		public void run() {
			// 界面已不再顶层或者关闭掉
			if (!isShow()) {
				refresh = null;
				return;
			}
			updateDynView();
			this.v.postDelayed(refresh, refreshInterval);
		}

	}

	protected void setDimming(float dimming) {
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = dimming;
		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
}
