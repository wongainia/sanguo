/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-7-23 下午5:11:47
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.List;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class ShowItemTip extends Alert implements OnClickListener {

	private final static int layout = R.layout.alert_showitem;

	private View content;

	@Override
	protected void playSound() {

	}

	public ShowItemTip() {
		super(true);
		content = Config.getController().inflate(layout);
	}

	private void show() {
		content.requestLayout();
		super.show(content);
		this.dialog.setOnDismissListener(null);		
		LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = LayoutParams.WRAP_CONTENT;
		if(Config.screenHeight <= (int)(350*Config.SCALE_FROM_HIGH))
		{
			lp.height = Config.screenHeight;
		}
		else
		{
			lp.height = (int) (350*Config.SCALE_FROM_HIGH);
		}
		dialog.getWindow().setAttributes(lp);
	}

	public View getLayout() {
		return this.content;
	}

	public void show(String titleStr, List<ShowItem> items, CallBack callBack,
			boolean playDefaultSound) {
		if (playDefaultSound)
			SoundMgr.play(R.raw.sfx_tips2);

		ViewUtil.setRichText(content, R.id.title, titleStr);
		ViewGroup itemsVg = (ViewGroup) content.findViewById(R.id.items);
		ViewGroup vg = (ViewGroup) controller
				.inflate(R.layout.task_award_item,itemsVg,false);

		ViewGroup content0 = (ViewGroup) vg.findViewById(R.id.content);
		ViewGroup content1 = (ViewGroup) vg.findViewById(R.id.content1);

		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getCount() > 0) {
				if ((i + 1) % 2 == 0) {
					content1.addView(ViewUtil.getShowItemView(items.get(i),
							R.color.color7, false, false));
				} else {
					content0.addView(ViewUtil.getShowItemView(items.get(i),
							R.color.color7, false, false));
				}
			}
		}
		itemsVg.addView(vg);  
		itemsVg.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0)
			{
				dismiss();				
			}
		});
		if (callBack == null)
			this.dialog.setOnCancelListener(null);
		else
			this.dialog.setOnCancelListener(new CancleListener(callBack));
		show();
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

	private class CancleListener implements OnCancelListener {

		CallBack call;

		public CancleListener(CallBack call) {
			this.call = call;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			if (null != call)
				call.onCall();
			ShowItemTip.this.dialog.setOnCancelListener(null);
		}
	}
}
