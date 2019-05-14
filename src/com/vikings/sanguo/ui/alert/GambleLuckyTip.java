/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-8-10
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.invoker.AddFriendInvoker;
import com.vikings.sanguo.model.MachinePlayStatInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class GambleLuckyTip extends CustomConfirmDialog {

	public GambleLuckyTip(final MachinePlayStatInfoClient statInfo) {
		super(MEDIUM);
		setTitle("幸运玩家");

		ViewUtil.setRichText(content, R.id.desc, statInfo.getLuckyTitle());
		ArrayList<ShowItem> ls = statInfo.getRic().showReturn(true);
		ViewGroup vg = (ViewGroup) content.findViewById(R.id.content);
		for (ShowItem it : ls)
			vg.addView(ViewUtil.getShowItemView(it));

		if (Account.user.getId() != statInfo.getUser().getId()) {
			setButton(CustomConfirmDialog.FIRST_BTN, "加为好友",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							dismiss();
							new AddFriendInvoker(statInfo.getUser(), null)
									.start();
						}
					});
		}

		setButton(CustomConfirmDialog.SECOND_BTN, "查看主城",
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
						controller.showCastle(statInfo.getUser());
					}
				});

		setButton(CustomConfirmDialog.THIRD_BTN, "关        闭", closeL);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.gamble_lucky_tip);
	}
}
