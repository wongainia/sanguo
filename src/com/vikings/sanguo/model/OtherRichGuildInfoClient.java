package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.OtherRichGuildInfo;

public class OtherRichGuildInfoClient {
	private BaseGuildInfoClient ogic; // 家族基本信息
	private List<Integer> members; // 家族成员

	public BaseGuildInfoClient getOgic() {
		return ogic;
	}

	public void setOgic(BaseGuildInfoClient ogic) {
		this.ogic = ogic;
	}

	public List<Integer> getMembers() {
		return members;
	}

	public void setMembers(List<Integer> members) {
		this.members = members;
	}

	public List<Integer> getMembersSequence() {
		synchronized (members) {
			if (null != ogic) {
				int leader = ogic.getLeader();
				members.remove(Integer.valueOf(leader));
				members.add(0, Integer.valueOf(leader));
			}
		}
		return members;
	}

	public static OtherRichGuildInfoClient convert(OtherRichGuildInfo info) {
		if (null == info)
			return null;
		OtherRichGuildInfoClient orgic = new OtherRichGuildInfoClient();

		orgic.setOgic(BaseGuildInfoClient.convert(info.getInfo()));

		if (info.getMemberInfos() != null) {
			orgic.setMembers(RichGuildInfoClient.convertGuildMembers(info
					.getMemberInfos().getInfosList()));
		} else {
			orgic.setMembers(new ArrayList<Integer>());
		}
		return orgic;
	}
	
	public boolean hasAltars() {
		return null != ogic && ogic.hasAltar();
	}
}
