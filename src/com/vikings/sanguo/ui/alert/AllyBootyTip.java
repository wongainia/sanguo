/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-7-27 下午4:15:10
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleLogReturnInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.ReturnHeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.protos.ReturnAttrInfo;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.PageListView;

public class AllyBootyTip extends PageListView {
	private List<BattleLogReturnInfoClient> blricLs;
	private BattleLogInfoClient blic;

	public AllyBootyTip(List<BattleLogReturnInfoClient> blricLs,
			BattleLogInfoClient blic) {
		super();
		setButton(FIRST_BTN, "关闭", closeL);
		ViewUtil.setGone(content, R.id.content_title);
		setTitle("援军掉落");
		this.blricLs = blricLs;
		this.blic = blic;
		firstPage();
		ViewUtil.setImage(listView, R.drawable.setoff_cnt_sg);
		listView.setSelector(android.R.color.transparent);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new AllyBootyAdapter();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		int start = resultPage.getCurIndex();
		int end = start + resultPage.getPageSize();
		if (end > blricLs.size())
			end = blricLs.size();
		if (start > end) {
			resultPage.setResult(new ArrayList<Integer>());
			resultPage.setTotal(blricLs.size());
			return;
		}

		List<BattleLogReturnInfoClient> tmp = blricLs.subList(start, end);
		List<Integer> ids = new ArrayList<Integer>();
		for (BattleLogReturnInfoClient it : tmp) {
			if (!ids.contains(it.getUserId()))
				ids.add(it.getUserId());
		}

		List<BriefUserInfoClient> users = CacheMgr.userCache.get(ids);

		for (BriefUserInfoClient user : users) {
			for (BattleLogReturnInfoClient it : tmp) {
				if (it.getUserId() == user.getId())
					it.setUser(user);
			}
		}

		resultPage.setResult(tmp);
		resultPage.setTotal(blricLs.size());
	}

	@Override
	protected void updateUI() {
		super.updateUI();
		dealwithEmptyAdpter();
	}

	@Override
	public void handleItem(Object o) {

	}

	@Override
	protected String getEmptyShowText() {
		return "盟军没有获得奖励";
	}

