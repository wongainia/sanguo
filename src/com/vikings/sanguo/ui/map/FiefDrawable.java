package com.vikings.sanguo.ui.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.NpcClientProp;
import com.vikings.sanguo.thread.NullImgCallBack;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TroopUtil;

public class FiefDrawable {

	private static Paint paint;

	private static Rect rect1 = new Rect();
	private static Rect rect2 = new Rect();

	private static int w = (int) (20 * Config.scaleRate);
	private static int h = (int) (20 * Config.scaleRate);

	private static Bitmap hero = Config.getController().getBitmap("hero_limit");

	private static Bitmap capital = Config.getController().getBitmap(
			"btl_capital");
	private static String[] blankFiefName = { "fief_blank_01", "fief_blank_03",
			"fief_blank_02", "fief_blank_05" };

	private static float baseTextSize;

	static {
		paint = new Paint();
		paint.setStyle(Style.FILL);
		String familyName = "宋体";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		if (Config.getZoomLevel() == 14)
			baseTextSize = 12 * Config.scaleRate;
		else if (Config.getZoomLevel() == 12)
			baseTextSize = 10 * Config.scaleRate;
		else
			baseTextSize = 11 * Config.scaleRate;
		paint.setTextSize(baseTextSize);
		paint.setTypeface(font);
		paint.setAntiAlias(true);
	}

	public static String getRandomFiefName(long fiefId) {
		if (fiefId % 9 == 0)
			return blankFiefName[0];
		else if (fiefId % 7 == 0)
			return blankFiefName[3];
		else if (fiefId % 6 == 0)
			return blankFiefName[2];
		else if (fiefId % 10 == 0 || fiefId % 15 == 0)
			return blankFiefName[1];
		else
			return null;
	}

