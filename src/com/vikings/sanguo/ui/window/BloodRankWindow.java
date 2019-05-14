package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.BloodRankQueryResp;
import com.vikings.sanguo.message.BloodRankRewardResp;
import com.vikings.sanguo.model.BloodRankInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.adapter.BloodRankAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class BloodRankWindow extends CustomBaseListWindow implements
		OnClickListener {

	private View selfRankLayout, awardBtn;
	private TextView gradientMsg, rank, reward;

	@Override
	protected void init() {
		super.init("血战荣耀");
		setContentBelowTitle(R.layout.honor_rank_title);
		setCommonBg(R.drawable.blood_war_bg);
		selfRankLayout = window.findViewById(R.id.selfRankLayout);
		gradientMsg = (TextView) window.findViewById(R.id.gradientMsg);
		rank = (TextView) selfRankLayout.findViewById(R.id.rank);
		reward = (TextView) selfRankLayout.findViewById(R.id.reward);
		awardBtn = selfRankLayout.findViewById(R.id.awardBtn);
		awardBtn.setOnClickListener(this);
		setLeftBtn("规则说明", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RuleSpecTip("血战规则说明", CacheMgr.uiTextCache
						.getTxt(UITextProp.BLOOD_RULER)).show();
			}
		});
	}

	public void open() {
		this.doOpen();
		firstPage();
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<BloodRankInfoClient> list = new ArrayList<BloodRankInfoClient>();
		if (0 == resultPage.getCurIndex()) {
			BloodRankQueryResp resp = GameBiz.getInstance().bloodRankQuery(
					resultPage, true);
			list.addAll(resp.getInfos());
			Account.myLordInfo.setSelfBloodRankInfo(resp.getSelfInfo());
		} else {
			BloodRankQueryResp resp = GameBiz.getInstance().bloodRankQuery(
					resultPage, false);
			list.addAll(resp.getInfos());
		}
		// 添加家族信息
		List<Integer> guildIds = new ArrayList<Integer>();
		for (BloodRankInfoClient bric : list) {
			if (bric.hasGuild() && guildIds.contains(bric.getGuildId())) {
				guildIds.add(bric.getGuildId());
			}
		}

		if (!guildIds.isEmpty()) {
			List<BriefGuildInfoClient> bgics = CacheMgr
					.getBriefGuildInfoClient(guildIds);
			for (BloodRankInfoClient bric : list) {
				if (bric.hasGuild()) {
					for (BriefGuildInfoClient bgic : bgics) {
						if (bgic.getId() == bric.getGuildId()) {
							bric.setBgic(bgic);
							break;
						}
					}
				}
			}
		}

		if (list.size() < resultPage.getPageSize()) {
			if (adapter != null)
				resultPage.setTotal(adapter.getCount() + list.size());
			else
				resultPage.setTotal(list.size());
		} else {
			resultPage.setTotal(Integer.MAX_VALUE);
		}
		resultPage.setResult(list);
	}

	@Override
	protected void updateUI() {
		if (0 == resultPage.getCurIndex()) {
			setSelfRankInfo();
		}
		super.updateUI();
	}

	private void setSelfRankInfo() {
		BloodRankInfoClient selfBric = Account.myLordInfo
				.getSelfBloodRankInfo();
		if (null != selfBric) {
			if (selfBric.hasRank()) {
				ViewUtil.setText(gradientMsg,
						"昨日你血战" + selfBric.getBestRecord() + "关，榜上有名");
				ViewUtil.setVisible(selfRankLayout);
				ViewUtil.setText(rank, "你以傲人的战绩位列第 " + selfBric.getRank()
						+ " 名");
				if (selfBric.canReward()) {
					ViewUtil.setRichText(reward, "奖励:"
							+ selfBric.getRewardItem().getName());
					ViewUtil.setVisible(awardBtn);
				} else {
					ViewUtil.setGone(awardBtn);
				}

			} else {
				ViewUtil.setText(gradientMsg,
						"昨日你血战" + selfBric.getBestRecord() + "关，名落孙山");
				ViewUtil.setGone(selfRankLayout);
			}

		} else {
			ViewUtil.setText(gradientMsg, "昨日你未参加血战");
			ViewUtil.setGone(selfRankLayout);
		}
	}

	@Override
	public void handleItem(Object o) {

	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new BloodRankAdapter();
	}

	@Override
	public void onClick(View v) {
		if (v == awardBtn) {
			new AwardInvoker().start();
		}
	}

	private class AwardInvoker extends BaseInvoker {
		private BloodRankRewardResp resp;

		@Override
		protected String loadingMsg() {
			return "领奖";
		}

		@Override
		protected String failMsg() {
			return "领奖失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().bloodRankReward();
			if (null != Account.myLordInfo.getSelfBloodRankInfo())
				Account.myLordInfo.getSelfBloodRankInfo().award();
		}

		@Override
		protected void onOK() {
			setSelfRankInfo();
			resp.getRic().setMsg("领奖成功");
			ctr.updateUI(resp.getRic(), true);
		}
	}

}
