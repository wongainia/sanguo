package com.vikings.sanguo.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.ArmInfoSelectData;
import com.vikings.sanguo.model.HeroArmPropClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.EditNumTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class TroopAdapter extends ObjectAdapter {
	private UserTroopEffectInfo troopEffectInfo;
	private List<HeroInfoClient> hic;
	private List<OtherHeroInfoClient> ohic;
	private List<HeroArmPropClient> armProps = new ArrayList<HeroArmPropClient>();

	private int total;// 总兵力上限
	private CallBack cb;
	private boolean hasSeekBar;

	public void setTroopEffectInfo(UserTroopEffectInfo userTroopEffectInfo) {
		this.troopEffectInfo = userTroopEffectInfo;
	}

	public void setHic(List<HeroInfoClient> hic) {
		this.hic = hic;
		armProps.clear();
		if (!ListUtil.isNull(hic)) {
			for (HeroInfoClient heroInfoClient : hic) {
				if (heroInfoClient.isValid())
					armProps.addAll(heroInfoClient.getArmPropClient());
			}
		}
	}

	// 设置总兵力上限
	public void setTotal(int total) {
		this.total = total;
	}

	public void setOhic(List<OtherHeroInfoClient> ohic) {
		this.ohic = ohic;
		armProps.clear();
		if (!ListUtil.isNull(ohic)) {
			for (OtherHeroInfoClient oheroInfoClient : ohic) {
				if (oheroInfoClient.isValid())
					armProps.addAll(oheroInfoClient.getArmPropClient());
			}
		}
	}

	public TroopAdapter(CallBack cb, boolean hasSeekBar) {
		this.cb = cb;
		this.hasSeekBar = hasSeekBar;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.selCnt = (TextView) convertView
					.findViewById(R.id.setOffCount);
			holder.moveCnt = (SeekBar) convertView.findViewById(R.id.moveCnt);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.sysTroop = convertView.findViewById(R.id.sysTroop);
			holder.lockFrame = convertView.findViewById(R.id.lockFrame);
			holder.armType = convertView.findViewById(R.id.armType);
			convertView.setTag(holder);
		}
		this.setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
		ViewHolder holder = (ViewHolder) v.getTag();
		ArmInfoSelectData ai = (ArmInfoSelectData) getItem(index);

		TroopProp tp = ai.getAic().getProp();

		// 区分heroInfo和otherHeroInfo
		if (!ListUtil.isNull(hic))
			IconUtil.setArmIcon(holder.icon, ai.getAic(), troopEffectInfo, hic);
		else if (!ListUtil.isNull(ohic))
			IconUtil.setOtherArmIcon(holder.icon, ai.getAic(), troopEffectInfo,
					ohic);
		else {
			IconUtil.setArmIcon(holder.icon, ai.getAic(), troopEffectInfo, null);
		}

		ViewUtil.setText(holder.name, tp.getName() + "："
				+ ai.getAic().getCount());

		if (ai.isMine()) {
			ViewUtil.setGone(holder.sysTroop);
		} else {
			ViewUtil.setVisible(holder.sysTroop);
		}

		if (!ai.canChangeCount() || !hasSeekBar) {
			ViewUtil.setHide(holder.selCnt);
			ViewUtil.setGone(holder.moveCnt);
			ViewUtil.setVisible(holder.desc);
			ViewUtil.setText(holder.desc, tp.getDesc());
		} else {
			holder.moveCnt.setOnSeekBarChangeListener(null);
			ViewUtil.setVisible(holder.selCnt);
			ViewUtil.setVisible(holder.moveCnt);
			holder.moveCnt.setMax(ai.getAic().getCount());
			holder.moveCnt.setProgress(ai.getSelCount());
			ViewUtil.setText(holder.selCnt, ai.getSelCount());
			ViewUtil.setGone(holder.desc);
			holder.moveCnt.setTag(ai);
			holder.moveCnt.setOnSeekBarChangeListener(holder.seekBarL);
			holder.selCnt.setOnClickListener(holder.click);
			ViewUtil.setGone(holder.lockFrame);
		}

		if (HeroArmPropClient.isStrength(armProps, tp)) {
			ViewUtil.setVisible(holder.armType);
			ViewUtil.setImage(holder.armType, tp.getSmallIcon());
		} else
			ViewUtil.setGone(holder.armType);
	}

	@Override
	public int getLayoutId() {
		return R.layout.setoff_troop_item;
	}

	public List<ArmInfoClient> getTroops() {
		List<ArmInfoClient> selTroops = new ArrayList<ArmInfoClient>();
		for (Object it : content) {
			ArmInfoSelectData ca = (ArmInfoSelectData) it;
			if (ca.getSelCount() > 0 && ca.isMine()) {
				try {
					selTroops.add(new ArmInfoClient(ca.getAic().getId(), ca
							.getSelCount()));
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}
		return selTroops;
	}

	public int getTroopCnt() {
		int count = 0;
		for (Object it : content) {
			ArmInfoSelectData ca = (ArmInfoSelectData) it;
			count += ca.getSelCount();
		}
		return count;
	}

	public int getTroopHp() {
		int count = 0;
		for (Object it : content) {
			ArmInfoSelectData ca = (ArmInfoSelectData) it;
			count += ca.getSelCount() * ca.getAic().getProp().getHp();
		}
		return count;
	}

	private int before = 0;
	private int left = 0;

	private class ViewHolder {
		ImageView icon;
		TextView name, selCnt, desc;
		SeekBar moveCnt;
		View sysTroop, armType, lockFrame;
		public SeekBarListener seekBarL = new SeekBarListener();

		public OnClickListener click = new OnClickListener() {

			@Override
			public void onClick(View v) {
				new EditNumTip((ArmInfoSelectData) moveCnt.getTag(), cb).show();
			}
		};

		private class SeekBarListener implements OnSeekBarChangeListener {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				if (fromUser && total > 0) {
					if (left - (progress - before) < 0) {
						seekBar.setProgress(left + before);
					}
				}

				int cnt = seekBar.getProgress();

				// 设置剩余数量和选中数量
				ViewUtil.setText(selCnt, String.valueOf(cnt));

				((ArmInfoSelectData) seekBar.getTag()).setSelCount(cnt);

				if (null != cb)
					cb.onCall();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				before = seekBar.getProgress();
				left = total - getCurCount();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (null != cb)
					cb.onCall();
			}
		}
	}

	private int getCurCount() {
		int count = 0;
		List list = getContent();
		for (Object obj : list) {
			ArmInfoSelectData data = (ArmInfoSelectData) obj;
			count += data.getSelCount();
		}
		return count;
	}

}
