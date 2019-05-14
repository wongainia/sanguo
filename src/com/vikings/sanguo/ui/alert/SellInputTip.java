package com.vikings.sanguo.ui.alert;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.invoker.SellInvoker;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class SellInputTip extends CustomConfirmDialog implements TextWatcher {
	private static final int layout = R.layout.alert_sell_input;

	private Button okBtn,closeBtn;
	private ItemBag itemBag;
	private Item item;

	
	private TextView price, total,desc;
	private EditText amount;

	public SellInputTip(ItemBag itemBag) {
		super("出售 " + itemBag.getItem().getName());
		this.itemBag = itemBag;
		this.item = itemBag.getItem();
		price = (TextView) content.findViewById(R.id.price);
		total = (TextView) content.findViewById(R.id.total);
		desc=(TextView) content.findViewById(R.id.desc);
		amount = (EditText) content.findViewById(R.id.amount);
		amount.addTextChangedListener(this);
		okBtn=(Button) content.findViewById(R.id.okBtn);
		
		closeBtn=(Button) content.findViewById(R.id.closeBtn);
		
	}

	public void show() {
		if (item == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		ViewUtil.setText(price, item.getSell());
		ViewUtil.setText(amount, itemBag.getCount());
		if (itemBag.getItem().needSellConfirm()){
			ViewUtil.setVisible(desc);
			ViewUtil.setText(desc, "该物品为贵重物品!确认是否出售?");
		}else{
			ViewUtil.setGone(desc);
		}
		
		setTotal(itemBag.getCount());
		
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sell();
			}
		});
		closeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	private void sell() {
		if (StringUtil.isNull(amount.getText().toString())) {
			controller.alert("物品数量不能为空");
			return;
		}

		if (Integer.valueOf(amount.getText().toString()) == 0) {
			controller.alert("数量不能为0");
			return;
		}

		final int count = Integer.valueOf(amount.getText().toString());

		if (count > itemBag.getCount()) {
			controller.alert("物品数量输入有误");
			return;
		}

		if (itemBag.getItem().needSellConfirm()) {
			new CommonCustomAlert("确认出售", CommonCustomAlert.DEFAULT, false,
					StringUtil.color("该物品为贵重物品，请确认是否出售", R.color.color5)
							+ StringUtil.color("" + count, R.color.color24)
							+ StringUtil.color("个", R.color.color5)
							+ StringUtil.color(itemBag.getItem().getName(),
									R.color.color24), "确  定", new CallBack() {

						@Override
						public void onCall() {
							new SellItemInvoker(itemBag, count).start();
						}
					}, "", null, "取  消", true).show();
			dismiss();
			return;
		}

		new SellItemInvoker(itemBag, count).start();

	}

	private int getAmount() {
		return StringUtil.parseInt(amount.getText().toString());
	}

	private int getTotal(int count) {
		return item.getSell() * count;
	}

	private void setTotal(int count) {
		ViewUtil.setText(total, getTotal(count));
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout,contentLayout, false);
	}

	private class SellItemInvoker extends SellInvoker {

		public SellItemInvoker(ItemBag itemBag, int count) {
			super(itemBag, count);
		}

		@Override
		protected void onOK() {
			super.onOK();
			dismiss();
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String str = amount.getText().toString().trim();
	
			if (str.length() == 0) {
//				ViewUtil.setEditText(amount, "1");
				setTotal(0);
			} else {
				int cnt = getAmount();
				if (cnt > itemBag.getCount()) {
					ViewUtil.setEditText(amount, String.valueOf(itemBag.getCount()));
					setTotal(itemBag.getCount());
				} else {
					setTotal(cnt);
				}
			}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

}
