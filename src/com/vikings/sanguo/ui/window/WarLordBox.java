package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.CurrencyBoxOpenResp;
import com.vikings.sanguo.model.CurrencyBox;
import com.vikings.sanguo.thread.ViewRichTextCallBack;
import com.vikings.sanguo.ui.alert.RewardTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class WarLordBox extends CustomPopupWindow implements OnClickListener {
	private int boxTypeLayout[] = { R.id.goldBox, R.id.silverBox, R.id.woodBox };

	private List<CurrencyBox> cb;

	// 当日宝箱剩余开启次数
	private int times = 0;

	@Override
	protected void init() {
		super.init("神秘宝箱");
		setContent(R.layout.war_lord_box_layout);
		setCommonBg(R.drawable.blood_war_bg);
		updateBoxView();
	}

	private void updateBoxView() {
		// 每天0：00重置次数
		if (!DateUtil.isToday(Account.readLog.LAST_OPEN_WAR_LORD_BOX_TIME)) {
			times = 100;
		} else {
			times = Account.user.warLordBoxTimes();
		}

		ViewUtil.setRichText(content, R.id.gradientMsg, "今日还可开" + times + "个宝箱");

		setRightTxt("#rmb#" + Account.user.getCurrency());
		cb = CacheMgr.currencyBoxCache.getAll();
		for (int i = 0; i < cb.size(); i++) {
			ViewGroup vGroup = (ViewGroup) content
					.findViewById(boxTypeLayout[i]);
			CurrencyBox cbox = CurrencyBox.getCurrencyBoxByType(cb,
					CurrencyBox.sortBox[i]);
			ViewUtil.setRichText(vGroup, R.id.openBoxSpc, cbox.getBoxSpec());

			new ViewRichTextCallBack(cbox.getExpendItem().getImage(), "", "",
					cbox.getExpendItem().getName()
							+ "x"
							+ cbox.getOpenBoxIdCount()
							+ "  ("
							+ StringUtil.color("" + cbox.getItemCount(), cbox
									.getItemCount() == 0 ? R.color.color24
									: R.color.color19) + ")",
					vGroup.findViewById(R.id.openBoxPrompt), 25, 25);

			// 显示需要消耗的元宝数
			if (!cbox.itemEnough()) {
				ViewUtil.setVisible(vGroup, R.id.currency_expend);
				ViewUtil.setRichText(vGroup, R.id.currency_expend, "#rmb#"
						+ cbox.getCurrencybox());
			}

			View btnView = vGroup.findViewById(R.id.openbox);
			btnView.setOnClickListener(this);
			btnView.setTag(cbox);
		}
	}

	public void open() {
		doOpen();
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	public void onClick(View v) {
		// 当天宝箱开启次数完毕
		if (times <= 0) {
			controller
					.alert("开启宝箱失败",
							"您今天已经开了太多的宝箱<br>不能继续开了<br><br><br><font color='#f3b338'>每日0:00重置开启次数</font>",
							null, false);
			return;
		}

		CurrencyBox cBox = (CurrencyBox) v.getTag();

		// 先判断道具是否足够
		if (cBox.itemEnough()) {
			new OpenWarLordBoxInvoker(cBox.getBoxId(), 1, cBox.getBoxType())
					.start();
			return;
		}
		// 显示需要消耗的元宝数
		// ViewGroup vGroup = (ViewGroup)
		// content.findViewById(boxTypeLayout[cBox
		// .getBoxType() - 1]);
		// ViewUtil.setVisible(vGroup, R.id.currency_expend);
		// ViewUtil.setRichText(vGroup, R.id.currency_expend,
		// "#rmb#" + cBox.getCurrencybox());

		// 元宝是否足够
		if (cBox.currencyEnough()) {
			new OpenWarLordBoxInvoker(cBox.getBoxId(), 2, cBox.getBoxType())
					.start();
			return;
		}
		new ToActionTip(cBox.getCurrencybox()).show();
	}

	public class OpenWarLordBoxInvoker extends BaseInvoker {

		// 宝箱Id
		private int target;
		// 1是道具开启< ----- >2是元宝开启
		private int type;

		// 金，银，木
		private int boxType;
		private CurrencyBoxOpenResp cbop;

		public OpenWarLordBoxInvoker(int target, int type, int boxType) {
			this.target = target;
			this.type = type;
			this.boxType = boxType;
		}

		@Override
		protected String failMsg() {
			return "开启宝箱失败";
		}

		@Override
		protected void fire() throws GameException {
			cbop = GameBiz.getInstance().currencyBoxOpen(target, type);
		}

		@Override
		protected String loadingMsg() {
			return "宝箱开启中...";
		}

		@Override
		protected void onOK() {
			// 记录最后一次成功开宝箱时间，以便第二天重置显示次数
			Account.readLog.LAST_OPEN_WAR_LORD_BOX_TIME = Config.serverTime();
			controller.updateUI(cbop.getRic(), true);
			updateBoxView();
			new RewardTip("开启" + CurrencyBox.getBoxTypeName(boxType) + "成功",
					cbop.getRic().showReturn(true), false, false).show();
		}
	}
}
