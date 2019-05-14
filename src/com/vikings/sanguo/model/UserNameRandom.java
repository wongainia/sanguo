package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class UserNameRandom {
	private int sex;
	private int id;
	private String familyName;
	private String lastName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public static UserNameRandom fromString(String line) {
		UserNameRandom random = new UserNameRandom();
		StringBuilder buf = new StringBuilder(line);
		random.setSex(StringUtil.removeCsvInt(buf));
		random.setId(StringUtil.removeCsvInt(buf));
		random.setFamilyName(StringUtil.removeCsv(buf));
		random.setLastName(StringUtil.removeCsv(buf));
		return random;
	}
}
