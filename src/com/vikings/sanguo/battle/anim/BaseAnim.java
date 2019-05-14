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

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.StringUtil;

public class BaseAnim extends Anim
{
	protected View view;
	protected Animation anim;
	protected boolean needGone; // 部分动画在结束后，需要将view设置为不可见
	protected String soundName;
	protected boolean isLast = false;
	private long start_time;
	private boolean  isBeat = false;   //是否是法术攻击的受击  法术攻击要判断是第几帧受击
	private boolean hasStart = false;
	private boolean isFallNum = false;
	
	private int delay = 0;
	
	public BaseAnim(View view, Animation anim, boolean needGone)
	{
		this.view = view;
		this.anim = anim;
		this.needGone = needGone;
	}

	// 启动动画前，确保view可见
	protected void start()
	{
		start_time = System.currentTimeMillis();		
		if(delay == 0)
		{
			prepare();		
			if (null != anim)
			{
				view.setVisibility(View.VISIBLE);
				view.startAnimation(anim);
			}
		}
		else
		{
			Config.getController().getHandler().postDelayed(new Runnable()
			{				
				@Override
				public void run()
				{
					prepare();		
					if (null != anim)
					{
						view.setVisibility(View.VISIBLE);
						view.startAnimation(anim);						
					}					
				}
			}, delay);
		}
	}

	protected void setAnim(Animation anim)
	{
		this.anim = anim;
	}

	protected void stop()
	{
//		Log.d("battleDriver", "位移动画所用时间:"
//				+ (System.currentTimeMillis() - start_time) + "---VIEW:" + (view.getTag()==null ? view:view.getTag()));
		if (null != anim)
		{
			anim.setAnimationListener(null);
		}
		// view.clearAnimation();
		if (needGone)
		{
			view.clearAnimation();
			view.setVisibility(View.INVISIBLE);
		}
	}

	protected void setListener(AnimationListener listener)
	{
		if (null != anim)
		{
			anim.setAnimationListener(listener);
		}
	}

	protected void animationEnd()
	{
	}; 

	public View getView()
	{
		return view;
	}

	public void setView(View view)
	{
		this.view = view;
	}

	public void setSoundName(String soundName)
	{
		if (!StringUtil.isNull(soundName) && SoundMgr.loadSound(soundName))
		{
			this.soundName = soundName;
		}
	}

	public void playSound()
	{
		if (StringUtil.isNull(soundName))
			return;
		SoundMgr.play(soundName);
	}

	protected void animationStart()
	{ // Animation animation
		playSound();
	};

	protected void prepare()
	{
	}

	public void setLast(boolean isLast)
	{
		this.isLast = isLast;
	}

	public boolean isLast()
	{
		return isLast;
	}
	
	//动画结束的时间
	public int getTotalTime()
	{
		int during = 0;
		if (anim != null)
		{
			during = (int) (anim.getStartOffset() + anim.getDuration());
		}
		return during;
	}

	@Override
	protected boolean isRunning()
	{		
		return !anim.hasEnded();
	}
	
	public boolean hasStart()
	{
		return hasStart;
	}
	
	public void setStart(boolean isStart)
	{
		this.hasStart = isStart;
	}
	
	public void setBeat(boolean isBeat)
	{
		this.isBeat = isBeat;
	}
	
	public boolean getBeat()
	{
		return this.isBeat;
	}	
	
	//飘数字  和血变化  
	
	public void setFallNum(boolean isFallNum)
	{
		this.isFallNum = isFallNum;
	}
	
	public boolean getFallNum()
	{
		return this.isFallNum;
	}	
		
	public void setDelay(int delay)
	{
		this.delay = delay;
	}
}
