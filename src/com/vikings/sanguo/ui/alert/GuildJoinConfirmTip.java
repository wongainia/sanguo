package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.model.GuildSearchInfoClient;
import com.vikings.sanguo.ui.adapter.GuildJoinAskInvoker;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class GuildJoinConfirmTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_guild_join_confirm;
	private GuildSearchInfoClient data;
	private GuildProp prop;

	public GuildJoinConfirmTip(GuildSearchInfoClient data) {
		super("申请确认", DEFAULT);
		this.data = data;
		prop = data.getGuildProp();
		setButton(FIRST_BTN, "确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				new GuildJoinAskInvoker(GuildJoinConfirmTip.this.data).start();
			}
		});
		setButton(SECOND_BTN, "关闭", closeL);
	}

	public void show() {
		setValue();// 设值
		super.show();
	}

	private void setValue() {
		if (data.getBriefUser().isVip()) {
			IconUtil.setUserIcon(
					(ViewGroup) content.findViewById(R.id.iconLayout),
					data.getBriefUser(), "VIP"
							+ data.getBriefUser().getCurVip().getLevel());
		} else {
			IconUtil.setUserIcon(
					(ViewGroup) content.findViewById(R.id.iconLayout),
					data.getBriefUser());
		}

		ViewUtil.setText(content, R.id.name, data.getBriefUser().getNickName()
				+ "(ID:" + data.getBriefUser().getId().intValue() + ")");
		ViewUtil.setText(content, R.id.level, "等级:"
				+ data.getBriefUser().getLevel().intValue() + "级");
		ViewUtil.setText((TextView) content.findViewById(R.id.count),
				"族员:" + data.getInfo().getMembers()
						+ (null != prop ? "/" + prop.getMaxMemberCnt() : ""));

		int cost = CacheMgr.dictCache.getDictInt(Dict.TYPE_GUILD, 2);
		if (cost > 0)
			ViewUtil.setRichText(tip.findViewById(R.id.desc), "申请需要花费#rmb#"
					+ cost + "元宝", true);
		else
			ViewUtil.setText(tip.findViewById(R.id.desc), "");
	}

	@Override
	public View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
