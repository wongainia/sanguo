package com.vikings.sanguo.model;

import java.io.Serializable;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 区域规模
 * 
 * @author susong
 * 
 */
public class FiefScale implements Serializable {

	private static final long serialVersionUID = 7432398031120243221L;

	protected int scaleId; // 规模类型
	protected String name; // Name（只针对主城）
	protected String icon;
	protected String image;
	protected int minPop; // 规模值下限
	protected int maxPop; // 规模值上限
	// protected byte wall;// 是否有城墙（0无、1有）
	protected int lockTime; // 围城时间（秒）
	// protected int defenseBuff;// 城防加成（1/100，对士兵核心属性加成）
	protected int needFood;// 围城粮草消耗（相当于士兵移动的格数）
	// protected int draftRate; // 征兵比率 千分比
	protected int minCount;// 出征最小兵力数量
	// protected int minTax; // 最低税收
	// protected int forceAtkBufId; // 强攻时城防加成效果ID
	protected int maxReinforceCount; // 同玩家对同领地出征、增援最大上限
	// protected int defBufId;// 城防加成效果ID（将领版）
	protected int troopRate; // 远征兵力倍率（基数100）
	protected int errCode;

	public void setScaleId(int scaleId) {
		this.scaleId = scaleId;
	}

	public int getScaleId() {
		return scaleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getMinPop() {
		return minPop;
	}

	public void setMinPop(int minPop) {
		this.minPop = minPop;
	}

	public int getMaxPop() {
		return maxPop;
	}

	public void setMaxPop(int maxPop) {
		this.maxPop = maxPop;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setTroopRate(int troopRate) {
		this.troopRate = troopRate;
	}

	// public byte getWall() {
	// return wall;
	// }
	//
	// public void setWall(byte wall) {
	// this.wall = wall;
	// }
	//
	// // 是否有城墙
	// public boolean hasWall() {
	// return this.wall == 1;
	// }

	public int getLockTime() {
		return lockTime;
	}

	public void setLockTime(int lockTime) {
		this.lockTime = lockTime;
	}

	// public int getDefenseBuff() {
	// return defenseBuff;
	// }
	//
	// public void setDefenseBuff(int defenseBuff) {
	// this.defenseBuff = defenseBuff;
	// }

	public int getNeedFood() {
		return needFood;
	}

	public void setNeedFood(int needFood) {
		this.needFood = needFood;
	}

	// public int getDraftRate() {
	// return draftRate;
	// }
	//
	// public void setDraftRate(int draftRate) {
	// this.draftRate = draftRate;
	// }

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}

	// public int getMinTax() {
	// return minTax;
	// }
	//
	// public void setMinTax(int minTax) {
	// this.minTax = minTax;
	// }
	//
	// public int getForceAtkBufId() {
	// return forceAtkBufId;
	// }
	//
	// public void setForceAtkBufId(int forceAtkBufId) {
	// this.forceAtkBufId = forceAtkBufId;
	// }

	public int getMaxReinforceCount() {
		return maxReinforceCount;
	}

	public void setMaxReinforceCount(int maxReinforceCount) {
		this.maxReinforceCount = maxReinforceCount;
	}

	// public int getDefBufId() {
	// return defBufId;
	// }
	//
	// public void setDefBufId(int defBufId) {
	// this.defBufId = defBufId;
	// }

	public int getErrCode() {
		return errCode;
	}

	public String getImage() {
		return image;
	}

	public int getTroopRate() {
		return troopRate;
	}

	public static FiefScale fromString(String csv) {
		FiefScale prop = new FiefScale();
		StringBuilder buf = new StringBuilder(csv);
		prop.setScaleId(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setName(StringUtil.removeCsv(buf));
		prop.setIcon(StringUtil.removeCsv(buf));
		prop.setImage(StringUtil.removeCsv(buf));
		prop.setMinPop(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setMaxPop(Integer.valueOf(StringUtil.removeCsv(buf)));
		// prop.setWall(Byte.valueOf(StringUtil.removeCsv(buf)));
		prop.setLockTime(Integer.valueOf(StringUtil.removeCsv(buf)));
		// prop.setDefenseBuff(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setNeedFood(Integer.valueOf(StringUtil.removeCsv(buf)));
		// prop.setDraftRate(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setMinCount(Integer.valueOf(StringUtil.removeCsv(buf)));
		// prop.setMinTax(Integer.valueOf(StringUtil.removeCsv(buf)));
		// prop.setForceAtkBufId(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setMaxReinforceCount(StringUtil.removeCsvInt(buf));
		// prop.setDefBufId(StringUtil.removeCsvInt(buf));
		prop.setTroopRate(StringUtil.removeCsvInt(buf));
		prop.setErrCode(StringUtil.removeCsvInt(buf));
		return prop;
	}
}
