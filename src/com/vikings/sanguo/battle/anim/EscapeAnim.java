package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;

public class EscapeAnim extends BaseAnim
{
	private View v;
	private Drawable d;
	private int x;

	public EscapeAnim(View view, Animation anim, View v, Drawable d, int x)
	{
		super(view, anim, true);
		this.v = v;
		this.d = d;
		this.x = x;
	}

	@Override
	protected void prepare()
	{
		ViewUtil.setRichText(view, "#btl_help#");
		Drawable help = Config.getController().getDrawable(R.drawable.btl_help);
		// if (0 != x) {
		// int l = x;
		// int b = view.getBottom();
		// int t = b - help.getIntrinsicHeight();
		// int r = x + help.getIntrinsicWidth();
		//
		// ViewUtil.setMarginLeft(view, x);
		// view.layout(l, t, r, b);
		// }

		// {
		// LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// lp.leftMargin = x;
		// lp.gravity = Gravity.LEFT|Gravity.BOTTOM;
		// view.setLayoutParams(lp);
		// }
	}

	@Override
	protected void animationEnd()
	{ // Animation animation
		// v.findViewById(R.id.amy).setBackgroundDrawable(d);
	}
}
