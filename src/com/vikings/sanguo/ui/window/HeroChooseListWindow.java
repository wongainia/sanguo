package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.view.View;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroInit;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.CallBackAppear;
import com.vikings.sanguo.ui.adapter.HeroChooseAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class HeroChooseListWindow extends CustomBaseListWindow {
	private HeroInfoClient[] hics;
	private int index;
	private byte minHeroTalent;
	private BriefFiefInfoClient briefFief;
	private CallBack mCallBack;
	public static CallBack guideCallBack;
	// 引导专用 添加将领
	public static boolean isGuide = false;
	public static HeroInfoClient guideChooseHeroInfoClient = null;
	
	//
	private CallBackAppear mCallBackAppear;

	@Override
	protected void init() {
		adapter = new HeroChooseAdapter(hics[index], mCallBack,mCallBackAppear);
		super.init("将领列表");
		setContentBelowTitle(R.layout.gradient_msg);
		TextView textView = (TextView) window.findViewById(R.id.gradientMsg);
		textView.setTextSize(14);
		ViewUtil.setText(textView, "请选择要上场的将领");
	}

//	public void open(HeroInfoClient[] hics, int index,
//			BriefFiefInfoClient briefFief, byte minHeroTalent) {
//		this.open(hics, index, briefFief, minHeroTalent, null);
//	}
//
	public void open(HeroInfoClient[] hics, int index,
			BriefFiefInfoClient briefFief, byte minHeroTalent,
			CallBack mCallBack) {
		this.mCallBack = mCallBack;
		this.hics = hics;
		this.index = index;
		this.briefFief = briefFief;
		this.minHeroTalent = minHeroTalent;
		this.doOpen();
		firstPage();
	}
	
	public void open(HeroInfoClient[] hics, int index,
			BriefFiefInfoClient briefFief, byte minHeroTalent,CallBackAppear callBackAppear) {
		this.open(hics, index, briefFief, minHeroTalent, null,callBackAppear);
	}

	public void open(HeroInfoClient[] hics, int index,
			BriefFiefInfoClient briefFief, byte minHeroTalent,
			CallBack mCallBack,CallBackAppear callBackAppear) {
		this.mCallBack = mCallBack;
		this.mCallBackAppear = callBackAppear;
		this.hics = hics;
		this.index = index;
		this.briefFief = briefFief;
		this.minHeroTalent = minHeroTalent;
		this.doOpen();
		firstPage();
	}
	
	@Override
	protected void updateUI() {
		super.updateUI();
		dealwithEmptyAdpter();
	}

	@Override
	protected String getEmptyShowText() {
		return "没有合适将领";
	}

	public List<HeroInfoClient> filterHeroinfos(List<HeroInfoClient> heroInfos) {
		List<HeroInfoClient> list = new ArrayList<HeroInfoClient>();
		HeroInfoClient selectHero = null;
		// 排除已经选中的将领
		for (HeroInfoClient hic : heroInfos) {
			if (!contains(hic) && hic.getTalent() >= minHeroTalent)
				list.add(hic);
			if (hics[index].isValid() && hic.getId() == hics[index].getId()) {
				selectHero = hic;
			}
		}
		Collections.sort(list);
		if (null != selectHero)
			list.add(0, selectHero);
		return list;
	}

	private boolean contains(HeroInfoClient hic) {
		for (int i = 0; i < hics.length; i++) {
			if (hics[i] != null && hics[i].isValid()
					&& hics[i].getId() == hic.getId())
				return true;
		}
		return false;
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void destory() {
		controller.removeContentFullScreen(window);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<HeroInfoClient> heros = new ArrayList<HeroInfoClient>();
		if (null != briefFief) { // 从领地取将领
			heros.addAll(Account.heroInfoCache.getHeroInFief(briefFief.getId()));
		} else { // 取全部将领
			heros.addAll(Account.heroInfoCache.get());
		}
		heros = filterHeroinfos(heros);

		// 引导专用配置
		if (isGuide) {
			List<HeroInfoClient> hicClients = new ArrayList<HeroInfoClient>();
			HeroInit mHeroInit = CacheMgr.trainingRewardsCache
					.getHeroInit(BaseStep.INDEX_STEP201);
			for (int i = 0; i < heros.size(); i++) {
				if (mHeroInit != null
						&& heros.get(i).getHeroId() == mHeroInit.getHeroId()) {
					// 取得 step205得到的英雄 放列表第一位
					hicClients.add(0, heros.get(i));
					guideChooseHeroInfoClient = heros.get(i);
				} else {
					hicClients.add(heros.get(i));
				}
			}
			heros = hicClients;
		}
		resultPage.setResult(heros);
		resultPage.setTotal(heros.size());
	}

	@Override
	public void handleItem(Object o) {

	}

}
