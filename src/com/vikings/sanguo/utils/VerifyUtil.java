package com.vikings.sanguo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {
	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			// 标准邮箱格式
			// String check =
			// "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			// 为了怕出现很怪异的邮箱，邮箱@之前任何位置出现[-，_，.]符号都是合法
			String check = "^([a-z0-9A-Z-|_|.]+[-|_|\\.]?)+[a-z0-9A-Z-|_|.]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
		}
		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		if (StringUtil.isNull(mobiles))
			return false;
		
		boolean flag = false;
		try {
			// String check = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
			String check = "^((1[0-9]))\\d{9}$";
			Pattern p = Pattern.compile(check);
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
		}
		return flag;
	}

	/**
	 * 验证是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean isIDCard(String str) {
		boolean flag = false;
		try {
			Pattern pattern = Pattern
					.compile("([\\d]{6}(19|20)[\\d]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])\\d{3}[0-9xX])|([\\d]{6}[\\d]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])\\d{3})");
			Matcher m = pattern.matcher(str);
			flag = m.matches();
		} catch (Exception e) {

		}
		return flag;
	}
}
