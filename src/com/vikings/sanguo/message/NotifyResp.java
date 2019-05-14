package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.model.PushData;
import com.vikings.sanguo.utils.BytesUtil;


public class NotifyResp extends BaseResp {
	
	private List<PushData> rs;

	public List<PushData> getRs() {
		return rs;
	}
	
	@Override
	protected void fromBytes(byte[] buf, int index) {
		short count = BytesUtil.getShort(buf, index);
		index += Constants.SHORT_LEN;
		rs = new ArrayList<PushData>();
		for (int i = 0; i < count; i++) {
			PushData p = new PushData();
			p.setType(buf[index++]);
			int time = BytesUtil.getInt(buf, index);
			index += Constants.INT_LEN;
			p.setTime(time);
//			如果消息过期  就不处理了
			p.setMsg(BytesUtil.getString(buf, index, Constants.MAX_LEN_MESSAGE));
			rs.add(p);
			index += Constants.MAX_LEN_MESSAGE;
		}
	}
}
