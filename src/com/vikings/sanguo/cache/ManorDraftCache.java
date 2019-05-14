package com.vikings.sanguo.cache;

import java.text.DecimalFormat;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.utils.CalcUtil;

public class ManorDraftCache extends ArrayFileCache {
	private static final String FILE_NAME = "manor_draft.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return ManorDraft.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((ManorDraft) obj).getArmId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((ManorDraft) obj).getBuildingId();
	}

	public ManorDraft getSingleDraftByBuildingId(int propId) {
		for (Object obj : list) {
			ManorDraft manorDraft = (ManorDraft) obj;
			if (manorDraft.getBuildingId() == propId)
				return manorDraft;
		}
		return null;
	}
	
	// 根据元宝计算可以训练的数量
	public int getCostByCurrency(ManorDraft md, int count) {
		return CalcUtil.upNum(count * md.getCost() / Constants.PER_TEN_THOUSAND);
	}
	
	//元宝单价保留小数点后三位
	public Double getCostByCurrencyOne(ManorDraft md){
		return Double.parseDouble(CalcUtil.format3(md.getCost() / Constants.PER_TEN_THOUSAND));
	}
}
