package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.ConditionNum;
import com.vikings.sanguo.protos.ConditionStr;
import com.vikings.sanguo.utils.StringUtil;

public class UserQuery {
	// 条件
	private byte CONDITION_FIELD_NONE = 0; // (无)
	private byte CONDITION_FIELD_MOBILE = 1; // 手机号
	private byte CONDITION_FIELD_NICK = 2; // 昵称
	private byte CONDITION_FIELD_SEX = 3; // 性别 1 ： 女、 2 ： 男
	private byte CONDITION_FIELD_AGE = 4; // 年龄
	private byte CONDITION_FIELD_PROVINCE = 5; // 省份
	private byte CONDITION_FIELD_CITY = 6; // 城市
	private byte CONDITION_FIELD_MOBILE_LIKE = 7;
	private byte CONDITION_FIELD_USERID = 8;// 用户id
	private byte CONDITION_FIELD_IMAGE = 9; // 用户自定义头像 1：系统头像（image
											// <10000）2：自定义头像(image >
											// 10000)
	private byte CONDITION_FIELD_NEW_PLAYER = 10; // 新注册用户
	private byte CONDITION_FIELD_COUNTRY = 11; // 国家

	private boolean isNewFish = false;
	// 年龄段
	private byte CONDITION_VALUE_AGE_ALL = 0;// 所有年龄
	private byte CONDITION_VALUE_AGE_16_22 = 1;// 16-22
	private byte CONDITION_VALUE_AGE_23_30 = 2;// 22-30
	private byte CONDITION_VALUE_AGE_31_40 = 3;// 30-40
	private byte CONDITION_VALUE_AGE_41 = 4;// 40+

	private int userId;

	private int sex;

	private byte age;

	private byte province;

	private byte city;

	// 自定义头像
	private byte image;

	private String nickName;

	private String cellPhone;

	private String cellPhoneLike;

	private int country;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public byte getAge() {
		return age;
	}

	public void setAge(byte age) {
		this.age = age;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public byte getProvince() {
		return province;
	}

	public void setProvince(byte province) {
		this.province = province;
	}

	public byte getCity() {
		return city;
	}

	public void setCity(byte city) {
		this.city = city;
	}

	public byte getImage() {
		return image;
	}

	public void setImage(byte image) {
		this.image = image;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getCellPhoneLike() {
		return cellPhoneLike;
	}

	public void setCellPhoneLike(String cellPhoneLike) {
		this.cellPhoneLike = cellPhoneLike;
	}

	public boolean isNewFish() {
		return isNewFish;
	}

	public void setNewFish(boolean isNewFish) {
		this.isNewFish = isNewFish;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public List<ConditionNum> getConditionNumList() {
		List<ConditionNum> list = new ArrayList<ConditionNum>();
		// id是精确查找，与其他条件互斥，优先级最高
		if (userId != 0) {
			ConditionNum con = new ConditionNum().setField(
					(int) CONDITION_FIELD_USERID).setValue(userId);
			list.add(con);
		}

		if (sex != 0) {
			ConditionNum con = new ConditionNum().setField(
					(int) CONDITION_FIELD_SEX).setValue((int) sex);
			list.add(con);
		}

		if (age != 0) {
			ConditionNum con = new ConditionNum().setField(
					(int) CONDITION_FIELD_AGE).setValue((int) age);
			list.add(con);
		}

		if (province != 0) {
			ConditionNum con = new ConditionNum().setField(
					(int) CONDITION_FIELD_PROVINCE).setValue((int) province);
			list.add(con);
		}

		if (city != 0) {
			ConditionNum con = new ConditionNum().setField(
					(int) CONDITION_FIELD_CITY).setValue((int) city);
			list.add(con);
		}

		if (image != 0) {
			ConditionNum con = new ConditionNum().setField(
					(int) CONDITION_FIELD_IMAGE).setValue((int) image);
			list.add(con);
		}

		if (country != 0) {
			ConditionNum con = new ConditionNum().setField(
					(int) CONDITION_FIELD_COUNTRY).setValue(country);
			list.add(con);
		}

		return list;
	}

	public List<ConditionStr> getConditionStrList() {
		List<ConditionStr> list = new ArrayList<ConditionStr>();
		if (!StringUtil.isNull(cellPhone)) {
			ConditionStr str = new ConditionStr().setField(
					(int) CONDITION_FIELD_MOBILE).setValue(cellPhone);
			list.add(str);
		}

		if (!StringUtil.isNull(nickName)) {
			ConditionStr str = new ConditionStr().setField(
					(int) CONDITION_FIELD_NICK).setValue(nickName);
			list.add(str);
		}

		if (!StringUtil.isNull(cellPhoneLike)) {
			ConditionStr str = new ConditionStr().setField(
					(int) CONDITION_FIELD_MOBILE_LIKE).setValue(cellPhoneLike);
			list.add(str);
		}

		return list;
	}

	public List<ConditionNum> getConditionNewPlay() {
		List<ConditionNum> list = new ArrayList<ConditionNum>();
		ConditionNum con = new ConditionNum()
				.setField((int) CONDITION_FIELD_NEW_PLAYER);
		list.add(con);
		if (image != 0) {
			ConditionNum con1 = new ConditionNum().setField(
					(int) CONDITION_FIELD_IMAGE).setValue((int) image);
			list.add(con1);
		}
		if (sex != 0) {
			ConditionNum con2 = new ConditionNum().setField(
					(int) CONDITION_FIELD_SEX).setValue((int) sex);
			list.add(con2);
		}
		return list;
	}
}
