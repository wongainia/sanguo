package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.widget.TextView;
import android.view.View;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.protos.BattleAttackType;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.thread.ViewRichTextCallBack;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

/**
 * 开始崛起成功弹出框
 * 
 */
public class FiefSucTip extends Alert {

	private static final int layout = R.layout.alert_fief_msg;

	private View content, armInfo, foodInfo, lockTimeInfo, rmbInfo,
			staminaInfo, itemInfo;
	private TextView armCount, foodCount, locktimeCount, title, rmbCount, desc,
			armType, staminaCount, itemCount, itemDesc;

	private int armCnt, foodCnt, lockTimeCnt, rmbCnt, staminaCnt;
	private long tileId;
	private int type, battleType;
	private List<ItemBag> itemBags;

	public FiefSucTip() {
		this.dialog = new TouchCloseDialog(controller.getUIContext(),
				new Dismiss());
		content = controller.inflate(layout);
		armInfo = content.findViewById(R.id.armInfo);
		itemInfo = content.findViewById(R.id.itemInfo);
		foodInfo = content.findViewById(R.id.foodInfo);
		lockTimeInfo = content.findViewById(R.id.locktimeInfo);
		rmbInfo = content.findViewById(R.id.rmbInfo);
		staminaInfo = content.findViewById(R.id.staminaInfo);
		itemDesc = (TextView) content.findViewById(R.id.itemDesc);
		itemCount = (TextView) content.findViewById(R.id.itemCount);
		armType = (TextView) content.findViewById(R.id.armType);
		armCount = (TextView) content.findViewById(R.id.armCount);
		rmbCount = (TextView) content.findViewById(R.id.rmbCnt);
		staminaCount = (TextView) content.findViewById(R.id.staminaCnt);
		foodCount = (TextView) content.findViewById(R.id.foodCount);
		locktimeCount = (TextView) content.findViewById(R.id.lockTime);
		title = (TextView) content.findViewById(R.id.title);
		desc = (TextView) content.findViewById(R.id.desc);

	}

	public void show(int type, List<ItemBag> itemBags, int armCnt, int foodCnt,
			int lockTime, int rmb, int stamina, long tileId) {
		this.type = type;
		this.itemBags = itemBags;
		this.armCnt = armCnt;
		this.foodCnt = foodCnt;
		this.lockTimeCnt = lockTime;
		this.rmbCnt = rmb;
		this.staminaCnt = stamina;
		this.tileId = tileId;
		setValue();
		this.show(content);
	}

	/**
	 * 
	 * @param type
	 * @param armCnt
	 * @param foodCnt
	 * @param lockTime
	 * @param moveTime
	 * @param rmb
	 * @param user
	 * @param tileId
	 */
	public void show(int type, List<ItemBag> itemBags, int battleType,
			int armCnt, int foodCnt, int lockTime, int rmb, int stamina,
			long tileId) {
		this.battleType = battleType;
		this.show(battleType, itemBags, armCnt, foodCnt, lockTime, rmb,
				stamina, tileId);
	}

	private void setValue() {
		switch (type) {
		case Constants.TROOP_DISPATCH:
			ViewUtil.setText(title, "调遣成功!");
			if (armCnt == 0)
				ViewUtil.setText(armType, "调遣将领:");
			else
				ViewUtil.setText(armType, "调遣兵力:");
			new AddrLoader(desc, tileId, "你调往",
					"的部队已经出发!<br>可以在目标领地的[详情]中查看军队详情", AddrLoader.TYPE_SUB);
			break;
		case Constants.TROOP_REINFORCE:
			ViewUtil.setText(title, "援兵出发!");
			ViewUtil.setText(armType, "援军兵力:");
			new AddrLoader(desc, tileId, "你增援", "的部队已经出发!<br>可以在[查看战场]中查看部队详情",
					AddrLoader.TYPE_SUB);
			break;
		case Constants.TROOP_OCCUPY:
		case Constants.TROOP_PLUNDER:
		case Constants.TROOP_DUEL:
		case Constants.TROOP_MASSACRE:
			ViewUtil.setText(title, "军队出征!");
			ViewUtil.setText(armType, "出征兵力:");
			ViewUtil.setText(desc, "部队已经出发,请及时发起进攻");
			break;
		default:
			break;
		}

		if (armCnt > 0) {
			ViewUtil.setVisible(armInfo);
			ViewUtil.setText(armCount, armCnt);
		} else {
			ViewUtil.setGone(armInfo);
		}

		// 物品
		if (ListUtil.isNull(itemBags)) {
			ViewUtil.setGone(itemInfo);
		} else {
			ViewUtil.setVisible(itemInfo);
			ViewUtil.setText(itemCount, Math.abs(itemBags.get(0).getCount()));
			ViewUtil.setText(itemDesc, "消耗"
					+ itemBags.get(0).getItem().getName() + ":");

			new ViewImgScaleCallBack(itemBags.get(0).getItem().getImage(),
					content.findViewById(R.id.itemimg),
					30 * Config.SCALE_FROM_HIGH, 30 * Config.SCALE_FROM_HIGH);
			// new ViewRichTextCallBack(itemBags.get(0).getItem().getImage(),
			// "duel_icon", "", "消耗" + itemBags.get(0).getItem().getName()
			// + ":", itemDesc, 30, 30);
		}

		if (foodCnt != 0) {
			ViewUtil.setVisible(foodInfo);
			ViewUtil.setText(foodCount, StringUtil.abs(foodCnt));
		} else {
			ViewUtil.setGone(foodInfo);
		}

		if (battleType == BattleAttackType.E_BATTLE_DUEL_ATTACK.number) {
			ViewUtil.setVisible(lockTimeInfo);
			ViewUtil.setText(locktimeCount, DateUtil
					.formatSecond(CacheMgr.dictCache.getDictInt(
							Dict.TYPE_BATTLE_DUEL, 3)));
		} else {
			if (lockTimeCnt > 0) {
				ViewUtil.setVisible(lockTimeInfo);
				ViewUtil.setText(locktimeCount,
						DateUtil.formatSecond(lockTimeCnt));

			} else {
				ViewUtil.setGone(lockTimeInfo);
			}
		}

		if (rmbCnt != 0) {
			ViewUtil.setVisible(rmbInfo);
			ViewUtil.setText(rmbCount, StringUtil.abs(rmbCnt) + "元宝");
		} else {
			ViewUtil.setGone(rmbInfo);
		}

		if (staminaCnt != 0) {
			ViewUtil.setVisible(staminaInfo);
			ViewUtil.setText(staminaCount, StringUtil.abs(staminaCnt));
		} else {
			ViewUtil.setGone(staminaInfo);
		}

	}

	class Dismiss implements CallBack {
		@Override
		public void onCall() {
			FiefSucTip.this.dismiss();
		}
	}

}
