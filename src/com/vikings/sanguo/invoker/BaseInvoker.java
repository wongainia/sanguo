package com.vikings.sanguo.invoker;

import android.util.Log;

import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.AESKeyCache;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.controller.GameController;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 封装android烦琐的 新建子线程处理耗时操作 子线程返回结果 回调ui线程处理
 * 
 * @author Brad.Chen
 * 
 */
abstract public class BaseInvoker {

	protected GameController ctr = Config.getController();

	/**
	 * 显示loading的提示信息
	 * 
	 * @return
	 */
	abstract protected String loadingMsg();

	/**
	 * 失败后的提示消息
	 * 
	 * @return
	 */
	abstract protected String failMsg();

	/**
	 * 开始执行前 ui提示
	 */
	protected void beforeFire() {
		Config.getController().showLoading(loadingMsg());
	}

	/**
	 * call server的方法
	 */
	abstract protected void fire() throws GameException;

	/**
	 * 服务器返回后 不论成功/失败 的统一处理
	 */
	protected void afterFire() {
		Config.getController().dismissLoading();
	}

	/**
	 * 服务器返回ok后该如何处理?
	 */
	abstract protected void onOK();

	/**
	 * 服务器返回fail后的处理 如有需要可被子类override
	 * 
	 * @param exception
	 */
	protected void onFail(GameException exception) {
		Log.e("Invoker fail", Log.getStackTraceString(exception));
		String msg = failMsg();
		if (!StringUtil.isNull(msg))
			msg = msg + exception.getErrorMsg();
		else
			msg = exception.getErrorMsg();
		Config.getController().alert("", msg, null, false);
	}

	/**
	 * 外部调用 开始执行
	 */
	public void start() {
		beforeFire();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					try {
						fire();
					} catch (GameException e) {
						// 如果错误码为149，调用登录接口取token
						if (e.getResult() == ResultCode.RESULT_FAILED_AES_INVALID) {
							GameBiz.getInstance().reLogin();
							AESKeyCache.save();
							// 重新发业务请求（该方式在极端的情况下会有漏洞，如：fire中有多个请求，第一个请求（比如开战）发送成功，第二个请求返回149，重新调fire，会第二次调开战，而此时该战斗已经结束）
							fire();
						} else {
							if (e.getResult() == ResultCode.RESULT_FAILED_TOKEN) {
								if (null != Account.user) {
									AESKeyCache.clear(Account.user.getId());
								}
								// Config.sessionId = 0;
							}
							throw e;
						}
					}
					ctr.postRunnable(new CallBack());
				} catch (GameException e) {
					ctr.postRunnable(new CallBack().setException(e));
				} catch (Exception e) {
					ctr.postRunnable(new CallBack()
							.setException(new GameException(e.getMessage())));
					Log.e("Invoker", e.getMessage(), e);
				}
			}
		}).start();
	}

	private class CallBack implements Runnable {

		protected GameException exception = null;

		public Runnable setException(GameException exception) {
			this.exception = exception;
			return this;
		}

		public void run() {
			try {
				if (exception == null)
					onOK();
				else
					onFail(exception);
			} catch (Exception e) {
				Log.e("Exception", "Exception occur when update UI", e);
			}
			afterFire();
		}
	}
}
