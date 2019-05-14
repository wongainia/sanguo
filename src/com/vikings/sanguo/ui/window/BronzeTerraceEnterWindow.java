package com.vikings.sanguo.ui.window;

import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.MachinePlayStatInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.protos.MachinePlayType;
import com.vikings.sanguo.sound.MediaPlayerMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.LuckyAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class BronzeTerraceEnterWindow extends CustomBaseListWindow implements
		OnClickListener {
	private static final int MSG_TOTAL = 10;// 显示前十名的排行

	@Override
	protected void init() {
		super.init("铜雀台");
		setLeftBtn("规则说明", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new RuleSpecTip("铜雀台规则说明", CacheMgr.uiTextCache
						.getTxt(UITextProp.TONGQUETAI_SPEC)).show();
			}
		});
		setRightTxt("#rmb#" + Account.user.getCurrency());
		setContentBelowTitle(R.layout.gamble_enter);

		ViewUtil.setImage(findViewById(R.id.daqiao_layout), "daqiao.png");
		ViewUtil.setImage(findViewById(R.id.xiaoqiao_layout), "xiaoqiao.png");

		bindButton(R.id.xiaoqiao_btn, this);
		bindButton(R.id.daqiao_btn, this);
	}

	private void setValue() {
		ViewUtil.setRichText(window, R.id.xiaoqiao_propmt,
				CacheMgr.uiTextCache.getTxt(UITextProp.XIAOQIAO_BRONZE_PROMPT));
		ViewUtil.setRichText(window, R.id.xiaoqiao_spec,
				CacheMgr.uiTextCache.getTxt(UITextProp.XIAOQIAO_BRONZE_SPEC));
		ViewUtil.setRichText(window, R.id.daqiao_propmt,
				CacheMgr.uiTextCache.getTxt(UITextProp.DAQIAO_BRONZE_PROMPT));
		ViewUtil.setRichText(window, R.id.daqiao_spec,
				CacheMgr.uiTextCache.getTxt(UITextProp.DAQIAO_BRONZE_SPEC));
	}

	public void open() {
		doOpen();
		// 第一次进入，弹出奖励规则
		if (!Account.readLog.FIRST_ENTER_GAMBLE) {
			Account.readLog.FIRST_ENTER_GAMBLE = true;
			new RuleSpecTip("铜雀台规则说明",
					CacheMgr.uiTextCache.getTxt(UITextProp.TONGQUETAI_SPEC))
					.show();
		}
		this.firstPage();
	}

	@Override
	public void showUI() {
		super.showUI();
		setValue();
	}

	@Override
	public void onClick(View v) {
		MediaPlayerMgr.getInstance().pauseSound();
		if (v.getId() == R.id.daqiao_btn) {
			new GambleWindow().open(MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR,
					new UpdateLuckyList(), new UpdateCurrency());
		} else if (v.getId() == R.id.xiaoqiao_btn) {
			new GambleWindow().open(MachinePlayType.MACHINE_PLAY_TYPE_MIDDLE,
					new UpdateLuckyList(), new UpdateCurrency());
		}

	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new LuckyAdapter();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<MachinePlayStatInfoClient> list = GameBiz.getInstance()
				.machinePlayStatData();
		resultPage.setTotal(MSG_TOTAL);
		resultPage.setResult(list);
	}

	@Override
	public void handleItem(Object o) {
		MachinePlayStatInfoClient mpsic = (MachinePlayStatInfoClient) o;
		controller.showCastle(mpsic.getUser().getId());
	}

	class UpdateLuckyList implements CallBack {
		@Override
		public void onCall() {
			BronzeTerraceEnterWindow.this.firstPage();
		}
	}

	class UpdateCurrency implements CallBack {
		@Override
		public void onCall() {
			setRightTxt("#rmb#" + Account.user.getCurrency());
		}
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}
}
