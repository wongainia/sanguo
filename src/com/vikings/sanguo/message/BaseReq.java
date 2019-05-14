package com.vikings.sanguo.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

import android.util.Log;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.AesUtil;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.RsaUtil;

abstract public class BaseReq {

	private static final CRC32 crc32 = new CRC32();

	private static LinkedBuffer linkedBuffer = LinkedBuffer.allocate(512);
	
	public static int seq = 0;

	private MsgHeader header;

	private boolean isProbuf = false;

	public MsgHeader getHeader() {
		return header;
	}

	public void setHeader(MsgHeader header) {
		this.header = header;
	}

	abstract protected void toBytes(OutputStream out);

	/**
	 * 需要写入probuf消息时请使用此方法
	 * 
	 * @param probuf
	 * @param out
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void writeProbuf(Schema probuf, OutputStream out) {
		isProbuf = true;
		try {
			ProtobufIOUtil.writeTo(out, probuf, probuf, linkedBuffer);
		} catch (Exception e) {
			Log.e(probuf.messageFullName(), "write error",e);
		}finally{
			linkedBuffer.clear();
		}
	}

	protected int getUserId() {
		if (Account.user == null)
			return 0;
		else
			return Account.user.getId();
	}

	private byte[] encryptHeader(byte[] buf) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream(16);
		crc32.reset();
		crc32.update(buf);
		BytesUtil.putLong(out, crc32.getValue());
		BytesUtil.putLong(out, BytesUtil.getLong(DateUtil.getTimeSS(), seq++));
		if (Config.aesKey == null) {
			return new byte[16];
		} else {
			return AesUtil.encrypt(out.toByteArray(), Config.aesKey, Config.iv);
		}
	}

	public abstract short cmd();

	private byte[] packData(byte[] data, byte mode) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BytesUtil.putByte(out, mode);
		BytesUtil.putInt(out, data.length);
		switch (mode) {
		// 客户端目前只需要rsa加密数据区
		case CMD.ENCODE_RSA:
			out.write(RsaUtil.encrypt(Config.pubKey, data));
			break;
		default:
			out.write(data);
			break;
		}
		return out.toByteArray();
	}

	public byte[] getBytes() throws GameException {
		// 先获取逻辑层 数据包
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		this.toBytes(out);
		byte[] body = out.toByteArray();

		// 开始组装消息包body数据 userid+checksum+data
		out = new ByteArrayOutputStream();
		BytesUtil.putInt(out, getUserId());
		BytesUtil.putLong(out, Config.sessionId);
		try {
			out.write(encryptHeader(body));
		} catch (Exception e) {
			throw new GameException("pack msg header error", e);
		}
		// 打包数据区
		try {
			body = packData(body, isProbuf ? CMD.ENCODE_PROTOBUF
					: CMD.ENCODE_RAW);
			// StringBuilder b = new StringBuilder();
			// for (int i = 0; i < body.length; i++) {
			// b.append(Integer.toHexString(body[i] & 0xFF)).append(" ");
			// }

			if (CMD.rsa.contains(cmd())) {
				body = packData(body, CMD.ENCODE_RSA);
				//
				// b = new StringBuilder();
				// for (int i = 0; i < body.length; i++) {
				// b.append(Integer.toHexString(body[i]& 0xFF)).append(" ");
				// }

				// byte[] test = RsaUtil.decrypt(
				// RsaUtil.loadPrivateKey(FileUtil.read(Config
				// .getController().getResources()
				// .openRawResource(R.raw.pri))), body, 5,
				// body.length - 5);
				// b = new StringBuilder();
				// for (int i = 0; i < test.length; i++) {
				// b.append(Integer.toHexString(test[i] & 0xFF)).append(" ");
				// }

			}

		} catch (Exception e) {
			throw new GameException("pack msg body error", e);
		}
		try {
			out.write(body);
		} catch (IOException e) {
		}
		body = out.toByteArray();
		out = new ByteArrayOutputStream();
		// 组装消息包结构 header+body
		BytesUtil.putInt(out, body.length);
		BytesUtil.putShort(out, cmd());
		try {
			out.write(body);
		} catch (IOException e) {
		}
		return out.toByteArray();
	}
}
