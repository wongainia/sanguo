package com.vikings.sanguo.model;

import java.util.List;

import android.util.Log;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BaseEquipmentInfo;
import com.vikings.sanguo.protos.EquipmentInfo;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 装备信息
 * 
 * @author susong
 * 
 */
public class EquipmentInfoClient {
	private long id;
	private int equipmentId;
	private int level;
	private int quality;
	private int slotItemId;// 镶嵌宝石id
	private EquipEffectClient effect;// 附加效果

	private long heroId;// 将领id【注意】该字段为客户端展示时需要，所以客户端需要自己维护

	private PropEquipment prop;
	private EquipmentType equipmentType;
	private EquipmentDesc equipmentDesc;
	private Item stone;// 宝石

	// 套装技能没有触发时的 套装效果
	private BattleSkillEquipment battleSkillEquipment;

	public EquipmentInfoClient(int equipmentId, int quality, int slotItemId,
			long heroId) throws GameException {
		this.equipmentId = equipmentId;
		this.quality = quality;
		this.slotItemId = slotItemId;
		this.heroId = heroId;
		if (equipmentId > 0)
			prop = (PropEquipment) CacheMgr.propEquipmentCache.get(equipmentId);
		if (quality > 0) {
			if (null != prop)
				equipmentType = (EquipmentType) CacheMgr.equipmentTypeCache
						.search(prop.getQualityScheme(), quality);
			equipmentDesc = (EquipmentDesc) CacheMgr.equipmentDescCache
					.get((byte) quality);
		}
		if (slotItemId > 0)
			stone = (Item) CacheMgr.itemCache.get(slotItemId);
	}

	public EquipmentInfoClient(int initId) throws GameException {
		EquipmentInit init = (EquipmentInit) CacheMgr.equipmentInitCache
				.get(initId);
		if (null != init) {
			this.equipmentId = init.getId();
			this.quality = init.getInitQuality();
			this.level = init.getInitLevel();
			if (equipmentId > 0)
				prop = (PropEquipment) CacheMgr.propEquipmentCache
						.get(equipmentId);
			if (quality > 0) {
				if (null != prop)
					equipmentType = (EquipmentType) CacheMgr.equipmentTypeCache
							.search(prop.getQualityScheme(), quality);
				equipmentDesc = (EquipmentDesc) CacheMgr.equipmentDescCache
						.get((byte) quality);
			}
			if (slotItemId > 0)
				stone = (Item) CacheMgr.itemCache.get(slotItemId);
		}

	}

	public BattleSkillEquipment getBattleSkillEquipment() {
		return battleSkillEquipment;
	}

	// 得到 套装技能
	public void getBattleSkillEqu() {
		if (!prop.isSuitEq()) {
			battleSkillEquipment = null;
			return;
		}
		List<BattleSkillEquipment> bSkillEquipments = CacheMgr.battleSkillEquipmentCache
				.getAll();
		for (BattleSkillEquipment battleSkillEquipment : bSkillEquipments) {
			if (isFoundBattleSkillEqu(battleSkillEquipment.getEquipId1(),
					battleSkillEquipment)) {
				return;
			}
			if (isFoundBattleSkillEqu(battleSkillEquipment.getEquipId2(),
					battleSkillEquipment)) {
				return;
			}
			if (isFoundBattleSkillEqu(battleSkillEquipment.getEquipId3(),
					battleSkillEquipment)) {
				return;
			}
			if (isFoundBattleSkillEqu(battleSkillEquipment.getEquipId4(),
					battleSkillEquipment)) {
				return;
			}
		}
	}

	public boolean itemHasSuit() {
		return battleSkillEquipment != null;
	}

	private boolean isFoundBattleSkillEqu(int equipmentId,
			BattleSkillEquipment battleSkillEquipment) {
		if (equipmentId == prop.getId()) {
			this.battleSkillEquipment = battleSkillEquipment;
			if (heroId != 0 && heroId == battleSkillEquipment.getHeroId()) {
				return true;
			}
		}
		return false;
	}

	public EquipmentInfoClient() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getSlotItemId() {
		return slotItemId;
	}

