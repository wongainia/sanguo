package com.vikings.sanguo.invoker;

import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;

/**
 * 退出游戏调用，保存玩家信息 写文件结束后回调callBack.onCall退出
 */
public class SaveFilesInvoker extends BaseInvoker {

	private boolean isLogout;

	/**
	 * @param isLogout
	 *            是否是退出
	 */
	public SaveFilesInvoker(boolean isLogout) {
		this.isLogout = isLogout;
	}

	@Override
	protected String failMsg() {
		return null;
	}

	@Override
	protected void fire() throws GameException {
		Account.saveAccount();
		if (isLogout) {
			ctr.getServerFileCache().save();
			ctr.getHandler().postDelayed(new Runnable() {

				@Override
				public void run() {
					ctr.killProcess();
				}
			}, 2000);
			GameBiz.getInstance().logout();
		}

	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.RegisterInvoker_loadingMsg);
	}

	@Override
	protected void afterFire() {
		if (isLogout)
			super.afterFire();
	}

	@Override
	protected void beforeFire() {
		if (isLogout)
			super.beforeFire();
	}

	@Override
	protected void onFail(GameException exception) {
		Log.e("Invoker fail", Log.getStackTraceString(exception));
		if (isLogout) {
			ctr.killProcess();
		}
	}

	@Override
	protected void onOK() {
		if (isLogout) {
			ctr.killProcess();
		}
	}

}
