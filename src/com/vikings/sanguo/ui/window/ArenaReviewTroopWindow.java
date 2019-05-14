package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.adapter.ArenaReviewArmAdapter;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class ArenaReviewTroopWindow extends CustomPopupWindow {

	private ViewGroup[] heroLayouts = new ViewGroup[3];
	private ViewGroup gradientLayout, gradientBelowLayout;
	private GridView gridView;

	private List<ArmInfoClient> arenaTroopInfos;
	private List<HeroIdInfoClient> arenaHeroInfos;

	private ArenaReviewArmAdapter adapter;

	public void open() {
		doOpen();
	}

	@Override
	protected void init() {
		super.init("队伍总览");
		setContent(R.layout.arena_review_troop);
		gradientLayout = (ViewGroup) window.findViewById(R.id.gradientLayout);
		gradientBelowLayout = (ViewGroup) window
				.findViewById(R.id.gradientBelowLayout);
		heroLayouts[0] = (ViewGroup) window.findViewById(R.id.hero1);
		heroLayouts[1] = (ViewGroup) window.findViewById(R.id.hero2);
		heroLayouts[2] = (ViewGroup) window.findViewById(R.id.hero3);
		gridView = (GridView) window.findViewById(R.id.gridView);
		adapter = new ArenaReviewArmAdapter();
		gridView.setAdapter(adapter);

		setBottomButton("配置队伍", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ArenaTroopSetWindow().open();
			}
		});
	}
	
	

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	private void setHeroViews() {
		HeroInfoClient main = null, assist1 = null, assist2 = null;

		for (int i = 0; i < arenaHeroInfos.size(); i++) {
			HeroIdInfoClient hiic = arenaHeroInfos.get(i);
			HeroInfoClient heroInfo = Account.heroInfoCache.get(hiic.getId());
			if (heroInfo == null)
				continue;
			if (hiic.isMainHero())
				main = heroInfo;
			else {
				if (assist1 == null)
					assist1 = heroInfo;
				else
					assist2 = heroInfo;
			}
		}
		setHeroView(heroLayouts[0], main);
		setHeroView(heroLayouts[1], assist1);
		setHeroView(heroLayouts[2], assist2);
	}

	private void setHeroView(View view, HeroInfoClient hic) {
		if (null != hic) {
			view.findViewById(R.id.state).setBackgroundDrawable(null);
			ViewUtil.setText(view.findViewById(R.id.desc), "");
			ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar);
			bar.set(hic.getStamina(), HeroInfoClient.getMaxStamina());
			ViewUtil.setVisible(view, R.id.progressDesc);
			ViewUtil.setText(view.findViewById(R.id.progressDesc),
					hic.getStamina() + "/" + HeroInfoClient.getMaxStamina());
			ViewUtil.setVisible(view, R.id.heroIcon);
			view.setOnClickListener(new OwnHeroClickListerner(hic));
			IconUtil.setHeroIcon(view, hic.getHeroProp(), hic.getHeroQuality(),
					hic.getStar(), IconUtil.HERO_ICON_SCALE_MED);
		} else {
			setNoHero(view);
		}

	}

	public void setNoHero(View view) {
		ViewUtil.setImage(view.findViewById(R.id.iconBg),
				R.drawable.hero_ext_bg);
		ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar);
		ViewUtil.setGone(view, R.id.state);
		ViewUtil.setText(view.findViewById(R.id.desc), "");
		ViewUtil.setGone(view, R.id.heroIcon);
		bar.set(0, HeroInfoClient.getMaxStamina());
		ViewUtil.setGone(view, R.id.progressDesc);
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	private void setValue() {
		this.arenaTroopInfos = Account.myLordInfo.getArenaTroopInfo();
		this.arenaHeroInfos = Account.myLordInfo.getArenaHeroInfos();
		int count = 0;
		if (null != arenaTroopInfos) {
			adapter.clear();
			adapter.addItems(arenaTroopInfos);
			for (ArmInfoClient armInfo : arenaTroopInfos) {
				if (armInfo.getCount() <= 0)
					continue;
				count += armInfo.getCount();
			}
			adapter.notifyDataSetChanged();
		}

		setHeroViews();

		// 免费挑战次数
		ViewUtil.setRichText(gradientLayout.findViewById(R.id.gradientMsg),
				StringUtil.getArenaFreeTimes());
		ViewUtil.setRichText(
				gradientBelowLayout.findViewById(R.id.gradientMsg), "您当前配置的兵力为"
						+ count);
	}

}
