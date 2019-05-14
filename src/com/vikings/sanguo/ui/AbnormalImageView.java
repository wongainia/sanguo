package com.vikings.sanguo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AbnormalImageView extends ImageView
{
	private Drawable d;
	private AnimationDrawable animDrawable; 
	private boolean isRecyled = false; 
	
	private boolean isVeriMirror = false;  //垂直镜像
	private float scale = 1.0f;                   //放大比例
	private int rotate = 0;                         //旋转角度
    
	
	public AbnormalImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public AbnormalImageView(Context context)
	{
		super(context);
	}
	
	public void setDrawPara(boolean isVeriMirror,float scale,int rotate)
	{
		this.isVeriMirror = isVeriMirror;
		this.scale = scale;
		this.rotate = rotate;
	}
		
	@Override
	public void setImageDrawable(Drawable drawable)
	{
		if(drawable != null)
		{
			this.d = drawable;
		}
		super.setImageDrawable(drawable);
	}
	
	@Override
	public void setBackgroundDrawable(Drawable d)
	{	
		if(d != null && (isVeriMirror ||  scale != 1.0f || rotate != 0))
		{
			this.d = d;
			return;
		}
		super.setBackgroundDrawable(d);
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
			
//			if(d != null  && d instanceof AnimationDrawable)
//			{
//					
//				if(isVeriMirror ||  scale != 1.0f || rotate != 0)
//				{
//					canvas.save();
//					AnimationDrawable animationDrawable = (AnimationDrawable)d;
//					int width = 0;
//					int height = 0;
//					if(animationDrawable.getFrame(0) != null)
//					{	
//						width = animationDrawable.getFrame(0).getIntrinsicWidth();
//					    height = animationDrawable.getFrame(0).getIntrinsicHeight();
//					}
//					if(isVeriMirror)
//					{
//						canvas.scale(1, -1, width/2, height/2);
//					}
//					
//					if(scale != 1.0f)
//					{
//						canvas.scale(scale, scale);
//					}
//					
//					if(rotate != 0)
//					{
//						canvas.rotate(rotate);
//					}
//					d.draw(canvas);
//					canvas.restore();	
//					return;
//					}
//				}
//			if(d != null && (isVeriMirror ||  scale != 1.0f || rotate != 0))
//			{
//				if(d instanceof BitmapDrawable)
//				{
//					d.setFilterBitmap(true); 
//					BitmapDrawable drawable = (BitmapDrawable)d;
//					Bitmap bmp = drawable.getBitmap();
//					drawBitmap(canvas, bmp);
//				}
//			}
//			else
			{
				super.onDraw(canvas);
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void drawBitmap(Canvas canvas,Bitmap bitmap)
	{			
		if(bitmap == null || bitmap.isRecycled() == true)
		{
			return;
		}		
		Matrix matrix = new Matrix();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		if (isVeriMirror)
		{
			matrix.postScale(1, -1,width/2,height/2);
		}
		
		if (rotate > 0) 
		{
			if(rotate > 30)
			{
				rotate = rotate - 20;
			}
			matrix.postRotate(rotate, (float) width/2,height / 2);			
		}
	
		if (scale != 1.0f)
		{
			matrix.postScale(scale, scale);
		}				
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, matrix, paint);       		
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
		}
	}
		
	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		isRecyled = true;
		setBackgroundDrawable(null);
		if (d != null && d instanceof BitmapDrawable) 
		{
				recycleBitmapDrawable((BitmapDrawable) d);
				d = null;
		} 
		if (animDrawable != null  && animDrawable.getNumberOfFrames() > 0) 
		{
				((AnimationDrawable) d).stop();
				int frames = ((AnimationDrawable) d).getNumberOfFrames();
				for (int i = 0; i < frames; i++)
				{
					Drawable frameDrawable = ((AnimationDrawable) d).getFrame(i);
					if (frameDrawable instanceof BitmapDrawable)
						recycleBitmapDrawable((BitmapDrawable) frameDrawable);
				}
				animDrawable = null;
		}
	}
	
	private void recycleBitmapDrawable(BitmapDrawable drawable) 
	{
		Bitmap b = drawable.getBitmap();		
		if (b != null && !b.isRecycled())
		{
			//Log.d("recycleBitmapDrawable", ""+b + "width" +b.getWidth() );
			b.recycle();
			b = null;
		}
	}
}
