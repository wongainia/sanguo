package com.vikings.sanguo.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例线程 name为key 保证最多只会有一个该runnale的线程实例在运行
 * 
 * @author Brad.Chen
 * 
 */
public abstract class SingletonRunnable implements Runnable {

	private static Map<String, Thread> threadHolder = new HashMap<String, Thread>();

	abstract public String getThreadName();

	abstract public void doRun();

	@Override
	public void run() {
		String threadName = getThreadName();
		// 如果同名thread已经启动并运行，quit当前此重复线程
		if (threadHolder.containsKey(threadName)
				&& threadHolder.get(threadName).isAlive()) {
			return;
		}
		Thread cur = Thread.currentThread();
		cur.setName(threadName);
		threadHolder.put(threadName, cur);
		doRun();
		// 这里 极小概率会有问题。。。
		threadHolder.remove(threadName);
	}

}
