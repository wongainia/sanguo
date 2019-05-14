package com.vikings.sanguo.battle.anim;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import android.view.animation.Animation;

import com.vikings.sanguo.model.BattleAnimEffects;
import com.vikings.sanguo.model.BattleArrayInfoClient;
import com.vikings.sanguo.model.BattleLogHeroInfoClient;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.Buff;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.BattleEventArmInfo;
import com.vikings.sanguo.protos.BattleEventInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
//把isLeft换成 isDown   便于理解   
public interface ModifyBattleWindow {
	// 清空技能栏
	public void clearSkillColumn();

	// //创建部队栏
	// public void createTroop(List<BattleArrayInfoClient> list,
	// BattleLogHeroInfoClient blhic, boolean isDown, int type);
	// 设置部队信息
	public void setTroopInfo(List<BattleArrayInfoClient> list,
			List<BattleLogHeroInfoClient> blhic, boolean isDown);

	// 设置将领技能
	public void setHeroSkill(List<BattleLogHeroInfoClient> blhic,
			boolean isDown, int type);

	// 设置选中军队
	public void setSelTroop(boolean isDown, int id, boolean isTarget);

	// 修改一支军队数量
	public void modifyTroopAmount(boolean isDown, long value, String name);

	// 修改一支军队HP
	public void modifyTroopHP(boolean isDown, long value, long total);

	// 群伤
	public ArrayList<BaseAnim> groupHurt(boolean isDown,
			List<BattleEventArmInfo> list, List<BattleArrayInfoClient> ls,
			boolean isOwner);

	// 处理buf
	public BaseAnim handleBuf(boolean isDown, Animation anim, int troopId,
			Drawable d);

	// 回合结束
	public void clearTroop(BattleEventInfo info);

	// 显示回合
	public BaseAnim showRound(Animation anim, int round);

	// 入场动画
	// public BaseAnim enterBattleField(boolean isDown, Animation anim, Drawable
	// d);

	// 显示技能buff
	public void setSkillAppear(boolean isDown, Animation anim1,
			Animation anim2, Buff buff, Drawable d);

	// 显示已有buff
	// public BaseAnim setSkill(boolean isDown, Animation anim, Buff buff,
	// Drawable d);

	// 延时动画
	public BaseAnim getDelay(Animation anim);

	// 近程攻击
	public BaseAnim shortRangeAtk(boolean isDown, Animation anim, int dis,
			int atkNum, boolean isBounce);

	// 近程攻击弹回
	public BaseAnim shortRangeAtkBounce(boolean isDown, Animation anim, int dis);

	// 技能消失
	public void setSkillDisappear(boolean isDown, Animation anim, int skillId);

	// 逃跑
	public BaseAnim getEscape(boolean isDown, int troopId, Animation anim);

	// 头顶信息
	public BaseAnim getTopEff(boolean isDown, Animation anim, String str,
			TroopProp tp, BattleArrayInfoClient baic); // , int width
	// 暴击动画

	public BaseAnim getForceAtk(boolean isDown, Animation anim, String str,
			TroopProp tp, BattleArrayInfoClient baic);

	// 帮助
	public BaseAnim getHelp(boolean isDown, Animation anim, Drawable d);

	// 设置是否是我进攻
	public void setMeAtk(boolean isMeAtk);

	// 到达进攻位置
	public BaseAnim reachAtkPos(boolean isDown, Animation anim, Drawable d,
			int dis);
	public List<BaseAnim> reachAtkTarget(boolean isDown, Animation anim, Drawable d,
			int dis);
	
	// 受击动画
	// public BaseAnim getBeaten(boolean isDown, Animation anim, int atkPosX);

	// 清除动画
	public void clearAnimation();

	// 远程攻击
	// public BaseAnim longRangeAtk(boolean isDown, Animation anim, int
	// atkRange);

	// 受击抖动
	public BaseAnim getShake(boolean isDown, Animation anim, boolean isArchor);

	// 受击效果
	// public BaseAnim getHit(boolean isDown, Animation anim, boolean isArchor);
	// //, int atkPosX
	// int
	// atkPosX
	// 技能变大

	// public BaseAnim skillLarge(boolean isDown, Animation anim, Drawable d,
	// boolean isArchor, String name); // , int atkPosX
	// 停顿

