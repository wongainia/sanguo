package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.ItemUseResp;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.RewardTip;
import com.vikings.sanguo.ui.alert.UserLevelUpTip;

public class BuyAndOpenGiftInvoker extends BaseInvoker {

	protected Item item;
	protected ReturnInfoClient ric1;
	protected ReturnInfoClient ric2;

	protected ReturnInfoClient ri = null;

	public BuyAndOpenGiftInvoker(Item item) {
		this.item = item;
	}

	@Override
	protected String loadingMsg() {
		return "购买...";
	}

	@Override
	protected String failMsg() {
		return "购买失败";
	}

	@Override
	protected void fire() throws GameException {
		ric1 = GameBiz.getInstance().itemBuy(item.getId(), 1);
		ItemBag itemBag = Account.store.getItemBag(item.getId());
		if (null != itemBag) {
			try {
				ItemUseResp resp = GameBiz.getInstance().itemUse(
						Account.user.getId(), itemBag.getId(),
						itemBag.getItemId());
				ric2 = resp.getRi();
			} catch (GameException e) {

			}
		}

	}

	@Override
	protected void onOK() {
		if (null != ric1)
			ri = ric1;
		if (null != ric2)
			ri = ric2;
		new RewardTip(item.getName() + "来喽!", ri.showReturn(true, false), true,
				true, new CallBack() {

					@Override
					public void onCall() {
						if (ri.getLevel() > 0)
							new UserLevelUpTip(ri).show();

					}
				}).show();
	}

}
