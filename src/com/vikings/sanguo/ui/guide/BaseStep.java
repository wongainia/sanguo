package com.vikings.sanguo.ui.guide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.controller.GameController;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.EndGuider;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.HeroIdBaseInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroInit;
import com.vikings.sanguo.model.ManorLocation;
import com.vikings.sanguo.ui.window.CastleWindow;
import com.vikings.sanguo.utils.GuidePosition;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

@SuppressWarnings("deprecation")
public abstract class BaseStep {
	private final static int TEXT_TIME = 500;

	public static final int ITEM_ID_SOUL_ANLUSHAN = 20241;

	public static final int HERO_ID_ANLUSHAN = 43001;
	public static final int HERO_ID_XISHI = 43017;
	public static final int HERO_ID_YUJI = 42002;

	public static final int DUNGEON_ID_1_5 = 11041;
	public static final int DUNGEON_ID_1_6 = 11051;
	public static final int DUNGEON_ID_1_7 = 11061;

	public static final int INDEX_STEP100 = 0;// 首次登录游戏引导打副本
	public static final int INDEX_STEP201 = 1;// 第四关战败红楼召将
	public static final int INDEX_STEP206 = 2;// 第四关战败红楼召将
	public static final int INDEX_STEP301 = 3;// 天降横财
	public static final int INDEX_STEP401 = 4;// 英雄殿 将领进化
	public static final int INDEX_STEP501 = 5;// 世界地图 第一部分引导
	public static final int INDEX_STEP503 = 6;// 世界地图 第二部分引导
	public static final int INDEX_STEP601 = 7;// 募兵所
	public static final int INDEX_STEP701 = 8;// 装备兵器
	public static final int INDEX_STEP801 = 9;// 无尽血战
	public static final int INDEX_STEP901 = 10;// 开放巅峰
	public static final int INDEX_STEP10001 = 11;// 战役副本 1-5关 引导
	public static final int INDEX_STEP11001 = 12;// 战役副本 1-7关 引导
	public static final int INDEX_STEP12001 = 13;// 战役副本 1-8关 引导
	public static final int INDEX_STEP13001 = 14;// 战役副本 1-4关 引导
	public static final int INDEX_STEP14001 = 15;// 战败充值指引 触发条件v0 1-8
	public static final int INDEX_STEP15001 = 16;// 战败充值指引 触发条件v1

	public static final int INDEX_STEP501_ENTER = 25;// 记录是否进入过副本第四关

	public static final int INDEX_FIRST_JOIN_GUILD = 61;
	public static final int ACT_CAMPAINGN_1_4 = 100104;// 战役副本 1-4关
	public static final int ACT_CAMPAINGN_1_5 = 100105;// 战役副本 1-5关
	public static final int ACT_CAMPAINGN_1_7 = 100107;// 战役副本 1-7关
	public static final int ACT_CAMPAINGN_1_8 = 100108;// 战役副本 1-8关

	// 第一次打副本1-6 和 1-7 主动上将领方案号 2101 、2102
	public static final int heroInit1 = 2101;
	public static final int heroInit2 = 2102;

	protected GameController ctr = Config.getController();

	protected ViewGroup window = (ViewGroup) ctr.findViewById(R.id.guideWindow);

	private View promptView;

	protected long time;

	protected static final int DOWN = 0;

	protected static final int LEFT = 1;

	protected static final int TOP = 2;

	protected static final int RIGHT = 3;

	protected Worker worker = new Worker();

	protected boolean isArrowShow = false;

	protected ImageView arrowView;

	protected TextView arrowText = (TextView) ctr.inflate(R.layout.arrow_text);

	protected ImageView cpUI = null;

	protected Handler handler = new Handler();

	private ImageView img;

	final static public int PADDING = 20;

	protected int waitTime = 200;

