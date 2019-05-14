package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.HotUserAttrScoreInfoQueryResp;
import com.vikings.sanguo.model.FatSheepData;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.FatSheepFiefAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.PageListView;

public class FatSheepTip extends PageListView {

	public FatSheepTip() {
		super();
		setTitle();
		firstPage();
	}

	public void setTitle() {
		setTitle("推荐对手");
		setContentTitle("为你推荐了以下对手");
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new FatSheepFiefAdapter(new CallBack() {
			@Override
			public void onCall() {
				dismiss();
			}
		});
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		HotUserAttrScoreInfoQueryResp resp = GameBiz.getInstance()
				.hotUserAttrScoreInfoQuery(Account.user.getLevel(),
						Account.user.getCountry(), resultPage, 1);
		List<FatSheepData> contentDatas = new ArrayList<FatSheepData>();
		if (null != adapter)
			contentDatas.addAll(adapter.getContent());
		List<FatSheepData> fetchDatas = resp.getDatas();
		// 服务器的肥羊由于翻页会出现重复，客户端过滤
		List<FatSheepData> datas = filter(contentDatas, fetchDatas);
		resultPage.setResult(datas);
		int total = resp.getTotal();
		// 当次取到的数据不满一页，说明已经取到了最后； 当已经取到的数据大于服务器给的total时，不再继续取
		if (fetchDatas.size() < resultPage.getPageSize()
				|| (contentDatas.size() + datas.size() > total)) {
			total = contentDatas.size() + datas.size();
		}
		resultPage.setTotal(total);
	}

	private List<FatSheepData> filter(List<FatSheepData> contentDatas,
			List<FatSheepData> fatSheepDatas) {
		List<FatSheepData> datas = new ArrayList<FatSheepData>();
		for (FatSheepData data : fatSheepDatas) {
			if (!contentDatas.contains(data)) {
				datas.add(data);
			}
		}
		return datas;
	}

	@Override
	public void handleItem(Object o) {

	}
}
