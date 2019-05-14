/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-6-27
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui.adapter;

import java.text.DecimalFormat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.RechargeLog;
import com.vikings.sanguo.utils.ViewUtil;

public class RechargeLogAdapter extends ObjectAdapter {
	private DecimalFormat df;

	public RechargeLogAdapter() {
		df = new DecimalFormat("0.##");
	}

	@Override
	public int getLayoutId() {
		return R.layout.recharge_log;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.index = (TextView) convertView.findViewById(R.id.index);
			holder.rmb = (TextView) convertView.findViewById(R.id.rmb);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.rechargeAccount = (TextView) convertView
					.findViewById(R.id.account);
			holder.channel = (TextView) convertView.findViewById(R.id.channel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		RechargeLog log = (RechargeLog) getItem(position);
		ViewUtil.setText(holder.index, (position + 1) + ".");
		ViewUtil.setText(holder.rmb, df.format((float) log.getRmb() / 100)
				+ " 元");
		ViewUtil.setText(holder.time, log.getTimeInfo());
		ViewUtil.setRichText(holder.rechargeAccount, log.getRechargeInfo());
		ViewUtil.setText(holder.channel, "通过" + log.getChannelInfo() + "  购入"
				+ log.getCash() + "元宝");
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	static class ViewHolder {
		int no;
		TextView index;
		TextView rmb;
		TextView time;
		TextView rechargeAccount;
		TextView channel;
	}
}
