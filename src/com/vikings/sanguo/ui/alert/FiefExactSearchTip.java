package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.utils.ViewUtil;

public class FiefExactSearchTip extends FiefSearchTypeTip implements
		OnClickListener {

	@Override
	public void onClick(View v) {
		if (v == type1Btn) {
			dismiss();
			new FiefUserListTip(FiefUserListTip.TYPE_BLACK_LIST).show();
		} else if (v == type2Btn) {
			dismiss();
			new FiefUserListTip(FiefUserListTip.TYPE_FRIEND_LIST).show();
		} else if (v == type3Btn) {
			dismiss();
			new FiefUserIdSearchTip().show();
		}
	}

	@Override
	protected void setClickListener() {
		type1Btn.setOnClickListener(this);
		type2Btn.setOnClickListener(this);
		type3Btn.setOnClickListener(this);
	}

	@Override
	protected void setBtnText() {
		ViewUtil.setText(type1Btn, "查找仇人");
		ViewUtil.setBold(type1Btn);
		ViewUtil.setText(type2Btn, "查找好友");
		ViewUtil.setBold(type2Btn);
		ViewUtil.setText(type3Btn, "玩家ID查找");
		ViewUtil.setBold(type3Btn);
	}
}
