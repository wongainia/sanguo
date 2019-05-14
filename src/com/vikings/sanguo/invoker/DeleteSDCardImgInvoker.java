package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.exception.GameException;

public class DeleteSDCardImgInvoker extends BaseInvoker {
	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.DeleteSDCardImgInvoker_loadingMsg);
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.DeleteSDCardImgInvoker_failMsg);
	}

	@Override
	protected void fire() throws GameException {
		ctr.getFileAccess().clearImage();
		ctr.getFileAccess().clearCacheFile(FileAccess.BATTLE_LOG_PATH,
				FileAccess.BATTLE_LOG_PREFIX);
		// ctr.getFileAccess().clearCacheFile(
		// FileAccess.PARNET_PATH + "/" + BuildingData.FOLDER,
		// BuildingData.PREFIX);
	}

	@Override
	protected void onOK() {
		ctr.alert(ctr.getString(R.string.DeleteSDCardImgInvoker_onOK));
	}

}