	public static void draw(Canvas canvas, long fiefId,
			BriefFiefInfoClient bfic, Rect r, long selId) {
		// 画背景
		if (Setting.isMapEnable()) {
			paint.setColor(0x20000000);
			canvas.drawRect(r.left - 1, r.top - 1, r.right + 1, r.bottom + 1,
					paint);
			paint.setColor(0xFFFFFFFF);
		}

		if (null == bfic || null == bfic.getProp()
				|| bfic.getProp().getId() == 0) {
			if (bfic != null && bfic.isHoly()) {
				// 圣都的 fiefprop 的id 也是0 故需要排除
			} else {
				if (!Setting.isMapEnable()) {
					String fiefName = getRandomFiefName(fiefId);
					if (null != fiefName) {
						Bitmap b = Config.getController().getBitmap(fiefName);
						if (null != b)
							drawFiefIcon(canvas, fiefName, r, b);
					}
				}
				return;
			}
		}
		if (bfic.isWasteland() && !CacheMgr.holyPropCache.isHoly(bfic.getId()))
			return;

		if (CacheMgr.npcCache.containKey(bfic.getUserId())
				&& 0 != bfic.getUserId()
				&& !CacheMgr.holyPropCache.isHoly(bfic.getId())) {
			try {
				NpcClientProp ncp = (NpcClientProp) CacheMgr.npcCache.get(bfic
						.getUserId());
				Bitmap b = Config.getController().getBitmap(ncp.getMapImg());
				if (null != b) {
					float scaleX = (float) (r.right - r.left) / b.getWidth()
							* 1f;
					float scaleY = (float) (r.bottom - r.top) / b.getHeight()
							* 1f;
					float scale = (scaleX < scaleY ? scaleX : scaleY) * 100;
					if ((scaleX < scaleY ? scaleY / scaleX : scaleX / scaleY) > 1.2f)
						scale = scale * 0.9f;
					else
						scale = scale * 0.7f;
					b = Config.getController().getScaleBitmap(ncp.getMapImg(),
							scale, scale);
					canvas.drawBitmap(b,
							r.left + (r.right - r.left - b.getWidth()) / 2,
							r.top + (r.bottom - r.top - b.getHeight()) / 2 + 5,
							paint);
				}

			} catch (GameException e) {
				e.printStackTrace();
			}
		} else {
			// 画规模图标
			if (null == bfic.getLord() || 0 == bfic.getLord().getId()) {
				paint.setColor(0xCDFFFFFF);
				paint.setColorFilter(ImageUtil.garyFilter);
			}

			Bitmap b = Config.getController().getBitmap(bfic.getIcon());
			if (null != b) {
				drawFiefIcon(canvas, bfic.getIcon(), r, b);
			} else {
				new NullImgCallBack(bfic.getIcon());
			}
			paint.setColorFilter(null);
			paint.setColor(0xFFFFFFFF);
		}

		// 画状态
		String stateIconName = bfic.getStateIcon(); // getStateIcon(bfic);

		if (!StringUtil.isNull(stateIconName)) {
			Bitmap stateIcon = Config.getController().getBitmap(stateIconName);
			canvas.drawBitmap(stateIcon,
					r.left + (r.right - r.left - stateIcon.getWidth()) / 2,
					r.top + (r.bottom - r.top - stateIcon.getHeight()) / 2,
					paint);
		}
		// 画肥羊
		if (bfic.drawFatSheep()) {
			Bitmap fatSheep = Config.getController().getBitmap("fat_sheep");
			canvas.drawBitmap(fatSheep, r.right - fatSheep.getWidth(), r.top,
					paint);
		}

		// 画以掠
		if (Account.plunderedCache.plundered(bfic.getId())) {
			Bitmap yilue = Config.getController().getBitmap("yilue");
			canvas.drawBitmap(yilue, r.left, r.top, paint);
		}

		// 底条
		// 自己蓝色
		if (Account.user.getId() == bfic.getUserId()) {
			paint.setColor(0x800C69F3);
		}
		// 家族绿色
		else if (Account.user.hasGuild()
				&& Account.guildCache.getRichInfoInCache() != null
				&& Account.guildCache.getRichInfoInCache().isMember(
						bfic.getUserId())) {
			paint.setColor(0xB216E686);
		}
		// 不同国 玩家红色
		else if (!BriefUserInfoClient.isNPC(bfic.getUserId())
				&& Account.user.getCountry() != bfic.getLord().getCountry()) {
			paint.setColor(0xB2F01313);
		} else
			paint.setColor(0xB2000000);

		canvas.drawRect(r.left, r.bottom - h, r.right, r.bottom, paint);

		int state = TroopUtil.getCurBattleState(bfic.getBattleState(),
				bfic.getBattleTime());
		if (state != BattleStatus.BATTLE_STATE_FINISH) {
			drawLowerInfo(canvas, bfic, r);
		} else {
			// 画战场清理中
			String text = "战场清理中";
			int textW = 0;
			int textH = 0;
			paint.setColor(0xFFFFFFFF);
			paint.getTextBounds(text, 0, text.length(), rect2);
			textW = rect2.width();
			textH = rect2.height();
			canvas.drawText(text, r.left + (r.width() - textW) / 2, r.bottom
					- (h - textH) / 2, paint);
		}

		paint.setColor(0xFFFFFFFF);
		// 画都城
		if (bfic.getId() == Account.manorInfoClient.getPos()) {
			canvas.drawBitmap(capital, r.left, r.top, paint);
		}

		if (bfic.getId() == selId) {
			paint.setColor(0xFFFF0000);
			paint.setStrokeWidth(5f);
			canvas.drawLine(r.left, r.top, r.right, r.top, paint);
			canvas.drawLine(r.left, r.bottom, r.right, r.bottom, paint);
			canvas.drawLine(r.left, r.top, r.left, r.bottom, paint);
			canvas.drawLine(r.right, r.top, r.right, r.bottom, paint);
			paint.setColor(0xFFFFFFFF);
		}
	}

