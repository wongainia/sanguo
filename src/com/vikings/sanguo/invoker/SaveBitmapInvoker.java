package com.vikings.sanguo.invoker;

import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;

public class SaveBitmapInvoker extends BackgroundInvoker
{
	private Bitmap b;
	private String fileName;
	
	public SaveBitmapInvoker(Bitmap b,String fileName)
	{
		this.b = b;
		this.fileName = fileName;
	}
	
	@Override
	protected void fire() throws GameException
	{
		try 
		{
			FileOutputStream out = new FileOutputStream(Config.getController()
					.getFileAccess()
					.readImage(fileName));
			b.compress(CompressFormat.PNG, 100, out);
			out.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onOK()
	{

	}
	
}