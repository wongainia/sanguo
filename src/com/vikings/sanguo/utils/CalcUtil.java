package com.vikings.sanguo.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.vikings.sanguo.Constants;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class CalcUtil {
	public final static int TEN_THOUSAND = 10000;
	public final static int HUNDRED_THOUSAND = TEN_THOUSAND * 10;
	public final static int HUNDRED_MILLION = TEN_THOUSAND * TEN_THOUSAND;

	public static int round(int n, int base, boolean add) {
		int m = n % base;
		if (m == 0)
			return n;
		else {
			if (n > 0) {
				if (add)
					return (n / base + 1) * base;
				else
					return n / base * base;
			} else {
				if (add)
					return n / base * base;
				else
					return (n / base - 1) * base;
			}
		}
	}

	public static boolean isRectHit(Rect r1, Rect r2) {
		// 计算相交矩形的坐标
		int left = Math.max(r1.left, r2.left);
		int top = Math.max(r1.top, r2.top);
		int right = Math.min(r1.right, r2.right);
		int bottom = Math.min(r1.bottom, r2.bottom);
		if (left < right && top < bottom)
			return true;
		else
			return false;
	}

	public static int round(int value, int mod) {
		if (value > 0) {
			if (value % mod > mod / 2)
				return value / mod + 1;
			else
				return value / mod;
		} else {
			if (value % mod < -mod / 2)
				return value / mod - 1;
			else
				return value / mod;
		}
	}

	public static void calcDecoRect(Rect screenRect, int index, Rect decoRect,
			Bitmap bitmap, float zoomLevel) {
		int w = (int) (bitmap.getWidth() * zoomLevel);
		int h = (int) (bitmap.getHeight() * zoomLevel);
		switch (index) {
		case 1:
			decoRect.left = screenRect.left
					+ (screenRect.right - screenRect.left) / 4 - w / 2;
			decoRect.top = screenRect.top
					+ (screenRect.bottom - screenRect.top) / 2 - h / 2;
			break;
		case 2:
			decoRect.left = screenRect.left
					+ (screenRect.right - screenRect.left) / 2 - w / 2;
			decoRect.top = screenRect.top
					+ (screenRect.bottom - screenRect.top) / 4 - h / 2;
			break;
		case 3:
			decoRect.left = screenRect.left
					+ (screenRect.right - screenRect.left) / 2 - w / 2;
			decoRect.top = screenRect.top
					+ (screenRect.bottom - screenRect.top) / 4 * 3 - h / 2;
			break;
		default:
			decoRect.left = screenRect.left
					+ (screenRect.right - screenRect.left) / 4 * 3 - w / 2;
			decoRect.top = screenRect.top
					+ (screenRect.bottom - screenRect.top) / 2 - h / 2;
			break;
		}
		decoRect.right = decoRect.left + w;
		decoRect.bottom = decoRect.top + h;
	}

	// 生成下标序列
	public static int[] randomSerial(int length) {
		int[] ss = new int[length];
		for (int i = 0; i < ss.length; i++) {
			ss[i] = i;
		}
		for (int i = 0; i < ss.length - 1; i++) {
			int index = (int) (Math.random() * (length - i)) + i;
			int tmp = ss[i];
			ss[i] = ss[index];
			ss[index] = tmp;
		}
		return ss;
	}

	public static int upNum(float num) {
		int tmp = (int) num;
		if (num > tmp)
			tmp += 1;
		return tmp;
	}

	public static long upLongNum(float num) {
		long tmp = (long) num;
		if (num > tmp)
			tmp += 1;
		return tmp;
	}

	public static ArrayList<Integer> parseInteger(int num) {
		return parseLong(num);
	}

	public static ArrayList<Integer> parseLong(long num) {
		ArrayList<Integer> list = new ArrayList<Integer>(1);
		long i = num;
		while (i / 10 != 0) {
			list.add((int) (i % 10));
			i /= 10;
		}

		list.add((int) i);

		return list;
	}

	// 小数点 解成 -2， w 解析成 -1 超过1w的解析
	public static ArrayList<Integer> parseMoreMill(long num) {
		ArrayList<Integer> list = new ArrayList<Integer>(1);
		list.add(-1);
		String numb = formatNumber(num);
		for (int index = numb.length() - 1; index >= 0; index--) {
			char c = numb.charAt(index);
			if (c == '.') {
				list.add(-2);
			} else if (c >= '0' && c <= '9') {
				int digit = c - '0';
				list.add(digit);
			}
		}
		return list;
	}

	// 对军队数量 以万计
	public static String formatNumber(long number) {
		float meas = 10000f;
		float num = number / meas;
		return String.format("%.1f", num);
	}

	public static int upNum(double num) {
		int tmp = (int) num;
		if (num > tmp)
			tmp += 1;
		return tmp;
	}

	public static String turnToTenThousand(int value) {
		if (value < TEN_THOUSAND)
			return String.valueOf(value);
		else
			return (value / TEN_THOUSAND) + "万";
	}

	public static String turnToHundredThousand(int value) {
		if (value < HUNDRED_THOUSAND)
			return String.valueOf(value);
		else
			return (value / TEN_THOUSAND) + "万";
	}

	// 加单位
	public static String unitNum(long num) {
		StringBuffer buf = new StringBuffer();
		if (num >= HUNDRED_MILLION) {
			buf.append(num / HUNDRED_MILLION).append("亿");
			num = num % HUNDRED_MILLION;
		}

		if (num > TEN_THOUSAND) {
			buf.append(num / TEN_THOUSAND).append("万");
			num = num % TEN_THOUSAND;
		}
		if (num != 0 || buf.length() == 0)
			buf.append(num);
		return buf.toString();
	}

	public static String format(double num) {
		int temp = (int) num;
		if (temp == num)
			return String.valueOf(temp);
		else {
			DecimalFormat format = new DecimalFormat("#.#");
			return format.format(num);
		}
	}

	public static String format2(double num) {
		int temp = (int) num;
		if (temp == num)
			return String.valueOf(temp);
		else {
			DecimalFormat format = new DecimalFormat("#.##");
			return format.format(num);
		}
	}

	public static String format3(double num) {
		int temp = (int) num;
		if (temp == num)
			return String.valueOf(temp);
		else {
			DecimalFormat format = new DecimalFormat("#.###");
			return format.format(num);
		}
	}

	/**
	 * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
	 * 
	 * @param num
	 *            无逗号的数字
	 * @return 加上逗号的数字
	 */
	public static String addComma(int num) {

		// 将传进数字反转
		String reverseStr = new StringBuilder(String.valueOf(num)).reverse()
				.toString();

		String strTemp = "";
		for (int i = 0; i < reverseStr.length(); i++) {
			if (i * 3 + 3 > reverseStr.length()) {
				strTemp += reverseStr.substring(i * 3, reverseStr.length());
				break;
			}
			strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
		}
		// 将 【789,456,】 中最后一个【,】去除
		if (strTemp.endsWith(",")) {
			strTemp = strTemp.substring(0, strTemp.length() - 1);
		}

		// 将数字重新反转
		String resultStr = new StringBuilder(strTemp).reverse().toString();
		return resultStr;
	}

	// 传入的是元宝，转换成元
	public static String rechargeAmountStr(int amount) {
		if (amount % Constants.CENT == 0)
			return String.valueOf(amount / Constants.CENT);
		else
			return format2(amount * 1f / Constants.CENT);
	}

	public static long buildKey(int param1, int param2) {
		long temp = param1;
		temp <<= 32;
		temp |= param2;
		return temp;
	}

	public static int[] parse(long key) {
		int[] values = new int[2];
		values[0] = (int) (key >> 32);
		values[1] = (int) (key & 0x00000000ffffffff);
		return values;
	}

	public static String turnToTenThousand(long value) {
		if (value < CalcUtil.TEN_THOUSAND)
			return String.valueOf(value);
		else
			return (value / CalcUtil.TEN_THOUSAND) + "万";
	}

	public static String turnToTenThousandEx(long value) {
		if (value < CalcUtil.HUNDRED_THOUSAND)
			return String.valueOf(value);
		else
			return formatNumber(value) + "万";
		// return (value / CalcUtil.TEN_THOUSAND) + "万";
	}

}
