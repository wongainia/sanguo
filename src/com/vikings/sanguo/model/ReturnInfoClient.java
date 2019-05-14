package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.protos.RES_DATA_TYPE;
import com.vikings.sanguo.protos.ReturnAttrInfo;
import com.vikings.sanguo.protos.ReturnInfo;
import com.vikings.sanguo.protos.ReturnThingInfo;
import com.vikings.sanguo.protos.RoleAttrInfo;
import com.vikings.sanguo.protos.ThingType;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 
 * 新的probuf returninfo 统一使用
 * 
 * @author Brad.Chen
 * 
 */
public class ReturnInfoClient extends AttrData {

	// 客户端用透传文字
	private String msg;

	private CheckInRewards checkInRewards;

	private List<ItemBag> itemPack = new ArrayList<ItemBag>();

	private List<BuildingInfoClient> buildings = new ArrayList<BuildingInfoClient>();

	private List<ArmInfoClient> armInfos = new ArrayList<ArmInfoClient>();

	private List<HeroInfoClient> heroInfos = new ArrayList<HeroInfoClient>();

	private List<EquipmentInfoClient> equipInfos = new ArrayList<EquipmentInfoClient>();

	private ReturnInfo returnInfo;

	public ReturnInfoClient() {
		returnInfo = new ReturnInfo();
		List<ReturnAttrInfo> attrs = new ArrayList<ReturnAttrInfo>();
		returnInfo.setRaisList(attrs);
	}

	public CheckInRewards getCheckInRewards() {
		return checkInRewards;
	}

	public void setCheckInRewards(CheckInRewards checkInRewards) {
		this.checkInRewards = checkInRewards;
	}

	@Override
	protected List<RoleAttrInfo> getRoleAttrInfos() {
		return null;
	}

	@Override
	protected List<ReturnAttrInfo> getReturnAttrInfos() {
		if (returnInfo != null)
			return returnInfo.getRaisList();
		else
			return null;
	}

	public int getRealExp() {
		if (getLevel() == 0) {
			return getExp();
		}

		int gainExpTotal = this.getExp();
		try {
			for (int i = 1; i <= this.getLevel(); i++) {
				PropUser l = (PropUser) CacheMgr.propUserCache
						.get((Account.user.getLevel() - i));
				gainExpTotal += l.getExpTotal();
			}
		} catch (GameException e) {
		}
		return gainExpTotal;
	}

	public ReturnInfo getReturnInfo() {
		return returnInfo;
	}

	public List<ItemBag> getItemPack() {
		return itemPack;
	}

	public List<ArmInfoClient> getArmInfos() {
		return armInfos;
	}

	public List<BuildingInfoClient> getBuildings() {
		return buildings;
	}

	public List<HeroInfoClient> getHeroInfos() {
		return heroInfos;
	}

