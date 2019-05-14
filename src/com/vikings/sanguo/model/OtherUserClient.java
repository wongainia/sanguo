package com.vikings.sanguo.model;

import java.util.List;

import android.util.Log;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BriefUserInfo;
import com.vikings.sanguo.protos.ManorInfo;
import com.vikings.sanguo.protos.OtherLordInfo;
import com.vikings.sanguo.protos.OtherUserInfo;
import com.vikings.sanguo.protos.ROLE_STATUS;
import com.vikings.sanguo.protos.ReturnAttrInfo;
import com.vikings.sanguo.protos.RoleAttrInfo;
import com.vikings.sanguo.protos.RoleStatusInfo;
import com.vikings.sanguo.protos.TroopEffectInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.utils.VipUtil;

/**
 * other的rich info
 * 
 * @author chenqing
 * 
 */
public class OtherUserClient extends AttrData {

	private OtherUserInfo otherUserInfo;

	private OtherLordInfoClient lord;

	private ManorInfoClient manor;

	private UserTroopEffectInfo troopEffectInfo;

	private int expTotal;

	private List<ArmInfoClient> armInfos;

	private List<OtherHeroInfoClient> ohics;

	// 客户端填数据
	private BriefGuildInfoClient bgic;

	public OtherUserClient(int id) {
		otherUserInfo = new OtherUserInfo();
		setId(id);
	}

	private OtherUserClient() {
	}

	public OtherLordInfoClient getLord() {
		return lord;
	}

	public ManorInfoClient getManor() {
		return manor;
	}

	public OtherUserInfo getOtherUserInfo() {
		return otherUserInfo;
	}

