package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.HeroSkillStudyResp;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.HeroSkillUpgrade;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.protos.RES_DATA_TYPE;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HeroSkillStudyTip extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_hero_skill;

	private ViewGroup curEffectLayout, nextEffectLayout, costLayout,
			upgradeLayout, othersLayout, iconLayout;
	private Button studyBtn, upgradeBtn, abandonBtn;
	private ImageView icon;
	private View rating;

	private HeroInfoClient hic;
	private HeroSkillSlotInfoClient hssic;
	private CallBack mCallBack;
	// 学习技能需要传入的技能
	private BattleSkill battleSkill;

	// 学习技能时调用
	public HeroSkillStudyTip(HeroInfoClient hic, HeroSkillSlotInfoClient hssic,
			BattleSkill battleSkill, CallBack mCallBack) {
		super();
		this.hic = hic;
		this.hssic = hssic;
		this.battleSkill = battleSkill;
		this.mCallBack = mCallBack;
	}

	// 升级技能调用
	public HeroSkillStudyTip(HeroInfoClient hic, HeroSkillSlotInfoClient hssic) {
		super();
		this.hic = hic;
		this.hssic = hssic;
	}

	public void show() {
		if (!hssic.hasSkill() && null == battleSkill) {
			controller.alert("请先选择要学习的技能");
			return;
		}
		super.show();
		curEffectLayout = (ViewGroup) content
				.findViewById(R.id.curEffectLayout);
		nextEffectLayout = (ViewGroup) content
				.findViewById(R.id.nextEffectLayout);
		costLayout = (ViewGroup) content.findViewById(R.id.costLayout);
		upgradeLayout = (ViewGroup) content.findViewById(R.id.upgradeLayout);
		othersLayout = (ViewGroup) content.findViewById(R.id.othersLayout);
		studyBtn = (Button) content.findViewById(R.id.studyBtn);
		upgradeBtn = (Button) content.findViewById(R.id.upgradeBtn);
		abandonBtn = (Button) content.findViewById(R.id.abandonBtn);
		icon = (ImageView) content.findViewById(R.id.icon);
		iconLayout = (ViewGroup) content.findViewById(R.id.iconLayout);
		rating = content.findViewById(R.id.rating);
		setValue();
	}

	private void setValue() {
		if (hssic.hasSkill()) {// 升级技能
			IconUtil.setSkillIcon(iconLayout, hssic.getBattleSkill());
			ViewUtil.setImage(rating, hssic.getBattleSkill().getRatingPic());
			ViewUtil.setText(curEffectLayout, R.id.desc, hssic.getBattleSkill()
					.getEffectDesc());
			final BattleSkill nextSkill = CacheMgr.battleSkillCache
					.getNextLevel(hssic.getBattleSkill());
			if (null != nextSkill) { // 可以升级
				setTitle(hssic.getBattleSkill().getName());
				ViewUtil.setVisible(nextEffectLayout);
				ViewUtil.setText(nextEffectLayout, R.id.desc,
						nextSkill.getEffectDesc());
				String cost = getCost(nextSkill.getId());
				if (!StringUtil.isNull(cost)) {
					ViewUtil.setVisible(costLayout);
					ViewUtil.setRichText(costLayout.findViewById(R.id.desc),
							cost, true);
				} else {
					ViewUtil.setGone(costLayout);
				}
				ViewUtil.setVisible(upgradeLayout);
				ViewUtil.setText(upgradeLayout, R.id.name, "升级成功：");
				ViewUtil.setText(upgradeLayout, R.id.desc,
						nextSkill.getUpgradeRate() + "%");
				ViewUtil.setGone(othersLayout);
				ViewUtil.setVisible(upgradeBtn);
				upgradeBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new UpgradeSkillInvoker(nextSkill).start();
					}
				});
				ViewUtil.setGone(studyBtn);
			} else { // 最高级
				setTitle(hssic.getBattleSkill().getName()/* + " MAX" */);
				ViewUtil.setGone(nextEffectLayout);
				ViewUtil.setGone(costLayout);
				ViewUtil.setGone(upgradeLayout);
				ViewUtil.setGone(othersLayout);
				ViewUtil.setGone(upgradeBtn);
				ViewUtil.setGone(studyBtn);
			}

			if (hssic.isStaticSkill()) {
				ViewUtil.setGone(abandonBtn);
			} else {
				ViewUtil.setVisible(abandonBtn);
				abandonBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new CommonCustomAlert("遗忘技能",
								CommonCustomAlert.DEFAULT, false, "是否遗忘当前【"
										+ hssic.getBattleSkill().getName()
										+ "】技能", "确定", new CallBack() {
									@Override
									public void onCall() {
										// 调用遗忘技能的接口
										new AbandonSkillInvoker().start();
									}
								}, "", null, "取消", true).show();
					}
				});
			}
		} else { // 学习技能
			IconUtil.setSkillIcon(iconLayout, battleSkill);
			ViewUtil.setImage(rating, battleSkill.getRatingPic());
			ViewUtil.setText(curEffectLayout, R.id.desc,
					battleSkill.getEffectDesc());
			setTitle(battleSkill.getName());
			ViewUtil.setGone(nextEffectLayout);
			String cost = getCost(battleSkill.getId());
			if (!StringUtil.isNull(cost)) {
				ViewUtil.setVisible(costLayout);
				ViewUtil.setRichText(costLayout.findViewById(R.id.desc), cost,
						true);
			} else {
				ViewUtil.setGone(costLayout);
			}
			ViewUtil.setVisible(upgradeLayout);
			ViewUtil.setText(upgradeLayout, R.id.name, "学习成功：");
			ViewUtil.setText(upgradeLayout, R.id.desc,
					battleSkill.getUpgradeRate() + "%");
			ViewUtil.setGone(othersLayout);
			ViewUtil.setVisible(studyBtn);
			studyBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new StudySkillInvoker(battleSkill).start();

				}
			});
			ViewUtil.setGone(abandonBtn);
			ViewUtil.setGone(upgradeBtn);
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	private String getCost(int skillId) {
		StringBuilder buf = new StringBuilder();
		ReturnInfoClient ric = getReturnInfoClient(skillId);
		List<ShowItem> showItems = ric.showRequire();
		for (ShowItem showItem : showItems) {
			buf.append("#"
					+ showItem.getImg()
					+ "# "
					+ showItem.getName()
					+ " x"
					+ showItem.getCount()
					+ "("
					+ StringUtil.color("" + showItem.getSelfCount(), (showItem
							.isEnough() ? R.color.color19 : R.color.color11))
					+ "<br/>" + ")");
		}
		if (buf.length() > 0) {
			int index = buf.lastIndexOf("<br/>");
			buf.delete(index, index + "<br/>".length());
		}
		return buf.toString();
	}

	@SuppressWarnings("unchecked")
	private ReturnInfoClient getReturnInfoClient(int skillId) {
		List<HeroSkillUpgrade> list = CacheMgr.heroSkillUpgradeCache
				.search(skillId);

		ReturnInfoClient ric = new ReturnInfoClient();
		for (HeroSkillUpgrade upgrade : list) {
			ric.addCfg(RES_DATA_TYPE.RES_DATA_TYPE_ITEM.getNumber(),
					upgrade.getItemID(), upgrade.getCount());
		}
		return ric;
	}

	private class StudySkillInvoker extends BaseInvoker {
		private BattleSkill battleSkill;
		private HeroSkillStudyResp heroSkillStudyResp;

		public StudySkillInvoker(BattleSkill battleSkill) {
			this.battleSkill = battleSkill;
		}

		@Override
		protected String loadingMsg() {
			return "修习技能";
		}

		@Override
		protected String failMsg() {
			return "修习技能失败";
		}

		@Override
		protected void fire() throws GameException {
			heroSkillStudyResp = GameBiz.getInstance().heroSkillStudy(
					hic.getId(), hssic.getId(), battleSkill.getId());
			if (heroSkillStudyResp.isSuccess()) {
				hssic.setSkillId(battleSkill.getId());
				hssic.setBattleSkill(battleSkill);
				for (HeroSkillSlotInfoClient it : hic.getSkillSlotInfos()) {
					if (it.getId() == hssic.getId()) {
						it.setSkillId(battleSkill.getId());
						it.setBattleSkill(battleSkill);
					}
				}

			}
		}

		@Override
		protected void onFail(GameException exception) {
			SoundMgr.play(R.raw.sfx_tips3);
			String msg = failMsg();
			if (!StringUtil.isNull(msg))
				msg = msg + exception.getErrorMsg();
			else
				msg = exception.getErrorMsg();
			Config.getController().alert("学习技能失败", "", msg, null, false);
		}

		@Override
		protected void onOK() {
			dismiss();
			if (heroSkillStudyResp.isSuccess()) {
				Config.getController().goBack();
				SoundMgr.play(R.raw.sfx_tips2);
				heroSkillStudyResp.getRi().setMsg(
						"技能修习成功<br/>" + hic.getHeroProp().getName() + "习得【"
								+ battleSkill.getName() + "】" + getLoss());
			} else {
				SoundMgr.play(R.raw.sfx_tips3);
				heroSkillStudyResp.getRi().setMsg(
						"技能修习失败<br/>" + hic.getHeroProp().getName()
								+ "未修习得该技能！" + getLoss());
			}
			ctr.updateUI(heroSkillStudyResp.getRi(), false, false, true);

			setValue();
			if (mCallBack != null) {
				mCallBack.onCall();
			}
		}

	}

	private class UpgradeSkillInvoker extends BaseInvoker {
		private BattleSkill battleSkill;
		private HeroSkillStudyResp heroSkillStudyResp;
		private ReturnInfoClient resultInfo;

		public UpgradeSkillInvoker(BattleSkill battleSkill) {
			this.battleSkill = battleSkill;
		}

		@Override
		protected String loadingMsg() {
			return "升级技能";
		}

		@Override
		protected String failMsg() {
			return "升级失败";
		}

		@Override
		protected void fire() throws GameException {
			heroSkillStudyResp = GameBiz.getInstance().heroSkillStudy(
					hic.getId(), hssic.getId(), battleSkill.getId());
			resultInfo = heroSkillStudyResp.getRi();
			if (heroSkillStudyResp.isSuccess()) {
				hssic.setSkillId(battleSkill.getId());
				hssic.setBattleSkill(battleSkill);
				hic.addSkill(hssic.getId(), battleSkill.getId(), battleSkill);
			}
		}

		@Override
		protected void onOK() {
			setValue();
			if (heroSkillStudyResp.isSuccess()) {
				resultInfo.setMsg("技能升级成功" + getLoss());
				SoundMgr.play(R.raw.sfx_tips2);
				setValue();
			} else {
				SoundMgr.play(R.raw.sfx_tips3);
				resultInfo.setMsg("技能升级失败<br/>看来这次运气不好，没升上去…" + getLoss());
			}
			ctr.updateUI(resultInfo, false, false, true);

		}
	}

	private String getLoss() {
		return "<br><br><font size='11' color='#BF1F2E'>对应数量的技能书已经被扣除</font>";
	}

	private class AbandonSkillInvoker extends BaseInvoker {
		@Override
		protected String loadingMsg() {
			return "遗忘技能";
		}

		@Override
		protected String failMsg() {
			return "遗忘失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().heroSkillAbandon(hic.getId(), hssic.getId());
			hssic.clearBattleSkill();
			hic.abandonSkill(hssic.getId());
		}

		@Override
		protected void onOK() {
			dismiss();
			controller.alert("遗忘技能成功");
		}

	}

}
