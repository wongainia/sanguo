package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.ui.window.RechargeCenterWindow;
import com.vikings.sanguo.ui.window.RechargeWindow;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class RechargeOtherInputTip extends CustomConfirmDialog {
	private EditText number;
	private Object window;

	public RechargeOtherInputTip(Object window) {
		super("输入对方ID", CustomConfirmDialog.DEFAULT);
		this.window = window;
		number = (EditText) content.findViewById(R.id.number);
		number.setText("");
		setButton(FIRST_BTN, "确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				String str = number.getText().toString().trim();
				if (0 == str.length())
					controller.alert("请输入有效ID");
				else {
					int id = Integer.valueOf(str);
					if (id < 100000)
						controller.alert("请输入有效ID");
					else {
						new QueryOtherUserInvoker(id).start();
					}
				}
			}
		});

		setButton(SECOND_BTN, "取消", closeL);
	}

	private class QueryOtherUserInvoker extends BaseInvoker {
		private int id;
		private BriefUserInfoClient user;

		public QueryOtherUserInvoker(int id) {
			this.id = id;
		}

		@Override
		protected String failMsg() {
			return "查看资料错误";
		}

		@Override
		protected void fire() throws GameException {
			user = CacheMgr.userCache.getNew(id);
		}

		@Override
		protected String loadingMsg() {
			return "查询用户信息";
		}

		@Override
		protected void onOK() {
			dismiss();
			if (window instanceof RechargeWindow) {
				((RechargeWindow) window).changeUser(user);
			} else if (window instanceof RechargeCenterWindow) {
				((RechargeCenterWindow) window).changeUser(user);
			}

		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_recharge_other, contentLayout,
				false);
	}
}
