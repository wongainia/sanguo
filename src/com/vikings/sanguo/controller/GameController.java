package com.vikings.sanguo.controller;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.vikings.pay.VKPayMgr;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.cache.BitmapCache;
import com.vikings.sanguo.cache.ServerFileCache;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.BuildingType;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.HolyCategory;
import com.vikings.sanguo.model.OtherRichGuildInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.PropActType;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.RechargeCardInfo;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.model.UserQuery;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.HeartBeat;
import com.vikings.sanguo.ui.AccountBar;
import com.vikings.sanguo.ui.FiefMap;
import com.vikings.sanguo.ui.Home;
import com.vikings.sanguo.ui.NotifyMsg;
import com.vikings.sanguo.ui.NotifyUI;
import com.vikings.sanguo.ui.NotifyWorldChatMsg;
import com.vikings.sanguo.ui.PhotoTaker;
import com.vikings.sanguo.ui.ScrollText;
import com.vikings.sanguo.ui.map.BattleMap;
import com.vikings.sanguo.ui.window.CastleWindow;
import com.vikings.sanguo.ui.window.PopupUI;

/**
 * 
 * 游戏UI控制， 界面切换 /变化操作接口
 * 
 * @author Brad.Chen
 * 
 */
public interface GameController {

	public void postRunnable(Runnable r);

	public Context getUIContext();

	public Activity getMainActivity();

	public View findViewById(int resId);

	public Resources getResources();

	public String getString(int resId);

	/**
	 * 根据图片名得到未缩放的原始图片 查找次序 res目录-- sdCard
	 * 
	 * @param name
	 * @return
	 */
	public Bitmap getBitmap(String name);

	/**
	 * 得到镜像图片
	 * 
	 * @param name
	 * @return
	 */
	public Bitmap getMirrorBitmap(String name);

	/**
	 * 旋转
	 * 
	 * @param name
	 * @param degree
	 * @return
	 */
	public Bitmap getRotateBitmap(String name, float degree);

	public Bitmap getScaleBitmap(String name, float xScale, float yScale);

	public Bitmap getScaleBitmap(String name, float xScale, float yScale,
			String post);

	public Bitmap getScaleDrawable(Drawable d, String name, float xScale,
			float yScale);

	public View getCachedView(int resId);

	public BattleMap getBattleMap();

	/**
	 * 根据id获取的图片都会常驻内存
	 * 
	 * @param id
	 * @return
	 */
	public Bitmap getBitmap(int id);

	public Drawable getDrawable(int resId);

	// public int findResId(String name);

	public int findResId(String name, String defType);

	public String getResourceColorText(int resId);

	/**
	 * 根据图片名得到正确缩放过的图片 查找次序 res目录-- sdCard 注意通过名字得到的图片 程序自行管理和释放 不会常驻内存
	 * 
	 * @param name
	 * @return
	 */
	public Drawable getDrawable(String name);

	public Drawable getDrawable(String name, boolean reload);

	public Drawable getDrawable(String name, int scale);

	public Drawable getDrawable(String name, int xScale, int yScale);

	public Drawable getDrawable(String name, int xScale, int yScale, String post);

	/**
	 * res目录和sdcard是否有该图片
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasPic(String name);

	public View inflate(int resId);

	public View inflate(int resId, ViewGroup root);

	public View inflate(int resId, ViewGroup root, boolean attach);

	public Object getSystemService(String serviceName);

	public void addContent(View v);

	public void addContentFullScreen(View v);

	public void addContentFullScreenGuide(View v);

	public void removeContent(View v);

	public void removeContentFullScreen(View v);

	public void showLoading(String msg);

	public void alert(String titleStr, String msgStr, int gravity,
			String msgExtendStr, CallBack callBack, boolean playDefaultSound);

	public void alert(String titleStr, String msgStr, String msgExtendStr,
			CallBack callBack, boolean playDefaultSound);

	public void alert(String msgStr, String msgExtendStr, CallBack callBack,
			boolean playDefaultSound);

	public void alert(String msgStr, CallBack callBack);

	public void alert(String msgStr);

	public void confirm(String msg, CallBack okHandler);

	public void confirm(String msg, String msgTip, CallBack okHandler,
			final CallBack dismissHandler);

	public void confirm(String title, int scale, String msg, String msgTip,
			CallBack okHandler, CallBack dismissHandler);

	public void confirm(String title, int scale, String msg, String msgTip,
			String okBtnStr, CallBack okHandler, String cancelBtnStr,
			CallBack dismissHandler);

	public void dismissLoading();

	public NotifyUI getNotify();

	/**
	 * 打开弹出窗口
	 * 
	 * @param popup
	 */
	public void registerWindow(PopupUI popup);

