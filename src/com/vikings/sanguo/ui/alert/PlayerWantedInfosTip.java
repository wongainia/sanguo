package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.PlayerWantedInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.PlayerWantedInfo;
import com.vikings.sanguo.protos.RoleStatusInfo;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.PlayerWantedInfoAdapter;
import com.vikings.sanguo.widget.PageListView;

public class PlayerWantedInfosTip extends PageListView {

	public PlayerWantedInfosTip() {
		super();
		setTitle();
		firstPage();
	}

	public void setTitle() {
		setTitle("江湖追杀令");
		setContentTitle("进攻他们的领地能获得额外的奖励");
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new PlayerWantedInfoAdapter(new CallBack() {
			@Override
			public void onCall() {
				dismiss();
			}
		});
	}

	private long getFetchBeginId() {
		Object obj = adapter.getLast();
		if (obj == null)
			return 0;
		else
			return ((PlayerWantedInfoClient) obj).getInfo().getId();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<PlayerWantedInfoClient> contentDatas = new ArrayList<PlayerWantedInfoClient>();
		long beginId = getFetchBeginId();
		if (beginId == 0) {
			if (Account.user.isWanted()) {
				RoleStatusInfo statusInfo = Account.user.getWantedInfo();
				BriefUserInfoClient briefUser = CacheMgr.userCache
						.getUser(statusInfo.getValue());
				PlayerWantedInfo info = new PlayerWantedInfo()
						.setCountry(briefUser.getCountry())
						.setEndTime(statusInfo.getTime())
						.setTarget(Account.user.getId())
						.setUserid(statusInfo.getValue());
				PlayerWantedInfoClient pwid = new PlayerWantedInfoClient(info);
				pwid.setTargetUser(Account.user.bref());
				pwid.setBriefUser(briefUser);
				contentDatas.add(pwid);
			}
		}

		List<PlayerWantedInfoClient> temp = GameBiz.getInstance()
				.playerWantedInfoQuery(Account.user.getCountry(), beginId,
						resultPage.getPageSize());

		// 过滤已经过期的追杀令
		List<PlayerWantedInfoClient> datas = new ArrayList<PlayerWantedInfoClient>();
		if (!temp.isEmpty()) {
			for (PlayerWantedInfoClient info : temp) {
				int leftTime = info.getInfo().getEndTime()
						- Config.serverTimeSS();
				if (leftTime > 0)
					datas.add(info);
			}
		}
		int total = Integer.MAX_VALUE;
		// 当次取到的数据不满一页，说明已经取到了最后；
		if (temp.size() < resultPage.getPageSize()) {
			total = adapter.getCount() + datas.size();
		}
		for (Iterator<PlayerWantedInfoClient> iter = datas.iterator(); iter
				.hasNext();) {
			PlayerWantedInfoClient info = iter.next();
			if (info.getInfo().getTarget().intValue() == Account.user.getId()) {
				iter.remove();
				total = total - 1;
				break;
			}
		}
		contentDatas.addAll(datas);
		resultPage.setResult(contentDatas);
		resultPage.setTotal(total);
	}

	@Override
	public void handleItem(Object o) {

	}
}
