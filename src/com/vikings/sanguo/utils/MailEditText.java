/**
 *  
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  All right reserved.
 *
 *  Time : 2012-9-4
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 处理邮箱的EditText 
 */

package com.vikings.sanguo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;

public class MailEditText {
	private EditText editText;
	private ViewGroup mailSuffixFrame; // 邮箱后缀
	private MailSuffixAdapter adapter;

	public MailEditText(EditText editText, ViewGroup mailSuffixFrame) {
		this.editText = editText;
		this.mailSuffixFrame = mailSuffixFrame;
		editText.addTextChangedListener(mailWatcher);
		// 邮箱后缀数组
		String[] suffix = { "@qq.com", "@vip.qq.com", "@163.com", "@126.com",
				"@sina.com", "@hotmail.com", "@gmail.com" };
		adapter = new MailSuffixAdapter();
		adapter.addItems(suffix);
	}

	private TextWatcher mailWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// EditView为空时，备选框消失
			if (StringUtil.isNull(s.toString()))
				ViewUtil.setGone(mailSuffixFrame);
			else {
				// EditView内有一个字符时，就会弹出备选框
				if (s.length() == 1 && ViewUtil.isGone(mailSuffixFrame))
					ViewUtil.setVisible(mailSuffixFrame);

				// 输入框有字符后，有没有@是判断是否弹出备选框的唯一标准
				if (s.toString().contains("@"))
					ViewUtil.setGone(mailSuffixFrame);
				else
					ViewUtil.setVisible(mailSuffixFrame);
			}
		}
	};

	private class MailSuffixAdapter extends ObjectAdapter implements
			OnClickListener {

		@Override
		public int getLayoutId() {
			return R.layout.mail_suffix;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (null == convertView) {
				convertView = Config.getController().inflate(getLayoutId());
				holder = new ViewHolder();
				holder.mailSuffix = (TextView) convertView
						.findViewById(R.id.suffix);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String str = (String) getItem(position);
			ViewUtil.setText(holder.mailSuffix, str);
			convertView.setOnClickListener(this);
			return convertView;
		}

		@Override
		public void setViewDisplay(View v, Object o, int index) {
		}

		@Override
		public void onClick(View view) {
			View v = view.findViewById(R.id.suffix);
			if (v != null) {
				TextView textView = (TextView) v;
				String str = editText.getText().toString();
				// 如果editView中最后一个字符是@，则去掉它
				if (str.lastIndexOf("@") == str.length() - 1)
					str = str.substring(0, str.length() - 1);
				StringBuffer buf = new StringBuffer(str);
				buf.append(textView.getText());
				ViewUtil.setEditText(editText, buf.toString());
			}
		}

		class ViewHolder {
			public TextView mailSuffix;
		}
	}

	public MailSuffixAdapter getAdapter() {
		return adapter;
	}
}
