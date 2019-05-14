package com.vikings.sanguo.model;

import com.vikings.sanguo.exception.GameException;

public class ArmInfoSelectData extends SelectData {
	private ArmInfoClient aic;
	private boolean isMine; // true：非系统赠送，false：系统赠送（用于显示士兵头像上的，系统赠送标签）
	private boolean changeCount = false;// 是否可以选择数量 (只有自己的兵，该数据才有效)

	public ArmInfoSelectData(ArmInfoClient aic, boolean isMine,
			boolean changeCount) {
		this.aic = aic;
		this.isMine = isMine;
		this.changeCount = changeCount;
		selCount = aic.getCount();
	}

	public ArmInfoSelectData(int id, int count, int selCount, boolean isMine,
			boolean changeCount) throws GameException {
		this.aic = new ArmInfoClient(id, count);
		this.isMine = isMine;
		this.selCount = selCount;
		this.changeCount = changeCount;
	}

	public boolean canChangeCount() {
		return isMine && changeCount;
	}

	public void setAic(ArmInfoClient aic) {
		this.aic = aic;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public ArmInfoClient getAic() {
		return aic;
	}

	public boolean isMine() {
		return isMine;
	}

	@Override
	public void setSelCount(int selCount) {
		this.selCount = selCount;
		if (this.selCount > aic.getCount())
			this.selCount = aic.getCount();
	}

	public int getLeftCount() {
		int i = aic.getCount() - getSelCount();
		if (i < 0)
			return 0;
		else
			return i;
	}

	@Override
	public int getMax() {
		return aic.getCount();
	}
}
