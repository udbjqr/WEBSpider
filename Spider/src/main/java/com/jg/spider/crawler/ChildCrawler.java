package com.jg.spider.crawler;

/**
 * 执行获取网站详细内容方式，并集成相应的处理代码.
 * <p>
 * 此类的对象不保证线程安全，调用类需要保证线程安全
 * <p>
 * 子类从此类继承并重写获取网站数据代码，此分离每一个不同的网站获取方式
 * <p>
 * create by 17/5/26.
 *
 * @author yimin
 */


public abstract class ChildCrawler {

	/**
	 * 用来获取明细数据的方法.
	 */
	public abstract boolean transition();
}
