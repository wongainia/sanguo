package com.vikings.sanguo.model;

public class Flag {
	private boolean dataChange = true;

	public Flag(boolean dataChange) {
		this.dataChange = dataChange;
	}

	public boolean isDataChange() {
		return dataChange;
	}

	public void setDataChange(boolean dataChange) {
		this.dataChange = dataChange;
	}

}
