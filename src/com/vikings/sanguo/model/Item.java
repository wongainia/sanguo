package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.StringUtil;

/**
 * @author Brad.Chen
 * 
 */
public class Item {

	// 对应clentType
	public static final int TYPE_GIFT = 1;// 礼包
	public static final int TYPE_TOOLS = 2;// 道具
	public static final int TYPE_STONE = 4;// 宝石
	public static final int TYPE_HERO_SOUL = 5; // 將魂

	public static final int ITME_ID_MONEY_SPEED_UP_CARD = 10031;
	public static final int ITME_ID_FOOD_SPEED_UP_CARD = 10032;
	public static final int ITEM_ID_PLAYER_WANTED = 10989;
	public static final int ITEM_ID_STRENGTHEN = 0;

	private int id;
	private int serverType; // 1仓库使用、2仓库不可用
	private int clentType; // 1道具、2材料、3书籍
	private String name;
	private int money; // 单价
	private int sell;// 卖出单价
	private int funds; // 现金
	private String image;
	private String desc;
	private String typeName;
	private String useTip;
	private int use;// 是否可用（控制界面，0不可用，1打开，2使用）
	private int oneKey;// 是否一键开启
	private int period; // 过期时间（只对1K、9K系列有效）
	private int nonjoinder;// 同类物品是否合并成一个包裹
	private int sellConfirm;// 卖出时是否二次确认 0不确认，1确认
	private int costFunds;// 原价
	private int discount;// 折扣率
	private short minUseLevel; // 使用最低等级
	private int currency; // 开启需要的元宝数量
	private int illustrationIdx; // 技能图鉴排序（0不在技能图鉴显示，数字正序排序）

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerType() {
		return serverType;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	public int getClentType() {
		return clentType;
	}

	public void setClentType(int clentType) {
		this.clentType = clentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getSell() {
		return sell;
	}

	public void setSell(int sell) {
		this.sell = sell;
	}

	public int getFunds() {
		return funds;
	}

	public void setFunds(int funds) {
		this.funds = funds;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getUseTip() {
		return useTip;
	}

	public void setUseTip(String useTip) {
		this.useTip = useTip;
	}

	public int getUse() {
		return use;
	}

	public void setUse(int use) {
		this.use = use;
	}

	public int getOneKey() {
		return oneKey;
	}

	public void setOneKey(int oneKey) {
		this.oneKey = oneKey;
	}

	public boolean canOpenOneKey() {
		return this.oneKey == 1;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getNonjoinder() {
		return nonjoinder;
	}

	public void setNonjoinder(int nonjoinder) {
		this.nonjoinder = nonjoinder;
	}

	public int getSellConfirm() {
		return sellConfirm;
	}

	public void setSellConfirm(int sellConfirm) {
		this.sellConfirm = sellConfirm;
	}

	public boolean needSellConfirm() {
		return this.sellConfirm == 1;
	}

	public int getCostFunds() {
		return costFunds;
	}

	public void setCostFunds(int costFunds) {
		this.costFunds = costFunds;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public short getMinUseLevel() {
		return minUseLevel;
	}

	public void setMinUseLevel(short minUseLevel) {
		this.minUseLevel = minUseLevel;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void setIllustrationIdx(int illustrationIdx) {
		this.illustrationIdx = illustrationIdx;
	}

	public int getIllustrationIdx() {
		return illustrationIdx;
	}

	public boolean isIllustastion() {
		return illustrationIdx > 0;
	}

	public static Item fromString(String csv) {
		Item item = new Item();
		StringBuilder buf = new StringBuilder(csv);
		item.setId(StringUtil.removeCsvInt(buf));
		item.setServerType(StringUtil.removeCsvShort(buf));
		item.setClentType(StringUtil.removeCsvShort(buf));
		item.setName(StringUtil.removeCsv(buf));
		item.setMoney(StringUtil.removeCsvInt(buf));
		item.setSell(StringUtil.removeCsvInt(buf));
		item.setFunds(StringUtil.removeCsvInt(buf));
		item.setImage(StringUtil.removeCsv(buf));
		item.setDesc(StringUtil.removeCsv(buf));
		item.setTypeName(StringUtil.removeCsv(buf));
		item.setUseTip(StringUtil.removeCsv(buf));
		item.setUse(StringUtil.removeCsvInt(buf));
		item.setOneKey(StringUtil.removeCsvInt(buf));
		item.setPeriod(StringUtil.removeCsvInt(buf));
		item.setNonjoinder(StringUtil.removeCsvInt(buf));
		item.setSellConfirm(StringUtil.removeCsvInt(buf));
		item.setCostFunds(StringUtil.removeCsvInt(buf));
		item.setDiscount(StringUtil.removeCsvInt(buf));
		item.setMinUseLevel(StringUtil.removeCsvShort(buf));
		item.setCurrency(StringUtil.removeCsvInt(buf));
		item.setIllustrationIdx(StringUtil.removeCsvInt(buf));
		return item;
	}

	/**
	 * 是否可重叠
	 * 
	 * @return
	 */
	public boolean overlap() {
		return nonjoinder == 1;
	}

	public boolean canUse() {
		return use != 0;
	}

	// 0不可用，1打开，2使用，3鉴定，4兑换，5分解，6合成，7召唤，8进化，9锻造，10强化，11升级，12洗炼
	public String getUseBtnStr() {
		String str = "";
		switch (getUse()) {
		case 1:
			str = "打   开";
			break;
		case 2:
			str = "使   用";
			break;
		case 3:
			str = "鉴   定";
			break;
		case 4:
			str = "兑   换";
			break;
		case 5:
			str = "分   解";
			break;
		case 6:
			str = "合   成";
			break;
		case 7:
			str = "召   唤";
			break;
		case 8:
			str = "进   化";
			break;
		case 9:
			str = "锻   造";
			break;
		case 10:
			str = "强   化";
			break;
		case 11:
			str = "升   级";
			break;
		case 12:
			str = "洗   炼";
			break;
		default:
			str = "";
			break;
		}
		return str;
	}

	public boolean isCurrencyItem() {
		return funds > 0;
	}

	public int getPrice() {
		return isCurrencyItem() ? getFunds() : getMoney();
	}

	public int getDiscountPrice(int discount) {
		return CalcUtil.upNum(getPrice() * discount / 100f);
	}

	public String getPreIcon() {
		return isCurrencyItem() ? "#rmb#" : "#money#";
	}

	public boolean isEnoughToBuy(int discount, int count) {
		if (isCurrencyItem()) {
			if (Account.user.getCurrency() >= getDiscountPrice(discount)
					* count)
				return true;
		} else {
			if (Account.user.getMoney() >= getDiscountPrice(discount) * count)
				return true;

		}
		return false;
	}
}
