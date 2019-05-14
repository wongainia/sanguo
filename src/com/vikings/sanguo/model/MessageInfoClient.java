package com.vikings.sanguo.model;

import java.io.Serializable;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.MessageInfo;

/**
 * 聊天消息
 * 
 * @author susong
 */
public class MessageInfoClient implements Serializable,
		Comparable<MessageInfoClient> {

	private static final long serialVersionUID = 4760127177486503258L;
	// 聊天消息
	public static final int TYPE_CHAT = 1;

	private MessageInfo messageInfo;
	private int to; // 接收

	private String sendState = null;
	private boolean read = true;
	private transient BriefUserInfoClient bic;

	private MessageInfoClient() {
	}

	public MessageInfoClient(int from, int to, String msg) {
		this.messageInfo = new MessageInfo();
		setType(TYPE_CHAT).setFrom(from).setTo(to)
				.setTime((int) (Config.serverTime() / 1000)).setContext(msg);
	}

	public MessageInfoClient(BriefUserInfoClient fromUser,
			BriefUserInfoClient toUser, String msg) {
		this(fromUser.getId(), toUser.getId(), msg);
	}

	public long getId() {
		return null == messageInfo ? 0 : messageInfo.getId();
	}

	public MessageInfoClient setId(long id) {
		if (null != messageInfo)
			messageInfo.setId(id);
		return this;
	}

	public int getFrom() {
		return null == messageInfo ? 0 : messageInfo.getFrom();
	}

	public MessageInfoClient setFrom(int from) {
		if (null != messageInfo)
			messageInfo.setFrom(from);
		return this;
	}

	public int getTo() {
		return to;
	}

	public MessageInfoClient setTo(int to) {
		this.to = to;
		return this;
	}

	public MessageInfoClient setContext(String msg) {
		if (null != messageInfo)
			messageInfo.setContext(msg);
		return this;
	}

	public String getContext() {
		return null == messageInfo ? "" : messageInfo.getContext();
	}

	public MessageInfoClient setTime(int time) {
		if (null != messageInfo)
			messageInfo.setTime(time);
		return this;
	}

	public int getTime() {
		return null == messageInfo ? 0 : messageInfo.getTime();
	}

	public MessageInfoClient setType(int type) {
		if (null != messageInfo)
			messageInfo.setType(type);
		return this;
	}

	public int getType() {
		return null == messageInfo ? 0 : messageInfo.getType();
	}

	public String getSendState() {
		return sendState;
	}

	public MessageInfoClient setSendState(String sendState) {
		this.sendState = sendState;
		return this;
	}

	public MessageInfo getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(MessageInfo messageInfo) {
		this.messageInfo = messageInfo;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public BriefUserInfoClient getBic() {
		return bic;
	}

	public void setBic(BriefUserInfoClient bic) {
		this.bic = bic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getFrom();
		result = prime * result + getContext().hashCode();
		result = prime * result + getTime();
		result = prime * result + getTo();
		result = prime * result + getType();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageInfoClient other = (MessageInfoClient) obj;
		if (getId() != other.getId())
			return false;
		if (getFrom() != other.getFrom())
			return false;
		if (!getContext().equals(other.getContext()))
			return false;
		if (getTime() != other.getTime())
			return false;
		if (getTo() != other.getTo())
			return false;
		if (getType() != other.getType())
			return false;
		return true;
	}

	public static MessageInfoClient convert(MessageInfo mi, boolean read) {
		if (mi == null)
			return null;
		MessageInfoClient mic = new MessageInfoClient();
		mic.setMessageInfo(mi);
		mic.setRead(read);
		mic.setTo(Account.user.getId());
		return mic;
	}

	@Override
	public int compareTo(MessageInfoClient another) {
		return getTime() - another.getTime();
	}
}
