package com.vikings.sanguo.ui;

import com.vikings.sanguo.config.Config;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class AbnormalFrameLayout extends FrameLayout
{
	private RecyleResource mRecyleResource;
	private boolean isRecyled = false;  //判断是否回收了    如果回收了  不执行ondraw  draw方法
	
	public abstract interface RecyleResource
	{
		public void doRecyle();
	}
	public AbnormalFrameLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setWillNotDraw(false);//必须
	}

	public AbnormalFrameLayout(Context context)
	{
		super(context);
		this.setWillNotDraw(false);//必须
	}
	
	public void setRecyleResourceListener(RecyleResource recyleResource)
	{
		this.mRecyleResource = recyleResource;
	}
	
	@Override  
	protected void onDraw(Canvas canvas)
	{
		try
		{
			if(isRecyled == true)
			{
				return;
			}
			super.onDraw(canvas);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		try
		{
			if(isRecyled == true)
			{
				return;  
			}
			super.draw(canvas);
		}
		catch(Exception e)
		{
			e.printStackTrace();  
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		try
		{
			if(isRecyled == true)
			{
				return;  
			}
			super.dispatchDraw(canvas);
		}
		catch(Exception e)
		{
			e.printStackTrace();  
			Config.getController().goBack();
		}
	}
	
	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		try
		{
			if(mRecyleResource != null)
			{
				isRecyled = true;
				mRecyleResource.doRecyle();			
				mRecyleResource = null;
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
