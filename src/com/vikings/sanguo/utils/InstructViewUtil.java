/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-9-21
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.view.View;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.controller.GameController;

public class InstructViewUtil {
	//手指方向
	public static final int UP_RIGHT = 0;
	public static final int RIGHT = 1;
	public static final int DOWN_RIGHT = 2;
	public static final int DOWN = 3;
	public static final int DOWN_LEFT = 4;
	public static final int LEFT = 5;
	public static final int UP_LEFT = 6;
	public static final int UP = 7;
	
	//类型：1，新手教程，2，任务引导
	public static final int TYPE_TUTOR = 0;
	public static final int TYPE_INSTRUCT = 1;
	
	// npc的上中下三个位置
	final static public byte ABOVE = 0;
	final static public byte MIDDLE = ABOVE + 1;
	final static public byte BELOW = MIDDLE + 1;
	
	public static Paint getDottedLinePaint() {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);  
		paint.setColor(0xFFFD3A3A);
		//线宽为4, DashPathEffect中，线长为13，空白为6
		paint.setStrokeWidth(4);
		PathEffect effects = new DashPathEffect(new float[]{13,6},1);  
		paint.setPathEffect(effects);
		return paint;
	}
	
	public static Paint getTxtPaint() {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);  
		paint.setColor(0xFFFFFFFF);
		paint.setTextSize(16);
		paint.setFakeBoldText(true);
		return paint;
	}
	
	//将v绘制到bitmap的canvas上
	public static Bitmap getViewBitmap(View v) {
		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		bitmap.setDensity(Config.densityDpi);
		Canvas c = new Canvas(bitmap);
		v.draw(c);
		c.save(Canvas.ALL_SAVE_FLAG);
		c.restore();
		return bitmap;
	}
	
	public static Bitmap clockWiseRotate45(Bitmap bmp, int amount) {
		GameController controller = Config.getController();
		Bitmap temp = controller.getBitmapCache().get("down" + amount);
		if (temp == null) {
			int w = bmp.getWidth();
			int h = bmp.getHeight();
			Matrix matrix = new Matrix();
			matrix.postRotate(45f * amount);
			bmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
			controller.getBitmapCache().save("down" + amount, bmp);
		} else {
			bmp = temp;
		}
		
		return bmp; 
	}
	
	//返回数组中，0，1为获取箭头左上点，2，3为手指偏移量，4，5为text偏移
	public static int[] getOffsets(Bitmap bitmap, View v, Rect rect, int direct) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int location[] = new int[2];
		v.getLocationInWindow(location);
		int left = location[0];
		int top = location[1];
		// 图片对角线长度
		int diagonal = (int) Math.sqrt(w * w + h * h);
		
		int[] offset = new int[6];
		
		switch (direct) {
		case UP_RIGHT:
			offset[0] = left - w;
			offset[1] = top + v.getHeight();
			offset[2] = 10;
			offset[3] = -10;
			if (null != rect) {
				offset[4] = offset[0] - rect.width() / 2;
				offset[5] = offset[1] + h;
			}
			break;
		case RIGHT:
			offset[0] = left - diagonal;
			offset[1] = top - (diagonal / 2 - v.getHeight() / 2);
			offset[2] = 10;
			offset[3] = 0;
			if (null != rect) {
				offset[4] = offset[0] - rect.width();
				offset[5] = offset[1] + (h / 2 - rect.height() / 2);
			}
			break;
		case DOWN_RIGHT:
			offset[0] = left - w;
			offset[1] = top - h;
			offset[2] = 10;
			offset[3] = 10;
			if (null != rect) {
				offset[4] = offset[0] - rect.width() / 2;
				offset[5] = offset[1] - rect.height();
			}
			break;
		case DOWN:
			offset[0] = left - (diagonal / 2 - v.getWidth() / 2);
			offset[1] = top - diagonal;
			offset[2] = 0;
			offset[3] = 10;
			if (null != rect) {
				offset[4] = offset[0] + (w / 2 - rect.width() / 2);
				offset[5] = offset[1] - rect.height();
			}
			break;
		case DOWN_LEFT:
			offset[0] = left + v.getWidth();
			offset[1] = top - h;
			offset[2] = -10;
			offset[3] = 10;
			if (null != rect) {
				offset[4] = offset[0] + w - rect.width() / 2;
				offset[5] = offset[1] - rect.height();
			}
			break;
		case LEFT:
			offset[0] = left + v.getWidth();
			offset[1] = top - (diagonal / 2 - v.getHeight() / 2);
			offset[2] = -10;
			offset[3] = 0;
			if (null != rect) {
				offset[4] = offset[0] + w;
				offset[5] = offset[1] + (h / 2 - rect.height() / 2);
			}
			break;
		case UP_LEFT:
			offset[0] = left + v.getWidth();
			offset[1] = top + v.getHeight();
			offset[2] = -10;
			offset[3] = -10;
			if (null != rect) {
				offset[4] = offset[0] + w - rect.width() / 2;
				offset[5] = offset[1] + h;
			}
			break;
		case UP:
			offset[0] = left - (diagonal / 2 - v.getWidth() / 2);
			offset[1] = top + v.getHeight();
			offset[2] = 0;
			offset[3] = -10;
			if (null != rect) {
				offset[4] = offset[0] + (w / 2 - rect.width() / 2);
				offset[5] = offset[1] + h;
			}
			break;
		}
		return offset;
	}
}
