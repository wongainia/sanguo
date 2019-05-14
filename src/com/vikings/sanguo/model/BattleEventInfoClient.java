/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-11 上午11:41:40
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BattleEvent;
import com.vikings.sanguo.protos.BattleEventArmInfo;
import com.vikings.sanguo.protos.BattleEventInfo;
import com.vikings.sanguo.utils.StringUtil;

public class BattleEventInfoClient implements Cloneable,Serializable
{
	private BattleEventInfo info;
	private String heroName = "";

	public BattleEventInfoClient(BattleEventInfo info)
	{
		this.info = info;
	}

	public String getAttackerName(boolean isMyBattle, boolean isMeAttacker)
	{
		return getAttackSide(isMyBattle, isMeAttacker)
				+ getTroopName(getActiveId());
	}

	public void setHeroName(String heroName)
	{
		//设置英雄名字 为组合技能处理 
		this.heroName = heroName;
	}
	
	// 获取英雄名字
	public String getAttackerHeroName(boolean isMyBattle, boolean isMeAttacker)
	{
		//判断是不是组合技能   	
		return getAttackSide(isMyBattle, isMeAttacker) + "英雄" + heroName;
//		return getAttackSide(isMyBattle, isMeAttacker) + "英雄" + "["
//				+ getHeroName(getActiveId()) + "]";
	}

	public String getAttackerName(boolean isMyBattle, boolean isMeAttacker,
			int armId)
	{
		return getAttackSide(isMyBattle, isMeAttacker) + getTroopName(armId);
	}

	public String getDefenderName(boolean isMyBattle, boolean isMeAttacker)
	{
		return getDefendSide(isMyBattle, isMeAttacker)
				+ getTroopName(getPassiveId());
	}

	public String getDefenderName(boolean isMyBattle, boolean isMeAttacker,
			int armId)
	{
		return getDefendSide(isMyBattle, isMeAttacker) + getTroopName(armId);
	}

	public String getAttackSide(boolean isMyBattle, boolean isMeAttacker)
	{
		String activeName;
		if (isMyBattle)
		{
			if (isMeAttacker)
				activeName = info.getAttack() ? "我军" : "敌军";
			else
				activeName = info.getAttack() ? "敌军" : "我军";
		} else
			activeName = info.getAttack() ? "攻方" : "守方";

		return activeName;
	}

	public String getDefendSide(boolean isMyBattle, boolean isMeAttacker)
	{
		String passiveName;
		if (isMyBattle)
		{
			if (isMeAttacker)
				passiveName = info.getAttack() ? "敌军" : "我军";
			else
				passiveName = info.getAttack() ? "我军" : "敌军";
		} else
			passiveName = info.getAttack() ? "守方" : "攻方";

		return passiveName;
	}

	public String getTroopName(int val)
	{
		TroopProp troop = null;
		try
		{
			if (0 != val)
			{
				troop = (TroopProp) CacheMgr.troopPropCache.get(val);
			}
		} catch (GameException e)
		{
			e.printStackTrace();
		}

		if (null != troop)
			return ("[" + troop.getName() + "]");
		else
			return "[" + val + "]";
	}

	public String getHeroName(int val)
	{
		HeroProp hero = null;
		try
		{
			if (0 != val)
			{
				hero = (HeroProp) CacheMgr.heroPropCache.get(val);
			}
		} catch (GameException e)
		{
			e.printStackTrace();
		}

		if (null != hero)
			return hero.getName();
		else
			return "" + val;
	}

	
	
	
	public int getEvent()
	{
		return info.getEvent();
	}

	public int getValue()
	{
		return info.getValue();
	}

	public String getIcon()
	{
		String img = "";
		// 英雄技能 或者士兵技能
		try
		{
			if (getActive().getEx() == 2 || getValue() == 1)
			{
				BattleSkill bs = null;
				bs = (BattleSkill) CacheMgr.battleSkillCache.get(getActive().getValue().intValue()); // info.getValue()
				if (bs != null)
				{
					img = bs.getIcon();
				}
			} 
			else
			{
				BattleBuff bf = null;
				bf = (BattleBuff) CacheMgr.battleBuffCache.get(getActive().getValue().intValue()); // info.getValue()
				if (bf != null)
				{
					img = bf.getIcon();
				}
			}
		} 
		catch (GameException e)
		{
			e.printStackTrace();
		}
		return img;
	}
	
