/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-2 下午2:40:39
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class GuildAssignTroopTip extends CustomConfirmDialog implements OnClickListener{
	private View guard, withdraw, detail, history;
	
	public GuildAssignTroopTip() {
		super("兵力调遣", MEDIUM);
		
		guard = findViewById(R.id.guard);
		guard.setOnClickListener(this);
		withdraw = findViewById(R.id.withdraw);
		withdraw.setOnClickListener(this);
		detail = findViewById(R.id.detail);
		detail.setOnClickListener(this);
		history = findViewById(R.id.history);
		history.setOnClickListener(this);
		View close = findViewById(R.id.close);
		close.setOnClickListener(closeL);
	}
	
	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_guild_assign_troop, tip, false);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == guard) {
			
		} else if (v == withdraw) {
			
		} else if (v == detail) {
			controller.openFiefTroopWindow(Account.guildAltar);
		} else if (v == history) {
			controller.openHistoryWarInfoWindow(Account.guildAltar);
		}
	}

}
