package com.vikings.sanguo.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vikings.sanguo.config.Config;

@SuppressWarnings("unchecked")
abstract public class ObjectAdapter extends BaseAdapter {

	protected List content = new ArrayList();

	@Override
	public int getCount() {
		return content.size();
	}

	@Override
	public Object getItem(int position) {
		if (content == null || (content.size() == 0)) {
			return null;
		}
		if (position < 0)
			position = 0;
		if (position >= content.size())
			position = content.size() - 1;
		return content.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
		}
		// convertView = Config.getController().inflate(getLayoutId());
		this.setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	public int indexOf(Object o) {
		return content.indexOf(o);
	}

	/**
	 * 设置list中一行的显示样式
	 * 
	 * @param v
	 * @param o
	 * @param index
	 *            object在content list中的index
	 */
	abstract public void setViewDisplay(View v, Object o, int index);

	/**
	 * 设置list中的显示layout
	 * 
	 * @return
	 */
	abstract public int getLayoutId();

	public void fillEmpty() {

	}

	public void addItem(Object o) {
		this.content.add(o);
	}

	public void addItems(List ls) {
		if (ls != null && !ls.isEmpty())
			this.content.addAll(ls);
	}

	public void removeItem(Object o) {
		this.content.remove(o);
		notifyDataSetChanged();
	}

	public void addItems(Object[] list) {
		for (Object o : list) {
			this.content.add(o);
		}
	}

	public void insertItemAtHead(Object o) {
		if (content.contains(o))
			content.remove(o);
		this.content.add(0, o);
	}

	public void insertItemsAtHead(List ls) {
		for (int i = ls.size() - 1; i >= 0; i--) {
			insertItemAtHead(ls.get(i));
		}

	}

	public void insertItemNoRepeat(List ls) {
		if (ls == null)
			return;
		List temp = new ArrayList();
		for (Object o : ls) {
			if (!this.content.contains(o))
				temp.add(o);
		}
		if (temp.size() > 0)
			this.content.addAll(0, temp);
	}

	public void clear() {
		this.content.clear();
	}

	public List getContent() {
		return content;
	}

	public Object getLast() {
		if (this.content.size() == 0)
			return null;
		else
			return this.content.get(content.size() - 1);
	}

}
