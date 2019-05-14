package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.PropAct;
import com.vikings.sanguo.model.PropActType;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.alert.ShopHintTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.utils.VipUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

//Comment : 章节界面
public class ActWindow extends CustomListViewWindow implements OnClickListener {
	private PropActType propActType;
	private byte difficult;

	private TextView gradientMsg;

	public void open(PropActType propActType) {
		this.open(propActType, PropAct.DIFFICULT_NORMAL);
	}

	public void open(PropActType propActType, byte difficult) {
		this.propActType = propActType;
		this.difficult = difficult;
		doOpen();
	}

	@Override
	protected void init() {
		super.init("战役列表");
		// setLeftBtn(PropAct.getDifficultyDesc(difficult), this);
		setContentBelowTitle(R.layout.gradient_msg);
		gradientMsg = (TextView) window.findViewById(R.id.gradientMsg);
	}

	private void setUserStaminaDesc() {
		int stamina = Account.user.getUserStamina();
		setRightTxt("行动:" + stamina + "/" + Account.user.getMaxStamina());
		if (stamina == Account.user.getMaxStamina()) {
			ViewUtil.setText(gradientMsg, "行动力已满");
		} else {
			int time = Account.user.getUserStaminaRecoverLeftTime();
			ViewUtil.setRichText(
					gradientMsg,
					"行动力"
							+ StringUtil.color(
									DateUtil.formatDownCountTime(time),
									R.color.color19) + "后恢复1点");
		}

	}

	@Override
	public void showUI() {
		// refreshLeftBtn(PropAct.getDifficultyDesc(difficult));
		setUserStaminaDesc();
		List<ActInfoClient> datas = getDatas();
		adapter.clear();
		adapter.addItems(datas);
		adapter.notifyDataSetChanged();
		super.showUI();
	}

	@Override
	public void onClick(View view) {
		changeDifficult();
	}

	private void changeDifficult() {
		if (difficult == PropAct.DIFFICULT_NORMAL) {
			difficult = PropAct.DIFFICULT_HARD;
		} else if (difficult == PropAct.DIFFICULT_HARD) {
			difficult = PropAct.DIFFICULT_NORMAL;
		}
		showUI();
	}

	private List<ActInfoClient> getDatas() {
		return Account.actInfoCache.getActsByType(propActType.getType(),
				difficult);
	}

	// @Override
	// protected byte getLeftBtnBgType() {
	// return WINDOW_BTN_BG_TYPE_CLICK;
	// }

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	private class ActAdapter extends ObjectAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = Config.getController().inflate(getLayoutId());
				holder = new ViewHolder();
				holder.actIcon = (ImageView) convertView
						.findViewById(R.id.actIcon);
				holder.nameIcon = (ImageView) convertView
						.findViewById(R.id.nameIcon);
				holder.clearBtn = convertView.findViewById(R.id.clearBtn);
				holder.itemLayout = convertView.findViewById(R.id.itemLayout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final ActInfoClient actInfoClient = (ActInfoClient) getItem(position);
			PropAct propAct = actInfoClient.getPropAct();
			new ViewImgCallBack(propAct.getIcon(), holder.actIcon);
			new ViewImgCallBack(propAct.getNameIcon(), holder.nameIcon);
			if (propAct.getDifficult() == PropAct.DIFFICULT_HARD) {
				ViewUtil.setImage(holder.itemLayout,
						R.drawable.transcript_list_bg);
			} else {
				ViewUtil.setImage(holder.itemLayout, R.drawable.common_item_bg);
			}
			if (actInfoClient.isComplete()) {
				ViewUtil.setVisible(holder.clearBtn);

				final int needVip = VipUtil.campaginClear();
				if (Account.user.getCurVip().getLevel() >= needVip) {
					holder.clearBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new ActClearWindow().open(actInfoClient);
						}
					});
				} else {
					ImageUtil.setBgGray(holder.clearBtn);
					holder.clearBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							UserVip vip = CacheMgr.userVipCache
									.getVipByLvl(needVip);
							StringBuffer buf = new StringBuffer(
									StringUtil.color("单挑副本耗时多多，繁琐操作不堪忍受？",
											R.color.color6));
							buf.append("<br>");
							if (null != vip) {
								if (vip.getChargeRate() != 0)
									buf.append(StringUtil.color(
											"充满"
													+ (vip.getCharge() / vip
															.getChargeRate())
													+ "元，开通VIP" + needVip,
											R.color.color19));
								else
									buf.append(StringUtil.color(
											"充满"
													+ (vip.getCharge() / Constants.CENT)
													+ "元，开通VIP" + needVip,
											R.color.color19));
							} else {
								buf.append(StringUtil.color("开通VIP" + needVip,
										R.color.color19));
							}

							buf.append(StringUtil.color("，立刻享有群撸功能，扫荡副本不费力！",
									R.color.color6));

							new ShopHintTip("友情提示", StringUtil.color(
									"抱歉，你的VIP等级不够!", R.color.color11), buf
									.toString(), "立刻开通VIP" + needVip, true)
									.show();
						}
					});
				}
			} else {
				ViewUtil.setHide(holder.clearBtn);
				holder.clearBtn.setOnClickListener(null);
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					controller.openCampaignWindow(actInfoClient);
				}
			});
			return convertView;
		}

		@Override
		public void setViewDisplay(View v, Object o, int index) {
		}

		@Override
		public int getLayoutId() {
			return R.layout.act_item;
		}

		private class ViewHolder {
			ImageView actIcon, nameIcon;
			View clearBtn, itemLayout;
		}
	}

	@Override
	protected ObjectAdapter getAdapter() {
		ActAdapter adapter = new ActAdapter();
		return adapter;
	}

	@Override
	protected int refreshInterval() {
		return 1000;
	}

	@Override
	protected void refreshUI() {
		setUserStaminaDesc();
	}

}
