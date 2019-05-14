/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-8 上午11:06:29
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 进度条，对应布局：progress_bar.xml
 */
package com.vikings.sanguo.widget;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.ui.BaseUI;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

//该类的使用者，必须按照顺序来调用提供的方法，否则有可能会出问题。这个顺序应该由该类本身控制，不暴露给使用者
public class CustomProgressBar extends BaseUI {
	private View widget;
	private int height = 0;

	public CustomProgressBar(View vg) {
		widget = vg.findViewById(R.id.bar);
	}

	// 设置进度条底的图片
	public void setBarBg(int resId) {
		ViewUtil.setImage(widget, R.id.bg, resId);
	}

	public void setBarWidth(int width) {
		widget.findViewById(R.id.bg).getLayoutParams().width = width;
	}

	// 此方法必须在setBarBg()前使用才有效
	public void setBarHeight(int height) {
		this.height = height;
	}

	// 设置进度条底的图片和宽度
	public void setBarBg(int resId, int width) {
		setBarWidth(width);
		if (height != 0)
			widget.findViewById(R.id.bg).getLayoutParams().height = height;
		ViewUtil.setImage(widget, R.id.bg, resId);
	}

	// 设置进度条的图片
	public void setProgressBg(int resId) {
		ViewUtil.setImage(widget, R.id.progress, resId);
	}

	public void setMidProgressBg(int resId) {
		ViewUtil.setImage(widget, R.id.midProgress, resId);
	}

	// 设置进度
	public void setProgress(int cur, int total) {
		setProgress(cur, 0, total);
	}

	// 设置进度
	public void setProgress(int cur, int mid, int total) {
		if (0 == total)
			return;

		if (cur > total)
			cur = total;

		if (mid > total)
			mid = total;

		if (cur > 0)
			ViewUtil.setVisible(widget, R.id.progress);
		else
			ViewUtil.setGone(widget, R.id.progress);

		if (mid > 0)
			ViewUtil.setVisible(widget, R.id.midProgress);
		else
			ViewUtil.setGone(widget, R.id.midProgress);

		View bg = widget.findViewById(R.id.bg);
		int barW = bg.getLayoutParams().width;

		ImageView progress = (ImageView) widget.findViewById(R.id.progress);
		if (null == progress)
			return;
		int progressW = (int) (1F * barW * cur / (float) total);
		// 保证进度条不被压缩的很严重
		Drawable progressD = progress.getBackground();
		if (null != progressD) {
			progressW = Math.max(progressW, progressD.getIntrinsicWidth());
		}

		LayoutParams p = progress.getLayoutParams();
		p.width = progressW;
		if (height != 0)
			p.height = height;
		progress.setLayoutParams(p);
		progress.invalidate();

		ImageView midProgress = (ImageView) widget
				.findViewById(R.id.midProgress);
		if (null == midProgress)
			return;
		int midProgressW = (int) (1F * barW * mid / (float) total);
		// 保证进度条不被压缩的很严重
		Drawable midProgressD = progress.getBackground();
		if (null != midProgressD) {
			midProgressW = Math.max(midProgressW,
					midProgressD.getIntrinsicWidth());
		}
		LayoutParams mp = midProgress.getLayoutParams();
		mp.width = midProgressW;
		if (height != 0)
			p.height = height;
		midProgress.setLayoutParams(mp);
		midProgress.invalidate();
	}

	public void setDesc(String str, float textSize) {
		TextView textView = (TextView) widget.findViewById(R.id.desc);
		textView.setTextSize(textSize);
		setDesc(str);
	}

	public void setDesc(String str) {
		if (StringUtil.isNull(str))
			ViewUtil.setGone(widget, R.id.desc);
		else {
			ViewUtil.setVisible(widget, R.id.desc);
			ViewUtil.setRichText(widget, R.id.desc, str, true);
		}
	}

	public void setGone() {
		ViewUtil.setGone(widget);
	}

	public void setVisible() {
		ViewUtil.setVisible(widget);
	}
}
