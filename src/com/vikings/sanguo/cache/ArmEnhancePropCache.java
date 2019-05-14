/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午3:06:29
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.vikings.sanguo.model.ArmEnhanceProp;

public class ArmEnhancePropCache extends ArrayFileCache{
	public static String FILE_NAME = "arm_enhance_prop.csv";
	
	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return ArmEnhanceProp.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((ArmEnhanceProp)obj).getArmId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((ArmEnhanceProp)obj).getPropId();
	}

	public List<Integer> armId(){
		HashSet<Integer> set = new HashSet<Integer>();
		for(Object a: getAll()){
			ArmEnhanceProp p = (ArmEnhanceProp)a;
			set.add(p.getArmId());
		}
		ArrayList<Integer> ls = new ArrayList<Integer>(set);
		Collections.sort(ls);
		return ls;
	}
	
}
