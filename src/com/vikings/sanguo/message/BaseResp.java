package com.vikings.sanguo.message;

import android.util.Log;

import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.AesUtil;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.GzipUtil;

abstract public class BaseResp {

	private MsgHeader header;

	private short result = -1;

	public MsgHeader getHeader() {
		return header;
	}

	public void setHeader(MsgHeader header) {
		this.header = header;
	}

	public short getResult() {
		return result;
	}

	public void setResult(short result) {
		this.result = result;
	}

	protected abstract void fromBytes(byte[] buf, int index) throws Exception;

	public boolean isOK() {
		return result == ResultCode.RESULT_SUCCEED;
	}

	private byte[] unpackData(byte[] data, int index) throws Exception {
		int dataStart = Constants.INT_LEN + 1 + index;
		if (data.length < dataStart)
			throw new GameException("返回消息包错误");
		byte mode = data[index++];
		int ogrLen = BytesUtil.getInt(data, index);
		index += Constants.INT_LEN;
		byte[] buf = new byte[data.length - dataStart];
		System.arraycopy(data, dataStart, buf, 0, buf.length);
		switch (mode) {
		case CMD.ENCODE_AESC:
			buf = AesUtil.decrypt(buf, Config.clientKey, Config.iv);
			break;
		case CMD.ENCODE_AESS:
			if (Config.aesKey == null)
				throw new GameException("no token.. please login first");
			buf = AesUtil.decrypt(buf, Config.aesKey, Config.iv);
			break;
		case CMD.ENCODE_COMPRESS:
			buf = GzipUtil.decompress(buf);
			break;
		default:
			break;
		}
		if (ogrLen != buf.length)
			throw new GameException("lengh changed, broken package.");
		if (mode == CMD.ENCODE_RAW || mode == CMD.ENCODE_PROTOBUF)
			return buf;
		else
			return unpackData(buf, 0);
	}

	public void decode(byte[] body) throws GameException {
		int index = 0;
		result = BytesUtil.getShort(body, index);
		// Log.e("cmd=" + header.getCmd(), "result = " + result);
		index += Constants.SHORT_LEN;
		try {
			if (index < body.length) {
				byte[] buf = unpackData(body, index);
				// 只在请求成功解包
				if (isOK())
					fromBytes(buf, 0);
			}
		} catch (Exception e) {
			if (isOK()) {
				if (e instanceof GameException) {
					throw (GameException) e;
				} else
					throw new GameException("通讯解码错误", e);
			}
		}
		if (!isOK())
			throw new GameException(this.getHeader().getCmd(), result, this);
	}

}
