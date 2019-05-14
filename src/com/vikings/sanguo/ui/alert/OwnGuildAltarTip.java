/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-29 下午5:47:01
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.invoker.GetAltarInvoker;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class OwnGuildAltarTip extends GuildAltarTip{
	private View idol, donate, battleField, assistDef, detail, assign, close;
	
	public OwnGuildAltarTip() {
		super();
		bfic = Account.guildAltar;
		gic = Account.guildCache.getRichInfoInCache().getGic();
	}

	public void show() {
		new GetAltarInvoker(new CallBack() {
			@Override
			public void onCall() {
				showTip();
			}
		}, null).start();
	}

	@Override
	protected void setButtons() {
		idol = findViewById(R.id.idol);
		idol.setOnClickListener(this);
		
		if (Account.isAltarInBattle()) {
			ViewUtil.setGone(tip, R.id.donate);
			ViewUtil.setGone(tip, R.id.detail);
			
			battleField = findViewById(R.id.battleField);
			ViewUtil.setVisible(battleField);
			battleField.setOnClickListener(this);
			
			assistDef = findViewById(R.id.assistDef);
			ViewUtil.setVisible(assistDef);
			assistDef.setOnClickListener(this);
		} else {
			donate = findViewById(R.id.donate);
			donate.setOnClickListener(this);
			
			detail = findViewById(R.id.detail);
			detail.setOnClickListener(this);
		}
		
		assign = findViewById(R.id.assign);
		if (Account.isMeGuildLeader()) 
			assign.setOnClickListener(this);
		else
			ViewUtil.setGone(assign);
		
		close = findViewById(R.id.close);
		close.setOnClickListener(closeL);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == idol) {
			
		} else if (v == donate) {
			
		} else if (v == battleField) {
			
		} else if (v == assistDef) {
			
		} else if (v == detail) {
			controller.openFiefDetailWindow(Account.guildAltar);
		} else if (v == assign) {
			new GuildAssignTroopTip().show();
		} 
	}

	@Override
	protected int getLayoutId() {
		return R.layout.alert_own_guild_altar;
	}
}
