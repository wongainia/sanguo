package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.GetHelpArmResp;
import com.vikings.sanguo.message.UserStatusUpdateResp;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.protos.ROLE_STATUS;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ProtectedTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_protected;
	private TextView newerDesc, vipDesc, desc, timeDesc;
	private ImageView icon;
	private Button activeBtn, helpArmBtn, closeBtn;

	public ProtectedTip() {
		super("神圣护佑", DEFAULT);
		refreshInterval = 1000;
		newerDesc = (TextView) content.findViewById(R.id.newerDesc);
		vipDesc = (TextView) content.findViewById(R.id.vipDesc);
		desc = (TextView) content.findViewById(R.id.desc);
		timeDesc = (TextView) content.findViewById(R.id.time);
		icon = (ImageView) content.findViewById(R.id.icon);
		activeBtn = (Button) content.findViewById(R.id.activeBtn);
		activeBtn.setOnClickListener(this);
		helpArmBtn = (Button) content.findViewById(R.id.helpArmBtn);
		helpArmBtn.setOnClickListener(this);
		ViewUtil.setText(helpArmBtn, "领取救兵");
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		updateDynView();
		ViewUtil.setImage(icon, R.drawable.protected_icon);
		ViewUtil.setRichText(desc,
				CacheMgr.uiTextCache.getTxt(UITextProp.PROTECTED_DESC));
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	@Override
	public void onClick(View v) {
		if (v == activeBtn) {
			if (Account.user.isNewerProtectedLevel()
					&& Account.user.getCurVip().getLevel() >= CacheMgr.userCommonCache
							.getVipSpecialMinLevel()) {
				new ActiveInvoker(Constants.TYPE_NOAMRL).start();
			} else {
				if (Account.user.getCurrency() < CacheMgr.userCommonCache
						.getVipPrice()) {
					dismiss();
					new ToActionTip(CacheMgr.userCommonCache.getVipPrice())
							.show();
					return;
				}
				new ActiveInvoker(Constants.TYPE_CURRENCY).start();
			}
		} else if (v == helpArmBtn) {
			if (Account.user.getWeakLeftTime() > 0) {
				controller.alert("你被屠城了，不能领取救济士兵");
				return;
			}
			if (Account.user.getHelpArmCount() > 0) {
				controller.alert("您已经领取过救兵了（激活【勇者无敌】状态，只可免费领取一次救兵）");
				return;
			}

			if (Account.myLordInfo.getArmCount() >= CacheMgr.userCommonCache
					.getVipBlessMinTroopCount()) {
				controller.alert("只有您的兵力低于10万时，才能领取系统救兵");
				return;
			}

			new HelpArmInvoker().start();
		}
	}

	@Override
	protected void updateDynView() {
		byte newerMaxLevel = CacheMgr.userCommonCache.getNewerMaxLevel();
		byte vipLevel = CacheMgr.userCommonCache.getVipSpecialMinLevel();
		int time = Account.user.getVipBlessTime();
		if (Account.user.getLevel() < newerMaxLevel) {
			ViewUtil.setGone(newerDesc);
			ViewUtil.setText(newerDesc, "你不能对" + newerMaxLevel
					+ "级及以上玩家发动攻击，也不会受到他们的攻击！（该保护" + newerMaxLevel + "级后消失）");
			ViewUtil.setVisible(vipDesc);
			// ViewUtil.setText(vipDesc, "达到VIP" + vipLevel + "可免费激活以下效果");
			ViewUtil.setRichText(vipDesc,
					CacheMgr.uiTextCache.getTxt(UITextProp.PROTECTED_VIP_DESC));

			if (time <= 0) { // 处于未激活状态
				ViewUtil.setVisible(activeBtn);
				if (Account.user.getCurVip().getLevel() < CacheMgr.userCommonCache
						.getVipSpecialMinLevel()) {
					ViewUtil.setRichText(activeBtn, "激活 #rmb#"
							+ CacheMgr.userCommonCache.getVipPrice(), true);
				} else {
					ViewUtil.setRichText(activeBtn, "激  活");
				}
				ViewUtil.setGone(helpArmBtn);
				ViewUtil.setText(timeDesc, "激活后可生效24小时");
			} else {
				ViewUtil.setGone(activeBtn);
				ViewUtil.setVisible(helpArmBtn);
				ViewUtil.setText(timeDesc, "剩余:" + DateUtil.formatMinute(time));
			}
		} else {
			ViewUtil.setGone(newerDesc);
			ViewUtil.setGone(vipDesc);
			if (time <= 0) { // 处于未激活状态
				ViewUtil.setRichText(activeBtn, "激活 #rmb#"
						+ CacheMgr.userCommonCache.getVipPrice(), true);
				ViewUtil.setVisible(activeBtn);
				ViewUtil.setGone(helpArmBtn);
				ViewUtil.setText(timeDesc, "激活后可生效24小时");
			} else {
				ViewUtil.setGone(activeBtn);
				ViewUtil.setVisible(helpArmBtn);
				ViewUtil.setText(timeDesc, "剩余:" + DateUtil.formatMinute(time));
			}
		}

	}

	private class ActiveInvoker extends BaseInvoker {
		private int type;
		private UserStatusUpdateResp resp;

		public ActiveInvoker(int type) {
			this.type = type;
		}

		@Override
		protected String loadingMsg() {
			return "激活";
		}

		@Override
		protected String failMsg() {
			return "激活失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().userStatusUpdate(
					ROLE_STATUS.ROLE_STATUS_VIP_BLESS, type);
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRic(), true);
			ctr.alert("激活成功");
		}

	}

	private class HelpArmInvoker extends BaseInvoker {
		private GetHelpArmResp resp;

		@Override
		protected String loadingMsg() {
			return "领取救兵";
		}

		@Override
		protected String failMsg() {
			return "领取救兵失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().getHelpArm();
		}

		@Override
		protected void onOK() {
			resp.getRic().setMsg("恭喜你，领取救兵成功");
			ctr.updateUI(resp.getRic(), true);
		}

	}

}
