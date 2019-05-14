package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class PlayerWantedTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_player_wanted;

	private ImageView icon;
	private TextView type, count, price, desc;
	private ItemBag itemBag;
	private Item item;

	public PlayerWantedTip(ItemBag itemBag) {
		super("发布个人追杀令", DEFAULT);
		this.itemBag = itemBag;
		this.item = itemBag.getItem();
		icon = (ImageView) content.findViewById(R.id.icon);
		type = (TextView) content.findViewById(R.id.type);
		count = (TextView) content.findViewById(R.id.count);
		price = (TextView) content.findViewById(R.id.price);
		desc = (TextView) content.findViewById(R.id.desc);
	}

	public void show() {
		if (item == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		ViewUtil.setGone(content, R.id.openBtn);
		ViewUtil.setGone(content, R.id.overBtn);
		ViewUtil.setGone(content, R.id.sellBtn);
		ViewUtil.setGone(content, R.id.closeBtn);
		new ViewImgScaleCallBack(item.getImage(), icon, Config.SCALE_FROM_HIGH
				* Constants.ICON_WIDTH, Config.SCALE_FROM_HIGH
				* Constants.ICON_HEIGHT);
		ViewUtil.setText(type, "类型：" + item.getTypeName());
		ViewUtil.setText(count, "数量：" + itemBag.getCount());

		if (item.getSell() > 0) {
			ViewUtil.setRichText(price, "售价：#money#" + item.getSell(), true);
		} else {
			ViewUtil.setRichText(
					price,
					"售价："
							+ StringUtil.color("不可出售", Config.getController()
									.getResourceColorText(R.color.k7_color8)),
					true);
		}

		if (item.canUse()) {
			setButton(FIRST_BTN, item.getUseBtnStr(), new OnClickListener() {

				@Override
				public void onClick(View v) {
					String text = ViewUtil.getText(content, R.id.userID);
					if (StringUtil.isNormalUserId(text)) {
						int id = Integer.valueOf(text);
						if (BriefUserInfoClient.isNPC(id)) {
							controller.alert("请输入有效的玩家ID!");
							return;
						}
						new FetchUseInvoker(id).start();
					} else
						Config.getController().alert("请输入有效的玩家ID!");
				}

			});
		}

		ViewUtil.setRichText(desc, item.getDesc());
		setButton(THIRD_BTN, "关闭", closeL);
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	private class FetchUseInvoker extends BaseInvoker {
		private int userID;
		private OtherUserClient ouc;
		private BriefGuildInfoClient bgic;

		public FetchUseInvoker(int userID) {
			this.userID = userID;
		}

		@Override
		protected String loadingMsg() {
			return "请稍候...";
		}

		@Override
		protected String failMsg() {
			return "获取玩家信息失败";
		}

		@Override
		protected void fire() throws GameException {
			ouc = GameBiz
					.getInstance()
					.queryRichOtherUserInfo(
							userID,
							com.vikings.sanguo.message.Constants.DATA_TYPE_OTHER_RICHINFO);
			if (ouc.hasGuild())
				bgic = CacheMgr.bgicCache.get(ouc.getGuildid());
		}

		@Override
		protected void onOK() {
			dismiss();
			new PlayerWantedConfirmTip(ouc, bgic, itemBag).show();
		}

	}
}
