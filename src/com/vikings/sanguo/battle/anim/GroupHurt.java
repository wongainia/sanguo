/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-3-18 下午7:40:10
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.thread.CallBack;
import android.view.View;
import android.view.animation.Animation;

public class GroupHurt extends BaseAnim{
	private CallBack cb;
	
	public GroupHurt(View view, Animation anim, CallBack cb) {
		super(view, anim, false);
		this.cb = cb;
	}

	@Override
	protected void prepare() {
		if (null != cb)
			cb.onCall();
	}
}
