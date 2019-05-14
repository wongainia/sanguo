package com.vikings.sanguo.sound;

import java.util.Stack;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.message.Constants;

public class MediaPlayerMgr {
	private static MediaPlayerMgr mgr;

	private MediaPlayer mediaPlayer;
	private int curResId = 0;

	private int delayTime = 10;

	private Stack<Integer> stack = new Stack<Integer>();

	private float leftVolume = 1f, rightVolume = 1f;
	private long strongerTime = 0, weakerTime = 0;

	public static MediaPlayerMgr getInstance() {
		if (null == mgr) {
			mgr = new MediaPlayerMgr();
		}
		return mgr;
	}

	private MediaPlayerMgr() {
		mediaPlayer = new MediaPlayer();
	}

	public void startSound(int resId) {
		if (resId <= 0)
			return;
		if (!stack.isEmpty()) {
			if (resId == curResId)
				return;
		}
		stack.push(resId);
		strongerTime = Config.serverTime();
		stronger(resId);
	}

	public void stopSound() {
		if (stack.isEmpty())
			return;
		stack.pop();
		int resId = 0;
		if (!stack.isEmpty())
			resId = stack.peek();
		weakerTime = Config.serverTime();
		weaker(resId);
	}

	public void pauseSound() {
		if (null != mediaPlayer && mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}

	public void realseSound() {
		if (null != mediaPlayer) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void restartSound() {
		if (!stack.isEmpty()) {
			int resId = stack.peek();
			play(resId);
		}
	}

	public boolean isPause() {
		if (null != mediaPlayer)
			return !mediaPlayer.isPlaying();
		else
			return true;
	}

	private void stronger(int resId) {
		if (strongerTime == 0) {
			return;
		} else {
			if (weakerTime == 0) {
				strongerChange(resId);
			} else {
				if (strongerTime < weakerTime) {
					strongerChange(resId);
				}
			}
		}
		Config.getController().getHandler()
				.postDelayed(new StartRunnable(resId), delayTime);
	}

	private void strongerChange(int resId) {
		if (curResId == 0) {
			leftVolume = 0.1f;
			rightVolume = 0.1f;
			setVolume();
			play(resId);
		} else {
			if (curResId != resId) {
				if (leftVolume <= 0.1f) {
					pauseSound();
					play(resId);
				} else {
					volumeReduce();
				}

			} else {
				if (leftVolume >= 1) {
					strongerTime = 0;
					return;
				} else {
					volumeAdd();
				}
			}
		}
	}

	private void weaker(int resId) {
		if (weakerTime == 0) {
			return;
		} else {
			if (strongerTime == 0) {
				weakerChange(resId);
			} else {
				if (weakerTime < strongerTime) {
					weakerChange(resId);
				}
			}
		}
		Config.getController().getHandler()
				.postDelayed(new StopRunnable(resId), delayTime);
	}

	private void weakerChange(int resId) {
		if (resId == 0) {
			if (leftVolume <= 0.1f) {
				pauseSound();
				curResId = 0;
				weakerTime = 0;
				return;
			} else {
				volumeReduce();
			}
		} else {
			if (curResId != resId) {
				if (leftVolume <= 0.1f) {
					play(resId);
				} else {
					volumeReduce();
				}
			} else {
				if (leftVolume >= 1f) {
					weakerTime = 0;
					return;
				} else {
					volumeAdd();
				}
			}
		}
	}

	private void play(int resId)// 设置mediaPlayer播放视频
	{
		if (null == mediaPlayer)
			return;
		curResId = resId;
		try {

			mediaPlayer.reset();
			mediaPlayer.setDataSource(Config.getController().getUIContext(),
					getUri(resId));
			mediaPlayer.setLooping(true);
			mediaPlayer.prepare();// 缓冲
			mediaPlayer.setOnPreparedListener(new PrepareListener(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isMusicOpen() {
		return Constants.VALUEOPEN.equals(Setting.music);
	}

	public void musicStateChange() {
		if (null == mediaPlayer)
			return;
		if (isMusicOpen()) {
			if (!stack.isEmpty()) {
				int resId = stack.peek();
				if (!mediaPlayer.isPlaying())
					play(resId);
			}
		} else {
			if (mediaPlayer.isPlaying())
				pauseSound();
		}
	}

	private void volumeReduce() {
		leftVolume -= 0.2f;
		rightVolume -= 0.2f;
		setVolume();
	}

	private void volumeAdd() {
		leftVolume += 0.2f;
		rightVolume += 0.2f;
		setVolume();
	}

	public void setVolume() {
		if (null != mediaPlayer)
			mediaPlayer.setVolume(leftVolume, rightVolume);
	}

	private Uri getUri(int resId) {
		return Uri.parse("android.resource://"
				+ Config.getController().getUIContext().getPackageName() + "/"
				+ resId);
	}

	private class StopRunnable implements Runnable {

		private int resId;

		public StopRunnable(int resId) {
			this.resId = resId;
		}

		@Override
		public void run() {
			weaker(resId);
		}

	}

	private class StartRunnable implements Runnable {

		private int resId;

		public StartRunnable(int resId) {
			this.resId = resId;
		}

		@Override
		public void run() {
			stronger(resId);
		}

	}

	private final class PrepareListener implements OnPreparedListener {
		private int position;

		public PrepareListener(int position) {
			this.position = position;
		}

		@Override
		public void onPrepared(MediaPlayer player) {
			player.seekTo(position);
			if (isMusicOpen())
				player.start();
		}

	}

}
