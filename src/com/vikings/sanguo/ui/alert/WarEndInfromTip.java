package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;
import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefFiefInfoClient;

public class WarEndInfromTip extends CustomConfirmDialog {
	private EndType endTye;
	private long fiefid;

	public WarEndInfromTip(EndType endType, long fiefid) {
		super(CustomConfirmDialog.DEFAULT);

		this.endTye = endType;
		this.fiefid = fiefid;
		this.setValue();

		setButton(CustomConfirmDialog.SECOND_BTN, "查看详情",
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						backToMap();
						new BriefFiefInvoker().start();
					}
				});

		setButton(CustomConfirmDialog.THIRD_BTN, "我知道了", new OnClickListener() {
			@Override
			public void onClick(View v) {
				backToMap();
			}
		});
	}

	private void backToMap() {
		controller.closeAllPopup();
		controller.getFiefMap().backToMap();
		dismiss();
	}

	private void setValue() {
		StringBuilder builder = new StringBuilder();
		if (this.endTye == EndType.sally) {
			setTitle("报告领主大人");
			builder.append("对方").append(StringUtil.color("突围", "red"))
					.append("了！战斗已结束，请查看战争日志了解详情。");
		} else if (this.endTye == EndType.attack) {
			setTitle("报告领主大人");
			builder.append("对方").append(StringUtil.color("进攻", "red"))
					.append("了！战斗已结束，请查看战争日志了解详情。");
		} else {
			setTitle("战斗已经结束");
			builder.append("双方").append(StringUtil.color("交战", "red"))
					.append("了！战斗已结束，你可以查看历史战况，了解详情。");
		}
		ViewUtil.setRichText(content, R.id.msg, builder.toString());
		ViewUtil.setGone(content, R.id.msgExt);
	}

	public enum EndType {
		sally, attack, ohter
	}

	class BriefFiefInvoker extends BaseInvoker {

		private BriefFiefInfoClient bfic;

		@Override
		protected String loadingMsg() {
			return "正在加载数据";
		}

		@Override
		protected String failMsg() {
			return "获取数据失败";
		}

		@Override
		protected void fire() throws GameException {
			// 获取防守方的领地信息
			List<Long> ids = new ArrayList<Long>(1);
			ids.add(fiefid);
			List<BriefFiefInfoClient> ls = GameBiz.getInstance()
					.briefFiefInfoQuery(ids);
			if (null != ls && ls.size() > 0) {
				bfic = ls.get(0);
			}
		}

		@Override
		protected void onOK() {
			if (bfic != null) {
				controller.openHistoryWarInfoWindow(bfic);
			}
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_confirm);
	}
}
