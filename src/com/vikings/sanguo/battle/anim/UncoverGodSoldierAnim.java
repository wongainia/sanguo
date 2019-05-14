package com.vikings.sanguo.battle.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.AnimAdapter;
import com.vikings.sanguo.utils.ViewUtil;

public class UncoverGodSoldierAnim {
	private View view;
	private CallBack callBack;

	public UncoverGodSoldierAnim(View view, CallBack callBack) {
		this.view = view;
		this.callBack = callBack;
	}

	public void start() {
		Animation backScaleAnim = AnimationUtils.loadAnimation(Config
				.getController().getUIContext(), R.anim.back_scale);
		backScaleAnim.setAnimationListener(new FlopAnimListener(view));
		view.clearAnimation();
		view.startAnimation(backScaleAnim);
	}

	class FlopAnimListener extends AnimAdapter {
		private View view;

		public FlopAnimListener(View view) {
			super();
			this.view = view;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (null != view) {
				view.setOnClickListener(null);
				ViewUtil.setGone(view.findViewById(R.id.reverse));
				ViewUtil.setVisible(view.findViewById(R.id.front));

				Animation frontScaleAnim = AnimationUtils.loadAnimation(Config
						.getController().getUIContext(), R.anim.front_scale);
				frontScaleAnim.setAnimationListener(new EndAnimListener());
				view.startAnimation(frontScaleAnim);
			}
		}
	}

	class EndAnimListener extends AnimAdapter {

		@Override
		public void onAnimationEnd(Animation animation) {
			if (null != callBack)
				callBack.onCall();
		}

	}
}
