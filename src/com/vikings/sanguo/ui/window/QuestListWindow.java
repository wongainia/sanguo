package com.vikings.sanguo.ui.window;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.QuestAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class QuestListWindow extends CustomBaseListWindow {

	public QuestInfoClient qic;

	private boolean init = false;

	@Override
	protected void init() {
		adapter = new QuestAdapter(this);
		super.init("任务列表");
	}

	public void open(QuestInfoClient qic) {
		this.qic = qic;
		doOpen();
		this.firstPage();
	}

	public void open() {
		open(null);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void showUI() {
		if (null != adapter && init) {
			adapter.clear();
			adapter.addItems(getQuestInfoClientList());
		}
		super.showUI();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		if (!init) {
			GameBiz.getInstance().refreshQuest();
			init = true;
		}
		List<QuestInfoClient> ls = getQuestInfoClientList();
		resultPage.setTotal(ls.size());
		resultPage.setResult(ls);
	}

	private List<QuestInfoClient> getQuestInfoClientList() {
		List<QuestInfoClient> ls = Account.getNormalQuest();
		sort(ls);
		return ls;
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
					if (o2.getQuest().getMinLevel() == o1.getQuest()
							.getMinLevel()) {
						return o1.getQuest().getSequence()
								- o2.getQuest().getSequence();
					} else {
						return o2.getQuest().getMinLevel()
								- o1.getQuest().getMinLevel();
					}
				} else {
					return o1.getQuest().getSequence()
							- o2.getQuest().getSequence();
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void updateUI() {
		super.updateUI();
		if (null != qic && null != adapter) {
			List<QuestInfoClient> ls = adapter.getContent();
			int pos = ls.indexOf(qic);
			if (pos != -1) {
				listView.requestFocusFromTouch();
				listView.setSelection(pos);
			}
			qic = null;
		}
	}

	@Override
	public void handleItem(Object o) {
	}

	public void setFooterDetailGone() {
		if (null != footerView
				&& ViewUtil.isVisible(footerView
						.findViewById(R.id.detailLayout))) {
			ViewUtil.setGone(footerView, R.id.detailLayout);
		}
	}
}
