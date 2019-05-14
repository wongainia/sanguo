package com.vikings.sanguo.utils;

import com.vikings.sanguo.config.Config;

import android.graphics.Point;

public class BattleCoordUtil {
		public final static float SCALE_ARM = 11/12.0f;                   //士兵阵列占整个的比例	
		public final static float LEFT_MOST_SCALE = 285/440.0f;	        //起点分隔所占比例
		public final static float  PANE_COUNT = 440/75.0f;             //一行有多少个小方形
	
		public static Point[] downArmPosition;    //下方士兵坐标
		public static Point[] upArmPosition;    //上方士兵坐标
		
		//骑兵的坐标单独出来  最多8个
		
		public static Point[] downAvalvyPosition;         //攻方骑兵坐标
		public static Point[] upAvalvyPosition;         //守方骑兵坐标
		
		public static Point[] downElephantsPosition;    //下方象兵坐标
		public static Point[] upElephantsPosition;    //上方象兵兵坐标
		
		public static Point downBossPosition;                  //boss		
		public static Point upBossPosition;    
		
		public static int armGap = 0;                        //双方的间隔  军队
		
		public static Point downMatrixLB;                  //下方矩阵坐标的起始点  移动的时候要跟着移动
		public static Point upMatrixLB;                      //上方矩阵坐标的  移动的时候跟着移动
		
		public static Point downMatrixInitLB;                       //攻击方坐标的  移动的时候要跟着移动
		public static Point upMatrixInitLB;                      //防守方坐标的  移动的时候跟着移动
		
		public static int OFFSET_OPPOSITE_WIDTH = 480;
		
		public static void initBattleCoorDate()
		{
			getDownPoint();
			getUpPoint();
			
			getDownAvalvyPoint();
			getUpAvalvyPoint();
			
			initDownElephantsPosition();
			initUpElephantsPosition();
			
			downBossPosition = new Point();
			downBossPosition.x = (downArmPosition[1].x + downArmPosition[5].x)/2;
			downBossPosition.y = (downArmPosition[1].y + downArmPosition[5].y)/2;
			
			upBossPosition = new Point();
			upBossPosition.x = (upArmPosition[5].x + upArmPosition[9].x)/2;
			upBossPosition.y = (upArmPosition[5].y + upArmPosition[9].y)/2;
			
			float middle = Config.screenWidth*SCALE_ARM * SCALE_ARM * LEFT_MOST_SCALE;
			double d =middle*middle*2;
			double l = Config.screenWidth*SCALE_ARM / PANE_COUNT *Config.screenWidth*SCALE_ARM / PANE_COUNT/2;			
			double  temp = (Math.sqrt(d) - 6*Math.sqrt(l));			
			armGap = (int) (Math.sqrt(temp*temp/2) -5*Config.SCALE_FROM_HIGH);
			
			downMatrixLB = new Point();
			downMatrixLB.x = (int) (downArmPosition[11].x);
			downMatrixLB.y = (int) (downArmPosition[10].y) ;
			
			upMatrixLB = new Point();
			upMatrixLB.x = (int) (upArmPosition[3].x) ;
			upMatrixLB.y = (int) (upArmPosition[2].y);		
						
			downMatrixInitLB = new Point();
			downMatrixInitLB.x = downMatrixLB.x;
			downMatrixInitLB.y = downMatrixLB.y ;
			
			upMatrixInitLB = new Point();
			upMatrixInitLB.x = upMatrixLB.x ;
			upMatrixInitLB.y = upMatrixLB.y;		
		}
		
		public static void initArmLBCoord(boolean isLeft)
		{
			if(isLeft)
			{
				downMatrixLB.x = downMatrixInitLB.x;
				downMatrixLB.y = downMatrixInitLB.y;
			}
			else
			{
				upMatrixLB.x = upMatrixInitLB.x;
				upMatrixLB.y = upMatrixInitLB.y;
			}
			
		}
		
