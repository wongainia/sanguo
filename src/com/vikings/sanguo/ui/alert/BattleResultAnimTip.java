/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-27 下午4:44:36
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout.LayoutParams;

import com.vikings.sanguo.R;
import com.vikings.sanguo.battle.anim.AnimPool;
import com.vikings.sanguo.battle.anim.StarFlyAnim;
import com.vikings.sanguo.cache.BitmapCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.AbnormalFrameLayout;
import com.vikings.sanguo.ui.AbnormalFrameLayout.RecyleResource;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class BattleResultAnimTip extends Alert implements OnClickListener,OnKeyListener {
	private boolean isWin;
	private CallBack cb;

	private final static int layout = R.layout.alert_battle_result_anim_tip;

	private AbnormalFrameLayout content;
	private boolean isCancel;

	private StarFlyAnim starFlyAnim;
	private View battle_over_bg;
	private View screen_frame;
	private View subject;
	private View light_effect;
	private View name;
	private View knife_left;
	private View knife_right;
	private View knife_shadow_left;
	private View knife_shadow_right;

	private Drawable bg;
	private Drawable light;
	private Drawable ject;
	private Drawable d_name;

	public BattleResultAnimTip(boolean isWin) {
		super(true);
		content = (AbnormalFrameLayout) Config.getController().inflate(layout);
		content.setRecyleResourceListener(mRecyleResource);
		this.isWin = isWin;
	}

	public BattleResultAnimTip(boolean isWin, CallBack cb) {
		super(true);
		content = (AbnormalFrameLayout) Config.getController().inflate(layout);
		content.setRecyleResourceListener(mRecyleResource);
		this.isWin = isWin;
		this.cb = cb;
	}

	protected void onAnimEnd() {
		if (null != cb)
			cb.onCall();
		dismiss();
	}

	public BattleResultAnimTip() {
		super(true);
		content = (AbnormalFrameLayout) Config.getController().inflate(layout);
		content.setRecyleResourceListener(mRecyleResource);
		
	}

	public void show() {
		this.dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.dialog.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (content != null) {
			content.requestLayout();
			super.show(content);
		}
		WindowManager.LayoutParams params = this.dialog.getWindow()
				.getAttributes();
		params.width = LayoutParams.FILL_PARENT;
		params.height = LayoutParams.FILL_PARENT; //
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
		this.dialog.setOnKeyListener(this);
		doAnimation();
	}

	public View getLayout() {
		return this.content;
	}
	
	@Override
	public void dismiss()
	{
		super.dismiss();
		isCancel = true;
		mHandler.removeMessages(0);
		if (starFlyAnim != null) {
			starFlyAnim.rubAllViews();
		}
		content.removeView(starFlyAnim);
	}

	public void handleTouchClose() {
		super.handleTouchClose();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		this.dismiss();
		onAnimEnd();
		isCancel = true;
		mHandler.removeMessages(0);
		if (starFlyAnim != null) {
			starFlyAnim.rubAllViews();
		}
	}

	private void doAnimation() {
		if (content == null) {
			return;
		}
		starFlyAnim = (StarFlyAnim) content.findViewById(R.id.fly_star);

		screen_frame = content.findViewById(R.id.screen_frame);
		battle_over_bg = content.findViewById(R.id.battle_over_bg);
		subject = content.findViewById(R.id.subject);

		light_effect = content.findViewById(R.id.light_effect);
		name = content.findViewById(R.id.name);

		knife_left = content.findViewById(R.id.knife_left);
		knife_right = content.findViewById(R.id.knife_right);
		knife_shadow_left = content.findViewById(R.id.knife_shadow_left);
		knife_shadow_right = content.findViewById(R.id.knife_shadow_right);

		Animation appear = AnimPool.getNullAnim(50);
		screen_frame.setVisibility(View.VISIBLE);
		screen_frame.startAnimation(appear);

		bg = Config.getController().getDrawableHdInBattle("battle_over_bg",ImageUtil.ARGB4444);
		if (bg != null) {
			battle_over_bg.setBackgroundDrawable(bg);
		}
		light = Config.getController().getDrawableHdInBattle("effect_light",ImageUtil.RGB565);
		if (light != null) {
			light_effect.setBackgroundDrawable(light);
		}
		ject = Config.getController().getDrawableHdInBattle("subject",ImageUtil.ARGB4444);
		if (ject != null) {
			subject.setBackgroundDrawable(ject);
		}
		appear.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Animation subjectAnim = AnimPool.largeOut(0.4f, 1f, 0.4f, 1f,
						150);
				battle_over_bg.setVisibility(View.VISIBLE);
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
								1f, 0.5f, 1f, 0f, 0.8f);
						if (isWin) {
							d_name = Config.getController().getDrawable("battle_win");
						} else {
							d_name = Config.getController().getDrawable("battle_lose");
						}
						name.setBackgroundDrawable(d_name);
						int nameWidth = 0;
						if (d_name != null) {
							nameWidth = d_name.getIntrinsicWidth();
						}
						name.setVisibility(View.VISIBLE);
						name.startAnimation(nameAnimation);

						if (isWin == false) {
							Animation leftRotate = AnimPool.effectLeftRotate();
							knife_left.setVisibility(View.VISIBLE);
							knife_left.startAnimation(leftRotate);
							leftRotate
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
											int top = knife_left.getTop();
											int left = knife_left.getLeft();
											int width = knife_shadow_left
													.getWidth();
											int height = knife_shadow_left
													.getHeight();
											int shadowLeft = (int) (left - 70
													* Config.SCALE_FROM_HIGH - width / 2);
											int shadowTop = (int) (top - 10 * Config.SCALE_FROM_HIGH);
											ViewUtil.setMargin(
													knife_shadow_left,
													shadowLeft, shadowTop, 0,
													0, Gravity.LEFT
															| Gravity.TOP);
											knife_shadow_left.layout(
													shadowLeft, shadowTop,
													shadowLeft + width,
													shadowTop + height);
											Animation knifeShadow = AnimPool
													.battleOverShadowRotate();
											knife_shadow_left
													.startAnimation(knifeShadow);
											knifeShadow
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
															clear();
															name.clearAnimation();
															Animation nameShow = AnimPool
																	.getNullAnim(500);
															name.startAnimation(nameShow);
															nameShow.setAnimationListener(new nameShowListen());
														}
													});
										}
									});

							Animation effectRight = AnimPool
									.effectRightRotate();
							knife_right.setVisibility(View.VISIBLE);
							knife_right.startAnimation(effectRight);
							effectRight
									.setAnimationListener(new effectRightListen(
											false));
							return;
						}

						Animation effectLarge = AnimPool.effectLargeOut();
						light_effect.setVisibility(View.VISIBLE);
						light_effect.startAnimation(effectLarge);

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
												.rollingLightAnim();
										light_effect
												.startAnimation(effectRotate);
										effectRotate
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
														clear();
														name.clearAnimation();
														Animation nameShow = AnimPool
																.getNullAnim(500);
														name.startAnimation(nameShow);
														nameShow.setAnimationListener(new nameShowListen());
													}
												});

										Animation effectleft = AnimPool
												.effectLeftRotate();
										knife_left.setVisibility(View.VISIBLE);
										knife_left.startAnimation(effectleft);
										effectleft
												.setAnimationListener(new effectLeftListen());

										Animation effectRight = AnimPool
												.effectRightRotate();
										knife_right.setVisibility(View.VISIBLE);
										knife_right.startAnimation(effectRight);
										effectRight
												.setAnimationListener(new effectRightListen(
														true));
									}
								});
					}
				});
			}
		});
	}

	private void clear() {
		battle_over_bg.clearAnimation();
		battle_over_bg.setVisibility(View.GONE);

		screen_frame.clearAnimation();
		screen_frame.setVisibility(View.GONE);

		subject.clearAnimation();
		subject.setVisibility(View.GONE);

		light_effect.clearAnimation();
		light_effect.setVisibility(View.GONE);

		knife_left.clearAnimation();
		knife_left.setVisibility(View.GONE);
		
		name.clearAnimation();
		name.setVisibility(View.GONE);

		knife_right.clearAnimation();
		knife_right.setVisibility(View.GONE);

		knife_shadow_left.clearAnimation();
		knife_shadow_left.setVisibility(View.GONE);

		knife_shadow_right.clearAnimation();
		knife_shadow_right.setVisibility(View.GONE);

		if (starFlyAnim != null && starFlyAnim.getChildCount() > 0) {
			starFlyAnim.rubAllViews();
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isCancel == true) {
				if (starFlyAnim != null && starFlyAnim.getChildCount() > 0) {
					starFlyAnim.rubAllViews();
					//content.removeView(starFlyAnim);
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

	class nameShowListen implements AnimationListener {
		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			clear();
			name.clearAnimation();
			Animation nameShow = AnimPool.getNullAnim(500);
			name.startAnimation(nameShow);
			nameShow.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					onAnimEnd();
					isCancel = true;
				}
			});

		}
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
			int top = knife_left.getTop();
			int left = knife_left.getLeft();
			int width = knife_shadow_left.getWidth();
			int height = knife_shadow_left.getHeight();
			//Log.d("AlertTaskFinishTip", "左边刀-" + " left--" + left + " top:"
			//		+ top + " width:" + width + " height:" + height);
			int shadowLeft = (int) (left - 70 * Config.SCALE_FROM_HIGH - width / 2);
			int shadowTop = (int) (top - 10 * Config.SCALE_FROM_HIGH);
			ViewUtil.setMargin(knife_shadow_left, shadowLeft, shadowTop, 0, 0,
					Gravity.LEFT | Gravity.TOP);
			knife_shadow_left.layout(shadowLeft, shadowTop, shadowLeft + width,
					shadowTop + height);

			knife_shadow_left.startAnimation(AnimPool.lightEffectRotate());
		}
	}

	class effectRightListen implements AnimationListener {
		boolean isShowStar;

		public effectRightListen(boolean isShowStar) {
			this.isShowStar = isShowStar;
		}

		@Override
		public void onAnimationEnd(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationStart(Animation animation) {
			int top = knife_right.getTop();
			int left = knife_right.getRight();
			int width = knife_shadow_right.getWidth();
			int height = knife_shadow_right.getHeight();
			int shadowLeft = (int) (left + 70 * Config.SCALE_FROM_HIGH - width / 2);
			int shadowTop = (int) (top - 10 * Config.SCALE_FROM_HIGH);
			//Log.d("AlertTaskFinishTip", "右边刀" + " left--" + left + " top:"
			//		+ top + " width:" + width + " height:" + height);
			ViewUtil.setMargin(knife_shadow_right, shadowLeft, shadowTop, 0, 0,
					Gravity.LEFT | Gravity.TOP);
			knife_shadow_right.layout(shadowLeft, shadowTop,
					shadowLeft + width, shadowTop + height);
			knife_shadow_right.setVisibility(View.VISIBLE);
			knife_shadow_right.startAnimation(AnimPool.lightEffectRotate());
			if (isShowStar) {
				mHandler.sendEmptyMessage(0);
			}
		}
	}

	private void startStarFlyAnim() {
		starFlyAnim.setDuration(2000);
		//starFlyAnim.setDuration(1000);
		for (int i = 0; i < 5; i++) {
			starFlyAnim.feedKeyword("");
		}
		starFlyAnim.go2Show(StarFlyAnim.ANIMATION_OUT);
		int random = Math.abs(new Random().nextInt() % 500) + 1500;
		mHandler.sendEmptyMessageDelayed(0, random);
	}
	
	private void releaseSource()
	{
//		content.removeAllViews();		
		ViewUtil.releaseDrawable(battle_over_bg,bg);
		ViewUtil.releaseDrawable(light_effect,light);
		ViewUtil.releaseDrawable(subject,ject);
		ViewUtil.releaseDrawable(name,d_name);

	}

	protected void doOnDismiss() {
		super.doOnDismiss();
		onAnimEnd();
		isCancel = true;
		mHandler.removeMessages(0);
		if (starFlyAnim != null) {
			starFlyAnim.rubAllViews();
			content.removeView(starFlyAnim);
			
		}
	}

	
	private RecyleResource mRecyleResource = new RecyleResource()
	{		
		@Override
		public void doRecyle()
		{
			List<String> imgs = new ArrayList<String>();
			imgs.add("battle_over_bg");
			imgs.add("effect_light");
			imgs.add("subject");
			imgs.add("battle_win");
			imgs.add("battle_lose");
			BitmapCache bitmapCache = Config.getController().getBitmapCache();
			for (String img : imgs)
			{
				Bitmap bmp = bitmapCache.get(img);
				if (bmp != null && bmp.isRecycled() == false)
				{
					bitmapCache.remove(img); 
				}
			}
			
			System.gc();
		}
	};

	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2)
	{
		if ((arg1 == KeyEvent.KEYCODE_BACK))
		{
			onAnimEnd();
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
