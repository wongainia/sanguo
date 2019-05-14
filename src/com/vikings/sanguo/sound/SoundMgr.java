package com.vikings.sanguo.sound;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.message.Constants;

public class SoundMgr {

	private static SoundPool soundPool;
	private static HashMap<Integer, Integer> soundPoolMap;
	private static HashMap<String, Integer> soundPoolMap1;

	private static HashMap<Integer, Integer> streamIdMap = new HashMap<Integer, Integer>();

	static public void initSounds() {
		// 这里指定声音池的最大音频流数目为4，音效质量100
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap1 = new HashMap<String, Integer>();
	}

	private static void playSound(int sound, boolean loop) {
		AudioManager mgr = (AudioManager) Config.getController()
				.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 设置最大音量
		float volume = streamVolumeCurrent / streamVolumeMax; // 设备的音量
		// 播放音频，可以对左右音量分别设置，还可以设置优先级，循环次数以及速率
		// 速率最低0.5最高为2，1代表正常速度
		// soundPool.play(soundID, leftVolume, rightVolume, priority, loop,
		// rate)
		int streamId = soundPool.play(sound, volume, volume, 1, loop ? -1 : 0,
				1f);// 播放
		streamIdMap.put(sound, streamId);
	}

	public static void pause(int id) {
		if (streamIdMap.containsKey(id))
			soundPool.stop(streamIdMap.get(id));
	}

	public static void play(String resName) {
		int resId = Config.getController().findResId(resName, "raw");
		if (resId > 0)
			play(resId);
		else
			ply(resName);

	}

	public static void ply(String resName) {
		if (Constants.VALUEOPEN.equals(Setting.sound) && checkSound(resName)) {
			playSound(soundPoolMap1.get(resName), false);
		}
	}

	public static void play(int resId) {
		if (Constants.VALUEOPEN.equals(Setting.sound) && checkSound(resId))
			playSound(soundPoolMap.get(resId), false);
	}

	public static void playLoop(int resId) {
		if (Constants.VALUEOPEN.equals(Setting.sound) && checkSound(resId))
			playSound(soundPoolMap.get(resId), true);
	}

	public static boolean loadSound(String resName) {
		int resId = Config.getController().findResId(resName, "raw");
		if (resId > 0)
			return checkSound(resId);
		else
			return checkSound(resName);
	}

	// 检查声音文件是否已经加载，如果未加载则加载
	public static boolean checkSound(int resId) {
		if (!soundPoolMap.containsKey(resId)) {
			int id = soundPool.load(Config.getController().getUIContext(),
					resId, 1);
			if (id != 0) {
				soundPoolMap.put(resId, id);
			}
		}
		return soundPoolMap.containsKey(resId);
	}

	private static boolean checkSound(String resName) {
		if (!soundPoolMap1.containsKey(resName)) {
			try {
				File file = Config.getController().getFileAccess()
						.getFile(resName, FileAccess.SOUND_PATH);
				if (null != file) {
					int id = soundPool.load(file.getAbsolutePath(), 1);
					if (id != 0)
						soundPoolMap1.put(resName, id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return soundPoolMap1.containsKey(resName);
	}
}
