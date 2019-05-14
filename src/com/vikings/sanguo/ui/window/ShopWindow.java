package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.ShopData;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.adapter.ShopItemAdapter;
import com.vikings.sanguo.ui.alert.ShopHintTip;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class ShopWindow extends CustomPopupWindow implements OnClickListener {
	static public final int TAB1 = 0;
	static public final int TAB2 = 1;
	static public final int TAB3 = 2;

	private GridView grid;

	private ShopItemAdapter shopItemAdapter;

	private ImageButton[] tabs;// 分类的图片按钮
	private ImageView[] tabWords;// 分类的图片文字

	// 热卖、道具、装备按钮背景图
	private int[] press = { R.drawable.hotsell_word_press,
			R.drawable.tools_word_press, R.drawable.equipments_word_press };// 选中的亮色文字
	private int[] nopress = { R.drawable.hotsell_word_bg,
			R.drawable.tools_word, R.drawable.equipments_word_bg };// 未选中的灰色文字

	// 点击按钮的效果
	private int[] bgIds = { R.drawable.tab_btn1, R.drawable.tab_btn1_press };// 图片依次为未点击效果、点击效果

	// 当前选中tab
	private int index = 0;

	// 热卖、道具、装备
	private List<ShopData> hotShop;// 热卖商品
	private List<ShopData> tools;// 道具商品
	private List<ShopData> equipments;// 装备商品

	@Override
	protected void init() {
		super.init("商店");
		setLeftBtn("充值元宝", new OnClickListener() {// 标题栏左侧按钮

					@Override
					public void onClick(View v) {
						controller.openRechargeCenterWindow();
					}
				});
		setContent(R.layout.shop);

		grid = (GridView) findViewById(R.id.gridView);
		shopItemAdapter = new ShopItemAdapter();
		grid.setPadding(0, 0, 0, 0);
		grid.setAdapter(shopItemAdapter);
		grid.setNumColumns(3);
	}

	// 框体左按钮背景
	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	// 框体右按钮背景
	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	public void open() {
		initList();
		this.index = getInitIndex();
		if (-1 == this.index) {
			controller.getCastleWindow().setRightTopInfoVisible();
			new ShopHintTip("您的VIP等级不足，不能进入商店，请先将VIP等级提升至 "
					+ StringUtil.color(CacheMgr.shop.getEnterMinVipLvl() + "",
							R.color.color11) + " 级").show();
		} else {
			doOpen();
			select(this.index);
		}
	}

	public void open(int index) {
		initList();
		if (CacheMgr.shop.canOpenTab((byte) index)) {
			doOpen();
			select(index);
		} else {
			new ShopHintTip("您的VIP等级不足，不能进入【"
					+ StringUtil.color(CacheMgr.shop.getTabName((byte) index),
							R.color.color11)
					+ "】商店，请先将VIP等级提升至"
					+ StringUtil.color(
							CacheMgr.shop.getMinVipLvByTab((byte) index) + "",
							R.color.color11) + " 级").show();
		}
	}

	public void open(int index, byte type, int id) {
		initList();
		if (CacheMgr.shop.canOpenTab((byte) index)) {
			doOpen();
			select(index, type, id);
		} else {
			new ShopHintTip("您的VIP等级不足，不能进入【"
					+ StringUtil.color(CacheMgr.shop.getTabName((byte) index),
							R.color.color11)
					+ "】商店，请先将VIP等级提升至"
					+ StringUtil.color(
							CacheMgr.shop.getMinVipLvByTab((byte) index) + "",
							R.color.color11) + " 级").show();
		}
	}

	private int getInitIndex() {
		int idx = -1;

		if (!ListUtil.isNull(hotShop))// 热卖
			idx = 0;
		else if (!ListUtil.isNull(tools))// 道具
			idx = 1;
		else if (!ListUtil.isNull(equipments))// 装备
			idx = 2;

		return idx;
	}

	protected void initList() {
		hotShop = CacheMgr.shop.getTabData(ShopData.TAB1);// 热卖
		tools = CacheMgr.shop.getTabData(ShopData.TAB2);// 道具
		equipments = CacheMgr.shop.getTabData(ShopData.TAB3);// 装备
	}

	@Override
	public void showUI() {
		setRightTxt("#rmb#" + Account.user.getCurrency());
		setData();
		super.showUI();
	}

	private void setData() {
		tabs = new ImageButton[3];
		tabWords = new ImageView[3];

		tabs[0] = (ImageButton) content.findViewById(R.id.tab1);
		tabWords[0] = (ImageView) content.findViewById(R.id.tabwords1);
		if (ListUtil.isNull(hotShop)) {// 热卖
			ViewUtil.setGone(tabs[0]);
			ViewUtil.setGone(tabWords[0]);
		} else
			tabs[0].setOnClickListener(this);

		tabs[1] = (ImageButton) content.findViewById(R.id.tab2);
		tabWords[1] = (ImageView) content.findViewById(R.id.tabwords2);
		if (ListUtil.isNull(tools)) {// 道具
			ViewUtil.setGone(tabs[1]);
			ViewUtil.setGone(tabWords[0]);
		} else
			tabs[1].setOnClickListener(this);

		tabs[2] = (ImageButton) content.findViewById(R.id.tab3);
		tabWords[2] = (ImageView) content.findViewById(R.id.tabwords3);
		if (ListUtil.isNull(equipments)) {// 装备
			ViewUtil.setGone(tabs[2]);
			ViewUtil.setGone(tabWords[2]);
		} else
			tabs[2].setOnClickListener(this);

		shopItemAdapter.clear();
		switch (index) {
		case 0:
			shopItemAdapter.addItems(hotShop);// 热卖
			break;
		case 1:
			shopItemAdapter.addItems(tools);// 道具
			break;
		case 2:
			shopItemAdapter.addItems(equipments);// 装备
			break;
		default:
			break;
		}

		shopItemAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		int index = ViewUtil.indexOf(tabs, v);
		if (index != -1) {
			SoundMgr.play(R.raw.sfx_window_open);
			if (this.index != index)
				select(index);
		}
	}

	private void select(int index) {
		selectTab(index);
		showUI();
	}

	private void select(int index, byte type, int id) {
		selectTab(index);
		showUI();
		if (null != shopItemAdapter) {
			int selectIndex = shopItemAdapter.getIndex(type, id);
			if (selectIndex >= 0)
				grid.setSelection(selectIndex);
		}
	}

	private void selectTab(int index) {
		this.index = index;
		// 图片按钮的图片字体效果
		for (int i = 0; i < tabs.length; i++) {
			if (i == index) {
				tabs[i].setBackgroundResource(bgIds[1]);
				tabWords[i].setBackgroundResource(press[i]);
			} else {
				tabs[i].setBackgroundResource(bgIds[0]);
				tabWords[i].setBackgroundResource(nopress[i]);
			}
		}

	}
}
