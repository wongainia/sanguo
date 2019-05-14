/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-9-4
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 处理电话号码的EditText
 */

package com.vikings.sanguo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneNumEditText {
	private final static String PHONE_NUM_SPLIT = "-";
	private final static int FIRST_PHONE_NUM_POS = 3;
	private final static int SECOND_PHONE_NUM_POS = 8;
	public final static int MAX_PHONE_NUM_LEN = 13;
	
	private EditText editText;
	
	public PhoneNumEditText(EditText editText) {
		this.editText = editText;
		this.editText.addTextChangedListener(phoneWatcher);
	}

	private TextWatcher phoneWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//回退时不加分隔符
			if (before < count) {
				//11位的手机号码，按344分割
				String str = s.toString();
				StringBuffer buf = new StringBuffer(str);
				if (FIRST_PHONE_NUM_POS == str.length()) {
					buf.append(PHONE_NUM_SPLIT);
					ViewUtil.setEditText(editText, buf.toString());
				}
					
				if (SECOND_PHONE_NUM_POS == str.length()) {
					buf.append(PHONE_NUM_SPLIT);
					ViewUtil.setEditText(editText, buf.toString());
				}
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {

		}
	};
	
	public String getText() {
		return editText.getText().toString().replaceAll(PHONE_NUM_SPLIT, "");
	}
}
