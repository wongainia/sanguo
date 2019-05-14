package com.vikings.sanguo.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.AccountInfoPart1;
import com.vikings.sanguo.protos.AccountInfoPart3;
import com.vikings.sanguo.protos.BriefUserInfo;
import com.vikings.sanguo.protos.ROLE_STATUS;
import com.vikings.sanguo.protos.ReturnAttrInfo;
import com.vikings.sanguo.protos.ReturnInfo;
import com.vikings.sanguo.protos.RichUserInfo;
import com.vikings.sanguo.protos.RoleAttrInfo;
import com.vikings.sanguo.protos.RoleChargeInfo;
import com.vikings.sanguo.protos.RoleDayInfo;
import com.vikings.sanguo.protos.RoleInfo;
import com.vikings.sanguo.protos.RoleInfoPart1;
import com.vikings.sanguo.protos.RoleInfoPart3;
import com.vikings.sanguo.protos.RoleStatusInfo;
import com.vikings.sanguo.ui.alert.StrongerWindow;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.VipUtil;

/**
 * 
 * 用户自己账户信息 UserAccountClient
 * 
 * @author Brad.Chen
 * 
 */
public class UserAccountClient extends AttrData {

	private int id;

	private AccountInfoPart1 accountInfoPart1;

	private AccountInfoPart3 accountInfoPart3;

	private RoleInfoPart1 roleInfoPart1;

	private RoleInfoPart3 roleInfoPart3;

	private int expTotal;

	// 荣耀榜是否有可领取的奖励
	public static boolean[] hasHonorRankReward = { false, false, false, false };

	/**
	 * 用于 登录前的 取用户操作
	 * 
	 * @param id
	 * @param pwd
	 */
	public UserAccountClient(int id, String pwd) {
		accountInfoPart1 = new AccountInfoPart1();
		accountInfoPart3 = new AccountInfoPart3();
		roleInfoPart1 = new RoleInfoPart1();
		roleInfoPart3 = new RoleInfoPart3();
		this.id = id;
		this.setPsw(pwd);
	}

	public void setAccountInfoPart1(AccountInfoPart1 accountInfoPart1) {
		this.accountInfoPart1 = accountInfoPart1;
	}

	public void setAccountInfoPart3(AccountInfoPart3 accountInfoPart3) {
		this.accountInfoPart3 = accountInfoPart3;
	}

	private UserAccountClient() {
	}

	@Override
	protected List<RoleAttrInfo> getRoleAttrInfos() {
		if (roleInfoPart3 != null)
			return roleInfoPart3.getInfosList();
		else
			return null;
	}

