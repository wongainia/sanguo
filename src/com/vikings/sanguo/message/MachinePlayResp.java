package com.vikings.sanguo.message;

import java.util.List;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspMachinePlay;

public class MachinePlayResp extends BaseResp
{

	private List<Integer> list;
	private ReturnInfoClient ri;
	private List<BuildingInfoClient> biList;
	// private ManorInfoClient mic;
	// 是否上榜，1：上榜，0：不上榜
	private int isOnList;
	private int leftCnt; // 剩余次数

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception
	{
		MsgRspMachinePlay resp = new MsgRspMachinePlay();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		list = resp.getWheelsList();
		ri = ReturnInfoClient.convert2Client(resp.getRi());

		Account.manorInfoClient.updateArmFromReturnInfo(ri);

		isOnList = resp.getIsOnList();
		if (resp.hasRemainTimes())
		{
			leftCnt = resp.getRemainTimes();

			Account.readLog.REMAIN_TIMES = leftCnt;
			if (leftCnt == 0)
			{
				Account.readLog.REMAIN_TIMES=88888;
			}
		}

		// 更新重置时间或剩余次数,这两个参数互斥
		if (resp.hasFreeResetTime())
		{
			Account.readLog.FREE_RESET_TIME = resp.getFreeResetTime();
			Account.readLog.FREE_TIMES = 0;
		}
		if (resp.hasFreeTimes())
		{
			Account.readLog.FREE_RESET_TIME = 0;
			Account.readLog.FREE_TIMES = resp.getFreeTimes();
		}
		Account.readLog.save();

		// 存储果实
		if (null != resp.getRi())
			Config.getController().updateUI(ri, true);

		// // 设置城堡
		// if (null != resp.getMic())
		// Account.manorInfoClient.update(resp.getMic());
	}

	public List<Integer> getList()
	{
		return list;
	}

	public ReturnInfoClient getRi()
	{
		return ri;
	}

	public List<BuildingInfoClient> getBiList()
	{
		return biList;
	}

	// public ManorInfoClient getMic() {
	// return mic;
	// }

	public int getIsOnList()
	{
		return isOnList;
	}

	public int getLeftCnt()
	{
		return leftCnt;
	}
}
