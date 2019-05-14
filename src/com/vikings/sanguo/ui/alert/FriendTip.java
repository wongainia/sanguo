package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Country;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class FriendTip extends CustomConfirmDialog implements OnClickListener {

	private static final int layout = R.layout.alert_friend;

	private GuildUserData data;
	private BriefUserInfoClient briefUser;
	private ImageView vipIcon;
	private TextView vipLevel;
	private ViewGroup icon;
	private Button sendMsgBtn, castleBtn, deleteBtn, blackBtn, closeBtn;

	public FriendTip(GuildUserData data) {
		super("选择操作");
		this.data = data;
		this.briefUser = data.getUser();
		icon = (ViewGroup) content.findViewById(R.id.iconLayout);
		vipIcon = (ImageView) content.findViewById(R.id.vipIcon);
		vipLevel = (TextView) content.findViewById(R.id.vipLevel);
		sendMsgBtn = (Button) content.findViewById(R.id.sendMsgBtn);
		sendMsgBtn.setOnClickListener(this);
		castleBtn = (Button) content.findViewById(R.id.castleBtn);
		castleBtn.setOnClickListener(this);
		deleteBtn = (Button) content.findViewById(R.id.deleteBtn);
		deleteBtn.setOnClickListener(this);
		blackBtn = (Button) content.findViewById(R.id.blackBtn);
		blackBtn.setOnClickListener(this);
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(this);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		if (briefUser.isVip()) {
			IconUtil.setUserIcon(icon, briefUser, "VIP"
					+ briefUser.getCurVip().getLevel());
		} else {
			IconUtil.setUserIcon(icon, briefUser);
		}

		ViewUtil.setText(content, R.id.name, briefUser.getNickName());
		ViewUtil.setText(content, R.id.level, "等级: " + briefUser.getLevel()
				+ "级");
		if (null != data.getBgic())
			ViewUtil.setText(content, R.id.guild, "家族: "
					+ data.getBgic().getName());
		else
			ViewUtil.setText(content, R.id.guild, "家族: 无");
		Country country = CacheMgr.countryCache.getCountry(briefUser
				.getCountry());
		if (null != country)
			ViewUtil.setText(content, R.id.country, "国家: " + country.getName());
		else
			ViewUtil.setText(content, R.id.country, "国家: 无");

		ViewUtil.setText(content, R.id.userid, "ID: " + briefUser.getId());

		if (briefUser.getCurVip().getLevel() >= 1) {
			ImageUtil.setBgNormal(vipIcon);
			ViewUtil.setRichText(vipLevel,
					StringUtil.vipNumImgStr(briefUser.getCurVip().getLevel()));
		} else {
			ViewUtil.setImage(vipLevel, R.drawable.vip_0);
			ImageUtil.setBgGray(vipLevel);
			ImageUtil.setBgGray(vipIcon);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == sendMsgBtn) {
			dismiss();
			controller.openChatWindow(briefUser);
		} else if (v == castleBtn) {
			dismiss();
			controller.showCastle(briefUser);
		} else if (v == deleteBtn) {
			new DeleteFriendInvoker(briefUser).start();
		} else if (v == blackBtn) {
			dismiss();
			new AddBlackListConfirmTip(data).show();
		} else if (v == closeBtn) {
			dismiss();
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

	private class DeleteFriendInvoker extends BaseInvoker {

		protected BriefUserInfoClient user;

		public DeleteFriendInvoker(BriefUserInfoClient user) {
			this.user = user;
		}

		@Override
		protected String failMsg() {
			return StringUtil.repParams(
					ctr.getString(R.string.DeleteFriendInvoker_failMsg),
					user.getNickName());
		}

		@Override
		protected void fire() throws GameException {
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(user.getId());
			GameBiz.getInstance().deleteFriend(ids);
			Account.deleteFriend(user);
		}

		@Override
		protected String loadingMsg() {
			return StringUtil.repParams(
					ctr.getString(R.string.DeleteFriendInvoker_loadingMsg),
					user.getNickName());
		}

		@Override
		protected void onOK() {
			dismiss();
			ctr.alert("删除成功",
					StringUtil.color(user.getHtmlNickName(), R.color.k7_color5)
							+ "已不再是你的好友", null, true);
		}
	}
}
