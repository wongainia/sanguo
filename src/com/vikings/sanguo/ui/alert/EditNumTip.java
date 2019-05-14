package com.vikings.sanguo.ui.alert;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.SelectData;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class EditNumTip extends CustomConfirmDialog implements OnClickListener {

	private static final int layout = R.layout.alert_edit_num;

	private SelectData data;

	private EditText num;

	private CallBack cb;

	public EditNumTip(SelectData data, CallBack cb) {
		super("请精确输入数量", DEFAULT);
		this.data = data;
		num = (EditText) content.findViewById(R.id.num);
		num.setText("" + data.getSelCount());
		setButton(FIRST_BTN, "确定", this);
		setButton(SECOND_BTN, "放弃", closeL);
		this.cb = cb;
	}

	@Override
	public void show() {
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
		}
		if (count > data.getMax())
			count = data.getMax();
		else if (count < 0)
			count = 0;
		data.setSelCount(count);
		if (null != cb)
			cb.onCall();
		dismiss();
	}

}
