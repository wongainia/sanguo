package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class AddBlackListTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_add_black_list;

	public AddBlackListTip() {
		super("添加仇人", CustomConfirmDialog.DEFAULT);
		setButton(FIRST_BTN, "确定", this);
		setButton(SECOND_BTN, "取消", closeL);
	}

	public void show() {
		super.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

	@Override
	public void onClick(View v) {
		String text = ViewUtil.getText(content, R.id.userID);
		if (StringUtil.isNull(text) || !StringUtil.isNumeric(text)) {
			controller.alert("请输入正确的ID");
			return;
		}
		int id = 0;
		try {
			id = Integer.valueOf(text);
		} catch (Exception e) {
			id = Integer.MAX_VALUE - 1;
		}
		
		if (BriefUserInfoClient.isNPC(id)) {
			controller.alert("你不能将NPC拉黑");
			return;
		}
		
		if (Account.user.getId() == id) {
			controller.alert("不能加自己为仇人!");
			return;
		}
		
		new UserInvoker(id).start();

	}

	private class UserInvoker extends BaseInvoker {
		private int id;
		private GuildUserData data = new GuildUserData();

		public UserInvoker(int id) {
			this.id = id;
		}

		@Override
		protected String loadingMsg() {
			return "查询玩家";
		}

		@Override
		protected String failMsg() {
			return "查询玩家失败";
		}

		@Override
		protected void onFail(GameException exception) {
			controller.alert(StringUtil.color("没有找到ID对应玩家，请重新输入",
					ctr.getResourceColorText(R.color.k7_color5)));
		}

		@Override
		protected void fire() throws GameException {
			BriefUserInfoClient briefUser = CacheMgr.userCache.get(id);
			data.setUser(briefUser);
			if (briefUser.hasGuild()) {
				BriefGuildInfoClient bgic = CacheMgr.bgicCache.get(briefUser
						.getGuildid().intValue());
				data.setBgic(bgic);
			}

			if (briefUser.hasCountry())
				data.setCountry(CacheMgr.countryCache.getCountry(briefUser
						.getCountry().intValue()));

		}

		@Override
		protected void onOK() {
			dismiss();
			new AddBlackListConfirmTip(data).show();
		}

	}
}
