package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 刷将将领初始化表
 * 
 * @author susong
 * 
 */
public class HeroInit {
	private int boxId;// 箱子id
	private int heroId;
	private byte talent;// 天赋品质
	private byte star;// 星级
	private byte level;// 等级
	private int money;// 金币价格
	private int currency;// 元宝价格
	private int attack;// 初始武力
	private int defend;// 初始防护
	private int armProp1;// 统率兵种1(1盾 2骑 3枪 4弓 5法 ）
	private int armProp2;
	private int armProp3;
	private int armProp4;
	private int armProp5;
	private int value1;// 当前统率1(显性上限内)
	private int maxValue1;// 显性上限1
	private int maxHideValue1;// 隐藏上限1
	private int value2;// 当前统率2(显性上限内)
	private int maxValue2;// 显性上限2
	private int maxHideValue2;// 隐藏上限2
	private int value3;// 当前统率3(显性上限内)
	private int maxValue3;// 显性上限3
	private int maxHideValue3;// 隐藏上限3
	private int value4;// 当前统率4(显性上限内)
	private int maxValue4;// 显性上限4
	private int maxHideValue4;// 隐藏上限4
	private int value5;// 当前统率5(显性上限内)
	private int maxValue5;// 显性上限5
	private int maxHideValue5;// 隐藏上限5
	private int staticSkillId;// 固化技能
	private int skill1;// 自由技能槽1
	private int skill2;// 自由技能槽2

	private int openLevel; // 开启打折等级
	private int rate;// 打折系数 折扣 = 100%-(当前等级-开启等级+1)*打折系数
	private int equip1;// 初始【武器】方案号
	private int equip2;// 初始【头饰】方案号
	private int equip3;// 初始【战甲】方案号
	private int equip4;// 初始【饰品】方案号

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public byte getTalent() {
		return talent;
	}

	public void setTalent(byte talent) {
		this.talent = talent;
	}

	public byte getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefend() {
		return defend;
	}

	public void setDefend(int defend) {
		this.defend = defend;
	}

	public int getArmProp1() {
		return armProp1;
	}

	public void setArmProp1(int armProp1) {
		this.armProp1 = armProp1;
	}

	public int getArmProp2() {
		return armProp2;
	}

	public void setArmProp2(int armProp2) {
		this.armProp2 = armProp2;
	}

	public int getArmProp3() {
		return armProp3;
	}

	public void setArmProp3(int armProp3) {
		this.armProp3 = armProp3;
	}

	public int getArmProp4() {
		return armProp4;
	}

	public void setArmProp4(int armProp4) {
		this.armProp4 = armProp4;
	}

	public int getArmProp5() {
		return armProp5;
	}

	public void setArmProp5(int armProp5) {
		this.armProp5 = armProp5;
	}

	public int getValue1() {
		return value1;
	}

	public void setValue1(int value1) {
		this.value1 = value1;
	}

	public int getMaxValue1() {
		return maxValue1;
	}

	public void setMaxValue1(int maxValue1) {
		this.maxValue1 = maxValue1;
	}

	public int getMaxHideValue1() {
		return maxHideValue1;
	}

	public void setMaxHideValue1(int maxHideValue1) {
		this.maxHideValue1 = maxHideValue1;
	}

	public int getValue2() {
		return value2;
	}

	public void setValue2(int value2) {
		this.value2 = value2;
	}

	public int getMaxValue2() {
		return maxValue2;
	}

	public void setMaxValue2(int maxValue2) {
		this.maxValue2 = maxValue2;
	}

	public int getMaxHideValue2() {
		return maxHideValue2;
	}

	public void setMaxHideValue2(int maxHideValue2) {
		this.maxHideValue2 = maxHideValue2;
	}

	public int getValue3() {
		return value3;
	}

	public void setValue3(int value3) {
		this.value3 = value3;
	}

	public int getMaxValue3() {
		return maxValue3;
	}

	public void setMaxValue3(int maxValue3) {
		this.maxValue3 = maxValue3;
	}

	public int getMaxHideValue3() {
		return maxHideValue3;
	}

	public void setMaxHideValue3(int maxHideValue3) {
		this.maxHideValue3 = maxHideValue3;
	}

	public int getValue4() {
		return value4;
	}

	public void setValue4(int value4) {
		this.value4 = value4;
	}

	public int getMaxValue4() {
		return maxValue4;
	}

	public void setMaxValue4(int maxValue4) {
		this.maxValue4 = maxValue4;
	}

	public int getMaxHideValue4() {
		return maxHideValue4;
	}

	public void setMaxHideValue4(int maxHideValue4) {
		this.maxHideValue4 = maxHideValue4;
	}

