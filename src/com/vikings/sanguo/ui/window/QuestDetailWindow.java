package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.FinishQuestInvoker;
import com.vikings.sanguo.model.Quest;
import com.vikings.sanguo.model.QuestCondition;
import com.vikings.sanguo.model.QuestConditionInfoClient;
import com.vikings.sanguo.model.QuestEffect;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class QuestDetailWindow extends CustomPopupWindow {

	private View targetLayout, rewardLayout, typeLayout;
	private ViewGroup targetView, rewardView;
	private ImageView icon;
	private TextView name, desc;

	private QuestInfoClient qic;

	@Override
	protected void init() {
		super.init("任务详情");
		setContent(R.layout.quest_detail);
		targetLayout = window.findViewById(R.id.targetLayout);
		rewardLayout = window.findViewById(R.id.rewardLayout);

		targetView = (ViewGroup) window.findViewById(R.id.targetView);
		rewardView = (ViewGroup) window.findViewById(R.id.rewardView);

		icon = (ImageView) window.findViewById(R.id.icon);
		name = (TextView) window.findViewById(R.id.name);
		desc = (TextView) window.findViewById(R.id.desc);
	}

	public void open(QuestInfoClient qic) {
		this.qic = Account.getQuestInfoById(qic.getQuestId());
		doOpen();
		setValue();
	}

	private void setValue() {
		TextView belowBtn = (TextView) window.findViewById(R.id.belowBtn);
		if (!qic.isComplete()) {
			if (qic.getQuest().getSpecialType() == Quest.SPECIAL_TYPE_RECHARGE) {
				ImageUtil.setBgNormal(belowBtn);
				setBottomButton("去充值", new OnClickListener() {

					@Override
					public void onClick(View v) {
						controller.goBack();
						controller.openRechargeCenterWindow();
					}
				});
			} else {
				ImageUtil.setBgGray(belowBtn);
				setBottomButton("领奖", new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (qic.isComplete()) {
							new FinishInvoker(qic).start();
						}
					}
				});
			}

		} else {
			ImageUtil.setBgNormal(belowBtn);
			setBottomButton("领奖", new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (qic.isComplete()) {
						new FinishInvoker(qic).start();
					}

				}
			});
		}

		Quest quest = qic.getQuest();
		if (null == quest)
			return;

		new ViewImgScaleCallBack(quest.getIcon(), icon,
				Constants.QUEST_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.QUEST_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

		ViewUtil.setText(name, quest.getName());
		ViewUtil.setRichText(desc, quest.getDesc(), true);
		setTargetContent();
		setRewardContent();
		// Account.readLog.addKnownQuest(qic.getQuestId());
	}

	/**
	 * @return 是否完成所有条件
	 */
	private void setTargetContent() {
		List<QuestCondition> list = qic.getConditions();
		List<QuestConditionInfoClient> infos = qic.getQcics();
		if (null == list || list.isEmpty()) {
			ViewUtil.setGone(targetLayout);
			return;
		}

		Collections.sort(list, new Comparator<QuestCondition>() {

			@Override
			public int compare(QuestCondition object1, QuestCondition object2) {
				return object1.getSequence() - object2.getSequence();
			}
		});

		List<QuestCondition> showList = new ArrayList<QuestCondition>();
		List<QuestCondition> hideList = new ArrayList<QuestCondition>();
		for (QuestCondition qc : list) {
			if (qc.getShow() == QuestCondition.FLAG_SHOW) {
				showList.add(qc);
			} else {
				hideList.add(qc);
			}
		}
		// 如果没有隐藏条件，正常显示；否则，显示条件的完成情况依赖于隐藏条件
		if (hideList.isEmpty()) {
			for (QuestCondition qc : list) {
				ViewGroup view = (ViewGroup) controller
						.inflate(R.layout.mission_target);
				new ViewImgScaleCallBack(qc.getImg(),
						view.findViewById(R.id.icon),
						Config.SCALE_FROM_HIGH * 30,
						Config.SCALE_FROM_HIGH * 30);
				// ViewUtil.setImage(view, R.id.icon, qc.getImg());
				ViewUtil.setRichText(view, R.id.desc, qc.getDesc());
				ViewUtil.setText(view, R.id.progress,
						"0/" + qc.getTargetCount());
				ViewUtil.setGone(view, R.id.confirm);

				for (QuestConditionInfoClient info : infos) {
					if (qc.getTargetType() == info.getType()
							&& qc.getTargetValue() == info.getTarget()) {
						// 特别处理：解决巅峰战场任务详情中，QuestConditionInfoClient的功勋和实际功勋不一致的问题
						int val = info.getValue();
						// if (1016 == info.getType() && 3 == info.getTarget())
						// val = Account.user.getExploit();

						if (info.getValue() >= qc.getTargetCount()) {
							ViewUtil.setRichText(
									view,
									R.id.progress,
									StringUtil.color(
											val + "/" + qc.getTargetCount(),
											controller
													.getResourceColorText(R.color.k7_color12)));
						} else {
							ViewUtil.setText(view, R.id.progress, val + "/"
									+ qc.getTargetCount());
						}
					}
				}
				targetView.addView(view);
			}
		} else {
			for (QuestCondition qc : showList) {
				ViewGroup view = (ViewGroup) controller
						.inflate(R.layout.mission_target);
				ViewUtil.setImage(view, R.id.icon, qc.getImg());
				ViewUtil.setRichText(view, R.id.desc, qc.getDesc());
				if (qic.isComplete()) {
					ViewUtil.setRichText(
							view,
							R.id.progress,
							StringUtil.color(
									qc.getTargetCount() + "/"
											+ qc.getTargetCount(),
									controller
											.getResourceColorText(R.color.k7_color12)));
				} else {
					ViewUtil.setText(view, R.id.progress,
							"0/" + qc.getTargetCount());
				}
				targetView.addView(view);
			}
		}
	}

	private void setRewardContent() {
		List<QuestEffect> list = qic.getQuestEffect();
		if (null == list || list.isEmpty()) {
			ViewUtil.setGone(rewardLayout);
			return;
		}
		// 过滤所有负效果
		List<QuestEffect> m1line2Items = new ArrayList<QuestEffect>();
		List<QuestEffect> m1line1Items = new ArrayList<QuestEffect>();
		for (int i = 0; i < list.size(); i++) {
			QuestEffect qe = list.get(i);
			if (qe.getTypeValue() >= 0 && qe.getCount() >= 0) {
				if (m1line1Items.size() == m1line2Items.size())
					m1line1Items.add(qe);
				else
					m1line2Items.add(qe);
			}
		}
		for (int i = 0; i < m1line2Items.size(); i += 2) {
			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.mission_reward);
			ViewGroup rewardView1 = (ViewGroup) viewGroup
					.findViewById(R.id.reward1);
			ViewGroup rewardView2 = (ViewGroup) viewGroup
					.findViewById(R.id.reward2);
			QuestEffect qe1 = m1line2Items.get(i);
			setSingleRewardValue(rewardView1, qe1);
			if (i + 1 < m1line2Items.size()) {
				QuestEffect qe2 = m1line2Items.get(i + 1);
				setSingleRewardValue(rewardView2, qe2);
			} else {
				ViewUtil.setGone(rewardView2);
			}
			rewardView.addView(viewGroup);
		}

		for (int i = 0; i < m1line1Items.size(); i++) {
			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.mission_reward);
			ViewGroup rewardView1 = (ViewGroup) viewGroup
					.findViewById(R.id.reward1);
			ViewGroup rewardView2 = (ViewGroup) viewGroup
					.findViewById(R.id.reward2);
			QuestEffect qe1 = m1line1Items.get(i);
			ViewUtil.setGone(rewardView2);
			setSingleRewardValue(rewardView1, qe1);
			rewardView.addView(viewGroup);
		}
	}

	private void setSingleRewardValue(ViewGroup viewGroup, QuestEffect qe) {
		ImageView icon = (ImageView) viewGroup.findViewById(R.id.icon);
		TextView name = (TextView) viewGroup.findViewById(R.id.name);
		TextView value = (TextView) viewGroup.findViewById(R.id.value);
		if (qe.isAttr()) {
			ViewUtil.setImage(icon,
					ReturnInfoClient.getAttrTypeIcon(qe.getTypeValue()));
			ViewUtil.setText(name,
					ReturnInfoClient.getAttrTypeName(qe.getTypeValue()));
			ViewUtil.setText(value, "+" + qe.getCount());
		} else if (qe.isItem()) {
			new ViewImgScaleCallBack(qe.getItem().getImage(), icon,
					35 * Config.SCALE_FROM_HIGH, 35 * Config.SCALE_FROM_HIGH);
			ViewUtil.setText(name, qe.getItem().getName());
			ViewUtil.setText(value, "+" + qe.getCount());
		} else if (qe.isHero()) {
			new ViewImgScaleCallBack(qe.getHero().getIcon(), icon,
					35 * Config.SCALE_FROM_HIGH, 35 * Config.SCALE_FROM_HIGH);
			ViewUtil.setText(name, qe.getHero().getName());
			ViewUtil.setText(value, "+" + qe.getCount());
		} else if (qe.isTroop()) {
			new ViewImgScaleCallBack(qe.getTroop().getSmallIcon(), icon,
					35 * Config.SCALE_FROM_HIGH, 35 * Config.SCALE_FROM_HIGH);
			ViewUtil.setText(name, qe.getTroop().getName());
			ViewUtil.setText(value, "+" + qe.getCount());
		}
	}

	private class FinishInvoker extends FinishQuestInvoker {

		public FinishInvoker(QuestInfoClient qi) {
			super(qi);
		}

		@Override
		protected void onOK() {
			super.onOK();
			ctr.goBack();
		}

	}

}
