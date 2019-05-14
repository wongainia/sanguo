package com.vikings.sanguo.cache;

import android.util.Log;

import com.vikings.sanguo.model.FiefScale;
import com.vikings.sanguo.model.HolyProp;

public class FiefScaleCache extends FileCache {
	private static final String FILE_NAME = "manor_scale.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((FiefScale) obj).getScaleId();
	}

	@Override
	public Object fromString(String line) {
		return FiefScale.fromString(line);
	}

	public FiefScale getFiefScale(int scaleId, long fiefId){
		try {
			FiefScale fs = (FiefScale) get(scaleId);
			if (CacheMgr.holyPropCache.isHoly(fiefId)) {
				HolyProp hp = (HolyProp) CacheMgr.holyPropCache.get(fiefId);
				hp.setScaleId(fs.getScaleId());
				fs = hp;
			}
			return fs;
		} catch (Exception e) {
			Log.e("FiefScaleCache","err" , e);
			return null;
		}
	}

	public FiefScale getByScaleValue(int scaleValue, long fiefId){
		int scaleId = 0;
		for( Object o : content.values()){
			FiefScale fs = (FiefScale)o;
			if( scaleValue >= fs.getMinPop()  &&  scaleValue <= fs.getMaxPop()){
				scaleId = fs.getScaleId();
				break;
			}
		}
		return getFiefScale(scaleId, fiefId);
	}
	
//	public ArrayList<String> getFiefIconName() {
//		Iterator iter = content.entrySet().iterator();
//		ArrayList<String> name = new ArrayList<String>();
//		while (iter.hasNext()) {
//			Map.Entry entry = (Map.Entry) iter.next();
//			FiefScale val = (FiefScale) entry.getValue();
//			name.add(val.getIcon());
//		}
//		return name;
//	}
}
