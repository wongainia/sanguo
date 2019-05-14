/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-23
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.GambleData;
import com.vikings.sanguo.protos.MachinePlayType;

public class GambleCache extends FileCache{
	private static final String FILE_NAME = "prop_machine.csv";
	private ArrayList<GambleData> juniorData = new ArrayList<GambleData>(1);
	private ArrayList<GambleData> middleData = new ArrayList<GambleData>(1);
	private ArrayList<GambleData> advancedData = new ArrayList<GambleData>(1);
	
	@Override
	public synchronized void init() throws GameException {
		super.init();
		
		sort(juniorData);
		sort(middleData);
		sort(advancedData);
	}

	private void sort(ArrayList<GambleData> l) {
		if (l.size() > 0) {
			Collections.sort(l, new Comparator<Object>() {
				@Override
				public int compare(Object object1, Object object2) {
					GambleData gd1 = (GambleData) object1;
					GambleData gd2 = (GambleData) object2;
					return gd1.getSerialNum() - gd2.getSerialNum();
				}
			});
		}
	}
	
	@Override
	public Object fromString(String line) {
		return GambleData.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return null;
	}
	
	@Override
	protected void addContent(Object obj) {
		GambleData gd = (GambleData) obj;
		
		switch (gd.getMachineType()) {
		//1:初级，2：中级，3：高级
		case 1:
			juniorData.add(gd);
			break;
		case 2:
			middleData.add(gd);
			break;
		case 3:
			advancedData.add(gd);
			break;
		}
	}
	
	public ArrayList<GambleData> getJuniorData() {
		return juniorData;
	}
	
	public ArrayList<GambleData> getMiddleData() {
		return middleData;
	}
	
	public ArrayList<GambleData> getAdvancedData() {
		return advancedData;
	}
	
	public short getJuniorPrice() {
		if (juniorData.size() > 0)
			return juniorData.get(0).getPrice();
		else
			return 0;
	}
	
	public String getJuniorName() {
		if (juniorData.size() > 0)
			return juniorData.get(0).getMachineName();
		else
			return "";
	}
	
	public short getMiddlePrice() {
		if (middleData.size() > 0)
			return middleData.get(0).getPrice();
		else
			return 0;
	}
	
	public String getMiddleName() {
		if (middleData.size() > 0)
			return middleData.get(0).getMachineName();
		else
			return "";
	}
	
	public short getAdvancedPrice() {
		if (advancedData.size() > 0)
			return advancedData.get(0).getPrice();
		else
			return 0;
	}
	
	public String getAdvancedName() {
		if (advancedData.size() > 0)
			return advancedData.get(0).getMachineName();
		else
			return "高级幸运轮盘";
	}
	
	public int middleToJuniorRate() {
		int mSize = middleData.size();
		int jSize = juniorData.size();
		
		if (jSize > 0 && mSize > 0)
			return mSize / jSize;
		
		return 1;
	}
	
	public int advancedToJuniorRate() {
		int aSize = advancedData.size();
		int jSize = juniorData.size();
		
		if (jSize > 0 && aSize > 0)
			return aSize / jSize;
		
		return 1;
	}
	
	public short getPrice(MachinePlayType type) {
		if (MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR == type)
			return CacheMgr.gambleCache.getJuniorPrice();
		else if (MachinePlayType.MACHINE_PLAY_TYPE_MIDDLE == type) 
			return CacheMgr.gambleCache.getMiddlePrice();
		else if (MachinePlayType.MACHINE_PLAY_TYPE_SENIOR == type) 
			return CacheMgr.gambleCache.getAdvancedPrice();
		
		return 0;
	}
	
	public List<GambleData> getGambleData(MachinePlayType type) {
		if (MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR == type) 
			return CacheMgr.gambleCache.getJuniorData();
		else if (MachinePlayType.MACHINE_PLAY_TYPE_MIDDLE == type) 
			return CacheMgr.gambleCache.getMiddleData();
		else if (MachinePlayType.MACHINE_PLAY_TYPE_SENIOR == type)
			return CacheMgr.gambleCache.getAdvancedData();
		
		return new ArrayList<GambleData>();
	}
	
	public String getName(MachinePlayType type) {
		if (MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR == type)
			return CacheMgr.gambleCache.getJuniorName();
		else if (MachinePlayType.MACHINE_PLAY_TYPE_MIDDLE == type)
			return CacheMgr.gambleCache.getMiddleName();
		else if (MachinePlayType.MACHINE_PLAY_TYPE_SENIOR == type) 
			return CacheMgr.gambleCache.getAdvancedName();
		
		return "";
	}
}