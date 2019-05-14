package com.vikings.sanguo.ui.alert;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;

public class ForecastTip extends Alert implements OnClickListener {
	private static final int layout=R.layout.forecast;
    private TextView desc,requestDesc,msg_1;
	private View content;
	public ForecastTip()
	{
		content=controller.inflate(layout);
		desc=(TextView)content.findViewById(R.id.forecastDesc);
		requestDesc=(TextView)content.findViewById(R.id.requestDesc);
		msg_1=(TextView)content.findViewById(R.id.msg_1);
		this.dialog = new TouchCloseDialog(controller.getUIContext());
		desc.setOnClickListener(this);
		requestDesc.setOnClickListener(this);
		msg_1.setOnClickListener(this);
	}

	public void show(String name)
	{
		ViewUtil.setRichText(desc, name);
		ViewUtil.setRichText(requestDesc, "占卜成功！已扣除#rmb#2元宝");
		super.show(content);
	}
	@Override
	public void onClick(View v) {
		
		 dismiss();
	}
	class TouchCloseDialog extends Dialog {

		protected TouchCloseDialog(Context context) {
			super(context, R.style.dialog);
			
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP)
				ForecastTip.this.dismiss();
			return super.onTouchEvent(event);
		}

	}
}
