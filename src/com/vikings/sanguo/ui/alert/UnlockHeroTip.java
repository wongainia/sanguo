package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.utils.VipUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class UnlockHeroTip extends CustomConfirmDialog {
	public static final byte COUNT_ONE = 1;
	public static final byte COUNT_TWO = 2;

	private int count;
	private TextView vipOpenDesc, desc;

	/**
	 * @param count
	 *            第count位副将
	 */
	public UnlockHeroTip(int count) {
		super("解锁副将", DEFAULT);
		this.count = count;
		vipOpenDesc = (TextView) content.findViewById(R.id.vipOpenDesc);
		desc = (TextView) content.findViewById(R.id.desc);
		setButton(FIRST_BTN, "开通VIP", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				controller.openVipListWindow();
			}
		});

		setButton(SECOND_BTN, "取消", closeL);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		if (count == 1) {
			ViewUtil.setRichText(vipOpenDesc,
					CacheMgr.uiTextCache.getTxt(UITextProp.UNLOCK_HERO1));

			ViewUtil.setRichText(
					desc,
					"VIP"
							+ VipUtil.openSecondHero()
							+ "或玩家等级达到"
							+ CacheMgr.dictCache.getDictInt(
									Dict.TYPE_UNLOCK_HERO, 3) + "级可解锁。");
		} else if (count == 2) {
			ViewUtil.setRichText(vipOpenDesc,
					CacheMgr.uiTextCache.getTxt(UITextProp.UNLOCK_HERO2));

			ViewUtil.setRichText(
					desc,
					"VIP"
							+ VipUtil.openThirdHero()
							+ "或玩家等级达到"
							+ CacheMgr.dictCache.getDictInt(
									Dict.TYPE_UNLOCK_HERO, 4) + "级可解锁。");
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_unlock_hero, contentLayout,
				false);
	}

}
