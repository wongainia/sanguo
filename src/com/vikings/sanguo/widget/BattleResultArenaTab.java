package com.vikings.sanguo.widget;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class BattleResultArenaTab {
	private BattleLogInfoClient blic;
	protected ViewGroup arenaWin, arenaLoss;
	private ViewGroup contentGroup;
	private int oldRank;
	private int newRank;

	public BattleResultArenaTab(BattleLogInfoClient blic, ViewGroup content,
			int oldRank, int newRank) {
		this.blic = blic;
		this.contentGroup = content;
		this.oldRank = oldRank;
		this.newRank = newRank;
		arenaWin = (ViewGroup) content.findViewById(R.id.arenaWin);
		arenaLoss = (ViewGroup) content.findViewById(R.id.arenaLoss);
		setValue();
	}

	private void setValue() {

		if (blic.isMeAttacker()) {
			if (blic.isMeWin()) {
				ViewUtil.setVisible(arenaWin);
				ViewUtil.setGone(arenaLoss);
				setView(arenaWin, "恭喜你，挑战成功！");
			} else {
				ViewUtil.setGone(arenaWin);
				ViewUtil.setVisible(arenaLoss);
				setView(arenaLoss, "真遗憾，挑战失败！");
			}
		} else {
			if (blic.isMeWin()) {
				ViewUtil.setVisible(arenaWin);
				ViewUtil.setGone(arenaLoss);
				setView(arenaWin, "恭喜你，防守成功！");
			} else {
				ViewUtil.setGone(arenaWin);
				ViewUtil.setVisible(arenaLoss);
				setView(arenaLoss, "真遗憾，防守失败！");
			}
		}
	}

	private void setView(View view, String opDesc) {
		int exploit = 0;
		if (newRank > 0)
			exploit = CacheMgr.arenaRewardCache.getSpecificExploit(newRank);
		String time = getTime();

		ViewUtil.setText(view, R.id.opDesc, opDesc);

		if (newRank == oldRank) {
			ViewUtil.setRichText(
					view,
					R.id.rank,
					"你当前的巅峰战场名次是【第 "
							+ StringUtil.color("" + newRank, R.color.color16)
							+ " 名】");
		} else if (newRank < oldRank) {
			ViewUtil.setRichText(
					view,
					R.id.rank,
					"你当前的巅峰战场名名次上升了 "
							+ StringUtil.color("" + (oldRank - newRank),
									R.color.color16) + " 名，达到【第"
							+ StringUtil.color("" + newRank, R.color.color16)
							+ " 名】");
		} else {
			ViewUtil.setRichText(
					view,
					R.id.rank,
					"你当前的巅峰战场名名次下降了 "
							+ StringUtil.color("" + (newRank - oldRank),
									R.color.color16) + " 名，到【第"
							+ StringUtil.color("" + newRank, R.color.color16)
							+ " 名】");
		}
		if (exploit > 0) {
			ViewUtil.setVisible(view, R.id.exploit);
			ViewUtil.setRichText(view, R.id.exploit, "每" + time + ",功勋值增加【"
					+ StringUtil.color("" + exploit, R.color.color16) + " 点】");
		} else {
			ViewUtil.setGone(view, R.id.exploit);
		}
	}

	private String getTime() {
		return DateUtil.formatTime(CacheMgr.dictCache.getDictInt(
				Dict.TYPE_ARENA, 4));
	}

}