	/**
	 * 关闭所有弹出窗口回到主界面
	 */
	public void closeAllPopup();

	/**
	 * 回退到上一窗口
	 */
	public void goBack();

	/**
	 * 退出程序
	 */
	public void quit();

	/**
	 * 得到栈中的PreviewMian
	 */
	public PopupUI getCurPopupUI();

	public void refreshCurPopupUI();

	/**
	 * 设置当前位置 location 监听回调函数
	 * 
	 * @param location
	 */
	public void setCurLocation(Location location);

	/**
	 * 得到玩家当前位置
	 */
	public Location getCurLocation();

	/**
	 * 检验是否定位成功
	 * 
	 * @return
	 */
	public boolean checkCurLocation();

	public void updateUI(ReturnInfoClient rsinfo, boolean isGain);

	public void updateUI(ReturnInfoClient rsinfo, boolean isGain,
			boolean playDefaultSound, boolean showLevelUpTip);

	public void setAccountBarUser(UserAccountClient user);

	/**
	 * 添加新聊天玩家 获取到服务器发过来消息后
	 * 
	 * @param u
	 */
	public void addChatUser(final List<BriefUserInfoClient> ls);

	public void refreshCurMap();

	/**
	 * 登陆游戏后第一个界面逻辑
	 */
	public void openFirst();

	/**
	 * 重启当前Activity
	 */
	public void reboot();

	public FileAccess getFileAccess();

	// public HouseDetailWindow getHouseDetailWindow();

	public PhotoTaker getPhotoTaker();

	/**
	 * 设置back按钮是否可见 true可见
	 */
	public void setBackBt(boolean visibale);

	public boolean isBackBtVisibale();

	public void setBackKeyValid(boolean valid);

	public boolean getBackKeyValid();

	public AccountBar getAccountBar();

	public BitmapCache getBitmapCache();

	public HeartBeat getHeartBeat();

	public ScrollText getScrollText();

	public Handler getHandler();

	public String getStringById(int resid);

	public Home getHome();

	public void killProcess();

	public void initHeartBeat(SyncDataSet syncData);

	public FiefMap getFiefMap();

	public NotifyMsg getNotifyMsg();

	public void hideSystemAnnonce();

	public void showSystemAnnonce();

	public void hideIconForFullScreen();

	public View getBackKey();

	public void showIconForFullScreen();

	public CastleWindow getCastleWindow();

	public void pay(int channel, int userId, int amount, String exParam);

	public VKPayMgr getVKPayMgr();

	public void showEventEntry();

	public ServerFileCache getServerFileCache();

	public void moveToFief(long tileId);

	public NotifyWorldChatMsg getNotifyWorldChatMsg();

	public int getSdkLevel();

	// WINDOW

	/**
	 * 添加好友列表选项
	 */
	public void showOptionsWindow();

	/**
	 * 好友列表
	 * 
	 * @param om
	 */
	public void openFriendsWindow();

	/**
	 * 用户自己资料界面
	 */
	public void openUserInfo();

	/**
	 * 别人资料界面
	 * 
	 * @param ouc
	 */
	public void openUserInfo(OtherUserClient ouc);

	/**
	 * 帐号管理
	 */
	public void openAccountMangt();

	/**
	 * 编辑用户资料
	 * 
	 * @param user
	 */
	public void openUserInfoEditWindow();

	/**
	 * 打开用户聊天界面
	 * 
	 * @param u
	 */
	public void openChatWindow(BriefUserInfoClient u);

	/**
	 * 用户群聊
	 * 
	 * @param type
	 *            1: 私聊 2：家族 3：国家 4：世界
	 */
	public void openGroupChatWindow(int type);

	/**
	 * 打开用户聊天界面
	 * 
	 * @param u
	 */
	public void openChatUserListWindow();

	/**
	 * 打开搜索界面
	 * 
	 * @param u
	 */
	public void openSearchWindow();

	/**
	 * 搜索结果窗口
	 * 
	 * @param u
	 */
	public void openSearchResult(UserQuery query);

	/**
	 * 仓库界面
	 * 
	 */
	public void openStore(int index);

	/**
	 * 商店界面
	 * 
	 */
	public void openShop();

	public void openShop(int index);

	public void openShop(int index, byte type, int id);

	public void openCheckIn();

	/**
	 * 设置界面
	 * 
	 */
	public void openGameSetting(boolean fullScreen);

	/**
	 * 找回账号
	 * 
	 * @param reboot
	 *            找回后是否重启
	 * @param fullScreen
	 *            是否全屏（无上面的公告栏）
	 */
	public void openRertievePwd(boolean reboot, boolean fullScreen);

