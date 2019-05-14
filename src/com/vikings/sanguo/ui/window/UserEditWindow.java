package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.alert.EditNameTip;
import com.vikings.sanguo.ui.pick.DatePick;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class UserEditWindow extends CustomPopupWindow implements
		OnClickListener {

	private ViewGroup iconLayout, femaleLayout, maleLayout;
	private TextView rename, age;

	private DatePick birthdayPick;

	private TextView guildName;
	private int sex;

	@Override
	protected void init() {
		super.init("修改资料");
		setContent(R.layout.user_info_edit);
		iconLayout = (ViewGroup) window.findViewById(R.id.iconLayout);
		iconLayout.setOnClickListener(this);
		femaleLayout = (ViewGroup) window.findViewById(R.id.femaleLayout);
		femaleLayout.setOnClickListener(this);
		maleLayout = (ViewGroup) window.findViewById(R.id.maleLayout);
		maleLayout.setOnClickListener(this);
		rename = (TextView) window.findViewById(R.id.rename);
		rename.setOnClickListener(this);
		guildName = (TextView) window.findViewById(R.id.guild);
		age = (TextView) window.findViewById(R.id.age);
		initPicker();
		setBottomButton("保存修改", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SaveInvoker().start();

			}
		});
		setValue();
	}

	@Override
	public void showUI() {
		ViewUtil.setText(window, R.id.name, Account.user.getNick());
		new UserIconCallBack(Account.user.bref(),
				window.findViewById(R.id.icon), Constants.ICON_WIDTH
						* Config.SCALE_FROM_HIGH, Constants.ICON_HEIGHT
						* Config.SCALE_FROM_HIGH);
		super.showUI();
	}

	private void initPicker() {
		birthdayPick = new DatePick(
				(TextView) window.findViewById(R.id.birthday), age, "出生日期");
	}

	public void show() {
		sex = Account.user.getSex();
		doOpen();
	}

	private void setValue() {
		ViewUtil.setUserDetail(Account.user, window);

		if (Account.user.hasGuild()
				&& null != Account.guildCache.getRichInfoInCache()) {
			ViewUtil.setRichText(guildName, "<U>"
					+ Account.guildCache.getRichInfoInCache().getGic()
							.getName() + "</U>");
			guildName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new GuildInfoWindow().open(Account.guildCache.getGuildid());

				}
			});
		} else {
			ViewUtil.setText(window, R.id.guild, "无");
		}

		if (sex == Constants.MALE) {
			selectMale();
		} else {
			selectFemale();
		}
	}

	private void selectFemale() {
		ViewUtil.setGone(maleLayout, R.id.checked);
		ViewUtil.setVisible(femaleLayout, R.id.checked);
	}

	private void selectMale() {
		ViewUtil.setVisible(maleLayout, R.id.checked);
		ViewUtil.setGone(femaleLayout, R.id.checked);
	}

	private void fillUser(UserAccountClient userAccountClient) {
		userAccountClient.setId(Account.user.getId());
		userAccountClient.setImage(Account.user.getImage());
		userAccountClient.setNick(ViewUtil.getText(window, R.id.name));
		userAccountClient.setSex(sex);
		userAccountClient.setBirthday(birthdayPick.getDate());
		userAccountClient.setDesc(ViewUtil.getText(window, R.id.sign));
	}

	@Override
	public void onClick(View v) {
		if (v == rename) {
			new EditNameTip().show();
		} else if (v == iconLayout) {
			new IconPickTip().show();
		} else if (v == maleLayout) {
			if (sex == Constants.MALE)
				return;

			sex = Constants.MALE;
			selectMale();

		} else if (v == femaleLayout) {
			if (sex == Constants.FEMALE)
				return;

			sex = Constants.FEMALE;
			selectFemale();

		}
	}

	private class SaveInvoker extends BaseInvoker {

		private UserAccountClient tmp;

		private ReturnInfoClient ri;

		@Override
		protected void beforeFire() {
			tmp = Account.user.emptyUser();
			fillUser(tmp);
			super.beforeFire();
		}

		@Override
		protected String failMsg() {
			return "保存信息失败";
		}

		@Override
		protected void fire() throws GameException {
			ri = GameBiz.getInstance().playerUpdate(tmp);
		}

		@Override
		protected String loadingMsg() {
			return "保存用户资料中..";
		}

		@Override
		protected void onOK() {
			fillUser(Account.user);
			controller.setAccountBarUser(Account.user);
			controller.goBack();
			ri.setMsg("保存成功");
			controller.updateUI(ri, true);

		}

	}
}
