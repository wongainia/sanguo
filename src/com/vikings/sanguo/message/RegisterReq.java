package com.vikings.sanguo.message;

import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.MsgRegisterReq;
import com.vikings.sanguo.security.DeviceInfo;
import com.vikings.sanguo.utils.StringUtil;

public class RegisterReq extends BaseReq {

	private MsgRegisterReq req;

	protected String extend = "";
	protected String imsi = "";
	protected String pId = "";

	public RegisterReq(String nick, int sex, int province, String partnerId) {
		req = new MsgRegisterReq();
		this.pId = partnerId;
		DeviceInfo info = DeviceInfo.getInstance();
		imsi = info.getImsi();
		initExtend(nick, sex, info);
		req.setSim(imsi).setProgramid(Config.clientCode)
				.setChannel(Config.getChannel()).setProvince(province)
				.setExtend(extend);
		if (!StringUtil.isNull(pId))
			req.setPartnerId(pId);
	}

	private void initExtend(String nick, int sex, DeviceInfo info) {
		JSONObject obj = DeviceInfo.getInstance().getJSONObject();
		try {
			obj.put("clientVer", Config.getClientVer());
			if (!StringUtil.isNull(nick) && sex != 0) {
				obj.put("nick", nick);
				obj.put("sex", sex);
			}
		} catch (JSONException e) {
		}
		extend = obj.toString();
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_REGISTER
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
