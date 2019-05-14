package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.EquipEffectClient;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentForgeEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.ui.window.EquipmentDetailWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class EquipmentAdapter extends ObjectAdapter {

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.effect = (TextView) convertView.findViewById(R.id.effect);
			holder.properties = (TextView) convertView
					.findViewById(R.id.properties);
			holder.stoneName = (TextView) convertView
					.findViewById(R.id.stoneName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		EquipmentInfoClient eic = (EquipmentInfoClient) getItem(position);

		IconUtil.setEquipmentIcon(convertView, eic.getProp().getType(), eic,
				false);
		ViewUtil.setRichText(holder.name, StringUtil.color(eic.getProp()
				.getName(), eic.getEquipmentDesc().getColor()));
		HeroInfoClient hic = null;
		if (eic.weared())
			hic = Account.heroInfoCache.get(eic.getHeroId());

		if (null != hic) {
			ViewUtil.setRichText(holder.state, hic.getColorTypeName() + " "
					+ hic.getColorHeroName());
		} else {
			ViewUtil.setRichText(holder.state,
					StringUtil.color("未装备", R.color.color19));
		}

		EquipmentEffect ee = CacheMgr.equipmentEffectCache.getEquipmentEffect(
				eic.getEquipmentId(), (byte) eic.getQuality(), eic.getLevel());

		if (null != ee) {
			ViewUtil.setRichText(
					holder.properties,
					"【属性】" + ee.getEffectTypeName() + "+"
							+ ee.getEffectValue(eic.getLevel()));
		} else {
			ViewUtil.setText(holder.properties, "【属性】无");
		}

		EquipEffectClient effect = eic.getEffect();
		if (effect != null && effect.getType() > 0) {
			EquipmentForgeEffect eqUipEffect = CacheMgr.getEffectById(effect
					.getType());
			ViewUtil.setText(holder.effect,
					"【特效】" + eqUipEffect.getEffectDesc());
		} else {
			ViewUtil.setText(holder.effect, "【特效】无");
		}

		if (eic.hasStone()) {
			ViewUtil.setText(holder.stoneName, "【宝石】"
					+ eic.getStone().getName());
		} else {
			ViewUtil.setText(holder.stoneName, "【宝石】无");
		}

		convertView.setOnClickListener(new ClickListener(hic, eic));

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.equipment_item;
	}

	private class ClickListener implements OnClickListener {
		private EquipmentInfoClient eic;
		private HeroInfoClient hic;

		public ClickListener(HeroInfoClient hic, EquipmentInfoClient eic) {
			this.hic = hic;
			this.eic = eic;
		}

		@Override
		public void onClick(View v) {
			new EquipmentDetailWindow().open(eic, hic, false);
		}
	}

	private static class ViewHolder {
		TextView name, state, effect, properties, stoneName;
	}
}
