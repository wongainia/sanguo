package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.ItemInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.PropBox;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class StoreItemTip extends CustomConfirmDialog implements
		OnClickListener {
	private static final int layout = R.layout.alert_store_item;

	private Button openBtn, overBtn, sellBtn, closeBtn;
	private ImageView icon;
	private TextView type, count, price, desc;
	private ItemBag itemBag;
	private Item item;
	private boolean isOpenBox = false;
	private int openCount = 1;

	private List<PropBox> pbs = new ArrayList<PropBox>();// 一键开启显示的消耗

	public StoreItemTip(ItemBag itemBag) {
		super(itemBag.getItem().getName());
		this.itemBag = itemBag;
		this.item = itemBag.getItem();
		pbs = CacheMgr.propBoxCache.getAll();
		icon = (ImageView) content.findViewById(R.id.icon);
		type = (TextView) content.findViewById(R.id.type);
		count = (TextView) content.findViewById(R.id.count);
		price = (TextView) content.findViewById(R.id.price);
		desc = (TextView) content.findViewById(R.id.desc);

		openBtn = (Button) content.findViewById(R.id.openBtn);
		overBtn = (Button) content.findViewById(R.id.overBtn);
		sellBtn = (Button) content.findViewById(R.id.sellBtn);
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
	}

	public void show() {
		if (item == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		new ViewImgScaleCallBack(item.getImage(), icon, Config.SCALE_FROM_HIGH
				* Constants.ICON_WIDTH, Config.SCALE_FROM_HIGH
				* Constants.ICON_HEIGHT);
		ViewUtil.setText(type, "类型：" + item.getTypeName());
		ViewUtil.setText(count, "数量：" + itemBag.getCount());
		ViewUtil.setText(overBtn, "一键"
				+ item.getUseBtnStr().replaceAll(" ", ""));
		// 使用
		if (item.canUse() && !StringUtil.isNull(item.getUseBtnStr())) {
			ViewUtil.setVisible(openBtn);
			ViewUtil.setText(openBtn, item.getUseBtnStr());
			openBtn.setOnClickListener(this);
		} else {
			ViewUtil.setGone(openBtn);
		}

		// 一键开启宝箱
		if (itemBag.getItem().canOpenOneKey() && itemBag.getCount() > 1) {
			isOpenBox = true;
			overBtn.setOnClickListener(this);
		} else {
			overBtn.setVisibility(View.GONE);
		}

		// 出售物品
		if (item.getSell() > 0) {
			ViewUtil.setRichText(price, "售价：#money#" + item.getSell(), true);
			sellBtn.setOnClickListener(this);
		} else {

			ViewUtil.setRichText(
					price,
					"售价："
							+ StringUtil.color("不可出售", Config.getController()
									.getResourceColorText(R.color.k7_color8)),
					true);
			sellBtn.setVisibility(View.GONE);
		}
		ViewUtil.setRichText(desc, item.getDesc());
		// 关闭
		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	private class ItemUseInvoker extends ItemInvoker {
		public ItemUseInvoker(ItemBag bag, BriefUserInfoClient user, int count,
				boolean isOpenBox) {
			super(bag, user, count, isOpenBox);
		}

		@Override
		protected void onOK() {
			dismiss();
			super.onOK();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.openBtn:// 使用
			dismiss();
			int costCurrency = itemBag.getItem().getCurrency();
			if (Account.user.getCurrency() < costCurrency) {
				dismiss();
				new ToActionTip(costCurrency).show();
				return;
			}

			if (Account.user.getLevel() < itemBag.getItem().getMinUseLevel()) {
				controller.alert(CacheMgr.errorCodeCache.getMsg((short) 305));
				return;
			}

			// 判断是否是双倍卡
			if (item.getId() == Item.ITME_ID_FOOD_SPEED_UP_CARD
					|| item.getId() == Item.ITME_ID_MONEY_SPEED_UP_CARD) {
				int attrId = CacheMgr.dictCache.getDictInt(Dict.TYPE_ITME_ID,
						item.getId());
				BuildingInfoClient bic = Account.manorInfoClient
						.getSpeedUpBuilding(attrId);
				if (null != bic) {
					int leftTime = bic.getResSpeedupTime();
					if (leftTime > 0) {
						dismiss();
						String effectDesc = "";
						if (item.getId() == Item.ITME_ID_MONEY_SPEED_UP_CARD)
							effectDesc = StringUtil.color("主城目前有",
									R.color.color5)
									+ StringUtil.color("[双倍金币]",
											R.color.k7_color15)
									+ StringUtil.color(
											"效果，再次使用将覆盖原效果并重置持续时间，确定要使用吗？",
											R.color.color5);
						else if (item.getId() == Item.ITME_ID_FOOD_SPEED_UP_CARD)
							effectDesc = StringUtil.color("主城目前有",
									R.color.color5)
									+ StringUtil.color("[双倍粮草]",
											R.color.k7_color15)
									+ StringUtil.color(
											"效果，再次使用将覆盖原效果并重置持续时间，确定要使用吗？",
											R.color.color5);

						new CommonCustomAlert("确定使用",
								CommonCustomAlert.DEFAULT, false, effectDesc,
								"确  定", new CallBack() {

									@Override
									public void onCall() {
										new ItemUseInvoker(itemBag,
												Account.user.bref(), openCount,
												isOpenBox).start();

									}
								}, "", null, "取  消", true).show();
					} else {
						new ItemUseInvoker(itemBag, Account.user.bref(),
								openCount, isOpenBox).start();
					}

				} else {
					new ItemUseInvoker(itemBag, Account.user.bref(), openCount,
							isOpenBox).start();
				}
			} else {
				new ItemUseInvoker(itemBag, Account.user.bref(), openCount,
						isOpenBox).start();
			}

			break;
		case R.id.overBtn:// 一键开启
			dismiss();
			new OpenAllBoxAlert(itemBag).show();
			break;
		case R.id.sellBtn:// 出售物品
			dismiss();
			new SellInputTip(itemBag).show();
			break;
		case R.id.closeBtn:// 关闭
			dismiss();
			break;
		}
	}
}
