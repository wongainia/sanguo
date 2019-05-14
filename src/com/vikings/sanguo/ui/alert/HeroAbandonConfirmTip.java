package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.HeroAbandonInvoker;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.HeroAbandonExpToItem;
import com.vikings.sanguo.model.HeroEvolve;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroType;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HeroAbandonConfirmTip extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_hero_abandon_confirm;

	private List<HeroInfoClient> hics = new ArrayList<HeroInfoClient>();;
	private TextView desc;
	private ViewGroup gainContent;
	private Button abandonBtn;
	private List<HeroAbandonExpToItem> list;
	private int totalExp;
	private List<GainInfo> infos = new ArrayList<GainInfo>();
	private CallBack callBack;

	public HeroAbandonConfirmTip(List<HeroInfoClient> hics) {
		super("分解将领");
		this.hics = hics;
	}

	public HeroAbandonConfirmTip(HeroInfoClient hic, CallBack callBack) {
		super("分解将领");
		this.callBack = callBack;
		if (hic != null) {
			hics.add(hic);
		}
	}

	public void show() {
		super.show();
		desc = (TextView) content.findViewById(R.id.desc);
		gainContent = (ViewGroup) content.findViewById(R.id.gainContent);
		abandonBtn = (Button) content.findViewById(R.id.abandonBtn);
		abandonBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				new HeroAbandonInvoker(hics, callBack).start();
			}
		});
		setValue();
	}

	private void setValue() {
		String num = StringUtil.color(String.valueOf(hics.size()), Config
				.getController().getResourceColorText(R.color.color19));
		String abandonTip = "确定分解 " + num + " 名将领,  将获得以下物品";
		ViewUtil.setRichText(desc, abandonTip);

		if (ListUtil.isNull(hics)) {
			return;
		}

		for (HeroInfoClient hic : hics) {
			// 添加分解后获得的技能书
			if (hic.getHeroProp().getAbadonItemId() > 0) {
				addGainItem(hic.getHeroProp().getAbadonItemId(), 1);
			}
			// 经验丹
			list = CacheMgr.heroAbandonExpToItemCache.getAll();
			totalExp = hic.getAbandonExp();
			exp2Item();
			// 强化丸
			int totalArmProp = hic.getAbandonArmPropValue();
			if (totalArmProp >= CacheMgr.heroCommonConfigCache
					.getHeroStrengthenAddByItem()) {
				int itemId = CacheMgr.dictCache.getDictInt(
						Dict.TYPE_HERO_ENHANCE, 1);
				if (itemId > 0) {
					addGainItem(
							itemId,
							totalArmProp
									/ CacheMgr.heroCommonConfigCache
											.getHeroStrengthenAddByItem());
				}
			}
			// 将魂
			HeroEvolve heroEvolve = null;
			try {
				heroEvolve = (HeroEvolve) CacheMgr.heroEvolveCache.get(hic
						.getHeroId());
			} catch (GameException e) {
				Log.e("HeroAbandonConfirmTip", e.getErrorMsg());
			}
			if (null != heroEvolve) {
				int count = 0;
				for (int i = hic.getTalent(); i > 0; i--) {
					for (int j = (i == hic.getTalent() ? hic.getStar()
							: Constants.HERO_MAX_STAR); j > 0; j--) {
						if (count > 3)
							break;
						count++;
						// 过滤当前品质
						if (j != hic.getStar() || i != hic.getTalent()) {
							HeroType type = null;
							try {
								type = CacheMgr.heroTypeCache.getHeroType(i, j);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (type != null) {
								if (type.getSoulId() != 0) {
									addGainItem(type.getSoulId(), (int) (type
											.getSoulRate() / 100f * heroEvolve
											.getSoulCount()));
								}
							}
						}
					}
					if (count > 3)
						break;
				}
			}
		}
		ViewGroup vg = (ViewGroup) controller.inflate(
				R.layout.hero_abandon_goods, gainContent, false);
		ViewGroup content0 = (ViewGroup) vg.findViewById(R.id.content);
		ViewGroup content1 = (ViewGroup) vg.findViewById(R.id.content1);
		for (int i = 0; i < infos.size(); i++) {
			GainInfo info = infos.get(i);
			if (null == info.getItem())
				continue;
			{
				if (info.getCount() > 0) {
					ShowItem showItem = ShowItem.fromItem(info.getItemId(),
							info.getCount());
					if ((i + 1) % 2 == 0) {
						content1.addView(ViewUtil.getShowItemView(showItem,
								R.color.color7, false, false));
					} else {
						content0.addView(ViewUtil.getShowItemView(showItem,
								R.color.color7, false, false));
					}
				}
			}
		}
		gainContent.addView(vg);
	}

	private void exp2Item() {
		if (null != list && !list.isEmpty()) {
			HeroAbandonExpToItem haeti = list.get(0);
			int count = totalExp / haeti.getExp();
			if (count > 0) {
				totalExp = totalExp - count * haeti.getExp();
				addGainItem(haeti.getItemId(), count);
			}
			list.remove(0);
		}
		if (null != list && !list.isEmpty())
			exp2Item();
	}

	private void addGainItem(int itemId, int count) {
		try {
			for (GainInfo info : infos) {
				if (info.getItemId() == itemId) {
					info.setCount(info.getCount() + count);
					return;
				}
			}
			GainInfo info = new GainInfo(itemId, count);
			infos.add(info);
		} catch (GameException e) {
			Log.e("HeroAbandonConfirmTip", e.getMessage());
		}
	}

	@Override
	public View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	private class GainInfo {
		private int itemId;
		private int count;
		private Item item;

		public GainInfo(int itemId, int count) throws GameException {
			this.itemId = itemId;
			this.count = count;
			this.item = (Item) CacheMgr.itemCache.get(itemId);
		}

		public int getItemId() {
			return itemId;
		}

		public void setItemId(int itemId) {
			this.itemId = itemId;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public Item getItem() {
			return item;
		}

		public void setItem(Item item) {
			this.item = item;
		}

	}
}
