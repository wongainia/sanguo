package com.vikings.sanguo.cache;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

import android.content.Context;
import android.util.Log;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.message.Constants;
import com.vikings.sanguo.utils.AesUtil;
import com.vikings.sanguo.utils.BytesUtil;

public class AESKeyCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7432372736089026645L;

	static final private String file = "aeskey";

	private AESKeyCache() {
	}

	public static void save() {
		try {
			ByteArrayOutputStream arrayout = new ByteArrayOutputStream();
			if (null != Config.aesKey && Config.sessionId != 0) {
				arrayout.write(Config.aesKey.getEncoded());
				BytesUtil.putLong(arrayout, Config.sessionId);
			}

			FileOutputStream out = Config
					.getController()
					.getUIContext()
					.openFileOutput(file + Account.user.getSaveID(),
							Context.MODE_PRIVATE);

			out.write(arrayout.toByteArray());
			out.close();
		} catch (Exception e) {
			Log.e("AESKeyCache", e.getMessage(), e);
		}
	}

	public static void clear(int userId) {
		try {
			ByteArrayOutputStream arrayout = new ByteArrayOutputStream();

			FileOutputStream out = Config.getController().getUIContext()
					.openFileOutput(file + Account.user.getSaveID(), Context.MODE_PRIVATE);

			out.write(arrayout.toByteArray());
			out.close();
		} catch (Exception e) {
			Log.e("AESKeyCache", e.getMessage(), e);
		}
	}

	public static void getInstance() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			FileInputStream in = Config.getController().getUIContext()
					.openFileInput(file + Account.user.getSaveID());

			byte[] buf = new byte[1024];

			int count = -1;
			while ((count = in.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			in.close();

		} catch (Exception e) {
			Log.e("AESKeyCache", e.getMessage(), e);
		}

		byte[] bytes = out.toByteArray();

		if (bytes.length >= (Constants.MAX_LEN_KEN_LEN + Constants.LONG_LEN)) {
			int index = 0;
			Config.aesKey = AesUtil.loadKey(BytesUtil.getBytes(bytes, index,
					Constants.MAX_LEN_KEN_LEN));
			index += Constants.MAX_LEN_KEN_LEN;
			Config.sessionId = BytesUtil.getLong(bytes, index);
			index += Constants.LONG_LEN;
		}
	}
}