	public List<EquipmentInfoClient> getEquipInfos() {
		return equipInfos;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getUserId() {
		return returnInfo.getUserid();
	}

	public String value(int i) {
		if (i > 0)
			return "+" + i;
		else if (i == 0)
			return "";
		else
			return "" + i;
	}

	public int abs(int i) {
		return Math.abs(i);
	}

	/**
	 * 
	 * @param isGain
	 *            true表示获得物品，false 消耗物品
	 * @param rewards
	 *            是否显示奖励物品
	 * @return
	 */
	public String toDesc(boolean isGain, boolean rewards) {
		StringBuilder buf = new StringBuilder();
		StringBuilder gain = new StringBuilder(); // "<br/>获得物品："
		StringBuilder pay = new StringBuilder(); // "<br/>消耗物品："
		StringBuilder troop = new StringBuilder(); // "<br/>获得士兵："
		StringBuilder hero = new StringBuilder(); // "<br/>获得将领："
		if (!StringUtil.isNull(msg)) {
			buf.append("<b>" + msg + "</b><br/>");
		}
		if (getLevel() != 0)
			buf.append("#lv_bg#").append(value(getLevel()) + " ");
		if (getRealExp() != 0)
			buf.append("#exp#").append(value(getRealExp()) + " ");
		if (getMoney() != 0)
			buf.append("#money#").append(value(getMoney()) + " ");
		if (getCurrency() != 0)
			buf.append("#rmb#").append(value(getCurrency()) + " ");
		if (getExploit() != 0)
			buf.append("#exploit#").append(value(getExploit()) + " ");
		if (getWood() != 0)
			buf.append("#mu#").append(value(getWood()) + " ");
		if (getFood() != 0)
			buf.append("#fief_food#").append(value(getFood()) + " ");
		if (getMaterial0() != 0)
			buf.append("#"
					+ getAttrTypeIconName(AttrType.ATTR_TYPE_MATERIAL_0.number)
					+ "#" + value(getMaterial0()) + " ");
		if (getMaterial1() != 0)
			buf.append("#"
					+ getAttrTypeIconName(AttrType.ATTR_TYPE_MATERIAL_1.number)
					+ "#" + value(getMaterial1()) + " ");

		if (itemPack != null && !itemPack.isEmpty()) {
			for (ItemBag it : itemPack) {
				if (it.getCount() > 0) {
					if (it.getSource() == Constants.COMMON_ITEM
							|| (it.getSource() == Constants.GIFT_ITEM && rewards)) {
						gain.append("<br/>#" + it.getItem().getImage() + "#"
								+ it.getItem().getName() + "x" + it.getCount());
					}
				} else {
					pay.append("<br/>#" + it.getItem().getImage() + "#"
							+ it.getItem().getName() + value(it.getCount()));
				}
			}
		}
		if (buildings != null && !buildings.isEmpty()) {
			for (BuildingInfoClient info : buildings) {
				gain.append("<br/>#" + info.getProp().getImage() + "#"
						+ info.getProp().getBuildingName() + "x 1");
			}
		}

		// 获得了军队
		if (armInfos != null && !armInfos.isEmpty()) {
			for (ArmInfoClient armInfo : armInfos) {
				try {
					TroopProp prop = (TroopProp) CacheMgr.troopPropCache
							.get(armInfo.getId());
					if (null != prop) {
						troop.append("<br/>#" + prop.getIcon() + "#"
								+ prop.getName() + "x " + armInfo.getCount());
					}
				} catch (GameException e) {

				}

			}
		}

		if (heroInfos != null && !heroInfos.isEmpty()) {
			for (HeroInfoClient heroInfo : heroInfos) {
				if (null != heroInfo.getHeroProp()) {
					hero.append("<br/>#" + heroInfo.getHeroProp().getIcon()
							+ "#" + heroInfo.getStar() + "★"
							+ heroInfo.getHeroQuality().getName() + " "
							+ heroInfo.getHeroProp().getName() + "x1");
				}
			}
		}

		if (equipInfos != null && !equipInfos.isEmpty()) {
			for (EquipmentInfoClient eic : equipInfos) {
				// TODO
			}
		}

		if (isGain && gain.length() > 10) {
			buf.append(gain);
		}

		if (isGain && troop.length() > 10) {
			buf.append(troop);
		}

		if (isGain && hero.length() > 10) {
			buf.append(hero);
		}

		if (!isGain && pay.length() > 10) {
			buf.append(pay);
		}
		return buf.toString();
	}

	public String toTextDesc(boolean isGain) {
		StringBuilder buf = new StringBuilder();
		StringBuilder gain = new StringBuilder(); // "<br/>获得物品："
		StringBuilder pay = new StringBuilder(); // "<br/>消耗物品："
		StringBuilder troop = new StringBuilder(); // "<br/>获得士兵："
		StringBuilder hero = new StringBuilder(); // "<br/>获得将领："
		StringBuilder equip = new StringBuilder();// 获得装备

		if (!StringUtil.isNull(msg)) {
			buf.append("<b>" + msg + "</b><br/>");
		}

		if (getLevel() != 0)
			buf.append(getLevel()
					+ getAttrTypeName(AttrType.ATTR_TYPE_LEVEL.number) + ",");
		if (getRealExp() != 0)
			buf.append(getRealExp()
					+ getAttrTypeName(AttrType.ATTR_TYPE_EXP.number) + ",");

		if (isGain && getMoney() > 0)
			buf.append(getMoney()
					+ getAttrTypeName(AttrType.ATTR_TYPE_MONEY.number) + ",");
		else if (!isGain && getMoney() < 0)
			buf.append(getMoney()
					+ getAttrTypeName(AttrType.ATTR_TYPE_MONEY.number) + ",");

		if (isGain && getCurrency() > 0)
			buf.append(getCurrency()
					+ getAttrTypeName(AttrType.ATTR_TYPE_CURRENCY.number) + ",");
		else if (!isGain && getCurrency() < 0)
			buf.append(getCurrency()
					+ getAttrTypeName(AttrType.ATTR_TYPE_CURRENCY.number) + ",");

		if (getExploit() != 0)
			buf.append(getExploit()
					+ getAttrTypeName(AttrType.ATTR_TYPE_EXPLOIT.number) + ",");
		if (getWood() != 0)
			buf.append(getWood()
					+ getAttrTypeName(AttrType.ATTR_TYPE_WOOD.number) + ",");
		if (getFood() != 0)
			buf.append(getFood()
					+ getAttrTypeName(AttrType.ATTR_TYPE_FOOD.number) + ",");
		if (getHeroExp() != 0)
			buf.append(getHeroExp()
					+ getAttrTypeName(AttrType.ATTR_TYPE_HERO_EXP.number) + ",");
		if (getHeroStamina() != 0)
			buf.append(getHeroStamina() + "点"
					+ getAttrTypeName(AttrType.ATTR_TYPE_HERO_STAMINA.number)
					+ ",");
		if (getMaterial0() != 0)
			buf.append(getMaterial0()
					+ getAttrTypeName(AttrType.ATTR_TYPE_MATERIAL_0.number)
					+ ",");
		if (getMaterial1() != 0)
			buf.append(getMaterial1()
					+ getAttrTypeName(AttrType.ATTR_TYPE_MATERIAL_1.number)
					+ ",");

		if (itemPack != null && !itemPack.isEmpty()) {
			for (ItemBag it : itemPack) {
				if (it.getSource() == Constants.COMMON_ITEM) {
					if (it.getCount() > 0) {
						gain.append(it.getCount() + "个"
								+ it.getItem().getName() + ",");
					} else if (it.getCount() < 0) {
						pay.append(it.getCount() + "个" + it.getItem().getName()
								+ ",");
					}
				}
			}
		}
		if (buildings != null && !buildings.isEmpty()) {
			for (BuildingInfoClient info : buildings) {
				gain.append("1个" + info.getProp().getBuildingName() + ",");
			}
		}

		// 获得了军队
		if (armInfos != null && !armInfos.isEmpty()) {
			for (ArmInfoClient armInfo : armInfos) {
				try {
					TroopProp prop = (TroopProp) CacheMgr.troopPropCache
							.get(armInfo.getId());
					if (null != prop) {
						troop.append(armInfo.getCount() + "个" + prop.getName()
								+ ",");
					}
				} catch (GameException e) {

				}

			}
		}

		if (heroInfos != null && !heroInfos.isEmpty()) {
			for (HeroInfoClient heroInfo : heroInfos) {
				if (null != heroInfo.getHeroProp()) {
					hero.append("1个" + heroInfo.getHeroProp().getName() + ",");
				}
			}
		}

		if (equipInfos != null && !equipInfos.isEmpty()) {
			for (EquipmentInfoClient eic : equipInfos) {
				equip.append("1件" + eic.getProp().getName());
			}
		}

		if (isGain && gain.length() > 0) {
			buf.append(gain);
		}

		if (isGain && troop.length() > 0) {
			buf.append(troop);
		}

		if (isGain && hero.length() > 0) {
			buf.append(hero);
		}

		if (isGain && equip.length() > 0) {
			buf.append(equip);
		}

		if (!isGain && pay.length() > 0) {
			buf.append(pay);
		}

		// 去掉，
		if (buf.length() > 0)
			buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	public boolean isLevelUp() {
		return getLevel() != 0;
	}

	public boolean isReturnNothing() {
		for (ReturnAttrInfo rai : returnInfo.getRaisList()) {
			if (rai.getValue().intValue() > 0)
				return false;
		}
		for (ReturnThingInfo rti : returnInfo.getRtisList()) {
			if (rti.getCount().intValue() > 0)
				return false;
		}
		return true;
	}

	static private <T> void addList(List<T> list, T i) {
		if (list == null)
			list = new ArrayList<T>();
		list.add(i);
	}

	/**
	 * 用于提示掉落或者消耗
	 * 
	 * @param returnInfo
	 */
	public void setReturnInfo(ReturnInfo returnInfo) {
		this.returnInfo = returnInfo;
	}

	/**
	 * 检查是否满足需求条件
	 * 
	 * @return
	 */
	public ArrayList<ShowItem> checkRequire() {
		ArrayList<ShowItem> show = new ArrayList<ShowItem>();
		for (ShowItem si : showRequire()) {
			if (!si.isEnough())
				show.add(si);
		}
		return show;
	}

	/**
	 * 显示需求条件
	 * 
	 * @return
	 */
	public ArrayList<ShowItem> showRequire() {
		return showRequire(true);
	}

	public ArrayList<ShowItem> showRequire(boolean rewards) {
		ArrayList<ShowItem> show = new ArrayList<ShowItem>();
		if (returnInfo.hasRais()) {
			for (ReturnAttrInfo ri : returnInfo.getRaisList()) {
				ShowItem si = ShowItem.fromAttr(ri.getType(), ri.getValue());
				if (si != null)
					show.add(si);
			}
		}
		for (ItemBag i : itemPack) {
			if (!rewards && i.getSource() == Constants.GIFT_ITEM)
				continue;
			ShowItem si = ShowItem.fromItem(i.getItemId(), i.getCount());
			if (si != null)
				show.add(si);
		}
		for (ArmInfoClient ai : armInfos) {
			ShowItem si = ShowItem.fromAmy(ai);
			show.add(si);
		}
		// 处理英雄
		for (HeroInfoClient hic : heroInfos) {
			ShowItem si = ShowItem.fromHero(hic);
			show.add(si);
		}

		// 处理装备
		for (EquipmentInfoClient equip : equipInfos) {
			ShowItem si = ShowItem.fromEquip(equip);
			show.add(si);
		}
		return show;
	}

	/**
	 * 特殊处理配置中 特别的配置武将需消耗体力
	 * 
	 * @return
	 */
	public int getCfgHeroStamina() {
		if (returnInfo.hasRais()) {
			for (ReturnAttrInfo ri : returnInfo.getRaisList()) {
				if (AttrType.valueOf(ri.getType()) == AttrType.ATTR_TYPE_HERO_STAMINA) {
					return ri.getValue();
				}
			}
		}
		return 0;
	}

	public ArrayList<ShowItem> showReturn(boolean gain) {
		return showReturn(gain, true);
	}

	/**
	 * 
	 * @param gain
	 *            显示得到的物品还是消耗的
	 * @param heroColor
	 *            是否根据将领品质显示颜色
	 * @param rewards
	 *            是否显示奖励物品
	 * @return
	 */
	public ArrayList<ShowItem> showReturn(boolean gain, boolean rewards) {
		ArrayList<ShowItem> msg = new ArrayList<ShowItem>();
		ArrayList<ShowItem> ls = showRequire(rewards);

		for (ShowItem showItem : ls) {
			if (gain && showItem.getCount() > 0) {
				msg.add(showItem);
			} else if (!gain && showItem.getCount() < 0) {
				msg.add(showItem);
			}
		}
		return msg;
	}

	/**
	 * 统一的由 配置表中 类型 ID value 构造returninfoclient
	 * 
	 * @param type
	 * @param id
	 * @param value
	 */
	public void addCfg(int type, int id, int value) {
		switch (RES_DATA_TYPE.valueOf(type)) {
		case RES_DATA_TYPE_ATTR:
			ReturnAttrInfo attr = new ReturnAttrInfo();
			attr.setType(id);
			attr.setValue(value);
			returnInfo.getRaisList().add(attr);
			break;
		case RES_DATA_TYPE_ITEM:
			ItemBag bag = new ItemBag();
			bag.setId(0);
			bag.setItemId(id);
			bag.setCount(value);
			try {
				bag.setItem((Item) CacheMgr.itemCache.get(id));
			} catch (GameException e) {
				e.printStackTrace();
			}
			getItemPack().add(bag);
			break;
		case RES_DATA_TYPE_ARM:
			try {
				getArmInfos().add(new ArmInfoClient(id, value));
			} catch (GameException e) {
				e.printStackTrace();
			}
			break;
		case RES_DATA_TYPE_EQUIPMENT:
			try {
				getEquipInfos().add(new EquipmentInfoClient(id));
			} catch (GameException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	public static ReturnInfoClient convert2Client(ReturnInfo ri,
			boolean updateUserData) throws GameException {
		if (ri == null)
			return null;
		ReturnInfoClient resultInfo = new ReturnInfoClient();
		resultInfo.returnInfo = ri;
		List<Long> newHeroIds = new ArrayList<Long>();
		if (ri.hasRtis()) {
			for (ReturnThingInfo rti : ri.getRtisList()) {
				switch (ThingType.valueOf(rti.getType())) {
				case THING_TYPE_ITEM:
					ItemBag bag = new ItemBag();
					bag.setId(rti.getGuid());
					bag.setItemId(rti.getThingid().intValue());
					bag.setCount(rti.getCount());
					bag.setItem((Item) CacheMgr.itemCache.get(bag.getItemId()));
					bag.setSource(rti.getParam0()); // .intValue()
					addList(resultInfo.itemPack, bag);
					break;
				case THING_TYPE_ARM:
					ArmInfoClient ai = new ArmInfoClient(rti.getThingid()
							.intValue(), rti.getCount());
					addList(resultInfo.armInfos, ai);
					break;
				case THING_TYPE_BUILDING:
					BuildingInfoClient bi = new BuildingInfoClient(
							rti.getThingid());
					addList(resultInfo.buildings, bi);
					break;
				case THING_TYPE_HERO:
					if (rti.getCount() > 0)
						newHeroIds.add(rti.getGuid());
					break;
				case THING_TYPE_EQUIPMENT:
					int initId = rti.getParam0(); // 同服务器协商，该字段传装备的方案号
					if (initId > 0) {
						EquipmentInit init = (EquipmentInit) CacheMgr.equipmentInitCache
								.get(initId);
						EquipmentInfoClient equip = new EquipmentInfoClient(
								init.getId(), init.getInitQuality(), 0, 0);
						addList(resultInfo.equipInfos, equip);
					}
				default:
					break;
				}
			}
		}
		// 填武将数据
		if (newHeroIds.size() > 0) {
			for (long id : newHeroIds) {
				if (Account.heroInfoCache.get(id) == null) {
					Account.heroInfoCache.synDiff();
					break;
				}
			}
			for (long id : newHeroIds) {
				HeroInfoClient h = Account.heroInfoCache.get(id);
				if (h != null)
					addList(resultInfo.heroInfos, h);
			}
		}

		// 更新装备数据
		if (resultInfo.equipInfos != null && !resultInfo.equipInfos.isEmpty()) {
			Account.equipmentCache.synDiff();
		}

		// 更新本地数据
		if (ri.getUserid() == Account.user.getId() && updateUserData) {
			Account.updateData(resultInfo);
		}
		return resultInfo;
	}

	public static ReturnInfoClient convert2Client(ReturnInfo ri)
			throws GameException {
		return convert2Client(ri, true);
	}

	public static String getAttrTypeName(int type) {
		switch (AttrType.valueOf(type)) {
		case ATTR_TYPE_EXP:
			return Constants.USER_EXP;
		case ATTR_TYPE_EXPLOIT:
			return "功勋";
		case ATTR_TYPE_LEVEL:
			return "玩家等级";
		case ATTR_TYPE_CURRENCY:
			return "元宝";
		case ATTR_TYPE_MONEY:
			return "金币";
		case ATTR_TYPE_FOOD:
			return "粮草";
		case ATTR_TYPE_WOOD:
			return "木材";
		case ATTR_TYPE_MATERIAL_0:
			return "镔铁";
		case ATTR_TYPE_MATERIAL_1:
			return "皮革";
		case ATTR_TYPE_HERO_EXP:
			return Constants.HERO_EXP;
		case ATTR_TYPE_HERO_STAMINA:
			return "武将体力";
		default:
			return "";
		}
	}

	public static String getAttrTypeIconName(int type) {

		switch (AttrType.valueOf(type)) {
		case ATTR_TYPE_EXP:
			return "exp";
		case ATTR_TYPE_EXPLOIT:
			return "exploit";
		case ATTR_TYPE_LEVEL:
			return "lv";
		case ATTR_TYPE_CURRENCY:
			return "rmb";
		case ATTR_TYPE_MONEY:
			return "money";
		case ATTR_TYPE_FOOD:
			return "fief_food";
		case ATTR_TYPE_WOOD:
			return "mu";
		case ATTR_TYPE_MATERIAL_0:
			return "tie";
		case ATTR_TYPE_MATERIAL_1:
			return "pi";
		case ATTR_TYPE_HERO_EXP:
			return "hero_exp";
		default:
			return "";
		}

	}

	public static String getAttrTypeBigIconName(int type) {
		switch (AttrType.valueOf(type)) {
		case ATTR_TYPE_EXP:
			return "exp";
		case ATTR_TYPE_EXPLOIT:
			return "big_icon_merit.png";
		case ATTR_TYPE_LEVEL:
			return "lv";
		case ATTR_TYPE_CURRENCY:
			return "big_icon_ingot.png";
		case ATTR_TYPE_MONEY:
			return "big_icon_gold.png";
		case ATTR_TYPE_FOOD:
			return "item_group_rice.png";
		case ATTR_TYPE_WOOD:
			return "item_group_wood.png";
		case ATTR_TYPE_MATERIAL_0:
			return "item_group_iron.png";
		case ATTR_TYPE_MATERIAL_1:
			return "item_group_leather.png";
		case ATTR_TYPE_HERO_EXP:
			return "hero_exp";
		default:
			return "";
		}

	}

	public static int getAttrTypeIcon(int type) {
		switch (AttrType.valueOf(type)) {
		case ATTR_TYPE_EXP:
			return R.drawable.exp;
		case ATTR_TYPE_EXPLOIT:
			return R.drawable.exploit;
		case ATTR_TYPE_LEVEL:
			return R.drawable.lv;
		case ATTR_TYPE_CURRENCY:
			return R.drawable.rmb;
		case ATTR_TYPE_MONEY:
			return R.drawable.money;
		case ATTR_TYPE_FOOD:
			return R.drawable.fief_food;
		case ATTR_TYPE_WOOD:
			return R.drawable.mu;
		case ATTR_TYPE_MATERIAL_0:
			return R.drawable.tie;
		case ATTR_TYPE_MATERIAL_1:
			return R.drawable.pi;
		case ATTR_TYPE_HERO_EXP:
			return R.drawable.hero_exp;
		default:
			return 0;
		}
	}

	public ReturnInfoClient mergeBonus(ReturnInfoClient ric) {
		if (null == ric)
			return this;

		this.setCurrency(this.getCurrency() + ric.getCurrency());
		this.setExp(this.getExp() + ric.getExp());
		this.setExploit(this.getExploit() + ric.getExploit());
		this.setFood(this.getFood() + ric.getFood());
		this.setLevel(this.getLevel() + ric.getLevel());
		this.setMaterial0(this.getMaterial0() + ric.getMaterial0());
		this.setMaterial1(this.getMaterial1() + ric.getMaterial1());
		this.setMoney(this.getMoney() + ric.getMoney());
		this.setScore(this.getScore() + ric.getScore());
		this.setWood(this.getWood() + ric.getWood());
		this.setHeroExp(this.getHeroExp() + ric.getHeroExp());
		this.setHeroStamina(this.getHeroStamina() + ric.getHeroStamina());
		this.setHeroType(this.getHeroType() + ric.getHeroType());

		ListUtil.mergeItemBag(ric.getItemPack(), this.getItemPack());
		if (!ListUtil.isNull(ric.getBuildings()))
			this.getBuildings().addAll(ric.getBuildings());
		ListUtil.mergeArmInfo(ric.getArmInfos(), this.getArmInfos());
		if (!ListUtil.isNull(ric.getHeroInfos()))
			this.getHeroInfos().addAll(ric.getHeroInfos());

		return this;
	}
}
