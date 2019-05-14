package com.vikings.sanguo.battle.anim;

import android.view.View;

import com.vikings.sanguo.utils.ViewUtil;

public class HitDrawableAnimation extends BaseDrawableAnim {

	private View amy;

	public HitDrawableAnimation(View view, View amy,
			DrawableAnimationBasis anim, boolean needGone) {
		super(view, anim, true);
		this.amy = amy;
	}

	@Override
	public void animationEnd() {

	}

	@Override
	protected void prepare() {
		ViewUtil.setHide(view);
	}

	@Override
	protected void stop() {
		super.stop();
	}

}
