package com.vikings.sanguo.invoker;

import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.alert.AlertTaskFinishTip;

public class FinishQuestInvoker extends BaseInvoker {
	protected QuestInfoClient qic;

	protected ReturnInfoClient rs;

	private boolean showAnim;

	private String opName;

	public FinishQuestInvoker(String opName, QuestInfoClient qic,
			boolean showAnim) {
		if(qic.getQuest() == null && qic.getQuestId() != 0)
		{
			try
			{
				this.qic = new QuestInfoClient(qic.getQuestId());
			} catch (GameException e)
			{
				this.qic = qic;
				e.printStackTrace();
			}
		}
		else
		{
			this.qic = qic;
		}
		this.showAnim = showAnim;
		this.opName = opName;
	}

	public FinishQuestInvoker(QuestInfoClient qic) {
		this("领奖", qic, true);
	}

	@Override
	protected String failMsg() {
		return opName + "失败";
	}

	@Override
	protected void fire() throws GameException {
		rs = GameBiz.getInstance().questFinish(qic.getQuestId());

		Account.removeQuest(qic);

		try {
			GameBiz.getInstance().refreshQuest();
		} catch (Exception e) {
			Log.e("FinishQuestInvoker", "fail to refresh quest data", e);
		}
	}

	@Override
	protected String loadingMsg() {
		return opName;
	}

	@Override
	protected void onOK() {
		SoundMgr.play(R.raw.sfx_receive);
		if (showAnim) {
			ctr.updateUI(rs, true, false, true);					
			if(qic != null)
			{
				if(qic.getQuest() != null)
				{
					if(qic.getQuest().getSpecialType() == 12 || qic.getQuest().getSpecialType() == 16)
					{
						AlertTaskFinishTip alertTaskFinishTip = new AlertTaskFinishTip()
						{
							@Override
							protected int getDrawable()
							{
								return R.drawable.award_success;
							}
						};
						alertTaskFinishTip.show(rs.showReturn(true), null);
						return;
					}
				}
			}
			new AlertTaskFinishTip().show(rs.showReturn(true), null);
		} else {
			rs.setMsg(opName + "成功");
			ctr.updateUI(rs, true, false, true);
		}
	}
}
