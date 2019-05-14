package com.vikings.sanguo.model;

public class NearbyUserInfo {
	private int userId;
	private int wishTime;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getWishTime() {
		return wishTime;
	}

	public void setWishTime(int wishTime) {
		this.wishTime = wishTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
		result = prime * result + wishTime;
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
		NearbyUserInfo other = (NearbyUserInfo) obj;
		if (userId != other.userId)
			return false;
		if (wishTime != other.wishTime)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NearbyUserInfo [userId=" + userId + ", wishTime=" + wishTime
				+ ", getUserId()=" + getUserId() + ", getWishTime()="
				+ getWishTime() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}
	
	

}
