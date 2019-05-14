package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.HeroFavourResp;
import com.vikings.sanguo.model.HeroFavour;
import com.vikings.sanguo.model.HeroFavourWords;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.PropHeroFavour;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.NullImgCallBack;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.thread.ViewImgGrayCallBack;
import com.vikings.sanguo.thread.ViewImgHdCallBack;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroFavourWindow extends PopupWindow implements OnClickListener {

	private ViewGroup window, heroLayout;
	private LinearLayout luckLayout;
	private ProgressBar progressBar;
	private TextView theResult, theTime, progressDesc, costDesc, favourMe,
			luckDesc;
	private ViewGroup[] happyTooles = new ViewGroup[4];
	private View hdImg, top, bgLayout, heroRefreshMid, heroRefreshBottom,
			patronizeBottom, groupLayout;
	private List<PropHeroFavour> favours;
	private List<HeroFavourWords> allWords;
	private List<HeroFavourWords> favourOne = new ArrayList<HeroFavourWords>();// 满足宠幸条件1
																				// 的id
	private List<HeroFavourWords> favourTwo = new ArrayList<HeroFavourWords>();// 满足宠幸条件2
																				// 的id
	private List<HeroFavourWords> favourThree = new ArrayList<HeroFavourWords>();// 满足宠幸条件3
																					// 的id
	private int outExp = 0;// 上一次兴奋值更新的值
	private boolean isChanger;// 兴奋值在0~100间是否有变动
	private HeroInfoClient hic;
	private FrameLayout animLayout;

	private TextView exciteLevelTv; // 兴奋等级
	private TextView favourRemark;

	private int exciteLevel = 0;// 兴奋值等级

	private TextView attack_add;
	private TextView defend_add;

	private boolean isDownDress = false; // 是否开始下载脱衣图
	private int convertHeroId = 0;
	private String dressImg;
	private int heroId = 0;

	private boolean openPlay = false;
	private ImageView midLeft, midRight;

	private TextView excite_full_tip;

	public HeroFavourWindow(HeroInfoClient hic) {
		super();
		this.hic = hic;
		favours = CacheMgr.propHeroFavourCache.getAll();
		allWords = CacheMgr.heroFavourWordCache.getAll();
		if (hic != null) {
			heroId = hic.getHeroId();
		}
	}

	public void open() {
		doOpen();
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void init() {
		window = (ViewGroup) controller.inflate(R.layout.hero_patronize);
		luckLayout = (LinearLayout) window.findViewById(R.id.luckLayout);

		theResult = (TextView) window.findViewById(R.id.the_result);
		theTime = (TextView) window.findViewById(R.id.the_time);
		costDesc = (TextView) window.findViewById(R.id.costDesc);
		favourMe = (TextView) window.findViewById(R.id.favourMe);
		luckDesc = (TextView) window.findViewById(R.id.luckDesc);

		progressBar = (ProgressBar) window.findViewById(R.id.progressBar);
		progressDesc = (TextView) window.findViewById(R.id.progressDesc);

		// 兴奋级别
		exciteLevelTv = (TextView) window.findViewById(R.id.excite_level);
		favourRemark = (TextView) window.findViewById(R.id.favourRemark);

		attack_add = (TextView) window.findViewById(R.id.attack_add);
		defend_add = (TextView) window.findViewById(R.id.defend_add);

		animLayout = (FrameLayout) window.findViewById(R.id.animLayout);
		heroLayout = (ViewGroup) window.findViewById(R.id.heroLayout);
		hdImg = heroLayout.findViewById(R.id.hdImg);

		happyTooles[0] = (ViewGroup) window.findViewById(R.id.tools1);

		happyTooles[1] = (ViewGroup) window.findViewById(R.id.tools2);

		happyTooles[2] = (ViewGroup) window.findViewById(R.id.tools3);

		happyTooles[3] = (ViewGroup) window.findViewById(R.id.tools4);

		top = window.findViewById(R.id.top);
		bgLayout = window.findViewById(R.id.bgLayout);
		heroRefreshMid = window.findViewById(R.id.heroRefreshMid);
		heroRefreshBottom = window.findViewById(R.id.heroRefreshBottom);
		// patronizeBottom = window.findViewById(R.id.patronizeBottom);
		groupLayout = window.findViewById(R.id.groupLayout);

		midLeft = (ImageView) window.findViewById(R.id.midLeft);
		midRight = (ImageView) window.findViewById(R.id.midRight);
		excite_full_tip = (TextView) window.findViewById(R.id.excite_full_tip);
		initImg();

		setWordsItem();
		setTextOneShow();

		setFavourTip();
		setFavourLevel();
		controller.addContentFullScreen(window);
	}

	private void initImg() {
		ViewUtil.setImage(top, R.drawable.hero_top);
		ViewUtil.setImage(bgLayout, R.drawable.hero_bottom);
		ViewUtil.setImage(heroRefreshMid, R.drawable.hero_refresh_mid);
		ViewUtil.setImage(heroRefreshBottom, R.drawable.hero_refresh_bottom);
		// ViewUtil.setImage(patronizeBottom, R.drawable.patronize_bottom);
		ViewUtil.setImage(groupLayout, R.drawable.patronize_bottom);

		ViewUtil.setImage(midLeft, R.drawable.hero_mid_left);
		ViewUtil.setImage(midRight, R.drawable.hero_mid_right);
		ViewUtil.setHide(midLeft);
		ViewUtil.setHide(midRight);
		imageHolder.saveRef(hdImg);
	}

	// 调戏方式(蜡烛、面具、皮鞭、麻绳)
	private void setItem() {
		for (int i = 0; i < happyTooles.length; i++) {
			if (i < favours.size()) {
				View view = happyTooles[i];
				PropHeroFavour favour = favours.get(i);
				ViewUtil.setText(view.findViewById(R.id.name), favour.getName());
				if ((null != hic.getFavourInfoClient() && hic
						.getFavourInfoClient().hasValue(favour.getId()))
						|| heroId != hic.getHeroId()) {
					new ViewImgGrayCallBack(favour.getImage(),
							view.findViewById(R.id.icon));
					view.setOnClickListener(null);
					view.setTag(null);
				} else {
					new ViewImgCallBack(favour.getImage(),
							view.findViewById(R.id.icon));
					view.setOnClickListener(this);
					view.setTag(favour);
				}
			}
		}
	}

	private void setItemGray() {
		for (int i = 0; i < happyTooles.length; i++) {
			View view = happyTooles[i];
			PropHeroFavour favour = favours.get(i);
			ViewUtil.setText(view.findViewById(R.id.name), favour.getName());
			{
				new ViewImgGrayCallBack(favour.getImage(),
						view.findViewById(R.id.icon));
				view.setOnClickListener(null);
				view.setTag(null);
			}
		}
	}

	// 宠幸调戏语的条件分组
	private void setWordsItem() {
		for (int i = 0; i < allWords.size(); i++) {
			HeroFavourWords hfws = allWords.get(i);
			if (hfws.getState() == HeroFavourWords.FAVOURWORD_ONE) {
				favourOne.add(hfws);
				continue;
			}
			if (hfws.getState() == HeroFavourWords.FAVOURWORD_TWO) {
				favourTwo.add(hfws);
				continue;
			}
			if (hfws.getState() == HeroFavourWords.FAVOURWORD_THREE) {
				favourThree.add(hfws);
				continue;
			}
		}
	}

	// 更具条件，返回相应的气泡内容（条件2、条件3 随机的内容）
	private HeroFavourWords setFavourWords(int state) {
		HeroFavourWords word = new HeroFavourWords();
		if (state == HeroFavourWords.FAVOURWORD_TWO) {
			int favourWord = (int) (Math.random() * favourTwo.size());
			for (int i = 0; i < favourTwo.size(); i++) {
				if (i == favourWord) {
					word = favourTwo.get(i);
				}
			}
		} else if (state == HeroFavourWords.FAVOURWORD_THREE) {
			int favourWord = (int) (Math.random() * favourThree.size());
			for (int i = 0; i < favourThree.size(); i++) {
				if (i == favourWord) {
					word = favourThree.get(i);
				}
			}
		}
		return word;
	}

	// 宠幸气泡内容的前提条件
	private int favourState() {
		int state = 2;

		// 条件2 为激活技能状态下
		if (getPatronizeTime() > 0 && getPatronizeTime() < 100
				&& !getIsChanger()) {
			state = HeroFavourWords.FAVOURWORD_TWO;
			return state;
		}

		return state;
	}

	// 宠幸（显示3秒，三秒内操作内容会更换且只在这3秒剩余时间内显示）
	private void setTextShow() {
		ViewUtil.setVisible(favourMe);
		HeroFavourWords words = setFavourWords(favourState());
		ViewUtil.setText(favourMe, words.getFavourDesc());

		long nowTime = System.currentTimeMillis();
		favourMe.setTag(nowTime);
		favourMe.postDelayed(new MyRunble(nowTime, favourMe), 2000);
	}

	// 进入宠幸主页，未激活技能状态时显示的内容
	private void setTextOneShow() {
		if (getTimeNum(hic) <= 0) {
			int favourWord = (int) (Math.random() * favourOne.size());
			HeroFavourWords words = new HeroFavourWords();
			for (int i = 0; i < favourOne.size(); i++) {
				if (i == favourWord) {
					words = favourOne.get(i);
				}
			}
			ViewUtil.setVisible(favourMe);
			ViewUtil.setText(favourMe, words.getFavourDesc());

			long nowTime = System.currentTimeMillis();
			favourMe.setTag(nowTime);
			favourMe.postDelayed(new MyRunble(nowTime, favourMe), 2000);
		} else {
			ViewUtil.setGone(favourMe);
		}
	}

	// 激活宠幸技能提示
	private void setLuckSkill() {
		if (0 < hic.getFavourInfoClient().getLevel()) {
			ViewUtil.setVisible(luckLayout);

			int attackEnhan = 0;
			int defendEnhan = 0;

			HeroFavour hf = null;
			hf = CacheMgr.heroFavourCache.getHeroFavour(exciteLevel);

			if (hf != null) {
				attackEnhan = hf.getAttackEnhan();
				defendEnhan = hf.getDefendEnhan();
			}
			String attack = attackEnhan > 0 ? ("武力值+" + attackEnhan) : "";
			String defend = defendEnhan > 0 ? ("      防护值+" + defendEnhan) : "";
			StringBuilder addValue = new StringBuilder();
			if (StringUtil.isNull(attack) == false) {
				addValue.append(attack);
			}
			if (StringUtil.isNull(defend) == false) {
				addValue.append(defend);
			}

			if (StringUtil.isNull(addValue.toString()) == false) {
				if (heroId != hic.getHeroId()) {
					ViewUtil.setVisible(window.findViewById(R.id.dressTip));
					String hint = addValue.toString() + "\n进化为脱衣将, 获得五转能力";
					ViewUtil.setText(luckDesc, hint);
				} else {
					ViewUtil.setGone(window.findViewById(R.id.dressTip));
					ViewUtil.setText(luckDesc, addValue);
				}
			} else {
				ViewUtil.setGone(luckLayout);
			}

			// 兴奋值满条
			progressBar.set(100, 100);// 兴奋值进度条
			ViewUtil.setText(progressDesc, 100 + "/" + 100);// 兴奋值进度显示满

			// 把道具变灰
			setItemGray();

			long nowTime = System.currentTimeMillis();
			luckLayout.setTag(nowTime);
			Config.getController().getHandler()
					.postDelayed(new MyRunble(nowTime, luckLayout), 2000);
		} else {
			ViewUtil.setGone(luckLayout);
		}
	}

	// 设置值
	private void setValue() {
		if (heroId == hic.getHeroId()) {
			ViewUtil.setVisible(window.findViewById(R.id.costTipLayout));
			ViewUtil.setRichText(costDesc, R.id.costDesc, getCostDesc());
		} else {
			ViewUtil.setHide(window.findViewById(R.id.costTipLayout));
		}

		if (heroId != 0 && heroId != hic.getHeroId()) {
			convertHeroId = hic.getHeroId();
			if (StringUtil.isNull(dressImg) == true) {
				HeroProp hp = null;
				try {
					hp = (HeroProp) CacheMgr.heroPropCache.get(convertHeroId);
				} catch (GameException e) {
					e.printStackTrace();
				}
				if (hp != null) {
					dressImg = hp.getImg();
				}
			}
			if (StringUtil.isNull(dressImg) == false) {

				// closeAnim();
				// new ViewImgHdCallBack(dressImg, heroLayout, hdImg,new
				// CallBack() {
				//
				// @Override
				// public void onCall() {
				// ViewUtil.setGone(findViewById(R.id.progress_layout));
				// }
				// }, new CallBack() {
				//
				// @Override
				// public void onCall() {
				// ViewUtil.setRichText(findViewById(R.id.loading),
				// "加载失败！");
				//
				// }
				// });
				// openAnim();
			}
		} else {
			ViewUtil.setVisible(findViewById(R.id.progress_layout));
			new ViewImgHdCallBack(hic.getHeroProp().getImg(), heroLayout,
					hdImg, new CallBack() {

						@Override
						public void onCall() {
							ViewUtil.setGone(findViewById(R.id.progress_layout));
						}
					}, new CallBack() {

						@Override
						public void onCall() {
							ViewUtil.setRichText(findViewById(R.id.loading),
									"加载失败！");

						}
					});
		}
		setViewValues();
	}

	// 需要刷新的页面控件（*宠幸叫唤对话框显示3秒，不能放入每秒页面刷新*）
	private void setViewValues() {
		setItem();// 宠幸方式
		if (heroId != hic.getHeroId()) {
			progressBar.set(100, 100);// 兴奋值进度条
			ViewUtil.setText(progressDesc, 100 + "/" + 100);// 兴奋值进度显示满
		} else {
			progressBar.set(getPatronizeTime(), 100);// 兴奋值进度条
			ViewUtil.setText(progressDesc, getPatronizeTime() + "/" + 100);// 兴奋值进度显示值
		}
		// 显示兴奋值 满后攻击、防护
		int attackEnhan = 0;
		int defendEnhan = 0;
		HeroFavour hf = null;
		if (heroId == hic.getHeroId()) {
			hf = CacheMgr.heroFavourCache.getHeroFavour(exciteLevel + 1);
			if (hf != null) {
				attackEnhan = hf.getAttackEnhan();
				defendEnhan = hf.getDefendEnhan();
			}
			ViewUtil.setVisible(window.findViewById(R.id.exciteFullTip));
			if (attackEnhan > 0) {
				attack_add.setText("武力值+" + attackEnhan);
			}

			if (defendEnhan > 0) {
				defend_add.setText("防护值+" + defendEnhan);
			}
		} else {
			ViewUtil.setGone(window.findViewById(R.id.exciteFullTip));
		}
		setFavourLevel();
	}

	@Override
	protected void destory() {
		controller.removeContentFullScreen(window);
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
		controller.hideIconForFullScreen();
	}

	@Override
	public void hideUI() {
		super.hideUI();
		controller.showIconForFullScreen();
	}

	// 宠幸方式的点击事件
	@Override
	public void onClick(View v) {
		if (Account.user.getCurrency() < getCost()) {
			new ToActionTip(getCost()).show();
			return;
		} else {
			for (int i = 0; i < happyTooles.length; i++) {
				if (v.getId() == happyTooles[i].getId()) {
					Object obj = happyTooles[i].getTag();
					if (null != obj) {
						new HeroPatronizeInvoker(hic, (PropHeroFavour) obj)
								.start();
						luckLayout.setVisibility(View.GONE);
					}
				}
			}
		}
	}

	// 宠幸技能的时间---->Int类型
	private int getTimeNum(HeroInfoClient heroInfo) {
		return 0;
	}

	// 兴奋值Invoker
	private class HeroPatronizeInvoker extends BaseInvoker {
		private HeroInfoClient hic;
		private HeroFavourResp ric;
		private PropHeroFavour hfs;// 使用道具
		private int before;// 宠幸之前的宠幸值

		public HeroPatronizeInvoker(HeroInfoClient hic, PropHeroFavour hfs) {
			this.hic = hic;
			this.before = getFavourExp(hic);
			this.hfs = hfs;
		}

		protected int getFavourExp(HeroInfoClient hic) {
			return null == hic.getFavourInfoClient() ? 0 : hic
					.getFavourInfoClient().getExp();
		}

		@Override
		protected String loadingMsg() {
			return "宠幸" + hic.getHeroName();
		}

		@Override
		protected String failMsg() {
			return "宠幸失败";
		}

		@Override
		protected void fire() throws GameException {
			ric = GameBiz.getInstance().heroFavour(hic.getId(), hfs.getId());
			hic.update(ric.getHic());
		}

		@Override
		protected void onOK() {
			SoundMgr.play(R.raw.sfx_strengthening);
			int after = getFavourExp(hic);
			// startAnimation((after == 0 ? 100 : after) -
			// before,progressDesc);// 出现的兴奋值显示

			for (int i = 0; i < happyTooles.length; i++) {
				if (hfs == happyTooles[i].getTag()) {
					startAnimationConsume(getCost(), happyTooles[i]);
					break;
				}
			}

			if (after != 0 && after != 100) {
				setTextShow();// 宠幸文字
			}
			exciteLevel = hic.getFavourInfoClient().getLevel();
			if (after == 0) {
				setLuckSkill();// 激活宠幸技能提示
				// 从第17级开始下载脱衣图
				if (exciteLevel == 17 && isDownDress == false) {
					if (hic != null) {
						int heroId = hic.getHeroId();
						if (heroId != 0) {
							downDress(heroId);
						}
					}
				}
			} else {
				setValue();
			}
		}
	}

	// 宠幸消耗元宝
	private int getCost() {
		return CacheMgr.heroFavourCache.getFavourCost(exciteLevel);
	}

	// 宠幸消耗提示
	private String getCostDesc() {
		StringBuffer buf = new StringBuffer();
		buf.append("#rmb#").append(getCost()).append("(");
		if (Account.user.getCurrency() < getCost()) {
			buf.append(StringUtil.color(Account.user.getCurrency() + "",
					R.color.color11) + ")");
		} else {
			buf.append(Account.user.getCurrency() + ")");
		}
		String costDesc = buf.toString();
		return costDesc;
	}

	// 兴奋值在0~100间时每五分钟扣10点
	private int getPatronizeTime() {
		int exp = hic.getFavourInfoClient().getExp();
		int time = (int) (Config.serverTime() / 1000);
		int startTime = hic.getFavourInfoClient().getExpUpdateTime();
		if (exp > 0 && exp < 100) {
			exp = exp - (time - startTime) / 300 * 10;
		}
		if (exp <= 0) {
			exp = 0;
		}
		return exp;
	}

	// 兴奋值在0~100间有否改变（用于宠幸叫唤内容选择判断）
	private boolean getIsChanger() {
		if (getPatronizeTime() != 0) {
			isChanger = outExp == getPatronizeTime() ? true : false;
		} else {
			outExp = 0;
			isChanger = true;
		}

		if (!isChanger) {
			outExp = getPatronizeTime();
		}
		return isChanger;
	}

	// 兴奋值动画没有完成！
	private void startAnimation(int gainExp, View v) {
		if (gainExp <= 0)
			return;
		int pos[] = new int[2];
		v.getLocationOnScreen(pos);
		TextView textView = new TextView(controller.getUIContext());

		String str = "#favour_add#" + StringUtil.numImgStr("favour_", gainExp);
		LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = (int) (pos[0] + 10 * Config.SCALE_FROM_HIGH);
		params.topMargin = pos[1];

		animLayout.addView(textView, params);
		ViewUtil.setRichText(textView, str);
		Animation animation = getAnimation(textView);
		textView.startAnimation(animation);
	}

	private void startAnimationConsume(int cost, View v) {
		if (cost <= 0)
			return;
		int pos[] = new int[2];
		v.getLocationOnScreen(pos);
		TextView textView = new TextView(controller.getUIContext());
		textView.setSingleLine(true);
		String str = "#rmb#" + "#!btl_minus#"
				+ StringUtil.numImgStrNoScale("btl_", cost);

		int left = (int) (pos[0] - 10 * Config.SCALE_FROM_HIGH);
		int top = pos[1];
		if (v.getId() == R.id.tools4) {
			left = left - v.getWidth() / 2;
		}
		LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = left;
		params.topMargin = top;
		animLayout.addView(textView, params);
		ViewUtil.setRichText(textView, str, false, 30, 30);
		Animation animation = getAnimation(textView);
		textView.startAnimation(animation);
	}

	private class AnimaListener implements AnimationListener {

		private View view;

		public AnimaListener(View view) {
			this.view = view;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			ViewUtil.setGone(view);
			controller.getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					remove();
				}
			}, 100);
		}

		private void remove() {
			if (animLayout != null && animLayout.indexOfChild(view) != -1) {
				view.clearAnimation();
				animLayout.removeView(view);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	private Animation getAnimation(View view) {
		AnimationSet animationSet = new AnimationSet(false);
		// 往上移动
		TranslateAnimation transAnim = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -5f);
		transAnim.setDuration(900);
		transAnim.setInterpolator(new AccelerateDecelerateInterpolator());
		animationSet.addAnimation(transAnim);
		// 消失动画
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0f);
		alphaAnim.setDuration(300);
		alphaAnim.setStartOffset(600);
		animationSet.addAnimation(alphaAnim);
		animationSet.setAnimationListener(new AnimaListener(view));
		return animationSet;
	}

	// 刷新宠幸对话框内容线程
	private class MyRunble implements Runnable {
		private long time;
		private View view;

		public MyRunble(long time, View view) {
			this.time = time;
			this.view = view;
		}

		@Override
		public void run() {
			Object obj = view.getTag();
			if (null != obj) {
				long l = (Long) obj;
				if (time != l) {
					return;
				}
			}
			ViewUtil.setGone(view);
			if (view == luckLayout) {
				setValue();
				if (heroId != hic.getHeroId() && openPlay == false) {
					closeAnim();
				}
			}
		}
	}

	private void setFavourTip() {
		StringBuilder multiWord = new StringBuilder();
		multiWord.append("兴奋20级, 可获得");
		String cloth1 = StringUtil.color("换装将领", Config.getController()
				.getResourceColorText(R.color.color10));
		multiWord.append(cloth1);
		multiWord.append("并且获得");
		String cloth2 = StringUtil.color(" \"五转\" ", Config.getController()
				.getResourceColorText(R.color.color10));
		multiWord.append(cloth2);
		multiWord.append("资格!");
		ViewUtil.setRichText(favourRemark, multiWord.toString());
	}

	private void setFavourLevel() {
		if (hic != null) {
			if (hic.getFavourInfoClient() != null) {
				exciteLevel = hic.getFavourInfoClient().getLevel();
				exciteLevelTv.setText("兴奋" + exciteLevel + "级 :");
				excite_full_tip.setText("兴奋" + (exciteLevel + 1) + "级 :");
			}
		}
	}

	// 开始下载脱衣图片
	private void downDress(int heroId) {
		convertHeroId = CacheMgr.heroFavourConvertCache
				.getConvertHeroId(heroId);
		if (convertHeroId != 0) {
			HeroProp hp = null;
			try {
				hp = (HeroProp) CacheMgr.heroPropCache.get(convertHeroId);
			} catch (GameException e) {
				e.printStackTrace();
			}
			if (hp != null) {
				dressImg = hp.getImg();
				if (StringUtil.isNull(dressImg) == false) {
					new NullImgCallBack(hp.getImg());
					isDownDress = true;
				}
			}
		}
	}

	private void closeAnim() {
		openPlay = true;
		ScaleAnimation animLeft = new ScaleAnimation(0.3f, 1, 1, 1,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		animLeft.setDuration(400);
		animLeft.setFillAfter(true);
		animLeft.setInterpolator(new AccelerateInterpolator());

		ScaleAnimation animRight = new ScaleAnimation(0.3f, 1, 1, 1,
				Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
		animRight.setDuration(400);
		animRight.setFillAfter(true);
		animRight.setInterpolator(new AccelerateInterpolator());

		ViewUtil.setVisible(midLeft);
		ViewUtil.setVisible(midRight);

		midLeft.startAnimation(animLeft);
		midRight.startAnimation(animRight);

		animRight.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				hdImg.setBackgroundDrawable(null);
				ViewUtil.setVisible(findViewById(R.id.progress_layout));
				if (StringUtil.isNull(dressImg)) {
					if (hic.getHeroProp() != null) {
						dressImg = hic.getHeroProp().getImg();
					}
				}
				if (StringUtil.isNull(dressImg) == false) {
					new ViewImgHdCallBack(dressImg, heroLayout, hdImg,
							new CallBack() {

								@Override
								public void onCall() {
									ViewUtil.setGone(findViewById(R.id.progress_layout));
								}
							}, new CallBack() {
								@Override
								public void onCall() {
									ViewUtil.setRichText(
											findViewById(R.id.loading), "加载失败！");
								}
							});
				}
				openAnim();
			}
		});
	}

	private void openAnim() {
		ScaleAnimation animLeft = new ScaleAnimation(1, 0.3f, 1, 1,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		animLeft.setDuration(400);
		animLeft.setFillAfter(true);
		animLeft.setInterpolator(new AccelerateInterpolator());

		ScaleAnimation animRight = new ScaleAnimation(1, 0.3f, 1, 1,
				Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
		animRight.setDuration(400);
		animRight.setFillAfter(true);
		animRight.setInterpolator(new AccelerateInterpolator());

		midLeft.startAnimation(animLeft);
		midRight.startAnimation(animRight);

		animRight.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {

			}
		});

	}
}
