package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.FinishQuestInvoker;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.Quest;
import com.vikings.sanguo.model.QuestEffect;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.ItemTip;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ArenaQuestAdapter extends ObjectAdapter {
	private CallBack callBack;

	public ArenaQuestAdapter(CallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	public int getLayoutId() {
		return R.layout.arena_reward_list_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = Config.getController().inflate(getLayoutId());
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.bottomBg = convertView.findViewById(R.id.bottomBg);
			holder.typeLayout = convertView.findViewById(R.id.typeLayout);
			holder.typeName = (TextView) convertView
					.findViewById(R.id.typeName);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			ViewUtil.setVisible(holder.desc);
			holder.condition = (TextView) convertView
					.findViewById(R.id.condition);
			ViewUtil.setVisible(holder.condition);
			holder.finishBtn = (Button) convertView
					.findViewById(R.id.finishBtn);			
			ViewUtil.setText(holder.finishBtn, "兑换");
			
			holder.iconGroup = convertView.findViewById(R.id.iconGroup);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final QuestInfoClient qic = (QuestInfoClient) getItem(position);
		Quest quest = qic.getQuest();

		new ViewImgScaleCallBack(quest.getIcon(), holder.icon,
				Constants.QUEST_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.QUEST_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

		holder.bottomBg.setBackgroundResource(R.drawable.has_troop_bg);

		String questTagName = quest.getTagName();
		if (StringUtil.isNull(questTagName)) {
			ViewUtil.setGone(holder.typeLayout);
		} else {
			ViewUtil.setText(holder.typeName, questTagName);
			ViewUtil.setVisible(holder.typeLayout);
		}

		ViewUtil.setRichText(holder.desc, quest.getTarget());
		int exploit = qic.getNeedExploit();
		if (0 == exploit)
			ViewUtil.setGone(holder.condition);
		else
			ViewUtil.setRichText(holder.condition, "需要功勋：");

		ViewUtil.setRichText(holder.name, quest.getName());
		ViewUtil.setVisible(holder.finishBtn);
		int currentExplot = Account.user.getExploit();
		int comsume = 0;
		List<QuestEffect> effects = qic.getQuestEffect();
		if(ListUtil.isNull(effects) == false)
		{
			for(int i = 0; i < effects.size(); i ++)
			{
				QuestEffect questEffect = effects.get(i);
				//消耗功勋
				if(questEffect.isAttr() && questEffect.getTypeValue() == 3)
				{
					comsume = questEffect.getCount();		
					break;
				}
			}
		}
		
		if(currentExplot < Math.abs(comsume))
		{
			holder.finishBtn.setOnClickListener(new OnClickListener()
			{				
				@Override
				public void onClick(View v)
				{
					Config.getController().alert( "功勋不足", "你的功勋不足，无法兑换，请先积累功勋", "", null, false);
				}
			});
		}
		else
		{
				holder.finishBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new FQInvoker(qic).start();
				}
			});
		}
				
		holder.iconGroup.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(qic != null)
				{
					List<QuestEffect> effect =	qic.getQuestEffect();
					if(ListUtil.isNull(effect) == false)
					{
						for(int i = 0; i < effect.size(); i ++)
						{
							QuestEffect questEffect = effect.get(i);
							if(questEffect.isItem())
							{
								Item item = questEffect.getItem();
								if(item != null)
								{
									ItemTip itemTip = new ItemTip(item);
									itemTip.setHideButton();
									itemTip.show();
								}
							}
						}
					}
				}
				
			}
		});
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	private class FQInvoker extends FinishQuestInvoker {

		public FQInvoker(QuestInfoClient qi) {
			super(qi);
		}

		@Override
		protected void onOK() {
			super.onOK();
			if (null != callBack)
				callBack.onCall();
			notifyDataSetChanged();
		}
	}

	private static class ViewHolder {
		ImageView icon;
		View typeLayout, bottomBg;
		TextView typeName, name, desc, condition;
		Button finishBtn;
		
		View iconGroup;
	}
}