	/**
	 * 打开当前战争
	 */
	public void openWarInfoWindow(BriefFiefInfoClient fief);

	/**
	 * 新用户打开填写昵称和性别界面
	 */
	public void openFillUserBaseInfoWindow();

	/**
	 * 打开网站
	 */
	public void openSite(String strUri);

	/**
	 * 打开任务列表
	 */
	public void openQuestListWindow();

	/**
	 * 打开任务详情,且滚动到指定任务qi
	 */
	public void openQuestListWindow(QuestInfoClient qi);

	/**
	 * 打开任务详情
	 */
	public void openQuestDetailWindow(QuestInfoClient qi);

	/**
	 * 充值中心
	 * 
	 * @param user
	 */
	public void openRechargeCenterWindow();

	/**
	 * 充值界面
	 * 
	 * @param user
	 */
	public void openRechargeWindow(BriefUserInfoClient user);

	public void openRechargeWindow(byte type, BriefUserInfoClient user);

	/**
	 * 充值日志界面
	 */
	public void openRechageLogWindow();

	/**
	 * 训练所
	 * 
	 * @param bic
	 * @param user
	 */
	public void openArmTrainingWindow(TroopProp prop);

	/**
	 * 庄园检阅
	 */
	public void openReviewArmInManorListWindow(); // int type

	/**
	 * 领地的历史战斗列表
	 * 
	 * @param briefFiefInfoClient
	 */

	public void openHistoryWarInfoWindow(BriefFiefInfoClient briefFiefInfoClient);

	/**
	 * 家族成员列表，不同的user打开列表操作不同
	 * 
	 */
	public void openGuildUserListWindow(RichGuildInfoClient rgic);

	/**
	 * 查看别人家族成员列表，不同的user打开列表操作不同
	 * 
	 */
	public void openOtherGuildUserListWindow(OtherRichGuildInfoClient orgic);

	/**
	 * 家族群聊界面
	 * 
	 * @param guildid
	 */
	public void openGuildChatWindow(RichGuildInfoClient rgic);

	/**
	 * 打开强化属性窗口
	 */
	public void openStrengthenWindow(HeroInfoClient heroInfoClient);

	/***
	 * 打开将领技能列表
	 */
	public void openHeroSkillListWindow(HeroInfoClient heroInfoClient,
			HeroSkillSlotInfoClient heroSkillSlotInfoClient);

	// 副本相关接口
	public void openActTypeWindow();

	public void openActWindow(PropActType propActType);

	public void openCampaignWindow(ActInfoClient actClient);

	public void openFiefDetailWindow(BriefFiefInfoClient bfic);

	public void openFiefTroopWindow(BriefFiefInfoClient bfic);

	public void openSetOffTroopWindow(CampaignInfoClient campaignClient);

	public void showCastle(BriefUserInfoClient user);

	public void showCastle(int userId);

	public void openArenaWindow();

	public void openBloodWindow();

	public void openRechargeInputConfirmWindow(RechargeCardInfo info);

	// 打开圣都 名州 重郡
	public void openHolyFiefDetail(HolyCategory hc);

	// 外敌入侵
	public void openForeignInvasionFiefTip();

	// 凤仪亭
	public void openRouletteWindow();

	// 铜雀台
	public void openBronzeTerraceEnterWindow();

	// 战神宝箱
	public void openWarLordBox();

	// 天降横财
	public void openGodWealth();

	// 轮盘，馈赠
	public void openRouletteGoodListWindow();

	// 我要变强
	public void openStronger();

	// VIP特权
	public void openVipListWindow();

	// 高清大图
	public void openHeroDetailHDWindow(HeroProp hp);

	// 双倍优惠
	public void openDoubleRechargeWindow();

	// 包月优惠
	public void openMonthRechargeWindow();

	// 购买神兵
	public void openAssistGodSoldierWindow(String title,
			RichBattleInfoClient rbic);

	public void openAssistGodSoldierWindow(String title,
			RichBattleInfoClient rbic, int battleType);

	// 酒馆界面
	public void openBarWindow();

	// 将魂兑换
	public void openHeroExchangeListWindow();

	// 英雄殿
	public void openHeroCenterWindow();

	// 铁匠铺
	public void openSmithyWindow();

	// 募兵所界面
	public void openArmTrainingListWindow();

	public Bitmap getMirrorRotateBitmap(String name, int horiMirror,
			int veriMirror, int degree, float xScale, float yScale,
			boolean isSave);

	// 建筑类别列表
	public void openBuildingTypeListWindow();

	// 建筑列表
	public void openBuildingListWindow(BuildingType type);

	public Drawable getDrawableHdInBattle(String name, int type);

	public Bitmap getBitmapHdInBattle(String name, int type);

}
