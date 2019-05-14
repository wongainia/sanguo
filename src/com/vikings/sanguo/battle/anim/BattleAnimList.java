/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-11-9
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.battle.anim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.graphics.Bitmap;

import com.vikings.sanguo.cache.BitmapCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.BattleResultAnimTip;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

public class BattleAnimList extends AnimList
{
	private BattleDriver battleDriver;
	private CallBack callBackAfterAnim;
	private CallBack endCB;
	private boolean isSkip = false;

	public BattleAnimList(BattleDriver battleDriver,
			CallBack callBackAfterAnim, CallBack endCB)
	{ // , CallBack cb
		this.battleDriver = battleDriver;
		this.callBackAfterAnim = callBackAfterAnim;
		this.endCB = endCB;
	}

	private void add(ArrayList<List<Anim>> list)
	{ // ArrayList<BaseAnim> list
		animList.clear();
		animList.addAll(list);
	}

	@Override
	public void play()
	{
		ArrayList<List<Anim>> list = battleDriver.createAnim(); // BaseAnim
		if (battleDriver.isAnimOver())
		{
			showResult();
			endCB.onCall();
		} else
		{
			if (!ListUtil.isNull(list))
			{
				add(list);
				super.play();
			} else
				play();
		}
	}

	//回收掉所有bitmap
	public void showResult()
	{
		if (battleDriver.getBlic().isMyBattle())
		{
			new BattleResultAnimTip(battleDriver.getBlic().isMeWin()).show();
			if (battleDriver.getBlic().isMeWin())
				SoundMgr.play("battle_win.ogg");
			else
				SoundMgr.play("battle_lose.ogg");		
		} 
		else
		{
			if (null != endCB)
				endCB.onCall();
			Config.getController().goBack();
		}		
	}
    
	private void releseMemory()
	{
		BattleLogInfoClient blic = battleDriver.getBlic();
		if(blic == null)
		{
			return;
		}
		HashSet<String> imgs = blic.getBattleImgs();
		if(imgs != null && imgs.size() > 0) 
		{
			BitmapCache bitmapCache = Config.getController().getBitmapCache();
			for(String img:imgs)
			{			
				if(StringUtil.isNull(img) == false)
				{									
					Bitmap bmp = bitmapCache.get(img);
					if(bmp != null && bmp.isRecycled() == false)
					{
						//bmp.recycle();
						//bmp = null;
						bitmapCache.remove(img);
					}				
				}
			}
		}
	}
	
	//结束回收掉战斗中用的 bitmap
	@Override
	protected void animationEnd()
	{ // Animation animation
		super.animationEnd(); // animation
		if (isEnd() && (!isSkip))
		{
			play();
		}
		else if (isSkip)
		{
			animList.clear();
		}
	}

	public void setSkip()
	{
		this.isSkip = true;
	}
}
