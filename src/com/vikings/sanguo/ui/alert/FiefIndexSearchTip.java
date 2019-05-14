/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-10 上午9:55:08
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.regex.Pattern;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FiefIndexSearchTip extends CustomConfirmDialog{
	protected EditText xIdx;
	protected EditText yIdx;

	public FiefIndexSearchTip() {
		super("通过位置坐标查找", CustomConfirmDialog.DEFAULT);
		
		setButton(CustomConfirmDialog.SECOND_BTN, "查找", searchL);
		setButton(CustomConfirmDialog.THIRD_BTN, "关闭", closeL);
		
		xIdx = (EditText) tip.findViewById(R.id.x);
		yIdx = (EditText) tip.findViewById(R.id.y);

		xIdx.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int actionId,
					KeyEvent arg2) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					yIdx.setFocusableInTouchMode(true);
					yIdx.setFocusable(true);
					yIdx.requestFocus();
				}
				return false;
			}
		});
	}

	private OnClickListener searchL = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (StringUtil.isNull(xIdx.getText().toString())
					|| StringUtil.isNull(yIdx.getText().toString())) {
				Config.getController().alert("请输入完整的领地坐标位置!");
			} else {
				String xStr = xIdx.getText().toString().trim();
				String yStr = yIdx.getText().toString().trim();

				Pattern pattern = Pattern.compile("[0-9]*"); // 正数 ^(-|[0-9])

				if (pattern.matcher(xStr).matches()
						&& pattern.matcher(yStr).matches()) {
					int x = Integer.valueOf(xStr);
					int y = Integer.valueOf(yStr);

					// 暂时支持的范围：x范围[0, 8100],y范围[0, 4050]
					// if (Math.abs(x) <= 8100 && Math.abs(y) < 4050) {
					if (x >= 0 && x <= 999999 && y >= 0 && y < 999999) {
						handle(x, y);
						dialog.dismiss();
					} else
						Config.getController().alert("请输入有效的领地坐标位置!");
				} else
					Config.getController().alert("请输入有效的领地坐标位置!");
			}
		}
	};

	public void handle(int x, int y) {
		long fiefId = TileUtil.index2TileId(x, y);
		Config.getController().getBattleMap().moveToFief(fiefId, true,true);
	}

	@Override
	public View getContent() {
		return Config.getController().inflate(R.layout.alert_search_idx);
	}
}
