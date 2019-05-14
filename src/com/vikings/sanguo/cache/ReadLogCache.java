package com.vikings.sanguo.cache;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.EventRewards;
import com.vikings.sanguo.utils.ListUtil;

/**
 * @author Brad.Chen
 * 
 */
public class ReadLogCache implements Serializable {

	private static final long serialVersionUID = -2969918633584336380L;

	static final private String file = "readLog";

	public boolean BUILD_ITEM = false;

	public boolean HOUSE_UI = false;

	public boolean FINISHED_FRESHMAN_STUDYING = false;

	public boolean FIRST_ENTER_HOUSE = false;

	public boolean FIRST_ENTER_MANNOR = false;
	public boolean FIRST_LOCK_STORE = true;

	public boolean FIRST_ENTER_ABNORMAL_PIT = false;

	public boolean FIRST_ENTER_GAMBLE = false;

	public boolean PROLOGUE = false;

	public List<Integer> knownQuestIds = new ArrayList<Integer>();

	public int MAP_GUIDE = 0;

	// 距离下次免费重置的时间
	public int FREE_RESET_TIME = 0;

	// 水果机剩余的免费次数
	public int FREE_TIMES = 0;

	public int REMAIN_TIMES = 0;

	// 副本默认选中将领
	public long DEFAULT_CAMPAIGN_HERO_ID1 = 0;
	public long DEFAULT_CAMPAIGN_HERO_ID2 = 0;
	public long DEFAULT_CAMPAIGN_HERO_ID3 = 0;
	//血战
	public long DEFAULT_BLOOD_HERO_ID1 = 0;
	public long DEFAULT_BLOOD_HERO_ID2 = 0;
	public long DEFAULT_BLOOD_HERO_ID3 = 0;

	// 当日战神宝箱可开启总次数
	public int WAR_LORD_BOX_TIMES = 0;

	// 最后一次开宝箱的时间
	public long LAST_OPEN_WAR_LORD_BOX_TIME = 0;

	// 天降横财 拜财神 已经开启的次数
	public int GOD_WEALTH_TIMES = 0;

	// 最后一次签到时间
	public long LAST_CHECKIN_TIME = 0;

	public int STEP1001 = 0;// 标识 拜财神，将领进化完成时回主城 触发 step1001指引

	public long training = 0;

	/*
	 * 选将成功前中断，回主城后从指引“红楼”开始； 选将成功后中断，从指引“战役”开始（指战役 指撸）
	 */
	public int Step201SelectHero = 0;// 1 成功前 2成功后

	public int step401 = 0;// 1 已经有将领达到10级 而且进入了401引导

	public int Step601 = 0;// 1 进入过601引导

	public int Step701 = 0;// 1 触发过 0没触发过
	
	public List<EventRewards> rewards = new ArrayList<EventRewards>();   

	private ReadLogCache() {
	}

	public void save() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(Config
					.getController()
					.getUIContext()
					.openFileOutput(file + Account.user.getSaveID(),
							Context.MODE_PRIVATE));
			out.writeObject(this);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ReadLogCache getInstance() {
		ReadLogCache cache = null;
		try {
			ObjectInputStream in = new ObjectInputStream(Config.getController()
					.getUIContext()
					.openFileInput(file + Account.user.getSaveID()));
			cache = (ReadLogCache) in.readObject();
			if (cache.knownQuestIds == null)
				cache.knownQuestIds = new ArrayList<Integer>();
			if (cache.rewards == null)
				cache.rewards = new ArrayList<EventRewards>();
			in.close();
		} catch (Exception e) {
			cache = new ReadLogCache();
		}
		return cache;
	}

	public void setTraining(long newtraining) {
		this.training = training | newtraining;
	}

	public boolean isKnownQuest(int id) {
		return knownQuestIds.contains(id);
	}

	public void addKnownQuest(int id) {
		knownQuestIds.add(id);
	}
	
	//根据活动或者奖励的id  得到它的最小开放等级
	public int getOpenLevel(int id)
	{
		int openLevel = 0;
		if(ListUtil.isNull(rewards) == false)
		{
			for(EventRewards eventRewards : rewards)
			{
				if(eventRewards.getId() == id)
				{
					openLevel = eventRewards.getMinLevel();
					break;
				}
			}
		}
		return openLevel;
	}
}
