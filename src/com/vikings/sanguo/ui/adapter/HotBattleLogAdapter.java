package com.vikings.sanguo.ui.adapter;

import java.util.Date;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleHotInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.thread.AddrMutiLoader;
import com.vikings.sanguo.ui.alert.HistoryBattleLogTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HotBattleLogAdapter extends ObjectAdapter {

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.result = (TextView) convertView.findViewById(R.id.result);
			holder.logDesc = (TextView) convertView.findViewById(R.id.logDesc);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		BattleHotInfoClient bhic = (BattleHotInfoClient) getItem(position);
		BriefUserInfoClient attacker = bhic.getAttacker();
		BriefUserInfoClient defender = bhic.getDefender();

		String timeStr = DateUtil.db2MinuteFormat.format(new Date(bhic
				.getTime() * 1000L));

		ViewUtil.setText(holder.time, timeStr);

		String result = (bhic.getResult() == 1 ? "成功击溃了敌军，缴获了大量战利品！" : "遭遇惨败，铩羽而归。");
		BriefUserInfoClient user = (bhic.getResult() == 1 ? attacker : defender);
		if (null != user)
			ViewUtil.setRichText(holder.name,
					StringUtil.color(user.getHtmlNickName(), R.color.color23));
		else
			ViewUtil.setText(holder.name, "");

		ViewUtil.setText(holder.result, "胜利"); // result

		StringBuilder builder = new StringBuilder();
		boolean isHoly = CacheMgr.holyPropCache.isHoly(bhic.getFiefid()); // isHoly
		// 圣都
		String holyFiefName = CacheMgr.holyPropCache.getFiefName(bhic
				.getFiefid());

		if (BriefUserInfoClient.isNPC(attacker.getId())) {
			if (null != defender)
				builder.append(StringUtil.color(defender.getHtmlNickName(), R.color.color23));

			if (!StringUtil.isNull(bhic.getDefendCountry()))
				builder.append(StringUtil.color("(" + bhic.getDefendCountry()
						+ ")", R.color.color23));

			builder.append("率领").append(bhic.getTotalDefTroop())
					.append("大军，开启了");

			if (!StringUtil.isNull(holyFiefName)) {
				builder.append(StringUtil.color(holyFiefName,
						R.color.color24));

				if (isHoly) {
					try {
						HolyProp hp = (HolyProp) CacheMgr.holyPropCache
								.get(bhic.getFiefid());
						builder.append(
								StringUtil.color(" 的 ", R.color.color24))
								.append(StringUtil.color(hp.getEvilDoorName(),
										R.color.color24));
					} catch (GameException e) {
						e.printStackTrace();
					}
				}

				builder.append(",").append(
						bhic.getResult() == 1 ? "遭遇惨败，铩羽而归。" : "成功击溃了敌军，缴获了大量战利品！");
			}
		} else {
			builder.append(StringUtil.color(attacker.getHtmlNickName(), R.color.color23));

			if (null != bhic.getAttackCountry())
				builder.append(StringUtil.color("(" + bhic.getAttackCountry()
						+ ")", R.color.color23));

			builder.append("率领").append(bhic.getTotalAtkTroop()).append("大军，对");

			builder.append(StringUtil.color(defender.getHtmlNickName(), R.color.color23));
			if (!StringUtil.isNull(bhic.getDefendCountry())) {
				builder.append(StringUtil.color("(" + bhic.getDefendCountry()
						+ ")", R.color.color23));
			}

			builder.append("的").append(holyFiefName);

			if (!isHoly)
				builder.append(StringUtil.color("<tile>", R.color.color24));

			builder.append(
					StringUtil.color(
							"(" + TileUtil.uniqueMarking(bhic.getFiefid())
									+ ")", R.color.color24)).append("发起了")
					.append(bhic.getBattleTypeName()).append(",");

			builder.append(result);
		}

		long[] tiles = new long[1];
		tiles[0] = TileUtil.fiefId2TileId(bhic.getFiefid());

		new AddrMutiLoader(tiles, holder.logDesc, builder.toString());
		convertView.setOnClickListener(new ClickListener(bhic));
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	@Override
	public int getLayoutId() {
		return R.layout.hot_battle_log_item;
	}

	private class ClickListener implements OnClickListener {
		private BattleHotInfoClient bhic;

		public ClickListener(BattleHotInfoClient bhic) {
			this.bhic = bhic;
		}

		@Override
		public void onClick(View v) {
			new HistoryBattleLogTip(bhic).show();
		}
	}

	private class ViewHolder {
		TextView time, name, result, logDesc;
	}
}
