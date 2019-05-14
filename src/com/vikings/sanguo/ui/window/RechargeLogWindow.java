package com.vikings.sanguo.ui.window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.RechargeLog;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.RechargeLogAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class RechargeLogWindow extends CustomBaseListWindow {
	private static final String TAG = "RechargeLogWindow";

	@Override
	protected void init() {
		super.init("充值记录");
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new RechargeLogAdapter();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		int start = resultPage.getCurIndex();
		int count = resultPage.getPageSize();

		String orderQueryUrl = Config.rechargeUrl + "/charge/orderQuery";

		try {
			JSONObject params = new JSONObject();
			params.put("userId", Account.user.getId());
			params.put("start", start);
			params.put("count", count);
			params.put("sid", Config.serverId);
			params.put("game", Config.gameId);

			String json = HttpConnector.getInstance().httpPost(orderQueryUrl,
					params);
			JSONObject rs = new JSONObject(json);

			if (0 != rs.getInt("rs"))
				throw new GameException(rs.getString("error"));
			else {
				int total = rs.getInt("total");
				if (0 != total) {
					JSONArray array = rs.getJSONArray("logs");
					ArrayList<RechargeLog> ls = new ArrayList<RechargeLog>(
							array.length());
					ArrayList<Integer> idList = new ArrayList<Integer>(
							array.length());

					// 获取RechargeLog中的targetIf列表
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						int targetId = obj.getInt("target_id");
						if (Account.user.getId() != targetId)
							idList.add(targetId);
					}

					List<BriefUserInfoClient> user = null;
					if (!idList.isEmpty())
						user = CacheMgr.getUser(idList);

					for (int i = 0; i < array.length(); i++) {
						RechargeLog log = new RechargeLog();
						JSONObject obj = array.getJSONObject(i);

						// 充值金额
						log.setRmb(obj.getInt("amount_rmb"));

						// 充值对象
						int targetId = obj.getInt("target_id");
						if (targetId == Account.user.getId())
							log.setRechargeUser(Account.user.bref());
						else {
							if (null != user) {
								for (int j = 0; j < user.size(); j++) {
									if (user.get(j).getId() == targetId)
										log.setRechargeUser(user.get(j));
								}
							}
						}

						// 充值时间
						log.setTime(obj.getLong("notify_time"));

						// 充值渠道
						log.setChannel(obj.getInt("channel"));

						// 充值元宝
						log.setCash(obj.getInt("amount_game"));
						ls.add(log);
					}

					resultPage.setTotal(total);
					resultPage.setResult(ls);
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new GameException("哎呀，出错了，稍后请重试!!");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new GameException("网络异常,请重试");
		}
	}

	@Override
	public void handleItem(Object o) {
	}

	public void open() { // User user
		this.doOpen();
		this.firstPage();
		ViewUtil.setGone(window, R.id.listView);
		ViewUtil.setGone(window, R.id.emptyShow);
	}

	@Override
	protected void updateUI() {
		super.updateUI();
		dealwithEmptyAdpter();
	}

	@Override
	protected String getEmptyShowText() {
		return "没有充值记录";
	}

}
