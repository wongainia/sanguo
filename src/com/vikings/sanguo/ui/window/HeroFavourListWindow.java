package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.view.View;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.ui.adapter.HeroFavourAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class HeroFavourListWindow extends CustomListViewWindow {
	public void open() {
		this.doOpen();
	}

	@Override
	protected void init() {
		adapter = new HeroFavourAdapter();
		super.init("宠幸将领");
		setContentBelowTitle(R.layout.gradient_msg);
		TextView textView = (TextView) window.findViewById(R.id.gradientMsg);
		textView.setTextSize(13);

		StringBuilder multiWord = new StringBuilder();
		multiWord.append("可宠幸");
		String level = StringUtil.color("[天选]、[无双]", Config.getController()
				.getResourceColorText(R.color.color19));
		multiWord.append(level);
		multiWord.append("及更高品质的未脱衣的将领");
		ViewUtil.setRichText(textView, multiWord.toString());
	}

	@Override
	public void showUI() {
		super.showUI();
		List<HeroInfoClient> heros = getHeroinfos();
		adapter.clear();
		adapter.addItems(heros);
		adapter.notifyDataSetChanged();
	}

	public static List<HeroInfoClient> getHeroinfos() {
		List<HeroInfoClient> hics = new ArrayList<HeroInfoClient>();
		List<HeroInfoClient> allHeros = Account.heroInfoCache.get();
		for (HeroInfoClient hic : allHeros) {
			if (canFavour(hic))
				hics.add(hic);
		}
		Collections.sort(hics);
		return hics;
	}

	public static boolean canFavour(HeroInfoClient hic) {
		boolean isCanFavour = true;
		HeroProp heroProp = hic.getHeroProp();
		if (heroProp != null) {
			if (heroProp.getRating() <= HeroProp.HERO_RATING_MING_JIANG) {
				isCanFavour = false;
			}
			if (heroProp.isNoClothHero()) {
				isCanFavour = false;
			}
			if (hic.isInBattle()) {
				isCanFavour = false;
			}
		}
		return isCanFavour;
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void destory() {
		controller.removeContentFullScreen(window);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected String getEmptyShowText() {
		return "您当前没有可宠幸的将领!";
	}
}
