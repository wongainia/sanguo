package com.vikings.sanguo.ui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.FinishQuestInvoker;
import com.vikings.sanguo.model.Quest;
import com.vikings.sanguo.model.QuestCondition;
import com.vikings.sanguo.model.QuestConditionInfoClient;
import com.vikings.sanguo.model.QuestEffect;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.window.QuestListWindow;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class QuestAdapter extends ObjectAdapter {

	private QuestListWindow window;

	@Override
	public int getLayoutId() {
		return R.layout.quest_list_item;
	}

	public QuestAdapter(QuestListWindow window) {
		this.window = window;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.finishBtn = (ImageButton) convertView
					.findViewById(R.id.finishBtn);

			holder.type = (ImageView) convertView.findViewById(R.id.type);

			holder.itemLayout = (ViewGroup) convertView
					.findViewById(R.id.itemLayout);
			holder.detailLayout = (ViewGroup) convertView
					.findViewById(R.id.detailLayout);

			holder.targetLayout = convertView.findViewById(R.id.targetLayout);
			holder.rewardLayout = convertView.findViewById(R.id.rewardLayout);

			holder.targetView = (ViewGroup) convertView
					.findViewById(R.id.targetView);
			holder.rewardView = (ViewGroup) convertView
					.findViewById(R.id.rewardView);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final QuestInfoClient qic = (QuestInfoClient) getItem(position);

		holder.questInfoClient = qic;

		if (qic.isOpen()) {
			ViewUtil.setVisible(holder.detailLayout);
			setValue(holder, qic);
		} else {
			ViewUtil.setGone(holder.detailLayout);
		}

		final Quest quest = qic.getQuest();
		new ViewImgCallBack(quest.getIcon(), holder.icon);
		// String questTypeName = quest.getTypeName();
		int type = quest.getType();
		// （1：主线任务 2：日常任务 3：活动任务 4:巅峰奖励）
		ViewUtil.setVisible(holder.type);
		if (type == 1) {
			holder.type.setBackgroundResource(R.drawable.mainline_task);
		} else if (type == 2) {
			holder.type.setBackgroundResource(R.drawable.daily_task);
		} else if (type == 3) {
			holder.type.setBackgroundResource(R.drawable.active_task);
		} else if (type == 4) {
			holder.type.setBackgroundResource(R.drawable.peak_reward);
		}
		ViewUtil.setText(holder.name, quest.getName());
		if (qic.isComplete()) {
			holder.finishBtn.setOnClickListener(new FinishQuestClickListener(
					qic));
			ViewUtil.setGone(holder.type);
			ViewUtil.setVisible(holder.finishBtn);
		} else {
			holder.finishBtn.setOnClickListener(null);
			ViewUtil.setGone(holder.finishBtn);
		}
		holder.itemLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				move(v, qic);
			}
		});
		return convertView;
	}

	private void move(View v, QuestInfoClient quest) {
		List<QuestInfoClient> data = content;
		for (QuestInfoClient it : data) {
			if (it.getId() == quest.getId()) {
				if (it.isOpen())
					it.setOpen(false);
				else {
					it.setOpen(true);
					window.smoothMoveItem((View) v.getParent());
				}
			} else
				it.setOpen(false);
		}
		notifyDataSetChanged();
		window.setFooterDetailGone();
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	private class FinishQuestClickListener implements OnClickListener {
		private QuestInfoClient qic;

		public FinishQuestClickListener(QuestInfoClient qic) {
			this.qic = qic;
		}

		@Override
		public void onClick(View v) {
			new FQInvoker(qic).start();
		}
	}

	private class FQInvoker extends FinishQuestInvoker {

		public FQInvoker(QuestInfoClient qi) {
			super(qi);
		}

		@Override
		protected void onOK() {
			super.onOK();
			notifyDataSetChanged();
		}

	}

	private static class ViewHolder {
		ImageView icon;
		TextView name;
		ImageButton finishBtn;

		ImageView type;
		ViewGroup itemLayout, detailLayout;
		View targetLayout, rewardLayout;
		ViewGroup targetView, rewardView;
		TextView desc;

		QuestInfoClient questInfoClient;
	}

	private void setValue(ViewHolder holder, final QuestInfoClient qic) {

		Quest quest = qic.getQuest();
		if (null == quest)
			return;

		ViewUtil.setRichText(holder.desc, quest.getDesc(), true);
		setTargetContent(holder, qic);
		setRewardContent(holder, qic);
		// Account.readLog.addKnownQuest(qic.getQuestId());
	}

	/**
	 * @return 是否完成所有条件
	 */
	private void setTargetContent(ViewHolder holder, final QuestInfoClient qic) {
		List<QuestCondition> list = qic.getConditions();
		List<QuestConditionInfoClient> infos = qic.getQcics();
		if (null == list || list.isEmpty()) {
			ViewUtil.setGone(holder.targetLayout);
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
		if (holder.targetView.getChildCount() > 0) {
			holder.targetView.removeAllViews();
		}
		// 如果没有隐藏条件，正常显示；否则，显示条件的完成情况依赖于隐藏条件
		if (hideList.isEmpty()) {
			for (int i = 0; i < list.size(); i++)
			// for (QuestCondition qc : list)
			{
				QuestCondition qc = list.get(i);
				ViewGroup view = (ViewGroup) Config.getController().inflate(
						R.layout.mission_target);
				ViewUtil.setRichText(view, R.id.desc,
						(i + 1) + "." + qc.getDesc());
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
											Config.getController()
													.getResourceColorText(
															R.color.k7_color12)));
						} else {
							ViewUtil.setText(view, R.id.progress, val + "/"
									+ qc.getTargetCount());
						}
					}
				}
				holder.targetView.addView(view);
			}
		} else {
			for (QuestCondition qc : showList) {
				ViewGroup view = (ViewGroup) Config.getController().inflate(
						R.layout.mission_target);
				ViewUtil.setImage(view, R.id.icon, qc.getImg());
				ViewUtil.setRichText(view, R.id.desc, qc.getDesc());
				if (qic.isComplete()) {
					ViewUtil.setRichText(view, R.id.progress, StringUtil.color(
							qc.getTargetCount() + "/" + qc.getTargetCount(),
							Config.getController().getResourceColorText(
									R.color.k7_color12)));
				} else {
					ViewUtil.setText(view, R.id.progress,
							"0/" + qc.getTargetCount());
				}
				holder.targetView.addView(view);
			}
		}
	}

	private void setRewardContent(ViewHolder holder, final QuestInfoClient qic) {
		List<QuestEffect> list = qic.getQuestEffect();
		if (null == list || list.isEmpty()) {
			ViewUtil.setGone(holder.rewardLayout);
			return;
		}
		if (holder.rewardView.getChildCount() > 0) {
			holder.rewardView.removeAllViews();
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

		for (int i = 0; i < list.size(); i += 2) {
			ViewGroup viewGroup = (ViewGroup) Config.getController().inflate(
					R.layout.mission_reward);
			ViewGroup rewardView1 = (ViewGroup) viewGroup
					.findViewById(R.id.reward1);
			ViewGroup rewardView2 = (ViewGroup) viewGroup
					.findViewById(R.id.reward2);
			QuestEffect qe1 = list.get(i);
			setSingleRewardValue(rewardView1, qe1);

			if (i + 1 >= list.size()) {
				ViewUtil.setGone(rewardView2);
				holder.rewardView.addView(viewGroup);
				break;
			}
			QuestEffect qe2 = list.get(i + 1);
			setSingleRewardValue(rewardView2, qe2);

			// if (i + 1 < m1line2Items.size()) {
			// QuestEffect qe2 = m1line2Items.get(i + 1);
			// setSingleRewardValue(rewardView2, qe2);
			// } else {
			// ViewUtil.setGone(rewardView2);
			// }
			holder.rewardView.addView(viewGroup);
			// ((viewGroup)vg.findViewById(R.id.rewardView)).addView(viewGroup);
		}

		// for (int i = 0; i < m1line2Items.size(); i += 2) {
		// ViewGroup viewGroup = (ViewGroup) Config.getController()
		// .inflate(R.layout.mission_reward);
		// ViewGroup rewardView1 = (ViewGroup) viewGroup
		// .findViewById(R.id.reward1);
		// ViewGroup rewardView2 = (ViewGroup) viewGroup
		// .findViewById(R.id.reward2);
		// QuestEffect qe1 = m1line2Items.get(i);
		// setSingleRewardValue(rewardView1, qe1);
		// if (i + 1 < m1line2Items.size()) {
		// QuestEffect qe2 = m1line2Items.get(i + 1);
		// setSingleRewardValue(rewardView2, qe2);
		// } else {
		// ViewUtil.setGone(rewardView2);
		// }
		// holder.rewardView.addView(viewGroup);
		// //((viewGroup)vg.findViewById(R.id.rewardView)).addView(viewGroup);
		// }
		//
		// for (int i = 0; i < m1line1Items.size(); i++) {
		// ViewGroup viewGroup = (ViewGroup) Config.getController()
		// .inflate(R.layout.mission_reward);
		// ViewGroup rewardView1 = (ViewGroup) viewGroup
		// .findViewById(R.id.reward1);
		// ViewGroup rewardView2 = (ViewGroup) viewGroup
		// .findViewById(R.id.reward2);
		// QuestEffect qe1 = m1line1Items.get(i);
		// ViewUtil.setGone(rewardView2);
		// setSingleRewardValue(rewardView1, qe1);
		// holder.rewardView.addView(viewGroup);
		// }
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

}
