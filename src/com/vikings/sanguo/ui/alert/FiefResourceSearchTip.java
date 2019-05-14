/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-14 上午11:59:32
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class FiefResourceSearchTip extends FiefSearchTypeTip implements
		OnClickListener {

	@Override
	public void onClick(View v) {
		if (v == type1Btn) {
			dismiss();
			new FatSheepTip().show();
		} else if (v == type2Btn) {
			dismiss();
			if (Account.user.getCurVip().getLevel() < CacheMgr.dictCache
					.getDictInt(Dict.TYPE_ADVANCED_RESOURCE, 1)) {
				new ShopHintTip("您的VIP等级不足，不能查看高级资源点，请先将VIP等级提升至 " + StringUtil.color(CacheMgr.dictCache.getDictInt(
						Dict.TYPE_ADVANCED_RESOURCE, 1) + "", R.color.color11)+" 级").show();
			} else {
				new AdvancedRessourceNameTip().show();
			}
		}
	}

	@Override
	protected void setClickListener() {
		type1Btn.setOnClickListener(this);
		type2Btn.setOnClickListener(this);
	}

	@Override
	protected void setBtnText() {
		ViewUtil.setText(type1Btn, "肥羊列表");
		ViewUtil.setBold(type1Btn);
		ViewUtil.setText(type2Btn, "高级资源点");
		ViewUtil.setBold(type2Btn);
		ViewUtil.setGone(type3Btn);
	}
}
