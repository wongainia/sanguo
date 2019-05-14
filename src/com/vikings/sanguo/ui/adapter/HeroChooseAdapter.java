package com.vikings.sanguo.ui.adapter;

import java.util.List;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroLevelUp;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.CallBackAppear;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.HeroSetTip;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.ui.window.HeroChooseListWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroChooseAdapter extends ObjectAdapter {
	private HeroInfoClient heroInfoClient;
	private CallBack mCallBack;
	private CallBackAppear mCallBackAppear;

	public HeroChooseAdapter(HeroInfoClient heroInfoClient) {
		this(heroInfoClient, null, null);
	}

	public HeroChooseAdapter(HeroInfoClient heroInfoClient, CallBack mCallBack,
			CallBackAppear callBackAppear) {
		this.heroInfoClient = heroInfoClient;
		this.mCallBack = mCallBack;
		this.mCallBackAppear = callBackAppear;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.iconLayout = (ViewGroup) convertView
					.findViewById(R.id.iconLayout);
			holder.qualityName = (TextView) convertView
					.findViewById(R.id.qualityName);
			holder.heroName = (TextView) convertView
					.findViewById(R.id.heroName);
			holder.heroLevel = (TextView) convertView
					.findViewById(R.id.heroLevel);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.ability = (TextView) convertView.findViewById(R.id.ability);
			holder.staminaTitle = (TextView) convertView
					.findViewById(R.id.staminaTitle);
			holder.armProps = (TextView) convertView
					.findViewById(R.id.armProps);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			holder.progressDesc = (TextView) convertView
					.findViewById(R.id.progressDesc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final HeroInfoClient heroInfo = (HeroInfoClient) getItem(position);
		IconUtil.setHeroIconScale(holder.iconLayout, heroInfo);

		// 在将领详情框 是否显示上阵按钮
		holder.iconLayout.setOnClickListener(new OwnHeroClickListerner(
				heroInfo, (heroInfoClient.getId() == heroInfo.getId()) ? null
						: new CallBack() {

							@Override
							public void onCall() {
								heroInfoClient.update(heroInfo);

								if (mCallBack != null) {
									mCallBack.onCall();
								}
								Config.getController().goBack();
								Config.getController().goBack();
								if (mCallBackAppear != null) {
									String richText = "";
									if (heroInfo != null) {
										richText = heroInfo.getColorTypeName()
												+ " "
												+ StringUtil.getHeroName(
														heroInfo.getHeroProp(),
														heroInfo.getHeroQuality())
												+ "  " + (true ? "上场" : "卸下");
									}
									mCallBackAppear.onCall(richText, true);
								}
								// else {
								// new HeroSetTip(heroInfo, true).show();
								// }
							}
						}));
		if (heroInfo.getHeroQuality() != null
				&& !StringUtil.isNull(heroInfo.getHeroQuality().getName())) {
			ViewUtil.setVisible(holder.qualityName);
			ViewUtil.setRichText(holder.qualityName,
					heroInfo.getColorTypeName());
		} else {
			ViewUtil.setGone(holder.qualityName);
		}

		if (heroInfo.getId() == heroInfoClient.getId()) {
			ViewUtil.setVisible(holder.state);
			ViewUtil.setImage(convertView, R.drawable.transcript_list_bg);
			Resources resource = Config.getController().getResources();
			ColorStateList csl = (ColorStateList) resource
					.getColorStateList(R.color.color13);
			if (csl != null) {
				holder.ability.setTextColor(csl);
				holder.armProps.setTextColor(csl);
				holder.staminaTitle.setTextColor(csl);
			}
		} else {
			ViewUtil.setGone(holder.state);
			ViewUtil.setImage(convertView, R.drawable.common_item_bg);
			Resources resource = Config.getController().getResources();
			ColorStateList csl = (ColorStateList) resource
					.getColorStateList(R.color.color9);
			if (csl != null) {
				holder.ability.setTextColor(csl);
				holder.armProps.setTextColor(csl);
				holder.staminaTitle.setTextColor(csl);
			}
		}

		ViewUtil.setRichText(
				holder.heroName,
				StringUtil.getHeroName(heroInfo.getHeroProp(),
						heroInfo.getHeroQuality()));

		ViewUtil.setText(holder.heroLevel, "LV" + heroInfo.getLevel());

		HeroLevelUp levelUp = (HeroLevelUp) CacheMgr.heroLevelUpCache.getExp(
				heroInfo.getHeroType().getType(), heroInfo.getStar(),
				heroInfo.getLevel());
		if (null != levelUp) {
			holder.progressBar.set(heroInfo.getStamina(),
					HeroInfoClient.getMaxStamina());
			ViewUtil.setRichText(holder.progressDesc, heroInfo.getStamina()
					+ "/" + HeroInfoClient.getMaxStamina());
		}

		ViewUtil.setText(holder.ability, "总战力：" + heroInfo.getHeroAbility());

		StringBuilder buf = new StringBuilder("统率：");
		List<HeroArmPropInfoClient> list = heroInfo.getArmPropInfos();
		for (int i = 0; i < list.size(); i++) {
			buf.append(list.get(i).getHeroTroopName().getSlug());
			if (i != list.size() - 1)
				buf.append("、");
		}
		ViewUtil.setRichText(holder.armProps, buf.toString(), true);
		convertView.setOnClickListener(new ClickListener(heroInfo));

		// 引导用的真的 很奇葩的做法 为了保证程序不挂
		if (HeroChooseListWindow.guideCallBack != null
				&& HeroChooseListWindow.isGuide && position == 0) {
			HeroChooseListWindow.guideCallBack.onCall();
			HeroChooseListWindow.guideCallBack = null;
			HeroChooseListWindow.isGuide = false;
		}
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.hero_choose_list_item;
	}

	private static class ViewHolder {
		ViewGroup iconLayout;
		TextView qualityName, heroName, heroLevel, progressDesc, armProps,
				ability, state, staminaTitle;
		ProgressBar progressBar;
	}

	private class ClickListener implements OnClickListener {
		private HeroInfoClient hic;

		public ClickListener(HeroInfoClient hic) {
			this.hic = hic;
		}

		@Override
		public void onClick(View v) {
			if (null != heroInfoClient) {
				boolean set = true;
				if (heroInfoClient.getId() == hic.getId()) {
					heroInfoClient.setInvalid();
					set = false;
				} else {
					heroInfoClient.update(hic);

				}
				if (mCallBack != null) {
					mCallBack.onCall();
				}

				Config.getController().goBack();
				if (mCallBackAppear != null) {
					String richText = "";
					if (hic != null) {
						richText = hic.getColorTypeName()
								+ " "
								+ StringUtil.getHeroName(hic.getHeroProp(),
										hic.getHeroQuality()) + "  "
								+ (set ? "上场" : "卸下");
					}
					mCallBackAppear.onCall(richText, true);
				}
//				else
//				{
//					new HeroSetTip(hic, set).show();
//				}
			}

		}

	}

}
