package com.vikings.sanguo.model;

public abstract class SelectData {
	protected int selCount;

	public int getSelCount() {
		return selCount;
	}

	public void setSelCount(int selCount) {
		this.selCount = selCount;
	}

	public abstract int getMax();
}
