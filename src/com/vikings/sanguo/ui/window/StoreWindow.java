package com.vikings.sanguo.ui.window;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.EquipmentCache;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.Storehouse;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.adapter.EquipmentItemAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.StoreItemAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class StoreWindow extends CustomPopupWindow implements OnClickListener {

	private View emptyShow;

	private ImageButton[] tabs;// 分类的图片按钮
	private ImageView[] tabWords;// 分类的图片文字

	// 道具 材料 书籍 魂魄
	private int[] press = { R.drawable.robe_woed_press,
			R.drawable.tools_word_press, R.drawable.equipments_word_press,
			R.drawable.stone_word_press };// 选中的亮色文字
	private int[] nopress = { R.drawable.robe_woed_bg, R.drawable.tools_word,
			R.drawable.equipments_word_bg, R.drawable.stone_word_bg };// 未选中的灰色文字

	private int[] bgIds = { R.drawable.tab_btn1, R.drawable.tab_btn1_press };// Tab按钮图片依次为未点击效果、点击效果

	private GridView grid;

	private StoreItemAdapter storeItemAdapter = new StoreItemAdapter();// 礼包、道具、宝石

	private EquipmentItemAdapter equipmentItemAdapter = new EquipmentItemAdapter();// 装备

	// 当前选中tab
	private int index = 0;

	private boolean isGuider = false;

	@Override
	protected void init() {
		super.init("背包");
		setContent(R.layout.store);

		// 顶部图片按钮
		tabs = new ImageButton[4];
		tabWords = new ImageView[4];
		tabs[0] = (ImageButton) content.findViewById(R.id.tab1);// 礼包
		tabs[0].setOnClickListener(this);
		tabWords[0] = (ImageView) content.findViewById(R.id.tabwords1);

		tabs[1] = (ImageButton) content.findViewById(R.id.tab2);// 道具
		tabs[1].setOnClickListener(this);
		tabWords[1] = (ImageView) content.findViewById(R.id.tabwords2);

		tabs[2] = (ImageButton) content.findViewById(R.id.tab3);// 装备
		tabs[2].setOnClickListener(this);
		tabWords[2] = (ImageView) content.findViewById(R.id.tabwords3);

		tabs[3] = (ImageButton) content.findViewById(R.id.tab4);// 宝石
		tabs[3].setOnClickListener(this);
		tabWords[3] = (ImageView) content.findViewById(R.id.tabwords4);

		grid = (GridView) content.findViewById(R.id.gridView);

		ViewGroup storeLayout = (ViewGroup) window
				.findViewById(R.id.storeLayout);
		emptyShow = controller.inflate(R.layout.empty_show, storeLayout, false);
		storeLayout.addView(emptyShow);

		TextView emptyText = (TextView) emptyShow.findViewById(R.id.emptyDesc);
		emptyText.setTextSize(18);
	}

	@Override
	protected void destory() {
		controller.removeContent(window);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void showUI() {
		super.showUI();

		if (index < 0 || index > 3)
			return;

		if (index == 2) {// 技能按钮对应视图
			equipmentItemAdapter.getContent().clear();
			List<EquipmentInfoClient> lt = Account.equipmentCache.getAll();
			Collections.sort(lt, new Comparator<EquipmentInfoClient>() {
				@Override
				public int compare(EquipmentInfoClient equip1,
						EquipmentInfoClient equip2) {
					if (equip1.getQuality() == equip2.getQuality()) {
						return equip2.getLevel() - equip1.getLevel();
					} else {
						return equip2.getQuality() - equip1.getQuality();
					}
				}
			});
			equipmentItemAdapter.getContent().addAll(lt);
			equipmentItemAdapter.fillEmpty();
			equipmentItemAdapter.notifyDataSetChanged();
			dealwithEmptyAdpter(equipmentItemAdapter, index);
			setRightTxt(lt.size() + "/" + EquipmentCache.getCapability());
		} else {
			// 右上角显示物品类型数量占仓库的比值
			setRightTxt(Account.store.getSize() + "/"
					+ Storehouse.getCapability());
			storeItemAdapter.getContent().clear();
			if (index == 0) {
				storeItemAdapter.getContent().addAll(Account.store.getGift());
			} else if (index == 1) {
				storeItemAdapter.getContent().addAll(Account.store.getTools());
			} else if (index == 3) {
				storeItemAdapter.getContent().addAll(Account.store.getStone());
			}
			storeItemAdapter.fillEmpty();
			storeItemAdapter.notifyDataSetChanged();
			dealwithEmptyAdpter(storeItemAdapter, index);
		}

	}

	// 框体右按钮背景
	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	// 传入顶部控件对应的序列值
	private void select(int index) {
		selectTab(index);
		if (index == 2) {
			grid.setAdapter(equipmentItemAdapter);
		} else {
			grid.setAdapter(storeItemAdapter);
		}
		grid.setNumColumns(3);// 数值表示设置一行显示多少列
		showUI();
	}

	// 顶部按钮图片文字偏移
	private void selectTab(int index) {
		this.index = index;
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

	// 传入数值展现对应按钮事件视图内容
	public void open(int tabIndex) {
		this.index = tabIndex % 4;
		doOpen();
		select(this.index);
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	public void onClick(View v) {
		int index = ViewUtil.indexOf(tabs, v);// 遍历控件，对应则返回该控件的数组位置
		if (index != -1) {
			SoundMgr.play(R.raw.sfx_window_open);
			if (this.index != index)
				select(index);
		}
	}

	// 无物品展示时的显示效果
	protected void dealwithEmptyAdpter(ObjectAdapter adapter, int index) {
		if (adapter.isEmpty()) {// 内容展示列表为空
			ViewUtil.setGone(grid);
			ViewUtil.setVisible(emptyShow);
			ViewUtil.setRichText(emptyShow, R.id.emptyDesc,
					getEmptyShowText(index));
		} else {
			ViewUtil.setVisible(grid);
			ViewUtil.setGone(emptyShow);
		}
	}

	// 无展示列表显示
	private String getEmptyShowText(int index) {
		String bgText = "";
		if (index == 0) {
			bgText = "背包中尚无任何礼包";
		} else if (index == 1) {
			bgText = "背包中尚无任何道具";
		} else if (index == 2) {
			bgText = "背包中尚无任何装备";
		} else if (index == 3) {
			bgText = "背包中尚无任何宝石";
		}

		return bgText;
	}

	public boolean isGuider() {
		return isGuider;
	}

	public void setGuider(boolean isGuider) {
		this.isGuider = isGuider;
	}

	public GridView getGridView() {
		return grid;
	}

}
