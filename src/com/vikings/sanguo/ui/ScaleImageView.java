package com.vikings.sanguo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ScaleImageView extends ImageView
{
	private Drawable d;
	private float scale = 1.0f;
	
	private boolean isMirror = false;
	
	public ScaleImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ScaleImageView(Context context)
	{
		super(context);
	}
		
	@Override
	public void setBackgroundDrawable(Drawable d)
	{	
		if(d != null && isMirror)
		{
			this.d = d;
			return;
		}
		super.setBackgroundDrawable(d);
	}
	
	public void setScale(float scale)
	{
		this.scale = scale;
	}
	
	//是否镜像
	public void setMirror(boolean isMirror)
	{
		this.isMirror = isMirror;
	}
	
	@Override  
	protected void onDraw(Canvas canvas)
	{
		try
		{
			if(d != null && d instanceof BitmapDrawable && isMirror == true)
			{
				d.setFilterBitmap(true);
				Bitmap bmp = ((BitmapDrawable)d).getBitmap();
				if(bmp != null && bmp.isRecycled() == false)
				{
					Matrix matrix = new Matrix();										
					int width = bmp.getWidth();
					int height = bmp.getHeight();
					matrix.postScale(-1, 1,width/2,height/2);
					
					matrix.postScale(scale,scale);										
					Paint paint = new Paint();
					paint.setFilterBitmap(true);
					paint.setAntiAlias(true);
					canvas.drawBitmap(bmp, matrix, paint);
					return;
				}
			}
			super.onDraw(canvas);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
}
