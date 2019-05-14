package com.vikings.pay;

import org.json.JSONObject;

import com.vikings.sanguo.config.Config;

public class PrepayReq {

	public static void req(final String orderId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject params = new JSONObject();
					params.put("orderId", orderId);
					HttpUtil.httpPost(Config.rechargeUrl + "/charge/prepay", params);
				} catch (Exception e) {
				}
			}
		}).start();
	}
	
}
