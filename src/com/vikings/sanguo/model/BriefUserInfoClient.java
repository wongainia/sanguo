package com.vikings.sanguo.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.BriefUserInfo;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 
 * 新做的breif user 取代以前的user 专门用于用户缓存，显示列表，地图上的批量用户信息
 * 
 * 用户自己信息 UserAccountClient 查询别人的UserOtherClient
 * 
 * @author Brad.Chen
 * 
 */
public class BriefUserInfoClient implements Comparable<BriefUserInfoClient>,
		Serializable {

	private static final long serialVersionUID = -5136491581891297415L;
	private BriefUserInfo briefUserInfo;

	public BriefUserInfoClient() {
		this.briefUserInfo = new BriefUserInfo();
	}

	public BriefUserInfoClient(BriefUserInfo briefUserInfo) {
		this.briefUserInfo = briefUserInfo;
	}

	public Integer getId() {
		return briefUserInfo.getId();
	}

	public BriefUserInfo setId(Integer id) {
		return briefUserInfo.setId(id);
	}

	public Integer getImage() {
		return briefUserInfo.getImage();
	}

	public BriefUserInfo setImage(Integer image) {
		return briefUserInfo.setImage(image);
	}

	public String getNickName() {
		return briefUserInfo.getNick();
	}

	public String getHtmlNickName() {
		return TextUtils.htmlEncode(briefUserInfo.getNick());
	}

	public BriefUserInfo setNickName(String nick) {
		return briefUserInfo.setNick(nick);
	}

	public Integer getSex() {
		return briefUserInfo.getSex();
	}

	public BriefUserInfo setSex(Integer sex) {
		return briefUserInfo.setSex(sex);
	}

	public Integer getBirthday() {
		return briefUserInfo.getBirthday();
	}

	public BriefUserInfo setBirthday(Integer birthday) {
		return briefUserInfo.setBirthday(birthday);
	}

	public Integer getProvince() {
		return briefUserInfo.getProvince();
	}

	public BriefUserInfo setProvince(Integer province) {
		return briefUserInfo.setProvince(province);
	}

	public Integer getCity() {
		return briefUserInfo.getCity();
	}

	public BriefUserInfo setCity(Integer city) {
		return briefUserInfo.setCity(city);
	}

	public Integer getLevel() {
		return briefUserInfo.getLevel();
	}

	public BriefUserInfo setLevel(Integer level) {
		return briefUserInfo.setLevel(level);
	}

	public void setCountry(int country) {
		briefUserInfo.setCountry(country);
	}

	public Integer getGuildid() {
		return briefUserInfo.getGuildid();
	}

	public BriefUserInfo setGuildid(Integer guildid) {
		return briefUserInfo.setGuildid(guildid);
	}

	public int getLastLoginTime() {
		return briefUserInfo.getLastLoginTime();
	}

	public BriefUserInfo setLastLoginTime(Integer lastLoginTime) {
		return briefUserInfo.setLastLoginTime(lastLoginTime);
	}

	public String getSexName() {
		return briefUserInfo.getSex().byteValue() == Constants.MALE ? Config
				.getController().getString(R.string.male) : Config
				.getController().getString(R.string.female);
	}

	public String getBirthdayStr() {
		try {
			return DateUtil.date1.format(new Date(getBirthday() * 1000L));
		} catch (Exception e) {
			return "1995/01/01";
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

	public Integer getCountry() {
		return briefUserInfo.getCountry();
	}

	public BriefUserInfo setCountry(Integer country) {
		return briefUserInfo.setCountry(country);
	}

	public Integer getCharge() {
		return briefUserInfo.getCharge() < 0 ? 0 : briefUserInfo.getCharge();
	}

	public BriefUserInfo setCharge(Integer charge) {
		return briefUserInfo.setCharge(charge);
	}

	public boolean isCustomIcon() {
		return getImage() > 1000;
	}

	public boolean isValidUser() {
		return getId() > 0;
	}

	public boolean hasGuild() {
		return getGuildid() > 0;
	}

	public boolean hasCountry() {
		return getCountry() > 0;
	}

	public boolean isSelf() {
		if (null != getId())
			return Account.user.getId() == getId();
		else
			return false;
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

	@Override
	public boolean equals(Object o) {
		if (o instanceof BriefUserInfoClient) {
			return getId().equals(((BriefUserInfoClient) o).getId());
		} else
			return false;
	}

	// 比较等级，等级一样比较经验
	@Override
	public int compareTo(BriefUserInfoClient another) {
		return this.getLevel() - another.getLevel();
	}

	// 是否是NPC，如果是读配置取user信息
	public static boolean isNPC(int userid) {
		// return userid < 10000;
		return CacheMgr.npcCache.containKey(userid);
	}

	public boolean isOtherUser() {
		return !isNPC(getId()) && (Account.user.getId() != getId());
	}

	// 是否是NPC，如果是读配置取user信息
	public boolean isNPC() {
		return CacheMgr.npcCache.containKey(briefUserInfo.getId());
	}

	public static BriefUserInfoClient convert(BriefUserInfo b) {
		return new BriefUserInfoClient(b);
	}

	public String getCountryName() {
		Country country = CacheMgr.countryCache.getCountry(getCountry()
				.intValue());
		if (null != country && null != country.getName())
			return country.getName();
		else
			return "";
	}

	public UserVip getCurVip() {
		return CacheMgr.userVipCache.getVipByCharge(getCharge());
	}

	public boolean isVip() {
		return getCurVip().getLevel() > 0;
	}

	// 是否处于新手保护期
	public boolean isNewerProtected() {
		return getLevel() >= CacheMgr.userCommonCache.getNewerMinLevel()
				&& getLevel() < CacheMgr.userCommonCache.getNewerMaxLevel();
	}

	// 新手与非新手之间能否打开关(true 有限制，false 无限制)
	public static boolean attackLevelLimit() {
		return CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 41) == 0;
	}
}
