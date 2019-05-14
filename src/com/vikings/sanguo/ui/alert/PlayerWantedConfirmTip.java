package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class PlayerWantedConfirmTip extends CustomConfirmDialog implements
		OnClickListener {

	private static final int layout = R.layout.alert_player_wanted_confirm;

	private OtherUserClient ouc;
	private BriefGuildInfoClient bgic;
	private ItemBag itemBag;

	public PlayerWantedConfirmTip(OtherUserClient ouc,
			BriefGuildInfoClient bgic, ItemBag itemBag) {
		super("确认追杀目标", CustomConfirmDialog.DEFAULT);
		this.bgic = bgic;
		this.ouc = ouc;
		this.itemBag = itemBag;
		setButton(FIRST_BTN, "确定", this);
		setButton(SECOND_BTN, "取消", closeL);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		if (ouc.bref().isVip()) {
			IconUtil.setUserIcon(
					(ViewGroup) content.findViewById(R.id.iconLayout),
					ouc.bref(), "VIP" + ouc.bref().getCurVip().getLevel());
		} else {
			IconUtil.setUserIcon(
					(ViewGroup) content.findViewById(R.id.iconLayout),
					ouc.bref());
		}

		ViewUtil.setText(content, R.id.nickName, ouc.getNick());
		ViewUtil.setText(content, R.id.userID, "(ID:" + ouc.getId().intValue()
				+ ")");
		String countryName = ouc.bref().getCountryName();
		ViewUtil.setText(content, R.id.countryName,
				"国家:" + (StringUtil.isNull(countryName) ? "无" : countryName));
		if (bgic == null) {
			ViewUtil.setText(content, R.id.guildName, "家族:无");
		} else {
			ViewUtil.setText(content, R.id.guildName, "家族:" + bgic.getName());
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	@Override
	public void onClick(View v) {
		if (Account.user.getCountry().intValue() == ouc.getCountry()) {
			dismiss();
			controller.alert("抱歉，追杀令使用失败！<br/>不可对本国玩家发布追杀令，请填写敌国玩家的ID；");
			return;
		}

		if (ouc.getManor().getPos() == 0) {
			dismiss();
			controller.alert("抱歉，追杀令使用失败！<br>你选择的用户级别太低，等他开启了世界征战再来吧！");
			return;
		}

		if (ouc.isWanted()) {
			dismiss();
			controller.alert("抱歉，追杀令使用失败！<br>目标正在被人追杀，请不要重复使用！");
			return;
		}
		new PlayerWantedInvoker().start();
	}

	private class PlayerWantedInvoker extends BaseInvoker {
		private ReturnInfoClient ric;

		@Override
		protected String loadingMsg() {
			return "发布中...";
		}

		@Override
		protected String failMsg() {
			return "使用" + itemBag.getItem().getName() + "失败";
		}

		@Override
		protected void fire() throws GameException {
			ric = GameBiz.getInstance().playerWanted(ouc.getId());

		}

		@Override
		protected void onOK() {
			dismiss();
			ric.setMsg("使用" + itemBag.getItem().getName() + "成功");
			controller.updateUI(ric, true, false, true);
		}

	}
}
