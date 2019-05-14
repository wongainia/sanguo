package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.model.HeroRecruitExchange;
import com.vikings.sanguo.model.HeroRecruitExchangeData;

public class HeroRecruitExchangeCache extends ArrayFileCache {
	public static String FILE_NAME = "hero_recruit_exchange.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((HeroRecruitExchange) obj).getSchemaId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((HeroRecruitExchange) obj).getItemId();
	}

	@Override
	public Object fromString(String line) {
		return HeroRecruitExchange.fromString(line);
	}

	public List<HeroRecruitExchangeData> getByCountry(byte country) {
		List<HeroRecruitExchangeData> datas = new ArrayList<HeroRecruitExchangeData>();
		for (Object obj : list) {
			HeroRecruitExchangeData data = new HeroRecruitExchangeData();
			HeroRecruitExchange exchange = (HeroRecruitExchange) obj;
			if (exchange.getTab() == country) {
				data.setExchange(exchange);
				datas.add(data);
			}
		}
		if (datas.size() > 1) {
			Collections.sort(datas, new Comparator<HeroRecruitExchangeData>() {

				@Override
				public int compare(HeroRecruitExchangeData obj1,
						HeroRecruitExchangeData obj2) {
					return obj1.getExchange().getSequence()
							- obj2.getExchange().getSequence();
				}

			});
		}
		return datas;
	}
}
