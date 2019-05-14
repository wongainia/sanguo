package com.vikings.sanguo.model;

public class StatInfo {

	private int itemId;

	private int recv;

	private int steal;

	private Item item;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getRecv() {
		return recv;
	}

	public void setRecv(int recv) {
		this.recv = recv;
	}

	public int getCount() {
		return this.recv + this.steal;
	}

	public int getSteal() {
		return steal;
	}

	public void setSteal(int steal) {
		this.steal = steal;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}
