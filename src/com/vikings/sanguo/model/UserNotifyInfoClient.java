package com.vikings.sanguo.model;

import java.io.Serializable;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.UserNotifyInfo;

/**
 * 系統 消息
 * 
 * 
 */
public class UserNotifyInfoClient implements Serializable {

	private static final long serialVersionUID = 4760127177486503258L;

	private UserNotifyInfo notifyInfo;

	private UserNotifyInfoClient() {
	}

	public int getType() {
		return null == notifyInfo ? 0 : notifyInfo.getType();
	}

	public UserNotifyInfoClient setType(int type) {
		if (null != notifyInfo)
			notifyInfo.setType(type);
		return this;
	}

	public int getStart() {
		return null == notifyInfo ? 0 : notifyInfo.getStart();
	}

	public UserNotifyInfoClient setStart(int start) {
		if (null != notifyInfo)
			notifyInfo.setStart(start);
		return this;
	}

	public int getDuration() {
		return null == notifyInfo ? 0 : notifyInfo.getDuration();
	}

	public UserNotifyInfoClient setDuration(int duration) {
		if (null != notifyInfo)
			notifyInfo.setDuration(duration);
		return this;
	}

	public String getMessage() {
		return null == notifyInfo ? "" : notifyInfo.getMessage();
	}

	public UserNotifyInfoClient setMessage(String message) {
		if (null != notifyInfo)
			notifyInfo.setMessage(message);
		return this;
	}

	public UserNotifyInfo getNotifyInfo() {
		return notifyInfo;
	}

	public void setNotifyInfo(UserNotifyInfo notifyInfo) {
		this.notifyInfo = notifyInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getDuration();
		result = prime * result + getStart();
		result = prime * result + getMessage().hashCode();
		result = prime * result + getType();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserNotifyInfoClient other = (UserNotifyInfoClient) obj;
		if (getDuration() != other.getDuration())
			return false;
		if (!getMessage().equals(other.getMessage()))
			return false;
		if (getStart() != other.getStart())
			return false;
		if (getType() != other.getType())
			return false;
		return true;
	}

	/**
	 * 判断是否过期
	 * 
	 * @return
	 */
	public boolean isExpired() {
		return getDuration() < (int) (Config.serverTime() / 1000)
						- getStart();
	}

	public static UserNotifyInfoClient convert(UserNotifyInfo uni) {
		if (null == uni)
			return null;
		UserNotifyInfoClient unic = new UserNotifyInfoClient();
		unic.setNotifyInfo(uni);
		return unic;
	}
}
