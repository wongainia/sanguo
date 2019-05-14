package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BuildingRequirement;
import com.vikings.sanguo.model.HeroInit;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.model.TrainingReward;

public class TrainingRewardsCache extends LazyLoadArrayCache {
	private static final String FILE_NAME = "training_rewards.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((TrainingReward) obj).getTrainingId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((TrainingReward) obj).getThingType(),
				((TrainingReward) obj).getThingId());
	}

	@Override
	public Object fromString(String line) {
		return TrainingReward.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public List<TrainingReward> getTrainingRewards(int trainingId) {
		List<TrainingReward> trainingRewards = new ArrayList<TrainingReward>();
		List<TrainingReward> trainingRewards2 = search(trainingId);
		if (!ListUtil.isNull(trainingRewards2)) {
			trainingRewards.addAll(trainingRewards2);
		}
		return trainingRewards;
	}

	// 得到红楼第一部分引导的 将领 方案号
	public HeroInit getHeroInit(int trainingId) {
		List<TrainingReward> trainingRewards = getTrainingRewards(trainingId);
		for (TrainingReward trainingReward : trainingRewards) {
			if (trainingReward.getThingType() == 4/* 将领 */) {
				try {
					HeroInit heroInit = (HeroInit) CacheMgr.heroInitCache
							.get(trainingReward.getThingId());
					return heroInit;
				} catch (GameException e) {
					e.printStackTrace();
					Log.e("TrainingReward",
							"heroInit" + trainingReward.getThingId()
									+ "not found!");
				}
			}
		}
		return null;
	}

	// 得到引导结束该得到的奖励
	public ReturnInfoClient getRi(int trainingId) {
		ReturnInfoClient ri = new ReturnInfoClient();
		List<TrainingReward> trainingRewards = getShowRewards(trainingId);
		if (!ListUtil.isNull(trainingRewards)) {
			for (TrainingReward trainingReward : trainingRewards) {
				ri.addCfg(trainingReward.getThingType(),
						trainingReward.getThingId(), trainingReward.getAmount());
			}
		}
		return ri;
	}

	public boolean hasRewards(int trainingId) {
		List<TrainingReward> trainingRewards = getShowRewards(trainingId);
		if (!ListUtil.isNull(trainingRewards)) {
			return true;
		}
		return false;
	}

	public List<TrainingReward> getShowRewards(int trainingId) {
		List<TrainingReward> trainingRewards = new ArrayList<TrainingReward>();
		List<TrainingReward> trainingRewards2 = getTrainingRewards(trainingId);
		if (!ListUtil.isNull(trainingRewards2)) {
			for (TrainingReward trainingReward : trainingRewards2) {
				if (trainingReward.isShowReward()) {
					trainingRewards.add(trainingReward);
				}
			}
		}
		return trainingRewards;
	}

}
