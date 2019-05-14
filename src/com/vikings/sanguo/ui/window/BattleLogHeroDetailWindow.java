package com.vikings.sanguo.ui.window;

import java.util.List;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.BattleLogHeroInfoClient;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.OtherHeroArmPropInfoClient;
import com.vikings.sanguo.utils.ViewUtil;

public class BattleLogHeroDetailWindow extends OthersHeroDetailWindow {
	private BattleLogHeroInfoClient blhic;

	public void open(BattleLogHeroInfoClient blhic) {
		if (null == blhic) {
			controller.alert("您查看的将领不存在!");
			return;
		}
		this.blhic = blhic;
		//doOpen();
		super.open(true);
	}

	@Override
	protected boolean isMineHero() {
		return false;
	}

	@Override
	protected BaseHeroInfoClient getBaseHeroInfoClient() {
		return blhic.getBaseHeroInfoClient();
	}

	@Override
	protected List<OtherHeroArmPropInfoClient> getArmPropInfoClients() {
		return blhic.getArmPropInfos();
	}

	@Override
	protected int getDefend() {
		return blhic.getDefend();
	}

	@Override
	protected int getAttack() {
		return blhic.getAttack();
	}

	@Override
	protected List<HeroSkillSlotInfoClient> getSkillSlotInfoClients() {
		return blhic.getSkillInfos();
	}

	@Override
	protected boolean isSuit() {
		return blhic.isHeroSuit();
	}

	@Override
	protected int getAbility() {
		return blhic.getHeroAbility();
	}
	
	@Override
	public void showUI()
	{
		// TODO Auto-generated method stub
		super.showUI();
		//if(blhic.getUserId() != Account.user.getId())
		{
			heroIconLayout.setOnClickListener(null);
			ViewUtil.setGone(seeLargePic);
		}
	}

}
