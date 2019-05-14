package com.vikings.sanguo;

public interface ResultCode {
	short RESULT_SUCCEED = (short) (0);
	short RESULT_FAILED = (short) (RESULT_SUCCEED + 1); // 未知错误
	short RESULT_FAILED_NO_LOGIN = (short) (RESULT_FAILED + 1); // 未登录
	short RESULT_FAILED_ACCOUNT_INVALID = (short) (RESULT_FAILED_NO_LOGIN + 1); // 帐号不存在（登录）
	short RESULT_FAILED_ACCOUNT_VERIFY = (short) (RESULT_FAILED_ACCOUNT_INVALID + 1); // 帐号秘密错误（登录）
	short RESULT_FAILED_PLAYER_INVALID = (short) (RESULT_FAILED_ACCOUNT_VERIFY + 1); // 玩家不存在
	short RESULT_FAILED_TARGET_INVALID = (short) (RESULT_FAILED_PLAYER_INVALID + 1); // 目标玩家不存在
	short RESULT_FAILED_SITE_INVALID = (short) (RESULT_FAILED_TARGET_INVALID + 1); // 资源点不存在（查询）
	short RESULT_FAILED_SITE_FARM_EXISTED = (short) (RESULT_FAILED_SITE_INVALID + 1); // 资源点已有农场（查询）
	short RESULT_FAILED_FARM_INVALID = (short) (RESULT_FAILED_SITE_FARM_EXISTED + 1); // 农场不存在（查询）
	short RESULT_FAILED_MONEY_ABSENCE = (short) (RESULT_FAILED_FARM_INVALID + 1); // 金钱不足(操作)
	short RESULT_FAILED_EXP_ABSENCE = (short) (RESULT_FAILED_MONEY_ABSENCE + 1); // 经验不足(操作)
	short RESULT_FAILED_REGARD_ABSENCE = (short) (RESULT_FAILED_EXP_ABSENCE + 1); // 关注度不足(操作)
	short RESULT_FAILED_FARM_STOLEN = (short) (RESULT_FAILED_REGARD_ABSENCE + 1); // 已经偷过
	short RESULT_FAILED_FARM_NOT_WAITING = (short) (RESULT_FAILED_FARM_STOLEN + 1); // 不在待采状态
	short RESULT_FAILED_FARM_NOT_COLLECTING = (short) (RESULT_FAILED_FARM_NOT_WAITING + 1); // 不在开采中
	short RESULT_FAILED_FARM_COLLECTING = (short) (RESULT_FAILED_FARM_NOT_COLLECTING + 1); // 用户在此资源点上开过农场，不能重复种植
	short RESULT_FAILED_FARM_NOT_COLLECTED = (short) (RESULT_FAILED_FARM_COLLECTING + 1); // 开采未结束
	short RESULT_FAILED_FARMER_ABSENCE = (short) (RESULT_FAILED_FARM_NOT_COLLECTED + 1); // 农夫数不足(操作)
	short RESULT_FAILED_TARGET_OFFLINE = (short) (RESULT_FAILED_FARMER_ABSENCE + 1); // 目标玩家不在线
	short RESULT_FAILED_PARAM_INVALID = (short) (RESULT_FAILED_TARGET_OFFLINE + 1); // 参数无效
	short RESULT_FAILED_LEVEL = (short) (RESULT_FAILED_PARAM_INVALID + 1); // 等级不够
	short RESULT_FAILED_TILEID_INVALID = (short) (RESULT_FAILED_LEVEL + 1); // 无效的tileid
	short RESULT_FAILED_SKILL_STUDYED = (short) (RESULT_FAILED_TILEID_INVALID + 1); // 已经学习过了
	short RESULT_FAILED_SKILL_NOTSTUDY = (short) (RESULT_FAILED_SKILL_STUDYED + 1); // 没有学习过此技能
	short RESULT_FAILED_SKILL_INVALID = (short) (RESULT_FAILED_SKILL_NOTSTUDY + 1); // 技能不存在
	short RESULT_FAILED_SKILL_FREE = (short) (RESULT_FAILED_SKILL_INVALID + 1); // 免费的技能不需要学习
	short RESULT_FAILED_SKILL_DROPNOTHING = (short) (RESULT_FAILED_SKILL_FREE + 1); // 打扫房屋什么都没得到
	short RESULT_FAILED_SKILL_CLEANED = (short) (RESULT_FAILED_SKILL_DROPNOTHING + 1); // 房屋已经被打扫过了
	short RESULT_FAILED_BAG_SELL_FORBIDDEN = (short) (RESULT_FAILED_SKILL_CLEANED + 1); // 此物品不能卖
	short RESULT_FAILED_BAG_INVALID = (short) (RESULT_FAILED_BAG_SELL_FORBIDDEN + 1); // 物品不存在
	short RESULT_FAILED_BAG_LOCK_FORBIDDEN = (short) (RESULT_FAILED_BAG_INVALID + 1); // 此物品不能上锁
	short RESULT_FAILED_BAG_USE_FORBIDDEN = (short) (RESULT_FAILED_BAG_LOCK_FORBIDDEN + 1); // 此物品不能使用
	short RESULT_FAILED_BAG_BUY_FORBIDDEN = (short) (RESULT_FAILED_BAG_USE_FORBIDDEN + 1); // 此物品不能买
	short RESULT_FAILED_BAG_ABSENCE = (short) (RESULT_FAILED_BAG_BUY_FORBIDDEN + 1); // 背包中item数不足
	short RESULT_FAILED_BAG_LOCKED = (short) (RESULT_FAILED_BAG_ABSENCE + 1); // 物品被锁定
	short RESULT_FAILED_BUILDING_INVALID = (short) (RESULT_FAILED_BAG_LOCKED + 1); // 建筑物id不存在
	short RESULT_FAILED_FARM_TOO_LITTLE = (short) (RESULT_FAILED_BUILDING_INVALID + 1); // 剩下的农产品已经够少了
	short RESULT_FAILED_BUILDING_BOUGHT = (short) (RESULT_FAILED_FARM_TOO_LITTLE + 1); // 建筑物已经买过了
	short RESULT_FAILED_SKILL_NOBUFF = (short) (RESULT_FAILED_BUILDING_BOUGHT + 1); // 没有对应的buff状态啊
	short RESULT_FAILED_SKILL_WATERED = (short) (RESULT_FAILED_SKILL_NOBUFF + 1); // 已经有水啦
	short RESULT_FAILED_SKILL_SLEEPED = (short) (RESULT_FAILED_SKILL_WATERED + 1); // 已经催眠啦
	short RESULT_FAILED_SKILL_GHOSTED = (short) (RESULT_FAILED_SKILL_SLEEPED + 1); // 已经有鬼啦
	short RESULT_FAILED_SKILL_FORBIDDEN = (short) (RESULT_FAILED_SKILL_GHOSTED + 1); // 使用对象错误
	short RESULT_FAILED_MESSAGE_INVALID = (short) (RESULT_FAILED_SKILL_FORBIDDEN + 1); // 消息字符串非法
	short RESULT_FAILED_MESSAGE_TOOLONG = (short) (RESULT_FAILED_MESSAGE_INVALID + 1); // 消息太长了
	short RESULT_FAILED_SKILL_TOOMUCH = (short) (RESULT_FAILED_MESSAGE_TOOLONG + 1); // 当天使用次数已到
	short RESULT_FAILED_FRIEND_EXIST = (short) (RESULT_FAILED_SKILL_TOOMUCH + 1); // 好友已存在
	short RESULT_FAILED_FRIEND_NOTEXIST = (short) (RESULT_FAILED_FRIEND_EXIST + 1); // 好友不存在
	short RESULT_FAILED_ITEM_ABSENCE = (short) (RESULT_FAILED_FRIEND_NOTEXIST + 1); // 物品不存在
	short RESULT_FAILED_BAG_FULL = (short) (RESULT_FAILED_ITEM_ABSENCE + 1); // 背包已满(200)
	short RESULT_FAILED_PACKET_INVALID = (short) (RESULT_FAILED_BAG_FULL + 1); // 无效的数据包
	short RESULT_FAILED_TRAINING_FORBIDDEN = (short) (RESULT_FAILED_PACKET_INVALID + 1); // 已经训练过了啊
	short RESULT_FAILED_REGION_OUTSIDE = (short) (RESULT_FAILED_TRAINING_FORBIDDEN + 1); // 超出开采范围外
	short RESULT_FAILED_TIMEOUT = (short) (RESULT_FAILED_REGION_OUTSIDE + 1); // 请求处理超时
	short RESULT_FAILED_REQUEST_TOO_FREQUENT = (short) (RESULT_FAILED_TIMEOUT + 1); // 请求太频繁
	short RESULT_FAILED_DATA_UPDATE_CONFLICT = (short) (RESULT_FAILED_REQUEST_TOO_FREQUENT + 1); // 数据更新冲突
	short RESULT_FAILED_SKILL_ROBBED = (short) (RESULT_FAILED_DATA_UPDATE_CONFLICT + 1); // 今天已经被别人打劫过了，没多少油水啦，换个人试试吧
	short RESULT_FAILED_BAG_EXTREM = (short) (RESULT_FAILED_SKILL_ROBBED + 1); // 背包已达极限(250)
	short RESULT_FAILED_FRIEND_MAX = (short) (RESULT_FAILED_BAG_EXTREM + 1); // 好友已到上限了...
	short RESULT_FAILED_TARGET_FORBIDDEN = (short) (RESULT_FAILED_FRIEND_MAX + 1); // 目标玩家不允许
	short RESULT_FAILED_BUILDING_EXIST = (short) (RESULT_FAILED_TARGET_FORBIDDEN + 1); // 建筑已经存在
	short RESULT_FAILED_ITEM_FORBIDDEN = (short) (RESULT_FAILED_BUILDING_EXIST + 1); // 此物品不能使用
	short RESULT_FAILED_BOX_KEY_NEEDED = (short) (RESULT_FAILED_ITEM_FORBIDDEN + 1); // 缺少用于开启的物品
	short RESULT_FAILED_SKILL_SELFFORBIDDEN = (short) (RESULT_FAILED_BOX_KEY_NEEDED + 1); // 自己放的不能自己解除哦
	short RESULT_FAILED_ITEMEFFECT_INVALID = (short) (RESULT_FAILED_SKILL_SELFFORBIDDEN + 1); // 对应物品的效果不存在
	short RESULT_FAILED_MONEY_NOTMUCH = (short) (RESULT_FAILED_ITEMEFFECT_INVALID + 1); // 此玩家剩的钱不多了。。。
	short RESULT_FAILED_LEVEL_NOTMUCH = (short) (RESULT_FAILED_MONEY_NOTMUCH + 1); // 此玩家的等级很低，不能执行
	short RESULT_FAILED_BUILDING_POS_INVALID = (short) (RESULT_FAILED_LEVEL_NOTMUCH + 1); // 此位置不能够放置房屋
	short RESULT_FAILED_SITE_TOO_MANY_FARM = (short) (RESULT_FAILED_BUILDING_POS_INVALID + 1); // 此资源点已达到最大开采农场数，不能种植了
	short RESULT_FAILED_TARGET_NEEDFRIEND = (short) (RESULT_FAILED_SITE_TOO_MANY_FARM + 1); // 先加好友再打劫吧
	short RESULT_FAILED_CURRENCY_ABSENCE = (short) (RESULT_FAILED_TARGET_NEEDFRIEND + 1); // 元宝不足啊~~~
	short RESULT_FAILED_SKILL_NOWATER = (short) (RESULT_FAILED_CURRENCY_ABSENCE + 1); // 没有水啊
	short RESULT_FAILED_SKILL_NOSLEEP = (short) (RESULT_FAILED_SKILL_NOWATER + 1); // 没有被催眠啊
	short RESULT_FAILED_SKILL_NOGHOST = (short) (RESULT_FAILED_SKILL_NOSLEEP + 1); // 哪有鬼啊
	short RESULT_FAILED_SKILL_NOTSELF = (short) (RESULT_FAILED_SKILL_NOGHOST + 1); // 这技能可不能对自己使用
	short RESULT_FAILED_SKILL_NOTOTHER = (short) (RESULT_FAILED_SKILL_NOTSELF + 1); // 这技能不能对别人用
	short RESULT_FAILED_SIM_INVALID = (short) (RESULT_FAILED_SKILL_NOTOTHER + 1); // sim卡信息异常
	short RESULT_FAILED_BAG_CNT_INVALID = (short) (RESULT_FAILED_SIM_INVALID + 1); // 请求的物品数量非法（为0或者只能买一个的要求多个了）
	short RESULT_FAILED_FARM_NOT_SEEDACTION = (short) (RESULT_FAILED_BAG_CNT_INVALID + 1); // 现在不是松土时间啦
	short RESULT_FAILED_FARM_NOT_PLANTACTION = (short) (RESULT_FAILED_FARM_NOT_SEEDACTION + 1); // 现在不是传粉时间啦
	short RESULT_FAILED_SKILL_PROTECTED = (short) (RESULT_FAILED_FARM_NOT_PLANTACTION + 1); // 该玩家有神卡附身
	short RESULT_FAILED_WITHOUT_FARM = (short) (RESULT_FAILED_SKILL_PROTECTED + 1); // 此玩家没有任何农场啦（炸农场失败）
	short RESULT_FAILED_SEED_ABSENCE = (short) (RESULT_FAILED_WITHOUT_FARM + 1); // 没有对应的种子了
	short RESULT_FAILED_STEAL_NULL = (short) (RESULT_FAILED_SEED_ABSENCE + 1); // 这小子包裹空空，没东西偷了
	short RESULT_FAILED_BEYOND_SCOPE = (short) (RESULT_FAILED_STEAL_NULL + 1); // 请求区域超出服务器服务范围
	short RESULT_FAILED_FARM_DROOPED = (short) (RESULT_FAILED_BEYOND_SCOPE + 1); // 早就枯萎了（不能偷等操作）
	short RESULT_FAILED_SITE_UNSUITABLE = (short) (RESULT_FAILED_FARM_DROOPED + 1); // 这地可不能种这个（适应性为0）
	short RESULT_FAILED_NICK_NULL = (short) (RESULT_FAILED_SITE_UNSUITABLE + 1); // 昵称不能为空
	short RESULT_FAILED_FARM_BROKEN = (short) (RESULT_FAILED_NICK_NULL + 1); // 农场被人破坏了，去彻底销毁吧
	short RESULT_FAILED_NICK_USED = (short) (RESULT_FAILED_FARM_BROKEN + 1); // 昵称已被占用，再想一个吧
	short RESULT_FAILED_BAG_BUY_ONLYONE = (short) (RESULT_FAILED_NICK_USED + 1); // 此物品只能买一次啦
	short RESULT_FAILED_MOBILE_USED = (short) (RESULT_FAILED_BAG_BUY_ONLYONE + 1); // 手机号已绑定过啦
	short RESULT_FAILED_EMAIL_USED = (short) (RESULT_FAILED_MOBILE_USED + 1); // 邮箱已绑定过啦
	short RESULT_FAILED_BINDING_NONE = (short) (RESULT_FAILED_EMAIL_USED + 1); // 不能绑定空的啦
	short RESULT_FAILED_ACCOUNT_RESTORE_TWICE = (short) (RESULT_FAILED_BINDING_NONE + 1); // 已经发送了验证码了，稍候
	short RESULT_FAILED_ACCOUNT_RESTORE_TIMEOUT = (short) (RESULT_FAILED_ACCOUNT_RESTORE_TWICE + 1); // 验证码过期了。。。
	short RESULT_FAILED_ACCOUNT_RESTORE_NOCODE = (short) (RESULT_FAILED_ACCOUNT_RESTORE_TIMEOUT + 1); // 请先生成验证码。
	short RESULT_FAILED_ACCOUNT_RESTORE_ERROR = (short) (RESULT_FAILED_ACCOUNT_RESTORE_NOCODE + 1); // 验证码不对
	short RESULT_FAILED_ACCOUNT_RESTORE_NONE = (short) (RESULT_FAILED_ACCOUNT_RESTORE_ERROR + 1); // 没有对应账号（通过邮箱手机号找回）
	short RESULT_FAILED_BUILDING_PRODUCTING = (short) (RESULT_FAILED_ACCOUNT_RESTORE_NONE + 1); // 建筑物已在生产中了
	short RESULT_FAILED_BUILDING_PRODUCTED = (short) (RESULT_FAILED_BUILDING_PRODUCTING + 1); // 建筑物还有产物没有收取，不能再生产
	short RESULT_FAILED_BUILDING_PRODUCT_UNABLE = (short) (RESULT_FAILED_BUILDING_PRODUCTED + 1); // 建筑物现在不能产这个卡
	short RESULT_FAILED_BUILDING_PRODUCT_NONE = (short) (RESULT_FAILED_BUILDING_PRODUCT_UNABLE + 1); // 建筑物现在没有生产任何东西
	short RESULT_FAILED_INVITE_NOTSELF = (short) (RESULT_FAILED_BUILDING_PRODUCT_NONE + 1); // 邀请者不能填自己哦
	short RESULT_FAILED_DATA_ABSENCE = (short) (RESULT_FAILED_INVITE_NOTSELF + 1); // 缺少对应玩家的数据
	short RESULT_FAILED_POKE_TARGET_LEVEL = (short) (RESULT_FAILED_DATA_ABSENCE + 1); // 动一下对象等级需要9级以上
	short RESULT_FAILED_FARM_SUBACTION_INVALID = (short) (RESULT_FAILED_POKE_TARGET_LEVEL + 1); // 种植辅助动作失败（时机不对）
	short RESULT_FAILED_FARM_MAINACTION_INVALID = (short) (RESULT_FAILED_FARM_SUBACTION_INVALID + 1); // 种植主动作失败（时机不对）
	short RESULT_FAILED_FARM_ACTION_INVALID = (short) (RESULT_FAILED_FARM_MAINACTION_INVALID + 1); // 现在种植状态正常，不需要动作
	short RESULT_FAILED_FARM_PICK_NOTHING = (short) (RESULT_FAILED_FARM_ACTION_INVALID + 1); // 现在田里没有东西捡呢
	short RESULT_FAILED_ACCOUNT_MOBILE_NOSUPPORT = (short) (RESULT_FAILED_FARM_PICK_NOTHING + 1); // 短信找回账号暂不支持。
	short RESULT_FAILED_FARM_ITEM_FORBIDDEN = (short) (RESULT_FAILED_ACCOUNT_MOBILE_NOSUPPORT + 1); // 这个种子不能使用道具哦
	short RESULT_FAILED_ACCOUNT_SIM_BINDED = (short) (RESULT_FAILED_FARM_ITEM_FORBIDDEN + 1); // SIMk卡已经绑定过其他账号了
	short RESULT_FAILED_ACCOUNT_LOGIN_FORBIDDEN = (short) (RESULT_FAILED_ACCOUNT_SIM_BINDED + 1); // 玩家被禁止登陆了
	short RESULT_FAILED_INVALID_RANKTYPE = (short) (RESULT_FAILED_ACCOUNT_LOGIN_FORBIDDEN + 1); // 排行类型错误
	short RESULT_FAILED_INVALID_RANGETYPE = (short) (RESULT_FAILED_INVALID_RANKTYPE + 1); // 范围类型错误
	short RESULT_FAILED_RANKDATA_ABSENCE = (short) (RESULT_FAILED_INVALID_RANGETYPE + 1); // 缺少用户的排行数据
	short RESULT_FAILED_STAT_SERVER_STARTING = (short) (RESULT_FAILED_RANKDATA_ABSENCE + 1); // 统计服务刚启动，没有统计信息
	short RESULT_FAILED_SVR_CLOSED = (short) (RESULT_FAILED_STAT_SERVER_STARTING + 1); // 服务器已经关闭
	short RESULT_FAILED_PLAYER_NAME_FORBIDDEN = (short) (RESULT_FAILED_SVR_CLOSED + 1); // 资料里面有那个（敏感）词（包括昵称，签名等非聊天类资料）
	short RESULT_FAILED_QUEST_INVILD = (short) (RESULT_FAILED_PLAYER_NAME_FORBIDDEN + 1); // 任务不存在
	short RESULT_FAILED_QUEST_NOT_FINISH = (short) (RESULT_FAILED_QUEST_INVILD + 1); // 任务还没完成
	short RESULT_FAILED_SVR_MAP_ERR = (short) (RESULT_FAILED_QUEST_NOT_FINISH + 1); // map服务器内部处理错误
	short RESULT_FAILED_ITEM_USE_MOBILE_NEED = (short) (RESULT_FAILED_SVR_MAP_ERR + 1); // 兑奖需要绑定手机号
	short RESULT_FAILED_MAPTHING_DEAD = (short) (RESULT_FAILED_ITEM_USE_MOBILE_NEED + 1); // 野果已经消失（同farm分开）
	short RESULT_FAILED_SELL_FRUIT_NULL = (short) (RESULT_FAILED_MAPTHING_DEAD + 1); // (强卖)这小子包裹空空，没东西卖了
	short RESULT_FAILED_RECOVER_NOFARM = (short) (RESULT_FAILED_SELL_FRUIT_NULL + 1); // 此玩家没有农场可以恢复

