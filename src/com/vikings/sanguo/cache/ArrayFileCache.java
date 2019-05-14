package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.EventRewards;

/**
 * 文件缓存 用于保存用资源服获取的资源，在客户端保存在本地
 * 
 * @author Brad.Chen
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
abstract public class ArrayFileCache extends FileCache {

	protected ArrayList list = new ArrayList();

	abstract public long getSearchKey1(Object obj);

	abstract public long getSearchKey2(Object obj);

	abstract public Object fromString(String line);

	@Override
	public synchronized void init() throws GameException {
		super.init();
		// 必须保证list有序
		Collections.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object object1, Object object2) {
				if (getSearchKey1(object1) > getSearchKey1(object2))
					return 1;
				else if (getSearchKey1(object1) < getSearchKey1(object2))
					return -1;
				else {
					if (getSearchKey2(object1) > getSearchKey2(object2))
						return 1;
					else if (getSearchKey2(object1) < getSearchKey2(object2))
						return -1;
					else
						return 0;
				}
			}
		});
	}

	@Override
	public Object getKey(Object obj) {
		return null;
	}

	@Override
	protected void addContent(Object obj) {
		list.add(obj);
	}

	public List search(long key1) {
		List rs = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			if (getSearchKey1(list.get(i)) != key1 && rs.size() == 0)
				continue;
			if (getSearchKey1(list.get(i)) != key1 && rs.size() != 0)
				break;
			rs.add(list.get(i));
		}
		return rs;
	}
	
	public Object search(long key1, long key2) {
		List list = search(key1);
		for (int i = 0; i < list.size(); i++) {
			if (getSearchKey2(list.get(i)) == key2)
				return list.get(i);
		}
		return null;
	}

	@Override
	public List getAll() {
		return list;
	}

}
