package com.vikings.sanguo.battle.anim;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.AbnormalImageView;
import com.vikings.sanguo.utils.ImageUtil;

public class HeroSkillAnim extends BaseDrawableAnim {		
	
	private final int FIRST_DURING = 250;//200;//200;	
	private final int HERO_FRAME_LENGTH = (int) ((189 + 10)*Config.SCALE_FROM_HIGH);
	
	private ViewGroup window;
	private int effectCount = 0;
	private boolean isAttack;
	private boolean isDefend;
	private boolean isGain; //是对自己  还是对对方
	
	private int move_dx = 0;
	private int move_dy = 0; //移动时候的偏移
	
	private int final_dx = 0;
	private int final_dy = 0;
	
	private String attackImg = "";
	private String finalImg = "";
	
	private final int SCALE_X = 180;
	private final int SCALE_Y = 180;
	
	private int mapGunWidth = 0;
	private int mapGunHeigh = 0;
	
	private final int amyFrameWidth = (int) (75*Config.SCALE_FROM_HIGH);  //军队框的高度
	
	private List<TargetInfo> amyPosition = new ArrayList<TargetInfo>();
	
	private final float initRate = 0.6f;     
	
	private Handler handler = new Handler() 
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				handleAnimation();
				break;
			}
		};
	};

	public void setView(ViewGroup vg) {
		this.window = vg;
	}

	private TargetInfo getPosition(View v, boolean isLeft) {
		if (v == null) {
			return null;
		}
		int[] location = new int[2];
		v.getLocationOnScreen(location);

		Point point = new Point();
		point.x = location[0] /* + v.getWidth()/2 */;
		point.y = (int) (location[1]/* + v.getHeight()/2 */);
		if (isLeft) 
		{
			if (point.x + v.getWidth() < 0 || point.x > (Config.screenWidth - HERO_FRAME_LENGTH))
			{
				return null;
			}
		} 
		else 
		{
			if (point.x > Config.screenWidth || point.x + v.getWidth() < HERO_FRAME_LENGTH) 
			{
				return null;
			}
		}
		TargetInfo targetInfo = new TargetInfo();
		targetInfo.point = point;
		targetInfo.v = v;
		return targetInfo;
	}

	private void getPosition(boolean isLeft) {
		ViewGroup upAmy = (ViewGroup) window.findViewById(R.id.upAmy);
		ViewGroup downAmy = (ViewGroup) window.findViewById(R.id.downAmy);

		ViewGroup armyLayout = (isLeft ? downAmy : upAmy);

		int childCount = armyLayout.getChildCount();
		if (!isLeft) 
		{
			for (int i = 0; i < childCount; i++) 
			{
				View v = armyLayout.getChildAt(i);
				TargetInfo point = getPosition(v, isLeft);
				if (point != null) {
					amyPosition.add(point);
				}
			}
		} 
		else
		{
			for (int i = childCount - 1; i >= 0; i--) 
			{
				View v = armyLayout.getChildAt(i);
				TargetInfo point = getPosition(v, isLeft);
				if (point != null) 
				{
					amyPosition.add(point);
				}
			}
		}
	}

	public HeroSkillAnim(boolean isAttack,boolean isDefend, ViewGroup view, boolean needGone,boolean isGain) {
		super(view, null, needGone);
		this.window = view;
		this.isAttack = isAttack;		
		this.isDefend = isDefend;
		this.isGain = isGain;
				
		if(isGain)
		{
			finalImg = "hero_skill_star";
			attackImg = "hero_add_hp";				
		}
		else
		{
			finalImg = "explosion";
			attackImg = "hero_drop_hp";
		}
		Drawable d = Config.getController().getDrawable(attackImg);
		Drawable final_d = Config.getController().getDrawable(finalImg);
		{
			mapGunWidth = (int) (d.getIntrinsicWidth()*(float)SCALE_X*initRate/100);
			mapGunHeigh = (int) (d.getIntrinsicHeight()*(float)SCALE_Y*initRate/100);
			{
				move_dx = (int) ((mapGunWidth - amyFrameWidth)/2);
				if(isDefend)  //当自己是被打时候
				{
					move_dy = (int) (mapGunHeigh - amyFrameWidth);
				}
				
				if(final_d != null)
				{
					final_dx = (int) ((final_d.getIntrinsicWidth() - amyFrameWidth)/2);
					final_dy = (int) ((final_d.getIntrinsicHeight() - amyFrameWidth)/2);
				}
			}		
		}
	}
	
	public void handleAnimation() {
		if (effectCount >= amyPosition.size()) 
		{
			return;
		}
		//286  英雄的高度作为基准
		int d_height = (int) (286 * Config.SCALE_FROM_HIGH);
		int left = Config.screenWidth/2;
		int top;
		//地图炮的起始点    如果打对方  起始点在自己这方
		if (isAttack) 
		{
			int heroBase = Config.screenHeight - (int) (135 * Config.SCALE_FROM_HIGH)- d_height;				
			top = (int) (heroBase - 70 * Config.SCALE_FROM_HIGH);
		} 
		else 
		{
			int heroBase = (int) (135 * Config.SCALE_FROM_HIGH);
			top = (int) (heroBase + d_height * 2 / 3 - 40 * Config.SCALE_FROM_HIGH);
		}
		Drawable d = Config.getController().getDrawable(attackImg);
		if(d != null)
		{
			left = left - mapGunWidth/2;
		}
		TargetInfo targetPoint = amyPosition.get(amyPosition.size()- effectCount - 1);
		final Point point = targetPoint.point;
		point.x = point.x - move_dx;
		point.y = point.y - move_dy;

		float dy = Math.abs(point.y - top);
		float dx = Math.abs(point.x - left);

		double tanAngle = Math.atan2(dx, dy);
		int degree = 0;
		//自己是防守方
		if (isDefend)    
		{
			if (point.x - left > 0) 
			{
				degree = (int) (-tanAngle * 180 / Math.PI);
			} 
			else 
			{
				degree = (int) (tanAngle * 180 / Math.PI);
			}			
		} 
		else 
		{
			if (point.x - left >= 0) 
			{
				degree = (int) (tanAngle * 180 / Math.PI);
			}
			else 
			{
				degree = (int) (-tanAngle * 180 / Math.PI);
			}
		}
		if (degree < 0) 
		{
			degree = 360 + degree;
		}
		
		int isVeriMirror = isDefend ? 0: 1;	
		final AbnormalImageView skill = new AbnormalImageView(Config.getController().getUIContext());

		final Drawable attackDrawable = ImageUtil.getMirrorRotateBitmapDrawable(attackImg, 0, isVeriMirror, degree, SCALE_X, SCALE_Y, false);
		//skill.setBackgroundDrawable(attackDrawable);
		skill.setVisibility(View.INVISIBLE);
		skill.setTag(new Integer(effectCount));
		
		FrameLayout.LayoutParams lp = new LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.gravity = Gravity.TOP;
		window.addView(skill, lp);

		Animation light = getLightAnim(0, point.x - left, 0, point.y - top, 0);
		light.setDuration(650/*900*/);

		light.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				skill.setBackgroundDrawable(attackDrawable);
				skill.setVisibility(View.VISIBLE);
//				handler.post(new Runnable() {
//					@Override
//					public void run() {
//						animationBasis.start();
//					}
//				});
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Drawable d = skill.getBackground();
				if (d != null && d instanceof AnimationDrawable) {
					((AnimationDrawable) d).stop();
					d = null;
				}
				skill.clearAnimation();
				skill.setVisibility(View.INVISIBLE);
				skill.setBackgroundDrawable(null);
				skill.setDrawPara(false,1.0f,0);
			
				final Drawable default_pic = Config.getController().getDrawable(finalImg);
				
				int width = 0;
				int height = 0;
				if(default_pic != null)
				{
					width = default_pic.getIntrinsicWidth();
					height = default_pic.getIntrinsicHeight();
				}
				int left = point.x + move_dx- final_dx;
				int top = point.y  + move_dy - final_dy;
	
				FrameLayout.LayoutParams lp = (LayoutParams) skill.getLayoutParams();
				lp.width = LayoutParams.WRAP_CONTENT;
				lp.height = LayoutParams.WRAP_CONTENT;
				lp.topMargin =top ;//point.y + final_dy;
				lp.leftMargin = left;//point.x + final_dx;
				
				if(width > 0 && height > 0)
				{
					lp.width = default_pic.getIntrinsicWidth();
					lp.height = default_pic.getIntrinsicHeight();					
				}
				lp.gravity = Gravity.TOP;
				skill.setLayoutParams(lp);
				if(width > 0 && height > 0)
				{
					skill.layout(left, top, left+width, top+height);
				}
							
				//skill.setBackgroundDrawable(default_pic);
				Animation stay = AnimPool.getNullAnim(200);
				skill.setVisibility(View.VISIBLE);
				skill.setBackgroundDrawable(default_pic);
				skill.startAnimation(stay);
				final Integer tag = (Integer) skill.getTag();
				stay.setAnimationListener(new AnimationListener()
				{					
					@Override
					public void onAnimationStart(Animation arg0)
					{
									
					}					
					@Override
					public void onAnimationRepeat(Animation arg0)
					{
						
					}
					
					@Override
					public void onAnimationEnd(Animation arg0)
					{	
						//window.removeView(skill);
						skill.setBackgroundDrawable(null);
						skill.clearAnimation();
						skill.setVisibility(View.GONE);						
						//window.removeView(skill);
						if (tag == amyPosition.size() - 1) {
							if (drawableListen != null) {
								handler.removeMessages(0);
								handler.removeMessages(1);
								drawableListen.onAnimationEnd();
								return;
							}
						}						
					}
				});
			}
		});
		skill.startAnimation(light);
		int during = getBlinkDuring(effectCount);
		handler.sendEmptyMessageDelayed(2, during);
		effectCount++;
	}

	private int getBlinkDuring(int index) // 时间间隔
	{		
		int during;
		if (index == 0) {
			during = FIRST_DURING;
		} else if (index == 1) {
			during = FIRST_DURING * 3 / 4;
		} else {
			during = FIRST_DURING * 3 / 4 - (index - 1) * 5;
		}

		if (during <= 0) {
			during = 10;
		}
		return during;
	}

	@Override
	public void start() {
		getPosition(isDefend);
		super.start();
		// doAnimation();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				handleAnimation();
			}
		}, 150);
	}

	@Override
	public void animationEnd() {

	}

	private AnimationDrawableListen drawableListen;

	public void setListen(AnimationDrawableListen listen) {
		drawableListen = listen;
	}

	class TargetInfo {
		Point point;
		View v;
		int frameIndex;
		int dx = 0;
		int dy = 0; // 每帧移动的距离
	}
	
	private Animation getLightAnim(float fromX, float toX, float fromY,
			float toY, int startOffset) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation trans = new TranslateAnimation(fromX, toX,
				fromY, toY);
		if(startOffset != 0)
		{
			trans.setStartOffset(startOffset);
		}
		trans.setFillAfter(true);		
		
		ScaleAnimation large = new ScaleAnimation(initRate, 1, initRate, 1);
		if(startOffset != 0)
		{
			large.setStartOffset(startOffset);
		}
		large.setFillAfter(true);		
		set.addAnimation(trans);
		set.addAnimation(large);		
		set.setInterpolator(new AccelerateInterpolator(1));
		return set;
	}
}
