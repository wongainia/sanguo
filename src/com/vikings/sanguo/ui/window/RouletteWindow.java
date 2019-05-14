package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.RouletteAnimInvoker;
import com.vikings.sanguo.model.PropRoulette;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.thread.ImageLoader;
import com.vikings.sanguo.ui.RouletteView;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

//轮盘主界面
public class RouletteWindow extends CustomPopupWindow implements
		OnClickListener {
	private RouletteView layer1, layer2, layer3;
	private TextView percent;
	private View goodBtnLayout, btn1, btn2, btn3, specificBg;

	private RouletteAnimInvoker rouletteAnimInvoker;

	@Override
	protected void init() {
		super.init("凤仪亭");
		setContent(R.layout.roulette);
		specificBg = window.findViewById(R.id.specificBg);
		ViewUtil.setImage(specificBg, "roulette_bg.jpg");
		imageHolder.saveRef(specificBg);
		layer1 = (RouletteView) window.findViewById(R.id.layer1);
		layer1.init(getRoulettes((byte) 1), R.drawable.roulette_layer1, 1500);
		layer2 = (RouletteView) window.findViewById(R.id.layer2);
		layer2.init(getRoulettes((byte) 2), R.drawable.roulette_layer2, 1000);
		layer3 = (RouletteView) window.findViewById(R.id.layer3);
		layer3.init(getRoulettes((byte) 3), R.drawable.roulette_layer3, 800);
		percent = (TextView) window.findViewById(R.id.percent);
		goodBtnLayout = window.findViewById(R.id.goodBtnLayout);
		goodBtnLayout.setOnClickListener(this);
		btn1 = window.findViewById(R.id.btn1);
		btn2 = window.findViewById(R.id.btn2);
		btn3 = window.findViewById(R.id.btn3);
		setBtn(btn1, "转1个轮", CacheMgr.propRouletteCommonCache.getCost1Layer());
		setBtn(btn2, "转2个轮", CacheMgr.propRouletteCommonCache.getCost2Layers());
		setBtn(btn3, "转3个轮", CacheMgr.propRouletteCommonCache.getCost3Layers());

		rouletteAnimInvoker = new RouletteAnimInvoker(layer1, layer2, layer3);
		setLeftBtn("规则说明", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RuleSpecTip("凤仪亭规则说明", CacheMgr.uiTextCache
						.getTxt(UITextProp.ROULETTE_SPEC)).show();
			}
		});

	}

	private void setBtn(View view, String preStr, int price) {
		ViewUtil.setRichText(view, preStr + "<br/>#rmb#" + price + "元宝");
		view.setOnClickListener(this);
		view.setTag(price);
	}

	public void open() {
		new LoadImageInvoker().start();
	}

	@Override
	public void showUI() {
		super.showUI();
		setValue();

	}

	private void setValue() {
		setRightTxt("#rmb#" + Account.user.getCurrency());
		setRouletteGood();
	}

	private void setRouletteGood() {
		ViewUtil.setText(percent,
				CalcUtil.format(Account.user.getRealRouletteGood()) + "%");
	}

	@SuppressWarnings("unchecked")
	private List<PropRoulette> getRoulettes(byte layer) {
		return CacheMgr.propRouletteCache.search(layer);
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	@Override
	public void onClick(View v) {
		if (v == btn1) {
			if (!checkCurrency(btn1))
				return;
			rouletteAnimInvoker.start(1);
		} else if (v == btn2) {
			if (!checkCurrency(btn2))
				return;
			rouletteAnimInvoker.start(2);
		} else if (v == btn3) {
			if (!checkCurrency(btn3))
				return;
			rouletteAnimInvoker.start(3);
		} else if (v == goodBtnLayout) {
			controller.openRouletteGoodListWindow();
		}
	}

	protected boolean checkCurrency(View view) {
		Object obj = view.getTag();
		if (null != obj) {
			int price = (Integer) obj;
			if (Account.user.getCurrency() < price) {
				new ToActionTip(price).show();
				return false;
			}
		}
		return true;
	}

	private class LoadImageInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "加载中";
		}

		@Override
		protected String failMsg() {
			return "加载失败";
		}

		@Override
		protected void fire() throws GameException {
			List<PropRoulette> list = CacheMgr.propRouletteCache.getAll();
			for (PropRoulette prop : list) {
				ImageLoader.getInstance().downloadInCase(prop.getIcon(),
						Config.imgUrl);
			}
		}

		@Override
		protected void onOK() {
			doOpen();
		}

	}
}
