package com.vikings.sanguo.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.DictCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.BaseReq;
import com.vikings.sanguo.message.BaseResp;
import com.vikings.sanguo.message.MsgHeader;
import com.vikings.sanguo.utils.StringUtil;

public class SocketShortConnector {

	private static final SocketShortConnector instance = new SocketShortConnector();

	private final static String tag = "SocketShortConnector";

	private InetAddress addr;

	public int port;

	private int timeout;

	private boolean addrFetched = false;

	private boolean test = false;

	public static SocketShortConnector getInstance() {
		return instance;
	}

	public SocketShortConnector() {
		reset();
	}

	public boolean isAddrInvalid() {
		return !addrFetched || addr == null
				|| StringUtil.isNull(addr.toString())
				|| addr.toString().contains("localhost");
	}

	public void reset() {
		String ip = Config.gameIp;
		try {
			if (!test)
				addr = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
		}
		port = Config.gamePort;
		timeout = Config.getIntConfig("serverTimeout");
		addrFetched = false;
	}

	// 尝试自动调整地址
	public void tryConn() throws GameException {
		// 如果配置未加載，单独加载dict
		DictCache dc = CacheMgr.dictCache;
		if (dc == null) {
			dc = new DictCache();
			try {
				dc.init();
			} catch (GameException e) {
			}
		}
		int[] v = { 1, 2, 3 };
		String[] gameIps = dc.getDict(10001, v);
		// String[] resIps = dc.getDict(10002, v);
		// String[] snsIps = dc.getDict(10003, v);
		int connTime = Integer.MAX_VALUE;
		for (int i = 0; i < gameIps.length; i++) {
			try {
				long time = System.currentTimeMillis();
				Socket socket = new Socket(gameIps[i], port);
				socket.setSoTimeout(3000);
				socket.getOutputStream();
				socket.getInputStream();
				time = System.currentTimeMillis() - time;
				if (time < connTime) {
					connTime = (int) time;
					try {
						addr = InetAddress.getByName(gameIps[i]);
					} catch (UnknownHostException e) {
					}
					// Config.resURl = resIps[i];
					// Config.snsUrl = snsIps[i];
				}
			} catch (Exception e) {
			}
		}
		if (connTime == Integer.MAX_VALUE)
			throw new GameException("无法连接到服务器，请检查网络");
	}

	public void setAddr(InetAddress gsAddr, int addrPort) {
		addr = gsAddr;
		port = addrPort;
		addrFetched = true;
	}

	public void setPort(int addrPort) {
		port = addrPort;
	}

	public void setAddr(InetAddress gsAddr) {
		addr = gsAddr;
		addrFetched = false;
		test = true;
	}

	private void read(InputStream in, byte[] buf) throws IOException {
		int count = 0;
		int length = buf.length;
		while (count != length) {
			int i = in.read(buf, count, length - count);
			if (i != -1) {
				count = count + i;
			} else {
				throw new IOException("链接被断开");
			}
		}
	}

	/**
	 * 单向请求
	 * 
	 * @param req
	 * 
	 */
	synchronized public void post(BaseReq req) {
		Socket socket = null;
		try {
			socket = new Socket(addr, port);
			socket.setSoTimeout(500);
			OutputStream out = socket.getOutputStream();
			// send
			out.write(req.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			Log.e(tag, "IOException", e);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	/**
	 * 请求 应答
	 * 
	 * @param req
	 * @param resp
	 * @throws GameException
	 */
	synchronized public void send(BaseReq req, BaseResp resp)
			throws GameException {

		// Config.getController().testPost(new Runnable() {
		// @Override
		// public void run() {
		// Toast.makeText(Config.getController().getMainActivity(),
		// addr.toString(), Toast.LENGTH_LONG).show();
		// }
		// });

		Socket socket = null;
		long time = System.currentTimeMillis();
		long connTime = 0;
		long sendTime = 0;
		long recvTime = 0;
		try {
			if (addr == null)
				throw new GameException("网络异常，请检查数据连接重试");
			socket = new Socket(addr, port);
			socket.setSoTimeout(timeout);
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();

			connTime = System.currentTimeMillis() - time;
			time = System.currentTimeMillis();

			// send
			byte[] send = req.getBytes();
			out.write(send);
			out.flush();
			NetStat.getInstance().log(req.cmd(), true, send.length);

			sendTime = System.currentTimeMillis() - time;
			time = System.currentTimeMillis();

			// receive
			MsgHeader header = new MsgHeader();
			byte[] headerBytes = new byte[header.size()];
			read(in, headerBytes);
			header.fromBytes(headerBytes);

			resp.setHeader(header);

			byte[] buf = new byte[header.getLen()];
			read(in, buf);

			recvTime = System.currentTimeMillis() - time;
			NetStat.getInstance().log(req.cmd(), false,
					header.size() + buf.length);
			NetStat.getInstance().log(req.cmd(), connTime, sendTime, recvTime);
			resp.decode(buf);
		} catch (IOException e) {
			String text = "";
			if (Config.getController() != null)
				text = Config.getController().getString(
						R.string.network_anomalies_2);
			text = "<br/><br/>" + text;
			throw new GameException(text, e);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	public static String netstat() {
		return NetStat.getInstance().toString();
	}

}
