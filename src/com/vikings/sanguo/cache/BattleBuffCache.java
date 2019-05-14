/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-21 下午8:11:52
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import android.graphics.drawable.Drawable;
import android.util.Log;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleBuff;
import com.vikings.sanguo.thread.NullImgCallBack;

public class BattleBuffCache extends LazyLoadCache {

	@Override
	public String getName() {
		return "battle_buff.csv";
	}

	@Override
	public Object getKey(Object obj) {
		return ((BattleBuff) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return BattleBuff.fromString(line);
	}

	// buff 图标是否显示
	public boolean getBuffIsDisplay(int val) {
		try {
			BattleBuff bs = (BattleBuff) CacheMgr.battleBuffCache.get(val);
			return bs.isShowIcon();
		} catch (GameException e) {
			Log.e("BattleBuffCache", " buff not find", e);
		}
		return false;
	}

	public Drawable getBuffDrawable(int val, boolean isSmall) {
		String name = "";
		Drawable d = null;

		try {
			BattleBuff bs = (BattleBuff) CacheMgr.battleBuffCache.get(val);
			name = bs.getIcon();
			d = Config.getController().getDrawable(name);

			if (isSmall) {
				Drawable bg = Config.getController().getDrawable(
						"battle_skill_bg");
				int scale = 100;

				if (null != bg && null != d) {
					int bgSize = bg.getIntrinsicHeight() > bg
							.getIntrinsicWidth() ? bg.getIntrinsicHeight() : bg
							.getIntrinsicWidth();

					int skillSize = d.getIntrinsicHeight() > d
							.getIntrinsicWidth() ? d.getIntrinsicHeight() : d
							.getIntrinsicWidth();

					scale = (int) ((float) bgSize / skillSize * 100);
					d = Config.getController().getDrawable(name, scale, scale);// ,
				}
			} else {
				d = Config.getController().getDrawable(name, 45, 45);// ,
				if (d == null) {
					d = Config.getController().getDrawable("skill_small", 45,
							45);// ,
				}
			}
		} catch (GameException e) {
			Log.e("BattleBuffCache", "Image " + name + " not find", e);
		}

		if (d == null)
			new NullImgCallBack(name);
		return d;
	}
}
