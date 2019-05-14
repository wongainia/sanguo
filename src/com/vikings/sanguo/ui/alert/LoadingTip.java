package com.vikings.sanguo.ui.alert;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;

public class LoadingTip extends Alert {

	private static final int layout = R.layout.alert_loading;

	private View content;
	private TextView text;
	private View light;
	private ImageView hero;
	private Drawable drawable;
	private Animation rotateAnim;

	public LoadingTip() {
		this.content = controller.inflate(layout);
		text = (TextView) content.findViewById(R.id.text);
		light = content.findViewById(R.id.light);
		hero = (ImageView) content.findViewById(R.id.hero);
		drawable = hero.getDrawable();
		rotateAnim = getAnim(3000, true);
	}

	private Animation getAnim(int time, boolean clockwise) {
		Animation rotate;
		if (clockwise) {
			rotate = new RotateAnimation(0, 1 * 359,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			rotate = new RotateAnimation(1 * 359, 0,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}

		rotate.setDuration(time);
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setRepeatMode(Animation.RESTART);
		rotate.setInterpolator(new LinearInterpolator());
		rotate.setFillAfter(true);
		return rotate;
	}

	@Override
	protected void playSound() {
	}

	public void show(String msg) {
		ViewUtil.setVisible(content);
		// 不显示文字
		// if (msg != null && msg.length() > 16) {
		// msg = msg.substring(0, 15) + "...";
		// }
		// ViewUtil.setRichText(text, msg);
		this.show(content);
		light.startAnimation(rotateAnim);
		if (null != drawable && drawable instanceof AnimationDrawable) {
			final AnimationDrawable gif = (AnimationDrawable) drawable;
			gif.setOneShot(false);
			hero.post(new Runnable() {

				@Override
				public void run() {
					gif.start();
				}
			});

		}
		this.dialog.setOnDismissListener(null);
	}

	@Override
	public void dismiss() {
		ViewUtil.setHide(content);
		super.dismiss();
		light.clearAnimation();
		if (null != drawable && drawable instanceof AnimationDrawable) {
			AnimationDrawable gif = (AnimationDrawable) drawable;
			gif.stop();
		}
	}

}
