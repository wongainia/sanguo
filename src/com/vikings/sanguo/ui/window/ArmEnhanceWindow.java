package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
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
import com.vikings.sanguo.model.ArmEffectClient;
import com.vikings.sanguo.model.ArmEnhanceProp;
import com.vikings.sanguo.model.ArmPropInfoClient;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BattlePropDefine;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.model.UserInfoHeadData;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.RechargeBuyGiftTip;
import com.vikings.sanguo.ui.alert.ScrollDescTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.ui.alert.TroopDetailTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;
import com.vikings.sanguo.widget.SlowOnClick;

public class ArmEnhanceWindow extends CustomPopupWindow {

	private ViewGroup propsLayout;
	private AbsoluteLayout absoluteLayou;

	private TroopProp troopProp;
	private List<ViewGroup> armPropViews;

	@Override
	protected void init() {
		super.init("强化" + troopProp.getName());
		setContentAboveTitle(R.layout.user_info_head);
		setContent(R.layout.soldier_strengthens);
		propsLayout = (ViewGroup) window.findViewById(R.id.propsLayout);
		armPropViews = new ArrayList<ViewGroup>();
		controller.inflate(R.layout.absolute_layout, window);
		absoluteLayou = (AbsoluteLayout) window.findViewById(R.id.layout);
		setLeftBtn("属性说明", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ScrollDescTip("属性说明", CacheMgr.uiTextCache
						.getTxt(UITextProp.ARM_PROP_DESC)).show();
			}
		});
	}

	public void open(TroopProp troopProp) {
		this.troopProp = troopProp;
		doOpen();
	}

	@Override
	public void showUI() {
		setSoldierValues(troopProp);
		setValue();
		super.showUI();
	}

	private List<UserInfoHeadData> getUserInfoHeadDatas() {
		List<UserInfoHeadData> datas = new ArrayList<UserInfoHeadData>();
		UserInfoHeadData data0 = new UserInfoHeadData();
		data0.setType(UserInfoHeadData.DATA_TYPE_ARMY);
		data0.setValue(Account.myLordInfo.getArmCount());
		datas.add(data0);

		UserInfoHeadData data1 = new UserInfoHeadData();
		data1.setType(UserInfoHeadData.DATA_TYPE_CURRENCY);
		data1.setValue(Account.user.getCurrency());
		datas.add(data1);

		UserInfoHeadData data2 = new UserInfoHeadData();
		data2.setType(UserInfoHeadData.DATA_TYPE_FOOD);
		data2.setValue(Account.user.getFood());
		datas.add(data2);

		UserInfoHeadData data3 = new UserInfoHeadData();
		data3.setType(UserInfoHeadData.DATA_TYPE_MONEY);
		data3.setValue(Account.user.getMoney());
		datas.add(data3);

		return datas;
	}

	// 士兵信息
	private void setSoldierValues(TroopProp troop) {
		View iconGroup = findViewById(R.id.iconGroup);
		new ViewImgScaleCallBack(troopProp.getIcon(),
				iconGroup.findViewById(R.id.icon), Constants.ARM_ICON_WIDTH
						* Config.SCALE_FROM_HIGH, Constants.ARM_ICON_HEIGHT
						* Config.SCALE_FROM_HIGH);
		iconGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new TroopDetailTip().show(troopProp,
						Account.getUserTroopEffectInfo(troopProp.getId()));
			}
		});
		ViewUtil.setRichText(window, R.id.soldier_name, troop.getName()); // 士兵名字
		ViewUtil.setImage(window, R.id.armicon, troop.getSmallIcon());// 士兵小图标
		ViewUtil.setRichText(window, R.id.soldier_count,
				Account.myLordInfo.getArmCountByType(troop.getId()) + "");// 士兵数量
	}

	private void setValue() {
		setEnhanceViews(Account.armEnhanceCache.getPropByArmId(troopProp
				.getId()));
		ViewUtil.setUserInfoHeadAttrs(window, getUserInfoHeadDatas(), true,
				Account.user);
	}

	private void setEnhanceViews(ArmPropInfoClient apic) {
		List<ArmEffectClient> aecs = apic.getEnhanceList();
		Collections.sort(aecs, new Comparator<ArmEffectClient>() {

			@Override
			public int compare(ArmEffectClient aec1, ArmEffectClient aec2) {
				return aec1.getSequence() - aec2.getSequence();
			}
		});
		initArmpropViews(aecs.size());
		setArmPropValue(aecs);
	}

	// 根据可强化的属性 显示强化属性的个数
	private void initArmpropViews(int size) {
		if (armPropViews.size() < size) {
			propsLayout.removeAllViews();
			armPropViews.clear();
			for (int i = 0; i < size; i++) {
				ViewGroup viewGroup = (ViewGroup) controller
						.inflate(R.layout.soldier_strengthen_item);
				propsLayout.addView(viewGroup);
				armPropViews.add(viewGroup);

				// 资源强化按钮
				View resourceBtn = viewGroup.findViewById(R.id.enhanceBtn1);
				resourceBtn.setOnClickListener(new ClickListener(viewGroup,
						Constants.TYPE_MATERIAL));

				// 元宝至尊强化按钮
				View enhanceBtn = viewGroup.findViewById(R.id.enhanceBtn2);
				enhanceBtn.setOnClickListener(new ClickListener(viewGroup,
						Constants.TYPE_CURRENCY));
			}
		}
	}

	/**
	 * 强化类型列表
	 */
	private void setArmPropValue(List<ArmEffectClient> aecs) {
		for (int i = 0; i < aecs.size(); i++) {
			ArmEffectClient aec = aecs.get(i);
			ViewGroup viewGroup = armPropViews.get(i);

			// 强化进度条
			ProgressBar progressBar = (ProgressBar) viewGroup
					.findViewById(R.id.progressBar);

			progressBar.set(aec.getExp().intValue(), aec.getExpTotal());

			// 设置进度条前景色+强化类型及等级
			setImageAndValue(aec, viewGroup, progressBar, troopProp);

			// 强化经验比值
			TextView progressDesc = (TextView) viewGroup
					.findViewById(R.id.progressDesc);

			// 资源强化
			View enhanceBtn1 = viewGroup.findViewById(R.id.enhanceBtn1);
			enhanceBtn1.setTag(aec);
			if (aec.isMax()) {
				ViewUtil.setText(progressDesc,
						aec.getExpTotal() + "/" + aec.getExpTotal());// 进度框显示的比值
				progressBar.set(aec.getExpTotal(), aec.getExpTotal());// 绘制进度条
				ViewUtil.setText(enhanceBtn1, "已满级");
				ViewUtil.disableButton(enhanceBtn1);
			} else {
				ViewUtil.setText(progressDesc, aec.getExp().intValue() + "/"
						+ aec.getExpTotal());// 进度框显示的比值
				progressBar.set(aec.getExp().intValue(), aec.getExpTotal());// 绘制进度条
				ViewUtil.setText(enhanceBtn1, "资源强化");
				ViewUtil.enableButton(enhanceBtn1);
			}

			// 至尊强化
			View enhanceBtn2 = viewGroup.findViewById(R.id.enhanceBtn2);
			enhanceBtn2.setTag(aec);
			if (aec.isMax()) {
				ViewUtil.setText(enhanceBtn2, "已满级");
				ViewUtil.disableButton(enhanceBtn2);
			} else {
				ViewUtil.setText(enhanceBtn2, "元宝至尊强化");
				ViewUtil.enableButton(enhanceBtn2);
			}

			// 资源强化消耗
			ReturnInfoClient ric = aec.getCost();
			List<ShowItem> items1 = ric.showRequire();
			StringBuilder buf1 = new StringBuilder();
			for (ShowItem item : items1) {
				buf1.append("#").append(item.getImg()).append("#[")
						.append(item.getName()).append("]x")
						.append(item.getCount());
			}

			String str1 = buf1.toString();
			if (StringUtil.isNull(str1)) {
				ViewUtil.setText(viewGroup, R.id.cost1, "");
				ViewUtil.setHide(enhanceBtn1);
			} else {
				ViewUtil.setRichText(viewGroup.findViewById(R.id.cost1), str1,
						true, 25, 25);
				ViewUtil.setVisible(enhanceBtn1);
			}

			// 元宝强化消耗
			StringBuilder buf2 = new StringBuilder();
			buf2.append("#rmb#").append("[元宝]x").append(getCost(aec));
			String str2 = buf2.toString();
			if (StringUtil.isNull(str2)) {
				ViewUtil.setText(viewGroup, R.id.cost2, "");
				ViewUtil.setHide(enhanceBtn2);
			} else {
				ViewUtil.setRichText(viewGroup.findViewById(R.id.cost2), str2,
						true, 25, 25);
				ViewUtil.setVisible(enhanceBtn2);
			}

		}
	}

	// 进度条前景色
	private void setImageAndValue(ArmEffectClient aec, ViewGroup viewGroup,
			ProgressBar progressBar, TroopProp troop) {

		String enhanceValue = (aec.getEnhanceValue() == 0 ? "" : StringUtil
				.color("+" + aec.getEnhanceValue(), R.color.color19));// 本类型本级别属性的总值
		String name = BattlePropDefine.desc[aec.getEnhanceProp().getPropId()];
		ViewUtil.setText(viewGroup, R.id.name, name + "Lv" + aec.getLevel());// 强化属性

		switch (aec.getEnhanceProp().getPropId()) {
		case BattlePropDefine.PROP_LIFE:// 生命值
			progressBar.setImageResource(R.drawable.progress_1);
			enhanceValue = troop.getHp() + enhanceValue;
			ViewUtil.setRichText(window, R.id.soldier_hp, name + "："
					+ enhanceValue);// 生命
			break;

		case BattlePropDefine.PROP_ATTACK:// 攻击
			progressBar.setImageResource(R.drawable.progress_3);
			enhanceValue = troop.getAttack() + enhanceValue;
			ViewUtil.setRichText(window, R.id.soldier_attack, name + "："
					+ enhanceValue);
			break;

		case BattlePropDefine.PROP_DEFEND:// 防御
			progressBar.setImageResource(R.drawable.progress_4);
			enhanceValue = troop.getDefend() + enhanceValue;
			ViewUtil.setRichText(window, R.id.soldier_defense, name + "："
					+ enhanceValue);// 防御
			break;

		case BattlePropDefine.PROP_RANGE:// 射程
			progressBar.setImageResource(R.drawable.progress_5);
			break;

		case BattlePropDefine.PROP_BLOCK:// 拦截
			progressBar.setImageResource(R.drawable.progress_6);
			break;

		case BattlePropDefine.PROP_DEXTEROUS:// 灵巧
			progressBar.setImageResource(R.drawable.progress_7);
			break;

		case BattlePropDefine.PROP_SPEED:// 速度
			progressBar.setImageResource(R.drawable.progress_9);
			break;

		case BattlePropDefine.PROP_CRIT:// 暴率
			progressBar.setImageResource(R.drawable.progress_10);
			break;

		case BattlePropDefine.PROP_CRIT_MULTIPLE:// 暴伤
			progressBar.setImageResource(R.drawable.progress_11);
			if (!StringUtil.isNull(enhanceValue))
				enhanceValue = enhanceValue
						+ StringUtil.color("%", R.color.color19);
			enhanceValue = troop.getCritMultiple() + "%" + enhanceValue;
			ViewUtil.setRichText(window, R.id.soldier_hurt, name + "："
					+ enhanceValue);// 暴伤
			break;

		case BattlePropDefine.PROP_ANTICRIT:// 韧性
			progressBar.setImageResource(R.drawable.progress_12);
			break;
		}

	}

	// 元宝消耗
	private int getCost(ArmEffectClient aec) {
		return CalcUtil.upNum((aec.getExpTotal() - aec.getExp())
				* (CacheMgr.armEnhanceCostCache.getCostRmb(troopProp.getId(),
						aec.getEnhanceProp().getPropId(), aec.getEnhanceProp()
								.getLevel()) / 100f));
	}

	@Override
	protected void destory() {
		if (null != armPropViews) {
			armPropViews.clear();
			armPropViews = null;
		}
		super.destory();
	}

	// 点击事件
	private class ClickListener extends SlowOnClick {
		private ArmEffectClient aec;
		private ViewGroup viewGroup;
		private int type;

		public ClickListener(ViewGroup viewGroup, int type) {
			this.viewGroup = viewGroup;
			this.type = type;
		}

		@Override
		public void doOnClick(View v) {
			Object obj = v.getTag();
			if (obj == null)
				return;
			this.aec = (ArmEffectClient) v.getTag();
			if (Constants.TYPE_MATERIAL == type) {
				ReturnInfoClient ric = aec.getCost();
				List<ShowItem> showItems = ric.checkRequire();
				if (!showItems.isEmpty()) {
					if (AttrData.isResource(showItems.get(0).getType())
							|| AttrData.isMaterial(showItems.get(0).getType()))
						new RechargeBuyGiftTip(showItems.get(0).getType())
								.show();
					else
						controller.alert("您的" + showItems.get(0).getName()
								+ "数量不足");
					return;
				}

			} else if (Constants.TYPE_CURRENCY == type) { // 元宝强化
				int costCurrency = getCost(aec);
				if (Account.user.getCurrency() < costCurrency) {
					new ToActionTip(costCurrency).show();
					return;
				}
			}
			new EnhanceInvoker(aec, viewGroup, type).start();
		}

		@Override
		protected int getTime() {
			return 500;
		}
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	// 强化经验比值
	private class EnhanceInvoker extends BaseInvoker {
		private ViewGroup viewGroup;
		private ArmEffectClient aec;
		private ReturnInfoClient ric;
		private int level;
		private int exp;
		private int levelNew;
		private int expNew;
		private int type;

		public EnhanceInvoker(ArmEffectClient aec, ViewGroup viewGroup, int type) {
			this.aec = aec;
			this.level = aec.getLevel();
			this.exp = aec.getExp();
			this.viewGroup = viewGroup;
			this.type = type;
		}

		@Override
		protected String loadingMsg() {
			return "强化" + aec.getName();
		}

		@Override
		protected String failMsg() {
			return "强化失败";
		}

		@Override
		protected void fire() throws GameException {
			ric = GameBiz.getInstance().armEhance(troopProp.getId(),
					aec.getId(), type);
			ArmPropInfoClient apic = Account.armEnhanceCache
					.getPropByArmId(troopProp.getId());
			List<ArmEffectClient> aecs = apic.getEnhanceList();
			for (ArmEffectClient aecN : aecs) {
				if (aecN.getId().intValue() == aec.getId().intValue()) {
					this.levelNew = aecN.getLevel();
					this.expNew = aecN.getExp();
				}
			}
		}

		@Override
		protected void onOK() {
			SoundMgr.play(R.raw.sfx_strengthening);
			ctr.updateUI(ric, true);
			startAnimation(
					type,
					getEnhanceExp(aec.getId().intValue(), level, exp, levelNew,
							expNew, type), viewGroup);
			setValue();
		}

	}

	@SuppressWarnings("deprecation")
	private void startAnimation(int type, int gainExp, ViewGroup viewGroup) {
		int pos[] = new int[2];
		viewGroup.getLocationOnScreen(pos);
		ImageView imageView = new ImageView(controller.getUIContext());
		if (getDrawableRes(type, gainExp) != 0)
			ViewUtil.setImage(imageView, getDrawableRes(type, gainExp));
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, pos[0], pos[1]);
		absoluteLayou.addView(imageView, params);
		Animation animation = getAnimation(imageView);
		imageView.startAnimation(animation);
	}

	private int getDrawableRes(int type, int gainExp) {
		// 7.15：属性升级时，服务器并未告知准确的增长值
		if (3 == gainExp)
			gainExp = 4;
		else if (gainExp > 4)
			gainExp = 10;

		if (type == Constants.TYPE_CURRENCY) {
			return R.drawable.arm_enhance_level_up;
		} else {
			if (gainExp == 1) {
				return R.drawable.arm_enhance_1;
			} else if (gainExp == 2) {
				return R.drawable.arm_enhance_2;
			} else if (gainExp == 4) {
				return R.drawable.arm_enhance_4;
			} else if (gainExp == 10) {
				return R.drawable.arm_enhance_10;
			} else {
				return 0;
			}
		}

	}

	private class AnimaListener implements AnimationListener {

		View v;

		public AnimaListener(View v) {
			this.v = v;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			ViewUtil.setGone(v);
			controller.getHandler().postDelayed(new Runnable() {

				@Override
				public void run() {
					remove();
				}
			}, 1000);
		}

		private void remove() {
			if (absoluteLayou != null && absoluteLayou.indexOfChild(v) != -1) {
				v.clearAnimation();
				absoluteLayou.removeView(v);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	private Animation getAnimation(View view) {
		AnimationSet animationSet = new AnimationSet(false);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				160 * Config.SCALE_FROM_HIGH, 160 * Config.SCALE_FROM_HIGH, 0,
				-30);
		translateAnimation.setDuration(300);
		animationSet.addAnimation(translateAnimation);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
		alphaAnimation.setDuration(300);
		alphaAnimation.setStartOffset(600);
		animationSet.addAnimation(alphaAnimation);
		animationSet.setAnimationListener(new AnimaListener(view));
		return animationSet;
	}

	@SuppressWarnings("unchecked")
	private int getEnhanceExp(int propId, int level, int exp, int levelNew,
			int expNew, int type) {
		int expAdd = 0;
		List<ArmEnhanceProp> ls = CacheMgr.armEnhancePropCache.search(troopProp
				.getId());
		if (levelNew == level) {
			expAdd = expNew - exp;
		} else if (Constants.TYPE_MATERIAL == type) {
			for (ArmEnhanceProp prop : ls) {
				if (prop.getPropId() != propId)
					continue;
				if (prop.getLevel() < level)
					continue;
				else if (prop.getLevel() == level)
					expAdd += prop.getTotalExp() - exp;
				else if (prop.getLevel() > level && prop.getLevel() < levelNew)
					expAdd += prop.getTotalExp();
				else if (prop.getLevel() == levelNew)
					expAdd += expNew;
				else
					continue;
			}
		} else if (Constants.TYPE_CURRENCY == type) {
			for (ArmEnhanceProp prop : ls) {
				if (prop.getPropId() != propId)
					continue;
				if (prop.getLevel() < level)
					continue;
				expAdd += prop.getTotalExp() - exp;
			}
		}

		return expAdd;
	}
}
