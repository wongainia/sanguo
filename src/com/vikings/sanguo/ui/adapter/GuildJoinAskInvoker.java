/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-19 下午7:09:41
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.GuildJoinAskResp;
import com.vikings.sanguo.model.GuildSearchInfoClient;
import com.vikings.sanguo.ui.alert.CommonAlert;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.ui.window.GuildInfoWindow;
import com.vikings.sanguo.utils.StringUtil;

public class GuildJoinAskInvoker extends BaseInvoker {
	private GuildJoinAskResp resp;
	private GuildSearchInfoClient data;
	
	public GuildJoinAskInvoker(GuildSearchInfoClient data) {
		this.data = data;
	}
	
	@Override
	protected String loadingMsg() {
		return "申请中...";
	}

	@Override
	protected String failMsg() {
		return "申请失败";
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().guildJoinAsk(data.getInfo().getId(),
				"");
		if (resp.isAutoJoin()) {
			Account.guildCache.setGuildid(data.getInfo().getId());
			Account.guildCache.updata(true);
		}
		
		if (!StringUtil.isFlagOn(Account.user.getTraining(), BaseStep.INDEX_FIRST_JOIN_GUILD))
			Account.setFlag(BaseStep.INDEX_FIRST_JOIN_GUILD);
	}

	@Override
	protected void onOK() {
		ctr.updateUI(resp.getRi(), true);
		if (resp.isAutoJoin()) {
			if (null != ctr.getHeartBeat())
				ctr.getHeartBeat().updataChatIds();
			ctr.closeAllPopup();
			new GuildInfoWindow().open(data.getInfo().getId());
			new CommonAlert("加入家族成功", "恭喜您成为"
					+ StringUtil.color(data.getInfo().getName(),
							R.color.k7_color9) + "家族的一员，快和您所属家族的成员打个招呼吧！",
					"", "点击任意位置关闭", true).show();
		} else {
			ctr.alert("已成功向" + data.getInfo().getName() + "家族发出申请");
		}
	}

}