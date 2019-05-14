package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.OtherUserBattleIdInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.protos.BaseBattleIdInfo;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.PageListView;

public class ReinforceTip extends PageListView {

	private ArrayList<Integer> guildUserId = new ArrayList<Integer>();

	private RichGuildInfoClient rg;

	private int fetchUserIndex = 0;

	public ReinforceTip() {
		setTitle("增援领地");
		setContentTitle("-----请选择要增援的领地-----");
		fetchUserIndex = 0;
	}

	@Override
	public void show() {
		if (!Account.user.hasGuild()) {
			Config.getController().alert("不能增援家族,您还未加入任何家族");
			return;
		}
		super.show();
		this.firstPage();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new FiefAdapter();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		// 获取家族信息
		Account.guildCache.updata(false);
		rg = Account.guildCache.getRichInfoInCache();
		if (rg != null) {
			guildUserId.clear();
			guildUserId.addAll(rg.getMembers());
			// 将族长放第一个 去掉自己
			guildUserId.remove(Integer.valueOf(rg.getGic().getLeader()));
			guildUserId.add(0, Integer.valueOf(rg.getGic().getLeader()));
			guildUserId.remove(Integer.valueOf(Account.user.getId()));
		}

		// 已经取完user了
		if (fetchUserIndex >= guildUserId.size()) {
			resultPage.clearResult();
			resultPage.setTotal(resultPage.getCurIndex());
			return;
		}
		// 每次取5个人
		int end = fetchUserIndex + 5;
		if (end > guildUserId.size()) {
			end = guildUserId.size();
		}

		List<OtherUserBattleIdInfoClient> battleIdInfos = GameBiz.getInstance()
				.userBattleInfoQuery(guildUserId.subList(fetchUserIndex, end));
		fetchUserIndex = end;
		List<Long> fiefIds = new ArrayList<Long>();
		for (OtherUserBattleIdInfoClient o : battleIdInfos) {
			for (BaseBattleIdInfo b : o.getInfos()) {
				if (!fiefIds.contains(b.getBattleid()))
					fiefIds.add(b.getBattleid());
			}
		}

		List<BriefBattleInfoClient> briefBattles = GameBiz.getInstance()
				.briefBattleInfoQuery(fiefIds);

		for (Iterator<BriefBattleInfoClient> iterator = briefBattles.iterator(); iterator
				.hasNext();) {
			BriefBattleInfoClient briefBattle = (BriefBattleInfoClient) iterator
					.next();
			BriefFiefInfoClient briefFief = briefBattle.getBfic();
			if (null == briefFief
					|| (!briefFief.canReinforceAttack() && !briefFief
							.canReinforceDefend()))
				iterator.remove();
		}
		resultPage.setResult(briefBattles);
		resultPage.setTotal(Integer.MAX_VALUE);
	}

	@Override
	public void handleItem(Object o) {
		dismiss();
		BriefBattleInfoClient bbic = (BriefBattleInfoClient) o;
		Config.getController().getBattleMap()
				.moveToFief(bbic.getBfic().getId(), true, true);
	}

	private class FiefAdapter extends ObjectAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (null == convertView) {
				convertView = Config.getController().inflate(getLayoutId());
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.battleTypeIcon = (ImageView) convertView
						.findViewById(R.id.battleTypeIcon);
				holder.scaleName = (TextView) convertView
						.findViewById(R.id.scaleName);
				holder.attackName = (TextView) convertView
						.findViewById(R.id.attackName);
				holder.defendName = (TextView) convertView
						.findViewById(R.id.defendName);
				holder.state = (TextView) convertView.findViewById(R.id.state);
				holder.addr = (TextView) convertView.findViewById(R.id.addr);
				holder.troop = (TextView) convertView.findViewById(R.id.troop);
				holder.position = (TextView) convertView
						.findViewById(R.id.position);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			BriefBattleInfoClient bbic = (BriefBattleInfoClient) getItem(position);
			BriefFiefInfoClient bfic = bbic.getBfic();
			// 领地图标
			new ViewImgScaleCallBack(bfic.getIcon(), holder.icon,
					Constants.ARM_ICON_WIDTH, Constants.ARM_ICON_HEIGHT);

			ViewUtil.setVisible(holder.battleTypeIcon);
			if (BriefUserInfoClient.isNPC(bfic.getAttackerId())) {
				ViewUtil.setImage(holder.battleTypeIcon, R.drawable.battle_emo);
			} else if (bbic.getType() == BriefBattleInfoClient.BATTLE_TYPE_OCCUPY) {
				ViewUtil.setImage(holder.battleTypeIcon,
						R.drawable.battle_zhanling);
			} else if (bbic.getType() == BriefBattleInfoClient.BATTLE_TYPE_PLUNDER) {
				ViewUtil.setImage(holder.battleTypeIcon,
						R.drawable.battle_lueduo);
			} else {
				ViewUtil.setGone(holder.battleTypeIcon);
			}

			// 领地规模名称和领地名称
			ViewUtil.setText(holder.scaleName, bfic.getName());

			new AddrLoader(holder.addr, TileUtil.fiefId2TileId(bfic.getId()),
					AddrLoader.TYPE_MAIN);

			ViewUtil.setText(holder.position,
					" (" + TileUtil.uniqueMarking(bfic.getId()) + ")");

			// 判断家族成员是进攻方还是防守方
			if (bfic.canReinforceAttack()) {
				ViewUtil.setRichText(holder.attackName, StringUtil
						.color(bbic.getAttackerUser().getHtmlNickName(),
								R.color.k7_color7));
				ViewUtil.setText(holder.defendName, bbic.getDefendUser()
						.getNickName());
				ViewUtil.setRichText(holder.state,
						StringUtil.color("协助进攻", R.color.k7_color8));
			} else if (bfic.canReinforceDefend()) {
				ViewUtil.setText(holder.attackName, bbic.getAttackerUser()
						.getNickName());
				ViewUtil.setRichText(holder.defendName, StringUtil.color(bbic
						.getDefendUser().getHtmlNickName(), R.color.k7_color7));
				ViewUtil.setRichText(holder.state,
						StringUtil.color("协助防守", R.color.k7_color12));
			} else {
				ViewUtil.setText(holder.attackName, bbic.getAttackerUser()
						.getNickName());
				ViewUtil.setText(holder.defendName, bbic.getDefendUser()
						.getNickName());
				ViewUtil.setText(holder.state, " ");
			}

			// 兵力
			StringBuilder buf = new StringBuilder("兵力:")
					.append(CalcUtil.turnToHundredThousand(bfic.getUnitCount()))
					.append("(").append("将:").append(bfic.getHeroCount())
					.append(")");
			ViewUtil.setText(holder.troop, buf.toString());
			return convertView;
		}

		@Override
		public void setViewDisplay(View v, Object o, int index) {
		}

		@Override
		public int getLayoutId() {
			return R.layout.reinforce_fief_item;
		}

	}

	private static class ViewHolder {
		ImageView icon, battleTypeIcon;
		TextView scaleName, attackName, defendName, addr, troop, state,
				position;
	}

	// @Override
	// protected void updateUI() {
	// super.updateUI();
	// dealwithEmptyAdpter();
	// }

	@Override
	protected String getEmptyShowText() {
		return "当前无家族成员需要增援";
	}
}
