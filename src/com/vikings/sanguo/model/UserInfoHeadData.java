package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 用于数据传递，用于处理界面头部显示user相关信息
 * 
 * @author susong
 * 
 */
public class UserInfoHeadData {
	public static final int DATA_TYPE_MONEY = 1;
	public static final int DATA_TYPE_CURRENCY = 2;
	public static final int DATA_TYPE_FOOD = 3;
	public static final int DATA_TYPE_ARMY = 4;
	public static final int DATA_TYPE_POP = 5;
	public static final int DATA_TYPE_EXPLOIT = 6;
	public static final int DATA_TYPE_WOOD = 7;
	public static final int DATA_TYPE_HERO = 8;
	public static final int DATA_TYPE_TIE = 9;
	public static final int DATA_TYPE_PI = 10;

	private int type;
	private String value;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setValue(int value) {
		this.value = String.valueOf(value);
	}

	public static List<UserInfoHeadData> getUserInfoHeadDatas() {
		return getUserInfoHeadDatas(UserInfoHeadData.DATA_TYPE_MONEY,
				UserInfoHeadData.DATA_TYPE_CURRENCY,
				UserInfoHeadData.DATA_TYPE_FOOD,
				UserInfoHeadData.DATA_TYPE_ARMY);
	}

	public static List<UserInfoHeadData> getSpecialDatas1() {
		List<UserInfoHeadData> datas = new ArrayList<UserInfoHeadData>();
		datas.add(UserInfoHeadData
				.getUserInfoHeadData(UserInfoHeadData.DATA_TYPE_MONEY));
		datas.add(UserInfoHeadData
				.getUserInfoHeadData(UserInfoHeadData.DATA_TYPE_CURRENCY));
		UserInfoHeadData popData = new UserInfoHeadData();
		popData.setType(UserInfoHeadData.DATA_TYPE_POP);
		popData.setValue(CalcUtil.turnToHundredThousand(Account.manorInfoClient
				.getRealCurPop())
				+ "/"
				+ CalcUtil.turnToHundredThousand(Account.manorInfoClient
						.getTotPop()));
		datas.add(popData);
		UserInfoHeadData armData = new UserInfoHeadData();
		armData.setType(UserInfoHeadData.DATA_TYPE_ARMY);
		armData.setValue(CalcUtil.turnToHundredThousand(Account.myLordInfo
				.getArmCount())
				+ "/"
				+ CalcUtil.turnToHundredThousand(Account.manorInfoClient
						.getToplimitArmCount()));
		datas.add(armData);
		return datas;
	}

	public static UserInfoHeadData getUserInfoHeadData(int type) {
		UserInfoHeadData data = new UserInfoHeadData();
		data.setType(type);
		switch (type) {
		case DATA_TYPE_MONEY:
			data.setValue(CalcUtil.turnToHundredThousand(Account.user
					.getMoney()));
			break;
		case DATA_TYPE_CURRENCY:
			data.setValue(Account.user.getCurrency());
			break;
		case DATA_TYPE_FOOD:
			data.setValue(CalcUtil.turnToHundredThousand(Account.user.getFood()));
			break;
		case DATA_TYPE_ARMY:
			data.setValue(Account.myLordInfo.getArmCount());
			break;
		case DATA_TYPE_POP:
			int totalPop = Account.manorInfoClient.getTotPop();
			int curPop = Account.manorInfoClient.getRealCurPop();
			data.setValue(curPop > totalPop ? totalPop : curPop);
			break;
		case DATA_TYPE_EXPLOIT:
			data.setValue(CalcUtil.turnToHundredThousand(Account.user
					.getExploit()));
			break;
		case DATA_TYPE_WOOD:
			data.setValue(Account.user.getWood());
			break;
		case DATA_TYPE_HERO:
			int heroSize = Account.heroInfoCache.size();
			int totalHero = Account.user.getHeroLimit();
			data.setValue(heroSize > totalHero ? totalHero : heroSize);
			break;
		case DATA_TYPE_TIE:
			data.setValue(Account.user.getMaterial0());
			break;
		case DATA_TYPE_PI:
			data.setValue(Account.user.getMaterial1());
			break;
		default:
			break;
		}
		return data;
	}

	public static List<UserInfoHeadData> getUserInfoHeadDatas(int... types) {
		List<UserInfoHeadData> datas = new ArrayList<UserInfoHeadData>();
		for (int i = 0; i < types.length; i++) {
			UserInfoHeadData data = getUserInfoHeadData(types[i]);
			datas.add(data);
		}
		return datas;
	}
}
