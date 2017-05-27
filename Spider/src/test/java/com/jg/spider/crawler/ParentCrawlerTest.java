package com.jg.spider.crawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * create by 17/5/26.
 *
 * @author yimin
 */
public class ParentCrawlerTest {
	private static final Logger log = LogManager.getLogger();

	@Test
	public void testGetMater() {
		ParentCrawler parentCrawler = ParentCrawler.getInstance(200);
		boolean getted = parentCrawler.obtainDetail();

		log.info("成功标志：{}", getted);
		parentCrawler.getChildren().forEach(it -> {
			try {
				Long time = Math.round(2000 + Math.random() * 3000);
				log.info("休息一下，时间：" + time);
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			it.transition();
		});
	}

	@Test
	public void testMath() {
		for (int i = 0; i < 10; i++) {
			log.info(Math.round(2000 + Math.random() * 3000));
		}
	}
}