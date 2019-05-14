package com.vikings.sanguo.ui;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UserNotifyInfoClient;

public class ScrollText extends View {

	public final static String TAG = ScrollText.class.getSimpleName();

	private String text = "";// 文本内容

	private int index = 0;

	private float tmp_x = 0;

	private float tmp_left = 0;

	private Paint paint = new Paint();

	private UserNotifyInfoClient msg = null;

	private LinkedList<UserNotifyInfoClient> msgLs = new LinkedList<UserNotifyInfoClient>();

	public ScrollText(Context context) {
		super(context);
	}

	public ScrollText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void fetchMsg() {
		this.post(new FetchMsg());
	}

	// private int y = (int) (20*Config.SCALE_FROM_HIGH);

	@Override
	protected void onDraw(Canvas canvas) {
		if (this.msg == null) {
			return;
		}
		// canvas.drawColor(Color.WHITE);
		canvas.drawText(text, tmp_x, paint.getTextSize(), paint);
		tmp_x -= 2 * Config.SCALE_FROM_HIGH;
		if (tmp_x <= tmp_left) {
			if (msg.isExpired()) {
				synchronized (msgLs) {
					msgLs.remove(msg);
				}
				Account.notifyInfoCache.removeMsg(msg);
			}
			showNext();
		} else {
			postInvalidateDelayed(100);
		}
	}

	private void stop() {
		tmp_x = 0;
		// 将msg置空，解决重绘announce时，调用ScrollText的ondraw，导致最后一条过期公告会再跑
		msg = null;
		return;
	}

	/**
	 * 显示下一条公告
	 */
	private void showNext() {
		paint.setColor(0xFFADD0D3);
		paint.setTextSize(20 * Config.SCALE_FROM_HIGH);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		synchronized (this.msgLs) {
			if (this.msgLs.size() == 0) {
				stop();
				return;
			}
			goNext();
			msg = this.msgLs.get(index);
		}
		this.text = msg.getMessage();
		tmp_left = -paint.measureText(text);
		tmp_x = this.getWidth();
		// this.setText(text);
		postInvalidate();
	}

	public void goNext() {
		index++;
		index = index % msgLs.size();
		// if (index == 0) {
		// synchronized (this.msgLs) {
		// Collections.sort(msgLs, new Comparator<UserNotifyInfoClient>() {
		//
		// @Override
		// public int compare(UserNotifyInfoClient notify1,
		// UserNotifyInfoClient notify2) {
		// return notify1.getNotifyInfo().getType().intValue()
		// - notify2.getNotifyInfo().getType().intValue();
		// }
		// });
		// }
		// }
	}

	public LinkedList<UserNotifyInfoClient> getMsgLs() {
		return msgLs;
	}

	private class FetchMsg implements Runnable {

		@Override
		public void run() {
			// if (msg.isExpired())
			// return;

			synchronized (msgLs) {
				msgLs.clear();
				if (null != Account.notifyInfoCache.getSysMsg())
					msgLs.addAll(Account.notifyInfoCache.getSysMsg());
				// // test
				// for (int i = 0; i < 10; i++) {
				// UserNotifyInfoClient ms = UserNotifyInfoClient
				// .convert(new UserNotifyInfo()
				// .setMessage(
				// "测试公告测试公告测试公告测试公告测试公告测试公告测试公告测试公告测试公告")
				// .setDuration(100000)
				// .setStart(
				// (int) (Config.serverTime() / 1000))
				// .setType((i % 4 + 1)));
				// msgLs.add(ms);
				// }
			}
			showNext();
		}

	}

}
