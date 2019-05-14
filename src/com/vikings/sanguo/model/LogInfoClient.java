package com.vikings.sanguo.model;

import android.util.Log;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.LogInfo;
import com.vikings.sanguo.protos.ReturnEffectInfo;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class LogInfoClient {
	// 技能类型
	public static final int LT_GENERAL = 1; // (保留)
	public static final int LT_FARM_SKILL = 2; // 使用技能(目标果园)
	public static final int LT_FARM_ITEM = 3; // 使用物品(目标果园)
	public static final int LT_POKE_SKILL = 4; // 使用技能poke日志(可以回poke)(目标为人)
	public static final int LT_POKE_ITEM = 5; // 使用道具poke日志(可以回poke)(目标为人)
	public static final int LT_CHAT = 6; // 聊天消息
	public static final int LT_FOOT = 7; // 足迹消息
	public static final int LT_FRIEND_ADD = 8; // 添加好友消息
	public static final int LT_FRIEND_DEL = 9; // 删除好友
	public static final int LT_ITEM_EXPIRE = 10; // 物品失效
	public static final int LT_TARGET_LEVELUP = 11; // 玩家升级日志
	public static final int LT_INVITER_REWORD = 12; // 邀请奖励（包括绑定和升级）
	public static final int LT_BIND_REWORD = 13; // 绑定奖励
	public static final int LT_BLACKLIST_ADD = 14; // 黑名单添加
	public static final int LT_BUILDING_ACTION = 15; // 建筑动作
	public static final int LT_PRESENT_REQUEST = 16; // 其他人赠送给我的日志
	public static final int LT_PRESENT_RESPONSE = 17; // 对方回应过来的赠送日志
	public static final int LT_GUILD = 18; // 家族日志 （stid 1:邀请 2：禅让 3：踢出家族 4:申请加入
											// 5:被批准加入家族 6：被拒绝加入家族 7：任命为长老
											// 8：贬为平民）

	private LogInfo logInfo;

	private Item item;

	private BriefUserInfoClient fromUser;

	private BuildingProp building;

	private int times;

	private BriefGuildInfoClient guildInfo;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public BuildingProp getBuilding() {
		return building;
	}

	public void setBuilding(BuildingProp building) {
		this.building = building;
	}

	/**
	 * 改变值
	 */

	public LogInfo getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(LogInfo logInfo) {
		this.logInfo = logInfo;
	}

	public BriefUserInfoClient getFromUser() {
		return fromUser;
	}

	public void setFromUser(BriefUserInfoClient fromUser) {
		this.fromUser = fromUser;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public BriefGuildInfoClient getGuildInfo() {
		return guildInfo;
	}

	public void setGuildInfo(BriefGuildInfoClient guildInfo) {
		this.guildInfo = guildInfo;
	}

	public boolean isGuildMsg() {
		if (LogInfoClient.LT_GUILD == logInfo.getType())
			return true;
		else
			return false;
	}

	public boolean isGuildInvite() {
		if (LogInfoClient.LT_GUILD == logInfo.getType()
				&& Constants.GUILD_INVITE == logInfo.getStid())
			return true;
		else
			return false;
	}

	public boolean isMakeOver() {
		if (LogInfoClient.LT_GUILD == logInfo.getType()
				&& Constants.GUILD_MAKE_OVER == logInfo.getStid())
			return true;
		else
			return false;
	}

	public boolean isKickOut() {
		if (LogInfoClient.LT_GUILD == logInfo.getType()
				&& Constants.GUILD_KICK_OUT == logInfo.getStid())
			return true;
		else
			return false;
	}

	public boolean isJoin() {
		if (LogInfoClient.LT_GUILD == logInfo.getType()
				&& Constants.GUILD_JOIN == logInfo.getStid())
			return true;
		else
			return false;
	}

	public boolean isJoinAgree() {
		if (LogInfoClient.LT_GUILD == logInfo.getType()
				&& Constants.GUILD_JOIN_AGREE == logInfo.getStid())
			return true;
		else
			return false;
	}

	public boolean isJoinRefuse() {
		if (LogInfoClient.LT_GUILD == logInfo.getType()
				&& Constants.GUILD_JOIN_REFUSE == logInfo.getStid())
			return true;
		else
			return false;
	}

	public boolean isSetElder() {
		if (LogInfoClient.LT_GUILD == logInfo.getType()
				&& Constants.GUILD_SET_ELDER == logInfo.getStid())
			return true;
		else
			return false;
	}

	public boolean isRemoveElder() {
		if (LogInfoClient.LT_GUILD == logInfo.getType()
				&& Constants.GUILD_REMOVE_ELDER == logInfo.getStid())
			return true;
		else
			return false;
	}

	public String toDetail() {
		StringBuilder buf = new StringBuilder();
		if (logInfo.getType() == LT_FRIEND_ADD) {
			buf.append(StringUtil.color(fromUser.getHtmlNickName(), "red"));
			if (Account.isFriend(fromUser)) {
				buf.append("添加我为好友，快去看看吧！");
			} else {
				buf.append("添加我为好友，快添加TA吧！");
			}
		} else if (this.logInfo.getType() == LT_FRIEND_DEL) {
			buf.append(StringUtil.color(fromUser.getHtmlNickName(), "red"));
			buf.append("将我从好友列表中删除！");
		} else if (this.logInfo.getType() == LT_INVITER_REWORD) {
			buf.append("我邀请")
					.append(StringUtil.color(fromUser.getHtmlNickName(), "red"))
					.append("成功,获得奖励").append(getItemName());
		} else if (this.logInfo.getType() == LT_BIND_REWORD) {
			buf.append("绑定奖励已发送到你的仓库道具栏中").append(getItemName());
		} else if (this.logInfo.getType() == LT_TARGET_LEVELUP) {
			buf.append(StringUtil.color(fromUser.getHtmlNickName(), "red"));
			buf.append("升级成功啦，TA当前级别是").append(fromUser.getLevel());
		} else if (this.logInfo.getType() == LT_BLACKLIST_ADD) {
			buf.append(StringUtil.color(fromUser.getHtmlNickName(), "red"));
			buf.append("将我拉入仇人录");
		} else if (this.logInfo.getType() == LT_ITEM_EXPIRE) {
			buf.append(getItemExpire());
		}
		for (ReturnEffectInfo ri : logInfo.getReisList()) {
			byte effectId = (byte) ri.getField().intValue();
			long value = ri.getValue();
			String tmp = toDesc(fromUser.getNickName(), effectId, value);
			if (!StringUtil.isNull(tmp))
				buf.append("<br/>").append(tmp);
		}
		return buf.toString();
	}

	public String toDesc() {
		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(logInfo.getTime())).append("前，");
		if (this.logInfo.getType() == LT_FRIEND_ADD) {
			buf.append(StringUtil.nickNameColor(fromUser));
			buf.append("添加我为好友，快添加TA吧！");
		} else if (this.logInfo.getType() == LT_FRIEND_DEL) {
			buf.append(StringUtil.nickNameColor(fromUser));
			buf.append("将我从好友列表中删除！");
		} else if (this.logInfo.getType() == LT_INVITER_REWORD) {
			buf.append("我邀请").append(StringUtil.nickNameColor(fromUser))
					.append("成功,获得奖励")
					.append(StringUtil.color(getItemName(), "red"));
		} else if (this.logInfo.getType() == LT_BIND_REWORD) {
			buf.append("绑定奖励已发送到你的仓库道具栏中").append(
					StringUtil.color(getItemName(), "red"));
		} else if (this.logInfo.getType() == LT_TARGET_LEVELUP) {
			buf.append(StringUtil.nickNameColor(fromUser));
			buf.append("升级成功啦，TA当前级别是").append(fromUser.getLevel());
		} else if (this.logInfo.getType() == LT_BLACKLIST_ADD) {
			buf.append(StringUtil.nickNameColor(fromUser));
			buf.append("将我拉入仇人录");
		} else if (this.logInfo.getType() == LT_FOOT) {

		} else if (this.logInfo.getType() == LT_ITEM_EXPIRE) {
			buf.append("你的物品" + StringUtil.color(getItemName(), "red") + "已经过期");
		} else {
			if (logInfo.getPassive() == Account.user.getId()) {
				if (Account.isFriend(fromUser)) {
					buf.append("好友");
				} else {
					buf.append("陌生人");
				}
			}
			buf.append(StringUtil.nickNameColor(fromUser));
			if ((logInfo.getType() == LT_POKE_ITEM) && item != null) {
				buf.append("对我使用").append(
						StringUtil.color(item.getName(), "#E86E01"));
			}
		}
		return buf.toString();
	}

	public String getInvitedLogDesc() {

		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(logInfo.getTime())).append("前，");

		buf.append(StringUtil.nickNameColor(fromUser)).append(" 邀请你加入");
		if (null != guildInfo) {
			buf.append(guildInfo.getName()).append("家族");
		} else {
			buf.append("他的家族");
		}
		return buf.toString();
	}

	public String getKickOutLogDesc() {

		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(logInfo.getTime())).append("前，");

		String guildInfoStr = "";
		if (null != guildInfo) {

			guildInfoStr = StringUtil.color(guildInfo.getName(),
					R.color.k7_color8) + "家族";
		}
		buf.append(guildInfoStr).append("族长")
				.append(StringUtil.nickNameColor(fromUser))
				.append("将你踢出了家族，你已成为自由之身");
		return buf.toString();
	}

	public String getMakeOverLogDesc() {
		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(logInfo.getTime())).append("前，")
				.append("原");

		String guildInfoStr = "";
		if (null != guildInfo) {

			guildInfoStr = StringUtil.color(guildInfo.getName(),
					R.color.k7_color8) + "家族";
		}
		buf.append(guildInfoStr).append("族长")
				.append(StringUtil.nickNameColor(fromUser))
				.append("将族长之位转让给了你。你成为了");
		buf.append(guildInfoStr).append("家族新一任族长!");
		return buf.toString();
	}

	public String getJoinDesc() {
		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(logInfo.getTime())).append("前，")
				.append(StringUtil.nickNameColor(fromUser)).append("申请加入家族");
		return buf.toString();
	}

	public String getJoinAgreeDesc() {
		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(logInfo.getTime())).append("前，你已经被")
				.append(StringUtil.nickNameColor(fromUser)).append("批准加入");
		if (null != guildInfo) {
			buf.append(StringUtil.color(guildInfo.getName(), R.color.k7_color8));
		}
		buf.append("家族");
		return buf.toString();
	}

	public String getJoinRefuseDesc() {
		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(logInfo.getTime())).append("前，")
				.append(StringUtil.nickNameColor(fromUser)).append("拒绝了你加入");
		if (null != guildInfo) {
			buf.append(StringUtil.color(guildInfo.getName(), R.color.k7_color8));
		}
		buf.append("家族的申请");
		return buf.toString();
	}

	public String getSetElderDesc() {
		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(logInfo.getTime())).append("前，")
				.append(StringUtil.nickNameColor(fromUser)).append("提升你为");
		if (null != guildInfo) {
			buf.append(StringUtil.color(guildInfo.getName(), R.color.k7_color8));
		}
		buf.append("家族的长老");
		return buf.toString();
	}

	public String getRemoveElderDesc() {
		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.formatBefore(logInfo.getTime())).append("前，")
				.append(StringUtil.nickNameColor(fromUser)).append("将你贬为平民");
		return buf.toString();
	}

	/**
	 * 替换技能成功描述
	 * 
	 * @param desc
	 */
	public String setDesc(String desc, byte effectId, long value) {
		if (StringUtil.isNull(desc))
			return desc;
		switch (effectId) {
		case 1:
			return desc.replaceAll("<money>", StringUtil.abs(value));
		case 2:
			return desc.replaceAll("<exp>", StringUtil.abs(value));
		case 3:
			return desc.replaceAll("<regards>", StringUtil.abs(value));
		case 4:
			return desc.replaceAll("<money>", StringUtil.abs(value));
		case 5:
			return desc.replaceAll("<exp>", StringUtil.abs(value));
		case 6:
			return desc.replaceAll("<regards>", StringUtil.abs(value));
		case 7:
		case 8:
			return setItemDesc(desc, value);
		case 10:
		case 26:
			return setFarmDesc(desc, value);
		case 20:
			return setHouseDesc(desc, value);
		default:
			return desc;
		}
	}

	private String itemDesc(String name, long value) {
		if (name == null)
			name = "";
		int[] v = getIntValue(value);
		int itemId = v[0];
		int count = v[1];
		if (count == 0)
			return "";
		Item item;
		try {
			item = (Item) CacheMgr.itemCache.get(itemId);
		} catch (GameException e) {
			return "";
		}
		if (count > 0)
			return name + "获得物品#" + item.getImage() + "#X" + count;
		else
			return name + "消耗物品#" + item.getImage() + "#X" + count;
	}

	private String setItemDesc(String desc, long value) {
		if (StringUtil.isNull(desc))
			return desc;
		int[] v = getIntValue(value);
		int count = Math.abs(v[0]);
		int itemId = v[1];
		Item item;
		try {
			item = (Item) CacheMgr.itemCache.get(itemId);
		} catch (GameException e) {
			return desc;
		}
		desc = desc.replaceAll("<number>", String.valueOf(count));
		desc = desc.replaceAll("<item>",
				StringUtil.color(item.getName(), "red"));
		return desc;
	}

	/**
	 * 果园被破坏或复活，果园通过种子id取得
	 * 
	 * @param desc
	 * @return
	 */
	private String setFarmDesc(String desc, long value) {
		if (StringUtil.isNull(desc))
			return desc;
		int[] v = getIntValue(value);
		int itemId = v[0];
		Item item;
		try {
			item = (Item) CacheMgr.itemCache.get(itemId);
		} catch (GameException e) {
			return desc;
		}
		desc = desc.replaceAll("<item>",
				StringUtil.color(item.getName().replace("种子", "园"), "red"));
		return desc;
	}

	private String setHouseDesc(String desc, long value) {
		if (StringUtil.isNull(desc))
			return desc;
		try {
			BuildingProp b = (BuildingProp) CacheMgr.buildingPropCache
					.get(value);
			desc = desc.replaceAll("<house>", b.getBuildingName());
		} catch (GameException e) {
			Log.e("ReturnInfo", e.getMessage(), e);
			return desc;
		}
		return desc;
	}

	private int[] getIntValue(long value) {
		byte[] buf = new byte[8];
		BytesUtil.putLong(buf, value, 0);
		int[] rs = new int[2];
		rs[0] = BytesUtil.getInt(buf, 0);
		rs[1] = BytesUtil.getInt(buf, 4);
		return rs;
	}

	/**
	 * 效果数值描述
	 * 
	 * @param fromUser
	 * @return
	 */
	public String toDesc(String fromUser, byte effectId, long value) {
		switch (effectId) {
		case 4:
			return "金钱#money#" + StringUtil.value(value);
		case 5:
			return "经验" + StringUtil.value(value);
		case 6:
			return "关注#regards#" + StringUtil.value(value);
		case 7:
		case 8:
			return itemDesc(fromUser, value);
		case 17:
			return "元宝#rmb#" + StringUtil.value(value);
		default:
			return "";
		}
	}

	/**
	 * 自己获得物品，用于绑定和邀请奖励(奖励只会有一个物品)
	 * 
	 * @return
	 */
	private String getItemName() {
		StringBuilder buf = new StringBuilder();
		for (ReturnEffectInfo ri : logInfo.getReisList()) {
			if (ri.getField() == 8) {
				buf.append(getChgItemName(ri.getValue()));
			}
		}
		return buf.toString();
	}

	public String getChgItemName(long value) {
		int[] v = getIntValue(value);
		int itemId = v[1];
		int count = v[0];
		if (count == 0)
			return "";
		Item item;
		try {
			item = (Item) CacheMgr.itemCache.get(itemId);
		} catch (GameException e) {
			return "";
		}
		return item.getName();
	}

	private String getItemExpire() {
		StringBuilder buf = new StringBuilder("你的物品 ");
		for (ReturnEffectInfo ri : logInfo.getReisList()) {
			if (ri.getField() == 7) {
				int[] v = getIntValue(ri.getValue());
				int itemId = v[1];
				int count = v[0];
				if (count != 0) {
					Item item = CacheMgr.getItemByID(itemId);
					if (null != item) {
						buf.append("#"
								+ item.getImage()
								+ "#  "
								+ StringUtil.color(item.getName() + "("
										+ StringUtil.abs(count) + "个)", "red")
								+ " 过期了");
					}
					break;
				}

			}
		}
		return buf.toString();
	}
}
