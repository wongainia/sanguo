package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class WeakStateTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_common_custom;
	private BriefUserInfoClient butcher;
	private UserAccountClient accountUser;
	private OtherUserClient otherUser;
	private ManorInfoClient mic;
	private int butcherId;

	public WeakStateTip(UserAccountClient accountUser, ManorInfoClient mic,
			int butcherId) {
		super("虚弱状态提示", DEFAULT);
		this.accountUser = accountUser;
		this.mic = mic;
		this.butcherId = butcherId;
	}

	public WeakStateTip(OtherUserClient otherUser, ManorInfoClient mic,
			int butcherId) {
		super("虚弱状态提示", DEFAULT);
		this.otherUser = otherUser;
		this.mic = mic;
		this.butcherId = butcherId;
	}

	public void show() {
		new FetchButcher().start();
	}

	private void showTip() {
		setValue();
		super.show();
	}

	private void setValue() {
		if (null != accountUser) {
			final int cost = TroopUtil.getMassacreCost(mic);
			ViewUtil.setRichText(tip, R.id.desc,
					"领主大人，你被" + butcher.getNickName() + "(ID:"
							+ butcher.getId().intValue()
							+ ")屠城了，目前正处于虚弱状态!<br/><br/>期间你无法使用资源招募士兵，消耗"
							+ StringUtil.color(cost + "元宝", R.color.k7_color8)
							+ "即可立即解除虚弱状态!");

			setButton(FIRST_BTN, "解除虚弱#rmb#" + cost, new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Account.user.getCurrency() < cost) {
						dismiss();
						new ToActionTip(cost).show();
						return;
					}
					new WeakRemoveInvoker().start();
				}
			});
		} else if (null != otherUser) {
			ViewUtil.setRichText(tip, R.id.desc,
					"该领主已被" + butcher.getNickName() + "(ID:"
							+ butcher.getId().intValue()
							+ ")屠城了，目前正处于虚弱状态!虚弱期间将无法使用资源招募士兵！");
		}

		setButton(SECOND_BTN, "取消", closeL);
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	private class FetchButcher extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "稍等...";
		}

		@Override
		protected String failMsg() {
			return "获取屠城者数据失败";
		}

		@Override
		protected void fire() throws GameException {
			butcher = CacheMgr.userCache.getUser(butcherId);
		}

		@Override
		protected void onOK() {
			showTip();
		}
	}

	private class WeakRemoveInvoker extends BaseInvoker {
		private ReturnInfoClient ric;

		@Override
		protected String loadingMsg() {
			return "解除...";
		}

		@Override
		protected String failMsg() {
			return "解除失败";
		}

		@Override
		protected void fire() throws GameException {
			ric = GameBiz.getInstance().manorWeakRemove();
			if (null != accountUser)
				accountUser.removeWeak();
			else if (null != otherUser)
				otherUser.removeWeak();
		}

		@Override
		protected void onOK() {
			ctr.updateUI(ric, true, false, true);
			WeakStateTip.this.dismiss();
			ctr.alert("成功解除虚弱状态");
		}

	}
}
