package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.GuildInfoUpdateResp;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.ui.alert.GuildIconPickTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;
import com.vikings.sanguo.widget.GuildDetailTopInfo;

public class GuildEditWindow extends CustomPopupWindow implements
		OnClickListener {

	private EditText desc, announcement;
	private ImageView icon;

	private RichGuildInfoClient rgic;
	private GuildProp guildProp;

	@Override
	protected void init() {
		super.init("家族信息");
		setContent(R.layout.guild_edit);

		desc = (EditText) window.findViewById(R.id.desc);
		announcement = (EditText) window.findViewById(R.id.announcement);

		icon = (ImageView) window.findViewById(R.id.icon);
		if (rgic.isLeader(Account.user.getId())) {
			icon.setOnClickListener(this);
			desc.setEnabled(true);
			announcement.setEnabled(true);
		} else if (rgic.isElder(Account.user.getId())) {
			icon.setOnClickListener(null);
			desc.setEnabled(false);
			announcement.setEnabled(true);
		} else {
			desc.setEnabled(false);
			announcement.setEnabled(false);
			icon.setOnClickListener(null);
		}

		setBottomButton("保存修改", this);

		try {
			guildProp = (GuildProp) CacheMgr.guildPropCache.get(rgic.getGic()
					.getLevel());
		} catch (GameException e) {
			e.printStackTrace();
		}
	}

	private void setValue() {
		GuildDetailTopInfo.updateOwner(rgic, Account.myGuildLeader, guildProp,
				(ViewGroup) window.findViewById(R.id.guildDetailTopLayout));

		if (StringUtil.isNull(rgic.getGic().getDesc())) {
			ViewUtil.setText(desc, "");
		} else {
			ViewUtil.setText(desc, rgic.getGic().getDesc());
		}

		if (StringUtil.isNull(rgic.getGic().getAnnouncement())) {
			ViewUtil.setText(announcement, "");
		} else {
			ViewUtil.setText(announcement, rgic.getGic().getAnnouncement());
		}
	}

	public void open(RichGuildInfoClient rgic) {
		this.rgic = rgic;
		doOpen();
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	@Override
	public void onClick(View v) {
		if (v == icon) {
			new GuildIconPickTip(rgic).show();
		} else {
			String descStr = desc.getText().toString().trim();
			String announcementStr = announcement.getText().toString().trim();
			new SaveInvoker(descStr, announcementStr).start();
		}
	}

	private class SaveInvoker extends BaseInvoker {
		private String descStr;
		private String announcementStr;
		private GuildInfoUpdateResp resp;

		public SaveInvoker(String descStr, String announcementStr) {
			this.descStr = descStr;
			this.announcementStr = announcementStr;
		}

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
			resp = GameBiz.getInstance().guildInfoUpdate(rgic.getGuildid(),
					descStr, rgic.getGic().getImage(), announcementStr,
					rgic.getGic().isAutoJoin());
			rgic.setGic(resp.getGic());
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRi(), true);
			ctr.goBack();
			ctr.alert("修改家族信息成功");
		}

	}
}