	class AllyBootyAdapter extends ObjectAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = Config.getController().inflate(getLayoutId());
				ViewHolder holder = new ViewHolder();
				holder.name = convertView.findViewById(R.id.name);
				holder.line = (ViewGroup) convertView.findViewById(R.id.line);
				holder.noBooty = convertView.findViewById(R.id.noBooty);
				holder.header_layout = (ViewGroup) convertView
						.findViewById(R.id.header_layout);
				convertView.setTag(holder);
			}
			this.setViewDisplay(convertView, getItem(position), position);
			return convertView;
		}

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			BattleLogReturnInfoClient blric = (BattleLogReturnInfoClient) getItem(index);
			ReturnInfoClient ric = blric.getReturnInfoClient();
			List<ShowItem> showItems = ric.showReturn(true);
			ViewHolder holder = (ViewHolder) v.getTag();
			ViewUtil.setText(holder.name, blric.getUser().getNickName() + " 的"
					+ blric.getTypeName()); // " 的战斗奖励");

			if (index == 0) {
				ViewUtil.setVisible(holder.header_layout);
				ViewUtil.setImage(holder.header_layout, R.id.poto_right1,
						ImageUtil.getMirrorBitmapDrawable("potpourri_bg"));
			} else {
				ViewUtil.setGone(holder.header_layout);
			}

			int idx = 0;

			holder.line.removeAllViews();

			ViewGroup vg = (ViewGroup) controller
					.inflate(R.layout.battles_award_item);

			ViewGroup content = (ViewGroup) vg.findViewById(R.id.content);
			ViewGroup content1 = (ViewGroup) vg.findViewById(R.id.content1);
			if (!ListUtil.isNull(showItems))
				for (int i = 0; i < showItems.size(); i++) {
					if (showItems.get(i).getCount() > 0) {
						if ((i + 1) % 2 == 0) {
							content1.addView(ViewUtil.getShowItemView(
									showItems.get(i), R.color.color5, false,
									false, R.drawable.shopitem_bg1));
						} else {
							content.addView(ViewUtil.getShowItemView(
									showItems.get(i), R.color.color5, false,
									false, R.drawable.shopitem_bg1));
						}
						idx++;
					}
				}
			holder.line.addView(vg);

			// // 解析Attr
			// List<ReturnAttrInfo> raics = blric.getReturnInfo().getRaisList();
			// for (ReturnAttrInfo raic : raics) {
			// if (raic.getValue() > 0 && AttrData.isShowAttr(raic.getType())) {
			// String icon = ReturnInfoClient.getAttrTypeIconName(raic
			// .getType().intValue());
			// View itemLine = ViewUtil.getShowItemView(icon,
			// ReturnInfoClient.getAttrTypeName(raic.getType()
			// .intValue()), "×" + raic.getValue(),
			// R.color.k7_color6);
			// holder.line.addView(itemLine);
			// idx++;
			// } else if (raic.getType() == AttrType.ATTR_TYPE_HERO_EXP.number)
			// {
			// // 将领经验，只有攻守方才有将领经验
			// for (ReturnHeroInfoClient rhic : blic.getDetail()
			// .getRhics()) {
			// int heroLvl = blic.getHeroLvl(rhic.getId());
			// int exp = rhic.getExpDiff()
			// + rhic.getLevelUpExp(heroLvl);
			// if (exp != 0 // rhic.getExpDiff()
			// && rhic.getUserid() == blric.getUserId()) {
			//
			// View itemLine = ViewUtil.getShowItemView(
			// "hero_exp.png", "武将经验", "×" + exp,
			// R.color.k7_color6);
			// holder.line.addView(itemLine);
			// idx++;
			// }
			// }
			// }
			// }
			//
			// // 道具
			// for (ItemBag ibIt : ric.getItemPack()) {
			// if (ibIt.getCount() > 0) {
			// Item item = ibIt.getItem();
			// View itemLine = ViewUtil.getShowItemView(item.getImage(),
			// item.getName(), "×" + ibIt.getCount(),
			// R.color.k7_color6);
			// holder.line.addView(itemLine);
			// idx++;
			// }
			// }
			//
			// for (BuildingInfoClient bicIt : ric.getBuildings()) {
			// BuildingProp buildingProp = bicIt.getProp();
			// View itemLine = ViewUtil
			// .getShowItemView(buildingProp.getImage(),
			// buildingProp.getBuildingName(), "×1",
			// R.color.k7_color6);
			// holder.line.addView(itemLine);
			// idx++;
			// }
			//
			// for (ArmInfoClient aiIt : ric.getArmInfos()) {
			// if (aiIt.getCount() > 0) {
			// TroopProp tp = (TroopProp) aiIt.getProp();
			// View itemLine = ViewUtil.getShowItemView(tp.getIcon(),
			// tp.getName(), "×" + aiIt.getCount(),
			// R.color.k7_color6);
			// holder.line.addView(itemLine);
			// idx++;
			// }
			// }
			//
			// for (HeroInfoClient hicIt : ric.getHeroInfos()) {
			// HeroProp hp = hicIt.getHeroProp();
			// View itemLine = ViewUtil.getShowItemView(hp.getIcon(),
			// hp.getName(), "×1", R.color.k7_color6);
			// holder.line.addView(itemLine);
			// idx++;
			// }

			if (0 == idx)
				ViewUtil.setVisible(holder.noBooty);
			else
				ViewUtil.setGone(holder.noBooty);
		}

		@Override
		public int getLayoutId() {
			return R.layout.ally_booty_item;
		}

		class ViewHolder {
			public View name;
			public ViewGroup line, header_layout;
			public View noBooty;
		}
	}
}
