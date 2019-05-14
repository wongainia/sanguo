package com.vikings.sanguo.exception;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.message.BaseResp;

public class GameException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final short RUNTIME_ERROR = -100;

	public static final short ERROR_ACCOUNT = 3;
	
	public static final short ERROR_PASSWORD = 4;
	
	private short result;

	private int cmd;
	
	private String msg;
	
	private BaseResp resp;

	/**
	 * 用于通讯正常，解码正常，服务器返回的游戏逻辑错误
	 * 
	 * @param result
	 */
	public GameException(int cmd,short result,BaseResp resp) {
		this.cmd = cmd;
		this.result = result;
		this.resp = resp;
	}

	/**
	 * 用于发生运行期异常
	 * 
	 * @param result
	 * @param msg
	 * @param e
	 */
	public GameException(String msg, Exception e) {
		super(msg, e);
		this.result = RUNTIME_ERROR;
		this.msg = msg;
	}

	/**
	 * 用于发生运行期异常
	 * 
	 * @param result
	 * @param msg
	 * @param e
	 */
	public GameException(String msg) {
		super(msg);
		this.result = RUNTIME_ERROR;
		this.msg = msg;
	}
	
	public short getResult() {
		return result;
	}

	public int getCmd() {
		return cmd;
	}
	
	public String getErrorMsg() {
		if (result == RUNTIME_ERROR)
			return msg;
		else
			return CacheMgr.errorCodeCache.getMsg(result);
	}
	
	public BaseResp getResp() {
		return resp;
	}
	
	@Override
	public String getMessage() {
		return getErrorMsg();
	}
	
}
