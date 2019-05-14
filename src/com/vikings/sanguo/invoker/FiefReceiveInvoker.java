package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.sound.SoundMgr;

public class FiefReceiveInvoker extends BaseInvoker {

	private long fiefId;

	private ReturnInfoClient ri;

	public FiefReceiveInvoker(long fiefId) {
		this.fiefId = fiefId;
	}

	@Override
	protected String loadingMsg() {
		return "收获中";
	}

	@Override
	protected String failMsg() {
		return "收获失败";
	}

	@Override
	protected void fire() throws GameException {
		ri = GameBiz.getInstance().fiefReceive(fiefId);
	}

	@Override
	protected void onOK() {
		SoundMgr.play(R.raw.sfx_receive);
		ri.setMsg("收获成功");
		ctr.updateUI(ri, true, false, true);
	}

}
