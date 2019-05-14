package com.vikings.sanguo.utils;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BattleHeroInfoClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleLossDetail;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.ReturnHeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.TroopMoveInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.model.UserInfoHeadData;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.protos.ReturnAttrInfo;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.ScrollWebView;
import com.vikings.sanguo.ui.alert.BattleHeroUpdateSucTip;
import com.vikings.sanguo.ui.alert.MsgConfirm;
import com.vikings.sanguo.ui.listener.OtherHeroClickListener;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomProgressBar;

public class ViewUtil {

	public static byte LEFT_IN = 0;
	public static byte TOP_IN = 1;
	public static byte RIGHT_IN = 2;
	public static byte BOTTOM_IN = 3;

	static public void setText(View v, Object o) {
		if (v == null || o == null)
			return;
		if (!(v instanceof TextView))
			return;
		TextView t = (TextView) v;
		if (t == null || o == null)
			return;
		if (o instanceof String) {
			t.setText((String) o);
		} else
			t.setText(o.toString());

	}

	public static String getText(View parent, int viewId) {
		if (parent == null)
			return "";
		View v = parent.findViewById(viewId);
		if (v == null)
			return "";
		if (!(v instanceof TextView))
			return "";
		return ((TextView) v).getText().toString().trim();
	}

	static public void setText(View parent, int viewId, Object o) {
		if (parent == null)
			return;
		setText(parent.findViewById(viewId), o);
	}

	static public void setImage(View v, Object o) {
		if (v == null || o == null)
			return;
		if (o instanceof Integer)
			if (((Integer) o).intValue() != 0)
				v.setBackgroundDrawable(Config.getController().getDrawable(
						(Integer) o));
		if (o instanceof String)
			v.setBackgroundDrawable(Config.getController().getDrawable(
					(String) o));
		if (o instanceof Drawable)
			v.setBackgroundDrawable((Drawable) o);
		if (o instanceof Bitmap)
			v.setBackgroundDrawable(new BitmapDrawable(Config.getController()
					.getResources(), (Bitmap) o));
	}

	static public void setTextColor(View parent, int viewId, int colorId) {
		if (parent == null)
			return;
		setTextColor(parent.findViewById(viewId), colorId);
	}

	static public void setTextColor(View v, int colorId) {
		if (v == null || colorId == 0 || !(v instanceof TextView))
			return;
		((TextView) v).setTextColor(Config.getController().getResources()
				.getColor(colorId));
	}

	static public void setImage(View parent, int viewId, Object o) {
		if (parent == null)
			return;
		setImage(parent.findViewById(viewId), o);
	}

	// static public void setImageTest(View v, String name) {
	// v.setBackgroundDrawable(Config.getController().getDrawable(name));
	// }
	//
	// static public void removeImageTest(View v) {
	// Drawable d = v.getBackground();
	// if (d == null)
	// return;
	// v.setBackgroundDrawable(null);
	// }

	static public void fadeShow(View v) {
		v.startAnimation(AnimUtil.alphaShow);
		v.setVisibility(View.VISIBLE);
	}

	static public void fadeHide(View v) {
		v.startAnimation(AnimUtil.alphaHide);
		v.setVisibility(View.GONE);
	}

	static public void setVisible(View v) {
		if (v == null)
			return;
		v.setVisibility(View.VISIBLE);
	}

	static public void setGone(View v) {
		if (v == null)
			return;
		v.setVisibility(View.GONE);
	}

	static public void setHide(View v) {
		if (v == null)
			return;
		v.setVisibility(View.INVISIBLE);
	}

	static public void setVisible(View v, int resId) {
		View view = v.findViewById(resId);
		if (view == null)
			return;
		view.setVisibility(View.VISIBLE);
	}

	static public void setGone(View v, int resId) {
		View view = v.findViewById(resId);
		if (view == null)
			return;
		view.setVisibility(View.GONE);
	}

	static public void setHide(View v, int resId) {
		View view = v.findViewById(resId);
		if (view == null)
			return;
		view.setVisibility(View.INVISIBLE);
	}

	static public boolean isHidden(View v) {
		return v.getVisibility() == View.INVISIBLE;
	}

	static public boolean isVisible(View v) {
		return v.getVisibility() == View.VISIBLE;
	}

	static public void disableButton(View v) {
		ImageUtil.setBgGray(v);
		v.setEnabled(false);
	}

	static public void enableButton(View v) {
		ImageUtil.setBgNormal(v);
		v.setEnabled(true);
	}

	static public boolean isGone(View v) {
		if (v == null)
			return true;
		else
			return v.getVisibility() == View.GONE;
	}

	static public void toggleVisible(View v) {
		if (v.getVisibility() == View.VISIBLE) {
			v.setVisibility(View.GONE);
		} else
			v.setVisibility(View.VISIBLE);
	}

