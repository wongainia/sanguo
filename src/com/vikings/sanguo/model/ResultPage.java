package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.config.Config;

/**
 * 取后台分页动态数据
 * 
 * @author Brad.Chen
 * 
 */
@SuppressWarnings("unchecked")
public class ResultPage {

	// 总数
	private int total;
	// 每页大小
	private short pageSize = (short) Config.getIntConfig("resultPageSize");

	// 当前index
	private int curIndex = 0;

	// 数据list
	private List result = new ArrayList();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public short getPageSize() {
		return pageSize;
	}

	public void setPageSize(short pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		if (curIndex < 0)
			return;
		this.curIndex = curIndex;
	}

	public List getResult() {
		if (result == null)
			return new ArrayList();
		else
			return result;
	}

	public void setResult(List result) {
		this.result = result;
	}

	public void addIndex(int increase) {
		// if (result == null)
		// return;
		// this.curIndex += this.result.size();
		this.curIndex += increase;
	}

	public boolean isReachEnd() {
		return this.curIndex >= total;
	}

	public void add(Object o) {
		this.result.add(o);
	}

	public void clearResult() {
		this.result = null;
	}
}
