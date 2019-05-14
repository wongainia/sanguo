package com.vikings.sanguo.ui.alert;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroEvolveSuccessTip extends ResultAnimTip {

	private HeroInfoClient oldHero, newHero;
	private ReturnInfoClient ric;
	private boolean isGuide;

	public HeroEvolveSuccessTip(HeroInfoClient oldHero, HeroInfoClient newHero,
			ReturnInfoClient ric) {
		super(true);
		this.oldHero = oldHero;
		this.newHero = newHero;
		this.ric = ric;
	}

	public HeroEvolveSuccessTip(HeroInfoClient oldHero, HeroInfoClient newHero,
			ReturnInfoClient ric, boolean isGuide) {
		super(true);
		this.oldHero = oldHero;
		this.newHero = newHero;
		this.ric = ric;
		this.isGuide = isGuide;
	}

	private void setHeroArmProp(ViewGroup propsLayout) {
		for (HeroArmPropInfoClient newHapic : newHero.getArmPropInfos()) {
			for (HeroArmPropInfoClient oldHapic : oldHero.getArmPropInfos()) {
				if (newHapic.getType() == oldHapic.getType())
					propsLayout
							.addView(getPropText(newHapic.getHeroTroopName()
									.getName(), newHapic.getValue() + "/"
									+ newHapic.getMaxValue()));
			}
		}
	}

	public void show() {
		SoundMgr.play(R.raw.sfx_evolution);
		super.show(null, false);

		if (isGuide)
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					controller.showCastle(Account.user.getId());
				}
			});
	}

	@Override
	protected int getDrawable() {
		return R.drawable.txt_jljh;
	}

	@Override
	protected View getContent() {
		View view = controller.inflate(R.layout.result_hero_levelup,
				rewardLayout, false);

		IconUtil.setHeroIconScale(view.findViewById(R.id.iconLayout), newHero);

		ViewUtil.setBoldRichText(view.findViewById(R.id.heroTalent),
				newHero.getHeroTypeName());
		ViewUtil.setBoldRichText(view.findViewById(R.id.heroName),
				newHero.getHeroName());

		ViewUtil.setGone(view, R.id.levelLayout);
		ViewUtil.setGone(view, R.id.goBtn);
		ViewUtil.setGone(view, R.id.desc);

		ViewGroup items = (ViewGroup) view.findViewById(R.id.items);

		items.addView(getPropText("将领品质", oldHero.getColorTypeStar() + "→"
				+ newHero.getColorTypeStar(), false));
		items.addView(getPropText("将领武力", oldHero.getRealAttack() + "→"
				+ newHero.getRealAttack()));
		items.addView(getPropText("将领防护", oldHero.getRealDefend() + "→"
				+ newHero.getRealDefend()));
		setHeroArmProp(items);
		LinearLayout level_up_content = (LinearLayout) view
				.findViewById(R.id.level_up_content);
		level_up_content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		return view;
	}
}
