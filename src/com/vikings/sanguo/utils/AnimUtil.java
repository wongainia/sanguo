package com.vikings.sanguo.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;

public class AnimUtil {

	public static Animation transShow;

	public static Animation transHide;

	public static Animation alphaShow;

	public static Animation alphaHide;

	public static Animation emptyAnim;

	public static Animation recv;

	public static Animation pushLeftOut;

	public static Animation pushRightOut;

	public static Animation pushLeftIn;

	public static Animation pushRightIn;
	
	private static int duration = 500;

	private static int duration_long = 1000;

	public static Animation radar;

	public static Animation slideOut;
	public static Animation slideIn;

	static {
		// 这里是TranslateAnimation动画
		transShow = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		transShow.setDuration(duration);
		// 透明度动画
		alphaShow = new AlphaAnimation(0, 1);
		alphaShow.setDuration(duration_long);
		// 这里是ScaleAnimation动画
		// show = new ScaleAnimation(
		// 1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f);

		// 这里是TranslateAnimation动画
		transHide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
		transHide.setDuration(duration_long);
		// 透明度动画
		alphaHide = new AlphaAnimation(1, 0);
		alphaHide.setDuration(duration);
		// 这里是ScaleAnimation动画
		// hideAction = new ScaleAnimation(
		// 1.0f, 1.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f);

		recv = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -10.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 10.0f);
		// 这里是ScaleAnimation动画
		// hideAction = new ScaleAnimation(
		// 1.0f, 1.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f);
		recv.setDuration(duration);

		slideOut = AnimationUtils.loadAnimation(Config.getController()
				.getUIContext(), R.anim.slide_out);
		slideIn = AnimationUtils.loadAnimation(Config.getController()
				.getUIContext(), R.anim.slide_in);

		emptyAnim = new AlphaAnimation(1, 1);

		pushLeftOut = AnimationUtils.loadAnimation(Config.getController()
				.getUIContext(), R.anim.push_left_out);

		pushRightOut = AnimationUtils.loadAnimation(Config.getController()
				.getUIContext(), R.anim.push_right_out);

		pushLeftIn =  AnimationUtils.loadAnimation(Config.getController()
				.getUIContext(), R.anim.push_left_in);
		
		pushRightIn =  AnimationUtils.loadAnimation(Config.getController()
				.getUIContext(), R.anim.push_right_in);
		
	}

}
