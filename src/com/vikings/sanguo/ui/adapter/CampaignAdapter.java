package com.vikings.sanguo.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.CampaignData;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.PropCampaign;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.window.CampaignWindow;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CampaignSpoilItem;

public class CampaignAdapter extends ObjectAdapter {

	private ActInfoClient actClient;
	private CampaignWindow window;
	public static boolean isFromBattleResult = false;
	public static boolean isFirstWin = false;

	public CampaignAdapter(ActInfoClient actClient, CampaignWindow window) {
		this.actClient = actClient;
		this.window = window;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.itemLayout = (ViewGroup) convertView
					.findViewById(R.id.itemLayout);
			holder.detailLayout = (ViewGroup) convertView
					.findViewById(R.id.detailLayout);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.bossImage = (ImageView) convertView
					.findViewById(R.id.bossImage);
			holder.campaignName = (TextView) convertView
					.findViewById(R.id.campaignName);
			holder.leftTimes = (TextView) convertView
					.findViewById(R.id.leftTimes);
			holder.fightBtn = (ImageButton) convertView
					.findViewById(R.id.fightBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CampaignData campaignData = (CampaignData) getItem(position);
		final CampaignInfoClient campaignClient = campaignData
				.getCampaignClient();
		holder.campaignData = campaignData;
		PropCampaign propCampaign = campaignClient.getPropCampaign();
		if (null != propCampaign) {
			new ViewImgScaleCallBack(propCampaign.getIcon(), holder.icon,
					Config.SCALE_FROM_HIGH * Constants.CAMPAIGN_ICON_WIDTH,
					Config.SCALE_FROM_HIGH * Constants.CAMPAIGN_ICON_HEIGHT);
			ViewUtil.setText(holder.campaignName, propCampaign.getName());
		}
		if (campaignData.isOpen()) {
			ViewUtil.setVisible(holder.detailLayout);
			setCampaignDetail(holder.detailLayout, campaignClient);
		} else {
			ViewUtil.setGone(holder.detailLayout);
		}

		setFightBtn(holder.fightBtn, campaignData,
				campaignClient.getCompleteCnt());
		holder.fightBtn.setTag(campaignData);
		holder.fightBtn.setOnClickListener(fightL);

		if (campaignClient.getPropCampaignMode().isBoss())
			ViewUtil.setVisible(holder.bossImage);
		else
			ViewUtil.setGone(holder.bossImage);

		int[] times = campaignClient.getTimeLimit();
		if (times[1] == 0) {
			ViewUtil.setText(holder.leftTimes, "不限次数");
		} else {
			if (times[0] < times[1]) {
				ViewUtil.setText(holder.leftTimes, "今日还可挑战"
						+ (times[1] - times[0]) + "次");
			} else {
				ViewUtil.setText(holder.leftTimes, "次数已用尽");
				ViewUtil.setHide(holder.fightBtn);
			}

		}

		holder.itemLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				move(v, campaignClient);
			}
		});

		return convertView;
	}

	private void setFightBtn(View view, CampaignData campaignData, int[] cnt) {
		// 选中或已通关或未通关但可以打的战役，显示战按钮
		if (campaignData.isOpen()
				|| cnt[0] > 0
				|| ListUtil
						.isNull(campaignData.getCampaignClient().checkOpen())) {
			ViewUtil.setVisible(view);
			if (ListUtil.isNull(campaignData.getCampaignClient().checkOpen())) {
				ImageUtil.setBgNormal(view);
			} else {
				ImageUtil.setBgGray(view);
			}
		} else
			ViewUtil.setGone(view);
	}

	private void move(View v, CampaignInfoClient campaignClient) {
		List<CampaignData> data = content;
		for (CampaignData it : data) {
			if (it.getCampaignClient().getId() == campaignClient.getId()) {
				if (it.isOpen())
					it.setOpen(false);
				else {
					it.setOpen(true);
					window.smoothMoveItem((View) v.getParent());
				}
			} else
				it.setOpen(false);
		}
		notifyDataSetChanged();
		window.setFooterDetailGone();
	}

	private void setCampaignDetail(ViewGroup vg,
			CampaignInfoClient campaignClient) {
		ViewUtil.setRichText(vg, R.id.desc, campaignClient.getDesc());

		// 通关奖励
		setSpoils((ViewGroup) vg.findViewById(R.id.passSpoilLayout),
				campaignClient, CampaignInfoClient.BONUS_FIX);
		// 意外奖励
		setSpoils((ViewGroup) vg.findViewById(R.id.extraSpoilLayout),
				campaignClient, CampaignInfoClient.BONUS_RATE);

		if (null != campaignClient.getPropCampaignMode()) {
			ViewUtil.setVisible(vg, R.id.costLayout);
			int userStamina = campaignClient.getPropCampaignMode()
					.getUserStaminaCost();
			if (userStamina > 0) {
				ViewUtil.setVisible(vg, R.id.costLayout);
				ViewUtil.setText(vg, R.id.cost, "行动力-" + userStamina);
			} else {
				ViewUtil.setHide(vg, R.id.costLayout);
			}
		} else {
			ViewUtil.setHide(vg, R.id.costLayout);
		}
	}

	private void setSpoils(ViewGroup viewGroup,
			CampaignInfoClient campaignClient, int spoilType) {
		ReturnInfoClient passSpoil = campaignClient.getBonus(spoilType);
		ArrayList<ShowItem> passSpoilLs = passSpoil.showReturn(true);
		setSpoilsLine(passSpoilLs, viewGroup);
	}

	private void setSpoilsLine(ArrayList<ShowItem> ls, View vg) {
		ViewGroup line1 = (ViewGroup) vg.findViewById(R.id.line1);
		ViewUtil.removeAllViews(line1);
		ViewGroup line2 = (ViewGroup) vg.findViewById(R.id.line2);
		ViewUtil.removeAllViews(line2);

		if (ListUtil.isNull(ls))
			return;

		for (int i = 0; i < ls.size(); i++) {
			ShowItem showItem = ls.get(i);
			if (i % 2 == 0)
				line1.addView(new CampaignSpoilItem(showItem).getWidget());
			else
				line2.addView(new CampaignSpoilItem(showItem).getWidget());
		}
	}

	private OnClickListener fightL = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CampaignData campaignData = (CampaignData) v.getTag();
			CampaignInfoClient campaignClient = campaignData
					.getCampaignClient();

			// 检查是否可以进入
			ArrayList<String> err = campaignClient.checkOpen();
			if (!ListUtil.isNull(err)) {
				Config.getController().alert(err.get(0));
				return;
			}

			String limit = campaignClient.checkLimit(Account
					.getVipExtraActTimes(actClient));

			if (null != limit) {
				Config.getController().alert("已经达到挑战次数上限");
				return;
			}

			List<CampaignData> data = content;
			for (CampaignData it : data) {
				if (it.getCampaignClient().getId() == campaignClient.getId())
					it.setOpen(true);
				else
					it.setOpen(false);
			}
			Config.getController().openSetOffTroopWindow(campaignClient);
		}
	};

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	private class ViewHolder {
		ViewGroup detailLayout, itemLayout;
		ImageView icon, bossImage;
		TextView campaignName, leftTimes;
		ImageButton fightBtn;
		CampaignData campaignData;
	}

	@Override
	public int getLayoutId() {
		return R.layout.campaign_item;
	}

	public void setItemOpen() {
		if (!isFirstWin)
			return;

		List<CampaignData> data = content;

		int openItemIdx = -1;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isOpen()) {
				openItemIdx = i;
			}
			data.get(i).setOpen(false);
		}

		int selId = -1;
		if (-1 != openItemIdx) {
			if (isFromBattleResult && openItemIdx + 1 < data.size()) {
				data.get(openItemIdx + 1).setOpen(true);
				selId = data.get(openItemIdx + 1).getCampaignClient().getId();
			} else {
				data.get(openItemIdx).setOpen(true);
				selId = data.get(openItemIdx).getCampaignClient().getId();
			}

			isFromBattleResult = false;
			isFirstWin = false;

			notifyDataSetChanged();

			for (int i = 0; i < window.getListView().getChildCount(); i++) {
				View v = window.getListView().getChildAt(i);
				if (null != v.getTag()
						&& v.getTag() instanceof ViewHolder
						&& ((ViewHolder) v.getTag()).campaignData
								.getCampaignClient().getId() == selId) {
					window.smoothMoveItem(window.getListView().getChildAt(i));
					break;
				}
			}
		}
	}

	public void close() {
		setAllItemClose();
		notifyDataSetChanged();
	}

	private void setAllItemClose() {
		List<CampaignData> ls = content;
		for (CampaignData it : ls)
			it.setOpen(false);
	}
}