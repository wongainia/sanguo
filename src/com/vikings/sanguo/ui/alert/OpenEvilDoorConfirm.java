package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.OpenEvilDoorInvoker;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.FiefScale;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class OpenEvilDoorConfirm extends CustomConfirmDialog {

	private BriefFiefInfoClient bfic;
	private TextView desc, descExt;
	private int itemId, itemCount;

	public OpenEvilDoorConfirm(BriefFiefInfoClient briefFiefInfoClient) {
		super("开启恶魔之门", DEFAULT);
		this.bfic = briefFiefInfoClient;
		this.desc = (TextView) content.findViewById(R.id.desc);
		this.descExt = (TextView) content.findViewById(R.id.descExt);
		setButton(FIRST_BTN, "确认", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (itemId > 0 && itemCount > 0) {
					Item item = CacheMgr.getItemByID(itemId);
					ItemBag itemBag = Account.store.getItemBag(itemId);
					if (null == itemBag || itemBag.getCount() < itemCount) {
						controller.alert(item.getName() + "数量不足");
						return;
					}
				}
				new OpenEvilDoorInvoker(bfic, (itemId > 0 ? itemCount : 0))
						.start();			
			}
		});
		setButton(SECOND_BTN, "取消", closeL);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_open_evil_door_confirm, tip,
				false);
	}

	@Override
	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		int count = 0;
		Item item = null;
		ItemBag itemBag = null;
		FiefScale fiefScale = bfic.getFiefScale();
		if (null != fiefScale && fiefScale instanceof HolyProp) {
			HolyProp prop = (HolyProp) fiefScale;
			count = prop.getMaxReinforceUser();
			itemId = prop.getItemId();
			itemCount = prop.getItemCost();
			if (itemId > 0 && itemCount > 0) {
				item = CacheMgr.getItemByID(itemId);
				itemBag = Account.store.getItemBag(itemId);
			}
		}
		ViewUtil.setRichText(
				desc,
				"开启恶魔之门后，将有强大的怪物来袭，击败他们能获得丰厚的战利品！<br/><br/>本场战斗为"
						+ StringUtil.color(
								"挑战" + bfic.getSimpleName() + "恶魔之门",
								R.color.k7_color8)
						+ (count > 0 ? ",最多允许"
								+ StringUtil.color("" + count,
										R.color.k7_color8) + "名玩家增援" : ""));
		if (null != item) {
			int hasCount = (itemBag == null ? 0 : itemBag.getCount());
			ViewUtil.setRichText(
					descExt,
					"需要消耗"
							+ item.getName()
							+ "x"
							+ itemCount
							+ (hasCount >= itemCount ? "(" + hasCount + ")"
									: StringUtil.color("(" + hasCount + ")",
											R.color.k7_color8)));
		}
	}

}
