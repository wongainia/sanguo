package com.vikings.sanguo.widget;

import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.model.OtherRichGuildInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.thread.GuildIconCallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class GuildDetailTopInfo {
	public static void updateOwner(RichGuildInfoClient rgic,
			final BriefUserInfoClient briefUser, GuildProp guildProp,
			ViewGroup view) {
		new GuildIconCallBack(rgic.getGic(),
				(ImageView) view.findViewById(R.id.icon),
				Config.SCALE_FROM_HIGH * Constants.GUILD_ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.GUILD_ICON_HEIGHT);
		ViewUtil.setText((TextView) view.findViewById(R.id.name), rgic.getGic()
				.getName() + "家族");

		ViewUtil.setText((TextView) view.findViewById(R.id.guildId),
				rgic.getGuildid());
		ViewUtil.setText((TextView) view.findViewById(R.id.count), rgic
				.getMembers().size() + "/" + guildProp.getMaxMemberCnt());

		ViewUtil.setText((TextView) view.findViewById(R.id.level),
				briefUser.getLevel() + "级");
		TextView leaderName = (TextView) view.findViewById(R.id.leaderName);
		ViewUtil.setText(leaderName, briefUser.getNickName());
		leaderName.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		leaderName.setTextColor(Config.getController().getResources()
				.getColor(R.color.color16));
		ViewUtil.setBold(leaderName);

		leaderName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.getController().showCastle(briefUser.getId().intValue());
			}
		});
	}

	public static void updateOther(OtherRichGuildInfoClient orgic,
			final BriefUserInfoClient briefUser, GuildProp guildProp,
			ViewGroup view) {
		new GuildIconCallBack(orgic.getOgic(),
				(ImageView) view.findViewById(R.id.icon),
				Config.SCALE_FROM_HIGH * Constants.GUILD_ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.GUILD_ICON_HEIGHT);
		ViewUtil.setText((TextView) view.findViewById(R.id.name), orgic.getOgic()
				.getName() + "家族");

		ViewUtil.setText((TextView) view.findViewById(R.id.guildId),
				orgic.getOgic().getId());
		ViewUtil.setText((TextView) view.findViewById(R.id.count), orgic
				.getMembers().size() + "/" + guildProp.getMaxMemberCnt());

		ViewUtil.setText((TextView) view.findViewById(R.id.level),
				briefUser.getLevel() + "级");
		TextView leaderName = (TextView) view.findViewById(R.id.leaderName);
		ViewUtil.setText(leaderName, briefUser.getNickName());
		leaderName.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		leaderName.setTextColor(Config.getController().getResources()
				.getColor(R.color.color16));
		ViewUtil.setBold(leaderName);

		leaderName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.getController().showCastle(briefUser.getId().intValue());
			}
		});
	}
}
