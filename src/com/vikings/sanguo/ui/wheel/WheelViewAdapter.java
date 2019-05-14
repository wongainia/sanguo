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

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

public interface WheelViewAdapter {
	//获取Item个数
	public int getItemsCount();
	
	//Get a View that displays the data at the specified position in the data set
	public View getItem(int index, View convertView, ViewGroup parent);

	//Get a View that displays an empty wheel item placed before the first or after the last wheel item.
	public View getEmptyItem(View convertView, ViewGroup parent);
	
	public void registerDataSetObserver(DataSetObserver observer);
	
	void unregisterDataSetObserver (DataSetObserver observer);
}
