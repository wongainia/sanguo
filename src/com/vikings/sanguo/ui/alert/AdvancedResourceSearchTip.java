/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-28 下午4:05:28
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.AdvancedResource;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.SearchFiefAdapter;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.PageListView;

public class AdvancedResourceSearchTip extends PageListView {
	private AdvancedResource advancedRes;
	private int propIdx;
	private int start;

	public AdvancedResourceSearchTip(AdvancedResource advancedRes) {
		super();
		this.advancedRes = advancedRes;
		setTitle();
		firstPage();
		initHeader();
	}

	public void setTitle() {
		setTitle(advancedRes.getName());
		setContentTitle("请选择" + advancedRes.getName());
	}

	protected void initHeader() {
		View header = controller.inflate(R.layout.res_item);
		ViewUtil.setVisible(header, R.id.arrow);
		ViewUtil.setBtnMirrorBt(header.findViewById(R.id.arrow), "arrow");
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				new AdvancedRessourceNameTip().show();
			}
		});
		ViewUtil.setText(header, R.id.province, "返回高级资源点列表");
		addHeader(header);

	}

	@Override
	protected ObjectAdapter getAdapter() {
		SearchFiefAdapter adapter = new SearchFiefAdapter();
		adapter.setType(Constants.TOWN);
		return adapter;
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<Integer> propIds = advancedRes.getPropIds();
		if (ListUtil.isNull(propIds) || (propIdx >= propIds.size()))
			return;

		int pageSize = Config.getIntConfig("resultPageSize");
		List<Long> ids = GameBiz.getInstance().advancedSiteQuery(
				propIds.get(propIdx), start, pageSize);
		if (0 == ids.size()) {
			propIdx++;
			start = 0;

			if (propIdx < propIds.size())
				ids = GameBiz.getInstance().advancedSiteQuery(
						propIds.get(propIdx), start, pageSize);
		} else if (ids.size() < pageSize) {
			start = 0;
			propIdx++;
		} else
			start += pageSize;

		if (!ListUtil.isNull(ids)) {
			List<BriefFiefInfoClient> fiefs = GameBiz.getInstance()
					.briefFiefInfoQuery(ids);
			resultPage.setResult(fiefs);
			resultPage.setTotal(resultPage.getTotal() + fiefs.size());
		}
	}

	@Override
	public void handleItem(Object o) {
		dismiss();
		BriefFiefInfoClient bfic = (BriefFiefInfoClient) o;
		Config.getController().getBattleMap()
				.moveToFief(bfic.getId(), true, true);
	}

	@Override
	protected void updateUI() {
		super.updateUI();
		dealwithEmptyAdpter();
	}

	@Override
	protected String getEmptyShowText() {
		return "暂时没有高级资源点";
	}
}
