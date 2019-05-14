package com.vikings.sanguo.ui.window;

import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.UserQuery;

public class SearchCountryUserResultWindow extends SearchResultWindow {

	private UserQuery userQuery;

	public SearchCountryUserResultWindow(int country) {
		this.userQuery = new UserQuery();
		userQuery.setCountry(country);
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<BriefUserInfoClient> users = GameBiz.getInstance().userSerarch(
				resultPage, userQuery.getConditionNumList(),
				userQuery.getConditionStrList());
		List<GuildUserData> datas = Account.packGuildUserDatas(users);
		resultPage.setResult(datas);
	}

}
