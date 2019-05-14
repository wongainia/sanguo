package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BloodTroop;

public class BloodTroopCache extends FileCache {

	public static String FILE_NAME = "blood_troop.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BloodTroop) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		BloodTroop troop = BloodTroop.fromString(line);
		return troop;
	}

	@SuppressWarnings("unchecked")
	public List<BloodTroop> getAll() {
		List<BloodTroop> troops = new ArrayList<BloodTroop>();
		Set<Entry<Integer, BloodTroop>> set = content.entrySet();
		for (Entry<Integer, BloodTroop> entry : set) {
			troops.add(entry.getValue());
		}
		Collections.sort(troops);
		return troops;
	}

	@SuppressWarnings("unchecked")
	public List<ArmInfoClient> getAllTroop() {
		List<ArmInfoClient> troops = new ArrayList<ArmInfoClient>();
		Set<Entry<Integer, BloodTroop>> set = content.entrySet();
		for (Entry<Integer, BloodTroop> entry : set) {
			BloodTroop bt = entry.getValue();
			try {
				ArmInfoClient aic = new ArmInfoClient(bt.getId(), bt.getCount());
				troops.add(aic);
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(troops, new Comparator<ArmInfoClient>() {

			@Override
			public int compare(ArmInfoClient obj1, ArmInfoClient obj2) {
				return obj1.getId() - obj2.getId();
			}
		});
		return troops;
	}
}
