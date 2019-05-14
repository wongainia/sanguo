/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-24
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui.wheel;

import com.vikings.sanguo.config.Config;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class WheelScroller {
    private ScrollingListener listener;                       
    private Scroller scroller; 
    private int lastScrollY;                                  //上次移动距离
    private boolean isScrollingPerformed;                     //是否开始转动
    
    private static final int SCROLLING_DURATION = 5000;       //默认最小转动时间
    public static final int MIN_DELTA_FOR_SCROLLING = 1;      //最小转动距离
   
    //消息
    private final int MESSAGE_SCROLL = 0;
    private final int MESSAGE_JUSTIFY = 1;
    
    public WheelScroller(ScrollingListener listener) {
        scroller = new Scroller(Config.getController().getUIContext());
        this.listener = listener;
    }
    
    public void setInterpolator(Interpolator interpolator) {
        scroller.forceFinished(true);
        scroller = new Scroller(Config.getController().getUIContext(), interpolator);
    }

    public void scroll(int distance, int time) {
        scroller.forceFinished(true);
        lastScrollY = 0;
        scroller.startScroll(0, 0, 0, distance, time != 0 ? time : SCROLLING_DURATION);
        setNextMessage(MESSAGE_SCROLL);
        startScrolling();
    }
    
    public void stopScrolling() {
        scroller.forceFinished(true);
    }

    
    private void setNextMessage(int message) {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    //清除handler中所有消息
    private void clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }
    
    private void justify() {
        listener.onJustify();
        setNextMessage(MESSAGE_JUSTIFY);
    }

    //启动转动标志位,并开始监听
    private void startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            listener.onStarted();
        }
    }

    //停止转动标志位,并开始监听
    void finishScrolling() {
        if (isScrollingPerformed) {
            listener.onFinished();
            isScrollingPerformed = false;
        }
    }
    
    public int getTimePassed() {
    	return scroller.timePassed();
    }
    
    // animation handler
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
        	//本类的核心是scroller，每次处理消息时，获取距离上次处理消息时的移动距离，以驱动WheelView滚动
            scroller.computeScrollOffset();
            int currY = scroller.getCurrY();
            int delta = lastScrollY - currY;
            lastScrollY = currY;
            if (delta != 0) {
                listener.onScroll(delta);
            }
            
            //微调 
            if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                currY = scroller.getFinalY();
                scroller.forceFinished(true);
            }

            //每发一条消息sleep一定时间，保证滚动不卡
        	try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == MESSAGE_SCROLL) {
            	//最后一次微调
                justify();
            } else {
                finishScrolling();
            }
        }
    };
    
    //scroller的状态响应
    public interface ScrollingListener {
        void onScroll(int distance);
        void onStarted();
        void onFinished();
        void onJustify();
    }
    
    public int getDistanceY() {
    	return scroller.getFinalY() - scroller.getStartY();
    }
    
    public int getDuration() {
    	return scroller.getDuration();
    }
}
