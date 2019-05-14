/**
 *  
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  All right reserved.
 *
 *  Time : 2012-9-6
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 用于保存每个聊天对象的未读消息数 
 */

package com.vikings.sanguo.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class UnReadChatMsgCntCache {
	private HashMap<Integer, Integer> content = new HashMap<Integer, Integer>();
	
	public void add(int key, int value) {
		if (!content.containsKey(key))
			content.put(key, value);
		else {
			int cnt = content.get(key);
			cnt += value;
			content.put(key, cnt);
		}
	}
	
	public void clear(int key) {
		if (content.containsKey(key))
			content.put(key, 0);
	}
	
	public int get(int key) {
		if (!content.containsKey(key))
			return 0;
		return content.get(key);
	}
	
	//获取消息未读的玩家个数
	public int getUnReadPlayerCnt() {
		Set<Integer> set = content.keySet();
		int cnt = 0;
		for (Iterator<Integer> it = set.iterator(); it.hasNext();) {
			int unReadCnt = content.get(it.next());
			if (0 < unReadCnt)
				cnt++;
		}
		return cnt;
	}
}
