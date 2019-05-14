package com.vikings.sanguo.ui.alert;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.JumpTargetPanel;
import com.vikings.sanguo.model.LevelUpDesc;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class UserLevelUpTip extends ResultAnimTip {

	private LevelUpDesc levelUpDesc;
	private ReturnInfoClient ric;

	public UserLevelUpTip(ReturnInfoClient ric) {
		super(isTouchClose());
		this.ric = ric;
	}

	private static boolean isTouchClose() {
		LevelUpDesc levelUpDesc = (LevelUpDesc) CacheMgr.levelUpDescCache
				.search(Account.user.getLevel(), 1);

		if (null != levelUpDesc) {
			if (levelUpDesc.getUiCtr() == 1 || levelUpDesc.getUiCtr() == 3)
				return true;
		}
		return false;
	}

	public void show() {
		SoundMgr.play(R.raw.sfx_leveup);
		levelUpDesc = (LevelUpDesc) CacheMgr.levelUpDescCache.search(
				Account.user.getLevel(), 1);

		if (null != levelUpDesc) {
		}
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (null != levelUpDesc && levelUpDesc.getUiCtr() == 1) {

					if (levelUpDesc.getToWindow() == 1) {
						controller.openSite(levelUpDesc.getUrl());
					} else {
						JumpTargetPanel.doJump(levelUpDesc.getToWindow());
					}

				}
				StepMgr.checkStep301();
				StepMgr.checkStep501();

			}
		});

		super.show(null, false);
	}

	@Override
	protected int getDrawable() {
		return R.drawable.txt_gxsj;
	}

	@Override
	protected View getContent() {
		View view = controller.inflate(R.layout.result_levelup, rewardLayout,
				false);

		ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.items);
		Button goBtn = (Button) view.findViewById(R.id.goBtn);
		Button closeBtn = (Button) view.findViewById(R.id.closeBtn);
		if (null != levelUpDesc) {

			ViewUtil.setVisible(content, R.id.title);
			ViewUtil.setRichText(content, R.id.title, "解锁以下功能");

			View funcView = controller.inflate(R.layout.user_level_up_func,
					viewGroup, false);
			new ViewImgScaleCallBack(levelUpDesc.getImage(),
					funcView.findViewById(R.id.icon),
					70 * Config.SCALE_FROM_HIGH, 70 * Config.SCALE_FROM_HIGH);
			ViewUtil.setText(funcView, R.id.funName, levelUpDesc.getTypeName());
			ViewUtil.setRichText(funcView, R.id.desc, levelUpDesc.getDesc(),
					true);
			viewGroup.addView(funcView);

			if (levelUpDesc.getUiCtr() == 2) {
				ViewUtil.setVisible(goBtn);
				goBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
						if (levelUpDesc.getToWindow() == 1) {
							controller.openSite(levelUpDesc.getUrl());
						} else if (levelUpDesc.getBuildingType() > 0) {
							controller.closeAllPopup();
							controller.getCastleWindow().moveToBuilding(
									levelUpDesc.getBuildingType());
						} else {
							JumpTargetPanel.doJump(levelUpDesc.getToWindow());
						}
					}
				});

				ViewUtil.setVisible(closeBtn);
				closeBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
			} else {
				ViewUtil.setGone(goBtn);
				ViewUtil.setGone(closeBtn);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
				ViewUtil.setVisible(view, R.id.closeDesc);
			}
		} else {
			ViewUtil.setGone(goBtn);
			ViewUtil.setGone(closeBtn);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
			ViewUtil.setVisible(view, R.id.closeDesc);
			ViewUtil.setGone(content, R.id.title);
		}

		ViewUtil.setImage(view, R.id.levelIcon1, R.drawable.txt_gxnsd);
		ViewUtil.setRichText(view, R.id.level,
				StringUtil.numImgStr("v", Account.user.getLevel()));
		ViewUtil.setImage(view, R.id.levelIcon2, R.drawable.txt_ji);

		return view;
	}

}
