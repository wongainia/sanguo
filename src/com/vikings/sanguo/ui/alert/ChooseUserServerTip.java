package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ChooseUserServerTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_choose_user_server;

	private ViewGroup userLayout;
	private ServerData data;
	private CallBack callBack;

	public ChooseUserServerTip() {
		super("选择角色", DEFAULT);
		userLayout = (ViewGroup) content.findViewById(R.id.userLayout);
		setCloseBtn();
	}

	public void show(ServerData data, List<AccountPswInfoClient> clients,
			CallBack callBack) {
		this.data = data;
		this.callBack = callBack;
		setValue(clients);
		super.show();
	}

	private void setValue(List<AccountPswInfoClient> clients) {
		ViewUtil.setRichText(content, R.id.gradientMsg,
				StringUtil.color(data.getName(), R.color.color6)
						+ "有多个角色，请选择角色进入游戏");
		for (AccountPswInfoClient client : clients) {
			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.choose_user_server_item);
			userLayout.addView(viewGroup);
			ViewUtil.setText(viewGroup, R.id.name, client.getNick());
			ViewUtil.setText(viewGroup, R.id.level, "LV" + client.getLevel());
			viewGroup.setOnClickListener(new ChooseListener(client));
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	private class ChooseListener implements OnClickListener {
		private AccountPswInfoClient client;

		public ChooseListener(AccountPswInfoClient client) {
			this.client = client;
		}

		@Override
		public void onClick(View v) {
			dismiss();
			Config.setServer(data, client);
			if (null != callBack)
				callBack.onCall();
		}

	}

}
