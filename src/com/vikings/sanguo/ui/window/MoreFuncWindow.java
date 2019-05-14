package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class MoreFuncWindow extends CustomListViewWindow {

	@Override
	protected ObjectAdapter getAdapter() {
		FuncAdapter adapter = new FuncAdapter();
		adapter.addItems(text);
		return adapter;
	}

	@Override
	protected void init() {
		init("更多功能");
	}

	private class FuncAdapter extends ObjectAdapter {

		@Override
		public void setViewDisplay(View v, Object o, int index) {

			ViewUtil.setBoldText(v.findViewById(R.id.name), text[index]);
			v.setOnClickListener(onclick[index]);
		}

		@Override
		public int getLayoutId() {
			return R.layout.func_item;
		}

	}

	private String[] text = { "将领图鉴", // "技能图鉴",
			"官网公告", "游戏设置", "帐号管理", "退出游戏" };

	private OnClickListener[] onclick = {
			// new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // new
			// //
			// SystemNotifyTip(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
			// // (byte) 26)).show();
			// controller.closeAllPopup();
			// new Prologue().debug();
			// }
			// },
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					new HeroStatisticsWindow().open();
				}
			}, 
			// new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// new SkillIllustrationWindow().open();
			// }
			// },
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					controller.openSite(CacheMgr.dictCache.getDict(
							Dict.TYPE_SITE_ADDR, (byte) 1));
				}
			}, new OnClickListener() {

				@Override
				public void onClick(View v) {
					controller.openGameSetting(false);
				}
			}, new OnClickListener() {

				@Override
				public void onClick(View v) {
					controller.openAccountMangt();
				}
			}, new OnClickListener() {

				@Override
				public void onClick(View v) {
					Config.getController().quit();
				}
			} };

}
