package com.vikings.sanguo.ui.window;

import java.util.List;

import android.graphics.Point;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.battle.anim.RotateAnimation;
import com.vikings.sanguo.battle.anim.RotateAnimation.InterpolatedTimeListener;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.HeroRefreshInvoker;
import com.vikings.sanguo.model.HeroShopInfoClient;
import com.vikings.sanguo.model.HeroShopItemInfoClient;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GroupRefreshWindow extends PopupWindow implements OnClickListener,
		InterpolatedTimeListener {
	private ViewGroup window, groupLayout;
	private ImageView bg, midLeft, midRight, top, heroRefreshMid,
			heroRefreshBottom;
	private TextView heroTalent, heroName, groupMsg;
	private Button refreshBtn, backBtn;
	private ViewGroup[] heroIcons = new ViewGroup[8];
	private LinearLayout tip_layout;

	private boolean isFirst = true;
	private boolean isFirstFlop = true; // 第一次显示翻牌
	private boolean hasFlop = false; // 是否翻过牌 已经翻过了 显示上次的数据

	boolean enableRefresh = false;

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void init() {
		window = (ViewGroup) controller.inflate(R.layout.hero_refresh);
		groupLayout = (ViewGroup) window.findViewById(R.id.groupLayout);
		ViewUtil.setVisible(groupLayout);
		bg = (ImageView) window.findViewById(R.id.bg);
		midLeft = (ImageView) window.findViewById(R.id.midLeft);
		midRight = (ImageView) window.findViewById(R.id.midRight);
		top = (ImageView) window.findViewById(R.id.top);
		heroRefreshMid = (ImageView) window.findViewById(R.id.heroRefreshMid);
		heroRefreshBottom = (ImageView) window
				.findViewById(R.id.heroRefreshBottom);
		heroTalent = (TextView) window.findViewById(R.id.heroTalent);
		ViewUtil.setGone(heroTalent);
		heroName = (TextView) window.findViewById(R.id.heroName);
		ViewUtil.setGone(heroName);
		groupMsg = (TextView) window.findViewById(R.id.gradientMsg);
		refreshBtn = (Button) window.findViewById(R.id.refreshBtn);
		refreshBtn.setOnClickListener(this);
		backBtn = (Button) window.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		ViewUtil.setText(window, R.id.title, "翻牌");
		
		tip_layout = (LinearLayout) window.findViewById(R.id.tip_layout);
		
		setImage();
		controller.addContentFullScreen(window);
		heroIcons[0] = (ViewGroup) window.findViewById(R.id.hero1);
		heroIcons[1] = (ViewGroup) window.findViewById(R.id.hero2);
		heroIcons[2] = (ViewGroup) window.findViewById(R.id.hero3);
		heroIcons[3] = (ViewGroup) window.findViewById(R.id.hero4);
		heroIcons[4] = (ViewGroup) window.findViewById(R.id.hero5);
		heroIcons[5] = (ViewGroup) window.findViewById(R.id.hero6);
		heroIcons[6] = (ViewGroup) window.findViewById(R.id.hero7);
		heroIcons[7] = (ViewGroup) window.findViewById(R.id.hero8);
	}

	public void open() {
		doOpen();
	}

	private void setImage() {
		ViewUtil.setImage(bg, R.drawable.hero_bottom);
		ViewUtil.setImage(midLeft, R.drawable.hero_mid_left);
		ViewUtil.setImage(midRight, R.drawable.hero_mid_right);
		ViewUtil.setImage(top, R.drawable.hero_top);
		ViewUtil.setImage(heroRefreshMid, R.drawable.hero_refresh_mid);
		ViewUtil.setImage(heroRefreshBottom, R.drawable.hero_refresh_bottom);
	}
	
	private void setDynamic() {
		HeroShopInfoClient hsic = Account.myLordInfo.getHeroShopInfo();
		if(hsic != null)
		{
			List<HeroShopItemInfoClient> shopItems = hsic.getInfos();
			if(ListUtil.isNull(shopItems) == false)
			{
				for(int i = 0; i < 8; i++)
				{
					final HeroShopItemInfoClient shopItem = shopItems.get(i);
					if(shopItem.isRecruit() == true)
					{
						ViewUtil.setVisible(tip_layout);
						TextView talent = (TextView) tip_layout.findViewById(R.id.tipTalent);
						TextView name = (TextView) tip_layout.findViewById(R.id.tipName);

						talent.setText(shopItem.getHeroQuality().getName());
						name.setText(shopItem.getHeroProp().getName());
						
						Config.getController().getHandler().postDelayed(new Runnable() {						
											@Override
											public void run() {
												shopItem.setRecruit(false);
												ViewUtil.setGone(tip_layout);
											}
										}, 1000);						
					}
				}
			}
		}
		
		
		groupMsg.setVisibility(View.VISIBLE);
		if (null != hsic && null != hsic.getInfos()
				&& !hsic.getInfos().isEmpty()) {
			String msg = "翻牌";

			if (isFirstFlop == false) {
				msg = "再翻一次";
			}
			ViewUtil.setRichText(
					refreshBtn,
					msg
							+ "#rmb#"
							+ CacheMgr.heroCommonConfigCache
									.getGroupRefreshHeroPrice(), true);
			ViewUtil.setText(groupMsg, "点击喜爱的女神头像进行招募");
			hasFlop = true;
			List<HeroShopItemInfoClient> hsiics = hsic.getInfos();
			for (int i = 0; i < hsiics.size(); i++) {
				if (i < heroIcons.length) {
					if (isFirstFlop || enableRefresh == false)
						setHero(hsiics.get(i), heroIcons[i], true);
				}
			}
			// groupLayout.invalidate();
		} else {
			ViewUtil.setRichText(
					refreshBtn,
					"开始翻牌"
							+ "#rmb#"
							+ CacheMgr.heroCommonConfigCache
									.getGroupRefreshHeroPrice(), true);
			for (int i = 0; i < heroIcons.length; i++) {
				IconUtil.setEmptyHeroIcon(heroIcons[i]);
			}
			hasFlop = false;
			ViewUtil.setText(groupMsg, "点击开始翻牌刷新女神");
		}
	}

	@Override
	public void showUI() {
		super.showUI();
		
		setDynamic();
		for (int i = 0; i < 8; i++) {
			if (heroIcons[i] != null) {
				heroIcons[i].setVisibility(View.VISIBLE);
			}
		}
		if (isFirst == true) {
			doTurnChoose(false);
			isFirst = false;
		}
		controller.hideIconForFullScreen();
	}

	private void setHero(final HeroShopItemInfoClient hsiic,
			ViewGroup viewGroup, boolean isDefault) {

		IconUtil.setRecruitHeroIcon(viewGroup, hsiic.getHeroProp(),
				hsiic.getHeroQuality(), hsiic.getHeroInit().getStar(), true,
				isDefault);

		if (hsiic.isBought()) {
			ViewUtil.setVisible(viewGroup, R.id.state);
			ViewUtil.setText(viewGroup, R.id.state, "已招募");
			viewGroup.setOnClickListener(null);
		} else {
			ViewUtil.setGone(viewGroup, R.id.state);
			viewGroup.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {	
					for(int i = 0; i < 8;  i++)
					{
						if(heroIcons[i].getAnimation() != null)
						{
							return;
						}
					}
					doChooseHero(v, hsiic);
				}
			});

		}
	}

	// 开始选择
	private void doChooseHero(final View v, final HeroShopItemInfoClient hsiic) {
		int[] locationCenter = new int[2];
		groupLayout.getLocationOnScreen(locationCenter);

		int[] locationHero = new int[2];
		v.getLocationOnScreen(locationHero);
		v.bringToFront();
		for (int i = 0; i < 8; i++) {
			if (heroIcons[i] != v) {
				heroIcons[i].clearAnimation();
				heroIcons[i].setVisibility(View.INVISIBLE);
			}
			else
			{
				heroIcons[i].clearAnimation();
			}
		}
		int targetXCenter = locationCenter[0] + groupLayout.getWidth() / 2
				- v.getWidth() / 2;
		int targetYCenter = locationCenter[1] + groupLayout.getHeight() / 2
				- v.getHeight() / 2;

		int disXCenter = (locationHero[0] + targetXCenter) / 2;
		int disYCenter = (int) (Math.min(locationHero[1], targetYCenter) - 80 * Config.SCALE_FROM_HIGH);// (locationHero[1]
																										// +
																										// targetYCenter)/2;
		AnimationSet move = new AnimationSet(false);

		int dex1 = disXCenter - locationHero[0];
		int dey1 = disYCenter - locationHero[1];
		TranslateAnimation ts1 = new TranslateAnimation(0, dex1, 0, dey1);

		ts1.setDuration(100);
		ts1.setInterpolator(new AccelerateInterpolator());

		int dex2 = targetXCenter - disXCenter;
		int dey2 = targetYCenter - disYCenter;

		TranslateAnimation ts2 = new TranslateAnimation(0, dex2, 0, dey2);
		ts2.setDuration(100);
		ts2.setStartOffset(100);
		ts2.setInterpolator(new DecelerateInterpolator());
		move.addAnimation(ts1);
		move.addAnimation(ts2);
		move.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (animation.hasEnded()) {
					return;
				}
				//v.clearAnimation();
				//v.setVisibility(View.INVISIBLE);
				new SingleRefreshWindow(hsiic, hsiic.getId()).open();
				enableRefresh = false;
			}
		});
		v.startAnimation(move);
	}

	@Override
	public void hideUI() {
		super.hideUI();
		controller.showIconForFullScreen();
	}

	@Override
	protected void destory() {
		controller.removeContentFullScreen(window);
	}

	@Override
	public void onClick(View v) {
		if (v == refreshBtn) {

			// 防止上次没有翻完 就点击
			// for (int i = 0; i < isHeroEnd.length; i++)
			// {
			// if(isHeroEnd[i] == false)
			// {
			// Log.d("GroupRefreshWindow", "翻牌没有结束  点击直接返回");
			// return;
			// }
			// }
			// for (int i = 0; i < isHeroEnd.length; i++)
			// {
			// isHeroEnd[i] = false;
			// }

			// for (int i = 0; i < isHeroUpdate.length; i++) {
			// isHeroUpdate[i] = false;
			// }

			if (isFirstFlop == true) {
				isFirstFlop = false;
			}
			enableRefresh = true;
			new GroupHeroRefresh().start();
		} else if (v == backBtn) {
			controller.goBack();
		}
	}

	private class GroupHeroRefresh extends HeroRefreshInvoker {

		public GroupHeroRefresh() {
			super(TYPE_GROUP);
		}

		@Override
		protected void refreshUI() {

			// setDynamic();
			if (hasFlop == false) {
				doFlopPre(-1, true);
			} else {
				doFlop();
			}

		}

	}

	private void doFlopPre(int i, boolean isSametime) {
		if (isSametime) {
			for (int j = 0; j < 8; j++) {
				RotateAnimation rotation = new RotateAnimation(
						heroIcons[j].getWidth() / 2,
						heroIcons[j].getHeight() / 2,
						RotateAnimation.ROTATE_DECREASE, j);
				rotation.setInterpolatedTimeListener(this);
				rotation.setDuration(500);
				rotation.setFillAfter(true);
				rotation.setInterpolator(new DecelerateInterpolator());
				heroIcons[j].startAnimation(rotation);
				isHeroUpdate[j] = false;

				final int index = j;
				rotation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// isHeroEnd[index] = true;
						groupMsg.setVisibility(View.VISIBLE);
					}
				});

			}
		} else {
			int[] location = new int[2];
			heroIcons[i].getLocationOnScreen(location);
			Point point = new Point();
			point.x = location[0];
			point.y = location[1];

			RotateAnimation rotation = new RotateAnimation(
					heroIcons[i].getWidth() / 2, heroIcons[i].getHeight() / 2,
					RotateAnimation.ROTATE_DECREASE, i);
			rotation.setInterpolatedTimeListener(this);
			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());
			isHeroUpdate[i] = false;
			heroIcons[i].startAnimation(rotation);

			final int index = i;
			rotation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					groupMsg.setVisibility(View.VISIBLE);
					heroIcons[index].clearAnimation();
					// isHeroEnd[index] = true;
				}
			});
		}
	}

	private void doFlop() {
		int dex = Config.screenWidth;
		for (int i = 0; i < 4; i++) {
			TranslateAnimation leftEnter = new TranslateAnimation(0, -dex, 0, 0);
			leftEnter.setInterpolator(new AccelerateInterpolator());
			leftEnter.setDuration(100 + i * 50);
			heroIcons[i].startAnimation(leftEnter);
		}
		for (int i = 7; i >= 4; i--) {
			TranslateAnimation rightEnter = new TranslateAnimation(0, dex, 0, 0);
			rightEnter.setInterpolator(new AccelerateInterpolator());
			rightEnter.setDuration(100 + (i - 4) * 50);
			if (i == 4) {
				rightEnter.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						HeroShopInfoClient hsic = Account.myLordInfo
								.getHeroShopInfo();
						groupMsg.setVisibility(View.VISIBLE);
						if (null != hsic && null != hsic.getInfos()
								&& !hsic.getInfos().isEmpty()) {
							List<HeroShopItemInfoClient> hsiics = hsic
									.getInfos();
							for (int i = 0; i < hsiics.size(); i++) {
								if (i < heroIcons.length) {
									setHero(hsiics.get(i), heroIcons[i], false);
								}
							}

						}
						doTurnChoose(true);
					}
				});
			}
			heroIcons[i].startAnimation(rightEnter);
		}
	}

	private void doTurnChoose(final boolean isFlop) {
		int dex = Config.screenWidth;
		for (int i = 3; i >= 0; i--) {
			TranslateAnimation leftEnter = new TranslateAnimation(-dex, 0, 0, 0);
			leftEnter.setInterpolator(new AccelerateInterpolator());
			leftEnter.setDuration(200 + i * 50);
			heroIcons[i].startAnimation(leftEnter);
			final int index = i;
			leftEnter.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (isFlop) {
						doFlopPre(index, false);
					}

				}
			});
		}

		for (int i = 4; i < 8; i++) {
			TranslateAnimation rightEnter = new TranslateAnimation(dex, 0, 0, 0);
			rightEnter.setInterpolator(new AccelerateInterpolator());
			rightEnter.setDuration(200 + (i - 4) * 50);
			heroIcons[i].startAnimation(rightEnter);
			final int index = i;
			rightEnter.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (isFlop) {
						doFlopPre(index, false);
					}

				}
			});

		}
	}

	private boolean[] isHeroUpdate = { true, true, true, true, true, true,
			true, true };

	private boolean[] isHeroEnd = { true, true, true, true, true, true, true,
			true }; // 动画是否结束

	@Override
	public void interpolatedTime(float interpolatedTime, int index) {

		if (interpolatedTime > 0.5f && isHeroUpdate[index] == false) {
			HeroShopInfoClient hsic = Account.myLordInfo.getHeroShopInfo();
			List<HeroShopItemInfoClient> hsiics = hsic.getInfos();

			setHero(hsiics.get(index), heroIcons[index], true);
			isHeroUpdate[index] = true;
			heroIcons[index].setTag(new Integer(1));
			// enableRefresh = false;
		}
	}

	// 翻转采用放大 缩小
	private void doTurnOver(int i, boolean isSametime) {

		if (isSametime) {
			for (int j = 0; j < 8; j++) {
				ScaleAnimation scaleIn = new ScaleAnimation(1, 0.2f, 1, 1,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0);
				scaleIn.setDuration(200);
				scaleIn.setFillAfter(true);
				scaleIn.setInterpolator(new AccelerateInterpolator());
				heroIcons[j].startAnimation(scaleIn);

				final int index = j;
				scaleIn.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						HeroShopInfoClient hsic = Account.myLordInfo
								.getHeroShopInfo();
						groupMsg.setVisibility(View.VISIBLE);
						if (null != hsic && null != hsic.getInfos()
								&& !hsic.getInfos().isEmpty()) {
							List<HeroShopItemInfoClient> hsiics = hsic
									.getInfos();
							if (hsiics != null && hsiics.size() > index)
								setHero(hsiics.get(index), heroIcons[index],
										true);

						}

						ScaleAnimation scaleOut = new ScaleAnimation(0.2f, 1f,
								1, 1, Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						scaleOut.setDuration(200);
						scaleOut.setFillAfter(true);
						scaleOut.setInterpolator(new DecelerateInterpolator());
						heroIcons[index].startAnimation(scaleOut);

					}
				});

			}
		} else {

			ScaleAnimation scaleIn = new ScaleAnimation(1, 0.2f, 1, 1,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0);
			scaleIn.setDuration(200);
			scaleIn.setFillAfter(true);
			scaleIn.setInterpolator(new AccelerateInterpolator());
			heroIcons[i].startAnimation(scaleIn);

			final int index = i;
			scaleIn.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					HeroShopInfoClient hsic = Account.myLordInfo
							.getHeroShopInfo();
					groupMsg.setVisibility(View.VISIBLE);
					if (null != hsic && null != hsic.getInfos()
							&& !hsic.getInfos().isEmpty()) {
						List<HeroShopItemInfoClient> hsiics = hsic.getInfos();
						if (hsiics != null && hsiics.size() > index)
							setHero(hsiics.get(index), heroIcons[index], true);

					}

					ScaleAnimation scaleOut = new ScaleAnimation(0.2f, 1f, 1,
							1, Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0);
					scaleOut.setDuration(200);
					scaleOut.setFillAfter(true);
					scaleOut.setInterpolator(new DecelerateInterpolator());
					heroIcons[index].startAnimation(scaleOut);

				}
			});
		}

	}
}
