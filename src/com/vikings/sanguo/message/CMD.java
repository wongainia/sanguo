package com.vikings.sanguo.message;

import java.util.HashSet;

public class CMD {

	public static final byte ENCODE_RSA = 1;
	public static final byte ENCODE_AESC = 1 << 1;
	public static final byte ENCODE_AESS = 1 << 2;
	public static final byte ENCODE_COMPRESS = 1 << 3;
	public static final byte ENCODE_PROTOBUF = 1 << 4;
	public static final byte ENCODE_RAW = 1 << 5;

	public static HashSet<Short> rsa = new HashSet<Short>();

	static {
		rsa.add((short) com.vikings.sanguo.protos.CMD.MSG_REQ_LOGIN.getNumber());
		rsa.add((short) com.vikings.sanguo.protos.CMD.MSG_REQ_ACCOUNT_QUERY
				.getNumber());
		rsa.add((short) com.vikings.sanguo.protos.CMD.MSG_REQ_REGISTER
				.getNumber());
		rsa.add((short) com.vikings.sanguo.protos.CMD.MSG_REQ_ACCOUNT_RESTORE
				.getNumber());
		rsa.add((short) com.vikings.sanguo.protos.CMD.MSG_REQ_ACCOUNT_RESTORE2
				.getNumber());
	}

}
