package com.vikings.sanguo.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.util.Log;

import com.vikings.sanguo.controller.GameController;

public class ControllerHandler implements InvocationHandler {

	private GameController controller;

	public ControllerHandler(GameController controller) {
		this.controller = controller;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		doBefore();
		Object v = null;
		try {
			v = method.invoke(controller, args);
		} catch (Exception e) {
			onError(e);
		}
		doAfter();
		return v;
	}

	private void doBefore() {

	}

	private void doAfter() {

	}

	private void onError(Exception e) {
		Throwable cause = e.getCause();
		if (cause == null)
			return;
		Log.e("GameController", cause.getMessage(), cause);
	}

}
