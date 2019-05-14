package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class FiefReportAlert extends Alert implements OnClickListener {

	private final static int layout = R.layout.alert_fief_report;
	private ViewGroup content, hasFief, hasNoFief;
	private TextView title, fiefCountInBattle, fiefCountLost;
	private Button okBt, cancelBt;

	private int battleCount, lostCount;

	public FiefReportAlert() {
		content = (ViewGroup) controller.inflate(layout);
		hasFief = (ViewGroup) content.findViewById(R.id.hasFief);
		hasNoFief = (ViewGroup) content.findViewById(R.id.hasNoFief);

		title = (TextView) content.findViewById(R.id.title);
		fiefCountInBattle = (TextView) content
				.findViewById(R.id.fiefCountInBattle);
		fiefCountLost = (TextView) content.findViewById(R.id.fiefCountLost);

		okBt = (Button) content.findViewById(R.id.okBt);
		okBt.setOnClickListener(this);

		cancelBt = (Button) content.findViewById(R.id.cancelBt);
		cancelBt.setOnClickListener(this);
	}

	public void show(int battleCount, int lostCount) {
		this.battleCount = battleCount;
		this.lostCount = lostCount;
		if (Account.isLord()) {
			ViewUtil.setVisible(hasFief);
			ViewUtil.setGone(hasNoFief);
			ViewUtil.setText(title, "报告领主大人");
			if (battleCount != 0) {
				ViewUtil.setVisible(fiefCountInBattle);
				ViewUtil.setRichText(
						fiefCountInBattle,
						"#xx#您的"
								+ StringUtil.color(String.valueOf(battleCount),
										"red") + "块领地被进攻中，请尽快增援");
			} else {
				ViewUtil.setGone(fiefCountInBattle);
			}

			if (lostCount != 0) {
				ViewUtil.setVisible(fiefCountLost);
				ViewUtil.setRichText(
						fiefCountLost,
						"#fn#您今天已经有"
								+ StringUtil.color(String.valueOf(lostCount),
										"red") + "块领地已被其他领主攻占了");
			} else {
				ViewUtil.setGone(fiefCountLost);
			}

		} else {
			ViewUtil.setGone(hasFief);
			ViewUtil.setVisible(hasNoFief);
			ViewUtil.setText(title, "您的领地状态");
		}
		super.show(content);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == okBt) {

		} else if (v == cancelBt) {

		}
	}
}
