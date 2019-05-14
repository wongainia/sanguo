package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.ArmInfoSelectData;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.CallBackAppear;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.TroopAdapter;
import com.vikings.sanguo.ui.alert.UnlockHeroTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public abstract class TroopSetWindow extends CustomBaseListWindow implements
		OnClickListener {
	protected ViewGroup heroLayout, hero1, hero2, hero3, troopLayout,
			gradientLayout, gradientBelowLayout;
	protected HeroInfoClient[] hics = new HeroInfoClient[3];
	protected List<ArmInfoSelectData> selectArmDatas;
	private CallBack troopDetailAlert;

	@Override
	protected void init() {
		super.init(getTitle());
		setContentBelowTitle(R.layout.hero_top);
		heroLayout = (ViewGroup) window.findViewById(R.id.heroLayout);
		hero1 = (ViewGroup) window.findViewById(R.id.hero1);// 主将                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		hero1.setOnClickListener(this);
		hero2 = (ViewGroup) window.findViewById(R.id.hero2);// 副将
		hero2.setOnClickListener(this);
		hero3 = (ViewGroup) window.findViewById(R.id.hero3);// 副将
		hero3.setOnClickListener(this);
		troopLayout = (ViewGroup) window.findViewById(R.id.troopList);
		gradientLayout = (ViewGroup) window.findViewById(R.id.gradientLayout);
		gradientBelowLayout = (ViewGroup) window
				.findViewById(R.id.gradientBelowLayout);
		ViewUtil.setGone(gradientBelowLayout);
		setBottomPadding();
		setinitHeroInfos();
		callTroopDetailHero();
	}

	// 英雄改变时 对士兵弹出框的 属性加成影响
	private void callTroopDetailHero() {
		troopDetailAlert = new CallBack() {

			@Override
			public void onCall() {
				ObjectAdapter objectAdapter = getAdapter();
				if (objectAdapter != null
						&& objectAdapter instanceof TroopAdapter) {
					((TroopAdapter) objectAdapter).setHic(TroopUtil
							.getList(hics));
					((TroopAdapter) objectAdapter).notifyDataSetChanged();
				}
			}
		};
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		selectArmDatas = getArmInfoSelectDatas();
		resultPage.setResult(selectArmDatas);
		resultPage.setTotal(selectArmDatas.size());
	}

	protected void setinitHeroInfos() {
		hics[0] = HeroInfoClient.newInstance();
		hics[1] = HeroInfoClient.newInstance();
		hics[2] = HeroInfoClient.newInstance();
	}

	@Override
	public void showUI() {
		super.showUI();
		if (needSaveCampaignHero()) {
			Account.readLog.DEFAULT_CAMPAIGN_HERO_ID1 = hics[0].getId();
			Account.readLog.DEFAULT_CAMPAIGN_HERO_ID2 = hics[1].getId();
			Account.readLog.DEFAULT_CAMPAIGN_HERO_ID3 = hics[2].getId();
		}
		if (needSaveBloodHero()) {
			Account.readLog.DEFAULT_BLOOD_HERO_ID1 = hics[0].getId();
			Account.readLog.DEFAULT_BLOOD_HERO_ID2 = hics[1].getId();
			Account.readLog.DEFAULT_BLOOD_HERO_ID3 = hics[2].getId();
		}
		setHeros();
		// 每次上阵将领改变时 都会英雄士兵弹出框的加成值
		if (troopDetailAlert != null) {
			troopDetailAlert.onCall();
		}
	}

	protected void setHeros() {
		setMainHero(hero1, hics[0]);
		setExtHero(hero2, hics[1], Account.user.unlockExtHero1());
		setExtHero(hero3, hics[2], Account.user.unlockExtHero2());
	}

	public void setMainHero(ViewGroup viewGroup, BaseHeroInfoClient hic) {
		ViewUtil.setImage(viewGroup.findViewById(R.id.iconBg),
				R.drawable.hero_main_bg);
		ProgressBar bar = (ProgressBar) viewGroup
				.findViewById(R.id.progressBar);
		if (null != hic && hic.isValid()) {
			viewGroup.findViewById(R.id.state).setBackgroundDrawable(null);
			ViewUtil.setText(viewGroup.findViewById(R.id.desc), "");
			ViewUtil.setVisible(viewGroup.findViewById(R.id.heroIcon));
			IconUtil.setHeroIconScale(viewGroup.findViewById(R.id.heroIcon),
					hic);
			bar.set(hic.getStamina(), HeroInfoClient.getMaxStamina());
			ViewUtil.setVisible(viewGroup, R.id.progressDesc);
			ViewUtil.setText(viewGroup.findViewById(R.id.progressDesc),
					hic.getStamina() + "/" + HeroInfoClient.getMaxStamina());
		} else {
			ViewUtil.setImage(viewGroup.findViewById(R.id.state),
					R.drawable.hero_add);
			ViewUtil.setText(viewGroup.findViewById(R.id.desc), "添加主将");
			ViewUtil.setGone(viewGroup.findViewById(R.id.heroIcon));
			bar.set(0, HeroInfoClient.getMaxStamina());
			ViewUtil.setGone(viewGroup, R.id.progressDesc);
		}
	}

	// 查看它人将领信息时 初始化无将领界面
	public void setExtOtherHero(ViewGroup viewGroup) {
		ViewUtil.setImage(viewGroup.findViewById(R.id.iconBg),
				R.drawable.hero_ext_bg);
		ProgressBar bar = (ProgressBar) viewGroup
				.findViewById(R.id.progressBar);
		ViewUtil.setGone(viewGroup, R.id.state);
		ViewUtil.setText(viewGroup.findViewById(R.id.desc), "");
		ViewUtil.setGone(viewGroup, R.id.heroIcon);
		bar.set(0, HeroInfoClient.getMaxStamina());
		ViewUtil.setGone(viewGroup, R.id.progressDesc);
	}

	public void setExtHero(ViewGroup viewGroup, BaseHeroInfoClient hic,
			boolean unlock) {
		ViewUtil.setImage(viewGroup.findViewById(R.id.iconBg),
				R.drawable.hero_ext_bg);
		ProgressBar bar = (ProgressBar) viewGroup
				.findViewById(R.id.progressBar);
		if (unlock) {
			if (null != hic && hic.isValid()) {
				viewGroup.findViewById(R.id.state).setBackgroundDrawable(null);
				ViewUtil.setText(viewGroup.findViewById(R.id.desc), "");
				ViewUtil.setVisible(viewGroup.findViewById(R.id.heroIcon));
				IconUtil.setHeroIconScale(
						viewGroup.findViewById(R.id.heroIcon), hic);
				bar.set(hic.getStamina(), HeroInfoClient.getMaxStamina());
				ViewUtil.setVisible(viewGroup, R.id.progressDesc);
				ViewUtil.setText(viewGroup.findViewById(R.id.progressDesc),
						hic.getStamina() + "/" + HeroInfoClient.getMaxStamina());
			} else {
				ViewUtil.setImage(viewGroup.findViewById(R.id.state),
						R.drawable.hero_add);
				ViewUtil.setText(viewGroup.findViewById(R.id.desc), "添加副将");
				ViewUtil.setGone(viewGroup.findViewById(R.id.heroIcon));
				bar.set(0, HeroInfoClient.getMaxStamina());
				ViewUtil.setGone(viewGroup, R.id.progressDesc);
			}
		} else {
			ViewUtil.setImage(viewGroup.findViewById(R.id.state),
					R.drawable.hero_lock);
			ViewUtil.setText(viewGroup.findViewById(R.id.desc), "解锁副将");
			ViewUtil.setGone(viewGroup, R.id.heroIcon);
			bar.set(0, HeroInfoClient.getMaxStamina());
			ViewUtil.setGone(viewGroup, R.id.progressDesc);
		}
	}

	// 已经选择的士兵总数
	protected int getSelectTroopCount() {
		int count = 0;
		if (null != selectArmDatas)
			for (ArmInfoSelectData aic : selectArmDatas) {
				count += aic.getSelCount();
			}
		return count;
	}

	// 已经选择的troopId兵种士兵数
	protected int getSelectTroopCount(int troopId) {
		if (null != selectArmDatas)
			for (ArmInfoSelectData armInfo : selectArmDatas) {
				if (armInfo.getAic().getId() == troopId)
					return armInfo.getSelCount();
			}
		return 0;
	}

	// 选中的将领数量
	protected int getSelectHeroCount() {
		int count = 0;
		for (int i = 0; i < hics.length; i++) {
			if (hics[i].isValid())
				count++;
		}
		return count;
	}

	// 是否选了主将
	protected boolean hasMainHero() {
		return hics[0].isValid();
	}

	@Override
	public void onClick(View v) {
		if (v == hero1) {
			new HeroChooseListWindow().open(hics, 0, getHeroChooseBriefFief(),
					(byte) 0, getCallBackAfterChooseHero(), new AppearAnim(v));
		} else if (v == hero2) {
			if (Account.user.unlockExtHero1()) {
				new HeroChooseListWindow().open(hics, 1,
						getHeroChooseBriefFief(), (byte) 0,
						getCallBackAfterChooseHero(), new AppearAnim(v));
			} else {
				new UnlockHeroTip(UnlockHeroTip.COUNT_ONE).show();
			}
		} else if (v == hero3) {
			if (Account.user.unlockExtHero2()) {
				new HeroChooseListWindow().open(hics, 2,
						getHeroChooseBriefFief(), (byte) 0,
						getCallBackAfterChooseHero(), new AppearAnim(v));
			} else {
				new UnlockHeroTip(UnlockHeroTip.COUNT_TWO).show();
			}
		}
		// 每次上阵将领改变时 都会影响士兵弹出框的加成值
		if (troopDetailAlert != null) {
			troopDetailAlert.onCall();
		}
	}

	protected List<ArmInfoClient> tidyArmInfos() throws GameException {
		List<ArmInfoClient> aics = new ArrayList<ArmInfoClient>();
		if (null != selectArmDatas) {
			for (ArmInfoSelectData selectData : selectArmDatas) {
				if (selectData.getSelCount() > 0)
					aics.add(new ArmInfoClient(selectData.getAic().getId(),
							selectData.getSelCount()));
			}
		}
		return aics;
	}

	protected List<HeroIdInfoClient> tidyHeroData() throws GameException {
		List<HeroIdInfoClient> list = new ArrayList<HeroIdInfoClient>();
		if (null != hics) {
			for (int i = 0; i < hics.length; i++) {
				HeroInfoClient hic = hics[i];
				if (null != hic && hic.isValid()) {
					HeroIdInfoClient hiic = new HeroIdInfoClient(hic.getId(),
							hic.getHeroId(), hic.getStar(), hic.getTalent());
					if (i == 0)
						hiic.setRole(HeroIdInfoClient.HERO_ROLE_ATTACK_MAIN);
					else
						hiic.setRole(HeroIdInfoClient.HERO_ROLE_ATTACK_ASSIST);
					list.add(hiic);
				}
			}
		}
		return list;
	}

	protected String getTitle() {
		return "出征部队";
	}

	protected abstract List<ArmInfoSelectData> getArmInfoSelectDatas()
			throws GameException;

	// 返回选择将领所在的领地，如果选择全部，该方法返回null即可
	protected abstract BriefFiefInfoClient getHeroChooseBriefFief();

	protected abstract CallBack getCallBackAfterChooseHero();

	protected boolean needSaveCampaignHero() {
		return false;
	}

	protected boolean needSaveBloodHero() {
		return false;
	}

	private class AppearAnim implements CallBackAppear {
		private View view;

		public AppearAnim(View view) {
			this.view = view;
		}

		@Override
		public void onCall(String heroName, boolean isAppear) {
			startAnimation(view, heroName, isAppear);
		}

	}

	private Animation getAnimation(View view) {
		AnimationSet animationSet = new AnimationSet(false);
		// 往上移动
		TranslateAnimation transAnim = new TranslateAnimation(
				0, 0,0, -50*Config.SCALE_FROM_HIGH);
		transAnim.setDuration(900);
		transAnim.setInterpolator(new AccelerateDecelerateInterpolator());
		animationSet.addAnimation(transAnim);
		// 消失动画
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0f);
		alphaAnim.setDuration(300);
		alphaAnim.setStartOffset(600);
		animationSet.addAnimation(alphaAnim);
		animationSet.setAnimationListener(new AnimaListener(view));
		return animationSet;
	}

	public void startAnimation(View v, String richText, boolean isAppeasr) {
		int pos[] = new int[2];
		v.getLocationOnScreen(pos);
		TextView textView = new TextView(controller.getUIContext());
	
		LayoutParams params = new FrameLayout.LayoutParams((int)(430*Config.SCALE_FROM_HIGH),LayoutParams.WRAP_CONTENT);
		//params.leftMargin = (int) (pos[0]) ;
		params.topMargin = (int) (pos[1]+ v.getHeight() + 50*Config.SCALE_FROM_HIGH);
		params.gravity=Gravity.CENTER_HORIZONTAL;

		textView.setSingleLine();
		textView.setTextSize(14);
		textView.setBackgroundDrawable(Config.getController().getDrawable(
				R.drawable.prompt_bg));
		textView.setGravity(Gravity.CENTER);
		window.addView(textView,params);//, params);
		
		ViewUtil.setRichText(textView, richText);
		Animation animation = getAnimation(textView);
		textView.setVisibility(View.VISIBLE);
		textView.startAnimation(animation);
	}

	private class AnimaListener implements AnimationListener {

		private View view;

		public AnimaListener(View view) {
			this.view = view;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			view.clearAnimation();
			ViewUtil.setGone(view);
			controller.getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					remove();
				}
			}, 100);
		}

		private void remove() {
			if (window != null && window.indexOfChild(view) != -1) {
				view.clearAnimation();
				window.removeView(view);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

}