package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.FiefDataSynInfo;
import com.vikings.sanguo.protos.MsgReqFiefDataSyn;

public class FiefDataSynReq extends BaseReq {

	private MsgReqFiefDataSyn req;

	public FiefDataSynReq(List<Long> allSynIds, List<Long> difSynIds) {
		List<FiefDataSynInfo> infos = new ArrayList<FiefDataSynInfo>();
		if (null != allSynIds && !allSynIds.isEmpty()) {
			for (long fiefid : allSynIds) {
				FiefDataSynInfo info = new FiefDataSynInfo().setSynFlag(
						Integer.valueOf(Constants.SYNC_TYPE_ALL)).setFiefid(
						fiefid);
				infos.add(info);
			}
		}
		if (null != difSynIds && !difSynIds.isEmpty()) {
			for (long fiefid : difSynIds) {
				FiefDataSynInfo info = new FiefDataSynInfo().setSynFlag(
						Integer.valueOf(Constants.SYNC_TYPE_DIFF)).setFiefid(
						fiefid);
				infos.add(info);
			}
		}

		req = new MsgReqFiefDataSyn().setInfosList(infos);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FIEF_DATA_SYN.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
