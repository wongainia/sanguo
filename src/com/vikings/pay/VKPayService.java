package com.vikings.pay;

public abstract class VKPayService {

	protected int game;

	protected int channel;

	protected OnChargeSubmitListener onChargeSubmitListener;

	public VKPayService(int game) {
		this.game = game;
	}

	protected VKPayService setOnChargeSubmitListener(
			OnChargeSubmitListener onChargeSubmitListener) {
		this.onChargeSubmitListener = onChargeSubmitListener;
		return this;
	}

	protected VKPayService setChannel(int channel) {
		this.channel = channel;
		return this;
	}

	public abstract void pay(int userId, int amount, String exParam);
}
