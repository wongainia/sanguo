package com.vikings.sanguo.model;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;

public class Poker {

	private boolean isOpen = false;
	public Reverse reverse = new Reverse();
	public Front front = new Front();
	private int index;

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setOpenPoker(int index, PokerInfoclient pi) throws GameException {
		setOpen(true);
		setIndex(index);
		front.setOpen(pi);
	}
	
	public void setUnOpenPoker(int index, PokerConfig pc, double totalHP) throws GameException {
		setOpen(false);
		setIndex(index);
		front.setUnOpen(pc, totalHP);
	}
	
	public class Reverse {
		private int price;

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}
	}

	public class Front {
		private String name = "";
		private String Image = "";
		private int soldierId;
		private int baseCount;
		private int rewardCount;
		private String backImg = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getBaseCount() {
			return baseCount;
		}

		public void setBaseCount(int baseCount) {
			this.baseCount = baseCount;
		}

		public int getRewardCount() {
			return rewardCount;
		}

		public void setRewardCount(int rewardCount) {
			this.rewardCount = rewardCount;
		}

		public String getImage() {
			return Image;
		}

		public void setImage(String image) {
			Image = image;
		}

		public int getSoldierId() {
			return soldierId;
		}

		public String getBackImg() {
			return backImg;
		}

		public void setBackImg(String backImg) {
			this.backImg = backImg;
		}

		public void setSoldierId(int soldierId) {
			this.soldierId = soldierId;
		}

		public void setOpen(PokerInfoclient pi) throws GameException {
			PokerConfig pc = (PokerConfig) CacheMgr.pokerConfigCache
					.get(pi.getResult());
			TroopProp tp = (TroopProp) CacheMgr.troopPropCache
					.get(pc.getTroopId());
			setSoldierId(pc.getId());
			setImage(tp.getIcon());
			setBackImg(pc.getBackImg());
			setName(tp.getName());
			setBaseCount(pc.getCount());
			setRewardCount(pi.getCount());
		}
		
		public void setUnOpen(PokerConfig pc, double totalHP) throws GameException {
			TroopProp tp = (TroopProp) CacheMgr.troopPropCache.get(pc
					.getTroopId());
			int rewardCount = (int) (((double) pc.getCount() / Constants.GOD_SOLDIER_BASE_COUNT) * totalHP);
			setSoldierId(pc.getId());
			setImage(tp.getIcon());
			setBackImg(pc.getBackImg());
			setName(tp.getName());
			setBaseCount(pc.getCount());
			setRewardCount(rewardCount);
		}
	}

}
