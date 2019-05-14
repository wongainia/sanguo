package com.vikings.sanguo.cache;

import com.vikings.sanguo.utils.StringUtil;

public class PropRouletteCommonCache extends FileCache {

	private static final String FILE_NAME = "prop_roulette_common.csv";
	private int cost1Layer, cost2Layers, cost3Layers;

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		this.cost1Layer = StringUtil.removeCsvInt(buf);
		this.cost2Layers = StringUtil.removeCsvInt(buf);
		this.cost3Layers = StringUtil.removeCsvInt(buf);
		return line;
	}

	public int getCost1Layer() {
		return cost1Layer;
	}

	public int getCost2Layers() {
		return cost2Layers;
	}


	public int getCost3Layers() {
		return cost3Layers;
	}

}