	// 是否在神圣护佑状态
	public boolean isVipBlessState() {
		if (otherUserInfo == null)
			return false;

		for (RoleStatusInfo info : otherUserInfo.getStatusInfosList()) {
			if (info.getId() == ROLE_STATUS.ROLE_STATUS_VIP_BLESS.getNumber()) {
				if (info.getTime() > Config.serverTimeSS()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected List<RoleAttrInfo> getRoleAttrInfos() {
		return otherUserInfo.getInfosList();
	}

	@Override
	protected List<ReturnAttrInfo> getReturnAttrInfos() {
		return null;
	}

	public Integer getId() {
		return otherUserInfo.getId();
	}

	public OtherUserInfo setId(Integer id) {
		return otherUserInfo.setId(id);
	}

	public Integer getImage() {
		return otherUserInfo.getImage();
	}

	public OtherUserInfo setImage(Integer image) {
		return otherUserInfo.setImage(image);
	}

	public Integer getSex() {
		return otherUserInfo.getSex();
	}

	public OtherUserInfo setSex(Integer sex) {
		return otherUserInfo.setSex(sex);
	}

	public Integer getBirthday() {
		return otherUserInfo.getBirthday();
	}

	public OtherUserInfo setBirthday(Integer birthday) {
		return otherUserInfo.setBirthday(birthday);
	}

	public Integer getProvince() {
		return otherUserInfo.getProvince();
	}

	public OtherUserInfo setProvince(Integer province) {
		return otherUserInfo.setProvince(province);
	}

	public Integer getCity() {
		return otherUserInfo.getCity();
	}

	public OtherUserInfo setCity(Integer city) {
		return otherUserInfo.setCity(city);
	}

	public Integer getHomeProvince() {
		return otherUserInfo.getHomeProvince();
	}

	public OtherUserInfo setHomeProvince(Integer homeProvince) {
		return otherUserInfo.setHomeProvince(homeProvince);
	}

	public Integer getHomeCity() {
		return otherUserInfo.getHomeCity();
	}

	public OtherUserInfo setHomeCity(Integer homeCity) {
		return otherUserInfo.setHomeCity(homeCity);
	}

	public String getNick() {
		return otherUserInfo.getNick();
	}

	public OtherUserInfo setNick(String nick) {
		return otherUserInfo.setNick(nick);
	}

	public Integer getMarital() {
		return otherUserInfo.getMarital();
	}

	public OtherUserInfo setMarital(Integer marital) {
		return otherUserInfo.setMarital(marital);
	}

	public Integer getStyle() {
		return otherUserInfo.getStyle();
	}

	public OtherUserInfo setStyle(Integer style) {
		return otherUserInfo.setStyle(style);
	}

	public Integer getBlood() {
		return otherUserInfo.getBlood();
	}

	public OtherUserInfo setBlood(Integer blood) {
		return otherUserInfo.setBlood(blood);
	}

	public String getDesc() {
		return otherUserInfo.getDesc();
	}

	public OtherUserInfo setDesc(String desc) {
		return otherUserInfo.setDesc(desc);
	}

	public Integer getGuildid() {
		return otherUserInfo.getGuildid();
	}

	public OtherUserInfo setGuildid(Integer guildid) {
		return otherUserInfo.setGuildid(guildid);
	}

	public Integer getLastLoginTime() {
		return otherUserInfo.getLastLoginTime();
	}

	public OtherUserInfo setLastLoginTime(Integer lastLoginTime) {
		return otherUserInfo.setLastLoginTime(lastLoginTime);
	}

	public int getCountry() {
		return otherUserInfo.getCountry();
	}

	public int getExpTotal() {
		return expTotal;
	}

	public void setExpTotal(int expTotal) {
		this.expTotal = expTotal;
	}

	public List<ArmInfoClient> getArmInfos() {
		return armInfos;
	}

	public void setArmInfos(List<ArmInfoClient> armInfos) {
		this.armInfos = armInfos;
	}

	public List<OtherHeroInfoClient> getOhics() {
		return ohics;
	}

	public void setOhics(List<OtherHeroInfoClient> ohics) {
		this.ohics = ohics;
	}

	public void setTroopEffectInfo(UserTroopEffectInfo troopEffectInfo) {
		this.troopEffectInfo = troopEffectInfo;
	}

	public UserTroopEffectInfo getTroopEffectInfo() {
		if (null != troopEffectInfo) {
			if (null != manor) {
				int value = manor.getArmHpAdd();
				if (value > 0) {
					boolean has = false;// 是否已经添加了血量加成
					for (TroopEffectInfo info : troopEffectInfo.getInfosList()) {
						if (info.getAttr().intValue() == BattlePropDefine.PROP_LIFE) {
							has = true;
							break;
						}
					}

					if (!has) {
						List<TroopEffectInfo> infos = troopEffectInfo
								.getInfosList();
						List<TroopProp> props = CacheMgr.troopPropCache
								.getHpAddTroops();
						for (TroopProp prop : props) {
							TroopEffectInfo info = new TroopEffectInfo();
							info.setArmid(prop.getId())
									.setAttr(BattlePropDefine.PROP_LIFE)
									.setValue(value * prop.getHpModulus());
							infos.add(info);
						}
						troopEffectInfo.setInfosList(infos);
					}
				}

			}
		}
		return troopEffectInfo;
	}

	public BriefGuildInfoClient getBgic() {
		return bgic;
	}

	public void setBgic(BriefGuildInfoClient bgic) {
		this.bgic = bgic;
	}

	public BriefUserInfoClient bref() {
		if (BriefUserInfoClient.isNPC(getId())) {
			try {
				return CacheMgr.userCache.get(getId());
			} catch (GameException e) {
				return null;
			}
		} else
			return new BriefUserInfoClient(new BriefUserInfo()
					.setBirthday(getBirthday()).setCity(getCity())
					.setId(getId()).setImage(getImage()).setLevel(getLevel())
					.setNick(getNick()).setProvince(getProvince())
					.setSex(getSex()).setGuildid(getGuildid())
					.setCountry(getCountry()).setCharge(getCharge())
					.setLastLoginTime(getLastLoginTime()));
	}

	public static OtherUserClient convert(OtherUserInfo oi, OtherLordInfo lord,
			ManorInfo manor, UserTroopEffectInfo userTroopEffectInfo)
			throws GameException {
		OtherUserClient o = new OtherUserClient();
		if (oi == null)
			o.otherUserInfo = new OtherUserInfo();
		else {
			o.otherUserInfo = oi;
			CacheMgr.userCache.updateCache(o.bref());
		}
		o.lord = OtherLordInfoClient.conver(lord);
		o.manor = ManorInfoClient.convert(manor);
		if (null != userTroopEffectInfo)
			o.troopEffectInfo = userTroopEffectInfo;
		o.checkLevel();
		return o;
	}

	// 根据用户等级 补充用户数据
	private void checkLevel() {
		try {
			if (getLevel() < 1)
				return;

			PropUser l = (PropUser) CacheMgr.propUserCache.get(getLevel());
			setExpTotal(l.getExpTotal());
		} catch (GameException e) {
			Log.e("LevelCache", e.getMessage());
		}
	}

	public int getHeroLimit() {
		int heroLimit = 0;
		if (getLevel() < 1)
			return heroLimit;

		try {
			PropUser level = (PropUser) CacheMgr.propUserCache.get(getLevel());
			heroLimit = level.getHeroLimit();
		} catch (GameException e) {
			Log.e("LevelCache", e.getMessage());
		}
		return heroLimit;
	}

	// 是否解锁副将1
	public boolean unlockExtHero1() {
		return getLevel() >= CacheMgr.dictCache.getDictInt(
				Dict.TYPE_UNLOCK_HERO, 3)
				|| getCurVip().getLevel() >= VipUtil.openSecondHero();
	}

	// 是否解锁副将2
	public boolean unlockExtHero2() {
		return getLevel() >= CacheMgr.dictCache.getDictInt(
				Dict.TYPE_UNLOCK_HERO, 4)
				|| getCurVip().getLevel() >= VipUtil.openThirdHero();
	}

	public boolean hasGuild() {
		if (null == otherUserInfo)
			return false;
		return otherUserInfo.getGuildid() > 0;
	}

	private RoleStatusInfo getInfo(ROLE_STATUS status) {
		for (RoleStatusInfo info : otherUserInfo.getStatusInfosList()) {
			if (info.getId() == status.getNumber()) {
				if (info.getTime() > Config.serverTimeSS())
					return info;
			}
		}
		return null;
	}

	private RoleStatusInfo getWeakInfo() {
		return getInfo(ROLE_STATUS.ROLE_STATUS_WEAK);
	}

	private RoleStatusInfo getDueProtectedInfo() {
		return getInfo(ROLE_STATUS.ROLE_STATUS_1VS1_PROTECT);
	}

	private RoleStatusInfo getWantedInfo() {
		return getInfo(ROLE_STATUS.ROLE_STATUS_WANTED);
	}

	// 虚弱状态结束时间，返回0表示不在虚弱状态
	public int getWeakLeftTime() {
		int time = 0;
		RoleStatusInfo info = getWeakInfo();
		if (null != info)
			time = info.getTime() - Config.serverTimeSS();
		if (time < 0)
			time = 0;
		return time;
	}

	// 返回屠城者id， 返回0表示不在虚弱状态；
	public int getButcherId() {
		int id = -1;
		RoleStatusInfo info = getWeakInfo();
		if (null != info)
			id = info.getValue();
		return id;
	}

	public boolean isWeak() {
		return getWeakInfo() != null;
	}

	public void removeWeak() {
		RoleStatusInfo info = getWeakInfo();
		if (null != info) {
			info.setTime(0);
		}
	}

	public boolean isDuelProtected() {
		return getDueProtectedInfo() != null;
	}

	public boolean isWanted() {
		return getWantedInfo() != null;
	}
}
