package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.ui.adapter.AbandonHeroAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.alert.HeroAbandonConfirmTip;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class HeroAbandonListWindow extends CustomListViewWindow {

	public void open() {
		this.doOpen();
	}

	@Override
	protected void init() {
		super.init("分解将领");
		setContentBelowTitle(R.layout.gradient_msg);
		List<HeroInfoClient> heros = getHeroinfos();
		if (ListUtil.isNull(heros) == false) {
			setBottomButton("分  解", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					List<HeroInfoClient> abandonHeros = getAbandonHeros();
					if (ListUtil.isNull(abandonHeros) == false) {
						new HeroAbandonConfirmTip(abandonHeros).show();
					} else {
						Config.getController().alert("请选择需要分解的将领！");
					}
				}
			});
		}
		// else
		// {
		// setBottomButton(StringUtil.color("分  解", R.color.color17), null);
		// }
		setBottomPadding();
		TextView textView = (TextView) window.findViewById(R.id.gradientMsg);
		textView.setTextSize(14);
		ViewUtil.setText(textView, "请勾选想要分解的将领");

		// 去掉默认的选中色
		getListView().setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void showUI() {
		super.showUI();
		List<HeroInfoClient> heros = getHeroinfos();
		adapter.clear();
		adapter.addItems(heros);
		adapter.notifyDataSetChanged();
		dealwithEmptyAdpter();
	}

	protected void dealwithEmptyAdpter() {
		super.dealwithEmptyAdpter();
		List<HeroInfoClient> heros = getHeroinfos();
		if (ListUtil.isNull(heros)) {
			ViewUtil.setGone(belowBtnFrame);
		}
	};

	public List<HeroInfoClient> getHeroinfos() {
		List<HeroInfoClient> hics = new ArrayList<HeroInfoClient>();
		List<HeroInfoClient> allHeros = Account.heroInfoCache.get();
		for (HeroInfoClient hic : allHeros) {
			if (canAbandon(hic))
				hics.add(hic);
		}
		Collections.sort(hics, new Comparator<HeroInfoClient>() {
			@Override
			public int compare(HeroInfoClient hic1, HeroInfoClient hic2) {
				if (hic1.getTalent() == hic2.getTalent()) {
					if (hic1.getStar() == hic2.getStar()) {
						return hic2.getLevel() - hic1.getLevel();
					} else {
						return hic2.getStar() - hic1.getStar();
					}

				} else {
					return hic2.getTalent() - hic1.getTalent();
				}
			}
		});
		return hics;
	}

	public static boolean canAbandon(HeroInfoClient hic) {
		if (hic.isInBattle())
			return false;
		return true;
	}

	@Override
	protected ObjectAdapter getAdapter() {
		AbandonHeroAdapter adapter = new AbandonHeroAdapter();
		return adapter;
	}

	@Override
	protected String getEmptyShowText() {
		return "您的幕府中一名将领都没有<br><br>快去[酒馆]中招募将领吧";
	}

	private List<HeroInfoClient> getAbandonHeros() {
		List<HeroInfoClient> hics = new ArrayList<HeroInfoClient>();
		List<HeroInfoClient> allHeros = Account.heroInfoCache.get();
		for (HeroInfoClient hic : allHeros) {
			if (hic.getChooseAbandon()) {
				hics.add(hic);
			}
		}
		return hics;
	}
	
	@Override
	protected void destory()
	{
		super.destory();
		//退出后清除选中的标记
		List<HeroInfoClient> heros = getHeroinfos();
		if(ListUtil.isNull(heros) == false)
		{
			for(HeroInfoClient heroInfoClient : heros)
			{
				heroInfoClient.setChooseAbandon(false);
			}
		}
	}
}