	public String getSkillName()
	{
		String name = "";
		// 英雄技能 或者士兵技能
		try
		{
			if (getActive().getEx() == 2 || getValue() == 1)
			{
				BattleSkill bs = null;
				bs = (BattleSkill) CacheMgr.battleSkillCache.get(getActive()
						.getValue().intValue()); // info.getValue()
				if (bs != null)
				{
					name = bs.getName();
				}
			} else
			{
				BattleBuff bf = null;
				bf = (BattleBuff) CacheMgr.battleBuffCache.get(getActive()
						.getValue().intValue()); // info.getValue()
				if (bf != null)
				{
					name = bf.getName();
				}
			}
		} catch (GameException e)
		{
			e.printStackTrace();
		}
		return name;
	}

	public String getSkillEffect()
	{
		BattleSkill bs = null;
		try
		{
			bs = (BattleSkill) CacheMgr.battleSkillCache.get(getActive()
					.getValue().intValue()); // info.getValue()
		} catch (GameException e)
		{
			e.printStackTrace();
		}

		if (null != bs)
			return bs.getEffects();

		return "";
	}

	public String getBuffName()
	{
		BattleBuff bs = null;
		try
		{
			bs = (BattleBuff) CacheMgr.battleBuffCache.get(getActive()
					.getValue().intValue()); // info.getValue()
		} catch (GameException e)
		{
			e.printStackTrace();
		}

		if (null != bs)
			return bs.getName();

		return "";
	}

	public int getSkillAnimType()
	{
		BattleSkill bs = null;
		try
		{
			bs = (BattleSkill) CacheMgr.battleSkillCache.get(getActive()
					.getValue().intValue());
		} catch (GameException e)
		{
			e.printStackTrace();
		}

		if (null != bs)
			return bs.getAnimType();

		return 0;
	}

