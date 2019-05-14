package com.vikings.sanguo.ui.window;

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
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.HeroEvolveResp;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroEvolve;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroQuality;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.WorldLevelInfoClient;
import com.vikings.sanguo.model.WorldLevelProp;
import com.vikings.sanguo.ui.alert.HeroEvolveSuccessTip;
import com.vikings.sanguo.ui.alert.ItemNotEnoughTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

/**
 * 将领进化
 * 
 * @author susong
 * 
 */
public class HeroEvolveWindow extends CustomPopupWindow implements
		OnClickListener {
	private ViewGroup evolveContent, heroLayout, newHeroLayout, evolveArmprops;
	private TextView evolveDesc, worldLevelDesc, cost;
	private Button evolveBtn;
	private HeroInfoClient hic;
	private HeroEvolve heroEvolve;
	private int needCount;
	private int selfCount;
	private Item soulItem;
	// 引导专用
	public static boolean isGuide = false;

	public void open(HeroInfoClient hic) {
		this.hic = hic;
		doOpen();
	}

	@Override
	protected void init() {
		super.init("武将进化");
		setContent(R.layout.hero_evolve_window);
		evolveContent = (ViewGroup) window.findViewById(R.id.evolveContent);
		heroLayout = (ViewGroup) window.findViewById(R.id.heroLayout);
		newHeroLayout = (ViewGroup) window.findViewById(R.id.newHeroLayout);
		evolveArmprops = (ViewGroup) window.findViewById(R.id.evolveArmprops);
		evolveDesc = (TextView) window.findViewById(R.id.evolveDesc);
		worldLevelDesc = (TextView) window.findViewById(R.id.worldLevelDesc);
		cost = (TextView) window.findViewById(R.id.cost);
		evolveBtn = (Button) window.findViewById(R.id.evolveBtn);
		evolveBtn.setOnClickListener(this);
		ViewUtil.setImage(window, R.id.evolveBg, R.drawable.evolve_bg);
		imageHolder.setBg(evolveContent,
				ImageUtil.getRotateBitmapDrawable("jianbian_bg", 180));
		ViewUtil.setImage(window, R.id.evole_icon_layout,
				R.drawable.envole_star);
		try {
			heroEvolve = (HeroEvolve) CacheMgr.heroEvolveCache.get(hic
					.getHeroId());
		} catch (GameException e) {
		}
	}

	private String getEvolveCostDesc() {
		StringBuffer buf = new StringBuffer();
		if (null != heroEvolve) {
			soulItem = CacheMgr.getItemByID(hic.getHeroType().getSoulId());
			ItemBag itemBag = Account.store.getItemBag(hic.getHeroType()
					.getSoulId());
			if (null != soulItem && heroEvolve.getSoulCount() > 0) {
				needCount = (int) (heroEvolve.getSoulCount() / 100f * hic
						.getHeroType().getSoulRate());
				buf.append("#").append(soulItem.getImage()).append("#")
						.append(soulItem.getName()).append("x")
						.append(needCount);
				if (null != itemBag && itemBag.getCount() > 0)
					selfCount = itemBag.getCount();

				if (needCount > selfCount) {
					buf.append(StringUtil.color(" (" + selfCount + ")",
							R.color.color11));
				} else {
					buf.append(StringUtil.color(" (" + selfCount + ")",
							R.color.color19));
				}
			}
		}
		return buf.toString();
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	private void setValue() {
		needCount = 0;
		selfCount = 0;
		soulItem = null;
		setHeroInfo();
		setArmPropValue();
		String costStr = getEvolveCostDesc();
		if (!StringUtil.isNull(costStr)) {
			ViewUtil.setVisible(cost);
			ViewUtil.setRichText(cost, "进化需要：" + costStr, true, 25, 25);
		} else {
			ViewUtil.setGone(cost);
		}
	}

	private void setArmPropValue() {
		// 取herotype中百分比乘当前值、上限
		evolveArmprops.removeAllViews();
		if (!ListUtil.isNull(hic.getArmPropInfos())) {
			for (HeroArmPropInfoClient hapic : hic.getArmPropInfos()) {
				View view = controller.inflate(R.layout.hero_armprop_evolve);
				ViewUtil.setImage(view, R.id.smallIcon, hapic
						.getHeroTroopName().getSmallIcon());
				ViewUtil.setText(view, R.id.name, hapic.getHeroTroopName()
						.getName() + "：");
				ViewUtil.setRichText(view, R.id.desc,
						hapic.getHeroEvolveArmPropDesc(hic));
				evolveArmprops.addView(view);
			}
		}
	}

	private void setHeroInfo() {
		ViewUtil.setRichText(heroLayout, R.id.heroDesc, hic.getColorTypeName()
				+ hic.getColorHeroName());
		IconUtil.setHeroIconScale(heroLayout.findViewById(R.id.iconLayout), hic);
		WorldLevelProp prop = WorldLevelInfoClient.getWorldLevelProp();

		if (hic.getTalent() >= prop.getMaxHeroTalent()
				&& hic.getStar() >= Constants.HERO_MAX_STAR - 1) {
			ViewUtil.setVisible(worldLevelDesc);
			ViewUtil.setRichText(worldLevelDesc, "世界等级达到"
					+ (WorldLevelInfoClient.worldLevel + 1) + "级，才能升级到更高品质");
			if (hic.isMaxStar())
				ViewUtil.disableButton(evolveBtn);
			else
				ViewUtil.enableButton(evolveBtn);
		} else {
			ViewUtil.setGone(worldLevelDesc);
			ViewUtil.enableButton(evolveBtn);
		}

		if (hic.isMaxStar()) {
			try {
				HeroQuality heroQuality = (HeroQuality) CacheMgr.heroQualityCache
						.get((byte) (hic.getTalent() + 1));
				ViewUtil.setRichText(
						newHeroLayout,
						R.id.heroDesc,
						StringUtil.getHeroTypeName(heroQuality)
								+ StringUtil.getHeroName(hic.getHeroProp(),
										heroQuality));
				IconUtil.setHeroIcon(
						newHeroLayout.findViewById(R.id.iconLayout),
						hic.getHeroProp(), heroQuality, 1, true);
			} catch (GameException e) {
				ViewUtil.setGone(newHeroLayout, R.id.heroDesc);
				ViewUtil.setGone(newHeroLayout.findViewById(R.id.iconLayout));
			}
			ViewUtil.setText(evolveDesc, "进化为1星");
		} else {
			ViewUtil.setRichText(newHeroLayout, R.id.heroDesc,
					hic.getColorTypeName() + hic.getColorHeroName());
			IconUtil.setHeroIcon(newHeroLayout.findViewById(R.id.iconLayout),
					hic.getHeroProp(), hic.getHeroQuality(), hic.getStar() + 1,
					true);
			ViewUtil.setText(evolveDesc,
					hic.getStar() + "星进化为" + (hic.getStar() + 1) + "星");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == evolveBtn) {
			if (isGuide) {
				new HeroEvolveInvoker(true).start();
				isGuide = false;
				return;
			}
			if (needCount <= selfCount)
				new HeroEvolveInvoker().start();
			else
				new HeroRmbEvolveTip("补足道具").show(soulItem, needCount,
						selfCount);
		}

	}

	private class HeroEvolveInvoker extends BaseInvoker {
		private HeroEvolveResp resp;
		private HeroInfoClient oldHero;
		private HeroInfoClient newHero;
		private int type = Constants.TYPE_NOAMRL;
		private boolean isGuide = false;

		public HeroEvolveInvoker() {
		}

		public HeroEvolveInvoker(int type) {
			this.type = type;
		}

		public HeroEvolveInvoker(boolean isGuide) {
			this.isGuide = isGuide;
		}

		@Override
		protected String loadingMsg() {
			return "将领进化";
		}

		@Override
		protected String failMsg() {
			return "将领进化失败";
		}

		@Override
		protected void fire() throws GameException {
			oldHero = hic.copy();
			resp = GameBiz.getInstance().heroEvolve(hic.getId(), type); // Constants.TYPE_NOAMRL
			newHero = resp.getHero();
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRi(), true);
			hic.update(resp.getHero());
			setValue();
			// if (hic.isMaxStar())
			controller.goBack();
			new HeroEvolveSuccessTip(oldHero, newHero, resp.getRi(), isGuide)
					.show();
		}
	}

	private class HeroRmbEvolveTip extends ItemNotEnoughTip {
		public HeroRmbEvolveTip(String title) {
			super(title);
		}

		private int needCurrency;

		@Override
		protected void setDesc() {
			ViewUtil.setText(desc1, "进化材料不足，还缺少以下物品");
			needCurrency = CalcUtil
					.upNum(item.getFunds()
							* (needCount - selfCount)
							* (CacheMgr.heroCommonConfigCache
									.getHeroEvolveDiscount() / 100f));
			ViewUtil.setRichText(
					desc2,
					"仅需"
							+ StringUtil.color(needCurrency + "元宝",
									R.color.color4)
							+ "，即可补齐"
							+ StringUtil.color((needCount - selfCount) + "个["
									+ item.getName() + "]", R.color.color4)
							+ "，让将领立刻进化！"
							+ StringUtil.color("（比商场购买材料更便宜）", R.color.color19));

		}

		@Override
		protected OnClickListener getClickListener() {
			return new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if (Account.user.getCurrency() < needCurrency) {
						new ToActionTip(needCurrency).show();
					} else {
						new HeroEvolveInvoker(Constants.TYPE_RMB).start();
					}
				}
			};
		}
	}
}
