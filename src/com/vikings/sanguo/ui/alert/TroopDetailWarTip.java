/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-4-22 下午7:29:42
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleBuff;
import com.vikings.sanguo.model.BattleLogHeroInfoClient;
import com.vikings.sanguo.model.BattlePropDefine;
import com.vikings.sanguo.model.Buff;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.PropTroopName;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.TroopEffectInfo;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class TroopDetailWarTip extends CustomConfirmDialog {
	private TroopProp troopProp;
	// private UserTroopEffectInfo troopEffectInfo;
	// private HeroInfoClient hic;
	// private OtherHeroInfoClient ohic;
	private BattleLogHeroInfoClient blhic;
	// private FiefProp fiefProp;

	private Button close; // 关闭按钮
	private ViewGroup armBuffLayout;// 战斗buff
	private int buffImage[] = { R.drawable.arm_buff_bg,
			R.drawable.arm_debuff_bg };
	private List<ViewGroup> armBuffViews;

	public TroopDetailWarTip() {
		super("士兵详情");
		armBuffLayout = (ViewGroup) content.findViewById(R.id.armbuffLayout);
		armBuffViews = new ArrayList<ViewGroup>();
		close = (Button) content.findViewById(R.id.closeBtn);
	}

	/**
	 * 
	 * @param troopProp
	 * @param blhic
	 * @param troopEffectInfo
	 *            效果中的数值服务器已经合并了各种加成（包括将领技能等）的【差量】， 直接显示即可
	 */
	public void show(TroopProp troopProp, BattleLogHeroInfoClient blhic,
			List<TroopEffectInfo> troopEffectInfo, List<Buff> buffs) {
		if (troopProp == null)
			return;
		this.troopProp = troopProp;
		this.blhic = blhic;
		setTroopProp();// 士兵信息
		setPropValue(troopEffectInfo);// 战斗士兵属性信息Account.armEnhanceCache.getPropByArmId(troopProp.getId())

		initArmBuffViews(buffs);// 士兵buff

		ViewUtil.setImage(content, R.id.poto_right,
				ImageUtil.getMirrorBitmapDrawable("potpourri_bg"));

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		super.show();
	}

	// 根据战斗中获得的buff数量 显示相应的buff
	private void initArmBuffViews(List<Buff> buffs) {
		ViewUtil.setVisible(content, R.id.nobuff);
		armBuffLayout.removeAllViews();
		armBuffViews.clear();

		if (buffs.size() > 0) {
			ViewUtil.setGone(content, R.id.nobuff);

			for (int i = 0; i < buffs.size(); i++) {
				ViewGroup viewGroup = (ViewGroup) controller
						.inflate(R.layout.alert_armdetail_buff);
				armBuffLayout.addView(viewGroup);
				armBuffViews.add(viewGroup);

				Buff bf = buffs.get(i);
				try {
					BattleBuff bb = (BattleBuff) CacheMgr.battleBuffCache
							.get(bf.buffId);

					View imageBuffBg = viewGroup.findViewById(R.id.armbuff_bg);// buff背景图

					// TODO Buff背景图区分增益、减益效果
					if (i == 2) {// buff的背景图
						imageBuffBg.setBackgroundResource(buffImage[1]);
					} else {// debuff的背景图
						imageBuffBg.setBackgroundResource(buffImage[0]);
					}

					View imageBuff = viewGroup.findViewById(R.id.armbuff);// buff图片
					new ViewImgScaleCallBack(bb.getIcon(), imageBuff,
							Constants.COMMON_ICON_WIDTH,
							Constants.COMMON_ICON_HEIGHT);

					View textBuffName = viewGroup
							.findViewById(R.id.armbuff_name);// buff名字
					ViewUtil.setText(textBuffName,
							StringUtil.getNCharStr(bb.getName(), 4));

				} catch (GameException e) {
					e.printStackTrace();
				}

				// TODO 战斗士兵buff的点击事件效果待做 2014-4-22
				// viewGroup//buff长按事件---->显示buff描述
			}
		}
	}

	private void setTroopProp() {
		new ViewImgScaleCallBack(troopProp.getIcon(),
				content.findViewById(R.id.icon), Constants.COMMON_ICON_WIDTH,
				Constants.COMMON_ICON_HEIGHT);
		ViewUtil.setText(content, R.id.troopName, troopProp.getName());

		try {
			PropTroopName ptn = (PropTroopName) CacheMgr.propTroopNameCache
					.get((int) troopProp.getType());
			ViewUtil.setRichText(content, R.id.troopType, ptn.getName(), true);

			ViewUtil.setImage(content, R.id.armicon, troopProp.getSmallIcon());
		} catch (GameException e) {
			Log.e("TroopDetailTip", e.getErrorMsg());
		}

		ViewUtil.setText(content, R.id.desc, troopProp.getDesc());
	}

	// 粮草消耗
	private String getFoodValue(int food) {
		float value = food / 1000f;
		if (value > 10) {
			return String.valueOf((int) value);
		} else {
			if (value >= 0.1)
				return CalcUtil.format(value);
			else if (value < 0.1 && value >= 0.01)
				return CalcUtil.format2(value);
			else
				return CalcUtil.format3(value);
		}
	}

	// 战斗效果暴击率
	private String getCritRate(List<TroopEffectInfo> troopEffectInfo) {
		return getCritRateDesc(getBattleLogEffectVal(
				BattlePropDefine.PROP_CRIT, troopEffectInfo));
	}

	// 暴率
	private String getCritRateDesc(int crit) {
		String critRateDesc = "";
		if (crit < 0) {
			critRateDesc = " -" + CalcUtil.format2(crit / 10000f) + "%";
		} else {
			critRateDesc = " +" + CalcUtil.format2(crit / 10000f) + "%";
		}

		return StringUtil.color(critRateDesc,
				controller.getResourceColorText(R.color.color19));
	}

	// 暴击率基础值
	private String getCritRateValue(int crit) {
		return CalcUtil.format2(crit / 10000f) + "%";
	}

	// 战斗效果暴伤
	private String getCritValueWar(List<TroopEffectInfo> troopEffectInfo) {
		return getCritValue(getBattleLogEffectVal(
				BattlePropDefine.PROP_CRIT_MULTIPLE, troopEffectInfo));
	}

	// 暴伤加成
	private String getCritValue(int critMultiple) {
		String critValue = "";

		if (critMultiple < 0) {
			critValue = " -" + critMultiple + "%";
		} else {
			critValue = " +" + critMultiple + "%";
		}

		return StringUtil.color(critValue,
				controller.getResourceColorText(R.color.color19));
	}

	// 免暴伤
	private String getRemit(int antiCrit) {
		int param = CacheMgr.dictCache.getDictInt(Dict.TYPE_ARM_PROP_EFFECT, 5);
		float rate = antiCrit / 10000f * param;
		return StringUtil.color(CalcUtil.format2(rate) + "%",
				controller.getResourceColorText(R.color.color19));
	}

	// 三国免爆率（1=0.01%）=韧性
	private String getAntiCritRateDesc(int antiCrit) {
		return StringUtil.color(CalcUtil.format2(antiCrit / 10000f) + "%",
				controller.getResourceColorText(R.color.color19));
	}

	// 三国暴率效果加成值*********直接提取本级强化配置的加成
	private String getColorBattleLogExtValue(int value) {
		return StringUtil.color(" +" + value, R.color.color19);
	}

	private String getCilorBattleLogExt(List<TroopEffectInfo> troopEffectInfo) {
		return getColorBattleLogExtValue(getBattleLogEffectVal(
				BattlePropDefine.PROP_ANTICRIT, troopEffectInfo));
	}

	// 差量的显示效果
	private String getColorBattleLogExtValue(int propId,
			List<TroopEffectInfo> effect) {

		int effectValue = (int) (getBattleLogEffectVal(propId, effect));
		String effectDesc = "";

		if (effectValue < 0) {
			effectDesc = "-" + effectValue;
		} else {
			effectDesc = "+" + effectValue;
		}

		return StringUtil.color(effectDesc, R.color.color19);
	}

	// 服务器给的差量
	private int getBattleLogEffectVal(int propId, List<TroopEffectInfo> effect) {
		int extValue = 0;
		if (!ListUtil.isNull(effect)) {
			for (TroopEffectInfo tei : effect) {
				if (troopProp.getId() == tei.getArmid().intValue()
						&& propId == tei.getAttr().intValue()) {
					extValue = tei.getValue();
					break;
				}
			}
		}
		return extValue;
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_troop_detail_war, tip, false);
	}

	// 士兵新的属性值
	private void setPropValue(List<TroopEffectInfo> troopEffectInfo) {

		// 生命值
		ViewUtil.setRichText(
				content,
				R.id.hp,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_LIFE)
						+ ": "
						+ troopProp.getHp()
						+ getColorBattleLogExtValue(BattlePropDefine.PROP_LIFE,
								troopEffectInfo));

		// 攻击
		ViewUtil.setRichText(
				content,
				R.id.atk,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_ATTACK)
						+ ": "
						+ troopProp.getAttack()
						+ getColorBattleLogExtValue(
								BattlePropDefine.PROP_ATTACK, troopEffectInfo));

		// 防御
		ViewUtil.setRichText(
				content,
				R.id.def,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_DEFEND)
						+ ": "
						+ troopProp.getDefend()
						+ getColorBattleLogExtValue(
								BattlePropDefine.PROP_DEFEND, troopEffectInfo));

		// 射程
		ViewUtil.setRichText(
				content,
				R.id.range,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_RANGE)
						+ ": "
						+ troopProp.getRange()
						+ getColorBattleLogExtValue(
								BattlePropDefine.PROP_RANGE, troopEffectInfo));

		// 暴击率
		ViewUtil.setRichText(content, R.id.crit,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_CRIT) + ": "
						+ getCritRateValue(troopProp.getCritRate())
						+ getCritRate(troopEffectInfo));

		// 暴伤
		ViewUtil.setRichText(content, R.id.critTitle,
				"暴伤: " + troopProp.getCritMultiple() + "% "
						+ getCritValueWar(troopEffectInfo));

		// 韧性
		ViewUtil.setRichText(
				content,
				R.id.antiCritTitle,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_ANTICRIT)
						+ ": "
						+ troopProp.getAntiCrit()
						+ getCilorBattleLogExt(troopEffectInfo)
						+ " ("
						+ getAntiCritRateDesc(troopProp.getAntiCrit()
								+ getBattleLogEffectVal(
										BattlePropDefine.PROP_ANTICRIT,
										troopEffectInfo))
						+ "免爆率，减免"
						+ getRemit(troopProp.getAntiCrit()
								+ getBattleLogEffectVal(
										BattlePropDefine.PROP_ANTICRIT,
										troopEffectInfo)) + "暴击伤害)");

		// 出征消耗
		ViewUtil.setRichText(content, R.id.food, "出征消耗: #fief_food#"
				+ getFoodValue(troopProp.getFood()) + "/次", true);

		// 驻防消耗
		ViewUtil.setRichText(content, R.id.foodConsume, "驻防消耗: #fief_food#"
				+ getFoodValue(troopProp.getFoodConsume()) + "/小时", true);
	}
}
