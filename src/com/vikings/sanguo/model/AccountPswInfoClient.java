package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.AccountPswInfo;

public class AccountPswInfoClient {
	private int userid;
	private String psw;
	private String nick;
	private int sid;
	private int level;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountPswInfoClient other = (AccountPswInfoClient) obj;
		if (userid != other.userid || sid != other.userid)
			return false;
		return true;
	}

	public static AccountPswInfoClient convert(AccountPswInfo info) {
		AccountPswInfoClient client = new AccountPswInfoClient();
		if (null != info) {
			client.setUserid(info.getUserid());
			client.setSid(info.getZoneid());
			client.setNick(info.getNick());
			client.setLevel(info.getLevel());
			client.setPsw(info.getPsw());
		}
		return client;
	}

	public static List<AccountPswInfoClient> convert2List(
			List<AccountPswInfo> infos) {
		List<AccountPswInfoClient> list = new ArrayList<AccountPswInfoClient>();
		if (null != infos && !infos.isEmpty()) {
			for (AccountPswInfo info : infos) {
				list.add(convert(info));
			}
		}
		return list;
	}
}
