/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-12 上午10:41:35
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;


public class SelectedHero {
	private HeroInfoClient hic;
	private boolean isSel;

	public void setHic(HeroInfoClient hic) {
		this.hic = hic;
	}

	public void setSel(boolean isSel) {
		this.isSel = isSel;
	}

	public HeroInfoClient getHic() {
		return hic;
	}

	public boolean isSel() {
		return isSel;
	}
}
