package com.vikings.sanguo.widget;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.PopupWindow;
import com.vikings.sanguo.utils.ViewUtil;

abstract public class CustomPopupWindow extends PopupWindow {
	protected static final byte WINDOW_BTN_BG_TYPE_CLICK = 1;
	protected static final byte WINDOW_BTN_BG_TYPE_DESC = 2;
	protected static final byte WINDOW_BTN_BG_TYPE_NULL = 3;
	protected ViewGroup window;
	protected TextView leftBtnTxt, rightBtnTxt;
	protected View belowBtn;
	protected ViewGroup mainContent, leftBtnLayout, rightBtnLayout,
			contentAboveTitle, contentBelowTitle, content, belowBtnFrame;

	protected boolean fullScreen = false;

	protected void init(String title, boolean fullScreen) {
		this.fullScreen = fullScreen;
		addWindow();
		mainContent = (ViewGroup) window.findViewById(R.id.mainContent);
		if (fullScreen) {
			LayoutParams params = (LayoutParams) mainContent.getLayoutParams();
			params.topMargin = 0;
		}
		leftBtnLayout = (ViewGroup) window.findViewById(R.id.leftBtn);
		rightBtnLayout = (ViewGroup) window.findViewById(R.id.rightBtn);
		leftBtnTxt = (TextView) window.findViewById(R.id.leftText);
		rightBtnTxt = (TextView) window.findViewById(R.id.rightText);
		contentAboveTitle = (ViewGroup) window.findViewById(R.id.upInfo);
		contentBelowTitle = (ViewGroup) window.findViewById(R.id.upContent);
		content = (ViewGroup) window.findViewById(R.id.content);
		belowBtnFrame = (ViewGroup) window.findViewById(R.id.belowBtnFrame);
		belowBtn = (Button) belowBtnFrame.findViewById(R.id.belowBtn);
		setBtnBg();
		ViewUtil.setBoldText(this.window.findViewById(R.id.title), title);
		setCommonBg();
	}

	protected void init(String title) {
		this.init(title, false);
	}

	private void setBtnBg() {
		setLeftBtnBg(getLeftBtnBgType());
		setRightBtnBg(getRightBtnBgType());
	}

	protected void setLeftBtnBg(byte type) {
		int resId = R.drawable.window_lt3;
		switch (type) {
		case WINDOW_BTN_BG_TYPE_CLICK:
			resId = R.drawable.window_lt1;
			break;
		case WINDOW_BTN_BG_TYPE_DESC:
			resId = R.drawable.window_lt2;
			break;
		case WINDOW_BTN_BG_TYPE_NULL:
			resId = R.drawable.window_lt3;
			break;
		default:
			resId = R.drawable.window_lt3;
			break;
		}
		setBtnBg(leftBtnLayout, resId);
	}

	protected void setRightBtnBg(byte type) {
		String name = "window_lt3";
		switch (type) {
		case WINDOW_BTN_BG_TYPE_CLICK:// 点击
			name = "window_lt1";
			break;
		case WINDOW_BTN_BG_TYPE_DESC:// 描述
			name = "window_lt2";
			break;
		case WINDOW_BTN_BG_TYPE_NULL:// 默认
			name = "window_lt3";
			break;
		default:
			name = "window_lt3";
			break;
		}
		ViewUtil.setBtnMirrorBt(rightBtnLayout, name);
	}

	/**
	 * 设置左右两边按钮样式
	 * 
	 * @param view
	 * @param string
	 * @param bgType
	 */
	protected void changeBtn(View view, String string, byte bgType) {
		if (view == leftBtnLayout) {
			ViewUtil.setRichText(view.findViewById(R.id.leftText), string, true);
			setLeftBtnBg(bgType);
		} else if (view == rightBtnLayout) {
			ViewUtil.setRichText(view.findViewById(R.id.rightText), string,
					true);
			setRightBtnBg(bgType);
		}

	}

	/**
	 * 设置按钮背景
	 * 
	 * @param view
	 * @param resId
	 */
	private void setBtnBg(View view, int resId) {
		ViewUtil.setImage(view, resId);
	}

	/**
	 * 默认背景3，其他需要替换的类重写改方法
	 * 
	 * @return
	 */
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_NULL;
	}

	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_NULL;
	}

	protected void addWindow() {
		window = (ViewGroup) controller.inflate(R.layout.common_list);
		controller.addContentFullScreen(window);
	}

	public void setBottomButton(String str, OnClickListener l) {
		if (ViewUtil.isGone(belowBtnFrame))
			ViewUtil.setVisible(belowBtnFrame);
		ViewUtil.setRichText(belowBtn, str, true);
		belowBtn.setOnClickListener(l);
	}

	public void setContentAboveTitle(int layoutId) {
		if (ViewUtil.isGone(contentAboveTitle))
			ViewUtil.setVisible(contentAboveTitle);
		controller.inflate(layoutId, contentAboveTitle);
	}

	public void setContentBelowTitle(int layoutId) {
		if (ViewUtil.isGone(contentBelowTitle))
			ViewUtil.setVisible(contentBelowTitle);

		controller.inflate(layoutId, contentBelowTitle);
	}

	public void setContent(int layoutId) {
		controller.inflate(layoutId, content);
	}

	public void setContent(View v) {
		content.addView(v);
	}

	@Override
	protected void destory() {
		controller.removeContentFullScreen(window);

	}

	@Override
	public void showUI() {
		super.showUI();
		if (fullScreen)
			controller.hideIconForFullScreen();
	}

	@Override
	public void hideUI() {
		super.hideUI();
		if (fullScreen)
			controller.showIconForFullScreen();
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	protected void setCommonBg() {
		ViewUtil.setImage(window, R.id.specificBg, R.drawable.common_list_bg);
	}

	protected void setCommonBg(int resId) {
		ViewUtil.setImage(window, R.id.specificBg, resId);
	}

	protected void setBottomPadding() {
		content.setPadding(0, 0, 0, (int) (30 * Config.scaleRate));
	}

	protected void setLeftTxt(String str) {
		ViewUtil.setBold((TextView) findViewById(R.id.leftText));
		ViewUtil.setRichText(window, R.id.leftText, str);
	}

	protected void setLeftBtn(String str, OnClickListener l) {
		View leftBtn = findViewById(R.id.leftBtn);
		ViewUtil.setBold((TextView) findViewById(R.id.leftText));
		ViewUtil.setRichText(window, R.id.leftText, str);
		leftBtn.setOnClickListener(l);
	}

	protected void refreshLeftBtn(String str) {
		ViewUtil.setBold((TextView) findViewById(R.id.leftText));
		ViewUtil.setRichText(window, R.id.leftText, str);
	}

	protected void setRightTxt(String str) {
		ViewUtil.setBold((TextView) findViewById(R.id.rightText));
		ViewUtil.setRichText(window, R.id.rightText, str);
	}

	protected void setRightTxt(String str, int iconW, int iconH) {
		ViewUtil.setBold((TextView) findViewById(R.id.rightText));
		ViewUtil.setRichText(window.findViewById(R.id.rightText), str, false,
				iconW, iconH);
	}

	protected void setRightBtn(String str, OnClickListener l) {
		View rightBtn = findViewById(R.id.rightBtn);
		ViewUtil.setBold((TextView) findViewById(R.id.rightText));
		ViewUtil.setRichText(window, R.id.rightText, str);
		rightBtn.setOnClickListener(l);
	}
}
