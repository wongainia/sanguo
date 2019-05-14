package com.vikings.sanguo.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.PropRoulette;

public class RouletteView extends ImageView implements Runnable {
	private static final int FRAME_TIME = 20; // 转动间隔时间（毫秒）

	private Bitmap mBitmap = null;
	private boolean isScroll;
	private int mWidth, mHeight;
	private boolean isRoating = false;
	private boolean stop = false;
	private boolean forceStop = false;
	private float gridDegree;// 每个grid所占的区域
	private float curDegree;// 当前轮盘角度
	private float endDegree;//
	private int time;// 转一圈时间
	private Matrix matrix = new Matrix();

	public RouletteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RouletteView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RouletteView(Context context) {
		super(context);
	}

	public boolean isScroll() {
		return isScroll;
	}

	public void init(List<PropRoulette> propRoulettelist, int bgResId, int time) {
		this.time = time;
		Bitmap src = getBitmap(bgResId);
		src = src.copy(src.getConfig(), true);
		if (null == src || propRoulettelist.isEmpty())
			return;
		gridDegree = 360f / propRoulettelist.size();
		mWidth = src.getWidth();
		mHeight = src.getHeight();
		mBitmap = mixBitmap(src, propRoulettelist);
	}

	private Bitmap mixBitmap(Bitmap src, List<PropRoulette> list) {
		Bitmap mixBitmap = Bitmap.createBitmap(src, 0, 0, mWidth, mHeight);
		Canvas canvas = new Canvas(mixBitmap);
		// canvas.drawBitmap(src, 0, 0, null);
		for (int i = 0; i < list.size(); i++) {
			PropRoulette pr = list.get(i);
			Bitmap icon = getBitmap(pr.getIcon());
			if (icon == null)
				continue;
			matrix.reset();

			matrix.postRotate(gridDegree * i, icon.getWidth() / 2f,
					icon.getHeight() / 2f);
			matrix.postTranslate(pr.getX() * Config.SCALE_FROM_HIGH, pr.getY()
					* Config.SCALE_FROM_HIGH);
			canvas.drawBitmap(icon, matrix, null);
		}
		canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
		canvas.restore();// 存储
		return mixBitmap;
	}

	private Bitmap getBitmap(int resId) {
		return Config.getController().getBitmap(resId);
	}

	private Bitmap getBitmap(String name) {
		return Config.getController().getBitmap(name);
	}

	public void freeMem() {
		mBitmap.recycle();
		mBitmap = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null != mBitmap) {
			canvas.save();
			matrix.reset();
			matrix.preRotate(curDegree, mWidth / 2f, mHeight / 2f);
			canvas.drawBitmap(mBitmap, matrix, null);
			canvas.restore();
		}
	}

	public void start() {
		if (isRoating) {
			return;
		} else {
			startRoating();
		}
	}

	public void stop(int index) {
		this.stop = true;
		endDegree = getEndDegree(index);
	}

	public void stop() {
		stop = true;
		forceStop = true;
	}

	private void startRoating() {
		stop = false;
		forceStop = false;
		new Thread(this).start();
	}

	// 每次时间间隔旋转的角度
	private float getRotateDegree() {
		return 360f / time * FRAME_TIME; //
	}

	@Override
	public void run() {
		while (!stop || (curDegree != endDegree)) {
			isScroll = true;
			if (forceStop)
				break;
			float temp = curDegree;
			curDegree += getRotateDegree();
			if (curDegree > 360)
				curDegree = curDegree - 360;
			if (stop) {
				if (temp > curDegree) {
					if (curDegree >= endDegree)
						curDegree = endDegree;
				} else {
					if (temp < endDegree && curDegree >= endDegree) {
						curDegree = endDegree;
					}
				}
			}
			try {
				Thread.sleep(FRAME_TIME);
			} catch (InterruptedException e) {

			}
			postInvalidate();
		}
		isScroll = false;
	}

	protected float getEndDegree(int index) {
		float degree = 360 - (index - 1) * gridDegree;
		if (degree >= 360)
			degree = degree - 360;
		return degree;
	}
}
