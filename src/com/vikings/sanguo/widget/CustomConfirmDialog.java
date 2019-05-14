package com.vikings.sanguo.widget;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.Alert;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;

public abstract class CustomConfirmDialog extends Alert {
	protected ViewGroup tip, contentLayout;
	protected View content;
	protected int scale;

	public final static int FIRST_BTN = 0;
	public final static int SECOND_BTN = 1;
	public final static int THIRD_BTN = 2;

	// 5.2 19：50 与李浩确认方案
	public final static int DEFAULT = 0;
	public final static int MEDIUM = 1;
	public final static int LARGE = 2;
	public final static int HUGE = 3;

	private final static int HEIGHT_MEDIUM = 500;
	private final static int HEIGHT_LARGE = 700;

	public CustomConfirmDialog() {
		init(CustomConfirmDialog.DEFAULT);
	}

	public CustomConfirmDialog(String title) {
		this(title, CustomConfirmDialog.DEFAULT);
	}

	// scale:dialog的规模大小，分小/中/大三种
	public CustomConfirmDialog(int scale) {
		init(scale);
	}

	public CustomConfirmDialog(int scale, boolean isTouchClose) {
		super(isTouchClose);
		init(scale);
	}

	public CustomConfirmDialog(String title, int scale) {
		this(title, scale, false);
	}

	public CustomConfirmDialog(String title, int scale, boolean isTouchClose) {
		super(isTouchClose);
		init(scale);
		setTitle(title);
	}

	private void init(int scale) {
		tip = (ViewGroup) controller.inflate(R.layout.alert_custom_confirm);
		ViewUtil.setImage(tip, R.drawable.alert_custom_bg);
		contentLayout = (ViewGroup) tip.findViewById(R.id.content);
		this.scale = scale;
		content = getContent();
		if (content != null) {
			ViewGroup vg = ((ViewGroup) tip.findViewById(R.id.content));
			vg.addView(content);
		}
		if (isTouchClose) {
			ViewUtil.setVisible(tip, R.id.closeDesc);
		}
	}

	protected void setTipBg(int scale) {
		View view = tip.findViewById(R.id.bg);
		if (null == view)
			return;
		switch (scale) {
		case MEDIUM:
			setLayoutParams(view, LayoutParams.FILL_PARENT,
					(int) (Config.SCALE_FROM_HIGH * HEIGHT_MEDIUM));
			break;
		case LARGE:
			setLayoutParams(view, LayoutParams.FILL_PARENT,
					(int) (Config.SCALE_FROM_HIGH * HEIGHT_LARGE));
			break;
		default:
			setLayoutParams(view, LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			break;
		}
	}

	protected void setLayoutParams(View view, int width, int height) {
		LayoutParams params = view.getLayoutParams();
		params.width = width;
		params.height = height;
		view.setLayoutParams(params);
	}

	public void setTitle(String title) {
		ViewUtil.setBoldText(tip.findViewById(R.id.title), title);
	}

	public TextView getTitle() {
		return (TextView) tip.findViewById(R.id.title);
	}

	protected abstract View getContent();

	public View setButton(int idx, String str, OnClickListener l) {
		if (ViewUtil.isVisible(tip.findViewById(R.id.closeFrame)))
			ViewUtil.setGone(tip, R.id.closeFrame);

		if (2 == str.length())
			str = ViewUtil.half2Full(str.charAt(0) + "  " + str.charAt(1));
		switch (idx) {
		case FIRST_BTN:
			setButtonContent(R.id.btn1, str, l);
			return tip.findViewById(R.id.btn1);
		case SECOND_BTN:
			setButtonContent(R.id.btn2, str, l);
			return tip.findViewById(R.id.btn2);
		case THIRD_BTN:
			setButtonContent(R.id.btn3, str, l);
			return tip.findViewById(R.id.btn3);
		default:
			return null;
		}
	}

	public void setButtonGone(int idx) {
		switch (idx) {
		case FIRST_BTN:
			ViewUtil.setGone(tip, R.id.btn1);
			break;
		case SECOND_BTN:
			ViewUtil.setGone(tip, R.id.btn2);
			break;
		case THIRD_BTN:
			ViewUtil.setGone(tip, R.id.btn3);
			break;
		default:
			break;
		}
	}

	public void setButtonGray(int idx) {
		switch (idx) {
		case FIRST_BTN:
			View btn1 = tip.findViewById(R.id.btn1);
			if (null != btn1) {
				ViewUtil.setVisible(btn1);
				ImageUtil.setBgGray(btn1);
				btn1.setOnClickListener(null);
			}
			break;
		case SECOND_BTN:
			View btn2 = tip.findViewById(R.id.btn2);
			if (null != btn2) {
				ViewUtil.setVisible(btn2);
				ImageUtil.setBgGray(btn2);
				btn2.setOnClickListener(null);
			}
			break;
		case THIRD_BTN:
			View btn3 = tip.findViewById(R.id.btn3);
			if (null != btn3) {
				ViewUtil.setVisible(btn3);
				ImageUtil.setBgGray(btn3);
				btn3.setOnClickListener(null);
			}
			break;
		default:
			break;
		}
	}

	private void setButtonContent(int resId, String str, OnClickListener l) {
		ViewUtil.setVisible(tip.findViewById(R.id.btnFrame));
		Button btn = (Button) tip.findViewById(resId);
		ViewUtil.setVisible(btn);
		ViewUtil.setBoldRichText(btn, str);
		btn.setOnClickListener(l);
	}

	// 与setButton(int idx, String str, OnClickListener l)不同时使用
	public void setCloseBtn() {
		ViewUtil.setVisible(tip, R.id.closeFrame);
		ViewUtil.setGone(tip, R.id.btnFrame);
		Button close = (Button) tip.findViewById(R.id.close);
		close.setOnClickListener(closeL);
		ViewUtil.setBold(close);
	}

	public void setCloseBtn(String str, OnClickListener l) {
		ViewUtil.setVisible(tip, R.id.closeFrame);
		ViewUtil.setGone(tip, R.id.btnFrame);
		ViewUtil.setText(tip, R.id.close, str);
		Button close = (Button) tip.findViewById(R.id.close);
		close.setOnClickListener(l);
		ViewUtil.setBold(close);
	}

	// 设置右上角的关闭按钮
	public void setRightTopCloseBtn() {
		View close = tip.findViewById(R.id.rtClose);
		ViewUtil.setVisible(close);
		close.setOnClickListener(closeL);
	}

	public void show() {
		super.show(tip);
		setTipBg(scale);
	}

	public OnClickListener closeL = new OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	public CallBack closeCb = new CallBack() {
		@Override
		public void onCall() {
			dismiss();
		}
	};

	public View findViewById(int resId) {
		return content.findViewById(resId);
	}
}