	public static boolean isStep501Enter() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP501_ENTER);
	}

	protected void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	// 在Step1001中，确保不是castle不显示
	protected boolean isSpecificWindow() {
		return true;
	}

	// 提示框
	public void initPromptView(String windowName) {
		window.setTag(windowName);
		window.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (promptView == null) {
					promptView = addSpecTip("请点击“"
							+ StringUtil.color((String) v.getTag(),
									R.color.color11) + "“");
				}
				if (promptView.getVisibility() == View.GONE) {
					ViewUtil.setVisible(promptView);
					promptView.startAnimation(getAnimation(promptView));
				}

			}
		});
	}

	// 画主城建筑 和 箭头
	public View cpGameUiByBuildingType(int buildingType) {
		View v;
		((CastleWindow) ctr.getCurPopupUI()).moveToBuilding(buildingType);

		int resID = getGuideResId(buildingType);
		GuidePosition gp = getGuidePosition(buildingType, resID);
		v = cpGameUI(gp.getVleft(), gp.getVTop(), gp.getWidth(), gp.getHight(),
				resID);

		addArrow(v, 3, 0, 0);
		return v;
	}

	// 根据heroInit 制造假将领
	public static HeroInfoClient getFakeGuideHero(int heroInit) {
		try {
			HeroInit mHeroInit = (HeroInit) CacheMgr.heroInitCache
					.get(heroInit);
			HeroInfoClient hic = new HeroInfoClient(122558,
					mHeroInit.getHeroId(), mHeroInit.getStar(),
					mHeroInit.getTalent());
			hic.setLevel(mHeroInit.getLevel());
			hic.setAttack(mHeroInit.getAttack());
			hic.setDefend(mHeroInit.getDefend());
			hic.setStamina(200);
			hic.setArmPropInfos(mHeroInit.getHeroArmProps());
			return hic;
		} catch (GameException e) {
			e.printStackTrace();
		}
		return HeroInfoClient.newInstance();
	}

	public static int getGuideResId(int buildingType) {
		int resID = 0;
		switch (buildingType) {
		case BuildingProp.BUILDING_TYPE_HERO_CENTER:
			resID = R.drawable.yingxiongdian;
			break;
		case BuildingProp.BUILDING_TYPE_ARM_ENROLL:
			resID = R.drawable.mubingsuo;
			break;
		case BuildingProp.BUILDING_TYPE_BAR:
			resID = R.drawable.honglou;
			break;
		default:
			break;
		}

		return resID;
	}

	public static GuidePosition getGuidePosition(int buildingType, int resId) {

		Drawable drawable = Config.getController().getDrawable(resId);
		Rect rect = Config.getController().getCastleWindow().getCastleUI()
				.getBuildingTouchArea(buildingType);

		GuidePosition gp = new GuidePosition(0, 0, 0, 0);
		switch (buildingType) {
		case BuildingProp.BUILDING_TYPE_HERO_CENTER:
			gp.setVleft(rect.left
					+ (rect.right - rect.left - drawable.getIntrinsicWidth())
					/ 2 + (int) (25 * Config.SCALE_FROM_HIGH));
			gp.setVTop((int) (rect.top - 40 * Config.SCALE_FROM_HIGH));
			break;
		case BuildingProp.BUILDING_TYPE_ARM_ENROLL:
			gp.setVleft(rect.left
					+ (rect.right - rect.left - drawable.getIntrinsicWidth())
					/ 2 + (int) (5 * Config.SCALE_FROM_HIGH));
			gp.setVTop((int) (rect.top - 10 * Config.SCALE_FROM_HIGH));
			break;
		case BuildingProp.BUILDING_TYPE_BAR:
			gp.setVleft(rect.left
					+ (rect.right - rect.left - drawable.getIntrinsicWidth())
					/ 2);
			gp.setVTop((int) (rect.top - 25 * Config.SCALE_FROM_HIGH));
			break;
		default:
			break;
		}
		gp.setWidth(drawable.getIntrinsicWidth());
		gp.setHight(drawable.getIntrinsicHeight());
		return gp;
	}

	protected void preSetWindowBackground() {
		window.setBackgroundColor(Config.getController().getResources()
				.getColor(R.color.guide_back));
	}

	protected void postSetWindowBackground() {
	}

	private Animation getAnimation(View view) {
		AnimationSet animationSet = new AnimationSet(false);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
				-0.5f);
		translateAnimation.setDuration(300);
		animationSet.addAnimation(translateAnimation);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
		alphaAnimation.setDuration(300);
		alphaAnimation.setStartOffset(600);
		animationSet.addAnimation(alphaAnimation);
		animationSet.setAnimationListener(new AnimaListener(view));
		return animationSet;
	}

	private class AnimaListener implements AnimationListener {
		View v;

		public AnimaListener(View v) {
			this.v = v;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			ViewUtil.setGone(v);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	public void run() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (!isSpecificWindow())
					return;

				preSetWindowBackground();
				ViewUtil.setVisible(window);
				if (window.getChildCount() > 0)
					window.removeAllViews();

				window.setOnClickListener(null);
				isArrowShow = false;
				setUI();
				playSound();
				time = System.currentTimeMillis();
				View v = getListenerView();
				if (v == null)
					v = window;
				v.setOnClickListener(getListener());
			}

		});
	}

	protected OnClickListener getListener() {
		return selViewL;
	}

	protected View findView(int id) {
		return ctr.getCurPopupUI().findViewById(id);
	}

	protected SelViewClickListener selViewL = new SelViewClickListener();

	protected class SelViewClickListener implements OnClickListener {
		private boolean clicked = false;

		@Override
		public void onClick(View v) {
			click();
		}

		public void click() {
			if (System.currentTimeMillis() - time < waitTime)
				return;
			if (invalidClick())
				return;
			if (clicked)
				return;
			clicked = true;
			isArrowShow = false;
			onDestory();
			postSetWindowBackground();
			clearImg();
			next();
			// 处理引导 界面回退问题
			onDirectQuit();
		}
	}

	protected boolean invalidClick() {
		return false;
	}

	protected void onDirectQuit() {
	}

	protected OnClickListener otherViewL = new OnClickListener() {

		private boolean clicked = false;

		@Override
		public void onClick(View v) {
			if (System.currentTimeMillis() - time < waitTime)
				return;
			if (clicked)
				return;
			clicked = true;
			isArrowShow = false;
			postSetWindowBackground();
			clearImg();
			next();
			onDirectQuit();
		}
	};

	private void clearImg() {
		if (window.getChildCount() > 0)
			window.removeAllViews();
		Bitmap bitmap = null;
		if (cpUI != null) {
			if (null == cpUI.getDrawable())
				bitmap = ((BitmapDrawable) cpUI.getBackground()).getBitmap();
			else
				bitmap = ((BitmapDrawable) cpUI.getDrawable()).getBitmap();
			if (null != bitmap)
				bitmap.recycle();
			bitmap = null;
			cpUI = null;
		}
	}

	protected void next() {
		BaseStep next = getNextStep();
		if (next == null) {
			quit();
		} else {
			next.run();
		}
	}

	protected void quit() {
		if (window.getChildCount() > 0)
			window.removeAllViews();
		window.setBackgroundColor(0x77000000);
		ViewUtil.setGone(window);
	}

	/**
	 * 播放声音，需要播放的子类重写该方法
	 */
	protected void playSound() {

	}

	protected View addHeroTip(int heroId, int star, String text) {
		View v = ctr.inflate(R.layout.guider_hero_tip);
		HeroIdBaseInfoClient hiic = getHero(heroId, star);
		if (null != hiic) {
			IconUtil.setHeroIcon(v.findViewById(R.id.iconLayout), hiic);
			ViewUtil.setRichText(v.findViewById(R.id.heroName),
					hiic.getColorTypeName() + hiic.getColorHeroName());
		}
		ViewUtil.setRichText(v.findViewById(R.id.tipText), text);
		addCenterUI(v, v.getBackground().getIntrinsicWidth(), v.getBackground()
				.getIntrinsicHeight());
		setWaitTime(1000);
		return v;
	}

	// 英雄speak 居中显示
	protected View addUICenterHeroSpeakTip(String text) {
		View v = Config.getController().inflate(R.layout.hero_guild_center);
		window.addView(v, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, 0, 0));
		ViewUtil.setRichText(v.findViewById(R.id.tipText), text);
		setWaitTime(1000);
		v.findViewById(R.id.name).startAnimation(getTextTipAnim());
		v.findViewById(R.id.tipText).startAnimation(getTextTipAnim());
		return v;
	}

	// 在指定高度加入 英雄speak
	protected View addUIHeroSpeakTip(String text, int bottom) {
		return addUIHeroSpeakTip(text);
	}

	// 在指定高度加入 英雄speak
	protected View addUIHeroSpeakTip(String text) {
		View v = Config.getController().inflate(R.layout.hero_guild_bottom);
		window.addView(v, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, 0, 0));

		ViewUtil.setRichText(v.findViewById(R.id.tipText), text);
		setWaitTime(1000);
		v.findViewById(R.id.name).startAnimation(getTextTipAnim());
		v.findViewById(R.id.tipText).startAnimation(getTextTipAnim());
		return v;
	}

	private Animation getTextTipAnim() {
		TranslateAnimation ta = new TranslateAnimation(-Config.screenWidth, 0,
				0, 0);
		ta.setDuration(TEXT_TIME);
		ta.setInterpolator(new AccelerateInterpolator());

		ta.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});

		return ta;
	}

	// 在固定高度加入 提示框
	protected TextView addSpecTip(String text) {
		TextView tv = new TextView(ctr.getUIContext());
		Drawable drawable = ctr.getDrawable(R.drawable.prompt_bg);
		ViewUtil.setImage(tv, drawable);
		tv.setGravity(Gravity.CENTER);
		ViewUtil.setRichText(tv, text);
		ViewUtil.setGone(tv);
		addUI(tv, (Config.screenWidth - drawable.getIntrinsicWidth()) / 2,
				(Config.screenHeight - drawable.getIntrinsicHeight()) / 2);
		setWaitTime(1000);
		return tv;
	}

	private HeroIdBaseInfoClient getHero(int heroId, int star) {
		try {
			return new HeroIdBaseInfoClient(0, heroId, star, 1);
		} catch (GameException e) {

		}
		return null;
	}

	/**
	 * 
	 * @param v
	 *            指向的View
	 * @param down
	 *            箭头指向的方向 0：右上，1：右， 2：右下，3：下，4：左下, 5:左， 6:左上， 7：上
	 * @param leftOffset
	 *            水平向右的偏移量
	 * @param topOffset
	 *            垂直向下的偏移量
	 */
	protected void addArrow(final View v, final int down, final int leftOffset,
			final int topOffset) {
		addArrow(v, down, leftOffset, topOffset, "");
	}

	protected void addArrow(final View v, final int down, final int leftOffset,
			final int topOffset, final String text) {

		handler.post(new Runnable() {
			@Override
			public void run() {
				int offset[] = new int[2];
				v.getLocationInWindow(offset);
				int left = offset[0];
				int top = offset[1];
				addArrow(left, top, v.getWidth(), v.getHeight(), down,
						leftOffset, topOffset, text);
			}
		});
	}

	protected void addArrow(final int vleft, final int vtop, final int width,
			final int height, final int down, final int leftOffset,
			final int topOffset, final String text) {

		handler.post(new Runnable() {
			@Override
			public void run() {
				set(vleft, vtop, width, height, down, leftOffset, topOffset,
						text);
			}

		});
	}

	protected void addArrow(final int vleft, final int vtop, final int width,
			final int height, final int down, final int leftOffset,
			final int topOffset) {

		handler.post(new Runnable() {
			@Override
			public void run() {
				set(vleft, vtop, width, height, down, leftOffset, topOffset, "");
			}

		});
	}

	private void set(final int vleft, final int vtop, final int width,
			final int height, final int down, final int leftOffset,
			final int topOffset, final String text) {
		Bitmap bitmap = ctr.getBitmap(R.drawable.oblique_arrow_right);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int left = vleft;
		int top = vtop;

		MyRect rect = null;
		if (!StringUtil.isNull(text))
			rect = new MyRect(text);

		// 图片对角线长度
		int diagonal = (int) Math.sqrt(w * w + h * h);
		switch (down) {
		case 0:
			left = left - w + (int) (leftOffset * Config.scaleRate);
			top = top + height + (int) (topOffset * Config.scaleRate);
			worker.setOffset(left, top, 10, -10);
			if (null != rect) {
				addArrowText(text, rect, left - rect.width() / 2, top + h
						+ (int) (10 * Config.scaleRate));
			}
			break;
		case 1:
			left = left - diagonal + (int) (leftOffset * Config.scaleRate);
			top = top - (diagonal / 2 - width / 2)
					+ (int) (topOffset * Config.scaleRate);
			worker.setOffset(left, top, 10, 0);
			if (null != rect) {
				addArrowText(text, rect, left - rect.width()
						- (int) (10 * Config.scaleRate),
						top + (h / 2 - rect.height() / 2));
			}
			break;
		case 2:
			left = left - w + (int) (leftOffset * Config.scaleRate);
			top = top - h + (int) (topOffset * Config.scaleRate);
			worker.setOffset(left, top, 10, 10);
			if (null != rect) {
				addArrowText(text, rect, left - rect.width() / 2,
						top - rect.height() - (int) (10 * Config.scaleRate));
			}
			break;
		case 3:
			left = left - (diagonal / 2 - width / 2)
					+ (int) (leftOffset * Config.scaleRate);
			top = top - diagonal + (int) (topOffset * Config.scaleRate);
			worker.setOffset(left, top, 0, 10);
			if (null != rect) {
				addArrowText(text, rect, left + (w / 2 - rect.width() / 2)
						+ (int) (8 * Config.scaleRate), top - rect.height()
						- (int) (10 * Config.scaleRate));
			}
			break;
		case 4:
			left = left + width + (int) (leftOffset * Config.scaleRate);
			top = top - h + (int) (topOffset * Config.scaleRate);
			worker.setOffset(left, top, -10, 10);
			if (null != rect) {
				addArrowText(text, rect, left + w - rect.width() / 2, top
						- rect.height() - (int) (10 * Config.scaleRate));
			}
			break;
		case 5:
			left = left + width + (int) (leftOffset * Config.scaleRate);
			top = top - (diagonal / 2 - height / 2)
					+ (int) (topOffset * Config.scaleRate);
			worker.setOffset(left, top, -10, 0);
			if (null != rect) {
				addArrowText(text, rect, left + w
						+ (int) (10 * Config.scaleRate),
						top + (h / 2 - rect.height() / 2));
			}
			break;
		case 6:
			left = left + width + (int) (leftOffset * Config.scaleRate);
			top = top + height + (int) (topOffset * Config.scaleRate);
			worker.setOffset(left, top, -10, -10);
			if (null != rect) {
				addArrowText(text, rect, left + w - rect.width() / 2, top + h
						+ (int) (10 * Config.scaleRate));
			}
			break;
		case 7:
			left = left - (diagonal / 2 - width / 2)
					+ (int) (leftOffset * Config.scaleRate);
			top = top + height + (int) (topOffset * Config.scaleRate);
			worker.setOffset(left, top, 0, -10);
			if (null != rect) {
				addArrowText(text, rect, left + (w / 2 - rect.width() / 2)
						+ (int) (8 * Config.scaleRate), top + h
						+ (int) (10 * Config.scaleRate));
			}
			break;
		}
		if (down != 0) {
			Bitmap temp = ctr.getBitmapCache().get("down" + down);
			if (temp == null) {
				Matrix matrix = new Matrix();
				matrix.postRotate(45f * down);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
				ctr.getBitmapCache().save("down" + down, bitmap);
			} else {
				bitmap = temp;
			}
		}
		arrowView = new ImageView(ctr.getUIContext());
		arrowView.setBackgroundDrawable(new BitmapDrawable(ctr.getResources(),
				bitmap));

		if (!isSpecificWindow()) {
			quit();
			return;
		}

		window.addView(arrowView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, left, top));
		arrowView.setTag(Boolean.TRUE);
		isArrowShow = true;
		refresh();
	}

	// 得到指定主城建筑位置 buildingType 在buildingProp中
	public ManorLocation getMLByBuildType(int buildingType) {
		ManorLocation mlLocation = null;
		try {
			mlLocation = (ManorLocation) CacheMgr.manorLocationCache
					.get(buildingType);
		} catch (GameException e) {
			// 指定建筑不存在
			Log.e("BaseStop", "buildingType:" + buildingType + "not found!");
			e.printStackTrace();
		}
		return mlLocation;
	}

	private void addArrowText(final String text, MyRect rect, int left, int top) {

		ViewUtil.setRichText(arrowText, text);
		if (text.length() <= 10)
			arrowText.setGravity(Gravity.CENTER_HORIZONTAL);
		else
			arrowText.setGravity(Gravity.LEFT);
		window.addView(arrowText, new LayoutParams(rect.width(),
				LayoutParams.WRAP_CONTENT, left, top));
	}

	private class MyRect {
		private String text;
		private int width;
		private int height;

		public MyRect(String text) {
			this.text = text;
			if (arrowText != null)
				init();
		}

		private void init() {
			Paint paint = arrowText.getPaint();
			String temp = "一二三四五六七八九十一二三四";
			if (text.length() <= 10)
				temp = temp.substring(0, text.length() + 1);
			else
				temp = temp.substring(0, 12);
			Rect rect = new Rect();
			paint.getTextBounds(temp, 0, temp.length(), rect);
			width = rect.width();
			int n = text.length() / 10;
			int m = text.length() % 10;
			height = rect.height() * (n == 0 ? 1 : n + (m == 0 ? 0 : 1));
		}

		public int width() {
			return width;
		}

		public int height() {
			return height;
		}

	}

	/**
	 * 游戏中的ui 在教程中相同位置重画
	 * 
	 * @param v
	 */
	protected ImageView cpGameUI(View v) {
		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		bitmap.setDensity(Config.densityDpi);
		Canvas canvas = new Canvas(bitmap);
		v.draw(canvas);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		img = new ImageView(ctr.getUIContext());
		img.setImageDrawable(new BitmapDrawable(ctr.getResources(), bitmap));
		img.setBackgroundDrawable(ctr.getDrawable(R.drawable.guide_sel_bg));
		img.setPadding(PADDING, PADDING, PADDING, PADDING);
		int[] offset = new int[2];
		v.getLocationInWindow(offset);

		int width = LayoutParams.WRAP_CONTENT;
		if (v.getWidth() + 2 * PADDING >= Config.screenWidth)
			width = v.getWidth() + 2 * PADDING;

		window.addView(img, new LayoutParams(width, LayoutParams.WRAP_CONTENT,
				offset[0] - PADDING, offset[1] - PADDING));
		this.cpUI = img;
		return img;
	}

	/**
	 * 游戏中的ui 在教程中相同位置重画
	 * 
	 * @param v
	 */
	protected ImageView cpGameUI(View v, int paddingBottom) {
		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		bitmap.setDensity(Config.densityDpi);
		Canvas canvas = new Canvas(bitmap);
		v.draw(canvas);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		img = new ImageView(ctr.getUIContext());
		img.setImageDrawable(new BitmapDrawable(ctr.getResources(), bitmap));
		img.setBackgroundDrawable(ctr.getDrawable(R.drawable.guide_sel_bg));
		img.setPadding(PADDING, PADDING, PADDING, PADDING + paddingBottom);
		int[] offset = new int[2];
		v.getLocationInWindow(offset);

		int width = LayoutParams.WRAP_CONTENT;
		if (v.getWidth() + 2 * PADDING >= Config.screenWidth)
			width = v.getWidth() + 2 * PADDING;

		window.addView(img, new LayoutParams(width, LayoutParams.WRAP_CONTENT,
				offset[0] - PADDING, offset[1] - PADDING));
		this.cpUI = img;
		return img;
	}

	/**
	 * 游戏中的ui 在教程中相同位置重画 vleft :vtop 视图坐标 vWidth ,vHeight 视图宽高 resID 需要贴的图
	 * 
	 * @param v
	 */
	protected ImageView cpGameUI(int vleft, int vtop, int vWidth, int vHeight,
			int resID) {
		Bitmap bitmap2 = BitmapFactory
				.decodeResource(ctr.getResources(), resID);
		bitmap2.setDensity(Config.densityDpi);

		img = new ImageView(ctr.getUIContext());
		img.setImageDrawable(new BitmapDrawable(ctr.getResources(), bitmap2));
		img.setBackgroundDrawable(ctr.getDrawable(R.drawable.guide_sel_bg));
		img.setPadding(PADDING, PADDING, PADDING, PADDING);

		int width = LayoutParams.WRAP_CONTENT;
		if (vWidth + 2 * PADDING >= Config.screenWidth)
			width = vWidth + 2 * PADDING;

		window.addView(img, new LayoutParams(width, LayoutParams.WRAP_CONTENT,
				vleft - PADDING, vtop - PADDING));
		this.cpUI = img;
		return img;
	}

	/**
	 * 游戏中的ui 在教程中相同位置重画 vleft :vtop 视图坐标 vWidth ,vHeight 视图宽高
	 * 
	 * @param v
	 */
	protected ImageView cpGameUI(int vleft, int vtop, int vWidth, int vHeight) {
		Bitmap bitmap = Bitmap.createBitmap(vWidth, vHeight,
				Bitmap.Config.ARGB_8888);
		bitmap.setDensity(Config.densityDpi);
		img = new ImageView(ctr.getUIContext());
		img.setImageDrawable(new BitmapDrawable(ctr.getResources(), bitmap));
		img.setBackgroundDrawable(ctr.getDrawable(R.drawable.guide_sel_bg));
		img.setPadding(PADDING, PADDING, PADDING, PADDING);

		int width = LayoutParams.WRAP_CONTENT;
		if (vWidth + 2 * PADDING >= Config.screenWidth)
			width = vWidth + 2 * PADDING;

		window.addView(img, new LayoutParams(width, LayoutParams.WRAP_CONTENT,
				vleft - PADDING, vtop - PADDING));
		this.cpUI = img;
		return img;
	}

	/**
	 * 在指定位置加入ui
	 * 
	 * @param v
	 * @param left
	 * @param top
	 */
	private void addUI(final View v, int left, int top) {
		window.addView(v, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, left, top));
	}

	/**
	 * 
	 * @param v
	 * @param centerHorizontal
	 *            是否水平居中
	 * @param centerVertical
	 *            是否垂直居中
	 * @param left
	 *            水平x坐标，centerHorizontal=false时有效
	 * @param top
	 *            垂直y坐标，centerVertical = false时有效
	 */
	private void addCenterUI(View v, int w, int h) {

		int pw = Config.screenWidth;
		int ph = Config.screenHeight;

		window.addView(v, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, (pw - w) / 2, (ph - h) / 2));

	}

	protected static void endGuider(int index) {
		new EndGuider(index).start();
	}

	/**
	 * 设置教程UI
	 */
	protected abstract void setUI();

	/**
	 * 响应点击事件的 ui
	 * 
	 * @return
	 */
	protected abstract View getListenerView();

	/**
	 * 本步骤结束
	 */
	protected abstract void onDestory();

	protected abstract BaseStep getNextStep();

	// 刷新箭头
	protected void refresh() {
		window.postDelayed(worker, 500);
	}

	protected class Worker implements Runnable {
		private int left;
		private int top;
		private int lo;
		private int to;

		public void setOffset(int left, int top, int leftOffset, int topOffset) {
			this.left = left;
			this.top = top;
			lo = leftOffset;
			to = topOffset;
		}

		@Override
		public void run() {
			if (arrowView == null || window.indexOfChild(arrowView) == -1)
				return;
			if ((Boolean) arrowView.getTag()) {
				arrowView.setTag(Boolean.FALSE);
				LayoutParams lp = (LayoutParams) arrowView.getLayoutParams();
				lp.x = left + (int) (lo * Config.scaleRate);
				lp.y = top + (int) (to * Config.scaleRate);
				arrowView.setLayoutParams(lp);
			} else {
				arrowView.setTag(Boolean.TRUE);
				LayoutParams lp = (LayoutParams) arrowView.getLayoutParams();
				lp.x = left;
				lp.y = top;
				arrowView.setLayoutParams(lp);
			}
			if (isArrowShow) {
				refresh();
			} else {
				if (arrowView != null)
					window.removeView(arrowView);
			}
		}
	}

	protected boolean isStepRunning() {
		return ViewUtil.isVisible(window);
	}
}
