package com.vikings.sanguo.ui.alert;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.GuildBuildResp;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.ui.window.GuildInfoWindow;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class GuildBuildTip extends CustomConfirmDialog implements
		OnClickListener {
	private static final int layout = R.layout.alert_guild_build;
	private EditText name;
	private ViewGroup materialLayout;
	private GuildProp guildProp;

	public GuildBuildTip() {
		super("创建家族", CustomConfirmDialog.DEFAULT);
		setButton(FIRST_BTN, "确定", this);
		setButton(SECOND_BTN, "取消", closeL);
		name = (EditText) content.findViewById(R.id.name);
		materialLayout = (ViewGroup) content.findViewById(R.id.materialLayout);
	}

	public void show() {
		try {
			guildProp = (GuildProp) (CacheMgr.guildPropCache.get(1));
		} catch (GameException e) {
			Log.e("GuildBuildTip", e.getMessage());
			return;
		}
		setValue();
		super.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	@Override
	public void onClick(View v) {
		String nameStr = name.getText().toString();
		if (StringUtil.isNull(nameStr)) {
			controller.alert("请输入家族名称");
			return;
		}

		if (nameStr.length() > Constants.NAME_MAX_LENGTH) {
			controller.alert("家族名称不能超过" + Constants.NAME_MAX_LENGTH + "个字");
			return;
		}

		if (null != guildProp) {
			if (Account.user.getMoney() < guildProp.getMoney4LvUp()) {
				controller.alert("金钱不足");
				return;
			}

		}
		new GuildBuildInvoker(nameStr).start();
	}

	private void setValue() {

		if (guildProp.getMoney4LvUp() > 0) {
			ViewUtil.setVisible(materialLayout);
			View view = controller.inflate(R.layout.common_item);
			ViewUtil.setRichText(
					view,
					R.id.name,
					"#money#"
							+ StringUtil.color("金钱:", controller
									.getResourceColorText(R.color.color5)));
			if (Account.user.getMoney() < guildProp.getMoney4LvUp()) {
				ViewUtil.setRichText(
						view.findViewById(R.id.value),
						StringUtil.color(guildProp.getMoney4LvUp() + "(",
								controller.getResourceColorText(R.color.color5))
								+ StringUtil.color(
										String.valueOf(Account.user.getMoney()),
										controller
												.getResourceColorText(R.color.k7_color5))
								+ StringUtil.color(")", controller
										.getResourceColorText(R.color.color5)));
			} else {
				ViewUtil.setRichText(
						view.findViewById(R.id.value),
						StringUtil.color(guildProp.getMoney4LvUp() + "("
								+ Account.user.getMoney() + ")",
								controller.getResourceColorText(R.color.color5)));
			}

			materialLayout.addView(view);

		} else {
			ViewUtil.setGone(materialLayout);
		}
	}

	private class GuildBuildInvoker extends BaseInvoker {

		private String name;

		private GuildBuildResp resp;

		public GuildBuildInvoker(String name) {
			this.name = name;
		}

		@Override
		protected String loadingMsg() {
			return "创建家族…";
		}

		@Override
		protected String failMsg() {
			return "创建家族失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().guildBuild(name, 1);
			if (null != Account.guildCache) {
				Account.guildCache.setGuildid(resp.getGuildid());
				Account.guildCache.updata(true);
			}
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRi(), true);
			dismiss();
			ctr.goBack();
			if (null != ctr.getHeartBeat())
				ctr.getHeartBeat().updataChatIds();
			new GuildInfoWindow().open(Account.guildCache.getGuildid());
			controller
					.alert("创建家族成功！",
							"您创建了 " + StringUtil.color(name, R.color.k7_color5)
									+ " 家族", "您可以在成员列表中邀请好友加入家族", null, true);
		}

	}
}