	public void setSlotItemId(int slotItemId) {
		this.slotItemId = slotItemId;
	}

	public boolean hasStone() {
		return this.slotItemId > 0;
	}

	public EquipEffectClient getEffect() {
		return effect;
	}

	public void setEffect(EquipEffectClient effect) {
		this.effect = effect;
	}

	public PropEquipment getProp() {
		return prop;
	}

	public void setProp(PropEquipment prop) {
		this.prop = prop;
	}

	public EquipmentDesc getEquipmentDesc() {
		return equipmentDesc;
	}

	public void setEquipmentDesc(EquipmentDesc equipmentDesc) {
		this.equipmentDesc = equipmentDesc;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public Item getStone() {
		return stone;
	}

	public void setStone(Item stone) {
		this.stone = stone;
	}

	public long getHeroId() {
		return heroId;
	}

	public boolean weared() {
		return this.heroId > 0;
	}

	public void putOn(long heroId) {
		this.heroId = heroId;
	}

	public void putOff() {
		this.heroId = 0;
	}

	public String getColorName() {
		String colorName = "";
		if (null != prop)
			colorName = prop.getName();
		if (null != equipmentDesc)
			colorName = StringUtil.color(colorName, equipmentDesc.getColor());
		return colorName;
	}

	public String getColorTypeName() {
		String colorType = "";
		if (null != equipmentDesc)
			colorType = StringUtil.color(equipmentDesc.getName(),
					equipmentDesc.getColor());
		return colorType;
	}

	public boolean canUpgrade() {
		List<EquipmentLevelUp> list = CacheMgr.equipmentLevelUpCache
				.getLevelUp(prop.getLevelUpScheme(), (byte) quality,
						(byte) level);
		return !list.isEmpty();
	}

	public boolean canUpgradeStrone() {
		if (slotItemId > 0) {
			return null != CacheMgr.equipmentInsertItemCache
					.getNextLevel(slotItemId);
		}
		return false;
	}

	public boolean openStoneSlot() {
		if (null != prop)
			return prop.getMinQuality2OpenSlot() <= quality;
		return true;
	}

	public boolean canForge() {
		EquipmentForge ef = getNextForgeLevel();
		if (null == ef)
			return false;
		return ef.getMinQuality() < getQuality();
	}

	public EquipmentForge getNextForgeLevel() {
		if (null != effect && null != prop) {
			EquipmentForge ef = (EquipmentForge) CacheMgr.equipmentForgeCache
					.search(prop.getForgeScheme(), effect.getEffect()
							.getLevel());
			return ef;
		}
		return null;
	}

	public static EquipmentInfoClient convert(EquipmentInfo info, long heroId)
			throws GameException {
		if (null == info)
			return null;
		BaseEquipmentInfo bei = info.getBi();
		EquipmentInfoClient eic = new EquipmentInfoClient(bei.getItemid(),
				bei.getQuality(), bei.getSlotItemid(), heroId);
		eic.setId(bei.getId());
		eic.setLevel(bei.getLevel());
		eic.setEffect(EquipEffectClient.convert(bei.getEffect()));
		return eic;
	}

	public static EquipmentInfoClient convert(EquipmentInfo info)
			throws GameException {
		return convert(info, 0);
	}

	public void update(EquipmentInfoClient eic) {
		if (null == eic)
			return;
		setEquipmentId(eic.getEquipmentId());
		setLevel(eic.getLevel());
		setQuality(eic.getQuality());
		setSlotItemId(eic.getSlotItemId());
		setEffect(eic.getEffect());
		putOn(eic.getHeroId());
		setProp(eic.getProp());
		setEquipmentDesc(eic.getEquipmentDesc());
		setStone(eic.getStone());
	}

	public EquipmentInfoClient copy(int quality, int level) {
		try {
			EquipmentInfoClient newEic = new EquipmentInfoClient(
					getEquipmentId(), quality, getSlotItemId(), getHeroId());
			newEic.setLevel(level);
			return newEic;
		} catch (GameException e) {
			Log.e("EquipmentInfoClient", e.getErrorMsg());
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EquipmentInfoClient other = (EquipmentInfoClient) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
