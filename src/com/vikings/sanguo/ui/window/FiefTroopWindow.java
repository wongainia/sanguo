package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.ArmInfoSelectData;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.OtherFiefInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.RichFiefInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.ReviewEnemyAdapter;
import com.vikings.sanguo.ui.alert.HeroDetailTip;
import com.vikings.sanguo.ui.alert.UnlockHeroTip;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

public class FiefTroopWindow extends TroopSetWindow {
	private static final boolean SET_HERO = true;
	private static final boolean CANCEL_HERO = false;
	// private OtherUserClient ouc;
	private BriefFiefInfoClient bfic;
	private OtherFiefInfoClient ofic;
	private List<HeroIdInfoClient> otherHeroIdInfoClients = new ArrayList<HeroIdInfoClient>();
	private List<OtherHeroInfoClient> otherHeros = new ArrayList<OtherHeroInfoClient>(); //
	private ReviewEnemyAdapter reviewEnemyAdapter = new ReviewEnemyAdapter();

	public void open(BriefFiefInfoClient bfic) {
		this.bfic = bfic;
		hics[0] = HeroInfoClient.newInstance();
		hics[1] = HeroInfoClient.newInstance();
		hics[2] = HeroInfoClient.newInstance();
		new Fetch().start();
	}

	@Override
	protected void init() {
		super.init();
	}

	protected void setinitHeroInfos() {
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return reviewEnemyAdapter;
	}

	@Override
	public void showUI() {
		super.showUI();
		if (!Account.richFiefCache.isMyFief(bfic.getId())) {

			// 取消选将事件
			cancelAllHeroClick();
			if (ListUtil.isNull(otherHeros)) {
				setContentBelowTitleGone();
			} else {
				// 查看其它领地守将详情
				for (int i = 0; i < otherHeros.size(); i++) {
					HeroIdInfoClient mClient = HeroIdInfoClient.searchHiicById(
							otherHeros.get(i).getId(), otherHeroIdInfoClients);
					if (mClient == null)
						continue;
					if (mClient.isMainHero()) {
						// 设置主将选择巢
						setMainHero(hero1, otherHeros.get(i));
						setOtherHeroClick(hero1, otherHeros.get(i));
					} else {
						// 设置副将选择巢
						if (hero2.getTag() == null) {
							setExtHero(hero2, otherHeros.get(i), true);
							setMainHero(hero2, otherHeros.get(i));
							setOtherHeroClick(hero2, otherHeros.get(i));
							hero2.setTag(true);
						} else if (hero3.getTag() == null) {
							setExtHero(hero3, otherHeros.get(i), true);
							setMainHero(hero3, otherHeros.get(i));
							setOtherHeroClick(hero3, otherHeros.get(i));
							hero3.setTag(true);
						}
					}
				}

			}
		}

	}

	// 隐藏选将控件
	public void setContentBelowTitleGone() {
		contentBelowTitle.setVisibility(View.GONE);
	}

	// 清理选将点击事件
	public void cancelAllHeroClick() {
		hero1.setOnClickListener(null);
		hero2.setOnClickListener(null);
		hero3.setOnClickListener(null);
		// 初始化将领曹状态
		setExtOtherHero(hero1);
		setExtOtherHero(hero2);
		setExtOtherHero(hero3);
		hero2.setTag(null);
		hero3.setTag(null);
	}

