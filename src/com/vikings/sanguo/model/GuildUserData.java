package com.vikings.sanguo.model;

public class GuildUserData {
	private BriefUserInfoClient user;
	private BriefGuildInfoClient bgic;
	private Country country;

	public BriefUserInfoClient getUser() {
		return user;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

	public BriefGuildInfoClient getBgic() {
		return bgic;
	}

	public void setBgic(BriefGuildInfoClient bgic) {
		this.bgic = bgic;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		GuildUserData other = (GuildUserData) o;
		if (null == user || null == other.user)
			return false;
		if (user.getId().intValue() != other.getUser().getId().intValue())
			return false;
		return true;
	}

}