	public String setBuffDesc()
	{
		StringBuilder buf = new StringBuilder();

		try
		{
			for (BattleEventArmInfo it : getActives())
			{
				BattleBuff bb = (BattleBuff) CacheMgr.battleBuffCache.get(it
						.getValue().intValue());
				buf.append("[").append(bb.getName()).append("]效果影响,")
						.append(bb.getEffect() + ",");
			}
		} catch (GameException e)
		{
			e.printStackTrace();
		}

		if (buf.length() > 1)
			buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	public String clearBuffDesc()
	{
		StringBuilder buf = new StringBuilder();

		try
		{
			for (BattleEventArmInfo it : getActives())
			{
				BattleBuff bb = (BattleBuff) CacheMgr.battleBuffCache.get(it
						.getValue().intValue());
				buf.append("[").append(bb.getName()).append("],");
			}
		} catch (GameException e)
		{
			e.printStackTrace();
		}

		if (buf.length() > 1)
			buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	public boolean hasActives()
	{
		return info.hasActives(); // && info.getActivesList().size() > 1;
	}

	public boolean hasPassive()
	{
		return info.hasPassives(); // && info.getPassivesList().size() == 1;
	}

	public List<BattleEventArmInfo> getActives()
	{
		return info.getActivesList();
	}

	public List<BattleEventArmInfo> getPassives()
	{
		return info.getPassivesList();
	}

	public int getActiveId()
	{
		if (info.hasActives())
			return info.getActives(0).getArmid();
		return 0;
	}

	public BattleEventArmInfo getActive()
	{
		if (info.hasActives())
			return info.getActives(0);
		return null;
	}

	public BattleEventArmInfo getPassive()
	{
		if (info.hasPassives())
			return info.getPassives(0);
		return null;
	}

	public int getPassiveId()
	{
		if (info.hasPassives())
			return info.getPassives(0).getArmid();
		return 0;
	}

	// 判断是否下方开始行动
	public boolean isDownAct(boolean isMyBattle, boolean isMeAttacker)
	{
		boolean isDown;
		if (isMyBattle)
		{
			if (info.getAttack())
				isDown = isMeAttacker ? true : false;
			else
				isDown = isMeAttacker ? false : true;
		} else
			isDown = info.getAttack() ? true : false;

		return isDown;
	}

	public String createLog(boolean isMyBattle, boolean isMeAttacker,
			boolean isNewLog, int round)
	{
		StringBuilder builder = new StringBuilder();

		// 获取双方兵种信息
		String activeName = getAttackerName(isMyBattle, isMeAttacker);
		//String passiveName = getDefenderName(isMyBattle, isMeAttacker);
		String heroName = getAttackerHeroName(isMyBattle, isMeAttacker);

		switch (BattleEvent.valueOf(info.getEvent()))
		{
		case BATTLE_EVENT_ACT: // 行动者
			builder.append(StringUtil.color("第" + round + "回合:<br>",
					R.color.k7_color4));
			builder.append(activeName);
			builder.append("开始行动");
			break;

		case BATTLE_EVENT_FIGHT_BACK: // 还击
			builder.append(activeName);
			builder.append("进行反击");
			break;

		case BATTLE_EVENT_SKILL: // 触发技能
			String skillName = getSkillName();

			if (getValue() == 1)
			{
				builder.append(heroName);
				builder.append("触发技能[");
				builder.append(skillName + "]");
			} else
			{
				builder.append(activeName);
				if (getActive().getEx() == 2)
				{
					builder.append("触发技能[");
				} else
				{
					builder.append("释放buff[");
				}
				builder.append(skillName + "]");
			}
			break;

		case BATTLE_EVENT_DEATH: // 死亡
			builder.append(activeName);
			builder.append("发动攻击<br>");

			// 2013.4.7 与王磊确定，只用解析passives即可
			if (info.hasPassives())
			{
				List<BattleEventArmInfo> ls = getPassives();
				for (BattleEventArmInfo it : ls)
				{
					if (0 != it.getValue()
							&& 0 != it.getArmid()
							&& (!CacheMgr.troopPropCache.needShowHP(it
									.getArmid())))
					{
						builder.append(getDefendSide(isMyBattle, isMeAttacker));
						builder.append(getTroopName(it.getArmid()));
						builder.append("损失[" + it.getValue() + "]兵力");

						if (1 == it.getEx())
							builder.append(StringUtil.color("(暴击)",
									R.color.k7_color8));

						builder.append("<br>");
					}
				}

				// 去掉最后一个<br>
				if (builder.length() >= 4)
					builder.delete(builder.length() - 4, builder.length());
			}
			break;

		case BATTLE_EVENT_WIN: // 胜利
			builder.append(getAttackSide(isMyBattle, isMeAttacker));
			builder.append("胜利");
			break;

		case BATTLE_EVENT_RUN_AWAY: // 逃跑
			builder.append(activeName);
			builder.append("士气大乱,想逃离战场");
			break;

		// case BATTLE_EVENT_BEHEADED: // 斩杀
		// builder.append(activeName);
		// builder.append("乘胜追击,对");
		// builder.append(passiveName);
		// builder.append("发动斩杀");
		// break;

		case BATTLE_EVENT_DEMAGE:
			if (info.hasPassives())
			{
				for (BattleEventArmInfo it : getPassives())
				{
					builder.append(getDefenderName(isMyBattle, isMeAttacker,
							it.getArmid()));
					builder.append("受到" + it.getValue() + "点伤害"); // info.getValue()
					builder.append("<br>");
				}
			}

			// 去掉最后一个<br>
			if (builder.length() >= 4)
				builder.delete(builder.length() - 4, builder.length());
			// }
			break;

		case BATTLE_EVENT_FALL_HP:
			if (info.hasActives())
			{
				for (BattleEventArmInfo it : getActives())
				{
					if (CacheMgr.troopPropCache.needShowHP(it.getArmid()))
					{
						builder.append(getAttackerName(isMyBattle,
								isMeAttacker, it.getArmid()));
						builder.append("受到" + getActive().getValue() + "点伤害"); // info.getValue()

						builder.append("<br>");
					}
				}

				// 去掉最后一个<br>
				if (builder.length() >= 4)
					builder.delete(builder.length() - 4, builder.length());
			}
			break;
		case BATTLE_EVENT_BOUT: // 回合结束
			if (0 != getActiveId())
			{
				builder.append(activeName);
				if (0 == getActive().getValue()) // info.getValue()
					builder.append("全灭!");
				else
					builder.append("逃走了!");
			}
			break;

		case BATTLE_EVENT_BUFF_SET: // 获得技能
			if (info.hasActives())
			{
				for (BattleEventArmInfo it : getActives())
				{
					try
					{
						BattleBuff bb = (BattleBuff) CacheMgr.battleBuffCache
								.get(it.getValue());
						builder.append(
								getAttackerName(isMyBattle, isMeAttacker,
										it.getArmid())).append("受到[")
								.append(bb.getName()).append("]效果影响,")
								.append(bb.getEffect()).append("<br>");
					} catch (GameException e)
					{
						e.printStackTrace();
					}
				}

				// 去掉最后一个<br>
				if (builder.length() >= 4)
					builder.delete(builder.length() - 4, builder.length());
			}
			break;

		case BATTLE_EVENT_BUFF_CLEAR: // 清除效果
			if (info.hasActives())
			{
				for (BattleEventArmInfo it : getActives())
				{
					try
					{
						BattleBuff bb = (BattleBuff) CacheMgr.battleBuffCache
								.get(it.getValue());
						builder.append(
								getAttackerName(isMyBattle, isMeAttacker,
										it.getArmid())).append("的[")
								.append(bb.getName()).append("]效果消除了")
								.append("<br>");
					} catch (GameException e)
					{
						e.printStackTrace();
					}
				}

				// 去掉最后一个<br>
				if (builder.length() >= 4)
					builder.delete(builder.length() - 4, builder.length());
			}
			break;

		case BATTLE_EVENT_MODIFY_HP:
			builder.append(activeName);
			List<BattleEventArmInfo> ls = getActives();
			BattleEventArmInfo beai = ls.get(0);
			if (beai.getValue() > 0)
			{
				builder.append("恢复" + beai.getValue() + "点血量,恢复兵力"
						+ beai.getEx());
			} else
			{
				builder.append("受到" + Math.abs(beai.getValue()) + "点伤害,损失兵力"
						+ Math.abs(beai.getEx()));
			}
			// if (CacheMgr.troopPropCache.needShowHP(getActiveId()))
			// builder.append("恢复了" + beai.getValue() + "的血量,兵力恢复为" +
			// beai.getEx());
			// else
			// builder.append("兵力恢复为" + beai.getEx());
			break;
		default:
			break;
		}

		if (builder.toString().length() > 0)
			return builder.toString();
		return null;
	}

	// 加2个参数 伤害、数量
	public String createLogEx(boolean isMyBattle, boolean isMeAttacker,
			boolean isNewLog, int round, List<BattleEventArmInfo> data,List<Integer> forceInfo)
	{
		StringBuilder builder = new StringBuilder();

		// 获取双方兵种信息
		String activeName = getAttackerName(isMyBattle, isMeAttacker);
		//String passiveName = getDefenderName(isMyBattle, isMeAttacker);

		switch (BattleEvent.valueOf(info.getEvent()))
		{
		case BATTLE_EVENT_ACT: // 行动者
			builder.append(StringUtil.color("第" + round + "回合:<br>",
					R.color.k7_color4));
			builder.append(activeName);
			builder.append("开始行动");
			break;

		case BATTLE_EVENT_FIGHT_BACK: // 还击
			builder.append(activeName);
			builder.append("进行反击");
			break;

		case BATTLE_EVENT_SKILL: // 触发技能
			String skillName = getSkillName();

			if (getValue() == 1)
			{
				String heroName = getAttackerHeroName(isMyBattle, isMeAttacker);
				builder.append(heroName);
				builder.append("触发技能[");
				builder.append(skillName + "]");
			} else
			{
				builder.append(activeName);
				if (getActive().getEx() == 2)
				{
					builder.append("触发技能[");
				} else
				{
					builder.append("释放buff[");
				}
				builder.append(skillName + "]");
			}
			break;

		case BATTLE_EVENT_WIN: // 胜利
			builder.append(getAttackSide(isMyBattle, isMeAttacker));
			builder.append("胜利");
			break;

		case BATTLE_EVENT_RUN_AWAY: // 逃跑
			builder.append(activeName);
			builder.append("士气大乱,想逃离战场");
			break;

		case BATTLE_EVENT_DEMAGE:
		{
			if (data == null || data.size() == 0)
			{
				return null;
			}
			// builder.append(activeName);
			// builder.append("发动攻击<br>");
			if (hasPassive())
			{
				List<BattleEventArmInfo> passiveInfo = getPassives();
				for (int i = 0; i < data.size(); i++)
				{
					BattleEventArmInfo it = data.get(i);

					builder.append(getDefenderName(isMyBattle, isMeAttacker,
							it.getArmid()));
					TroopProp tp = null;
					try
					{
						tp = (TroopProp) CacheMgr.troopPropCache.get(it.getArmid());
					} 
					catch (GameException e)
					{
						e.printStackTrace();
					}
					if(tp != null && tp.isBoss())
					{
						builder.append("受到" + it.getValue() + "点伤害  "); // info.getValue()
						builder.append(getDefenderName(isMyBattle, isMeAttacker,it.getArmid()));
						builder.append("损失生命值" + it.getEx()); // info.getValue()
					}
					else
					{
						builder.append("受到" + it.getValue() + "点伤害  "); // info.getValue()
						builder.append(getDefenderName(isMyBattle, isMeAttacker,
								it.getArmid()));
						builder.append("损失兵力" + it.getEx()); // info.getValue()
					}
					if(forceInfo.size() > i)
					{
						
						if (2 == forceInfo.get(i))
							builder.append(StringUtil.color("(暴击)",
								R.color.k7_color8));
					}
					
					builder.append("<br>");
				}
				// 去掉最后一个<br>
				if (builder.length() >= 4)
					builder.delete(builder.length() - 4, builder.length());
			}
		}
			break;

		case BATTLE_EVENT_BOUT: // 回合结束
			if (0 != getActiveId())
			{
				builder.append(activeName);
				if (0 == getActive().getValue()) // info.getValue()
					builder.append("全灭!");
				else
					builder.append("逃走了!");
			}
			break;

		case BATTLE_EVENT_BUFF_SET: // 获得技能
			if (info.hasActives())
			{
				for (BattleEventArmInfo it : getActives())
				{
					try
					{
						BattleBuff bb = (BattleBuff) CacheMgr.battleBuffCache
								.get(it.getValue().intValue());
						builder.append(
								getAttackerName(isMyBattle, isMeAttacker,
										it.getArmid())).append("受到[")
								.append(bb.getName()).append("]效果影响,")
								.append(bb.getEffect()).append("<br>");
					} catch (GameException e)
					{
						e.printStackTrace();
					}
				}

				// 去掉最后一个<br>
				if (builder.length() >= 4)
					builder.delete(builder.length() - 4, builder.length());
			}
			break;

		case BATTLE_EVENT_BUFF_CLEAR: // 清除效果
			if (info.hasActives())
			{
				for (BattleEventArmInfo it : getActives())
				{
					try
					{
						BattleBuff bb = (BattleBuff) CacheMgr.battleBuffCache
								.get(it.getValue().intValue());
						builder.append(
								getAttackerName(isMyBattle, isMeAttacker,
										it.getArmid())).append("的[")
								.append(bb.getName()).append("]效果消除了")
								.append("<br>");
					} catch (GameException e)
					{
						e.printStackTrace();
					}
				}

				// 去掉最后一个<br>
				if (builder.length() >= 4)
					builder.delete(builder.length() - 4, builder.length());
			}
			break;

		case BATTLE_EVENT_MODIFY_HP:
			builder.append(activeName);
			List<BattleEventArmInfo> ls = getActives();
			BattleEventArmInfo beai = ls.get(0);
			if (beai.getValue() > 0)
			{
				builder.append("恢复" + beai.getValue() + "点血量,恢复兵力"
						+ beai.getEx());
			} else
			{
				builder.append("受到" + Math.abs(beai.getValue()) + "点伤害,损失兵力"
						+ Math.abs(beai.getEx()));
			}
			// if (CacheMgr.troopPropCache.needShowHP(getActiveId()))
			// builder.append("恢复了" + beai.getValue() + "的血量,兵力恢复为" +
			// beai.getEx());
			// else
			// builder.append("兵力恢复为" + beai.getEx());
			break;
		default:
			break;
		}

		if (builder.toString().length() > 0)
			return builder.toString();
		return null;
	}

	public boolean hasSkillIcon()
	{
		return (BattleEvent.BATTLE_EVENT_SKILL == BattleEvent.valueOf(info
				.getEvent()));
	}

	public boolean hasDemage()
	{
		return (BattleEvent.BATTLE_EVENT_DEMAGE == BattleEvent.valueOf(info
				.getEvent()));
	}

	public boolean hasBuffIcon()
	{
		return (BattleEvent.BATTLE_EVENT_BUFF_SET == BattleEvent.valueOf(info
				.getEvent()) || BattleEvent.BATTLE_EVENT_BUFF_CLEAR == BattleEvent
				.valueOf(info.getEvent()));
	}

	public boolean getAttack()
	{
		return info.getAttack();
	}

	public String getSkillIconById() throws GameException
	{
		String icon = "";
		icon = ((BattleSkill) CacheMgr.battleSkillCache.get(getActive()
				.getValue().intValue())).getIcon(); //
		return icon;
	}

	public String getBuffIconById() throws GameException
	{
		String icon = "";
		icon = ((BattleBuff) CacheMgr.battleBuffCache.get(getActive()
				.getValue().intValue())).getIcon(); //
		return icon;
	}

	public String getEffectsById() throws GameException
	{
		String icon = "";
		icon = ((BattleSkill) CacheMgr.battleSkillCache.get(getActive()
				.getValue().intValue())).getEffects(); //
		return icon;
	}

	public BattleEventInfo getInfo()
	{
		return info;
	}

	public int getSn()
	{
		return info.getSn();
	}

	public boolean isNewRound()
	{
		return BattleEvent.valueOf(info.getEvent()) == BattleEvent.BATTLE_EVENT_ACT;
	}

	public String getEventType()
	{
		switch (info.getEvent())
		{
		case 1:
			return "BATTLE_EVENT_ACT(1)";
		case 2:
			return "BATTLE_EVENT_TARGET(2)";
		case 3:
			return "BATTLE_EVENT_FIGHT_BACK(3)";
		case 4:
			return "BATTLE_EVENT_DEMAGE(4)";
		case 5:
			return "BATTLE_EVENT_SKILL(5)";
		case 6:
			return "BATTLE_EVENT_DEATH(6)";
		case 7:
			return "BATTLE_EVENT_WIN(7)";
		case 8:
			return "BATTLE_EVENT_RUN_AWAY(8)";
		case 9:
			return "BATTLE_EVENT_BEHEADED(9)";
		case 10:
			return "BATTLE_EVENT_BYPASS(10)";
		case 11:
			return "BATTLE_EVENT_ROUND(11)";
		case 12:
			return "BATTLE_EVENT_NUM(12)";
		case 13:
			return "BATTLE_EVENT_MORALE(13)";
		case 14:
			return "BATTLE_EVENT_FALL_HP(14)";
		case 15:
			return "BATTLE_EVENT_BOUT(15)";
		case 16:
			return "BATTLE_EVENT_BUFF_SET(16)";
		case 17:
			return "BATTLE_EVENT_BUFF_CLEAR(17)";
		case 18:
			return "BATTLE_EVENT_MODIFY_HP(18)";

		default:
			return "";
		}
	}

	public String tosString()
	{
		if (info != null)
		{
			return info.toString();
		}
		return "";
	}

	public Object clone()
	{
		Object o = null;
		try
		{
			o = (BattleEventInfoClient) super.clone();// Object中的clone()识别出你要复制
			// 的是哪一个对象。
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return o;
	}
	
	public BattleEventInfoClient deepClone() {
		BattleEventInfoClient  dc = null;
		    try {
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  ObjectOutputStream oos = new ObjectOutputStream(baos);
		  oos.writeObject(this);
		  oos.close();
		 
		  ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		  ObjectInputStream bis = new ObjectInputStream(bais);
		  dc = (BattleEventInfoClient)bis.readObject();
		 } 
		 catch (IOException e) 
		 {
		  e.printStackTrace();
		 } catch (ClassNotFoundException e) {
		  e.printStackTrace();
		 }
		    return dc;
		}	
}
