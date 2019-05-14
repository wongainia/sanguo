package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.thread.SetCountry;
import com.vikings.sanguo.utils.TileUtil;

public class UserLocInvoker extends BackgroundInvoker {

	private int province = 0;
	private int country = 0;

	private ReturnInfoClient ri;

	@Override
	protected void fire() throws GameException {
		if (Account.user == null || !Account.user.isValidUser())
			return;
		if (Config.getController().getCurLocation() != null) {
			long fiefId = TileUtil.tileId2FiefId(TileUtil.toTileId(Config
					.getController().getCurLocation()));
			province = CacheMgr.zoneCache.getProvince(fiefId);
			country = CacheMgr.countryCache.getCountryByProvice(province)
					.getCountryId();
		}
		UserAccountClient tmp = Account.user.emptyUser();
		tmp.setProvince(province);
		ri = GameBiz.getInstance().playerUpdate(tmp);
		if (country > 0)
			Account.user.setCountry(country);
	}

	@Override
	protected void onOK() {
		Config.getController().setAccountBarUser(Account.user);
		Config.getController().updateUI(ri, true);
	}

	@Override
	protected void afterFire() {
		if (Account.user.getCountry() == 0) {
			Config.getController().getHandler()
					.postDelayed(new SetCountry(), 30 * 1000);
		}
	}

}
