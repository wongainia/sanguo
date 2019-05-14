package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class GroupHurtAnim extends BaseAnim
{
	private TextView tv;
	private StringBuilder builder;

	private TextView cnt;
	private int num;

	private boolean isLeft;

	private TextView armName;
	private BattleLogInfoClient battleLogInfo;
	private ViewGroup vg;
	private ViewGroup window;
	
	private TextView flyNum;
	

	public GroupHurtAnim(boolean isLeft,ViewGroup window,TextView view, Animation anim, StringBuilder builder,
			TextView cnt, int num)
	{ 
		super(view, anim, true);
		this.tv = view;
		this.builder = builder;

		this.cnt = cnt;
		this.num = num;		
		this.window = window;
				
		this.isLeft = isLeft;
	}

	public void setHpInfo(TextView armName, ViewGroup vg,
			BattleLogInfoClient battleLogInfo)
	{
		this.armName = armName;
		this.battleLogInfo = battleLogInfo;
		this.vg = vg;
	}

	@Override
	protected void prepare()
	{	
		ViewUtil.setRichText(view, builder.toString());
		
//		int[] location = new  int[2] ;
//		tv.getLocationOnScreen(location);
//		//tv.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
//			
//		flyNum = new TextView(Config.getController().getUIContext());
//		flyNum.setSingleLine(true);
//		ViewUtil.setRichText(flyNum, builder.toString());				
//		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		
//		lp.leftMargin = location[0] - vg.getWidth()/4;
//		lp.topMargin = location[1]/*vg.getTop() + vg.getHeight()*/; //location[1];
//		lp.gravity = Gravity.TOP ;
//		
//		window.addView(flyNum,lp);
//		flyNum.bringToFront();
//		setView(flyNum);		
	}

	@Override
	protected void animationStart()
	{ 
		super.animationStart(); // animation
	}

	@Override
	protected void animationEnd()
	{
		super.animationEnd();
		armName.setText(battleLogInfo.getNickName(isLeft) + "("
				+ battleLogInfo.getCurrTroopAmount(isLeft) + ")");

		if (null != vg)
		{
			ViewUtil.setRichText(vg, R.id.cnt, CalcUtil.turnToTenThousandEx(num));
			if (0 == num)
			{ // 全灭
				ViewUtil.setHide(vg, R.id.selBg);
				View state = vg.findViewById(R.id.state);
				// if (ViewUtil.isHidden(state));
				ViewUtil.setVisible(state);
				ViewUtil.setImage(vg, R.id.role, R.drawable.btl_die);
				vg.setTag(-1);
				//SoundMgr.play("dead.ogg");
			}
		}
		
		//window.removeView(flyNum);
		
		//flyNum.clearAnimation();
		//flyNum.setVisibility(View.GONE);
	}

}