	protected static void drawFiefIcon(Canvas canvas, String iconName, Rect r,
			Bitmap b) {

		if (b.getWidth() > b.getHeight()) {
			float scale = (float) r.width() * 3 / 4 / (float) b.getWidth()
					* 100;
			b = Config.getController().getScaleBitmap(iconName, scale, scale);
		} else {
			float scale = (float) r.height() * 3 / 4 / (float) b.getHeight()
					* 100;
			b = Config.getController().getScaleBitmap(iconName, scale, scale);
		}

		if (null != b)
			canvas.drawBitmap(b,
					r.left + (r.right - r.left - b.getWidth()) / 2, r.top
							+ (r.bottom - r.top - b.getHeight()) / 2 + 5, paint);
	}

	protected static void drawLowerInfo(Canvas canvas,
			BriefFiefInfoClient bfic, Rect r) {
		if (bfic.isLordNotNPC()) {
			drawNameBackground(canvas, r);
			drawNameFirstChar(canvas, bfic, r);
		}

		paint.setColor(0xFFFFFFFF);
		adjustTextSize(r, bfic);
		drawTroopAmount(canvas, bfic, r);
		drawHeroAmount(canvas, bfic, r);

		paint.setTextSize(baseTextSize);
	}

	protected static void drawTroopAmount(Canvas canvas,
			BriefFiefInfoClient bfic, Rect r) {
		String text = bfic.isLordNotNPC() ? CalcUtil.turnToTenThousand(bfic
				.getUnitCount()) : bfic.getName();

		int textW = 0;
		int textH = 0;
		if (!StringUtil.isNull(text)) {
			paint.getTextBounds(text, 0, text.length(), rect2);
			textW = rect2.width();
			textH = rect2.height();
		}

		if (bfic.isLordNotNPC())
			canvas.drawText(text, r.left + w, r.bottom - (h - textH) / 2, paint);
		else
			canvas.drawText(text, r.left + (r.width() - textW) / 2, r.bottom
					- (h - textH) / 2, paint);
	}

	protected static void adjustTextSize(Rect r, BriefFiefInfoClient bfic) {
		String tmp = bfic.getHeroCount() > 0 ? String.valueOf(bfic
				.getHeroCount()) : "";

		tmp += bfic.isLordNotNPC() ? CalcUtil.turnToTenThousand(bfic
				.getUnitCount()) : bfic.getName();

		paint.getTextBounds(tmp, 0, tmp.length(), rect2);

		if (bfic.getHeroCount() > 0) {
			int imgW = w + hero.getWidth();
			if (rect2.width() + imgW > r.width())
				paint.setTextSize((int) ((r.width() - imgW) * 1.4f / tmp
						.length()));
		} else {
			if (!bfic.isLordNotNPC() && tmp.length() >= 5) {
				paint.setTextSize(baseTextSize - 1 * Config.scaleRate);
			}
		}

	}

	protected static void drawHeroAmount(Canvas canvas,
			BriefFiefInfoClient bfic, Rect r) {
		if (bfic.getHeroCount() > 0) {
			String heroCntStr = String.valueOf(bfic.getHeroCount());
			Rect rectHero = new Rect();
			paint.getTextBounds(heroCntStr, 0,
					String.valueOf(bfic.getHeroCount()).length(), rectHero);

			// 4:数字距图片3像素
			canvas.drawBitmap(hero,
					r.right - rectHero.width() - hero.getWidth() - 6, r.bottom
							- hero.getHeight() - 8, paint);
			canvas.drawText(heroCntStr, r.right - rectHero.width() - 3,
					r.bottom - ((hero.getHeight() - rectHero.height()) >> 1)
							- 8, paint);// y=bottom
		}
	}

	protected static void drawNameFirstChar(Canvas canvas,
			BriefFiefInfoClient bfic, Rect r) {
		String nikeName = bfic.getLord().getNickName();
		if (!StringUtil.isNull(nikeName)) {
			String charr = nikeName.substring(0, 1);
			paint.getTextBounds(charr, 0, charr.length(), rect1);

			paint.setColor(0xFFFCFF1F);
			canvas.drawText(charr, r.left + (w - rect1.width()) / 2, r.bottom
					- (h - rect1.height()) / 2, paint);
		}
	}

	protected static void drawNameBackground(Canvas canvas, Rect r) {
		canvas.drawRect(r.left, r.bottom - h, r.left + h, r.bottom, paint);
	}
}
