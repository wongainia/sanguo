package com.vikings.sanguo.message;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.util.Log;

import com.vikings.sanguo.utils.BytesUtil;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class QueryServerResp extends BaseResp {

	private InetAddress addr = null;

	private short port;
	
	protected void fromBytes(byte[] buf, int index) {
		String ip = BytesUtil.getString(buf, index, Constants.MAX_LEN_IP_ADDR);
		index += Constants.MAX_LEN_IP_ADDR;
		try {
			addr = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			Log.e("QueryServerResp", "error:"+ip, e);
		}
		this.port = BytesUtil.cNetByte2short(buf, index);
		index += Constants.SHORT_LEN;
	}

	public InetAddress getAddr() {
		return addr;
	}
	
	public int getPort() {
		return port;
	}
}
