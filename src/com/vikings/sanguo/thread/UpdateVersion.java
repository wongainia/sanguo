package com.vikings.sanguo.thread;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.ui.alert.UpdateSucTip;
import com.vikings.sanguo.ui.alert.UpdateTips;

public class UpdateVersion {

	private DownloadListener dl;

	private File tmp;

	private String url = Config.downloadUrl;

	private static final String website = Config.getController().getResources()
			.getString(R.string.website);

	private static final String path = "vk_3g_tmp.apk";

	public UpdateVersion(DownloadListener dl) {
		this.dl = dl;
		FileAccess fa = Config.getController().getFileAccess();
		tmp = fa.getSdCardFile(path);
	}

	public void start() {
		if (tmp == null) {
			new UpdateTips(Config.getController().getResources()
					.getString(R.string.sdcard_error_msg)
					.replace("[website]", "" + website + "")).show();
			dl.cancle();
			return;
		}
		new Thread(new Download()).start();
	}

	public static void install() {
		Config.getController().postRunnable(new Install());
	}

	private static class Install implements Runnable {

		@Override
		public void run() {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.fromFile(Config.getController().getFileAccess()
							.getSdCardFile(path)),
					"application/vnd.android.package-archive");
			Config.getController().getMainActivity().startActivity(intent);
		}

	}

	private class Download implements Runnable {

		int total = 0;

		int cur = 0;

		RandomAccessFile out;

		Download() {
			cur = PrefAccess.getPercent();
		}

		public void notice() {
			Config.getController().postRunnable(new Runnable() {
				@Override
				public void run() {
					dl.setDownloadPercent(cur * 100 / total);
				}
			});
		}

		private int getSize() throws IOException {
			HttpURLConnection connn = (HttpURLConnection) new URL(url)
					.openConnection();
			int tempSize = connn.getContentLength();
			connn.disconnect();
			return tempSize;
		}

		private int getDv() {
			int dv = 0;
			String dvUrl = Config.dvURL;
			try {
				dv = HttpConnector.getInstance().getInteger(dvUrl);
			} catch (IOException e) {

			}
			return dv;
		}

		@Override
		public void run() {
			url = url + "?v=" + getDv();
			int retryCount = 1;
			while (retryCount < 6) {
				try {
					total = getSize();
					if (total <= 0)
						throw new IOException("content length error");
					HttpURLConnection conn = (HttpURLConnection) new URL(url)
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("RANGE", "bytes=" + cur + "-");

					InputStream in = conn.getInputStream();
					out = new RandomAccessFile(tmp, "rw");
					out.seek(cur);
					byte[] buf = new byte[4096];
					int count;
					int time = 0;

					while ((count = in.read(buf)) != -1) {
						// 4kb *25 每100kb通知刷新进度条
						out.write(buf, 0, count);
						cur += count;
						time++;
						if (time % 25 == 0) {
							PrefAccess.savePercent(cur);
							notice();
						}
					}
					out.setLength(total);
					out.close();
					cur = total;
					conn.disconnect();
					notice();
					PrefAccess.savePercent(0);
					Config.getController().postRunnable(new Runnable() {
						@Override
						public void run() {
							new UpdateSucTip(dl).show();
						}
					});
					break;
				} catch (IOException e) {
					Log.e("UpdateVersion", e.getMessage(), e);
					// 先保存文件
					try {
						if (out != null)
							out.close();
					} catch (IOException e1) {
					}
					if (retryCount == 5) {
						Config.getController().postRunnable(new Runnable() {
							@Override
							public void run() {
								new UpdateTips(Config.getController()
										.getResources()
										.getString(R.string.update_error_msg)
										+ website).show();
								// home.showMenu();
							}
						});
						break;
					}
					try {
						Thread.sleep(retryCount * 1000);
					} catch (InterruptedException ie) {
					}
					retryCount++;
				}
			}

		}

	}

	public interface DownloadListener {

		public void setDownloadPercent(int p);

		public void cancle();

	}

}
