package com.vikings.sanguo.battle.anim;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleArrayInfoClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class TopEffectAnim extends BaseAnim {
	private int x;
	private int y;
	private String str;

	private boolean isDown;
	private TextView cnt;
	private TextView armName;
	private LinearLayout hpBar;
	private long currentHp;
	private long totalHp;
	private long num;
	private String name;
	private boolean isBoss;
	private TroopProp tp;
	private BattleLogInfoClient battleLogInfo;
	private ViewGroup vg;
	private BattleArrayInfoClient baic;
	private TextView amyName;
	
	public TopEffectAnim(View view, Animation anim, String str, int x, int y) { // ,
																				// int
																				// width
		super(view, anim, true);
		this.x = x;
		this.str = str;
		this.y = y;
	}

	public void setHpInfo(boolean isDown, TextView cnt,TextView amyName, TextView armName,
			LinearLayout hpBar, ViewGroup vg, BattleArrayInfoClient baic,
			TroopProp tp, BattleLogInfoClient battleLogInfo) {
		this.isDown = isDown;
		this.cnt = cnt;
		this.armName = armName;
		this.hpBar = hpBar;
		if (baic != null) {
			this.currentHp = baic.getCurrentHp();
			this.totalHp = baic.getHp();
			this.num = baic.getNum();
		}
		if (tp != null) {
			this.name = tp.getName();
			this.isBoss = tp.isBoss();
		}
		this.battleLogInfo = battleLogInfo;
		this.vg = vg;
		this.baic = baic;
		
		this.amyName = amyName;
	}

	@Override
	protected void prepare() {
		int width = view.getWidth();
		int height = view.getHeight();

		ViewUtil.setMarginLeft(view, x - width / 2);
		ViewUtil.setMarginTop(view,
				(int) (y - height / 2 - 10 * Config.SCALE_FROM_HIGH));

		int l = x - width / 2;
		int t = (int) (y - height / 2 - 10 * Config.SCALE_FROM_HIGH);
		int r = l + width;
		int b = t + height;
		view.layout(l, t, r, b);
		if (view instanceof TextView)
			ViewUtil.setRichText(view, str);
		{
			amyName.setText(name);
			cnt.setText(/*name +*/ "(" + num + ")");
		}
		armName.setText(battleLogInfo.getNickName(isDown) + "("
				+ battleLogInfo.getCurrTroopAmount(isDown) + ")");

		if (hpBar.getVisibility() != View.VISIBLE) {
			hpBar.setVisibility(View.VISIBLE);
		}

		if (null != vg) {
			ViewUtil.setRichText(vg, R.id.cnt,
					CalcUtil.turnToTenThousandEx(num));
			if (0 == baic.getNum()) { // 全灭
				ViewUtil.setHide(vg, R.id.selBg);

				View state = vg.findViewById(R.id.state);
				if (ViewUtil.isHidden(state))
					;
				ViewUtil.setVisible(state);
				ViewUtil.setImage(vg, R.id.role, R.drawable.btl_die);
				vg.setTag(-1);
				// SoundMgr.play("dead.ogg");
			}
		}

		if (totalHp == 0) {
			return;
		}
		ProgressBar v = (ProgressBar) (isDown ? hpBar.findViewById(R.id.downAmyHP)
				: hpBar.findViewById(R.id.upAmyHP));

		if (currentHp < 0)
			currentHp = 0;

		int cur = (int) (100 * currentHp / totalHp);
		v.set(cur);
	}

	@Override
	protected void animationStart() { // Animation animation
		super.animationStart(); // animation

	}

	// 动画结束后改变血量 数量
	@Override
	protected void animationEnd() {
		super.animationEnd();
		if (view instanceof TextView)
			((TextView) view).setText("");
	}
}
