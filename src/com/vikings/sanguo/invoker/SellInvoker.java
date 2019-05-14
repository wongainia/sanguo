package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;

public class SellInvoker extends BaseInvoker {

	private ItemBag itemBag;
	private int count;
	private ReturnInfoClient rs;

	public SellInvoker(ItemBag itemBag, int count) {
		this.itemBag = itemBag;
		this.count = count;
	}

	public void confirm() {
		if (null == itemBag || count <= 0)
			return;
		Item item = itemBag.getItem();
		if (item.needSellConfirm()) {
			ctr.confirm(ctr.getString(R.string.SellInvoker_confirm),
					new CallBack() {

						@Override
						public void onCall() {
							SellInvoker.this.start();
						}
					});
		} else {
			this.start();
		}

	}

	@Override
	protected String loadingMsg() {
		return StringUtil.repParams(ctr
				.getString(R.string.SellInvoker_loadingMsg), itemBag.getItem()
				.getName());
	}

	@Override
	protected String failMsg() {
		return StringUtil.repParams(
				ctr.getString(R.string.SellInvoker_failMsg), itemBag.getItem()
						.getName());
	}

	@Override
	protected void fire() throws GameException {
		rs = GameBiz.getInstance().sellItem(itemBag.getId(), count);
	}

	@Override
	protected void onOK() {
		ctr.updateUI(rs, true);
		ctr.alert(StringUtil.repParams(
				ctr.getString(R.string.SellInvoker_onOK), "<br/>#"
						+ itemBag.getItem().getImage() + "#"
						+ itemBag.getItem().getName() + "X" + count
						+ "<br/><br/>", "<br/>#money#+" + rs.getMoney()));
	}
}
