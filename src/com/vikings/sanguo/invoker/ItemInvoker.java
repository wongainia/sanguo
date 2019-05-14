package com.vikings.sanguo.invoker;

import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ImageLoader;
import com.vikings.sanguo.ui.alert.RewardTip;
import com.vikings.sanguo.ui.alert.UserLevelUpTip;
import com.vikings.sanguo.utils.StringUtil;

public class ItemInvoker extends BaseInvoker {

	protected ItemBag bag;
	
	protected int count;
	
	protected boolean isOpenBox;

	protected BriefUserInfoClient user;

	protected ReturnInfoClient ri;

	private String opDesc;

	public ItemInvoker(ItemBag bag, BriefUserInfoClient user,int count,boolean isOpenBox) {
		this(bag, user, "");
		this.count=count;
		this.isOpenBox=isOpenBox;
	}

	public ItemInvoker(ItemBag bag, BriefUserInfoClient user, String opDesc) {
		this.opDesc = opDesc;
		this.bag = bag;
		this.user = user;
	}

	@Override
	protected String failMsg() {
		if (StringUtil.isNull(opDesc))
			return StringUtil.repParams(ctr
					.getString(R.string.ItemInvoker_failMsg), bag.getItem()
					.getName());
		else
			return opDesc + "失败";
	}

	@Override
	protected void fire() throws GameException {

		if (CacheMgr.dictCache.getDictInt(Dict.TYPE_ITME_ID, 1) == bag
				.getItemId()) {
			ri = GameBiz.getInstance().manorRandomMove().getRi();
			ri.setMsg("主城移动成功");
			return;
		}

		if(isOpenBox){
			ri = GameBiz.getInstance()
					.itemUse(user.getId(), bag.getId(), bag.getItemId(),0,count).getRi();
		}else{
			ri = GameBiz.getInstance()
					.itemUse(user.getId(), bag.getId(), bag.getItemId()).getRi();
		}
		if (null != ri.getHeroInfos() && !ri.getHeroInfos().isEmpty()) {
			for (HeroInfoClient hic : ri.getHeroInfos()) {
				if (hic.getId() > 0
						&& null != hic.getHeroProp()
						&& !Config.getController().hasPic(
								hic.getHeroProp().getIcon())) {
					ImageLoader.getInstance().downloadInCase(
							hic.getHeroProp().getIcon(), Config.imgUrl);
				}
			}
		}

		if (ri.getItemPack() != null && !ri.getItemPack().isEmpty()) {//背包物品
			for (ItemBag bag : ri.getItemPack()) {
				if (!Config.getController().hasPic(bag.getItem().getImage())) {
					ImageLoader.getInstance().downloadInCase(
							bag.getItem().getImage(), Config.imgUrl);
				}
			}
		}
	}

	@Override
	protected String loadingMsg() {
		if (StringUtil.isNull(opDesc))
			return StringUtil.repParams(ctr
					.getString(R.string.ItemInvoker_loadingMsg), bag.getItem()
					.getName());
		else
			return opDesc + "...";
	}

	@Override
	protected void onOK() {
		SoundMgr.play(R.raw.sfx_use);
		if (ri.getMsg() == null) {
			String title = "使用";
			if (!StringUtil.isNull(opDesc)) {
				title = opDesc;
			}

			List<ShowItem> showItems = ri.showReturn(true, false);
			if (null != showItems && !showItems.isEmpty()) {
				new RewardTip(title + "成功", showItems, true, true,
						new CallBack() {

							@Override
							public void onCall() {
								if (ri.getLevel() > 0)
									new UserLevelUpTip(ri).show();

							}
						}).show();
			} else {
				if (bag.getItemId() == Item.ITME_ID_MONEY_SPEED_UP_CARD) {
					ctr.alert("使用成功！", "你主城中的[铸币工坊]，将于接下来的24小时内产出双倍资源！", null,
							false);
				} else if (bag.getItemId() == Item.ITME_ID_FOOD_SPEED_UP_CARD) {
					ctr.alert("使用成功！", "你主城中的[粮草工坊]，将于接下来的24小时内产出双倍资源！", null,
							false);
				} else {
					ctr.alert("", title + bag.getItem().getName() + "成功", null,
							false);
				}
			}
		}
		ctr.updateUI(ri, true, false, false);
	}
}