	// public BaseAnim stay(Animation anim);

	// 修改部队grid上的数量
	public void modifyTroopGridAmount(boolean isDown, BattleArrayInfoClient baic);

	// 清除血量
	public void clearTroopHP(boolean isDown);

	// 清除数量
	public void clearTroopAmount(boolean isDown);

	// 展示英雄头像
	public BaseAnim moveHeroIcon(Animation anim, boolean isGone,
			boolean isDown, Drawable d, int index, String heroName);

	// 展示技能名称
	public BaseAnim moveSkillName(String name, Animation anim, boolean isGone,
			boolean isDown, Drawable d);

	// 缓慢移动技能名称
	public BaseAnim moveSkillNameSlow(String name, Animation anim,
			boolean isGone);

	// 技能名称出屏
	public BaseAnim moveSkillNameOut(String name, Animation anim, boolean isGone);

	// 展示技能背景
	public BaseAnim moveSkillBg(Animation anim, boolean isGone, int resId,
			boolean isDown);

	// 展示技能图片
	public BaseAnim showSkill(Animation anim, Drawable d, boolean isGone);

	// 展示背景
	public BaseAnim showBg(Animation anim);

	// 旋转闪光
	// public List<BaseAnim> skillLight(Animation anim, boolean isGone, boolean
	// isDown,
	// int index);

	public BaseAnim skillLight(Animation anim, boolean isGone, boolean isDown,
			int index);

	// 清除动画
	public BaseAnim clearSkill(Animation anim);

	public BaseDrawableAnim getBeatAnimationDrawable(boolean isDown);

	public BaseDrawableAnim longRangeAtkDrawable(boolean isDown, int atkRange);

	public ArrayList<Anim> getHit(boolean isDown, Drawable d, boolean isArchor,
			int deadCount, int type, int currentHp, int totalHP,
			BattleEventArmInfo beai, BattleArrayInfoClient baic);

	public ArrayList<Anim> getHitContinus(boolean isDown, Drawable d,
			boolean isArchor, int startCount, int deadCount, int type,
			int currentHp, int totalHP);

	public ArrayList<Anim> startReadyEffects(boolean isDown, Animation anim,
			boolean isArchor, boolean isGone, int atkNum);

	public ArrayList<Anim> magicStartEffects(boolean isDown, boolean isGone,
			int atkNum);

	public int getAtkDis(boolean isDown, Drawable d);

	// 入场动画 armCount 显示总的士兵的数目 armType 士兵类型：骑兵 BOSS 其他
	public Anim enterBattleField(boolean isDown, Animation anim, Drawable d,
			int armCount, byte armType, boolean isBoss);

	// 英雄的花 飞进来
	public BaseAnim skillFling(Animation anim, boolean isGone, boolean isDown);

	public BaseAnim armSkillAppear(boolean isDown, Animation anim);

	public BaseAnim armSkillRotate(boolean isDown, Animation anim);

	public BaseAnim armSkilldisppear(boolean isDown, Animation anim);

	public BaseAnim armSkillImageAppear(boolean isDown, Animation anim,
			Drawable d, String img);

	public BaseAnim armSkillNameAppear(boolean isDown, Animation anim,
			String name);

	public BaseAnim armSkillClear(boolean isDown, Animation anim);

	public BaseAnim modifyHpNum(Animation anim, boolean isDown,
			BattleArrayInfoClient baic, String name, boolean isBoss);

	public BaseAnim stay(boolean isDown, Animation anim);

	public BaseAnim stayHp(boolean isDown, Animation anim);

	public BaseAnim delay(boolean isDown, Animation anim);

	// 特效动画处理
	public BaseDrawableAnim effectDrawableAnim(boolean isDown,
			BattleAnimEffects animEffects);

	public BaseDrawableAnim heroEffectAnim(boolean isDown, boolean isDefend,
			boolean isGain);

	public BaseAnim getAnimLight(boolean isDown, Animation anim, int index);

	public BaseAnim getLightBlink(boolean isDown, Animation anim);

	// 设置站位
	public void setAmyOrder(List<BattleArrayInfoClient> list,
			final List<BattleLogHeroInfoClient> blhic, boolean isDown,
			final BattleSkill fiefBattleSkill,
			UserTroopEffectInfo userTroopEffectInfo);
}
