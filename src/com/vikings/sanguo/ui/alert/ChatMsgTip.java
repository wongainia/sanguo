package com.vikings.sanguo.ui.alert;

import android.content.Context;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.model.GuildChatData;
import com.vikings.sanguo.model.MessageInfoClient;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ChatMsgTip extends CustomConfirmDialog {
	public ChatMsgTip(final View v) {
		super("选择操作", DEFAULT);
		setButton(FIRST_BTN, "复制", new OnClickListener() {

			@Override
			public void onClick(View view) {
				Object o = v.getTag();
				if (o == null)
					return;
				ClipboardManager clip = (ClipboardManager) controller
						.getSystemService(Context.CLIPBOARD_SERVICE);
				if (o instanceof MessageInfoClient) {
					MessageInfoClient msgInfo = (MessageInfoClient) o;
					clip.setText(msgInfo.getContext());
				} else if (o instanceof GuildChatData) {
					GuildChatData chatData = (GuildChatData) o;
					clip.setText(chatData.getMsg());
				}
				dismiss();
			}
		});
		setButton(SECOND_BTN, "关闭", closeL);
	}

	public void show() {
		super.show();
	}

	@Override
	protected View getContent() {
		return null;
	}
}
