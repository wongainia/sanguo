package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.CampaignData;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.thread.ViewImgGrayCallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.adapter.CampaignAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.alert.RewardTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CampaignSpoilItem;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class CampaignWindow extends CustomListViewWindow {
	private ActInfoClient actClient;

	private boolean firstIn = false;

	private TextView gradientMsg;

	public void open(ActInfoClient actClient) {
		this.actClient = actClient;
		this.firstIn = true;
		doOpen();
	}

	public void openGuard(ActInfoClient actClient) {
		this.actClient = actClient;
		this.firstIn = false;
		doOpen();
	}

	@Override
	protected void init() {
		super.init(getTitleResId());
		setContentAboveTitle(R.layout.top_desc_txt);
		ViewUtil.setRichText(window.findViewById(R.id.topDesc), actClient
				.getPropAct().getDesc());
		setContentBelowTitle(R.layout.gradient_msg);
		gradientMsg = (TextView) window.findViewById(R.id.gradientMsg);
		listView.setVerticalScrollBarEnabled(false);
	}

	@Override
	protected View initFooterView() {
		if (CacheMgr.actSpoilsCache.hasActSpoil(actClient.getId()))
			return getSpoilItem();
		else
			return null;
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	private void setPassCount() {
		String complete = actClient.getCompletedDesc();
		setLeftTxt("进度：" + complete);
	}

	private void setUserStaminaDesc() {
		int stamina = Account.user.getUserStamina();
		setRightTxt("行动:" + stamina + "/" + Account.user.getMaxStamina());
		if (stamina == Account.user.getMaxStamina()) {
			ViewUtil.setText(gradientMsg, "行动力已满");
		} else {
			int time = Account.user.getUserStaminaRecoverLeftTime();
			ViewUtil.setRichText(
					gradientMsg,
					"行动力"
							+ StringUtil.color(
									DateUtil.formatDownCountTime(time),
									R.color.color19) + "后恢复1点");
		}

	}

	@Override
	public void showUI() {
		setPassCount();
		adapter.notifyDataSetChanged();
		setUserStaminaDesc();
		refreshFooter();
		super.showUI();
		if (firstIn) {
			int[] completed = actClient.getCompleted();
			if (completed[0] != completed[1]) {
				listView.setSelection(completed[0]);
			}
			firstIn = false;
		}
		((CampaignAdapter) adapter).setItemOpen();
	}

	private String getTitleResId() {
		switch (actClient.getPropAct().getType()) {
		case 1:
			return "新手副本";
		case 2:
			return "战役副本";
		case 3:
			return "活动副本";
		case 4:
			return "时空副本";
		case 5:
			return "名将副本";
		case 6:
			return "秘籍副本";
		case 7:
			return "神兽副本";
		default:
			return "副本";
		}
	}

	private View getSpoilItem() {
		View spoilLayout = controller.inflate(R.layout.campaign_item);
		ViewUtil.setRichText(spoilLayout, R.id.desc, actClient.getPropAct()
				.getSpoilDesc());
		ViewUtil.setText(spoilLayout, R.id.campaignName, "战役全通大礼");
		ViewUtil.setGone(spoilLayout, R.id.detailLayout);
		ImageButton fightBtn = (ImageButton) spoilLayout
				.findViewById(R.id.fightBtn);
		fightBtn.setImageResource(R.drawable.txt_lj);
		ViewUtil.setGone(spoilLayout, R.id.extraSpoilLayout);
		ViewUtil.setGone(spoilLayout, R.id.costLayout);

		setSpoilsLine(actClient.getActSpoils(true).showReturn(true),
				spoilLayout.findViewById(R.id.passSpoilLayout));
		ViewUtil.setText(spoilLayout.findViewById(R.id.passSpoilName), "通关奖励");

		spoilLayout.findViewById(R.id.itemLayout).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						switchSpoilItem(v);
					}
				});
		return spoilLayout;
	}

	// 未通关
	private void setUncomplete() {
		ImageUtil.setBgGray(footerView.findViewById(R.id.itemLayout));
		ViewGroup iconGroup = (ViewGroup) footerView
				.findViewById(R.id.iconGroup);
		ImageUtil.setBgGray(iconGroup.findViewById(R.id.iconBg));
		ImageUtil.setBgGray(iconGroup.findViewById(R.id.hasTroopBg));
		new ViewImgGrayCallBack("tax_center.png",
				iconGroup.findViewById(R.id.icon),
				Constants.CAMPAIGN_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.CAMPAIGN_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
	}

	// 已通关
	private void setComplete() {
		ImageUtil.setBgNormal(footerView.findViewById(R.id.itemLayout));
		ViewGroup iconGroup = (ViewGroup) footerView
				.findViewById(R.id.iconGroup);
		ImageUtil.setBgNormal(iconGroup.findViewById(R.id.iconBg));
		ImageUtil.setBgNormal(iconGroup.findViewById(R.id.hasTroopBg));
		new ViewImgScaleCallBack("tax_center.png",
				iconGroup.findViewById(R.id.icon),
				Constants.CAMPAIGN_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.CAMPAIGN_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
	}

	public void refreshFooter() {
		if (null != footerView) {
			if (actClient.isComplete()) {
				setComplete();
				ViewUtil.setVisible(footerView, R.id.reason);
				if (!actClient.hasCompleteBonus()) {
					ViewUtil.setVisible(footerView, R.id.fightBtn);
					footerView.findViewById(R.id.fightBtn).setOnClickListener(
							dungeonReward);
					ViewUtil.setGone(footerView, R.id.reason);
				} else {
					ViewUtil.setGone(footerView, R.id.fightBtn);
					ViewUtil.setVisible(footerView, R.id.reason);
					ViewUtil.setText(footerView, R.id.reason, "奖励已领取");
				}
			} else {
				setUncomplete();
				ViewUtil.setVisible(footerView, R.id.reason);
				ViewUtil.setText(footerView, R.id.reason, "全通本章战役后可领取");
				ViewUtil.setGone(footerView, R.id.fightBtn);
			}
		}
	}

	private void switchSpoilItem(View v) {
		View parent = (View) v.getParent();
		if (ViewUtil.isGone(parent.findViewById(R.id.detailLayout))) {
			ViewUtil.setVisible(parent, R.id.detailLayout);
			((CampaignAdapter) adapter).close();
			smoothMoveItem(parent);
		} else {
			ViewUtil.setGone(parent, R.id.detailLayout);
		}
	}

	OnClickListener dungeonReward = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new DungeonRewardInvoker(actClient.getId()).start();
		}
	};

	private void setSpoilsLine(List<ShowItem> ls, View vg) {
		if (ListUtil.isNull(ls))
			return;

		ViewGroup line1 = (ViewGroup) vg.findViewById(R.id.line1);
		ViewUtil.removeAllViews(line1);
		ViewGroup line2 = (ViewGroup) vg.findViewById(R.id.line2);
		ViewUtil.removeAllViews(line2);

		for (int i = 0; i < ls.size(); i++) {
			ShowItem showItem = ls.get(i);
			if (i % 2 == 0)
				line1.addView(new CampaignSpoilItem(showItem).getWidget());
			else
				line2.addView(new CampaignSpoilItem(showItem).getWidget());
		}
	}

	class DungeonRewardInvoker extends BaseInvoker {
		private int actId;
		private ReturnInfoClient ric;

		public DungeonRewardInvoker(int actId) {
			this.actId = actId;
		}

		@Override
		protected String loadingMsg() {
			return "请稍候";
		}

		@Override
		protected String failMsg() {
			return "领奖失败";
		}

		@Override
		protected void fire() throws GameException {
			ric = GameBiz.getInstance().dungeonReward(actId);
		}

		@Override
		protected void onOK() {
			new RewardTip("获得奖励", ric.showReturn(true)).show();
			adapter.notifyDataSetChanged();
			controller.goBack();
		}

	}

	@Override
	protected ObjectAdapter getAdapter() {
		CampaignAdapter adapter = new CampaignAdapter(actClient, this);
		adapter.addItems(CampaignData.getCampaignData(actClient));
		return adapter;
	}

	// 引导使用的
	public CampaignAdapter getCampaignAdapter() {
		return (CampaignAdapter) adapter;
	}

	public void setFooterDetailGone() {
		if (null != footerView
				&& ViewUtil.isVisible(footerView
						.findViewById(R.id.detailLayout))) {
			ViewUtil.setGone(footerView, R.id.detailLayout);
		}
	}

	@Override
	protected int refreshInterval() {
		return 1000;
	}

	@Override
	protected void refreshUI() {
		setUserStaminaDesc();
	}
}
