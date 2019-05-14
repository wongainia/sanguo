/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-18 下午5:03:18
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.model.ArenaLogInfoClient;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ArenaResultTip extends Alert {

	private View content;
	
	public ArenaResultTip(ArenaLogInfoClient log) {
		super(true);
		content = controller.inflate(R.layout.arena_rank_item);
		if (log.isMeWin()) {
			ViewUtil.setRichText(content, R.id.title, StringUtil.color("战斗胜利", R.color.k7_color15));
			if (log.isMeAtk()) {
				ViewUtil.setRichText(content, R.id.desc, StringUtil.color("您的排名提升了", R.color.k7_color2));
				ViewUtil.setRichText(content, R.id.rank1, StringUtil.color("No." + log.getAttackerPos(), R.color.k7_color2));
				ViewUtil.setRichText(content, R.id.rank2, StringUtil.color("No." + log.getDefenderPos(), R.color.k7_color2));
			} else {
				ViewUtil.setGone(content, R.id.rank);
				ViewUtil.setVisible(content, R.id.noRank);
				ViewUtil.setRichText(content, R.id.noRank, StringUtil.color("你成功防御的TA的进攻", R.color.k7_color2));
			}
		} else {
			ViewUtil.setRichText(content, R.id.title, StringUtil.color("战斗失败", R.color.k7_color12));
			if (log.isMeAtk()) {
				ViewUtil.setGone(content, R.id.rank);
				ViewUtil.setVisible(content, R.id.noRank);
				ViewUtil.setRichText(content, R.id.noRank, StringUtil.color("你的排名没有发生变化", R.color.k7_color2));
			} else {
				ViewUtil.setRichText(content, R.id.desc, StringUtil.color("您的排名下降了", R.color.k7_color2));
				ViewUtil.setRichText(content, R.id.rank1, StringUtil.color("No." + log.getAttackerPos(), R.color.k7_color2));
				ViewUtil.setRichText(content, R.id.rank2, StringUtil.color("No." + log.getDefenderPos(), R.color.k7_color2));
			}
		}
	}

	public void show() {
		super.show(content);
	}
}
