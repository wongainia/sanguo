package com.vikings.sanguo.ui.alert;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class QuitConfirm extends CustomConfirmDialog {

	private CallBack callBack;;

	public QuitConfirm(CallBack cb) {
		super("退出游戏", DEFAULT);
		this.callBack = cb;
		setButton(FIRST_BTN, "退出", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (null != callBack)
					callBack.onCall();
			}
		});
		setButton(SECOND_BTN, "取消", closeL);
	}

	@Override
	protected View getContent() {
		TextView textView = new TextView(Config.getController().getUIContext());
		textView.setTextAppearance(Config.getController().getUIContext(),
				R.style.color13_14);
		textView.setGravity(Gravity.LEFT);
		ViewUtil.setRichText(
				textView,
				Account.myLordInfo != null ? Account.myLordInfo
						.checkFoodOnExit() : "");
		// textView.setText("是否退出游戏" + NetStat.getInstance().getFlowConsume());
		textView.setPadding((int) (10 * Config.SCALE_FROM_HIGH),
				(int) (20 * Config.SCALE_FROM_HIGH),
				(int) (10 * Config.SCALE_FROM_HIGH),
				(int) (0 * Config.SCALE_FROM_HIGH));
		return textView;
	}

}
