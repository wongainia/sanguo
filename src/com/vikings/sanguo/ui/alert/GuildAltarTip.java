/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-3 下午2:21:11
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.FiefDetailTopInfo;

public abstract class GuildAltarTip extends CustomConfirmDialog implements
		OnClickListener {
	protected BriefFiefInfoClient bfic;
	protected BriefGuildInfoClient gic;
	
	public GuildAltarTip() {
		super(CustomConfirmDialog.LARGE);
	}
	
	protected void showTip() {
		setTopInfo();
		super.show();
	}
	
	protected void setTopInfo() {
		new AddrLoader(getTitle(), TileUtil.fiefId2TileId(bfic.getId()),
				AddrLoader.TYPE_SUB);
		IconUtil.setFiefIconWithBattleState(findViewById(R.id.iconLayout), bfic);
		FiefDetailTopInfo.setBaseFiefInfo(tip, bfic, true);
		FiefDetailTopInfo.setGuildInfo(tip, gic, closeCb);
		ViewUtil.setRichText(tip, R.id.resTitle, "家族建设资源");
		setButtons();
	}
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected View getContent() {
		return controller.inflate(getLayoutId(), tip, false);
	}

	protected abstract int getLayoutId();
	protected abstract void setButtons();
}