		public static void getDownAvalvyPoint()
		{
			downAvalvyPosition= new Point[8];
			for(int i = 0; i < downAvalvyPosition.length; i++)
			{
				downAvalvyPosition[i] = new Point();
			}
			downAvalvyPosition[0].x = (downArmPosition[0].x + downArmPosition[4].x)/2;
			downAvalvyPosition[0].y = (downArmPosition[0].y + downArmPosition[4].y)/2;
			
			downAvalvyPosition[1].x = (downArmPosition[1].x + downArmPosition[5].x)/2;
			downAvalvyPosition[1].y = (downArmPosition[1].y + downArmPosition[5].y)/2;
			
			downAvalvyPosition[2].x = (downArmPosition[2].x + downArmPosition[6].x)/2;
			downAvalvyPosition[2].y = (downArmPosition[2].y + downArmPosition[6].y)/2;
			
			downAvalvyPosition[3].x = (downArmPosition[3].x + downArmPosition[7].x)/2;
			downAvalvyPosition[3].y = (downArmPosition[3].y + downArmPosition[7].y)/2;
			
			downAvalvyPosition[4].x = downArmPosition[8].x;
			downAvalvyPosition[4].y = downArmPosition[8].y ;
			
			downAvalvyPosition[5].x = downArmPosition[9].x;
			downAvalvyPosition[5].y = downArmPosition[9].y ;
			
			downAvalvyPosition[6].x = downArmPosition[10].x;
			downAvalvyPosition[6].y = downArmPosition[10].y ;
			
			downAvalvyPosition[7].x = downArmPosition[11].x;
			downAvalvyPosition[7].y = downArmPosition[11].y ;
		}
		
		public static void getUpAvalvyPoint()
		{
			upAvalvyPosition= new Point[8];
			for(int i = 0; i < upAvalvyPosition.length; i++)
			{
				upAvalvyPosition[i] = new Point();
			}
			upAvalvyPosition[0].x = upArmPosition[0].x;//(defArmPosition[0].x + defArmPosition[4].x)/2;
			upAvalvyPosition[0].y = upArmPosition[0].y;//(defArmPosition[0].y + defArmPosition[4].y)/2;
			
			upAvalvyPosition[1].x = upArmPosition[1].x;//(defArmPosition[1].x + defArmPosition[5].x)/2;
			upAvalvyPosition[1].y = upArmPosition[1].y;//(defArmPosition[1].y + defArmPosition[5].y)/2;
			
			upAvalvyPosition[2].x = upArmPosition[2].x;//(defArmPosition[2].x + defArmPosition[6].x)/2;
			upAvalvyPosition[2].y = upArmPosition[2].y;//(defArmPosition[2].y + defArmPosition[6].y)/2;
			
			upAvalvyPosition[3].x = upArmPosition[3].x;//(defArmPosition[3].x + defArmPosition[7].x)/2;
			upAvalvyPosition[3].y = upArmPosition[3].y;//(defArmPosition[3].y + defArmPosition[7].y)/2;
			
			upAvalvyPosition[4].x = (upArmPosition[4].x + upArmPosition[8].x)/2;
			upAvalvyPosition[4].y = (upArmPosition[4].y + upArmPosition[8].y)/2 ;
			
			upAvalvyPosition[5].x = (upArmPosition[5].x + upArmPosition[9].x)/2;
			upAvalvyPosition[5].y = (upArmPosition[5].y + upArmPosition[9].y)/2 ;
			
			upAvalvyPosition[6].x = (upArmPosition[6].x + upArmPosition[10].x)/2;
			upAvalvyPosition[6].y = (upArmPosition[6].y + upArmPosition[10].y)/2 ;
			
			upAvalvyPosition[7].x = (upArmPosition[7].x + upArmPosition[11].x)/2;
			upAvalvyPosition[7].y = (upArmPosition[7].y +upArmPosition[11].y)/2 ;
		}
		
		public static void initDownElephantsPosition()
		{
			downElephantsPosition = new Point[6];
			for(int i = 0; i < downElephantsPosition.length; i++)
			{
				downElephantsPosition[i] = new Point();
			}
			
			int armRectWidth = (int) (Config.screenWidth *SCALE_ARM);
			float paneWidth =  armRectWidth/PANE_COUNT;
					   
		   downElephantsPosition[2].x =  downAvalvyPosition[2].x;
		   downElephantsPosition[2].y =  downAvalvyPosition[2].y;
		   
		   downElephantsPosition[0].x =  (int) (downAvalvyPosition[0].x - paneWidth/6);
		   downElephantsPosition[0].y =  (int) (downAvalvyPosition[0].y - paneWidth/6);
		   
		   downElephantsPosition[1].x =  (int) (downAvalvyPosition[1].x - paneWidth/3);
		   downElephantsPosition[1].y =  (int) (downAvalvyPosition[1].y - paneWidth/3);
		   
		   downElephantsPosition[5].x = downAvalvyPosition[6].x;
		   downElephantsPosition[5].y = downAvalvyPosition[6].y ;
		   
		   downElephantsPosition[3].x = (int) (downAvalvyPosition[4].x - paneWidth/6);
		   downElephantsPosition[3].y = (int) (downAvalvyPosition[4].y - paneWidth/6);
		   
		   downElephantsPosition[4].x = (int) (downAvalvyPosition[5].x - paneWidth/3);
		   downElephantsPosition[4].y = (int) (downAvalvyPosition[5].y - paneWidth/3);
		   
		}
		
