package com.vikings.sanguo.model;

//将领兑换列表数据结构
public class HeroRecruitExchangeData {
	private HeroRecruitExchange exchange;
	private OtherHeroInfoClient ohic;
	private boolean init = false;

	public HeroRecruitExchange getExchange() {
		return exchange;
	}

	public void setExchange(HeroRecruitExchange exchange) {
		this.exchange = exchange;
	}

	public OtherHeroInfoClient getOhic() {
		return ohic;
	}

	public void setOhic(OtherHeroInfoClient ohic) {
		this.init = true;
		this.ohic = ohic;
	}

	public boolean isInit() {
		return init;
	}
}
