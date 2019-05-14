package com.vikings.sanguo.cache;

import java.util.ArrayList;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.NpcClientProp;
import com.vikings.sanguo.model.BriefUserInfoClient;

public class NpcClientPropCache extends LazyLoadCache {
	private static final String FILE_NAME = "npc_client_prop.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((NpcClientProp) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return NpcClientProp.fromString(line);
	}

	public BriefUserInfoClient getNpcUser(int id) throws GameException {
		NpcClientProp prop = (NpcClientProp) get(id);
		BriefUserInfoClient user = new BriefUserInfoClient();
		user.setId(id);
		user.setImage(prop.getIconId());
		user.setSex((int) prop.getSex());
		user.setNickName(prop.getNickName());
		user.setLevel((int) prop.getLevel());
		user.setBirthday(prop.getBirthday());
		return user;
	}
	
	public boolean containKey(Integer id) {
		return getContent().containsKey(id);
	}
	
	public ArrayList<String> getNPCIconName() {
		ArrayList<String> name = new ArrayList<String>();
		for(Object o: getContent().values()){
			NpcClientProp np = (NpcClientProp)o;
			name.add(np.getIconName());
		}
		return name;
	}
}
