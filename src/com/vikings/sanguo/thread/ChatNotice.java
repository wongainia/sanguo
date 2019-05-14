package com.vikings.sanguo.thread;

import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildChatData;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.window.ChatWindow;
import com.vikings.sanguo.ui.window.GuildChatWindow;
import com.vikings.sanguo.ui.window.PopupUI;

public class ChatNotice implements Runnable {

	private List<BriefUserInfoClient> userList;
	private GuildChatData chatData;

	public ChatNotice(List<BriefUserInfoClient> userList) {
		this.userList = userList;
	}

	public ChatNotice(GuildChatData chatData) {
		this.chatData = chatData;
	}

	@Override
	public void run() {
		if (null != userList && !userList.isEmpty()) {
			BriefUserInfoClient u = userList.get(userList.size() - 1);
			SoundMgr.play(R.raw.sfx_chat);
			if (u != null) {
				PopupUI popupUI = Config.getController().getCurPopupUI();
				if (popupUI != null
						&& popupUI instanceof ChatWindow
						&& ((ChatWindow) popupUI).getUser().getId().intValue() == u
								.getId().intValue()) {

				} else {
					Config.getController().getNotify().startNotify(u);
				}
			}
		} else if (null != chatData) {
			SoundMgr.play(R.raw.sfx_chat);
			if (chatData.isGuildChatData()) {
				PopupUI popupUI = Config.getController().getCurPopupUI();
				if (popupUI != null
						&& popupUI instanceof GuildChatWindow
						&& ((GuildChatWindow) popupUI).getType() == Constants.MSG_GUILD) {

				} else {
					Config.getController().getNotify().startNotify(chatData);
				}
			}
		}
		// Config.getController().addChatUser(userList);
		// 刷新chatWindow中未读消息数量，必须在addChatUser后使用，保证所有聊天对象头像都可刷新
		// Config.getController().refreshChatWindow();
	}
}