	public int getValue5() {
		return value5;
	}

	public void setValue5(int value5) {
		this.value5 = value5;
	}

	public int getMaxValue5() {
		return maxValue5;
	}

	public void setMaxValue5(int maxValue5) {
		this.maxValue5 = maxValue5;
	}

	public int getMaxHideValue5() {
		return maxHideValue5;
	}

	public void setMaxHideValue5(int maxHideValue5) {
		this.maxHideValue5 = maxHideValue5;
	}

	public int getStaticSkillId() {
		return staticSkillId;
	}

	public void setStaticSkillId(int staticSkillId) {
		this.staticSkillId = staticSkillId;
	}

	public int getSkill1() {
		return skill1;
	}

	public void setSkill1(int skill1) {
		this.skill1 = skill1;
	}

	public int getSkill2() {
		return skill2;
	}

	public void setSkill2(int skill2) {
		this.skill2 = skill2;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getEquip1() {
		return equip1;
	}

	public void setEquip1(int equip1) {
		this.equip1 = equip1;
	}

	public int getEquip2() {
		return equip2;
	}

	public void setEquip2(int equip2) {
		this.equip2 = equip2;
	}

	public int getEquip3() {
		return equip3;
	}

	public void setEquip3(int equip3) {
		this.equip3 = equip3;
	}

	public int getEquip4() {
		return equip4;
	}

	public void setEquip4(int equip4) {
		this.equip4 = equip4;
	}

	public List<OtherHeroArmPropInfoClient> getArmProps() throws GameException {
		List<OtherHeroArmPropInfoClient> armProps = new ArrayList<OtherHeroArmPropInfoClient>();

		if (isFixedArmProp(getArmProp1())) {
			armProps.add(getOtherHeroArmPropInfoClient(
					getRealArmProp(getArmProp1()), getValue1(), getMaxValue1()));
		}
		if (isFixedArmProp(getArmProp2())) {
			armProps.add(getOtherHeroArmPropInfoClient(
					getRealArmProp(getArmProp2()), getValue2(), getMaxValue2()));
		}
		if (isFixedArmProp(getArmProp3())) {
			armProps.add(getOtherHeroArmPropInfoClient(
					getRealArmProp(getArmProp3()), getValue3(), getMaxValue3()));
		}
		if (isFixedArmProp(getArmProp4())) {
			armProps.add(getOtherHeroArmPropInfoClient(
					getRealArmProp(getArmProp4()), getValue4(), getMaxValue4()));
		}
		if (isFixedArmProp(getArmProp5())) {
			armProps.add(getOtherHeroArmPropInfoClient(
					getRealArmProp(getArmProp5()), getValue5(), getMaxValue5()));
		}
		return armProps;
	}

	private OtherHeroArmPropInfoClient getOtherHeroArmPropInfoClient(int type,
			int value, int maxValue) throws GameException {
		OtherHeroArmPropInfoClient prop = new OtherHeroArmPropInfoClient(type);
		prop.setValue(value);
		prop.setMaxValue(maxValue);
		return prop;
	}

	public List<HeroArmPropClient> getHeroArmPropsEx() throws GameException {
		List<HeroArmPropClient> armProps = new ArrayList<HeroArmPropClient>();

		if (getArmProp1() != 0) {
			int type = 0;
			if (isFixedArmProp(getArmProp1())) {
				type = convertArmProp(getArmProp1());
				HeroArmPropInfoClient prop = new HeroArmPropInfoClient(type);
				prop.setValue(getValue1());
				prop.setMaxValue(getMaxValue1());
				armProps.add(prop);
			} else {
				HeroArmPropClient prop = new HeroArmPropClient();
				HeroTroopName heroTroopName = new HeroTroopName();
				heroTroopName.setType(0);
				heroTroopName.setSlug("?");
				prop.heroTroopName = heroTroopName;
				armProps.add(prop);
			}
		}

		if (getArmProp2() != 0) {
			int type = 0;
			if (isFixedArmProp(getArmProp2())) {
				type = convertArmProp(getArmProp2());

				HeroArmPropInfoClient prop = new HeroArmPropInfoClient(type);
				prop.setValue(getValue2());
				prop.setMaxValue(getMaxValue2());
				armProps.add(prop);
			} else {
				HeroArmPropClient prop = new HeroArmPropClient();
				HeroTroopName heroTroopName = new HeroTroopName();
				heroTroopName.setType(0);
				heroTroopName.setSlug("?");
				prop.heroTroopName = heroTroopName;
				armProps.add(prop);
			}
		}

		if (getArmProp3() != 0) {
			int type = 0;
			if (isFixedArmProp(getArmProp3())) {
				type = convertArmProp(getArmProp3());
				HeroArmPropInfoClient prop = new HeroArmPropInfoClient(type);
				prop.setValue(getValue3());
				prop.setMaxValue(getMaxValue3());
				armProps.add(prop);
			} else {
				HeroArmPropClient prop = new HeroArmPropClient();
				HeroTroopName heroTroopName = new HeroTroopName();
				heroTroopName.setType(0);
				heroTroopName.setSlug("?");
				prop.heroTroopName = heroTroopName;
				armProps.add(prop);
			}
		}
		if (getArmProp4() != 0) {
			int type = 0;
			if (isFixedArmProp(getArmProp4())) {
				type = convertArmProp(getArmProp4());

				HeroArmPropInfoClient prop = new HeroArmPropInfoClient(type);
				prop.setValue(getValue4());
				prop.setMaxValue(getMaxValue4());
				armProps.add(prop);
			} else {

				HeroArmPropClient prop = new HeroArmPropClient();
				HeroTroopName heroTroopName = new HeroTroopName();
				heroTroopName.setType(0);
				heroTroopName.setSlug("?");
				prop.heroTroopName = heroTroopName;
				armProps.add(prop);
			}
		}
		if (getArmProp5() != 0) {
			int type = 0;
			if (isFixedArmProp(getArmProp5())) {
				type = convertArmProp(getArmProp5());

				HeroArmPropInfoClient prop = new HeroArmPropInfoClient(type);
				prop.setValue(getValue5());
				prop.setMaxValue(getMaxValue5());
				armProps.add(prop);
			} else {
				HeroArmPropClient prop = new HeroArmPropClient();
				HeroTroopName heroTroopName = new HeroTroopName();
				heroTroopName.setType(0);
				heroTroopName.setSlug("?");
				prop.heroTroopName = heroTroopName;
				armProps.add(prop);
			}
		}
		return armProps;
	}

	public List<HeroArmPropInfoClient> getHeroArmProps() throws GameException {
		List<HeroArmPropInfoClient> armProps = new ArrayList<HeroArmPropInfoClient>();

		if (isFixedArmProp(getArmProp1())) {
			HeroArmPropInfoClient prop = new HeroArmPropInfoClient(
					getRealArmProp(getArmProp1()));
			prop.setValue(getValue1());
			prop.setMaxValue(getMaxValue1());
			armProps.add(prop);
		}
		if (isFixedArmProp(getArmProp2())) {
			HeroArmPropInfoClient prop = new HeroArmPropInfoClient(
					getRealArmProp(getArmProp2()));
			prop.setValue(getValue2());
			prop.setMaxValue(getMaxValue2());
			armProps.add(prop);
		}
		if (isFixedArmProp(getArmProp3())) {
			HeroArmPropInfoClient prop = new HeroArmPropInfoClient(
					getRealArmProp(getArmProp3()));
			prop.setValue(getValue3());
			prop.setMaxValue(getMaxValue3());
			armProps.add(prop);
		}
		if (isFixedArmProp(getArmProp4())) {
			HeroArmPropInfoClient prop = new HeroArmPropInfoClient(
					getRealArmProp(getArmProp4()));
			prop.setValue(getValue4());
			prop.setMaxValue(getMaxValue4());
			armProps.add(prop);
		}
		if (isFixedArmProp(getArmProp5())) {
			HeroArmPropInfoClient prop = new HeroArmPropInfoClient(
					getRealArmProp(getArmProp5()));
			prop.setValue(getValue5());
			prop.setMaxValue(getMaxValue5());
			armProps.add(prop);
		}
		return armProps;
	}

	public List<EquipmentSlotInfoClient> getEquips() throws GameException {
		List<EquipmentSlotInfoClient> equips = new ArrayList<EquipmentSlotInfoClient>();
		if (equip1 != 0) {
			equips.add(getEquipmentSlotInfoClient(equip1));
		}

		if (equip2 != 0) {
			equips.add(getEquipmentSlotInfoClient(equip2));
		}

		if (equip3 != 0) {
			equips.add(getEquipmentSlotInfoClient(equip3));
		}

		if (equip4 != 0) {
			equips.add(getEquipmentSlotInfoClient(equip4));
		}
		return equips;
	}

	protected EquipmentSlotInfoClient getEquipmentSlotInfoClient(int equipInitId)
			throws GameException {
		EquipmentSlotInfoClient esic = new EquipmentSlotInfoClient();
		EquipmentInfoClient eic = new EquipmentInfoClient(equipInitId);
		esic.setEic(eic);
		esic.setType(eic.getProp().getType());
		return esic;
	}

	public List<HeroSkillSlotInfoClient> getSkills() throws GameException {
		List<HeroSkillSlotInfoClient> skills = new ArrayList<HeroSkillSlotInfoClient>();

		if (0 != getStaticSkillId()) {
			HeroSkillSlotInfoClient skill = new HeroSkillSlotInfoClient(
					getStaticSkillId());
			skill.setStaticSkill(true);
			skill.setId(0);
			skills.add(skill);
		}

		if (0 != getSkill1()) {
			HeroSkillSlotInfoClient skill = new HeroSkillSlotInfoClient(
					getSkill1());
			skill.setStaticSkill(false);
			skill.setId(1);
			skills.add(skill);
		}

		if (0 != getSkill2()) {
			HeroSkillSlotInfoClient skill = new HeroSkillSlotInfoClient(
					getSkill2());
			skill.setStaticSkill(false);
			skill.setId(2);
			skills.add(skill);
		}
		return skills;
	}

	// 配置里面的armprop转成二进制，如果只有1个1，则表明统该（位数+1）的属性，否则为不确定统兵属性
	public static boolean isFixedArmProp(int armProp) {
		String c = "1";
		String string = Integer.toBinaryString(armProp);
		int first = string.indexOf(c);
		int last = string.lastIndexOf(c);
		if (first == last && first != -1)
			return true;
		else
			return false;

	}

	public static byte getRealArmProp(int armProp) {
		String c = "1";
		String string = Integer.toBinaryString(armProp);
		int first = string.indexOf(c);
		int last = string.lastIndexOf(c);
		if (last == first) {
			return (byte) (string.length() - last);
		} else {
			return 0;
		}
	}

	private byte convertArmProp(int armProp) {
		String c = "1";
		String string = Integer.toBinaryString(armProp);
		int first = string.indexOf(c);
		int last = string.lastIndexOf(c);
		if (last == first) {
			return (byte) (string.length());
		} else {
			return 0;
		}
	}

	public static HeroInit fromString(String line) {
		HeroInit hi = new HeroInit();
		StringBuilder buf = new StringBuilder(line);
		hi.setBoxId(StringUtil.removeCsvInt(buf));
		hi.setHeroId(StringUtil.removeCsvInt(buf));
		hi.setTalent(StringUtil.removeCsvByte(buf));
		hi.setStar(StringUtil.removeCsvByte(buf));
		hi.setLevel(StringUtil.removeCsvByte(buf));
		hi.setMoney(StringUtil.removeCsvInt(buf));
		hi.setCurrency(StringUtil.removeCsvInt(buf));
		hi.setAttack(StringUtil.removeCsvInt(buf));
		hi.setDefend(StringUtil.removeCsvInt(buf));
		hi.setArmProp1(StringUtil.removeCsvInt(buf));
		hi.setArmProp2(StringUtil.removeCsvInt(buf));
		hi.setArmProp3(StringUtil.removeCsvInt(buf));
		hi.setArmProp4(StringUtil.removeCsvInt(buf));
		hi.setArmProp5(StringUtil.removeCsvInt(buf));
		hi.setValue1(StringUtil.removeCsvInt(buf));
		hi.setMaxValue1(StringUtil.removeCsvInt(buf));
		hi.setMaxHideValue1(StringUtil.removeCsvInt(buf));
		hi.setValue2(StringUtil.removeCsvInt(buf));
		hi.setMaxValue2(StringUtil.removeCsvInt(buf));
		hi.setMaxHideValue2(StringUtil.removeCsvInt(buf));
		hi.setValue3(StringUtil.removeCsvInt(buf));
		hi.setMaxValue3(StringUtil.removeCsvInt(buf));
		hi.setMaxHideValue3(StringUtil.removeCsvInt(buf));
		hi.setValue4(StringUtil.removeCsvInt(buf));
		hi.setMaxValue4(StringUtil.removeCsvInt(buf));
		hi.setMaxHideValue4(StringUtil.removeCsvInt(buf));
		hi.setValue5(StringUtil.removeCsvInt(buf));
		hi.setMaxValue5(StringUtil.removeCsvInt(buf));
		hi.setMaxHideValue5(StringUtil.removeCsvInt(buf));
		hi.setStaticSkillId(StringUtil.removeCsvInt(buf));
		hi.setSkill1(StringUtil.removeCsvInt(buf));
		hi.setSkill2(StringUtil.removeCsvInt(buf));

		hi.setOpenLevel(StringUtil.removeCsvInt(buf));
		hi.setRate(StringUtil.removeCsvInt(buf));
		return hi;
	}
}
