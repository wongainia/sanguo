package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.UserCache;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildInviteInfoClient;
import com.vikings.sanguo.model.GuildJoinInfoClient;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.model.UserTimeData;
import com.vikings.sanguo.ui.adapter.GuildJoinAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.alert.InviteUserTip;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class GuildJoinListWindow extends CustomBaseListWindow {
	private RichGuildInfoClient rgic;
	private List<UserTimeData> userTimeDatas;
	private GuildInviteInfoClient temp; // 用于邀请后刷新界面

	@Override
	public void init() {
		adapter = new GuildJoinAdapter(rgic);
		super.init("管理申请");
		if (rgic.isLeader(Account.user.getId()) || rgic.isElder(Account.user.getId()))
			setLeftBtn("设置", new OnClickListener() {
				@Override
				public void onClick(View v) {
					new GuildSettingWindow().open(rgic);
				}
			});
		setContentBelowTitle(R.layout.list_title);
		temp = new GuildInviteInfoClient();
		setBottomButton("邀请玩家", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new InviteUserTip(rgic, temp).show();
			}
		});
	}
	
	

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}



	public void open(RichGuildInfoClient rgic) {
		if (Account.guildCache.getGuildid() != rgic.getGuildid()) {
			controller.alert("你不是该家族族长,不能管理申请列表");
			return;
		}
		this.rgic = rgic;
		doOpen();
		firstPage();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void showUI() {
		setTitle();
		checkData();
		super.showUI();
	}

	@SuppressWarnings("unchecked")
	private void checkData() {
		if (null == adapter || null == resultPage || null == userTimeDatas)
			return;
		List<UserTimeData> list = new ArrayList<UserTimeData>();
		list.addAll(adapter.getContent());
		for (int i = 0; i < list.size(); i++) {
			UserTimeData data = list.get(i);
			if (data.isApprove()) { // 如果已经处理过
				if (data.getType() == UserTimeData.TYPE_GUILD_INVITE) {
					GuildInviteInfoClient info = (GuildInviteInfoClient) data;
					if (rgic.getGiics().contains(info)) {
						rgic.removeInviteInfo(info.getUserId());
					}
				} else if (data.getType() == UserTimeData.TYPE_GUILD_JOIN) {
					GuildJoinInfoClient info = (GuildJoinInfoClient) data;
					if (rgic.getGjics().contains(info)) {
						rgic.removeJoinInfo(info.getUserId());
					}
				}
				adapter.removeItem(data);
				resultPage.setCurIndex(resultPage.getCurIndex() - 1);
				userTimeDatas.remove(data);
			}
		}
		// 如果有新邀请，刷新界面
		if (!temp.isApprove() && temp.getUserId() > 0 && temp.getTime() > 0
				&& temp.getBriefUser() != null) {
			GuildInviteInfoClient giic = temp.copy();
			adapter.insertItemAtHead(giic);
			userTimeDatas.add(0, giic);
			temp.clear();
		}
		resultPage.setCurIndex(adapter.getContent().size());
		resultPage.setTotal(userTimeDatas.size());

	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		if (null == userTimeDatas) {
			userTimeDatas = new ArrayList<UserTimeData>();
			userTimeDatas.addAll(rgic.getGjics());
			userTimeDatas.addAll(rgic.getGiics());
			sortData();
		}

		int start = resultPage.getCurIndex();
		int end = start + resultPage.getPageSize();
		if (end > userTimeDatas.size())
			end = userTimeDatas.size();
		if (start > end) {
			resultPage.setResult(new ArrayList<Integer>());
			resultPage.setTotal(userTimeDatas.size());
			return;
		}

		// 取User
		List<UserTimeData> subInfos = userTimeDatas.subList(start, end);
		List<Integer> ids = new ArrayList<Integer>();
		for (UserTimeData data : subInfos) {
			if (!ids.contains(data.getUserId()))
				ids.add(data.getUserId());
		}
		List<BriefUserInfoClient> users = UserCache.sequenceByIds(ids,
				CacheMgr.getUser(ids));
		for (UserTimeData data : subInfos) {
			data.setBriefUser(CacheMgr.getUserById(data.getUserId(), users));
		}
		resultPage.setResult(subInfos);
		resultPage.setTotal(userTimeDatas.size());
	}

	private void sortData() {
		if (null != userTimeDatas && !userTimeDatas.isEmpty()) {
			Collections.sort(userTimeDatas, new Comparator<UserTimeData>() {

				@Override
				public int compare(UserTimeData data1, UserTimeData data2) {
					return data2.getTime() - data1.getTime();
				}
			});
		}

	}

	public void setTitle() {
//		GuildProp guildProp = CacheMgr.guildPropCache.getGuildProp(rgic
//				.getMembers().size());
		GuildProp guildProp = CacheMgr.guildPropCache.search(rgic.getGic()
				.getLevel());
		if (null != guildProp)
			ViewUtil.setText(
					window,
					R.id.listTitle,
					"家族现有成员" + rgic.getMembers().size() + "/"
							+ guildProp.getMaxMemberCnt());
	}

	@Override
	public void handleItem(Object o) {

	}
}
