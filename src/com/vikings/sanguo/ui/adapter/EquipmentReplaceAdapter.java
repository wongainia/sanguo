package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.EquipmentReplaceResp;
import com.vikings.sanguo.model.EquipEffectClient;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentForgeEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.guide.Step7051;
import com.vikings.sanguo.ui.window.EquipmentWearWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class EquipmentReplaceAdapter extends ObjectAdapter {

	private EquipmentInfoClient equipmentInfoClient;
	private HeroInfoClient hic;

	public EquipmentReplaceAdapter(EquipmentInfoClient equipmentInfoClient,
			HeroInfoClient hic) {
		this.equipmentInfoClient = equipmentInfoClient;
		this.hic = hic;
	}

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
		if (eic.weared()) {
			HeroInfoClient hic = Account.heroInfoCache.get(eic.getHeroId());
			ViewUtil.setRichText(holder.state, hic.getColorTypeName() + " "
					+ hic.getColorHeroName());
		} else {
			ViewUtil.setRichText(holder.state,
					StringUtil.color("未装备", R.color.color19));
		}

		EquipmentEffect ee = CacheMgr.equipmentEffectCache.getEquipmentEffect(
				eic.getEquipmentId(), (byte) eic.getQuality(), eic.getLevel());

		if (null != equipmentInfoClient) {
			EquipmentEffect oldee = CacheMgr.equipmentEffectCache
					.getEquipmentEffect(equipmentInfoClient.getEquipmentId(),
							(byte) equipmentInfoClient.getQuality(),
							equipmentInfoClient.getLevel());

			StringBuilder buf = new StringBuilder("【属性】");
			int oldvalue = 0;
			int newValue = 0;
			if (oldee != null)
				oldvalue = oldee.getEffectValue(equipmentInfoClient.getLevel());
			if (null != ee) {
				newValue = ee.getEffectValue(eic.getLevel());
				buf.append(ee.getEffectTypeName()
						+ ee.getEffectValue(eic.getLevel()));
			}
			int dif = newValue - oldvalue;

			if (dif >= 0) {
				buf.append(StringUtil.color("(↑" + dif + ")", R.color.color19));
			} else {
				buf.append(StringUtil.color("(↓" + (-1 * dif) + ")",
						R.color.color11));
			}

			ViewUtil.setRichText(holder.properties, buf.toString());
		} else {
			if (null != ee) {
				ViewUtil.setRichText(
						holder.properties,
						"【属性】" + ee.getEffectTypeName()
								+ ee.getEffectValue(eic.getLevel()));
			} else {
				ViewUtil.setText(holder.properties, "【属性】无");
			}
		}

		EquipEffectClient effect = eic.getEffect();
		if (effect != null && effect.getType() > 0) {
			EquipmentForgeEffect equipEffect = CacheMgr.getEffectById(effect
					.getType());
			ViewUtil.setText(holder.effect,
					"【特效】" + equipEffect.getEffectDesc());
		} else {
			ViewUtil.setText(holder.effect, "【特效】无");
		}

		if (eic.hasStone()) {
			ViewUtil.setText(holder.stoneName, "【宝石】"
					+ eic.getStone().getName());
		} else {
			ViewUtil.setText(holder.stoneName, "【宝石】无");
		}

		convertView.setOnClickListener(new ClickListener(eic));

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.equipment_replace_item;
	}

	private class ClickListener implements OnClickListener {
		private EquipmentInfoClient eic;

		public ClickListener(EquipmentInfoClient eic) {
			this.eic = eic;
		}

		@Override
		public void onClick(View v) {
			new EquipmentReplaceInvoker(eic).start();
		}

	}

	private class EquipmentReplaceInvoker extends BaseInvoker {
		private EquipmentInfoClient eic;
		private String op = "";
		private EquipmentReplaceResp resp;

		private EquipmentReplaceInvoker(EquipmentInfoClient eic) {
			this.eic = eic;
			if (null == equipmentInfoClient)
				op = "添加装备";
			else
				op = "替换装备";
		}

		@Override
		protected String loadingMsg() {
			return op;
		}

		@Override
		protected String failMsg() {
			return op + "失败";
		}

		@Override
		protected void fire() throws GameException {
			if (null != equipmentInfoClient) {

			}
			resp = GameBiz.getInstance().equipmentReplace(eic.getId(),
					eic.getHeroId(), hic.getId());
			eic.putOn(hic.getId());
			Account.equipmentCache.remove(eic);
		}

		@Override
		protected void onOK() {
			ctr.alert(op + "成功",
					EquipmentWearWindow.step701_guide == false ? null
							: new CallBack() {
								@Override
								public void onCall() {
									new Step7051().run();
								}
							});
			EquipmentWearWindow.step701_guide = false;
			ctr.goBack();
		}
	}

	private static class ViewHolder {
		TextView name, state, effect, properties, stoneName;
	}
}
