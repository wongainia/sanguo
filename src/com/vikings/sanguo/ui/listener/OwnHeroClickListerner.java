package com.vikings.sanguo.ui.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.window.OwnHeroDetailWindow;

public class OwnHeroClickListerner implements OnClickListener {
	private HeroInfoClient hic;
	private boolean showAbandon;
	// 将领上阵
	private CallBack mCallBack;

	public OwnHeroClickListerner(HeroInfoClient hic, boolean showAbandon) {
		this.hic = hic;
		this.showAbandon = showAbandon;
	}

	public OwnHeroClickListerner(HeroInfoClient hero, CallBack mCallBack) {
		this.hic = hero;
		this.showAbandon = false;
		this.mCallBack = mCallBack;
	}

	public OwnHeroClickListerner(HeroInfoClient hero) {
		this.hic = hero;
		this.showAbandon = false;
	}

	@Override
	public void onClick(View v) {
		if (null != hic && hic.isValid()
				&& Account.heroInfoCache.get(hic.getId()) != null) {
			new OwnHeroDetailWindow().open(hic, showAbandon, mCallBack);
		}
	}

}
