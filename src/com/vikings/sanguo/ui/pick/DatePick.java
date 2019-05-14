package com.vikings.sanguo.ui.pick;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class DatePick implements OnClickListener, OnDateSetListener {

	private TextView text,ageText;

	private String title = "请选择日期";

	private Date initDate;

	private DatePickerDialog dialog;
	
	public DatePick(TextView text,TextView ageText, String title) {
		this.text = text;
		this.ageText=ageText;
		text.setOnClickListener(this);
		if (title != null)
			this.title = title;
	}

	@Override
	public void onClick(View v) {
		if (v == text) {
			try {
				initDate = DateUtil.date1.parse(text.getText().toString());
			} catch (Exception e) {
				initDate = new Date();
			}
			Calendar ca = Calendar.getInstance();
			ca.setTime(initDate);
			int year = ca.get(Calendar.YEAR);
			int month = ca.get(Calendar.MONTH);
			int day = ca.get(Calendar.DAY_OF_MONTH);
			dialog = new DatePickerDialog(
					Config.getController().getUIContext(), this, year, month,
					day);
			dialog.setTitle(title);
			dialog.show();
		}

	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar ca = Calendar.getInstance();
		ca.set(year, monthOfYear, dayOfMonth);
		if (ca.getTime().getTime() > Config.serverTime()) {
			Config.getController().alert("生日不能晚于当前时间");
			return;
		}
		text.setText(DateUtil.date1.format(ca.getTime()));
		ViewUtil.setRichText(ageText, R.id.age, DateUtil.getAge(DateUtil.date1.format(new Date(getDate() * 1000L)))+"");
	}

	public int getDate() {
		Date date = DateUtil.getDate(text.getText().toString());
		if (date != null) {
			return (int) (date.getTime() / 1000);
		} else {
			return 0;
		}
	}
}
