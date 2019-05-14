package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BuildingProp;

public class BuildingPropCache extends FileCache {

	private static final String FILE_NAME = "building_prop.csv";

	private TreeMap<Integer, ArrayList<BuildingProp>> buildingProps;

	@Override
	public void init() throws GameException {
		buildingProps = new TreeMap<Integer, ArrayList<BuildingProp>>();
		super.init();
	}

	@Override
	protected void addContent(Object obj) {
		super.addContent(obj);
		BuildingProp b = (BuildingProp) obj;
		if (!buildingProps.containsKey(b.getType())) {
			buildingProps.put(b.getType(), new ArrayList<BuildingProp>());
		}
		buildingProps.get(b.getType()).add(b);
	}

	@Override
	public Object fromString(String line) {
		return BuildingProp.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		return ((BuildingProp) obj).getId();
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	/**
	 * 根据类型取建筑
	 * 
	 * @param type
	 * @return
	 */
	public List<BuildingProp> getBuildingsByType(int type) {
		if (buildingProps.containsKey(type))
			return buildingProps.get(type);
		else 
			return new ArrayList<BuildingProp>();
	}

	/**
	 * 根据建筑类型取初始1级建筑
	 * 
	 * @param type
	 * @return
	 */
	public BuildingProp getInitialBuildingByType(int type) {
		BuildingProp buildingProp = null;
		List<BuildingProp> list = getBuildingsByType(type);
		for (BuildingProp prop : list) {
			if (prop.getLevel() == 1)
				buildingProp = prop;
		}
		return buildingProp;
	}

	public BuildingProp getBuildingByTypeAndLevel(int type, short level) {
		BuildingProp buildingProp = null;
		List<BuildingProp> list = getBuildingsByType(type);
		for (BuildingProp prop : list) {
			if (prop.getLevel() == level)
				buildingProp = prop;
		}
		return buildingProp;
	}


	public List<BuildingProp> getInitialBuildingsByMainType(short mainType){
		List<BuildingProp> list = new ArrayList<BuildingProp>();
		for(Integer type:buildingProps.keySet()){
			BuildingProp b = getInitialBuildingByType(type);
			if(b == null)continue;
			if(b.getMainType() == mainType)
				list.add(b);
		}
		return list;
	}
}
