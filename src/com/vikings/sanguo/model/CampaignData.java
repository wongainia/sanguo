package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

public class CampaignData {
	private CampaignInfoClient campaignClient;
	private boolean isOpen = false;

	public CampaignData(CampaignInfoClient campaignClient) {
		this.campaignClient = campaignClient;
		this.isOpen = false;
	}

	public void setCampaignClient(CampaignInfoClient campaignClient) {
		this.campaignClient = campaignClient;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public CampaignInfoClient getCampaignClient() {
		return campaignClient;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public static List<CampaignData> getCampaignData(ActInfoClient actInfoClient) {
		List<CampaignData> data = new ArrayList<CampaignData>();
		if (null != actInfoClient) {
			List<CampaignInfoClient> ls = actInfoClient.getCampaignList();
			int[] completed = actInfoClient.getCompleted();
			for (int i = 0; i < ls.size(); i++) {
				CampaignInfoClient it = ls.get(i);
				CampaignData cd = new CampaignData(it);
				if (completed[0] != completed[1] && completed[0] == i)
					cd.setOpen(true);
				data.add(cd);
			}
		}
		return data;
	}
}
