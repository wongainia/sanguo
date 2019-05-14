package com.vikings.sanguo.ui.alert;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.SpeedUpInvoker;
import com.vikings.sanguo.message.ManorReceiveResp;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.protos.BUILDING_STATUS;
import com.vikings.sanguo.protos.BuildingStatusInfo;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class BuildingStatTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_building_state;

	private Button upgradeBtn;
	private TextView level, desc;

	private BuildingInfoClient bic;
	private int statusID;
	private boolean isMyBuilding;

	// private Button speedUpBtn;
	// private ProgressBar progressBar;
	// private TextView progressDesc;

	public BuildingStatTip(BuildingInfoClient buildingInfoClient, int statusID,
			boolean isMyBuilding) {
		super(buildingInfoClient.getProp().getBuildingName());
		this.bic = buildingInfoClient;
		this.statusID = statusID;
		this.isMyBuilding = isMyBuilding;
		// speedUpBtn = (Button) content.findViewById(R.id.speedUpBtn);
		// progressBar = (ProgressBar) content.findViewById(R.id.progressBar);
		// progressDesc = (TextView) content.findViewById(R.id.progressDesc);
		refreshInterval = 5000;
	}

	public void show() {
		super.show();
		level = (TextView) content.findViewById(R.id.level);
		desc = (TextView) content.findViewById(R.id.desc);
		upgradeBtn = (Button) content.findViewById(R.id.upgradeBtn);
		setValue();
	}

	private void setValue() {
		BuildingProp prop = bic.getProp();
		new ViewImgCallBack(prop.getImage(),
				content.findViewById(R.id.buildingIcon));
		LayoutParams params = (LayoutParams) level.getLayoutParams();
		params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		ViewUtil.setText(level, "LV" + prop.getLevel());
		ViewUtil.setRichText(desc, R.id.desc, prop.getDesc(), true);

		if (isMyBuilding && bic.getProp().getNextLevel() != null) {
			upgradeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					new BuildingBuyTip(bic.getProp().getNextLevel()).show();
				}
			});
		} else {
			ViewUtil.setGone(upgradeBtn, R.id.upgradeBtn);
		}

		ViewUtil.setGone(content, R.id.speedUpGroup);
		updateDynView();
	}

	protected void updateDynView() {
		int time = bic.getResSpeedupTime();
		View v = content.findViewById(R.id.speedUpItemDesc);
		BuildingStatusInfo speedInfo = bic.getResSpeed();
		int speed = speedInfo.getValue();
		if (time > 0) {
			ViewUtil.setVisible(v);
			ViewUtil.setRichText(content, R.id.speedUpItemDesc, "产量加倍中，"
					+ DateUtil.formatMinute(time), true);

			BuildingStatusInfo speedUpInfo = bic.getResSpeedup();
			if (null != speedUpInfo)
				speed = (int) (speed / 100d * speedUpInfo.getValue());
		} else {
			ViewUtil.setGone(v);
		}

		ViewUtil.setRichText(content, R.id.desc,
				"每小时产出 " + StringUtil.color(speed + " ", R.color.k7_color7)
						+ bic.getResourceName(statusID), true);

		// int productCount = bic.produce(BUILDING_STATUS.valueOf(statusID));
		// if (productCount > bic.maxStore())
		// productCount = bic.maxStore();
		// progressBar.set(productCount, bic.maxStore());
		// ViewUtil.setRichText(progressDesc,
		// "已产出:#" + bic.getResourceStatusImg(statusID) + "#"
		// + productCount + "/" + bic.maxStore(), true);
		// int cd = bic.getResetCd();
		// if (cd > 0) {
		// ViewUtil.setRichText(content, R.id.speedUpDesc, StringUtil.color(
		// DateUtil.formatSecond(cd) + "后可再次加速", R.color.k7_color8));
		// ImageUtil.setBgGray(speedUpBtn);
		// speedUpBtn.setOnClickListener(null);
		// } else {
		// ViewUtil.setRichText(
		// content,
		// R.id.speedUpDesc,
		// "花费#rmb#"
		// + getSpeedUpCost(productCount, bic.maxStore(), bic
		// .producePerHour(BUILDING_STATUS
		// .valueOf(statusID)))
		// + "元宝加速，可立即满产收获", true);
		// ImageUtil.setBgNormal(speedUpBtn);
		// speedUpBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (bic.produce(BUILDING_STATUS.valueOf(statusID)) >= bic
		// .maxStore()) {
		// controller.alert("已经生产完成，不用加速");
		// return;
		// }
		//
		// int costCurrency = getSpeedUpCost(bic.produce(), bic
		// .maxStore(), bic.producePerHour(BUILDING_STATUS
		// .valueOf(bic.getResourceStatus())));
		// if (Account.user.getCurrency() < costCurrency) {
		// dismiss();
		// new ToActionTip(costCurrency).show();
		// return;
		// }
		// new BuildingSpeedUpInvoker(bic).start();
		// }
		// });
		// }
	}

	// private int getSpeedUpCost(int product, int maxStore, float speed) {
	// if (speed == 0 || product >= maxStore)
	// return 0;
	// else
	// return CalcUtil.upNum((maxStore - product) / speed) * 10;
	// }

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

	@Override
	public void onClick(View v) {

		if (bic.produce(BUILDING_STATUS.valueOf(statusID),
				Account.user.getLastLoginTime()) <= 0) {
			controller.alert("还没有产出资源，等一会再来收获吧");
			return;
		}
		String msg = Account.user.checkWeight(statusID);
		if (msg != null) {
			controller.alert(msg);
			return;
		}
		new ReceiveInvoker().start();
	}

	private class ReceiveInvoker extends BaseInvoker {
		private ManorReceiveResp resp;

		@Override
		protected String loadingMsg() {
			return "收获中";
		}

		@Override
		protected String failMsg() {
			return "收获失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().manorReceive(bic.getItemId());
		}

		@Override
		protected void onOK() {
			dismiss();
			SoundMgr.play(R.raw.sfx_receive);
			resp.getRi().setMsg("收获成功");
			ctr.updateUI(resp.getRi(), true, false, true);
			Account.manorInfoClient.update(resp.getMic());
		}
	}

	private class BuildingSpeedUpInvoker extends SpeedUpInvoker {

		public BuildingSpeedUpInvoker(BuildingInfoClient bic) {
			super(bic, null);
		}

		@Override
		protected void onOK() {
			super.onOK();
			setValue();
		}

	}

}
