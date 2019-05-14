package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildInfoClient;
import com.vikings.sanguo.model.OtherRichGuildInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class GuildUserTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_guild_user;

	private BriefUserInfoClient briefUser;
	private RichGuildInfoClient rgic;
	private OtherRichGuildInfoClient orgic;
	private ViewGroup icon;
	private Button sendMsgBtn, castleBtn, kickedBtn, transferBtn, closeBtn,
			elderBtn;
	private ImageView vipIcon;
	private TextView vipLevel;

	public GuildUserTip(BriefUserInfoClient briefUser, RichGuildInfoClient rgic) {
		super("选择操作", DEFAULT);
		this.briefUser = briefUser;
		this.rgic = rgic;
		// if (rgic.isLeader(Account.user.getId()))
		// setTipBg(LARGE);
		// else
		// setTipBg(DEFAULT);
		// if (briefUser.getId().intValue() == Account.user.getId())
		// setTipBg(DEFAULT);
		initView();
	}

	public GuildUserTip(BriefUserInfoClient briefUser,
			OtherRichGuildInfoClient orgic) {
		super("选择操作", DEFAULT);
		this.briefUser = briefUser;
		this.orgic = orgic;
		initView();
	}

	protected void initView() {
		icon = (ViewGroup) content.findViewById(R.id.iconLayout);
		sendMsgBtn = (Button) content.findViewById(R.id.sendMsgBtn);
		sendMsgBtn.setOnClickListener(this);
		castleBtn = (Button) content.findViewById(R.id.castleBtn);
		castleBtn.setOnClickListener(this);
		kickedBtn = (Button) content.findViewById(R.id.kickedBtn);
		kickedBtn.setOnClickListener(this);
		transferBtn = (Button) content.findViewById(R.id.transferBtn);
		transferBtn.setOnClickListener(this);
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(this);
		elderBtn = (Button) content.findViewById(R.id.elderBtn);

		vipIcon = (ImageView) content.findViewById(R.id.vipIcon);
		vipLevel = (TextView) content.findViewById(R.id.vipLevel);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {

		new UserIconCallBack(briefUser, icon, Constants.USER_ICON_WIDTH
				* Config.SCALE_FROM_HIGH, Constants.USER_ICON_HEIGHT_BIG
				* Config.SCALE_FROM_HIGH);

		ViewUtil.setText(content, R.id.name, briefUser.getNickName());
		ViewUtil.setText(content, R.id.level, "等级："
				+ briefUser.getLevel().intValue() + "级");
		ViewUtil.setText(content, R.id.userId, "ID："
				+ briefUser.getId().intValue());

		if (briefUser.getCurVip().getLevel() >= 1) {
			ImageUtil.setBgNormal(vipIcon);
			ViewUtil.setVisible(vipLevel);
			ViewUtil.setRichText(vipLevel,
					StringUtil.vipNumImgStr(briefUser.getCurVip().getLevel()));
		} else {
			ImageUtil.setBgGray(vipIcon);
			ViewUtil.setGone(vipLevel);
		}

		if (null != rgic) {
			if (rgic.isLeader(briefUser.getId())) {
				ViewUtil.setText(content, R.id.position, "职务:族长");
			} else if (rgic.isElder(briefUser.getId())) {
				ViewUtil.setText(content, R.id.position, "职务:长老");
			} else {
				ViewUtil.setText(content, R.id.position, "职务:成员");
			}
		} else if (null != orgic) {
			if (orgic.getOgic().getLeader() == briefUser.getId()) {
				ViewUtil.setText(content, R.id.position, "职务:族长");
			} else {
				ViewUtil.setText(content, R.id.position, "职务:成员");
			}
		}

		if (null != rgic) {
			if (Account.user.getId() == briefUser.getId()) { // 点击的是自己
				ViewUtil.setGone(sendMsgBtn);
				ViewUtil.setGone(castleBtn);
				ViewUtil.setGone(kickedBtn);
				ViewUtil.setGone(transferBtn);
				ViewUtil.setGone(elderBtn);
			} else { // 点击的是别人
				ViewUtil.setVisible(sendMsgBtn);
				ViewUtil.setVisible(castleBtn);
				if (rgic.isLeader(Account.user.getId())) { // 自己是族长
					ViewUtil.setVisible(kickedBtn);
					ViewUtil.setVisible(transferBtn);
					ViewUtil.setVisible(elderBtn);
					if (rgic.isElder(briefUser.getId())) { // 如果是长老
						elderBtn.setText("废除长老");
						elderBtn.setTag(true);
						elderBtn.setOnClickListener(this);
					} else {
						elderBtn.setText("提升长老");
						elderBtn.setTag(false);
						elderBtn.setOnClickListener(this);
					}
				} else { // 族员
					ViewUtil.setGone(kickedBtn);
					ViewUtil.setGone(transferBtn);
					ViewUtil.setGone(elderBtn);
				}
			}
		} else if (null != orgic) {
			if (briefUser.getCountry().intValue() == Account.user.getCountry()
					.intValue())
				ViewUtil.setVisible(sendMsgBtn);
			else
				ViewUtil.setGone(sendMsgBtn);
			ViewUtil.setVisible(castleBtn);
			ViewUtil.setGone(kickedBtn);
			ViewUtil.setGone(transferBtn);
			ViewUtil.setGone(elderBtn);
		}

	}

	@Override
	public void onClick(View v) {
		if (v == sendMsgBtn) {
			dismiss();
			controller.openChatWindow(briefUser);
		} else if (v == castleBtn) {
			dismiss();
			// 打开主城
			controller.showCastle(briefUser);
		} else if (v == kickedBtn) {
			new MsgConfirm("踢出家族", DEFAULT).show(
					"你确定将"
							+ StringUtil.color(briefUser.getHtmlNickName(),
									R.color.k7_color9) + "踢出家族么",
					new CallBack() {

						@Override
						public void onCall() {
							new KickedInvoker().start();
						}
					}, null);
		} else if (v == transferBtn) {
			new MsgConfirm("转让族长", DEFAULT).show(
					"是否将族长转让给"
							+ StringUtil.color(briefUser.getHtmlNickName(),
									R.color.color24) + "？", new CallBack() {

						@Override
						public void onCall() {
							new TransferInvoker().start();
						}
					}, null);
		} else if (v == closeBtn) {
			dismiss();
		} else if (v == elderBtn) {
			Object obj = elderBtn.getTag();
			if (null != obj) {
				boolean isElder = (Boolean) obj;
				if (isElder) {
					// 废除长老
					new RepealElderInvoker().start();
				} else {
					// 提升长老
					new ImproveElderInvoker().start();
				}
			}
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	// 踢出家族，组长操作，成员不可见
	private class KickedInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "踢出家族...";
		}

		@Override
		protected String failMsg() {
			return "踢出家族失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().guildMemberRemove(rgic.getGuildid(),
					briefUser.getId().intValue());
		}

		@Override
		protected void onOK() {
			rgic.getMembers().remove(briefUser.getId());
			dismiss();
			ctr.alert("你已经将"
					+ StringUtil.color(briefUser.getHtmlNickName(),
							R.color.k7_color5) + "踢出家族");
		}

	}

	// 转换族长，组长操作，成员不可见
	private class TransferInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "转让族长";
		}

		@Override
		protected String failMsg() {
			return "转让族长失败";
		}

		@Override
		protected void fire() throws GameException {
			// 调用转让族长接口，参数为家族ID和用户ID
			GameBiz.getInstance().guildLeaderAssign(rgic.getGuildid(),
					briefUser.getId().intValue());
		}

		@Override
		protected void onOK() {
			rgic.getGic().setLeader(briefUser.getId().intValue());
			CacheMgr.bgicCache.updateCache(rgic.getGic());
			Account.myGuildLeader = briefUser;
			dismiss();
			ctr.alert(StringUtil.color(briefUser.getHtmlNickName(),
					R.color.k7_color5)
					+ "已成为"
					+ rgic.getGic().getName()
					+ "家族的族长了");
		}
	}

	private class ImproveElderInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "提升长老";
		}

		@Override
		protected String failMsg() {
			return "提升长老失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().guildPositionAssign(rgic.getGuildid(),
					briefUser.getId(), GuildInfoClient.POSITION_ELDER);
			rgic.getGic().addElder(briefUser.getId());

		}

		@Override
		protected void onOK() {
			dismiss();
			ctr.alert(StringUtil.color(briefUser.getHtmlNickName(),
					R.color.k7_color5)
					+ "已成为"
					+ rgic.getGic().getName()
					+ "家族的长老了");
		}

	}

	private class RepealElderInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "废除长老";
		}

		@Override
		protected String failMsg() {
			return "废除长老失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().guildPositionAssign(rgic.getGuildid(),
					briefUser.getId(), 0);
			if (null != rgic) {
				rgic.getGic().removeElder(briefUser.getId());
			}
		}

		@Override
		protected void onOK() {
			dismiss();
			ctr.alert(StringUtil.color(briefUser.getHtmlNickName(),
					R.color.k7_color5)
					+ "已废除成为"
					+ rgic.getGic().getName()
					+ "家族的成员了");
		}

	}
}
