package com.vikings.sanguo.ui;

import java.util.ArrayList;
import java.util.Date;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.AddFriend;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildJoinInfoClient;
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.model.PlayerWantedInfoClient;
import com.vikings.sanguo.protos.TroopLogInfo;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.CommonCustomAlert;
import com.vikings.sanguo.ui.alert.GuildInviteLogTip;
import com.vikings.sanguo.ui.alert.GuildJoinLogApproveTip;
import com.vikings.sanguo.ui.alert.NotifyTip;
import com.vikings.sanguo.ui.alert.PlayerWantedInfosTip;
import com.vikings.sanguo.ui.window.LogAttackWindow;
import com.vikings.sanguo.ui.window.LogDefendWindow;
import com.vikings.sanguo.ui.window.LogTroopWindow;
import com.vikings.sanguo.ui.window.WarInfoWindow;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class NotifyMsg extends BaseUI implements OnClickListener {

	/**
	 * 围城结束
	 */
	public static final int TYPE_FIEF_SURROUND_END = 1;

	/**
	 * 战斗结束
	 */
	public static final int TYPE_FIEF_BATTLE_END = 2;

	/**
	 * 被攻击
	 */
	public static final int TYPE_FIEF_ATTACK = 3;

	private ViewGroup content;

	private int max = 10;

	private int width = (int) (170 * Config.SCALE_FROM_HIGH);

	private TranslateAnimation anim;

	public NotifyMsg() {
		content = (ViewGroup) controller.findViewById(R.id.msgNotify);
		anim = new TranslateAnimation(-width, 0, 0, 0);
		anim.setDuration(500);
	}

	public void addMsg(final int type, final BriefBattleInfoClient battle) {
		content.post(new Runnable() {
			@Override
			public void run() {
				clearBattle(battle.getBattleid());
				add(new BattleMsg(type, battle));
			}
		});
	}

	public void addMsg(final String title, final String msg, final int icon) {
		content.post(new Runnable() {
			@Override
			public void run() {
				clearCommMsg(title);
				add(new CommMsg(title, msg, icon));
			}
		});
	}

	public void addMsg(final BriefUserInfoClient user) {
		content.post(new Runnable() {
			@Override
			public void run() {
				add(new FriendMsg(user));
			}
		});
	}

	public void addMsg(final LogInfoClient log) {
		content.post(new Runnable() {
			@Override
			public void run() {
				add(new GuildMsg(log));
			}
		});
	}

	public void addMsg(final GuildJoinInfoClient gjic, final int guildId) {
		content.post(new Runnable() {
			@Override
			public void run() {
				add(new GuildJoinMsg(gjic, guildId));
			}
		});
	}

	public void addMsg(final TroopLogInfo log) {
		content.post(new Runnable() {
			@Override
			public void run() {
				add(new TroopMsg(log));
			}
		});
	}

	public void addMsg(final PlayerWantedInfoClient pwic) {
		if (pwic != null && pwic.getBriefUser() != null)
			content.post(new Runnable() {

				@Override
				public void run() {
					add(new WantedMsg(pwic));
				}
			});
	}

	synchronized public void clearBattle(long battleId) {
		ArrayList<View> tmp = new ArrayList<View>();
		for (int i = 0; i < content.getChildCount(); i++) {
			View t = content.getChildAt(i);
			if (t.getTag() != null && t.getTag() instanceof BattleMsg) {
				if (((BattleMsg) (t.getTag())).battle.getBattleid() == battleId)
					tmp.add(t);
			}
		}
		for (View v : tmp) {
			content.removeView(v);
		}
		tmp = null;
	}

	synchronized public void clearCommMsg(String title) {
		ArrayList<View> tmp = new ArrayList<View>();
		for (int i = 0; i < content.getChildCount(); i++) {
			View t = content.getChildAt(i);
			if (t.getTag() != null && t.getTag() instanceof CommMsg) {
				if (((CommMsg) (t.getTag())).title.equals(title))
					tmp.add(t);
			}
		}
		for (View v : tmp) {
			content.removeView(v);
		}
		tmp = null;
	}

	synchronized private void add(MsgHandler h) {
		View v = controller.inflate(R.layout.msg_notify_item);
		v.setOnClickListener(this);
		h.setMsg(v);
		v.setTag(h);
		content.addView(v, 0);
		if (content.getChildCount() > max)
			content.removeViewAt(max);
		v.startAnimation(anim);
		content.post(new Runnable() {

			@Override
			public void run() {
				if (null != controller.getFiefMap()) {
					MyFiefUI myFiefUI = controller.getFiefMap().getMyFiefUI();
					if (null != myFiefUI)
						myFiefUI.updateFief();
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		content.removeView(v);
		((MsgHandler) v.getTag()).onClick();
	}

	private interface MsgHandler {
		public void onClick();

		public void setMsg(View v);
	}

	private class CommMsg implements MsgHandler {

		private String title;

		private String msg;

		private int icon;

		public CommMsg(String title, String msg, int icon) {
			this.title = title;
			this.msg = msg;
			this.icon = icon;
		}

		@Override
		public void onClick() {
			// MsgConfirm c = new MsgConfirm("通知", CustomConfirmDialog.SMALL);
			// ViewUtil.setGone(c.getOkButton());
			// c.setCancelText("关闭");
			Date date = new Date(Config.serverTime());
			msg = DateUtil.db2TimeFormat.format(date) + "<br/>" + msg;
			// c.show(msg, null, null);
			new NotifyTip("", msg, null).show();
		}

		@Override
		public void setMsg(View v) {
			ViewUtil.setImage(v, R.id.icon, controller.getDrawable(icon));
			ViewUtil.setText(v, R.id.msg, StringUtil.getNCharStr(title, 16));
		}

	}

	private class FriendMsg implements MsgHandler {

		private BriefUserInfoClient user;

		public FriendMsg(BriefUserInfoClient user) {
			this.user = user;
		}

		@Override
		public void onClick() {
			if (Account.isFriend(user)) {
				controller.confirm("通知", CommonCustomAlert.DEFAULT,
						StringUtil.color(user.getHtmlNickName(), "#b006cf")
								+ " 加你为好友了", "", "查看详情", new CallBack() {

							@Override
							public void onCall() {
								controller.showCastle(user);
							}
						}, "关闭", null);
			} else {
				new CommonCustomAlert("通知", CommonCustomAlert.DEFAULT, false,
						StringUtil.color(user.getHtmlNickName(), "#b006cf")
								+ " 加你为好友了", "加为好友", new Friend(user), "查看详情",
						new CallBack() {

							@Override
							public void onCall() {
								controller.showCastle(user);
							}
						}, "关闭", true).show();
			}

		}

		@Override
		public void setMsg(View v) {
			ViewUtil.setImage(v, R.id.icon, R.drawable.notify_friend);
			ViewUtil.setText(v, R.id.msg, user.getNickName() + "加你为好友");
		}

	}

	private class GuildJoinMsg implements MsgHandler {
		private GuildJoinInfoClient gjic;
		private int guildId;

		public GuildJoinMsg(GuildJoinInfoClient gjic, int guildId) {
			this.gjic = gjic;
			this.guildId = guildId;
		}

		@Override
		public void onClick() {
			new GuildJoinLogApproveTip(gjic, guildId).show();
		}

		@Override
		public void setMsg(View v) {
			ViewUtil.setImage(v, R.id.icon, R.drawable.notify_guild);
			// 保护
			if (null == gjic)
				return;
			ViewUtil.setText(v, R.id.msg, "玩家申请加入家族");
		}

	}

	private class GuildMsg implements MsgHandler {

		private LogInfoClient lic;

		public GuildMsg(LogInfoClient log) {
			this.lic = log;
		}

		@Override
		public void onClick() {
			if (lic.isGuildInvite())
				new GuildInviteLogTip(lic).show();
			else if (lic.isKickOut())
				Config.getController().alert(lic.getKickOutLogDesc());
			else if (lic.isMakeOver())
				Config.getController().alert(lic.getMakeOverLogDesc());
			else if (lic.isJoinAgree())
				Config.getController().alert(lic.getJoinAgreeDesc());
			else if (lic.isJoinRefuse())
				Config.getController().alert(lic.getJoinRefuseDesc());
			else if (lic.isJoin())
				new GuildJoinLogApproveTip(lic).show();
			else if (lic.isSetElder())
				Config.getController().alert(lic.getSetElderDesc());
			else if (lic.isRemoveElder())
				Config.getController().alert(lic.getRemoveElderDesc());
		}

		@Override
		public void setMsg(View v) {
			ViewUtil.setImage(v, R.id.icon, R.drawable.notify_guild);
			// 保护
			if (null == lic || null == lic.getGuildInfo())
				return;

			if (lic.isGuildInvite())
				ViewUtil.setText(v, R.id.msg, lic.getGuildInfo().getName()
						+ "邀请您加入");
			else if (lic.isKickOut())
				ViewUtil.setText(v, R.id.msg, "您已被踢出家族:"
						+ lic.getGuildInfo().getName());
			else if (lic.isMakeOver())
				ViewUtil.setText(v, R.id.msg, "您已成为"
						+ lic.getGuildInfo().getName() + "族长");
			else if (lic.isJoin())
				ViewUtil.setText(v, R.id.msg, "玩家申请加入家族");
			else if (lic.isJoinAgree())
				ViewUtil.setText(v, R.id.msg, "你已加入家族");
			else if (lic.isJoinRefuse())
				ViewUtil.setText(v, R.id.msg, "你被拒绝加入家族");
			else if (lic.isSetElder())
				ViewUtil.setText(v, R.id.msg, "你被提升为长老");
			else if (lic.isRemoveElder())
				ViewUtil.setText(v, R.id.msg, "你被贬为平民");
		}

	}

	private class TroopMsg implements MsgHandler {

		private TroopLogInfo log;

		public TroopMsg(TroopLogInfo log) {
			this.log = log;
		}

		private int countTroop() {
			int count = 0;
			for (com.vikings.sanguo.protos.ArmInfo a : log.getInfo()
					.getInfosList()) {
				count += a.getCount();
			}
			return count;
		}

		@Override
		public void onClick() {
			Date date = new Date(log.getTime() * 1000L);
			String msg = DateUtil.db2TimeFormat.format(date);
			msg = msg + "<br/>由于缺粮," + countTroop() + "士兵逃亡了<br/><br/>" + "主城升级“"
					+ StringUtil.color("屯田", R.color.color20) + "“" + "或在世界掠夺“"
					+ StringUtil.color("农田", R.color.color20) + "“都可获得粮草";
			new NotifyTip("查看部队日志", msg, new CallBack() {
				@Override
				public void onCall() {
					new LogTroopWindow().open();
				}
			}).show();
		}

		@Override
		public void setMsg(View v) {
			ViewUtil.setImage(v, R.id.icon, R.drawable.notify_troop);
			ViewUtil.setRichText(v, R.id.msg, "由于缺粮，士兵逃跑了");
		}

	}

	private class WantedMsg implements MsgHandler {

		private PlayerWantedInfoClient pwic;

		public WantedMsg(PlayerWantedInfoClient pwic) {
			this.pwic = pwic;
		}

		@Override
		public void onClick() {
			if (null != pwic.getTargetUser()
					&& pwic.getTargetUser().getId() == Account.user.getId()) {
				new NotifyTip("查看追杀令列表", pwic.getWantedMeDesc(),
						new CallBack() {
							@Override
							public void onCall() {
								if (Config.getController().getCurPopupUI() != Config
										.getController().getFiefMap()) {
									Config.getController().closeAllPopup();
									Config.getController().getFiefMap().open();
								}
								new PlayerWantedInfosTip().show();
							}
						}).show();
			} else {
				new PlayerWantedInfosTip().show();
			}

		}

		@Override
		public void setMsg(View v) {
			ViewUtil.setImage(v, R.id.icon, R.drawable.notify_attack);
			int userId = pwic.getInfo().getTarget();
			if (userId == Account.user.getId())
				ViewUtil.setText(v, R.id.msg, "有人对你使用了江湖追杀令，点击查看详情");
			else
				ViewUtil.setText(v, R.id.msg, pwic.getBriefUser().getNickName()
						+ "(ID:" + pwic.getInfo().getUserid()
						+ ")发布了江湖追杀令!点击查看详情");
		}
	}

	private String color4(String str) {
		return " "
				+ StringUtil.color(str, Config.getController()
						.getResourceColorText(R.color.k7_color4)) + " ";
	}

	private class BattleMsg implements MsgHandler {

		BriefBattleInfoClient battle;

		int type;

		public BattleMsg(int type, BriefBattleInfoClient battle) {
			this.battle = battle;
			this.type = type;
		}

		@Override
		public void onClick() {
			String ok = "";
			String msg = "";
			CallBack call = null;
			switch (type) {
			case TYPE_FIEF_SURROUND_END:
				if (battle.getDefender() == Account.user.getId())
					msg = "你的" + color4(battle.getBfic().getName())
							+ "已被包围，请下令进攻";
				else
					msg = "你的部队已完成对" + color4(battle.getBfic().getName())
							+ "的包围，请下令进攻";
				ok = "查看战场";
				call = new Battle(battle);
				break;
			case TYPE_FIEF_ATTACK:
				if (battle.getDefender() == Account.user.getId())
					msg = "你的" + color4(battle.getBfic().getName())
							+ "遭到攻击，请前往查看";
				else
					msg = "攻击" + color4(battle.getBfic().getName()) + "，请前往查看";
				ok = "查看战场";
				call = new Battle(battle);
				break;
			case TYPE_FIEF_BATTLE_END:
				if (battle == null) {
					msg = "你的领地被攻击了，请查看遇袭日志";
					ok = "查看日志";
					call = new DefendLog();
				} else {
					if (battle.getAttacker() == Account.user.getId()) {
						msg = "进攻" + color4(battle.getBfic().getName())
								+ "的战斗结束了";
						call = new AttackLog();
					} else if (battle.getDefender() == Account.user.getId()) {
						msg = "防守" + color4(battle.getBfic().getName())
								+ "的战斗结束了";
						call = new DefendLog();
					} else {
						msg = "援助" + color4(battle.getBfic().getName())
								+ "的战斗结束了";
						call = new AttackLog();
					}
					ok = "查看日志";
				}
				break;
			default:
				break;
			}
			new NotifyTip(ok, msg, call).show();
		}

		@Override
		public void setMsg(View v) {
			// 设置提示文字
			String text;
			switch (type) {
			case TYPE_FIEF_SURROUND_END:
				if (battle.getDefender() == Account.user.getId()) {
					ViewUtil.setImage(v, R.id.icon, R.drawable.notify_defend);
					text = battle.getBfic().getName() + "已被包围，请下令突围";
				} else {
					ViewUtil.setImage(v, R.id.icon, R.drawable.notify_attack);
					text = "已包围" + battle.getBfic().getName() + "，请下令进攻";
				}
				ViewUtil.setText(v, R.id.msg, text);
				break;
			case TYPE_FIEF_ATTACK:
				if (battle.getDefender() == Account.user.getId()) {
					text = battle.getBfic().getName() + "遭到攻击，请前往查看";
					ViewUtil.setImage(v, R.id.icon, R.drawable.notify_defend);
				} else {
					text = "攻击" + battle.getBfic().getName() + "，请前往查看";
					ViewUtil.setImage(v, R.id.icon, R.drawable.notify_attack);
				}
				ViewUtil.setText(v, R.id.msg, text);
				break;
			case TYPE_FIEF_BATTLE_END:
				if (battle == null) {
					text = "你的领地被攻击了，请查看遇袭日志";
					ViewUtil.setImage(v, R.id.icon, R.drawable.notify_defend);
				} else {
					if (battle.getAttacker() == Account.user.getId()) {
						text = "进攻" + battle.getBfic().getName() + "的战斗结束了";
						ViewUtil.setImage(v, R.id.icon,
								R.drawable.notify_attack);
					} else if (battle.getDefender() == Account.user.getId()) {
						text = "防守" + battle.getBfic().getName() + "的战斗结束了";
						ViewUtil.setImage(v, R.id.icon,
								R.drawable.notify_defend);
					} else {
						text = "援助" + battle.getBfic().getName() + "的战斗结束了";
						ViewUtil.setImage(v, R.id.icon,
								R.drawable.notify_reinforce);
					}
					ViewUtil.setText(v, R.id.msg, text);
				}
				break;
			default:
				break;
			}
		}
	}

	private class DefendLog implements CallBack {

		@Override
		public void onCall() {
			new LogDefendWindow().open();
		}

	}

	private class AttackLog implements CallBack {

		@Override
		public void onCall() {
			new LogAttackWindow().open();
		}

	}

	private class Battle implements CallBack {

		BriefBattleInfoClient battle;

		public Battle(BriefBattleInfoClient battle) {
			this.battle = battle;
		}

		@Override
		public void onCall() {
			new WarInfoWindow().open(battle.getBfic()); // , null
		}
	}

	private class Move implements CallBack {
		private BriefFiefInfoClient fief;

		public Move(BriefFiefInfoClient fief) {
			this.fief = fief;
		}

		@Override
		public void onCall() {
			if (Config.getController().getCurPopupUI() != Config
					.getController().getFiefMap()) {
				Config.getController().closeAllPopup();
				Config.getController().getFiefMap().open();
			}
			Config.getController().getBattleMap()
					.moveToFief(fief.getId(), true, true);
		}
	}

	private class Friend implements CallBack {

		private BriefUserInfoClient user;

		public Friend(BriefUserInfoClient user) {
			this.user = user;
		}

		@Override
		public void onCall() {
			new AddFriend(user).start();
		}

	}

}
