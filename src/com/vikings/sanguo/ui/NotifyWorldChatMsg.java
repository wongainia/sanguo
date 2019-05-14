package com.vikings.sanguo.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.GuildChatData;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class NotifyWorldChatMsg extends BaseUI {

	private ViewGroup content; // 世界聊天
	private TextView worldMsg;

	private GuildChatData data;

	private int time = 5000;// 毫秒

	public NotifyWorldChatMsg() {
		content = (ViewGroup) controller.findViewById(R.id.worldMsgLayout);
		content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GuildChatData guildChatData = (GuildChatData) v.getTag();
				if (guildChatData.isCountryChatData()) {
					controller.openGroupChatWindow(Constants.MSG_COUNTRY);
				} else {
					controller.openGroupChatWindow(Constants.MSG_WORLD);
				}
			}
		});
		worldMsg = (TextView) content.findViewById(R.id.worldMsg);
		time = CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 35);
	}

	public void addMsg(GuildChatData gcdata) {
		if (this.data != null)
			return;
		this.data = gcdata;
		content.post(new Runnable() {

			@Override
			public void run() {
				show(data);
			}
		});
	}

	synchronized private void show(GuildChatData gcData) {
		ViewUtil.setVisible(content);
		content.setTag(gcData);
		BriefUserInfoClient briefUser = gcData.getUser();
		String str = briefUser.getNickName() + ":" + gcData.getMsg() + "  ";

		UserVip vipCfg = briefUser.getCurVip();
		if (null != vipCfg && vipCfg.getLevel() > 0) {
			str = "#player_vip#" + StringUtil.vipNumImgStr(vipCfg.getLevel())
					+ str;
		}
		ViewUtil.setRichText(worldMsg, str, true);
		content.postDelayed(new Runnable() {

			@Override
			public void run() {
				ViewUtil.setGone(content);
				data = null;
			}
		}, time);
	}

	public void setMarginTop(int margin) {
		if (null != content) {
			LayoutParams params = (LayoutParams) content.getLayoutParams();
			params.topMargin = (int) (margin * Config.SCALE_FROM_HIGH);
			content.setLayoutParams(params);
		}
	}
}
