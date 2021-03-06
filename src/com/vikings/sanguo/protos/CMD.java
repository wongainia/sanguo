// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

package com.vikings.sanguo.protos;

public enum CMD implements com.dyuproject.protostuff.EnumLite<CMD>
{
    MSG_RSP_REQUEST(1),
    MSG_ROUTE(2),
    MSG_CLIENT_RESET(3),
    MSG_SERVER_RESET(4),
    MSG_SERVER_ROLE(5),
    MSG_COMPRESSED(6),
    MSG_PING_REQ(7),
    MSG_PING_RSP(8),
    MSG_REQ_SERVER_WHERE(9),
    MSG_RSP_SERVER_WHERE(10),
    MSG_REQ_HEARTBEAT(100),
    MSG_RSP_HEARTBEAT(101),
    MSG_REQ_USER_DATA_SYN(102),
    MSG_RSP_USER_DATA_SYN(103),
    MSG_REQ_TRAY_DATA_SYN(104),
    MSG_RSP_TRAY_DATA_SYN(105),
    MSG_REQ_REGISTER(150),
    MSG_RSP_REGISTER(151),
    MSG_REQ_LOGIN(152),
    MSG_RSP_LOGIN(153),
    MSG_REQ_LOGOUT(154),
    MSG_RSP_LOGOUT(155),
    MSG_REQ_ACCOUNT_QUERY(156),
    MSG_RSP_ACCOUNT_QUERY(157),
    MSG_REQ_ACCOUNT_BIND(158),
    MSG_RSP_ACCOUNT_BIND(159),
    MSG_REQ_CHANNEL_ACTIVATE(164),
    MSG_RSP_CHANNEL_ACTIVATE(165),
    MSG_REQ_ACCOUNT_BIND2(174),
    MSG_RSP_ACCOUNT_BIND2(175),
    MSG_REQ_GAME_ENTER(176),
    MSG_RSP_GAME_ENTER(177),
    MSG_REQ_USER_CHECKIN(178),
    MSG_RSP_USER_CHECKIN(179),
    MSG_REQ_ACCOUNT_RESTORE(160),
    MSG_RSP_ACCOUNT_RESTORE(161),
    MSG_REQ_ACCOUNT_RESTORE2(162),
    MSG_RSP_ACCOUNT_RESTORE2(163),
    MSG_REQ_ACCOUNT_RESTORE3(180),
    MSG_RSP_ACCOUNT_RESTORE3(181),
    MSG_REQ_PLAYER_UPDATE(200),
    MSG_RSP_PLAYER_UPDATE(201),
    MSG_REQ_TRAINING_COMPLETE2(214),
    MSG_RSP_TRAINING_COMPLETE2(215),
    MSG_REQ_USER_STATUS_UPDATE(216),
    MSG_RSP_USER_STATUS_UPDATE(217),
    MSG_REQ_FRIEND_ADD(250),
    MSG_RSP_FRIEND_ADD(251),
    MSG_REQ_FRIEND_DEL(252),
    MSG_RSP_FRIEND_DEL(253),
    MSG_REQ_MESSAGE_POST(280),
    MSG_RSP_MESSAGE_POST(281),
    MSG_REQ_PLAYER_WANTED(282),
    MSG_RSP_PLAYER_WANTED(283),
    MSG_REQ_ITEM_BUY(300),
    MSG_RSP_ITEM_BUY(301),
    MSG_REQ_ITEM_SELL(302),
    MSG_RSP_ITEM_SELL(303),
    MSG_REQ_ITEM_USE(304),
    MSG_RSP_ITEM_USE(305),
    MSG_REQ_MANOR_DRAFT(350),
    MSG_RSP_MANOR_DRAFT(351),
    MSG_REQ_MANOR_POP_RECOVER(352),
    MSG_RSP_MANOR_POP_RECOVER(353),
    MSG_REQ_MANOR_BUILDING_BUY(362),
    MSG_RSP_MANOR_BUILDING_BUY(363),
    MSG_REQ_MANOR_RECEIVE(364),
    MSG_RSP_MANOR_RECEIVE(365),
    MSG_REQ_MANOR_MOVE_TROOP(366),
    MSG_RSP_MANOR_MOVE_TROOP(367),
    MSG_REQ_MANOR_RECEIVE_ALL(368),
    MSG_RSP_MANOR_RECEIVE_ALL(369),
    MSG_REQ_USER_TROOP_COST_UPDATE(370),
    MSG_RSP_USER_TROOP_COST_UPDATE(371),
    MSG_REQ_QUERY_EMPTY_ZONE(386),
    MSG_RSP_QUERY_EMPTY_ZONE(387),
    MSG_REQ_MANOR_LAY_DOWN(388),
    MSG_RSP_MANOR_LAY_DOWN(389),
    MSG_REQ_MANOR_RANDOM_MOVE(390),
    MSG_RSP_MANOR_RANDOM_MOVE(391),
    MSG_REQ_BUILDING_RESET(392),
    MSG_RSP_BUILDING_RESET(393),
    MSG_REQ_HOT_USER_SCORE_INFO_QUERY(394),
    MSG_RSP_HOT_USER_SCORE_INFO_QUERY(395),
    MSG_REQ_MANOR_WEAK_REMOVE(396),
    MSG_RSP_MANOR_WEAK_REMOVE(397),
    MSG_REQ_MANOR_REVIVE(398),
    MSG_RSP_MANOR_REVIVE(399),
    MSG_REQ_MANOR_REVIVE_CLEAN(400),
    MSG_RSP_MANOR_REVIVE_CLEAN(401),
    MSG_REQ_USER_SEARCH(520),
    MSG_RSP_USER_SEARCH(521),
    MSG_REQ_USER_STAMINA_RESET(522),
    MSG_RSP_USER_STAMINA_RESET(523),
    MSG_REQ_USER_RANK_INFO(700),
    MSG_RSP_USER_RANK_INFO(701),
    MSG_REQ_USER_STAT_DATA2(704),
    MSG_RSP_USER_STAT_DATA2(705),
    MSG_REQ_MACHINE_PLAY_STAT_DATA(706),
    MSG_RSP_MACHINE_PLAY_STAT_DATA(707),
    MSG_REQ_BATTLE_HOT_INFO(710),
    MSG_RSP_BATTLE_HOT_INFO(711),
    MSG_REQ_HONOR_RANK_INFO(712),
    MSG_RSP_HONOR_RANK_INFO(713),
    MSG_REQ_HERO_RANK_INFO(714),
    MSG_RSP_HERO_RANK_INFO(715),
    MSG_REQ_RANK_REWARD(716),
    MSG_RSP_RANK_REWARD(717),
    MSG_REQ_QUEST_FINISH(750),
    MSG_RSP_QUEST_FINISH(751),
    MSG_REQ_BLACKLIST_ADD(800),
    MSG_RSP_BLACKLIST_ADD(801),
    MSG_REQ_BLACKLIST_DEL(802),
    MSG_RSP_BLACKLIST_DEL(803),
    MSG_REQ_BLACKLIST_CHECK(804),
    MSG_RSP_BLACKLIST_CHECK(805),
    MSG_REQ_STATIC_USER_DATA_QUERY(850),
    MSG_RSP_STATIC_USER_DATA_QUERY(851),
    MSG_REQ_PLAYER_STATIC_USER_DATA_QUERY(852),
    MSG_RSP_PLAYER_STATIC_USER_DATA_QUERY(853),
    MSG_REQ_STATIC_GUILD_DATA_QUERY(856),
    MSG_RSP_STATIC_GUILD_DATA_QUERY(857),
    MSG_REQ_PLAYER_WANTED_INFO_QUERY(858),
    MSG_RSP_PLAYER_WANTED_INFO_QUERY(859),
    MSG_REQ_ADVANCED_SITE_QUERY(860),
    MSG_RSP_ADVANCED_SITE_QUERY(861),
    MSG_REQ_MACHINE_PLAY(870),
    MSG_RSP_MACHINE_PLAY(871),
    MSG_REQ_ROULETTE(872),
    MSG_RSP_ROULETTE(873),
    MSG_REQ_ROULETTE_SACRIFICE(874),
    MSG_RSP_ROULETTE_SACRIFICE(875),
    MSG_REQ_CURRENCY_MACHINE_PLAY(876),
    MSG_RSP_CURRENCY_MACHINE_PLAY(877),
    MSG_REQ_RICH_OTHER_USER_INFO_QUERY(880),
    MSG_RSP_RICH_OTHER_USER_INFO_QUERY(881),
    MSG_REQ_BRIEF_USER_INFO_QUERY(882),
    MSG_RSP_BRIEF_USER_INFO_QUERY(883),
    MSG_REQ_FIEF_RECEIVE(1001),
    MSG_RSP_FIEF_RECEIVE(1002),
    MSG_REQ_FIEF_BUILDING_BUY(1003),
    MSG_RSP_FIEF_BUILDING_BUY(1004),
    MSG_REQ_FIEF_QUERY_BRIEF(1005),
    MSG_RSP_FIEF_QUERY_BRIEF(1006),
    MSG_REQ_FIEF_MOVE_TROOP(1007),
    MSG_RSP_FIEF_MOVE_TROOP(1008),
    MSG_REQ_FIEF_ABANDON(1009),
    MSG_RSP_FIEF_ABANDON(1010),
    MSG_REQ_SELF_FIEF_INFO_QUERY(1011),
    MSG_RSP_SELF_FIEF_INFO_QUERY(1012),
    MSG_REQ_OTHER_FIEF_INFO_QUERY(1013),
    MSG_RSP_OTHER_FIEF_INFO_QUERY(1014),
    MSG_REQ_FIEF_DATA_SYN(1017),
    MSG_RSP_FIEF_DATA_SYN(1018),
    MSG_REQ_FAVORITE_FIEF_ADD(1027),
    MSG_RSP_FAVORITE_FIEF_ADD(1028),
    MSG_REQ_FAVORITE_FIEF_DEL(1029),
    MSG_RSP_FAVORITE_FIEF_DEL(1030),
    MSG_REQ_FAVORITE_FIEF_QUERY(1031),
    MSG_RSP_FAVORITE_FIEF_QUERY(1032),
    MSG_REQ_FIEF_HERO_SELECT(1033),
    MSG_RSP_FIEF_HERO_SELECT(1034),
    MSG_REQ_FIEF_FIGHT_NPC(1049),
    MSG_RSP_FIEF_FIGHT_NPC(1050),
    MSG_REQ_LORD_FIEFID_QUERY(1053),
    MSG_RSP_LORD_FIEFID_QUERY(1054),
    MSG_REQ_OTHER_LORD_TROOP_INFO_QUERY(1059),
    MSG_RSP_OTHER_LORD_TROOP_INFO_QUERY(1060),
    MSG_REQ_BATTLE_BUY_UNIT(1080),
    MSG_RSP_BATTLE_BUY_UNIT(1081),
    MSG_REQ_RICH_BATTLE_INFO_QUERY(1082),
    MSG_RSP_RICH_BATTLE_INFO_QUERY(1083),
    MSG_REQ_BATTLE_REINFOR(1084),
    MSG_RSP_BATTLE_REINFOR(1085),
    MSG_REQ_BATTLE_DIVINE(1086),
    MSG_RSP_BATTLE_DIVINE(1087),
    MSG_REQ_BATTLE_ATTACK(1088),
    MSG_RSP_BATTLE_ATTACK(1089),
    MSG_REQ_DUNGEON_ATTACK(1090),
    MSG_RSP_DUNGEON_ATTACK(1091),
    MSG_REQ_BRIEF_BATTLE_INFO_QUERY(1092),
    MSG_RSP_BRIEF_BATTLE_INFO_QUERY(1093),
    MSG_REQ_BATTLE_CONFIRM_BUY_UNIT(1094),
    MSG_RSP_BATTLE_CONFIRM_BUY_UNIT(1095),
    MSG_REQ_BATTLE_LOG_INFO_QUERY(1096),
    MSG_RSP_BATTLE_LOG_INFO_QUERY(1097),
    MSG_REQ_BRIEF_BATTLE_INFO_QUERY_BY_FIEFID(1098),
    MSG_RSP_BRIEF_BATTLE_INFO_QUERY_BY_FIEFID(1099),
    MSG_REQ_USER_BATTLE_INFO_QUERY(1100),
    MSG_RSP_USER_BATTLE_INFO_QUERY(1101),
    MSG_REQ_BATTLE_RISE_TROOP(1102),
    MSG_RSP_BATTLE_RISE_TROOP(1103),
    MSG_REQ_PLUNDER_ATTACK(1104),
    MSG_RSP_PLUNDER_ATTACK(1105),
    MSG_REQ_DUNGEON_RESET(1106),
    MSG_RSP_DUNGEON_RESET(1107),
    MSG_REQ_DUNGEON_DIVINE(1108),
    MSG_RSP_DUNGEON_DIVINE(1109),
    MSG_REQ_DUNGEON_REWARD(1110),
    MSG_RSP_DUNGEON_REWARD(1111),
    MSG_REQ_DUNGEON_CLEAR(1112),
    MSG_RSP_DUNGEON_CLEAR(1113),
    MSG_REQ_DUNGEON_ACT_RESET(1114),
    MSG_RSP_DUNGEON_ACT_RESET(1115),
    MSG_REQ_ARM_ENHANCE(1130),
    MSG_RSP_ARM_ENHANCE(1131),
    MSG_REQ_DUEL_ATTACK(1132),
    MSG_RSP_DUEL_ATTACK(1133),
    MSG_REQ_REINFORCE_BUY_UNIT(1134),
    MSG_RSP_REINFORCE_BUY_UNIT(1135),
    MSG_REQ_QUERY_HOLY_BATTLE_STATE(1136),
    MSG_RSP_QUERY_HOLY_BATTLE_STATE(1137),
    MSG_REQ_GUILD_BUILD(1201),
    MSG_RSP_GUILD_BUILD(1202),
    MSG_REQ_GUILD_INFO_UPDATE(1203),
    MSG_RSP_GUILD_INFO_UPDATE(1204),
    MSG_REQ_GUILD_SEARCH(1205),
    MSG_RSP_GUILD_SEARCH(1206),
    MSG_REQ_RICH_GUILD_INFO_QUERY(1207),
    MSG_RSP_RICH_GUILD_INFO_QUERY(1208),
    MSG_REQ_OTHER_RICH_GUILD_INFO_QUERY(1209),
    MSG_RSP_OTHER_RICH_GUILD_INFO_QUERY(1210),
    MSG_REQ_GUILD_INVITE_ASK(1211),
    MSG_RSP_GUILD_INVITE_ASK(1212),
    MSG_REQ_GUILD_INVITE_APPROVE(1213),
    MSG_RSP_GUILD_INVITE_APPROVE(1214),
    MSG_REQ_GUILD_INVITE_REMOVE(1215),
    MSG_RSP_GUILD_INVITE_REMOVE(1216),
    MSG_REQ_GUILD_MEMBER_QUIT(1217),
    MSG_RSP_GUILD_MEMBER_QUIT(1218),
    MSG_REQ_GUILD_MEMBER_REMOVE(1219),
    MSG_RSP_GUILD_MEMBER_REMOVE(1220),
    MSG_REQ_GUILD_LEADER_ASSIGN(1221),
    MSG_RSP_GUILD_LEADER_ASSIGN(1222),
    MSG_REQ_BRIEF_GUILD_INFO_QUERY(1223),
    MSG_RSP_BRIEF_GUILD_INFO_QUERY(1224),
    MSG_REQ_RICH_GUILD_VERSION_QUERY(1225),
    MSG_RSP_RICH_GUILD_VERSION_QUERY(1226),
    MSG_REQ_GUILD_JOIN_ASK(1227),
    MSG_RSP_GUILD_JOIN_ASK(1228),
    MSG_REQ_GUILD_JOIN_APPROVE(1229),
    MSG_RSP_GUILD_JOIN_APPROVE(1230),
    MSG_REQ_GUILD_POSITION_ASSIGN(1231),
    MSG_RSP_GUILD_POSITION_ASSIGN(1232),
    MSG_REQ_GUILD_BUY_ALTAR(1233),
    MSG_RSP_GUILD_BUY_ALTAR(1234),
    MSG_REQ_GUILD_LEVELUP(1235),
    MSG_RSP_GUILD_LEVELUP(1236),
    MSG_REQ_ARENA_ATTACK(1271),
    MSG_RSP_ARENA_ATTACK(1272),
    MSG_REQ_ARENA_CONF(1273),
    MSG_RSP_ARENA_CONF(1274),
    MSG_REQ_ARENA_REWARD(1275),
    MSG_RSP_ARENA_REWARD(1276),
    MSG_REQ_ARENA_QUERY(1277),
    MSG_RSP_ARENA_QUERY(1278),
    MSG_REQ_HERO_REFRESH(1301),
    MSG_RSP_HERO_REFRESH(1302),
    MSG_REQ_HERO_BUY(1303),
    MSG_RSP_HERO_BUY(1304),
    MSG_REQ_OTHER_USER_HERO_INFO_QUERY(1305),
    MSG_RSP_OTHER_USER_HERO_INFO_QUERY(1306),
    MSG_REQ_HERO_ENHANCE(1307),
    MSG_RSP_HERO_ENHANCE(1308),
    MSG_REQ_HERO_SKILL_STUDY(1311),
    MSG_RSP_HERO_SKILL_STUDY(1312),
    MSG_REQ_HERO_SKILL_ABANDON(1313),
    MSG_RSP_HERO_SKILL_ABANDON(1314),
    MSG_REQ_HERO_STAMINA_RECOVERY(1315),
    MSG_RSP_HERO_STAMINA_RECOVERY(1316),
    MSG_REQ_HERO_ABANDON(1317),
    MSG_RSP_HERO_ABANDON(1318),
    MSG_REQ_HERO_DEVOUR(1319),
    MSG_RSP_HERO_DEVOUR(1320),
    MSG_REQ_HERO_EVOLVE(1321),
    MSG_RSP_HERO_EVOLVE(1322),
    MSG_REQ_HERO_FAVOUR(1323),
    MSG_RSP_HERO_FAVOUR(1324),
    MSG_REQ_HERO_EXCHANGE(1325),
    MSG_RSP_HERO_EXCHANGE(1326),
    MSG_REQ_EQUIPMENT_BUY(1401),
    MSG_RSP_EQUIPMENT_BUY(1402),
    MSG_REQ_EQUIPMENT_REPLACE(1403),
    MSG_RSP_EQUIPMENT_REPLACE(1404),
    MSG_REQ_EQUIPMENT_DISARM(1405),
    MSG_RSP_EQUIPMENT_DISARM(1406),
    MSG_REQ_EQUIPMENT_SELL(1407),
    MSG_RSP_EQUIPMENT_SELL(1408),
    MSG_REQ_EQUIPMENT_ITEM_INSERT(1409),
    MSG_RSP_EQUIPMENT_ITEM_INSERT(1410),
    MSG_REQ_EQUIPMENT_ITEM_REMOVE(1411),
    MSG_RSP_EQUIPMENT_ITEM_REMOVE(1412),
    MSG_REQ_EQUIPMENT_FORGE(1413),
    MSG_RSP_EQUIPMENT_FORGE(1414),
    MSG_REQ_EQUIPMENT_LEVELUP(1415),
    MSG_RSP_EQUIPMENT_LEVELUP(1416),
    MSG_REQ_EQUIPMENT_INSERT_ITEM_LEVELUP(1417),
    MSG_RSP_EQUIPMENT_INSERT_ITEM_LEVELUP(1418),
    MSG_REQ_BLOOD_ATTACK(1500),
    MSG_RSP_BLOOD_ATTACK(1501),
    MSG_REQ_BLOOD_CONF(1502),
    MSG_RSP_BLOOD_CONF(1503),
    MSG_REQ_BLOOD_REWARD(1504),
    MSG_RSP_BLOOD_REWARD(1505),
    MSG_REQ_BLOOD_POKER(1506),
    MSG_RSP_BLOOD_POKER(1507),
    MSG_REQ_BLOOD_RANK_QUERY(1508),
    MSG_RSP_BLOOD_RANK_QUERY(1509),
    MSG_REQ_BLOOD_RANK_REWARD(1510),
    MSG_RSP_BLOOD_RANK_REWARD(1511),
    MSG_REQ_CHARGE_MONTH_REWARD(1550),
    MSG_RSP_CHARGE_MONTH_REWARD(1551),
    MSG_REQ_CURRENCY_BOX_OPEN(1552),
    MSG_RSP_CURRENCY_BOX_OPEN(1553),
    MSG_REQ_GET_HELP_ARM(1600),
    MSG_RSP_GET_HELP_ARM(1601);
    
