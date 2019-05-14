package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;

//酒馆
public class BarWindow extends PopupWindow implements OnClickListener {
	private ViewGroup window;
	private ImageView bg, midLeft, midRight, top, heroRefreshMid,
			heroRefreshBottom;

	private View singleBtn, groupBtn, exchangeBtn;

	private FrameLayout btn_select_layout;

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void init() {
		window = (ViewGroup) controller.inflate(R.layout.bar);
		controller.addContent(window);
		bg = (ImageView) window.findViewById(R.id.bg);
		midLeft = (ImageView) window.findViewById(R.id.midLeft);
		midRight = (ImageView) window.findViewById(R.id.midRight);
		top = (ImageView) window.findViewById(R.id.top);
		heroRefreshMid = (ImageView) window.findViewById(R.id.heroRefreshMid);
		heroRefreshBottom = (ImageView) window
				.findViewById(R.id.heroRefreshBottom);

		singleBtn = window.findViewById(R.id.singleBtn);
		singleBtn.setOnClickListener(this);
		groupBtn = window.findViewById(R.id.groupBtn);
		groupBtn.setOnClickListener(this);
		exchangeBtn = window.findViewById(R.id.exchangeBtn);
		exchangeBtn.setOnClickListener(this);
		btn_select_layout = (FrameLayout) window
				.findViewById(R.id.btn_select_layout);

		ViewUtil.setText(window, R.id.title, "红楼");
		setImage();
		controller.addContentFullScreen(window);
	}

	private void setImage() {
		ViewUtil.setImage(bg, R.drawable.hero_bottom);
		ViewUtil.setImage(midLeft, R.drawable.hero_mid_left);
		ViewUtil.setImage(midRight, R.drawable.hero_mid_right);
		ViewUtil.setImage(top, R.drawable.hero_top);
		ViewUtil.setImage(heroRefreshMid, R.drawable.hero_refresh_mid);
		ViewUtil.setImage(heroRefreshBottom, R.drawable.hero_refresh_bottom);
	}

	public void open() {
		doOpen();
	}

	@Override
	protected void destory() {
		controller.removeContent(window);
	}

	@Override
	public void showUI() {
		super.showUI();
		controller.hideSystemAnnonce();
		if (singleBtn.getVisibility() != View.VISIBLE) {
			singleBtn.setVisibility(View.VISIBLE);
		}

		if (groupBtn.getVisibility() != View.VISIBLE) {
			groupBtn.setVisibility(View.VISIBLE);
		}

		if (exchangeBtn.getVisibility() != View.VISIBLE) {
			exchangeBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideUI() {
		super.hideUI();
		controller.showSystemAnnonce();
	}

	@Override
	public void onClick(final View v) {
		if (v == singleBtn || v == groupBtn) {
			int dex = 2 * btn_select_layout.getHeight();
			startButtonDown(dex, 200, exchangeBtn, new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					exchangeBtn.clearAnimation();
					exchangeBtn.setVisibility(View.INVISIBLE);
				}
			});
			startButtonDown(dex, 200, groupBtn, new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					groupBtn.clearAnimation();
					groupBtn.setVisibility(View.INVISIBLE);
				}
			});
			startButtonDown(dex, 200, singleBtn, new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					singleBtn.clearAnimation();
					singleBtn.setVisibility(View.INVISIBLE);
					if (v == singleBtn) {
						new SingleRefreshWindow(null,0).open();
					} else if (v == groupBtn) {
						new GroupRefreshWindow().open();
					}
				}
			});
		} else if (v == exchangeBtn) {
			controller.openHeroExchangeListWindow();
		}

	}

	private void startButtonDown(int dex, int time, View btn,
			AnimationListener listener) {
		TranslateAnimation refresh = new TranslateAnimation(0, 0, 0, dex);
		refresh.setDuration(time);
		if (listener != null) {
			refresh.setAnimationListener(listener);
		}
		refresh.setFillAfter(true);
		refresh.setInterpolator(new AccelerateInterpolator());
		btn.startAnimation(refresh);
	}
}
