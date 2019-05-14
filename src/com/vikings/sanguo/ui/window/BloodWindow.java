package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class BloodWindow extends CustomPopupWindow implements OnClickListener {
	private TextView bestRecord, lastRecord;
	private Button continueBtn, bloodBtn, honorBtn;

	@Override
	protected void init() {
		super.init("血战到底");
		setContentAboveTitle(R.layout.top_desc_txt);
		setContent(R.layout.blood);
		setCommonBg(R.drawable.blood_war_bg);
		bestRecord = (TextView) window.findViewById(R.id.bestRecord);
		lastRecord = (TextView) window.findViewById(R.id.lastRecord);
		continueBtn = (Button) window.findViewById(R.id.recordBtn);
		continueBtn.setOnClickListener(this);
		bloodBtn = (Button) window.findViewById(R.id.bloodBtn);
		bloodBtn.setOnClickListener(this);
		honorBtn = (Button) window.findViewById(R.id.honorBtn);
		honorBtn.setOnClickListener(this);
		ViewUtil.setRichText(window.findViewById(R.id.topDesc),
				CacheMgr.uiTextCache.getTxt(UITextProp.BLOOD_RULER));
	}

	public void open() {
		this.doOpen();
	}

	@Override
	public void showUI() {
		super.showUI();
		setValue();
	}

	private void setValue() {
		setLeftTxt("次数:" + Account.myLordInfo.getBloodCount() + "/"
				+ Account.myLordInfo.getMaxBloodCount());
		int itemId = CacheMgr.bloodCommonCache.getCostId();
		Item item = CacheMgr.getItemByID(itemId);
		if (null != item) {
			ItemBag itemBag = Account.store.getItemBag(itemId);
			if (null != itemBag && itemBag.getCount() > 0) {
				setRightTxt("#blood_icon#" + itemBag.getCount(), 25, 25);
			} else {
				setRightTxt("#blood_icon#0", 25, 25);
			}
		}
		if (Account.myLordInfo.getTodayBestRecord() > 0)
			ViewUtil.setText(bestRecord,
					"今日你的最佳战绩为:" + Account.myLordInfo.getTodayBestRecord());
		else
			ViewUtil.setText(bestRecord, "今日你尚未取得任何战绩");

		int costCount = Account.myLordInfo.getBloodCostCount();
		String costStr = "";
		if (costCount > 0)
			costStr = (null != item && costCount > 0 ? "#blood_icon# x"
					+ costCount : "");
		if (StringUtil.isNull(costStr))
			costStr = "(本次免费)";

		if (Account.myLordInfo.getLastRecord() > 0) {
			ViewUtil.setVisible(lastRecord);
			ViewUtil.setText(lastRecord,
					"上次成功挑战到 " + Account.myLordInfo.getLastRecord() + " 关");
			if (Account.myLordInfo.getContinueRecord() > 1) {
				ViewUtil.setVisible(continueBtn);

				ViewUtil.setRichText(continueBtn,
						"从" + Account.myLordInfo.getContinueRecord() + "关挑战"
								+ costStr, true);

			} else {
				ViewUtil.setHide(continueBtn);
			}
		} else {
			ViewUtil.setHide(lastRecord);
			ViewUtil.setHide(continueBtn);
		}
		ViewUtil.setRichText(bloodBtn, "开起血战" + costStr, true);
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	public void onClick(View v) {
		if (v == continueBtn) {
			new BloodTroopSetWindow().open(Account.myLordInfo
					.getContinueRecord());
		} else if (v == bloodBtn) {
			new BloodTroopSetWindow().open(1);
		} else if (v == honorBtn) {
			new BloodRankWindow().open();
		}
	}

}
