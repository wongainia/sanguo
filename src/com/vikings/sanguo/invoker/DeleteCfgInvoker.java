package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.exception.GameException;

public class DeleteCfgInvoker extends BaseInvoker {
	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.DeleteCfgInvoker_loadingMsg);
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.DeleteCfgInvoker_failMsg);
	}

	@Override
	protected void fire() throws GameException {
		ctr.getFileAccess().clearCfg();
	}

	@Override
	protected void onOK() {
		ctr.alert(ctr.getString(R.string.DeleteCfgInvoker_onOK_1),
				ctr.getString(R.string.DeleteCfgInvoker_onOK_2), null, true);
	}

}
