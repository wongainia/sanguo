package com.vikings.sanguo.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.sound.MediaPlayerMgr;
import com.vikings.sanguo.ui.window.PopupWindow;
import com.vikings.sanguo.utils.ViewUtil;

@SuppressWarnings("deprecation")
public class Prologue extends PopupWindow {

	private ViewGroup window;

	private ViewGroup bg;

	private TextView text;

	private Interpolator linearInterpolator = new LinearInterpolator();

	private boolean debug = false;

	public void debug() {
		debug = true;
		doOpen();
	}

	private AlphaAnimation getShow() {
		AlphaAnimation hideShow = new AlphaAnimation(0f, 1f);
		hideShow.setDuration(1000);
		return hideShow;
	}

	private AlphaAnimation getTextShow() {
		AlphaAnimation hideShow = new AlphaAnimation(0f, 1f);
		hideShow.setDuration(500);
		return hideShow;
	}

	private AlphaAnimation getHide() {
		AlphaAnimation hideShow = new AlphaAnimation(1f, 0f);
		hideShow.setDuration(1000);
		return hideShow;
	}

	private ScaleAnimation getScaleOut() {
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				1f);
		scaleAnimation.setInterpolator(new OvershootInterpolator(2f));
		scaleAnimation.setDuration(300);
		return scaleAnimation;
	}

	private Animation getBattleAnim() {
		RotateAnimation r = new RotateAnimation(-5, 5,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				1f);
		r.setDuration(50);
		r.setRepeatMode(Animation.REVERSE);
		r.setRepeatCount(Animation.INFINITE);
		return r;
	}

	private Animation getDieAnim() {
		ScaleAnimation scaleAnimation = new ScaleAnimation(2f, 1f, 2f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(1000);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(scaleAnimation);
		set.addAnimation(getShow());
		set.setInterpolator(new AccelerateInterpolator(4f));
		return set;
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	private int[] textInterval = { 8000, 9000, 7000, 11000, 5000, 7000, 7000,
			7000, 2000, 5000 };

	private String[] textStr = { "东汉末年，天下大乱，群雄并起，逐鹿中原，美女们在战火中受到威胁",
			"为了平定乱世，一统中原，救出受战火威胁的美女们", "我广招精兵强将，实力大增，终成一方诸侯！",
			"每个想和我争夺美女的诸侯都是我的敌人，击败他们独揽天下美女！", "我的主城威武壮观，生活着万千的臣民，一起去看看吧！",
			"主城里，建筑井然有序，臣民们过着安详的生活…", "士兵们骁勇善战，美女们在红楼里等待着我的召唤…",
			"然而，黄巾军作乱，臣民们苦不堪言……", "战斗的号角已经响起，你准备好了吗！" };

	private String[] countryName = { "魏国", "蜀国", "蜀国", "魏国", "吴国", "魏国", "吴国",
			"魏国" };

	private int[][] countryPos = { { 230, 250 }, { 480, 480 }, { 665, 530 },
			{ 690, 270 }, { 775, 420 }, { 775, 100 }, { 880, 335 },
			{ 520, 305 } };
	private int[] countryInterval = { 1000, 3000, 1000, 1000, 1000, 1000, 1000,
			500 };

	private String[] tipName = { "民居", "酒馆", "国会", "募兵所" };
	private int[][] tipPos = { { 388, 544 }, { 514, 700 }, { 694, 484 },
			{ 758, 636 } };
	private int[] tipInterval = { 3000, 3000, 2000, 2000 };

	private Bitmap map;

	@Override
	protected void init() {
		MediaPlayerMgr.getInstance().startSound(R.raw.prologue);
		window = (ViewGroup) controller.inflate(R.layout.prologue);
		window.setBackgroundColor(0xFF000000);
		bg = (ViewGroup) window.findViewById(R.id.bg);
		text = (TextView) window.findViewById(R.id.text);
		controller.addContentFullScreen(window);
		controller.hideIconForFullScreen();

		// 初始化文本框位置
		LayoutParams l = (LayoutParams) text.getLayoutParams();
		l.width = Config.screenWidth;
		l.height = (int) (100 * Config.SCALE_FROM_HIGH);
		l.x = 0;
		l.y = Config.screenHeight - l.height;
		text.setLayoutParams(l);
		map = controller.getBitmap("prologue_map.jpg");
		ViewUtil.setGone(bg);
		window.findViewById(R.id.skip).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						controller.goBack();
					}
				});
	}

	@Override
	protected void destory() {
		map = null;
		castle_bg = null;
		controller.removeContentFullScreen(window);
		controller.showIconForFullScreen();
		MediaPlayerMgr.getInstance().stopSound();
		if (!debug)
			controller.openFirst();
	}

	@Override
	public void showUI() {
		super.showUI();
		doAnim();
	}

	private void doAnim() {
		changeText();
		window.postDelayed(new InnerRunner() {

			@Override
			public void doRun() {
				animInit();
			}
		}, 2000);
		startThread(new Runnable() {
			@Override
			public void run() {
				Account.readLog.PROLOGUE = true;
				Account.readLog.save();
			}
		});
	}

	// 初始化图片位置 处于中间
	private void animInit() {
		bg.setBackgroundDrawable(new BitmapDrawable(Config.getController()
				.getResources(), map));
		LayoutParams l = (LayoutParams) bg.getLayoutParams();
		l.width = Config.screenWidth;
		l.height = map.getHeight() * Config.screenWidth / map.getWidth();
		l.x = 0;
		l.y = Config.screenHeight - (l.height + map.getHeight()) / 2;
		// l.y = (Config.screenHeight - l.height)/2;
		bg.setLayoutParams(l);
		ViewUtil.setVisible(bg);
		bg.startAnimation(getShow());
		window.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				anim1();
			}
		}, 1000 + 500);
	}

	/**
	 * 放大
	 */
	private void anim1() {
		LayoutParams l = (LayoutParams) bg.getLayoutParams();
		ScaleAnimation scaleAnimation = new ScaleAnimation((float) l.width
				/ (float) map.getWidth(), 1f, (float) l.height
				/ (float) map.getHeight(), 1f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		l.width = map.getWidth();
		l.height = map.getHeight();
		l.x = 0;
		l.y = (Config.screenHeight - l.height);
		bg.setLayoutParams(l);
		scaleAnimation.setDuration(5000);
		bg.startAnimation(scaleAnimation);
		window.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				anim2();
			}
		}, scaleAnimation.getDuration());
	}

	/**
	 * 移动到最右
	 */
	private void anim2() {
		TranslateAnimation translateAnimation = new TranslateAnimation(0,
				-(map.getWidth() - Config.screenWidth), 0, 0);
		translateAnimation.setFillAfter(true);
		translateAnimation.setDuration(10000);
		translateAnimation.setInterpolator(linearInterpolator);
		bg.startAnimation(translateAnimation);
		window.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				LayoutParams l = (LayoutParams) bg.getLayoutParams();
				l.x = -(map.getWidth() - Config.screenWidth);
				bg.setLayoutParams(l);
				anim3();
			}
		}, translateAnimation.getDuration());
		addCountry(0);
	}

	/**
	 * 移回左边一些
	 */
	private void anim3() {
		TranslateAnimation translateAnimation = new TranslateAnimation(0,
				160 * Config.SCALE_FROM_HIGH, 0, 0);
		translateAnimation.setDuration(2000);
		translateAnimation.setFillAfter(true);
		translateAnimation.setInterpolator(linearInterpolator);
		bg.startAnimation(translateAnimation);
		window.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				anim4();
			}
		}, translateAnimation.getDuration() + 1000);
	}

	/**
	 * 国家消失
	 */
	private void anim4() {
		bg.clearAnimation();
		LayoutParams l = (LayoutParams) bg.getLayoutParams();
		l.x = -(map.getWidth() - Config.screenWidth)
				+ (int) (160 * Config.SCALE_FROM_HIGH);
		bg.setLayoutParams(l);
		AlphaAnimation hide = getHide();
		for (int i = 0; i < bg.getChildCount(); i++) {
			View v = bg.getChildAt(i);
			ViewUtil.setGone(v);
			v.startAnimation(hide);
		}
		bg.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				anim5();
			}
		}, 1500);
	}

	/**
	 * 出现城堡
	 */
	private void anim5() {
		bg.removeAllViews();
		addFief();
	}

	/**
	 * 战斗标志闪烁 灭出现
	 */
	private void anim6() {
		for (int i = 0; i < 3; i++) {
			ViewUtil.setHide(enemy[i], R.id.name);
			View v = enemy[i].findViewById(R.id.stat);
			ViewUtil.setVisible(v);
			v.startAnimation(getBattleAnim());
			final int index = i;
			bg.postDelayed(new InnerRunner() {
				@Override
				public void doRun() {
					View v = enemy[index].findViewById(R.id.stat);
					v.clearAnimation();
					ViewUtil.setGone(v);
					v = enemy[index].findViewById(R.id.die);
					ViewUtil.setVisible(v);
					v.startAnimation(getDieAnim());
				}
			}, 2000 + i * 500);
		}
		bg.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				anim7();
			}
		}, 6000 + 1000);
	}

	private ImageView arrowView;

	private OnClickListener arrowViewL = new OnClickListener() {

		@Override
		public void onClick(View v) {
			anim8();
		}
	};

	/**
	 * 主城手指
	 */
	private void anim7() {
		// int down = 3;
		// Bitmap bitmap = controller.getBitmap("oblique_arrow_right");
		// Bitmap temp = controller.getBitmapCache().get("down" + down);
		// if (temp == null) {
		// Matrix matrix = new Matrix();
		// matrix.postRotate(45f * down);
		// bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
		// bitmap.getHeight(), matrix, true);
		// controller.getBitmapCache().save("down" + down, bitmap);
		// } else {
		// bitmap = temp;
		// }
		// arrowView = new ImageView(controller.getUIContext());
		// arrowView.setBackgroundDrawable(new BitmapDrawable(controller
		// .getResources(), bitmap));
		// LayoutParams c = (LayoutParams) castle.getLayoutParams();
		// LayoutParams l = new
		// LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT, c.x + castle.getWidth()
		// / 2 - bitmap.getWidth() / 2, c.y);
		// window.addView(arrowView, l);
		// TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,
		// -40 * Config.SCALE_FROM_HIGH, 0);
		// translateAnimation.setDuration(800);
		// translateAnimation.setRepeatCount(Animation.INFINITE);
		// translateAnimation.setRepeatMode(Animation.REVERSE);
		// arrowView.startAnimation(translateAnimation);
		// castle.setOnClickListener(arrowViewL);
		bg.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				anim8();
			}
		}, 3000);
	}

	private Bitmap castle_bg;

	private boolean anim8 = false;

	private void anim8() {
		if (anim8)
			return;
		anim8 = true;
		while (window.getChildCount() > 3) {
			window.getChildAt(3).clearAnimation();
			window.removeViewAt(3);
		}
		ViewUtil.setHide(bg);

		bg.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				// 进入主城
				castle_bg = Config.getController().getBitmap("castle_bg.jpg");
				LayoutParams para = (LayoutParams) bg.getLayoutParams();
				para.height = castle_bg.getHeight();
				para.width = castle_bg.getWidth();
				para.y = -castle_bg.getHeight() + Config.screenHeight;
				para.x = 0;
				bg.setLayoutParams(para);
				bg.setBackgroundDrawable(new BitmapDrawable(Config
						.getController().getResources(), castle_bg));
				ViewUtil.setVisible(bg);
				bg.startAnimation(getShow());
				bg.postDelayed(new InnerRunner() {
					@Override
					public void doRun() {
						anim9();
					}
				}, 1000);
			}
		}, 200);

	}

	private void anim9() {
		TranslateAnimation translateAnimation = new TranslateAnimation(0,
				-(castle_bg.getWidth() - Config.screenWidth), 0, 0);
		translateAnimation.setFillAfter(true);
		translateAnimation.setDuration(15714);
		translateAnimation.setInterpolator(linearInterpolator);
		bg.startAnimation(translateAnimation);
		window.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				LayoutParams l = (LayoutParams) bg.getLayoutParams();
				l.x = -(castle_bg.getWidth() - Config.screenWidth);
				bg.setLayoutParams(l);
				anim10();
			}
		}, translateAnimation.getDuration());
		addTip(0);
	}

	private void anim10() {
		TranslateAnimation translateAnimation = new TranslateAnimation(0,
				castle_bg.getWidth() / 2 - Config.screenWidth / 2, 0, 0);
		translateAnimation.setDuration(15714 / 2);
		translateAnimation.setFillAfter(true);
		translateAnimation.setInterpolator(linearInterpolator);
		bg.startAnimation(translateAnimation);
		bg.postDelayed(new InnerRunner() {

			@Override
			public void doRun() {
				animEnd();
			}
		}, translateAnimation.getDuration());
	}

	private void animEnd() {
		window.startAnimation(getHide());
		bg.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				controller.goBack();
			}
		}, 1000);
	}

	private void addView(View v, int x, int y) {
		LayoutParams l = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, x, y);
		bg.addView(v, l);
	}

	private void addFief(View v, int x, int y) {
		y = y + 140;
		x = (int) (x * Config.SCALE_FROM_HIGH
				- (map.getWidth() - Config.screenWidth) + (160 * Config.SCALE_FROM_HIGH));
		y = (int) (y * Config.SCALE_FROM_HIGH + Config.screenHeight - map
				.getHeight());
		LayoutParams l = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, x, y);
		window.addView(v, l);
	}

	private View[] enemy = new View[3];
	private View castle;

	private void addFief() {
		View v = controller.inflate(R.layout.prologue_fief);
		ViewUtil.setImage(v, R.id.img, R.drawable.prologue_castle);
		ViewUtil.setText(v, R.id.name, "我的城池");
		addFief(v, 730, 365);
		castle = v;
		v.startAnimation(getScaleOut());

		int[][] pos = { { 585, 295 }, { 665, 490 }, { 880, 300 } };
		for (int i = 0; i < 3; i++) {
			v = controller.inflate(R.layout.prologue_fief);
			enemy[i] = v;
			ViewUtil.setImage(v, R.id.img, R.drawable.prologue_enemy);
			((TextView) v.findViewById(R.id.name)).setTextColor(Color.RED);
			ViewUtil.setText(v, R.id.name, "敌人");
			addFief(v, pos[i][0], pos[i][1]);
			ViewUtil.setHide(v);
		}
		bg.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				for (int i = 0; i < 3; i++) {
					ViewUtil.setVisible(enemy[i]);
					enemy[i].startAnimation(getScaleOut());
				}
				bg.postDelayed(new InnerRunner() {

					@Override
					public void doRun() {
						anim6();
					}
				}, 2000);
			}
		}, 5000);
	}

	private void addCountry(final int index) {
		if (index >= countryInterval.length)
			return;
		bg.postDelayed(new InnerRunner() {
			@Override
			public void doRun() {
				TextView v = (TextView) controller
						.inflate(R.layout.prologue_country);
				v.setText(countryName[index]);
				addView(v,
						(int) (countryPos[index][0] * Config.SCALE_FROM_HIGH),
						(int) ((countryPos[index][1] + 180) * Config.SCALE_FROM_HIGH));
				v.startAnimation(getShow());
				addCountry(index + 1);
			}
		}, countryInterval[index]);
	}

	private void addTip(final int index) {

		// if (index >= tipInterval.length)
		// return;
		// bg.postDelayed(new InnerRunner() {
		// @Override
		// public void doRun() {
		// TextView v = (TextView) controller
		// .inflate(R.layout.prologue_tip);
		// v.setText(tipName[index]);
		// addView(v, (int) (tipPos[index][0] * Config.SCALE_FROM_HIGH),
		// (int) (tipPos[index][1] * Config.SCALE_FROM_HIGH));
		// v.startAnimation(getScaleOut());
		// addTip(index + 1);
		// }
		// }, tipInterval[index]);
	}

	private void changeText() {
		int index = -1;
		if (text.getTag() != null) {
			index = (Integer) text.getTag();
		}
		if (index >= textStr.length - 1)
			return;
		text.startAnimation(getTextShow());
		index = index + 1;
		text.setText(textStr[index]);
		text.setTag(index);
		text.postDelayed(new InnerRunner() {

			@Override
			public void doRun() {
				changeText();
			}
		}, textInterval[index]);
	}

	private abstract class InnerRunner implements Runnable {

		@Override
		public void run() {
			if (Prologue.this.isShow())
				doRun();
		}

		public abstract void doRun();

	}

}
