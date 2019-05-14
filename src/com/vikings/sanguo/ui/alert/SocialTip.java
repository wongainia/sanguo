package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.invoker.AddBlackListInvoker;
import com.vikings.sanguo.invoker.AddFriendInvoker;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class SocialTip extends CustomConfirmDialog implements OnClickListener {
	private OtherUserClient ouc;
	private Button addBlackBtn, deleteBlackBtn, addFriendBtn, deleteFriendBtn,
			sendMsgBtn, closeBtn;

	public SocialTip(OtherUserClient otherUserClient) {
		super("请选择操作", DEFAULT);
		this.ouc = otherUserClient;
		addBlackBtn = (Button) content.findViewById(R.id.addBlackBtn);
		addBlackBtn.setOnClickListener(this);

		deleteBlackBtn = (Button) content.findViewById(R.id.deleteBlackBtn);
		deleteBlackBtn.setOnClickListener(this);

		addFriendBtn = (Button) content.findViewById(R.id.addFriendBtn);
		addFriendBtn.setOnClickListener(this);

		deleteFriendBtn = (Button) content.findViewById(R.id.deleteFriendBtn);
		deleteFriendBtn.setOnClickListener(this);

		sendMsgBtn = (Button) content.findViewById(R.id.sendMsgBtn);
		sendMsgBtn.setOnClickListener(this);

		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);

		if (Account.user.hasCountry()
				&& Account.user.getCountry().intValue() == otherUserClient
						.getCountry()) { // 同国添加聊天按钮
			ViewUtil.setVisible(sendMsgBtn);
		} else {
			ViewUtil.setGone(sendMsgBtn);
		}

		if (Account.isFriend(ouc.bref())) {
			ViewUtil.setGone(addFriendBtn);
			ViewUtil.setVisible(deleteFriendBtn);
		} else {
			ViewUtil.setVisible(addFriendBtn);
			ViewUtil.setGone(deleteFriendBtn);
		}

		if (Account.isBlackList(ouc.bref())) {
			ViewUtil.setGone(addBlackBtn);
			ViewUtil.setVisible(deleteBlackBtn);
		} else {
			ViewUtil.setVisible(addBlackBtn);
			ViewUtil.setGone(deleteBlackBtn);
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_social, contentLayout, false);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == addBlackBtn) {
			if (Account.isBlackList(ouc.bref())) {
				controller.alert("对方已经是你的仇人了");
				return;
			}

			new AddBlackListInvoker(ouc.bref()).start();
		} else if (v == addFriendBtn) {
			if (Account.isFriend(ouc.bref())) {
				controller.alert("对方已经是你的好友了");
				return;
			}
			if (ouc.getCountry() != Account.user.getCountry().intValue()) {
				controller.alert("对方和你分属不同国家，不能添加好友");
				return;
			}
			new AddFriendInvoker(ouc.bref(), null).start();
		} else if (v == sendMsgBtn) {
			controller.openChatWindow(ouc.bref());
		} else if (v == deleteBlackBtn) {
			new DeleteBlackListConfirmTip(ouc.bref()).show();
		} else if (v == deleteFriendBtn) {
			new AddFriendInvoker(ouc.bref(), null).start();
		}
	}

}
