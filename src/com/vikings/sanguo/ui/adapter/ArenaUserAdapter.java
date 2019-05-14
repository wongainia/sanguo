package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArenaReward;
import com.vikings.sanguo.model.ArenaUserRankInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.window.ArenaReviewTroopWindow;
import com.vikings.sanguo.ui.window.ArenaTroopContrastWindow;
import com.vikings.sanguo.ui.window.ArenaTroopSetWindow;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ArenaUserAdapter extends ObjectAdapter {
	private List<ArenaUserRankInfoClient> topUsers;
	private List<ArenaUserRankInfoClient> attackUsers;

	// 传入数据，用于操作后回退刷新界面
	public ArenaUserAdapter(List<ArenaUserRankInfoClient> topUsers,
			List<ArenaUserRankInfoClient> attackUsers) {
		this.topUsers = topUsers;
		this.attackUsers = attackUsers;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.layout = convertView.findViewById(R.id.layout);
			holder.iconLayout = (ViewGroup) convertView
					.findViewById(R.id.iconLayout);
			holder.heroLayout = (ViewGroup) convertView
					.findViewById(R.id.heroLayout);
			holder.descLayout = (ViewGroup) convertView
					.findViewById(R.id.descLayout);
			holder.gap = convertView.findViewById(R.id.gap);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.attack = (ImageView) convertView.findViewById(R.id.attack);
			holder.exploit = (TextView) convertView.findViewById(R.id.exploit);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ArenaUserRankInfoClient auric = (ArenaUserRankInfoClient) getItem(position);
		final BriefUserInfoClient briefUser = auric.getUser();
		if (null != briefUser) {
			ViewUtil.setVisible(holder.iconLayout);

			IconUtil.setUserIcon(holder.iconLayout, briefUser,
					getRank(auric.getRank(), briefUser.getId()));

			ArenaReward arenaReward = CacheMgr.arenaRewardCache
					.getArenaReward(auric.getRank());

			if (briefUser.getId() == Account.user.getId()) { // 自己
				ViewUtil.setText(holder.name, "我自己");
				ViewUtil.setGone(holder.attack);

				if (auric.hasRank()) {
					ViewUtil.setVisible(holder.heroLayout);
					ViewUtil.setGone(holder.descLayout);

					ViewUtil.setRichText(holder.exploit, "+" + "#exploit#"
							+ getExploit(auric) + "/" + getTime(), true);

					setHeros(holder.heroLayout, auric.getArenaHeros());
				} else {
					ViewUtil.setGone(holder.heroLayout);
					ViewUtil.setVisible(holder.descLayout);
					ViewUtil.setText(holder.exploit, "");
					ViewUtil.setText(
							holder.descLayout.findViewById(R.id.desc1),
							"你需要进行一场定级赛来获得参赛资格");
					if (Account.myLordInfo.isArenaConfig())
						ViewUtil.setText(
								holder.descLayout.findViewById(R.id.desc2), "");
					else
						ViewUtil.setRichText(
								holder.descLayout.findViewById(R.id.desc2),
								"(请配置将领并保存后方可挑战)");
				}
				holder.iconLayout.setOnClickListener(null);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!Account.myLordInfo.isArenaConfig())
							new ArenaTroopSetWindow().open();
						else
							new ArenaReviewTroopWindow().open();

					}
				});
			} else { // 别人
				ViewUtil.setVisible(holder.heroLayout);
				ViewUtil.setGone(holder.descLayout);
				if (briefUser.isNPC()) {
					ViewUtil.setText(holder.name, briefUser.getNickName());
					holder.iconLayout.setOnClickListener(null);
				} else {
					ViewUtil.setText(holder.name, briefUser.getNickName()
							+ " (" + briefUser.getCountryName() + ")");
					holder.iconLayout.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Config.getController().showCastle(briefUser);

						}
					});
				}
				int arenaLvl = CacheMgr.dictCache
						.getDictInt(Dict.TYPE_ARENA, 1);
				if (auric.canAttack() && Account.user.getLevel() >= arenaLvl) {
					ViewUtil.setVisible(holder.attack);
				} else {
					ViewUtil.setGone(holder.attack);
				}

				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (!Account.isTroopReady()) {
							Config.getController().alert("请先配置你的队伍！",
									new CallBack() {

										@Override
										public void onCall() {
											new ArenaTroopSetWindow().open();
										}
									});
							return;
						}

						new ArenaTroopContrastWindow().open(auric, topUsers,
								attackUsers);
					}
				});

				if (null != arenaReward) {
					ViewUtil.setRichText(holder.exploit, "+" + "#exploit#"
							+ getExploit(auric) + "/" + getTime(), true);
				} else {
					ViewUtil.setText(holder.exploit, "");
				}

				setHeros(holder.heroLayout, auric.getArenaHeros());

			}
		} else {
			ViewUtil.setGone(holder.iconLayout);
			ViewUtil.setGone(holder.heroLayout);
			ViewUtil.setGone(holder.descLayout);
			ViewUtil.setGone(holder.name);
			ViewUtil.setGone(holder.gap);
			ViewUtil.setGone(holder.attack);
			ViewUtil.setGone(holder.exploit);
			convertView.setOnClickListener(null);
		}

		return convertView;
	}

	private void setHeros(ViewGroup viewGroup, List<HeroIdInfoClient> arenaHeros) {
		HeroIdInfoClient main = null;
		HeroIdInfoClient assit1 = null;
		HeroIdInfoClient assit2 = null;
		for (int i = 0; i < arenaHeros.size(); i++) {
			HeroIdInfoClient hiic = arenaHeros.get(i);
			if (hiic.isMainHero()) {
				main = hiic;
			} else {
				if (assit1 == null)
					assit1 = hiic;
				else
					assit2 = hiic;
			}
		}

		setHeroView(viewGroup.getChildAt(0), assit1);
		setHeroView(viewGroup.getChildAt(1), main);
		setHeroView(viewGroup.getChildAt(2), assit2);
	}

	private void setHeroView(View view, HeroIdInfoClient ahiic) {
		if (null == view || null == ahiic)
			ViewUtil.setGone(view);
		else {
			ViewUtil.setVisible(view);
			IconUtil.setHeroIcon(view, ahiic.getHeroProp(),
					ahiic.getHeroQuality(), ahiic.getStar(),
					IconUtil.HERO_ICON_SCALE_SMALL);
		}
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	// 数据字典配置时间 单位秒
	private String getTime() {
		return DateUtil.formatTime(CacheMgr.dictCache.getDictInt(
				Dict.TYPE_ARENA, 4));
	}

	private int getExploit(ArenaUserRankInfoClient auric) {
		return CacheMgr.arenaRewardCache.getSpecificExploit(auric.getRank());
	}

	@Override
	public int getLayoutId() {
		return R.layout.arena_user_item;
	}

	private static class ViewHolder {
		View layout, gap;
		ViewGroup iconLayout, heroLayout, descLayout;
		TextView name, exploit;
		ImageView attack;
	}

	private String getRank(int rank, int userId) {
		if (rank > 0) {
			return "No." + rank;
		} else {
			if (BriefUserInfoClient.isNPC(userId)) {
				return "战场使者";
			} else {
				return "未定级";
			}
		}
	}
}
