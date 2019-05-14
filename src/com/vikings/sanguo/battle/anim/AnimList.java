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

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.battle.anim.BaseDrawableAnim.AnimationDrawableListen;
import com.vikings.sanguo.battle.anim.BaseDrawableAnim.FrameBeatListener;
import com.vikings.sanguo.utils.ListUtil;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AnimList
{
	protected List<List<Anim>> animList = new ArrayList<List<Anim>>();
	protected int curAnimIdx = 0; // 当前动画索引

	private int endIndex = 0; // 时间最长的动画索引
	
	private final int MAX_EXEC_TIME = 4000;
	//对超时时间进行监听，如果超过秒还没有执行完 自动进入下一组
//	private  CountDownTimer mCountDownTimer = new CountDownTimer(MAX_EXEC_TIME, 1000)
//	{		
//		@Override
//		public void onTick(long millisUntilFinished)
//		{
//			
//		}
//		
//		@Override
//		public void onFinish()
//		{
//			Log.d("AnimList", "动画到达超时时间，自动进入下一组");
//			//animationEnd();			
//		}
//	};

	// 播放到最后的那个动画的listener 决定是否进下一组

	public void play()
	{
		if (animList.size() <= 0) // || curAnimIdx >= animList.size()
			return;
		curAnimIdx = 0;
		startCurAnim(curAnimIdx);
	}

	// 针对法术类攻击 单独处理

	private void startCurAnim(int cnt)
	{
		if (cnt < animList.size())
		{
			List<Anim> curAnim = animList.get(cnt);
			// 得到结束时间最长的动画
			boolean isBeat = false; // 是否到攻击帧
			// 是否有攻击
			for (int index = 0; index < curAnim.size(); index++)
			{
				Anim anim = curAnim.get(index);
				if (anim instanceof BaseAnim)
				{
					BaseAnim baseAnim = (BaseAnim) anim;
					if (baseAnim.getBeat() == true)
					{
						isBeat = true;
						break;
					}
				}
			}
			// 被攻击
			if (isBeat)
			{
				if (!ListUtil.isNull(curAnim))
				{
					for (int i = 0; i < curAnim.size(); i++)
					{
						Anim anim = curAnim.get(i);
						if (anim instanceof BaseAnim)
						{
							BaseAnim baseAnim = (BaseAnim) anim;
							baseAnim.setListener(new listener(baseAnim, cnt));
							if (baseAnim.getBeat() == false && baseAnim.getFallNum() == false)
							{
								baseAnim.start();
							}
						} 
						else
						{
							BaseDrawableAnim drawableAnim = (BaseDrawableAnim) anim;
							drawableAnim.setListen(new drawListen(
									(BaseDrawableAnim) anim, cnt));
							drawableAnim.start();
							int hitFrame = drawableAnim.getHitFrame();
							drawableAnim.setFrameListener(new FrameListen(drawableAnim, cnt), hitFrame);

						}
					}
				}
				return;
			}

			if (!ListUtil.isNull(curAnim))
			{
				for (int i = 0; i < curAnim.size(); i++)
				{
					if (curAnim.get(i) instanceof BaseAnim)
					{
						((BaseAnim) curAnim.get(i)).setListener(new listener((BaseAnim) curAnim.get(i), cnt));
					}
					else
					{
						((BaseDrawableAnim) curAnim.get(i)).setListen(new drawListen((BaseDrawableAnim) curAnim.get(i), cnt));
					}
					curAnim.get(i).start();
				}
				//如果组里面  没有需要结束才到下一组的  直接跳到下一组
				
				boolean isNeedEnd = false;
				for(Anim anim :curAnim)
				{
					if(anim.getNeedEnd() == true)
					{
						isNeedEnd = true;
					}
				}
				
				if(isNeedEnd == false)
				{
					animationEnd();
				}
//				else
//				{
//					mCountDownTimer.start();
//				}
			}
		}
	}

	private Anim getCurAnim(int cnt)
	{
		if (cnt < animList.size() && !ListUtil.isNull(animList.get(cnt)))
			return animList.get(cnt).get(endIndex);
		return null;
	}

	protected void animationEnd()
	{ // Animation animation
		Anim curAnim = getCurAnim(curAnimIdx);
		if (null != curAnim)
		{
			startCurAnim(++curAnimIdx);
		}
	}

	protected void animationStart()
	{ // Animation animation
		Anim curAnim = getCurAnim(curAnimIdx);
		if (curAnim instanceof BaseAnim && null != curAnim)
		{
			((BaseAnim) curAnim).animationStart(); // animation
		}
	}

	protected boolean isEnd()
	{
		return (null == getCurAnim(curAnimIdx));
	}

	class drawListen implements AnimationDrawableListen
	{

		private BaseDrawableAnim anim;
		private int animIdx;

		public drawListen(BaseDrawableAnim anim, int curAnimIdx)
		{
			animIdx = curAnimIdx;
			this.anim = anim;
		}

		@Override
		public void onAnimationEnd()
		{
			// 对当前组所有的动画 查询是否还有没有结束的动画
			if (animIdx != curAnimIdx || animList == null
					|| animList.size() <= animIdx)
			{
				return;
			}

			List<Anim> curAnim = animList.get(animIdx);
			if (curAnim != null && curAnim.size() > 0)
			{
				for (Anim anim : curAnim)
				{
					if (anim != this.anim)
					{
						if (anim.isRunning()&& anim.getNeedEnd() == true)
						{
							this.anim.stop();
							this.anim.animationEnd();
							this.anim.setEnd(true);
							return;
						}
					}
				}
				for (Anim anim : curAnim)
				{
					if (anim != null && anim.getEnd() == false && anim.getNeedEnd() == true)
					{
						anim.stop();
						anim.animationEnd();
						anim.setEnd(true);
					}
				}		
				//mCountDownTimer.cancel();
				animationEnd();
			}
		}
	}
		
	private class listener implements AnimationListener
	{
		private BaseAnim anim;
		private int animIdx;

		public listener(BaseAnim anim, int curAnimIdx)
		{
			animIdx = curAnimIdx;
			this.anim = anim;
		}

		public void onAnimationStart(Animation animation)
		{
			if (null != anim)
				anim.animationStart();
		}

		public void onAnimationRepeat(Animation animation)
		{

		}

		public void onAnimationEnd(Animation animation)
		{
			if (animIdx != curAnimIdx || animList == null
					|| animList.size() <= animIdx)
			{
				return;
			}

			// 对当前组所有的动画 查询是否还有没有结束的动画
			List<Anim> curAnim = animList.get(animIdx);
			if (curAnim != null)
			{
				if (curAnim.size() == 1)
				{
					if (anim != null && anim.getEnd() == false && anim.getNeedEnd() == true)
					{
						this.anim.stop();
						this.anim.animationEnd();
					}
					animationEnd();
				} 
				else
				{
					for (Anim anim : curAnim)
					{
						if (anim != this.anim)
						{
							if (anim.isRunning() && anim.getNeedEnd() == true)
							{
								if (null != this.anim)
								{
									this.anim.stop();
									this.anim.animationEnd();
									this.anim.setEnd(true);
								}
								return;
							}
						}
					}

					for (Anim anim : curAnim)
					{
						if (anim != null && anim.getEnd() == false && anim.getNeedEnd() == true)
						{
							anim.stop();
							anim.animationEnd();
							anim.setEnd(true);
						}
					}
					//mCountDownTimer.cancel();
					animationEnd();
				}
			}
		}
	}

	public class FrameListen implements FrameBeatListener
	{
		private int animIdx;

		public FrameListen(BaseDrawableAnim drawableAnim, int animIdx)
		{
			this.animIdx = animIdx;
		}

		@Override
		public void beginBeat()
		{
			if (animIdx != curAnimIdx || animList == null
					|| animList.size() <= animIdx)
			{
				return;
			}

			List<Anim> curAnim = animList.get(animIdx);
			if (curAnim != null && curAnim.size() > 0)
			{
				for (Anim anim : curAnim)
				{
					if (anim instanceof BaseAnim)
					{
						BaseAnim baseAnim = (BaseAnim) anim;
						if (baseAnim.getBeat() == true&& baseAnim.hasStart() == false)
						{
							baseAnim.start();  
							baseAnim.setStart(true);
						}
						
						if (baseAnim.getFallNum() == true&& baseAnim.hasStart() == false)
						{	
							baseAnim.setDelay(200);// 200毫秒后执行
							baseAnim.start();  							
							baseAnim.setStart(true);
						}
					}
				}
			}
		}
	}
}