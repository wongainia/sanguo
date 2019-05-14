package com.vikings.sanguo.ui.window;

import java.io.File;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.network.AlbumConnector;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.PhotoTaker;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class IconPickTip extends CustomConfirmDialog implements
		OnClickListener, CallBack {

	private ImageView icon;
	private TextView desc;
	private Button cameraBtn, photoGalleryBtn, saveBtn, closeBtn;

	private UserAccountClient accountUser;

	private int index = -1;

	private File cusIcon;

	private PhotoTaker photoTaker;

	public IconPickTip() {
		super("修改头像", LARGE);
		icon = (ImageView) content.findViewById(R.id.icon);
		desc = (TextView) content.findViewById(R.id.desc);
		cameraBtn = (Button) content.findViewById(R.id.cameraBtn);
		cameraBtn.setOnClickListener(this);
		photoGalleryBtn = (Button) content.findViewById(R.id.photoGalleryBtn);
		photoGalleryBtn.setOnClickListener(this);
		saveBtn = (Button) content.findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(this);
		ViewUtil.setText(saveBtn, "保存头像");
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);
	}

	/**
	 * user 是 tmp save后才修改account
	 * 
	 * @param user
	 */
	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		this.accountUser = Account.user.emptyUser();
		initPhotoTaker();

		index = -1;
		cusIcon = null;
		new UserIconCallBack(Account.user.bref(), icon);
		LayoutParams params = (LayoutParams) icon.getLayoutParams();
		params.width = LayoutParams.FILL_PARENT;
		params.height = LayoutParams.FILL_PARENT;
		ViewUtil.setText(desc, "当前头像");
	}

	private void initPhotoTaker() {
		photoTaker = controller.getPhotoTaker();
		photoTaker.setCallBack(this);
	}

	@Override
	public void onCall() {
		show();
		cusIcon = controller.getPhotoTaker().getFile();
		if (cusIcon == null && !cusIcon.exists()) {
			controller.alert("获取图片失败");
			cusIcon = null;
			return;
		}
		index = -1;
		icon.setBackgroundDrawable(controller.getDrawable(cusIcon.getName(),
				true));
	}

	private void saveIcon() {
		if (this.index == -1 && this.cusIcon == null) {
			controller.goBack();
			controller.openUserInfoEditWindow();
			return;
		}
		// if (this.index != -1) {
		// user.setImage(index);
		// }
		if (this.cusIcon != null) {
			int time = (int) (Config.serverTime() / 1000);
			File f = new File(cusIcon.getParentFile().getAbsolutePath() + "/"
					+ BytesUtil.getLong(accountUser.getId(), time) + "_h.png");
			this.cusIcon.renameTo(f);
			this.cusIcon = f;
			accountUser.setImage(time);
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
		protected void beforeFire() {
			super.beforeFire();
		}

		@Override
		protected String failMsg() {
			return "保存头像失败";
		}

		@Override
		protected void fire() throws GameException {
//			AlbumInfo albumInfo = AlbumConnector.getAlbumInfo(Config.snsUrl
//					+ "/userAlbum", Account.user.getId(), Account.user.getId());
//			if (albumInfo.getTimestamp().size() >= 20) {
//				throw new GameException("相册已满，不能更改头像，请先删除之前的照片");
//			}

			if (accountUser.isCustomIcon()) {
				AlbumConnector.uploadAlbum(
						Config.snsUrl + "/userAlbum/upload",
						BytesUtil.getLong(accountUser.getId(),
								accountUser.getImage()), cusIcon);
			}
			ri = GameBiz.getInstance().playerUpdate(accountUser);
		}

		@Override
		protected String loadingMsg() {
			return "保存用户资料中..";
		}

		@Override
		protected void onOK() {
			controller.updateUI(ri, true);
			Account.user.setImage(accountUser.getImage());
			dismiss();
			controller.setAccountBarUser(Account.user);

		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_pick_guild_icon, tip, false);
	}
}
