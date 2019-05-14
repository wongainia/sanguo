package com.vikings.sanguo.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;
import android.util.Log;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleLogHeroInfoClient;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.HeroArmPropClient;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroQuality;
import com.vikings.sanguo.model.OtherHeroArmPropInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.UserVip;

public class StringUtil {

	public final static String UNICODE = "UNICODE";

	public static boolean isStrIn(String src, String[] s) {
		if (s == null)
			return false;
		for (int i = 0; i < s.length; i++) {
			if (s[i] == null) {
				if (src == null)
					return true;
			} else {
				if (s[i].equals(src))
					return true;
			}
		}
		return false;
	}

	public static String trimLR(String str) {
		return str.replaceAll("\\s", "");
	}

	public static boolean isNull(String str) {
		if (str == null || str.trim().length() == 0)
			return true;
		else
			return false;
	}

	public static String noNull(String str) {
		if (str == null)
			return "";
		else
			return str;
	}

	public static String trimAddr(String str) {
		if (str == null)
			return "";
		int x1 = str.indexOf('(');
		if (x1 == -1)
			return str;
		return str.substring(0, x1);
	}

	public static int parseInt(String str) {
		if (isNull(str))
			return 0;
		else {
			str = str.trim();
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				return 0;
			}
		}
	}

	public static String intString(String str) {
		if (str != null)
			return strip(str, '0', "right");
		else
			return str;
	}

	public static int countChar(String str, char ch) {
		if (isNull(str))
			return 0;
		int sum = 0;
		for (int i = 0; i < str.length(); i++) {
			if (ch == str.charAt(i))
				sum++;
		}
		return sum;
	}

	public static String getPadding(char repeat, int length) {
		StringBuffer buf = new StringBuffer();
		while (buf.length() < length) {
			buf.append(repeat);
		}
		return buf.toString();
	}

	public static String fill(String src, int size) {
		return padding(src, ' ', size, "left");
	}

	public static String fill0(String src, int size) {
		return padding(src, '0', size, "right");
	}

	public static String padding(String src, char padding, int length,
			String align) {
		if (src == null)
			src = "";
		if (src.length() > length)
			return src.substring(0, length);
		String str = getPadding(padding, length - src.length());
		if ("left".equalsIgnoreCase(align))
			return src + str;
		else
			return str + src;
	}

	public static String strip(String src, char padding, String align) {
		if (src == null)
			src = "";
		if ("left".equalsIgnoreCase(align)) {
			int i = src.length() - 1;
			while (i >= 0 && src.charAt(i) == padding)
				i--;
			return src.substring(0, i + 1);
		} else {
			int i = 0;
			while (i < src.length() && src.charAt(i) == padding)
				i++;
			return src.substring(i, src.length());
		}
	}

	public static String remove(StringBuilder buf, int start, int end) {
		String str = buf.substring(start, end);
		buf.delete(start, end);
		return str;
	}

	public static String remove(StringBuilder buf, String text) {
		int end = buf.indexOf(text);
		if (end == -1)
			return null;
		String str = buf.substring(0, end);
		buf.delete(0, end + text.length());
		return str.trim();
	}

	public static String removeCsv(StringBuilder buf) {
		String str;
		int end = buf.indexOf(",");
		if (end == -1) {
			str = buf.toString();
			buf.delete(0, buf.length());
		} else {
			str = buf.substring(0, end);
			buf.delete(0, end + 1);
		}

		return str.trim();
	}

	// 提取六位数的每一位
	public static List<Integer> dealWithCurrency(int count) {
		List<Integer> list = new ArrayList<Integer>();
		if (count >= 0 && count <= 999999) {
			list.add(count / 100000);
			list.add(count % 100000 / 10000);
			list.add(count % 100000 % 10000 / 1000);
			list.add(count % 100000 % 10000 % 1000 / 100);
			list.add(count % 100000 % 10000 % 1000 % 100 / 10);
			list.add(count % 100000 % 10000 % 1000 % 100 % 10);
		}
		return list;
	}

	public static long removeCsvLong(StringBuilder buf) {
		String str = removeCsv(buf);
		if (isNull(str))
			return 0;
		return Long.valueOf(str);
	}

	public static int removeCsvInt(StringBuilder buf) {
		String str = removeCsv(buf);
		if (isNull(str))
			return 0;
		return Integer.valueOf(str);
	}

	public static short removeCsvShort(StringBuilder buf) {
		String str = removeCsv(buf);
		if (isNull(str))
			return 0;
		return Short.valueOf(str);
	}

	public static byte removeCsvByte(StringBuilder buf) {
		String str = removeCsv(buf);
		if (isNull(str))
			return 0;
		return Byte.valueOf(str);
	}

	public static boolean hasChinese(String str) {
		if (str == null)
			return false;
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+"))
				return true;
		}
		return false;
	}

	public static boolean equals(String str1, String str2) {
		if (str1 == null)
			return str2 == null;
		else
			return str1.equals(str2);
	}

	public static String toUp1st(String str) {
		if (str == null || str.length() < 1)
			return str;
		return Character.toUpperCase(str.charAt(0))
				+ str.substring(1, str.length());
	}

	public static String toLow1st(String str) {
		if (str == null || str.length() < 1)
			return str;
		return Character.toLowerCase(str.charAt(0))
				+ str.substring(1, str.length());
	}

	/**
	 * 状态字 右起第index位(start 0)是否为1
	 * 
	 * @param bufState
	 * @param index
	 * @return
	 */
	public static boolean isFlagOn(long bufState, int index) {
		long tmp = bufState >> index;
		tmp = tmp & 0x1L;
		return tmp == 1L;
	}

	/**
	 * 状态字 右起第index位(start 0), 如果是0设为1，是1设为0
	 * 
	 * @param bufState
	 * @param index
	 * @return
	 */
	public static long setFlagXOR(long bufState, int index) {
		long i = 0x1L << index;
		bufState = bufState ^ i;
		return bufState;
	}

	public static long setFlagOn(long bufState, int index) {
		long i = 0x1L << index;
		bufState = bufState | i;
		return bufState;
	}

	// 将第index位设置为0
	public static long setFlagOff(long bufState, int index) {
		long i = ~(0x1L << index);
		bufState = bufState & i;
		return bufState;
	}

	public static String value(long i) {
		if (i < 0)
			return String.valueOf(i);
		else
			return "+" + i;
	}

	public static String abs(long i) {
		return String.valueOf(Math.abs(i));
	}

	public static String color(String text, String color) {
		return new StringBuilder().append("<font color='").append(color)
				.append("'>").append(text).append("</font>").toString();
	}

	public static String color(String text, int resId) {
		return color(text, Config.getController().getResourceColorText(resId));
	}

	public static String nickNameColor(BriefUserInfoClient u) {
		return StringUtil.color(u.getHtmlNickName(), getNickNameColor(u));
	}

	public static String getNickNameColor(BriefUserInfoClient u) {
		return "1e5513";
	}

	public static String vipNumImgStr(int num) {
		StringBuilder b = new StringBuilder();
		do {
			StringBuilder tmp = new StringBuilder();
			tmp.append("#vip_").append(num % 10).append("#");
			b.insert(0, tmp);
			num = num / 10;
		} while (num > 0);
		return b.toString();
	}

	// 描述图片
	public static void descImg(ViewGroup viewGroup, int value, int resid_img1,
			int resid_img2, int resid_img3) {
		if (resid_img1 > 0) {
			viewGroup.findViewById(R.id.img1).setBackgroundResource(resid_img1);
		} else {
			ViewUtil.setGone(viewGroup.findViewById(R.id.img1));
		}

		viewGroup.findViewById(R.id.img2).setBackgroundResource(resid_img2);
		viewGroup.findViewById(R.id.img3).setBackgroundResource(resid_img3);
		ViewUtil.setRichText(viewGroup.findViewById(R.id.img0),
				vipNumImgStr(value));
	}

	public static String numImgStr(String pre, int num) {
		StringBuilder b = new StringBuilder();
		do {
			StringBuilder tmp = new StringBuilder();
			tmp.append("#").append(pre).append(num % 10).append("#");
			b.insert(0, tmp);
			num = num / 10;
		} while (num > 0);
		return b.toString();
	}

	public static String numImgStrNoScale(String pre, int num) {
		StringBuilder b = new StringBuilder();
		do {
			StringBuilder tmp = new StringBuilder();
			tmp.append("#!").append(pre).append(num % 10).append("#");
			b.insert(0, tmp);
			num = num / 10;
		} while (num > 0);
		return b.toString();
	}

	public static List<String> smallVipNumImgStr(int num) {
		List<String> names = new ArrayList<String>();
		do {
			int i = num % 10;
			num = num / 10;
			names.add("s_vip" + i + ".png");
		} while (num > 0);
		return names;
	}

	public static byte[] getBytes(String src, String encoding)
			throws UnsupportedEncodingException {
		byte[] b = src.getBytes(encoding);
		if (UNICODE.equals(encoding)) {
			if (b.length < 2)
				return b;
			else {
				byte[] buf = new byte[b.length - 2];
				System.arraycopy(b, 2, buf, 0, buf.length);
				return buf;
			}
		} else
			return b;
	}

	// 将超过四个字符的字符串只显示前四个字符和..
	public static String get6CharStr(String name) {
		return getNCharStr(name, 6);
	}

	// 返回字符串截取前n个字符
	public static String getNCharStr(String name, int n) {
		if (null == name) {
			return "";
		} else {
			return name.length() <= n ? name : name.substring(0, n) + "..";
		}
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}
		return true;
	}

	public static Date toDate(String input) {
		if (StringUtil.isNull(input) || input.length() < 5)
			return null;
		try {
			return DateUtil.cfgFormat.parse(input);
		} catch (Exception e) {
			Log.e("DateUtil", "", e);
			return null;
		}
	}

	public static String toSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);

		}
		return new String(c);
	}

	public static String repParams(String rsc, String... params) {
		for (int i = 0; i < params.length; i++) {
			int index = rsc.indexOf("<param" + i + ">");
			if (index != -1)
				rsc = rsc.replace("<param" + i + ">", params[i]);
		}
		return rsc;
	}

	public static int parseSS(String s) {
		try {
			String[] ss = s.split(":");
			int h = Integer.valueOf(ss[0]);
			int m = Integer.valueOf(ss[1]);
			return h * 60 * 60 + m * 60;
		} catch (Exception e) {
			return 0;
		}
	}

	// 将领【名称】
	public static String getHeroName(HeroProp heroProp, HeroQuality heroQuality) {
		if (null == heroProp || null == heroQuality)
			return "";
		return color(heroProp.getName(), heroQuality.getColor());
	}

	// 将领【类型】
	public static String getHeroTypeName(HeroQuality heroQuality) {
		if (null == heroQuality)
			return "";
		return color(heroQuality.getName(), heroQuality.getColor());
	}

	public static String getHeroNameByType(int type) {
		if (type == Constants.MAIN_GENERAL) {
			return "主将";
		} else {
			return "副将";
		}
	}

	public static String getArmPropDesc(
			List<? extends HeroArmPropClient> armPropInfos) {
		if (ListUtil.isNull(armPropInfos))
			return "";

		StringBuilder buf = new StringBuilder("擅长兵种:");
		for (HeroArmPropClient it : armPropInfos) {
			buf.append(it.getHeroTroopName().getSlug() + "兵,");
		}

		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	// 坐标
	public static String getCoordinateDesc(BriefFiefInfoClient bfic) {
		StringBuilder buf = new StringBuilder("坐标:")
				.append(bfic.getNatureCountryName()).append("(")
				.append(TileUtil.uniqueMarking(bfic.getId())).append(")");
		return buf.toString();
	}

	// 兵力
	public static String getTroopDesc(BriefFiefInfoClient bfic) {
		StringBuilder buf = new StringBuilder().append(bfic.getUnitCount())
				.append("(").append("将:").append(bfic.getHeroCount())
				.append(")");
		return buf.toString();
	}

	public static String getShowItemDesc(ArrayList<ShowItem> showItems) {
		if (ListUtil.isNull(showItems))
			return "";

		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < showItems.size(); i++) {
			ShowItem showItem = showItems.get(i);

			buf.append(showItem.getCount())
					.append(showItem.getName())
					.append("  (")
					.append((showItem.isEnough() ? showItem.getSelfCount()
							: StringUtil.color(
									String.valueOf(showItem.getSelfCount()),
									"red"))).append(")");

			if (i != showItems.size() - 1)
				buf.append(",");
		}

		return buf.toString();
	}

	public static String getShowItemDescWithoutOwnCnt(
			ArrayList<ShowItem> showItems) {
		if (ListUtil.isNull(showItems))
			return "";

		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < showItems.size(); i++) {
			ShowItem showItem = showItems.get(i);

			buf.append(showItem.getCount()).append(showItem.getName());

			if (i != showItems.size() - 1)
				buf.append(",");
		}

		return buf.toString();
	}

	public static String getSkillEffectDesc(OtherHeroInfoClient ohic,
			BattleSkill battleSkill) {
		List<OtherHeroArmPropInfoClient> list = ohic.getArmPropInfos();
		StringBuffer sBuffer = new StringBuffer();
		for (OtherHeroArmPropInfoClient client : list) {
			sBuffer.append(replaceEscapeCharacter(client, battleSkill)
					+ "<br/>");
		}
		return sBuffer.toString();
	}

	public static String getSkillEffectDesc(BattleLogHeroInfoClient blhic,
			BattleSkill battleSkill) {
		if (null == blhic || null == battleSkill)
			return "";

		List<OtherHeroArmPropInfoClient> list = blhic.getArmPropInfos();

		if (ListUtil.isNull(list))
			return "";

		StringBuffer sBuffer = new StringBuffer();
		for (OtherHeroArmPropInfoClient client : list) {
			sBuffer.append(replaceEscapeCharacter(client, battleSkill)
					+ "<br/>");
		}
		return sBuffer.toString();
	}

	public static String getSkillEffectDesc(
			List<OtherHeroArmPropInfoClient> list, BattleSkill battleSkill) {
		if (null == battleSkill || ListUtil.isNull(list))
			return "";
		StringBuffer sBuffer = new StringBuffer();
		for (OtherHeroArmPropInfoClient client : list) {
			sBuffer.append(replaceEscapeCharacter(client, battleSkill)
					+ "<br/>");
		}
		return sBuffer.toString();
	}

	public static String getSkillEffectDesc(OtherHeroArmPropInfoClient client,
			BattleSkill skill) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(replaceEscapeCharacter(client, skill) + "<br/>");
		return sBuffer.toString();
	}

	public static String getSkillEffectDesc(HeroInfoClient hic,
			BattleSkill battleSkill) {
		List<HeroArmPropInfoClient> list = hic.getArmPropInfos();
		StringBuffer sBuffer = new StringBuffer();

		for (int i = 0; i < list.size(); i++) {
			HeroArmPropInfoClient hapic = list.get(i);
			sBuffer.append(replaceEscapeCharacter(hapic, battleSkill));
			if (i != list.size() - 1)
				sBuffer.append("<br/>");
		}
		return sBuffer.toString();
	}

	public static String replaceEscapeCharacter(HeroArmPropClient hapc,
			BattleSkill battleSkill) {
		String desc = battleSkill.getEffectDesc();
		// double factor1 = (double) battleSkill.getBaseValue() / 100
		// + ((double) hapc.getValue() / battleSkill.getExpandBase())
		// * ((double) battleSkill.getExpandValue() / 100);
		//
		// double factor2 = (double) battleSkill.getBaseValue1() / 100
		// + ((double) hapc.getValue() / battleSkill.getExpandBase1())
		// * ((double) battleSkill.getExpandValue1() / 100);
		//
		// double factor3 = (double) battleSkill.getBaseValue2() / 100
		// + ((double) hapc.getValue() / battleSkill.getExpandBase2())
		// * ((double) battleSkill.getExpandValue2() / 100);
		//
		// desc = desc
		// .replace(
		// "<prop>",
		// StringUtil.color(hapc.getHeroTroopName().getSlug()
		// + "兵", "green"))
		// .replace(
		// "<factor1>",
		// StringUtil.color(
		// new DecimalFormat("0.00").format(factor1),
		// "green"))
		// .replace(
		// "<factor2>",
		// StringUtil.color(
		// new DecimalFormat("0.00").format(factor2),
		// "green"))
		// .replace(
		// "<factor3>",
		// StringUtil.color(
		// new DecimalFormat("0.00").format(factor3),
		// "green"));
		return desc;

	}

	public static String getNumRichText(int num) {
		StringBuilder b = new StringBuilder();
		for (char c : String.valueOf(num).toCharArray())
			b.append("#vip_").append(c).append("#");
		return b.toString();
	}

	// 将tab，回车，换行 替换为 空格，再将两个以上的 空格 替换为 单个空格
	public static String RemoveAdditionalChar(String str) {
		if (StringUtil.isNull(str))
			return "";

		return str.replaceAll("\t|\r|\n", " ").replaceAll("\\s{2,}", " ");
	}

	// 查找符合规则的坐标
	public static List<String> regexMarking(String str) {
		List<String> list = new ArrayList<String>();
		String regex = "\\d{3,5}" + Constants.FIEF_MARKING_SEPERATER
				+ "\\d{3,5}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String marking = matcher.group();
			if (!list.contains(marking))
				list.add(marking);
		}
		return list;
	}

	public static String starStr(int count) {
		String str = "";
		for (int i = 0; i < count; i++) {
			str += "#hero_star#";
		}
		return str;
	}

	public static boolean isNormalUserId(String userId) {
		if (StringUtil.isNull(userId))
			return false;
		Pattern pattern = Pattern.compile("[0-9]{6,9}"); // 正数
		if (pattern.matcher(userId).matches())
			return true;
		return false;
	}

	public static String getVipDesc() {
		UserVip vip = Account.getCurVip();
		if (0 == vip.getLevel())
			return "未开通VIP,开通后获得更多副本经验";
		else {
			StringBuilder buf = new StringBuilder("您当前为VIP").append(vip
					.getLevel());

			if (!Account.isTopVip())
				buf.append(",升级VIP后获得更多经验和次数");

			return buf.toString();
		}
	}

	public static String getArenaFreeTimes() {
		UserVip vip = Account.getCurVip();
		StringBuilder buf = new StringBuilder();
		if (null != vip) {
			int arenaFreeTime = vip.getFreeArenaCount();
			int left = arenaFreeTime - Account.myLordInfo.getArenaCount();

			if (Account.isVip()) {
				buf.append("您是VIP").append(vip.getLevel());
				if (left <= 0) {
					buf.append("，")
							.append(DateUtil.getMonthDay(new Date(
									Account.myLordInfo.getArenaCountResetTime()
											* (long) 1000))).append("可再次免费体验");
				} else {
					buf.append("，本日");
					if (arenaFreeTime != left)
						buf.append("还");
					buf.append("可免费挑战 ")
							.append(StringUtil
									.color(left + "", R.color.color11))
							.append("次");
				}
			} else {
				buf.append("您还不是VIP，VIP每日有一定的免费挑战次数");
			}
		}
		return buf.toString();
	}

	public static int[] parseIntArr(String str) {
		if (StringUtil.isNull(str))
			return new int[0];
		String[] tmp = str.split(",");
		int[] rs = new int[tmp.length];
		for (int i = 0; i < rs.length; i++) {
			rs[i] = Integer.parseInt(tmp[i]);
		}
		return rs;
	}
}