    public final int number;
    
    private CMD (int number)
    {
        this.number = number;
    }
    
    public int getNumber()
    {
        return number;
    }
    
    public static CMD valueOf(int number)
    {
        switch(number) 
        {
            case 1: return MSG_RSP_REQUEST;
            case 2: return MSG_ROUTE;
            case 3: return MSG_CLIENT_RESET;
            case 4: return MSG_SERVER_RESET;
            case 5: return MSG_SERVER_ROLE;
            case 6: return MSG_COMPRESSED;
            case 7: return MSG_PING_REQ;
            case 8: return MSG_PING_RSP;
            case 9: return MSG_REQ_SERVER_WHERE;
            case 10: return MSG_RSP_SERVER_WHERE;
            case 100: return MSG_REQ_HEARTBEAT;
            case 101: return MSG_RSP_HEARTBEAT;
            case 102: return MSG_REQ_USER_DATA_SYN;
            case 103: return MSG_RSP_USER_DATA_SYN;
            case 104: return MSG_REQ_TRAY_DATA_SYN;
            case 105: return MSG_RSP_TRAY_DATA_SYN;
            case 150: return MSG_REQ_REGISTER;
            case 151: return MSG_RSP_REGISTER;
            case 152: return MSG_REQ_LOGIN;
            case 153: return MSG_RSP_LOGIN;
            case 154: return MSG_REQ_LOGOUT;
            case 155: return MSG_RSP_LOGOUT;
            case 156: return MSG_REQ_ACCOUNT_QUERY;
            case 157: return MSG_RSP_ACCOUNT_QUERY;
            case 158: return MSG_REQ_ACCOUNT_BIND;
            case 159: return MSG_RSP_ACCOUNT_BIND;
            case 160: return MSG_REQ_ACCOUNT_RESTORE;
            case 161: return MSG_RSP_ACCOUNT_RESTORE;
            case 162: return MSG_REQ_ACCOUNT_RESTORE2;
            case 163: return MSG_RSP_ACCOUNT_RESTORE2;
            case 164: return MSG_REQ_CHANNEL_ACTIVATE;
            case 165: return MSG_RSP_CHANNEL_ACTIVATE;
            case 174: return MSG_REQ_ACCOUNT_BIND2;
            case 175: return MSG_RSP_ACCOUNT_BIND2;
            case 176: return MSG_REQ_GAME_ENTER;
            case 177: return MSG_RSP_GAME_ENTER;
            case 178: return MSG_REQ_USER_CHECKIN;
            case 179: return MSG_RSP_USER_CHECKIN;
            case 180: return MSG_REQ_ACCOUNT_RESTORE3;
            case 181: return MSG_RSP_ACCOUNT_RESTORE3;
            case 200: return MSG_REQ_PLAYER_UPDATE;
            case 201: return MSG_RSP_PLAYER_UPDATE;
            case 214: return MSG_REQ_TRAINING_COMPLETE2;
            case 215: return MSG_RSP_TRAINING_COMPLETE2;
            case 216: return MSG_REQ_USER_STATUS_UPDATE;
            case 217: return MSG_RSP_USER_STATUS_UPDATE;
            case 250: return MSG_REQ_FRIEND_ADD;
            case 251: return MSG_RSP_FRIEND_ADD;
            case 252: return MSG_REQ_FRIEND_DEL;
            case 253: return MSG_RSP_FRIEND_DEL;
            case 280: return MSG_REQ_MESSAGE_POST;
            case 281: return MSG_RSP_MESSAGE_POST;
            case 282: return MSG_REQ_PLAYER_WANTED;
            case 283: return MSG_RSP_PLAYER_WANTED;
            case 300: return MSG_REQ_ITEM_BUY;
            case 301: return MSG_RSP_ITEM_BUY;
            case 302: return MSG_REQ_ITEM_SELL;
            case 303: return MSG_RSP_ITEM_SELL;
            case 304: return MSG_REQ_ITEM_USE;
            case 305: return MSG_RSP_ITEM_USE;
            case 350: return MSG_REQ_MANOR_DRAFT;
            case 351: return MSG_RSP_MANOR_DRAFT;
            case 352: return MSG_REQ_MANOR_POP_RECOVER;
            case 353: return MSG_RSP_MANOR_POP_RECOVER;
            case 362: return MSG_REQ_MANOR_BUILDING_BUY;
            case 363: return MSG_RSP_MANOR_BUILDING_BUY;
            case 364: return MSG_REQ_MANOR_RECEIVE;
            case 365: return MSG_RSP_MANOR_RECEIVE;
            case 366: return MSG_REQ_MANOR_MOVE_TROOP;
            case 367: return MSG_RSP_MANOR_MOVE_TROOP;
            case 368: return MSG_REQ_MANOR_RECEIVE_ALL;
            case 369: return MSG_RSP_MANOR_RECEIVE_ALL;
            case 370: return MSG_REQ_USER_TROOP_COST_UPDATE;
            case 371: return MSG_RSP_USER_TROOP_COST_UPDATE;
            case 386: return MSG_REQ_QUERY_EMPTY_ZONE;
            case 387: return MSG_RSP_QUERY_EMPTY_ZONE;
            case 388: return MSG_REQ_MANOR_LAY_DOWN;
            case 389: return MSG_RSP_MANOR_LAY_DOWN;
            case 390: return MSG_REQ_MANOR_RANDOM_MOVE;
            case 391: return MSG_RSP_MANOR_RANDOM_MOVE;
            case 392: return MSG_REQ_BUILDING_RESET;
            case 393: return MSG_RSP_BUILDING_RESET;
            case 394: return MSG_REQ_HOT_USER_SCORE_INFO_QUERY;
            case 395: return MSG_RSP_HOT_USER_SCORE_INFO_QUERY;
            case 396: return MSG_REQ_MANOR_WEAK_REMOVE;
            case 397: return MSG_RSP_MANOR_WEAK_REMOVE;
            case 398: return MSG_REQ_MANOR_REVIVE;
            case 399: return MSG_RSP_MANOR_REVIVE;
            case 400: return MSG_REQ_MANOR_REVIVE_CLEAN;
            case 401: return MSG_RSP_MANOR_REVIVE_CLEAN;
            case 520: return MSG_REQ_USER_SEARCH;
            case 521: return MSG_RSP_USER_SEARCH;
            case 522: return MSG_REQ_USER_STAMINA_RESET;
            case 523: return MSG_RSP_USER_STAMINA_RESET;
            case 700: return MSG_REQ_USER_RANK_INFO;
            case 701: return MSG_RSP_USER_RANK_INFO;
            case 704: return MSG_REQ_USER_STAT_DATA2;
            case 705: return MSG_RSP_USER_STAT_DATA2;
            case 706: return MSG_REQ_MACHINE_PLAY_STAT_DATA;
            case 707: return MSG_RSP_MACHINE_PLAY_STAT_DATA;
            case 710: return MSG_REQ_BATTLE_HOT_INFO;
            case 711: return MSG_RSP_BATTLE_HOT_INFO;
            case 712: return MSG_REQ_HONOR_RANK_INFO;
            case 713: return MSG_RSP_HONOR_RANK_INFO;
            case 714: return MSG_REQ_HERO_RANK_INFO;
            case 715: return MSG_RSP_HERO_RANK_INFO;
            case 716: return MSG_REQ_RANK_REWARD;
            case 717: return MSG_RSP_RANK_REWARD;
            case 750: return MSG_REQ_QUEST_FINISH;
            case 751: return MSG_RSP_QUEST_FINISH;
            case 800: return MSG_REQ_BLACKLIST_ADD;
            case 801: return MSG_RSP_BLACKLIST_ADD;
            case 802: return MSG_REQ_BLACKLIST_DEL;
            case 803: return MSG_RSP_BLACKLIST_DEL;
            case 804: return MSG_REQ_BLACKLIST_CHECK;
            case 805: return MSG_RSP_BLACKLIST_CHECK;
            case 850: return MSG_REQ_STATIC_USER_DATA_QUERY;
            case 851: return MSG_RSP_STATIC_USER_DATA_QUERY;
            case 852: return MSG_REQ_PLAYER_STATIC_USER_DATA_QUERY;
            case 853: return MSG_RSP_PLAYER_STATIC_USER_DATA_QUERY;
            case 856: return MSG_REQ_STATIC_GUILD_DATA_QUERY;
            case 857: return MSG_RSP_STATIC_GUILD_DATA_QUERY;
            case 858: return MSG_REQ_PLAYER_WANTED_INFO_QUERY;
            case 859: return MSG_RSP_PLAYER_WANTED_INFO_QUERY;
            case 860: return MSG_REQ_ADVANCED_SITE_QUERY;
            case 861: return MSG_RSP_ADVANCED_SITE_QUERY;
            case 870: return MSG_REQ_MACHINE_PLAY;
            case 871: return MSG_RSP_MACHINE_PLAY;
            case 872: return MSG_REQ_ROULETTE;
            case 873: return MSG_RSP_ROULETTE;
            case 874: return MSG_REQ_ROULETTE_SACRIFICE;
            case 875: return MSG_RSP_ROULETTE_SACRIFICE;
            case 876: return MSG_REQ_CURRENCY_MACHINE_PLAY;
            case 877: return MSG_RSP_CURRENCY_MACHINE_PLAY;
            case 880: return MSG_REQ_RICH_OTHER_USER_INFO_QUERY;
            case 881: return MSG_RSP_RICH_OTHER_USER_INFO_QUERY;
            case 882: return MSG_REQ_BRIEF_USER_INFO_QUERY;
            case 883: return MSG_RSP_BRIEF_USER_INFO_QUERY;
            case 1001: return MSG_REQ_FIEF_RECEIVE;
            case 1002: return MSG_RSP_FIEF_RECEIVE;
            case 1003: return MSG_REQ_FIEF_BUILDING_BUY;
            case 1004: return MSG_RSP_FIEF_BUILDING_BUY;
            case 1005: return MSG_REQ_FIEF_QUERY_BRIEF;
            case 1006: return MSG_RSP_FIEF_QUERY_BRIEF;
            case 1007: return MSG_REQ_FIEF_MOVE_TROOP;
            case 1008: return MSG_RSP_FIEF_MOVE_TROOP;
            case 1009: return MSG_REQ_FIEF_ABANDON;
            case 1010: return MSG_RSP_FIEF_ABANDON;
            case 1011: return MSG_REQ_SELF_FIEF_INFO_QUERY;
            case 1012: return MSG_RSP_SELF_FIEF_INFO_QUERY;
            case 1013: return MSG_REQ_OTHER_FIEF_INFO_QUERY;
            case 1014: return MSG_RSP_OTHER_FIEF_INFO_QUERY;
            case 1017: return MSG_REQ_FIEF_DATA_SYN;
            case 1018: return MSG_RSP_FIEF_DATA_SYN;
            case 1027: return MSG_REQ_FAVORITE_FIEF_ADD;
            case 1028: return MSG_RSP_FAVORITE_FIEF_ADD;
            case 1029: return MSG_REQ_FAVORITE_FIEF_DEL;
            case 1030: return MSG_RSP_FAVORITE_FIEF_DEL;
            case 1031: return MSG_REQ_FAVORITE_FIEF_QUERY;
            case 1032: return MSG_RSP_FAVORITE_FIEF_QUERY;
            case 1033: return MSG_REQ_FIEF_HERO_SELECT;
            case 1034: return MSG_RSP_FIEF_HERO_SELECT;
            case 1049: return MSG_REQ_FIEF_FIGHT_NPC;
            case 1050: return MSG_RSP_FIEF_FIGHT_NPC;
            case 1053: return MSG_REQ_LORD_FIEFID_QUERY;
            case 1054: return MSG_RSP_LORD_FIEFID_QUERY;
            case 1059: return MSG_REQ_OTHER_LORD_TROOP_INFO_QUERY;
            case 1060: return MSG_RSP_OTHER_LORD_TROOP_INFO_QUERY;
            case 1080: return MSG_REQ_BATTLE_BUY_UNIT;
            case 1081: return MSG_RSP_BATTLE_BUY_UNIT;
            case 1082: return MSG_REQ_RICH_BATTLE_INFO_QUERY;
            case 1083: return MSG_RSP_RICH_BATTLE_INFO_QUERY;
            case 1084: return MSG_REQ_BATTLE_REINFOR;
            case 1085: return MSG_RSP_BATTLE_REINFOR;
            case 1086: return MSG_REQ_BATTLE_DIVINE;
            case 1087: return MSG_RSP_BATTLE_DIVINE;
            case 1088: return MSG_REQ_BATTLE_ATTACK;
            case 1089: return MSG_RSP_BATTLE_ATTACK;
            case 1090: return MSG_REQ_DUNGEON_ATTACK;
            case 1091: return MSG_RSP_DUNGEON_ATTACK;
            case 1092: return MSG_REQ_BRIEF_BATTLE_INFO_QUERY;
            case 1093: return MSG_RSP_BRIEF_BATTLE_INFO_QUERY;
            case 1094: return MSG_REQ_BATTLE_CONFIRM_BUY_UNIT;
            case 1095: return MSG_RSP_BATTLE_CONFIRM_BUY_UNIT;
            case 1096: return MSG_REQ_BATTLE_LOG_INFO_QUERY;
            case 1097: return MSG_RSP_BATTLE_LOG_INFO_QUERY;
            case 1098: return MSG_REQ_BRIEF_BATTLE_INFO_QUERY_BY_FIEFID;
            case 1099: return MSG_RSP_BRIEF_BATTLE_INFO_QUERY_BY_FIEFID;
            case 1100: return MSG_REQ_USER_BATTLE_INFO_QUERY;
            case 1101: return MSG_RSP_USER_BATTLE_INFO_QUERY;
            case 1102: return MSG_REQ_BATTLE_RISE_TROOP;
            case 1103: return MSG_RSP_BATTLE_RISE_TROOP;
            case 1104: return MSG_REQ_PLUNDER_ATTACK;
            case 1105: return MSG_RSP_PLUNDER_ATTACK;
            case 1106: return MSG_REQ_DUNGEON_RESET;
            case 1107: return MSG_RSP_DUNGEON_RESET;
            case 1108: return MSG_REQ_DUNGEON_DIVINE;
            case 1109: return MSG_RSP_DUNGEON_DIVINE;
            case 1110: return MSG_REQ_DUNGEON_REWARD;
            case 1111: return MSG_RSP_DUNGEON_REWARD;
            case 1112: return MSG_REQ_DUNGEON_CLEAR;
            case 1113: return MSG_RSP_DUNGEON_CLEAR;
            case 1114: return MSG_REQ_DUNGEON_ACT_RESET;
            case 1115: return MSG_RSP_DUNGEON_ACT_RESET;
            case 1130: return MSG_REQ_ARM_ENHANCE;
            case 1131: return MSG_RSP_ARM_ENHANCE;
            case 1132: return MSG_REQ_DUEL_ATTACK;
            case 1133: return MSG_RSP_DUEL_ATTACK;
            case 1134: return MSG_REQ_REINFORCE_BUY_UNIT;
            case 1135: return MSG_RSP_REINFORCE_BUY_UNIT;
            case 1136: return MSG_REQ_QUERY_HOLY_BATTLE_STATE;
            case 1137: return MSG_RSP_QUERY_HOLY_BATTLE_STATE;
            case 1201: return MSG_REQ_GUILD_BUILD;
            case 1202: return MSG_RSP_GUILD_BUILD;
            case 1203: return MSG_REQ_GUILD_INFO_UPDATE;
            case 1204: return MSG_RSP_GUILD_INFO_UPDATE;
            case 1205: return MSG_REQ_GUILD_SEARCH;
            case 1206: return MSG_RSP_GUILD_SEARCH;
            case 1207: return MSG_REQ_RICH_GUILD_INFO_QUERY;
            case 1208: return MSG_RSP_RICH_GUILD_INFO_QUERY;
            case 1209: return MSG_REQ_OTHER_RICH_GUILD_INFO_QUERY;
            case 1210: return MSG_RSP_OTHER_RICH_GUILD_INFO_QUERY;
            case 1211: return MSG_REQ_GUILD_INVITE_ASK;
            case 1212: return MSG_RSP_GUILD_INVITE_ASK;
            case 1213: return MSG_REQ_GUILD_INVITE_APPROVE;
            case 1214: return MSG_RSP_GUILD_INVITE_APPROVE;
            case 1215: return MSG_REQ_GUILD_INVITE_REMOVE;
            case 1216: return MSG_RSP_GUILD_INVITE_REMOVE;
            case 1217: return MSG_REQ_GUILD_MEMBER_QUIT;
            case 1218: return MSG_RSP_GUILD_MEMBER_QUIT;
            case 1219: return MSG_REQ_GUILD_MEMBER_REMOVE;
            case 1220: return MSG_RSP_GUILD_MEMBER_REMOVE;
            case 1221: return MSG_REQ_GUILD_LEADER_ASSIGN;
            case 1222: return MSG_RSP_GUILD_LEADER_ASSIGN;
            case 1223: return MSG_REQ_BRIEF_GUILD_INFO_QUERY;
            case 1224: return MSG_RSP_BRIEF_GUILD_INFO_QUERY;
            case 1225: return MSG_REQ_RICH_GUILD_VERSION_QUERY;
            case 1226: return MSG_RSP_RICH_GUILD_VERSION_QUERY;
            case 1227: return MSG_REQ_GUILD_JOIN_ASK;
            case 1228: return MSG_RSP_GUILD_JOIN_ASK;
            case 1229: return MSG_REQ_GUILD_JOIN_APPROVE;
            case 1230: return MSG_RSP_GUILD_JOIN_APPROVE;
            case 1231: return MSG_REQ_GUILD_POSITION_ASSIGN;
            case 1232: return MSG_RSP_GUILD_POSITION_ASSIGN;
            case 1233: return MSG_REQ_GUILD_BUY_ALTAR;
            case 1234: return MSG_RSP_GUILD_BUY_ALTAR;
            case 1235: return MSG_REQ_GUILD_LEVELUP;
            case 1236: return MSG_RSP_GUILD_LEVELUP;
            case 1271: return MSG_REQ_ARENA_ATTACK;
            case 1272: return MSG_RSP_ARENA_ATTACK;
            case 1273: return MSG_REQ_ARENA_CONF;
            case 1274: return MSG_RSP_ARENA_CONF;
            case 1275: return MSG_REQ_ARENA_REWARD;
            case 1276: return MSG_RSP_ARENA_REWARD;
            case 1277: return MSG_REQ_ARENA_QUERY;
            case 1278: return MSG_RSP_ARENA_QUERY;
            case 1301: return MSG_REQ_HERO_REFRESH;
            case 1302: return MSG_RSP_HERO_REFRESH;
            case 1303: return MSG_REQ_HERO_BUY;
            case 1304: return MSG_RSP_HERO_BUY;
            case 1305: return MSG_REQ_OTHER_USER_HERO_INFO_QUERY;
            case 1306: return MSG_RSP_OTHER_USER_HERO_INFO_QUERY;
            case 1307: return MSG_REQ_HERO_ENHANCE;
            case 1308: return MSG_RSP_HERO_ENHANCE;
            case 1311: return MSG_REQ_HERO_SKILL_STUDY;
            case 1312: return MSG_RSP_HERO_SKILL_STUDY;
            case 1313: return MSG_REQ_HERO_SKILL_ABANDON;
            case 1314: return MSG_RSP_HERO_SKILL_ABANDON;
            case 1315: return MSG_REQ_HERO_STAMINA_RECOVERY;
            case 1316: return MSG_RSP_HERO_STAMINA_RECOVERY;
            case 1317: return MSG_REQ_HERO_ABANDON;
            case 1318: return MSG_RSP_HERO_ABANDON;
            case 1319: return MSG_REQ_HERO_DEVOUR;
            case 1320: return MSG_RSP_HERO_DEVOUR;
            case 1321: return MSG_REQ_HERO_EVOLVE;
            case 1322: return MSG_RSP_HERO_EVOLVE;
            case 1323: return MSG_REQ_HERO_FAVOUR;
            case 1324: return MSG_RSP_HERO_FAVOUR;
            case 1325: return MSG_REQ_HERO_EXCHANGE;
            case 1326: return MSG_RSP_HERO_EXCHANGE;
            case 1401: return MSG_REQ_EQUIPMENT_BUY;
            case 1402: return MSG_RSP_EQUIPMENT_BUY;
            case 1403: return MSG_REQ_EQUIPMENT_REPLACE;
            case 1404: return MSG_RSP_EQUIPMENT_REPLACE;
            case 1405: return MSG_REQ_EQUIPMENT_DISARM;
            case 1406: return MSG_RSP_EQUIPMENT_DISARM;
            case 1407: return MSG_REQ_EQUIPMENT_SELL;
            case 1408: return MSG_RSP_EQUIPMENT_SELL;
            case 1409: return MSG_REQ_EQUIPMENT_ITEM_INSERT;
            case 1410: return MSG_RSP_EQUIPMENT_ITEM_INSERT;
            case 1411: return MSG_REQ_EQUIPMENT_ITEM_REMOVE;
            case 1412: return MSG_RSP_EQUIPMENT_ITEM_REMOVE;
            case 1413: return MSG_REQ_EQUIPMENT_FORGE;
            case 1414: return MSG_RSP_EQUIPMENT_FORGE;
            case 1415: return MSG_REQ_EQUIPMENT_LEVELUP;
            case 1416: return MSG_RSP_EQUIPMENT_LEVELUP;
            case 1417: return MSG_REQ_EQUIPMENT_INSERT_ITEM_LEVELUP;
            case 1418: return MSG_RSP_EQUIPMENT_INSERT_ITEM_LEVELUP;
            case 1500: return MSG_REQ_BLOOD_ATTACK;
            case 1501: return MSG_RSP_BLOOD_ATTACK;
            case 1502: return MSG_REQ_BLOOD_CONF;
            case 1503: return MSG_RSP_BLOOD_CONF;
            case 1504: return MSG_REQ_BLOOD_REWARD;
            case 1505: return MSG_RSP_BLOOD_REWARD;
            case 1506: return MSG_REQ_BLOOD_POKER;
            case 1507: return MSG_RSP_BLOOD_POKER;
            case 1508: return MSG_REQ_BLOOD_RANK_QUERY;
            case 1509: return MSG_RSP_BLOOD_RANK_QUERY;
            case 1510: return MSG_REQ_BLOOD_RANK_REWARD;
            case 1511: return MSG_RSP_BLOOD_RANK_REWARD;
            case 1550: return MSG_REQ_CHARGE_MONTH_REWARD;
            case 1551: return MSG_RSP_CHARGE_MONTH_REWARD;
            case 1552: return MSG_REQ_CURRENCY_BOX_OPEN;
            case 1553: return MSG_RSP_CURRENCY_BOX_OPEN;
            case 1600: return MSG_REQ_GET_HELP_ARM;
            case 1601: return MSG_RSP_GET_HELP_ARM;
            default: return null;
        }
    }
}
