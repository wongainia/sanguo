/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-9-18
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class DottedLineInstructView extends View {
	private View instructedView; // 被指示的view
	private View contentView;
	private Bitmap dottedLine;
	private Bitmap verDottedLine;
	private Bitmap leftArrow;
	private Bitmap rightArrow;
	private Bitmap upArrow;
	private Bitmap downArrow;
	private Paint paint = new Paint();
	final static private int ARROW_OFFSET = 10; // 箭头相对于选中框的偏移
	private float startScale = 0.8f;

	public DottedLineInstructView(Context context) {
		super(context);
	}

	public DottedLineInstructView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DottedLineInstructView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	private void getBitmap() {
		dottedLine = Config.getController()
				.getBitmap(R.drawable.quest_dot_line);
		verDottedLine = Config.getController().getRotateBitmap(
				"quest_dot_line", 90);

		leftArrow = Config.getController().getBitmap(R.drawable.quest_arrow);
		upArrow = Config.getController().getRotateBitmap("quest_arrow", 90);
		rightArrow = Config.getController().getRotateBitmap("quest_arrow", 180);
		downArrow = Config.getController().getRotateBitmap("quest_arrow", 270);
	}

	public void set(View instructedView, View contentView) { // , CallBack
																// callBack byte
																// shape
		this.instructedView = instructedView;
		this.contentView = contentView;
		getBitmap();
	}

	public void setStartScale(float startScale) {
		this.startScale = startScale;
	}

	public void resetScale() {
		startScale = 0.8f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (null == instructedView || null == contentView)
			return;

		// 获取instructedView的左上点坐标和长宽
		Rect rect = new Rect();
		instructedView.getGlobalVisibleRect(rect);
		int iLeft = rect.left;
		int iTop = rect.top;
		int iRight = rect.right;
		int iBottom = rect.bottom;
		// 设置点击区域
		int iYMid = iTop + ((iBottom - iTop) >> 1);

		// 获取instructedView的左上点坐标和长宽
		contentView.getGlobalVisibleRect(rect);
		int cLeft = rect.left;
		int cTop = rect.top;
		int cRight = rect.right;
		int cBottom = rect.bottom;

		// 画虚线连线
		int finalX = (int) (cLeft + ((cRight - cLeft) * startScale));
		int dotLen = dottedLine.getWidth();

		int horCnt = 0;
		int verCnt = 0;
		int turningY = 0; // 虚线转折点x坐标
		int offY = 0; // 竖直虚线竖直居中的偏移量

		// instructView的右边在finalX的左边
		if (iRight <= finalX) {
			// 水平虚线中实线段的个数,四舍五入是为了让横线足够长
			horCnt = (int) ((float) (finalX - iRight + ARROW_OFFSET) / dotLen + 0.5f);
			turningY = iRight - ARROW_OFFSET + lineLength(horCnt)
					- (dottedLine.getHeight() >> 1);

			// 画水平线
			for (int i = 0; i < horCnt; i++) {
				if (0 == i)
					canvas.drawBitmap(leftArrow, iRight - ARROW_OFFSET, iYMid
							- ((dottedLine.getHeight()) >> 1), paint);
				else
					canvas.drawBitmap(dottedLine, iRight - ARROW_OFFSET + i
							* dotLen, iYMid - ((dottedLine.getHeight()) >> 1),
							paint);
			}

			// 画垂直线
			if (iYMid <= cTop) { // instructedView在contentView上面
				// 垂直虚线中实线段个数
				verCnt = (cTop - iYMid) / dotLen;
				// 垂直虚线竖直居中处理
				offY = (((cTop - iYMid) - lineLength(verCnt)) >> 1);

				for (int i = 0; i < verCnt; i++)
					canvas.drawBitmap(verDottedLine, turningY, iYMid + offY + i
							* dotLen, paint);
			} else if (cBottom <= iYMid) { // contentView在instructedView上面
				verCnt = (iYMid - cBottom) / dotLen;
				offY = ((iYMid - cBottom) - lineLength(verCnt)) >> 1;

				for (int i = 0; i < verCnt; i++)
					canvas.drawBitmap(verDottedLine, turningY, cBottom + offY
							+ i * dotLen, paint);
			}
		}
		// instructView的右边在finalX的右边，且instructView的左边在finalX的右边
		else if (iLeft <= finalX && iRight > finalX) {
			int offX = finalX - dottedLine.getHeight();
			if (iBottom <= cTop) { // 取instructView的中点为x坐标
				verCnt = (int) ((float) (cTop - iBottom + ARROW_OFFSET)
						/ dotLen + 0.5f);
				if (1 == verCnt)
					verCnt = 2;
				for (int i = 0; i < verCnt; i++) {
					if (i == 0)
						canvas.drawBitmap(upArrow, offX,
								iBottom - ARROW_OFFSET, paint);
					else
						canvas.drawBitmap(verDottedLine, offX, iBottom
								- ARROW_OFFSET + i * dotLen, paint);
				}
			} else if (cBottom <= iTop) {
				verCnt = (int) ((float) (iTop - cBottom + ARROW_OFFSET)
						/ dotLen + 0.5f);
				if (1 == verCnt)
					verCnt = 2;
				for (int i = 1; i <= verCnt; i++) {
					if (1 == i)
						canvas.drawBitmap(downArrow, offX, iTop + ARROW_OFFSET
								- i * dotLen, paint);
					else
						canvas.drawBitmap(verDottedLine, offX, iTop
								+ ARROW_OFFSET - i * dotLen, paint);
				}
			}
		} else {
			horCnt = (int) ((float) (iLeft - finalX + ARROW_OFFSET) / dotLen + 0.5f);
			turningY = iLeft + ARROW_OFFSET - lineLength(horCnt);

			for (int i = 0; i < horCnt; i++) {
				if (i == horCnt - 1)
					canvas.drawBitmap(rightArrow, turningY - ARROW_OFFSET + i
							* dotLen, iYMid, paint);
				else
					canvas.drawBitmap(dottedLine, turningY - ARROW_OFFSET + i
							* dotLen, iYMid, paint);
			}

			// 画垂直线
			if (iBottom <= cTop) { // instructedView在contentView上面
				// 垂直虚线中实线段个数
				verCnt = (cTop - iYMid) / dotLen;
				// 垂直虚线竖直居中处理
				offY = (((cTop - iYMid) - lineLength(verCnt)) >> 1);

				for (int i = 0; i < verCnt; i++)
					canvas.drawBitmap(verDottedLine,
							turningY - (dottedLine.getHeight() >> 1), iYMid
									+ offY + i * dotLen, paint);
			} else if (cBottom <= iTop) { // contentView在instructedView上面
				verCnt = (iYMid - cBottom) / dotLen;
				offY = ((iYMid - cBottom) - lineLength(verCnt)) >> 1;

				for (int i = 0; i < verCnt; i++)
					canvas.drawBitmap(verDottedLine,
							turningY - (dottedLine.getHeight() >> 1), cBottom
									+ offY + i * dotLen, paint);
			}
		}
	}

	private int lineLength(int cnt) {
		return cnt * dottedLine.getWidth();
	}
}
