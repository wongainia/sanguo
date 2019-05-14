package com.vikings.sanguo.ui.window;

abstract public class PopupWindow extends PopupUI {

	@Override
	public void showUI() {
		checkUI();
		super.showUI();
	}

	/**
	 * 根据等级判断功能是否开启 需要进行判断的子类需要重写
	 */
	protected void checkUI() {
	}

}
