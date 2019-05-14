package com.vikings.sanguo.battle.anim;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.vikings.sanguo.model.BattleAnimEffects;
import com.vikings.sanguo.utils.BattleCoordUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class MagicLongAtkAnim extends BaseDrawableAnim {
	// 盾兵放大到140
	private int xScale = 250; // x
	private int yScale = 250; // y 方向放大比例

	private boolean isMirror;
	private int rotate = 180; // 旋转的角度

	private int lXOffset = -26; // 与左边矩阵的x偏移
	private int lYOffset = 454;// 与左边矩阵的y偏移

	private int rXOffset = -116; // 与右边矩阵的x偏移
	private int rYOffset = 441;// 与右边矩阵的y偏移

	private int armX = 0; // 当前矩阵的左上的x
	private int armY = 0; // 当前矩阵的左上角y
	private boolean isLeft;

	private BattleAnimEffects battleAnimEffects;

	public MagicLongAtkAnim(View view, DrawableAnimationBasis anim,
			boolean isLeft, String soundName) {
		super(view, anim, true);
		this.isLeft = isLeft;
		setSoundName(soundName);
		// setDelay(200);

		Point armLR;
		if (isLeft) {
			armLR = BattleCoordUtil.downMatrixLB;
		} else {
			armLR = BattleCoordUtil.upMatrixLB;
		}

		this.armX = armLR.x;
		this.armY = armLR.y;
	}

	public MagicLongAtkAnim(View view, DrawableAnimationBasis anim,
			boolean isLeft, String soundName,
			BattleAnimEffects battleAnimEffects) {
		super(view, anim, true);
		this.isLeft = isLeft;
		setSoundName(soundName);
		// setDelay(200);

		Point armLR;
		if (isLeft) {
			armLR = BattleCoordUtil.downMatrixLB;
		} else {
			armLR = BattleCoordUtil.upMatrixLB;
		}

		this.armX = armLR.x;
		this.armY = armLR.y;
		this.battleAnimEffects = battleAnimEffects;
	}

	@Override
	protected void prepare() {
		// view.bringToFront();
		view.clearAnimation();
		// int left;
		// int top;
		// if (isLeft)
		// {
		// left = (int) (armX +
		// lXOffset*Config.SCALE_FROM_HIGH/**Config.screenWidth/BattleCoordUtil.OFFSET_OPPOSITE_WIDTH*/);
		// top = (int) (armY
		// -lYOffset*Config.SCALE_FROM_HIGH/**Config.screenWidth/BattleCoordUtil.OFFSET_OPPOSITE_WIDTH*/);
		// }
		// else
		// {
		// left = (int) (armX +
		// rXOffset*Config.SCALE_FROM_HIGH/**Config.screenWidth/BattleCoordUtil.OFFSET_OPPOSITE_WIDTH*/);
		// top = (int) (armY
		// -rYOffset*Config.SCALE_FROM_HIGH/**Config.screenWidth/BattleCoordUtil.OFFSET_OPPOSITE_WIDTH*/);
		// }
		int left = armX + battleAnimEffects.getXOffset();
		int top = armY - battleAnimEffects.getYOffset();
		Drawable d = view.getBackground();
		DrawableAnimationBasis beatAnimation = null;
		// if (d != null && d instanceof AnimationDrawable)
		// {
		// beatAnimation = (DrawableAnimationBasis) d;
		// }
		// if (isLeft)
		// {
		// beatAnimation = ImageUtil.createAnimationDrawable("btl_magic_frame",
		// 6, 60, 0, 0, xScale, yScale);
		// }
		// else
		// {
		// beatAnimation = ImageUtil.createAnimationDrawable("btl_magic_frame",
		// 6, 60, 1, 0, xScale, yScale);
		// }
		beatAnimation = ImageUtil.createAnimationDrawable(battleAnimEffects);
		view.setBackgroundDrawable(beatAnimation);
		// int width = beatAnimation.getFrame(0).getIntrinsicWidth();
		// int height = beatAnimation.getFrame(0).getIntrinsicHeight();

		if (beatAnimation != null) {
			setAnim(beatAnimation);
		}
		ViewUtil.setMarginLeft(view, left);
		ViewUtil.setMarginTop(view, top);
	}

	@Override
	public void animationEnd() {
	}

}