		public static void initUpElephantsPosition()
		{
			upElephantsPosition = new Point[6];
			for(int i = 0; i < upElephantsPosition.length; i++)
			{
				upElephantsPosition[i] = new Point();
			}			
			int armRectWidth = (int) (Config.screenWidth *SCALE_ARM);
			float paneWidth =  armRectWidth/PANE_COUNT;
						
			upElephantsPosition[2].x =  upAvalvyPosition[2].x;
			upElephantsPosition[2].y =  upAvalvyPosition[2].y;
		   
			upElephantsPosition[0].x =  (int) (upAvalvyPosition[0].x - paneWidth/6);
			upElephantsPosition[0].y =  (int) (upAvalvyPosition[0].y - paneWidth/6);
		   
			upElephantsPosition[1].x =  (int) (upAvalvyPosition[1].x - paneWidth/3);
			upElephantsPosition[1].y =  (int) (upAvalvyPosition[1].y - paneWidth/3);
		   
			upElephantsPosition[5].x = upAvalvyPosition[6].x;
			upElephantsPosition[5].y = upAvalvyPosition[6].y ;
		   
			upElephantsPosition[3].x = (int) (upAvalvyPosition[4].x - paneWidth/6);
			upElephantsPosition[3].y = (int) (upAvalvyPosition[4].y - paneWidth/6);
		   
			upElephantsPosition[4].x = (int) (upAvalvyPosition[5].x - paneWidth/3);
			upElephantsPosition[4].y = (int) (upAvalvyPosition[5].y - paneWidth/3);
		}
		
		public static void getDownPoint()
		{
			downArmPosition= new Point[12];
			int armRectWidth = (int) (Config.screenWidth *SCALE_ARM);
			int lastPointX = (int) (Config.screenWidth*(1-SCALE_ARM)/2);
			int laxtPointY = (int) ((Config.screenHeight - armRectWidth)/2 + armRectWidth*LEFT_MOST_SCALE);
			float paneWidth =  armRectWidth/PANE_COUNT;
			
			for(int i = 0; i < downArmPosition.length; i++)
			{
				downArmPosition[i] = new Point();
			}
			
			downArmPosition[3].x = (int) (lastPointX +  1.5 * paneWidth);
			downArmPosition[3].y = (int) (laxtPointY - 0.5 * paneWidth);
			
			downArmPosition[1].x = (int) (lastPointX +  2 * paneWidth);
			downArmPosition[1].y = laxtPointY;
			
			downArmPosition[0].x = (int) (downArmPosition[1].x +0.5 * paneWidth) ;
			downArmPosition[0].y = (int) (downArmPosition[1].y +0.5 * paneWidth);
			
			downArmPosition[2].x = (int) (downArmPosition[0].x +0.5 * paneWidth) ;
			downArmPosition[2].y = (int) (downArmPosition[0].y +0.5 * paneWidth);
			
			downArmPosition[7].x = (int) (lastPointX + paneWidth) ;
			downArmPosition[7].y = (int) (laxtPointY);
			
			downArmPosition[5].x = (int) (downArmPosition[7].x +0.5 * paneWidth) ;
			downArmPosition[5].y = (int) (downArmPosition[7].y +0.5 * paneWidth);
			
			downArmPosition[4].x = (int) (downArmPosition[5].x +0.5 * paneWidth) ;
			downArmPosition[4].y = (int) (downArmPosition[5].y +0.5 * paneWidth);
			
			downArmPosition[6].x = (int) (downArmPosition[4].x +0.5 * paneWidth) ;
			downArmPosition[6].y = (int) (downArmPosition[4].y +0.5 * paneWidth);
			
			downArmPosition[11].x = (int) (lastPointX + 0.5 *paneWidth) ;
			downArmPosition[11].y = (int) (laxtPointY+ 0.5 *paneWidth);
			
			downArmPosition[9].x = (int) (downArmPosition[11].x + 0.5 *paneWidth) ;
			downArmPosition[9].y = (int) (downArmPosition[11].y+ 0.5 *paneWidth);
			
			downArmPosition[8].x = (int) (downArmPosition[9].x + 0.5 *paneWidth) ;
			downArmPosition[8].y = (int) (downArmPosition[9].y+ 0.5 *paneWidth);
			
			downArmPosition[10].x = (int) (downArmPosition[8].x + 0.5 *paneWidth) ;
			downArmPosition[10].y = (int) (downArmPosition[8].y+ 0.5 *paneWidth);
						
			return ;
		}
		
