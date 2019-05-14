package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.EquipmentSellResp;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class EquipmentSellInput extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_sell_input;
	private EquipmentInfoClient eic;
	private PropEquipment propEquipment;

	private TextView price, total, amountText;
	private EditText amount;
	private Button okBtn, closeBtn;

	public EquipmentSellInput(EquipmentInfoClient eic) {
		super("确认出售" + eic.getProp().getName());
		this.eic = eic;
		this.propEquipment = eic.getProp();
		price = (TextView) content.findViewById(R.id.price);
		total = (TextView) content.findViewById(R.id.total);
		amountText = (TextView) content.findViewById(R.id.amount_text);
		amount = (EditText) content.findViewById(R.id.amount);
		okBtn = (Button) content.findViewById(R.id.okBtn);
		okBtn.setOnClickListener(this);
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);
	}

	public void show() {
		if (null == propEquipment)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		ViewUtil.setText(price, propEquipment.getSell());
		ViewUtil.setText(amountText, "出售数量：1");
		ViewUtil.setGone(amount);
		ViewUtil.setText(total, propEquipment.getSell());
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	public class EquipmentSellInvoker extends BaseInvoker {
		private EquipmentInfoClient eic;
		private PropEquipment propEquipment;
		private EquipmentSellResp resp;
		private ReturnInfoClient rs;

		public EquipmentSellInvoker(EquipmentInfoClient eic) {
			this.eic = eic;
			this.propEquipment = eic.getProp();
		}

		public void confirm() {
			if (null == propEquipment)
				return;
			this.start();

		}

		@Override
		protected String loadingMsg() {
			return StringUtil.repParams(
					ctr.getString(R.string.SellInvoker_loadingMsg),
					propEquipment.getName());
		}

		@Override
		protected String failMsg() {
			return StringUtil.repParams(
					ctr.getString(R.string.SellInvoker_failMsg),
					propEquipment.getName());
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().equipmentSell(eic.getId());
			rs = resp.getRic();
		}

		@Override
		protected void onOK() {
			// rs.setMsg("出售成功");
			ctr.updateUI(rs, true);
			ctr.alert(StringUtil.repParams(
					ctr.getString(R.string.SellInvoker_onOK),
					"<br/>#" + propEquipment.getIcon() + "#"
							+ propEquipment.getName() + "X1" + "<br/><br/>",
					"<br/>#money#+" + rs.getMoney()));
			dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == okBtn) {
			new EquipmentSellInvoker(eic).start();
		}
	}

}
