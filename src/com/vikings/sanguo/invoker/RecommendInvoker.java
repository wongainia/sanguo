package com.vikings.sanguo.invoker;

import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.HotUserAttrScoreInfoQueryResp;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.FatSheepData;
import com.vikings.sanguo.model.ResultPage;

public class RecommendInvoker extends BaseInvoker {
	private BriefFiefInfoClient briefFief;

	@Override
	protected String loadingMsg() {
		return "查找";
	}

	@Override
	protected String failMsg() {
		return "查找失败";
	}

	@Override
	protected void fire() throws GameException {
		ResultPage resultPage = new ResultPage();
		resultPage.setCurIndex(0);
		resultPage.setPageSize((short) 10);
		HotUserAttrScoreInfoQueryResp resp = GameBiz.getInstance()
				.hotUserAttrScoreInfoQuery(Account.user.getLevel(),
						Account.user.getCountry(), resultPage, 2);
		List<FatSheepData> fetchDatas = resp.getDatas();
		if (null != fetchDatas && !fetchDatas.isEmpty()) {
			for (FatSheepData data : fetchDatas) {
				BriefFiefInfoClient bfic = data.getBfic();
				if (null != bfic && !bfic.isInBattle()) {
					briefFief = bfic;
					break;
				}
			}
		}
	}

	@Override
	protected void onOK() {
		if (null != briefFief) {
			ctr.getBattleMap().moveToFief(briefFief.getId(), true, true);
		} else {
			ctr.alert("未匹配到合适的对手，请稍后再试");
		}
	}

}
