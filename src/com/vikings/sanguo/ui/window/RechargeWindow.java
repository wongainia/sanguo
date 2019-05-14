package com.vikings.sanguo.ui.window;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.RechargeState;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.adapter.RechargeAdapter;
import com.vikings.sanguo.ui.alert.AlertDoubleRechargeTip;
import com.vikings.sanguo.ui.alert.RechargeOtherInputTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class RechargeWindow extends CustomPopupWindow implements
		OnClickListener {
	public static final byte TYPE_SMS_RECHARGE = 1;
	public static final byte TYPE_REWARD_RECHARGE = 2;
	public static final byte TYPE_ALL = 0;
	private ViewGroup userInfoLayout;
	private View iconLayout;
	private GridView gridView;
	private TextView desc, name, id;
	private Button changeUserBtn;

	private byte type = TYPE_ALL;
	private RechargeAdapter adapter;
	private BriefUserInfoClient briefUser;

	@Override
	protected void init() {
		super.init("充值中心");
		setContent(R.layout.recharge);
		desc = (TextView) window.findViewById(R.id.desc);
		gridView = (GridView) window.findViewById(R.id.gridView);
		userInfoLayout = (ViewGroup) window.findViewById(R.id.userInfoLayout);
		iconLayout = (ViewGroup) userInfoLayout.findViewById(R.id.iconLayout);
		name = (TextView) userInfoLayout.findViewById(R.id.name);
		id = (TextView) userInfoLayout.findViewById(R.id.id);
		changeUserBtn = (Button) userInfoLayout
				.findViewById(R.id.changeUserBtn);
		changeUserBtn.setOnClickListener(this);
		adapter = new RechargeAdapter(briefUser);
		List<RechargeState> list = getRechargeStates();

		if (type == TYPE_SMS_RECHARGE) {
			ViewUtil.setRichText(desc, CacheMgr.uiTextCache
					.getTxt(UITextProp.SM_CHARGE_REWARDS_DESC));
		} else if (type == TYPE_REWARD_RECHARGE) {
			ViewUtil.setRichText(desc, CacheMgr.uiTextCache
					.getTxt(UITextProp.REWARD_CHARGE_REWARDS_DESC));
		}

		adapter.addItems(list);
		gridView.setAdapter(adapter);

		setLeftBtn("充值记录", new OnClickListener() {

			@Override
			public void onClick(View v) {
				controller.openRechageLogWindow();

			}
		});
	}

	private List<RechargeState> getRechargeStates() {
		if (type == TYPE_ALL)
			return CacheMgr.rechargeStateCache.getAll();
		else
			return CacheMgr.rechargeStateCache.getRechargeStates(type);

	}

	public void open(byte type, BriefUserInfoClient briefUser) {
		this.type = type;
		this.briefUser = briefUser;
		this.doOpen();
		// 双倍优惠提示
		if (Account.user.hasDoubleRechrgePrivilege()
				&& briefUser.getId() == Account.user.getId()) {
			new AlertDoubleRechargeTip().show();
		}
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	public void changeUser(BriefUserInfoClient briefUser) {
		this.briefUser = briefUser;
		if (null != adapter)
			adapter.setBriefuser(briefUser);
		setValue();
	}

	private void setValue() {
		setRightTxt("#rmb#" + Account.user.getCurrency());
		if (briefUser.isSelf()) {
			ViewUtil.setVisible(desc);
			ViewUtil.setGone(userInfoLayout);
		} else {
			ViewUtil.setGone(desc);
			ViewUtil.setVisible(userInfoLayout);
			setUserInfo();
		}
	}

	private void setUserInfo() {
		IconUtil.setUserIcon(iconLayout, briefUser);
		ViewUtil.setText(name, briefUser.getNickName());
		ViewUtil.setText(id, "ID:" + briefUser.getId());
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	public void onClick(View v) {
		if (v == changeUserBtn) {
			new RechargeOtherInputTip(this).show();
		}
	}
}
