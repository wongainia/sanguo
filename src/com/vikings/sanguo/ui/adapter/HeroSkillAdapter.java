package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.HeroSkillUpgrade;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.protos.RES_DATA_TYPE;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.HeroSkillStudyTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroSkillAdapter extends ObjectAdapter implements OnClickListener {

	private HeroInfoClient hic;
	private HeroSkillSlotInfoClient hssic;
	private CallBack mCallBack;

	public HeroSkillAdapter(HeroInfoClient hic, HeroSkillSlotInfoClient hssic) {
		this.hic = hic;
		this.hssic = hssic;

	}

	public HeroSkillAdapter(HeroInfoClient hic, HeroSkillSlotInfoClient hssic,
			CallBack mCallBack) {
		this.hic = hic;
		this.hssic = hssic;
		this.mCallBack = mCallBack;
	}

	@Override
	public int getLayoutId() {
		return R.layout.hero_skill_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.cost = (TextView) convertView.findViewById(R.id.cost);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.evaluate = (TextView) convertView
					.findViewById(R.id.evaluate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BattleSkill battleSkill = (BattleSkill) getItem(position);
		ViewUtil.setImage(holder.evaluate, battleSkill.getRatingPic());
		new ViewImgScaleCallBack(battleSkill.getIcon(), holder.icon,
				Constants.COMMON_SKILL_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.COMMON_SKILL_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

		// 技能名称
		ViewUtil.setText(holder.name, battleSkill.getName());
		ViewUtil.setRichText(holder.desc, battleSkill.getEffectDesc());// 效果描述
		ViewUtil.setRichText(holder.cost, getMaterialStr(battleSkill));
		convertView.setOnClickListener(new ClickListener(battleSkill));
		return convertView;
	}

	private class ClickListener implements OnClickListener {
		private BattleSkill skill;

		public ClickListener(BattleSkill skill) {
			this.skill = skill;
		}

		@Override
		public void onClick(View v) {
			new HeroSkillStudyTip(hic, hssic, skill, mCallBack).show();
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	private static class ViewHolder {
		ImageView icon;
		TextView name, cost, desc, evaluate;
	}

	private String getMaterialStr(BattleSkill skill) {
		StringBuffer sBuffer = new StringBuffer();
		for (ShowItem showItem : getShowItems(skill)) {
			sBuffer.append(showItem.getName()).append("x")
					.append(showItem.getCount()).append("(");
			if (showItem.isEnough()) {
				sBuffer.append("" + showItem.getSelfCount());
			} else {
				sBuffer.append(StringUtil.color("" + showItem.getSelfCount(),
						"red"));
			}
			sBuffer.append(")");
		}
		return sBuffer.toString();
	}

	@SuppressWarnings("unchecked")
	private List<ShowItem> getShowItems(BattleSkill skill) {
		List<HeroSkillUpgrade> list = CacheMgr.heroSkillUpgradeCache
				.search(skill.getId());
		ReturnInfoClient ric = new ReturnInfoClient();
		for (HeroSkillUpgrade upgrade : list) {
			ric.addCfg(RES_DATA_TYPE.RES_DATA_TYPE_ITEM.getNumber(),
					upgrade.getItemID(), upgrade.getCount());
		}

		List<ShowItem> showItems = ric.showRequire();
		return showItems;
	}
}
