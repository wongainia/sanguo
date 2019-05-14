package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.BattleLogInfoInvoker;
import com.vikings.sanguo.model.BattleHotInfoClient;
import com.vikings.sanguo.model.BattleLogClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Country;
import com.vikings.sanguo.model.HeroIdBaseInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.protos.BriefBattleLogInfo;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.window.OthersHeroDetailWindow;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HistoryBattleLogTip extends CustomConfirmDialog {

	private BriefBattleLogInfo bbli;
	private BattleLogClient blc;

	public HistoryBattleLogTip(BattleLogClient battleLogClient) {
		super("战场记录", CustomConfirmDialog.DEFAULT);
		this.blc = battleLogClient;
		this.bbli = battleLogClient.getBlogInfo();

		setAddress(battleLogClient);
		setButtons(bbli.getId());

		setAtkUserIcon(blc.getAttackUser());
		setAtkResultIcon(bbli.getBattleResult() == 1);

		setDefUserIcon(blc.getDefendUser());
		setDefResultIcon(blc.getBlogInfo().getBattleResult() == 2);

		if (null != bbli) {
			setAtkHeroIcon(bbli.hasAttackHeroInfo());
			setDefHeroIcon(bbli.hasDefendHeroInfo());
		}

		ViewUtil.setText(content, R.id.atkUnit, "兵力:" + bbli.getAttackUnit());
		ViewUtil.setText(content, R.id.defUnit, "兵力:" + bbli.getDefendUnit());

		setAtkCountry(blc.getAttackUser());
		setDefCountry(blc.getDefendUser());

	}

	public HistoryBattleLogTip(BattleHotInfoClient bhic) {
		super("战场记录", CustomConfirmDialog.DEFAULT);

		setAddress(bhic.getFiefid());

		setAtkUserIcon(bhic.getAttacker());
		setAtkResultIcon(1 == bhic.getResult());

		setDefUserIcon(bhic.getDefender());
		setDefResultIcon(2 == bhic.getResult());

		if (null != bhic.getAttackerHeroId())
			showAtkHeroIcon(bhic.getAttackerHeroId(), bhic.getAttacker());
		else
			setNoAtkHero();

		if (null != bhic.getDefenderHeroId())
			showDefHeroIcon(bhic.getDefenderHeroId(), bhic.getDefender());
		else
			setNoDefHero();

		ViewUtil.setText(content, R.id.atkUnit, "兵力:" + bhic.getTotalAtkTroop());
		ViewUtil.setText(content, R.id.defUnit, "兵力:" + bhic.getTotalDefTroop());

		setAtkCountry(bhic.getAttacker());
		setDefCountry(bhic.getDefender());

		setButtons(bhic.getBattleLogId());
	}

	protected void setDefCountry(BriefUserInfoClient def) {
		if (null != def) {
			Country defCountry = CacheMgr.countryCache.getCountry(def
					.getCountry());
			if (StringUtil.isNull(defCountry.getName()))
				ViewUtil.setText(content, R.id.defCountry, "国家: 无");
			else
				ViewUtil.setText(content, R.id.defCountry,
						"国家: " + defCountry.getName());
		}
	}

	protected void setAtkCountry(BriefUserInfoClient atk) {
		if (null != atk) {
			Country atkCountry = (Country) CacheMgr.countryCache.getCountry(atk
					.getCountry());
			if (StringUtil.isNull(atkCountry.getName()))
				ViewUtil.setText(content, R.id.atkCountry, "国家: 无");
			else
				ViewUtil.setText(content, R.id.atkCountry,
						"国家: " + atkCountry.getName());
		}
	}

	protected void setDefHeroIcon(boolean hasHero) {
		if (hasHero) {
			try {
				HeroIdInfoClient hiic = HeroIdInfoClient.convert(bbli
						.getDefendHeroInfo());
				showDefHeroIcon(hiic, blc.getDefendUser());
			} catch (GameException e) {
				ViewUtil.setText(content, R.id.defHero, "无将领");
				e.printStackTrace();
			}
		} else
			setNoDefHero();
	}

	private void setNoDefHero() {
		ViewUtil.setText(content, R.id.defHero, "无将领");

	}

	private void showDefHeroIcon(HeroIdBaseInfoClient hiic,
			BriefUserInfoClient user) {

		TextView defHeroTv = (TextView) content.findViewById(R.id.defHero);
		defHeroTv.setShadowLayer(1.0f, 0, 0, Color.BLACK);

		ViewUtil.setRichText(
				defHeroTv,
				StringUtil.getHeroTypeName(hiic.getHeroQuality())
						+ StringUtil.getHeroName(hiic.getHeroProp(),
								hiic.getHeroQuality()));
		ViewUtil.setUnderLine(defHeroTv);
		ViewUtil.setBold(defHeroTv);
		HeroListener defHeroL = new HeroListener(hiic, user);
		defHeroTv.setOnClickListener(defHeroL);
	}

	private void setAtkHeroIcon(boolean hasHero) {
		if (hasHero) {
			try {
				HeroIdInfoClient hiic = HeroIdInfoClient.convert(bbli
						.getAttackHeroInfo());
				showAtkHeroIcon(hiic, blc.getAttackUser());
			} catch (Exception e) {
				ViewUtil.setText(content.findViewById(R.id.atkHero), "无将领");
				e.printStackTrace();
			}
		} else
			setNoAtkHero();
	}

	private void setNoAtkHero() {
		ViewUtil.setText(content.findViewById(R.id.atkHero), "无将领");
	}

	private void showAtkHeroIcon(HeroIdBaseInfoClient hiic,
			BriefUserInfoClient user) {

		TextView atkHeroTv = (TextView) content.findViewById(R.id.atkHero);
		atkHeroTv.setShadowLayer(1.0f, 0, 0, Color.BLACK);

		ViewUtil.setRichText(
				atkHeroTv,
				StringUtil.getHeroTypeName(hiic.getHeroQuality())
						+ StringUtil.getHeroName(hiic.getHeroProp(),
								hiic.getHeroQuality()));

		ViewUtil.setUnderLine(atkHeroTv);
		ViewUtil.setBold(atkHeroTv);
		HeroListener atkHeroL = new HeroListener(hiic, user);
		atkHeroTv.setOnClickListener(atkHeroL);

	}

	protected void setDefResultIcon(boolean isWin) {
		int rightIcon = isWin ? R.drawable.btl_tri_icon_sg
				: R.drawable.btl_fail_icon_sg;
		ViewUtil.setImage(content, R.id.defResult, rightIcon);
	}

	protected void setDefUserIcon(BriefUserInfoClient rightUser) {
		if (null != rightUser) {
			View defName = content.findViewById(R.id.defName);
			ViewUtil.setText(defName, rightUser.getNickName());
			UserListener rightUserL = new UserListener(rightUser);
			defName.setOnClickListener(rightUserL);

			View defIcon = content.findViewById(R.id.defIcon);
			new UserIconCallBack(rightUser, defIcon, Config.SCALE_FROM_HIGH
					* Constants.USER_ICON_WIDTH, Config.SCALE_FROM_HIGH
					* Constants.HERO_ICON_HEIGHT);
			defIcon.setOnClickListener(rightUserL);
		}
	}

	protected void setAtkResultIcon(boolean isWin) {
		int leftIcon = isWin ? R.drawable.btl_tri_icon_sg
				: R.drawable.btl_fail_icon_sg;
		ViewUtil.setImage(content, R.id.atkResult, leftIcon);
	}

	protected void setAtkUserIcon(BriefUserInfoClient leftUser) {
		if (null != leftUser) {
			View atkName = content.findViewById(R.id.atkName);
			ViewUtil.setText(atkName, leftUser.getNickName());
			UserListener leftUserL = new UserListener(leftUser);
			atkName.setOnClickListener(leftUserL);

			View atkIcon = content.findViewById(R.id.atkIcon);
			new UserIconCallBack(leftUser, atkIcon, Config.SCALE_FROM_HIGH
					* Constants.USER_ICON_WIDTH, Config.SCALE_FROM_HIGH
					* Constants.USER_ICON_HEIGHT);
			atkIcon.setOnClickListener(leftUserL);
		}
	}

	protected void setButtons(final long battleLogId) {
		setButton(CustomConfirmDialog.SECOND_BTN, "回放动画",
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
						new BattleLogInfoInvoker(battleLogId, true).start();
					}
				});
		setButton(CustomConfirmDialog.THIRD_BTN, "关        闭", closeL);
	}

	private void setAddress(final BattleLogClient battleLogClient) {
		if (null != battleLogClient.getBlogInfo())
			setAddress(battleLogClient.getBlogInfo().getDefendFiefid());
	}

	private void setAddress(final long fiefId) {
		TextView address = (TextView) findViewById(R.id.address);
		ViewUtil.setUnderLine(address);
		ViewUtil.setBold(address);
		long tileId = TileUtil.fiefId2TileId(fiefId);
		if (CacheMgr.holyPropCache.isHoly(fiefId))
			ViewUtil.setText(address,
					CacheMgr.holyPropCache.getFiefName(fiefId));
		else
			new AddrLoader(address, tileId, AddrLoader.TYPE_SUB);
		address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				controller.moveToFief(fiefId);
			}
		});
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_battle_log_tip, tip, false);
	}

	class UserListener implements OnClickListener {
		private BriefUserInfoClient user;

		public UserListener(BriefUserInfoClient user) {
			this.user = user;
		}

		@Override
		public void onClick(View v) {
			if (!CacheMgr.npcCache.containKey(user.getId())) {
				dismiss();
				controller.showCastle(user);
			}
		}
	}

	class HeroListener implements OnClickListener {
		private HeroIdBaseInfoClient hero;
		private BriefUserInfoClient user;

		public HeroListener(HeroIdBaseInfoClient hero, BriefUserInfoClient user) {
			this.hero = hero;
			this.user = user;
		}

		@Override
		public void onClick(View v) {
			new QueryOtherHeroInvoker(hero, user).start();
		}

	}

	class QueryOtherHeroInvoker extends BaseInvoker {
		private HeroIdBaseInfoClient hero;
		private BriefUserInfoClient user;
		private List<OtherHeroInfoClient> heros;

		public QueryOtherHeroInvoker(HeroIdBaseInfoClient hero,
				BriefUserInfoClient user) {
			this.hero = hero;
			this.user = user;
		}

		@Override
		protected String loadingMsg() {
			return "获取将领信息中";
		}

		@Override
		protected String failMsg() {
			return "获取将领信息失败";
		}

		@Override
		protected void fire() throws GameException {
			if (isValid()) {
				List<Long> ids = new ArrayList<Long>();
				ids.add(hero.getId());
				heros = GameBiz.getInstance().otherUserHeroInfoQuery(
						user.getId(), ids);
			}
		}

		@Override
		protected void onOK() {
			if (isValid()) {
				dismiss();
				if (ListUtil.isNull(heros))
					controller.alert("该将领已不存在");
				else {
					OtherHeroInfoClient hero = heros.get(0);
					new OthersHeroDetailWindow().open(hero);
				}
			}
		}

		private boolean isValid() {
			return null != user && null != hero && user.getId() > 0
					&& hero.getId() > 0;
		}
	}
}
