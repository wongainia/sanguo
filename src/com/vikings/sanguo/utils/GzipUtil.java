package com.vikings.sanguo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class GzipUtil {

	public static byte[] decompress(byte[] src) throws IOException {
		return decompress(src,0);
	}

	public static byte[] decompress(byte[] src, int start) throws IOException {
		GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(src,
				start, src.length - start));
		byte[] buf = new byte[1024];
		int num = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((num = in.read(buf, 0, buf.length)) != -1) {
			baos.write(buf, 0, num);
		}
		byte[] dest = baos.toByteArray();
		baos.close();
		in.close();
		return dest;
	}

	public static byte[] decompress(InputStream in) throws IOException {
		GZIPInputStream zip = new GZIPInputStream(in);
		byte[] buf = new byte[1024];
		int num = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((num = zip.read(buf, 0, buf.length)) != -1) {
			baos.write(buf, 0, num);
		}
		byte[] dest = baos.toByteArray();
		baos.close();
		in.close();
		return dest;
	}
	
}
