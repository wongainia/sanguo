package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.window.HeroEvolveWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroDevourSuccessTip extends ResultAnimTip {
	private HeroInfoClient oldHero, newHero;

	public HeroDevourSuccessTip(HeroInfoClient oldHero, HeroInfoClient newHero) {
		super(!newHero.canEvolve());
		this.oldHero = oldHero;
		this.newHero = newHero;
	}

	protected void setArmPorp(ViewGroup propsLayout) {
		List<View> views = new ArrayList<View>();
		for (HeroArmPropInfoClient newHapic : newHero.getArmPropInfos()) {
			for (HeroArmPropInfoClient oldHapic : oldHero.getArmPropInfos()) {
				if (newHapic.getType() == oldHapic.getType()) {
					propsLayout.addView(getPropText(newHapic.getHeroTroopName()
							.getName() + "上限", oldHapic.getMaxValue(),
							newHapic.getMaxValue()));

					views.add(getPropText(
							newHapic.getHeroTroopName().getName(),
							oldHapic.getValue(), newHapic.getValue()));
				}
			}
		}
		for (View view : views)
			propsLayout.addView(view);
	}

	public void show() {
		SoundMgr.play(R.raw.sfx_evolution);
		super.show(null, false);
	}

	@Override
	protected int getDrawable() {
		return R.drawable.txt_gxsj;
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

		ViewUtil.setImage(view, R.id.levelIcon1, R.drawable.txt_gxjlsd);
		ViewUtil.setRichText(view, R.id.level,
				StringUtil.numImgStr("v", newHero.getLevel()));
		ViewUtil.setImage(view, R.id.levelIcon2, R.drawable.txt_ji);

		ViewGroup items = (ViewGroup) view.findViewById(R.id.items);
		if (newHero.getLevel() > oldHero.getLevel())
			setHeroLevelUp(items, oldHero.getLevel(), newHero.getLevel());
		setArmPorp(items);

		View evoluteBtn = view.findViewById(R.id.goBtn);

		if (newHero.canEvolve()) {
			ViewUtil.setVisible(evoluteBtn);
			ViewUtil.setText(evoluteBtn, "去进化");
			evoluteBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					new HeroEvolveWindow().open(newHero);
				}
			});
		} else {
			ViewUtil.setGone(evoluteBtn);
		}

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
