package com.vikings.sanguo.ui.window;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.BattleLogAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class QuestListMainWindow extends CustomBaseListWindow {

	private ObjectAdapter adapter = new BattleLogAdapter();

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		GameBiz.getInstance().refreshQuest();
		List<QuestInfoClient> ls = Account.getQuestInfoAll();
		sort(ls);
		resultPage.setResult(ls);
		resultPage.setTotal(ls.size());
	}

	
	@Override
	public void handleItem(Object o) {
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected void init() {
		init("任务列表");
	}

	private void sort(List<QuestInfoClient> list) {
		Collections.sort(list, new Comparator<QuestInfoClient>() {

			@Override
			public int compare(QuestInfoClient o1, QuestInfoClient o2) {
				if (o1.isComplete() && !o2.isComplete()) {
					return -1;
				} else if (!o1.isComplete() && o2.isComplete()) {
					return 1;
				} else if (!o1.isComplete() && !o2.isComplete()) {
					if (!Account.readLog.isKnownQuest(o1.getQuestId())
							&& Account.readLog.isKnownQuest(o2.getQuestId())) {
						return -1;
					} else if (Account.readLog.isKnownQuest(o1.getQuestId())
							&& Account.readLog.isKnownQuest(o2.getQuestId())) {
						if (o2.getQuest().getMinLevel() == o1.getQuest()
								.getMinLevel()) {
							return o2.getQuestId() - o1.getQuestId();
						} else {
							return o2.getQuest().getMinLevel()
									- o1.getQuest().getMinLevel();
						}

					} else if (!Account.readLog.isKnownQuest(o1.getQuestId())
							&& !Account.readLog.isKnownQuest(o2.getQuestId())) {
						if (o2.getQuest().getMinLevel() == o1.getQuest()
								.getMinLevel()) {
							return o2.getQuestId() - o1.getQuestId();
						} else {
							return o2.getQuest().getMinLevel()
									- o1.getQuest().getMinLevel();
						}
					} else
						return 1;
				} else {
					return 0;
				}
			}
		});
	}
	
	public void open() {
		this.doOpen();
		this.firstPage();
	}

}
