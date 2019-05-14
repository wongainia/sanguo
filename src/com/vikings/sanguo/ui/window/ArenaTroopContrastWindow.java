package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.ArenaAttackInvoker;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.Constants;
import com.vikings.sanguo.model.ArenaUserRankInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroInit;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroQuality;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.ShopData;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ArenaTroopAdapter;
import com.vikings.sanguo.ui.alert.MsgConfirm;
import com.vikings.sanguo.ui.listener.OtherHeroClickListener;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class ArenaTroopContrastWindow extends CustomPopupWindow {
	private ArenaUserRankInfoClient enemy;
	private OtherUserClient ouc;
	private ListView atkList, defList;
	private List<OtherHeroInfoClient> enemyHeros;
	private List<ArenaUserRankInfoClient> topUsers;
	private List<ArenaUserRankInfoClient> attackUsers;
	private BriefUserInfoClient npc;
	private LinearLayout atkHeader, defHeader;

	public void open(ArenaUserRankInfoClient enemy,
			List<ArenaUserRankInfoClient> topUsers,
			List<ArenaUserRankInfoClient> attackUsers) {
		this.enemy = enemy;
		this.topUsers = topUsers;
		this.attackUsers = attackUsers;
		new FetchDataInvoker().start();
	}

	@Override
	protected void init() {
		super.init("队伍比较");
		setLeftBtn("配置队伍", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ArenaTroopSetWindow().open();
			}
		});
		setContent(R.layout.arena_troop_contrast);
		prepareAtkListHeader();
		prepareDefListHeader();
		setBottomBtn();

		if (BriefUserInfoClient.isNPC(enemy.getUserId()))
			controller.alert("定级赛不会消耗免费次数和道具！");
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	private void setAtkTopInfo() {
		View top = findViewById(R.id.atkTop);
		ViewUtil.setRichText(top, R.id.country, Account.user.bref()
				.getCountryName());
		TextView atkName = (TextView) top.findViewById(R.id.atkName);
		ViewUtil.setRichText(atkName, Account.user.getNick());
		ViewUtil.setUnderLine(atkName);
		atkName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.showCastle(Account.user.getId());
			}
		});

		ViewUtil.setRichText(top, R.id.No, Account.myLordInfo.getArenaRank()
				+ "");

		if (Account.guildCache.hasGuide()) {
			TextView guildName = (TextView) top.findViewById(R.id.family);
			ViewUtil.setRichText(guildName, Account.guildCache
					.getRichInfoInCache().getGic().getName());
			ViewUtil.setUnderLine(guildName);
			guildName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new GuildInfoWindow().open(Account.guildCache.getGuildid());
				}
			});
		} else
			ViewUtil.setRichText(top, R.id.family, "无");
	}

	private void setDefTopInfo() {
		View top = findViewById(R.id.defTop);
		ViewUtil.setRichText(top, R.id.country, ouc.bref().getCountryName());
		TextView atkName = (TextView) top.findViewById(R.id.atkName);
		ViewUtil.setRichText(atkName, ouc.getNick());
		ViewUtil.setUnderLine(atkName);
		atkName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.showCastle(ouc.getId());
			}
		});

		ViewUtil.setRichText(top, R.id.No, enemy.getRank() + "");

		if (null != ouc.getBgic() && ouc.getGuildid() > 0) {
			TextView guildName = (TextView) top.findViewById(R.id.family);
			ViewUtil.setRichText(guildName, ouc.getBgic().getName());
			ViewUtil.setUnderLine(guildName);
			guildName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new GuildInfoWindow().open(ouc.getGuildid());
				}
			});
		} else
			ViewUtil.setRichText(top, R.id.family, "无");
	}

	private void setNPCDefTopInfo() {
		View top = findViewById(R.id.defTop);
		ViewUtil.setRichText(top, R.id.atkName, npc.getNickName());
		ViewUtil.setRichText(top, R.id.No, enemy.getRank() + "");
		ViewUtil.setRichText(top, R.id.family, "无");
	}

	private void prepareAtkListHeader() {
		atkList = (ListView) findViewById(R.id.atkList);
		atkHeader = new LinearLayout(controller.getUIContext());
		atkHeader.setOrientation(LinearLayout.VERTICAL);
		if (!ListUtil.isNull(Account.myLordInfo.getArenaHeroInfos())) {
			for (HeroIdInfoClient it : Account.myLordInfo.getArenaHeroInfos()) {
				View heroIcon = controller.inflate(R.layout.arena_hero_item);
				atkHeader.addView(heroIcon);
			}
		}
		atkList.addHeaderView(atkHeader);
	}

	private void refreshAtkListHeader() {
		View title = findViewById(R.id.atkTitle);
		ViewUtil.setText(title, R.id.masterName, "我方兵力");
		ViewUtil.setText(title, R.id.troopType,
				"" + TroopUtil.countArm(Account.myLordInfo.getArenaTroopInfo()));

		if (!ListUtil.isNull(Account.myLordInfo.getArenaHeroInfos())) {
			List<HeroIdInfoClient> heros = Account.myLordInfo
					.getArenaHeroInfos();
			Collections.sort(heros, new Comparator<HeroIdInfoClient>() {
				@Override
				public int compare(HeroIdInfoClient obj1, HeroIdInfoClient obj2) {
					if (obj1.isMainHero() && !obj2.isMainHero())
						return -1;
					else if (!obj1.isMainHero() && obj2.isMainHero())
						return 1;
					else
						return 0;
				}
			});

			for (int i = 0; i < heros.size(); i++) {
				HeroIdInfoClient it = heros.get(i);
				View heroIcon = atkHeader.getChildAt(i);
				if (null != heroIcon) {
					HeroInfoClient hic = Account.heroInfoCache.get(it.getId());

					IconUtil.setHeroIconScale(heroIcon, hic);
					ViewUtil.setRichText(heroIcon, R.id.heroTypeName,
							hic.getColorTypeName());
					ViewUtil.setRichText(heroIcon, R.id.heroName, StringUtil
							.color(hic.getHeroProp().getName(), hic
									.getHeroQuality().getColor()));
					ViewUtil.setText(heroIcon, R.id.heroLv,
							"Lv:" + hic.getLevel());
					heroIcon.setOnClickListener(new OwnHeroClickListerner(hic));
				}
			}
		}
	}

	private void prepareDefListHeader() {
		defList = (ListView) findViewById(R.id.defList);
		defHeader = new LinearLayout(controller.getUIContext());
		defHeader.setOrientation(LinearLayout.VERTICAL);
		if (!ListUtil.isNull(enemy.getArenaHeros())) {
			for (int i = 0; i < enemy.getArenaHeros().size(); i++) {
				View heroIcon = controller.inflate(R.layout.arena_hero_item);
				defHeader.addView(heroIcon);
			}
		}
		defList.addHeaderView(defHeader);
	}

	private void refeshDefListHeader() {
		View title = findViewById(R.id.defTitle);
		ViewUtil.setText(title, R.id.masterName, "敌方兵力");
		ViewUtil.setText(title, R.id.troopType,
				"" + TroopUtil.countArm(ouc.getLord().getArenaTroopInfo()));

		if (!ListUtil.isNull(enemy.getArenaHeros())) {
			for (int i = 0; i < enemy.getArenaHeros().size(); i++) {
				OtherHeroInfoClient it = getOtherHeroInfoClient(enemy
						.getArenaHeros().get(i));
				View heroIcon = defHeader.getChildAt(i);
				if (null != heroIcon && null != it) {
					IconUtil.setHeroIconScale(heroIcon, it);
					ViewUtil.setRichText(heroIcon, R.id.heroTypeName,
							it.getColorTypeName());
					ViewUtil.setRichText(heroIcon, R.id.heroName, StringUtil
							.color(it.getHeroProp().getName(), it
									.getHeroQuality().getColor()));
					ViewUtil.setText(heroIcon, R.id.heroLv,
							"Lv:" + it.getLevel());
					heroIcon.setOnClickListener(new OtherHeroClickListener(it));
				}
			}
		}
	}

	private OtherHeroInfoClient getOtherHeroInfoClient(HeroIdInfoClient ahiic) {
		if (ListUtil.isNull(enemyHeros) || ahiic == null)
			return null;
		for (OtherHeroInfoClient ohic : enemyHeros) {
			if (ohic.getId() == ahiic.getId())
				return ohic;
		}
		return null;
	}

	private void refreshNPCDefListHeader() {
		View title = findViewById(R.id.defTitle);
		ViewUtil.setText(title, R.id.masterName, "敌方兵力");
		ViewUtil.setText(
				title,
				R.id.troopType,
				""
						+ TroopUtil.countArm(CacheMgr.arenaNPCCache
								.getArms(enemy.getUserId())));

		try {
			if (!ListUtil.isNull(enemy.getArenaHeros())) {
				for (int i = 0; i < enemy.getArenaHeros().size(); i++) {
					HeroIdInfoClient it = enemy.getArenaHeros().get(i);
					View heroIcon = defHeader.getChildAt(i);
					HeroInit heroInit = (HeroInit) CacheMgr.heroInitCache
							.get(it.getSchema());
					HeroProp hp = (HeroProp) CacheMgr.heroPropCache
							.get(heroInit.getHeroId());
					HeroQuality hq = (HeroQuality) CacheMgr.heroQualityCache
							.get(heroInit.getTalent());
					IconUtil.setHeroIcon(heroIcon, hp, hq, heroInit.getStar(),
							true);
					ViewUtil.setRichText(heroIcon, R.id.heroTypeName,
							StringUtil.getHeroTypeName(hq));
					ViewUtil.setRichText(heroIcon, R.id.heroName,
							StringUtil.color(hp.getName(), hq.getColor()));
					ViewUtil.setText(heroIcon, R.id.heroLv,
							"Lv:" + heroInit.getLevel());
				}
			}
		} catch (GameException e) {
			e.printStackTrace();
		}
	}

	private void setAtkList() {
		ArenaTroopAdapter adapter = new ArenaTroopAdapter(
				Account.getUserTroopEffectInfo());
		adapter.addItems(Account.myLordInfo.getArenaTroopInfo());
		adapter.notifyDataSetChanged();
		atkList.setAdapter(adapter);
	}

	private void setDefList() {
		ArenaTroopAdapter adapter = new ArenaTroopAdapter(
				ouc.getTroopEffectInfo());
		adapter.addItems(ouc.getLord().getArenaTroopInfo());
		adapter.notifyDataSetChanged();
		defList.setAdapter(adapter);
	}

	private void setNPCDefList() {
		ArenaTroopAdapter adapter = new ArenaTroopAdapter(null);
		adapter.addItems(CacheMgr.arenaNPCCache.getArms(enemy.getUserId()));
		adapter.notifyDataSetChanged();
		defList.setAdapter(adapter);
	}

	private void setBottomBtn() {
		if (enemy.canAttack()) {
			if (BriefUserInfoClient.isNPC(enemy.getUserId())) { // 定级赛
				setBottomButton("挑战", new OnClickListener() {
					@Override
					public void onClick(View v) {
						handleAttack(true);
					}
				});
			} else {
				int arenaItemCnt = CacheMgr.dictCache.getDictInt(
						Dict.TYPE_ARENA, 3);

				String str = "";
				if (Account.getArenaLeftCount() > 0)
					str = "免费挑战";
				else
					str = "挑战#duel_icon#" + arenaItemCnt;

				setBottomButton(str, new OnClickListener() {
					@Override
					public void onClick(View v) {
						handleAttack(false);
					}
				});
			}
		}
	}

	// 是否定级赛
	private void handleAttack(boolean npc) {
		int arenaLvl = CacheMgr.dictCache.getDictInt(Dict.TYPE_ARENA, 1);
		if (Account.user.getLevel() < arenaLvl) {
			controller.alert("你的等级不够，需要达到" + arenaLvl + "级以上才能进行巅峰战场的战斗！");
			return;
		}

		MsgConfirm confirm = new MsgConfirm("", CustomConfirmDialog.DEFAULT);
		confirm.setMsgTextSize(13);
		if (npc) {
			confirm.setTitle("定级赛");
			confirm.show("定级赛不会消耗免费次数和道具！", cb, null);
		} else {
			int arenaItemCnt = CacheMgr.dictCache
					.getDictInt(Dict.TYPE_ARENA, 3);
			if (Account.getArenaLeftCount() > 0) {
				if (null != cb)
					cb.onCall();
			} else {
				final int itemId = CacheMgr.dictCache.getDictInt(
						Dict.TYPE_ARENA, 2);
				Item item = CacheMgr.getItemByID(itemId);
				if (arenaItemCnt > 0 && null != item) {
					ItemBag ib = Account.store.getItemBag(item);
					if (null == ib || ib.getCount() < arenaItemCnt) {
						confirm.setTitle("购买" + item.getName());
						confirm.setOKText("去商店");
						confirm.show(
								"本次挑战需要" + arenaItemCnt + "个" + item.getName()
										+ "，你的道具不足，是否去商店购买？", new CallBack() {
									@Override
									public void onCall() {
										controller.openShop(ShopWindow.TAB2,
												ShopData.TYPE_TOOL, itemId);
									}
								}, null);
					} else {
						confirm.setTitle("挑战");
						confirm.show(
								"本次挑战需要消耗" + arenaItemCnt + "个"
										+ item.getName() + "，是否进行挑战？", cb, null);
					}
				} else {
					confirm.setTitle("挑战");
					confirm.show("是否进行挑战？", cb, null);
				}
			}
		}
	}

	private CallBack cb = new CallBack() {
		@Override
		public void onCall() {
			new ArenaAttackInvoker(topUsers, attackUsers, enemy.getUserId(),
					enemy.getRank(), Account.myLordInfo.getArenaRank()).start();
			ViewUtil.disableButton(belowBtn);
		}
	};

	@Override
	public void showUI() {
		ViewUtil.setRichText(window, R.id.gradientMsg,
				StringUtil.getArenaFreeTimes());
		setAtkTopInfo();
		refreshAtkListHeader();
		setAtkList();
		if (BriefUserInfoClient.isNPC(enemy.getUserId())) {
			setNPCDefTopInfo();
			refreshNPCDefListHeader();
			setNPCDefList();
		} else {
			setDefTopInfo();
			refeshDefListHeader();
			setDefList();
		}
		super.showUI();
	}

	private class FetchDataInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "加载数据";
		}

		@Override
		protected String failMsg() {
			return "加载数据失败";
		}

		@Override
		protected void fire() throws GameException {
			if (BriefUserInfoClient.isNPC(enemy.getUserId())) {
				npc = CacheMgr.userCache.get(enemy.getUserId());
				enemyHeros = new ArrayList<OtherHeroInfoClient>();

				for (HeroIdInfoClient it : enemy.getArenaHeros()) {
					OtherHeroInfoClient ohic = OtherHeroInfoClient.convert(it,
							enemy.getUserId());
					enemyHeros.add(ohic);
				}
			} else {
				ouc = GameBiz.getInstance().queryRichOtherUserInfo(
						enemy.getUserId(), Constants.DATA_TYPE_OTHER_RICHINFO);
				if (ouc.bref().hasGuild())
					ouc.setBgic(CacheMgr.bgicCache.get(ouc.getGuildid()
							.intValue()));

				// 如果enemy中的将领信息与lord中不一致，取lord中信息更新enemy
				tidyData(ouc.getLord().getArenaHeroInfos());

				List<Long> ids = new ArrayList<Long>();
				for (HeroIdInfoClient ahiic : enemy.getArenaHeros()) {
					if (!ids.contains(ahiic.getId())) {
						ids.add(ahiic.getId());
					}
				}
				if (!ListUtil.isNull(ids))
					enemyHeros = GameBiz.getInstance().otherUserHeroInfoQuery(
							enemy.getUserId(), ids);
			}
		}

		// 取最新的数据更新到enemy中,并保持enemy中的位置
		private void tidyData(List<HeroIdInfoClient> news) {
			boolean changed = false;
			List<HeroIdInfoClient> olds = enemy.getArenaHeros();
			for (HeroIdInfoClient oldAhiic : olds) {
				if (!news.contains(oldAhiic)) {
					changed = true;
					break;
				}
			}
			if (olds.size() != news.size())
				changed = true;
			if (changed) {
				enemy.setArenaHeros(news);
			}
		}

		@Override
		protected void onOK() {
			for (OtherHeroInfoClient it : enemyHeros) {
				setStar(it, topUsers);
				setStar(it, attackUsers);
			}

			doOpen();
		}

		private void setStar(OtherHeroInfoClient it,
				List<ArenaUserRankInfoClient> aurics) {
			for (ArenaUserRankInfoClient auricIt : aurics) {
				for (HeroIdInfoClient ahicIt : auricIt.getArenaHeros()) {
					if (it.getId() == ahicIt.getId())
						ahicIt.setStar(it.getStar());
				}
			}
		}
	}
}