	short RESULT_FAILED_WISH_AMOUNT_OUT_RANGE = (short) 128; // 该愿望希望的数量超出范围了
	short RESULT_FAILED_WISH_DURATION = (short) (RESULT_FAILED_WISH_AMOUNT_OUT_RANGE + 1);// 该愿望不能持续这么久时间的
	short RESULT_FAILED_WISH_INVALID = (short) (RESULT_FAILED_WISH_DURATION + 1);// 愿望不存在
	short RESULT_FAILED_WISH_CLOSED = (short) (RESULT_FAILED_WISH_INVALID + 1); // 愿望已经关闭了
	short RESULT_FAILED_WISH_MAKED = (short) (RESULT_FAILED_WISH_CLOSED + 1); // 已经许下了一个愿望了（同时只能许一个）
	short RESULT_FAILED_WISH_TOO_MANY_ONE_DAY = (short) (RESULT_FAILED_WISH_MAKED + 1); // 今天没法许愿了(次数达到上限)
	short RESULT_FAILED_WISH_UPDATE_A_HOUR_A_TIME = (short) (RESULT_FAILED_WISH_TOO_MANY_ONE_DAY + 1); // 一小时内只能广播一次愿望
	short RESULT_FAILED_REGARD_NOT_MUCH = (short) (RESULT_FAILED_WISH_UPDATE_A_HOUR_A_TIME + 1); // 关注度不够(使用而言，不够扣除)
	short RESULT_FAILED_SKILL_CD_502 = (short) (RESULT_FAILED_REGARD_NOT_MUCH + 1);// 技能长度时间保护（502技能）
	short RESULT_FAILED_MESSAGE_UTF8_NOT_SUPPORT = (short) (RESULT_FAILED_SKILL_CD_502 + 1); // 有些字符现在还不支持
	short RESULT_FAILED_ZONE_ACTIVING = (short) (RESULT_FAILED_MESSAGE_UTF8_NOT_SUPPORT + 1);
	short RESULT_FAILED_BUILDING_NEED_BUILDING = (short) (RESULT_FAILED_ZONE_ACTIVING + 1); // 购买此建筑需要x类型建筑达到x级才行
	short RESULT_FAILED_BUILDING_ONLY_ONE = (short) (RESULT_FAILED_BUILDING_NEED_BUILDING + 1); // 不能重复购买
	short RESULT_FAILED_MANOR_INVALID = (short) (RESULT_FAILED_BUILDING_ONLY_ONE + 1); // 还没有城堡呢
	short RESULT_FAILED_MANOR_AREA_FORBIDDEN = (short) (RESULT_FAILED_MANOR_INVALID + 1); // 城堡扩建，指定的区域非法
	short RESULT_FAILED_BUILDING_PLACE_FORBIDDEN = (short) (RESULT_FAILED_MANOR_AREA_FORBIDDEN + 1); // 建筑摆放，指定的位置非法
	short RESULT_FAILED_BUILDING_BUY_NEED_LEVEUPID = (short) (RESULT_FAILED_BUILDING_PLACE_FORBIDDEN + 1); // 不能直接购买此建筑物，需要升级上级建筑
	short RESULT_FAILED_BUILDING_LEVELUP_FORBIDDEN = (short) (RESULT_FAILED_BUILDING_BUY_NEED_LEVEUPID + 1); // 不能升级此建筑物
	short RESULT_FAILED_BUILDING_BAG_FORBIDDEN = (short) (RESULT_FAILED_BUILDING_LEVELUP_FORBIDDEN + 1); // 此建筑物不能放仓库
	short RESULT_FAILED_BUILDING_SELL_FORBIDDEN = (short) (RESULT_FAILED_BUILDING_BAG_FORBIDDEN + 1); // 此建筑物不能卖
	short RESULT_FAILED_MANOR_BUY_FORBIDDEN = (short) (RESULT_FAILED_BUILDING_SELL_FORBIDDEN + 1); // 不能买这个庄园
	short RESULT_FAILED_AES_INVALID = (short) (RESULT_FAILED_MANOR_BUY_FORBIDDEN + 1); // AESkey失效
	short RESULT_FAILED_RSA_DECRYPT = (short) (RESULT_FAILED_AES_INVALID + 1); // RSA解码失败
	short RESULT_FAILED_AES_DECRYPT = (short) (RESULT_FAILED_RSA_DECRYPT + 1); // AES解码失败
	short RESULT_FAILED_UNCOMPRESS = (short) (RESULT_FAILED_AES_DECRYPT + 1); // 解压缩失败
	short RESULT_FAILED_TOKEN = (short) (RESULT_FAILED_UNCOMPRESS + 1); // token检查失败
	short RESULT_FAILED_ACCOUNT_TOO_MANY_ONE_DAY = (short) (RESULT_FAILED_TOKEN + 1); // 今天没法找回了(次数达到上限)
	short RESULT_FAILED_BUILDING_MARKET_UNABLE = (short) (RESULT_FAILED_ACCOUNT_TOO_MANY_ONE_DAY + 1); // 建筑物现在不能卖这个货物
	short RESULT_FAILED_BUILDING_MARKET_TWICE = (short) (RESULT_FAILED_BUILDING_MARKET_UNABLE + 1); // 建筑物还有商品没有收取，不能再进货
	short RESULT_FAILED_BUILDING_MARKET_SELLING = (short) (RESULT_FAILED_BUILDING_MARKET_TWICE + 1); // 商品还没有卖完呢
	short RESULT_FAILED_BUILDING_MARKET_SELLOUT = (short) (RESULT_FAILED_BUILDING_MARKET_SELLING + 1); // 商品还没有卖完呢
	short RESULT_FAILED_BUILDING_MARKET_NONE = (short) (RESULT_FAILED_BUILDING_MARKET_SELLOUT + 1); // 建筑物现在没有生产任何东西
	short RESULT_FAILED_BUILDING_REPAIR_FORBIDDEN = (short) (RESULT_FAILED_BUILDING_MARKET_NONE + 1); // 建筑物现在不需要修理
	short RESULT_FAILED_BUILDING_HELP_FORBIDDEN = (short) (RESULT_FAILED_BUILDING_REPAIR_FORBIDDEN + 1); // 建筑物现在不需要帮忙
	short RESULT_FAILED_BUILDING_HELPED_OTHER = (short) (RESULT_FAILED_BUILDING_HELP_FORBIDDEN + 1); // 你已经在另一个建筑物帮忙了
	short RESULT_FAILED_BUILDING_HELP_NOTSELF = (short) (RESULT_FAILED_BUILDING_HELPED_OTHER + 1); // 不能对自己建筑帮忙
	short RESULT_FAILED_BLACKLIST_EXIST = (short) (RESULT_FAILED_BUILDING_HELP_NOTSELF + 1); // 已经在黑名单里
	short RESULT_FAILED_BLACKLIST_MAX = (short) (RESULT_FAILED_BLACKLIST_EXIST + 1); // 黑名单已到上限了...
	short RESULT_FAILED_BLACKLIST_FORBIDDEN_72HOUR = (short) (RESULT_FAILED_BLACKLIST_MAX + 1); // 用户被列入黑名单后72小时之内不能删除
	short RESULT_FAILED_BLACKLIST_NOTEXIST = (short) (RESULT_FAILED_BLACKLIST_FORBIDDEN_72HOUR + 1); // 黑名单用户不存在
	short RESULT_FAILED_IN_TARGET_BLACKLIST = (short) (RESULT_FAILED_BLACKLIST_NOTEXIST + 1); // 你在对方黑名单里，不能进行此项操作
	short RESULT_FAILED_ACCOUNT_BIND_TWICE = (short) (RESULT_FAILED_IN_TARGET_BLACKLIST + 1); // 账号绑定已经发送了验证码了，稍候
	short RESULT_FAILED_ACCOUNT_BIND_MOBILE_NEED = (short) (RESULT_FAILED_ACCOUNT_BIND_TWICE + 1); // 账号绑定需要绑定手机
	short RESULT_FAILED_IDCARD_BINDED = (short) (RESULT_FAILED_ACCOUNT_BIND_MOBILE_NEED + 1); // 身份证号码已经绑定
	short RESULT_FAILED_ACCOUNT_BINDING_NOCODE = (short) (RESULT_FAILED_IDCARD_BINDED + 1); // 请先生成验证码。
	short RESULT_FAILED_ACCOUNT_BINDING_TIMEOUT = (short) (RESULT_FAILED_ACCOUNT_BINDING_NOCODE + 1); // 验证码过期了。。。
	short RESULT_FAILED_ACCOUNT_BINDING_ERROR = (short) (RESULT_FAILED_ACCOUNT_BINDING_TIMEOUT + 1); // 验证码不对
	short RESULT_FAILED_ID_CARD_NUMBER_USED = (short) (RESULT_FAILED_ACCOUNT_BINDING_ERROR + 1); // 身份证号码已经绑定过啦
	short RESULT_FAILED_ACCOUNT_BIND_TOO_MANY_ONE_DAY = (short) (RESULT_FAILED_ID_CARD_NUMBER_USED + 1); // 邮箱和手机的每日修改次数不超过3次
	short RESULT_FAILED_BUILDING_BAG_SIZE = (short) (RESULT_FAILED_ACCOUNT_BIND_TOO_MANY_ONE_DAY + 1); // 仓库已经装不下多的建筑了
	short RESULT_FAILED_MANOR_NEED_CLEAN_FOR_PLACE = (short) (RESULT_FAILED_BUILDING_BAG_SIZE + 1); // 环境值已经很低了，不能摆这个建筑
	short RESULT_FAILED_SKILL_SPECIAL_TARGET = (short) (RESULT_FAILED_MANOR_NEED_CLEAN_FOR_PLACE + 1); // 这技能需要按说明对特定人物使用
	short RESULT_FAILED_SKILL_TARGET_FORBIDDEN = (short) 180; // 这个技能不能对他使用
	short RESULT_FAILED_TARGET_IN_YOUR_BLACKLIST = (short) (RESULT_FAILED_SKILL_TARGET_FORBIDDEN + 1); // 目标在你的黑名单里，不能进行此项操作
	short RESULT_FAILED_MANOR_NAME_TOO_LONG = (short) (RESULT_FAILED_TARGET_IN_YOUR_BLACKLIST + 1); // 城堡名字可不能这么长
	short RESULT_FAILED_MANOR_NAME_FORBIDDEN = (short) (RESULT_FAILED_MANOR_NAME_TOO_LONG + 1); // 城堡名字-敏感词了
	short RESULT_FAILED_BUILDING_MARKET_AMOUNT_TOO_MUCH = (short) (RESULT_FAILED_MANOR_NAME_FORBIDDEN + 1); // 超过进货的最大限度了
	short RESULT_FAILED_RECHARGE_NEED_BIND = (short) (RESULT_FAILED_BUILDING_MARKET_AMOUNT_TOO_MUCH + 1); // 充值对象需要绑定手机号
	short RESULT_FAILED_BUILDING_COUNT_TOO_MUCH = RESULT_FAILED_RECHARGE_NEED_BIND + 1; // 不能再拥有新的建筑了，先卖点吧
	short RESULT_FAILED_EMAIL_INVALID = RESULT_FAILED_BUILDING_COUNT_TOO_MUCH + 1; // 邮箱无效
	short RESULT_FAILED_MOBILE_INVALID = RESULT_FAILED_EMAIL_INVALID + 1; // 手机号码无效
	short RESULT_FAILED_ID_CARD_NUMBER_INVALID = RESULT_FAILED_MOBILE_INVALID + 1; // 身份证号码无效

