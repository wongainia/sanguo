package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroSetTip extends Alert {

	private final static int layout = R.layout.alert_hero_set;

	private View content, iconLayout;
	private TextView title, heroName;
	private HeroInfoClient hic;
	private boolean setOn;

	public HeroSetTip(HeroInfoClient hic, boolean setOn) {
		super(true);
		content = Config.getController().inflate(layout);
		iconLayout = content.findViewById(R.id.iconLayout);
		title = (TextView) content.findViewById(R.id.title);
		heroName = (TextView) content.findViewById(R.id.heroName);
		this.hic = hic;
		this.setOn = setOn;
	}

	public void show() {
		IconUtil.setHeroIconScale(iconLayout, hic);
		if (setOn) {
			ViewUtil.setText(title, "上阵成功");
		} else {
			ViewUtil.setText(title, "卸下成功");
		}
		ViewUtil.setRichText(heroName, hic.getHeroFullName());
		super.show(content);
	}
}
