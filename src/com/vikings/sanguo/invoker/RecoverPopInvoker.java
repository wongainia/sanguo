/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-1-9 下午2:51:53
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.PlayerManorPopRecoverResp;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.thread.CallBack;

public class RecoverPopInvoker extends BaseInvoker {
	private PlayerManorPopRecoverResp resp;
	private int pop = 0;
	private CallBack cb;
	
	public RecoverPopInvoker(CallBack cb) {
		this.cb = cb;
	}
	
	@Override
	protected String loadingMsg() {
		return "恢复中…";
	}

	@Override
	protected String failMsg() {
		return "恢复失败";
	}

	@Override
	protected void fire() throws GameException {
		
		//TODO:建造民居或街市
		if (null == Account.manorInfoClient.getPopAddStatus()) {
		}
		
		
		pop = Account.manorInfoClient.getRealCurPop();
		resp = GameBiz.getInstance().playerManorPopRecover();
	}

	@Override
	protected void onOK() {
		SoundMgr.play(R.raw.sfx_resume);
		resp.getRi().setMsg(
				"恢复成功<br/><br/>你的主城恢复了"
						+ StringUtil.color(
								String.valueOf(resp.getMic()
										.getRealCurPop() - pop), "#F6A230")
						+ "居民");
		ctr.updateUI(resp.getRi(), true, false, false);
		Account.manorInfoClient.update(resp.getMic());
		if (null != cb)
			cb.onCall();
		
	}

}