package com.vikings.sanguo.cache;

import java.util.List;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.BuildingRequirement;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.WorldLevelInfoClient;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.utils.StringUtil;

public class BuildingRequirementCache extends LazyLoadArrayCache {
	private static final String FILE_NAME = "building_requirement.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((BuildingRequirement) obj).getBuildingId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((BuildingRequirement) obj).getValue(),
				((BuildingRequirement) obj).getExtension());
	}

	@Override
	public Object fromString(String line) {
		return BuildingRequirement.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	// 判断是否解锁(2013年6月9日15:32:25付斌确认解锁只判断玩家等级和前置建筑)
	public static boolean unlockByCurrency(int id, boolean checkLv) {
		return unlock(id, checkLv);
	}

	@SuppressWarnings("unchecked")
	public static boolean unlock(int id, boolean checkLv) {
		List<BuildingRequirement> requirements = CacheMgr.buildingRequireMentCache
				.search(id);
		for (BuildingRequirement requirement : requirements) {
			switch (requirement.getType()) {
			case BuildingRequirement.TYPE_RESOURCES:
				if (requirement.getValue() == AttrType.ATTR_TYPE_LEVEL
						.getNumber()) {
					if (checkLv
							&& Account.user.getLevel() < requirement
									.getExtension()) {
						return false;
					}
				}
				break;
			case BuildingRequirement.TYPE_BUILDING:
				boolean has = false;
				for (BuildingInfoClient bic : Account.manorInfoClient
						.getBuildingInfos()) {
					if (bic.getProp().getType() == requirement.getValue()
							&& bic.getProp().getLevel() >= requirement
									.getExtension()) {
						has = true;
						break;
					}
				}
				if (!has) {
					return false;
				}
				break;
			default:
				break;
			}

		}
		return true;

	}

	// 资源和元宝的判断条件只差一个等级
	public static boolean unlockByResource(int id) {
		return unlockByCurrency(id, true);
	}

	@SuppressWarnings("unchecked")
	public static String getRequirement(int id) {
		StringBuffer buf = new StringBuffer();
		List<BuildingRequirement> list = CacheMgr.buildingRequireMentCache
				.search(id);
		for (BuildingRequirement br : list) {
			switch (br.getType()) {
			case BuildingRequirement.TYPE_RESOURCES:
				if (br.getValue() == AttrType.ATTR_TYPE_EXPLOIT.getNumber()) {
					buf.append(
							Account.user.getExploit() >= br.getExtension() ? "功勋"
									+ br.getExtension()
									: StringUtil.color(
											"功勋" + br.getExtension(), "red"))
							.append("、");
				} else if (br.getValue() == AttrType.ATTR_TYPE_SCORE
						.getNumber()) {
					buf.append(
							Account.user.getScore() >= br.getExtension() ? "成就"
									+ br.getExtension() : StringUtil.color("成就"
									+ br.getExtension(), "red")).append("、");
				} else if (br.getValue() == AttrType.ATTR_TYPE_LEVEL
						.getNumber()) {
					buf.append(
							Account.user.getLevel() >= br.getExtension() ? "玩家等级"
									+ br.getExtension()
									: StringUtil.color(
											"玩家等级" + br.getExtension(), "red"))
							.append("、");
				}
				break;
			case BuildingRequirement.TYPE_BUILDING:
				BuildingProp prop = CacheMgr.buildingPropCache
						.getBuildingByTypeAndLevel(br.getValue(),
								(short) br.getExtension());
				if (null != prop) {
					BuildingInfoClient bic = Account.manorInfoClient
							.getBuilding(prop);
					if (null != bic
							&& bic.getProp().getLevel() >= prop.getLevel()) {
						buf.append(
								prop.getBuildingName() + "Lv" + prop.getLevel())
								.append("、");
					} else {
						buf.append(
								StringUtil.color(prop.getBuildingName() + "Lv"
										+ prop.getLevel(), "red")).append("、");
					}
				}
				break;
			case BuildingRequirement.TYPE_TOOLS:
				Item item = CacheMgr.getItemByID(br.getValue());
				if (null != item) {
					ItemBag itemBag = Account.store.getItemBag(item);
					if (null != itemBag
							&& itemBag.getCount() >= br.getExtension()) {
						buf.append(item.getName() + br.getExtension() + "个")
								.append("、");
					} else {
						buf.append(
								StringUtil.color(
										item.getName() + br.getExtension()
												+ "个", "red")).append("、");
					}
				}
				break;
			case BuildingRequirement.TYPE_VIPS:// 根据vip等级解锁相应建筑
				buf.append(
						br.getValue() <= Account.getCurVip().getLevel() ? "VIP等级"
								+ br.getValue()
								: StringUtil.color("VIP等级" + br.getValue(),
										"red")).append("、");
				break;
			case BuildingRequirement.TYPE_DUPLICATE:// 根据副本解锁相应建筑
				ActInfoClient aic = Account.actInfoCache.getAct(br.getValue());
				buf.append(
						aic.isComplete() ? "通过副本" + aic.getPropAct().getName()
								: StringUtil.color("通过副本"
										+ aic.getPropAct().getName(), "red"))
						.append("、");
				break;
			case BuildingRequirement.TYPE_WORLD_LEVEL:// 世界等级
				buf.append("世界等级达到" + br.getValue() + "解锁").append("、");
				break;
			default:
				break;
			}
		}
		int index = buf.lastIndexOf("、");
		if (index >= 0)
			buf.deleteCharAt(index);
		return buf.toString();
	}

	@SuppressWarnings("unchecked")
	public static String checkRequirement(int id, boolean checkLevel) {
		List<BuildingRequirement> list = CacheMgr.buildingRequireMentCache
				.search(id);
		for (BuildingRequirement br : list) {
			switch (br.getType()) {
			case BuildingRequirement.TYPE_RESOURCES:
				if (br.getValue() == AttrType.ATTR_TYPE_EXPLOIT.getNumber()) {
					if (Account.user.getExploit() < br.getExtension())
						return "功勋" + br.getExtension();
				} else if (br.getValue() == AttrType.ATTR_TYPE_SCORE
						.getNumber()) {
					if (Account.user.getScore() < br.getExtension())
						return "成就" + br.getExtension();
				} else if (br.getValue() == AttrType.ATTR_TYPE_LEVEL
						.getNumber()) {
					if (checkLevel
							&& Account.user.getLevel() < br.getExtension())
						return "等级" + br.getExtension();
				}
				break;
			case BuildingRequirement.TYPE_BUILDING:
				BuildingProp prop = CacheMgr.getBuildingByID(br.getValue());
				if (null != prop) {
					BuildingInfoClient bic = Account.manorInfoClient
							.getBuilding(prop);
					if (null == bic
							|| bic.getProp().getLevel() < prop.getLevel())
						return prop.getBuildingName() + "Lv" + prop.getLevel();

				}
				break;
			case BuildingRequirement.TYPE_TOOLS:
				Item item = CacheMgr.getItemByID(br.getValue());
				if (null != item) {
					ItemBag itemBag = Account.store.getItemBag(item);
					if (null == itemBag
							|| itemBag.getCount() < br.getExtension())
						return item.getName() + br.getExtension() + "个";
				}
				break;
			case BuildingRequirement.TYPE_VIPS:// 根据vip等级解锁相应建筑
				if (br.getValue() > Account.getCurVip().getLevel()) {
					return "VIP等级" + br.getValue();
				}
				break;
			case BuildingRequirement.TYPE_DUPLICATE:// 根据副本解锁相应建筑
				ActInfoClient aic = Account.actInfoCache.getAct(br.getValue());
				if (checkLevel && !aic.isComplete()) {
					return "通过副本" + aic.getPropAct().getName();
				}
				break;
			case BuildingRequirement.TYPE_WORLD_LEVEL:// 世界等级
				if (WorldLevelInfoClient.worldLevel < br.getValue()) {
					return "世界等级达到" + br.getValue() + "解锁";
				}
				break;
			default:
				break;
			}
		}
		return "";
	}
}
