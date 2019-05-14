package com.vikings.sanguo.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.RichFiefInfoClient;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class MyFiefUI extends BaseUI implements OnClickListener {
	private ViewGroup content;

	private ArrayList<BriefFiefInfoClient> fiefs = new ArrayList<BriefFiefInfoClient>();

	private FiefCompare cc = new FiefCompare();

	public MyFiefUI() {
		content = (ViewGroup) controller.findViewById(R.id.myFiefScroll);
	}

	private View getConvertView(int index) {
		if (index < content.getChildCount())
			return content.getChildAt(index);
		else {
			View v = controller.inflate(R.layout.my_fief_item);
			ViewUtil.setImage(v.findViewById(R.id.bg),
					R.drawable.fief_scroll_cell);
			content.addView(v);
			return v;
		}
	}

	public void updateFief() {
		fiefs.clear();

		// 自己领地
		List<RichFiefInfoClient> myFief = Account.richFiefCache.getAll();
		for (RichFiefInfoClient f : myFief) {
			// 解决问题（ #5519）：地图界面有2个自己的主城
			if (f.getFiefid() == 0
					&& Account.manorInfoClient.getPos() != ManorInfoClient.POS_EMPTY)
				continue;
			fiefs.add(f.brief());
		}
		// 合并战斗领地
		List<BriefBattleInfoClient> battles = Account.briefBattleInfoCache
				.getAll();
		for (BriefBattleInfoClient b : battles) {
			if (fiefs.contains(b.getBfic()))
				continue;
			fiefs.add(b.getBfic());
		}

		Collections.sort(fiefs, cc);

		int size = fiefs.size();

		for (int i = 0; i < size; i++) {
			View v = getConvertView(i);
			BriefFiefInfoClient r = fiefs.get(i);

			int curBattleState = TroopUtil.getCurBattleState(
					r.getBattleState(), r.getBattleTime());

			TextView battleDesc = (TextView) v.findViewById(R.id.battleDesc);
			if (BattleStatus.isInBattle(curBattleState)) {
				ViewUtil.setVisible(v, R.id.stat);
				setBattleIcon(r, v.findViewById(R.id.stat));
				ViewUtil.setGone(v, R.id.resStat);

				// if (curBattleState == BattleStatus.BATTLE_STATE_SURROUND) {
				// ViewUtil.setVisible(battleDesc);
				// ViewUtil.setText(battleDesc, "围城中");
				// battleDesc.setShadowLayer(2.0f, 0, 0, Color.GREEN);
				// } else
				if (curBattleState == BattleStatus.BATTLE_STATE_SURROUND_END) {
					ViewUtil.setVisible(battleDesc);
					ViewUtil.setText(battleDesc, "围城结束");
					battleDesc.setShadowLayer(2.0f, 0, 0, Color.RED);
				} else {
					ViewUtil.setGone(v, R.id.battleDesc);
				}
			} else {
				ViewUtil.setGone(battleDesc);
				ViewUtil.setGone(v, R.id.stat);
				ViewUtil.setGone(v, R.id.resStat);
				if (r.getUserId() == Account.user.getId() && r.isResource()) {
					BuildingInfoClient b = r.getBuilding();
					if (b != null) {
						int count = b.produce(r.getProp().getBuildingStatus(),
								Account.user.getLastLoginTime());
						int max = b.maxStore();
						if (count >= max / 2) {
							ViewUtil.setVisible(v, R.id.resStat);
							ViewUtil.setImage(v, R.id.resIcon, ReturnInfoClient
									.getAttrTypeIcon(b.getResourceStatus()));
						}
					}
				}
			}

			ViewUtil.setText(v, R.id.fiefName, r.getName());

			ViewUtil.setRichText(v.findViewById(R.id.heroCnt), "#hero_limit#"
					+ r.getHeroCount(), true, 15, 15);
			ViewUtil.setRichText(v.findViewById(R.id.armCnt), "#arm#"
					+ CalcUtil.turnToHundredThousand(r.getUnitCount()), true,
					15, 15);
			new ViewImgScaleCallBack(r.getIcon(),
					v.findViewById(R.id.fiefIcon),
					Constants.FIEF_ICON_SCROLL_WIDTH * Config.SCALE_FROM_HIGH,
					Constants.FIEF_ICON_SCROLL_HEIGHT * Config.SCALE_FROM_HIGH);

			controller.getBattleMap().updateFief(r);

			v.setTag(r);
			v.setOnClickListener(this);
		}

		for (int i = fiefs.size(); i < content.getChildCount(); i++) {
			content.removeViewAt(i);
		}
	}

	private void setBattleIcon(BriefFiefInfoClient r, View view) {
		BriefBattleInfoClient battle = null;

		List<BriefBattleInfoClient> battles = Account.briefBattleInfoCache
				.getAll();
		for (BriefBattleInfoClient b : battles) {
			if (b.getDefendFiefid() == r.getId()) {
				battle = b;
				break;
			}

		}

		if (null != battle) {
			if (battle.getAttacker() == Account.user.getId()) {
				ViewUtil.setImage(view, R.drawable.battle_state_attack);
			} else if (battle.getDefender() == Account.user.getId()) {
				ViewUtil.setImage(view, R.drawable.battle_state_defend);
			} else {
				ViewUtil.setImage(view, R.drawable.battle_state_reinforce);
			}
		}

	}

	@Override
	public void onClick(View v) {
		BriefFiefInfoClient r = (BriefFiefInfoClient) v.getTag();
		setItemBg(r);
		Config.getController().getBattleMap().moveToFief(r.getId(), true);
	}

	private void setItemBg(BriefFiefInfoClient brief) {
		if (null != content) {
			int count = content.getChildCount();
			for (int i = 0; i < count; i++) {
				BriefFiefInfoClient r = (BriefFiefInfoClient) content
						.getChildAt(i).getTag();
				if (null != r && r.getId() == brief.getId()) {
					ViewUtil.setImage(content.getChildAt(i), R.id.bg,
							R.drawable.fief_scroll_cell_selected);
				} else {
					ViewUtil.setImage(content.getChildAt(i), R.id.bg,
							R.drawable.fief_scroll_cell);
				}

			}
		}

	}

	/**
	 * 保证领地按规则排序
	 * 
	 * @author chenqing
	 * 
	 */
	private class FiefCompare implements Comparator<BriefFiefInfoClient> {

		@Override
		public int compare(BriefFiefInfoClient f1, BriefFiefInfoClient f2) {
			// 有战斗的排前面
			if (f1.isInBattle() && !f2.isInBattle())
				return -1;
			if (!f1.isInBattle() && f2.isInBattle())
				return 1;
			// 自己排前面
			if (f1.getUserId() == Account.user.getId()
					&& f2.getUserId() != Account.user.getId())
				return -1;
			if (f1.getUserId() != Account.user.getId()
					&& f2.getUserId() == Account.user.getId())
				return 1;
			// 主城在前面
			if (f1.isCastle() && !f2.isCastle())
				return -1;
			if (!f1.isCastle() && f2.isCastle())
				return 1;
			return (int) (f1.getId() - f2.getId());
		}
	}

}
