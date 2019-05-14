package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.battle.anim.UncoverGodSoldierAnim;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.BattleLogInfoInvoker;
import com.vikings.sanguo.invoker.BloodAttackInvoker;
import com.vikings.sanguo.message.BloodPokerResp;
import com.vikings.sanguo.model.BloodPokerClient;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class BloodRewardWindow extends CustomPopupWindow implements
		OnClickListener {

	private View[] pokerViews = new View[5];
	private ViewGroup resultLayout, recordLayout, pokerLayout, descLayout;
	private ImageView resultBg, resultIcon;
	private TextView recordDesc, desc;
	private Button continueBtn, rewardBtn, giveUpBtn,battleReplayBtn;
	private boolean anim;// 是否播放动画

	@Override
	protected void init() {
		super.init("血战结算");
		setContent(R.layout.blood_reward);
		setCommonBg(R.drawable.blood_war_bg);
		pokerViews[0] = window.findViewById(R.id.poker1);
		pokerViews[1] = window.findViewById(R.id.poker2);
		pokerViews[2] = window.findViewById(R.id.poker3);
		pokerViews[3] = window.findViewById(R.id.poker4);
		pokerViews[4] = window.findViewById(R.id.poker5);
		resultLayout = (ViewGroup) window.findViewById(R.id.resultLayout);
		recordLayout = (ViewGroup) window.findViewById(R.id.recordLayout);
		pokerLayout = (ViewGroup) window.findViewById(R.id.pokerLayout);
		descLayout = (ViewGroup) window.findViewById(R.id.descLayout);
		resultBg = (ImageView) window.findViewById(R.id.resultBg);
		resultIcon = (ImageView) window.findViewById(R.id.resultIcon);
		recordDesc = (TextView) window.findViewById(R.id.recordDesc);
		desc = (TextView) descLayout.findViewById(R.id.desc);
		continueBtn = (Button) window.findViewById(R.id.continueBtn);
		continueBtn.setOnClickListener(this);
		rewardBtn = (Button) window.findViewById(R.id.rewardBtn);
		rewardBtn.setOnClickListener(this);
		giveUpBtn = (Button) window.findViewById(R.id.giveUpBtn);
		giveUpBtn.setOnClickListener(this);
		
		battleReplayBtn = (Button) window.findViewById(R.id.battleReplayBtn); 
		battleReplayBtn.setOnClickListener(this);
	}

	public void open() {
		this.doOpen();
	}

	@Override
	public void showUI() {
		super.showUI();
		setValue();
	}

	private void setValue() {
		if (Account.myLordInfo.hasReward()) {
			anim = PrefAccess.getBloodPokerAnim();
			if (anim) {
				ViewUtil.setImage(resultIcon, R.drawable.txt_kscj);
				ViewUtil.setHide(resultBg);
				int leftCount = Account.myLordInfo.getBloodPokerOpenLeftCount();
				ViewUtil.setText(recordDesc, "你还可以翻开" + leftCount + "张卡牌");
				ViewUtil.setGone(continueBtn);
				ViewUtil.setGone(rewardBtn);
				ViewUtil.setGone(battleReplayBtn);
				if (leftCount <= 0) {
					ViewUtil.setVisible(giveUpBtn);
				} else {
					ViewUtil.setGone(giveUpBtn);
				}

				setVipDesc();

			} else {
				if (Account.myLordInfo.isBloodLoss()) {
					ViewUtil.setImage(resultIcon, R.drawable.txt_tzsb);
					ViewUtil.setVisible(resultBg);
					ViewUtil.setImage(resultBg, R.drawable.bg_tzsb);
					ViewUtil.setText(recordDesc,
							"你挑战第" + (Account.myLordInfo.getLastRecord() + 1)
									+ "关失败了");
					ViewUtil.setGone(continueBtn);
					ViewUtil.setGone(giveUpBtn);
					ViewUtil.setVisible(rewardBtn);
					ViewUtil.setVisible(battleReplayBtn);
					//setVipDesc();
				} else {
					ViewUtil.setImage(resultIcon, R.drawable.txt_tzcg);
					ViewUtil.setVisible(resultBg);
					ViewUtil.setImage(resultBg, R.drawable.bg_tzcg);
					ViewUtil.setText(recordDesc,
							"你成功挑战了第" + Account.myLordInfo.getLastRecord()
									+ "关，即将获得以下奖励");
					ViewUtil.setVisible(continueBtn);
					continueBtn.setText("挑战第" + Account.myLordInfo.getBloodAttackRecord()+ "关");
					ViewUtil.setVisible(battleReplayBtn);
					ViewUtil.setGone(giveUpBtn);
					ViewUtil.setGone(rewardBtn);

					//ViewUtil.setVisible(descLayout);
					//ViewUtil.setText(desc, "继续挑战获得更丰厚奖励，但失败会损失一半");
				}
			}

			setPokers();
		} else {
			controller.goBack();
		}

	}

	protected void setVipDesc() {
		UserVip curVip = Account.user.getCurVip();
		int cur = 0;
		if (null != curVip)
			cur = curVip.getBloodPokerCount();
		UserVip nextVip = CacheMgr.userVipCache.getVipByPoker(cur + 1);
		if (null != nextVip) {
			ViewUtil.setVisible(descLayout);
			ViewUtil.setText(desc, "开通VIP " + nextVip.getLevel() + "，即可翻"
					+ nextVip.getBloodPokerCount() + "张纸牌");
		} else {
			ViewUtil.setHide(descLayout);
		}
	}

	private void setPokers() {
		List<BloodPokerClient> list = Account.myLordInfo.getLbic()
				.getBloodPokers();
		boolean loss = Account.myLordInfo.isBloodLoss();
		for (int i = 0; i < pokerViews.length; i++) {
			if (i < list.size())
				setPoker(i, pokerViews[i], list.get(i), loss);
		}
	}

	private void setPoker(final int i, final View view,
			final BloodPokerClient bpc, boolean loss) {
		if (!anim || bpc.isOpen()) {
			ViewUtil.setImage(view.findViewById(R.id.bg), bpc.getBloodReward()
					.getPokerImg());
			new ViewImgScaleCallBack(bpc.getBloodReward().getRewardImg(),
					view.findViewById(R.id.icon), Config.SCALE_FROM_HIGH
							* Constants.ITEM_ICON_WIDTH, Config.SCALE_FROM_HIGH
							* Constants.ITEM_ICON_HEIGHT);
			ViewUtil.setVisible(view.findViewById(R.id.icon));
			ViewUtil.setVisible(view.findViewById(R.id.desc));
			if (loss) {
				ViewUtil.setText(
						view.findViewById(R.id.desc),
						bpc.getBloodReward().getRewardName()
								+ "x"
								+ bpc.getCount(
										Account.myLordInfo.getLastRecord(),
										true));
			} else {
				ViewUtil.setText(
						view.findViewById(R.id.desc),
						bpc.getBloodReward().getRewardName()
								+ "x"
								+ bpc.getCount(
										Account.myLordInfo.getLastRecord(),
										false));
			}
			view.setOnClickListener(null);
		} else {
			ViewUtil.setImage(view.findViewById(R.id.bg),
					R.drawable.icon_card_bg);
			ViewUtil.setGone(view.findViewById(R.id.icon));
			ViewUtil.setGone(view.findViewById(R.id.desc));
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Account.myLordInfo.getBloodPokerOpenLeftCount() <= 0) {
						controller.alert(CacheMgr.errorCodeCache
								.getMsg((short) 525));
						return;
					}
					new PokerOpenInvoker(i, view, bpc).start();
				}
			});
		}
	}

