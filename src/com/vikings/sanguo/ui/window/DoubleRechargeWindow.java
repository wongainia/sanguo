package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class DoubleRechargeWindow extends CustomPopupWindow implements
		OnClickListener {
	private ImageView doubleRechargeBg, progressBg, progress;
	private TextView desc, needDesc, progressDesc;
	private Button rechargeBtn;

	@Override
	protected void init() {
		super.init("双倍优惠");
		setContent(R.layout.double_recharge);
		android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) content
				.getLayoutParams();
		params.topMargin = 0;
		params.bottomMargin = 0;
		doubleRechargeBg = (ImageView) window
				.findViewById(R.id.doubleRechargeBg);
		progressBg = (ImageView) window.findViewById(R.id.progressBg);
		progress = (ImageView) window.findViewById(R.id.progress);
		desc = (TextView) window.findViewById(R.id.desc);
		needDesc = (TextView) window.findViewById(R.id.needDesc);
		progressDesc = (TextView) window.findViewById(R.id.progressDesc);
		rechargeBtn = (Button) window.findViewById(R.id.rechargeBtn);
		rechargeBtn.setOnClickListener(this);
		ViewUtil.setImage(window.findViewById(R.id.pattenMirror),
				ImageUtil.getMirrorBitmapDrawable("pattern_line.png"));
	}

	public void open() {
		doOpen();
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	private void setValue() {
		ViewUtil.setImage(doubleRechargeBg, "double_recharge_bg.jpg");
		ViewUtil.setRichText(desc, CacheMgr.uiTextCache
				.getTxt(UITextProp.DOUBLE_CHARGE_REWARDS_DESC), true);
		int times = Account.user.getDoubleChargeTimes();
		int total = CacheMgr.doubleChargeCache.getDoubleRechargeTotal(times);
		int cur = Account.user.getDoubleChargeValue();
		setProgress(total, cur);
	}

	private void setProgress(int total, int cur) {
		LayoutParams params = (LayoutParams) progressBg.getLayoutParams();
		int width = params.width;
		params = (LayoutParams) progress.getLayoutParams();
		float temp = 1f * cur / total * width;
		if (temp != 0 && temp < 23 * Config.SCALE_FROM_HIGH)
			temp = 23 * Config.SCALE_FROM_HIGH;
		params.width = (int) temp;
		progress.setLayoutParams(params);
		ViewUtil.setText(progressDesc, cur + "/" + total);
		if (cur < total)
			ViewUtil.setRichText(
					needDesc,
					"再获得 "
							+ StringUtil.color("" + (total - cur),
									R.color.color19) + " 分， 即可享受双倍优惠");
		else
			ViewUtil.setRichText(needDesc, "去充值立即享受双倍优惠");
	}

	@Override
	public void onClick(View v) {
		if (v == rechargeBtn) {
			controller.openRechargeWindow(RechargeWindow.TYPE_REWARD_RECHARGE,
					Account.user.bref());
		}
	}

}
