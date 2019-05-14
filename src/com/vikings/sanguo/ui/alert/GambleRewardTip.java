package com.vikings.sanguo.ui.alert;

import java.util.List;
import com.vikings.sanguo.R;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GambleRewardTip extends RewardTip {

	public GambleRewardTip(String title, List<ShowItem> reward, int leftTimes) {
		super(title, reward, false, false);
		ViewUtil.setGone(tip, R.id.closeDesc);
		ViewUtil.setVisible(tip, R.id.msgdesc);
		ViewUtil.setRichText(content, R.id.msgdesc, "每日可使用100次,今日剩余"
				+ StringUtil.color(String.valueOf(leftTimes), R.color.color24)
				+ "次");
	}
}
