package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class FiefSearchTip extends CustomConfirmDialog implements
		OnClickListener {
	private Button exactBtn; // 精确查找
	private Button idxBtn; // 位置坐标
	private Button favorBtn; // 领地收藏夹
	private Button resourceBtn; // 按资源查找

	public FiefSearchTip() {
		super("查  找", CustomConfirmDialog.DEFAULT);
		setCloseBtn();

		ViewUtil.setBold((TextView) content.findViewById(R.id.mode));

		exactBtn = (Button) content.findViewById(R.id.exactBtn);
		ViewUtil.setVisible(exactBtn);
		ViewUtil.setBold(exactBtn);
		exactBtn.setOnClickListener(this);

		idxBtn = (Button) tip.findViewById(R.id.idxBtn);
		ViewUtil.setVisible(idxBtn);
		ViewUtil.setBold(idxBtn);
		idxBtn.setOnClickListener(this);

		favorBtn = (Button) tip.findViewById(R.id.favorBtn);
		ViewUtil.setVisible(favorBtn);
		ViewUtil.setBold(favorBtn);
		favorBtn.setOnClickListener(this);

		resourceBtn = (Button) tip.findViewById(R.id.resourceBtn);
		ViewUtil.setVisible(resourceBtn);
		ViewUtil.setBold(resourceBtn);
		resourceBtn.setOnClickListener(this);

		ViewUtil.setText(content, R.id.mode, "--请选择查找方式--");
	}

	@Override
	public void onClick(View v) {
		if (v == exactBtn) {
			dismiss();
			new FiefExactSearchTip().show();
		} else if (v == idxBtn) {
			dismiss();
			new FiefPositionSearchTip().show();
		} else if (v == favorBtn) {
			dismiss();
			if (Account.hasFavorateFief())
				new FavorFiefSearchTip(Constants.FAVOR).show();
			else
				controller.alert("没有收藏领地!");
		} else if (v == resourceBtn) {
			dismiss();
			if (Account.user.getCurVip().getLevel() < CacheMgr.dictCache
					.getDictInt(Dict.TYPE_ADVANCED_RESOURCE, 1)) {
				new ShopHintTip("您的VIP等级不足，不能查看高级资源点，请先将VIP等级提升至 "
						+ StringUtil.color(
								CacheMgr.dictCache.getDictInt(
										Dict.TYPE_ADVANCED_RESOURCE, 1) + "",
								R.color.color11) + " 级").show();
			} else {
				new AdvancedRessourceNameTip().show();
			}
		}
	}

	@Override
	public View getContent() {
		return Config.getController().inflate(R.layout.alert_fief_search, tip,
				false);
	}
}
