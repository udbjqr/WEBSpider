package com.jg.spider.scheduledTask;

import com.jg.spider.Constant;
import com.jg.spider.crawler.ChildCrawler;
import com.jg.spider.crawler.ParentCrawler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.jg.common.sql.DBHelperFactory.DB_HELPER;

/**
 * 在web里面根据计划启动项目.
 * create by 2017/6/2.
 *
 * @author yimin
 */

public class Scheduled implements ServletContainerInitializer {
	private final static Logger log = LogManager.getLogger(Scheduled.class.getName());


	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		log.info("开始循环读数据库值！");

		Integer interval = Constant.CONFIG.getJSONObject("scheduled").getJSONObject("loadWeb").getInteger("interval");
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				DB_HELPER.selectWithRow("select id from web_address where flag = 1;", set -> loadWeb(set.getInt(1)));
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, 0, interval);
	}


	private void loadWeb(int id) {
		ParentCrawler parentCrawler = ParentCrawler.getInstance(id);
		if (parentCrawler != null) {
			boolean getted = parentCrawler.obtainDetail();

			log.info("成功标志：{}", getted);
			for (ChildCrawler child : parentCrawler.getChildren()) {
				try {
					Long time = Math.round(2000 + Math.random() * 3000);
					log.info("休息一下，时间：" + time);
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				child.transition();
			}
		}
	}
}