	short RESULT_FAILED_MANOR_ENLARGER_FORBIDDEN = 190; // 扩建的区域非法
	short RESULT_FAILED_BLACKLIST_FORBIDDEN_JENNY = RESULT_FAILED_MANOR_ENLARGER_FORBIDDEN + 1; // 禁止拉黑珍妮公主
	short RESULT_FAILED_NOT_TARGET_FRIEND = RESULT_FAILED_BLACKLIST_FORBIDDEN_JENNY + 1; // 你不是对方的好友
	short RESULT_FAILED_ACCOUNT_RECHARGED = RESULT_FAILED_NOT_TARGET_FRIEND + 1; // 请通过邮箱或者手机号找回账户（账户曾经充值,不能直接找回账号）
	short RESULT_FAILED_FILLER_CAN_NOT_CLOSED = RESULT_FAILED_ACCOUNT_RECHARGED + 1;// 已还愿不能立刻关闭
	short RESULT_FAILED_MANOR_NEED_CLEAN_SCORE_FOR_PLACE = RESULT_FAILED_FILLER_CAN_NOT_CLOSED + 1; // 环境指数很低，不能摆这个建筑
	short RESULT_FAILED_MANOR_CLEAN_PERFECT = RESULT_FAILED_MANOR_NEED_CLEAN_SCORE_FOR_PLACE + 1; // 你不能对这个玩家进行打扫了
	short RESULT_FAILED_BLACKLIST_FORBIDDEN_BADSKILL = RESULT_FAILED_MANOR_CLEAN_PERFECT + 1; // 恶意技能限制拉黑
	short RESULT_FAILED_POKE_NEED_LEVEL_DIFF_IN_10 = RESULT_FAILED_BLACKLIST_FORBIDDEN_BADSKILL + 1; // 该动一下技能需要玩家等级差在10级内
	short RESULT_FAILED_BANK_MONEY_NOT_ENOUGH_FOR_WITHDRAW = RESULT_FAILED_POKE_NEED_LEVEL_DIFF_IN_10 + 1; // 没这么多现金给你取，余额不足（取款提示）
	short RESULT_FAILED_BANK_MONEY_LIMIT_EXCEEDED = 200; // 存款超过上限（存款提示）
	short RESULT_FAILED_BANK_MONEY_NOT_ENOUGH_FOR_DEPOSIT = RESULT_FAILED_BANK_MONEY_LIMIT_EXCEEDED + 1; // 没这么多现金给你存（存款提示）
	short RESULT_FAILED_BUILDING_BAG_FORBIDDEN_HAVING_MONEY = RESULT_FAILED_BANK_MONEY_NOT_ENOUGH_FOR_DEPOSIT + 1; // 存钱罐里有钱不能放入仓库
	short RESULT_FAILED_BUILDING_SELL_FORBIDDEN_HAVING_MONEY = RESULT_FAILED_BUILDING_BAG_FORBIDDEN_HAVING_MONEY + 1; // 存钱罐里有钱不能出售
	short RESULT_FAILED_BUILDING_TYPE_PROVIDED_BY_GM_TOOL_FORBIDDEN = RESULT_FAILED_BUILDING_SELL_FORBIDDEN_HAVING_MONEY + 1; // 此类建筑不能通过gm工具分发
	short RESULT_FAILED_CREDIT_ABSENCE = RESULT_FAILED_BUILDING_TYPE_PROVIDED_BY_GM_TOOL_FORBIDDEN + 1; // 信用值太低(要求)
	short RESULT_FAILED_FORCE_LOGOUT = RESULT_FAILED_CREDIT_ABSENCE + 1; // 被强制下线
	short RESULT_FAILED_MACHINE_PLAY_TOO_MANY_ONE_DAY = RESULT_FAILED_FORCE_LOGOUT + 1; // 今天玩水果机的次数太多了啊
	short RESULT_FAILED_CAN_NOT_STUDY_SKILL_BY_CURRENCY = RESULT_FAILED_MACHINE_PLAY_TOO_MANY_ONE_DAY + 1; // 不能用元宝学习此技能
	short RESULT_FAILED_CAN_NOT_BROADCAST_FILLED_WISH = RESULT_FAILED_CAN_NOT_STUDY_SKILL_BY_CURRENCY + 1;// 已还愿不能广播
	short RESULT_FAILED_LEVEL_BIG_THAN_EFFECT = RESULT_FAILED_CAN_NOT_BROADCAST_FILLED_WISH + 1; // 你不能使用比自己等级还低的农田防偷盗卡
	short RESULT_FAILED_FARM_PROTECTED = RESULT_FAILED_LEVEL_BIG_THAN_EFFECT + 1; // 该农田被保护，不能被偷
	short RESULT_FAILED_TARGET_BUILDING_TOO_MUCH = RESULT_FAILED_FARM_PROTECTED + 1; // 对方已经不能再拥有新的建筑了
	short RESULT_FAILED_QUEST_LEVEL_TOO_HIGH = RESULT_FAILED_TARGET_BUILDING_TOO_MUCH + 1; // 任务等级太高，玩家等级还达不到任务的最低要求
	short RESULT_FAILED_QUEST_LEVEL_TOO_LOW = RESULT_FAILED_QUEST_LEVEL_TOO_HIGH + 1; // 任务等级太低，玩家等级已超过任务的最高限制
	short RESULT_FAILED_QUEST_HAVE_ACCEPTED = RESULT_FAILED_QUEST_LEVEL_TOO_LOW + 1; // 你已经有同样的任务存在了
	short RESULT_FAILED_BUILDING_LEVELUP_SKILL_AMOUNT = RESULT_FAILED_QUEST_HAVE_ACCEPTED + 1; // 升级此建筑物还需要更多人帮忙
	short RESULT_FAILED_BUILDING_LEVELUP_SKILL_FORBIDDEN = RESULT_FAILED_BUILDING_LEVELUP_SKILL_AMOUNT + 1; // 此建筑物升级不需要帮忙了
	short RESULT_FAILED_BILLBOARD_CONTEXT_TOO_LONG = RESULT_FAILED_BUILDING_LEVELUP_SKILL_FORBIDDEN + 1; // 告示牌字符过长
	short RESULT_FAILED_QUEST_HAS_NOT_BEGUN = RESULT_FAILED_BILLBOARD_CONTEXT_TOO_LONG + 1; // 还未到任务开启时间
	short RESULT_FAILED_QUEST_HAS_ENDED = RESULT_FAILED_QUEST_HAS_NOT_BEGUN + 1; // 任务时间已过
	short RESULT_FAILED_SELL_FORBIDDEN_FOR_USING = RESULT_FAILED_QUEST_HAS_ENDED + 1; // 不能出售正在使用的物品
	short RESULT_FAILED_SKILL_SMALL_GHOSTED = RESULT_FAILED_SELL_FORBIDDEN_FOR_USING + 1; // 已经有小鬼了
	short RESULT_FAILED_SKILL_BIG_GHOSTED = RESULT_FAILED_SKILL_SMALL_GHOSTED + 1; // 已经有大鬼了
	short RESULT_FAILED_DRAFT_CD = RESULT_FAILED_SKILL_BIG_GHOSTED + 1; // 现在还不能征兵
	short RESULT_FAILED_DRAFT_ARM_FORBIDDEN = RESULT_FAILED_DRAFT_CD + 1; // 不能征这种兵
	short RESULT_FAILED_ARM_KIND_MAX = RESULT_FAILED_DRAFT_ARM_FORBIDDEN + 1; // 不能再添加新的兵种了
	short RESULT_FAILED_ARM_COUNT_MAX = RESULT_FAILED_ARM_KIND_MAX + 1; // 不能再添加新的兵了，加兵营吧
	short RESULT_FAILED_FIEF_INVALID = RESULT_FAILED_ARM_COUNT_MAX + 1; // 领地不存在
	short RESULT_FAILED_FIEF_TAX_NOTHING = RESULT_FAILED_FIEF_INVALID + 1; // 领地上没有税可以收了
	short RESULT_FAILED_FIEF_NOT_IS_LORD = RESULT_FAILED_FIEF_TAX_NOTHING + 1; // 不是对应领地的领主
	short RESULT_FAILED_MANOR_NOT_BELONG_TO_FIEF = RESULT_FAILED_FIEF_NOT_IS_LORD + 1; // 玩家不在这个领地上
	short RESULT_FAILED_LORD_MANOR_RISE_FORBIDDEN = RESULT_FAILED_MANOR_NOT_BELONG_TO_FIEF + 1;// 禁止领主使用崛起功能
	short RESULT_FAILED_FIEF_LORD_INVALID = RESULT_FAILED_LORD_MANOR_RISE_FORBIDDEN + 1; // 没有对应的领主
	short RESULT_FAILED_CREATE_BATTLE = RESULT_FAILED_FIEF_LORD_INVALID + 1; // 生成战场失败
	short RESULT_FAILED_MANOR_RISE_TROOP_CD = RESULT_FAILED_CREATE_BATTLE + 1; // 现在不能崛起
	short RESULT_FAILED_MANOR_DRAFT_POP_ABSENCE = RESULT_FAILED_MANOR_RISE_TROOP_CD + 1; // 庄园人口不够了
	short RESULT_FAILED_BUILDING_PRODUCT_TOO_MUCH = RESULT_FAILED_MANOR_DRAFT_POP_ABSENCE + 1; // 一次生产的数量太多了
	short RESULT_FAILED_BATTLE_LOG_INVALID = RESULT_FAILED_BUILDING_PRODUCT_TOO_MUCH + 1; // 战场日志不存在
	short RESULT_FAILED_DRAFT_ARM_POP = RESULT_FAILED_BATTLE_LOG_INVALID + 1; // 领地的人口都不够了
	short RESULT_FAILED_BUILDING_PRODUCT_FINISHED = RESULT_FAILED_DRAFT_ARM_POP + 1; // 建筑物已经生产好了
	short RESULT_FAILED_BUILDING_PRODUCT_SPEED_FORBIDDEN = RESULT_FAILED_BUILDING_PRODUCT_FINISHED + 1; // 建筑物生产不能加速
	short RESULT_FAILED_MANOR_MOVE_FIEF_FORBIDDEN = RESULT_FAILED_BUILDING_PRODUCT_SPEED_FORBIDDEN + 1; // 领主禁止你搬到他的地盘
	short RESULT_FAILED_FIEF_DRAFT_REWARD_NULL = RESULT_FAILED_MANOR_MOVE_FIEF_FORBIDDEN + 1; // 没有征兵奖励可以领了
	short RESULT_FAILED_MANOR_MOVE_TROOP_NULL = RESULT_FAILED_FIEF_DRAFT_REWARD_NULL + 1; // 没有兵可以挪出去了
	short RESULT_FAILED_FIEF_IN_BATTLE = RESULT_FAILED_MANOR_MOVE_TROOP_NULL + 1; // 目标领地处于战争中，不能执行此操作
	short RESULT_FAILED_EXPLOIT_ABSENCE = RESULT_FAILED_FIEF_IN_BATTLE + 1; // 功勋不足
	short RESULT_FAILED_MILITARY_RANK_BUY_FORBIDDEN = RESULT_FAILED_EXPLOIT_ABSENCE + 1; // 以你当前的军衔不能买此军衔
	short RESULT_FAILED_MILITARY_RANK_NOT_ENOUGH = RESULT_FAILED_MILITARY_RANK_BUY_FORBIDDEN + 1; // 你的军衔不够（不能买这个建筑）
	short RESULT_FAILED_BUILDING_PRODUCT_STOP_FORBIDDEN = RESULT_FAILED_MILITARY_RANK_NOT_ENOUGH + 1; // 建筑物生产-不能停产这个东西
	short RESULT_FAILED_FIEF_ABANDON_ARM = RESULT_FAILED_BUILDING_PRODUCT_STOP_FORBIDDEN + 1; // 领地上还有部队，不能放弃
	short RESULT_FAILED_FIEF_MOVE_MANOR_NOTSELF = RESULT_FAILED_FIEF_ABANDON_ARM + 1; // 不能强迁自己的房子
	short RESULT_FAILED_FIEF_MOVE_MANOR_IN_RISE = RESULT_FAILED_FIEF_MOVE_MANOR_NOTSELF + 1; // 对方崛起中，不能强迁
	short RESULT_FAILED_FIEF_TAX_CD = RESULT_FAILED_FIEF_MOVE_MANOR_IN_RISE + 1; // 今天已经不能再征税了
	short RESULT_FAILED_DRAFT_NONE = RESULT_FAILED_FIEF_TAX_CD + 1; // 征兵数量为0
	short RESULT_FAILED_SVR_BATTLE_CLOSED = RESULT_FAILED_DRAFT_NONE + 1; // 战斗相关功能服务器已经关闭
	short RESULT_FAILED_WISH_FILL_FORBIDDEN = RESULT_FAILED_SVR_BATTLE_CLOSED + 1; // 该愿望不允许被还愿
	short RESULT_FAILED_DAILY_EXPLORER = RESULT_FAILED_WISH_FILL_FORBIDDEN + 1; // 超过每日探索上限
	short RESULT_FAILED_DAILY_EXPLORER_ALL = RESULT_FAILED_DAILY_EXPLORER + 1; // 超过每日全探上限
	short RESULT_FAILED_DAILY_BUILDING_CLEAN = RESULT_FAILED_DAILY_EXPLORER_ALL + 1; // 超过每日扫房子上限
	short RESULT_FAILED_DAILY_BUILDING_REPAIR = RESULT_FAILED_DAILY_BUILDING_CLEAN + 1; // 超过每日修理房子上限
	short RESULT_FAILED_MILITARY_RANK_BUY_INVALID = RESULT_FAILED_DAILY_BUILDING_REPAIR + 1; // 没有对应的军衔或者没有开放
	short RESULT_FAILED_GUILD_INVALID = RESULT_FAILED_MILITARY_RANK_BUY_INVALID + 1; // 家族不存在
	short RESULT_FAILED_GUILD_NAME_FORBIDDEN = RESULT_FAILED_GUILD_INVALID + 1; // 家族名字敏感词
	short RESULT_FAILED_GUILD_NAME_DOUBLE = RESULT_FAILED_GUILD_NAME_FORBIDDEN + 1; // 家族名字跟别人重复了
	short RESULT_FAILED_GUILD_INVITE_MEMBER = RESULT_FAILED_GUILD_NAME_DOUBLE + 1; // 已经是族员了，不能邀请
	short RESULT_FAILED_GUILD_INVITE_INVITED = RESULT_FAILED_GUILD_INVITE_MEMBER + 1; // 已经发出过邀请了，不要重复邀请
	short RESULT_FAILED_GUILD_AUTHORITY = RESULT_FAILED_GUILD_INVITE_INVITED + 1; // 没有对应权限执行对应操作（邀请，踢人。。。）
	short RESULT_FAILED_GUILD_INVITE_OTHER_MEMBER = RESULT_FAILED_GUILD_AUTHORITY + 1; // 不能邀请其他族里的族员
	short RESULT_FAILED_GUILD_LEVEL_FORBIDDEN = RESULT_FAILED_GUILD_INVITE_OTHER_MEMBER + 1; // 对应等级的家族没开放，不能升级
	short RESULT_FAILED_GUILD_BUILD_ALREADY = RESULT_FAILED_GUILD_LEVEL_FORBIDDEN + 1; // 已经是族员了，不能再创建新的
	short RESULT_FAILED_FAVORITE_FIEF_TOO_MUCH = RESULT_FAILED_GUILD_BUILD_ALREADY + 1; // 不能收藏更多的领地
	short RESULT_FAILED_FAVORITE_FIEF_DUPLICATE = RESULT_FAILED_FAVORITE_FIEF_TOO_MUCH + 1; // 领地已经被收藏
	short RESULT_FAILED_GUILD_JOIN_MEMBER = RESULT_FAILED_FAVORITE_FIEF_DUPLICATE + 1; // 已经是族员了，不能再响应邀请
	short RESULT_FAILED_GUILD_INVITE_NONE = RESULT_FAILED_GUILD_JOIN_MEMBER + 1; // 对方没有发出邀请或者邀请已取消
	short RESULT_FAILED_GUILD_MEMBER_MAX = RESULT_FAILED_GUILD_INVITE_NONE + 1; // 该家族已经达到最大人数上限了
	short RESULT_FAILED_GUILD_QUIT_LEADER = RESULT_FAILED_GUILD_MEMBER_MAX + 1; // 族长不能直接退出家族
	short RESULT_FAILED_GUILD_NOT_MEMBER = RESULT_FAILED_GUILD_QUIT_LEADER + 1; // 不是族员，不能执行对应操作（退族，。。）
	short RESULT_FAILED_GUILD_LOG_FORBIDDEN = RESULT_FAILED_GUILD_NOT_MEMBER + 1; // 没有权限查看家族日志
	short RESULT_FAILED_GUILD_ASSIGN_NEED_LEADER = RESULT_FAILED_GUILD_LOG_FORBIDDEN + 1; // 不是族长不能禅让

	short RESULT_FAILED_STAMINA_NOT_ENOUGH = 1085;// 体力不足

	short RESULT_FAILED_MIN_BLOOD = 1071;// 体力不足

	short RESULT_FAILED_ARENA_RANK_CHANGED = 502; // 巅峰战场的排名已经变化
}