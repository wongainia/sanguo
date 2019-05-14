package com.vikings.sanguo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.vikings.sanguo.message.Constants;

public class BytesUtil {

	public final static String encoding = "UTF-16LE";

	public static final byte end_code = (byte) '\0';

	private static final byte[] buf = new byte[1];

	private static final byte[] buf2 = new byte[2];

	private static final byte[] buf4 = new byte[4];

	private static final byte[] buf8 = new byte[8];

	public static void putByte(OutputStream out, byte b) {
		try {
			buf[0] = b;
			out.write(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int putShort(byte b[], short s, int index) {
		b[index + 1] = (byte) (s >> 8);
		b[index] = (byte) (s >> 0);
		return index + 2;
	}

	public static void putShort(OutputStream out, short s) {
		putShort(buf2, s, 0);
		try {
			out.write(buf2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static short getShort(byte[] b, int index) {
		return (short) (((b[index + 1] << 8) | b[index] & 0xff));
	}

	// ///////////////////////////////////////////////////////
	public static int putInt(byte[] bb, int x, int index) {
		bb[index + 3] = (byte) (x >> 24);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 0] = (byte) (x >> 0);
		return index + 4;
	}

	public static void putInt(OutputStream out, int x) {
		putInt(buf4, x, 0);
		try {
			out.write(buf4);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getInt(byte[] bb, int index) {
		return (int) ((((bb[index + 3] & 0xff) << 24)
				| ((bb[index + 2] & 0xff) << 16)
				| ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));
	}

	public static short cNetByte2short(byte b[], int index) {
		return (short) (b[index + 1] & 0xff | (b[index + 0] & 0xff) << 8);
	}

	public static int putLong(byte[] bb, long x, int index) {
		bb[index + 7] = (byte) (x >> 56);
		bb[index + 6] = (byte) (x >> 48);
		bb[index + 5] = (byte) (x >> 40);
		bb[index + 4] = (byte) (x >> 32);
		bb[index + 3] = (byte) (x >> 24);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 0] = (byte) (x >> 0);
		return index + 8;
	}

	public static void putLong(OutputStream out, long x) {
		putLong(buf8, x, 0);
		try {
			out.write(buf8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long getLong(byte[] bb, int index) {
		return ((((long) bb[index + 7] & 0xff) << 56)
				| (((long) bb[index + 6] & 0xff) << 48)
				| (((long) bb[index + 5] & 0xff) << 40)
				| (((long) bb[index + 4] & 0xff) << 32)
				| (((long) bb[index + 3] & 0xff) << 24)
				| (((long) bb[index + 2] & 0xff) << 16)
				| (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));
	}

	public static void putString(OutputStream out, String str, int length) {
		if (length == 0)
			return;
		if (str == null)
			str = "";
		try {
			byte[] buf = StringUtil.getBytes(str, encoding);
			if (buf.length >= length) {
				buf[length - 2] = end_code;
				buf[length - 1] = end_code;
				out.write(buf, 0, length);
			} else {
				out.write(buf);
				byte[] padding = new byte[length - buf.length];
				padding[0] = end_code;
				padding[1] = end_code;
				out.write(padding);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 计算动态字符长度 包含c结束符 /0
	 * 
	 * @param out
	 * @param str
	 */
	public static int sizeof(String str) {
		if (StringUtil.isNull(str))
			return 0;
		try {
			byte[] buf = StringUtil.getBytes(str, encoding);
			return buf.length + 2;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return Constants.MAX_LEN_CHAT;
		}
	}

	public static String getString(byte[] bb, int start, int length) {
		int count = 0;
		while (count < length) {
			if (bb[start + count] == end_code
					&& bb[start + count + 1] == end_code)
				break;
			count = count + 2;
		}
		try {
			return new String(bb, start, count, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取指定长度byte
	 * 
	 * @param in
	 * @param buf
	 * @throws IOException
	 */
	public static boolean read(InputStream in, byte[] buf) throws IOException {
		int index = 0;
		int length = buf.length;
		while (index != length) {
			int readCount = in.read(buf, index, length - index);
			if (readCount != -1) {
				index = index + readCount;
			} else {
				return false;
			}
		}
		return true;
	}

	public static long getLong(int i, int j) {
		long l = i;
		l <<= 32;
		l |= j;
		return l;
	}

	// 第0位对应于上面的i，第1位对应上面的j
	public static int[] getInt(long l) {
		int[] is = new int[2];
		is[0] = (int) (l >> 32);
		is[1] = (int) l;
		return is;
	}

	public static byte[] getBytes(byte[] src, int start, int count) {
		byte[] dest = new byte[count];
		System.arraycopy(src, start, dest, 0, count);
		return dest;
	}

}
