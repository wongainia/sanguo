package com.vikings.sanguo.ui.alert;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class MsgConfirm extends CustomConfirmDialog { // Alert

	private static final int layout = R.layout.alert_confirm;
	protected View ok;
	protected View cancel;
	protected TextView msg, msgExt;

	public MsgConfirm(String title, int scale) {
		super(title, scale);
		ok = setButton(CustomConfirmDialog.SECOND_BTN, "确    认", null);
		cancel = setButton(CustomConfirmDialog.THIRD_BTN, "取    消", closeL);
		msg = (TextView) content.findViewById(R.id.msg);
		msgExt = (TextView) content.findViewById(R.id.msgExt);

	}

	public MsgConfirm() {
		this("", CustomConfirmDialog.MEDIUM);
	}

	public MsgConfirm setOKText(String text) {
		setButton(CustomConfirmDialog.SECOND_BTN, text, null);
		return this;
	}

	public MsgConfirm setCancelText(String text) {
		setButton(CustomConfirmDialog.THIRD_BTN, text, closeL);
		return this;
	}

	private void setContent(String msgStr, String msgExtStr) {
		ViewUtil.setRichText(msg, msgStr);
		if (StringUtil.isNull(msgExtStr)) {
			ViewUtil.setGone(msgExt);
		} else {
			ViewUtil.setVisible(msgExt);
			ViewUtil.setRichText(msgExt, msgExtStr);
		}
	}

	private void setGuildContent(GuildProp guildProp) {
		ViewUtil.setGone(msgExt);
		ViewUtil.setGone(msg);
		ViewUtil.setVisible(content, R.id.guildUpgradeLayout);
		ViewUtil.setRichText(content, R.id.maxMemberCnt, "提升家族等级，家族总人数上限提升至"
				+ guildProp.getMaxMemberCnt() + "人");
		ViewUtil.setRichText(content, R.id.reqMinVip, " 条件：VIP等级大于等于"
				+ guildProp.getReqMinVip());

		ViewUtil.setRichText(
				content,
				R.id.rmb,
				"消耗："
						+ StringUtil.color(guildProp.getRmb4LvUp() + "/"
								+ Account.user.getCurrency(),
								guildProp.getRmb4LvUp() > Account.user
										.getCurrency() ? R.color.color24
										: R.color.color19) + "#rmb#");
	}

	/**
	 * 
	 * @param msg
	 *            主提示信息
	 * @param msgTip
	 *            附加提示信息
	 * @param okHandler
	 *            ok后动作
	 * @param dismissHandler
	 *            弹出框关闭后的统一动作 包括ok cancel 以及返回键取消对话框
	 */
	public void show(String msg, String msgTip, final CallBack okHandler,
			final CallBack dismissHandler) {
		setContent(msg, msgTip);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (okHandler != null)
					okHandler.onCall();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (dismissHandler != null)
					dismissHandler.onCall();
			}
		});

		super.show();
	}

	/**
	 * 
	 * @param msg
	 * @param msgTip
	 * @param isHyaline
	 * @param okHandler
	 *            ok后 只执行okhandler
	 * @param cancelHandler
	 *            取消/返回键取消 只执行cancelhandler
	 */
	public void show(String msg, final CallBack okHandler,
			final CallBack cancelHandler) {
		setContent(msg, null);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (okHandler != null)
					okHandler.onCall();
				okDismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelHandler != null)
					cancelHandler.onCall();
			}
		});

		super.show();
	}

	/**
	 * 
	 * @param msg
	 * @param msgTip
	 * @param isHyaline
	 * @param okHandler
	 *            ok后 只执行okhandler
	 * @param cancelHandler
	 *            取消/返回键取消 只执行cancelhandler
	 */
	public void show(final CallBack okHandler, final CallBack cancelHandler,
			GuildProp guildProp) {
		// 家族升级提示框
		setGuildContent(guildProp);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (okHandler != null)
					okHandler.onCall();
				okDismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelHandler != null)
					cancelHandler.onCall();
			}
		});

		super.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, tip, false);
	}

	public View getOkButton() {
		return ok;
	}

	public View getCancelButton() {
		return cancel;
	}

	public void setMsgTextSize(int size) {
		msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
	}

}
