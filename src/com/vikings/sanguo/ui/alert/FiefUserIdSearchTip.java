/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-7-24 下午2:48:19
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.regex.Pattern;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class FiefUserIdSearchTip extends CustomConfirmDialog {
	protected EditText id;

	public FiefUserIdSearchTip() {
		super("按玩家查找", CustomConfirmDialog.DEFAULT);

		setButton(CustomConfirmDialog.SECOND_BTN, "查找", searchL);
		setButton(CustomConfirmDialog.THIRD_BTN, "关闭", closeL);

		id = (EditText) tip.findViewById(R.id.id);
	}

	private OnClickListener searchL = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String xStr = id.getText().toString().trim();
			if (StringUtil.isNormalUserId(xStr)) {
				int id = Integer.valueOf(xStr);
				if (BriefUserInfoClient.isNPC(id)) {
					controller.alert("请输入有效的玩家ID!");
					return;
				}
				new QueryUserInvoker(id).start();
				dialog.dismiss();
			} else
				Config.getController().alert("请输入有效的玩家ID!");
		}
	};

	@Override
	public View getContent() {
		return Config.getController().inflate(R.layout.alert_search_userid,
				tip, false);
	}

	private class QueryUserInvoker extends BaseInvoker {
		private int userId;
		private BriefUserInfoClient user;

		public QueryUserInvoker(int userId) {
			this.userId = userId;
		}

		@Override
		protected String loadingMsg() {
			return "数据加载中";
		}

		@Override
		protected String failMsg() {
			return "获取数据失败";
		}

		@Override
		protected void fire() throws GameException {
			user = CacheMgr.userCache.getUser(userId);
		}

		@Override
		protected void onOK() {
			if (null != user)
				new FavorFiefSearchTip(Constants.USER, user).show();
			else
				controller.alert("玩家数据不存在!");
		}

	}
}
