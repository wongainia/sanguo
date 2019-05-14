package com.vikings.sanguo.widget;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleLogReturnInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.protos.ReturnAttrInfo;
import com.vikings.sanguo.protos.ReturnInfo;
import com.vikings.sanguo.protos.ReturnThingInfo;
import com.vikings.sanguo.ui.alert.AllyBootyTip;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class BattleResultAwardTab {
	private BattleLogInfoClient blic;
	protected ViewGroup bonusList, plunderedList;
	private ViewGroup contentGroup;

	public BattleResultAwardTab(BattleLogInfoClient blic, ViewGroup content) {
		this.blic = blic;
		this.contentGroup = content;
		setBonus();
	}

	private void setBonus() {
		if (blic.isMePartner()) {
			bonusList = (ViewGroup) contentGroup.findViewById(R.id.bonusList);
			ViewUtil.setHeroReward(blic);
			setPlayerReward();
		} else {
			ViewUtil.setGone(contentGroup, R.id.bonusFrame);
			ViewUtil.setVisible(contentGroup, R.id.other);
		}
	}

	// 设置玩家战利品信息
	private void setPlayerReward() {
		if (ListUtil.isNull(blic.getDetail().getBattleReturnInfos())) {
			setNoTrophy();
			return;
		}

		// 个人奖励
		List<BattleLogReturnInfoClient> ownReward = blic.getOwnReward();
		if (ListUtil.isNull(ownReward))
			ViewUtil.setGone(contentGroup, R.id.bonusFrame);
		else
			setOwnReward(ownReward);

		// 被掠物资
		List<BattleLogReturnInfoClient> plunderedThing = blic
				.getPlunderedThings();
		if (ListUtil.isNull(ownReward) && ListUtil.isNull(plunderedThing))
			setNoTrophy();

		if (!ListUtil.isNull(plunderedThing))
			setPlunderedThings(plunderedThing);

		// 盟友奖励
		setAllyReward(blic.getAllyReward());
	}

	private void setAllyReward(final List<BattleLogReturnInfoClient> allyReward) {
		if (!ListUtil.isNull(allyReward)) {
			View otherBonusBtn = contentGroup.findViewById(R.id.otherBonus);
			ViewUtil.setVisible(otherBonusBtn);

			if (blic.isMyBattle())
				ViewUtil.setText(otherBonusBtn, "查看盟友奖励");
			else
				ViewUtil.setText(otherBonusBtn, "查看他人奖励");

			otherBonusBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AllyBootyTip(allyReward, blic).show();
				}
			});
		}
	}

	private void setPlunderedThings(
			List<BattleLogReturnInfoClient> plunderedThing) {
		BattleLogReturnInfoClient blric = mergeBattleLogReturnInfoClient(plunderedThing);
		if (null != blric) {
			ViewGroup vg = (ViewGroup) Config.getController().inflate(
					R.layout.user_spoil_item);

			ViewUtil.setGone(vg, R.id.titleFrame);

			int idx = setReturnInfo(blric, vg);

			if (idx > 0) {
				if (ViewUtil.isGone(contentGroup
						.findViewById(R.id.plunderFrame))) {
					ViewUtil.setVisible(contentGroup, R.id.plunderFrame);
					plunderedList = (ViewGroup) contentGroup
							.findViewById(R.id.plunderList);
				}
				if (vg != null && plunderedList != null) {
					plunderedList.addView(vg);
				}
			}
		}
	}

	private void setOwnReward(List<BattleLogReturnInfoClient> ownReward) {
		if (ListUtil.isNull(ownReward))
			return;
		for (BattleLogReturnInfoClient it : ownReward) {
			ViewGroup vg = (ViewGroup) Config.getController().inflate(
					R.layout.user_spoil_item);

			// 生成标题
			ViewUtil.setImage(vg, R.id.type, it.getTypeImg());

			// 副本最佳战绩
			if (it.isRecordAward())
				ViewUtil.setBestRecord(vg, blic.getDetail().getRecord());

			int idx = setReturnInfo(it, vg);

			if (idx > 0)
				bonusList.addView(vg);
		}
	}

	// 没有战利品
	private void setNoTrophy() {
		ViewUtil.setVisible(contentGroup, R.id.other);
		ViewUtil.setGone(contentGroup, R.id.thingFrame); // bonusFrame
		ViewUtil.setText(contentGroup, R.id.other, "很抱歉,你没有获得战利品!");
	}

	private int setReturnInfo(BattleLogReturnInfoClient it, ViewGroup vg) {
		int idx = 0;

		ViewGroup line1 = (ViewGroup) vg.findViewById(R.id.line1);
		ViewGroup line2 = (ViewGroup) vg.findViewById(R.id.line2);

		// 解析Attr
		List<ReturnAttrInfo> rais = it.getReturnInfo().getRaisList();
		for (ReturnAttrInfo rai : rais) {
			View v = null;

			if (isShowPlayerResource(rai))
				v = ViewUtil.getItemLine(ReturnInfoClient
						.getAttrTypeIconName(rai.getType().intValue()),
						ReturnInfoClient.getAttrTypeName(rai.getType()
								.intValue()), "×" + rai.getValue());
			else if (isShowOwnHeroExp(it, rai))
				v = ViewUtil.getItemLine("hero_exp.png", "将领经验",
						"×" + rai.getValue()/* blic.getOwnHeroUpdateExp() */);
			// v = ViewUtil.getItemLine("hero_exp.png", "武将经验",
			// "×" + blic.getOwnHeroUpdateExp());

			if (null != v)
				idx = ViewUtil.setLine(line1, line2, idx, v);
		}

		for (ItemBag ibIt : it.getReturnInfoClient().getItemPack()) {
			if (ibIt.getCount() > 0) {
				Item item = ibIt.getItem();
				View v = ViewUtil.getItemLine(item.getImage(), item.getName(),
						"×" + ibIt.getCount());
				idx = ViewUtil.setLine(line1, line2, idx, v);
			}
		}

		for (BuildingInfoClient bicIt : it.getReturnInfoClient().getBuildings()) {
			BuildingProp buildingProp = bicIt.getProp();
			View v = ViewUtil.getItemLine(buildingProp.getImage(),
					buildingProp.getBuildingName(), "×1");
			idx = ViewUtil.setLine(line1, line2, idx, v);
		}

		for (ArmInfoClient aiIt : it.getReturnInfoClient().getArmInfos()) {
			if (aiIt.getCount() > 0) {
				TroopProp tp = (TroopProp) aiIt.getProp();
				View v = ViewUtil.getItemLine(tp.getIcon(), tp.getName(), "×"
						+ aiIt.getCount());
				idx = ViewUtil.setLine(line1, line2, idx, v);
			}
		}

		for (HeroInfoClient hicIt : it.getReturnInfoClient().getHeroInfos()) {
			HeroProp hp = hicIt.getHeroProp();
			View v = ViewUtil.getItemLine(hp.getIcon(), hp.getName(), "×1");
			idx = ViewUtil.setLine(line1, line2, idx, v);
		}

		for (EquipmentInfoClient eic : it.getReturnInfoClient().getEquipInfos()) {
			PropEquipment prop = eic.getProp();
			View v = ViewUtil.getItemLine(prop.getIcon(), prop.getName(), "×1");
			idx = ViewUtil.setLine(line1, line2, idx, v);
		}

		return idx;
	}

	private boolean isShowPlayerResource(ReturnAttrInfo rai) {
		return rai.getValue() > 0 && AttrData.isShowAttr(rai.getType());
	}

	private boolean isShowOwnHeroExp(BattleLogReturnInfoClient it,
			ReturnAttrInfo rai) {
		return rai.getType() == AttrType.ATTR_TYPE_HERO_EXP.number
				&& it.getReturnInfo().getUserid() == Account.user.getId();
	}

	// 仅用于合并被掠夺时的损失
	private BattleLogReturnInfoClient mergeBattleLogReturnInfoClient(
			List<BattleLogReturnInfoClient> ls) {
		if (ListUtil.isNull(ls))
			return null;

		ReturnInfo ri = new ReturnInfo();
		List<ReturnAttrInfo> rais = new ArrayList<ReturnAttrInfo>();
		ri.setRaisList(rais);
		List<ReturnThingInfo> rtis = new ArrayList<ReturnThingInfo>();
		ri.setRtisList(rtis);

		for (BattleLogReturnInfoClient it : ls) {
			ReturnInfo src = it.getReturnInfo();
			ListUtil.mergeReturnAttrInfo(src.getRaisList(), ri.getRaisList());
			ListUtil.mergeReturnThingInfo(src.getRtisList(), ri.getRtisList());
		}

		return BattleLogReturnInfoClient.createBattleLogReturnInfoClient(ri, ls
				.get(0).getType());
	}
}
