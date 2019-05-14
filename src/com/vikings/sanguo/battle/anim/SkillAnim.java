/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-11-20 下午9:24:58
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Buff;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.R;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.TextView;

public class SkillAnim extends BaseAnim {
	private Drawable d;
	private ViewGroup vg = null;
	private Buff buff;
	private boolean isInit;
	
	public SkillAnim(ViewGroup vg, Animation anim,  Drawable d,  
			Buff buff, boolean isInit) {
		super(null, anim, false);
		this.vg = vg;
		this.d = d;
		this.buff = buff;
		this.isInit = isInit;
	}

	@Override
	protected void prepare() {
		View v = Config.getController().inflate(R.layout.skill_item);
		TextView skill = (TextView) v.findViewById(R.id.skill);
//		ImageView iv = new ImageView(Config.getController().getUIContext());
		LayoutParams lp = new LayoutParams((int)(26 * Config.scaleRate), (int)(26 * Config.scaleRate));
		skill.setLayoutParams(lp);
		skill.setBackgroundDrawable(d);
		ViewUtil.setText(skill, buff.amount);
//		iv.setImageDrawable(d);
		v.setTag(buff.buffId);
		
		if (isInit) {
			//设置技能之前，先把不可见的技能view去掉
			for (int i = 0; i < vg.getChildCount(); i++) {
				if (ViewUtil.isHidden(vg.getChildAt(i))
						|| (-1 == (Integer)vg.getChildAt(i).getTag()))
					vg.removeView(vg.getChildAt(i));
//					ViewUtil.setGone(vg.getChildAt(i));
			}
		}
		
		this.vg.addView(v);
		setView(v);
	}
}
