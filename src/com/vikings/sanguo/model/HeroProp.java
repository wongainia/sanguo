package com.vikings.sanguo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

//将领属性表
public class HeroProp implements Serializable {
	private static final long serialVersionUID = -7615044451275122510L;

	public static final byte HERO_RATING_DA_JIANG = 1;
	public static final byte HERO_RATING_MING_JIANG = 2;
	public static final byte HERO_RATING_WU_SHUANG = 3;
	public static final byte HERO_RATING_TIAN_XUAN = 4;
	private int id;
	private String name;
	private byte country;// 国籍(1魏 2蜀 3吴 4群雄)
	private String desc; // 将领描述
	private String icon; // 头像图片
	private String img;// 高清大图
	private int food;// 粮草消耗
	private String armPropDesc; // 擅长兵种
	private int abadonItemId; // 分解材料
	private int staticSkillId; // 固化技能
	private byte maxTalent;// 最高品质（1大将 2名将 3无双 4天选 5传奇）
	private short sequence;// 将领图鉴排序字段
	private byte illustrations;// 图鉴是否显示（1显示、0不显示）
	private byte strongestTag;// 最强标签(头像显示一个特殊标签，1显示)
	private byte rating;// 将领评价(1大将 2名将 3无双 4天选)
	private int favourSchema;// 宠幸方案(favour_skill)

	private String sourceSpec;// 来源说明
	private int attack; // 武力上限
	private int defend; // 防御上限

	private String skilled1; // 统率1上限 格式（兵种|统率值）
	private String skilled2;// 统率2上限
	private String skilled3;// 统率3上限
	private String skilled4;// 统率4上限
	private String skilled5;// 统率5上限

	// 统率上限对应的兵种 和 统率值 heroTroopNames 和 skillVal一 一 对应
	public List<HeroTroopName> heroTroopNames;
	public List<Integer> skillVal;

	private String fitSkill;// 合体技 格式：33|33|783....
	private byte noCloth;// 是否脱衣（0否，1是）

	// 高清大图界面需要通过此方法初始化值
	public void initValue() {
		initTroopInfo();
	}

	public List<SkillCombo> getSkillCombos() {
		List<SkillCombo> skillCombos = new ArrayList<SkillCombo>();
		if (fitSkill.length() <= 2)
			return null;
		List<SkillCombo> skillCbs = new ArrayList<SkillCombo>();

		if (fitSkill.indexOf('|') != -1) {
			String[] fitSkills = fitSkill.split("\\|");

			for (String fitSkillItem : fitSkills) {
				skillCbs.addAll(CacheMgr.battleSkillCombo.getComboHeros(Integer
						.valueOf(fitSkillItem)));
			}
		} else {
			skillCbs.addAll(CacheMgr.battleSkillCombo.getComboHeros(Integer
					.valueOf(fitSkill)));
		}
		if (!ListUtil.isNull(skillCbs)) {
			for (SkillCombo skillCombo : skillCbs) {
				if (getId() == 0)
					continue;
				if (skillCombo.getHero1Id() == getId()) {
					skillCombos.add(skillCombo);
					continue;
				}
				if (skillCombo.getHero2Id() == getId()) {
					skillCombos.add(skillCombo);
					continue;
				}

				if (skillCombo.getHero3Id() == getId()) {
					skillCombos.add(skillCombo);
					continue;
				}
			}
		}
		List<SkillCombo> sc = new ArrayList<SkillCombo>();
		sc.addAll(skillCombos);

		// 移除组合技 重复的 组合技
		for (SkillCombo skillCombo : skillCombos) {
			for (SkillCombo skillCombo2 : skillCombos) {
				if (skillCombo.getId() == skillCombo2.getId()) {
					if (skillCombo.getKey() > skillCombo2.getKey()) {
						sc.remove(skillCombo);
					}
				}
			}
		}
		return sc;

	}

	// 得到每个组合描述：格式 hero1+hero2+hero3
	public String getComboHeroNamesBySkillCombo(SkillCombo sk) {
		StringBuffer comboHeroName = new StringBuffer();
		if (sk.getHero1Id() != 0) {
			comboHeroName.append(CacheMgr.heroPropCache.getHeroNameByHeroId(sk
					.getHero1Id()));
		}
		if (sk.getHero2Id() != 0) {
			comboHeroName.append("+");
			comboHeroName.append(CacheMgr.heroPropCache.getHeroNameByHeroId(sk
					.getHero2Id()));
		}
		if (sk.getHero3Id() != 0) {
			comboHeroName.append("+");
			comboHeroName.append(CacheMgr.heroPropCache.getHeroNameByHeroId(sk
					.getHero3Id()));
		}
		comboHeroName.append(":");

		return comboHeroName.toString();

	}

	public String getComboSkillDesc(SkillCombo sk) {
		// 组合技能描述
		return CacheMgr.battleSkillCache.getSkillDesc(sk.getId());
	}

	private void initTroopInfo() {
		heroTroopNames = new ArrayList<HeroTroopName>();
		skillVal = new ArrayList<Integer>();
		skillParse(skilled1);
		skillParse(skilled2);
		skillParse(skilled3);
		skillParse(skilled4);
		skillParse(skilled5);

	}

