package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ServerUserData {
	private ServerData serverData;
	private List<AccountPswInfoClient> infos = new ArrayList<AccountPswInfoClient>();

	public ServerData getServerData() {
		return serverData;
	}

	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}

	public List<AccountPswInfoClient> getInfos() {
		return infos;
	}

	public void setInfos(List<AccountPswInfoClient> infos) {
		this.infos = infos;
	}

	public void add(AccountPswInfoClient info) {
		if (!infos.contains(info))
			infos.add(info);
	}

	public void sort() {
		if (infos.size() <= 1)
			return;
		Collections.sort(infos, new Comparator<AccountPswInfoClient>() {

			@Override
			public int compare(AccountPswInfoClient client1,
					AccountPswInfoClient client2) {
				return client2.getLevel() - client1.getLevel();
			}
		});
	}

	public int getUserSize() {
		return infos.size();
	}
}
