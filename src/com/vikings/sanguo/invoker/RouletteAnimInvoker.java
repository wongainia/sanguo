package com.vikings.sanguo.invoker;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.RouletteResp;
import com.vikings.sanguo.model.PropRoulette;
import com.vikings.sanguo.protos.KeyValue;
import com.vikings.sanguo.ui.RouletteView;
import com.vikings.sanguo.ui.alert.AlertRouletteTip;
import com.vikings.sanguo.ui.alert.RewardTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class RouletteAnimInvoker extends BaseInvoker {

	protected RouletteView v1, v2, v3;

	private View block;

	private long startTime;
	private long minTime = 3000l;
	private int count; // 转的轮盘数量
	private RouletteResp resp;
	private boolean isNetOK;

	public RouletteAnimInvoker(RouletteView v1, RouletteView v2, RouletteView v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		block = Config.getController().findViewById(R.id.animBlock);
	}

	public void start(int count) {
		this.count = count;
		if (isAllStop())
			start();
	}

	@Override
	protected void beforeFire() {
		Config.getController().getHeartBeat().stop();
		ViewUtil.setVisible(block);
		if (count >= 1)
			v1.start();
		if (count >= 2)
			v2.start();
		if (count >= 3)
			v3.start();
		startTime = Config.serverTime();
		ctr.setBackKeyValid(false);

		setReward();
	}

	protected void afterFire() {
		ViewUtil.setGone(block);

		((ViewGroup) block).clearDisappearingChildren();
		ctr.setBackKeyValid(true);
		Config.getController().getHeartBeat().start();
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().roulette(count);
		while (startTime + minTime > Config.serverTime()) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		List<KeyValue> list = resp.getList();
		for (KeyValue keyValue : list) {
			if (keyValue.getKey() == 1) {
				v1.stop(keyValue.getValue());
			} else if (keyValue.getKey() == 2) {
				v2.stop(keyValue.getValue());
			} else if (keyValue.getKey() == 3) {
				v3.stop(keyValue.getValue());
			}
		}
		if (list.isEmpty())
			throw new GameException("服务器返回错误");
	}

	@Override
	protected String loadingMsg() {
		return null;
	}

	@Override
	protected void onFail(GameException exception) {
		isNetOK = false;
		v1.stop();
		v2.stop();
		v3.stop();
		super.onFail(exception);
	}

	@Override
	protected String failMsg() {
		return "转动轮盘失败";
	}

	@Override
	protected void onOK() {
		if (null != resp) {
			isNetOK = true;
			ctr.updateUI(resp.getRic(), true);

		}

	}

	private void setReward() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while (true) {
					if (isAllStop()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						v1.post(new Runnable() {
							@Override
							public void run() {
								if (resp != null && isNetOK) {
									if (PropRoulette.get7Count(resp.getList()) == 0) {
										new RewardTip(StringUtil.color("获得奖励",
												R.color.color6), resp.getRic()
												.showReturn(true), false, false)
												.show();
									} else {
										new AlertRouletteTip().show(resp
												.getRic().showReturn(true),
												null, PropRoulette
														.get7Count(resp
																.getList()));
									}
								}

							}
						});
						return;
					}
				}
			}
		}).start();
	}

	private boolean isAllStop() {
		return (!v1.isScroll() && !v2.isScroll() && !v3.isScroll());
	}
}
