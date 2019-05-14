package com.vikings.sanguo.model;

public class RechargeOrderData {
	private String orderId; // 订单号
	private int itemId;// 成功后需要购买的物品

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

}
