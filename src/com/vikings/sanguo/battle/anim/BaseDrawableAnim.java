package com.vikings.sanguo.battle.anim;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.battle.anim.DrawableAnimationBasis.AnimationDrawableFinish;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.StringUtil;

public abstract class BaseDrawableAnim extends Anim
{
	protected View view;
	protected DrawableAnimationBasis anim;
	protected boolean needGone; // 部分动画在结束后，需要将view设置为不可见
	protected String soundName;
	protected boolean isLast = false;

	private long start_time;
	private int delay = 0;
	
	private boolean isUseStart = false;

	private AnimationDrawableListen drawableListen;
	private FrameBeatListener mBeatListener;
	
	private int hitFrame = 1;           //击中目标帧
	
	public BaseDrawableAnim(View view, DrawableAnimationBasis anim,
			boolean needGone)
	{
		this.view = view;
		this.anim = anim;
		this.needGone = needGone;
	}
	
	public BaseDrawableAnim(ViewGroup view, DrawableAnimationBasis anim,
			boolean needGone)
	{
		this.view = view;
		this.anim = anim;
		this.needGone = needGone;
	}
	public void setListen(AnimationDrawableListen listen)
	{
		drawableListen = listen;
	}
	
	public void setFrameListener(FrameBeatListener mBeatListener,int frameIndex)
	{
		this.anim.setFrameListener(mBeatListener,frameIndex);
	}

	public interface AnimationDrawableListen
	{
		public void onAnimationEnd();
	}
	
	public interface FrameBeatListener
	{
		public void beginBeat();
	}
	
	// 启动动画前，确保view可见
	public void start()
	{
		prepare();
		
//		if(isUseStart)
//		{
//			start();
//		}
		
		if (null != anim)
		{
			if(delay != 0)
			{
				Config.getController().getHandler().postAtTime(new Runnable()
				{					
					@Override
					public void run()
					{
						view.setVisibility(View.VISIBLE);
						anim.setAnimFinishListen(drawableListener);
						start_time = System.currentTimeMillis();
						//playSound();
					}
				}, delay);
			}
			else
			{
				view.setVisibility(View.VISIBLE);
				anim.setAnimFinishListen(drawableListener);
				start_time = System.currentTimeMillis();
				//playSound();
			}
		}
	}

	protected void setAnim(DrawableAnimationBasis anim)
	{
		this.anim = anim;
	}
	
	//delay
	protected void setDelay(int delay)
	{
		this.delay = delay;
	}
	
	private void changeAnimStatus()
	{
		if (null != anim )
		{
			anim.stop();
			anim = null;
		}
		view.clearAnimation();		
		if (needGone)
		{
			view.setVisibility(View.INVISIBLE);			
		}
		view.setBackgroundDrawable(null);
	}

	protected void stop()
	{		
		animationEnd();
	}

	public abstract void animationEnd();

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
	
	private AnimationDrawableFinish drawableListener = new AnimationDrawableFinish()
	{
		@Override
		public void onAnimationFilish()
		{
			changeAnimStatus();
			if (drawableListen != null)
			{
				drawableListen.onAnimationEnd();
			}		
		}
	};
	
	public void onAnimationEnd()
	{
		if (drawableListen != null)
		{
			drawableListen.onAnimationEnd();
		}		
	}
	
	//动画结束的时间
	public int getTotalTime()
	{
		int during = 0;
		if(anim != null)
		{
			for(int i =0; i < anim.getNumberOfFrames(); i++)
			{
				during += anim.getDuration(i);
			}
		}
		return during;
	}

	public  boolean isRunning()
	{
		if (anim != null)
			return anim.isRunning();
		return false;
	}
	
	public void setHitFrame(int hitFrame)
	{
		this.hitFrame = hitFrame;
	}
	
	public int getHitFrame()
	{
		return this.hitFrame;
	}
	
	public void setUseStart(boolean setUseStart)
	{
		this.isUseStart = setUseStart;
	}	
	
	public DrawableAnimationBasis getAnimationDrawable()
	{
		return anim;
	}
	
}
