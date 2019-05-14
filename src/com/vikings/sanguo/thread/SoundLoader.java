package com.vikings.sanguo.thread;

import java.io.File;
import java.io.IOException;

import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.network.HttpConnector;

public class SoundLoader {

	private static final SoundLoader instance = new SoundLoader();

	private SoundLoader() {

	}

	public static SoundLoader getInstance() {
		if (instance == null)
			return new SoundLoader();
		else
			return instance;
	}

	FileAccess fa = Config.getController().getFileAccess();

	public boolean downloadInCase(String name) {
		// 先在打包资源里面找
		int resId = findInRaw(name);
		// 没有找到
		if (resId > 0) {
			return true;
		}
		File file = fa.getFile(name, FileAccess.SOUND_PATH);
		if (null != file)
			return true;
		return downloadSound(name, Config.soundUrl);
	}

	public int findInRaw(String name) {
		int resId = Config.getController().findResId(name, "raw");
		return resId;
	}

	private boolean downloadSound(String soundName, String url) {
		for (int i = 0; i < 3; i++) {
			try {
				byte[] buf = HttpConnector.getInstance().httpGetBytes(
						url + soundName);
				if (buf != null && buf.length != 0) {
					fa.saveSound(buf, soundName);
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
