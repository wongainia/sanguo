package com.vikings.sanguo.ui.window;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.ArenaAwradInvoker;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.adapter.ArenaQuestAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.ArenaWindowTab;

public class ArenaAwardTab implements ArenaWindowTab {
	private ArenaQuestAdapter questAdapter;
	private ViewGroup window;
	private CallBack callBack;

	public ArenaAwardTab(CallBack callBack) {
		this.callBack = callBack;
	}

	public void setWindow(ViewGroup window) {
		this.window = window;
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<QuestInfoClient> quest = Account.getArenaQuest();
		resultPage.setTotal(quest.size());
		resultPage.setResult(quest);
		if (!quest.isEmpty())
			Collections.sort(quest, new Comparator<QuestInfoClient>() {

				@Override
				public int compare(QuestInfoClient client1,
						QuestInfoClient client2) {
					return client1.getQuest().getSequence()
							- client2.getQuest().getSequence();
				}
			});
	}

	@Override
	public void showUI() {
		if (ViewUtil.isGone(window.findViewById(R.id.bonusLayout)))
			ViewUtil.setVisible(window, R.id.bonusLayout);

		if (ViewUtil.isGone(window.findViewById(R.id.gradientMsgLayout)))
			ViewUtil.setVisible(window.findViewById(R.id.gradientMsgLayout));
		ViewUtil.setText(window.findViewById(R.id.gradientMsg),
				CacheMgr.uiTextCache.getTxt(UITextProp.HERO_ARENA_TITLE));
		setArenaBonusInfo();
	}

	@Override
	public ObjectAdapter getAdapter() {
		if (null == questAdapter)
			questAdapter = new ArenaQuestAdapter(callBack);
		return questAdapter;
	}

	@Override
	public int refreshInterval() {
		return 1000;
	}

	private void setArenaBonusInfo() {
		View bonusLayout = window.findViewById(R.id.bonusLayout);
		new UserIconCallBack(Account.user.bref(),
				bonusLayout.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
						* Constants.ICON_WIDTH, Config.SCALE_FROM_HIGH
						* Constants.ICON_WIDTH);
		bonusLayout.findViewById(R.id.icon).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Config.getController().showCastle(Account.user.getId());
					}
				});

		if (Account.myLordInfo.isArenaGraded())
			ViewUtil.setText(window, R.id.No,
					"No." + Account.myLordInfo.getArenaRank());
		else
			ViewUtil.setText(window, R.id.No, "未定级");

		View award = bonusLayout.findViewById(R.id.award);
		ViewUtil.setRichText(award, "领取<br>功勋");

		if (0 == Account.myLordInfo.getArenaRank()) {
			ViewUtil.setGone(bonusLayout, R.id.progress);
			ViewUtil.disableButton(award);
			ViewUtil.setRichText(bonusLayout.findViewById(R.id.desc),
					"你需要一场定级赛来获取领奖资格");
		} else {
			ViewUtil.setVisible(bonusLayout, R.id.progress);
			ViewUtil.enableButton(award);
			ProgressBar bar = (ProgressBar) bonusLayout
					.findViewById(R.id.progressBar);
			int[] exploit = Account.myLordInfo.getCurArenaExploit();

			bar.set(exploit[0], exploit[1]);
			ViewUtil.setRichText(bonusLayout, R.id.progressDesc, StringUtil
					.color("可领取：" + exploit[0] + "/" + exploit[1],
							R.color.k7_color1));
			ViewUtil.setRichText(bonusLayout.findViewById(R.id.desc),
					CacheMgr.arenaResetTimeCache.getTitleDesc());

			award.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new ArenaAwradInvoker(callBack).start();
				}
			});
		}

	}

	@Override
	public boolean needScroll() {
		return false;
	}

	@Override
	public String getEmptyShowText() {
		return "暂时没有巅峰战场奖励";
	}
}
