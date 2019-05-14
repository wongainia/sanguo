/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-24
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui.wheel;

import java.util.List;

import com.vikings.sanguo.model.GambleData;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.adapter.CurrencySlotAdapter;
import com.vikings.sanguo.ui.adapter.GambleSlotAdapter;
import com.vikings.sanguo.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

//原理：1.由WheelScroller驱动，WheelScroller传入每帧的移动距离delta
//     2.根据delta计算移动了多少item，使用rebuildItems()更新itemLayout中的元素
//     3.根据delta计算itemLayout在y方向的偏移，重绘
public class WheelView extends View {
	private int currentItem = 0;
	private int itemHeight = 0;
	private WheelScroller scroller;
	private boolean isScrollingPerformed; // WheelView是否开始转动
	private int scrollingOffset;
	private boolean isCyclic = false; // 是否循环
	private LinearLayout itemsLayout; // 存放滚动内容的布局
	private int firstItem;
	private WheelViewAdapter viewAdapter;
	private WheelRecycle recycle = new WheelRecycle(this);
	private int stopItem = WHEELING;
	private int lastCounter = 0; // 最后转动圈数计数器
//	 private boolean canStop; // 是否可以停下
	// private WheelView next; // 下一个Wheel，第一个wheel停止后下一个才能停止
	private boolean forceStop = false; // 是否转动

	static final private int DEF_VISIBLE_ITEMS = 1; // 默认可见面数
	static final public int WHEELING = -1; // 正在转动
	public int DEFAULT_STOP = 0; // 默认的起始和终止面都是第一面
	static final public int MIN_WHEEL_DURATION = 2000; // 最小转动时间,单位ms
	static final public int MAX_WHEEL_DURATION = 20000; // 最大转动时间,单位ms 20000
	static final public int LAST_WHEEL_CNT = 2; // 最后转动圈数

	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	public WheelView(Context context) {
		super(context);
		initData(context);
	}

	private void initData(Context context) {
		scroller = new WheelScroller(scrollingListener);
		// setInterpolator(new DecelerateInterpolator(0.2f));
	}

	WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
		public void onStarted() {
			isScrollingPerformed = true;
		}

		public void onScroll(int distance) {
			doScroll(distance);

			int height = getHeight();
			if (scrollingOffset > height) {
				scrollingOffset = height;
				scroller.stopScrolling();
			} else if (scrollingOffset < -height) {
				scrollingOffset = -height;
				scroller.stopScrolling();
			}
		}

		public void onFinished() {
			if (isScrollingPerformed) {
				isScrollingPerformed = false;
			}

			scrollingOffset = 0;
			invalidate();
		}

