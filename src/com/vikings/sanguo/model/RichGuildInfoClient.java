package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ExGuildMemberInfo;
import com.vikings.sanguo.protos.GuildMemberInfo;
import com.vikings.sanguo.protos.RichGuildInfo;

public class RichGuildInfoClient {
	private int guildid;
	private GuildInfoClient gic; // 家族基本信息
	private List<Integer> members; // 家族成员
	private List<GuildJoinInfoClient> gjics;// 申请人列表
	private List<GuildInviteInfoClient> giics; // 邀请人列表
	private int version; // 版本号

	// 申请和邀请的 总请求数量
	public int getJoinAndInviteCount() {
		return getGjics().size() + getGiics().size();
	}

	public int getGuildid() {
		return guildid;
	}

	public void setGuildid(int guildid) {
		this.guildid = guildid;
	}

	public GuildInfoClient getGic() {
		return gic;
	}

	public void setGic(GuildInfoClient gic) {
		this.gic = gic;
	}

	public List<Integer> getMembers() {
		return members == null ? new ArrayList<Integer>() : members;
	}

	public List<Integer> getMembersSequence() {
		synchronized (members) {
			if (null != gic) {
				Collections.sort(members, new Comparator<Integer>() {

					@Override
					public int compare(Integer userId1, Integer userId2) {
						if (gic.isLeader(userId1) && !gic.isLeader(userId2))
							return -1;
						else if (!gic.isLeader(userId1)
								&& gic.isLeader(userId2))
							return 1;
						else if (gic.isLeader(userId1) && gic.isLeader(userId2))
							return 0;
						else {
							if (gic.isElder(userId1) && !gic.isElder(userId2))
								return -1;
							else if (!gic.isElder(userId1)
									&& gic.isElder(userId2))
								return 1;
							else if (gic.isElder(userId1)
									&& gic.isElder(userId2))
								return 0;
							else {
								return userId1 - userId2;
							}
						}

					}
				});
			}
		}
		return members;
	}

	public void setMembers(List<Integer> members) {
		this.members = members;
	}

	public boolean isMember(int userId) {
		if (null == members)
			return false;
		return members.contains(new Integer(userId));
	}

	// 是否长老
	public boolean isElder(int userId) {
		return gic.isElder(userId);
	}

	// 是否是族长
	public boolean isLeader(int userId) {
		return gic.isLeader(userId);
	}

	// 能否升级 策划规定只能升级一次
	public boolean canUpgrade() {
		if (gic.getLevel() == 1) {
			return true;
		}
		return false;
	}

	public List<GuildJoinInfoClient> getGjics() {
		return gjics == null ? new ArrayList<GuildJoinInfoClient>() : gjics;
	}

	public void setGjics(List<GuildJoinInfoClient> gjics) {
		this.gjics = gjics;
	}

	public List<GuildInviteInfoClient> getGiics() {
		return giics == null ? new ArrayList<GuildInviteInfoClient>() : giics;
	}

	public void setGiics(List<GuildInviteInfoClient> giics) {
		this.giics = giics;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public static RichGuildInfoClient convert(RichGuildInfo info)
			throws GameException {
		if (null == info)
			return null;
		RichGuildInfoClient rgic = new RichGuildInfoClient();
		rgic.setVersion(info.getCtrl().getVer());
		rgic.setGuildid(info.getGuildid());
		if (info.hasGuildInfo()) {
			rgic.setGic(GuildInfoClient.convert(info.getGuildInfo().getInfo()));
		}
		if (info.hasMemberInfos()) {
			rgic.setMembers(convertGuildMembers(info.getMemberInfos()
					.getInfosList()));
		} else {
			rgic.setMembers(new ArrayList<Integer>());
		}

		if (info.hasJoinInfos()) {
			rgic.setGjics(GuildJoinInfoClient.convertList(info.getJoinInfos()
					.getInfosList()));
		} else {
			rgic.setGjics(new ArrayList<GuildJoinInfoClient>());
		}

		if (info.hasInviteInfos()) {
			rgic.setGiics(GuildInviteInfoClient.convertList(info
					.getInviteInfos().getInfosList()));
		} else {
			rgic.setGiics(new ArrayList<GuildInviteInfoClient>());
		}
		return rgic;
	}

	public static List<Integer> convertGuildMembers(List<ExGuildMemberInfo> list) {
		List<Integer> members = new ArrayList<Integer>();
		if (null == list)
			return members;

		for (ExGuildMemberInfo info : list) {
			members.add(convertGuildMember(info.getInfo()));
		}
		return members;
	}

	public static int convertGuildMember(GuildMemberInfo info) {
		return info.getBi().getUserid();
	}

	public void update(RichGuildInfoClient rgic) {
		if (rgic == null)
			return;
		setGic(rgic.getGic());
		setMembers(rgic.getMembers());
		setGjics(rgic.getGjics());
		setGiics(rgic.getGiics());
		version = rgic.getVersion();
	}

	public void removeJoinInfo(int userId) {
		if (null == gjics || gjics.isEmpty())
			return;
		for (Iterator<GuildJoinInfoClient> iter = gjics.iterator(); iter
				.hasNext();) {
			GuildJoinInfoClient info = iter.next();
			if (info.getUserId() == userId) {
				iter.remove();
				break;
			}
		}
	}

	public void removeInviteInfo(int userId) {
		if (null == giics || giics.isEmpty())
			return;
		for (Iterator<GuildInviteInfoClient> iter = giics.iterator(); iter
				.hasNext();) {
			GuildInviteInfoClient info = iter.next();
			if (info.getUserId() == userId) {
				iter.remove();
				break;
			}
		}
	}

}
