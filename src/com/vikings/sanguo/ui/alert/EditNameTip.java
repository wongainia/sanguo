package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class EditNameTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_edit_name;
	private EditText newName;

	public EditNameTip() {
		super("修改昵称", DEFAULT);
		newName = (EditText) content.findViewById(R.id.newName);
		setButton(FIRST_BTN, "确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (StringUtil.isNull(newName.getText().toString())
						|| Constants.NAME_EDIT_HINT.equals(newName.getText()
								.toString().trim())) {
					controller.alert("新昵称不能为空");
					return;
				}
				if (newName.getText().toString().trim().length() > Constants.NAME_MAX_LENGTH) {
					controller.alert(Constants.NAME_EDIT_HINT);
					return;
				}

				int costCurrency = CacheMgr.dictCache.getDictInt(
						Dict.TYPE_CHANGE_NAME_COST, (byte) 1);
				if (Account.user.getCurrency() < costCurrency) {
					dismiss();
					new ToActionTip(costCurrency).show();
					return;
				}

				new SaveNameInvoker(newName.getText().toString().trim())
						.start();
			}
		});
		setButton(SECOND_BTN, "放弃", closeL);
	}

	private void setValue() {
		ViewUtil.setText(content, R.id.name, Account.user.getNick());
		ViewUtil.setRichText(
				content,
				R.id.cost,
				"修改昵称需要花费#rmb#"
						+ CacheMgr.dictCache.getDict(
								Dict.TYPE_CHANGE_NAME_COST, (byte) 1), true);
	}

	public void show() {
		setValue();
		super.show();
	}

	private class SaveNameInvoker extends BaseInvoker {

		private UserAccountClient tmp;

		private ReturnInfoClient ri;

		private String newName;

		public SaveNameInvoker(String newName) {
			this.newName = newName;
		}

		@Override
		protected void beforeFire() {
			tmp = new UserAccountClient(Account.user.getId(),
					Account.user.getPsw());
			fillUser(tmp, newName);
			super.beforeFire();
		}

		@Override
		protected String failMsg() {
			return "修改姓名失败";
		}

		@Override
		protected void fire() throws GameException {
			ri = GameBiz.getInstance().playerUpdate(tmp);
		}

		@Override
		protected String loadingMsg() {
			return "修改姓名…";
		}

		@Override
		protected void onOK() {
			Account.user.setNick(newName);
			controller.setAccountBarUser(Account.user);
			controller.updateUI(ri, true);
			dismiss();
		}
	}

	private void fillUser(UserAccountClient user, String newName) {
		user.setId(Account.user.getId());
		user.setImage(Account.user.getImage());
		user.setNick(newName);
		user.setSex(Account.user.getSex());
		user.setBirthday(Account.user.getBirthday());
		user.setProvince(Account.user.getProvince());
		user.setCity(Account.user.getCity());
		user.setDesc(Account.user.getDesc());
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}
}
