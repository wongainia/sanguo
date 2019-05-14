package com.vikings.sanguo.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class NetStat {

	private static NetStat instance = new NetStat();

	private Map<Integer, BytesStat> map = new HashMap<Integer, BytesStat>();

	private List<String> speedStat = new LinkedList<String>();
	private List<String> tmp;

	private final static String separator = ",";

	public static NetStat getInstance() {
		return instance;
	}

	public void log(int cmd, boolean send, int size) {
		if (!map.containsKey(cmd)) {
			map.put(cmd, new BytesStat(cmd));
		}
		map.get(cmd).count++;
		if (send)
			map.get(cmd).send += size;
		else
			map.get(cmd).recv += size;
	}

	public void log(int cmd, long connectTime, long sendTime, long recvTime) {
		if (!Setting.speedStat)
			return;
		StringBuilder buf = new StringBuilder();
		buf.append(DateUtil.timeStampFormatTrade.format(new Date()))
				.append(separator).append(cmd).append(separator)
				.append(connectTime).append(separator).append(sendTime)
				.append(separator).append(recvTime).append("\n");
		speedStat.add(buf.toString());
		if (speedStat.size() >= Setting.speedStatCount) {
			upload();
		}
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		ArrayList<BytesStat> ls = new ArrayList<BytesStat>(map.values());
		Collections.sort(ls);
		int send = 0;
		int recv = 0;
		for (BytesStat s : ls) {
			buf.append("[")
					.append(s.cmd)
					.append("*" + s.count + "]")
					.append(",")
					.append(Config.getController().getString(
							R.string.NetStat_toString_2))
					.append(s.send)
					.append("B,")
					.append(Config.getController().getString(
							R.string.NetStat_toString_1)).append(s.recv)
					.append("B").append("<br/>");
			send += s.send;
			recv += s.recv;
		}
		buf.insert(
				0,
				"total:"
						+ (send + recv)
						+ "B,"
						+ Config.getController().getString(
								R.string.NetStat_toString_2)
						+ send
						+ "B,"
						+ Config.getController().getString(
								R.string.NetStat_toString_1) + recv + "B<br/>");
		return buf.toString();
	}

	public String getFlowConsume() {
		ArrayList<BytesStat> ls = new ArrayList<BytesStat>(map.values());
		Collections.sort(ls);
		float send = 0;
		float recv = 0;
		for (BytesStat s : ls) {
			send += s.send;
			recv += s.recv;
		}
		String flowConsume = "";
		if (!Config.isWifi()) {
			flowConsume = "<br/><br/>"
					+ StringUtil.repParams(
							Config.getController().getString(
									R.string.NetStat_getFlowConsume),
							formatSize(send + recv)) + "<br/>";
		} else {
			flowConsume = "";
		}
		return flowConsume;
	}

	public String formatSize(float size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		// if (size < kb) {
		// return String.format("%d b", (int) size);
		// } else if (size < mb) {
		// return String.format("%.2f Kb", size / kb); // 保留两位小数
		// } else if (size < gb) {
		// return String.format("%.2f Mb", size / mb);
		// } else {
		// return String.format("%.2f Gb", size / gb);
		// }
		if (size < gb) {
			return String.format("%.2f MB", size / mb);
		}
		return "";
	}

	// public String formatSize(double size) {
	// DecimalFormat df = new DecimalFormat("###.##");
	// String[] units = new String[] { "B", "KB", "MB", "GB", "TB", "PB" };
	// double mod = 1024.0;
	// int i = 0;
	// for (i = 0; size >= mod; i++) {
	// size /= mod;
	// }
	// return df.format(new Float(size).doubleValue()) + units[i];
	// }

	private void upload() {
		tmp = speedStat;
		speedStat = new LinkedList<String>();
		new Thread(new Upload(tmp)).start();
	}

	class BytesStat implements Comparable<BytesStat> {
		int cmd;
		int count = 0;
		int send = 0;
		int recv = 0;

		public BytesStat(int cmd) {
			this.cmd = cmd;
		}

		@Override
		public int compareTo(BytesStat another) {
			return (another.send + another.recv) - (send + recv);
		}
	}

	class Upload implements Runnable {

		List<String> tmp;

		public Upload(List<String> tmp) {
			super();
			this.tmp = tmp;
		}

		@Override
		public void run() {
			// 获取网络连接信息
			int userId = 0;
			if (Account.user != null)
				userId = Account.user.getId();
			tmp.add(0, "*nw*:" + Config.getNetworkInfo() + "*id*:" + userId
					+ "\n");
			HttpConnector.getInstance().uploadSpeedStat(tmp);
			tmp.clear();
			tmp = null;
		}

	}

}