	// 查看其它领地将领信息
	public void setOtherHeroClick(ViewGroup viewGroup,
			final OtherHeroInfoClient ohic) {
		viewGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new HeroDetailTip(ohic).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == hero1) {
			if (hics[0].isValid()) {
				setHeroToServer(CANCEL_HERO, 0);
				// hics[0].setInvalid(); //请求成功才改变状态并且设置
				// setMainHero(hero1, hics[0]);

			} else {
				new HeroChooseListWindow().open(hics, 0,
						getHeroChooseBriefFief(), (byte) 0,
						new SetHeroCallBack(SET_HERO, 0));
			}
		} else if (v == hero2) {
			if (Account.user.unlockExtHero1()) {
				if (hics[1].isValid()) {
					setHeroToServer(CANCEL_HERO, 1);
					// hics[1].setInvalid();
					// setMainHero(hero2, hics[1]);

				} else {
					new HeroChooseListWindow().open(hics, 1,
							getHeroChooseBriefFief(), (byte) 0,
							new SetHeroCallBack(SET_HERO, 1));
				}
			} else {
				new UnlockHeroTip(UnlockHeroTip.COUNT_ONE).show();
			}
		} else if (v == hero3) {
			if (Account.user.unlockExtHero2()) {
				if (hics[2].isValid()) {
					setHeroToServer(CANCEL_HERO, 2);
					// hics[2].setInvalid();
					// setMainHero(hero3, hics[2]);
				} else {
					new HeroChooseListWindow().open(hics, 2,
							getHeroChooseBriefFief(), (byte) 0,
							new SetHeroCallBack(SET_HERO, 2));
				}
			} else {
				new UnlockHeroTip(UnlockHeroTip.COUNT_TWO).show();
			}
		}
	}

	// 设置选择单个巢
	private void setHeroNest(HeroIdInfoClient hiic) {
		// 选择巢将领次序 主将：hics[0] -> 副将1：hics[1] -> 副将2：hics[2]
		if (hiic.isMainHero()) {
			// 设置主将选择巢
			hics[0].update(Account.heroInfoCache.get(hiic.getId()));
		} else {
			// 设置副将选择巢
			if (!hics[1].isValid()) {
				hics[1].update(Account.heroInfoCache.get(hiic.getId()));
			} else if (!hics[2].isValid()) {
				hics[2].update(Account.heroInfoCache.get(hiic.getId()));
			}
		}

	}

	// 初始化将领巢（最多三个）
	private void initHerosNest(List<HeroIdInfoClient> hiicClients) {
		if (ListUtil.isNull(hiicClients)) {
			return;
		}
		for (HeroIdInfoClient heroIdInfoClient : hiicClients) {
			setHeroNest(heroIdInfoClient);
		}

	}

	private class Fetch extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "请稍候";
		}

		@Override
		protected String failMsg() {
			return "获取领地信息失败";
		}

		@Override
		protected void fire() throws GameException {
			if (Account.richFiefCache.isMyFief(bfic.getId())) {
				RichFiefInfoClient rfic = Account.richFiefCache.getInfo(bfic
						.getId());

				List<ArmInfoClient> ls = rfic.getFiefInfo().getTroopInfo();

				initHerosNest(rfic.getFiefInfo().getHeroIdInfos());
				reviewEnemyAdapter.addItems(ls);
				reviewEnemyAdapter.setUserTroopEffectInfo(Account
						.getUserTroopEffectInfo());
				reviewEnemyAdapter.setHic(Arrays.asList(hics));
			} else {
				if (null != bfic) {
					ofic = GameBiz.getInstance().otherFiefInfoQuery(
							bfic.getId());
					// ouc = GameBiz.getInstance().queryRichOtherUserInfo(
					// bfic.getUserId(),
					// Constants.DATA_TYPE_OTHER_RICHINFO
					// | Constants.DATA_TYPE_ARM_PROP);
				}

				if (!ListUtil.isNull(ofic.getHeroInfos())) {
					otherHeroIdInfoClients.clear();
					otherHeroIdInfoClients.addAll(ofic.getHeroInfos());
					List<Long> ids = new ArrayList<Long>();
					for (HeroIdInfoClient heroIdInfoClient : ofic
							.getHeroInfos()) {
						ids.add(heroIdInfoClient.getId());
					}

					List<OtherHeroInfoClient> ohicLs = GameBiz
							.getInstance()
							.otherUserHeroInfoQuery(bfic.getLord().getId(), ids);

					if (!ListUtil.isNull(ohicLs)) {
						otherHeros.clear();
						otherHeros.addAll(ohicLs);
						reviewEnemyAdapter.setOhic(otherHeros);
					}

				}

				reviewEnemyAdapter.addItems(ofic.getInfo());
				reviewEnemyAdapter.setUserTroopEffectInfo(ofic
						.getTroopEffectInfo());

			}
		}

		@Override
		protected void onOK() {
			doOpen();
			if (null != reviewEnemyAdapter) {
				reviewEnemyAdapter.setHic(Arrays.asList(hics));
			}
		}

	}

	private class SetHero extends BaseInvoker {
		private boolean set;
		private int index;

		public SetHero(boolean set, int index) {
			super();
			this.set = set;
			this.index = index;
		}

		@Override
		protected String loadingMsg() {
			return "设置守城将领";
		}

		@Override
		protected String failMsg() {
			return "设置失败";
		}

		@Override
		protected void fire() throws GameException {
			RichFiefInfoClient rfic = Account.richFiefCache.getInfo(bfic
					.getId());
			HeroIdInfoClient hiicClient = rfic.getFiefInfo().getHiicByID(
					hics[index].getId());
			if (hiicClient != null) {
				List<HeroIdInfoClient> hiics = new ArrayList<HeroIdInfoClient>();
				if (set) {
					if (index == 0) {
						// 为主将
						hiicClient
								.setRole(HeroIdInfoClient.HERO_ROLE_DEFEND_MAIN);
					} else {// 为副将
						hiicClient
								.setRole(HeroIdInfoClient.HERO_ROLE_DEFEND_ASSIST);
					}
					hiics.add(hiicClient);
					// GameBiz.getInstance().fiefHeroSelect(bfic.getId(),
					// hiics);
					for (int i = 0; i < hics.length; i++) {
						if (hics[i].isValid()
								&& hics[i].getId() != hiicClient.getId()) {
							HeroIdInfoClient hiicClient1 = rfic.getFiefInfo()
									.getHiicByID(hics[i].getId());
							if (hiicClient1 != null) {
								hiics.add(hiicClient1);
							}
						}

					}

					GameBiz.getInstance().fiefHeroSelect(bfic.getId(), hiics);
					rfic.getFiefInfo().fiefHeroAdd(hiicClient);

				} else {

					// hiicClient.setRole(0);// 去除将领
					// hiics.add(hiicClient);
					for (int i = 0; i < hics.length; i++) {
						if (hics[i].isValid()
								&& hics[i].getId() != hiicClient.getId()) {
							HeroIdInfoClient hiicClient1 = rfic.getFiefInfo()
									.getHiicByID(hics[i].getId());
							if (hiicClient1 != null) {
								hiics.add(hiicClient1);
							}
						}

					}
					GameBiz.getInstance().fiefHeroSelect(bfic.getId(), hiics);
					rfic.getFiefInfo().fiefHeroDel(hiicClient);

				}
				// bfic.setDefenderHero();
			}

		}

		@Override
		protected void onOK() {			
			//如果是取消成功    把对应英雄设成无效   
			if(set == false)
			{
				appearAnim(set, index);
				if(index < hics.length)
				{
					hics[index].setInvalid();
				}
				if (index == 0) {
					setMainHero(hero1, hics[0]);
				} else if (index == 1) {
					setMainHero(hero2, hics[1]);
				} else if (index == 2) {
					setMainHero(hero3, hics[2]);
				}
			}
			// ctr.alert((set ? "设置" : "取消") + "守将成功");
			if (null != reviewEnemyAdapter) {
				reviewEnemyAdapter.setHic(Arrays.asList(hics));
				reviewEnemyAdapter.notifyDataSetChanged();
			}
		}

	}

	@Override
	protected String getTitle() {
		return "领地部队";
	}

	@Override
	protected List<ArmInfoSelectData> getArmInfoSelectDatas() {
		return null;
	}

	@Override
	protected BriefFiefInfoClient getHeroChooseBriefFief() {
		if (Account.richFiefCache.isMyFief(bfic.getId())) {
			return bfic;
		} else {
			return null;
		}
	}

	@Override
	public void handleItem(Object o) {

	}

	// 设置守将信息 并且保存到服务器
	private void setHeroToServer(boolean isSet, int index) {
		if (Account.richFiefCache.isMyFief(bfic.getId())) {
			new SetHero(isSet, index).start();
		}
	}

	private class SetHeroCallBack implements CallBack {
		private boolean isSet;// 是取消将领还是设置将领
		private int index;// 是设置那个将领

		public SetHeroCallBack(boolean isSet, int index) {
			super();
			this.isSet = isSet;
			this.index = index;
		}

		@Override
		public void onCall() {
			setHeroToServer(isSet, index);
			appearAnim(isSet, index);
		}
	}

	private void appearAnim(boolean isSet, int index) {
		View v = null;
		if (index == 0) {
			v = hero1;
		} else if (index == 1) {
			v = hero2;
		} else if (index == 2) {
			v = hero3;
		}
		String richText = "";
		HeroInfoClient hic = null;
		if (index < hics.length) {
			hic = hics[index];
		}
		if (hic != null) {
			richText = hic.getColorTypeName()
					+ " "
					+ StringUtil.getHeroName(hic.getHeroProp(),
							hic.getHeroQuality()) + "  "
					+ (isSet ? "上场" : "卸下");
			startAnimation(v, richText, isSet);
		}
	}

	@Override
	protected CallBack getCallBackAfterChooseHero() {
		return null;
	}
}
