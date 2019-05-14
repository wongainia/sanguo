package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HonorRankData;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.HonorRankInfo;
import com.vikings.sanguo.protos.HonorRankType;
import com.vikings.sanguo.protos.MsgRspHonorRankInfo;
import com.vikings.sanguo.ui.alert.RewardTip;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public abstract class HonorRankWindow extends CustomBaseListWindow {
	protected MsgRspHonorRankInfo rsp;
	protected int guildId = 0;
	protected BriefGuildInfoClient bGuildInfoClient;

	@Override
	protected void init() {
		init(getTitle());
		setContentBelowTitle(R.layout.honor_rank_title1);
		ViewUtil.setRichText(window, R.id.rank, getHonorRule());
		firstPage();
	}

	abstract protected String getTitle();

	abstract protected String getHonorRule();

	abstract protected HonorRankType getHonorRankType();

	abstract protected void setTopDesc(MsgRspHonorRankInfo rsp);

	private void setTopDescWrap(MsgRspHonorRankInfo rsp) {
		setTopDesc(rsp);
		ViewUtil.setVisible(findViewById(R.id.reward_spec));
	}

	protected void setTopDescSuffix(StringBuilder buf) {
		if (hasSelfPos())
			buf.append("排行第").append(getSelfPos()).append("名");
		else
			buf.append("名落孙山，继续加油吧！");
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		getHonorRankInfo(resultPage);
		saveHonorRankReward();

		if ((getHonorRankType() == HonorRankType.HONOR_RANK_GUILD_COST || getHonorRankType() == HonorRankType.HONOR_RANK_GUILD)
				&& hasReward(rsp) && null == bGuildInfoClient) {
			bGuildInfoClient = CacheMgr.bgicCache.get(rsp.getSelfInfo()
					.getGuildid());
		}

		if (rsp.hasInfos()) {
			List<BriefUserInfoClient> users = getUsers(rsp);
			List<BriefGuildInfoClient> guilds = getGuilds(users);
			setHonorRankData(resultPage, users, guilds);
		}
	}

	protected void getHonorRankInfo(ResultPage resultPage) throws GameException {
		if (Account.user.hasGuild()) {
			guildId = Account.user.getGuildId();
		}
		rsp = GameBiz.getInstance().getHonorRankInfo(getHonorRankType(),
				resultPage, guildId);
		// 家族之光,威震江湖 需要通过家族IDs 找到族长信息
		if (getHonorRankType() == HonorRankType.HONOR_RANK_GUILD_COST
				|| getHonorRankType() == HonorRankType.HONOR_RANK_GUILD) {
			getBgicsByIds(rsp);
		}
		resultPage.setTotal(rsp.getTotal());
	}

	protected void saveHonorRankReward() {
		if (needSave()) {
			controller.getFileAccess().saveHonorRankReward(
					getHonorRankType().number,
					rsp.getSelfInfo().getReceiveAward());
		}
	}

	protected boolean needSave() {
		return rsp.hasSelfInfo() && hasSelfPos();
	}

	protected void setHonorRankData(ResultPage resultPage,
			List<BriefUserInfoClient> users, List<BriefGuildInfoClient> guilds)
			throws GameException {
		List<HonorRankData> data = new ArrayList<HonorRankData>();

		for (HonorRankInfo it : rsp.getInfosList()) {
			HonorRankData hrd = new HonorRankData();
			setUserAndGuild(users, guilds, it, hrd);
			hrd.setHonorRank(it);
			hrd.setItem(CacheMgr.honorRankCache.getItem(
					getHonorRankType().number, adapter.getCount()
							+ rsp.getInfosList().indexOf(it) + 1));
			setHeroInfo(it, hrd);

			data.add(hrd);
		}
		resultPage.setResult(data);
	}

	protected void setUserAndGuild(List<BriefUserInfoClient> users,
			List<BriefGuildInfoClient> guilds, HonorRankInfo it,
			HonorRankData hrd) {
		hrd.setUser(ListUtil.getUser(users, it.getUserid()));
		hrd.setGuild(ListUtil.getGuild(guilds, hrd.getUser().getGuildid()
				.intValue()));
	}

	@Override
	protected void updateUI() {
		if (0 == resultPage.getCurIndex()) {
			setTopDescWrap(rsp);
			setReward(rsp);
		}
		super.updateUI();
		dealwithEmptyAdpter();
	}

	@Override
	protected String getEmptyShowText() {
		return "暂无排行相关信息！";
	}

	protected void setHeroInfo(HonorRankInfo it, HonorRankData hrd)
			throws GameException {
		if (it.hasHeroId() && it.hasHeroPropId()) {
			hrd.setHeroId(it.getHeroId());
			hrd.setHeroProp((HeroProp) CacheMgr.heroPropCache.get(it
					.getHeroPropId()));
		}
	}

	protected List<BriefUserInfoClient> getUsers(MsgRspHonorRankInfo rsp)
			throws GameException {
		List<Integer> userIds = new ArrayList<Integer>();
		for (HonorRankInfo it : rsp.getInfosList()) {
			if (it.getUserid() > 0 && !userIds.contains(it.getUserid()))
				userIds.add(it.getUserid());
		}

		return CacheMgr.userCache.get(userIds);
	}

	protected List<BriefGuildInfoClient> getGuilds(
			List<BriefUserInfoClient> users) throws GameException {
		List<Integer> guildIds = new ArrayList<Integer>();
		for (BriefUserInfoClient it : users) {
			if (it.getGuildid() > 0 && !guildIds.contains(it.getGuildid()))
				guildIds.add(it.getGuildid());
		}

		return CacheMgr.bgicCache.get(guildIds);
	}

	protected void getBgicsByIds(MsgRspHonorRankInfo rsp) throws GameException {
		List<HonorRankInfo> infos = rsp.getInfosList();
		if (ListUtil.isNull(infos)) {
			return;
		}
		List<Integer> guildIds = new ArrayList<Integer>();
		for (HonorRankInfo hrinfo : infos) {
			if (hrinfo.getGuildid() > 0
					&& !guildIds.contains(hrinfo.getGuildid())) {
				guildIds.add(hrinfo.getGuildid());
			}
		}
		List<BriefGuildInfoClient> bgics = CacheMgr.bgicCache.get(guildIds);

		for (HonorRankInfo honorRankInfo : infos) {
			for (BriefGuildInfoClient briefGuildInfoClient : bgics) {
				if (honorRankInfo.getGuildid() == briefGuildInfoClient.getId()) {
					honorRankInfo.setUserid(briefGuildInfoClient.getLeader());
					break;
				}
			}
		}

	}

	@Override
	public void handleItem(Object o) {

	}

	protected void setReward(MsgRspHonorRankInfo rsp) {
		if (hasReward(rsp)) {
			if ((getHonorRankType() == HonorRankType.HONOR_RANK_GUILD_COST || getHonorRankType() == HonorRankType.HONOR_RANK_GUILD)) {
				ViewUtil.setRichText(findViewById(R.id.reward_spec), StringUtil
						.color("恭喜你的家族登上荣耀榜第" + getSelfPos() + "名!",
								R.color.color6));
			} else {
				ViewUtil.setRichText(findViewById(R.id.reward_spec), StringUtil
						.color("恭喜你登上荣耀榜第" + getSelfPos() + "名!",
								R.color.color6));
			}

			ViewUtil.setVisible(window, R.id.awardBtn);
			ViewUtil.setVisible(window, R.id.award_propmt);

			if ((getHonorRankType() == HonorRankType.HONOR_RANK_GUILD_COST || getHonorRankType() == HonorRankType.HONOR_RANK_GUILD)
					&& null != bGuildInfoClient
					&& Account.user.getId() != bGuildInfoClient.getLeader()) {
				ViewUtil.setGone(window, R.id.awardBtn);
				ViewUtil.setGone(window, R.id.award_propmt);
			}

			// Item reward = getOwnRewardItem(rsp);
			// if (null != reward)
			// ViewUtil.setText(window, R.id.reward, "奖励" + reward.getName()
			// + "1个");
			// else
			// ViewUtil.setGone(window, R.id.reward);

			View award = findViewById(R.id.awardBtn);
			award.setTag(rsp);
			award.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (getHonorRankType() == HonorRankType.HONOR_RANK_GUILD_COST
							|| getHonorRankType() == HonorRankType.HONOR_RANK_GUILD) {
						if (bGuildInfoClient == null
								|| !bGuildInfoClient.isLeader(Account.user.getId())) {
							Config.getController().alert("只有家族族长才可领奖！");
							return;
						}
					}
					new GetHonorRankRewardInvoker().start();
				}
			});
		}
	}

	protected int getSelfPos() {
		if (null == rsp)
			return 0;
		return rsp.getSelfPos() + 1;
	}

	protected Item getOwnRewardItem(MsgRspHonorRankInfo rsp) {
		if (hasReward(rsp)) {
			return CacheMgr.honorRankCache.getItem(getHonorRankType().number,
					getSelfPos());
		} else
			return null;
	}

	protected boolean hasReward(MsgRspHonorRankInfo rsp) {
		return hasSelfPos() && rsp.hasSelfInfo()
				&& !rsp.getSelfInfo().getReceiveAward();
	}

	protected boolean hasSelfPos() {
		return rsp.hasSelfPos()
				&& !CacheMgr.honorRankCache.isGtMaxRewardPos(
						getHonorRankType().number, getSelfPos());
	}

	class GetHonorRankRewardInvoker extends BaseInvoker {

		private ReturnInfoClient ric;

		@Override
		protected String loadingMsg() {
			return "领取奖励中";
		}

		@Override
		protected String failMsg() {
			return "领奖失败";
		}

		@Override
		protected void fire() throws GameException {
			ric = GameBiz.getInstance().getHonorRankReward(
					getHonorRankType().number);
		}

		@Override
		protected void onOK() {
			ViewUtil.setGone(window, R.id.awardBtn);
			ViewUtil.setGone(window, R.id.award_propmt);
			setTopDescWrap(rsp);
			controller.getFileAccess().saveHonorRankReward(
					getHonorRankType().number, true);
			Account.honorRankRewardInit();
			new RewardTip(getTitle() + "奖励", ric.showRequire(true)).show();
		}
	}
}
