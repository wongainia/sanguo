package com.vikings.sanguo.ui.alert;

import java.util.Random;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.battle.anim.AnimPool;
import com.vikings.sanguo.battle.anim.StarFlyAnim;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

/**
 * 一系列的成功动画提示框
 * 
 * @author susong
 * 
 */
public abstract class ResultAnimTip extends Alert implements OnClickListener,
		OnKeyListener {

	private final static int layout = R.layout.alert_animation;

	protected ViewGroup content, rewardLayout;

	protected boolean isCancel;

	protected StarFlyAnim starFlyAnim;

	protected View subject, lightEffect, name, knifeLeft, knifeRight,
			knifeShadowLeft, knifeShadowRight;

	public ResultAnimTip() {
		this(true);		
	}
	
	public ResultAnimTip(boolean touchClose) {
		super(touchClose);
		content = (ViewGroup) Config.getController().inflate(layout);
		content.requestLayout();
		super.show(content, 0.62f);
		this.dialog.setOnDismissListener(null);
		this.dialog.setOnKeyListener(this);
		init();
	}

	private void init() {
		subject = content.findViewById(R.id.subject);

		lightEffect = content.findViewById(R.id.lightEffect);
		name = content.findViewById(R.id.name);

		starFlyAnim = (StarFlyAnim) content.findViewById(R.id.flyStar);

		knifeLeft = content.findViewById(R.id.knifeLeft);
		knifeRight = content.findViewById(R.id.knifeRight);
		knifeShadowLeft = content.findViewById(R.id.knifeShadowLeft);
		knifeShadowRight = content.findViewById(R.id.knifeShadowRight);
		rewardLayout = (ViewGroup) content.findViewById(R.id.rewardLayout);

		setInitValue();
	}

	// 初始化部分图片
	private void setInitValue() {
		//ViewUtil.setImage(lightEffect, R.drawable.effect_light);
		ViewUtil.setImage(subject, R.drawable.subject);
		if (getDrawable() > 0)
			ViewUtil.setImage(name, getDrawable());
		ViewUtil.setImage(rewardLayout, R.drawable.alert_common_bg);
	}

	public void handleTouchClose() {
		super.handleTouchClose();
		isCancel = true;
		mHandler.removeMessages(0);
		if (starFlyAnim != null) {
			starFlyAnim.rubAllViews();
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		isCancel = true;
		mHandler.removeMessages(0);
		if (starFlyAnim != null) {
			starFlyAnim.rubAllViews();
		}
		content.removeView(starFlyAnim);
	}

	public void doOnDismiss() {
		isCancel = true;
		mHandler.removeMessages(0);
		if (starFlyAnim != null) {
			starFlyAnim.rubAllViews();
		}
		content.removeView(starFlyAnim);
	}

	protected void show(CallBack callBack, boolean playDefaultSound) {
		if (playDefaultSound)
			SoundMgr.play(R.raw.sfx_tips2);
		View view = getContent();
		if (null != view)
			rewardLayout.addView(view);

		if (isTouchClose)
			content.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dismiss();
					isCancel = true;
					mHandler.removeMessages(0);
					if (starFlyAnim != null) {
						starFlyAnim.rubAllViews();
					}
				}
			});

		if (callBack == null)
			this.dialog.setOnCancelListener(null);
		else
			this.dialog.setOnCancelListener(new CancleListener(callBack));

		doAnimation();
	}

	private class CancleListener implements OnCancelListener {

		CallBack call;

		public CancleListener(CallBack call) {
			this.call = call;
		}

		@Override
		public void onCancel(DialogInterface d) {
			if (null != call)
				call.onCall();
			isCancel = true;
			mHandler.removeMessages(0);
			if (starFlyAnim != null) {
				starFlyAnim.rubAllViews();
				content.removeView(starFlyAnim);
			}
			dialog.setOnCancelListener(null);
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		this.dismiss();
		isCancel = true;
		mHandler.removeMessages(0);
		if (starFlyAnim != null) {
			starFlyAnim.rubAllViews();

		}
	}

	private void doAnimation() {

		Animation appear = AnimPool.getNullAnim(100);
		content.startAnimation(appear);
		rewardLayout.setVisibility(View.VISIBLE);

		appear.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Animation subjectAnim = AnimPool
						.largeOut(0.5f, 1, 0.5f, 1, 150);
				subject.setVisibility(View.VISIBLE);
				subject.startAnimation(subjectAnim);

				subjectAnim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Animation nameAnimation = AnimPool.nameLargeApha(0.5f,
								1f, 0.5f, 1f, 0f, 1f);
						name.setVisibility(View.VISIBLE);
						name.startAnimation(nameAnimation);

						Animation effectLarge = AnimPool.effectLargeOut();
						lightEffect.setVisibility(View.VISIBLE);
						lightEffect.startAnimation(effectLarge);

						effectLarge
								.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(
											Animation animation) {
									}

									@Override
									public void onAnimationRepeat(
											Animation animation) {
									}

									@Override
									public void onAnimationEnd(
											Animation animation) {
										Animation effectRotate = AnimPool
												.lightEffectRotate();
										lightEffect
												.startAnimation(effectRotate);

										Animation effectleft = AnimPool
												.effectLeftRotate();
										knifeLeft.setVisibility(View.VISIBLE);
										knifeLeft.startAnimation(effectleft);
										effectleft
												.setAnimationListener(new effectLeftListen());

										Animation effectRight = AnimPool
												.effectRightRotate();
										knifeRight.setVisibility(View.VISIBLE);
										knifeRight.startAnimation(effectRight);

										effectRight
												.setAnimationListener(new effectRightListen());
									}
								});
					}
				});
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isCancel == true) {
				if (starFlyAnim != null && starFlyAnim.getChildCount() > 0) {
					starFlyAnim.rubAllViews();
				}
				return;
			}

			switch (msg.what) {
//			case 0: {
//				startStarFlyAnim();
//				break;
//			}
			}
		};
	};

	private void startStarFlyAnim() {
		starFlyAnim.setDuration(3000);
		// starFlyAnim.setDuration(1000);
		for (int i = 0; i < 5; i++) {
			starFlyAnim.feedKeyword("");
		}
		starFlyAnim.go2Show(StarFlyAnim.ANIMATION_OUT);
		int random = Math.abs(new Random().nextInt() % 500) + 1500;
		mHandler.sendEmptyMessageDelayed(0, random);
	}

	class effectLeftListen implements AnimationListener {
		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			int top = knifeLeft.getTop();
			int left = knifeLeft.getLeft();
			int width = knifeShadowLeft.getWidth();
			int height = knifeShadowLeft.getHeight();
			int shadowLeft = (int) (left - 70 * Config.SCALE_FROM_HIGH - width / 2);
			int shadowTop = (int) (top - 10 * Config.SCALE_FROM_HIGH);
			ViewUtil.setMargin(knifeShadowLeft, shadowLeft, shadowTop, 0, 0,
					Gravity.LEFT | Gravity.TOP);
			knifeShadowLeft.layout(shadowLeft, shadowTop, shadowLeft + width,
					shadowTop + height);

			knifeShadowLeft.startAnimation(AnimPool.lightEffectRotate());
		}
	}

	class effectRightListen implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			int top = knifeRight.getTop();
			int left = knifeRight.getRight();
			int width = knifeShadowRight.getWidth();
			int height = knifeShadowRight.getHeight();
			int shadowLeft = (int) (left + 70 * Config.SCALE_FROM_HIGH - width / 2);
			int shadowTop = (int) (top - 10 * Config.SCALE_FROM_HIGH);
			ViewUtil.setMargin(knifeShadowRight, shadowLeft, shadowTop, 0, 0,
					Gravity.LEFT | Gravity.TOP);
			knifeShadowRight.layout(shadowLeft, shadowTop, shadowLeft + width,
					shadowTop + height);
			knifeShadowRight.setVisibility(View.VISIBLE);
			knifeShadowRight.startAnimation(AnimPool.lightEffectRotate());

			// int maxSize;
			// maxSize = rewardLayout.getHeight();
			// StretchAnimation stretchanimation = new StretchAnimation(maxSize,
			// 0, StretchAnimation.TYPE.vertical, 50);
			// stretchanimation.setInterpolator(new AccelerateInterpolator());
			// stretchanimation.startAnimation(rewardLayout);

			mHandler.sendEmptyMessage(0);
		}
	}

	protected TextView getPropText(String name, String value) {
		TextView v = ViewUtil.getBattleLogTextView();
		ViewUtil.setRichText(v, StringUtil.color(name + "：", R.color.color13)
				+ StringUtil.color(value, R.color.color19));
		return v;
	}

	protected TextView getPropText(String name, String value, boolean valueColor) {
		TextView v = ViewUtil.getBattleLogTextView();
		if (valueColor)
			ViewUtil.setRichText(
					v,
					StringUtil.color(name + "：", R.color.color13)
							+ StringUtil.color(value, R.color.color19));
		else
			ViewUtil.setRichText(v,
					StringUtil.color(name + "：", R.color.color13) + value);
		return v;
	}

	protected void setHeroLevelUp(ViewGroup propsLayout, int oldLvl, int newLvl) {
		TextView levelView = (TextView) controller
				.inflate(R.layout.battle_log_txt);
		levelView.setGravity(Gravity.CENTER_HORIZONTAL);
		ViewUtil.setRichText(
				levelView,
				StringUtil.color("将领等级：", R.color.color13)
						+ StringUtil.color("Lv" + oldLvl + "→Lv" + newLvl,
								R.color.color19));
		propsLayout.addView(levelView);
	}

	protected TextView getPropText(String name, int oldVal, int newVal) {
		TextView v = ViewUtil.getBattleLogTextView();
		ViewUtil.setRichText(v, StringUtil.color(name + "：", R.color.color13)
				+ StringUtil.color(oldVal + "→" + newVal, R.color.color19));
		return v;
	}

	protected abstract int getDrawable();

	protected abstract View getContent();

	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		if ((arg1 == KeyEvent.KEYCODE_BACK)) {
			isCancel = true;
			mHandler.removeMessages(0);
			if (starFlyAnim != null) {
				starFlyAnim.rubAllViews();
				content.removeView(starFlyAnim);
			}
		}
		return false;
	}

}