	private void skillParse(String skilled) {
		// skillid 格式：xx|xx 随机兵种格式0|xx
		try {
			if (skilled.indexOf('|') != -1) {
				String[] fitSkills = skilled.split("\\|");
				// HeroTroopName 不用做位运算 策划直接配置了类型值故 直接查表格即可
				int heroTroopType = Integer.valueOf(fitSkills[0]);
				if (heroTroopType == 0) {
					// 随机兵种 用？号代替
					heroTroopNames.add(new HeroTroopName().getThis().setSlug(
							"?"));
				} else {
					heroTroopNames
							.add((HeroTroopName) CacheMgr.heroTroopNameCache
									.get(heroTroopType));
				}

				skillVal.add(Integer.valueOf(fitSkills[1]));
			}
		} catch (NumberFormatException e) {
			Log.e("heroprop", "兵种id转换出错！" + skilled + "    " + e);

		} catch (GameException e) {
			Log.e("heroprop", "HeroProp中找不到对应的英雄ID！" + skilled + e);
		}

	}

	public String getSkilled1() {
		return skilled1;
	}

	public void setSkilled1(String skilled1) {
		this.skilled1 = skilled1;
	}

	public String getSkilled2() {
		return skilled2;
	}

	public void setSkilled2(String skilled2) {
		this.skilled2 = skilled2;
	}

	public String getSkilled3() {
		return skilled3;
	}

	public void setSkilled3(String skilled3) {
		this.skilled3 = skilled3;
	}

	public String getSkilled4() {
		return skilled4;
	}

	public void setSkilled4(String skilled4) {
		this.skilled4 = skilled4;
	}

	public String getSkilled5() {
		return skilled5;
	}

	public void setSkilled5(String skilled5) {
		this.skilled5 = skilled5;
	}

	public String getSourceSpec() {
		return sourceSpec;
	}

	public void setSourceSpec(String sourceSpec) {
		this.sourceSpec = sourceSpec;
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

	public String getFitSkill() {
		return fitSkill;
	}

	public void setFitSkill(String fitSkill) {
		this.fitSkill = fitSkill;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public String getArmPropDesc() {
		return armPropDesc;
	}

	public void setArmPropDesc(String armPropDesc) {
		this.armPropDesc = armPropDesc;
	}

	public int getAbadonItemId() {
		return abadonItemId;
	}

	public void setAbadonItemId(int abadonItemId) {
		this.abadonItemId = abadonItemId;
	}

	public int getStaticSkillId() {
		return staticSkillId;
	}

	public void setStaticSkillId(int staticSkillId) {
		this.staticSkillId = staticSkillId;
	}

	public byte getMaxTalent() {
		return maxTalent;
	}

	public void setMaxTalent(byte maxTalent) {
		this.maxTalent = maxTalent;
	}

	public short getSequence() {
		return sequence;
	}

	public void setSequence(short sequence) {
		this.sequence = sequence;
	}

	public byte getIllustrations() {
		return illustrations;
	}

	public void setIllustrations(byte illustrations) {
		this.illustrations = illustrations;
	}

	public byte getStrongestTag() {
		return strongestTag;
	}

	public void setStrongestTag(byte strongestTag) {
		this.strongestTag = strongestTag;
	}

	public byte getRating() {
		return rating;
	}

	public void setRating(byte rating) {
		this.rating = rating;
	}

	public int getFavourSchema() {
		return favourSchema;
	}

	public void setFavourSchema(int favourSchema) {
		this.favourSchema = favourSchema;
	}

	public boolean isShowIllustrations() {
		return (illustrations == 1);
	}

	// 得到评定的图片
	public int getRatingPic() {
		return 0;
		
	}

	public byte getNoCloth() {
		return noCloth;
	}

	public void setNoCloth(byte noCloth) {
		this.noCloth = noCloth;
	}

	public boolean isNoClothHero() {
		return noCloth == 1;
	}

	public static HeroProp fromString(String csv) {
		HeroProp prop = new HeroProp();
		StringBuilder buf = new StringBuilder(csv);
		prop.setId(StringUtil.removeCsvInt(buf));
		prop.setName(StringUtil.removeCsv(buf));
		prop.setCountry(StringUtil.removeCsvByte(buf));
		prop.setDesc(StringUtil.removeCsv(buf));
		prop.setIcon(StringUtil.removeCsv(buf));
		prop.setImg(StringUtil.removeCsv(buf));
		prop.setFood(StringUtil.removeCsvInt(buf));
		prop.setArmPropDesc(StringUtil.removeCsv(buf));
		prop.setAbadonItemId(StringUtil.removeCsvInt(buf));
		prop.setStaticSkillId(StringUtil.removeCsvInt(buf));
		prop.setMaxTalent(StringUtil.removeCsvByte(buf));
		prop.setSequence(StringUtil.removeCsvShort(buf));
		prop.setIllustrations(StringUtil.removeCsvByte(buf));
		prop.setStrongestTag(StringUtil.removeCsvByte(buf));
		prop.setRating(StringUtil.removeCsvByte(buf));
		prop.setFavourSchema(StringUtil.removeCsvInt(buf));
		prop.setSourceSpec(StringUtil.removeCsv(buf));
		prop.setAttack(StringUtil.removeCsvInt(buf));
		prop.setDefend(StringUtil.removeCsvInt(buf));
		prop.setSkilled1(StringUtil.removeCsv(buf));
		prop.setSkilled2(StringUtil.removeCsv(buf));
		prop.setSkilled3(StringUtil.removeCsv(buf));
		prop.setSkilled4(StringUtil.removeCsv(buf));
		prop.setSkilled5(StringUtil.removeCsv(buf));
		prop.setFitSkill(StringUtil.removeCsv(buf));
		prop.setNoCloth(StringUtil.removeCsvByte(buf));
		return prop;
	}
}
