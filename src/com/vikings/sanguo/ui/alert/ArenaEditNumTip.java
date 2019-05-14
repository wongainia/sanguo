package com.vikings.sanguo.ui.alert;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;

import com.vikings.sanguo.R;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ArenaEditNumTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_edit_num;

	private EditText num;
	private int cnt, left;
	private SeekBar seekBar;
	private CallBack callBack;

	public ArenaEditNumTip() {
		super("请精确输入数量", DEFAULT);
		num = (EditText) content.findViewById(R.id.num);
		setButton(FIRST_BTN, "确定", this);
		setButton(SECOND_BTN, "放弃", closeL);
	}

	public void show(SeekBar seekBar, int left, CallBack callBack) {
		if (null == seekBar)
			return;
		this.seekBar = seekBar;
		this.left = left;
		this.callBack = callBack;
		setValue();
		super.show();
		num.postDelayed(new Runnable() {
			@Override
			public void run() {
				num.selectAll();
				num.requestFocus();
				InputMethodManager inputManager = (InputMethodManager) num
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(num, 0);
			}
		}, 200);

	}

	private void setValue() {
		cnt = seekBar.getProgress();
		num.setText(String.valueOf(getCount(left + cnt)));
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

	@Override
	public void onClick(View v) {
		int count = 0;
		try {
			count = Integer.parseInt(num.getText().toString());
		} catch (Exception e) {
			count = left + cnt;
		}
		count = getCount(count);
		seekBar.setProgress(count);
		if (null != callBack)
			callBack.onCall();
		dismiss();
	}

	protected int getCount(int count) {
		if (count > left + cnt)
			count = left + cnt;
		if (count > seekBar.getMax())
			count = seekBar.getMax();
		if (count < 0)
			count = 0;
		return count;
	}

}
