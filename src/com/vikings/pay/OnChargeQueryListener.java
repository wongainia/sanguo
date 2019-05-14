package com.vikings.pay;

public interface OnChargeQueryListener {

	void onQueryResult(String orderId, boolean ok,String error);

}
