package com.vikings.sanguo.model;

import java.util.List;

import com.vikings.sanguo.protos.HotUserScoreInfo;
import com.vikings.sanguo.protos.RoleAttrInfo;
import com.vikings.sanguo.utils.CalcUtil;

public class FatSheepData {
	private BriefFiefInfoClient bfic;
	private HotUserScoreInfo husi;

	public BriefFiefInfoClient getBfic() {
		return bfic;
	}

	public void setBfic(BriefFiefInfoClient bfic) {
		this.bfic = bfic;
	}

	public HotUserScoreInfo getHusi() {
		return husi;
	}

	public void setHusi(HotUserScoreInfo husi) {
		this.husi = husi;
	}

	// 8.29，与林洋确认：除了肥羊中scale表示scaleId，其他都是规模上下限值
	public String getIcon() {
		if (null == bfic || null == husi)
			return "";
		// return bfic.getManorFiefIcon(husi.getScale().intValue(),
		// bfic.getId());
		return bfic.getManorFiefIconByScaleId(husi.getScale().intValue());
	}

	public String getScaleName() {
		if (null == bfic || null == husi)
			return "";
		// return bfic.getManorFiefName(husi.getScale().intValue(),
		// bfic.getId());
		return bfic.getManorFiefNameByScaleId(husi.getScale().intValue());
	}

	public String getLordCountry() {
		if (null == bfic)
			return "";
		return bfic.getCountry();
	}

	public String getLordName() {
		if (null == bfic || null == bfic.getLord())
			return "";
		return bfic.getLord().getNickName();
	}

	public int getUnitCount() {
		if (null == bfic)
			return 0;
		return bfic.getUnitCount();
	}

	public int getTotalHeroInFief() {
		if (null == bfic)
			return 0;
		return bfic.getHeroCount();
	}

	public String getStateIcon() {
		if (null == bfic)
			return "";
		return bfic.getStateIcon();
	}

	public String getResource() {
		if (null == husi)
			return "";
		List<RoleAttrInfo> infos = husi.getInfosList();
		StringBuilder buf = new StringBuilder();
		for (RoleAttrInfo info : infos) {
			buf.append(
					"#"
							+ ReturnInfoClient.getAttrTypeIconName(info.getId()
									.intValue()) + "#").append(
					CalcUtil.turnToTenThousand(info.getValue().intValue())
							+ " ");
		}
		return buf.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FatSheepData other = (FatSheepData) obj;
		if (null == getBfic() || null == other.getBfic())
			return false;
		if (getBfic().getId() != other.getBfic().getId())
			return false;
		return true;
	}
}
