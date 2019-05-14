package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
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
import com.vikings.sanguo.invoker.EndGuider;
import com.vikings.sanguo.invoker.HeroRefreshInvoker;
import com.vikings.sanguo.message.HeroBuyResp;
import com.vikings.sanguo.model.HeroArmPropClient;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroInit;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroShopInfoClient;
import com.vikings.sanguo.model.HeroShopItemInfoClient;
import com.vikings.sanguo.model.WorldLevelInfoClient;
import com.vikings.sanguo.model.WorldLevelProp;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgHdCallBack;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class SingleRefreshWindow extends PopupWindow implements OnClickListener {

	private ViewGroup window, groupLayout, heroLayout;
	private ImageView bg, midLeft, midRight, top, heroRefreshMid,
			heroRefreshBottom;
	private TextView heroTalent, heroName, groupMsg;
	private Button refreshBtn, backBtn;
	private HeroInfoClient hic;
	private boolean open = false;

	private LinearLayout propertyLayout;

	private HeroShopItemInfoClient hsiic;
	private int index = 0;
	private View hdImg;

	public SingleRefreshWindow(HeroShopItemInfoClient hsiic, int index) {
		this.index = index;
		this.hsiic = hsiic;
	}

	// 引导用--用于处理假招募
	public boolean isGuild = false;

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void init() {
		window = (ViewGroup) controller.inflate(R.layout.hero_refresh);
		groupLayout = (ViewGroup) window.findViewById(R.id.groupLayout);
		ViewUtil.setGone(groupLayout);
		bg = (ImageView) window.findViewById(R.id.bg);
		heroLayout = (ViewGroup) window.findViewById(R.id.heroLayout);
		hdImg = window.findViewById(R.id.hdImg);
		midLeft = (ImageView) window.findViewById(R.id.midLeft);
		midRight = (ImageView) window.findViewById(R.id.midRight);
		top = (ImageView) window.findViewById(R.id.top);
		heroRefreshMid = (ImageView) window.findViewById(R.id.heroRefreshMid);
		heroRefreshBottom = (ImageView) window
				.findViewById(R.id.heroRefreshBottom);
		heroTalent = (TextView) window.findViewById(R.id.heroTalent);
		heroName = (TextView) window.findViewById(R.id.heroName);

		propertyLayout = (LinearLayout) window
				.findViewById(R.id.propertyLayout);

		refreshBtn = (Button) window.findViewById(R.id.refreshBtn);
		startButtonUp(100, refreshBtn, 1f);

		refreshBtn.setOnClickListener(this);
		backBtn = (Button) window.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		startButtonUp(100, backBtn, 0.5f);

		groupMsg = (TextView) window.findViewById(R.id.gradientMsg);

		ViewUtil.setText(window, R.id.title, "盲  选");

		setImage();
		setDynamic();
		controller.addContentFullScreen(window);
	}

	public void open() {
		doOpen();
	}

	private void setImage() {
		ViewUtil.setImage(bg, R.drawable.hero_bottom);
		ViewUtil.setImage(midLeft, R.drawable.hero_mid_left);
		ViewUtil.setImage(midRight, R.drawable.hero_mid_right);
		ViewUtil.setImage(top, R.drawable.hero_top);
		ViewUtil.setImage(heroRefreshMid, R.drawable.hero_refresh_mid);
		ViewUtil.setImage(heroRefreshBottom, R.drawable.hero_refresh_bottom);
		imageHolder.saveRef(hdImg);
	}

	private void setDynamic() {
		if (hsiic != null) {
			final HeroProp heroProp = hsiic.getHeroProp();
			HeroInit heroInit = hsiic.getHeroInit();
			if (heroProp == null) {
				return;
			}
			// ViewUtil.setVisible(heroRefreshMid);
			groupMsg.setVisibility(View.GONE);
			ViewUtil.setVisible(findViewById(R.id.progress_layout));
			// ViewUtil.setRichText(findViewById(R.id.loading), "");
			new ViewImgHdCallBack(heroProp.getImg(), heroLayout, hdImg,
					new CallBack() {

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
			openAnim();

			ViewUtil.setText(window, R.id.title, "招募");
			int currency = heroInit.getCurrency();
			int openLevel = heroInit.getOpenLevel();
			int rate = heroInit.getRate();
			WorldLevelProp worldLevel = WorldLevelInfoClient
					.getWorldLevelProp();
			if (worldLevel != null) {
				int level = worldLevel.getLevel();
				if (level >= openLevel) {
					if ((level - openLevel + 1) * rate > 90) {
						currency = currency * 10 / 100;
					} else {
						currency = currency
								* (100 - (level - openLevel + 1) * rate) / 100;
					}
				}
			}

			ViewUtil.setRichText(refreshBtn, "招  募" + "#rmb#" + currency, true);

			propertyLayout.setVisibility(View.VISIBLE);

			ViewUtil.setRichText(heroName, heroProp.getName());
			ViewUtil.setRichText(heroTalent, ""
					+ hsiic.getHeroQuality().getName());

			ViewUtil.setRichText(window.findViewById(R.id.force), "武力:   "
					+ heroInit.getAttack());
			ViewUtil.setRichText(window.findViewById(R.id.defend), "防护:   "
					+ heroInit.getDefend());

			if (heroProp.getStrongestTag() == 1) {
				window.findViewById(R.id.most_stronger).setVisibility(
						View.VISIBLE);
			} else {
				window.findViewById(R.id.most_stronger)
						.setVisibility(View.GONE);
			}

			window.findViewById(R.id.hero_detail).setVisibility(View.VISIBLE);
			window.findViewById(R.id.hero_detail).setOnClickListener(this);
			ViewUtil.setImage(window.findViewById(R.id.rating),
					heroProp.getRatingPic());

			ViewGroup vg = (ViewGroup) window
					.findViewById(R.id.proficientLayout);

			List<HeroArmPropClient> ls;
			try {
				ls = heroInit.getHeroArmPropsEx();
				if (!ListUtil.isNull(ls)) {
					ViewUtil.setVisible(vg);
					vg.removeAllViews();
					ViewUtil.setVisible(window.findViewById(R.id.skilled));
					for (int i = 0; i < ls.size(); i++) {
						if (ls.get(i).getHeroTroopName().getType() == 0) {
							String txt = ls.get(i).getHeroTroopName().getSlug();
							TextView iv = new TextView(Config.getController()
									.getUIContext());
							LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
									-2, -2);
							lp.leftMargin = (int) (4 * Config.SCALE_FROM_HIGH);
							iv.setText(txt);
							vg.addView(iv, lp);
						} else {
							String img = ls.get(i).getHeroTroopName()
									.getSmallIcon();
							ImageView iv = new ImageView(Config.getController()
									.getUIContext());
							LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
									-2, -2);
							lp.leftMargin = (int) (4 * Config.SCALE_FROM_HIGH);
							ViewUtil.setImage(iv, img);
							vg.addView(iv, lp);
						}
						//
						// ImageView iv = (ImageView) vg.getChildAt(i);
						// ViewUtil.setImage(iv,img);
					}
				}
			} catch (GameException e) {
				e.printStackTrace();
			}

		}

		else if (null != hic) {
			hic.getArmPropInfos();
			HeroProp heroProp = hic.getHeroProp();
			if (heroProp == null) {
				return;
			}
			// ViewUtil.setVisible(heroRefreshMid);
			groupMsg.setVisibility(View.GONE);
			ViewUtil.setVisible(findViewById(R.id.progress_layout));
			// ViewUtil.setRichText(findViewById(R.id.loading), "");
			new ViewImgHdCallBack(heroProp.getImg(), heroLayout, hdImg,
					new CallBack() {

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
			ViewUtil.setRichText(
					refreshBtn,
					"再选一次"
							+ "#rmb#"
							+ CacheMgr.heroCommonConfigCache
									.getSingleRefreshHeroPrice(), true);
			propertyLayout.setVisibility(View.VISIBLE);
			ViewUtil.setRichText(heroName, heroProp.getName());
			ViewUtil.setRichText(heroTalent, ""
					+ hic.getHeroQuality().getName());

			ViewUtil.setRichText(window.findViewById(R.id.force), "武力:   "
					+ hic.getRealAttack());
			ViewUtil.setRichText(window.findViewById(R.id.defend), "防护:   "
					+ hic.getRealDefend());

			if (hic.getHeroProp().getStrongestTag() == 1) {
				window.findViewById(R.id.most_stronger).setVisibility(
						View.VISIBLE);
			} else {
				window.findViewById(R.id.most_stronger)
						.setVisibility(View.GONE);
			}
			ViewUtil.setImage(window.findViewById(R.id.rating),
					heroProp.getRatingPic());
			window.findViewById(R.id.hero_detail).setVisibility(View.VISIBLE);
			window.findViewById(R.id.hero_detail).setOnClickListener(this);
			List<HeroArmPropInfoClient> ls = hic.getArmPropInfos();
			ViewGroup vg = (ViewGroup) window
					.findViewById(R.id.proficientLayout);

			if (!ListUtil.isNull(ls)) {
				ViewUtil.setVisible(vg);
				vg.removeAllViews();
				ViewUtil.setVisible(window.findViewById(R.id.skilled));
				for (int i = 0; i < ls.size(); i++) {
					String img = ls.get(i).getHeroTroopName().getSmallIcon();
					ImageView iv = new ImageView(Config.getController()
							.getUIContext());
					LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
							-2, -2);
					lp.leftMargin = (int) (4 * Config.SCALE_FROM_HIGH);
					ViewUtil.setImage(iv, img);
					vg.addView(iv, lp);
					//
					// ImageView iv = (ImageView) vg.getChildAt(i);
					// ViewUtil.setImage(iv,img);
				}
			}

		} else {
			// ViewUtil.setGone(heroRefreshMid);
			groupMsg.setVisibility(View.VISIBLE);
			ViewUtil.setText(groupMsg, "点击【盲选】随机获得一位女神");

			hdImg.setBackgroundDrawable(null);
			ViewUtil.setRichText(
					refreshBtn,
					"盲  选"
							+ "#rmb#"
							+ CacheMgr.heroCommonConfigCache
									.getSingleRefreshHeroPrice(), true);
			ViewUtil.setText(heroName, "");
			ViewUtil.setText(heroTalent, "");
		}
	}

	@Override
	public void showUI() {
		super.showUI();
		controller.hideIconForFullScreen();
	}

	@Override
	public void hideUI() {
		super.hideUI();
		controller.showIconForFullScreen();
	}

	@Override
	protected void destory() {
		controller.removeContentFullScreen(window);
	}

	@Override
	public void onClick(View v) {
		if (v == refreshBtn) {
			// 引导走的代码
			if (isGuild) {
				new EndGuideStep201(BaseStep.INDEX_STEP201).start();
				isGuild = false;
				return;
			}

			if (hsiic != null) {
				new HeroBuy(hsiic).start();
			} else {
				if (open)
					closeAnim();
				new SingleHeroRefresh().start();
			}
		} else if (v == backBtn) {
			controller.goBack();
		} else if (v.getId() == R.id.hero_detail) {
			HeroProp heroProp = null;
			if (hsiic != null) {
				heroProp = hsiic.getHeroProp();
			} else if (hic != null) {
				heroProp = hic.getHeroProp();
			}
			if(heroProp != null)
				new HeroDetailHDWindow().open(heroProp,true);
		}
	}

	private void openAnim() {
		open = true;
		ScaleAnimation animLeft = new ScaleAnimation(1, 0.3f, 1, 1,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		animLeft.setDuration(600);
		animLeft.setFillAfter(true);
		animLeft.setInterpolator(new AccelerateInterpolator());

		ScaleAnimation animRight = new ScaleAnimation(1, 0.3f, 1, 1,
				Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
		animRight.setDuration(600);
		animRight.setFillAfter(true);
		animRight.setInterpolator(new AccelerateInterpolator());

		midLeft.startAnimation(animLeft);
		midRight.startAnimation(animRight);

	}

	private void closeAnim() {
		open = false;
		ScaleAnimation animLeft = new ScaleAnimation(0.3f, 1, 1, 1,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		animLeft.setDuration(200);
		animLeft.setFillAfter(true);
		animLeft.setInterpolator(new AccelerateInterpolator());

		ScaleAnimation animRight = new ScaleAnimation(0.3f, 1, 1, 1,
				Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
		animRight.setDuration(200);
		animRight.setFillAfter(true);
		animRight.setInterpolator(new AccelerateInterpolator());

		midLeft.startAnimation(animLeft);
		midRight.startAnimation(animRight);
	}

	private class SingleHeroRefresh extends HeroRefreshInvoker {

		public SingleHeroRefresh() {
			super(TYPE_SINGLE);
		}

		@Override
		protected void refreshUI() {
			hic = resp.getHic();
			setDynamic();
			openAnim();
		}
	}

	private void startButtonUp(int time, Button btn, float rate) {
		TranslateAnimation rollBack = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_PARENT, rate,
				Animation.RELATIVE_TO_PARENT, 0);
		rollBack.setDuration(time);
		rollBack.setFillAfter(true);
		rollBack.setInterpolator(new DecelerateInterpolator());
		rollBack.setStartOffset(time);

		btn.startAnimation(rollBack);

	}

	private class EndGuideStep201 extends EndGuider {
		@Override
		protected void refreshUI() {
			hic = rs.getHeroInfos().get(0);
			setDynamic();
			openAnim();
		}

		public EndGuideStep201(int trainingId) {
			super(trainingId);
		}

	}

	private class HeroBuy extends BaseInvoker {
		private HeroShopItemInfoClient hsiic;
		private HeroBuyResp resp;

		public HeroBuy(HeroShopItemInfoClient hsiic) {
			this.hsiic = hsiic;
		}

		@Override
		protected String loadingMsg() {
			return "招募中";
		}

		@Override
		protected String failMsg() {
			return "招募失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().heroBuy(index);
		}

		@Override
		protected void onOK() {
			controller.updateUI(resp.getRi(), true);
			if (hsiic != null) {
				HeroShopInfoClient hsic = Account.myLordInfo.getHeroShopInfo();
				if (hsic != null) {
					List<HeroShopItemInfoClient> heroItems = hsic.getInfos();
					for (int i = 0; i < 8; i++) {
						HeroShopItemInfoClient shopItem = heroItems.get(i);
						shopItem.setRecruit(false);
					}
					if (ListUtil.isNull(heroItems) == false
							&& heroItems.size() > index) {
						HeroShopItemInfoClient infoClient = heroItems
								.get(index);
						infoClient.setBought(true);
						infoClient.setRecruit(true);
					}
				}
				controller.goBack();
			}
		}
	}
}