	@Override
	protected List<ReturnAttrInfo> getReturnAttrInfos() {
		return null;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSim() {
		return accountInfoPart1.getSim();
	}

	public AccountInfoPart1 setSim(String sim) {
		return accountInfoPart1.setSim(sim);
	}

	public String getPsw() {
		return accountInfoPart1.getPsw();
	}

	public AccountInfoPart1 setPsw(String psw) {
		return accountInfoPart1.setPsw(psw);
	}

	public Integer getImage() {
		return accountInfoPart3.getImage();
	}

	public AccountInfoPart3 setImage(Integer image) {
		return accountInfoPart3.setImage(image);
	}

	public String getNick() {
		return accountInfoPart3.getNick();
	}

	public String getHtmlNick() {
		return TextUtils.htmlEncode(accountInfoPart3.getNick());
	}

	public AccountInfoPart3 setNick(String nick) {
		return accountInfoPart3.setNick(nick);
	}

	public Integer getSex() {
		return accountInfoPart3.getSex();
	}

	public AccountInfoPart3 setSex(Integer sex) {
		return accountInfoPart3.setSex(sex);
	}

	public Integer getBirthday() {
		return accountInfoPart3.getBirthday();
	}

	public AccountInfoPart3 setBirthday(Integer birthday) {
		return accountInfoPart3.setBirthday(birthday);
	}

	public Integer getProvince() {
		return accountInfoPart3.getProvince();
	}

	public AccountInfoPart3 setProvince(Integer province) {
		return accountInfoPart3.setProvince(province);
	}

	public Integer getCity() {
		return accountInfoPart3.getCity();
	}

	public AccountInfoPart3 setCity(Integer city) {
		return accountInfoPart3.setCity(city);
	}

	public String getMobile() {
		return accountInfoPart3.getMobile();
	}

	public AccountInfoPart3 setMobile(String mobile) {
		return accountInfoPart3.setMobile(mobile);
	}

	public String getEmail() {
		return accountInfoPart3.getEmail();
	}

	public AccountInfoPart3 setEmail(String email) {
		return accountInfoPart3.setEmail(email);
	}

	public String getDesc() {
		return accountInfoPart3.getDesc();
	}

	public AccountInfoPart3 setDesc(String desc) {
		return accountInfoPart3.setDesc(desc);
	}

	public String getPartnerId() {
		return accountInfoPart3.getPartnerId();
	}

	public AccountInfoPart3 setPartnerId(String partnerId) {
		return accountInfoPart3.setPartnerId(partnerId);
	}

	public Integer getPartnerChannel() {
		return accountInfoPart3.getPartnerChannel();
	}

	public AccountInfoPart3 setPartnerChannel(Integer partnerChannel) {
		return accountInfoPart3.setPartnerChannel(partnerChannel);
	}

	public String getRenrenSchool() {
		return accountInfoPart3.getRenrenSchool();
	}

	public AccountInfoPart3 setRenrenSchool(String renrenSchool) {
		return accountInfoPart3.setRenrenSchool(renrenSchool);
	}

	public String getIdCardNumber() {
		return accountInfoPart3.getIdCardNumber();
	}

	public AccountInfoPart3 setIdCardNumber(String idCardNumber) {
		return accountInfoPart3.setIdCardNumber(idCardNumber);
	}

	public Integer getLastLoginTime() {
		return roleInfoPart1.getLastLoginTime();
	}

	public RoleInfoPart1 setLastLoginTime(Integer lastLoginTime) {
		return roleInfoPart1.setLastLoginTime(lastLoginTime);
	}

	public String getExtend() {
		return roleInfoPart1.getExtend();
	}

	public RoleInfoPart1 setExtend(String extend) {
		return roleInfoPart1.setExtend(extend);
	}

	public Integer getCountry() {
		return roleInfoPart3.getCountry();
	}

	public RoleInfoPart3 setCountry(Integer country) {
		return roleInfoPart3.setCountry(country);
	}

	public long getTraining() {
		if (null != Account.readLog) {
			return Account.readLog.training
					| roleInfoPart3.getTraining().longValue();
		} else {
			return roleInfoPart3.getTraining().longValue();
		}

	}

	public RoleInfoPart3 setTraining(long training) {
		roleInfoPart3.setTraining(training);
		if (null != Account.readLog)
			Account.readLog.setTraining(training);
		return roleInfoPart3;
	}

	public RoleInfoPart3 setRegTime(Integer regTime) {
		return roleInfoPart3.setRegTime(regTime);
	}

	public String getSexName() {
		return getSex().byteValue() == Constants.MALE ? Config.getController()
				.getString(R.string.male) : Config.getController().getString(
				R.string.female);
	}

	public String getBirthdayStr() {
		try {
			return DateUtil.date1.format(new Date(getBirthday() * 1000L));
		} catch (Exception e) {
			return "1990/01/01";
		}
	}

	public int getAge() {
		return DateUtil.getAge(getBirthdayStr());
	}

	public String getYear() {
		if (getBirthday() > 0) {
			String day = getBirthdayStr();
			char i = day.charAt(2);
			return StringUtil.repParams(
					Config.getController().getString(R.string.User_getYear), i
							+ "0");
		}
		return Config.getController().getString(R.string.unkown);
	}

	public boolean isCustomIcon() {
		return getImage() > 1000;
	}

	public boolean isNew() {
		return !StringUtil.isFlagOn(getTraining(), 0);
	}

	public boolean isNewUser() {
		return getExp() == 0 && Account.user.getLevel() <= 3;
	}

	public int getId() {
		return id;
	}

	public String getSaveID() {
		return "_" + Config.serverId + "_" + id;
	}

	public int getExpTotal() {
		return expTotal;
	}

	public void setExpTotal(int expTotal) {
		this.expTotal = expTotal;
	}

	public String getConstellation() {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(getBirthday() * 1000L);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		if (month == 1 && day >= 20 || month == 2 && day <= 18) {
			return Config.getController().getString(R.string.Aquarius);
		}
		if (month == 2 && day >= 19 || month == 3 && day <= 20) {
			return Config.getController().getString(R.string.Pisces);
		}
		if (month == 3 && day >= 21 || month == 4 && day <= 19) {
			return Config.getController().getString(R.string.Aries);
		}
		if (month == 4 && day >= 20 || month == 5 && day <= 20) {
			return Config.getController().getString(R.string.Taurus);
		}
		if (month == 5 && day >= 21 || month == 6 && day <= 21) {
			return Config.getController().getString(R.string.Gemini);
		}
		if (month == 6 && day >= 22 || month == 7 && day <= 22) {
			return Config.getController().getString(R.string.Cancer);
		}
		if (month == 7 && day >= 23 || month == 8 && day <= 22) {
			return Config.getController().getString(R.string.Leo);
		}
		if (month == 8 && day >= 23 || month == 9 && day <= 22) {
			return Config.getController().getString(R.string.Virgo);
		}
		if (month == 9 && day >= 23 || month == 10 && day <= 22) {
			return Config.getController().getString(R.string.Libra);
		}
		if (month == 10 && day >= 23 || month == 11 && day <= 21) {
			return Config.getController().getString(R.string.Scorpio);
		}
		if (month == 11 && day >= 22 || month == 12 && day <= 21) {
			return Config.getController().getString(R.string.Sagittarius);
		}
		if (month == 12 && day >= 22 || month == 1 && day <= 19) {
			return Config.getController().getString(R.string.Capricorn);
		}
		return "";
	}

	public String getZodiac() {
		String zodiac = "";
		int start = 1901;
		int x = (start - Integer.parseInt(getBirthdayStr().substring(0, 4))) % 12;
		if (x == 1 || x == -11) {
			zodiac = Config.getController().getString(R.string.mouse);
		}
		if (x == 0) {
			zodiac = Config.getController().getString(R.string.cow);
		}
		if (x == 11 || x == -1) {
			zodiac = Config.getController().getString(R.string.tiger);
		}
		if (x == 10 || x == -2) {
			zodiac = Config.getController().getString(R.string.rabbit);
		}
		if (x == 9 || x == -3) {
			zodiac = Config.getController().getString(R.string.dragon);
		}
		if (x == 8 || x == -4) {
			zodiac = Config.getController().getString(R.string.snake);
		}
		if (x == 7 || x == -5) {
			zodiac = Config.getController().getString(R.string.horse);
		}
		if (x == 6 || x == -6) {
			zodiac = Config.getController().getString(R.string.sheep);
		}
		if (x == 5 || x == -7) {
			zodiac = Config.getController().getString(R.string.monkey);
		}
		if (x == 4 || x == -8) {
			zodiac = Config.getController().getString(R.string.chicken);
		}
		if (x == 3 || x == -9) {
			zodiac = Config.getController().getString(R.string.dog);
		}
		if (x == 2 || x == -10) {
			zodiac = Config.getController().getString(R.string.pig);
		}
		return zodiac;
	}

	// 根据用户等级 补充用户数据
	private void checkLevel() {
		if (getLevel() < 1)
			return;

		try {
			PropUser l = (PropUser) CacheMgr.propUserCache.get(getLevel());
			setExpTotal(l.getExpTotal());
		} catch (GameException e) {
			Log.e("LevelCache", e.getMessage());
		}
	}

	public boolean isEmpty() {
		return id == 0;
	}

	public Integer getLastCheckinTime() {
		return roleInfoPart3.getLastCheckinTime();
	}

	public void setLastCheckinTime() {
		roleInfoPart3.setLastCheckinTime((int) (Config.serverTime() / 1000));
	}

	public Integer getCheckinCount() {
		return roleInfoPart3.getCheckinCount();
	}

	/**
	 * 签到后更新数据
	 */
	public void checked() {
		roleInfoPart3.setLastCheckinTime((int) (Config.serverTime() / 1000));
		roleInfoPart3.setCheckinCount(getCheckinCount() + 1);
	}

	/**
	 * 从服务器数据生成数据
	 * 
	 * @param info
	 * @return
	 */
	public static UserAccountClient covert(RichUserInfo info) {
		UserAccountClient user = new UserAccountClient();
		user.id = info.getUserid();
		if (info.hasAccountInfo() && info.getAccountInfo().hasInfo()
				&& info.getAccountInfo().getInfo().hasPart1()) {
			user.accountInfoPart1 = info.getAccountInfo().getInfo().getPart1();
		}
		if (info.hasAccountInfo() && info.getAccountInfo().hasInfo()
				&& info.getAccountInfo().getInfo().hasPart3()) {
			user.accountInfoPart3 = info.getAccountInfo().getInfo().getPart3();
		}
		if (info.hasRoleInfo() && info.getRoleInfo().hasInfo()
				&& info.getRoleInfo().getInfo().hasPart1()) {
			user.roleInfoPart1 = info.getRoleInfo().getInfo().getPart1();
		}
		if (info.hasRoleInfo() && info.getRoleInfo().hasInfo()
				&& info.getRoleInfo().getInfo().hasPart3()) {
			user.roleInfoPart3 = info.getRoleInfo().getInfo().getPart3();
		}
		return user;
	}

	/**
	 * 数据同步更新
	 * 
	 * @param user
	 */
	public void update(UserAccountClient user) {
		if (user.id != id)
			return;
		if (user.accountInfoPart1 != null)
			accountInfoPart1 = user.accountInfoPart1;
		if (user.accountInfoPart3 != null)
			accountInfoPart3 = user.accountInfoPart3;
		if (user.roleInfoPart1 != null)
			roleInfoPart1 = user.roleInfoPart1;
		if (user.roleInfoPart3 != null)
			roleInfoPart3 = user.roleInfoPart3;
		checkLevel();
	}

	public void update(RoleInfo roleInfo) {
		if (null == roleInfo || roleInfo.getId() != id)
			return;
		if (roleInfo.getPart1() != null)
			roleInfoPart1 = roleInfo.getPart1();
		if (roleInfo.getPart3() != null)
			roleInfoPart3 = roleInfo.getPart3();
		checkLevel();
	}

	/**
	 * 更新操作回报 只更新attr
	 * 
	 * @param user
	 */
	public void update(ReturnInfo ri) {

		for (ReturnAttrInfo rai : ri.getRaisList()) {
			boolean found = false;
			for (RoleAttrInfo dest : roleInfoPart3.getInfosList()) {
				if (rai.getType().equals(dest.getId())) {
					dest.setValue(dest.getValue() + rai.getValue());
					found = true;
				}
			}
			if (!found) {
				roleInfoPart3.getInfosList().add(
						new RoleAttrInfo().setId(rai.getType()).setValue(
								rai.getValue()));
			}
		}
		checkLevel();
	}

	public BriefUserInfoClient bref() {
		return new BriefUserInfoClient(new BriefUserInfo()
				.setBirthday(getBirthday()).setCity(getCity()).setId(getId())
				.setImage(getImage()).setLevel(getLevel()).setNick(getNick())
				.setProvince(getProvince()).setSex(getSex())
				.setGuildid(Account.guildCache.getGuildid())
				.setCountry(getCountry()).setCharge(getCharge())
				.setLastLoginTime(getLastLoginTime()));
	}

	public int getGuildId() {
		return Account.guildCache.getGuildid();
	}

	public boolean hasGuild() {
		return Account.guildCache.hasGuide();
	}

	public boolean isValidUser() {
		return this.id > 0;
	}

	// 自己是否绑定
	public boolean isSelfBound() {
		if (!StringUtil.isNull(getMobile()) || !StringUtil.isNull(getEmail())
				|| !StringUtil.isNull(getIdCardNumber()))
			return true;
		return false;
	}

	public UserAccountClient emptyUser() {
		UserAccountClient uac = new UserAccountClient();
		uac.id = id;
		uac.accountInfoPart1 = new AccountInfoPart1();
		uac.accountInfoPart3 = new AccountInfoPart3();
		uac.roleInfoPart1 = new RoleInfoPart1();
		uac.roleInfoPart3 = new RoleInfoPart3();

		uac.setImage(getImage());
		uac.setBirthday(getBirthday());
		uac.setNick(getNick());
		uac.setImage(getImage());
		uac.setDesc(getDesc());
		uac.setSex(getSex());
		uac.setProvince(getProvince());
		uac.setCity(getCity());
		return uac;
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

	/**
	 * 目前资源总重量
	 * 
	 * @return
	 */
	public int getTotalWeight(int attrId) {
		List<RoleAttrInfo> ls = getRoleAttrInfos();
		if (ls == null)
			return 0;
		int count = 0;
		for (RoleAttrInfo a : ls) {
			if (a.getId() == attrId) {
				count += CacheMgr.weightCache.getWeight(a.getId())
						* a.getValue();
				break;
			}
		}
		return count;
	}

	public String checkWeight(int attrId) {
		int cur = getTotalWeight(attrId);
		int carry = getMaxStoreWeight();
		if (cur < carry)
			return null;
		else
			return "目前资源总重量(" + cur + "),仓库存储重量上限(" + carry + "),请升级仓库或先使用资源";
	}

	/**
	 * 上次全收时间
	 * 
	 * @return
	 */
	public int getLastRecvAllTime() {
		return roleInfoPart3.getLastReceiveTime();
	}

	public void setLastRecvAllTime(int time) {
		roleInfoPart3.setLastReceiveTime(time);
	}

	public int getStaminaResetTime() {
		return roleInfoPart3.getStaminaResetTime();
	}

	public boolean isRoleInfoPart3Valid() {
		return roleInfoPart3 != null;
	}

	public boolean hasCountry() {
		return getCountry().intValue() > 0;
	}

	public int getRegTime() {
		return roleInfoPart3.getRegTime();
	}

	public int getLastReceiveTime() {
		return roleInfoPart3.getLastReceiveTime();
	}

	private RoleStatusInfo getInfo(ROLE_STATUS status) {
		if (roleInfoPart3 != null)
			for (RoleStatusInfo info : roleInfoPart3.getStatusInfosList()) {
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

	public RoleStatusInfo getWantedInfo() {
		return getInfo(ROLE_STATUS.ROLE_STATUS_WANTED);
	}

	public RoleStatusInfo getVipBlessInfo() {
		return getInfo(ROLE_STATUS.ROLE_STATUS_VIP_BLESS);
	}

	// 神圣护佑激活状态结束剩余时间
	public int getVipBlessTime() {
		int time = 0;
		RoleStatusInfo info = getVipBlessInfo();
		if (null != info)
			time = info.getTime() - Config.serverTimeSS();
		if (time < 0)
			time = 0;
		return time;
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

	public boolean isDuelProtected() {
		return getDueProtectedInfo() != null;
	}

	public boolean isWanted() {
		return getWantedInfo() != null;
	}

	public void removeWeak() {
		RoleStatusInfo info = getWeakInfo();
		if (null != info) {
			info.setTime(0);
		}
	}

	public boolean isNextDayLogin() {
		if (getRegTime() > 0) {
			return DateUtil.isYesterday(getRegTime() * 1000L);
		}
		return false;
	}

	public int getDoubleChargeTimes() {
		if (null == roleInfoPart3 || null == roleInfoPart3.getChargeInfo())
			return 0;
		else
			return roleInfoPart3.getChargeInfo().getDoubleChargeTimes();
	}

	// 当前双倍积分
	public int getDoubleChargeValue() {
		if (null == roleInfoPart3 || null == roleInfoPart3.getChargeInfo())
			return 0;
		else
			return roleInfoPart3.getChargeInfo().getDoubleChargeValue();
	}

	// 包月充值剩余天数（包含今天）
	public int getMonthChargeRewardLeftDays() {
		if (null == roleInfoPart3 || null == roleInfoPart3.getChargeInfo())
			return 0;
		int startTime = roleInfoPart3.getChargeInfo().getMonthChargeStart();
		if (startTime == 0)
			return 0;
		int leftDays = CacheMgr.chargeCommonConfigCache.getMonthDays()
				- DateUtil.dayBetween(startTime, Config.serverTimeSS());
		if (leftDays < 0)
			leftDays = 0;
		return leftDays;
	}

	// 今日是否领取过奖励
	public boolean rewardToday() {
		if (null == roleInfoPart3 || null == roleInfoPart3.getChargeInfo())
			return false;
		int lastTime = roleInfoPart3.getChargeInfo().getMonthChargeLastTime();
		if (lastTime == 0)
			return false;
		else
			return DateUtil.isToday(lastTime * 1000L);
	}

	// 玩家行动力上限
	public int getMaxStamina() {
		return CacheMgr.userCommonCache.getMaxStamina();
	}

	// 玩家行动力恢复基础价格
	public int getBaseStatminaRecoverPrice() {
		return CacheMgr.userCommonCache.getBasePrice();
	}

	// 玩家行动力恢复阶梯价格
	public int getStaminaRecoverStepPrice() {
		return CacheMgr.userCommonCache.getAddPrice();
	}

	public int getStaminaRecoverPrice() {
		return getBaseStatminaRecoverPrice() + getUserStaminaResetCount()
				* getStaminaRecoverStepPrice();
	}

	public void setStaminaRecoverTimes(int times) {
		if (null == roleInfoPart3 || null == roleInfoPart3.getDayInfo())
			return;
		roleInfoPart3.getDayInfo().setStaminaReset(times);
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

	// 是否存在解锁的副将
	public boolean isUnlockExtHero() {
		return unlockExtHero1() || unlockExtHero2();
	}

	private boolean isToday() {
		return DateUtil
				.isToday((roleInfoPart3.getDayInfo().getDay() - 1) * 1000L);
	}

	// 副本援助次数
	public int getDugeonHelp() {
		if (null == roleInfoPart3 || null == roleInfoPart3.getDayInfo()
				|| !isToday())
			return 0;
		else
			return roleInfoPart3.getDayInfo().getDungeonHelp();
	}

	// 行动力重置次数
	public int getUserStaminaResetCount() {
		if (null == roleInfoPart3 || null == roleInfoPart3.getDayInfo()
				|| !isToday())
			return 0;
		else
			return roleInfoPart3.getDayInfo().getStaminaReset();
	}

	public int getUserStaminaResetTime() {
		if (null == roleInfoPart3
				|| !DateUtil
						.isToday(roleInfoPart3.getStaminaResetTime() * 1000L))
			return 0;
		else
			return roleInfoPart3.getStaminaResetTime();
	}

	// 当前行动力值
	public int getUserStamina() {
		int lastTime = getUserStaminaResetTime();
		if (lastTime == 0) {
			return getMaxStamina();
		} else {
			int stamina = getStamina() + (Config.serverTimeSS() - lastTime)
					/ CacheMgr.userCommonCache.getAddTime();
			if (stamina > getMaxStamina())
				stamina = getMaxStamina();
			return stamina;
		}
	}

	public int getUserStaminaRecoverLeftTime() {
		int time = getUserStaminaResetTime();
		if (time == 0)
			return 0;
		return CacheMgr.userCommonCache.getAddTime()
				- ((Config.serverTimeSS() - time) % CacheMgr.userCommonCache
						.getAddTime());
	}

	public int getHelpArmCount() {
		if (null == roleInfoPart3
				|| !DateUtil.isToday(roleInfoPart3.getPowerHelpArm() * 1000L))
			return 0;
		else
			return 1;

	}

	/**
	 * 领地总数
	 * 
	 * @return
	 */
	public int getMaxFief() {
		try {
			PropUser propUser = (PropUser) CacheMgr.propUserCache
					.get(getLevel());
			return propUser.getFiefLimit();
		} catch (GameException e) {
			return 1;
		}
	}

	/**
	 * 仓库储物重量上限
	 * 
	 * @return
	 */
	public int getMaxStoreWeight() {
		try {
			PropUser propUser = (PropUser) CacheMgr.propUserCache
					.get(getLevel());
			return propUser.getResourceLimit();
		} catch (GameException e) {
			return Integer.MAX_VALUE;
		}
	}

	public void setRoleChargeInfo(RoleChargeInfo info) {
		if (null != roleInfoPart3)
			roleInfoPart3.setChargeInfo(info);
	}

	public void setRoleDayInfo(RoleDayInfo info) {
		if (null != roleInfoPart3)
			roleInfoPart3.setDayInfo(info);
	}

	// 天降横财 剩余次数
	public int godWealthLeftTimes() {
		return CacheMgr.dictCache
				.getDictInt(Dict.GOD_WEALTH_TIMES_CURRENCY, 99)
				- godWealthTimes();
	}

	// 战神宝箱当日剩余次数
	public int warLordBoxTimes() {
		return (CacheMgr.dictCache.getDictInt(
				Dict.WAR_GOD_BOX_OPEN_TOTAL_TIMES, 1) - Account.readLog.WAR_LORD_BOX_TIMES);
	}

	// 是否处于新手等级
	public boolean isNewerProtectedLevel() {
		return getLevel() >= CacheMgr.userCommonCache.getNewerMinLevel()
				&& getLevel() < CacheMgr.userCommonCache.getNewerMaxLevel();
	}

	// 是否可享受双倍优惠--积分满可享受
	public boolean hasDoubleRechrgePrivilege() {
		int times = Account.user.getDoubleChargeTimes();
		int total = CacheMgr.doubleChargeCache.getDoubleRechargeTotal(times);
		int cur = Account.user.getDoubleChargeValue();
		return cur >= total;
	}

	public int godWealthTimes() {
		int count = 0;
		if (null != roleInfoPart3)
			count = roleInfoPart3.getCurrencyMachineTimes();
		return count;
	}

	// 外敌入侵
	public int invasionTimes() {
		int count = 0;
		return count;
	}

	// 凤仪亭 好感度是否满100%
	public boolean realRouletteGoodIsFill() {
		return ((int) Account.user.getRealRouletteGood()) == 100;
	}

	// 铜雀台
	public int dongjakTimes() {
		return Account.readLog.FREE_TIMES != 0 ? 1 : 0;
	}

	// 每日签到
	public int dailyattendanceTimes() {
		return !Account.isTodayChecked() ? 1 : 0;
	}

	// 我要变强
	public int stiffenTimes() {
		return StrongerWindow.canReward() ? 1 : 0;
	}

	public void setInfo(AccountPswInfoClient client) {
		if (null != client) {
			setId(client.getUserid());
			setPsw(client.getPsw());
			setNick(client.getNick());
			setLevel(client.getLevel());
		}
	}
}
