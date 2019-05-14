/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-4 下午5:11:47
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.ArrayList;

import com.vikings.sanguo.model.Country;

public class CountryCache extends FileCache {

	public final static String FILE_NAME = "prop_country.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((Country) obj).getProvinceId();
	}

	@Override
	public Object fromString(String line) {
		return Country.fromString(line);
	}

	public ArrayList<Country> getAll() {
		return new ArrayList<Country>(content.values());
	}

	public Country getCountry(int countryId) {
		for (Object o : content.values()) {
			Country c = (Country) o;
			if (c.getCountryId() == countryId)
				return c;
		}
		return new Country();
	}

	public Country getCountryByProvice(int proviceId) {
		try {
			return (Country) get(proviceId);
		} catch (Exception e) {
			return new Country();
		}
	}

}
