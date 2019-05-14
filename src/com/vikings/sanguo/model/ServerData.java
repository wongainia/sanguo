package com.vikings.sanguo.model;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.StringUtil;

public class ServerData implements Comparable<ServerData> {
	private final int FULL = 100;
	private final int BUSY = 99;
	private final int SMOOTH = 0;
	private final int REPAIR = -1;

	private int serverId;
	private String name;
	private int state; // 100爆满， 80繁忙， 维护-1
	private int labelId;
	private String serverUrl;
	private int port;
	private String resourceUrl;
	private String snsUrl;
	private String payUrl;

	private int fiefSize;

	private int zoomLevel;

	private int check;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getSnsUrl() {
		return snsUrl;
	}

	public void setSnsUrl(String snsUrl) {
		this.snsUrl = snsUrl;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public void setFiefSize(int fiefSize) {
		this.fiefSize = fiefSize;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public int getFiefSize() {
		return fiefSize;
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public boolean isCheck() {
		return this.check == 1;
	}

	public static ServerData fromString(String csv) {
		ServerData data = new ServerData();
		StringBuilder buf = new StringBuilder(csv);
		data.setServerId(StringUtil.removeCsvInt(buf));
		data.setName(StringUtil.removeCsv(buf));
		data.setState(StringUtil.removeCsvInt(buf));
		data.setLabelId(StringUtil.removeCsvInt(buf));
		data.setServerUrl(StringUtil.removeCsv(buf));
		data.setPort(StringUtil.removeCsvInt(buf));
		data.setResourceUrl(StringUtil.removeCsv(buf));
		data.setSnsUrl(StringUtil.removeCsv(buf));
		data.setPayUrl(StringUtil.removeCsv(buf));
		data.setFiefSize(StringUtil.removeCsvInt(buf));
		data.setZoomLevel(StringUtil.removeCsvInt(buf));
		data.setCheck(StringUtil.removeCsvInt(buf));
		return data;
	}

	@Override
	public int compareTo(ServerData another) {
		return another.getServerId() - getServerId();
	}

	public boolean isNew() {
		return labelId == 1;
	}

	public boolean isRecommend() {
		return labelId == 2;
	}

	public boolean isFull() {
		return state >= FULL;
	}

	public boolean isBusy() {
		return state < FULL && state >= BUSY;
	}

	public boolean isRepair() {
		return state == REPAIR;
	}

	public int getStateImgId() {
		if (state >= FULL) {
			return R.drawable.txt_state_bm;
		} else if (state < FULL && state >= BUSY) {
			return R.drawable.txt_state_fm;
		} else if (state < BUSY && state >= SMOOTH) {
			return R.drawable.txt_state_lc;
		} else {
			return R.drawable.txt_state_wh;
		}
	}

	public String getColor() {
		if (state >= FULL) {
			return "#ffffff";
		} else if (state < FULL && state >= BUSY) {
			return "#ffffff";
		} else if (state < BUSY && state >= SMOOTH) {
			return "#ffffff";
		} else {
			return "#ffffff";
		}
	}
}