//	@Override
//	protected byte getLeftBtnBgType() {
//		return WINDOW_BTN_BG_TYPE_CLICK;
//	}

	@Override
	public void onClick(View v) {
		if (v == continueBtn) {
			new ContinueInvoker(Account.myLordInfo.getBloodAttackRecord())
					.start();
		} else if (v == rewardBtn) {
			PrefAccess.setBloodPokerAnim(true);
			setValue();
		} else if (v == giveUpBtn) {			
			new GiveUpInvoker().start();
		}
		else if(v == battleReplayBtn)
		{
			long logId = PrefAccess.getBloodLogId();
			if (logId <= 0)
				controller.alert("日志不存在");
			else
				// 战斗日志 处理
				new BloodBattleLogInvoker(logId).start();
				//new BattleLogInfoInvoker(logId).start();
		}
	}
	
	private class ContinueInvoker extends BloodAttackInvoker {

		public ContinueInvoker(int num) {
			super(num);
		}

		@Override
		protected void onOK() {
			super.onOK();
			setValue();
		}
	}

	private class PokerOpenInvoker extends BaseInvoker {
		private View view;
		private BloodPokerClient bpc;
		private int index;

		public PokerOpenInvoker(int index, View view, BloodPokerClient bpc) {
			this.index = index;
			this.view = view;
			this.bpc = bpc;
		}

		@Override
		protected String loadingMsg() {
			return "翻牌中";
		}

		@Override
		protected String failMsg() {
			return "翻牌失败";
		}

		@Override
		protected void fire() throws GameException {
			BloodPokerResp resp = GameBiz.getInstance().bloodPoker(index);
			Account.myLordInfo.getLbic().updateBloodPoker(index, resp.getBpc());
		}

		@Override
		protected void onOK() {
			new UncoverGodSoldierAnim(view, new CallBack() {

				@Override
				public void onCall() {
					setValue();
				}
			}).start();
		}
	}

	private class GiveUpInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "结束翻牌";
		}

		@Override
		protected String failMsg() {
			return "结束翻牌";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().bloodReward();
			PrefAccess.setBloodPokerAnim(false);
		}

		@Override
		protected void onOK() {
			controller.closeAllPopup();
			controller.openBloodWindow();
		}

	}
	
	private class BloodBattleLogInvoker extends BattleLogInfoInvoker {
		public BloodBattleLogInvoker(long battleLogId) {
			super(battleLogId);
		}

		@Override
		protected void onOK() {
			 //new BattleWindow().open(battleDriver, null, true);;
			new BattleWindow().open(battleDriver, null, true,true);
		}
	}
	
//	//屏蔽返回键
//	@Override
//	public boolean goBack() {
//			return true;
//	}
}
