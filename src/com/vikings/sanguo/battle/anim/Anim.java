package com.vikings.sanguo.battle.anim;

abstract public class  Anim {
	abstract protected void start() ;
	abstract protected void stop() ;
	abstract protected void animationEnd();
	abstract protected int getTotalTime();
	abstract protected boolean isRunning() ;
	private boolean isEnd = false;
	private boolean isNeedEnd = true;    //是否需要结束 才放下一组
	
	public void setEnd(boolean isEnd)
	{
		this.isEnd = isEnd;
	}
	
	public boolean getEnd()
	{
		return this.isEnd;
	}
	
	public void setNeedEnd(boolean isNeedEnd)
	{
		this.isNeedEnd = isNeedEnd;
	}
	
	public boolean getNeedEnd()
	{
		return this.isNeedEnd;
	}
}
