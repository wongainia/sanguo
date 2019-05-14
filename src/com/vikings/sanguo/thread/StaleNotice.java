package com.vikings.sanguo.thread;

import java.util.List;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.model.ReturnEffectInfoClient;
import com.vikings.sanguo.protos.ReturnEffectInfo;


public  class  StaleNotice implements Runnable {

	List<LogInfoClient> logs;

	public StaleNotice(List<LogInfoClient> logs) {
		this.logs = logs;
	}

	@Override
	public void run() {
		Config.getController().alert(getAlertMsg());
	}

	private String getAlertMsg() {
		StringBuffer sbf = new StringBuffer();
		for (LogInfoClient log : logs) {
			for (ReturnEffectInfo ri : log.getLogInfo().getReisList()) {
				ReturnEffectInfoClient returnInfo = new ReturnEffectInfoClient();
				returnInfo.setEffectId((byte) ri.getField().intValue());
				returnInfo.setValue(ri.getValue());
				Item item = returnInfo.getItem();
				if (item != null)
					sbf.append(item.getName()).append("已经过期<br>");
			}
		}
		return sbf.toString();
	}
}