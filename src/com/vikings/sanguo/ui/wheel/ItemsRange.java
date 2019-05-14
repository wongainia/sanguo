/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-24
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui.wheel;

//处理范围的类，标识起始索引及元素个数
public class ItemsRange {
	private int first;
	private int count;

    public ItemsRange() {
        this(0, 0);
    }
    
	public ItemsRange(int first, int count) {
		this.first = first;
		this.count = count;
	}
	
	public int getFirst() {
		return first;
	}
	
	public int getLast() {
		return getFirst() + getCount() - 1;
	}
	
	public int getCount() {
		return count;
	}
	
	public boolean contains(int index) {
		return index >= getFirst() && index <= getLast();
	}
}
