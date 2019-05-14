package com.vikings.sanguo.model;

import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.utils.StringUtil;

public class ShowItem {

	private String name; // 名称

	private String paramStr0; // 用于存放特殊String字段

	private String paramStr1; // 用于存放特殊String字段

	private int paramInt; // 用于存放特殊int字段

	private String img; // 显示图片

	private int count;// 需要数量

	private int selfCount;// 拥有数量

	private int type;// 类型

	private boolean isRewardItem = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 账户中已经有的数量
	 * 
	 * @return
	 */
	public int getSelfCount() {
		return selfCount;
	}

	public void setSelfCount(int selfCount) {
		this.selfCount = selfCount;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String getParamStr0() {
		return paramStr0;
	}

	public void setParamStr0(String paramStr0) {
		this.paramStr0 = paramStr0;
	}

	public String getParamStr1() {
		return paramStr1;
	}

	public void setParamStr1(String paramStr1) {
		this.paramStr1 = paramStr1;
	}

	public int getParamInt() {
		return paramInt;
	}

	public void setParamInt(int paramInt) {
		this.paramInt = paramInt;
	}

	public boolean isRewardItem() {
		return isRewardItem;
	}

	public void setRewardItem(boolean isRewardItem) {
		this.isRewardItem = isRewardItem;
	}

	public static ShowItem fromAttr(int type, int value) {
		ShowItem item = new ShowItem();
		item.setType(type);
		item.setCount(value);
		item.setSelfCount(Account.user.getAttr(AttrType.valueOf(type)));
		String name = ReturnInfoClient.getAttrTypeName(type);
		if (StringUtil.isNull(name))
			return null;
		item.setName(name);
		item.setImg(ReturnInfoClient.getAttrTypeIconName(type));

		// AttrType 100以上特殊处理 不再常规消耗检查里
		if (type > 100)
			item.setSelfCount(Integer.MAX_VALUE);
		return item;
	}

	public static ShowItem fromItem(int id, int count) {
		try {
			ShowItem item = new ShowItem();
			Item i = (Item) CacheMgr.itemCache.get(id);
			item.setName(i.getName());
			item.setImg(i.getImage());
			item.setCount(count);
			ItemBag bag = Account.store.getItemBag(i);
			if (bag != null)
				item.setSelfCount(bag.getCount());
			// if (i.getClentType() == Item.TYPE_HERO_SOUL) {
			// HeroIdBaseInfoClient hic = (HeroIdBaseInfoClient)
			// CacheMgr.heroRecruitExchangeCache
			// .getHero(id);
			// if (null != hic) {
			// if (hic.getHeroProp() != null)
			// item.setParamStr1(hic.getHeroQuality().getName());
			// if (hic.getHeroQuality() != null)
			// item.setParamStr0(hic.getHeroQuality().getColor());
			// }
			// }
			if (null != bag && bag.isReward()) {
				item.setRewardItem(true);
			}
			return item;
		} catch (GameException e) {
			Log.e("ShowItem", e.getMessage());
			return null;
		}
	}

	public static ShowItem fromAmy(ArmInfoClient arm) {
		ShowItem item = new ShowItem();
		item.setName(arm.getProp().getName());
		item.setImg(arm.getProp().getIcon());
		item.setCount(arm.getCount());
		item.setSelfCount(0);
		return item;
	}

	public static ShowItem fromHero(HeroInfoClient hic) {
		ShowItem item = new ShowItem();
		item.setName(hic.getHeroProp().getName());
		item.setParamStr1(hic.getHeroQuality().getName());

		if (null != hic.getHeroQuality()) {
			item.setParamStr0(hic.getHeroQuality().getColor());
		}

		item.setImg(hic.getHeroProp().getIcon());
		item.setCount(1);
		item.setSelfCount(0);
		// 显示的时候用于区分，很蛋疼的办法
		item.setType(Integer.MAX_VALUE);
		return item;
	}

	public static ShowItem fromEquip(EquipmentInfoClient equip) {
		ShowItem item = new ShowItem();
		item.setName(equip.getProp().getName());
		item.setImg(equip.getProp().getIcon());
		item.setCount(1);
		item.setSelfCount(0);
		return item;
	}

	/**
	 * 是否满足条件 自己有的 >= 需求数量
	 * 
	 * @return
	 */
	public boolean isEnough() {
		return selfCount >= count;
	}

	// // 是否是将领相关属性
	// public boolean isHeroProp() {
	// AttrType at = AttrType.valueOf(type);
	// return (AttrType.ATTR_TYPE_HERO_EXP == at
	// || AttrType.ATTR_TYPE_HERO_STAMINA == at || AttrType.ATTR_TYPE_HERO_TYPE
	// == at);
	// }

	public boolean isHeroStamina() {
		return type == AttrType.ATTR_TYPE_HERO_STAMINA.number;
	}

	public String getCountDesc() {
		if (getSelfCount() >= getCount())
			return getSelfCount() + "/" + getCount();
		else
			return StringUtil.color("" + getSelfCount(), R.color.k7_color8)
					+ "/" + getCount();
	}
}
