package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.HeroDevourResp;
import com.vikings.sanguo.model.HeroAbandonExpToItem;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroLevelUp;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.HeroDevourSuccessTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomPopupWindow;

/**
 * 将领升级
 * 
 * @author susong
 * 
 */
public class HeroDevourWindow extends CustomPopupWindow implements
		OnClickListener {
	private HeroInfoClient hic;
	private Button upgradeBtn, oneKeyBtn, evolveBtn;
	private ViewGroup needExpLayout, gainExpLayout, armPropLayout,
			heroExpLayout;
	private ViewGroup iconLayout;
	private TextView notice;
	private int[] itemIds = new int[Constants.DEVOUR_MAX_COUNT];
	private List<ViewGroup> devouredHeroViews = new ArrayList<ViewGroup>(
			Constants.DEVOUR_MAX_COUNT);
	private List<View> heroArmPropViews = new ArrayList<View>();

	public HeroDevourWindow(HeroInfoClient hic) {
		this.hic = hic;
	}

	public void open() {
		doOpen();
	}

	@Override
	protected void init() {
		super.init("将领升级");
		setContent(R.layout.hero_devour_window);
		needExpLayout = (ViewGroup) window.findViewById(R.id.needExpLayout);
		gainExpLayout = (ViewGroup) window.findViewById(R.id.gainExpLayout);
		armPropLayout = (ViewGroup) window.findViewById(R.id.armPropLayout);
		heroExpLayout = (ViewGroup) window.findViewById(R.id.heroExpLayout);
		upgradeBtn = (Button) window.findViewById(R.id.upgradeBtn);
		notice = (TextView) window.findViewById(R.id.notice);
		upgradeBtn.setOnClickListener(this);
		oneKeyBtn = (Button) window.findViewById(R.id.oneKeyBtn);
		oneKeyBtn.setOnClickListener(this);
		iconLayout = (ViewGroup) window.findViewById(R.id.iconLayout);
		// iconLayout.setOnClickListener(this);
		evolveBtn = (Button) window.findViewById(R.id.evolveBtn);
		evolveBtn.setOnClickListener(this);
		bindListener(R.id.devouredHeroView0);
		bindListener(R.id.devouredHeroView1);
		bindListener(R.id.devouredHeroView2);
		bindListener(R.id.devouredHeroView3);
		bindListener(R.id.devouredHeroView4);
		bindListener(R.id.devouredHeroView5);
	}

	private void bindListener(int viewId) {
		ViewGroup devouredHeroView = (ViewGroup) window.findViewById(viewId);
		devouredHeroViews.add(devouredHeroView);
		devouredHeroView.setOnClickListener(this);
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	private void setValue() {
		// 第一位为最高等级，第二位为差的经验
		int[] values = getExp2MaxLevel();
		setHeroInfo(values[0]);
		setDevouredHeroViews();
		setHeroArmProps();
		if (values[1] > 0) {
			ViewUtil.setRichText(needExpLayout, R.id.gradientMsg, "将领离满级还差 "
					+ values[1] + " 的经验");
			ViewUtil.setVisible(gainExpLayout);
			ViewUtil.setRichText(gainExpLayout, R.id.gradientMsg, "本次可以获得 "
					+ getGainExp() + " 的经验");
			int money = getToolsCount() * hic.getLevel()
					* CacheMgr.heroCommonConfigCache.getUpgradeByToolPrice();
			ViewUtil.setVisible(upgradeBtn);
			ViewUtil.setRichText(upgradeBtn, "升级"
					+ (money > 0 ? "#money#" + money : ""), true);
			int currency = CalcUtil.upNum(values[1] * 1f
					/ CacheMgr.heroCommonConfigCache.getExpPrice());
			oneKeyBtn.setTag(CalcUtil.buildKey(currency, values[1]));
			ViewUtil.setVisible(oneKeyBtn);
			ViewUtil.setRichText(oneKeyBtn, "一键满级" + "#rmb#" + currency, true);
			ViewUtil.setGone(evolveBtn);
			ViewUtil.setGone(notice);
		} else {
			ViewUtil.setRichText(needExpLayout, R.id.gradientMsg, "将领已经满级 ");
			ViewUtil.setGone(upgradeBtn);
			ViewUtil.setGone(oneKeyBtn);
			ViewUtil.setVisible(notice);
			ViewUtil.setHide(gainExpLayout);
			if (hic.canEvolve()) {
				ViewUtil.setVisible(evolveBtn);
				ViewUtil.setText(notice, "该将领已经满级，可以进化了");
			} else {
				ViewUtil.setGone(evolveBtn);
				ViewUtil.setText(notice, "该将领已经达到最高品质、最高等级");
			}
		}

	}

	private void setHeroArmProps() {
		List<HeroArmPropInfoClient> armPropInfos = hic.getArmPropInfos();
		for (int i = heroArmPropViews.size(); i < armPropInfos.size(); i++) {
			View view = controller.inflate(R.layout.hero_armprop,
					armPropLayout, false);
			armPropLayout.addView(view);
			heroArmPropViews.add(view);
		}
		for (int i = 0; i < heroArmPropViews.size(); i++) {
			View view = heroArmPropViews.get(i);
			if (i < armPropInfos.size()) {
				ViewUtil.setVisible(view);
				setHeroArmProp(view, armPropInfos.get(i));
			} else {
				ViewUtil.setGone(view);
			}
		}
	}

	private void setHeroArmProp(View view, HeroArmPropInfoClient hapic) {
		new ViewImgCallBack(hapic.getHeroTroopName().getSmallIcon(),
				view.findViewById(R.id.smallIcon));
		ViewUtil.setText(view.findViewById(R.id.name), hapic.getHeroTroopName()
				.getName() + "：");
		ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar);
		bar.set(hapic.getValue(), hapic.getMaxValue());
		ViewUtil.setText(view.findViewById(R.id.progressDesc), hapic.getValue()
				+ "/" + hapic.getMaxValue());
	}

	private int getGainExp() {
		return getTotalExp();
	}

	// 第一位为最高等级，第二位为差的经验
	private int[] getExp2MaxLevel() {
		int[] values = new int[2];
		List<HeroLevelUp> list = CacheMgr.heroLevelUpCache.searchExp(
				hic.getTalent(), hic.getStar());
		for (HeroLevelUp exp : list) {
			if (values[0] < exp.getLevel())
				values[0] = exp.getLevel();
			if (exp.getLevel() == hic.getLevel()) {
				int curLevelNeedExp = exp.getNeedExp() - hic.getExp();
				if (curLevelNeedExp < 0)
					curLevelNeedExp = 0;
				values[1] += curLevelNeedExp;
			} else if (exp.getLevel() > hic.getLevel()) {
				values[1] += exp.getNeedExp();
			} else {
				continue;
			}
		}
		return values;
	}

	// 取所有将领提供的经验
	private int getTotalExp() {
		int totalExp = 0;
		for (int i = 0; i < itemIds.length; i++) {
			if (itemIds[i] > 0) {
				HeroAbandonExpToItem haeti = (HeroAbandonExpToItem) CacheMgr.heroAbandonExpToItemCache
						.getByItemId(itemIds[i]);
				if (null != haeti)
					totalExp += haeti.getExp();
			}
		}
		return totalExp;
	}

	private int getLevelAfterDevour() {
		int level = hic.getLevel();
		int levelAdd = 0;
		int totalExp = getTotalExp();
		int totalNeedExp = 0;
		List<HeroLevelUp> list = CacheMgr.heroLevelUpCache.searchExp(
				hic.getTalent(), hic.getStar());
		for (int i = 0; i < list.size(); i++) {
			HeroLevelUp hlue = list.get(i);
			if (hlue.getLevel() < hic.getLevel()) {
				continue;
			} else if (hlue.getLevel() == hic.getLevel()) {
				totalNeedExp += (hlue.getNeedExp() - hic.getExp());
			} else {
				totalNeedExp += hlue.getNeedExp();
			}
			if (totalNeedExp <= totalExp && i < list.size() - 1)
				levelAdd++;
			else
				break;
		}
		return level + levelAdd;
	}

	private String getLevelString() {
		int level = getLevelAfterDevour();
		if (hic.getLevel() == level)
			return "";
		else
			return StringUtil.color(" → LV" + level,
					controller.getResourceColorText(R.color.k7_color4));
	}

	private void setDevouredHeroViews() {
		for (int i = 0; i < devouredHeroViews.size(); i++) {
			if (itemIds[i] > 0) {
				setView(devouredHeroViews.get(i), itemIds[i]);
			} else {
				clearView(devouredHeroViews.get(i));
			}
		}

	}

	private void setView(ViewGroup viewGroup, int itemId) {
		ItemBag itemBag = Account.store.getItemBag(itemId);
		if (null != itemBag) {
			new ViewImgScaleCallBack(itemBag.getItem().getImage(),
					viewGroup.findViewById(R.id.itemIcon),
					Constants.DEVOUR_ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH,
					Constants.DEVOUR_ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
		} else {
			clearView(viewGroup);
		}
	}

	private void clearView(View view) {
		ViewUtil.setImage(view, R.id.itemIcon, R.drawable.add_devour);
	}

	private void setHeroInfo(int maxLevel) {
		IconUtil.setHeroIconScale(iconLayout, hic);
		// iconLayout.setOnClickListener(new OwnHeroClickListerner(hic));
		ViewUtil.setRichText(window, R.id.talent, hic.getColorTypeName());
		ViewUtil.setRichText(window, R.id.name, hic.getColorHeroName());

		ViewUtil.setRichText(window, R.id.level, "LV" + hic.getLevel()
				+ getLevelString() + " (满级" + maxLevel + "级)");

		HeroLevelUp heroLevelUpExp = (HeroLevelUp) CacheMgr.heroLevelUpCache
				.getExp(hic.getTalent(), hic.getStar(), hic.getLevel());
		ProgressBar progressBar = (ProgressBar) heroExpLayout
				.findViewById(R.id.progressBar);
		progressBar.set(hic.getExp(), heroLevelUpExp.getNeedExp(), hic.getExp()
				+ getTotalExp(), R.drawable.progress_yellow_long);
		ViewUtil.setText(heroExpLayout, R.id.progressDesc, hic.getExp() + "/"
				+ heroLevelUpExp.getNeedExp());
	}

	@Override
	protected void destory() {
		devouredHeroViews.clear();
		devouredHeroViews = null;
		super.destory();
	}

	private int getToolsCount() {
		int count = 0;
		for (int i = 0; i < itemIds.length; i++) {
			if (itemIds[i] > 0) {
				count++;
			}
		}
		return count;
	}

	private void clearItemIds() {
		for (int i = 0; i < itemIds.length; i++) {
			itemIds[i] = 0;
		}
	}

	private List<Long> getBagIds() {
		List<Long> list = new ArrayList<Long>();
		for (int i = 0; i < itemIds.length; i++) {
			if (itemIds[i] > 0) {
				ItemBag itemBag = Account.store.getItemBag(itemIds[i]);
				if (itemBag != null)
					list.add(itemBag.getId());
			}
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		if (v == upgradeBtn) {
			if (getToolsCount() <= 0) {
				controller.alert("请先选择升级物品");
			} else {
				new HeroDevourInvoker(Constants.TYPE_NOAMRL, getBagIds())
						.start();
			}
		} else if (v == oneKeyBtn) {
			Object obj = oneKeyBtn.getTag();
			if (null != obj) {
				final long key = ((Long) obj).longValue();
				final int[] values = CalcUtil.parse(key);
				if (values[1] > 0) {
					controller.confirm(
							"一键满级",
							CustomConfirmDialog.DEFAULT,
							"将领离满级还差"
									+ StringUtil.color("" + values[1],
											R.color.color11) + "的经验<br/>花费 "
									+ values[0] + " 元宝让将领升到满级",
							"快速把武将升到满级，适合材料不足的情况使用", new CallBack() {

								@Override
								public void onCall() {
									if (Account.user.getCurrency() < values[0]) {
										new ToActionTip(values[0]).show();
										return;
									}
									new HeroDevourInvoker(
											Constants.TYPE_CURRENCY, null)
											.start();

								}
							}, null);
				} else {
					controller.alert("该将领已满级");
				}

			}

		} else if (v == evolveBtn) {
			new HeroEvolveWindow().open(hic);
		} else {
			int index = devouredHeroViews.indexOf(v);
			if (index <= -1)
				return;
			if (itemIds[index] > 0) {
				itemIds[index] = 0;
				setValue();
			} else {
				new HeroDevourChooseTip(hic, itemIds, index).show();
			}
		}

	}

	private class HeroDevourInvoker extends BaseInvoker {
		private HeroDevourResp resp;
		private HeroInfoClient oldHero;
		private HeroInfoClient newHero;

		private int type;
		private List<Long> bagIds;

		public HeroDevourInvoker(int type, List<Long> bagIds) {
			this.type = type;
			this.bagIds = bagIds;
		}

		@Override
		protected String loadingMsg() {
			return "将领升级";
		}

		@Override
		protected String failMsg() {
			return "将领升级失败";
		}

		@Override
		protected void fire() throws GameException {
			oldHero = hic.copy();
			resp = GameBiz.getInstance().heroDevour(hic.getId(), bagIds, type);
			clearItemIds();
			newHero = resp.getHero();
			hic.update(newHero);
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRi(), true);
			setValue();
			if (oldHero.getLevel() == newHero.getLevel()) {
				ctr.alert("将领吞噬成功",
						"将领获得了" + (newHero.getExp() - oldHero.getExp()) + "经验",
						null, true);
			} else {
				new HeroDevourSuccessTip(oldHero, newHero).show();
			}
		}

	}
}
