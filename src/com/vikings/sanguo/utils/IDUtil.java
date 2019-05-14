package com.vikings.sanguo.utils;

import java.net.URLEncoder;
import java.util.zip.CRC32;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;

public class IDUtil {

	public static String getMyId(){
		byte[] src = new byte[16];
		BytesUtil.putInt(src, Account.user == null?0:Account.user.getId(),0);
		BytesUtil.putInt(src, (int)(Config.serverTime()/1000) ,4);
		CRC32 crc32 = new CRC32();
		crc32.reset();
		crc32.update(src,0,8);
		BytesUtil.putLong(src, crc32.getValue(),8);
		try {
			return URLEncoder.encode(Base64.encode(AesUtil.encrypt(src, Config.configKey, Config.iv)),"UTF-8");
		} catch (Exception e) {
			return null;
		}
	}
	
}
