/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-5 下午3:22:28
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.io.Serializable;

public class ArenaHero implements Serializable{
	private static final long serialVersionUID = 8431691364844400279L;
	private HeroIdBaseInfoClient hero;
	private boolean isDead;
	
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	public void setHero(HeroIdBaseInfoClient hero) {
		this.hero = hero;
	}
	
	public HeroIdBaseInfoClient getHero() {
		return hero;
	}
	
	public boolean isDead() {
		return isDead;
	}
}
