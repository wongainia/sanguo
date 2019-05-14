package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.HeroExchangeResp;
import com.vikings.sanguo.model.HeroIdBaseInfoClient;
import com.vikings.sanguo.model.HeroRecruitExchange;
import com.vikings.sanguo.model.HeroRecruitExchangeData;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.ui.alert.HeroDetailTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroExchangeAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.hero_exchange_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			holder.rating = (ImageView) convertView.findViewById(R.id.rating);
			holder.limit = (TextView) convertView.findViewById(R.id.limit);
			holder.iconLayout = (ViewGroup) convertView
					.findViewById(R.id.iconLayout);
			holder.exchangeBtn = convertView.findViewById(R.id.exchangeBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HeroRecruitExchangeData data = (HeroRecruitExchangeData) getItem(position);
		HeroRecruitExchange exchange = data.getExchange();
		OtherHeroInfoClient ohic = null;
		if (!data.isInit()) {
			try {
				ohic = CacheMgr.heroInitCache.getOtherHeroInfoClient(0, 0,
						exchange.getSchemaId());
				data.setOhic(ohic);
			} catch (GameException e) {
				data.setOhic(null);
			}
		}

		ohic = data.getOhic();

		ItemBag bag = Account.store.getItemBag(Item.TYPE_HERO_SOUL,
				exchange.getItemId());

		if (null != ohic) {
			ViewUtil.setVisible(holder.iconLayout);
			ViewUtil.setVisible(holder.name);
			ViewUtil.setVisible(holder.count);
			ViewUtil.setVisible(holder.rating);
			ViewUtil.setVisible(holder.exchangeBtn);
			IconUtil.setHeroIconScale(holder.iconLayout, ohic);
			holder.iconLayout.setOnClickListener(new IconClickListener(ohic,
					exchange.getSoulSource()));
			ViewUtil.setRichText(holder.name, ohic.getColorTypeName() + " "
					+ ohic.getColorHeroName());
			boolean enable = true;
			int count = 0;
			if (null != bag)
				count = bag.getCount();
			if (count >= exchange.getAmount()) {
				ViewUtil.setRichText(holder.count, StringUtil.color(count + "/"
						+ exchange.getAmount(), R.color.color20));
			} else {
				ViewUtil.setRichText(holder.count, StringUtil.color(count + "/"
						+ exchange.getAmount(), R.color.color11));
				enable = false;
			}

			if (exchange.getVipLevel() > 0) {
				ViewUtil.setVisible(holder.limit);
				ViewUtil.setText(holder.limit, "VIP" + exchange.getVipLevel()
						+ "以上才能兑换");
			} else {
				ViewUtil.setHide(holder.limit);
			}

			if (Account.getCurVip().getLevel() >= exchange.getVipLevel()
					&& enable) {
				ViewUtil.enableButton(holder.exchangeBtn);
				holder.exchangeBtn
						.setOnClickListener(new ExchangeClickListener(exchange));
			} else {
				ViewUtil.disableButton(holder.exchangeBtn);
			}

		} else {
			ViewUtil.setGone(holder.iconLayout);
			ViewUtil.setGone(holder.name);
			ViewUtil.setGone(holder.count);
			ViewUtil.setGone(holder.limit);
			ViewUtil.setGone(holder.rating);
			ViewUtil.setGone(holder.exchangeBtn);
		}

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	private class ExchangeClickListener implements OnClickListener {
		private HeroRecruitExchange exchange;

		public ExchangeClickListener(HeroRecruitExchange exchange) {
			this.exchange = exchange;
		}

		@Override
		public void onClick(View v) {
			new HeroExchangeInvoker(exchange).start();
		}
	}

	private class IconClickListener implements OnClickListener {
		public HeroIdBaseInfoClient hibic;
		public String soulSource;

		public IconClickListener(HeroIdBaseInfoClient hibic, String soulSource) {
			this.hibic = hibic;
			this.soulSource = soulSource;
		}

		@Override
		public void onClick(View v) {
			new HeroDetailTip(hibic, soulSource).show();
		}

	}

	static class ViewHolder {
		ViewGroup iconLayout;
		ImageView rating;
		TextView name, count, limit;
		View exchangeBtn;
	}

	private class HeroExchangeInvoker extends BaseInvoker {
		private HeroRecruitExchange exchange;

		private HeroExchangeResp resp;

		public HeroExchangeInvoker(HeroRecruitExchange exchange) {
			this.exchange = exchange;
		}

		@Override
		protected String loadingMsg() {
			return "将领兑换";
		}

		@Override
		protected String failMsg() {
			return "兑换失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().heroExchange(exchange.getSchemaId());
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRi(), true, false, false);
			ctr.alert("恭喜你招募到 " + resp.getHic().getHeroFullName());
		}

	}

}
