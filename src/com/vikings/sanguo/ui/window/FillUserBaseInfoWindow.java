package com.vikings.sanguo.ui.window;

import java.util.List;
import java.util.Random;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.UserNameRandomCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BackgroundInvoker;
import com.vikings.sanguo.invoker.LogInvoker;
import com.vikings.sanguo.invoker.RegisterInvoker;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.model.UserNameRandom;
import com.vikings.sanguo.sound.MediaPlayerMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.BigMsgAlert;
import com.vikings.sanguo.ui.alert.SystemNotifyTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class FillUserBaseInfoWindow extends PopupWindow implements
		OnClickListener {

	protected ViewGroup window;
	private TextView server, retrievepswd, backHome, agreeDesc;
	private View randomBtn, saveLayout, agree, agreeIcon;
	private EditText nickName;
	private int sex;

	@Override
	protected void init() {
		new LogInvoker("到注册界面").start();
		MediaPlayerMgr.getInstance().startSound(R.raw.game_start);
		window = (ViewGroup) controller.inflate(R.layout.fill_user_base_info);
		((ViewGroup) controller.findViewById(R.id.mainLayout)).addView(window);
		window.setPadding(0, 0, 0, 0);

		server = (TextView) window.findViewById(R.id.server);
		retrievepswd = (TextView) window.findViewById(R.id.retrievepswd);
		retrievepswd.setOnClickListener(this);
		backHome = (TextView) window.findViewById(R.id.backHome);
		backHome.setOnClickListener(this);
		agreeDesc = (TextView) window.findViewById(R.id.agreeDesc);
		agreeDesc.setOnClickListener(this);

		randomBtn = window.findViewById(R.id.randomBtn);
		randomBtn.setOnClickListener(this);
		saveLayout = window.findViewById(R.id.saveLayout);
		saveLayout.setOnClickListener(this);

		agree = window.findViewById(R.id.agree);
		agree.setOnClickListener(this);

		agreeIcon = window.findViewById(R.id.agreeIcon);

		nickName = (EditText) window.findViewById(R.id.nickName);
		nickName.setHint(Constants.NAME_EDIT_HINT);

		sex = Constants.FEMALE;
	}

	@Override
	public void onClick(View v) {
		if (v == saveLayout) {
			if (check()) {
				new RegisterInvoker(nickName.getText().toString().trim(), sex,
						new CallBack() {

							@Override
							public void onCall() {
								controller.goBack();
							}
						}).start();
			}
		} else if (v == agreeDesc) {
			// 打开用户协议
			new SystemNotifyTip(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
					(byte) 8), "网络服务使用协议").show();
		} else if (v == agree) {
			if (ViewUtil.isGone(agreeIcon)) {
				ViewUtil.setVisible(agreeIcon);
			} else {
				ViewUtil.setGone(agreeIcon);
			}
		} else if (v == retrievepswd) {
			controller.openRertievePwd(true, true);
		} else if (v == randomBtn) {
			setRandomValue(sex);
		} else if (v == backHome) {
			controller.reboot();
		}
	}

	private boolean check() {
		if (StringUtil.isNull(nickName.getText().toString())) {
			new BigMsgAlert().show("请输入昵称", true);
			return false;
		}

		if (nickName.getText().toString().length() > Constants.NAME_MAX_LENGTH) {
			controller.alert(Constants.NAME_EDIT_HINT);
			return false;
		}
		if (ViewUtil.isGone(agreeIcon)) {
			controller.alert("请阅读并同意《用户协议》");
			return false;
		}
		return true;
	}

	public void open() {
		controller.setBackBt(false);
		doOpen();
		new InitConfigInvoker().start();
	}

	private void initValue() {
		ViewUtil.setRichText(agreeDesc, "<u>已经阅读并同意用户协议</u>");

		ViewUtil.setRichText(retrievepswd, "<u>找回账号</u>");

		ViewUtil.setRichText(backHome, "<u>返回选服</u>");

		setRandomValue(sex);
		if (null != Config.serverData) {
			ServerData data = Config.serverData;
			ViewUtil.setText(server, data.getName());
		} else {
			ViewUtil.setText(server, "");
		}

	}

	@SuppressWarnings("unchecked")
	private void setRandomValue(int sex) {
		if (null != CacheMgr.userNameRandomCache) {
			List<UserNameRandom> list = CacheMgr.userNameRandomCache
					.search(sex);
			int num1 = getRandom(list.size());
			int num2 = getRandom(list.size());

			UserNameRandom random1 = list.get(num1);
			UserNameRandom random2 = list.get(num2);

			String name = random1.getFamilyName() + random2.getLastName();
			ViewUtil.setText(
					nickName,
					name.length() <= Constants.NAME_MAX_LENGTH ? name : name
							.substring(0, Constants.NAME_MAX_LENGTH));
		}
	}

	private int getRandom(int max) {
		return new Random().nextInt(max);
	}

	@Override
	public boolean goBack() {
		if (Account.user.getId() <= 0) {
			controller.quit();
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void destory() {
		MediaPlayerMgr.getInstance().stopSound();
		((ViewGroup) controller.findViewById(R.id.mainLayout))
				.removeView(window);
		CacheMgr.userNameRandomCache.clear();
		CacheMgr.userNameRandomCache = null;
	}

	private class InitConfigInvoker extends BackgroundInvoker {

		@Override
		protected void fire() throws GameException {
			if (null == CacheMgr.userNameRandomCache) {
				CacheMgr.userNameRandomCache = new UserNameRandomCache();
				CacheMgr.userNameRandomCache.init();
			}
		}

		@Override
		protected void onOK() {
			initValue();
		}
	}

	@Override
	protected View getPopupView() {
		return window;
	}

}
