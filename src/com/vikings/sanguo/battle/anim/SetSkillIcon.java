
package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;

public class SetSkillIcon extends BaseAnim{
	private Drawable d;
	private String img;
	private View amy;
	
	public SetSkillIcon(View view,Animation anim, Drawable d, String img) {
		super(view, anim, false);
		this.d = d;
		this.img = img;
		this.amy = view;
		setSoundName("battle_skill.ogg");
	}

	@Override
	protected void prepare() {
		new ViewImgScaleCallBack(img,"skill_small",view, Config.SCALE_FROM_HIGH* 33,Config.SCALE_FROM_HIGH * 33);
	}
}