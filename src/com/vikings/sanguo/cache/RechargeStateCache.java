package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.RechargeState;

public class RechargeStateCache extends FileCache {

	private static final String FILE_NAME = "recharge_state.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((RechargeState) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return RechargeState.fromString(line);
	}

	public RechargeState getRechargeState(byte id) {
		try {
			return (RechargeState) get(id);
		} catch (GameException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RechargeState> getRechargeStates(byte type) {
		List<RechargeState> list = new ArrayList<RechargeState>();
		Set<Entry<Byte, RechargeState>> set = content.entrySet();
		for (Entry<Byte, RechargeState> entry : set) {
			RechargeState state = entry.getValue();
			if (state.getType() == type) {
				list.add(state);
			}
		}
		if (list.size() > 1) {
			Collections.sort(list, new Comparator<RechargeState>() {

				@Override
				public int compare(RechargeState state1, RechargeState state2) {
					return state1.getSequence() - state2.getSequence();
				}
			});
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<RechargeState> getAll() {
		List<RechargeState> list = new ArrayList<RechargeState>();
		list.addAll(content.values());
		return list;
	}
}
