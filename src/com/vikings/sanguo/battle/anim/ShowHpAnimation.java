package com.vikings.sanguo.battle.anim;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.BattleArrayInfoClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ShowHpAnimation extends BaseAnim
{	
	private TextView cnt;
	private TextView armName;
	private LinearLayout hpBar;
	private long currentHp;
	private long totalHp;
	private long num;
	private String name;
	private boolean isBoss;
	private boolean isLeft;
	private BattleLogInfoClient battleLogInfo;
	private ViewGroup vg;
	private BattleArrayInfoClient baic;
	

	public ShowHpAnimation(boolean isLeft, TextView cnt, TextView armName,LinearLayout hpBar, ViewGroup vg,Animation anim,
			BattleArrayInfoClient baic, String name, boolean isBoss,BattleLogInfoClient battleLogInfo)
	{
		super(cnt, anim, false);
		this.isLeft = isLeft;
		this.cnt = cnt;
		this.armName = armName;
		this.hpBar = hpBar;
		this.currentHp = baic.getCurrentHp();
		this.totalHp = baic.getHp();
		this.num = baic.getNum();
		this.name = name;
		this.isBoss = isBoss;
		this.battleLogInfo =battleLogInfo;
		this.vg = vg;
		this.baic = baic;
	}

	@Override
	protected void prepare()
	{
		if(isBoss)
		{
			cnt.setText(name + "(" + totalHp + ")");					
		}
		else
		{
			cnt.setText(name + "(" + num + ")");				
		}
		armName.setText(battleLogInfo.getNickName(isLeft) + "(" + battleLogInfo.getCurrTroopAmount(isLeft) + ")");
				
		if(hpBar.getVisibility() !=View.VISIBLE )
		{	
			hpBar.setVisibility(View.VISIBLE);
		}
		
		if (null != vg)
		{
			ViewUtil.setRichText(vg, R.id.cnt, CalcUtil.turnToTenThousandEx(baic.getNum()));
			if (0 == baic.getNum())
			{ // 全灭
				ViewUtil.setHide(vg, R.id.selBg);

				View state = vg.findViewById(R.id.state);
				if (ViewUtil.isHidden(state));
				ViewUtil.setVisible(state);
				ViewUtil.setImage(vg, R.id.role, R.drawable.btl_die);
				vg.setTag(-1);
				// SoundMgr.play("dead.ogg");
			}
		}
			
		if (totalHp == 0)
		{
			return;
		}
//		ProgressBar v = (ProgressBar) (isLeft ?hpBar. findViewById(R.id.lAmyHP)
//				: hpBar.findViewById(R.id.rAmyHP));
		
		ProgressBar v = (ProgressBar) (isLeft ?hpBar. findViewById(R.id.downAmyHP)
				: hpBar.findViewById(R.id.upAmyHP));
		
		if (currentHp < 0)
			currentHp = 0;

		int cur = (int) (100 * currentHp / totalHp);
		v.set(cur);		
	}
	
	@Override
	protected void animationEnd()
	{ 
		
	}
}
