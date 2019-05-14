package com.vikings.pay;

public interface OnChargeSubmitListener {

	void onSubmitOrder(String orderId, boolean ok,int channel,String error);

}
