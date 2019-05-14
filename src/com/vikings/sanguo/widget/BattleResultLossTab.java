package com.vikings.sanguo.widget;

import java.util.ArrayList;
import java.util.List;

import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleLossDetail;
import com.vikings.sanguo.model.BattleLossDetailList;
import com.vikings.sanguo.model.BattleSideInfo;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.TroopMoveInfoClient;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class BattleResultLossTab {
	private ViewGroup contentGroup;

	public BattleResultLossTab(BattleLogInfoClient blic, ViewGroup content) {

		BattleSideInfo downSide = blic.getDownSide();//blic.getAtkSide();//blic.getLeftSide()/*getAtkSide()*/;
		BattleSideInfo upSide = blic.getUpSide();//blic.getDefSide();//blic.getRightSide()/*getDefSide()*/;

		BattleLossDetailList downLossLs = downSide.getDeathDetail();
		BattleLossDetailList upLossLs = upSide.getDeathDetail();
		setSideName(blic);

		ViewGroup left = (ViewGroup) content.findViewById(R.id.left);
		if (null == downLossLs
				|| (null != downLossLs && ListUtil.isNull(downLossLs
						.getLossDetailLs())))
			setNoLoss(left);
		else {
			List<BattleLossDetail> leftLoss = downSide.getDeathDetail()
					.getLossDetailLs();

			BattleLossListView lListView = new BattleLossListView(
					(ExpandableListView) left.findViewById(R.id.listView),
					left.findViewById(R.id.loading),
					left.findViewById(R.id.empty), leftLoss, true);
			lListView.firstPage();
		}

		ViewGroup right = (ViewGroup) content.findViewById(R.id.right);
		if (null == upLossLs
				|| (null != upLossLs && ListUtil.isNull(upLossLs
						.getLossDetailLs()))) {
			setNoLoss(right);
		} else {
			List<BattleLossDetail> rightLoss = upSide.getDeathDetail()
					.getLossDetailLs();

			BattleLossListView rListView = new BattleLossListView(
					(ExpandableListView) right.findViewById(R.id.listView),
					right.findViewById(R.id.loading),
					right.findViewById(R.id.empty), rightLoss, false);
			rListView.firstPage();
		}
		TextView lSide = (TextView)content.findViewById(R.id.atkSide);
		TextView rSide = (TextView)content.findViewById(R.id.defSide);
		
		if(blic.isDownAtk())
		{
			lSide.setText("攻方");
			rSide.setText("守方");
		}
		else
		{
			lSide.setText("守方");
			rSide.setText("攻方");
		}
	}

	private void setNoLoss(ViewGroup left) {
		ViewUtil.setGone(left, R.id.listView);
		ViewUtil.setVisible(left, R.id.empty);
		TextView empty = (TextView) left.findViewById(R.id.empty);
		ViewUtil.setText(empty, "无士兵伤亡");
	}

	private void setSideName(BattleLogInfoClient blic) {
		ViewUtil.setText(contentGroup, R.id.atkSide, "攻  方");
		ViewUtil.setText(contentGroup, R.id.defSide, "守  方");
	}

	class BattleLossListView extends CustomListView {
		private List<BattleLossDetail> lossLs;
		private boolean isLeft;

		public BattleLossListView(ExpandableListView listView, View loadView,
				View emptyShow, List<BattleLossDetail> lossLs, boolean isLeft) {
			super(listView, loadView, emptyShow);
			this.lossLs = lossLs;
			this.isLeft = isLeft;
			setAdapterToListView();

			for (int i = 0; i < lossLs.size(); i++)
				this.listView.expandGroup(i);

			this.listView.setOnGroupClickListener(new OnGroupClickListener() {
				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					return true;
				}
			});
		}

		@Override
		protected BaseExpandableListAdapter getAdapter() {
			return new BattleLossAdapter(lossLs, isLeft);
		}

		@Override
		public void getServerData(ResultPage resultPage) throws GameException {
			int start = resultPage.getCurIndex();
			int end = start + resultPage.getPageSize();
			if (end > lossLs.size())
				end = lossLs.size();
			if (start > end) {
				resultPage.setResult(new ArrayList<Integer>());
				resultPage.setTotal(lossLs.size());
				return;
			}

			List<BattleLossDetail> tmp = lossLs.subList(start, end);
			List<Integer> ids = new ArrayList<Integer>();
			for (BattleLossDetail it : tmp) {
				if (!ids.contains(it.getUserId()) && it.getUserId() >= 0)
					ids.add(it.getUserId());
			}

			List<BriefUserInfoClient> users = CacheMgr.userCache.get(ids);

			for (BriefUserInfoClient user : users) {
				for (BattleLossDetail it : tmp) {
					if (it.getUserId() == user.getId())
						it.setName(user.getNickName());
				}
			}

			resultPage.setResult(tmp);
			resultPage.setTotal(lossLs.size());
		}

		@Override
		public void handleItem(Object o) {

		}

		@Override
		protected String getEmptyShowText() {
			return "无士兵伤亡";
		}
	}

	class BattleLossAdapter extends BaseExpandableListAdapter {
		private List<BattleLossDetail> lossLs;
		private boolean isAtk;

		public BattleLossAdapter(List<BattleLossDetail> lossLs, boolean isAtk) {
			this.lossLs = lossLs;
			this.isAtk = isAtk;
		}

		@Override
		public int getGroupCount() {
			return lossLs.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return lossLs.get(groupPosition).getArmInfoLs().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return lossLs.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return lossLs.get(groupPosition).getArmInfoLs().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = Config.getController().inflate(
						R.layout.battle_loss_group);
				TextPaint tp = ((TextView) convertView).getPaint();
				tp.setFakeBoldText(true);
			}

			BattleLossDetail bld = (BattleLossDetail) getGroup(groupPosition);
			String loss = "";
			if (bld.getUserId() == -1)
				loss = "总死亡:" + bld.getTotalLoss();
			else {
				if (bld.getUserId() == Account.user.getId()) {
					loss = "<br>" + bld.getRole() + " 我<br>死亡:"
							+ bld.getTotalLoss();
					loss = StringUtil.color(loss, R.color.color2);
				} else
					loss = "<br>" + bld.getRole() + " " + bld.getName()
							+ "<br>死亡:" + bld.getTotalLoss();
			}

			ViewUtil.setRichText(convertView, loss);

			if (isAtk) {
				((TextView) convertView).setGravity(Gravity.LEFT);
				convertView.setPadding(5, 0, 0, 0);
			} else {
				((TextView) convertView).setGravity(Gravity.RIGHT);
				convertView.setPadding(0, 0, 5, 0);
			}

			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = Config.getController().inflate(
						R.layout.battle_loss_group);
			}

			BattleLossDetail bld = (BattleLossDetail) getGroup(groupPosition);
			TroopMoveInfoClient aic = (TroopMoveInfoClient) getChild(
					groupPosition, childPosition);

			String arm = aic.getArmProp().getName() + " ×" + aic.getCount();
			if (bld.getUserId() == Account.user.getId())
				arm = StringUtil.color(arm, R.color.color2);
			ViewUtil.setRichText(convertView, arm);

			if (isAtk) {
				((TextView) convertView).setGravity(Gravity.LEFT);
				convertView.setPadding(5, 0, 0, 0);
			} else {
				((TextView) convertView).setGravity(Gravity.RIGHT);
				convertView.setPadding(0, 0, 5, 0);
			}

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

	}
}
