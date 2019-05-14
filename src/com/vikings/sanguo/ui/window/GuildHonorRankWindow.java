package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.HonorRankData;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.protos.HonorRankInfo;
import com.vikings.sanguo.protos.HonorRankType;
import com.vikings.sanguo.protos.MsgRspHonorRankInfo;
import com.vikings.sanguo.ui.adapter.GuildHonorRankAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GuildHonorRankWindow extends HonorRankWindow {
	private BriefGuildInfoClient guild;

	@Override
	protected String getTitle() {
		return "家族之光";
	}

	@Override
	protected String getHonorRule() {
		return CacheMgr.uiTextCache.getTxt(UITextProp.FAMILY_HONOR_RANK);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new GuildHonorRankAdapter();
	}

	@Override
	protected void setTopDesc(MsgRspHonorRankInfo rsp) {
		StringBuilder buf = new StringBuilder();
		if (rsp.hasSelfInfo())
			buf.append("你的家族昨日击杀").append(rsp.getSelfInfo().getRankData())
					.append("名敌军,");
		else {
			if (null == guild)
				buf.append("你还没有家族,");
			else
				buf.append("你的家族昨日没有击杀敌军,");
		}

		if (hasSelfPos())
			buf.append("排行第").append(getSelfPos()).append("名");
		else {
			if (null == guild)
				buf.append("请先加入家族");
			else
				buf.append("名落孙山继续加油吧！");
		}

		ViewUtil.setRichText(findViewById(R.id.reward_spec),
				StringUtil.color(buf.toString(), R.color.color11));

	}

	@Override
	protected HonorRankType getHonorRankType() {
		return HonorRankType.HONOR_RANK_GUILD;
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		getHonorRankInfo(resultPage);
		saveHonorRankReward();

		if (hasReward(rsp) && null == bGuildInfoClient) {
			bGuildInfoClient = CacheMgr.bgicCache.get(rsp.getSelfInfo()
					.getGuildid());
		}

		if (rsp.hasInfos()) {
			List<BriefGuildInfoClient> guilds = getGuilds();
			List<BriefUserInfoClient> users = getUsers(guilds);
			setHonorRankData(resultPage, users, guilds);
		}
	}

	protected List<BriefUserInfoClient> getUsers(
			List<BriefGuildInfoClient> guilds) throws GameException {
		List<Integer> userIds = new ArrayList<Integer>();
		for (BriefGuildInfoClient it : guilds) {
			if (!userIds.contains(it.getLeader()))
				userIds.add(it.getLeader());
		}

		List<BriefUserInfoClient> users = CacheMgr.userCache.get(userIds);
		return users;
	}

	protected List<BriefGuildInfoClient> getGuilds() throws GameException {
		List<Integer> guildIds = new ArrayList<Integer>();
		for (HonorRankInfo it : rsp.getInfosList()) {
			if (!guildIds.contains(it.getGuildid()))
				guildIds.add(it.getGuildid());
		}

		List<BriefGuildInfoClient> guilds = CacheMgr.bgicCache.get(guildIds);
		return guilds;
	}

	protected void setUserAndGuild(List<BriefUserInfoClient> users,
			List<BriefGuildInfoClient> guilds, HonorRankInfo it,
			HonorRankData hrd) {
		hrd.setGuild(ListUtil.getGuild(guilds, it.getGuildid()));
		hrd.setUser(ListUtil.getUser(users, hrd.getGuild().getLeader()));

	}

	@Override
	protected void getHonorRankInfo(ResultPage resultPage) throws GameException {
		guildId = (HonorRankType.HONOR_RANK_GUILD == getHonorRankType()) ? Account.user
				.getGuildId() : 0;
		super.getHonorRankInfo(resultPage);

		if (guildId > 0)
			guild = CacheMgr.bgicCache.get(guildId);
		else
			guild = null;
	}

	@Override
	protected boolean hasReward(MsgRspHonorRankInfo rsp) {
		return super.hasReward(rsp) && null != guild && guild.isMeLeader();
	}

	@Override
	protected boolean needSave() {
		return super.needSave() && null != guild && guild.isMeLeader();
	}
}
