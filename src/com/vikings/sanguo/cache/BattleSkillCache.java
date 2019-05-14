package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.thread.NullImgCallBack;

public class BattleSkillCache extends LazyLoadCache {

	private static final String FILE_NAME = "battle_skill.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BattleSkill) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return BattleSkill.fromString(line);
	}

	/**
	 * 取一个大类下的所有一级技能
	 * 
	 * @param mainType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BattleSkill> getHeroSkillLv1() {
		List<BattleSkill> list = new ArrayList<BattleSkill>();
		Set<Entry<Integer, BattleSkill>> set = getContent().entrySet();
		for (Entry<Integer, BattleSkill> entry : set) {
			if (entry.getValue().canStudy() && entry.getValue().getLevel() == 1)
				list.add(entry.getValue());
		}
		return list;
	}

	/**
	 * 取下一级技能
	 * 
	 * @param skill
	 * @return 如果找不到返回null（也说明是最高等级了）
	 */
	@SuppressWarnings("unchecked")
	public BattleSkill getNextLevel(BattleSkill skill) {
		if (null != skill) {
			Set<Entry<Integer, BattleSkill>> set = getContent().entrySet();
			for (Entry<Integer, BattleSkill> entry : set) {
				if (entry.getValue().getType() == skill.getType()
						&& entry.getValue().getLevel() == skill.getLevel() + 1)
					return entry.getValue();

			}
		}
		return null;
	}

	public ArrayList<String> getIcons() {
		Iterator iter = getContent().entrySet().iterator();
		ArrayList<String> list = new ArrayList<String>();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			BattleSkill val = (BattleSkill) entry.getValue();
			list.add(val.getIcon());
		}
		return list;
	}

	public Drawable getSkillDrawable(int val, boolean isSmall) {
		String name = "";
		Drawable d = null;
		try {
			BattleSkill bs = (BattleSkill) CacheMgr.battleSkillCache.get(val);
			name = bs.getIcon();
			{
				d = Config.getController().getDrawable(name, 45, 45);// ,
			}
		} catch (GameException e) {
			Log.e("BattleSkillCache", "Image " + name + " not find", e);
		}

		if (d == null) {
			new NullImgCallBack(name);
			d = Config.getController().getDrawable("skill_small", 45, 45);// ,
		}
		return d;
	}

	public String getSkillName(int val) {
		String name = "";
		try {
			BattleSkill bs = (BattleSkill) CacheMgr.battleSkillCache.get(val);
			name = bs.getName();
		} catch (GameException e) {
			Log.e("BattleSkillCache", "BattleSkill " + val + " not find", e);
		}

		return name;
	}

	public String getSkillDesc(int skillId) {
		try {
			BattleSkill bSkill = (BattleSkill) CacheMgr.battleSkillCache
					.get(skillId);
			return bSkill.getEffectDesc();
		} catch (GameException e) {
			Log.e("BattleSkillCache", "BattleSkill " + skillId + " not find", e);
		}
		return "";
	}

	// 获得技能动作类型
	public byte getSkillAction(int val) {
		byte action = 0;
		try {
			BattleSkill bs = (BattleSkill) CacheMgr.battleSkillCache.get(val);
			action = bs.getSkillAction();
		} catch (GameException e) {
			Log.e("BattleSkillCache", "BattleSkill " + val + " not find", e);
		}
		return action;
	}

	// 获得技能特效类型
	public int getAnimType(int val) {
		int animType = 0;
		try {
			BattleSkill bs = (BattleSkill) CacheMgr.battleSkillCache.get(val);
			animType = bs.getAnimType();
		} catch (GameException e) {
			Log.e("BattleSkillCache", "BattleSkill " + val + " not find", e);
		}
		return animType;
	}

	public String getEffectImageName(int val) {
		String effectName = "";
		try {
			BattleSkill bs = (BattleSkill) CacheMgr.battleSkillCache.get(val);
			effectName = bs.getIcon();
		} catch (GameException e) {
			Log.e("BattleSkillCache", "BattleSkill " + val + " not find", e);
		}
		return effectName;
	}
}
