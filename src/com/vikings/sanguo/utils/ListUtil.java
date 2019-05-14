package com.vikings.sanguo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.protos.ReturnAttrInfo;
import com.vikings.sanguo.protos.ReturnThingInfo;

public class ListUtil {

	/**
	 * 在src中删除dest已有的元素
	 * 
	 * 注意如果是protobuf对象，不能调用equals方法(用到反射，代码混淆后出错)
	 * 
	 * @param src
	 * @param dest
	 */
	public static void deleteRepeat(List src, List dest) {
		for (Iterator it = src.iterator(); it.hasNext();) {
			Object object = (Object) it.next();
			// if (object instanceof MessageLite)
			// return;
			if (dest.contains(object))
				it.remove();
		}
	}

	/**
	 * 合并list 将src元素 replace到dest
	 * 
	 * @param <T>
	 * 
	 * @param src
	 * @param dest
	 */
	public static <T> void merge(List<T> src, List<T> dest) {
		for (int i = 0; i < dest.size(); i++) {
			for (T t : src) {
				if (t.equals(dest.get(i))) {
					dest.set(i, t);
					break;
				}
			}
		}
		for (T it : src) {
			if (dest.contains(it))
				continue;
			dest.add(it);
		}
	}

	public static <T> void merge(T t, List<T> dest) {
		if (null == t || null == dest)
			return;
		for (int i = 0; i < dest.size(); i++) {
			if (t.equals(dest.get(i))) {
				dest.set(i, t);
				return;
			}
		}
		dest.add(t);
	}

	public static <T> boolean isNull(List<T> ls) {
		return null == ls || 0 == ls.size();
	}


	public static void mergeReturnAttrInfo(List<ReturnAttrInfo> src,
			List<ReturnAttrInfo> dst) {
		if (!ListUtil.isNull(src)) {
			for (ReturnAttrInfo it1 : src) {
				boolean hasSame = false;

				for (ReturnAttrInfo it2 : dst) {
					if (it2.getType() == it1.getType()) {
						it2.setValue(it2.getValue() + it1.getValue());
						hasSame = true;
						break;
					}
				}

				if (!hasSame)
					dst.add(it1);
			}
		}
	}

	public static void mergeReturnThingInfo(List<ReturnThingInfo> src,
			List<ReturnThingInfo> dst) {
		if (!ListUtil.isNull(src)) {
			for (ReturnThingInfo it1 : src) {
				boolean hasSame = false;

				for (ReturnThingInfo it2 : dst) {
					if (it2.getThingid() == it1.getThingid()) {
						it2.setCount(it2.getCount() + it1.getCount());
						hasSame = true;
						break;
					}
				}

				if (!hasSame)
					dst.add(it1);
			}
		}
	}

	public static void mergeItemBag(List<ItemBag> src, List<ItemBag> dst) {
		if (!ListUtil.isNull(src)) {
			for (ItemBag it1 : src) {
				boolean hasSame = false;

				for (ItemBag it2 : dst) {
					if (it2.getItemId() == it1.getItemId()) {
						it2.setCount(it2.getCount() + it1.getCount());
						hasSame = true;
						break;
					}
				}

				if (!hasSame)
					dst.add(it1);
			}
		}
	}

	public static void mergeArmInfo(List<ArmInfoClient> src,
			List<ArmInfoClient> dst) {
		if (!ListUtil.isNull(src)) {
			for (ArmInfoClient it1 : src) {
				boolean hasSame = false;

				for (ArmInfoClient it2 : dst) {
					if (it2.getId() == it1.getId()) {
						it2.setCount(it2.getCount() + it1.getCount());
						hasSame = true;
						break;
					}
				}

				if (!hasSame)
					dst.add(it1);
			}
		}
	}

	public static BriefUserInfoClient getUser(List<BriefUserInfoClient> users,
			int userId) {
		for (BriefUserInfoClient user : users) {
			if (user.getId() == userId)
				return user;
		}
		return null;
	}

	public static BriefGuildInfoClient getGuild(
			List<BriefGuildInfoClient> guilds, int guildId) {
		for (BriefGuildInfoClient guild : guilds) {
			if (guild.getId() == guildId)
				return guild;
		}
		return null;
	}

	// 数组深度拷贝
	public static <T> List<T> deepCopy(List<T> src) {
		try {
			if (null != src) {
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(byteOut);
				out.writeObject(src);
				out.close();

				ByteArrayInputStream byteIn = new ByteArrayInputStream(
						byteOut.toByteArray());
				ObjectInputStream in = new ObjectInputStream(byteIn);
				@SuppressWarnings("unchecked")
				List<T> dest = (List<T>) in.readObject();
				in.close();
				return dest;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<T>();
	}
}
