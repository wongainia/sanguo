package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.GuildLevelUpResp;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.model.GuildSearchInfoClient;
import com.vikings.sanguo.model.OtherRichGuildInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.protos.GuildSearchInfo;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.GuildJoinConfirmTip;
import com.vikings.sanguo.ui.alert.MsgConfirm;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomPopupWindow;
import com.vikings.sanguo.widget.GuildDetailTopInfo;

public class GuildInfoWindow extends CustomPopupWindow implements
		OnClickListener {
	private static final int TYPE_OTHER_GUILD = 1;
	private static final int TYPE_SELF_GUILD_MEMBER = 2;
	private static final int TYPE_SELF_GUILD_LEADER = 3;
	private static final int TYPE_SELF_GUILD_ELDER = 4;

	private View chatLayout, memberLayout, logLayout, quitLayout,
			editInfoLayout, announcementLayout, connectLeaderLayout,
			applyLayout, joinListLayout, upgradeLayout; // ,
														// altarsLayout;
	private TextView desc, announcement, state;
	private int type;
	private int guildid;
	private RichGuildInfoClient richGuildInfo;
	private OtherRichGuildInfoClient otherRichGuildInfo;
	private BriefUserInfoClient leader;
	private GuildProp guildProp;
	private GuildProp nextLVGuildProp;
	private GuildProp preLvGuildProp;

	@Override
	protected void init() {
		super.init("家族信息");
		setContent(R.layout.guild_info);

		chatLayout = window.findViewById(R.id.chatLayout);
		chatLayout.setOnClickListener(this);
		memberLayout = window.findViewById(R.id.memberLayout);
		memberLayout.setOnClickListener(this);
		logLayout = window.findViewById(R.id.logLayout);
		logLayout.setOnClickListener(this);
		quitLayout = window.findViewById(R.id.quitLayout);
		quitLayout.setOnClickListener(this);
		editInfoLayout = window.findViewById(R.id.editInfoLayout);
		editInfoLayout.setOnClickListener(this);
		announcementLayout = (ViewGroup) window
				.findViewById(R.id.announcementLayout);
		connectLeaderLayout = window.findViewById(R.id.connectLeaderLayout);
		connectLeaderLayout.setOnClickListener(this);
		applyLayout = window.findViewById(R.id.applyLayout);
		applyLayout.setOnClickListener(this);
		joinListLayout = window.findViewById(R.id.joinListLayout);
		joinListLayout.setOnClickListener(this);
		// altarsLayout = (ViewGroup) findViewById(R.id.altarsLayout);
		// altarsLayout.setOnClickListener(this);

		// 升级家族
		upgradeLayout = window.findViewById(R.id.upgradeLayout);
		upgradeLayout.setOnClickListener(this);

		desc = (TextView) window.findViewById(R.id.desc);
		announcement = (TextView) window.findViewById(R.id.announcement);

		state = (TextView) window.findViewById(R.id.state);
	}

	private void setValue() {
		if (type == TYPE_OTHER_GUILD) {
			ViewUtil.setGone(chatLayout);
			ViewUtil.setVisible(memberLayout);
			ViewUtil.setGone(quitLayout);
			ViewUtil.setGone(logLayout);
			ViewUtil.setGone(editInfoLayout);
			ViewUtil.setGone(joinListLayout);
			ViewUtil.setGone(upgradeLayout);
			if (Account.user.hasCountry()
					&& leader.hasCountry()
					&& Account.user.getCountry().intValue() == leader
							.getCountry().intValue()) {
				ViewUtil.setVisible(connectLeaderLayout);
				if (!Account.user.hasGuild())
					ViewUtil.setVisible(applyLayout);
				else
					ViewUtil.setGone(applyLayout);
			} else {
				ViewUtil.setGone(connectLeaderLayout);
				ViewUtil.setGone(applyLayout);
			}
		} else if (type == TYPE_SELF_GUILD_MEMBER) {
			ViewUtil.setVisible(chatLayout);
			ViewUtil.setVisible(memberLayout);
			ViewUtil.setVisible(quitLayout);
			ViewUtil.setGone(logLayout);
			ViewUtil.setGone(editInfoLayout);
			ViewUtil.setGone(connectLeaderLayout);
			ViewUtil.setGone(applyLayout);
			ViewUtil.setGone(joinListLayout);
			ViewUtil.setGone(upgradeLayout);
		} else if (type == TYPE_SELF_GUILD_LEADER) {
			ViewUtil.setVisible(chatLayout);
			ViewUtil.setVisible(memberLayout);
			ViewUtil.setGone(quitLayout);
			ViewUtil.setVisible(logLayout);
			ViewUtil.setVisible(editInfoLayout);
			ViewUtil.setGone(connectLeaderLayout);
			ViewUtil.setGone(applyLayout);
			ViewUtil.setVisible(joinListLayout);

			if (richGuildInfo.canUpgrade()) {
				ViewUtil.setVisible(upgradeLayout);
			} else {
				ViewUtil.setGone(upgradeLayout);
			}

		} else if (type == TYPE_SELF_GUILD_ELDER) {
			ViewUtil.setVisible(chatLayout);
			ViewUtil.setVisible(memberLayout);
			ViewUtil.setVisible(quitLayout);
			ViewUtil.setVisible(logLayout);
			ViewUtil.setVisible(editInfoLayout);
			ViewUtil.setGone(connectLeaderLayout);
			ViewUtil.setGone(applyLayout);
			ViewUtil.setVisible(joinListLayout);
			ViewUtil.setGone(upgradeLayout);
		}

		setBaseGuildInfo();
	}

	private void setBaseGuildInfo() {
		if (type == TYPE_OTHER_GUILD) {

			GuildDetailTopInfo.updateOther(otherRichGuildInfo, leader,
					guildProp,
					(ViewGroup) window.findViewById(R.id.guildDetailTopLayout));

			setGuildDesc(otherRichGuildInfo.getOgic().getDesc());
			ViewUtil.setGone(announcementLayout);

		} else if (type == TYPE_SELF_GUILD_MEMBER
				|| type == TYPE_SELF_GUILD_LEADER
				|| type == TYPE_SELF_GUILD_ELDER) {

			if (0 != richGuildInfo.getJoinAndInviteCount()
					&& joinListLayout.getVisibility() == View.VISIBLE) { // 有未读消息
				ViewUtil.setVisible(state);
				ViewUtil.setText(state,
						"新消息 " + richGuildInfo.getJoinAndInviteCount());
			} else {
				ViewUtil.setGone(state);
			}
			GuildDetailTopInfo.updateOwner(richGuildInfo,
					Account.myGuildLeader, guildProp,
					(ViewGroup) window.findViewById(R.id.guildDetailTopLayout));

			setGuildDesc(richGuildInfo.getGic().getDesc());
			setGuildAnnounce();

		}
	}

	private void setGuildAnnounce() {
		String announce = richGuildInfo.getGic().getAnnouncement();
		ViewUtil.setVisible(announcementLayout);
		if (StringUtil.isNull(announce)) {
			ViewUtil.setText(announcement, "族长尚未编写家族公告");
		} else {
			ViewUtil.setText(announcement,
					StringUtil.RemoveAdditionalChar(announce));
		}
	}

	private void setGuildDesc(String str) {
		if (StringUtil.isNull(str)) {
			ViewUtil.setText(desc, "族长尚未编写家族宗旨");
		} else {
			ViewUtil.setText(desc, StringUtil.RemoveAdditionalChar(str));
		}
	}

	public void open(int guilid) {
		this.guildid = guilid;
		new FetchInvoker().start();
	}

	private class FetchInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "正在加载数据!";
		}

		@Override
		protected String failMsg() {
			return "获取数据失败";
		}

		@Override
		protected void fire() throws GameException {
			if (Account.guildCache.getGuildid() == guildid) {
				if (null == richGuildInfo) {
					Account.guildCache.updata(true);
					richGuildInfo = Account.guildCache.getRichGuildInfoClient();
					leader = CacheMgr.userCache.get(richGuildInfo.getGic()
							.getLeader());
					Account.myGuildLeader = leader;

					guildProp = (GuildProp) CacheMgr.guildPropCache
							.get(richGuildInfo.getGic().getLevel());

					if (guildProp.getLevel() == 1) {
						nextLVGuildProp = (GuildProp) CacheMgr.guildPropCache
								.get(richGuildInfo.getGic().getLevel() + 1);
					}
				}

			} else {
				if (null == otherRichGuildInfo) {
					otherRichGuildInfo = GameBiz.getInstance()
							.otherRichGuildInfoClient(guildid);
					leader = CacheMgr.userCache.get(otherRichGuildInfo
							.getOgic().getLeader());
					guildProp = (GuildProp) CacheMgr.guildPropCache
							.get(otherRichGuildInfo.getOgic().getLevel());

				}
			}

		}

		@Override
		protected void onOK() {
			doOpen();
		}

	}

	@Override
	public void showUI() {
		setType();
		setValue();
		super.showUI();
	}

	private void setType() {
		if (Account.guildCache.getGuildid() == guildid) {
			RichGuildInfoClient rgic = Account.guildCache.getRichInfoInCache();
			if (null != rgic) {
				if (rgic.isLeader(Account.user.getId()))
					type = TYPE_SELF_GUILD_LEADER;
				else if (rgic.isElder(Account.user.getId()))
					type = TYPE_SELF_GUILD_ELDER;
				else
					type = TYPE_SELF_GUILD_MEMBER;
			} else {
				type = TYPE_SELF_GUILD_MEMBER;
			}
		} else {
			type = TYPE_OTHER_GUILD;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == chatLayout) {
			controller.openGuildChatWindow(richGuildInfo);
		} else if (v == memberLayout) {
			if (null != richGuildInfo)
				controller.openGuildUserListWindow(richGuildInfo);
			else if (null != otherRichGuildInfo)
				controller.openOtherGuildUserListWindow(otherRichGuildInfo);
		} else if (v == logLayout) {
			new GuildLogWindow().open(this.guildid);
		} else if (v == quitLayout) {
			StringBuilder builder = new StringBuilder();
			builder.append("确定要退出  ")
					.append(StringUtil.color(richGuildInfo.getGic().getName(),
							controller.getResourceColorText(R.color.color24)))
					.append("家族 吗?");
			new MsgConfirm("退出家族", CustomConfirmDialog.DEFAULT).show(
					builder.toString(), new QuitCallback(), null);
		} else if (v == editInfoLayout) {
			new GuildEditWindow().open(richGuildInfo);
		} else if (v == connectLeaderLayout) {
			controller.openChatWindow(leader);
		} else if (v == applyLayout) {
			// 申请加入家族
			GuildSearchInfo info = new GuildSearchInfo()
					.setId(otherRichGuildInfo.getOgic().getId())
					.setImage(otherRichGuildInfo.getOgic().getImage())
					.setMembers(otherRichGuildInfo.getMembers().size())
					.setName(otherRichGuildInfo.getOgic().getName())
					.setLevel(otherRichGuildInfo.getOgic().getLevel());
			GuildSearchInfoClient data = new GuildSearchInfoClient(info, leader);
			new GuildJoinConfirmTip(data).show();
		} else if (v == joinListLayout) {
			// 申请列表
			new GuildJoinListWindow().open(richGuildInfo);
		} else if (v == upgradeLayout) {
			new MsgConfirm("家族升级", CustomConfirmDialog.DEFAULT).show(
					new GuildUpgrade(), null, nextLVGuildProp);
		}
		// else if (v == altarsLayout) {
		// handleAltarsBtn();
		// }
	}

	private class QuitCallback implements CallBack {

		@Override
		public void onCall() {
			if (Account.guildCache.getGuildid() != guildid) {
				controller.alert("你已经不在该家族中,不能执行该操作");
				return;
			} else {
				new GuildQuitInvoker().start();
			}
		}

	}

	private class GuildUpgrade implements CallBack {

		@Override
		public void onCall() {
			if (leader.getCurVip().getLevel() < nextLVGuildProp.getReqMinVip()) {
				controller.alert("只有VIP" + nextLVGuildProp.getReqMinVip()
						+ "以上的玩家才能升级家族");
				return;
			}
			if (Account.user.getCurrency() < nextLVGuildProp.getRmb4LvUp()) {
				controller.alert("您的元宝不足" + nextLVGuildProp.getRmb4LvUp()
						+ "，不能进行此操作");
				return;
			}
			new GuildUpgradeInvoker().start();
		}

	}

	private class GuildUpgradeInvoker extends BaseInvoker {
		private GuildLevelUpResp resp;

		@Override
		protected String loadingMsg() {
			return "家族升级";
		}

		@Override
		protected String failMsg() {
			return "升级家族失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().guildLevelUp(guildid);
			richGuildInfo.setGic(resp.getGic());
			guildProp = (GuildProp) CacheMgr.guildPropCache.get(richGuildInfo
					.getGic().getLevel());

			preLvGuildProp = (GuildProp) CacheMgr.guildPropCache
					.get(richGuildInfo.getGic().getLevel() - 1);
		}

		@Override
		protected void onOK() {
			controller.alert("成功升级家族", "家族等级提升到" + guildProp.getLevel()
					+ "级，家族总人数上限从" + preLvGuildProp.getMaxMemberCnt() + "变为"
					+ guildProp.getMaxMemberCnt(), null, true);
			ctr.updateUI(resp.getRic(), true, false, true);
			setValue();
		}
	}

	private class GuildQuitInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "退出家族";
		}

		@Override
		protected String failMsg() {
			return "退出家族失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().guildMemberQuit(guildid);
		}

		@Override
		protected void onOK() {
			Account.guildCache.setGuildid(0);
			Account.guildCache.setRichInfo(null);
			if (null != ctr.getHeartBeat())
				ctr.getHeartBeat().updataChatIds();
			StringBuilder builder = new StringBuilder();
			controller.goBack();
			new GuildBuildWindow().open();
			builder.append("你已经不是  ")
					.append(StringUtil.color(richGuildInfo.getGic().getName(),
							ctr.getResourceColorText(R.color.color24)))
					.append(" 的一员了");
			controller.alert("成功退出家族", builder.toString(), null, true);
		}

	}
}
