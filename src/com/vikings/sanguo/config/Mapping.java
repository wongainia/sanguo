package com.vikings.sanguo.config;

import java.util.HashMap;
import java.util.TreeMap;

public class Mapping {

	public static TreeMap<Integer, String> cfgMapping = new TreeMap<Integer, String>();

	public static HashMap<Integer, Integer[]> div = new HashMap<Integer, Integer[]>();

	/**
	 * 默认强制更新的配置
	 */
	public static int[] default_update_cfg = { 65 };

	public static boolean isDefaultUpdate(String str) {
		int i = 0;
		try {
			i = Integer.parseInt(str);
		} catch (Exception e) {
			return false;
		}
		for (int j = 0; j < default_update_cfg.length; j++) {
			if (default_update_cfg[j] == i)
				return true;
			if (default_update_cfg[j] == i / 10000)
				return true;
		}
		return false;
	}

	static {
		cfgMapping.put(1, "dict.csv");
		cfgMapping.put(4, "tips.csv");
		cfgMapping.put(5, "prop_item.csv");
		cfgMapping.put(7, "prop_user.csv");
		cfgMapping.put(8, "errcode.csv");
		cfgMapping.put(9, "shop.csv");
		cfgMapping.put(10, "levelup_desc.csv");
		cfgMapping.put(11, "prop_city.csv");
		cfgMapping.put(12, "prop_machine.csv");
		cfgMapping.put(13, "manor_draft.csv");
		cfgMapping.put(14, "building_prop.csv");
		cfgMapping.put(15, "building_type.csv");
		cfgMapping.put(16, "building_buy_cost.csv");
		cfgMapping.put(17, "manor_scale.csv");
		cfgMapping.put(20, "prop_troop.csv");
		cfgMapping.put(22, "prop_troop_name.csv");
		cfgMapping.put(23, "hero_levelup.csv");
		cfgMapping.put(24, "hero_quality.csv");
		cfgMapping.put(26, "prop_hero.csv");
		cfgMapping.put(27, "province_hero.csv");
		cfgMapping.put(29, "hero_troop_name.csv");
		cfgMapping.put(30, "hero_skill_upgrade.csv");
		cfgMapping.put(31, "hero_common_config.csv");
		cfgMapping.put(33, "battle_skill.csv");
		cfgMapping.put(35, "poker_config.csv");
		cfgMapping.put(36, "poker_price.csv");
		cfgMapping.put(40, "npc_client_prop.csv");
		cfgMapping.put(42, "holy_prop.csv");
		cfgMapping.put(43, "holy_troop.csv");
		cfgMapping.put(44, "prop_quest.csv");
		cfgMapping.put(45, "quest_condition.csv");
		cfgMapping.put(46, "guild_prop.csv");
		cfgMapping.put(47, "guild_level_up_material.csv");
		cfgMapping.put(48, "guild_common_config.csv");
		cfgMapping.put(49, "prop_fief.csv");
		cfgMapping.put(50, "building_requirement.csv");

		cfgMapping.put(51, "prop_act.csv");
		cfgMapping.put(52, "prop_act_spoils.csv");
		cfgMapping.put(53, "prop_act_type.csv");
		cfgMapping.put(54, "prop_campaign.csv");
		cfgMapping.put(55, "prop_campaign_boss.csv");
		cfgMapping.put(58, "prop_campaign_mode.csv");
		cfgMapping.put(59, "prop_campaign_spoils.csv");
		cfgMapping.put(60, "prop_campaign_troop.csv");
		cfgMapping.put(61, "prop_time_condition.csv");

		cfgMapping.put(62, "manor_draft_resource.csv");
		cfgMapping.put(63, "prop_country.csv");
		cfgMapping.put(64, "prop_zone_client.csv");
		cfgMapping.put(65, "holy_fief.csv");
		cfgMapping.put(66, "building_effect.csv");
		cfgMapping.put(67, "building_store.csv");
		cfgMapping.put(68, "prop_battle_bg.csv");
		cfgMapping.put(69, "hero_type.csv");
		cfgMapping.put(70, "hero_devour_exp.csv");
		cfgMapping.put(71, "hero_evolve.csv");
		cfgMapping.put(72, "quest_effect.csv");

		cfgMapping.put(73, "arm_enhance_prop.csv");
		cfgMapping.put(74, "arm_enhance_cost.csv");
		cfgMapping.put(75, "building_status.csv");
		cfgMapping.put(76, "checkin_rewards.csv");
		cfgMapping.put(77, "prop_weight.csv");
		cfgMapping.put(78, "user_vip.csv");
		cfgMapping.put(79, "hero_recruit_exchange.csv");
		cfgMapping.put(80, "prop_bonus_client.csv");
		cfgMapping.put(81, "first_recharge_reward.csv");
		cfgMapping.put(82, "battle_buff.csv");
		cfgMapping.put(83, "manor_location.csv");
		cfgMapping.put(84, "prop_troop_desc.csv");
		cfgMapping.put(85, "hero_enhance.csv");
		cfgMapping.put(86, "prop_campaign_spoils_append.csv");
		cfgMapping.put(87, "username_random.csv");
		cfgMapping.put(89, "plunder.csv");
		cfgMapping.put(92, "battle_fief.csv");
		cfgMapping.put(93, "hero_evolve_discount.csv");
		cfgMapping.put(94, "prop_time.csv");
		cfgMapping.put(96, "shop_daily_discount.csv");
		cfgMapping.put(97, "shop_time_discount.csv");
		cfgMapping.put(98, "event_entry.csv");
		cfgMapping.put(100, "prop_ui_text.csv");
		cfgMapping.put(101, "honor_rank.csv");
		cfgMapping.put(102, "prop_rank.csv");
		cfgMapping.put(103, "recharge_state.csv");
		cfgMapping.put(104, "poker_reinforce.csv");
		cfgMapping.put(106, "holy_category.csv");
		cfgMapping.put(107, "site_special.csv");
		cfgMapping.put(116, "arena_reward.csv");
		cfgMapping.put(117, "arena_troop.csv");
		cfgMapping.put(118, "arena_npc.csv");
		cfgMapping.put(119, "arena_reward_reset_time.csv");
		cfgMapping.put(120, "manor_revive.csv");
		cfgMapping.put(122, "hero_init.csv");
		cfgMapping.put(124, "hero_abandon_exp_to_item.csv");
		cfgMapping.put(125, "equipment_desc.csv");
		cfgMapping.put(126, "prop_equipment.csv");
		cfgMapping.put(127, "equipment_type.csv");
		cfgMapping.put(128, "equipment_init.csv");
		cfgMapping.put(129, "equipment_effect.csv");
		cfgMapping.put(130, "equipment_levelup.csv");
		cfgMapping.put(131, "equipment_common_config.csv");
		cfgMapping.put(132, "equipment_forge.csv");
		cfgMapping.put(133, "equipment_insert_item.csv");
		cfgMapping.put(134, "equipment_insert_item_effect.csv");
		cfgMapping.put(135, "prop_hero_favour.csv");
		cfgMapping.put(136, "hero_favour_words.csv");
		cfgMapping.put(137, "blood_common.csv");
		cfgMapping.put(138, "blood_rank_reward.csv");
		cfgMapping.put(139, "blood_reward.csv");
		cfgMapping.put(140, "blood_troop.csv");
		cfgMapping.put(141, "prop_box.csv");
		cfgMapping.put(142, "skill_ani_effect.csv");
		cfgMapping.put(143, "battle_skill_combo.csv");
		cfgMapping.put(144, "event_rewards.csv");
		cfgMapping.put(145, "prop_roulette.csv");
		cfgMapping.put(146, "prop_roulette_common.csv");
		cfgMapping.put(147, "currency_box.csv");
		cfgMapping.put(148, "prop_stronger.csv");
		cfgMapping.put(149, "prop_fief_blank.csv");
		cfgMapping.put(150, "prop_roulette_item.csv");
		cfgMapping.put(151, "battle_skill_equipment.csv");
		cfgMapping.put(152, "vip_rewards.csv");
		cfgMapping.put(153, "worldlevel_prop.csv");
		cfgMapping.put(154, "user_common.csv");
		cfgMapping.put(155, "charge_common_config.csv");
		cfgMapping.put(156, "double_charge.csv");
		cfgMapping.put(157, "training_rewards.csv");

		
		

		cfgMapping.put(158, "battle_effect.csv");
		cfgMapping.put(159, "battle_skill_effect.csv");
		cfgMapping.put(160, "battle_buff_effect.csv");
		cfgMapping.put(161, "equipment_forge_effect.csv");

		cfgMapping.put(162, "hero_favour.csv");
		cfgMapping.put(163, "hero_favour_convert.csv");

	}

	public static int getNum(String name) {
		for (Integer i : cfgMapping.keySet()) {
			if (cfgMapping.get(i).equals(name))
				return i;
		}
		return 0;
	}

}
