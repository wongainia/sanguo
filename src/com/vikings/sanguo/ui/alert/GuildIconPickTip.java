package com.vikings.sanguo.ui.alert;

import java.io.File;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.GuildInfoUpdateResp;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.network.AlbumConnector;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.GuildIconCallBack;
import com.vikings.sanguo.ui.PhotoTaker;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class GuildIconPickTip extends CustomConfirmDialog implements
		OnClickListener, CallBack {

	private Button cameraBtn, photoGalleryBtn, saveBtn, closeBtn;
	private ImageView icon;

	private File cusIcon;
	private PhotoTaker photoTaker;

	private RichGuildInfoClient rgic;
	private int oldIcon;

	public GuildIconPickTip(RichGuildInfoClient rgic) {
		super("家族徽章", LARGE);
		this.rgic = rgic;
		oldIcon = rgic.getGic().getImage();
		cameraBtn = (Button) content.findViewById(R.id.cameraBtn);
		cameraBtn.setOnClickListener(this);
		photoGalleryBtn = (Button) content.findViewById(R.id.photoGalleryBtn);
		photoGalleryBtn.setOnClickListener(this);
		saveBtn = (Button) content.findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(this);
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);
		icon = (ImageView) content.findViewById(R.id.icon);
	}

	public void show() {
		new GuildIconCallBack(rgic.getGic(), icon);
		initPhotoTaker();
		cusIcon = null;
		super.show();
	}

	private void initPhotoTaker() {
		photoTaker = controller.getPhotoTaker();
		photoTaker.setCallBack(this);
	}

	@Override
	public void onCall() {
		cusIcon = controller.getPhotoTaker().getFile();
		if (cusIcon == null && !cusIcon.exists()) {
			controller.alert("获取图片失败");
			cusIcon = null;
			return;
		}
		icon.setBackgroundDrawable(controller.getDrawable(cusIcon.getName(),
				true));
	}

	private void saveIcon() {
		if (cusIcon == null) {
			dismiss();
			return;
		}

		new SaveInvoker().start();

	}

	@Override
	public void onClick(View view) {
		if (view == saveBtn) {
			saveIcon();
		} else if (view == cameraBtn) {
			photoTaker.camera();
		} else if (view == photoGalleryBtn) {
			photoTaker.pickFromGallery();
		}
	}

	private class SaveInvoker extends BaseInvoker {

		private ReturnInfoClient ri;

		@Override
		protected String failMsg() {
			return "保存徽章失败";
		}

		@Override
		protected void fire() throws GameException {
			int time = (int) (Config.serverTime() / 1000);
			File f = new File(cusIcon.getParentFile().getAbsolutePath() + "/"
					+ BytesUtil.getLong(rgic.getGuildid(), time) + "_h.png");
			cusIcon.renameTo(f);
			cusIcon = f;
			rgic.getGic().setImage(time);

			AlbumConnector.uploadAlbum(Config.snsUrl
					+ "/userAlbum/guild/upload", BytesUtil.getLong(
					rgic.getGuildid(), rgic.getGic().getImage()), cusIcon);
			GuildInfoUpdateResp resp = GameBiz.getInstance().guildInfoUpdate(
					rgic.getGuildid(), rgic.getGic().getDesc(),
					rgic.getGic().getImage(), rgic.getGic().getAnnouncement(),
					rgic.getGic().isAutoJoin());
			ri = resp.getRi();
			rgic.setGic(resp.getGic());

		}

		@Override
		protected void onFail(GameException exception) {
			// 失败时要rollback
			rgic.getGic().setImage(oldIcon);
			super.onFail(exception);
		}

		@Override
		protected String loadingMsg() {
			return "保存家族徽章";
		}

		@Override
		protected void onOK() {
			controller.updateUI(ri, true);
			dismiss();
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_pick_guild_icon);
	}

}
