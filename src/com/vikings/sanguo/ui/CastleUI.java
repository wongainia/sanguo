package com.vikings.sanguo.ui;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ManorLocation;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.thread.ViewImgGrayCallBack;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;

/**
 * 收获动画的版本，后来策划又要干掉主动收获
 * 
 * @author chenqing
 * 
 */
@SuppressWarnings("deprecation")
public class CastleUI implements DragLayout.OnClickListener,
		DragLayout.OnMoveListener, DragLayout.OnTouchDownListener {

	public static Bitmap bitmap = Config.getController().getBitmap(
			"castle_bg.jpg");

	private ManorInfoClient manorInfoClient;

	private int screen_h;

	private int screen_w;

	private AbsoluteLayout bg;

	private int min_x;

	private int max_x;

	private BuildingClickListener buildingClickListener;

	private CallBack callBack;

	private boolean self;

	// private View light, blink;

	// private Animation animLight, animBlink;

	private boolean weak = false;

	public CastleUI(DragLayout content,
			BuildingClickListener buildingClickListener, CallBack callBack,
			boolean self) {
		this.self = self;
		this.buildingClickListener = buildingClickListener;
		this.callBack = callBack;
		bg = (AbsoluteLayout) content.findViewById(R.id.bg);
		bg.setBackgroundDrawable(new BitmapDrawable(Config.getController()
				.getResources(), bitmap));

		// light = content.findViewById(R.id.light);
		// blink = content.findViewById(R.id.blink);

		content.setOnClick(this);
		content.setOnMove(this);
		content.setOnTouchDown(this);

		screen_h = Config.screenHeight;
		screen_w = Config.screenWidth;

		min_x = -bitmap.getWidth() + screen_w;
		max_x = 0;

		android.widget.AbsoluteLayout.LayoutParams para = (android.widget.AbsoluteLayout.LayoutParams) bg
				.getLayoutParams();
		para.height = bitmap.getHeight();
		para.width = bitmap.getWidth();
		para.y = -bitmap.getHeight() + screen_h;
		bg.setLayoutParams(para);
		moveTo(bitmap.getWidth() / 2);
		// if (self) {
		// getLightAnim();
		// getBlinkAnim();
		// }
	}

	private FrameLayout getConvertView(int index) {
		if (index < bg.getChildCount()) {
			FrameLayout v = (FrameLayout) bg.getChildAt(index);
			// v.removeAllViews();
			return v;
		} else {
			FrameLayout vg = new FrameLayout(Config.getController()
					.getUIContext());
			bg.addView(vg);
			return vg;
		}
	}

	public void firstBuy(BuildingInfoClient b, CallBack call) {
		if (!CacheMgr.manorLocationCache.contains(b.getProp().getType()))
			return;
		moveToBuilding(b.getProp().getType());
		View v = null;
		for (int i = 0; i < bg.getChildCount(); i++) {
			if (b.equals(bg.getChildAt(i).getTag())) {
				v = bg.getChildAt(i);
				break;
			}
		}
		if (v == null)
			return;
		// 动画
		// buyAnim(v, call);
	}

	public void moveToBuilding(int buildingType) {
		ManorLocation ml;
		try {
			ml = (ManorLocation) CacheMgr.manorLocationCache.get(buildingType);
		} catch (GameException e) {
			return;
		}
		moveTo((int) (ml.getClickRect().left * Config.SCALE_FROM_HIGH)
				+ (int) (ml.getClickRect().width() * Config.SCALE_FROM_HIGH / 2));
	}

	// private void buyAnim(final View v, final CallBack call) {
	// ViewUtil.setHide(v);
	// android.widget.AbsoluteLayout.LayoutParams para =
	// (android.widget.AbsoluteLayout.LayoutParams) v
	// .getLayoutParams();
	// int x1, y1;
	// if (v.getBackground() != null) {
	// x1 = para.x + v.getBackground().getIntrinsicWidth() / 2
	// - light.getBackground().getIntrinsicWidth() / 2;
	// y1 = para.y + v.getBackground().getIntrinsicHeight() / 2
	// - light.getBackground().getIntrinsicHeight() / 2;
	// } else {
	// x1 = para.x + para.width / 2
	// - light.getBackground().getIntrinsicWidth() / 2;
	// y1 = para.y + para.height / 2
	// - light.getBackground().getIntrinsicHeight() / 2;
	// }
	// x1 = x1
	// + ((android.widget.AbsoluteLayout.LayoutParams) bg
	// .getLayoutParams()).x;
	// y1 = y1
	// + ((android.widget.AbsoluteLayout.LayoutParams) bg
	// .getLayoutParams()).y;
	// android.widget.AbsoluteLayout.LayoutParams lightL =
	// (android.widget.AbsoluteLayout.LayoutParams) light
	// .getLayoutParams();
	// lightL.x = x1;
	// lightL.y = y1;
	// lightL.width = LayoutParams.WRAP_CONTENT;
	// lightL.height = LayoutParams.WRAP_CONTENT;
	// light.setLayoutParams(lightL);
	//
	// int x2, y2;
	// if (v.getBackground() != null) {
	// x2 = para.x + v.getBackground().getIntrinsicWidth() / 2
	// - blink.getBackground().getIntrinsicWidth() / 2;
	// y2 = para.y + v.getBackground().getIntrinsicHeight() / 2
	// - blink.getBackground().getIntrinsicHeight() / 2;
	// } else {
	// x2 = para.x + para.width / 2
	// - blink.getBackground().getIntrinsicWidth() / 2;
	// y2 = para.y + para.height / 2
	// - blink.getBackground().getIntrinsicHeight() / 2;
	// }
	// x2 = x2
	// + ((android.widget.AbsoluteLayout.LayoutParams) bg
	// .getLayoutParams()).x;
	// y2 = y2
	// + ((android.widget.AbsoluteLayout.LayoutParams) bg
	// .getLayoutParams()).y;
	// android.widget.AbsoluteLayout.LayoutParams blinkL =
	// (android.widget.AbsoluteLayout.LayoutParams) blink
	// .getLayoutParams();
	// blinkL.x = x2;
	// blinkL.y = y2;
	// blinkL.width = LayoutParams.WRAP_CONTENT;
	// blinkL.height = LayoutParams.WRAP_CONTENT;
	// blink.setLayoutParams(blinkL);
	//
	// ViewUtil.setVisible(light);
	// light.startAnimation(getLightAnim());
	// bg.postDelayed(new Runnable() {
	//
	// @Override
	// public void run() {
	// ViewUtil.setVisible(v);
	// v.setAnimation(getShowAnim());
	// ViewUtil.setVisible(blink);
	// blink.startAnimation(getBlinkAnim());
	// }
	// }, 200);
	// bg.postDelayed(new Runnable() {
	//
	// @Override
	// public void run() {
	// light.clearAnimation();
	// ViewUtil.setHide(light);
	// blink.clearAnimation();
	// ViewUtil.setHide(blink);
	// if (call != null)
	// call.onCall();
	// }
	// }, 900);
	// }

	// private Animation getLightAnim() {
	// if (animLight == null) {
	// ScaleAnimation scaleAnimation = new ScaleAnimation(2f, 1f, 2f, 1f,
	// Animation.RELATIVE_TO_SELF, 0.5f,
	// Animation.RELATIVE_TO_SELF, 0.5f);
	// scaleAnimation.setDuration(600);
	// RotateAnimation r = new RotateAnimation(0, 1080,
	// Animation.RELATIVE_TO_SELF, 0.5f,
	// Animation.RELATIVE_TO_SELF, 0.5f);
	// r.setDuration(900);
	// AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
	// alpha.setStartOffset(600);
	// alpha.setDuration(300);
	// AnimationSet set = new AnimationSet(true);
	// set.addAnimation(scaleAnimation);
	// set.addAnimation(r);
	// set.addAnimation(alpha);
	// animLight = set;
	// }
	// return animLight;
	// }
	//
	// private Animation getBlinkAnim() {
	// if (animBlink == null) {
	// TranslateAnimation translateAnimation = new TranslateAnimation(0,
	// 0, 0, -400 * Config.SCALE_FROM_HIGH);
	// translateAnimation.setDuration(700);
	// animBlink = translateAnimation;
	// animBlink.setFillAfter(true);
	// }
	// return animBlink;
	// }

	private Animation getShowAnim() {
		AlphaAnimation hideShow = new AlphaAnimation(0f, 1f);
		hideShow.setDuration(400);
		return hideShow;
	}

	// 2倍加速提醒
	private void addResTip(BuildingInfoClient b, View v) {
		if (b.getResSpeedupTime() > 1) {
			ImageView img = new ImageView(Config.getController().getUIContext());
			ViewUtil.setImage(img, R.drawable.res_speed_up);
			FrameLayout f = (FrameLayout) v;
			f.addView(img, new FrameLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
					Gravity.CENTER));
		}
	}

	// // 在主城中画View
	// public View getGuidView(int tagX, int tagY, int resid) {
	// FrameLayout woodView = getConvertView(90);
	// android.widget.AbsoluteLayout.LayoutParams para =
	// (android.widget.AbsoluteLayout.LayoutParams) woodView
	// .getLayoutParams();
	// para.width = LayoutParams.WRAP_CONTENT;
	// para.height = LayoutParams.WRAP_CONTENT;
	// para.x = (int) (tagX * Config.SCALE_FROM_HIGH);
	// para.y = (int) (tagY * Config.SCALE_FROM_HIGH);
	// woodView.setLayoutParams(para);
	// woodView.setBackgroundResource(resid);
	// return woodView;
	// }

	public void update(ManorInfoClient manorInfoClient) {
		this.manorInfoClient = manorInfoClient;
		List<ManorLocation> list = CacheMgr.manorLocationCache.getAll();

		// 在主城中挂牌
		for (int i = 0; i < list.size(); i++) {
			ManorLocation ml = list.get(i);
			BuildingInfoClient bic = manorInfoClient.getBuilding(ml.getType());

			if (bic == null) {
				continue;
			}
			FrameLayout woodView = getConvertView(i);
			android.widget.AbsoluteLayout.LayoutParams para = (android.widget.AbsoluteLayout.LayoutParams) woodView
					.getLayoutParams();
			if (ml.isShowWood() && bic.canHungLicense(ml)) {
				para.width = LayoutParams.WRAP_CONTENT;
				para.height = LayoutParams.WRAP_CONTENT;
				para.x = (int) (ml.getTagX() * Config.SCALE_FROM_HIGH);
				para.y = (int) (ml.getTagY() * Config.SCALE_FROM_HIGH);
				woodView.setLayoutParams(para);
				new ViewImgCallBack(ml.getUnlockImg(),
						BuildingProp.getBuildingTypePic(ml.getType()), woodView);
			} else {
				woodView.setBackgroundDrawable(null);
			}
		}

		for (int i = 0; i < list.size(); i++) {
			ManorLocation ml = list.get(i);
			BuildingInfoClient bic = manorInfoClient.getBuilding(ml.getType());
			FrameLayout v = getConvertView(list.size() + i + 1);
			android.widget.AbsoluteLayout.LayoutParams para = (android.widget.AbsoluteLayout.LayoutParams) v
					.getLayoutParams();

			BuildingProp initProp = CacheMgr.buildingPropCache
					.getInitialBuildingByType(ml.getType());
			if (null == initProp)
				continue;
			if (ml.isShow()) {
				para.width = LayoutParams.WRAP_CONTENT;
				para.height = LayoutParams.WRAP_CONTENT;
				para.x = (int) (ml.getX() * Config.SCALE_FROM_HIGH);
				para.y = (int) (ml.getY() * Config.SCALE_FROM_HIGH);
				v.setLayoutParams(para);
				new ViewImgCallBack(ml.getUnlockImg(),
						BuildingProp.getBuildingTypePic(ml.getType()), v);
				if (weak)
					new ViewImgGrayCallBack(initProp.getImage(), v);
				else
					new ViewImgCallBack(initProp.getImage(), v);
			} else {
				para.width = (int) (ml.getClickRect().width() * Config.SCALE_FROM_HIGH);
				para.height = (int) (ml.getClickRect().height() * Config.SCALE_FROM_HIGH);
				para.x = (int) (ml.getClickRect().left * Config.SCALE_FROM_HIGH);
				para.y = (int) (ml.getClickRect().top * Config.SCALE_FROM_HIGH);
				v.setBackgroundDrawable(null);
				v.setLayoutParams(para);
			}
		}
		// for (int i = manorInfoClient.getBuildingInfos().size(); i < bg
		// .getChildCount(); i++)
		// bg.removeViewAt(i);
	}

	// public void update(ManorInfoClient manorInfoClient) {
	// this.manorInfoClient = manorInfoClient;
	// try {
	// for (int i = 0; i < manorInfoClient.getBuildingInfos().size(); i++) {
	// BuildingInfoClient b = manorInfoClient.getBuildingInfos()
	// .get(i);
	// if (!CacheMgr.manorLocationCache
	// .contains(b.getProp().getType()))
	// continue;
	// ManorLocation ml = (ManorLocation) CacheMgr.manorLocationCache
	// .get(b.getProp().getType());
	// View v = getConvertView(i);
	// android.widget.AbsoluteLayout.LayoutParams para =
	// (android.widget.AbsoluteLayout.LayoutParams) v
	// .getLayoutParams();
	// if (ml.isShow()) {
	// para.width = LayoutParams.WRAP_CONTENT;
	// para.height = LayoutParams.WRAP_CONTENT;
	// para.x = (int) (ml.getX() * Config.SCALE_FROM_HIGH);
	// para.y = (int) (ml.getY() * Config.SCALE_FROM_HIGH);
	// v.setLayoutParams(para);
	// if (weak)
	// new ViewImgGrayCallBack(b.getProp().getImage(), v);
	// else
	// new ViewImgCallBack(b.getProp().getImage(), v);
	// } else {
	// para.width = (int) (ml.getClickRect().width() * Config.SCALE_FROM_HIGH);
	// para.height = (int) (ml.getClickRect().height() *
	// Config.SCALE_FROM_HIGH);
	// para.x = (int) (ml.getClickRect().left * Config.SCALE_FROM_HIGH);
	// para.y = (int) (ml.getClickRect().top * Config.SCALE_FROM_HIGH);
	// v.setBackgroundDrawable(null);
	// v.setLayoutParams(para);
	// }
	// v.setTag(b);
	// if (self)
	// addResTip(b, v);
	// }
	// for (int i = manorInfoClient.getBuildingInfos().size(); i < bg
	// .getChildCount(); i++)
	// bg.removeViewAt(i);
	// } catch (GameException e) {
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * 屏幕中心移动到背景图的x坐标
	 * 
	 * @param x
	 */
	public void moveTo(int x) {
		android.widget.AbsoluteLayout.LayoutParams para = (android.widget.AbsoluteLayout.LayoutParams) bg
				.getLayoutParams();
		para.x = screen_w / 2 - x;
		adjustLayout(para);
	}

	public void moveToCenter() {
		if (null == bitmap)
			return;
		moveTo(bitmap.getWidth() / 2);
	}

	private void adjustLayout(android.widget.AbsoluteLayout.LayoutParams para) {
		if (para.x > max_x)
			para.x = max_x;
		else if (para.x < min_x)
			para.x = min_x;
		bg.setLayoutParams(para);
	}

	@Override
	public void move(float x, float y) {
		android.widget.AbsoluteLayout.LayoutParams para = (android.widget.AbsoluteLayout.LayoutParams) bg
				.getLayoutParams();
		para.x = (int) (para.x + x);
		adjustLayout(para);
	}

	@Override
	public void click(float x, float y) {
		android.widget.AbsoluteLayout.LayoutParams para = (android.widget.AbsoluteLayout.LayoutParams) bg
				.getLayoutParams();
		int bg_x = (int) ((x - para.x) / Config.SCALE_FROM_HIGH);
		int bg_y = (int) ((y - para.y) / Config.SCALE_FROM_HIGH);
		BuildingInfoClient click = null;
		for (BuildingInfoClient b : manorInfoClient.getBuildingInfos()) {
			click = getClickBuilding(click, b, bg_x, bg_y);
		}

		if (click != null && buildingClickListener != null)
			buildingClickListener.click(click);

	}

	@Override
	public void touchDown() {
		if (null != callBack)
			callBack.onCall();
	}

	private BuildingInfoClient getClickBuilding(BuildingInfoClient click,
			BuildingInfoClient b, int bg_x, int bg_y) {
		if (!CacheMgr.manorLocationCache.contains(b.getProp().getType()))
			return click;
		try {
			ManorLocation ml = (ManorLocation) CacheMgr.manorLocationCache
					.get(b.getProp().getType());
			if (ml.getClickRect().contains(bg_x, bg_y)) {
				if (click == null
						|| (click != null && ((ManorLocation) CacheMgr.manorLocationCache
								.get(click.getProp().getType())).getZ() < ml
								.getZ()))
					return b;
			}
		} catch (GameException e) {
			e.printStackTrace();
		}
		return click;
	}

	public void setWeakState(boolean weakState) {
		if (weakState != weak) {
			if (weakState)
				setGray();
			else
				setNormal();
			weak = weakState;
		}
	}

	public void setGray() {
		if (null != bg)
			ImageUtil.setBgGray(bg);
		int count = bg.getChildCount();
		for (int i = 0; i < count; i++) {
			ImageUtil.setBgGray(bg.getChildAt(i));
		}
	}

	public void setNormal() {
		if (null != bg)
			ImageUtil.setBgNormal(bg);
		int count = bg.getChildCount();
		for (int i = 0; i < count; i++) {
			ImageUtil.setBgNormal(bg.getChildAt(i));
		}
	}

	public AbsoluteLayout getBg() {
		return bg;
	}

	public interface BuildingClickListener {

		void click(BuildingInfoClient b);

	}

	public Rect getBuildingTouchArea(int buildingId) {
		try {
			ManorLocation ml = (ManorLocation) CacheMgr.manorLocationCache
					.get(buildingId);
			android.widget.AbsoluteLayout.LayoutParams para = (android.widget.AbsoluteLayout.LayoutParams) bg
					.getLayoutParams();

			int l = (int) ((ml.getClickRect().left) * Config.SCALE_FROM_HIGH)
					+ para.x;
			int t = (int) ((ml.getClickRect().top) * Config.SCALE_FROM_HIGH)
					+ para.y;
			int r = (int) ((ml.getClickRect().right) * Config.SCALE_FROM_HIGH)
					+ para.x;
			int b = (int) ((ml.getClickRect().bottom) * Config.SCALE_FROM_HIGH)
					+ para.y;
			return new Rect(l, t, r, b);
		} catch (GameException e) {
			return new Rect();
		}
	}
}