		public static void getUpPoint()
		{
			upArmPosition = new Point[12];
			int armRectWidth = (int) (Config.screenWidth * SCALE_ARM);
			int lastPointX = (int) ((int) (Config.screenWidth*(1-SCALE_ARM)/2) +  armRectWidth* LEFT_MOST_SCALE);
			int laxtPointY = (int) ((Config.screenHeight - armRectWidth)/2 );
			float paneWidth =  armRectWidth/PANE_COUNT;
			
			for(int i = 0; i < upArmPosition.length; i++)
			{
				upArmPosition[i] = new Point();
			}
			
			upArmPosition[11].x = (int) (lastPointX);
			upArmPosition[11].y = (int) (laxtPointY + paneWidth);
			
			upArmPosition[9].x = (int) (upArmPosition[11].x +0.5 * paneWidth) ;
			upArmPosition[9].y = (int) (upArmPosition[11].y +0.5 * paneWidth);;
			
			upArmPosition[8].x = (int) (upArmPosition[9].x +0.5 * paneWidth) ;
			upArmPosition[8].y = (int) (upArmPosition[9].y +0.5 * paneWidth);
			
			upArmPosition[10].x = (int) (upArmPosition[8].x +0.5 * paneWidth) ;
			upArmPosition[10].y = (int) (upArmPosition[8].y +0.5 * paneWidth);
			
			upArmPosition[7].x = (int) (upArmPosition[11].x - 0.5* paneWidth) ;
			upArmPosition[7].y = (int) (upArmPosition[11].y+0.5 * paneWidth);
			
			upArmPosition[5].x = (int) (upArmPosition[7].x +0.5 * paneWidth) ;
			upArmPosition[5].y = (int) (upArmPosition[7].y +0.5 * paneWidth);
			
			upArmPosition[4].x = (int) (upArmPosition[5].x +0.5 * paneWidth) ;
			upArmPosition[4].y = (int) (upArmPosition[5].y +0.5 * paneWidth);
			
			upArmPosition[6].x = (int) (upArmPosition[4].x +0.5 * paneWidth) ;
			upArmPosition[6].y = (int) (upArmPosition[4].y +0.5 * paneWidth);
			
			upArmPosition[3].x =  (int) (upArmPosition[7].x - 0.5* paneWidth) ;
			upArmPosition[3].y = (int) (upArmPosition[7].y +0.5 * paneWidth);
			
			upArmPosition[1].x = (int) (upArmPosition[3].x + 0.5 *paneWidth) ;
			upArmPosition[1].y = (int) (upArmPosition[3].y+ 0.5 *paneWidth);
			
			upArmPosition[0].x = (int) (upArmPosition[1].x + 0.5 *paneWidth) ;
			upArmPosition[0].y = (int) (upArmPosition[1].y+ 0.5 *paneWidth);
			
			upArmPosition[2].x = (int) (upArmPosition[0].x + 0.5 *paneWidth) ;
			upArmPosition[2].y = (int) (upArmPosition[0].y+ 0.5 *paneWidth);			
			return ;
		}
		
		
	// 产生指定范围内的随机数坐标
	public static int[] randomSpecRange(int min, int max, int n)
	{
		if (n > (max - min + 1) || max < min)
		{
			return null;
		}
		int[] result = new int[n];
		int count = 0;
		while (count < n)
		{
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++)
			{
				if (num == result[j])
				{
					flag = false;
					break;
				}
			}
			if (flag)
			{
				result[count] = num;
				count++;
			}
		}
		return result;
	}

}
