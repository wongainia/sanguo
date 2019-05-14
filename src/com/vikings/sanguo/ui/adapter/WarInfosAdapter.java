package com.vikings.sanguo.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BattleHeroInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.FiefProp;
import com.vikings.sanguo.model.HeroArmPropClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.MoveTroopInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.ExUserTroopEffectInfo;
import com.vikings.sanguo.protos.HERO_ROLE;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfos;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.TroopDetailTip;
import com.vikings.sanguo.ui.listener.ShowOtherCastleClickListener;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class WarInfosAdapter extends BaseExpandableListAdapter {

	protected List<MoveTroopInfoClient> moveTroopInfoClients = new ArrayList<MoveTroopInfoClient>();
	private ExpandableListView list;
	private UserTroopEffectInfos userTroopEffectInfos;
	private FiefProp fiefProp;
	private List<BattleHeroInfoClient> BattleHICs;

	/**
	 * 
	 * @param list
	 * @param userTroopEffectInfo
	 * @param fiefProp
	 *            显示士兵属性用，只在防守方设置该值，进攻不能设置
	 */
	public WarInfosAdapter(ExpandableListView list,
			UserTroopEffectInfos userTroopEffectInfos, FiefProp fiefProp) {
		this.list = list;
		this.userTroopEffectInfos = userTroopEffectInfos;
		this.fiefProp = fiefProp;
	}

	public List<BattleHeroInfoClient> getHeroInfo() {
		return BattleHICs;
	}

	public void setHeroInfos(List<BattleHeroInfoClient> BattleHICs) {
		this.BattleHICs = BattleHICs;
	}

	public void addItem(MoveTroopInfoClient info) {
		this.moveTroopInfoClients.add(info);
	}

	public boolean hasNull() {
		if (!ListUtil.isNull(BattleHICs)) {
			for (BattleHeroInfoClient battleHeroInfoClient : BattleHICs) {
				if (battleHeroInfoClient.getHeroInfo() == null
						|| battleHeroInfoClient.getHeroInfo().getId() == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public void setItem(List<MoveTroopInfoClient> ls) {
		if (!ListUtil.isNull(moveTroopInfoClients))
			moveTroopInfoClients.clear();

		moveTroopInfoClients.addAll(ls);
	}

	public void clear() {
		moveTroopInfoClients = new ArrayList<MoveTroopInfoClient>();
	}

	public int count() {
		return moveTroopInfoClients.size();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (groupPosition > moveTroopInfoClients.size() - 1)
			return null;
		if (childPosition > moveTroopInfoClients.get(groupPosition)
				.getTroopInfo().size() - 1)
			return null;

		return moveTroopInfoClients.get(groupPosition).getTroopInfo()
				.get(childPosition);
	}

	public MoveTroopInfoClient getParent(int groupPosition) {
		if (groupPosition > moveTroopInfoClients.size() - 1)
			return null;
		return moveTroopInfoClients.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = (ViewGroup) Config.getController().inflate(
					R.layout.war_info_list);
		}

		int userId = -1;
		Object obj = getGroup(groupPosition);
		if (null != obj) {
			MoveTroopInfoClient mtic = (MoveTroopInfoClient) obj;
			userId = mtic.getUserid();
		}

		ArmInfoClient armInfo = (ArmInfoClient) getChild(groupPosition,
				childPosition);
		if (null != armInfo) {
			ViewUtil.setText(convertView, R.id.troopName, armInfo.getProp()
					.getName());

			ImageView img = (ImageView) convertView.findViewById(R.id.icon);
			new ViewImgScaleCallBack(armInfo.getProp().getIcon(), img,
					Constants.COMMON_ICON_WIDTH, Constants.COMMON_ICON_HEIGHT);

			ViewUtil.setText(convertView, R.id.troopSum,
					"×" + armInfo.getCount());

			convertView.setOnClickListener(new ArmInfoListener(armInfo,
					getTroopEffectInfo(userId)));
		}

		if (!ListUtil.isNull(BattleHICs)) {
			TroopProp tp = armInfo.getProp();
			if (HeroArmPropClient.isStrength(BattleHICs.get(0).getHeroInfo()
					.getArmPropClient(), tp)) {
				ViewUtil.setVisible(convertView, R.id.armType);
				ViewUtil.setImage(convertView, R.id.armType, tp.getSmallIcon());
			} else
				ViewUtil.setGone(convertView, R.id.armType);
		} else
			ViewUtil.setGone(convertView, R.id.armType);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition > moveTroopInfoClients.size() - 1)
			return 0;
		return moveTroopInfoClients.get(groupPosition).getTroopInfo().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (groupPosition > moveTroopInfoClients.size() - 1)
			return null;
		return moveTroopInfoClients.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return moveTroopInfoClients.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (ViewGroup) Config.getController().inflate(
					R.layout.war_troop_info);
		}

		final MoveTroopInfoClient moveTroopInfo = (MoveTroopInfoClient) getGroup(groupPosition);

		// View heroLayout = convertView.findViewById(R.id.heroLayout);
		if (moveTroopInfo != null
				&& groupPosition == 0
				&& !ListUtil.isNull(BattleHICs)
				&& (moveTroopInfo.getRole() == MoveTroopInfoClient.TROOP_ROLE_ATTACK_MAIN_FORCE || moveTroopInfo
						.getRole() == MoveTroopInfoClient.TROOP_ROLE_DEFEND_MAIN_FORCE)) {
			ViewUtil.setVisible(convertView.findViewById(R.id.heroLayout));
			dealWithHeros(convertView, moveTroopInfo.getUser(), BattleHICs);
		} else {
			ViewUtil.setGone(convertView.findViewById(R.id.heroLayout));
			ViewUtil.setGone(convertView.findViewById(R.id.layout));
			ViewUtil.setGone(convertView.findViewById(R.id.layout1));
			ViewUtil.setGone(convertView.findViewById(R.id.layout2));
			ViewUtil.setGone(convertView.findViewById(R.id.layout3));
		}

		if (moveTroopInfo != null) {
			// if (hasHero(moveTroopInfo)) { // 有将领
			// ViewUtil.setWarHeroInfo(convertView, heroInfo,
			// moveTroopInfo.getUser());
			// } else {
			// ViewUtil.setGone(heroLayout);
			// }

			View masterName = convertView.findViewById(R.id.masterName);
			if (Account.user.getId() == moveTroopInfo.getUserid()) {
				ViewUtil.setRichText(masterName, (groupPosition + 1) + " 我自己");
			} else {
				BriefUserInfoClient briefUser = moveTroopInfo.getUser();
				if (null != briefUser) {
					if (briefUser.isOtherUser()) {
						ViewUtil.setRichText(masterName, (groupPosition + 1)
								+ " <u>"
								+ moveTroopInfo.getUser().getHtmlNickName()
								+ "</u>");
					} else {
						ViewUtil.setRichText(masterName, (groupPosition + 1)
								// + " " +
								// moveTroopInfo.getUser().getNickName());
								+ " "
								+ moveTroopInfo.getUser().getHtmlNickName());
					}
				} else
					ViewUtil.setRichText(masterName, (groupPosition + 1) + " ");
			}

			convertView.findViewById(R.id.title).setOnClickListener(
					new ShowOtherCastleClickListener(moveTroopInfo.getUser()));

			int totalAmount = TroopUtil.countArm(moveTroopInfo.getTroopInfo());
			String amount = "";
			if (totalAmount > 100000)
				amount = String.valueOf(totalAmount / 10000) + "万";
			else
				amount = String.valueOf(totalAmount);

			if (moveTroopInfo.getRole() == MoveTroopInfoClient.TROOP_ROLE_ATTACK_MAIN_FORCE
					|| moveTroopInfo.getRole() == MoveTroopInfoClient.TROOP_ROLE_DEFEND_MAIN_FORCE)
				ViewUtil.setRichText(convertView, R.id.troopType, "主攻" + amount);
			else {
				// if (hasHero(moveTroopInfo) && 0 == totalAmount)
				// ViewUtil.setRichText(convertView, R.id.troopType, "增援将领");
				// else
				ViewUtil.setRichText(convertView, R.id.troopType, "援军" + amount);
			}
		}

		return convertView;
	}

	public static void dealWithHeros(View convertView,
			final BriefUserInfoClient user,
			List<BattleHeroInfoClient> BattleHICs) {
		ViewUtil.setGone(convertView.findViewById(R.id.heroLayout));
		ViewUtil.setGone(convertView.findViewById(R.id.heroLayout1));
		ViewUtil.setGone(convertView.findViewById(R.id.heroLayout2));
		ViewUtil.setGone(convertView.findViewById(R.id.heroLayout3));
		switch (BattleHICs.size()) {
		case 3:
			fillHeroItem(convertView.findViewById(R.id.heroLayout1),
					convertView, user, BattleHICs.get(2));

		case 2:
			fillHeroItem(convertView.findViewById(R.id.heroLayout2),
					convertView, user, BattleHICs.get(1));
		case 1:
			fillHeroItem(convertView.findViewById(R.id.heroLayout3),
					convertView, user, BattleHICs.get(0));
			break;
		default:
			break;
		}
	}

	private static void fillHeroItem(View heroLayout, View convertView,
			final BriefUserInfoClient user,
			final BattleHeroInfoClient mBattleHeroInfoClient) {
		if (mBattleHeroInfoClient.getRole() == HERO_ROLE.HERO_ROLE_ATTACK_MAIN
				.getNumber()
				|| mBattleHeroInfoClient.getRole() == HERO_ROLE.HERO_ROLE_DEFEND_MAIN
						.getNumber()) {
			ViewUtil.setWarHeroInfos(convertView,
					convertView.findViewById(R.id.heroLayout),
					mBattleHeroInfoClient, user);
		} else if (mBattleHeroInfoClient.getRole() == HERO_ROLE.HERO_ROLE_ATTACK_ASSIST
				.getNumber()
				|| mBattleHeroInfoClient.getRole() == HERO_ROLE.HERO_ROLE_DEFEND_ASSIST
						.getNumber()) {
			ViewUtil.setWarHeroInfos(convertView, heroLayout,
					mBattleHeroInfoClient, user);
		}
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		for (int i = 0; i < this.count(); i++)
			list.expandGroup(i);
	}

	private class ArmInfoListener implements OnClickListener {
		private ArmInfoClient armInfo;
		private UserTroopEffectInfo userTroopEffectInfo;

		public ArmInfoListener(ArmInfoClient armInfo,
				UserTroopEffectInfo userTroopEffectInfo) {
			this.armInfo = armInfo;
			this.userTroopEffectInfo = userTroopEffectInfo;
			if(this.userTroopEffectInfo == null)
			{
				this.userTroopEffectInfo = new UserTroopEffectInfo();
			}
		}

		@Override
		public void onClick(View v) {
			if (armInfo == null)
				return;

			if (!ListUtil.isNull(BattleHICs)) {
				List<OtherHeroInfoClient> otherHeroInfoClients = new ArrayList<OtherHeroInfoClient>();
				for (BattleHeroInfoClient battleHeroInfoClient : BattleHICs) {
					otherHeroInfoClients
							.add(battleHeroInfoClient.getHeroInfo());
				}
				new TroopDetailTip()
						.show(armInfo.getProp(), userTroopEffectInfo,
								otherHeroInfoClients,
								fiefProp != null ? fiefProp.getDefenseSkill()
										: 0, null);
			} else {
				new TroopDetailTip().show(armInfo.getProp(),
						userTroopEffectInfo, (List<HeroInfoClient>) null,
						fiefProp != null ? fiefProp.getDefenseSkill() : 0);
			}

		}
	}

	public UserTroopEffectInfo getTroopEffectInfo(int userId) {
		if (null == userTroopEffectInfos || !userTroopEffectInfos.hasInfos())
			return null;

		for (ExUserTroopEffectInfo it : userTroopEffectInfos.getInfosList()) {
			if (it.hasInfo() && it.getInfo().getUserid() == userId)
				return it.getInfo();
		}

		return null;
	}
}
