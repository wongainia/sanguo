package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.model.BattleLogHeroInfoClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.HeroArmPropClient;
import com.vikings.sanguo.model.HeroTroopName;
import com.vikings.sanguo.model.JumpTargetPanel;
import com.vikings.sanguo.model.ReturnHeroInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class BattleHeroUpdateSucTip extends ResultAnimTip {

	private ReturnHeroInfoClient rhic;
	private BattleLogInfoClient blic;
	private BattleLogHeroInfoClient blhic;

	public BattleHeroUpdateSucTip(ReturnHeroInfoClient rhic,
			BattleLogInfoClient blic) {
		super(true);
		this.rhic = rhic;
		this.blic = blic;
		List<BattleLogHeroInfoClient> blhics = blic.getHeroInfos(rhic
				.getUserid());
		if (blhics != null && blhics.size() > 0) {
			for (BattleLogHeroInfoClient info : blhics) {
				if (rhic.getId() == info.getId()) {
					blhic = info;
					break;
				}
			}
		}
	}

	public void show() {
		if (null == blhic)
			return;
		SoundMgr.play(R.raw.sfx_evolution);
		super.show(null, false);
		// 任意1星将领达到10级可进化时触发
		if (getHeroLvl() == 10) {
			StepMgr.startStep401(StringUtil.getHeroTypeName(rhic
					.getHeroQuality())
					+ "   "
					+ StringUtil.getHeroName(rhic.getHeroProp(),
							rhic.getHeroQuality()));
		}
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// 处理将领升级框框
				ViewUtil.setHeroReward(blic);
			}
		});
	}

	private void setArmPorp(ViewGroup propsLayout) {
		// 如有升级，则统率上限上升
		List<HeroArmPropClient> hapc = rhic.getDiffArmProps();
		if (null != hapc && null != blhic) {
			List<TextView> profLs = new ArrayList<TextView>();

			for (int i = 0; i < blhic.getArmPropInfos().size(); i++) {
				HeroArmPropClient myIt = blhic.getArmPropInfos().get(i);
				for (HeroArmPropClient upIt : hapc) {
					if (myIt.getType() == upIt.getType()) {
						HeroTroopName htn = upIt.getHeroTroopName();
						// 设置将领统率上限增加
						profLs.add(getPropText(htn.getName() + "上限",
								myIt.getMaxValue(),
								myIt.getMaxValue() + upIt.getMaxValue()));

						// 设置将领统率增加
						profLs.add(
								i,
								getPropText(htn.getName(), myIt.getValue(),
										myIt.getValue() + upIt.getValue()));
					}
				}
			}

			for (View view : profLs)
				propsLayout.addView(view);
		}
	}

	// private void setHeroLvlHint(ViewGroup propsLayout) {
	// int heroLvl = blic.getHeroLvl(rhic.getId());
	// if (-1 == heroLvl)
	// return;
	//
	// // 小于5级且获取经验不为0
	// int maxLvl = CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 21);
	//
	// if (rhic.getExpDiff() != 0) {
	// // dict:1409:21级以上只能通过PVP获取经验
	// if (heroLvl >= maxLvl)
	// str += "(LV" + maxLvl + "以上的将领只能通过出征其他玩家获得经验)";
	//
	// ViewUtil.setRichText(v, StringUtil.color(str, R.color.color11));
	// propsLayout.addView(v);
	// }
	// }

	protected int getHeroLvl() {
		int curLevel = 0;
		List<BattleLogHeroInfoClient> blhics = blic.getHeroInfos(rhic
				.getUserid());
		BattleLogHeroInfoClient blhic = null;
		if (blhics != null && blhics.size() > 0) {
			for (BattleLogHeroInfoClient info : blhics) {
				if (rhic.getHeroId() == info.getHeroId()) {
					blhic = info;
					break;
				}
			}
			if (blhic != null) {
				return blhic.getLevel() + rhic.getLevelDiff();
			}
		}
		// return blic.getHeroInfos(rhic.getUserid()).get(0/* 取其中一个将领就行 */)
		// .getLevel()
		// + rhic.getLevelDiff();

		for (int i = 0; i < blic.getHeroInfos(rhic.getUserid()).size(); i++) {
			if (blic.getHeroInfos(rhic.getUserid()).get(i).getId() == rhic
					.getId()) {
				curLevel = blic.getHeroInfos(rhic.getUserid()).get(i)
						.getLevel()
						+ rhic.getLevelDiff();
				break;
			}
		}
		return curLevel;
	}

	@Override
	protected int getDrawable() {
		return R.drawable.txt_gxsj;
	}

	@Override
	protected View getContent() {
		View view = controller.inflate(R.layout.result_hero_levelup,
				rewardLayout, false);

		IconUtil.setHeroIconScale(view.findViewById(R.id.iconLayout), rhic);

		ViewUtil.setBoldRichText(view.findViewById(R.id.heroTalent),
				StringUtil.getHeroTypeName(rhic.getHeroQuality()));
		ViewUtil.setBoldRichText(
				view.findViewById(R.id.heroName),
				StringUtil.getHeroName(rhic.getHeroProp(),
						rhic.getHeroQuality()));

		ViewUtil.setImage(view, R.id.levelIcon1, R.drawable.txt_gxjlsd);
		ViewUtil.setRichText(
				view,
				R.id.level,
				StringUtil.numImgStr("v",
						blhic.getLevel() + rhic.getLevelDiff()));
		ViewUtil.setImage(view, R.id.levelIcon2, R.drawable.txt_ji);

		ViewGroup items = (ViewGroup) view.findViewById(R.id.items);

		// setHeroLvlHint(items);
		if (blhic.getLevel() != 0)
			setHeroLevelUp(items, blhic.getLevel(),
					blhic.getLevel() + rhic.getLevelDiff());
		setArmPorp(items);

		View evoluteBtn = view.findViewById(R.id.goBtn);

		if (blhic.canEvolve(blhic.getLevel() + rhic.getLevelDiff()))
			ViewUtil.setVisible(view, R.id.evolveDesc);
		else
			ViewUtil.setGone(view, R.id.evolveDesc);

		ViewUtil.setGone(evoluteBtn);

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
