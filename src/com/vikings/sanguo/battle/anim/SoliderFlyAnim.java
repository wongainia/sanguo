package com.vikings.sanguo.battle.anim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleArrayInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.BattleEventArmInfo;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.window.BattleWindow;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ImageUtil;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class SoliderFlyAnim extends Animation
{
	private int mPivotXType = ABSOLUTE;
	private int mPivotYType = ABSOLUTE;

	private float mPivotXValue = 0.0f;
	private float mPivotYValue = 0.0f;

	private float mFromDegrees;
	private float mToDegrees;
	private float mPivotX;
	private float mPivotY;

	private int mFromXType = ABSOLUTE;
	private int mToXType = ABSOLUTE;
	private int mFromYType = ABSOLUTE;
	private int mToYType = ABSOLUTE;
	private float mFromXValue = 0.0f;
	private float mToXValue = 0.0f;

	private float mFromYValue = 0.0f;
	private float mToYValue = 0.0f;

	private float mFromXDelta;
	private float mToXDelta;
	private float mFromYDelta;
	private float mToYDelta;

	private float xDis; // x方向移动的距离
	private float yDis; // y方向移动的距离
	private boolean isLeft;
	private int startOffset;
	private int during;

	private int threshold;
	private float throisidDat;
	float a, b, c;
	private long start_time = 0;

	private final int BEAT_TIME = 100;
	private int beatDis = 30;

	private ViewGroup window;
	private boolean isShowHp = false;
	private int currentHp;
	private int totalHp;

	private BattleEventArmInfo beai;
	private BattleArrayInfoClient baic;
	private BattleWindow battleWin;

	// 飞的同时做掉血处理

	public SoliderFlyAnim(float fromDegrees, float toDegrees, int pivotXType,
			float pivotXValue, int pivotYType, float pivotYValue,
			int fromXType, float fromXValue, int toXType, float toXValue,
			int fromYType, float fromYValue, int toYType, float toYValue)
	{
		this.mFromDegrees = fromDegrees;
		this.mToDegrees = toDegrees;
		this.mPivotXType = pivotXType;
		this.mPivotXValue = pivotXValue;
		this.mPivotYType = pivotYType;
		this.mPivotYValue = pivotYValue;
		this.mFromXType = fromXType;
		this.mFromXValue = fromXValue;
		this.mToXType = toXType;
		this.mToXValue = toXValue;
		this.mFromYType = fromYType;
		this.mFromYValue = fromYValue;
		this.mToYType = toYType;
		this.mToYValue = toYValue;
	}

	public void setView(boolean isLeft, ViewGroup window, int currentHp,
			int totalHp, BattleEventArmInfo beai, BattleArrayInfoClient baic,
			BattleWindow battleWin)
	{
		this.isLeft = isLeft;
		this.window = window;
		this.currentHp = currentHp;
		this.totalHp = totalHp;

		this.beai = beai;
		this.baic = baic;
		this.battleWin = battleWin;
	}
	
	public void setView(boolean isLeft, ViewGroup window, int currentHp,
			int totalHp)
	{
		this.isLeft = isLeft;
		this.window = window;
		this.currentHp = currentHp;
		this.totalHp = totalHp;	
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight)
	{
		super.initialize(width, height, parentWidth, parentHeight);
		mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
		mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
		mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
		mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
		mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
		mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);

		threshold = 2 * during / 3
				+ Math.abs(new Random().nextInt() % (during / 3));
		throisidDat = (float) threshold / during;
		if (mFromYDelta - mToYDelta > 0) // 向上
		{
			a = -Math.abs(mFromYDelta - mToYDelta - beatDis)
					/ (throisidDat * throisidDat);
			b = -2 * a * throisidDat;
			c = mFromYDelta + beatDis;
		} else
		{
			a = -Math.abs(mToYDelta - beatDis - mFromYDelta);
			b = 0;
			c = mFromYDelta - beatDis;
		}
	}

