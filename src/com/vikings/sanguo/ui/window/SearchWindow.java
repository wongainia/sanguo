package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UserQuery;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class SearchWindow extends CustomPopupWindow implements OnClickListener {

	private ViewGroup defaultLayout,maleLayout,femaleLayout;
	private int sex=0;
	@Override
	protected void init() {
		super.init("条件查找");
		setContent(R.layout.search);
		defaultLayout=(ViewGroup) window.findViewById(R.id.defaultLayout);//默认性别
		defaultLayout.setOnClickListener(this);
		
		maleLayout=(ViewGroup) window.findViewById(R.id.maleLayout);//男
		maleLayout.setOnClickListener(this);
		
		femaleLayout=(ViewGroup) window.findViewById(R.id.femaleLayout);//女
		femaleLayout.setOnClickListener(this);
		
		setBottomButton("查找玩家", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Config.getController().openSearchResult(fillUser());
			}
		});
	}

	public void show() {
		doOpen();
		ViewUtil.setText(window, R.id.userID, "");
		ViewUtil.setText(window, R.id.name, "");
		selectDefault();
	}

	private UserQuery fillUser() {
		UserQuery userQuery = new UserQuery();
		userQuery.setSex(sex);
		userQuery.setNickName(ViewUtil.getText(window, R.id.name));
		if (!StringUtil.isNull(ViewUtil.getText(window, R.id.userID))) {
			int userId = 0;
			try {
				userId = Integer.valueOf(ViewUtil.getText(window, R.id.userID));
			} catch (Exception e) {
				userId = Integer.MAX_VALUE - 1;
			}	
			userQuery.setUserId(userId);
		} else {
			userQuery.setUserId(0);
		}
		return userQuery;
	}
	
	private void selectDefault(){//默认不限
		ViewUtil.setVisible(defaultLayout,R.id.checked);
		ViewUtil.setGone(maleLayout, R.id.checked);
		ViewUtil.setGone(femaleLayout, R.id.checked);
	}
	
	private void selectFemale() {//女
		ViewUtil.setGone(defaultLayout,R.id.checked);
		ViewUtil.setGone(maleLayout, R.id.checked);
		ViewUtil.setVisible(femaleLayout, R.id.checked);
	}

	private void selectMale() {//男
		ViewUtil.setGone(defaultLayout,R.id.checked);
		ViewUtil.setVisible(maleLayout, R.id.checked);
		ViewUtil.setGone(femaleLayout, R.id.checked);
	}

	@Override
	public void onClick(View v) {
		if(v==defaultLayout){
			if(sex==Constants.DEFAULT)
				return;
			sex = Constants.DEFAULT;
			selectDefault();
		}else if (v == maleLayout) {
			if (sex == Constants.MALE)
				return;
			sex = Constants.MALE;
			selectMale();
		} else if (v == femaleLayout) {
			if (sex == Constants.FEMALE)
				return;
			sex = Constants.FEMALE;
			selectFemale();
		}
		
	}
}
