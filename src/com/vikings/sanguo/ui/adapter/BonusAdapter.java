package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BonusProp;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.window.GuildExpendHonorRankWindow;
import com.vikings.sanguo.ui.window.GuildHonorRankWindow;
import com.vikings.sanguo.ui.window.MarsHonorRankWindow;
import com.vikings.sanguo.ui.window.PersonageExpendHonorRankWindow;
import com.vikings.sanguo.utils.ViewUtil;

public class BonusAdapter extends ObjectAdapter {

	private int[] iconIds = { R.drawable.tiexue_zhanshen_bg,
			R.drawable.jiazhu_zhiguang_bg, R.drawable.yizhi_qianjin_bg,
			R.drawable.weizhen_jianghu_bg, };

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.name = (ImageView) convertView.findViewById(R.id.name);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BonusProp bonus = (BonusProp) getItem(position);
		new ViewImgCallBack(bonus.getIcon(), holder.img);
		holder.name.setBackgroundResource(iconIds[position]);
		ViewUtil.setRichText(holder.desc, bonus.getDesc());
		convertView.setOnClickListener(new ClickListener(bonus));
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.common_type_item;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	class ViewHolder {
		ImageView img;
		ImageView name;
		TextView desc;
	}

	class ClickListener implements OnClickListener {
		private BonusProp bonus;

		public ClickListener(BonusProp bonus) {
			this.bonus = bonus;
		}

		@Override
		public void onClick(View v) {
			switch (bonus.getId()) {
			case BonusProp.TIEXUE_ZHANSHEN:
				// 铁血战神
				new MarsHonorRankWindow().doOpen();
				break;
			case BonusProp.JIAZHU_ZHIGUANG:
				// 家族之光
				new GuildHonorRankWindow().doOpen();
				break;
			// 威震江湖
			case BonusProp.WEIZHEN_JIANGHU:
				new GuildExpendHonorRankWindow().doOpen();
				break;
			// 一掷千金
			case BonusProp.YIZHI_QIANJIN:
				new PersonageExpendHonorRankWindow().doOpen();
				break;
			default:
				break;
			}
		}
	}

}
