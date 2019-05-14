package com.vikings.sanguo.invoker;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Item;

public class BuyAndOpenGiftBackInvoker extends BuyAndOpenGiftInvoker {

	public BuyAndOpenGiftBackInvoker(Item item) {
		super(item);
	}

	@Override
	protected void beforeFire() {

	}

	@Override
	protected void afterFire() {

	}

	@Override
	protected void onFail(GameException exception) {

	}

}
