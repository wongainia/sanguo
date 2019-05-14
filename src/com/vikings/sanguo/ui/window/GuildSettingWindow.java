package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.GuildInfoUpdateResp;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class GuildSettingWindow extends CustomPopupWindow implements
		OnClickListener {

	private View joinOpen;
	private boolean joinOpenValue = true;
	private RichGuildInfoClient rgic;

	@Override
	protected void init() {
		super.init("游戏设置");
		setContent(R.layout.guild_setting);
		setContentBelowTitle(R.layout.list_title);
		ViewUtil.setText(window.findViewById(R.id.listTitle), "设置加入本家族的条件");
		joinOpen = window.findViewById(R.id.joinOpen);
		joinOpen.setOnClickListener(this);
		switchState(joinOpenValue, joinOpen);
		setBottomButton("保存并返回", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SaveInvoker().start();
			}
		});
	}

	@Override
	protected void destory() {
		super.destory();
	}

	private void switchState(boolean value, View v) {
		if (value)
			ViewUtil.setVisible(v, R.id.set);
		else
			ViewUtil.setGone(v, R.id.set);
	}

	@Override
	public void onClick(View v) {
		if (v == joinOpen) {
			joinOpenValue = !joinOpenValue;
			switchState(joinOpenValue, joinOpen);
		}
	}

	public void open(RichGuildInfoClient rgic) {
		if (rgic == null)
			return;
		this.rgic = rgic;
		this.joinOpenValue = rgic.getGic().isAutoJoin();
		doOpen();
	}

	private class SaveInvoker extends BaseInvoker {
		private GuildInfoUpdateResp resp;

		@Override
		protected String loadingMsg() {
			return "保存中";
		}

		@Override
		protected String failMsg() {
			return "保存失败";
		}

		@Override
		protected void fire() throws GameException {
			if (rgic.getGic().isAutoJoin() != joinOpenValue) {
				resp = GameBiz.getInstance().guildInfoUpdate(rgic.getGuildid(),
						rgic.getGic().getDesc(), rgic.getGic().getImage(),
						rgic.getGic().getAnnouncement(), joinOpenValue);
				rgic.setGic(resp.getGic());
			}
		}

		@Override
		protected void onOK() {
			if (null != resp) {
				ctr.updateUI(resp.getRi(), true);
			}
			ctr.goBack();
			ctr.alert("设置成功");
		}

	}
}
