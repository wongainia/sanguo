package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.HolyCategory;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.FiefDetailTopInfo;

public class SearchHolyFiefAdapter extends ObjectAdapter {
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.scaleName = (TextView) convertView
					.findViewById(R.id.scaleName);
			holder.country = (TextView) convertView.findViewById(R.id.country);
			holder.troop = (TextView) convertView.findViewById(R.id.troop);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.fiefState = (ImageView) convertView
					.findViewById(R.id.fiefState);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HolyProp hp = (HolyProp) getItem(position);
		BriefFiefInfoClient bfic = hp.getbFiefInfoClient();
		String stateIcon = null;
		if (bfic != null) {
			stateIcon = bfic.getStateIcon();
		}
		if (null != stateIcon) {
			ViewUtil.setVisible(holder.fiefState);
			ViewUtil.setImage(holder.fiefState, stateIcon);
		} else {
			ViewUtil.setGone(holder.fiefState);
		}

		// 领地图标
		String fiefIconName = hp.getIcon();
		if (StringUtil.isNull(fiefIconName))
			fiefIconName = "fief_wasteland.png";
		new ViewImgScaleCallBack(fiefIconName, holder.icon,
				Constants.ARM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ARM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

		// 领地规模名称和领地名称
		ViewUtil.setText(holder.scaleName, hp.getName());
		if (type == HolyCategory.SHENJI || HolyCategory.SHENGDU == type) {
			if (bfic == null || null == bfic.getLord()) {
				ViewUtil.setText(holder.troop, "领主：无");
			} else {
				StringBuilder buf = new StringBuilder("领主：").append(bfic
						.getLord().getNickName());
				ViewUtil.setText(holder.country, hp.getName());

				ViewUtil.setText(holder.troop, buf.toString());

				// 兵力
				if (bfic.getLord().isNPC()) {
					// 是npc 取配置
					StringBuilder troopBuf = new StringBuilder("兵力:")
							.append(hp.getDefendTroopCnt()).append("( 将:")
							.append(hp.getDefendHeroCnt()).append(")");

					ViewUtil.setRichText(holder.state, troopBuf.toString());
				} else {
					ViewUtil.setRichText(holder.state,
							"兵力:" + StringUtil.getTroopDesc(bfic));
				}

			}

		} else {
			// 兵力
			StringBuilder buf = new StringBuilder("兵力:").append(hp
					.getTroopCnt());
			ViewUtil.setRichText(holder.troop, buf.toString());
			ViewUtil.setText(holder.country, hp.getName() + "("
					+ hp.getCountry().getName() + ")");
			if (hp.getState() == BattleStatus.BATTLE_STATE_SURROUND) {
				ViewUtil.setRichText(holder.state, StringUtil.color("可讨伐，参加人数"
						+ hp.getNum() + "/" + hp.getMaxReinforceUser(),
						R.color.color19));
			} else if (hp.getState() == BattleStatus.BATTLE_STATE_FINISH) {
				int time = FiefDetailTopInfo.getBattleTime(hp.getCdTime());
				if (time > 0) {
					ViewUtil.setRichText(holder.state,
							StringUtil.color("已被击溃，请等待刷新", R.color.color24));
				} else {
					ViewUtil.setRichText(
							holder.state,
							StringUtil.color("可讨伐，参加人数" + hp.getNum() + "/"
									+ hp.getMaxReinforceUser(), R.color.color19));
				}

			}

		}

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.search_holy_item;
	}

	private static class ViewHolder {
		ImageView icon;
		TextView state, troop, country, scaleName;
		ImageView fiefState;
	}

}
