package com.vikings.sanguo.model;

import java.io.SequenceInputStream;

import com.vikings.sanguo.utils.StringUtil;

//特效参数
public class BattleAnimEffects
{
	private int id;
	
	private byte discharge = 1;       			 //施放方  1  下方  2 上方  
	private String seqFrame = "12345";       //帧顺序	  比如12345 第一帧、第二帧 第 三帧
	private String icon; 								// 图片
	private int xScale = 100;   					//x 方向放大比例   
	private int yScale = 100;  					//y 方向放大比例   
	 
	private byte horiMirror = 0;   			// 是否水平镜像
	private byte verticalMirror = 0;    	//施放垂直镜像
	private int degree = 0;       				//旋转的角度
	
	private int transparency = 0;   			//透明度
	private int xOffset = 48;   				//与矩阵的x偏移	
	private int yOffset = 291;					//与矩阵的y偏移
			
	private int during;              				//一帧的时间
	
	private int frameNum;                     // 帧数
	
	private int hitFrame;                       //击中目标帧
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public byte getDischarge() {
		return discharge;
	}

	public void setDischarge(byte discharge) {
		this.discharge = discharge;
	}
	
	public String getSeqFrame() {
		return seqFrame;
	}

	public void setSeqFrame(String seqFrame) {
		this.seqFrame = seqFrame;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public void setXScale(int xScale)
	{
		this.xScale = xScale;
	}
	
	public int getXScale()
	{
		return xScale;
	}
	
	public void setYScale(int yScale)
	{
		this.yScale = yScale;
	}
	
	public int getYScale()
	{
		return yScale;
	}
	
	public void setHoriMirror(byte horiMirror)
	{
		this.horiMirror = horiMirror;
	}
	
	public byte getHoriMirror()
	{
		return horiMirror;
	}
	
	public void setVerticalMirror(byte verticalMirror)
	{
		this.verticalMirror = verticalMirror;
	}
	
	public byte getVerticalMirror()
	{
		return verticalMirror;
	}
	
	public void setRotateDegree(int degress)
	{
		this.degree = degress;
	}
	
	public int getRotateDegress()
	{
		return degree;
	}
	
	public void setTransparency(int transparency)
	{
		this.transparency = transparency;
	}
	
	public int getTransparency()
	{
		return transparency;
	}
	
	public void setXOffset(int xOffset)
	{
		this.xOffset = xOffset;
	}
	
	public int getXOffset()
	{
		return xOffset;
	}
	
	public void setYOffset(int yOffset)
	{
		this.yOffset = yOffset;
	}
	
	public int getYOffset()
	{
		return yOffset;
	}
	
	public void setDuring(int during)
	{
		this.during = during;
	}
	
	public int getDuring()
	{
		return during;
	}
	
	public int getFrameNum()
	{
		return frameNum;
	}
	
	public void setFrameNumber(int frameNum)
	{
		this.frameNum = frameNum;
	}
	
	public int getHitFrame()
	{
		return this.hitFrame;
	}
	
	public void setHitFrame(int hitFrame)
	{
		this.hitFrame = hitFrame;
	}
	
	
	public static BattleAnimEffects fromString(String csv) {
		BattleAnimEffects battleEffects = new BattleAnimEffects();
		StringBuilder buf = new StringBuilder(csv);
		battleEffects.setId(StringUtil.removeCsvInt(buf));
		battleEffects.setDischarge(StringUtil.removeCsvByte(buf));
		battleEffects.setSeqFrame(StringUtil.removeCsv(buf));
			
		battleEffects.setIcon(StringUtil.removeCsv(buf));
		battleEffects.setXScale(StringUtil.removeCsvInt(buf));
		battleEffects.setYScale(StringUtil.removeCsvInt(buf));
		battleEffects.setHoriMirror(StringUtil.removeCsvByte(buf));
		battleEffects.setVerticalMirror(StringUtil.removeCsvByte(buf));
		battleEffects.setRotateDegree(StringUtil.removeCsvInt(buf));
		battleEffects.setTransparency(StringUtil.removeCsvInt(buf));
		
		battleEffects.setXOffset(StringUtil.removeCsvInt(buf));
		battleEffects.setYOffset(StringUtil.removeCsvInt(buf));		
		battleEffects.setDuring(StringUtil.removeCsvInt(buf));	
		
		battleEffects.setHitFrame(StringUtil.removeCsvInt(buf));
		
		String removeMuli = removeMuliFrmae(battleEffects.seqFrame);		
		battleEffects.setFrameNumber(removeMuli.length());	
		return battleEffects;
	}
	
	private static  String removeMuliFrmae(String str) {
			char[] cy = str.toCharArray();
			String temp = "";
			for (int i = 0; i < cy.length; i++) 
			{
				if (temp.indexOf(cy[i]) == -1) {
					temp += cy[i];
				}
			}
			return temp;
		}	
}
