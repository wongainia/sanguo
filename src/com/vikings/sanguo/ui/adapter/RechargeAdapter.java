package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.RechargeState;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.window.AlipayWindow;
import com.vikings.sanguo.ui.window.RechargeInputWindow;
import com.vikings.sanguo.ui.window.UnionpayWindow;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class RechargeAdapter extends ObjectAdapter {
	private BriefUserInfoClient briefUser;

	public RechargeAdapter(BriefUserInfoClient briefUser) {
		this.briefUser = briefUser;
	}

	public void setBriefuser(BriefUserInfoClient briefUser) {
		this.briefUser = briefUser;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = Config.getController().inflate(getLayoutId());
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.stateImg = (ImageView) convertView
					.findViewById(R.id.stateImg);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final RechargeState state = (RechargeState) getItem(position);
		new ViewImgCallBack(state.getIcon(), holder.icon);
		ViewUtil.setText(holder.name, state.getName());
		if (state.getRate() > 0 && !StringUtil.isNull(state.getLabel())) {
			ViewUtil.setVisible(holder.stateImg);
			new ViewImgCallBack(state.getLabel(), holder.stateImg);
		} else {
			ViewUtil.setGone(holder.stateImg);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (state.isClose()) {
					Config.getController().alert(state.getMsg());
					return;
				}
				switch (state.getId()) {
				case RechargeState.ID_ALIPAY:
					new AlipayWindow().open(state, briefUser);
					break;
				case RechargeState.ID_CHINA_MOBILE_CARD:
					new RechargeInputWindow().open(
							RechargeState.ID_CHINA_MOBILE_CARD,
							state.getName(), briefUser);
					break;
				case RechargeState.ID_CHINA_UNICOM_CARD:
					new RechargeInputWindow().open(
							RechargeState.ID_CHINA_UNICOM_CARD,
							state.getName(), briefUser);
					break;
				case RechargeState.ID_CHINA_TELECOM_CARD:
					new RechargeInputWindow().open(
							RechargeState.ID_CHINA_TELECOM_CARD,
							state.getName(), briefUser);
					break;
				case RechargeState.ID_CHINA_MOBILE_SM:
					new RechargeInputWindow().open(
							RechargeState.ID_CHINA_MOBILE_SM, state.getName(),
							briefUser);
					break;
				case RechargeState.ID_CHINA_TELCOM_SM:
					new RechargeInputWindow().open(
							RechargeState.ID_CHINA_TELCOM_SM, state.getName(),
							briefUser);
					break;
				case RechargeState.ID_UNIONPAY:
					new UnionpayWindow().open(state, briefUser, false);
					break;
				case RechargeState.ID_TRANSFER_PAY:
					Config.getController().openSite(
							CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
									(byte) 14));
					break;
				case RechargeState.ID_CREDIT_CARD:
					new UnionpayWindow().open(state, briefUser, true);
					break;
				default:
					Config.getController().alert("不支持该种充值方式，请升级游戏版本，或选择其他支付方式");
					break;
				}
			}
		});

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.recharge_item;
	}

	private class ViewHolder {
		ImageView icon, stateImg;
		TextView name;
	}
}