package com.vikings.sanguo.ui.pick;

import java.util.Calendar;
import java.util.Date;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.DateUtil;

public class TimePick implements OnClickListener, OnTimeSetListener {

	private TextView text;

	private String title = "请选择开始时间";

	private Date initTime;

	private TimePickerDialog dialog;

	private CallBack callBack;

	public TimePick(TextView text, String title, CallBack callBack) {
		this.text = text;
		text.setOnClickListener(this);
		if (title != null)
			this.title = title;
		this.callBack = callBack;
	}

	@Override
	public void onClick(View v) {
		if (v == text) {
			try {
				initTime = DateUtil.time.parse(text.getText().toString()
						+ ":00");
			} catch (Exception e) {
				initTime = new Date();
			}
			Calendar ca = Calendar.getInstance();
			ca.setTime(initTime);
			int hour = ca.get(Calendar.HOUR_OF_DAY);
			int minute = ca.get(Calendar.MINUTE);
			dialog = new TimePickerDialog(
					Config.getController().getUIContext(), this, hour, minute,
					true);
			dialog.setTitle(title);
			dialog.show();
		}
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Calendar ca = Calendar.getInstance();
		// ca.set(2000, 1, 1, hourOfDay, minute, 0);
		ca.set(Calendar.HOUR_OF_DAY, hourOfDay);
		ca.set(Calendar.MINUTE, minute);
		ca.set(Calendar.SECOND, 0);
		String time = DateUtil.time.format(ca.getTime());
		int index = time.lastIndexOf(':');
		time = time.substring(0, index);
		text.setText(time);
		callBack.onCall();
	}
}
