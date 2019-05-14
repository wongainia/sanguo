package com.vikings.sanguo.ui.alert;

import java.util.LinkedList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class MsgAlert extends Alert implements OnClickListener {

	private final static int layout = R.layout.alert_msg;

	private View content;

	private TextView title, msg, msgExtend;

	private LinkedList<MsgHolder> msgList = new LinkedList<MsgHolder>();

	@Override
	protected void playSound() {

	}

	public MsgAlert() {
		super(true);
		content = Config.getController().inflate(layout);
		title = (TextView) content.findViewById(R.id.title);
		msg = (TextView) content.findViewById(R.id.msg);
		msgExtend = (TextView) content.findViewById(R.id.msgExtend);
	}

	private void show() {
		content.requestLayout();
		super.show(content);
		this.dialog.setOnDismissListener(null);
	}

	public View getLayout() {
		return this.content;
	}

	public void show(String titleStr, String msgStr, int gravity,
			String msgExtendStr, CallBack callBack, boolean playDefaultSound) {
		if (playDefaultSound)
			SoundMgr.play(R.raw.sfx_tips2);

		if (isShow) {
			MsgHolder msgHolder = new MsgHolder(titleStr, msgStr, gravity,
					msgExtendStr, callBack, playDefaultSound);
			addMsg(msgHolder);
		} else {
			setText(title, titleStr);
			setText(msg, msgStr);
			msg.setGravity(gravity);
			setText(msgExtend, msgExtendStr);
		}

		if (callBack == null)
			this.dialog.setOnCancelListener(null);
		else
			this.dialog.setOnCancelListener(new CancleListener(callBack));

		show();
	}

	private void setText(View view, String str) {
		if (StringUtil.isNull(str))
			ViewUtil.setGone(view);
		else {
			ViewUtil.setVisible(view);
			ViewUtil.setRichText(view, str, true);
		}
	}

	@Override
	protected void doOnDismiss() {
		if (msgList.size() != 0) {
			MsgHolder holder = getMsg();
			show(holder.titleStr, holder.msgStr, holder.gravity,
					holder.msgExtendStr, holder.callBack,
					holder.playDefaultSound);
		}
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

	private class CancleListener implements OnCancelListener {

		CallBack call;

		public CancleListener(CallBack call) {
			this.call = call;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			if (null != call)
				call.onCall();
			MsgAlert.this.dialog.setOnCancelListener(null);
		}

	}

	private class MsgHolder {
		private String titleStr, msgStr, msgExtendStr;
		private int gravity;
		private CallBack callBack;
		private boolean playDefaultSound;

		public MsgHolder(String titleStr, String msgStr, int gravity,
				String msgExtendStr, CallBack callBack, boolean playDefaultSound) {
			this.titleStr = titleStr;
			this.msgStr = msgStr;
			this.gravity = gravity;
			this.msgExtendStr = msgExtendStr;
			this.callBack = callBack;
			this.playDefaultSound = playDefaultSound;
		}
	}

	private void addMsg(MsgHolder msgHolder) {
		synchronized (msgList) {
			msgList.addLast(msgHolder);
		}
	}

	private MsgHolder getMsg() {
		MsgHolder msgHolder = null;
		synchronized (msgList) {
			if (msgList.size() != 0) {
				msgHolder = msgList.removeFirst();
			}
		}
		return msgHolder;
	}

	public TextView getMsgView() {
		return msg;
	}

	public TextView getMsgExtendView() {
		return msgExtend;
	}
}
