package com.vikings.sanguo.ui.alert;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.GuildInviteInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class GuildInviteInfoDeleteConfirm extends CustomConfirmDialog {

	private TextView view;
	private GuildInviteInfoClient info;
	private RichGuildInfoClient rgic;

	public GuildInviteInfoDeleteConfirm(RichGuildInfoClient rgic,
			GuildInviteInfoClient info) {
		super("取消邀请", CustomConfirmDialog.DEFAULT);
		this.info = info;
		this.rgic = rgic;
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		setButton(FIRST_BTN, "确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new GuildInviteRemoveInvoker(info).start();
			}
		});

		setButton(SECOND_BTN, "取消", closeL);

		ViewUtil.setRichText(
				view,
				"你确定对"
						+ StringUtil.color(info.getBriefUser()
								.getHtmlNickName(), R.color.k7_color9)
						+ "取消邀请么");
	}

	@Override
	protected View getContent() {
		view = new TextView(controller.getUIContext());
		view.setTextSize(13);
		view.setTextColor(controller.getResources().getColor(R.color.color7));
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int) (15 * Config.SCALE_FROM_HIGH), 0, 0);
		view.setGravity(Gravity.CENTER);
		contentLayout.addView(view, params);
		return null;
	}

	private class GuildInviteRemoveInvoker extends BaseInvoker {

		private GuildInviteInfoClient info;

		public GuildInviteRemoveInvoker(GuildInviteInfoClient info) {
			this.info = info;
		}

		@Override
		protected String loadingMsg() {
			return "取消邀请";
		}

		@Override
		protected String failMsg() {
			return "取消邀请失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().guildInviteRemove(rgic.getGuildid(),
					info.getBriefUser().getId().intValue());
			Account.guildCache.updata(true);
			info.setApprove(true);
		}

		@Override
		protected void onOK() {
			dismiss();
			ctr.alert("你已取消邀请"
					+ StringUtil.color(info.getBriefUser().getHtmlNickName(),
							R.color.k7_color9) + "加入家族");
		}

		@Override
		protected void onFail(GameException exception) {
			dismiss();
			super.onFail(exception);
		}

	}

}
