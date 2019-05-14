/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-11-8
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.battle.anim;

import java.util.Random;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleArrayInfoClient;
import com.vikings.sanguo.protos.BattleEventArmInfo;
import com.vikings.sanguo.ui.window.BattleWindow;

import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
//把isLeft改成 isDown
public class AnimPool {
	private static int rate = 1;
	private final static String TAG = "AnimPool";

	private static void setDuration(int time, Animation anim) {
		if (null == anim)
			return;

		anim.setDuration(time * rate);
	}

	// 入场动画
	public Animation enterBattleField() {
		AlphaAnimation ta = new AlphaAnimation(0f, 1f);
		setDuration(200, ta);
		return ta;
	}

	// 攻方进入战斗位置
	public Animation reachAtkPos(boolean isDown, int dis) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, dis, 0, -dis/* 0 */);
		else
			ta = new TranslateAnimation(0, (-dis), 0, dis); // *
		//ta.setFillBefore(true);
		setDuration(200, ta);
		return ta;
	}

	// 近程攻击
	public Animation shortRangeAtk(boolean isDown, int dis, int during) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, dis, 0, -dis);
		else
			ta = new TranslateAnimation(0, -dis, 0, dis);
		setDuration(during, ta);
		return ta;
	}
		
	public Animation remoteRangeAtk(boolean isDown, int dis, int during) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, dis, 0, -dis);
		else
			ta = new TranslateAnimation(0, -dis, 0, dis);
		ta.setInterpolator(new DecelerateInterpolator());
		setDuration(during, ta);
		return ta;
	}
	

	public Animation shortRangeAtkSpeed(boolean isDown, int dis, int during) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, dis, 0, -dis);
		else
			ta = new TranslateAnimation(0, -dis, 0, dis);
		setDuration(during, ta);
		ta.setInterpolator(new AccelerateInterpolator());
		// ta.setInterpolator(new Interpolator()
		// {
		// @Override
		// public float getInterpolation(float input)
		// {
		// return 15*input*input;
		// }
		// });
		return ta;
	}

	public Animation shortRangeAtkBounce(boolean isDown, int dis,
			int startOffset, int during) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, dis, 0, -dis);
		else
			ta = new TranslateAnimation(0, -dis, 0, dis);
		ta.setStartOffset(startOffset);
		setDuration(during, ta);
		return ta;
	}

	public static Animation shortAtkBounceSpeed(boolean isDown, int dis,
			int startOffset, int during) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, dis, 0, -dis);
		else
			ta = new TranslateAnimation(0, -dis, 0, dis);
		ta.setStartOffset(startOffset);
		setDuration(during, ta);
		// ta.setInterpolator(new Interpolator()
		// {
		// @Override
		// public float getInterpolation(float input)
		// {
		// return 15*input*input;
		// }
		// });
		ta.setInterpolator(new AccelerateInterpolator());
		// ta.setInterpolator(new AccelerateDecelerateInterpolator());
		return ta;
	}

	// 技能变大
	public Animation skillLarge() {
		ScaleAnimation sa = new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(300, sa);
		return sa;
	}

	// 停500ms
	public static Animation stay(int during) {
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0);
		setDuration(during, ta);
		return ta;
	}

	// 受击动画
	public Animation beaten(boolean isDown) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, -50 * Config.SCALE_FROM_HIGH, 0,
					50 * Config.SCALE_FROM_HIGH);
		else
			ta = new TranslateAnimation(0, 50 * Config.SCALE_FROM_HIGH, 0, -50
					* Config.SCALE_FROM_HIGH);
		setDuration(150, ta);
		return ta;
	}

	// 受击抖动
	public static Animation shake(boolean isDown) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, -10 * Config.SCALE_FROM_HIGH, 0,
					10 * Config.SCALE_FROM_HIGH);
		else
			ta = new TranslateAnimation(0, 10 * Config.SCALE_FROM_HIGH, 0, -10
					* Config.SCALE_FROM_HIGH);

		ta.setRepeatCount(1);
		setDuration(100, ta);
		return ta;
	}

	// 向右长距离移动
	private TranslateAnimation getMoveRFar() {
		TranslateAnimation moveRFar = new TranslateAnimation(0,
				300 * Config.SCALE_FROM_HIGH, 0, -300 * Config.SCALE_FROM_HIGH);
		setDuration(300, moveRFar);
		moveRFar.setInterpolator(new AccelerateInterpolator());
		return moveRFar;
	}

	// 向左长距离移动
	private TranslateAnimation getMoveLFar() {
		TranslateAnimation moveLFar = new TranslateAnimation(0, -300
				* Config.SCALE_FROM_HIGH, 0, 300 * Config.SCALE_FROM_HIGH);
		setDuration(300, moveLFar);
		moveLFar.setInterpolator(new AccelerateInterpolator());
		return moveLFar;
	}

	// 远程攻击
	public Animation longRangeAtk(boolean isDown) {
		return isDown ? getMoveRFar() : getMoveLFar();
	}

	// 逃跑，出屏
	public Animation escape(boolean isDown) {
		if (isDown)
			return getMoveLFar();
		else
			return getMoveRFar();
	}

	// 放大1倍
	public ScaleAnimation getZoomOut() {
		ScaleAnimation zoomOut = new ScaleAnimation(0.5f, 1.5f, 0.5f, 1.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(150, zoomOut);
		return zoomOut;
	}

	// 显示技能
	public Animation skillAppear() {
		ScaleAnimation zoomOut = new ScaleAnimation(1.5f, 1f, 1.5f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(200, zoomOut);
		return zoomOut;
	}

	// 技能消失
	public AnimationSet skillDisappear() {
		// info
		AnimationSet disappear = new AnimationSet(true);
		ScaleAnimation zoomOut = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
		disappear.addAnimation(zoomOut);
		disappear.addAnimation(alpha);
		setDuration(100, disappear);

		return disappear;
	}

	// 空动画，仅用于延时
	public static TranslateAnimation getNullAnim(int time) {
		TranslateAnimation nullAnim = new TranslateAnimation(0, 0, 0, 0);
		setDuration(time, nullAnim);
		// nullAnim.setFillAfter(true);
		return nullAnim;
	}

	public TranslateAnimation getClearAnim() {
		TranslateAnimation nullAnim = new TranslateAnimation(0, 0, 0, 0);
		setDuration(5, nullAnim);
		nullAnim.setFillAfter(true);
		return nullAnim;
	}

	// 头顶信息，包括技能名，损失人数/士气/逃跑
	public static AnimationSet getTop(int startOffset) {
		AnimationSet top = new AnimationSet(true);
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, -80
				* Config.SCALE_FROM_HIGH);

		AlphaAnimation sa = new AlphaAnimation(1f, 0f);
		top.addAnimation(ta);
		top.addAnimation(sa);

		top.setFillAfter(true);
		setDuration(700, top);
		if (startOffset != 0) {
			top.setStartOffset(startOffset);
		}
		// top.setInterpolator(new DecelerateInterpolator());
		return top;
	}

	public static AnimationSet forceAtk(int startOffset) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, -20
				* Config.SCALE_FROM_HIGH);
		ScaleAnimation zoomIn = new ScaleAnimation(0.5f, 1.2f, 0.5f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		set.addAnimation(ta);
		set.addAnimation(zoomIn);
		set.setFillAfter(true);
		setDuration(700, set);
		set.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input) * (1.0f - input)
						* (1.0f - input));
				// else
				// return 1;
			}
		});
		if (startOffset != 0) {
			set.setStartOffset(startOffset);
		}
		return set;
	}

	// 回合动画
	public Animation showRoundAnim() {
		TranslateAnimation nullAnim = new TranslateAnimation(0, 0, 0, 0);
		setDuration(700, nullAnim);
		return nullAnim;
	}

	public Animation showHeroIcon(boolean isDown) {
		TranslateAnimation anim = null;
		if (isDown) 
		{
			anim = new TranslateAnimation((int) (150 * Config.SCALE_FROM_HIGH),0, 0, 0);			
		} 
		else 
		{
			anim = new TranslateAnimation((int) (-150 * Config.SCALE_FROM_HIGH), 0, 0, 0);
		}

		setDuration(300, anim);
		anim.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input) * (1.0f - input)
						* (1.0f - input));
			}
		});
		anim.setFillAfter(true);
		return anim;
	}

	public Animation showHeroIconThird(boolean isDown, int startOffset, int time) {
		TranslateAnimation anim = null;
		if (isDown) {
			anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					1.0f, Animation.RELATIVE_TO_SELF, 0f);						
		} else {
			anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					-1.0f, Animation.RELATIVE_TO_SELF, 0);
		}

		if (startOffset != 0) {
			anim.setStartOffset(startOffset);
		}
		setDuration(time, anim);
		anim.setInterpolator(new AccelerateInterpolator());
		anim.setFillAfter(true);
		return anim;
	}

	public Animation showHeroIconSecond(boolean isDown, int startOffset,
			int time) {
		TranslateAnimation anim = null;
		if (isDown) {
			anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1f,
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					0, Animation.RELATIVE_TO_SELF, 0);			
		} else {
			anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0, Animation.RELATIVE_TO_SELF, 0);
		}

		if (startOffset != 0) {
			anim.setStartOffset(startOffset);
		}
		setDuration(time, anim);
		// anim.setInterpolator(new AccelerateInterpolator());
		anim.setInterpolator(new AccelerateInterpolator());
		anim.setFillAfter(true);
		return anim;
	}

	public Animation moveMoreHeroIcon() {
		TranslateAnimation anim = new TranslateAnimation(0,
				(int) (30 * Config.scaleRate), 0, 0); // SCALE_FROM_HIGH
		setDuration(1000, anim);
		return anim;
	}

	public Animation moveHeroIconOut() {
		TranslateAnimation anim = new TranslateAnimation(0, Config.screenWidth,
				0, 0);
		setDuration(150, anim);
		return anim;
	}

	public Animation moveSkillBgOut() {
		TranslateAnimation anim = new TranslateAnimation(0,
				-Config.screenWidth, 0, 0);
		setDuration(150, anim);
		return anim;
	}

	public Animation moveSkill(boolean isDown) {
		TranslateAnimation anim = null;
		if (isDown) 
		{
			anim = new TranslateAnimation(-Config.screenWidth, 0, 0, 0);			
		} 
		else 
		{
			anim = new TranslateAnimation(Config.screenWidth, 0, 0, 0);
		}
		setDuration(300, anim);
		anim.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input) * (1.0f - input)
						* (1.0f - input));
			}
		});
		return anim;
	}

	public Animation zoomOutSkillName() {
		ScaleAnimation sa = new ScaleAnimation(0.6f, 1.3f, 0.6f, 1.3f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(100, sa);
		sa.setFillAfter(true);
		return sa;
	}

	// 组合将领时候移动 名字
	public Animation moveCombiSkillName(boolean isDown) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation anim = null;
		if (isDown) {
			anim = new TranslateAnimation(0, -Config.screenWidth / 2 - 43
					* Config.SCALE_FROM_HIGH, 0, 0);
		} else {
			anim = new TranslateAnimation(0, Config.screenWidth / 2 + 43
					* Config.SCALE_FROM_HIGH, 0, 0);
		}
		setDuration(300, anim);
		anim.setFillAfter(true);

		AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.9f);
		setDuration(300, alpha);
		alpha.setFillAfter(true);

		// ScaleAnimation sa = new ScaleAnimation(3f, 0.5f, 3f, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		// 0.5f);

		ScaleAnimation sa = new ScaleAnimation(2.7f, 0.5f, 2.7f, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		setDuration(300, sa);
		// sa.setFillBefore(true);
		// sa.setFillAfter(false);
		sa.setFillAfter(true);

		ScaleAnimation sa_2 = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa_2.setStartOffset(300);
		setDuration(200, sa_2);
		sa_2.setFillAfter(true);

		set.addAnimation(anim);
		set.addAnimation(alpha);
		set.addAnimation(sa);
		set.addAnimation(sa_2);
		set.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input) * (1.0f - input)
						* (1.0f - input));
			}
		});
		// set.setFillAfter(true);
		return set;
	}

	public Animation moveSkillNameOld(boolean isDown, int dis) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation anim = null;
		if (isDown) {
			anim = new TranslateAnimation(0, -dis, 0, 0);
		} else {
			anim = new TranslateAnimation(0, dis, 0, 0);
		}
		setDuration(300, anim);
		anim.setFillAfter(true);

		AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.8f);
		setDuration(300, alpha);
		alpha.setFillAfter(true);

		ScaleAnimation sa = new ScaleAnimation(1.7f, 0.5f, 1.7f, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(300, sa);
		sa.setFillAfter(true);

		ScaleAnimation sa_2 = new ScaleAnimation(0.7f, 1.8f, 0.7f, 1.8f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa_2.setStartOffset(300);
		setDuration(100, sa_2);

		set.addAnimation(anim);
		set.addAnimation(alpha);
		set.addAnimation(sa);
		set.addAnimation(sa_2);
		set.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input) * (1.0f - input)
						* (1.0f - input));
			}
		});
		set.setFillAfter(true);
		return set;
	}

	public Animation moveSkillName(boolean isDown, int dis) {
		HeroNameFlyAnim move = new HeroNameFlyAnim(2.0f, 0.5f, 2.5f, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		if (isDown) {
			move.setParam(0, dis, 0, 0, 0.1f, 0.9f, 600);			
		} else {
			move.setParam(0, -dis, 0, 0, 0.1f, 0.9f, 600);
		}
		move.setFillAfter(true);
		return move;
	}

	public Animation moveSkillNameEx(boolean isDown, int dis) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation anim = null;
		if (isDown) {
			anim = new TranslateAnimation(0, -dis, 0, 0);
		} else {
			anim = new TranslateAnimation(0, dis, 0, 0);
		}
		setDuration(300, anim);
		anim.setFillAfter(true);

		AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.9f);
		setDuration(300, alpha);
		alpha.setFillAfter(true);

		ScaleAnimation sa = new ScaleAnimation(2.5f, 0.8f, 2.5f, 0.8f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(300, sa);
		anim.setFillAfter(true);

		ScaleAnimation sa_2 = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa_2.setStartOffset(300);
		setDuration(200, sa_2);
		anim.setFillAfter(true);

		set.addAnimation(anim);
		set.addAnimation(alpha);
		set.addAnimation(sa);
		set.addAnimation(sa_2);
		set.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input) * (1.0f - input)
						* (1.0f - input));
			}
		});
		// set.setFillAfter(true);
		return set;
	}

	public Animation moveMoreSkillName() {
		TranslateAnimation anim = new TranslateAnimation(0,
				(int) (-30 * Config.scaleRate), 0, 0);
		setDuration(1000, anim);
		anim.setFillAfter(true);
		return anim;
	}

	public Animation skillSmall() {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(3.0f, 1.0f, 3.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(150, sa);
		set.addAnimation(sa);

		AlphaAnimation alpha = new AlphaAnimation(0.4f, 0.8f);
		setDuration(150, alpha);
		set.addAnimation(alpha);

		return set;
	}

	public Animation skillLightLargeOut() {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(200, sa);
		set.addAnimation(sa);

		AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.9f);
		setDuration(200, alpha);
		set.addAnimation(alpha);
		set.setFillAfter(true);
		return set;
	}

	public Animation skillOut() {
		AlphaAnimation alpha = new AlphaAnimation(1f, 0.1f);
		;
		setDuration(150, alpha);
		return alpha;
	}

	public Animation skillLight() {
		Animation rotate = new RotateAnimation(0, 180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(1100, rotate);
		return rotate;
	}

	public Animation skillBg() {
		ScaleAnimation sa = new ScaleAnimation(1.5f, 1.0f, 1.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(300, sa);
		sa.setFillAfter(true);
		return sa;
	}

	// 开始动画 弓兵 向后、向前动作
	public Animation startReadyEffects(boolean isDown) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, -5 * Config.SCALE_FROM_HIGH, 0,
					5 * Config.SCALE_FROM_HIGH);
		else
			ta = new TranslateAnimation(0, 5 * Config.SCALE_FROM_HIGH, 0, -5
					* Config.SCALE_FROM_HIGH);
		setDuration(200, ta);
		ta.setInterpolator(new OvershootInterpolator());
		return ta;
	}

	// 士兵旋转 准备攻击
	public static Animation soldierRotate(boolean isDown) {
		Animation rotate = null;
		if (isDown) {
			rotate = new RotateAnimation(0, 45, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.75f);
		} else {
			rotate = new RotateAnimation(0, -45, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.75f);
		}

		// rotate.setInterpolator(new AccelerateDecelerateInterpolator());
		rotate.setInterpolator(new OvershootInterpolator());
		// rotate.setInterpolator(new AccelerateInterpolator());
		setDuration(200, rotate);
		return rotate;

	}

	// 法兵旋转动画
	public static Animation magicRotate(boolean isDown, int duration) {
		Animation rotate = null;
		if (isDown) {
			rotate = new RotateAnimation(0, -30, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.9f);
		} else {
			rotate = new RotateAnimation(0, 30, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.9f);
		}
		setDuration(duration, rotate);
		rotate.setInterpolator(new OvershootInterpolator());
		return rotate;
	}

	// 法兵转回动画
	public Animation magicRotateBounce(boolean isDown) {
		Animation rotate = null;
		if (isDown) {
			rotate = new RotateAnimation(0, 30, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.9f);
		} else {
			rotate = new RotateAnimation(0, -30, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.9f);
		}
		setDuration(100, rotate);

		rotate.setInterpolator(new OvershootInterpolator());
		return rotate;
	}

	// 受击
	public static Animation beat(boolean isDown, int startOffset) {
		TranslateAnimation ta = null;
		if (isDown)
			ta = new TranslateAnimation(0, -70 * Config.SCALE_FROM_HIGH, 0,
					70 * Config.SCALE_FROM_HIGH);
		else
			ta = new TranslateAnimation(0, 70 * Config.SCALE_FROM_HIGH, 0, -70
					* Config.SCALE_FROM_HIGH);
		ta.setInterpolator(new AccelerateInterpolator());
		if (startOffset != 0) {
			ta.setStartOffset(startOffset);
		}
		setDuration(200, ta);
		return ta;
	}

	public static Animation beatEx(boolean isDown, int startOffset) {
		AnimationSet beat = new AnimationSet(false);
		TranslateAnimation ta = null;
		TranslateAnimation rollBack = null;
		if (isDown) {
			ta = new TranslateAnimation(0, -65 * Config.SCALE_FROM_HIGH, 0,
					65 * Config.SCALE_FROM_HIGH);
			ta.setDuration(100);
			rollBack = new TranslateAnimation(0, 65 * Config.SCALE_FROM_HIGH,
					0, -65 * Config.SCALE_FROM_HIGH);
			rollBack.setDuration(100);
			rollBack.setStartOffset(120);
		} else {
			ta = new TranslateAnimation(0, 65 * Config.SCALE_FROM_HIGH, 0, -65
					* Config.SCALE_FROM_HIGH);
			ta.setDuration(100);
			rollBack = new TranslateAnimation(0, -65 * Config.SCALE_FROM_HIGH,
					0, 65 * Config.SCALE_FROM_HIGH);
			rollBack.setDuration(100);
			rollBack.setStartOffset(120);
		}
		ta.setInterpolator(new AccelerateInterpolator());
		rollBack.setInterpolator(new DecelerateInterpolator());
		beat.addAnimation(ta);
		beat.addAnimation(rollBack);
		// beat.setInterpolator(new AccelerateDecelerateInterpolator());
		if (startOffset != 0) {
			beat.setStartOffset(startOffset);
		}
		return beat;
	}

	public static Animation soldierFly(boolean isDown, int dis,
			int startOffset, int time, int width, ViewGroup window,
			int hpValue, int totalValue) {
		AnimationSet fly = new AnimationSet(true);
		TranslateAnimation backOff = null;
		if (isDown) {
			backOff = new TranslateAnimation(0, -40 * Config.SCALE_FROM_HIGH,
					0, 40 * Config.SCALE_FROM_HIGH);
		} else {
			backOff = new TranslateAnimation(0, 40 * Config.SCALE_FROM_HIGH, 0,
					-40 * Config.SCALE_FROM_HIGH);
		}
		setDuration(100, backOff);

		SoliderFlyAnim flyAnim = null;

		float moveY = 0;
		float moveX = 0;

		int degree = 0;
		if (dis > 0) {
			float moveXDis = 0;// ((float)dis + Math.abs(new
			float moveYDis = 0;// 3*moveXDis/4 + Math.abs(new

			if (isDown) {
				moveYDis = ((float) dis + Math
						.abs(new Random().nextInt() % dis));
				moveXDis = 2 * moveYDis / 3
						+ Math.abs(new Random().nextInt() % moveYDis / 3);
			} else {
				moveXDis = ((float) dis + Math
						.abs(new Random().nextInt() % dis));
				moveYDis = moveXDis / 2
						+ Math.abs(new Random().nextInt() % moveXDis / 2);
			}
			moveX = moveXDis / width;
			moveY = moveYDis / width;
		}
		degree = 180 + new Random().nextInt() % 30;
		if (isDown) {
			flyAnim = new SoliderFlyAnim(0, -degree,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					-moveX, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, moveY);
			flyAnim.setView(isDown, window, hpValue, totalValue);

		} else {
			flyAnim = new SoliderFlyAnim(0, degree, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					moveX, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, -moveY);
		}
		flyAnim.setDuration(700);
		fly.addAnimation(flyAnim);
		if (startOffset != 0) {
			fly.setStartOffset(startOffset);
		}
		fly.setInterpolator(new AccelerateInterpolator());

		return fly;
	}

	public static Animation soldierFly(boolean isDown, int dis,
			int startOffset, int time, int width, ViewGroup window,
			int hpValue, int totalValue, BattleEventArmInfo beai,
			BattleArrayInfoClient baic, BattleWindow battleWin) {
		AnimationSet fly = new AnimationSet(true);
		TranslateAnimation backOff = null;
		if (isDown) {
			backOff = new TranslateAnimation(0, -40 * Config.SCALE_FROM_HIGH,
					0, 40 * Config.SCALE_FROM_HIGH);
		} else {
			backOff = new TranslateAnimation(0, 40 * Config.SCALE_FROM_HIGH, 0,
					-40 * Config.SCALE_FROM_HIGH);
		}
		setDuration(100, backOff);

		SoliderFlyAnim flyAnim = null;

		float moveY = 0;
		float moveX = 0;

		int degree = 0;
		if (dis > 0) {
			float moveXDis = 0;// ((float)dis + Math.abs(new
			float moveYDis = 0;// 3*moveXDis/4 + Math.abs(new

			if (isDown) {
				moveYDis = ((float) dis + Math
						.abs(new Random().nextInt() % dis));
				moveXDis = 2 * moveYDis / 3
						+ Math.abs(new Random().nextInt() % moveYDis / 3);
			} else {
				moveXDis = ((float) dis + Math
						.abs(new Random().nextInt() % dis));
				moveYDis = moveXDis / 2
						+ Math.abs(new Random().nextInt() % moveXDis / 2);
			}
			moveX = moveXDis / width;
			moveY = moveYDis / width;
		}
		degree = 180 + new Random().nextInt() % 30;
		if (isDown) {
			flyAnim = new SoliderFlyAnim(0, -degree,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					-moveX, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, moveY);
			// flyAnim.setView(isDown, window, hpValue,
			// totalValue,beai,baic,battleWin);

		} else {
			flyAnim = new SoliderFlyAnim(0, degree, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					moveX, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, -moveY);
			// flyAnim.setView(isDown, window, hpValue,
			// totalValue,beai,baic,battleWin);
		}
		flyAnim.setDuration(700);
		fly.addAnimation(flyAnim);
		if (startOffset != 0) {
			fly.setStartOffset(startOffset);
		}
		fly.setInterpolator(new AccelerateInterpolator());

		return fly;
	}

	public Animation getEnter(boolean isDown) {
		TranslateAnimation animation = null;
		if (isDown) {
			animation = new TranslateAnimation(-200 * Config.SCALE_FROM_HIGH,
					0, 200 * Config.SCALE_FROM_HIGH, 0);
		} else {
			animation = new TranslateAnimation(200 * Config.SCALE_FROM_HIGH, 0,
					-200 * Config.SCALE_FROM_HIGH, 0);
		}
		//animation.setInterpolator(new DecelerateInterpolator());
		animation.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)*(1.0f - input)*(1.0f - input));
			}
		});
		setDuration(500, animation);
		animation.setFillAfter(true);
		return animation;
		
	}
	
	
	
	// 长光 短光 移动动画
	public Animation getLightAnimation(int startOffset, boolean isDown,
			int count) {
		TranslateAnimation anim = null;
		if (isDown) {
			anim = new TranslateAnimation(Config.screenWidth, -276
					* Config.SCALE_FROM_HIGH, 0, 0);
		} else {
			anim = new TranslateAnimation(-276 * Config.SCALE_FROM_HIGH,
					Config.screenWidth, 0, 0);
		}
		setDuration(80, anim);
		
		anim.setRepeatCount(count);
		anim.setRepeatMode(Animation.RESTART);
		anim.setFillAfter(true);
		if (startOffset != 0) {
			anim.setStartOffset(startOffset);
		}
		return anim;
	}

	// 技能图标 缩小 变不透明
	public Animation skillImageAppear() {
		AnimationSet appear = new AnimationSet(true);
		ScaleAnimation zoomOut = new ScaleAnimation(1.5f, 1f, 1.5f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
		appear.addAnimation(zoomOut);
		appear.addAnimation(alpha);
		setDuration(200, appear);
		appear.setFillAfter(true);
		return appear;
	}

	// 显示技能 放大 同时变成不透明
	public Animation skillBgAppear() {
		AnimationSet appear = new AnimationSet(true);
		ScaleAnimation zoomOut = new ScaleAnimation(0.66f, 1f, 0.66f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
		appear.addAnimation(zoomOut);
		appear.addAnimation(alpha);
		setDuration(200, appear);
		return appear;
	}

	public Animation skillRotate() {
		Animation rotate = new RotateAnimation(0, -60,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(500, rotate);
		// rotate.setFillAfter(true);
		return rotate;
	}

	// 技能消失
	public AnimationSet skillDisappearNew() {
		// info
		AnimationSet disappear = new AnimationSet(true);
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, -15
				* Config.SCALE_FROM_HIGH);
		AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
		disappear.addAnimation(ta);
		disappear.addAnimation(alpha);
		setDuration(200, disappear);
		disappear.setFillAfter(true);
		return disappear;
	}

	// 空动画，仅用于延时
	public TranslateAnimation getNullAnimSkillName() {
		TranslateAnimation nullAnim = new TranslateAnimation(0, 0, 0, 0);
		setDuration(100, nullAnim);
		return nullAnim;
	}

	public static TranslateAnimation groupHurt() {
		TranslateAnimation move = new TranslateAnimation(0, 0, 0, -45*Config.SCALE_FROM_HIGH);
		move.setDuration(800);
		move.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input) * (1.0f - input)
						* (1.0f - input));
			}
		});
		return move;
	}

	// 盾兵回退动画
	public Animation shieldRollback(boolean isDown, int dis, int dis_Situ) {
		AnimationSet Rollback = new AnimationSet(true);
		TranslateAnimation move = null;
		if (isDown) {
			move = new TranslateAnimation(0, -dis, 0, +dis);
		} else {
			move = new TranslateAnimation(0, dis, 0, -dis);
		}
		setDuration(80, move);

		TranslateAnimation moveSitu = null;
		if (isDown) {
			moveSitu = new TranslateAnimation(0, -dis_Situ, 0, dis_Situ);
		} else {
			moveSitu = new TranslateAnimation(0, dis_Situ, 0, -dis_Situ);
		}
		setDuration(160, moveSitu);
		moveSitu.setStartOffset(80);

		Rollback.addAnimation(move);
		Rollback.addAnimation(moveSitu);
		return Rollback;
	}

	public TranslateAnimation getHandleBufAnim(boolean isDown) {
		TranslateAnimation handleBuf = null;
		if (isDown) {
			handleBuf = new TranslateAnimation(0, 0, 0,
					65 * Config.SCALE_FROM_HIGH);
		} else {
			handleBuf = new TranslateAnimation(0, 0, 0, -65
					* Config.SCALE_FROM_HIGH);
		}
		setDuration(600, handleBuf);
		// handleBuf.setInterpolator(new AccelerateDecelerateInterpolator());
		handleBuf.setInterpolator(new AccelerateInterpolator() {
			@Override
			public float getInterpolation(float input) {
				return input * input * input * input * input * input;
			}
		});
		// nullAnim.setFillAfter(true);
		return handleBuf;
	}

	public static Animation groupHurtEx(boolean isDown) {
		AnimationSet flyNum = new AnimationSet(true);
		TranslateAnimation move = null;
		if (isDown) {
			move = new TranslateAnimation(0, 0, 0, -65 * Config.SCALE_FROM_HIGH);
		} else {
			move = new TranslateAnimation(0, 0, 0, -65 * Config.SCALE_FROM_HIGH);
		}
		setDuration(1000, move);

		AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
		alpha.setStartOffset(500);
		setDuration(500, move);
		flyNum.addAnimation(move);
		flyNum.addAnimation(alpha);
		flyNum.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input) * (1.0f - input)
						* (1.0f - input));
			}
		});
		flyNum.setDuration(1000);
		return flyNum;
	}

	public Animation getAttack(boolean isDown, int dis, int time1, int time2) {
		AnimationSet attack = new AnimationSet(false);
		TranslateAnimation rush = null;
		if (isDown) {
			rush = new TranslateAnimation(0, dis, 0, -dis);
		} else {
			rush = new TranslateAnimation(0, -dis, 0, dis);
		}
		//rush.setInterpolator(new LinearInterpolator());
		rush.setInterpolator(new AccelerateDecelerateInterpolator());
		setDuration(time1, rush);

		TranslateAnimation bounce = null;
		if (isDown) {
			bounce = new TranslateAnimation(0, -dis, 0, dis);
		} else {
			bounce = new TranslateAnimation(0, dis, 0, -dis);
		}
		setDuration(time2, bounce);
		bounce.setStartOffset(time1 + 40);

		attack.addAnimation(rush);
		attack.addAnimation(bounce);
		return attack;
	}

	public static Animation largeOut(float fXscale, float tXScale,
			float fYscale, float tYScale, int during) {
		ScaleAnimation sa = new ScaleAnimation(fXscale, tXScale, fYscale,
				tYScale, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		setDuration(during, sa);
		sa.setFillAfter(true);
		return sa;
	}

	public static Animation nameLargeApha(float fXscale, float tXScale,
			float fYscale, float tYScale, float fAlpha, float tAlpha) {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(fXscale, tXScale, fYscale,
				tYScale, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		setDuration(300, sa);
		set.addAnimation(sa);

		AlphaAnimation alpha = new AlphaAnimation(fAlpha, tAlpha);
		setDuration(300, alpha);
		set.addAnimation(alpha);
		set.setFillAfter(true);
		return set;
	}

	public static Animation effectLeftRotate() {
		AnimationSet left = new AnimationSet(true);
		Animation rotate = null;
		rotate = new RotateAnimation(30, -5, Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF, 1f);
		setDuration(60, rotate);
		// rotate.setFillAfter(true);

		Animation trans = null;
		trans = new TranslateAnimation(0, -70 * Config.SCALE_FROM_HIGH, 0, -10
				* Config.SCALE_FROM_HIGH);
		setDuration(60, trans);
		left.addAnimation(rotate);
		left.addAnimation(trans);
		left.setFillAfter(true);
		left.setInterpolator(new AccelerateInterpolator());
		return left;
	}

	public static Animation effectRightRotate() {
		AnimationSet right = new AnimationSet(true);
		Animation rotate = null;
		rotate = new RotateAnimation(-30, 5, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 1f);
		setDuration(60, rotate);
		// rotate.setFillAfter(true);

		Animation trans = null;
		trans = new TranslateAnimation(0, 70 * Config.SCALE_FROM_HIGH, 0, -10
				* Config.SCALE_FROM_HIGH);
		setDuration(60, trans);
		// rotate.setFillAfter(true);

		right.addAnimation(rotate);
		right.addAnimation(trans);
		right.setFillAfter(true);
		right.setInterpolator(new AccelerateInterpolator());
		return right;
	}

	// 空动画，仅用于延时
	public static TranslateAnimation getDealy(int time, int startOffset) {
		TranslateAnimation delay = new TranslateAnimation(0, 0, 0, 0);
		setDuration(time, delay);
		delay.setFillAfter(true);
		delay.setStartOffset(startOffset);
		return delay;
	}

	public static Animation lightEffectLargeOut() {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(0.2f, 1f, 0.2f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// sa.setFillAfter(true);
		setDuration(300, sa);
		set.addAnimation(sa);

		Animation rotate = null;
		rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		setDuration(1000, rotate);
		rotate.setStartOffset(300);
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setRepeatMode(Animation.RESTART);
		set.addAnimation(rotate);
		return set;
	}

	public static Animation effectLargeOut() {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(0.1f, 1f, 0.1f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		setDuration(500, sa);
		set.addAnimation(sa);

		AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.8f);
		setDuration(500, alpha);
		set.addAnimation(alpha);
		return sa;
	}

	public static Animation lightEffectRotate() {
		Animation rotate = null;
		rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		setDuration(4500, rotate);
		rotate.setInterpolator(new LinearInterpolator());
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setRepeatMode(Animation.RESTART);
		return rotate;
	}

	public static Animation rollingLightAnim() {
		Animation rotate = null;
		rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		setDuration(3000, rotate);
		rotate.setInterpolator(new DecelerateInterpolator());
		return rotate;
	}

	public static Animation battleOverShadowRotate() {
		Animation rotate = null;
		rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		setDuration(300, rotate);
		rotate.setRepeatCount(3);
		rotate.setRepeatMode(Animation.RESTART);
		return rotate;
	}

	public static Animation getLightBlink(boolean isDown, int dex) {
		AnimationSet blink = new AnimationSet(true);
		TranslateAnimation rush = null;

		rush = new TranslateAnimation(0, 0, 0, 20 * Config.SCALE_FROM_HIGH);
		setDuration(150, rush);

		TranslateAnimation bounce = null;
		{
			bounce = new TranslateAnimation(0, 0, 0, -dex + 20
					* Config.SCALE_FROM_HIGH);
		}
		setDuration(450, bounce);
		bounce.setStartOffset(150);

		AlphaAnimation ta = new AlphaAnimation(1f, 0f);
		setDuration(200, ta);
		ta.setStartOffset(400);

		blink.addAnimation(rush);
		blink.addAnimation(bounce);
		blink.addAnimation(ta);
		// blink.setDuration(600);

		blink.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0f - (1.0f - input) * (1.0f - input)
						* (1.0f - input) * (1.0f - input));
			}
		});
		return blink;
	}
	
	public static Animation getAwardTip()
	{
		Animation rotate = null;
		rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		setDuration(800, rotate);
		rotate.setInterpolator(new LinearInterpolator());
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setRepeatMode(Animation.RESTART);
		return rotate;
	}

}
