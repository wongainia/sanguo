package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class RechargeInputAdapter extends ObjectAdapter implements
		OnClickListener {

	private int select = -1;
	private CallBack callBack;
	private String amountDescSuffix;

	public RechargeInputAdapter(CallBack callBack, String amountDescSuffix) {
		this.callBack = callBack;
		this.amountDescSuffix = amountDescSuffix;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.clickLayout = convertView.findViewById(R.id.clickLayout);
			holder.checked = (ImageView) convertView.findViewById(R.id.checked);
			holder.amountDesc = (TextView) convertView
					.findViewById(R.id.amountDesc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int amount = 0;
		try {
			Integer amountI = (Integer) getItem(position);
			amount = amountI.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (select == position || (select == -1 && position == 0)) {
			ViewUtil.setVisible(holder.checked);
		} else {
			ViewUtil.setHide(holder.checked);
		}

		holder.clickLayout.setTag(position);
		holder.clickLayout.setOnClickListener(this);
		ViewUtil.setText(holder.amountDesc, CalcUtil.rechargeAmountStr(amount)
				+ amountDescSuffix);
		return convertView;
	}

	@Override
	public void onClick(View v) {
		Object obj = v.getTag();
		if (obj != null) {
			int position = (Integer) obj;
			if (position != select) {
				select = position;
				if (null != callBack)
					callBack.onCall();
			}
		}

	}

	public int getSelect() {
		return select < 0 ? 0 : select;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	@Override
	public int getLayoutId() {
		return R.layout.recharge_input_grid_item;
	}

	private class ViewHolder {
		View clickLayout;
		ImageView checked;
		TextView amountDesc;
	}

}
