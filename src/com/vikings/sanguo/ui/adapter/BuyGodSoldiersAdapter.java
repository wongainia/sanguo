package com.vikings.sanguo.ui.adapter;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.battle.anim.UncoverGodSoldierAnim;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.BuyBattleUnitResp;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.MoveTroopInfoClient;
import com.vikings.sanguo.model.Poker;
import com.vikings.sanguo.model.PokerInfoclient;
import com.vikings.sanguo.model.PokerPrice;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.ui.alert.WarEndInfromTip;
import com.vikings.sanguo.utils.ViewUtil;

public class BuyGodSoldiersAdapter extends ObjectAdapter {
	private RichBattleInfoClient rbic;
	private PokerPrice pp;
	private CallBack cb;
	private MoveTroopInfoClient moveTroopInfo;

	public BuyGodSoldiersAdapter(RichBattleInfoClient rbic, CallBack cb) {
		this.rbic = rbic;
		this.cb = cb;
		pp = rbic.getNextPokerPrice();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			ViewHolder holder = new ViewHolder();
			holder.front = convertView.findViewById(R.id.front);
			holder.reverse = convertView.findViewById(R.id.reverse);
			holder.armCount = (TextView) convertView
					.findViewById(R.id.armCount);
			holder.armIcon = (ImageView) convertView.findViewById(R.id.armIcon);
			holder.armName = (TextView) convertView.findViewById(R.id.armName);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		}

		setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
		ViewHolder holder = (ViewHolder) v.getTag();
		Poker poker = (Poker) getItem(index);

		if (rbic.getBattleInfo().getBbic().getPokerUnit() > 0) {
			if (!poker.isOpen()) {
				switchCard(false, holder);
				setReverse(v, holder, poker);
			} else {
				switchCard(true, holder);
				setFront(v, holder, poker);
			}
		} else {
			switchCard(true, holder);
			setFront(v, holder, poker);
		}
	}

	private void switchCard(boolean isOpen, ViewHolder holder) {
		if (isOpen) {
			ViewUtil.setGone(holder.reverse);
			ViewUtil.setVisible(holder.front);
		} else {
			ViewUtil.setVisible(holder.reverse);
			ViewUtil.setGone(holder.front);
		}
	}

	private void setFront(View v, ViewHolder holder, Poker poker) {
		holder.armName.setText(poker.front.getName());
		new ViewImgCallBack(poker.front.getImage(), holder.armIcon);
		// 用名称取图片的函数，会使画质下降，此处必须用resId取图片，避免出现锯齿，下同
		holder.front.setBackgroundResource(Config.getController().findResId(
				poker.front.getBackImg(), "drawable"));
		holder.armCount.setText("x" + poker.front.getRewardCount());
		v.setOnClickListener(null);
	}

	private void setReverse(View v, ViewHolder holder, Poker poker) {
		holder.price.setText(rbic.getNextPokerNeedCurrency(pp) + "");
		v.setOnClickListener(new ItemOnclickListener(poker));
	}

	@Override
	public int getLayoutId() {
		return R.layout.buy_soldires_grid;
	}

	static class ViewHolder {
		TextView price, armName, armCount;
		ImageView armIcon;
		View front, reverse;
	}

	class ItemOnclickListener implements View.OnClickListener {
		private Poker pk;

		public ItemOnclickListener(Poker pk) {
			this.pk = pk;
		}

		@Override
		public void onClick(View v) {
			int funds = rbic.getNextPokerNeedCurrency(pp);
			if (Account.user.getCurrency() < funds) {
				new ToActionTip(funds).show();
			} else {
				new UncoverInvoker(v, pk).start();
			}
		}
	}

	private class UncoverInvoker extends BaseInvoker {
		private Poker poker;
		private BuyBattleUnitResp resp;
		private View view;

		public UncoverInvoker(View view, Poker poker) {
			this.view = view;
			this.poker = poker;
		}

		@Override
		protected String loadingMsg() {
			return "正在揭牌";
		}

		@Override
		protected String failMsg() {
			return "获取数据失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().buyBattleUnit(rbic.getId(),
					this.poker.getIndex());
			moveTroopInfo = resp.getMoveTroopInfo();
			moveTroopInfo.setUser(Account.user.bref());
			rbic.addGodSoldier(moveTroopInfo);

			List<ArmInfoClient> armInfoList = moveTroopInfo.getTroopInfo();

			ArmInfoClient ai = armInfoList.get(0);
			PokerInfoclient pokerInfoclient = new PokerInfoclient();
			pokerInfoclient.setIndex(poker.getIndex());
			pokerInfoclient.setResult(resp.getPokeResult());
			pokerInfoclient.setCount(ai.getCount());

			poker.setOpenPoker(poker.getIndex(), pokerInfoclient);

			if (!rbic.getBattleInfo().getBbic().getPokerResult()
					.contains(pokerInfoclient)) {
				rbic.getBattleInfo().getBbic().getPokerResult()
						.add(pokerInfoclient);
			}
		}

		@Override
		protected void onOK() {
			SoundMgr.play(R.raw.sfx_buy);
			pp = rbic.getNextPokerPrice();
			setFront(view, (ViewHolder) view.getTag(), poker);
			new UncoverGodSoldierAnim(view, new CallBack() {

				@Override
				public void onCall() {
					notifyDataSetChanged();
				}
			}).start();
			if (null != cb)
				cb.onCall();
		}

		@Override
		protected void onFail(GameException exception) {
			if (exception.getResult() == 1028) {
				Config.getController().goBack();
				Config.getController().goBack();
				new WarEndInfromTip(rbic.getEndType(), rbic.getBattleInfo()
						.getBbic().getDefendFiefid()).show();
			} else {
				super.onFail(exception);
			}
		}
	}

	public MoveTroopInfoClient getMoveTroopInfo() {
		return moveTroopInfo;
	}
}
