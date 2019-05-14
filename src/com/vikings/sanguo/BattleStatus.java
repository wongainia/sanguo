package com.vikings.sanguo;

import com.vikings.sanguo.config.Config;

/**
 * 战争状态
 * 
 * @author susong
 * 
 */
public class BattleStatus {
	public static final int BATTLE_STATE_NONE = 0; // (无)
	
	public static final int BATTLE_STATE_SURROUND = BATTLE_STATE_NONE + 1 + 1; // 围城中
	public static final int BATTLE_STATE_SURROUND_END = BATTLE_STATE_SURROUND + 1; // 围城结束
	public static final int BATTLE_STATE_FINISH = BATTLE_STATE_SURROUND_END + 1; // 战斗结束

	public static String getStatusName(int state) {
		String statusName = "";
		switch (state) {
		case BATTLE_STATE_NONE:
			statusName = Config.getController().getString(
					R.string.BattleStatus_getStatusName_1);
			break;
		case BATTLE_STATE_SURROUND:
			statusName = Config.getController().getString(
					R.string.BattleStatus_getStatusName_3);
			break;
		case BATTLE_STATE_SURROUND_END:
			statusName = Config.getController().getString(
					R.string.BattleStatus_getStatusName_4);
			break;
		case BATTLE_STATE_FINISH:
			statusName = Config.getController().getString(
					R.string.BattleStatus_getStatusName_5);
			break;
		default:
			break;
		}

		return statusName;
	}

	public static boolean isSafe(int state) {
		return BATTLE_STATE_NONE == state;
	}

	public static boolean isInBattle(int state) {
		return (BATTLE_STATE_NONE != state && BATTLE_STATE_FINISH != state);
	}

	public static boolean isInSurround(int state) {
		return BATTLE_STATE_SURROUND == state;
	}

	public static boolean isInSurroundEnd(int state) {
		return BATTLE_STATE_SURROUND_END == state;
	}

	public static boolean isNoBattle(int state) {
		return (BATTLE_STATE_NONE == state || BATTLE_STATE_FINISH == state);
	}

	// 不是围城结束/战争结束状态，可以移动/增援
	// public static boolean canMove(int state) {
	// return !(BATTLE_STATE_SURROUND_END == state || BATTLE_STATE_FINISH ==
	// state);
	// }

	// 围城/围城结束状态，不可出征/移动/增援
	public static boolean canMove(int state) {
		return !(BATTLE_STATE_SURROUND_END == state || BATTLE_STATE_SURROUND == state);
	}
	
}
