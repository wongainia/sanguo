package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.invoker.FinishQuestInvoker;
import com.vikings.sanguo.model.JumpTargetPanel;
import com.vikings.sanguo.model.PropStronger;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class StrongerWindow extends CustomConfirmDialog implements
		OnClickListener {

	public static final int[] upgradeType = { Constants.GENERAL,
			Constants.EQUIPMENT, Constants.SOLDIER };
	public final int[] layoutIds = { R.id.general_upgrade,
			R.id.equipmen_upgrade, R.id.soldier_upgrade };

	public final String[] maxDesc = { "恭喜你！将领已达5星评价", "恭喜你！装备已达5星评价",
			"恭喜你！士兵已达5星评价" };

	public StrongerWindow() {
		super("变强引导");
		setButton(FIRST_BTN, "关闭", closeL);
	}

	@Override
	public void show() {
		super.show();
		setValue();
	}

	private void setValue() {
		// 得到推荐的类型 ：将领，装备，士兵
		int type = PropStronger.getRcommendType();

		// 初始化界面
		for (int i = 0; i < upgradeType.length; i++) {
			PropStronger psPropStronger = PropStronger
					.getCurrentPs(upgradeType[i]);
			ViewGroup vGroup = (ViewGroup) findViewById(layoutIds[i]);
			// 初始化星星
			if (psPropStronger == null) {
				IconUtil.strongerStar(vGroup, 5);
				ViewUtil.setRichText(vGroup, R.id.upgrade_spec,
						StringUtil.color(maxDesc[i], R.color.color19));
				vGroup.findViewById(R.id.go_upgrade).setVisibility(
						View.INVISIBLE);
				vGroup.findViewById(R.id.recommand).setVisibility(
						View.INVISIBLE);
			} else {
				if (psPropStronger.isFinishedTask()) {
					IconUtil.strongerStar(vGroup, psPropStronger.getStar());
				} else if (psPropStronger.isUnfinishedTask()) {
					IconUtil.strongerStar(vGroup, psPropStronger.getStar() - 1);
				}
				initUpgradeBtn(vGroup, psPropStronger);

				if (psPropStronger.isFinishedTask()
						&& psPropStronger.getStar() == 5) {
					// 特殊处理 任务完成 但是还没有领奖的情况
					ViewUtil.setRichText(vGroup, R.id.upgrade_spec, maxDesc[i]);
				} else {
					ViewUtil.setRichText(vGroup, R.id.upgrade_spec,
							psPropStronger.getDesc());
				}

				if (type == psPropStronger.getType()) {
					vGroup.findViewById(R.id.recommand).setVisibility(
							View.VISIBLE);
				} else {
					vGroup.findViewById(R.id.recommand).setVisibility(
							View.INVISIBLE);
				}

			}

		}

	}

	private void initUpgradeBtn(ViewGroup vGroup, PropStronger ps) {
		Button btn = (Button) vGroup.findViewById(R.id.go_upgrade);
		if (ps.isFinishedTask()) {
			btn.setBackgroundResource(R.drawable.honoree_bg_btn);
			btn.setText("领奖");
		} else if (ps.isUnfinishedTask()) {
			btn.setBackgroundResource(R.drawable.upgrade_bg_btn);
			btn.setText("去提升");
		}
		btn.setTag(ps);
		btn.setOnClickListener(this);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.stronger_layout, tip, false);
	}

	@Override
	public void onClick(View v) {
		PropStronger ps = (PropStronger) v.getTag();
		if (ps.isFinishedTask()) {
			new UpgradeInvoker(ps.getQuestInfoClient()).start();
		} else if (ps.isUnfinishedTask()) {
			dismiss();
			if (ps.getJumpId() == 1) {
				controller.openSite(ps.getUrl());
			} else {
				JumpTargetPanel.doJump(ps.getJumpId());
			}
		}
	}

	private class UpgradeInvoker extends FinishQuestInvoker {

		public UpgradeInvoker(QuestInfoClient qi) {
			super(qi);
		}

		@Override
		protected void onOK() {
			super.onOK();
			setValue();
		}

	}

	public static boolean canReward() {
		for (int i = 0; i < upgradeType.length; i++) {
			PropStronger psPropStronger = PropStronger
					.getCurrentPs(upgradeType[i]);
			if (psPropStronger != null && psPropStronger.isFinishedTask()) {
				return true;
			}
		}
		return false;
	}

}