		public void onJustify() {
			if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
				scroller.scroll(scrollingOffset, 0);
			}
		}
	};

	public void setInterpolator(Interpolator interpolator) {
		scroller.setInterpolator(interpolator);
	}

	public WheelViewAdapter getViewAdapter() {
		return viewAdapter;
	}

	// Adapter listener
	private DataSetObserver dataObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			invalidateWheel(false);
		}

		@Override
		public void onInvalidated() {
			invalidateWheel(true);
		}
	};

	/**
	 * Sets view adapter. Usually new adapters contain different views, so it
	 * needs to rebuild view by calling measure().
	 * 
	 * @param viewAdapter
	 *            the view adapter
	 */
	public void setViewAdapter(WheelViewAdapter viewAdapter) {
		if (this.viewAdapter != null) {
			this.viewAdapter.unregisterDataSetObserver(dataObserver);
		}
		this.viewAdapter = viewAdapter;
		if (this.viewAdapter != null) {
			this.viewAdapter.registerDataSetObserver(dataObserver);
		}

		invalidateWheel(true);
	}

	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int index, boolean animated) {
		if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
			return; // throw?
		}

		int itemCount = viewAdapter.getItemsCount();
		if (index < 0 || index >= itemCount) {
			if (isCyclic) {
				while (index < 0) {
					index += itemCount;
				}
				index %= itemCount;
			} else {
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
				int itemsToScroll = index - currentItem;
				if (isCyclic) {
					int scroll = itemCount + Math.min(index, currentItem)
							- Math.max(index, currentItem);
					if (scroll < Math.abs(itemsToScroll)) {
						itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
					}
				}
				scroll(itemsToScroll, 0);
			} else {
				scrollingOffset = 0;
				currentItem = index;
				invalidate();
			}
		}
	}

	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}

	public boolean isCyclic() {
		return isCyclic;
	}

	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;
		invalidateWheel(false);
	}

	public void invalidateWheel(boolean clearCaches) {
		if (clearCaches) {
			recycle.clearAll();
			if (itemsLayout != null) {
				itemsLayout.removeAllViews();
			}
			scrollingOffset = 0;
		} else if (itemsLayout != null) {
			// cache all items
			recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
		}

		invalidate();
	}

	// 计算layout期望的高度
	private int getDesiredHeight(LinearLayout layout) {
		if (layout != null && layout.getChildAt(0) != null) {
			itemHeight = layout.getChildAt(0).getMeasuredHeight();
		}

		int desired = itemHeight * DEF_VISIBLE_ITEMS;

		return Math.max(desired, getSuggestedMinimumHeight());
	}

	// 滚动item的高度
	private int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		}

		if (itemsLayout != null && itemsLayout.getChildAt(0) != null) {
			itemHeight = itemsLayout.getChildAt(0).getHeight();
			return itemHeight;
		}

		return getHeight() / DEF_VISIBLE_ITEMS;
	}

	// 计算控件宽度及字体布局
	private int calculateLayoutWidth(int widthSize, int mode) {
		itemsLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		// parent指定child长宽不受限制
		itemsLayout
				.measure(MeasureSpec.makeMeasureSpec(widthSize,
						MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
						0, MeasureSpec.UNSPECIFIED));
		int width = itemsLayout.getMeasuredWidth();

		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			// Check against our minimum width
			width = Math.max(width, getSuggestedMinimumWidth());

			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
			}
		}

		itemsLayout.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		return width;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		buildViewForMeasuring();

		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(itemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layout(r - l, b - t);
	}

	private void layout(int width, int height) {
		itemsLayout.layout(0, 0, width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (viewAdapter != null && viewAdapter.getItemsCount() > 0) {
			updateView();
			drawItems(canvas);
		}
	}

	private void drawItems(Canvas canvas) {
		canvas.save();
		// -top是layout真实的左上点y坐标,scrollingOffset是其该移动的距离
		int top = (currentItem - firstItem) * getItemHeight()
				+ (getItemHeight() - getHeight()) / 2;
		canvas.translate(0, -top + scrollingOffset);
		itemsLayout.draw(canvas);
		canvas.restore();
	}

	// Scrolls the wheel
	private void doScroll(int delta) {
		// delta表示两条滚动消息间，scroller移动的距离
		scrollingOffset += delta;

		int itemHeight = getItemHeight();
		int count = scrollingOffset / itemHeight;

		int pos = currentItem - count;
		int itemCount = viewAdapter.getItemsCount();

		int fixPos = scrollingOffset % itemHeight;

		if (Math.abs(fixPos) <= itemHeight / 2) {
			fixPos = 0;
		}
		if (isCyclic && itemCount > 0) {
			if (fixPos > 0) {
				pos--;
				count++;
			} else if (fixPos < 0) {
				pos++;
				count--;
			}
			// fix position by rotating
			while (pos < 0) {
				pos += itemCount;
			}
			pos %= itemCount;
		} else {
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= itemCount) {
				count = currentItem - itemCount + 1;
				pos = itemCount - 1;
			} else if (pos > 0 && fixPos > 0) {
				pos--;
				count++;
			} else if (pos < itemCount - 1 && fixPos < 0) {
				pos++;
				count--;
			}
		}

		int offset = scrollingOffset;
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}

		// update offset
		scrollingOffset = offset - count * itemHeight;
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}

		// 出错时强退
		if (forceStop) {
			SoundMgr.pause(R.raw.box_stop);
			setCurrentItem(stopItem, false);
			// stopItem = pos;
			forceStop = false;
			stop();
		} else if ((!isWheeling()) && pos == stopItem) { // && canStop
			lastCounter++;
			if (lastCounter == LAST_WHEEL_CNT) {
				stop();
			}
		}
	}

	private void stop() {
		scroller.stopScrolling();
		scroller.finishScrolling();
		SoundMgr.play(R.raw.box_stop);

		// if (null != next)
		// next.setCanStop(true);
	}

	public boolean isWheeling() {
		return scroller.getTimePassed() <= MIN_WHEEL_DURATION;
	}

	public void setForceStop(boolean forceStop) {
		this.forceStop = forceStop;
	}

	public void scroll(int itemsToScroll, int time) {
		int distance = itemsToScroll * getItemHeight() - scrollingOffset;
		scroller.scroll(distance, time);
	}

	/**
	 * Calculates range for wheel items
	 * 
	 * @return the items range
	 */
	private ItemsRange getItemsRange() {
		if (getItemHeight() == 0) {
			return null;
		}

		int first = currentItem;
		int count = 1;

		while (count * getItemHeight() < getHeight()) {
			first--;
			count += 2;
		}

		if (scrollingOffset != 0) {
			if (scrollingOffset > 0) {
				first--;
			}
			count++;

			// process empty items above the first or below the second
			int emptyItems = scrollingOffset / getItemHeight();
			first -= emptyItems;
			count += Math.asin(emptyItems);
		}
		return new ItemsRange(first, count);
	}

	/**
	 * Rebuilds wheel items if necessary. Caches all unused items.
	 * 
	 * @return true if items are rebuilt
	 */
	private boolean rebuildItems() {
		boolean updated = false;
		ItemsRange range = getItemsRange();
		if (itemsLayout != null) {
			int first = recycle.recycleItems(itemsLayout, firstItem, range);
			updated = firstItem != first;
			firstItem = first;
		} else {
			createItemsLayout();
			updated = true;
		}

		if (!updated) {
			updated = firstItem != range.getFirst()
					|| itemsLayout.getChildCount() != range.getCount();
		}

		if (firstItem > range.getFirst() && firstItem <= range.getLast()) {
			for (int i = firstItem - 1; i >= range.getFirst(); i--) {
				if (!addViewItem(i, true)) {
					break;
				}
				firstItem = i;
			}
		} else {
			firstItem = range.getFirst();
		}

		int first = firstItem;
		for (int i = itemsLayout.getChildCount(); i < range.getCount(); i++) {
			if (!addViewItem(firstItem + i, false)
					&& itemsLayout.getChildCount() == 0) {
				first++;
			}
		}
		firstItem = first;

		return updated;
	}

	private void updateView() {
		if (rebuildItems()) {
			calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			layout(getWidth(), getHeight());
		}
	}

	private void createItemsLayout() {
		if (itemsLayout == null) {
			itemsLayout = new LinearLayout(getContext());
			itemsLayout.setOrientation(LinearLayout.VERTICAL);
		}
	}

	private void buildViewForMeasuring() {
		// clear all items
		if (itemsLayout != null) {
			recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
		} else {
			createItemsLayout();
		}

		// add views
		int addItems = DEF_VISIBLE_ITEMS / 2;
		for (int i = currentItem + addItems; i >= currentItem - addItems; i--) {
			if (addViewItem(i, true)) {
				firstItem = i;
			}
		}
	}

	/**
	 * Adds view for item to items layout
	 * 
	 * @param index
	 *            the item index
	 * @param first
	 *            the flag indicates if view should be first
	 * @return true if corresponding item exists and is added
	 */
	private boolean addViewItem(int index, boolean first) {
		View view = getItemView(index);
		if (view != null) {
			if (first) {
				itemsLayout.addView(view, 0);
			} else {
				itemsLayout.addView(view);
			}

			return true;
		}

		return false;
	}

	/**
	 * Checks whether intem index is valid
	 * 
	 * @param index
	 *            the item index
	 * @return true if item index is not out of bounds or the wheel is cyclic
	 */
	private boolean isValidItemIndex(int index) {
		return viewAdapter != null
				&& viewAdapter.getItemsCount() > 0
				&& (isCyclic || index >= 0
						&& index < viewAdapter.getItemsCount());
	}

	/**
	 * Returns view for specified item
	 * 
	 * @param index
	 *            the item index
	 * @return item view or empty view if index is out of bounds
	 */
	private View getItemView(int index) {
		if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
			return null;
		}
		int count = viewAdapter.getItemsCount();
		if (!isValidItemIndex(index)) {
			return viewAdapter
					.getEmptyItem(recycle.getEmptyItem(), itemsLayout);
		} else {
			while (index < 0) {
				index = count + index;
			}
		}

		index %= count;
		return viewAdapter.getItem(index, recycle.getItem(), itemsLayout);
	}

	public void stopScrolling() {
		scroller.stopScrolling();
	}

	public boolean isScrollingPerformed() {
		return isScrollingPerformed;
	}

	public void setWheeling() {
		this.stopItem = WHEELING;
	}

	public void setStopItem(int stopItem) {
		this.stopItem = stopItem;

		int distance = ((int) (Math.random() * -10 - 3) * viewAdapter
				.getItemsCount()) + (this.stopItem - this.currentItem);
		scrollingOffset = 0;
		scroll(distance, 8000);
	}

	public void setDefaultStopItem() {
		this.stopItem = DEFAULT_STOP;
	}

//	 public void setCanStop(boolean canStop) {
//	 this.canStop = canStop;
//	 }
//
//	 public void setNext(WheelView next) {
//	 this.next = next;
//	 }

	public void setLastCnt(int lastCnt) {
		this.lastCounter = lastCnt;
	}

	public void initWheel(List<GambleData> gambleData) {
		setViewAdapter(new GambleSlotAdapter(gambleData));
		setCurrentItem(DEFAULT_STOP);
		setCyclic(true);
		setEnabled(false);
	}

	public void initWheel(List<Integer> imgs, int defStopPos) {
		DEFAULT_STOP = defStopPos;
		setViewAdapter(new CurrencySlotAdapter(imgs));
		setCurrentItem(DEFAULT_STOP);
		setCyclic(true);
		setEnabled(false);
	}

	public void setWheel(boolean canStop, WheelView next) {
		// 设置需要滚动的元素个数，以及滚动时间
		// wheel.scroll(-80 + (int) (Math.random() * 50),
		// WheelView.MAX_WHEEL_DURATION);
		scroll(-300 + (int) (Math.random() * 50), WheelView.MAX_WHEEL_DURATION);
		setWheeling();
//		 setCanStop(canStop);
//		 setNext(next);
		setLastCnt(0);
	}
}
