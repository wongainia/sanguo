package com.vikings.sanguo.invoker;

import com.vikings.sanguo.config.Config;

/**
 * 封装android烦琐的 新建子线程处理耗时操作 子线程返回结果 回调ui线程处理
 * 
 * @author Brad.Chen
 * 
 */
abstract public class SubThreadInvoker extends BaseInvoker{

	
	@Override
	protected void beforeFire() {
		ctr.postRunnable(new Runnable() {
			@Override
			public void run() {
				Config.getController().showLoading(loadingMsg());
			}
		});
	}
	
}
