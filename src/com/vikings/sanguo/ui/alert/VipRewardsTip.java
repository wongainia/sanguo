package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HeroInit;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroQuality;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.model.VipRewards;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class VipRewardsTip extends CustomConfirmDialog {

	private ViewGroup rewardsLayout;
	private UserVip userVip;
	private List<VipRewards> items = new ArrayList<VipRewards>();
	private List<VipRewards> heros = new ArrayList<VipRewards>();
	private List<VipRewards> arms = new ArrayList<VipRewards>();
	private List<VipRewards> equips = new ArrayList<VipRewards>();

	public VipRewardsTip(UserVip userVip) {
		super("VIP" + userVip.getLevel() + "礼品", LARGE);
		this.userVip = userVip;
		rewardsLayout = (ViewGroup) content.findViewById(R.id.rewardsLayout);
		setCloseBtn();
	}

	public void show() {
		super.show();
		setValue();
	}

	private void setValue() {
		List<VipRewards> list = CacheMgr.vipRewardsCache.search(userVip
				.getLevel());
		for (VipRewards reward : list) {
			switch (reward.getType()) {
			case VipRewards.TYPE_ITEM:
				items.add(reward);
				break;
			case VipRewards.TYPE_HERO:
				heros.add(reward);
				break;
			case VipRewards.TYPE_ARM:
				arms.add(reward);
				break;
			case VipRewards.TYPE_EQUIP:
				equips.add(reward);
				break;
			default:
				break;
			}
		}

		setHeros();
		setEquips();
		setArms();
		setItems();
		setSpecials();
	}

	private void setSpecials() {
		if (!StringUtil.isNull(userVip.getDesc())) {
			View view = controller.inflate(R.layout.vip_rewards_special,
					rewardsLayout, false);
			rewardsLayout.addView(view);
			ViewUtil.setRichText(view.findViewById(R.id.specialDesc),
					userVip.getVipSpecialDesc());
		}
	}

	private void setItems() {
		if (!items.isEmpty()) {
			ViewGroup layout = (ViewGroup) controller.inflate(
					R.layout.vip_rewards_layout, rewardsLayout, false);
			for (int i = 0; i < items.size(); i++) {
				VipRewards reward = items.get(i);
				try {
					Item item = (Item) CacheMgr.itemCache.get(reward.getId());
					if (null == item)
						continue;
					View view = controller.inflate(R.layout.vip_rewards_item,
							layout, false);
					layout.addView(view);
					new ViewImgScaleCallBack(item.getImage(),
							view.findViewById(R.id.icon),
							Constants.ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
							Constants.ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
					ViewUtil.setText(view.findViewById(R.id.name),
							item.getName() + "x" + reward.getCount());
					ViewUtil.setRichText(view.findViewById(R.id.desc),
							item.getDesc());
					if (i == items.size() - 1)
						ViewUtil.setGone(view, R.id.separateLine);
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void setArms() {
		if (!arms.isEmpty()) {
			for (VipRewards reward : arms) {
				try {
					TroopProp prop = (TroopProp) CacheMgr.troopPropCache
							.get(reward.getId());
					if (null == prop)
						continue;
					View view = controller.inflate(
							R.layout.vip_rewards_equipment, rewardsLayout,
							false);
					rewardsLayout.addView(view);
					new ViewImgScaleCallBack(prop.getIcon(),
							view.findViewById(R.id.icon),
							Constants.ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
							Constants.ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
					ViewUtil.setText(view.findViewById(R.id.name),
							prop.getName() + "x" + reward.getCount());
					ViewUtil.setRichText(view.findViewById(R.id.desc),
							prop.getDesc());
					ViewUtil.setGone(view.findViewById(R.id.rating));
					ViewUtil.setGone(view.findViewById(R.id.coordinatesDesc));
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void setEquips() {
		if (!equips.isEmpty()) {
			for (VipRewards reward : equips) {
				try {
					PropEquipment prop = (PropEquipment) CacheMgr.propEquipmentCache
							.get(reward.getId());
					if (null == prop)
						continue;
					View view = controller.inflate(
							R.layout.vip_rewards_equipment, rewardsLayout,
							false);
					rewardsLayout.addView(view);
					new ViewImgScaleCallBack(prop.getIcon(),
							view.findViewById(R.id.icon),
							Constants.ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
							Constants.ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
					ViewUtil.setText(view.findViewById(R.id.name),
							prop.getName());
					ViewUtil.setRichText(view.findViewById(R.id.desc),
							prop.getDesc());
					ViewUtil.setImage(view.findViewById(R.id.rating),
							prop.getRatingPic());
					// if (prop.isCoordinates()) {
					// ViewUtil.setVisible(view
					// .findViewById(R.id.coordinatesDesc));
					// ViewUtil.setText(
					// view.findViewById(R.id.coordinatesDesc),
					// "集齐套装属性增加");
					// } else {
					ViewUtil.setGone(view.findViewById(R.id.coordinatesDesc));
					// }
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void setHeros() {
		if (!heros.isEmpty()) {
			for (VipRewards reward : heros) {
				try {
					HeroInit init = (HeroInit) CacheMgr.heroInitCache
							.get(reward.getId());
					if (null == init)
						continue;
					HeroProp heroProp = (HeroProp) CacheMgr.heroPropCache
							.get(init.getHeroId());
					if (null == heroProp)
						continue;
					HeroQuality heroQuality = (HeroQuality) CacheMgr.heroQualityCache
							.get(init.getTalent());
					if (null == heroQuality)
						continue;
					View view = controller.inflate(R.layout.vip_rewards_hero,
							rewardsLayout, false);
					rewardsLayout.addView(view);
					IconUtil.setHeroIcon(view, heroProp, heroQuality,
							init.getStar(), true);
					ViewUtil.setRichText(
							view,
							R.id.name,
							StringUtil.getHeroTypeName(heroQuality)
									+ "  "
									+ StringUtil.getHeroName(heroProp,
											heroQuality));

					ViewUtil.setImage(view.findViewById(R.id.rating),
							heroProp.getRatingPic());
					ViewUtil.setText(view.findViewById(R.id.attack), "武力："
							+ init.getAttack());
					ViewUtil.setText(view.findViewById(R.id.defend), "防护："
							+ init.getDefend());
					ViewUtil.setRichText(view.findViewById(R.id.desc),
							heroProp.getDesc());
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_vip_rewards, contentLayout,
				false);
	}

}
