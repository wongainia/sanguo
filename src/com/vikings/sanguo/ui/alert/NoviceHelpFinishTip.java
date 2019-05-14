package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class NoviceHelpFinishTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_novice_help_finish;
	private ReturnInfoClient ri;
	private int nextCount;

	public NoviceHelpFinishTip(ReturnInfoClient ri, int nextCount) {
		super("新手救济", DEFAULT, true);
		this.ri = ri;
		this.nextCount = nextCount;
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		List<ShowItem> list = ri.showReturn(true);
		StringBuffer rewardsBuf = new StringBuffer();
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			ShowItem showItem = list.get(i);
			count += showItem.getCount();
			rewardsBuf.append("#" + showItem.getImg() + "# "
					+ showItem.getName() + "x" + showItem.getCount()
					+ (i == list.size() - 1 ? "" : "<br/>"));
		}
		ViewUtil.setRichText(
				content,
				R.id.desc,
				"副本打不过？别沮丧！送你"
						+ StringUtil.color(count + "名", R.color.k7_color8)
						+ "士兵，助你继续挑战，加油吧！", true);

		if (rewardsBuf.length() <= 0)
			ViewUtil.setGone(content, R.id.rewards);
		else {
			ViewUtil.setVisible(content, R.id.rewards);
			ViewUtil.setRichText(content, R.id.rewards, rewardsBuf.toString(),
					true);
		}

		// 下次失败次数
		ViewUtil.setVisible(content, R.id.extendsDesc);
		ViewUtil.setVisible(content, R.id.extendsDescWeight);
		if (nextCount <= 0) {
			ViewUtil.setRichText(content, R.id.extendsDesc,
					CacheMgr.uiTextCache.getTxt(401));
		} else {
			ViewUtil.setText(content, R.id.extendsDesc, "(再失败 " + nextCount
					+ " 次，可领取下次救济)");
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