//	public void modifyHP(boolean isLeft, long value, long total)
//	{
//		if (total == 0)
//		{
//			return;
//		}
//		ProgressBar v = (ProgressBar) (isLeft ? window
//				.findViewById(R.id.lAmyHP) : window.findViewById(R.id.rAmyHP));
//		if (value < 0)
//			value = 0;
//
//		int cur = (int) (100 * value / total);
//		v.set(cur);
//	}

	public void setDuration(long durationMillis)
	{
		super.setDuration(durationMillis);
		during = (int) durationMillis;
	}

	// 0-0.1 秒被击 后退s

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t)
	{
		if (start_time == 0)
		{
			start_time = System.currentTimeMillis();
		}

		float dx = mFromXDelta;
		float dy = mFromYDelta;
		if (mFromXDelta != mToXDelta)
		{
			dx = mFromXDelta + ((mToXDelta - mFromXDelta) * interpolatedTime);
		}
		if (mFromYDelta != mToYDelta)
		{
			dy = mFromYDelta + ((mToYDelta - mFromYDelta) * interpolatedTime);
		}
		// Log.e("SoliderFlyAnim", "time:" + (System.currentTimeMillis() -
		// start_time));
		int time = (int) (System.currentTimeMillis() - start_time);
		float interTime = (float) time / during;
		float baseTime = (float) BEAT_TIME / during;

		float degrees = mFromDegrees
				+ ((mToDegrees - mFromDegrees) * (interTime - baseTime));

		float yData = (float) (a * Math.pow((interTime - baseTime), 2)) + b
				* (interTime - baseTime) + c;
		// Log.e("SoliderFlyAnim", "dx:" + dx + " dy:" + dy + " degrees:" +
		// degrees
		// + " interpolatedTime:" + interpolatedTime + "yData:" + yData);
		if (mPivotX == 0.0f && mPivotY == 0.0f)
		{
			t.getMatrix().setRotate(degrees);
			t.getMatrix().postTranslate(dx, dy);
		} else
		{
			if (time <= BEAT_TIME)
			{
				if (mFromYDelta - mToYDelta > 0)
				{
					dx = mFromXDelta + (beatDis * time / BEAT_TIME);
					dy = mFromYDelta - (beatDis * time / BEAT_TIME);
				} else
				{
					dx = mFromXDelta - (beatDis * time / BEAT_TIME);
					dy = mFromYDelta + (beatDis * time / BEAT_TIME);
				}
				t.getMatrix().postTranslate(dx, dy);
			} else
			{
				if (mFromYDelta - mToYDelta > 0)
				{
					if (mFromXDelta != mToXDelta)
					{
						dx = mFromXDelta
								+ beatDis
								+ ((mToXDelta - mFromXDelta) * (interTime - baseTime));
					}

				} else
				{
					if (mFromXDelta != mToXDelta)
					{
						dx = mFromXDelta
								- beatDis
								+ ((mToXDelta - mFromXDelta) * (interTime - baseTime));
					}
				}

				t.getMatrix().setRotate(degrees, mPivotX, mPivotY);
				t.getMatrix().postTranslate(dx, -yData);

//				if (isShowHp == false)
//				{
//					// modifyHP(isLeft, currentHp, totalHp);
//					isShowHp = true;
//					if(beai != null && baic != null)
//					{
//						handFallHp(beai, isLeft, baic);
//					}
//				}
			}
		}
	}

	// 处理掉血
//	private void handFallHp(BattleEventArmInfo beai, boolean isLeft,
//			BattleArrayInfoClient baic)
//	{
//		BaseAnim numFly = null;
//		BaseAnim fallHp = null;
//		TroopProp tp = null;
//		try
//		{
//			tp = (TroopProp) CacheMgr.troopPropCache.get(beai.getArmid());
//		} catch (GameException e)
//		{
//			e.printStackTrace();
//		}
//
//		String pre = "";// "#btl_amy_hp#";
//		pre += beai.getValue() > 0 ? "#btl_minus#" : "#btl_add#";
//
//		List<BaseAnim> fallUp = new ArrayList<BaseAnim>();
//		// 把血换成士兵数
//
//		if (beai.getValue() != 0)
//		{
//			if (2 == beai.getEx())
//			{
//				pre = "#!force_atk# " + pre;
//
//				StringBuilder builder = new StringBuilder();
//				builder.append(pre);
//				ArrayList<Integer> valLs = CalcUtil.parseLong(Math.abs(beai
//						.getValue()));
//				builder.append(ImageUtil.getNumStr(valLs, "btl_"));
//
//				List<BaseAnim> ls = new ArrayList<BaseAnim>();
//
//				Animation forceAtk = AnimPool.forceAtk(0);
//
//				numFly = battleWin.getForceAtk(isLeft, forceAtk,
//						builder.toString(), tp, baic);
//
//			} else
//			{
//				ArrayList<Integer> valLs = null;
//				StringBuilder builder = new StringBuilder();
//				builder.append(pre);
//				valLs = CalcUtil.parseLong(Math.abs(beai.getValue()));
//				builder.append(ImageUtil.getNumStr(valLs, "btl_"));
//
//				List<BaseAnim> ls = new ArrayList<BaseAnim>();
//				numFly = battleWin.getTopEff(isLeft, AnimPool.getTop(0),
//						builder.toString(), tp, baic);
//
//				// 不是boss 返回数量
//
//			}
//		}
//		if (baic != null)
//		{
//			fallHp = battleWin.modifyHpNum(AnimPool.stay(100), isLeft, baic,
//					tp.getName(), tp.isBoss());
//		}
//		if (numFly != null)
//			numFly.start();
//		if (fallHp != null)
//			fallHp.start();
//
//	}
}
