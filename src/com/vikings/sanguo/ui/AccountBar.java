package com.vikings.sanguo.ui;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.ShowItemTip;
import com.vikings.sanguo.ui.alert.UserLevelUpTip;
import com.vikings.sanguo.utils.StringUtil;

public class AccountBar extends BaseUI {

	private View content;

	@Override
	protected void bindField() {
	}

	public void postUpdate() {
		content.post(new Runnable() {
			@Override
			public void run() {

			}
		});
	}

	public View getView() {
		return content;
	}

	public void updateUI(ReturnInfoClient rsinfo, boolean isGain,
			boolean playDefaultSound, boolean showLevelUpTip) {
		if (null == rsinfo)
			return;
		if (rsinfo.getMoney() != 0 && playDefaultSound) {
			SoundMgr.play(R.raw.sfx_sell);
		}
		CallBack checkCall = null;
		if (rsinfo.getLevel() > 0 && showLevelUpTip) {
			checkCall = new LevelUpCallback(rsinfo);
		}
		if (!StringUtil.isNull(rsinfo.getMsg())) {
			new ShowItemTip().show(rsinfo.getMsg(), rsinfo.showRequire(),
					checkCall, playDefaultSound);
			// controller.alert("", rsinfo.toDesc(isGain, false), checkCall,
			// playDefaultSound);
		} else {
			if (rsinfo.getLevel() > 0 && checkCall != null)
				checkCall.onCall();
		}
		controller.refreshCurPopupUI();
	}

	private class LevelUpCallback implements CallBack {

		private ReturnInfoClient rs;

		public LevelUpCallback(ReturnInfoClient rs) {
			this.rs = rs;
		}

		@Override
		public void onCall() {
			new UserLevelUpTip(rs).show();
		}
	}
}
