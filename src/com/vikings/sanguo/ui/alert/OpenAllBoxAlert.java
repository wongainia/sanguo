/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-4-19 下午5:15:10
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.invoker.ItemInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.PropBox;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class OpenAllBoxAlert extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_openallbox;
	private TextView deplete, desc;
	private Button okBtn, closeBtn;

	private ItemBag itemBag;
	private Item item;

	private List<PropBox> pbs = new ArrayList<PropBox>();// 一键开启显示的消耗

	public OpenAllBoxAlert(ItemBag itemBag) {
		super("一键" + itemBag.getItem().getUseBtnStr());
		this.itemBag = itemBag;
		this.item = itemBag.getItem();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

	public void show() {
		super.show();
		deplete = (TextView) content.findViewById(R.id.deplete);
		desc = (TextView) content.findViewById(R.id.desc);
		okBtn = (Button) content.findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ItemUseInvoker(itemBag, Account.user.bref(), itemBag
						.getCount(), true).start();
			}
		});
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		setValue();
	}

	private void setValue() {
		String str = getBox(item);
		if (null != str || str.equals("")) {
			ViewUtil.setGone(deplete);
		} else {
			ViewUtil.setRichText(deplete, getBox(item));
		}

		ViewUtil.setRichText(
				desc,
				"确认要"
						+ itemBag.getItem().getUseBtnStr()
						+ StringUtil.color("" + itemBag.getCount(),
								R.color.color19)
						+ "个"
						+ StringUtil.color(itemBag.getItem().getName(),
								R.color.color19) + "吗？");
	}

	// 一键开启箱子相关的需要
	private String getBox(Item item) {// 箱子id
		PropBox propBox = new PropBox();
		for (PropBox pb : pbs) {
			if (item.getId() == pb.getId()) {
				propBox = pb;
				break;
			}
		}
		String needDesc = "";
		if (propBox.getNeedCurrency() > 0 || propBox.getNeedMoney() > 0
				|| propBox.getNeedItemId() > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append("即将消耗");
			if (propBox.getNeedCurrency() > 0) {
				buf.append("#rmb#×").append(
						StringUtil.color(
								(String.valueOf(propBox.getNeedCurrency()
										* itemBag.getCount())), "red"));// 需要的元宝
			}
			if (propBox.getNeedMoney() > 0) {
				buf.append("#money#×").append(
						StringUtil.color(
								(String.valueOf(propBox.getNeedMoney()
										* itemBag.getCount())), "red"));// 需要的金币
			}
			if (propBox.getNeedItemId() > 0) {
				List<ItemBag> ibs = Account.store.get(1);
				Item it = new Item();
				for (ItemBag ib : ibs) {
					if (propBox.getNeedItemId() == ib.getItem().getId()) {
						it = ib.getItem();
						break;
					}
				}
				buf.append(StringUtil.color(String.valueOf(it.getName() + "×"),
						"red"));// 需要的物品
				buf.append(StringUtil.color(
						String.valueOf(propBox.getNeedItemCount()
								* itemBag.getCount()), "red"));// 需要的物品数量propBox.getNeedItemCount()*
			}

			needDesc = buf.toString();
		}
		return needDesc;
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

}
