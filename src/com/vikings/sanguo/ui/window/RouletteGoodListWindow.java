package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.RouletteSacrificeResp;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.PropRouletteItem;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.RouletteGoodData;
import com.vikings.sanguo.protos.KeyValue;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.RouletteGoodAdapter;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class RouletteGoodListWindow extends CustomBaseListWindow implements
		CallBack {

	private List<RouletteGoodData> datas;
	private TextView gradientMsg;

	@Override
	protected void init() {
		super.init("馈赠美人");
		setContentBelowTitle(R.layout.gradient_msg);
		gradientMsg = (TextView) window.findViewById(R.id.gradientMsg);
		gradientMsg.setTextSize(12);
		setGoodDesc(0, 0);
		setBottomButton("确定馈赠", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RouletteGoodInvoker().start();
			}
		});
		setBottomPadding();
	}

	public void open() {
		this.doOpen();
		firstPage();
	}

	private void setGoodDesc(int itemCount, float good) {
		ViewUtil.setText(gradientMsg,
				"已选择" + itemCount + "颗宝石,可增加" + CalcUtil.format(good) + "%的好感");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		datas = new ArrayList<RouletteGoodData>();
		List<PropRouletteItem> items = CacheMgr.propRouletteItemCache.getAll();
		Collections.sort(items, new Comparator<PropRouletteItem>() {

			@Override
			public int compare(PropRouletteItem object1,
					PropRouletteItem object2) {
				return object1.getItemId() - object2.getItemId();
			}

		});
		for (PropRouletteItem prop : items) {
			int itemId = prop.getItemId();
			ItemBag itemBag = Account.store.getItemBag(itemId);
			if (null != itemBag && itemBag.getCount() > 0) {
				RouletteGoodData data = new RouletteGoodData();
				data.setItemId(itemId);
				data.setItemBag(itemBag);
				data.setProp(prop);
				datas.add(data);
			}
		}
		resultPage.setResult(datas);
		resultPage.setTotal(datas.size());
	}

	@Override
	public void handleItem(Object o) {

	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new RouletteGoodAdapter(this);
	}

	@Override
	public void onCall() {
		if (null != adapter) {
			float[] values = ((RouletteGoodAdapter) adapter).getCurValue();
			setGoodDesc((int) values[0], values[1]);
		}
	}

	private class RouletteGoodInvoker extends BaseInvoker {
		private List<RouletteGoodData> removeList = new ArrayList<RouletteGoodData>();
		private RouletteSacrificeResp resp;
		private int old;

		@Override
		protected String loadingMsg() {
			return "馈赠";
		}

		@Override
		protected String failMsg() {
			return "馈赠失败";
		}

		@Override
		protected void fire() throws GameException {
			old = Account.user.getRouletteGood();
			List<KeyValue> list = new ArrayList<KeyValue>();
			for (Object obj : adapter.getContent()) {
				RouletteGoodData data = (RouletteGoodData) obj;
				if (data.getSelCount() > 0) {
					KeyValue keyValue = new KeyValue();
					keyValue.setKey(data.getItemId());
					keyValue.setValue(data.getSelCount());
					list.add(keyValue);
				}
			}
			if (!list.isEmpty())
				resp = GameBiz.getInstance().rouletteSacrifice(list);
			else
				throw new GameException("请选择馈赠宝石");
			for (Object obj : adapter.getContent()) {
				RouletteGoodData data = (RouletteGoodData) obj;
				boolean has = data.reduce();
				if (!has)
					removeList.add(data);
			}
		}

		@Override
		protected void onOK() {
			if (!removeList.isEmpty()) {
				for (RouletteGoodData data : removeList) {
					if (adapter.getContent().contains(data))
						adapter.removeItem(data);
				}
				adapter.notifyDataSetChanged();
			}
			controller.updateUI(resp.getRic(), true);
			controller
					.alert("好感度增加了"
							+ CalcUtil.format((Account.user.getRouletteGood() - old) / 10f)
							+ "%");
		}
	}

}
