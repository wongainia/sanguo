package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.FinishQuestInvoker;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.VipRewardsTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class VipAdapter extends ObjectAdapter {
	private int curVipLevel;
	private int questVipLevel;
	private int charge;
	private QuestInfoClient quest;
	private CallBack callBack;

	public VipAdapter(CallBack callBack) {
		this.callBack = callBack;
	}

	public void setInfos(int curVipLevel, int questVipLevel, int charge,
			QuestInfoClient quest) {
		this.curVipLevel = curVipLevel;
		this.questVipLevel = questVipLevel;
		this.charge = charge;
		this.quest = quest;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.vipImg = (ImageView) convertView.findViewById(R.id.vipImg);
			holder.needRecharge = (TextView) convertView
					.findViewById(R.id.needRecharge);
			holder.vipLevel = (TextView) convertView
					.findViewById(R.id.vipLevel);
			holder.privilegeDescLayout = convertView
					.findViewById(R.id.privilegeDescLayout);
			holder.privilegeDesc = (TextView) convertView
					.findViewById(R.id.privilegeDesc);
			holder.btn = (Button) convertView.findViewById(R.id.btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final UserVip vip = (UserVip) getItem(position);
		new ViewImgScaleCallBack(vip.getBgImg(), holder.vipImg,
				420 * Config.SCALE_FROM_HIGH, 260 * Config.SCALE_FROM_HIGH);
		ViewUtil.setRichText(holder.vipLevel,
				"#!player_vip#" + StringUtil.vipNumImgStr(vip.getLevel()));
		ViewUtil.setGone(holder.needRecharge);
		if (!StringUtil.isNull(vip.getDesc())) {
			ViewUtil.setVisible(holder.privilegeDescLayout);
			ViewUtil.setRichText(holder.privilegeDesc, vip.getDesc());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new VipRewardsTip(vip).show();
				}
			});
		} else {
			ViewUtil.setGone(holder.privilegeDescLayout);
			convertView.setOnClickListener(null);
		}
		if (curVipLevel >= vip.getLevel()) {
			if (vip.getLevel() < questVipLevel) { // 已领奖
				ViewUtil.setHide(holder.btn);
			} else if (vip.getLevel() == questVipLevel) { // 未领奖
				ViewUtil.setVisible(holder.btn);
				setFinishQuest(quest, holder.btn);
			} else {
				ViewUtil.setVisible(holder.btn);
				setUnFinishQuest(holder.btn);
			}

		} else {
			int rate = vip.getChargeRate();

			int amt = CalcUtil.upNum((vip.getCharge() - charge)
					/ (1f * (rate == 0 ? Constants.CENT : rate)));

			ViewUtil.setVisible(holder.btn);

			if (amt > 0) {
				ViewUtil.setVisible(holder.needRecharge);
				ViewUtil.setText(holder.needRecharge,
						"再充值" + String.valueOf(amt) + "元开通");
				ViewUtil.setVisible(holder.btn);
				setRecharge(holder.btn, vip);
			}
		}

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.vip_item;
	}

	private class ViewHolder {
		View privilegeDescLayout;
		ImageView vipImg;
		TextView needRecharge, vipLevel, privilegeDesc;
		Button btn;
	}

	private void setRecharge(View view, UserVip vip) {
		ImageUtil.setBgNormal(view);
		ViewUtil.setImage(view, R.drawable.btn_blue);
		ViewUtil.setText(view, "开通   VIP" + vip.getLevel());
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.getController().openRechargeCenterWindow();
			}
		});
	}

	private void setFinishQuest(final QuestInfoClient quest, View view) {
		ImageUtil.setBgNormal(view);
		ViewUtil.setImage(view, R.drawable.btn_red);
		ViewUtil.setText(view, "领    奖");
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != quest)
					new FinishInvoker(quest).start();
			}
		});
	}

	private void setUnFinishQuest(View view) {
		ImageUtil.setBgGray(view);
		ViewUtil.setText(view, "领    奖");
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.getController().alert("抱歉,请先领取上一级VIP的奖励!");
			}
		});
	}

	private class FinishInvoker extends FinishQuestInvoker {

		public FinishInvoker(QuestInfoClient qic) {
			super(qic);
		}

		@Override
		protected void onOK() {
			super.onOK();
			if (null != callBack)
				callBack.onCall();
		}
	}
}
