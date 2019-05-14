package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.EquipmentInit;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.ShopData;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.BuyEquipmentTip;
import com.vikings.sanguo.ui.alert.BuyTip;
import com.vikings.sanguo.ui.alert.ShopHintTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ShopItemAdapter extends ObjectAdapter implements OnClickListener {

	@Override
	public int getLayoutId() {
		return R.layout.shop_grid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.itemIcon = convertView.findViewById(R.id.itemIcon);
			holder.itemName = convertView.findViewById(R.id.itemName);
			holder.itemPrice = convertView.findViewById(R.id.itemPrice);
			holder.discountBg1 = convertView.findViewById(R.id.discountBg1);
			holder.discountBg2 = convertView.findViewById(R.id.discountBg2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ShopData shopData = (ShopData) getItem(position);

		ViewUtil.setVisible(holder.itemIcon);
		ViewUtil.setVisible(holder.itemName);
		ViewUtil.setVisible(holder.itemPrice);
		ViewUtil.setGone(holder.discountBg1);
		ViewUtil.setGone(holder.discountBg2);
		// TODO 热卖的类型待加

		if (shopData.isTool() && null != shopData.getItem()) {// 道具类商品
			Item item = shopData.getItem();
			ViewUtil.setText(holder.itemName,
					StringUtil.getNCharStr(item.getName(), 4));
			new ViewImgScaleCallBack(item.getImage(), holder.itemIcon,
					Constants.SHOP_ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
					Constants.SHOP_ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

			convertView.setOnClickListener(this);

			int discount = getDiscount(shopData, shopData.getType());
			if (Constants.NO_DISCOUNT != discount) {
				ViewUtil.setVisible(holder.discountBg1, R.id.discountBg1);
				ViewUtil.setVisible(holder.discountBg2, R.id.discountBg2);
				// 打折比值转换成小数点保留一位的折扣
				// String str = 0 == discount % 10 ? String.valueOf(discount /
				// 10)
				// : new DecimalFormat("0.0").format((discount / 10f));
				// ViewUtil.setText(holder.discountBg, R.id.discount, str +
				// "折");

				// 价格
				ViewUtil.setRichText(holder.itemPrice, item.getPreIcon()
						+ String.valueOf(item.getDiscountPrice(discount)));
			} else {
				ViewUtil.setRichText(holder.itemPrice,
						item.getPreIcon() + item.getPrice());
			}

			holder.itemName.setTag(shopData);
		} else if (shopData.isEquipment()
				&& null != shopData.getEquipmentInit()
				&& null != shopData.getEquipment()) {// 装备类商品
			EquipmentInit equipmentInit = shopData.getEquipmentInit();// 装备初始化定义
			PropEquipment equipment = shopData.getEquipment();// 装备定义配置（武器、饰品、战甲、防具）

			ViewUtil.setText(holder.itemName,
					StringUtil.getNCharStr(equipment.getName(), 4));

			new ViewImgScaleCallBack(equipment.getIcon(), holder.itemIcon,
					Constants.SHOP_ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
					Constants.SHOP_ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
			convertView.setOnClickListener(this);

			int discount = getDiscount(shopData, shopData.getType());
			if (Constants.NO_DISCOUNT != discount) {
				ViewUtil.setVisible(holder.discountBg1, R.id.discountBg1);
				ViewUtil.setVisible(holder.discountBg2, R.id.discountBg2);
				// String str = 0 == discount % 10 ? String.valueOf(discount /
				// 10)
				// : new DecimalFormat("0.0").format((discount / 10f));
				// ViewUtil.setText(holder.discountBg, R.id.discount, str +
				// "折");
				ViewUtil.setRichText(
						holder.itemPrice,
						equipmentInit.getPreIcon()
								+ String.valueOf(equipmentInit
										.getDiscountPrice(discount)));
			} else {
				ViewUtil.setRichText(holder.itemPrice,
						equipmentInit.getPreIcon() + equipmentInit.getPrice());
			}

			holder.itemName.setTag(shopData);
		} else {
			ViewUtil.setGone(holder.itemIcon);
			ViewUtil.setGone(holder.itemName);
			ViewUtil.setGone(holder.itemPrice);
			convertView.setOnClickListener(null);
		}
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	@Override
	public void onClick(View v) {
		ShopData shopData = (ShopData) v.findViewById(R.id.itemName).getTag();

		if (null != shopData) {
			if (Account.user.getCurVip().getLevel() < shopData.getBuyVipLv()) {// 购买商品的vip等级条件
				new ShopHintTip(shopData).show();
			} else {
				if (shopData.isTool())
					new BuyTip(shopData).show();// 购买道具商品弹框
				else if (shopData.isEquipment())
					new BuyEquipmentTip(shopData).show();// 购买装备商品弹框

			}
		}
	}

	class ViewHolder {
		View itemIcon, itemName, itemPrice, discountBg1, discountBg2;// ,maxcountBg
																		// 限购
	}

	// 售价折扣百分比
	public int getDiscount(ShopData shopData, int type) {
		if (ShopData.TYPE_TOOL == type) {
			if (null == shopData || null == shopData.getItem())
				return Constants.NO_DISCOUNT;
		} else if (ShopData.TYPE_EQUIPMENT == type) {
			if (null == shopData)
				return Constants.NO_DISCOUNT;
		}

		return shopData.getDiscount();
	}

	public int getIndex(byte type, int id) {
		int index = -1;
		if (null != content && !content.isEmpty()) {
			for (int i = 0; i < content.size(); i++) {
				ShopData data = (ShopData) content.get(i);
				if (data.getType() == type && data.getId() == id) {
					return i;
				}
			}
		}
		return index;
	}
}