	static public void toggleVisible(View v1, View v2) {
		SoundMgr.play(R.raw.sfx_window_open);
		if (v1.getVisibility() == View.VISIBLE) {
			v1.setVisibility(View.GONE);
			v2.setVisibility(View.VISIBLE);
		} else {
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.GONE);
		}
	}

	public static void setOtherUserDetail(OtherUserClient other, View view) {
		setBriefUserDetail(other.bref(), view);
		setText(view, R.id.sign, other.getDesc());
	}

	public static void setUserDetail(UserAccountClient user, View view,
			int width, int height) {
		setBriefUserDetail(user.bref(), view, width, height);
		setUserDetailBtn(user, view);
	}

	public static void setUserDetail(UserAccountClient user, View view) {
		setBriefUserDetail(user.bref(), view);
		setUserDetailBtn(user, view);
	}

	private static void setUserDetailBtn(UserAccountClient user, View view) {
		setText(view, R.id.sign, user.getDesc());
		if (StringUtil.isNull(user.getEmail())) {
			setImage(view.findViewById(R.id.emailLayout), R.id.state,
					R.drawable.cha);
		} else {
			setImage(view.findViewById(R.id.emailLayout), R.id.state,
					R.drawable.gou);
		}

		if (StringUtil.isNull(user.getMobile())) {
			setImage(view.findViewById(R.id.phoneLayout), R.id.state,
					R.drawable.cha);
		} else {
			setImage(view.findViewById(R.id.phoneLayout), R.id.state,
					R.drawable.gou);
		}

		if (StringUtil.isNull(user.getIdCardNumber())) {
			setImage(view.findViewById(R.id.idCardLayout), R.id.state,
					R.drawable.cha);
		} else {
			setImage(view.findViewById(R.id.idCardLayout), R.id.state,
					R.drawable.gou);
		}
	}

	public static void setBriefUserDetail(BriefUserInfoClient briefUser,
			View view) {
		new UserIconCallBack(briefUser, view.findViewById(R.id.icon),
				// Config.SCALE_FROM_HIGH *
				Constants.ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ICON_HEIGHT * Config.SCALE_FROM_HIGH); // Config.SCALE_FROM_HIGH
																	// *
		setText(view, R.id.name, briefUser.getNickName());
		setText(view, R.id.userID, briefUser.getId());
		setText(view, R.id.gender,
				Config.arrayValue(briefUser.getSex(), Config.sex));
		setText(view, R.id.age, briefUser.getAge());
		setText(view, R.id.birthday, briefUser.getBirthdayStr());
	}

	public static void setBriefUserDetail(BriefUserInfoClient briefUser,
			View view, int width, int height) {
		new UserIconCallBack(briefUser, view.findViewById(R.id.icon), width,
				height);
		setText(view, R.id.name, briefUser.getNickName());
		setText(view, R.id.userID, briefUser.getId());
		setText(view, R.id.gender,
				Config.arrayValue(briefUser.getSex(), Config.sex));
		setText(view, R.id.age, briefUser.getAge());
		setText(view, R.id.birthday, briefUser.getBirthdayStr());
	}

	public static void selectTab(Button[] tabs, int[] bgIds, String[] text,
			int index, int[][] sizeColor) {
		for (int i = 0; i < tabs.length; i++) {
			if (i == index) {
				tabs[i].setBackgroundResource(bgIds[1]);
				tabs[i].setText(text[i]);
				tabs[i].setTextColor(sizeColor[0][0]);
				tabs[i].setTextSize(sizeColor[0][1]);
			} else {
				tabs[i].setBackgroundResource(bgIds[0]);
				tabs[i].setText(text[i]);
				tabs[i].setTextColor(sizeColor[1][0]);
				tabs[i].setTextSize(sizeColor[1][1]);
			}
		}
	}

	public static void setBtnMirrorBt(View view, String bgName) {
		Bitmap mirrorWinLt = Config.getController().getMirrorBitmap(bgName);
		ViewUtil.setImage(view, mirrorWinLt);
	}

	public static void selectTab(ImageButton[] tabs, int[] txtIds,
			int[] txtPressIds, int[] bgIds, int index) {
		for (int i = 0; i < tabs.length; i++) {
			if (i == index) {
				tabs[i].setBackgroundResource(bgIds[1]);
				tabs[i].setImageResource(txtPressIds[i]);

			} else {
				tabs[i].setBackgroundResource(bgIds[0]);
				tabs[i].setImageResource(txtIds[i]);
			}
			tabs[i].invalidate();
		}
	}

	public static void selectTab(ImageButton[] tabs, int[] resIds, int[] bgIds,
			int index, int[] offset_press, int[] offset) {
		selectTab(tabs, resIds, bgIds, index, offset_press, offset,
				ImageUtil.textHighLightFilter);
	}

	public static void selectTab(ImageButton[] tabs, int[] resIds, int[] bgIds,
			int index, int[] offset_press, int[] offset,
			ColorMatrixColorFilter cmcf) {
		for (int i = 0; i < tabs.length; i++) {
			tabs[i].setImageResource(resIds[i]);
			if (i == index) {
				tabs[i].setBackgroundResource(bgIds[1]);
				tabs[i].getDrawable().setColorFilter(cmcf);
				tabs[i].setPadding(offset_press[0], offset_press[1],
						offset_press[2], offset_press[3]);
			} else {
				tabs[i].setBackgroundResource(bgIds[0]);
				tabs[i].getDrawable().clearColorFilter();
				tabs[i].setPadding(offset[0], offset[1], offset[2], offset[3]);
			}
			tabs[i].invalidate();
		}
	}

	/**
	 * 设置文字描边
	 * 
	 * @param view
	 * @param radius
	 * @param color
	 */
	public static void shadowText(TextView view, int radius, int color) {
		if (null == view)
			return;
		view.setShadowLayer(radius, 0, 0, color);
	}

	public static void selectView(ViewGroup father, int resId) {
		int count = father.getChildCount();
		for (int i = 0; i < count; i++) {
			setGone(father.getChildAt(i));
		}
		setVisible(father, resId);
	}

	public static int indexOf(View[] views, View v) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == v)
				return i;
		}
		return -1;
	}

	private static void appendRichText(TextView v, String richText,
			boolean alignBaseline, int width, int height) {
		if (v == null || richText == null)
			return;
		StringBuilder buf = new StringBuilder(richText);
		StringBuilder strBuilder = new StringBuilder();
		while (true) {
			int start = buf.indexOf("#");
			// 判断是不是颜色
			if (start > 7) {
				String subString = buf.substring(start - 7, start);
				if ("color='".equals(subString)) {
					// v.append(Html.fromHtml(buf.substring(0, start + 1)));
					strBuilder.append(buf.substring(0, start + 1));
					buf.delete(0, start + 1);
					continue;
				}
			}
			int end = -1;
			if (start != -1)
				end = buf.indexOf("#", start + 1);
			// 有转义符
			if (start != -1 && end != -1) {
				// v.append(Html.fromHtml(buf.substring(0, start)));
				strBuilder.append(buf.substring(0, start));
				String img = buf.substring(start + 1, end);
				boolean zoom = true;
				if (img.length() > 0 && img.charAt(0) == '!') {
					zoom = false;
					img = img.substring(1, img.length());
				}
				String text = "#" + img + "#";
				Drawable d = Config.getController().getDrawable(img);
				if (d != null) {
					int w = d.getIntrinsicWidth();
					int h = d.getIntrinsicHeight();
					if (zoom) {

						if (width > 0 && height > 0) {
							if (w > width || h > height) {
								// 先按高度缩放
								int w1 = w * height / h;
								int h1 = height;
								if (w1 > width) {
									w1 = width;
									h1 = h * width / w;
								}
								width = w1;
								height = h1;
							}
							w = (int) (width * Config.SCALE_FROM_HIGH);
							h = (int) (height * Config.SCALE_FROM_HIGH);
						} else {
							if (w > 60 * Config.SCALE_FROM_HIGH) {
								h = (int) (h * (40 * Config.SCALE_FROM_HIGH) / w);
								w = (int) (40 * Config.SCALE_FROM_HIGH);
							}
						}
					}
					d.setBounds(0, 0, w, h);
					int param = ImageSpan.ALIGN_BOTTOM;
					if (alignBaseline)
						param = ImageSpan.ALIGN_BASELINE;
					ImageSpan span = new ImageSpan(d, param);
					SpannableString tmp = new SpannableString(text);
					tmp.setSpan(span, 0, text.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					v.append(Html.fromHtml(strBuilder.toString()));
					v.append(tmp);
					strBuilder.delete(0, strBuilder.length());
				}
				buf.delete(0, end + 1);
			}
			// 无转义符
			else {
				// v.append(Html.fromHtml(buf.toString()));
				strBuilder.append(buf.toString());
				break;
			}
		}
		v.append(Html.fromHtml(strBuilder.toString()));
	}

	public static void setRichText(View v, String richText, boolean center,
			int width, int height) {
		if (v == null || richText == null)
			return;
		TextView text = (TextView) v;
		text.setText("");
		appendRichText(text, richText, true, width, height);
	}

	public static void setRichText(View v, String richText, boolean center) {
		if (v == null || richText == null)
			return;
		TextView text = (TextView) v;
		text.setText("");
		appendRichText(text, richText, center, 0, 0);
	}

	static public void setRichText(View v, String richText) {
		setRichText(v, richText, false);
	}

	public static void setRichText(View parent, int viewId, String richText) {
		if (parent == null)
			return;
		setRichText(parent.findViewById(viewId), richText);
	}

	public static void setRichText(View parent, int viewId, String richText,
			boolean center) {
		if (parent == null)
			return;
		setRichText(parent.findViewById(viewId), richText, center);
	}

	/**
	 * 调整绝对布局大小 绝对布局-》绝对布局背景大图-》内容区，不超过minWidth(背景图，屏幕宽度) minHeight(背景图，屏幕高度)
	 * 
	 * @param tip
	 */
	@SuppressWarnings("deprecation")
	public static int fit3Layout(View root) {
		AbsoluteLayout bgContent = (AbsoluteLayout) ((ViewGroup) root)
				.getChildAt(0);
		View content = bgContent.getChildAt(0);
		int w = bgContent.getBackground().getIntrinsicWidth();
		int h = bgContent.getBackground().getIntrinsicHeight();
		// 调整背景大图 底部居中，高度大于屏幕高度则超出屏幕
		android.widget.AbsoluteLayout.LayoutParams para = (android.widget.AbsoluteLayout.LayoutParams) bgContent
				.getLayoutParams();
		para.x = (Config.screenWidth - w) / 2;
		para.y = Config.contentHeight - h;
		para.width = w;
		para.height = h;
		bgContent.setLayoutParams(para);
		// 调整内容区，宽度=大图宽度 高度不超过大图和屏幕高度
		para = (android.widget.AbsoluteLayout.LayoutParams) content
				.getLayoutParams();
		para.x = 0;
		para.y = Config.contentHeight > h ? 0 : h - Config.contentHeight;
		para.width = w;
		para.height = Config.contentHeight > h ? h : Config.contentHeight;
		content.setLayoutParams(para);
		return para.height;
	}

	// @SuppressWarnings("deprecation")
	// public static void fitScreenLayout(ViewGroup root) {
	// // 背景布局
	// LinearLayout bgContent = (LinearLayout) root.getChildAt(0);
	// // 上部分图片
	// View content1 = bgContent.getChildAt(0);
	// int w1 = content1.getBackground().getIntrinsicWidth();
	// int h1 = content1.getBackground().getIntrinsicHeight();
	// // 下部分图片
	// View content2 = bgContent.getChildAt(1);
	// int w2 = content2.getBackground().getIntrinsicWidth();
	// int h2 = content2.getBackground().getIntrinsicHeight();
	//
	// int w = w1 + w2;
	// int h = h1 + h2;
	// // 调整背景大图 底部居中，高度大于屏幕高度则超出屏幕
	// android.widget.AbsoluteLayout.LayoutParams para =
	// (android.widget.AbsoluteLayout.LayoutParams) bgContent
	// .getLayoutParams();
	// para.x = (Config.screenWidth - w) / 2;
	// para.y = Config.contentHeight - h;
	// para.width = w;
	// para.height = h;
	// bgContent.setLayoutParams(para);
	// }

	/**
	 * 用于Farm中背景适应屏幕大小
	 * 
	 * @param root
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static void fitScreenLayout(ViewGroup root) {
		ImageView bgImg = (ImageView) root.getChildAt(0);
		int w = bgImg.getBackground().getIntrinsicWidth();
		int h = bgImg.getBackground().getIntrinsicHeight();
		// 调整背景大图 底部居中，高度大于屏幕高度则超出屏幕
		android.widget.AbsoluteLayout.LayoutParams para = (android.widget.AbsoluteLayout.LayoutParams) bgImg
				.getLayoutParams();
		para.x = (Config.screenWidth - w) / 2;
		para.y = Config.contentHeight - h;
		para.width = w;
		para.height = h;
		bgImg.setLayoutParams(para);
	}

	public static void adjustLayout(View v, int width, int height, boolean force) {
		if (v == null || v.getBackground() == null)
			return;
		int w = v.getBackground().getIntrinsicWidth();
		int h = v.getBackground().getIntrinsicHeight();

		LayoutParams para = v.getLayoutParams();
		if (para == null) {
			para = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}

		if (w <= width && h <= height && !force) {
			para.width = w;
			para.height = h;
		} else {
			// 先按高度缩放
			int w1 = w * height / h;
			int h1 = height;
			if (w1 > width) {
				w1 = width;
				h1 = h * width / w;
			}
			para.width = w1;
			para.height = h1;
		}
		v.setLayoutParams(para);
	}

	public static void adjustLayout(View v, int width, int height) {
		adjustLayout(v, width, height, false);
	}

	public static void adjustLayoutAndReset(View v, int width, int height) {
		int w = v.getBackground().getIntrinsicWidth();
		int h = v.getBackground().getIntrinsicHeight();
		if (w <= width && h <= height) {
			LayoutParams para = v.getLayoutParams();
			if (para == null) {
				para = new LayoutParams(-2, -2);
			} else {
				para.width = -2;
				para.height = -2;
			}
			return;
		}

		LayoutParams para = v.getLayoutParams();
		if (para == null) {
			para = new LayoutParams(-2, -2);
		}
		// 先按高度缩放
		int w1 = w * height / h;
		int h1 = height;
		if (w1 > width) {
			w1 = width;
			h1 = h * width / w;
		}
		para.width = w1;
		para.height = h1;
		v.setLayoutParams(para);
	}

	public static void adjustLayout(View v, int scale) {
		int w = v.getBackground().getIntrinsicWidth();
		int h = v.getBackground().getIntrinsicHeight();

		LayoutParams para = v.getLayoutParams();
		if (para == null) {
			para = new LayoutParams(-2, -2);
		}

		para.width = w * scale / 100;
		para.height = h * scale / 100;
		v.setLayoutParams(para);
	}

	public static void adjustMargin(View v) {
		MarginLayoutParams mp = (MarginLayoutParams) v.getLayoutParams();
		mp.width = (int) (480 * Config.SCALE_FROM_HIGH);
		mp.height = (int) (640 * Config.SCALE_FROM_HIGH);
		mp.topMargin = Config.contentHeight - mp.height;
		v.setLayoutParams(mp);
	}

	/**
	 * 播放进入动画
	 */
	public static void startAnim(View v, byte inType) {
		if (v.isShown()) {
			int pos[] = new int[2];
			v.getLocationOnScreen(pos);
			TranslateAnimation trans = null;
			if (inType == LEFT_IN) {
				trans = new TranslateAnimation(0 - v.getWidth(), pos[0],
						pos[1], pos[1]);
			} else if (inType == TOP_IN) {
				trans = new TranslateAnimation(pos[0], pos[0],
						0 - v.getHeight(), pos[1]);
			} else if (inType == RIGHT_IN) {
				trans = new TranslateAnimation(Config.screenWidth, pos[0],
						0 - v.getHeight(), pos[1]);
			} else if (inType == BOTTOM_IN) {
				trans = new TranslateAnimation(pos[0], pos[0],
						Config.screenHeight, pos[1]);
			} else {
				return;
			}
			trans.setDuration(200);
			v.startAnimation(trans);
		}

	}

	public static View getItemLineView() {
		View view = Config.getController().inflate(R.layout.item_line);
		return view;
	}

	public static View getItemLineView(Object icon, String name, int count) {
		return getItemLineView(icon, name, String.valueOf(count));
	}

	public static View getItemLineView(Object icon, String name, String count) {
		View view = getItemLineView();
		if (icon instanceof String) {
			new ViewImgCallBack((String) icon, view.findViewById(R.id.itemIcon));
		} else {
			setImage(view, R.id.itemIcon, icon);
		}
		setRichText(view.findViewById(R.id.itemName), name);
		setRichText(view.findViewById(R.id.itemCount), count);
		return view;
	}

	public static void setViewWrapContent(View v) {
		if (null == v)
			return;
		LayoutParams paras = v.getLayoutParams();
		paras.width = LayoutParams.WRAP_CONTENT;
		paras.height = LayoutParams.WRAP_CONTENT;
		v.setLayoutParams(paras);
	}

	public static WebView getWebView(Context context) {
		WebView webView = new WebView(context);
		setWebView(webView);
		return webView;
	}

	public static WebView getScrollWebView(Context context) {
		ScrollWebView webView = new ScrollWebView(context);
		setWebView(webView);
		return webView;
	}

	public static void setWebView(WebView webView) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true); // webview支持javascript
		// webSettings.setAppCacheEnabled(false);
		// webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setBlockNetworkImage(false);
		// 设置缩放
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		webView.setHorizontalScrollBarEnabled(false);// 水平不显示
		webView.setVerticalScrollBarEnabled(false); // 垂直不显示

	}

	public static void setEditText(EditText et, String str) {
		if (null == et || null == str)
			return;
		et.setText(str);
		et.setSelection(str.length());
	}

	public static Bitmap viewToBitmap(View v) {
		v.setDrawingCacheEnabled(true);
		v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
		v.buildDrawingCache();

		return v.getDrawingCache();
	}

	public static Drawable getLayerDrawable(String topName, String botName) {
		Drawable[] layers = new Drawable[2];
		layers[0] = Config.getController().getDrawable(botName);
		;
		layers[1] = Config.getController().getDrawable(topName);
		int w1 = layers[0].getIntrinsicWidth();
		int h1 = layers[0].getIntrinsicHeight();
		int w2 = layers[1].getIntrinsicWidth();
		int h2 = layers[1].getIntrinsicHeight();
		LayerDrawable layer = new LayerDrawable(layers);
		layer.setLayerInset(0, 0, 0, 0, 0);
		layer.setLayerInset(1, ((w1 - w2) >> 1), ((h1 - h2) >> 1),
				((w1 - w2) >> 1), ((h1 - h2) >> 1));
		return layer;
	}

	public static Drawable getLayerDrawable(String topName, String botName,
			int bgScale) {
		Drawable[] layers = new Drawable[2];
		layers[0] = Config.getController().getDrawable(botName, bgScale);
		layers[1] = Config.getController().getDrawable(topName);
		int w1 = layers[0].getIntrinsicWidth();
		int h1 = layers[0].getIntrinsicHeight();
		int w2 = layers[1].getIntrinsicWidth();
		int h2 = layers[1].getIntrinsicHeight();
		LayerDrawable layer = new LayerDrawable(layers);
		layer.setLayerInset(0, 0, 0, 0, 0);
		layer.setLayerInset(1, ((w1 - w2) >> 1), ((h1 - h2) >> 1),
				((w1 - w2) >> 1), ((h1 - h2) >> 1));
		return layer;
	}

	public static Drawable getLayerDrawableRT(Drawable top, Drawable bot) {
		Drawable[] layers = new Drawable[2];
		layers[0] = bot;
		layers[1] = top;

		int w1 = layers[0].getIntrinsicWidth();
		int h1 = layers[0].getIntrinsicHeight();
		int w2 = layers[1].getIntrinsicWidth();
		int h2 = layers[1].getIntrinsicHeight();
		LayerDrawable layer = new LayerDrawable(layers);
		layer.setLayerInset(0, 0, 0, 0, 0);
		layer.setLayerInset(1, (w1 - w2), 0, 0, (h1 - h2)); // left += l top +=
															// t; right -= r;
															// bottom -= b;
		return layer;
	}

	public static ImageView createImageView() {
		ImageView iv = new ImageView(Config.getController().getUIContext());
		iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		return iv;
	}

	public static void setMarginLeft(View view, int x) {
		android.widget.FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) view
				.getLayoutParams();
		// LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.rightMargin = 0;
		lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
		view.setLayoutParams(lp);
	}

	public static void setMarginTop(View view, int x) {
		android.widget.FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) view
				.getLayoutParams();
		// LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		lp.topMargin = x;
		lp.bottomMargin = 0;
		lp.gravity = Gravity.LEFT | Gravity.TOP;
		view.setLayoutParams(lp);
	}

	public static void setMargin(View view, int l, int t, int r, int b,
			int grivity) {
		android.widget.FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) view
				.getLayoutParams();
		if (t != 0) {
			lp.topMargin = t;
		}
		if (l != 0) {
			lp.leftMargin = l;
		}
		if (r != 0) {
			lp.rightMargin = r;
		}
		if (b != 0) {
			lp.bottomMargin = b;
		}
		lp.gravity = grivity;
		view.setLayoutParams(lp);
	}

	public static void setBoldText(View v, Object o) {
		if (v == null || o == null)
			return;
		setText(v, o);

		if (v instanceof TextView)
			((TextView) v).getPaint().setFakeBoldText(true);
	}

	public static void setBoldRichText(View v, String richText) {
		setRichText(v, richText, true);

		if (v instanceof TextView)
			((TextView) v).getPaint().setFakeBoldText(true);
	}

	public static void setBold(TextView v) {
		if (null == v || null == v.getPaint())
			return;

		v.getPaint().setFakeBoldText(true);
	}

	public static void setUnderLine(TextView v) {
		if (null == v || null == v.getPaint())
			return;

		v.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}

	public static void setTextViewNormal(TextView v) {
		if (null == v || null == v.getPaint())
			return;

		v.getPaint().setFlags(0);
	}

	public static void setMiddleLine(TextView v) {
		if (null == v || null == v.getPaint())
			return;

		v.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
	}

	// 全角转半角
	public static String full2Half(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}

		return new String(c);
	}

	// 半角转全角
	public static final String half2Full(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;

			} else if (c[i] < 127) {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	public static void addView(ViewGroup vg, View v) {
		removeAllViews(vg);
		vg.addView(v);
	}

	public static void setUserInfoHeadAttrs(View view,
			List<UserInfoHeadData> datas, boolean showMaterial,
			UserAccountClient userAccountClient) {
		if (null == view || null == datas || null == userAccountClient)
			return;

		for (int i = 0; i < 4; i++) {
			if (i < datas.size()) {
				int resId = Config.getController().findResId("attr" + (i + 1),
						"id");
				View childView = view.findViewById(resId);
				if (null != childView) {
					ViewUtil.setVisible(childView);
					setUserInfoHeadAttr(childView, datas.get(i));
				}
			}
		}

		if (showMaterial) {
			View materialGroup = view.findViewById(R.id.materialGroup);
			if (null != materialGroup) {
				ViewUtil.setVisible(materialGroup);
				setUserRes(materialGroup, userAccountClient);
			}
		}

	}

	private static void setUserInfoHeadAttr(View view, UserInfoHeadData data) {
		if (null == view || null == data)
			return;

		switch (data.getType()) {
		case UserInfoHeadData.DATA_TYPE_MONEY:
			ViewUtil.setImage(view, R.id.icon, ReturnInfoClient
					.getAttrTypeIcon(AttrType.ATTR_TYPE_MONEY.getNumber()));
			ViewUtil.setText(
					view,
					R.id.name,
					ReturnInfoClient.getAttrTypeName(AttrType.ATTR_TYPE_MONEY
							.getNumber()) + "：");
			ViewUtil.setText(view, R.id.value, data.getValue());
			break;
		case UserInfoHeadData.DATA_TYPE_CURRENCY:
			ViewUtil.setImage(view, R.id.icon, ReturnInfoClient
					.getAttrTypeIcon(AttrType.ATTR_TYPE_CURRENCY.getNumber()));
			ViewUtil.setText(
					view,
					R.id.name,
					ReturnInfoClient
							.getAttrTypeName(AttrType.ATTR_TYPE_CURRENCY
									.getNumber())
							+ "：");
			ViewUtil.setText(view, R.id.value, data.getValue());
			break;
		case UserInfoHeadData.DATA_TYPE_FOOD:
			ViewUtil.setImage(view, R.id.icon, ReturnInfoClient
					.getAttrTypeIcon(AttrType.ATTR_TYPE_FOOD.getNumber()));
			ViewUtil.setText(
					view,
					R.id.name,
					ReturnInfoClient.getAttrTypeName(AttrType.ATTR_TYPE_FOOD
							.getNumber()) + "：");
			ViewUtil.setText(view, R.id.value, data.getValue());
			break;
		case UserInfoHeadData.DATA_TYPE_ARMY:
			ViewUtil.setImage(view, R.id.icon, R.drawable.arm);
			ViewUtil.setText(view, R.id.name, "兵力：");
			ViewUtil.setText(view, R.id.value, data.getValue());
			break;
		case UserInfoHeadData.DATA_TYPE_POP:
			ViewUtil.setImage(view, R.id.icon, R.drawable.ren);
			ViewUtil.setText(view, R.id.name, "人口：");
			ViewUtil.setText(view, R.id.value, data.getValue());
			break;
		case UserInfoHeadData.DATA_TYPE_EXPLOIT:
			ViewUtil.setImage(view, R.id.icon, ReturnInfoClient
					.getAttrTypeIcon(AttrType.ATTR_TYPE_EXPLOIT.getNumber()));
			ViewUtil.setText(
					view,
					R.id.name,
					ReturnInfoClient.getAttrTypeName(AttrType.ATTR_TYPE_EXPLOIT
							.getNumber()) + "：");
			ViewUtil.setText(view, R.id.value, data.getValue());
			break;
		case UserInfoHeadData.DATA_TYPE_HERO:
			ViewUtil.setImage(view, R.id.icon, R.drawable.hero_limit);
			ViewUtil.setText(view, R.id.name, "将领：");
			ViewUtil.setText(view, R.id.value, data.getValue());
			break;
		default:
			break;
		}
	}

	// 设置用户资源
	public static void setUserRes(View content, UserAccountClient user) {
		setResValue(
				content.findViewById(R.id.woodLayout),
				ReturnInfoClient.getAttrTypeIconName(AttrType.ATTR_TYPE_WOOD
						.getNumber()),
				ReturnInfoClient.getAttrTypeName(AttrType.ATTR_TYPE_WOOD
						.getNumber()) + "：", CalcUtil.turnToTenThousand(user
						.getWood()));

		setResValue(
				content.findViewById(R.id.ironLayout),
				ReturnInfoClient
						.getAttrTypeIconName(AttrType.ATTR_TYPE_MATERIAL_0
								.getNumber()),
				ReturnInfoClient.getAttrTypeName(AttrType.ATTR_TYPE_MATERIAL_0
						.getNumber()) + "：", CalcUtil.turnToTenThousand(user
						.getMaterial0()));

		setResValue(
				content.findViewById(R.id.leatherLayout),
				ReturnInfoClient
						.getAttrTypeIconName(AttrType.ATTR_TYPE_MATERIAL_1
								.getNumber()),
				ReturnInfoClient.getAttrTypeName(AttrType.ATTR_TYPE_MATERIAL_1
						.getNumber()) + "：", CalcUtil.turnToTenThousand(user
						.getMaterial1()));
	}

	private static void setResValue(View view, Object icon, String name,
			String value) {
		ViewUtil.setImage(view, R.id.icon, icon);
		ViewUtil.setText(view, R.id.name, name);
		ViewUtil.setText(view, R.id.value, value);
	}

	public static void setWidthFillParent(View v) {
		if (null == v || null == v.getLayoutParams())
			return;

		v.getLayoutParams().width = LayoutParams.FILL_PARENT;
		v.invalidate();
	}

	public static void setHeightFillParent(View v) {
		if (null == v || null == v.getLayoutParams())
			return;

		v.getLayoutParams().height = LayoutParams.FILL_PARENT;
		v.invalidate();
	}

	public static void addSkillImageView(ViewGroup skillView, String name) {
		ImageView imageView = new ImageView(Config.getController()
				.getUIContext());
		new ViewImgScaleCallBack(name, imageView, 20 * Config.SCALE_FROM_HIGH,
				20 * Config.SCALE_FROM_HIGH);
		skillView.addView(imageView);
	}

	public static void removeAllViews(ViewGroup vg) {
		if (null == vg)
			return;

		if (vg.getChildCount() > 0)
			vg.removeAllViews();
	}

	public static View getShowItemView(ShowItem si) {
		View v = Config.getController().inflate(R.layout.common_item_line);
		new ViewImgScaleCallBack(si.getImg(), v.findViewById(R.id.itemIcon),
				Constants.SMALL_ICON_WIDTH, Constants.SMALL_ICON_HEIGHT);
		ViewUtil.setText(v, R.id.itemName, si.getName());
		if (si.getCount() >= 0)
			ViewUtil.setText(v, R.id.itemCount, "×" + si.getCount());
		else
			ViewUtil.setText(v, R.id.itemCount, si.getCount());
		return v;
	}

	public static View getShowItemView(String icon, String name, String cnt,
			int colodId) {
		View v = Config.getController().inflate(R.layout.common_item_line);

		new ViewImgScaleCallBack(icon, v.findViewById(R.id.itemIcon),
				Constants.SMALL_ICON_WIDTH, Constants.SMALL_ICON_HEIGHT);

		ViewUtil.setRichText(v, R.id.itemName, StringUtil.color(name, colodId));

		ViewUtil.setRichText(v, R.id.itemCount, StringUtil.color(cnt, colodId));

		return v;
	}

	public static View getUsedShowItemView(String icon, String name,
			String cnt, int colodId, ViewGroup parent) {
		View v = Config.getController().inflate(R.layout.short_item_line,
				parent, false);

		new ViewImgScaleCallBack(icon, v.findViewById(R.id.itemIcon),
				Constants.SMALL_ICON_WIDTH, Constants.SMALL_ICON_HEIGHT);

		ViewUtil.setRichText(v, R.id.itemName, StringUtil.color(name, colodId));

		ViewUtil.setRichText(v, R.id.itemCount, StringUtil.color(cnt, colodId));

		return v;
	}

	public static View getShowItemView(ShowItem si, int color,
			boolean heroColor, boolean showQulity) {
		return getShowItemView(si,
				Config.getController().getResourceColorText(color), heroColor,
				showQulity);
	}

	public static View getShowItemView(ShowItem si, int color,
			boolean heroColor, boolean showQulity, int itemLayoutBgRes) {
		return getShowItemView(si,
				Config.getController().getResourceColorText(color), heroColor,
				showQulity, itemLayoutBgRes);
	}

	public static View getShowItemView(ShowItem si, String color,
			boolean heroColor, boolean showQulity, int itemLayoutBgRes) {
		View v = Config.getController().inflate(R.layout.common_item_line);
		android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
				-1, -2);
		lp.topMargin = (int) (2 * Config.scaleRate);
		lp.leftMargin = (int) (5 * Config.scaleRate);
		lp.rightMargin = (int) (5 * Config.scaleRate);
		v.setLayoutParams(lp);
		new ViewImgScaleCallBack(si.getImg(), v.findViewById(R.id.itemIcon),
				Constants.SMALL_ICON_WIDTH, Constants.SMALL_ICON_HEIGHT);
		TextView itemName = (TextView) v.findViewById(R.id.itemName);
		String temp = color;
		if (heroColor && !StringUtil.isNull(si.getParamStr0()))
			temp = si.getParamStr0();

		if (itemLayoutBgRes != 0) {
			ViewUtil.setImage(v.findViewById(R.id.shopitem_layout),
					itemLayoutBgRes);
		}

		if (showQulity && !StringUtil.isNull(si.getParamStr1()))
			ViewUtil.setRichText(itemName, StringUtil.color(si.getParamStr1()
					+ "  " + si.getName(), temp));
		else
			ViewUtil.setRichText(itemName, StringUtil.color(si.getName(), temp));

		if (si.getCount() >= 0)
			ViewUtil.setRichText(v, R.id.itemCount,
					StringUtil.color("×" + si.getCount(), color));
		else
			ViewUtil.setRichText(v, R.id.itemCount,
					StringUtil.color("" + si.getCount(), color));
		return v;
	}

	public static View getShowItemView(ShowItem si, String color,
			boolean heroColor, boolean showQulity) {
		View v = Config.getController().inflate(R.layout.common_item_line);
		android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
				-1, -2);
		lp.topMargin = (int) (2 * Config.scaleRate);
		lp.leftMargin = (int) (5 * Config.scaleRate);
		lp.rightMargin = (int) (5 * Config.scaleRate);
		v.setLayoutParams(lp);
		new ViewImgScaleCallBack(si.getImg(), v.findViewById(R.id.itemIcon),
				Constants.SMALL_ICON_WIDTH, Constants.SMALL_ICON_HEIGHT);
		TextView itemName = (TextView) v.findViewById(R.id.itemName);
		String temp = color;
		if (heroColor && !StringUtil.isNull(si.getParamStr0()))
			temp = si.getParamStr0();

		if (showQulity && !StringUtil.isNull(si.getParamStr1()))
			ViewUtil.setRichText(itemName, StringUtil.color(si.getParamStr1()
					+ "  " + si.getName(), temp));
		else
			ViewUtil.setRichText(itemName, StringUtil.color(si.getName(), temp));

		if (si.getCount() >= 0)
			ViewUtil.setRichText(v, R.id.itemCount,
					StringUtil.color("×" + si.getCount(), color));
		else
			ViewUtil.setRichText(v, R.id.itemCount,
					StringUtil.color("" + si.getCount(), color));
		return v;
	}

	public static View getShowRewardView(ShowItem si, int color,
			boolean heroColor, boolean showQulity) {
		return getShowItemView(si,
				Config.getController().getResourceColorText(color), heroColor,
				showQulity);
	}

	public static View getShowRewardView(ShowItem si, String color,
			boolean heroColor, boolean showQulity) {
		View v = Config.getController().inflate(R.layout.common_item_line);
		android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
				-1, -2);
		lp.topMargin = (int) (2 * Config.scaleRate);
		lp.leftMargin = (int) (5 * Config.scaleRate);
		lp.rightMargin = (int) (5 * Config.scaleRate);
		v.setLayoutParams(lp);
		new ViewImgScaleCallBack(si.getImg(), v.findViewById(R.id.itemIcon),
				Constants.SMALL_ICON_WIDTH, Constants.SMALL_ICON_HEIGHT);
		TextView itemName = (TextView) v.findViewById(R.id.itemName);
		String temp = color;
		if (heroColor && !StringUtil.isNull(si.getParamStr0()))
			temp = si.getParamStr0();

		if (showQulity && !StringUtil.isNull(si.getParamStr1()))
			ViewUtil.setRichText(itemName, StringUtil.color(si.getParamStr1()
					+ "  " + si.getName(), temp));
		else
			ViewUtil.setRichText(itemName, StringUtil.color(si.getName(), temp));

		if (si.getCount() >= 0)
			ViewUtil.setRichText(v, R.id.itemCount,
					StringUtil.color("×" + si.getCount(), color));
		else
			ViewUtil.setRichText(v, R.id.itemCount,
					StringUtil.color("" + si.getCount(), color));
		return v;
	}

	public static View getShowItemView(ShowItem si, int color, int size) {
		View v = Config.getController().inflate(R.layout.common_item_line);
		android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
				-1, -2);
		lp.topMargin = (int) (2 * Config.scaleRate);
		lp.leftMargin = (int) (2 * Config.scaleRate);
		lp.rightMargin = (int) (2 * Config.scaleRate);
		v.setLayoutParams(lp);
		new ViewImgScaleCallBack(si.getImg(), v.findViewById(R.id.itemIcon),
				Constants.SMALL_ICON_WIDTH, Constants.SMALL_ICON_HEIGHT);
		TextView itemName = (TextView) v.findViewById(R.id.itemName);
		ViewUtil.setText(itemName, si.getName());
		itemName.setTextColor(color);
		itemName.setTextSize(size * Config.SCALE_FROM_HIGH);
		TextView itemCount = (TextView) v.findViewById(R.id.itemCount);
		if (si.getCount() >= 0)
			ViewUtil.setText(v, R.id.itemCount, "×" + si.getCount());
		else
			ViewUtil.setText(v, R.id.itemCount, si.getCount());
		itemCount.setTextColor(color);
		itemCount.setTextSize(size * Config.SCALE_FROM_HIGH);
		return v;
	}

	// public static void setHeroIconStar(ViewGroup viewGroup, int starCount) {
	// int[] starViewId = { R.id.star1, R.id.star2, R.id.star3, R.id.star4,
	// R.id.star5, R.id.star6 };
	//
	// for (int i = 0; i < starViewId.length; i++) {
	// if (i < starCount) {
	// ViewUtil.setVisible(viewGroup, starViewId[i]);
	// } else {
	// ViewUtil.setGone(viewGroup, starViewId[i]);
	// }
	// }
	// }

	// public static void setHeroIconStar(ViewGroup viewGroup, int starCount,
	// int width, int height) {
	// int[] starViewId = { R.id.star1, R.id.star2, R.id.star3, R.id.star4,
	// R.id.star5, R.id.star6 };
	//
	// for (int i = 0; i < starViewId.length; i++) {
	// if (i < starCount) {
	// Drawable troop_bg = Config.getController().getDrawable(
	// R.drawable.no_troop_bg);
	// Drawable star = Config.getController().getDrawable(
	// R.drawable.star);
	// int w = (int) ((float) width / troop_bg.getIntrinsicWidth()
	// * star.getIntrinsicWidth() + 0.5f);
	// int h = (int) ((float) height / troop_bg.getIntrinsicHeight()
	// * star.getIntrinsicHeight() + 0.5f);
	//
	// int top = Constants.HERO_STAR_TOP_MARGIN;
	// top = (int) ((float) width / troop_bg.getIntrinsicWidth() * top);
	// ((android.widget.FrameLayout.LayoutParams) viewGroup
	// .getLayoutParams()).topMargin = top;
	// View v = viewGroup.findViewById(starViewId[i]);
	// ViewUtil.setVisible(v);
	// ViewUtil.adjustLayout(v, w, h);
	// } else {
	// ViewUtil.setGone(viewGroup, starViewId[i]);
	// }
	// }
	// }

	public static void showRechargeTip(String title, String msg) {
		new MsgConfirm(title, CustomConfirmDialog.DEFAULT).setOKText("去充值")
				.show(msg, new CallBack() {
					@Override
					public void onCall() {
						Config.getController().openRechargeCenterWindow();
					}
				}, null);
	}

	public static void addSkillSlot(
			List<HeroSkillSlotInfoClient> skillSlotInfos, ViewGroup skillView) {
		if (ListUtil.isNull(skillSlotInfos))
			return;

		for (HeroSkillSlotInfoClient hssic : skillSlotInfos) {
			if (hssic.getSkillId() > 0)
				ViewUtil.addSkillImageView(skillView, hssic.getBattleSkill()
						.getIcon());
		}
	}

	public static void setWarHeroInfo(View v,
			final OtherHeroInfoClient heroInfo, final BriefUserInfoClient user) {
		View heroLayout = v.findViewById(R.id.heroLayout);
		ViewUtil.setVisible(heroLayout);

		// new HeroQualityBgImgCallBack(heroInfo.getHeroQuality().getImage(),
		// v.findViewById(R.id.rank));
		IconUtil.setHeroIcon(v, heroInfo.getHeroProp(),
				heroInfo.getHeroQuality(), heroInfo.getStar());

		ViewUtil.setRichText(v, R.id.heroTypeName, heroInfo.getColorTypeName());
		ViewUtil.setRichText(v, R.id.heroName, StringUtil.color(heroInfo
				.getHeroProp().getName(), heroInfo.getHeroQuality().getColor()));
		ViewUtil.setText(v, R.id.heroLv, "Lv:" + heroInfo.getLevel());

		heroLayout.setTag(user);
		heroLayout.setOnClickListener(new OtherHeroClickListener(heroInfo));

		if (null == user)
			ViewUtil.setRichText(v, R.id.masterName, "");
		else {
			if (Account.user.getId() == user.getId()) {
				ViewUtil.setRichText(v, R.id.masterName, "我自己");
			} else {
				ViewUtil.setRichText(v, R.id.masterName, user.getNickName());
			}
		}

		ViewUtil.setRichText(v, R.id.troopType, "主战部队");
	}

	public static void setWarHeroInfos(View convertView, View v,
			final BattleHeroInfoClient heroInfo, final BriefUserInfoClient user) {
		if (heroInfo == null || heroInfo.getHeroInfo() == null
				|| heroInfo.getHeroInfo().getId() == 0)
			return;
		ViewUtil.setVisible(v);

		// new HeroQualityBgImgCallBack(heroInfo.getHeroQuality().getImage(),
		// v.findViewById(R.id.rank));
		// IconUtil.setHeroIcon(v, heroInfo.getHeroProp(),
		// heroInfo.getHeroQuality(), heroInfo.getStar());

		// if (null == heroInfo || null == heroInfo.getHeroInfo().getHeroProp()
		// || null == heroInfo.getHeroInfo().getHeroQuality()) {
		// IconUtil.setHeroIconScale(v, heroInfo);
		//
		// ViewUtil.setRichText(v, R.id.heroTypeName, heroInfo
		// .getColorTypeName());
		//
		// ViewUtil.setRichText(
		// v,
		// R.id.heroName,
		// StringUtil.color(heroInfo.getHeroProp()
		// .getName(), heroInfo.getHeroQuality()
		// .getColor()));
		//
		// ViewUtil.setText(v, R.id.heroLv, "Lv:"
		// + 0);
		//
		// if(heroInfo.getHeroInfo()!=null){
		// v.setTag(user);
		// v.setOnClickListener(new OtherHeroClickListener(heroInfo
		// .getHeroInfo()));
		// }
		//
		// } else {
		IconUtil.setHeroIconScale(v, heroInfo);

		ViewUtil.setRichText(v, R.id.heroTypeName, heroInfo.getHeroInfo()
				.getColorTypeName());

		ViewUtil.setRichText(v, R.id.heroName, StringUtil.color(heroInfo
				.getHeroInfo().getHeroProp().getName(), heroInfo.getHeroInfo()
				.getHeroQuality().getColor()));

		ViewUtil.setText(v, R.id.heroLv, "Lv:"
				+ heroInfo.getHeroInfo().getLevel());

		v.setTag(user);
		v.setOnClickListener(new OtherHeroClickListener(heroInfo.getHeroInfo()));
		// }

		if (null == user)
			ViewUtil.setRichText(convertView, R.id.masterName, "");
		else {
			if (Account.user.getId() == user.getId()) {
				ViewUtil.setRichText(convertView, R.id.masterName, "我自己");
			} else {
				ViewUtil.setRichText(convertView, R.id.masterName,
						user.getNickName());
			}
		}

		ViewUtil.setRichText(convertView, R.id.troopType, "主战部队");
	}

	public static View getItemLine(String icon, String name, String cnt) {
		View v = Config.getController().inflate(R.layout.item_line);

		new ViewImgScaleCallBack(icon, v.findViewById(R.id.itemIcon),
				Constants.SMALL_ICON_WIDTH, Constants.SMALL_ICON_HEIGHT);
		ViewUtil.setText(v.findViewById(R.id.itemName), name);
		ViewUtil.setRichText(v.findViewById(R.id.itemCount), cnt);

		return v;
	}

	public static View getItemLine(String icon, String name, String cnt,
			int colodId) {
		View v = Config.getController().inflate(R.layout.item_line);

		new ViewImgScaleCallBack(icon, v.findViewById(R.id.itemIcon),
				Constants.SMALL_ICON_WIDTH, Constants.SMALL_ICON_HEIGHT);
		ViewUtil.setRichText(v.findViewById(R.id.itemName),
				StringUtil.color(name, colodId));
		ViewUtil.setRichText(v.findViewById(R.id.itemCount),
				StringUtil.color(cnt, colodId));

		return v;
	}

	public static int setLine(ViewGroup line1, ViewGroup line2, int idx, View v) {
		if (0 == idx % 2)
			line1.addView(v);
		else
			line2.addView(v);

		return idx + 1;
	}

	public static void setRepeatedTileImg(ImageView iv, int resId) {
		Bitmap bmp = Config.getController().getBitmap(resId);
		BitmapDrawable d = new BitmapDrawable(Config.getController()
				.getResources(), bmp);
		d.setTileModeXY(null, TileMode.REPEAT);
		d.setDither(true);
		iv.setBackgroundDrawable(d);
	}

	public static void setCastleLvlProgressBar(CustomProgressBar progressBar,
			int total, int val) {
		int width = (int) (Config.screenWidth - 182 * Config.SCALE_FROM_HIGH);
		progressBar.setBarBg(R.drawable.progress_castle_bg, width);
		progressBar.setProgressBg(R.drawable.progress_castle);
		progressBar.setProgress(val, total);
		progressBar.setDesc(getProgressDescStr(val, total), 10);
	}

	private static String getProgressDescStr(int exp, int expTotal) {
		return exp + "/" + expTotal + "(" + (int) (exp * 1f / expTotal * 100)
				+ "%)";
	}

	public static ViewGroup getBonusView(ReturnInfoClient ric, int type,
			ViewGroup reward, int bestRecord, UserVip vip) {
		if (null == ric)
			return null;

		int idx = 0;
		ViewGroup spoil = (ViewGroup) Config.getController().inflate(
				R.layout.act_clear_spoil, reward, false);
		ViewUtil.setImage(spoil, R.id.type, CampaignInfoClient.getTypeImg(type));
		if (bestRecord > 0)
			setBestRecord(spoil, bestRecord);

		ViewGroup line1 = (ViewGroup) spoil.findViewById(R.id.line1);
		ViewGroup line2 = (ViewGroup) spoil.findViewById(R.id.line2);

		// 解析Attr
		List<ReturnAttrInfo> rais = ric.getReturnInfo().getRaisList();
		for (ReturnAttrInfo rai : rais) {
			View v = null;

			if (rai.getValue() > 0 && AttrData.isShowAttr(rai.getType())) {
				int val = rai.getValue();
				int color = 0;
				if (AttrType.ATTR_TYPE_EXP.number == rai.getType()
						&& null != vip) {
					if (CacheMgr.dictCache.getUserExpBonusRate() != 100) {
						val *= CacheMgr.dictCache.getUserExpBonusRate() / 100f;
						color = R.color.k7_color12;
					}
				}
				String str = color == 0 ? "×" + val : StringUtil.color("×"
						+ val, color);

				v = ViewUtil.getItemLine(ReturnInfoClient
						.getAttrTypeIconName(rai.getType().intValue()),
						ReturnInfoClient.getAttrTypeName(rai.getType()
								.intValue()), str);
			} else if (rai.getType() == AttrType.ATTR_TYPE_HERO_EXP.number
					&& rai.getValue() > 0) {
				int val = rai.getValue();
				int color = 0;
				if (CacheMgr.dictCache.getHeroExpBonusRate() != 100) {
					val *= CacheMgr.dictCache.getHeroExpBonusRate() / 100f;
					color = R.color.k7_color12;
				}

				String str = color == 0 ? "×" + val : StringUtil.color("×"
						+ val, color);
				v = ViewUtil.getItemLine("hero_exp.png", "将领经验", str);
			}

			if (null != v)
				idx = ViewUtil.setLine(line1, line2, idx, v);
		}

		for (ItemBag ibIt : ric.getItemPack()) {
			if (ibIt.getCount() > 0) {
				Item item = ibIt.getItem();
				View v = ViewUtil.getItemLine(item.getImage(), item.getName(),
						"×" + ibIt.getCount());
				idx = ViewUtil.setLine(line1, line2, idx, v);
			}
		}

		for (BuildingInfoClient bicIt : ric.getBuildings()) {
			BuildingProp buildingProp = bicIt.getProp();
			View v = ViewUtil.getItemLine(buildingProp.getImage(),
					buildingProp.getBuildingName(), "×1");
			idx = ViewUtil.setLine(line1, line2, idx, v);
		}

		for (ArmInfoClient aiIt : ric.getArmInfos()) {
			if (aiIt.getCount() > 0) {
				TroopProp tp = (TroopProp) aiIt.getProp();
				View v = ViewUtil.getItemLine(tp.getIcon(), tp.getName(), "×"
						+ aiIt.getCount());
				idx = ViewUtil.setLine(line1, line2, idx, v);
			}
		}

		for (HeroInfoClient hicIt : ric.getHeroInfos()) {
			HeroProp hp = hicIt.getHeroProp();
			View v = ViewUtil.getItemLine(hp.getIcon(), hp.getName(), "×1");
			idx = ViewUtil.setLine(line1, line2, idx, v);
		}

		return spoil;
	}

	public static ViewGroup getLossView(BattleLossDetail bld, ViewGroup reward) {
		if (null == bld)
			return null;

		if (ListUtil.isNull(bld.getArmInfoLs()))
			return null;

		int idx = 0;
		ViewGroup spoil = (ViewGroup) Config.getController().inflate(
				R.layout.act_clear_spoil, reward, false);
		ViewUtil.setGone(spoil, R.id.titleFrame);

		ViewGroup line1 = (ViewGroup) spoil.findViewById(R.id.line1);
		ViewGroup line2 = (ViewGroup) spoil.findViewById(R.id.line2);

		for (TroopMoveInfoClient aiIt : bld.getArmInfoLs()) {
			if (aiIt.getCount() > 0) {
				TroopProp tp = (TroopProp) aiIt.getArmProp();
				View v = ViewUtil.getItemLine(tp.getIcon(), tp.getName(), "×"
						+ aiIt.getCount());
				idx = ViewUtil.setLine(line1, line2, idx, v);
			}
		}

		return spoil;
	}

	public static void setBestRecord(ViewGroup vg, int records) {
		if (records <= 0)
			return;

		ViewGroup bestRecord = (ViewGroup) vg.findViewById(R.id.bestRecord);
		ViewUtil.setVisible(bestRecord);
		addStars(records, bestRecord);
	}

	public static void setBestRecordStars(ViewGroup vg, int records) {
		if (records <= 0)
			return;

		ViewGroup bestRecord = (ViewGroup) vg.findViewById(R.id.bestRecord);
		ViewUtil.setVisible(bestRecord);
		bestRecord.removeAllViews();
		addStars(records, bestRecord);
	}

	private static void addStars(int records, ViewGroup bestRecord) {
		for (int i = 0; i < records; i++) {
			ImageView iv = new ImageView(Config.getController().getUIContext());
			android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.leftMargin = (int) (5 * Config.scaleRate);
			iv.setLayoutParams(lp);
			iv.setBackgroundResource(R.drawable.campaign_best_score);
			bestRecord.addView(iv);
		}
	}

	// 设置将领升级信息
	public static void setHeroReward(BattleLogInfoClient blic) {
		if (null == blic)
			return;

		List<ReturnHeroInfoClient> rhic = blic.getDetail().getRhics();
		if (ListUtil.isNull(rhic))
			return;

		for (ReturnHeroInfoClient it : rhic) {
			// 如果没获得经验，则不显示将领奖励信息,只显示我方将领
			if (Account.user.getId() == it.getUserid()) {
				int heroLvl = blic.getHeroLvl(it.getId());
				if (-1 == heroLvl)
					continue;
				if (0 != it.getLevelDiff() && !it.isShowed()) {
					it.setShowed(true);
					new BattleHeroUpdateSucTip(it, blic).show();
					break;
				}
			}
		}
	}

	public static TextView getBattleLogTextView() {
		TextView v = (TextView) Config.getController().inflate(
				R.layout.battle_log_txt);
		v.setGravity(Gravity.CENTER_HORIZONTAL);
		return v;
	}

	public static View getStaticSkillTextView() {
		View v = Config.getController().inflate(R.layout.static_skill);
		return v;
	}

	public static void setMarginPosition(View view, int x, int y) {
		android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) view
				.getLayoutParams();
		// LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		// lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
		view.setLayoutParams(lp);
	}

	public static void releaseDrawable(View v, Drawable d) {
		if (d == null) {
			return;
		}
		if (v != null) {
			v.clearAnimation();
			ViewUtil.setGone(v);
			v.setBackgroundDrawable(null);
		}

		// if(d instanceof BitmapDrawable)
		// {
		// Bitmap bmp = ((BitmapDrawable) d).getBitmap();
		//
		// Log.d("ViewUtil", "v--" + v + "bmp--" + bmp + "width--" +
		// v.getWidth() + "height---" + v.getHeight());
		// if(bmp != null && bmp.isRecycled() == false)
		// {
		// bmp.recycle();
		// bmp = null;
		// }
		// d = null;
		// }
	}

}
