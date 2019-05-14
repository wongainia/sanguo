package com.vikings.sanguo.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.UserNotifyInfoClient;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class Announce implements OnClickListener {

	private ScrollText scrollText;

	private ListView listView;

	private View bt, annDetail;
	private ImageView mirrorBt;

	private AnnAdpter adapter = new AnnAdpter();

	public Announce() {
		scrollText = (ScrollText) Config.getController().findViewById(
				R.id.scrollText);
		listView = (ListView) Config.getController().findViewById(
				R.id.detailList);
		listView.setAdapter(adapter);
		bt = Config.getController().findViewById(R.id.annBt);
		bt.setOnClickListener(this);

		mirrorBt = (ImageView) Config.getController().findViewById(
				R.id.annMirrorBt);
		mirrorBt.setBackgroundDrawable(ImageUtil.getRotateBitmapDrawable(
				"top_bg_bt", 180));
		mirrorBt.setOnClickListener(this);

		annDetail = Config.getController().findViewById(R.id.annDetail);
		android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) annDetail
				.getLayoutParams();
		params.height = (int) (Config.screenHeight - 200 * Config.SCALE_FROM_HIGH);
		annDetail.setLayoutParams(params);

		scrollText.setOnClickListener(this);

		// Config.getController().findViewById(R.id.announceBorderBtm)
		// .setOnClickListener(this);
		// adjustImg(R.id.announceBorderLeft, "a_boder_left");
		// adjustImg(R.id.announceBorderRight, "a_boder_right");
	}

	// private void adjustImg(int id, String name) {
	// View v = Config.getController().findViewById(id);
	// LayoutParams lp = v.getLayoutParams();
	// lp.height = (int) (Config.screenHeight - 200 * Config.SCALE_FROM_HIGH);
	// v.setLayoutParams(lp);
	// BitmapDrawable d = new BitmapDrawable(Config.getController()
	// .getResources(), Config.getController().getBitmap(name));
	// d.setTileModeXY(null, TileMode.REPEAT);
	// d.setDither(true);
	// v.setBackgroundDrawable(d);
	// }

	public ScrollText getScrollText() {
		return scrollText;
	}

	@Override
	public void onClick(View v) {
		ViewUtil.toggleVisible(annDetail);
		ViewUtil.toggleVisible(bt);
		if (ViewUtil.isVisible(annDetail)) {
			adapter.clear();
			adapter.addItems(scrollText.getMsgLs());
			adapter.notifyDataSetChanged();
		}
	}

	private class AnnAdpter extends ObjectAdapter {

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			UserNotifyInfoClient mc = (UserNotifyInfoClient) o;
			ViewUtil.setRichText(v, R.id.name, StringUtil.color(
					CacheMgr.dictCache.getDict(Dict.TYPE_ANNOUNCE_NAME,
							mc.getType()),
					CacheMgr.dictCache.getDict(Dict.TYPE_ANNOUNCE_COLOR,
							mc.getType())));
			ViewUtil.setText(v, R.id.desc, mc.getMessage());
		}

		@Override
		public int getLayoutId() {
			return R.layout.announce_item;
		}

	}

}
