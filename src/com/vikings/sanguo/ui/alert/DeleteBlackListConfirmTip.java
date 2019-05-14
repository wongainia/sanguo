package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class DeleteBlackListConfirmTip extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_delete_black_list_confirm;

	private BriefUserInfoClient briefUser;

	public DeleteBlackListConfirmTip(BriefUserInfoClient briefUser) {
		super("移出仇人录", CustomConfirmDialog.DEFAULT);
		this.briefUser = briefUser;
		ViewUtil.setText(content, R.id.desc, "是否将 " + briefUser.getNickName()
				+ "移出仇人录?");
		setButton(FIRST_BTN, "确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DeleteBlackListInvoker(
						DeleteBlackListConfirmTip.this.briefUser).start();
			}
		});
		setButton(SECOND_BTN, "取消", closeL);
	}

	public void show() {
		super.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	private class DeleteBlackListInvoker extends BaseInvoker {

		protected BriefUserInfoClient user;

		public DeleteBlackListInvoker(BriefUserInfoClient user) {
			this.user = user;
		}

		@Override
		protected String failMsg() {
			return StringUtil.repParams(
					ctr.getString(R.string.DeleteBlackListInvoker_failMsg),
					user.getNickName());
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().blackListDel(user.getId());
		}

		@Override
		protected String loadingMsg() {
			return StringUtil.repParams(
					ctr.getString(R.string.DeleteBlackListInvoker_loadingMsg),
					user.getNickName());
		}

		@Override
		protected void onOK() {
			Account.deleteBlackList(user);
			dismiss();
			ctr.alert("你已将 "
					+ StringUtil.color(user.getHtmlNickName(),
							R.color.k7_color5) + " 移出仇人录");
		}
	}
}
